package src;

public class Runner {
	
	public static void main(String[] args) {
		
		double density = 0.5;
		BasicGraph graph = GraphCreator.createRandomConnectedGraph(25, 3, density);
		System.out.println(graph);
		
		Analysis.analyzeStrictDecreasing(graph);
		Analysis.analyzeDecreasingNeighbors(graph);
		
	}

}
