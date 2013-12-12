package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;

public class VisualTotalClusters {

	private static ArrayList<VisualCluster> clusters = new ArrayList<VisualCluster>();
	
	public VisualTotalClusters(int i){
		
	}
	
	public VisualTotalClusters() {
		if(this.clusters==null){
			this.clusters = new ArrayList<VisualCluster>();
		}
	}

	protected void addCluster(VisualCluster cl){
		clusters.add(cl);
	}
	
	protected void clearList(){
		this.clusters.clear();
	}
	
	public ArrayList<VisualCluster> getClusters(){
		return this.clusters;
	}
	
	public String getClustersData(){
		String data = new String();
		data+="Total num of Clusters: " + this.clusters.size()+ "\n\n";
		double avgRad = 0, avgCrossings = 0, avgLength = 0;
		for(VisualCluster cl : this.clusters){
			data += cl.getClusterId()+ "\t" +cl.getClusterRad() + "\t\t" + cl.getInterClusterCrossings() + "\t" + cl.getLineLength() + "\n"; 
			avgRad+=cl.getClusterRad();
			avgCrossings+=cl.getInterClusterCrossings();
			avgLength+=cl.getLineLength();
		}
		
		avgRad = (double) Math.round(avgRad/this.clusters.size() * 100) / 100;
		avgCrossings = (double) Math.round(avgCrossings/this.clusters.size() * 100) / 100;
		avgLength = (double) Math.round(avgLength/this.clusters.size() * 100) / 100;
		data += "\n\n\t"+avgRad+"\t\t"+avgCrossings+"\t"+avgLength+"\n";
		return data;
	}
	/*
	 * get total area covered by clusters
	 * 
	 */
	public double getTotalArea(){
		double area = 0;
		for(VisualCluster cl : this.clusters){
			area += cl.getArea();
		}
		area = (double) Math.round(area * 100) / 100;
		return area;
	}
}
