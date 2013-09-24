package clusters.GraphFacades;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;


public class ClusterableQuery extends ClusterableObject{
	public ClusterableQuery(VisualNode q) {
		theObject = q;
	}
	public String printClusterableObject(){
		return theObject.getName(); 
	}
	public VisualNode getObject(){ return theObject; }
	
	
	private VisualNode theObject;
}
