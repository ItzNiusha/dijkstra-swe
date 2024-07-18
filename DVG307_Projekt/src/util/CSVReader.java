package util;

import model.Vertex;
import algorithm.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

	// läs vertex filen
	public static void readVertexFile(String filename, Graph graph) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				String name = parts[0];
				double longitude = Double.parseDouble(parts[1]);
				double latitude = Double.parseDouble(parts[2]);
				int population = Integer.parseInt(parts[3]);
				Vertex vertex = new Vertex(name, longitude, latitude, population);
				graph.addVertex(vertex);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// läs edge filen
	public static void readEdgeFile(String filename, Graph graph) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				String from = parts[0];
				String to = parts[1];
				double weight = Double.parseDouble(parts[2]);
				graph.addEdge(from, to, weight);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
