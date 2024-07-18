package visualization;

import algorithm.IGraph;
import model.Vertex;
import model.Edge;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

public class MapPanel extends JPanel {
	private IGraph graph;
	private List<String> shortestPath;
	private double shortestPathDistance;
	private Vertex startVertex;
	private Vertex endVertex;
	private JTextArea pathInfoArea;

	public MapPanel(IGraph graph, JTextArea pathInfoArea) {
		this.graph = graph;
		this.shortestPath = null;
		this.startVertex = null;
		this.endVertex = null;
		this.pathInfoArea = pathInfoArea;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Vertex clickedVertex = findVertexAt(e.getX(), e.getY());
				if (clickedVertex != null) {
					if (startVertex == null) {
						startVertex = clickedVertex;
						System.out.println("Start vertex set: " + startVertex.getName());
					} else if (endVertex == null) {
						endVertex = clickedVertex;
						System.out.println("End vertex set: " + endVertex.getName());
						findAndDisplayShortestPath();
					} else {
						clearSelections();
						startVertex = clickedVertex;
						System.out.println("Start vertex set: " + startVertex.getName());
					}
					repaint();
				}
			}
		});
	}

	private Vertex findVertexAt(int x, int y) {
		for (Vertex vertex : graph.getVertices()) {
			double vx = scaleLongitude(vertex.getLongitude());
			double vy = scaleLatitude(vertex.getLatitude());
			if (Math.abs(vx - x) < 5 && Math.abs(vy - y) < 5) {
				return vertex;
			}
		}
		return null;
	}

	private void findAndDisplayShortestPath() {
		if (startVertex != null && endVertex != null) {
			graph.clear(); // Clear previous path calculations
			IGraph.PathResult result = graph.findShortestPath(startVertex.getName(), endVertex.getName());
			shortestPath = result.getPath();
			shortestPathDistance = result.getDistance();
			displayPathInfo();
		}
	}

	private void displayPathInfo() {
		if (shortestPath != null && !shortestPath.isEmpty()) {
			StringBuilder info = new StringBuilder();
			info.append("Kortaste vägen från ").append(startVertex.getName()).append(" till ")
					.append(endVertex.getName()).append(":\n");
			for (String vertexName : shortestPath) {
				info.append(vertexName).append(" -> ");
			}
			info.setLength(info.length() - 4); // Remove the last arrow
			info.append("\nTotal distans: ").append(String.format("%.2f", shortestPathDistance)).append(" km");
			pathInfoArea.setText(info.toString());
		} else {
			pathInfoArea.setText("Ingen sökväg hittade mellan" + startVertex.getName() + " och " + endVertex.getName());
		}
	}

	public void clearSelections() {
		System.out.println("Clearing selections");
		startVertex = null;
		endVertex = null;
		shortestPath = null;
		shortestPathDistance = 0;
		pathInfoArea.setText("");
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawEdges(g2d);
		drawVertices(g2d);
		if (shortestPath != null) {
			drawShortestPath(g2d);
		}
	}

	private void drawVertices(Graphics2D g2d) {
		for (Vertex vertex : graph.getVertices()) {
			drawVertex(g2d, vertex);
		}
	}

	private void drawVertex(Graphics2D g2d, Vertex vertex) {
		double x = scaleLongitude(vertex.getLongitude());
		double y = scaleLatitude(vertex.getLatitude());
		Ellipse2D.Double circle = new Ellipse2D.Double(x - 3, y - 3, 6, 6);
		g2d.fill(circle);
		if (vertex == startVertex) {
			g2d.setColor(Color.GREEN);
			g2d.draw(circle);
			g2d.setColor(Color.BLACK);
		} else if (vertex == endVertex) {
			g2d.setColor(Color.RED);
			g2d.draw(circle);
			g2d.setColor(Color.BLACK);
		}
	}

	private void drawEdges(Graphics2D g2d) {
		g2d.setColor(Color.LIGHT_GRAY);
		for (Vertex vertex : graph.getVertices()) {
			for (Edge edge : vertex.getEdges()) {
				drawEdge(g2d, vertex, edge.getVertex());
			}
		}
	}

	private void drawEdge(Graphics2D g2d, Vertex fromVertex, Vertex toVertex) {
		double x1 = scaleLongitude(fromVertex.getLongitude());
		double y1 = scaleLatitude(fromVertex.getLatitude());
		double x2 = scaleLongitude(toVertex.getLongitude());
		double y2 = scaleLatitude(toVertex.getLatitude());
		Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
		g2d.draw(line);
	}

	private void drawShortestPath(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		for (int i = 0; i < shortestPath.size() - 1; i++) {
			Vertex fromVertex = graph.getVertex(shortestPath.get(i));
			Vertex toVertex = graph.getVertex(shortestPath.get(i + 1));
			drawEdge(g2d, fromVertex, toVertex);
		}
	}

	private double scaleLongitude(double longitude) {
		double minLongitude = graph.getVertices().stream().mapToDouble(Vertex::getLongitude).min().orElse(0);
		double maxLongitude = graph.getVertices().stream().mapToDouble(Vertex::getLongitude).max().orElse(1);
		return (longitude - minLongitude) * (getWidth() - 40) / (maxLongitude - minLongitude) + 20;
	}

	private double scaleLatitude(double latitude) {
		double minLatitude = graph.getVertices().stream().mapToDouble(Vertex::getLatitude).min().orElse(0);
		double maxLatitude = graph.getVertices().stream().mapToDouble(Vertex::getLatitude).max().orElse(1);
		return getHeight() - ((latitude - minLatitude) * (getHeight() - 40) / (maxLatitude - minLatitude) + 20);
	}
}
