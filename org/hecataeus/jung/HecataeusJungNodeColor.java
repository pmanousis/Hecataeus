/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.jung;

import java.awt.Color;
import java.awt.Paint;

import org.hecataeus.evolution.HecataeusStatusType;
import org.hecataeus.evolution.HecataeusNodeType;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.visualization.PickedInfo;

/*
 * sets the color of nodes according to their type and state
 */
public final class HecataeusJungNodeColor implements VertexPaintFunction
{
    protected PickedInfo picked;
    
    public HecataeusJungNodeColor(PickedInfo pi)
    {
        this.picked = pi;
    }
          
    public Paint getDrawPaint(Vertex v)
    {
        return Color.BLACK;
    }
    
    public Paint getFillPaint(Vertex v)
    {
        if (picked.isPicked(v))
        {
            return Color.CYAN; 
        }
        else
        {	
        	HecataeusStatusType status = ((HecataeusJungNode) v).getHecataeusEvolutionNode().getStatus();
        	
        	if (status==HecataeusStatusType.TO_DELETE)
        		return Color.RED;
        	else if (status==HecataeusStatusType.TO_DELETE_CHILD)
        		return new Color(130,0,0);
        	else if (status==HecataeusStatusType.TO_MODIFY_PROVIDER)
                return new Color(200,0,200);
        	else if (status==HecataeusStatusType.TO_ADD_CHILD)
        		return Color.GREEN;
        	else if (status==HecataeusStatusType.TO_RENAME)
        		return Color.ORANGE;
        	else if (status==HecataeusStatusType.TO_MODIFY)
        		return Color.ORANGE;
        	else if (status==HecataeusStatusType.BLOCKED)
        		return Color.BLACK;
        	else if (status==HecataeusStatusType.PROMPT)
        		return Color.DARK_GRAY;
        	else {
            	if (((HecataeusJungNode) v).getHasEvents())
            		return Color.PINK;
                else if (((HecataeusJungNode) v).getHasPolicies())
                    return Color.YELLOW;
                else {
                	
                	HecataeusNodeType type = ((HecataeusJungNode) v).getHecataeusEvolutionNode().getType();
                	
                	if (type==HecataeusNodeType.NODE_TYPE_RELATION)
                		return new Color(165,42,42);
                	else if (type==HecataeusNodeType.NODE_TYPE_QUERY)
                		return new Color(0,0,230);
                	else if (type==HecataeusNodeType.NODE_TYPE_CONDITION)
                		return new Color(100,150,255);
                	else if (type==HecataeusNodeType.NODE_TYPE_VIEW)
                		return new Color(46, 139, 87);
                	else if (type==HecataeusNodeType.NODE_TYPE_GROUP_BY)
                		return new Color(100,255,50) ;
                	else if (type==HecataeusNodeType.NODE_TYPE_ATTRIBUTE)
                		return Color.LIGHT_GRAY;
                	else return Color.WHITE;
                }
        	}

        }
            
    }
}
