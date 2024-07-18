package model;

public class Edge {
	private double weight;
	private Vertex vertex;

	public Edge(double weight, Vertex vertex) {
		this.weight = weight;
		this.vertex = vertex;
	}

	public double getWeight() {
		return weight;
	}

	public Vertex getVertex() {
		return vertex;
	}
}
