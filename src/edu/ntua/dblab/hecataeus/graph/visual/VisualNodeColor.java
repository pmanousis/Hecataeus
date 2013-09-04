/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
        	
        	Color color1 = new Color(0xeb,0xeb,0xeb);

        	Color color2 = new Color(0xa2,0xbd,0xd8);
        	
        	GradientPaint gp = new GradientPaint( 0f, 0f, color1, 0f, 10f, color2 );
        	
            
        	return gp;
        	//return Color.cyan;
            //return (Paint) new GradientVertexRenderer<Integer, Number>( new Color(175,224,0), new Color(133,170,173), true); 
        }
        else
        {	
        	StatusType status = (v.getStatus());
        	
/*pmanousi 	if (status==StatusType.TO_DELETE)
        		return Color.RED;
        	else if (status==StatusType.TO_DELETE_CHILD)
        		return new Color(130,0,0);
        	else if (status==StatusType.TO_MODIFY_PROVIDER)
                return new Color(200,0,200);
        	else if (status==StatusType.TO_ADD_CHILD)
        		return Color.GREEN;
        	else if (status==StatusType.TO_RENAME)
        		return Color.ORANGE;
        	else */if (status==StatusType.PROPAGATE)
        		return Color.RED;
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
                	else if (type==NodeType.NODE_TYPE_FILE)				//
                		return new Color(50,200,40,70);								//
                	else if (type==NodeType.NODE_TYPE_ANONYMOUS_BLOCK)		//
                		return new Color(200,170,10,70);								//
                	else if (type==NodeType.NODE_TYPE_SCRIPT)				//
                		return new Color(180,50,180);								//
                	else if (type==NodeType.NODE_TYPE_STORED_PROCEDURE)				//
                		return new Color(60,60,5,70);									//
                	else if (type==NodeType.NODE_TYPE_STORED_FUNCTION)				//
                		return new Color(100,40,36,70);								//
                	else if (type==NodeType.NODE_TYPE_TRIGGER)				//
                		return new Color(120,150,25,70);								//
                	else if (type==NodeType.NODE_TYPE_PACKAGE)				//
                		return new Color(100,255,50,70);								//
                	else if (type==NodeType.NODE_TYPE_EMBEDDED_STATEMENT)			//
                		return new Color(150,25,250,70);								//
                	else if (type==NodeType.NODE_TYPE_INSERT)				//added by sgerag
                		return new Color(50,200,40);								//
                	else if (type==NodeType.NODE_TYPE_DELETE)				//
                		return new Color(200,170,10);								//
                	else if (type==NodeType.NODE_TYPE_UPDATE)				//
                		return new Color(180,50,180);								//
                	else if (type==NodeType.NODE_TYPE_MERGE_INTO)				//
                		return new Color(100,100,100);								//
                	else if (type==NodeType.NODE_TYPE_CURSOR)				//
                		return new Color(60,60,5);									//
                	else if (type==NodeType.NODE_TYPE_VARIABLE)			//
                		return new Color(100,40,36);								//
                	else if (type==NodeType.NODE_TYPE_ASSIGNMENT)			//
                		return new Color(120,150,25);								//
                	else if (type==NodeType.NODE_TYPE_VIEW)
                		return new Color(46, 139, 87);
                	else if (type==NodeType.NODE_TYPE_GROUP_BY)
                		return new Color(100,255,50);
                	else if (type==NodeType.NODE_TYPE_ATTRIBUTE)
                		return Color.LIGHT_GRAY;
/**
 * @author pmanousi
 */
else if (type==NodeType.NODE_TYPE_INPUT)
	return Color.BLUE;
else if (type==NodeType.NODE_TYPE_OUTPUT)
	return Color.ORANGE;
else if (type==NodeType.NODE_TYPE_SEMANTICS)
	return Color.MAGENTA;
                	else return Color.WHITE;
                }
        	}

        }
            
    }
}
