package clusters.Parser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class CommonNeighborsDistance extends NodeDistanceFunction {

	/*
	 * Assumptions made:
	 * - we use undirected graph
	 * - we add each node's self in its extended neighborhood
	 * 
	 * @see Parser.NodeDistanceFunction#computeAllDistances(int[][], int)
	 */
	@Override
		public double[][] computeAllDistances(int[][] adjMatrix, int numObjects) {
		
			distanceMatrix = new double[numObjects][numObjects];
			
			//compute the simple Neighborhood of each node
			HashMap<Integer,LinkedHashSet<Integer>> neighbors = new HashMap<Integer,LinkedHashSet<Integer>>();
			for (int i=0; i< numObjects; i++){
				LinkedHashSet<Integer> N = new LinkedHashSet<Integer>();
				
				for (int j = 0; j < numObjects; j++){
					if (adjMatrix[i][j] == 1)
						N.add(j);
				}//end j
				neighbors.put(i,N);
			}//end i
			
			//compute the Jaccard of the extended neighborhoods of each pair
			for(int i=0; i< numObjects -1 ; i++){
				LinkedHashSet<Integer> iNeighbors = neighbors.get(i);
				LinkedHashSet<Integer> copyIN = new LinkedHashSet<Integer>();
				copyIN.addAll(iNeighbors);
				//copyIN.add(i);
				// ATTN: be careful, not to touch i/j NeighBors, but only the others, as iNeighBors is used again and again
				for (int j = i+1; j <numObjects; j++){
					LinkedHashSet<Integer> jNeighbors = neighbors.get(j);
					LinkedHashSet<Integer> copyJN = new LinkedHashSet<Integer>();
					copyJN.addAll(jNeighbors);
					//copyJN.add(j);
					
					

					int countCommon = 0;					//count Common
					for (Integer k: copyJN){
						if (copyIN.contains(k)){
							countCommon++;
							//System.out.println("i: " + i + " j: " + j + " common: " + k);
						}
					}
					/*
					 * TODO Think again if we must
					 * if (adjMAtrix[i][j] == 1) add to the common the to participants in the edge =>
					 *  numerator is incremented by two, one for each of the participants
					 */
					if (adjMatrix[i][j] == 1)
						countCommon +=2;

					copyJN.addAll(copyIN);			//count Union -- addAll does not insert an existing element twice
					int countUnion =copyJN.size();
					distanceMatrix[i][j] = 1 - ( (double) countCommon / (double) countUnion );
					distanceMatrix[j][i] = distanceMatrix[i][j];
				}//end j
			}//end i
			
				
		
		
		return distanceMatrix;
	}

}
