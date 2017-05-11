package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VisualClusteredNodeColor  implements Transformer<VisualNode, Paint>{

	protected static float dark_value = 0.8f;
	protected static float light_value = 0.7f;
	protected float fade = 0.1f;
	protected PickedInfo<VisualNode> picked;

	
//	protected VisualNode myNode;
	protected int color = 0;

	public VisualClusteredNodeColor(PickedInfo<VisualNode> pi){
		this.picked = pi;
//		this.myNode =  node;
	}
	
	@Override
	public Paint transform(VisualNode node) {
		float alpha = 0.2f;
		StatusType status = (node.getStatus());
		if (picked.isPicked(node)){
			return new Color(255,127,80);
		}
		else{
			Collection<VisualNode> toNodes = new ArrayList<VisualNode>();
			Collection<VisualNode> fromNodes = new ArrayList<VisualNode>();
			
			List<VisualEdge> inE = new ArrayList<VisualEdge>(node.getInEdges());
			List<VisualEdge> outE = new ArrayList<VisualEdge>(node.getOutEdges());
			VisualFileColor vfs = new VisualFileColor();
			
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
				if (picked.isPicked(w)){
					dark_value = 0.7f;
					light_value = 0.6f;
					Color dark = new Color(0, 1f, dark_value, alpha);
					return dark;
					//return new GradientPaint( 0, 0, Color.GRAY, 10, 0, dark, false);
				}
			}
			for(VisualNode w : fromNodes){
				if (picked.isPicked(w)){
					dark_value = 0.8f;
					light_value = 0.6f;
					Color light = new Color(1f, 0.3f, light_value, alpha);
					return light;
					//return new GradientPaint( 0, 2, Color.YELLOW, 10, 7.6f, light, false);
				}
			}
			if (status==StatusType.PROPAGATE)
        		return Color.RED;
        	else if (status==StatusType.BLOCKED)
        		return Color.BLACK;
        	else if (status==StatusType.PROMPT)
        		return Color.DARK_GRAY;
        	else {
            	if (node.getHasEvents())
            		return Color.PINK;
                else if (node.getHasPolicies())
                    return Color.YELLOW;
                else {
					if(node.getType().getCategory() == NodeCategory.MODULE ){
						if(node.getType() != NodeType.NODE_TYPE_RELATION){
							if(node.getType() == NodeType.NODE_TYPE_VIEW){
								//ta views einai prasina
								this.color = 150;
							}
							else{ //QUERY
								String fileName = node.getFileName();
								Color toReturn = vfs.getColorForFile(fileName);
								return toReturn;
							}
						}
						else{//ta relation einai grey
							this.color = 100;
						}
					}
					else{
						NodeType type = node.getType();
	                	if (type==NodeType.NODE_TYPE_CONDITION)
	                		return new Color(100,150,255);
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
	                	else if (type==NodeType.NODE_TYPE_GROUP_BY)
	                		return new Color(100,255,50);
	                	else if (type==NodeType.NODE_TYPE_ATTRIBUTE)
	                		return Color.LIGHT_GRAY;
						else if (type==NodeType.NODE_TYPE_INPUT)
							return Color.BLUE;
						else if (type==NodeType.NODE_TYPE_OUTPUT)
							return Color.ORANGE;
						else if (type==NodeType.NODE_TYPE_SEMANTICS)
							return Color.MAGENTA;
						/*else if(type == NodeType.NODE_TYPE_CLUSTER)
							return new Color(0.3f,0.9f,0.6f, alpha); //67,205,128*/
	                	else return Color.WHITE;
					}
                }
        	}
		}
		switch (this.color){
			case 100: return new Color (96,96,96);//relations
			case 150: return new Color(46,139,87);//views
//			case 200:  return new Color (0,0,0);
			
			default : return new Color(0,0,0);
		}
		
//		random colors
//		Random rand = new Random();
//		final float hue = rand.nextFloat();
//		// Saturation between 0.1 and 0.3
//		final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
//		final float luminance = 0.9f;
//		final Color color = Color.getHSBColor(hue, saturation, luminance);
//		return color;
	}
	
	
	private Color getColor(int c){
		switch (c){
			case 100: return new Color (255,62, 150);
			case 150: return new Color(46,139,87);
			default : return new Color(0,0,0);
		}
	}
}
