package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicGraph {

	public List<Node> allNodes;
	
	public BasicGraph() {
		allNodes = new LinkedList<Node>();
	}
	
	// setting all colors back to 0 after a coloring has been done
	public void reset() {
		for (Node n : allNodes) {
			n.color = 0;
		}
	}
	
	public boolean export_graph(File folderToCreate) throws IOException {
		if(folderToCreate.exists())
			return false;
		System.out.println(folderToCreate.getAbsolutePath());
		if(!folderToCreate.mkdir())
			return false;
		String base_dir = folderToCreate.toString();
		File f1 = new File(base_dir + "/node_chart.csv");
		File f2 = new File(base_dir + "/edge_chart.csv");
		
		BufferedWriter wr = new BufferedWriter(new FileWriter(f1));
		BufferedWriter wr2 = new BufferedWriter(new FileWriter(f2));
		wr.append("Id,Label,Color\n");
		wr2.append("Source,Target,Type,Id,Label,Weight\n");
		
		int counter = 0;
		
		for (Node n : allNodes) {
			wr.append(n.id + "," + "Node " + n.id + "," + String.valueOf(n.color) + "\n");
			for (Node n2 : n.connections) 
				wr2.append(n.id + ","
						+ n2.id + "," 
						+ "Directed" + "," 
						+ counter++ + "," 
						+ "Node " + n.id + " to Node " + n2.id + "," 
						+ "1\n");
		}
		wr.close();
		wr2.close();
		
		System.out.println("Graph Exported");
		return true;
	}
	
	// This will be lowest-first sorted order
	public static Comparator<Node> compareNodes = new Comparator<Node>(){
		public int compare(Node o1, Node o2) {
			return o2.connections.size() - o1.connections.size();
		}
	};
	
	//This will be order from highest number of neighbors colored to lowest
	public static Comparator<Node> compareNumColored = new Comparator<Node>() {
		public int compare(Node o1, Node o2) {
			int num1 = 0;
			int num2 = 0;
			for (Node n: o1.connections) {
				if (n.color != 0)
					num1++;
			}
			for (Node n: o2.connections) {
				if (n.color != 0)
					num2++;
			}
			return num2 - num1;
		}
	};
		
	//This will be order from highest neighbor color to lowest
	public static Comparator<Node> compareHighColor = new Comparator<Node>() {
		public int compare(Node o1, Node o2) {
			int num1 = 0;
			int num2 = 0;
			for (Node n: o1.connections) {
				if (n.color > num1)
					num1 = n.color;
			}
			for (Node n: o2.connections) {
				if (n.color > num2)
					num2 = n.color;
			}
			return num2 - num1;
		}
	};
	
	
	
	public void addNode(int id) {
		Node n = new Node(id);
		allNodes.add(n);
	}
	
	public void addNode(Node n) {
		allNodes.add(n);
	}
	
	/**
	 * Returns true if an edge connecting start and dest already exists in
	 * the graph, false otherwise
	 * 
	 * @param start
	 * @param dest
	 * @return
	 */
	public boolean existsEdge(Node start, Node dest) {
		if (start.connections.contains(dest)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if an edge did not previously exist between start and dest,
	 * false otherwise.
	 * 
	 * @param start
	 * @param dest
	 */
	public void addEdge(Node start, Node dest) {
		if (!start.connections.contains(dest)) {
			start.degree++;
			dest.degree++;
			start.connections.add(dest);
			dest.connections.add(start);
		}
	}
	
	/**
	 * Returns the max degree of any node in the graph
	 * 
	 * @return
	 */
	public int maxDegree() {
		int max = 0;
		int degree;
		
		for (Node n : allNodes) {
			degree = n.connections.size();
			if (degree > max) {
				max = degree;
			}
		}
		return max;
	}
	
	/**
	 * Returns the m degree of any node in the graph
	 * 
	 * @return
	 */
	public int minDegree() {
		int min = Integer.MAX_VALUE;
		int degree;
		
		for (Node n : allNodes) {
			degree = n.connections.size();
			if (degree < min) {
				min = degree;
			}
		}
		return min;
	}
	
	/**
	 * Returns the number of edges in the graph
	 * 
	 * @return
	 */
	public int numEdges() {
		int num = 0;
		for (Node n : allNodes) {
			num += n.connections.size();
		}
		return num / 2;
	}
	
	//TODO run DFS, see if #nodes visited == #nodes
	public boolean isConnected() {
		return false;
	}
	
	public String toString() {
		String ret = "Graph: ";
		for(Node n : allNodes) {
			ret += "\n\t" + n.toString();
		}
		return ret;
	}
	
	public class Node {
		public int id;
		public int color;
		public List<Node> connections;
		public int degree;
		public int setNumber; // used for simplified creation
		
		public Node(int mID) {
			id = mID;
			connections = new LinkedList<Node>();
			degree = 0;
			setNumber = -1; // -1 signals not assigned a set
		}
		
		// alternate constructor used in createRandomConnectedGraphSimplified
		public Node(int id, int setNumber) {
			this.id = id;
			connections = new LinkedList<Node>();
			degree = 0;
			this.setNumber = setNumber;
		}
		
		public String toString() {
			String ret = "[id:" + id + ", color:" + color + ", to:";
			Iterator<Node> conns = connections.iterator();
			if (conns.hasNext())
				ret += conns.next().id;
			while (conns.hasNext())
				ret += ", " + conns.next().id;
			ret += "]";
			return ret;
		}
	}
}
