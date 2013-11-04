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
	
	public List<Node> allNodes;
	public BasicGraph() {
		allNodes = new LinkedList<Node>();
	}
}
