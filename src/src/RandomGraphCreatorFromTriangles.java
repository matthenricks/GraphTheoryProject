package src;

import src.BasicGraph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomGraphCreatorFromTriangles {

	/** 
	 * Creates a graph with n nodes. This is done by creating nodes in groups 
	 * of 3 (triangles with node types 0, 1, 2). All nodes of "type 0" are
	 * stored in a list, and the same for types 1 and 2. Then a random number of
	 * edges are added between nodes of types 0 and 1, 0 and 2, and 1 and 2.
	 * We can expand this later to take a specified number of edges, to test
	 * different densities.
	 */
	public static BasicGraph createGraph(int n) {
		
		BasicGraph graph = new BasicGraph();
		Random randomGenerator = new Random();
		
		/* For now our algorithm will always create graphs in multiples of 3 nodes.
		 * If n is not divisible by 3, we use the next smallest number that is.
		 * (I can come back later and decide how to better deal with other
		 * numbers) */
		if (n % 3 != 0) {
			n -= (n % 3);
		}
		
		// lists that will store nodes by type
		ArrayList<Node> nodes0 = new ArrayList<Node>(n/3);
		ArrayList<Node> nodes1 = new ArrayList<Node>(n/3);
		ArrayList<Node> nodes2 = new ArrayList<Node>(n/3);
		
		// create nodes in groups of 3
		for (int id = 0; id < n; id += 3) {
			// create and store in correct list
			Node n0 = graph.new Node(id);
			Node n1 = graph.new Node(id + 1);
			Node n2 = graph.new Node(id + 2);
			nodes0.add(n0);
			nodes1.add(n1);
			nodes2.add(n2);
			graph.addNode(n0);
			graph.addNode(n1);
			graph.addNode(n2);
			
			// connect as a triangle
			graph.addEdge(n0, n1);
			graph.addEdge(n0, n2);
			graph.addEdge(n1, n2);
		}
		
		/* There are n/3 nodes of each type-- 0, 1, 2.
		 * We can add edges between types (0,1), (0,2), and (1,2). */
		// First determine how many edges there will be between each valid type pair
		int maxNumEdges = (n * n) / 9; // n/3 per list -> n^2/9 possible edges between each list
		int numEdges01 = randomGenerator.nextInt(maxNumEdges);
		int numEdges02 = randomGenerator.nextInt(maxNumEdges);
		int numEdges12 = randomGenerator.nextInt(maxNumEdges);
		
		// add these random amounts of edges (or possibly fewer if repeats occur)
		for (int i = 0; i < numEdges01; i++) {
			// pick random node of type 0 and another of type 1
			int randomNode0 = randomGenerator.nextInt(n/3);
			int randomNode1 = randomGenerator.nextInt(n/3);
			graph.addEdge(nodes0.get(randomNode0), nodes1.get(randomNode1));
		}
		for (int i = 0; i < numEdges02; i++) {
			// pick random node of type 0 and another of type 1
			int randomNode0 = randomGenerator.nextInt(n/3);
			int randomNode2 = randomGenerator.nextInt(n/3);
			graph.addEdge(nodes0.get(randomNode0), nodes2.get(randomNode2));
		}
		for (int i = 0; i < numEdges12; i++) {
			// pick random node of type 0 and another of type 1
			int randomNode1 = randomGenerator.nextInt(n/3);
			int randomNode2 = randomGenerator.nextInt(n/3);
			graph.addEdge(nodes1.get(randomNode1), nodes2.get(randomNode2));
		}
		
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}
}
