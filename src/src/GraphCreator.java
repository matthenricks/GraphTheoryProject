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
	 * This generalizes createRandomConnectedGraph so that sets of different
	 * sizes can be used.
	 * For example, if numNodes is 9, k is 3, and setSizes is [5,3,1], then 
	 * instead of the graph being created to have a 3-colorable solution with
	 * 3 nodes of each color, it will be created so that it can be colored with
	 * 5 nodes of color 0, 3 of color 1, and 1 of color 2.
	 * 
	 * Creates a k-colorable graph with the specified number of nodes. This is
	 * done by creating all nodes and then adding random edges between valid 
	 * nodes. When a new node is created, an edge is added between it and a 
	 * node that was previously created and which is in a different
	 * color set.
	 * This will ensure that the graph is both connected and k-colorable.
	 * The density is a double in the range [0, 1], and it determines how
	 * many extra edges are added after the initial edges which ensure
	 * connectedness. Think of it as the probability that any given valid
	 * edge will be added. 
	 * e.g. if there are 11 valid edges which can be added and density = 0.5,
	 * then (int)0.5*11 = 5 edges will be added. 
	 * The maxDegree sets a limit so that every node will have a degree less
	 * than or equal to this number.
	 * 
	 * @param numNodes - the number of nodes in the graph. 
	 * @param k - specifies that the graph will be k-colorable.
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 * @param maxDegree - sets a limit so that each node in the graph will 
	 * have a degree less than or equal to this number. 
	 * @param setSizes -- e.g. if setSizes is [5,2], then a coloring of the graph
	 * exists with 5 nodes of color 0 and 2 nodes of color 1. The sum of all
	 * elements in this array should be equal to numNodes 
	 * (but this is not enforced).
	 * 
	 * @throws IllegalArgumentException if numNodes < 0, if k < 2, 
	 * if density is outside the range [0,1], if maxDegree < 1, 
	 * if setSizes.length < k, or if setSizes[i] < 1 for any i.
	 * (The last two enforce that the graph will not be constructed with less
	 * than k intended color sets)
	 */
	public static BasicGraph createRandomConnectedGraphSimplified(int numNodes,
			int k, double density, int maxDegree, int[] setSizes) {

		// check that parameters are valid
		if (numNodes < 0 || k < 2 || density < 0 || density > 1 || 
				maxDegree < 1 || setSizes.length != k) {
			throw new IllegalArgumentException();
		}

		// find max set size, to help with traversing col-by-col later
		int maxSetSize = 0;
		int size;
		// each set should have at least one node, or else (k-1) or (k-2) colorable, etc
		for (int i = 0; i < setSizes.length; i++) {
			size = setSizes[i];
			if (size < 1) {
				throw new IllegalArgumentException();
			} else if (size > maxSetSize) {
				maxSetSize = size;
			}
		}

		BasicGraph graph = new BasicGraph();

		// create structure to store Nodes in k different sets of specified sizes
		Node[][] nodes = new Node[k][];
		for (int i = 0; i < k; i++) {
			nodes[i] = new Node[setSizes[i]];
		}

		// create Node column-by-column, connecting to a Node with smaller id from a different set
		Node newNode;
		int id = 0;
		for (int col = 0; col < maxSetSize; col++) {
			for (int row = 0; row < nodes.length; row++) {
				if (col < nodes[row].length) {
					newNode = graph.new Node(id, row); // row is setNumber
					nodes[row][col] = newNode;
					graph.addNode(newNode);

					if (id > 0) {
						// connect to a previously made node from a different set
						// which has degree < maxDegree
						for (int prev = id - 1; prev >= 0; prev--) {
							if (graph.allNodes.get(prev).setNumber != row &&
									graph.allNodes.get(prev).degree < maxDegree) {
								// connect new current node to a valid previous one
								graph.addEdge(newNode, graph.allNodes.get(prev));
								break;
							}
						}
					}
					id++;
				}
			}
		}

		// look at all edges, add ones randomly with given density probability
		addRandomValidEdgesSimplified(graph, density, maxDegree, nodes);

		// sort nodes
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}

	/** 
	 * This generalizes createRandomGraph so that sets of different
	 * sizes can be used.
	 * For example, if numNodes is 9, k is 3, and setSizes is [5,3,1], then 
	 * instead of the graph being created to have a 3-colorable solution with
	 * 3 nodes of each color, it will be created so that it can be colored with
	 * 5 nodes of color 0, 3 of color 1, and 1 of color 2.
	 * 
	 * Creates a k-colorable graph with the specified number of nodes. This is
	 * done by creating all nodes and then adding random edges between valid 
	 * nodes. The graph will not necessarily be connected.
	 * The density is a double in the range [0, 1], and it determines how
	 * many extra edges are added after the initial edges which ensure
	 * connectedness. Think of it as the probability that any given valid
	 * edge will be added. 
	 * e.g. if there are 11 valid edges which can be added and density = 0.5,
	 * then (int)0.5*11 = 5 edges will be added. 
	 * The maxDegree sets a limit so that every node will have a degree less
	 * than or equal to this number.
	 * 
	 * @param numNodes - the number of nodes in the graph. 
	 * @param k - specifies that the graph will be k-colorable.
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 * @param maxDegree - sets a limit so that each node in the graph will 
	 * have a degree less than or equal to this number. 
	 * @param setSizes -- e.g. if setSizes is [5,2], then a coloring of the graph
	 * exists with 5 nodes of color 0 and 2 nodes of color 1. The sum of all
	 * elements in this array should be equal to numNodes 
	 * (but this is not enforced).
	 * 
	 * @throws IllegalArgumentException if numNodes < 0, if k < 2, 
	 * if density is outside the range [0,1], if maxDegree < 1, 
	 * if setSizes.length < k, or if setSizes[i] < 1 for any i.
	 * (The last two enforce that the graph will not be constructed with less
	 * than k intended color sets)
	 */
	public static BasicGraph createRandomGraphSimplified(int numNodes,
			int k, double density, int maxDegree, int[] setSizes) {

		// check that parameters are valid
		if (numNodes < 0 || k < 2 || density < 0 || density > 1 || 
				maxDegree < 1 || setSizes.length != k) {
			throw new IllegalArgumentException();
		}

		// find max set size, to help with traversing col-by-col later
		int maxSetSize = 0;
		int size;
		// each set should have at least one node, or else (k-1) or (k-2) colorable, etc
		for (int i = 0; i < setSizes.length; i++) {
			size = setSizes[i];
			if (size < 1) {
				throw new IllegalArgumentException();
			} else if (size > maxSetSize) {
				maxSetSize = size;
			}
		}

		BasicGraph graph = new BasicGraph();

		// create structure to store Nodes in k different sets of specified sizes
		Node[][] nodes = new Node[k][];
		for (int i = 0; i < k; i++) {
			nodes[i] = new Node[setSizes[i]];
		}

		// create Node column-by-column
		int id = 0;
		Node newNode;
		for (int col = 0; col < maxSetSize; col++) {
			for (int row = 0; row < nodes.length; row++) {
				if (col < nodes[row].length) {
					newNode = graph.new Node(id++);
					nodes[row][col] = newNode;
					graph.addNode(newNode);
				}
			}
		}

		// look at all edges, add ones randomly with given density probability
		addRandomValidEdgesSimplified(graph, density, maxDegree, nodes);

		// sort nodes
		Collections.sort(graph.allNodes, BasicGraph.compareNodes);
		return graph;
	}

	// look at all edges, add ones randomly with given density probability
	private static void addRandomValidEdgesSimplified(BasicGraph graph, 
			double density, int maxDegree, Node[][] nodes) {
		// TODO can this be simplified??-- hideous layering of for loops here... sorry
		Node start, dest;
		for (int startRow = 0; startRow < nodes.length - 1; startRow++) {
			for (int startCol = 0; startCol < nodes[startRow].length; startCol++) {
				start = nodes[startRow][startCol];
				for (int destRow = startRow + 1; destRow < nodes.length && 
						start.degree < maxDegree; destRow++) {
					for (int destCol = 0; destCol < nodes[destRow].length && 
							start.degree < maxDegree && 
							nodes[destRow][destCol].degree < maxDegree; destCol++) {
						dest = nodes[destRow][destCol];
						if (!graph.existsEdge(start, dest) && 
								randomGenerator.nextDouble() < density) {
							graph.addEdge(start, dest);
						}
					}
				}
			}
		}
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
	 * The maxDegree sets a limit so that every node will have a degree less
	 * than or equal to this number.
	 * 
	 * @param numNodes - the number of nodes in the graph
	 * @param k - specifies that the graph is k-colorable
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 * @param maxDegree - sets a limit so that each node in the graph will 
	 * have a degree less than or equal to this number. maxDegree needs to be at 
	 * least 1 or an exception is thrown. 
	 * 
	 * @throws IllegalArgumentException if numNodes < 0, if k < 2, 
	 * if density is outside the range [0,1], or if maxDegree < 1
	 */
	public static BasicGraph createRandomConnectedGraphFromLine(int numNodes, 
			int k, double density, int maxDegree) {

		if (numNodes < 0 || k < 2 || density < 0 || density > 1 || 
				maxDegree < 1) {
			throw new IllegalArgumentException();
		}

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

		addRandomValidEdges(graph, numNodes, k, density, maxDegree);

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
	 * The maxDegree sets a limit so that every node will have a degree less
	 * than or equal to this number.
	 * 
	 * @param numNodes - the number of nodes in the graph
	 * @param k - specifies that the graph is k-colorable
	 * @param density - determines how many edges to add; a double in the range 
	 * [0,1]
	 * @param maxDegree - sets a limit so that each node in the graph will 
	 * have a degree less than or equal to this number. maxDegree needs to be at 
	 * least 1 or an exception is thrown. 
	 * 
	 * @throws IllegalArgumentException if numNodes < 0, if k < 2, 
	 * if density is outside the range [0,1], or if maxDegree < 1
	 */
	public static BasicGraph createRandomGraph(int numNodes, int k, 
			double density, int maxDegree) {

		if (numNodes < 0 || k < 2 || density < 0 || density > 1 || 
				maxDegree < 1) {
			throw new IllegalArgumentException();
		}

		BasicGraph graph = new BasicGraph();

		// create nodes
		for (int id = 0; id < numNodes; id++) {
			graph.addNode(id);
		}

		addRandomValidEdges(graph, numNodes, k, density, maxDegree);

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
	 * @param maxDegree - all nodes will have degree <= maxDegree
	 */
	private static void addRandomValidEdges(BasicGraph graph, int numNodes, 
			int k, double density, int maxDegree) {
		/* max number of edges in a k-colorable graph with n nodes:
		 * if (n%k == 0): (n^2)(k-1)/2k
		 * else: [(ceil(n/k))^2][(n%k)(n%k - 1)/2] + 
		 * [(floor(n/k))^2][(k - n%k)(k - n%k - 1)/2] +
		 * [(ceil(n/k))(floor(n/k))(n%k)(k - n%k)]
		 * 
		 * note that in the second case, floor(n/k) == n/k since Java truncates,
		 * and ceil(n/k) == n/k + 1 since Java truncates and we know n%k != 0.
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

		
		// Added a check to determine if the nodes can actually be added. If not, false will be returned
		// TODO: Remove this fix and make it a better one
		/// First remove current edges (we know ....)
		
		
		
		
		int randomStart, randomDest;
		// add that amounts of edges (or possibly fewer if repeats occur)
		for (int i = 0; i < numEdges; i++) {
			// pick two random nodes of different types (by id)
			
//			if(graph.minDegree() >= maxDegree) {
//				return; // prevent infinite loop //TODO still may have one?? if maxDegree too low
//			}
			do {
				randomStart = randomGenerator.nextInt(numNodes);
			} while (graph.allNodes.get(randomStart).degree >= maxDegree);

			/*
			 * TODO note a problem: I tried to only consider edges not already
			 * in the graph, but this too often leads to very long looping because
			 * of many iterations before finding a non-duplicate edge.
			 * So the simplified version is better in this sense, because
			 * it looks at each possible edge only once, and the randomness is
			 * more on the probability of it being added than it is on the 
			 * selection of possible edges being looked at. That is, the 
			 * simplified version will never run an infinite loop.
			 */

			do {
				randomDest = randomGenerator.nextInt(numNodes);
			} while ((randomStart % k) == (randomDest % k) ||
					//		graph.existsEdge(graph.allNodes.get(randomStart), graph.allNodes.get(randomDest)) ||
					graph.allNodes.get(randomDest).degree >= maxDegree);
			// don't add edges between nodes if their ids are the same mod k
			// or if at least one of the nodes already has the max degree.

			// add the edge
			graph.addEdge(graph.allNodes.get(randomStart), 
					graph.allNodes.get(randomDest));
		}
//
//		System.out.println("numNodes: " + numNodes + 
//				", maxNumEdges: " + maxNumEdges + 
//				", density: " + density +
//				", numEdges: " + numEdges);
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
