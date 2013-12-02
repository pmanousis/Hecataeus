package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;

public class VisualTotalClusters {

	private static ArrayList<VisualCluster> clusters = new ArrayList<VisualCluster>();
	
	public VisualTotalClusters() {
		if(this.clusters==null){
			this.clusters = new ArrayList<VisualCluster>();
		}
	}

	protected void addCluster(VisualCluster cl){
		clusters.add(cl);
	}
	
	public String getClustersData(){
		String data = new String();
		data+="Total num of Clusters: " + this.clusters.size()+ "\n\n";
		for(VisualCluster cl : this.clusters){
			data += cl.getClusterId()+ "\t" +cl.getClusterRad() + "\t" + cl.getInterClusterCrossings() + "\t" + cl.getLineLength() + "\n"; 
		}
		return data;
	}
}
