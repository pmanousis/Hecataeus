/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.jung;

import org.apache.commons.collections.Predicate;
import org.hecataeus.evolution.HecataeusStatusType;
import org.hecataeus.evolution.HecataeusNodeType;


/*
 * sets the visibility of nodes 
 */
 public final class HecataeusJungNodeVisible implements Predicate
 {
     public static enum VisibleLayer{
    	 RELATION,//show only relations,query and views
    	 ATTRIBUTE,//include attributes
    	 CONDITION,	//show all
    	 STATUS, //show node with status <> NO_STATUS
    	 POLICIES, //show node with status <> NO_STATUS
    	 EVENTS //show node with status <> NO_STATUS
    	 
    }
    
     public HecataeusJungNodeVisible()
     {
     }
     
     public void setVisibleLevel(HecataeusJungNodes nodes, VisibleLayer layer)
     {
    	 for (int i = 0; i < nodes.size(); i++) {
    		 HecataeusJungNode v=nodes.get(i);
    		 v.setVisible(false);
    		 
    		 switch (layer) {
    		 case ATTRIBUTE:
    			 v.setVisible((v.getType()==HecataeusNodeType.NODE_TYPE_RELATION)
    					 ||(v.getType()==HecataeusNodeType.NODE_TYPE_QUERY)
    					 ||(v.getType()==HecataeusNodeType.NODE_TYPE_VIEW)
    					 ||(v.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE));
    			 break;
    		 case RELATION:
    			 v.setVisible((v.getType()==HecataeusNodeType.NODE_TYPE_RELATION)
    					 ||(v.getType()==HecataeusNodeType.NODE_TYPE_QUERY)
    					 ||(v.getType()==HecataeusNodeType.NODE_TYPE_VIEW));
    			 break;
    		 case STATUS:
    			 v.setVisible(v.getHecataeusEvolutionNode().getStatus()!=HecataeusStatusType.NO_STATUS);
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
     
     public boolean evaluate(Object arg0)
     {
         HecataeusJungNode v = (HecataeusJungNode)arg0;
         return v.getVisible();         
     }
 }
 
