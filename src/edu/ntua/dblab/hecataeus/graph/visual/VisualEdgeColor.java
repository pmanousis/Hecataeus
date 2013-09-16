/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

/*
 * sets the color of edges according to their type and state
 */ 



public final class VisualEdgeColor implements Transformer<VisualEdge, Paint>
{
    protected PickedInfo<VisualEdge> picked;
    protected Color color;
    protected boolean unpicked = false;
    
    public VisualEdgeColor(PickedInfo<VisualEdge> pi)
    {
        this.picked = pi;
    }
          

	public VisualEdgeColor(VisualEdge visualEdge, Color magenta) {
		// TODO Auto-generated constructor stub
		this.unpicked = true;
		this.color = magenta;
		System.out.println("THE EDGE  " + visualEdge.getName());
		
	}


	public Paint transform(VisualEdge e){
		
		if(picked == null){
			if(unpicked && e.getHilight()){
		//		e.setHighlight(false);
        		return color;
			}
			return Color.WHITE;
		}
		else{
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
	        		return Color.WHITE;
	            else 
	            	return new Color(200,0,200);
	        }
		}
    }
}