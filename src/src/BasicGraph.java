package src;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicGraph {

	public List<Node> allNodes;
	// This will be lowest-first sorted order
	public static Comparator<Node> compareNodes = new Comparator<Node>(){
		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return o2.connections.size() - o1.connections.size();
		}
	};
	
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
	
	public BasicGraph() {
		allNodes = new LinkedList<Node>();
	}
}
