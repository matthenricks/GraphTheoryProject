package src;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicGraph {

	public List<Node> allNodes;
	
	public BasicGraph() {
		allNodes = new LinkedList<Node>();
	}
	
	// This will be highest-first sorted order
	public static Comparator<Node> compareNodes = new Comparator<Node>(){
		public int compare(Node o1, Node o2) {
			return o2.connections.size() - o1.connections.size();
		}
	};
	
	//This will be order from highest number of neighbors colored to lowest
	public static Comparator<Node> compareNumColored = 
			new Comparator<Node>() {
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
		public static Comparator<Node> compareHighColor = 
				new Comparator<Node>() {
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
	
	public void addEdge(Node start, Node dest) {
		if (!start.connections.contains(dest)) {
			start.connections.add(dest);
			dest.connections.add(start);
		}
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
		
		public Node(int mID) {
			id = mID;
			connections = new LinkedList<Node>();
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
