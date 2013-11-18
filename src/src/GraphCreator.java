package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import src.BasicGraph.Node;

/** 
 * Note that methods are sorted in order of most recent algorithm at the top
 * of this class, and older algorithms farther down.
 */
public class GraphCreator {
	
	private static Random randomGenerator = new Random();
	
	/** 
	 * Creates a k-colorable graph with the specified number of nodes. This is
	 * done by creating all nodes and then adding random edges between valid 
	 * nodes. When a new node is created, an edge is added between it and a 
	 * random node that was previously created and whose id is different mod k.
	 * This will ensure that the graph is both connected and k-colorable,
	 * but will be more random than one created from a line 
	 * (compare to createRandomConnectedGraphFromLine).
	 * The density is a double in the range [0, 1], and it determines how
	 * many extra edges are added after the initial edges which ensure
	 * connectedness. Think of it as the probability that any given valid
	 * edge will be added. 
	 * e.g. if there are 11 valid edges which can be added and density = 0.5,
	 * then (int)0.5*11 = 5 edges will be added. 
	 * 
	 * @param numNodes - the number of nodes in the graph
	 * @param k - specifies that the graph is k-colorable
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 */
	public static BasicGraph createRandomConnectedGraph(int numNodes, int k, 
			double density) {

		BasicGraph graph = new BasicGraph();

		// create nodes
		graph.addNode(0); // add first node
		Node currNode;
		int randomPrev;
		for (int id = 1; id < numNodes; id++) {
			currNode = graph.new Node(id);
			graph.addNode(currNode);
			do {
				randomPrev = randomGenerator.nextInt(id);
			} while (randomPrev % k == id % k);
			
			// connect new current node to a random (and valid) previous one
			graph.addEdge(currNode, graph.allNodes.get(randomPrev));
		}

		// add additional edges (with specified density)
		addRandomValidEdges(graph, numNodes, k, density);
		
		// sort nodes
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}
	
	/**
	 * Adds random, valid edges to the graph. A valid edge is one which 
	 * connects two nodes with different values of id % k.
	 * The density is a double in the range [0, 1], and it determines how
	 * many extra edges are added. Think of it as the probability that any given 
	 * valid edge will be added. 
	 * e.g. if there are 11 valid edges which can be added and density = 0.5,
	 * then (int)0.5*11 = 5 edges will be added. 
	 * 
	 * @param graph - the graph to which edges are added
	 * @param numNodes - the number of nodes in the graph
	 * @param k - specifies that the graph is k-colorable
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 */
	private static void addRandomValidEdges(BasicGraph graph, int numNodes, 
			int k, double density) {
		/* max number of edges in a k-colorable graph with n nodes:
		 * if (n%k == 0): (n^2)(k-1)/2k
		 * else: [(ceil(n/k))^2][(n%k)(n%k - 1)/2] + 
		 * [(floor(n/k))^2][(k - n%k)(k - n%k - 1)/2] +
		 * [(ceil(n/k))(floor(n/k))(n%k)(k - n%k)]
		 * 
		 * note that in the second case, floor(n/k) == n/k since Java truncates,
		 * and ceil(n/k) == n/k + 1 since Java truncates and we know n%k != 0.
		 * 
		 * TODO check this formula (but I (Ilse) already checked in many cases)
		 * 
		 * TODO should we make sure not to add duplicate edges?
		 * (because they only get added once anyway, so end up with fewer total edges)
		 */
		int maxNumEdges;
		int mod = numNodes % k;
		if (mod == 0) {
			maxNumEdges = (numNodes * numNodes * (k - 1)) / (2 * k);
		} else {
			int floor = numNodes / k;
			int ceil = floor + 1;
			int kMinusMod = k - mod;
			maxNumEdges = (ceil * ceil * ((mod * (mod - 1)) / 2)) +
					(floor * floor * ((kMinusMod * (kMinusMod - 1)) / 2)) +
					(ceil * floor * mod * kMinusMod);
		}

		// determine actual number of edges to use, based on density
		int numEdges = (int)(maxNumEdges * density);

		int randomStart, randomDest;
		// add that amounts of edges (or possibly fewer if repeats occur)
		for (int i = 0; i < numEdges; i++) {
			// pick two random nodes of different types (by id)
			randomStart = randomGenerator.nextInt(numNodes);
			do {
				randomDest = randomGenerator.nextInt(numNodes);
			} while ((randomStart % k) == (randomDest % k));
			
			// add the edge
			graph.addEdge(graph.allNodes.get(randomStart), 
					graph.allNodes.get(randomDest));
		}
		
		System.out.println("numNodes: " + numNodes + 
				", maxNumEdges: " + maxNumEdges + 
				", density: " + density +
				", numEdges: " + numEdges);
	}
	
	/** 
	 * Creates a k-colorable graph with the specified number of nodes. This is
	 * done by creating all nodes, originally all connected in a line,
	 * and then adding random edges between valid nodes. 
	 * The density is a double in the range [0, 1], and it determines how
	 * many extra edges are added after the initial edges which ensure
	 * connectedness. 
	 * e.g. if there are 11 valid edges which can be added and density = 0.5,
	 * then (int)0.5*11 = 5 edges will be added. 
	 * 
	 * @param numNodes - the number of nodes in the graph
	 * @param k - specifies that the graph is k-colorable
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 */
	public static BasicGraph createRandomConnectedGraphFromLine(int numNodes, 
			int k, double density) {

		BasicGraph graph = new BasicGraph();

		// create nodes, connecting in a line as they are created
		graph.addNode(0); // add first node
		Node currNode;
		for (int id = 1; id < numNodes; id++) {
			currNode = graph.new Node(id);
			graph.addNode(currNode);
			// connect new node (currNode) to the previous node
			graph.addEdge(currNode, graph.allNodes.get(id - 1));
		}

		addRandomValidEdges(graph, numNodes, k, density);
		
		// sort nodes
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}

	/** 
	 * Creates a k-colorable graph with the specified number of nodes. This is
	 * done by creating all nodes and then adding random edges between valid 
	 * nodes. The graph will be k-colorable at the end, but it will not 
	 * necessarily be connected.
	 * The density is a double in the range [0, 1], and it determines how
	 * many edges are added.
	 * e.g. if there are 11 valid edges which can be added and density = 0.5,
	 * then (int)0.5*11 = 5 edges will be added. 
	 * 
	 * @param numNodes - the number of nodes in the graph
	 * @param k - specifies that the graph is k-colorable
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 */
	public static BasicGraph createRandomGraph(int numNodes, int k, 
			double density) {

		BasicGraph graph = new BasicGraph();

		// create nodes
		for (int id = 0; id < numNodes; id++) {
			graph.addNode(id);
		}

		addRandomValidEdges(graph, numNodes, k, density);

		// sort nodes
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}

	/** 
	 * Creates a graph with n nodes. This is done by creating nodes in groups 
	 * of 3 (triangles with node types 0, 1, 2). All nodes of "type 0" are
	 * stored in a list, and the same for types 1 and 2. Then a random number of
	 * edges are added between nodes of types 0 and 1, 0 and 2, and 1 and 2.
	 * 
	 * @param n - the number of nodes in the graph
	 */
	//TODO expand to take k and density? (e.g. if k is 4, then do from squares)
	public static BasicGraph createRandomGraphFromTriangles(int n) {

		BasicGraph graph = new BasicGraph();

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
