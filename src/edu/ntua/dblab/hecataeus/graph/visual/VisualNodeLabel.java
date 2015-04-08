package edu.ntua.dblab.hecataeus.graph.visual;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class VisualNodeLabel extends ToStringLabeller<VisualNode> {

	private boolean visible;
	/**
     * Returns the label of the node
     */
    public String transform(VisualNode v) {
    	switch (v.getType()) {
    	/*case NODE_TYPE_CLUSTER:
    		return v.getNodeLabel();*/
    	case NODE_TYPE_RELATION:
    	{
    		if(this.visible==true && v.getInEdges().size()>1)
    			return v.getName();
    		else
    			return("");
    	}
		case NODE_TYPE_QUERY:
    	case NODE_TYPE_VIEW:
    			return("");
    	default: return(v.toString()); 
    	}
    }
    
    public void setVisibility(boolean tf)
    {
    	this.visible=tf;
    }
}
