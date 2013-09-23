package clusters.EngineConstructs;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

public class ClusterSet {

	public ClusterSet(int clustId){
		id = clustId;
		clusters = new ArrayList<Cluster>(); 

	}
	public int getId() {return id;}
	public ArrayList<Cluster> getClusters(){return clusters;}
	
	
	public ArrayList<String> cl = new ArrayList<String>();
	
	
	public double[][] createDistances(int numClusters){
		int i,j;
		clusterDistances = new double[numClusters][numClusters];
		//intentionally put meaningless values to avoid the default zeros!
		for(i=0; i< numClusters; i++)
			for(j=0;j < numClusters; j++)
				clusterDistances[i][j] = -1;
		return clusterDistances;
	}
	
	public double[][] getClusterDistances(){ return clusterDistances;}
	
	public String getCSDescriptionString(){
		String message = "ClusterSet: " + id + "\n";
		message += "-------------------" + "\n";
		Iterator<Cluster> it = clusters.iterator();
		while (it.hasNext()){
			message += it.next().printCluster() + "\n";
		}
		message += "\n\n";
		return message;
	}
	
	public ArrayList<String> getCl(String name){
		Iterator<Cluster> it = clusters.iterator();
		while (it.hasNext()){
			if(it.toString() == name){
				cl.add(it.toString());
			}
		}
		return cl;
	}
	
	
	public int cSize(ArrayList<Cluster> c){
		return c.size();
	}
	
	
	public boolean hasClusters(VisualNode v){	
		
		
		for(int i = 0; i < clusters.size(); i++){
			Cluster myC = clusters.get(i);
			System.out.println("name  -" + myC.getName(myC).toString() + "-v name  " + v.getName());
			String name = myC.getName(myC).toString();
			name = StringUtils.strip(name, "[");
			name = StringUtils.strip(name, "]");
			name = StringUtils.strip(name, " ");
//			if(myC.getName(myC).toString().trim().contains(v.getName()) && (myC.getName(myC)).size() > 1){
			if(myC.getName(myC).size() > 1){
				for(int j = 0; j < myC.getName(myC).size(); j++){
					if(myC.getName(myC).get(j).toString().trim().contentEquals(v.getName())){
						System.out.println("match   " + myC.getName(myC).toString().trim()   +  "      "  + v.getName());
						return true;
					}
				}
			}
		}
		
//		
//		Iterator<Cluster> it = clusters.iterator();
//		
//		while (it.hasNext()){
//			Cluster myC = (Cluster) it;
//			System.out.println("name  " + myC.getName(myC));
//			if(myC.getName(myC).toString().contains(v.getName())){
//				return true;
//			}
//			it.next();
//		}
		return false;
	}
	
	private int id = -1;
	private ArrayList<Cluster> clusters; 
	private double clusterDistances[][];
}
