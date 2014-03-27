package clusters.GraphFacades;

import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

public class ClusterableView extends ClusterableObject{

	public ClusterableView(VisualNode t){
		theObject = t;
	}
	@Override
	public String printClusterableObject(){
		//return "View " + getId() + ":" + theObject.getName() + " "; 
	
		return theObject.getName(); 
	}
	public VisualNode getObject(){ return theObject; }
	
	public String viewName(){
		return theObject.getName().trim();
	}
	
	private VisualNode theObject;

}
