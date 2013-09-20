package clusters.GraphFacades;
import clusters.GraphConstructs.HACQuery;


public class ClusterableQuery extends ClusterableObject{
	public ClusterableQuery(HACQuery q) {
		theObject = q;
	}
	public String printClusterableObject(){
		return "Query " + getId() + ": " + theObject.getName() + " "; 
	}
	public Object getObject(){ return theObject; }
	
	
	private HACQuery theObject;
}
