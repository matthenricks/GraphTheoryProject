package src;

import src.BasicGraph.Node;

public class Runner {

	static int counter = 0;
	
	public static void main(String[] args) {
		BasicGraph graph = new BasicGraph();
		
		/*
		 * sample test code, replace with graph generator when 
		 * ready
		 */
		
		Node[] n = new Node[9];
		for (int i = 0; i < n.length; i++) {
			graph.addNode(i);
		}
		for (int j = 0; j < n.length; j++) {
			graph.addEdge(graph.allNodes.get(j), 
					graph.allNodes.get(Math.abs((j + 1) % 9)));
			
			graph.addEdge(graph.allNodes.get(j), 
					graph.allNodes.get(Math.abs((j + 2) % 9)));
			
		}
		
		Analysis.analyzeStrictDecreasing(graph);
		Analysis.analyzeDecreasingNeighbors(graph);
	}

}
