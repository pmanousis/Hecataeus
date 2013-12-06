package edu.ntua.dblab.hecataeus.graph.visual;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class VisualNodeLabel extends ToStringLabeller<VisualNode> {

	/**
     * Returns the label of the node
     */
    public String transform(VisualNode v) {
    	switch (v.getType()) {
    	//case NODE_TYPE_RELATION:
    	//	return v.getName();
    	//default: return(v.toString()); 
//    	case NODE_TYPE_RELATION:
//    		String test = " " + v.getNodeAngle();
//    		return test;
//    	case NODE_TYPE_QUERY:
//    		String test1 = " " + v.getNodeAngle();
//    		return test1;
    	//default: return(v.toString()); 
    	default: return(""); 
    	}
    	
    }
}
