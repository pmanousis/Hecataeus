package clusters.GraphFacades;

import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

public abstract class ClusterableObject {
	
	public VisualNode getObject(){
		return theObject;
	}
	public int getId(){ return id;}
	public void setId(int n){ id=n; }
	public abstract String printClusterableObject();
	
	private VisualNode theObject;
	private int id = -1;
}

