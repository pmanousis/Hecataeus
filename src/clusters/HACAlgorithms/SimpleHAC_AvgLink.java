package clusters.HACAlgorithms;
import java.util.ArrayList;
import java.util.Iterator;

import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import clusters.GraphFacades.ClusterableObject;


public class SimpleHAC_AvgLink extends HACAlgorithm {

/* Implements HAC with (a) avg link, (b) no threshold on similarity -- i.e., all clusters
 * @param inputObjects The set of input objects to be clustered
 * @param solutions The set of solutions, each as a ClusterSet
 * Assumptions:
 * 1. we start with a set of input objects 
 * 2. We start with the first solution, where each object is a cluster, already inside the solutions collection
 */
	public ClusterSet execute(ArrayList<ClusterableObject> inputObjects, ArrayList<ClusterSet> solutions, double[][] inputObjectsDistances, int numC){
		int numObjects = inputObjects.size();
		int numClusterSets = solutions.size(); 
		int numClusters = numObjects;
		Cluster c1 = null, c2 = null; 
		double currDist = 2.0, minDist = 2.0; 
		int minPos1 = -1, minPos2 = -1;
		double copyDist[][];
		ClusterSet lastSolution = solutions.get(numClusterSets-1);
		int endC;
		System.out.println("ALGO \nObjs:"+numObjects+"\tCLS:"+numClusterSets+"\n");
		/*
		 * 0. compute pairwise distances
		 * 1. clone last solution
		 * 2. find min distance pair
		 * 3. merge the pair and evict constituents from the solution
		 * 4. reduce numClust by the number of merges 
		 */
		
		if(numC == 0){
			endC = 1;
		}
		else{
			endC = numC;
		}
		
		do{
			
//			numClusters = lastSolution.getClusters().size();
			copyDist = lastSolution.getClusterDistances();
			if (copyDist == null){
				System.out.println("Cluster Distances not available");
				System.exit(-1);
			}
			System.out.println("LAST SOL: " + lastSolution.getCSDescriptionString());
			
			//compute pairwise distances & find minimum pair
			for (int i = 0; i < numClusters - 1; i++){
				c1 = lastSolution.getClusters().get(i);
				for (int j = i+1; j < numClusters; j++){
					c2 = lastSolution.getClusters().get(j);
 
					currDist = c1.computeDistance(c2, inputObjectsDistances);

					copyDist[i][j] = currDist;
					copyDist[j][i] = currDist;
					//highlight the single pair with the minimum distance
					if (currDist < minDist){
						minPos1 = i;
						minPos2 = j;
						minDist = currDist;
					}
					else{                                 //?????????????????????????????
						if(minPos1 < 0){
							minPos1 = 0;
						}
						if(minPos2 < 0){
							minPos2 = 0;
						}
					}
				}
				copyDist[i][i] = 0;
			}		
			copyDist[numClusters-1][numClusters-1] = 0;
			
//			for (int i = 0; i < numClusters; i++){
//				for (int j = 0; j < numClusters; j++){
//					System.out.printf("%.2f\t", lastSolution.getClusterDistances()[i][j]);
//				}
//				System.out.println("");
//			}
			
			//now, merge the winner clusters	-- reuse vrbl c1 and c2
	
			c1 = lastSolution.getClusters().get(minPos1);
			c2 = lastSolution.getClusters().get(minPos2);
			System.out.println("MERGING clusters: " + c1.getId() + " and " + c2.getId());
			
			//find the maxId, to make a new Id for the new Cluster
			int newId = -1; int maxId = -1, currId = -1; Cluster currCl;
			Iterator<Cluster> itClust = lastSolution.getClusters().iterator();
			while(itClust.hasNext()){
				currCl = itClust.next();
				currId = currCl.getId();
				if (currId > maxId)
					maxId = currId;
			}
			newId = maxId + 1;

			//copy the c1 to the new cluster
			Cluster newCluster = new Cluster(newId);
			newCluster.setExtension(c1.getExtension()); 
			//merge with the other
			newCluster.mergeClusters(c2);	
				
			ClusterSet newSolution = new ClusterSet(lastSolution.getId()+1);
			Iterator<Cluster> it = lastSolution.getClusters().iterator();
			while (it.hasNext()){
				Cluster c = it.next();
				newSolution.getClusters().add(c);
			}
			
			//Kill the two old ones and add the new one from the new solution
			newSolution.getClusters().remove(c1);
			newSolution.getClusters().remove(c2);
			newSolution.getClusters().add(newCluster);
			System.out.println("NEW SOL: " + newSolution.getCSDescriptionString());
			solutions.add(newSolution);
			numClusters = newSolution.getClusters().size();
			lastSolution = newSolution;
			lastSolution.createDistances(numClusters);
			minDist = 2.0; minPos1 = -1; minPos2 = -1;
			System.out.println(numClusters);
		}while(numClusters != endC);
		return lastSolution;
	}//end execute
}
