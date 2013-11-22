package src;

import java.io.File;
import java.io.IOException;

public class Runner {
	
	public static void main(String[] args) throws IOException {
		
		double density = 0.5;
		String Output_Path = "../../../Desktop/EXPORTED_GRAPH/";
        BasicGraph graph = GraphCreator.createRandomConnectedGraph(25, 3, density);
        System.out.println(graph);
        // Original Graph Export
        // graph.export_graph(new File(Output_Path + "OG"));
        // Strict Decreasing Export
        Analysis.analyzeStrictDecreasing(graph);
        if(!graph.export_graph(new File(Output_Path + "SD")))
        	throw new Error("Export Failed");
        graph.reset();
        
        Analysis.analyzeDecreasingNeighbors(graph);
        if(!graph.export_graph(new File(Output_Path + "DN")))
        	throw new Error("Export Failed");
        graph.reset();
        
        Analysis.analyzeIncreasingNeighbors(graph);
        if(!graph.export_graph(new File(Output_Path + "IN")))
        	throw new Error("Export Failed");
        graph.reset();

        Analysis.analyzeStrictIncreasing(graph);
        if(!graph.export_graph(new File(Output_Path + "SI")))
        	throw new Error("Export Failed");
        graph.reset();
        
        Analysis.analyzeNumColored(graph);
        if(!graph.export_graph(new File(Output_Path + "NC")))
        	throw new Error("Export Failed");
        graph.reset();
        
        Analysis.analyzeHighColor(graph);
        if(!graph.export_graph(new File(Output_Path + "HC")))
        	throw new Error("Export Failed");
        
        // It worked!
        System.exit(0);
	}
}
