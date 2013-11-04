package src;

import java.util.*;
import src.BasicGraph.Node;

public class Analysis {
	
	/* 
	 * assuming that g.allNodes in decreasing sorted order by connections
	 * analyzes vertices from high to low degree, then moving onto the 
	 * next highest degree, ignoring coloring neighbors 
	 */
	
	static void analyzeStrictDecreasing(BasicGraph g) {
		
		int numColors = 0;
		
		
		long startTime = System.nanoTime();
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
		
		System.out.println("Stricly Coloring from High to Low");
		
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		
		System.out.println("Total Number of Colors Used: " + numColors);
		
		long endTime = System.nanoTime();
		
		long duration = endTime - startTime;
		
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		
		System.out.println();
		reset(g);
	}
	
	/*
	 * assuming g.allNodes is in decreasing sorted order by
	 * number of connections, colors vertices from high to low degree,
	 * colors its neighbors, then colors the vertex of next highest 
	 * degree
	 */
	
	static void analyzeDecreasingNeighbors(BasicGraph g) {
		int numColors = 0;
		
		
		long startTime = System.nanoTime();

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
		
		System.out.println("Coloring from High to Low, Coloring "
				+ "Neighbors along the way");
		
		System.out.println("On a graph with " + g.allNodes.size() 
				+ " vertices:");
		
		System.out.println("Total Number of Colors Used: " + numColors);
		
		long endTime = System.nanoTime();
		
		long duration = endTime - startTime;
		
		System.out.println("Total Time For Analysis (in nanoseconds): " 
				+ duration);
		
		System.out.println("Total Time For Analysis (in seconds): " 
				+ duration/1000000000.0);
		
		System.out.println();
		
		reset(g);
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
	
	// setting all colors back to 0 after a coloring has been done
	static void reset(BasicGraph g) {
		for (Node n : g.allNodes) {
			n.color = 0;
		}
	}
	
}
