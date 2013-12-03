package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import src.Analysis.StatTracker;
import src.BasicGraph.Node;

public class Runner {
	
	public static void main(String[] args) throws IOException {
		
		/**
		 * TODO: Create a function that modulates the different components and fills
		 * another mechanism that holds the tracker!
		 * This can all be abstracted and replicated :D
		 */
		
		
		// TODO: Make this work with the controlled sets. Luckily, we can use symmetry to get rid of some of 
		// the possibilities... But still, there will be a HUGE amount
		// ALSO, PLEASE DO NOT JUDGE ME FOR THIS SHITTY CODE :D
		
		int max_node_num = 500, step_node_num = 50;
		int max_k = 100, start_k = 5, step_k = 5;
		double max_density = 1.0, start_density = 0.1, step_density = 0.3;
		int max_degree = Integer.MAX_VALUE;
		

		BufferedWriter wr = new BufferedWriter(new FileWriter("../../../Desktop/Export.csv"));
		/**
		 * TestID = autonum
		 * K = k colorability of the graph
		 * Node_Num = number of nodes used for test
		 * Density = density value used for test
		 * Distribution = Type of distribution used to create the different sectors
		 * Is-Colored-Correctly = boolean (0, 1) if the test ran successfully
		 * K-Correctness = a measure for how much, positive or negative of K the test ran (colors used - k-value)
		 * Duration = the total time required to run the test
		 * Test_Type = the type of test run on the sample
		 */
		wr.append("TestID, K, Node_Num, Density, MaxDegree, Distribution, Is-Colored-Correctly, K-Correctness, Duration, Test_Type");
		wr.append("\n");
		int overall_counter = 0;
		// Doesn't handle the max degree at all
		for (int k = start_k; k < max_k; k += step_k) {
			for (int node_num = k; node_num < max_node_num; node_num += step_node_num) {
				for (double density = start_density; density <= max_density; density += step_density) {
					int[] set_sizes = new int[k];
					int mod = node_num % k; // Get the remainder of the nodes being placed
					int common_val = node_num / k; // Allow the rounding to take place, we will add it later
					// Create the sets by spreading the remainder out along the rounded down common value
					int counter = 0;
					for (; counter < mod; counter++) {
						set_sizes[counter] = common_val + 1;
					}
					for (; counter < k; counter++) {
						set_sizes[counter] = common_val;
					}
					
					// For now, just use a uniform distribution on the set_sizes			
			        BasicGraph graph = GraphCreator.createRandomConnectedGraphSimplified(node_num, k, density, max_degree, set_sizes);
			        for (Analysis.StatTestFunction func : Analysis.functions) {
			        	System.out.println("Running Test " + func.toString());
			        	StatTracker stats = func.runTest(graph);
			        	// Because more and more RAM will be used, it may be a good idea to reverse this
			        	wr.append(
			        			String.valueOf(overall_counter++) + "," +	// TestID
	        					String.valueOf(k) + "," +	// K
	        					String.valueOf(node_num) + "," +	// Node_Num
	        					String.valueOf(density) + "," +	// Density
	        					String.valueOf(max_degree) + "," +	// MaxDegree
	        					"Uniform" + "," +	// Distribution
	        					(stats.my_correctness ? "true" : "false") + "," +	// Is-Colored-Correctly
	        					String.valueOf(stats.my_color_count - k) + "," +	// K-Correctness
	        					String.valueOf(stats.my_duration) + "," +	// Duration
	        					func.toString() // Test_Type
	        			);
			        	wr.append("\n");
			        	
			        	graph.reset();
			        }
				}
			}
		}
		wr.close();
		
		/* String Output_Path = "../../../Desktop/EXPORTED_GRAPH/";
        BasicGraph graph = GraphCreator.createRandomConnectedGraphSimplified(numNodes, k, density, maxDegree, setSizes);
        
        // GraphCreator.createRandomGraph(25, 3, density, Integer.MAX_VALUE);
        
        System.out.println(graph);
        System.out.println("\n\n\n\n");
        
        for (Analysis.StatTestFunction func : Analysis.functions) {
        	System.out.println("Running Test " + func.toString());
        	StatTracker stats = func.runTest(graph);
        	System.out.println(graph);
        	System.out.println(stats.toString());
        	// if(!graph.export_graph(new File(Output_Path + func.name)))
            //	throw new Error("Export Failed");
        	// graph.reset();
        }
        */
                
        // It worked!
        System.exit(0);
	}
}
