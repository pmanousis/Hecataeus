/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

/*
 * sets the color of nodes according to their type and state
 */
public final class VisualNodeColor implements Transformer<VisualNode, Paint>
{
    protected PickedInfo<VisualNode> picked;
    
    public VisualNodeColor(PickedInfo<VisualNode> pi)
    {
        this.picked = pi;
    }
   
    public Paint transform(VisualNode v)
    {
        if (picked.isPicked(v))
        {
            return Color.CYAN; 
        }
        else
        {	
        	StatusType status = (v.getStatus());
        	
        	if (status==StatusType.TO_DELETE)
        		return Color.RED;
        	else if (status==StatusType.TO_DELETE_CHILD)
        		return new Color(130,0,0);
        	else if (status==StatusType.TO_MODIFY_PROVIDER)
                return new Color(200,0,200);
        	else if (status==StatusType.TO_ADD_CHILD)
        		return Color.GREEN;
        	else if (status==StatusType.TO_RENAME)
        		return Color.ORANGE;
        	else if (status==StatusType.TO_MODIFY)
        		return Color.ORANGE;
        	else if (status==StatusType.BLOCKED)
        		return Color.BLACK;
        	else if (status==StatusType.PROMPT)
        		return Color.DARK_GRAY;
        	else {
            	if (v.getHasEvents())
            		return Color.PINK;
                else if (v.getHasPolicies())
                    return Color.YELLOW;
                else {
                	
                	NodeType type = v.getType();
                	
                	if (type==NodeType.NODE_TYPE_RELATION)
                		return new Color(165,42,42);
                	else if (type==NodeType.NODE_TYPE_QUERY)
                		return new Color(0,0,230);
                	else if (type==NodeType.NODE_TYPE_CONDITION)
                		return new Color(100,150,255);
                	else if (type==NodeType.NODE_TYPE_VIEW)
                		return new Color(46, 139, 87);
                	else if (type==NodeType.NODE_TYPE_GROUP_BY)
                		return new Color(100,255,50) ;
                	else if (type==NodeType.NODE_TYPE_ATTRIBUTE)
                		return Color.LIGHT_GRAY;
                	else return Color.WHITE;
                }
        	}

        }
            
    }
}
