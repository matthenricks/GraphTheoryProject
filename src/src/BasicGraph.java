package src;

import java.util.LinkedList;
import java.util.List;

public class BasicGraph {

	public class Node {
		public int id;
		public int color;
		public List<Node> connections;
		
		public Node(int mID) {
			id = mID;
			connections = new LinkedList<Node>();
		}
	}
	
	public void addNode(int id) {
	Node n = new Node(id);
	allNodes.add(n);
	}
	
	public void addEdge(Node start, Node dest) {
		if (!start.connections.contains(dest)) {
			start.connections.add(dest);
			dest.connections.add(start);
		}
	}
	
	public List<Node> allNodes;
	public BasicGraph() {
		allNodes = new LinkedList<Node>();
	}
}
