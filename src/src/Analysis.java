package src;

import java.util.*;
import src.BasicGraph.Node;

public class Analysis {
	
	/*
	 * General notes:
	 * TODO: from what point do we want to start and 
	 * end the timer on each analysis?  At this point I think the 
	 * start time should be at the beginning of the method and the 
	 * end time should be at the end of the actual coloring of the 
	 * graph, so as to avoid timing printing and verifying the graph.
	 * 
	 * TODO: should sorting the vertices be in this class so it can 
	 * be counted on the timer?
	 * 
	 * TODO: what sorts of statistics should we get for each method 
	 * of analysis?
	 */
	
	/* 
	 * assuming that g.allNodes in decreasing sorted order by connections
	 * analyzes vertices from high to low degree, then moving onto the 
	 * next highest degree, ignoring coloring neighbors 
	 */
	
	static void analyzeStrictDecreasing(BasicGraph g) {
		long startTime = System.nanoTime();
		int numColors = 0;
		LinkedList<Node> unvisited = new LinkedList<Node>();
		for (Node n : g.allNodes) {
			unvisited.add(n);
		}
		
		//colors will start from 1; 0 means not yet colored
		while (!unvisited.isEmpty()) {
			Node current = unvisited.pop();
			color(current, g.allNodes.size());
			if (current.color > numColors) {					
				numColors++;
			}
		}
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		System.out.println("Stricly Coloring from High to Low");
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		System.out.println("Total Number of Colors Used: " + numColors);
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		
		System.out.println(verify(g));
		System.out.println();
	}
	
	/*
	 * assuming g.allNodes is in decreasing sorted order by
	 * number of connections, colors vertices from high to low degree,
	 * colors its neighbors, then colors the vertex of next highest 
	 * degree
	 */
	
	static void analyzeDecreasingNeighbors(BasicGraph g) {
		long startTime = System.nanoTime();
		int numColors = 0;
		LinkedList<Node> unvisited = new LinkedList<Node>();
		for (Node n : g.allNodes) {
			unvisited.add(n);
		}
		
		//colors will start from 1; 0 means not yet colored
		while (!unvisited.isEmpty()) {
			Node current = unvisited.pop();
			if (current.color == 0) {
				color(current, g.allNodes.size());
				if (current.color > numColors) {
					numColors++;
				}
			}
			
			//This will color neighbors of current node
			for (Node n : current.connections) {
				if (n.color == 0) {
					color(n, g.allNodes.size());
					if (n.color > numColors) {
						numColors++;
					}
				}
			}
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		System.out.println("Coloring from High to Low, Coloring "
				+ "Neighbors along the way");
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		System.out.println("Total Number of Colors Used: " + numColors);
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		System.out.println(verify(g));
		System.out.println();
	}
	
	static void analyzeStrictIncreasing(BasicGraph g) {
		long startTime = System.nanoTime();
		int numColors = 0;
		LinkedList<Node> unvisited = new LinkedList<Node>();
		for (Node n : g.allNodes) {
			unvisited.push(n);
		}
		
		//colors will start from 1; 0 means not yet colored
		while (!unvisited.isEmpty()) {
			Node current = unvisited.pop();
			color(current, g.allNodes.size());
			if (current.color > numColors) {					
				numColors++;
			}
		}
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		System.out.println("Stricly Coloring from Low to High");
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		System.out.println("Total Number of Colors Used: " + numColors);
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		System.out.println(verify(g));
		System.out.println();
	}
	
	static void analyzeIncreasingNeighbors(BasicGraph g) {
		long startTime = System.nanoTime();
		int numColors = 0;
		LinkedList<Node> unvisited = new LinkedList<Node>();
		for (Node n : g.allNodes) {
			unvisited.push(n);
		}
		
		//colors will start from 1; 0 means not yet colored
		while (!unvisited.isEmpty()) {
			Node current = unvisited.pop();
			if (current.color == 0) {
				color(current, g.allNodes.size());
				if (current.color > numColors) {
					numColors++;
				}
			}
			
			//This will color neighbors of current node
			for (Node n : current.connections) {
				if (n.color == 0) {
					color(n, g.allNodes.size());
					if (n.color > numColors) {
						numColors++;
					}
				}
			}
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;

		System.out.println("Coloring from Low to High, Coloring "
				+ "Neighbors along the way");
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		System.out.println("Total Number of Colors Used: " + numColors);
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		System.out.println(verify(g));
		System.out.println();
	}
	
	/*
	 * Given a graph, it colors it based on the number of 
	 * neighboring vertices that have previously been colored, from 
	 * highest number colored to lowest.  It starts off by coloring 
	 * the nodes of highest degree (number of vertices) and 
	 * resorts the list after each vertex is colored.
	 */
	static void analyzeNumColored(BasicGraph g) {
		long startTime = System.nanoTime();
		int numColors = 0;
		LinkedList<Node> unvisited = new LinkedList<Node>();
		for (Node n : g.allNodes) {
			unvisited.add(n);
		}
		
		while (!unvisited.isEmpty()) {
			Node current = unvisited.pop();
			color(current, g.allNodes.size());
			if (current.color > numColors) {					
				numColors++;
			}
			//resorting nodes based on number of neighbors colored
			Collections.sort(unvisited, BasicGraph.compareNumColored);
		}
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		System.out.println("Coloring from High to Low, reordering nodes based on the number of neighbors colored:");
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		System.out.println("Total Number of Colors Used: " + numColors);
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		System.out.println(verify(g));
		System.out.println();
	}
	
	/*
	 * Given a graph, it colors it based on the number of 
	 * neighboring vertices that have previously been colored, from 
	 * highest number colored to lowest.  It starts off by coloring 
	 * the nodes of highest degree (number of vertices) and 
	 * resorts the list after each vertex is colored.
	 */
	static void analyzeHighColor(BasicGraph g) {
		long startTime = System.nanoTime();
		int numColors = 0;
		LinkedList<Node> unvisited = new LinkedList<Node>();
		for (Node n : g.allNodes) {
			unvisited.add(n);
		}
		
		while (!unvisited.isEmpty()) {
			Node current = unvisited.pop();
			color(current, g.allNodes.size());
			if (current.color > numColors) {					
				numColors++;
			}
			//resorting nodes based on highest color among neighbors
			Collections.sort(unvisited, BasicGraph.compareHighColor);
		}
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		System.out.println("Coloring from High to Low, reordering nodes based on the highest color in neighbor set:");
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		System.out.println("Total Number of Colors Used: " + numColors);
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		System.out.println(verify(g));
		System.out.println();
	}
	
	/* 
	 * given an uncolored Node, colors it with the smallest color
	 * not found in its neighbors
	 */
	
	static void color(Node current, int size) {
		//seeking the smallest unfilled color among already colored neighbors
		boolean[] lowest = new boolean[size];
		for (Node n : current.connections) {
			if (n.color != 0) {
				lowest[n.color - 1] = true;
			}
		}
		
		for (int i = 0; current.color == 0; i++) {
			if (!lowest[i]) {
				current.color = i + 1;
			}
		}
	}
	
	/*
	 * verifies if the colored graph has the property that 
	 * none of its neighbors have the same color as the current node
	 */
	static boolean verify(BasicGraph g) {
		for(Node n: g.allNodes) {
			for(Node x: n.connections) {
				if (n.color == x.color)
					return false;
			}
		}
		return true;
	}
	
}
