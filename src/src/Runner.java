package src;

import java.io.File;
import java.io.IOException;

public class Runner {
	
	public static void main(String[] args) throws IOException {
		
		double density = 0.5;
        BasicGraph graph = GraphCreator.createRandomConnectedGraph(25, 3, density);
        System.out.println(graph);
        
        Analysis.analyzeStrictDecreasing(graph);
        Analysis.analyzeDecreasingNeighbors(graph);
        Analysis.analyzeIncreasingNeighbors(graph);
        Analysis.analyzeStrictIncreasing(graph);
        Analysis.analyzeNumColored(graph);
        Analysis.analyzeHighColor(graph);
        
        System.exit(graph.export_graph(new File("./EXPORTED_GRAPH/")) ? 0 : -1);
	}
}
