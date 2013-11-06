package src;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class RandomGraphCreator {

	/** 
	 * Creates a graph with numNodes nodes. This is done by creating all nodes 
	 * and then adding random edges between valid nodes. The graph will be
	 * 3-colorable at the end, but it will not necessarily be connected.
	 * We can expand this later to take a specified number of edges, to test
	 * different densities.
	 */
	public static BasicGraph createGraph(int numNodes) {
		
		BasicGraph graph = new BasicGraph();
		Random randomGenerator = new Random();
		
		/* For now our algorithm will always create graphs in multiples of 3 nodes.
		 * If numNodes is not divisible by 3, we use the next smallest number that is.
		 * (I can come back later and decide how to better deal with other
		 * numbers) */
		if (numNodes % 3 != 0) {
			numNodes -= (numNodes % 3);
		}
		
		// create nodes
		for (int id = 0; id < numNodes; id++) {
			// create and store in correct list
			graph.addNode(id);
		}
		
		/* There are n/3 nodes of each type-- 0, 1, 2.
		 * We can add edges between types (0,1), (0,2), and (1,2).
		 * Calculate maximum number of edges that can exist:
		 * n/3 of each type (0,1,2) -> n^2/9 possible edges between each valid pair
		 * ((0,1),(0,2),(1,2)) -> 3*(n^2/9) = n^2/3 total possible edges
		 */
		int maxNumEdges = (numNodes * numNodes) / 3;
		
		// determine actual number of edges
		int numEdges = 800;
		
		int randomStart, randomDest;
		// add these random amounts of edges (or possibly fewer if repeats occur)
		for (int i = 0; i < numEdges; i++) {
			// pick two random nodes of different types (by id)
			randomStart = randomGenerator.nextInt(numNodes);
			do {
				randomDest = randomGenerator.nextInt(numNodes);
			} while ((randomStart % 3) == (randomDest % 3));
			
			System.out.println("start" + randomStart + " dest" + randomDest);
			// add the edge
			// TODO constant time access if allNodes is instead an ArrayList...
			// make this change to BasicGraph unless LinkedList helps elsewhere
			graph.addEdge(graph.allNodes.get(randomStart),
					graph.allNodes.get(randomDest));
		}
		
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}
}
