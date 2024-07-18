package algorithm;

import model.Vertex;
import model.Edge;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Graph implements IGraph {

	private Map<String, Vertex> vertices;

	public Graph() {
		this.vertices = new HashMap<>();
	}

	public void addVertex(Vertex vertex) {
		vertices.put(vertex.getName(), vertex);
	}

	public Vertex getVertex(String name) {
		return vertices.get(name);
	}

	public List<Vertex> getVertices() {
		return new ArrayList<>(vertices.values());
	}

	public void addEdge(String from, String to, double weight) {
		Vertex fromVertex = getVertex(from);
		Vertex toVertex = getVertex(to);
		if (fromVertex != null && toVertex != null) {
			fromVertex.addEdge(new Edge(weight, toVertex));
			toVertex.addEdge(new Edge(weight, fromVertex)); // Se till dubbelriktade kanter
		}
	}

	public void dijkstra(String startName) {
		Vertex startVertex = getVertex(startName);
		if (startVertex == null) {
			throw new IllegalArgumentException("Start vertex hittades ej");
		}

		PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getDistance));
		startVertex.setDistance(0);
		priorityQueue.add(startVertex);

		while (!priorityQueue.isEmpty()) {
			Vertex currentVertex = priorityQueue.poll();
			for (Edge edge : currentVertex.getEdges()) {
				Vertex adjacentVertex = edge.getVertex();
				double newDist = currentVertex.getDistance() + edge.getWeight();
				if (newDist < adjacentVertex.getDistance()) {
					adjacentVertex.setDistance(newDist);
					adjacentVertex.setPredecessor(currentVertex);
					priorityQueue.add(adjacentVertex);
				}
			}
		}
	}

	public PathResult findShortestPath(String startName, String endName) {
		dijkstra(startName);
		List<String> path = new ArrayList<>();
		double totalDistance = 0;
		Vertex endVertex = getVertex(endName);

		if (endVertex == null || endVertex.getPredecessor() == null) {
			return new PathResult(path, totalDistance); // Path hittades ej
		}

		for (Vertex vertex = endVertex; vertex != null; vertex = vertex.getPredecessor()) {
			path.add(0, vertex.getName());
		}

		Vertex currentVertex = endVertex;
		while (currentVertex.getPredecessor() != null) {
			for (Edge edge : currentVertex.getPredecessor().getEdges()) {
				if (edge.getVertex().equals(currentVertex)) {
					totalDistance += haversineDistance(currentVertex.getPredecessor().getLatitude(),
							currentVertex.getPredecessor().getLongitude(), currentVertex.getLatitude(),
							currentVertex.getLongitude());
					break;
				}
			}
			currentVertex = currentVertex.getPredecessor();
		}

		return new PathResult(path, totalDistance);
	}

	private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371;
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c; // Konvertera till km
	}

	public void readVertexFile(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				if (parts.length >= 4) {
					String name = parts[0].trim();
					double longitude = Double.parseDouble(parts[2].trim().replace(',', '.'));
					double latitude = Double.parseDouble(parts[3].trim().replace(',', '.'));
					int population = Integer.parseInt(parts[1].trim());
					Vertex vertex = new Vertex(name, longitude, latitude, population);
					addVertex(vertex);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readEdgeFile(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				if (parts.length >= 3) {
					String from = parts[0].trim();
					String to = parts[1].trim();
					double weight = Double.parseDouble(parts[2].trim().replace(',', '.'));
					addEdge(from, to, weight);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		for (Vertex vertex : vertices.values()) {
			vertex.setDistance(Double.MAX_VALUE);
			vertex.setPredecessor(null);
		}
	}
}
