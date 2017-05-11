package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.Comparator;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class CustomComparator implements Comparator<VisualNode> {
    @Override
    public int compare(VisualNode o1, VisualNode o2) {
    	if(o1.getType() == NodeType.NODE_TYPE_RELATION && o2.getType() == NodeType.NODE_TYPE_QUERY){
    		return -1;
    	}
    	if(o1.getType() == NodeType.NODE_TYPE_QUERY && o2.getType() == NodeType.NODE_TYPE_RELATION){
    		return 1;
    	}
    	if(o1.getType() == NodeType.NODE_TYPE_RELATION && o2.getType()== NodeType.NODE_TYPE_RELATION){
    		return(compareLevel(o1, o2));
    	}
    	if(o1.getType()== NodeType.NODE_TYPE_QUERY && o2.getType() == NodeType.NODE_TYPE_QUERY){
    		return(compareLevel(o1, o2));
    	}
    	if(o1.getType() == NodeType.NODE_TYPE_RELATION && o2.getType() == NodeType.NODE_TYPE_VIEW){
    		return -1;
    	}
    	if(o1.getType() == NodeType.NODE_TYPE_VIEW && o2.getType() == NodeType.NODE_TYPE_QUERY){
    		return -1;
    	}
    	if(o1.getType() == NodeType.NODE_TYPE_VIEW && o2.getType() == NodeType.NODE_TYPE_RELATION){
    		return 1;
    	}
    	if(o1.getType() == NodeType.NODE_TYPE_QUERY && o2.getType() == NodeType.NODE_TYPE_VIEW){
    		return 1;
    	}
    	if(o1.getType()== NodeType.NODE_TYPE_VIEW && o2.getType() == NodeType.NODE_TYPE_VIEW){
    		return(compareLevel(o1, o2));
    	}
    	return 0;
    }
    
    public int compareLevel(VisualNode o1, VisualNode o2)
    {
		if (o1.getID() > o2.getID())
    	{
    		return(1);
    	}
    	return(-1);
    }
}