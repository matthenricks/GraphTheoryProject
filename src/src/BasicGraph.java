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
			wr.append(n.id + "," + "Node " + n.id + "," + n.color + "\n");
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
