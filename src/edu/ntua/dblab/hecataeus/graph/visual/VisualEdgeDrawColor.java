package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

/*
 * sets the color of edges according to their type and state
 */ 
public class VisualEdgeDrawColor implements Transformer<VisualEdge, Paint>{


	private PickedInfo<VisualEdge> picked;
    
    public VisualEdgeDrawColor(PickedInfo<VisualEdge> pi)
    {
        this.picked = pi;
    }
          
    public Paint transform(VisualEdge e)
    {
        if (picked.isPicked(e))
        {
            return Color.CYAN; 
        }
        else
        {
        	StatusType status = (e.getStatus());
        	
        	if (status==StatusType.PROPAGATE)
        		return Color.RED;
        	else if (status==StatusType.NO_STATUS)
        		return new Color(209,204,204);
            else 
            	return new Color(200,0,200);
        }
    }
}