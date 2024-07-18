package main;

import algorithm.Graph;
import visualization.MapPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args) {
		Graph graph = new Graph();
		graph.readVertexFile("/Users/niusha/Downloads/760_tatorter.csv");
		graph.readEdgeFile("/Users/niusha/Downloads/edges_760_tatorter.csv");

		JTextArea pathInfoArea = new JTextArea();
		pathInfoArea.setEditable(false);
		pathInfoArea.setMargin(new Insets(10, 10, 10, 10));
		pathInfoArea.setPreferredSize(new Dimension(800, 150));

		MapPanel mapPanel = new MapPanel(graph, pathInfoArea);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(new JScrollPane(mapPanel), BorderLayout.CENTER);

		JScrollPane pathInfoScrollPane = new JScrollPane(pathInfoArea);
		pathInfoScrollPane.setPreferredSize(new Dimension(800, 150));
		mainPanel.add(pathInfoScrollPane, BorderLayout.SOUTH);

		JFrame frame = new JFrame("Graph Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());
		frame.add(mainPanel, BorderLayout.CENTER);

		frame.setVisible(true);
	}
}
