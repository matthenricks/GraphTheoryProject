package src;

import src.BasicGraph.Node;

public class Runner {

	static int counter = 0;
	
	public static void main(String[] args) {
		
		BasicGraph graph = RandomGraphCreator.createGraph(6);
		System.out.println(graph);
		
		BasicGraph graph2 = RandomGraphCreatorFromTriangles.createGraph(6);
		System.out.println(graph2);
		
		Analysis.analyzeStrictDecreasing(graph2);
		Analysis.analyzeDecreasingNeighbors(graph2);
		
	}

}
