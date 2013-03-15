/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.jung;

import java.awt.Color;
import java.awt.Paint;

import org.hecataeus.evolution.HecataeusStatusType;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.visualization.PickedInfo;

/*
 * sets the color of edges according to their type and state
 */ 
public final class HecataeusJungEdgeColor implements EdgePaintFunction
{
    protected PickedInfo picked;
    
    public HecataeusJungEdgeColor(PickedInfo pi)
    {
        this.picked = pi;
    }
          
    public Paint getDrawPaint(Edge e)
    {
        return Color.BLACK;
    }
    
    public Paint getFillPaint(Edge e)
    {
        if (picked.isPicked(e))
        {
            return Color.CYAN; 
        }
        else
        {
        	HecataeusStatusType status = ((HecataeusJungEdge) e).getHecataeusEvolutionEdge().getStatus();
        	
        	if (status==HecataeusStatusType.TO_DELETE)
        		return Color.RED;
        	else if (status==HecataeusStatusType.NO_STATUS)
        		return Color.WHITE;
            else 
            	return new Color(200,0,200);
        }
            
    }
}