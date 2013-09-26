package clusters.EngineConstructs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import clusters.GraphFacades.ClusterableObject;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
public class Cluster {
	public Cluster(int n){
		id = n;
		extension  = new ArrayList<ClusterableObject>(); 
	}
	
	
	public Cluster(Cluster c){
		this.id = c.id;
		this.extension = c.extension;
	}
	
	public int getId(){ return id;} 
	public ArrayList<ClusterableObject> getExtension(){return extension;}
	
	public int setExtension(ArrayList<ClusterableObject> objectSet){
		extension = new ArrayList<ClusterableObject>(); 
		extension.addAll(objectSet);
		
		return extension.size();
	}
	
	public ArrayList<VisualNode> getNode(){
		Iterator<ClusterableObject> it = extension.iterator();
		ArrayList<VisualNode> nodes = new ArrayList<VisualNode>();
		while(it.hasNext()){
			nodes.add(it.next().getObject());
		}
		return nodes;
	}
	
	public String printCluster(){
		String message = "Cluster " + id + "\t";
		Iterator<ClusterableObject> it = extension.iterator();
		while(it.hasNext()){
			message += " " + it.next().printClusterableObject() + " ";
		}
		return message;
	}
	
	public ArrayList<String> getName(Cluster t){
		ArrayList<String> names = new ArrayList<String>();
		Iterator<ClusterableObject> it = extension.iterator();
		while(it.hasNext()){
			names.add(it.next().printClusterableObject());
		}
		return names;
	}
	
	
	// TODO put avg, min, max as parameters, compute them all and let the algo decide which one to use :)
	public double computeDistance(Cluster c, double[][] inputObjectsDistances){
		int posI = -1, posJ = -1;
		int thisSize = extension.size();
		int cSize = c.extension.size();
		double currDist = -1, totalDist = 0.0;
		
		for (int i = 0; i < thisSize; i++){
			posI = extension.get(i).getId();
			for (int j = 0; j < cSize; j++){
				posJ = c.extension.get(j).getId();
				currDist = inputObjectsDistances[posI][posJ];
				if(currDist == Double.NaN){
					System.out.println("Crashes at " + posI + " , " + posJ);
					System.exit(-120);
				}
				totalDist += currDist;
				//c2 = lastSolution.getClusters().get(j);
				//currDist = c1.computeDistance(c2);
				//lastSolution.getClusterDistances()[i][j] = c1.computeDistance(c2);
				
				//highlight the single pair with the minimum distance
				//if (currDist < minDist){
				//	minPos1 = i; minPos2 = j;
				}
			}
		totalDist = totalDist / (double)(thisSize * cSize);	
		
		//	System.out.println(" totalDist  " + totalDist + "thisSizr " + thisSize + " cSize " + cSize);
		
		
		return totalDist;
	}
	
	/*
	 * The caller cluster becomes the new cluster, so it retains its id
	 * We have to merge:
	 * (a) the source object list
	 * (b) nothing else
	 * 
	 * ATTN: the inputObjects MUST be iterated without order, due to the addAll call
	 */
	public void mergeClusters(Cluster c){
		extension.addAll(c.extension);
	}
	
	private int id = -1;
	private ArrayList<ClusterableObject> extension;
	
}
