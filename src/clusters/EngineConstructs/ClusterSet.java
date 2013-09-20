package clusters.EngineConstructs;
import java.util.ArrayList;
import java.util.Iterator;

public class ClusterSet {

	public ClusterSet(int clustId){
		id = clustId;
		clusters = new ArrayList<Cluster>(); 

	}
	public int getId() {return id;}
	public ArrayList<Cluster> getClusters(){return clusters;}
	
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
	
	private int id = -1;
	private ArrayList<Cluster> clusters; 
	private double clusterDistances[][];
}
