package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;

import clusters.EngineConstructs.Cluster;

@SuppressWarnings({ "serial", "rawtypes" })
class SortedArrayList extends ArrayList{

	public ArrayList<clusters.EngineConstructs.Cluster> insertSorted(ArrayList<clusters.EngineConstructs.Cluster> unsortedCl, double[][] dist) {
		
		ArrayList<clusters.EngineConstructs.Cluster> sorted = new ArrayList<clusters.EngineConstructs.Cluster>();
		
		for(Cluster usCl : unsortedCl){
			sorted.add(usCl);     //bazw to prwto
			for(int j = 1; j < dist.length-1; j++){
				for(int i = 0; i<j; i++){
					if(dist[i][j] < 1 && dist[i][j] !=0){
						if(sorted.isEmpty()){
							sorted.add(unsortedCl.get(i));
						}
						else{
							if(!sorted.contains(unsortedCl.get(i))){
								sorted.add(unsortedCl.get(i));
							}
						}
					}
				}
				
			}
		}
		return sorted;
    }
}
