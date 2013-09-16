package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VisualNodeNeighborColor implements Transformer<VisualNode, Paint>{

	protected VisualNode myNode;
	protected static float dark_value = 0.8f;
	protected static float light_value = 0.7f;
	protected float fade = 0.1f;
	protected PickedInfo<VisualNode> picked;
	
	public VisualNodeNeighborColor(PickedInfo<VisualNode> pi){
		this.picked = pi;
	}



	public Paint transform(VisualNode v) {

		StatusType status = (v.getStatus());
		NodeType type = v.getType();
		float alpha = 0.3f;
		
		
		if (picked.isPicked(v)){
			Color color1 = new Color(0xeb,0xeb,0xeb);
			Color color2 = new Color(0xa2,0xbd,0xd8);
			GradientPaint gp = new GradientPaint( 0f, 0f, color1, 9f, 1f, color2 , true);
			return gp;
		}
		else{
			
			Collection<VisualNode> toNodes = new ArrayList<VisualNode>();
			Collection<VisualNode> fromNodes = new ArrayList<VisualNode>();
			
			List<VisualEdge> inE = new ArrayList<VisualEdge>(v._inEdges);
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);

			
			for(VisualEdge edgeIndx : inE){
				if(edgeIndx.getFromNode()!=null){
					fromNodes.add(edgeIndx.getFromNode());
				}
			}
			
			
			for(VisualEdge edgeIndx : outE){
				if(edgeIndx.getToNode()!=null){
					toNodes.add(edgeIndx.getToNode());
				}
			}
			for(VisualNode w : toNodes){
//				System.out.println("NEIGHBORS  " + w.toString());
				if (picked.isPicked(w)){
					dark_value = 0.7f;
					light_value = 0.6f;
					Color dark = new Color(0, 0, dark_value, alpha);
					Color light = new Color(0, 0, light_value, alpha);
					return new GradientPaint( 0, 0, Color.RED, 10, 0, light, true);
				}
			}
			for(VisualNode w : fromNodes){
//				System.out.println("NEIGHBORS  " + w.toString());
				if (picked.isPicked(w)){
					dark_value = 0.8f;
					light_value = 0.6f;
					Color dark = new Color(0, 0, dark_value, alpha);
					Color light = new Color(0, 0, light_value, alpha);
					return new GradientPaint( 0, 2, Color.ORANGE, 10, 7.6f, light, true);
				}
			}
			
			if (status==StatusType.PROPAGATE ){
				return Color.RED;
			}
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

			
			
			
			
			
//			if(type == NodeType.NODE_TYPE_RELATION){
//				dark_value = 0.7f;
//				light_value = 0.1f;
//				Color dark = new Color(0, 0, dark_value, alpha);
//				Color light = new Color(0, 0, light_value, alpha);
//				return new GradientPaint( 0, 0, Color.RED, 10, 0, light, true);
//			}
//			else if (status == StatusType.PROPAGATE){
//				dark_value = 0.7f;
//				light_value = 0.1f;
//				Color dark = new Color(0, 0, dark_value, alpha);
//				Color light = new Color(0, 0, light_value, alpha);
//				return new GradientPaint( 0, 0, Color.GREEN, 10, 0, light, true);
//			}
//			else if(type == NodeType.NODE_TYPE_QUERY){
//				dark_value = 0.7f;
//				light_value = 0.1f;
//				Color dark = new Color(0, 0, dark_value, alpha);
//				Color light = new Color(0, 0, light_value, alpha);
//				return new GradientPaint( 0, 0, Color.MAGENTA, 10, 0, light, true);
//			}
//			else{
//				Color dark = new Color(0, 0, dark_value, alpha);
//				Color light = new Color(0, 0, light_value, alpha);
//				return new GradientPaint( 0, 0, dark, 10, 0, light, true);
//			}
		}

	}
}
