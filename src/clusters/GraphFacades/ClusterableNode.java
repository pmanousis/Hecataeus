package clusters.GraphFacades;
import clusters.GraphConstructs.HACNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;


public class ClusterableNode extends ClusterableObject{
	
	public ClusterableNode(VisualNode n) {
		theObject = n;
	}
	public String printClusterableObject(){
		return "Node " + getId() + ":" + theObject.getName() + " "; 
	}
	public VisualNode getObject(){ return theObject; }
	
	
	private VisualNode theObject;
	
}
