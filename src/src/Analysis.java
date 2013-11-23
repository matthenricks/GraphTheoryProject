package src;

import java.util.*;

import src.BasicGraph.Node;

public class Analysis {
	
	/*
	 * Object that contains fields for needed metrics that will be collected by each function
	 */
	public static class StatTracker {
		protected long my_duration;
		protected int my_color_count;
		protected boolean my_correctness;
		
		public StatTracker(long duration, int colors_used, boolean is_correct) {
			my_duration = duration;
			my_color_count = colors_used;
			my_correctness = is_correct;
		}
		
		public String toString() {
			StringBuffer ret_str = new StringBuffer();
			ret_str.append("Total Number of Colors Used: " + my_color_count + "\n");
			ret_str.append("Total Time For Analysis (in nanoseconds): " + String.valueOf(my_duration) + "\n");
			ret_str.append("Total Time For Analysis (in seconds): " + String.valueOf(my_duration/1000000000.0) + "\n");
			ret_str.append("Successfully k-colored the graph: " + String.valueOf(my_correctness) + "\n");
			return ret_str.toString();
		}
	};
	
	/**
	 * Abstract class for the test functions
	 * @author MOREPOWER
	 *
	 */
	public static abstract class StatTestFunction {
		public String name;

		public StatTestFunction(String name) {
			this.name = name;
		}
		
		public abstract StatTracker runTest(BasicGraph g);
		
		public String toString() {
			return name;
		}
	}
	
	/**
	 * List of all the current analysis within this function
	 */
	public static StatTestFunction[] functions = {
		new analyzeStrictDecreasingFunction(),
		new analyzeDecreasingNeighbors(),
		new analyzeStrictIncreasing(),
		new analyzeIncreasingNeighbors(),
		new analyzeNumColored(),
		new analyzeHighColor()
	};
	
	
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
	public static class analyzeStrictDecreasingFunction extends StatTestFunction {

		public analyzeStrictDecreasingFunction() {
			super("analyzeStrictDecreasing");
		}
		
		@Override
		public StatTracker runTest(BasicGraph g) {
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
			
			StatTracker tracker = new StatTracker(duration, numColors, verify(g));
			if (tracker.my_correctness == false) {
				throw new Error(this.name + " colored incorrectly!\n");
			}
			return tracker;
		}
		
	};
	
	/*
	 * assuming g.allNodes is in decreasing sorted order by
	 * number of connections, colors vertices from high to low degree,
	 * colors its neighbors, then colors the vertex of next highest 
	 * degree
	 */
	public static class analyzeDecreasingNeighbors extends StatTestFunction {

		public analyzeDecreasingNeighbors() {
			super("analyzeDecreasingNeighbors");
		}
		
		@Override
		public StatTracker runTest(BasicGraph g) {
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
			
			StatTracker tracker = new StatTracker(duration, numColors, verify(g));
			if (tracker.my_correctness == false) {
				throw new Error(this.name + " colored incorrectly!\n");
			}
			return tracker;
		}
	};
	
	public static class analyzeStrictIncreasing extends StatTestFunction {

		public analyzeStrictIncreasing() {
			super("analyzeStrictIncreasing");
		}
		
		@Override
		public StatTracker runTest(BasicGraph g) {
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
			
			StatTracker tracker = new StatTracker(duration, numColors, verify(g));
			if (tracker.my_correctness == false) {
				throw new Error(this.name + " colored incorrectly!\n");
			}
			return tracker;
		}
	};
	
	public static class analyzeIncreasingNeighbors extends StatTestFunction {

		public analyzeIncreasingNeighbors() {
			super("analyzeIncreasingNeighbors");
		}
		
		@Override
		public StatTracker runTest(BasicGraph g) {
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

			StatTracker tracker = new StatTracker(duration, numColors, verify(g));
			if (tracker.my_correctness == false) {
				throw new Error(this.name + " colored incorrectly!\n");
			}
			return tracker;
		}
	};

	/*
	 * Given a graph, it colors it based on the number of 
	 * neighboring vertices that have previously been colored, from 
	 * highest number colored to lowest.  It starts off by coloring 
	 * the nodes of highest degree (number of vertices) and 
	 * resorts the list after each vertex is colored.
	 */
	public static class analyzeNumColored extends StatTestFunction {
		public analyzeNumColored() {
			super("analyzeNumColored");
		}
		
		@Override
		public StatTracker runTest(BasicGraph g) {
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

			StatTracker tracker = new StatTracker(duration, numColors, verify(g));
			if (tracker.my_correctness == false) {
				throw new Error(this.name + " colored incorrectly!\n");
			}
			return tracker;
		}
	}
	
	/*
	 * Given a graph, it colors it based on the number of 
	 * neighboring vertices that have previously been colored, from 
	 * highest number colored to lowest.  It starts off by coloring 
	 * the nodes of highest degree (number of vertices) and 
	 * resorts the list after each vertex is colored.
	 */
	public static class analyzeHighColor extends StatTestFunction {
		public analyzeHighColor() {
			super("analyzeHighColor");
		}
		
		@Override
		public StatTracker runTest(BasicGraph g) {
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
			
			StatTracker tracker = new StatTracker(duration, numColors, verify(g));
			if (tracker.my_correctness == false) {
				throw new Error(this.name + " colored incorrectly!\n");
			}
			return tracker;
		}
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
