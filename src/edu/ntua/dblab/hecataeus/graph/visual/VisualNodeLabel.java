package edu.ntua.dblab.hecataeus.graph.visual;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class VisualNodeLabel extends ToStringLabeller<VisualNode> {

	/**
     * Returns the label of the node
     */
    public String transform(VisualNode v) {
    	switch (v.getType().getCategory()) {
    	/*case SCHEMA:
    		return "";*/ 
    	//default: return(v.toString()); 
    	default: return(""); 
    	}
    	
    }
    
}
