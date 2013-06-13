/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Component;
import java.util.List;

import org.apache.commons.collections15.Predicate;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/*
 * sets the visibility of nodes 
 */
 public final class VisualNodeVisible implements Predicate<Context<Graph<VisualNode,VisualEdge>,VisualNode>>
 {
     /**
	 * @author  gpapas
	 */
    public static enum VisibleLayer{
    	
    	CONTAINER,//show only containers, such as files, etc.
    	
    	MODULE,//include modules such as relations,query and views
    	
    	SCHEMA,//include attributes
    	  
    	SEMANTICS,	//show all
    	 
    	STATUS, //show node with status <> NO_STATUS
    	 
    	POLICIES, //show node with status <> NO_STATUS
    	 
    	EVENTS //show node with status <> NO_STATUS
    	 
    }
    
     public VisualNodeVisible()
     {}
     
     public void setVisibleLevel(List<VisualNode> nodes, VisibleLayer layer)
     {
    	 for (VisualNode v : nodes) {
    		 v.setVisible(false);
    		 
    		 switch (layer) {
    		 case CONTAINER:
    			 v.setVisible(v.getType().getCategory()==NodeCategory.CONTAINER);
    			 break;
    		 case MODULE:
    			 v.setVisible(v.getType().getCategory()==NodeCategory.MODULE
    					 ||v.getType().getCategory()==NodeCategory.CONTAINER);
    			 break;
    		 case SCHEMA:
    			 v.setVisible(v.getType().getCategory()==NodeCategory.MODULE
    					 ||v.getType().getCategory()==NodeCategory.CONTAINER
    					 ||v.getType().getCategory()==NodeCategory.INOUTSCHEMA);
    			 break;
    		 case SEMANTICS:
    			 v.setVisible(true);
    			 break;
    		 case STATUS:
    			 v.setVisible(v.getStatus()!=StatusType.NO_STATUS);
    			 break;
    		 case POLICIES:
    			 v.setVisible (v.getHasPolicies());
    			 break;
    		 case EVENTS:
    			 v.setVisible(v.getHasEvents());
    			 break;
    		 default:
    			 v.setVisible(true);
    			 break;
    		 }
		}
		
     }
     
    	@Override
	public boolean evaluate(
			Context<Graph<VisualNode, VisualEdge>, VisualNode> rc) {
		 return rc.element.getVisible();
		
	}
 }
 
