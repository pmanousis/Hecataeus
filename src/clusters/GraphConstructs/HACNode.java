package clusters.GraphConstructs;

import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

public class HACNode {

	public  HACNode(){
		name = null;
	}
	public  HACNode(VisualNode n){
		name = n;
	}
	
	public VisualNode getName(){ return name; }

	protected VisualNode name;
}
