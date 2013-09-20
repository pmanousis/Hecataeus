package clusters.GraphFacades;
import clusters.GraphConstructs.HACNode;


public class ClusterableNode extends ClusterableObject{

	public ClusterableNode(HACNode n) {
		theObject = n;
	}
	public String printClusterableObject(){
		return "Node " + getId() + ":" + theObject.getName() + " "; 
	}
	public Object getObject(){ return theObject; }
	
	
	private HACNode theObject;
	
}
