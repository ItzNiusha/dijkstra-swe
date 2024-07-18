package algorithm;

import model.Vertex;
import model.Edge;
import java.util.List;

public interface IGraph {
	void addVertex(Vertex vertex);

	Vertex getVertex(String name);

	List<Vertex> getVertices();

	void addEdge(String from, String to, double weight);

	void clear();

	PathResult findShortestPath(String startName, String endName);

	class PathResult {
		private final List<String> path;
		private final double distance;

		public PathResult(List<String> path, double distance) {
			this.path = path;
			this.distance = distance;
		}

		public List<String> getPath() {
			return path;
		}

		public double getDistance() {
			return distance;
		}
	}
}
