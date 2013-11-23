package src;

import java.io.File;
import java.io.IOException;

import src.Analysis.StatTracker;

public class Runner {
	
	public static void main(String[] args) throws IOException {
		
		/**
		 * TODO: Create a function that modulates the different components and fills
		 * another mechanism that holds the tracker!
		 * This can all be abstracted and replicated :D
		 */
		
		
		double density = 0.5;
		String Output_Path = "../../../Desktop/EXPORTED_GRAPH/";
        BasicGraph graph = GraphCreator.createRandomConnectedGraph(25, 3, density);
        System.out.println(graph);
        
        for (Analysis.StatTestFunction func : Analysis.functions) {
        	System.out.println("Running Test " + func.toString());
        	StatTracker stats = func.runTest(graph);
        	System.out.println(stats.toString());
        	if(!graph.export_graph(new File(Output_Path + func.name)))
            	throw new Error("Export Failed");
        	graph.reset();
        }
                
        // It worked!
        System.exit(0);
	}
}
