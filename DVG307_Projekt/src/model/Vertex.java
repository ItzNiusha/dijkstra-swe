package model;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
	private String name;
	private double longitude;
	private double latitude;
	private int population;
	private List<Edge> edges;
	private Vertex predecessor;
	private double distance;

	public Vertex(String name, double longitude, double latitude, int population) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.population = population;
		this.edges = new ArrayList<>();
		this.distance = Double.MAX_VALUE;
		this.predecessor = null;
	}

	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	public List<Edge> getEdges() {
		return edges;
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public Vertex getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Vertex predecessor) {
		this.predecessor = predecessor;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
