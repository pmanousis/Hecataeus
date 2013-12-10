package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VisualClusteredNodeColor  implements Transformer<VisualNode, Paint>{

	protected static float dark_value = 0.8f;
	protected static float light_value = 0.7f;
	protected float fade = 0.1f;
	protected PickedInfo<VisualNode> picked;

	
	protected VisualNode myNode;
	protected int color = 0;

	public VisualClusteredNodeColor(VisualNode node, PickedInfo<VisualNode> pi){
		this.picked = pi;
		this.myNode =  node;
	}
	
	@Override
	public Paint transform(VisualNode node) {
//		node=this.myNode;
		VisualNode file = null;
		float alpha = 0.2f;
		if (picked.isPicked(node)){
			return new Color(255,127,80);
		}
		else{
			Collection<VisualNode> toNodes = new ArrayList<VisualNode>();
			Collection<VisualNode> fromNodes = new ArrayList<VisualNode>();
			
			List<VisualEdge> inE = new ArrayList<VisualEdge>(node._inEdges);
			List<VisualEdge> outE = new ArrayList<VisualEdge>(node._outEdges);

			
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
					Color dark = new Color(0, 1f, dark_value, alpha);
					Color light = new Color(0, 0, light_value, alpha);
					return dark;
					//return new GradientPaint( 0, 0, Color.GRAY, 10, 0, dark, false);
				}
			}
			for(VisualNode w : fromNodes){
//				System.out.println("NEIGHBORS  " + w.toString());
				if (picked.isPicked(w)){
					dark_value = 0.8f;
					light_value = 0.6f;
					Color dark = new Color(0, 0, dark_value, alpha);
					Color light = new Color(1f, 0.3f, light_value, alpha);
					return light;
					//return new GradientPaint( 0, 2, Color.YELLOW, 10, 7.6f, light, false);
				}
			}
			if(node.getType().getCategory() == NodeCategory.MODULE ){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(node.getInEdges());
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
						file = e.getFromNode();
					}
				}
				if(node.getType() != NodeType.NODE_TYPE_RELATION){
					if(node.getType() == NodeType.NODE_TYPE_VIEW){
						//ta views einai prasina
						this.color = 150;
						node.setNodeColor(getColor(this.color));
					}
					else{
						VisualFileColor vfs = new VisualFileColor();
						if(vfs.getFileNames()!=null){
							if(file == null){
								this.color = 5;
							}else{
								int col = vfs.getFileNames().indexOf(file.getName());
								//System.out.println("my name is  " + node.getName()   + " i come from  " + file + " i thesi   " + col );
								
								if(col == -1){
									 System.out.println("WTF = what a terible failure");
									 this.color = -1;
								}
								else{
									this.color = (col+1)%6; 
									node.setNodeColor(getColor(this.color));
								}
							}
						}
						else{
							this.color = 0; 
						}
					}
				}
				else{//ta relation einai grey
					this.color = 100;
					node.setNodeColor(getColor(this.color));
				}
			}if(node.getType() == NodeType.NODE_TYPE_CLUSTER){
				this.color = 200;
			}
		}
		switch (this.color){
			case 0: return new Color(16, 78, 139);
			case 1: return new Color(255,102,102);
			case 2: return new Color(255,178,102);
			case 3: return new Color(178,255,102);
			case 4: return new Color(102,178,255);
			case 5: return new Color(178,102,255);
			case 6: return new Color(255,102,102);
//			case 7: return new Color(255,255,102);
//			case 8: return new Color(102,102,255);
//			case 9: return new Color(255,153,024);
//			case 10: return new Color(102,204,0);
//			case 11: return new Color(153,0,153);
//			case 12: return new Color(102,0,0);
//			case 13: return new Color(0,51,102);
//			case 14: return new Color(204,204,255);
//			case 15: return new Color(0,128,255);
//			case 16: return new Color(255,153,51);
//			case 17: return new Color(255,192,203);
//			case 18: return new Color(198, 226, 255);
//			case 19: return new Color(30, 199,	40);
//			case 20: return new Color(0, 201, 87);
//			case 21: return new Color(188, 238, 104);
//			case 22: return new Color(238, 230,133);
//			case 23: return new Color(0, 206, 209);
//			case 24: return new Color(61, 145, 64);
//			case 25: return new Color(127,255,212);
//			case 26: return new Color(0,0,128);
//			case 27: return new Color(147,112,219);
//			case 28: return new Color(255,250,205);
//			case 29: return new Color(113,198,113);
//			case 30: return new Color(139,131,120);
//			case 31: return new Color(224,102,255);
//			
			
			
			
			case 100: return new Color (96,96,96);//relations
			case 150: return new Color(46,139,87);//views
			case 200:  return new Color (0,0,0);
			
			default : return new Color(0,0,0);
		}
		
		
		
		
		
		
//		
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
		case 0: return new Color(16, 78, 139);
		case 1: return new Color(255,102,102);
		case 2: return new Color(255,178,102);
		case 3: return new Color(178,255,102);
		case 4: return new Color(102,178,255);
		case 5: return new Color(178,102,255);
		case 6: return new Color(255,102,102);
		case 7: return new Color(255,255,102);
		case 8: return new Color(102,102,255);
		case 9: return new Color(255,153,024);
		case 10: return new Color(102,204,0);
		case 11: return new Color(153,0,153);
		case 12: return new Color(102,0,0);
		case 13: return new Color(0,51,102);
		case 14: return new Color(204,204,255);
		case 15: return new Color(0,128,255);
		case 16: return new Color(255,153,51);
		case 17: return new Color(255,192,203);
		case 18: return new Color(198, 226, 255);
		case 19: return new Color(30, 199,	40);
		case 20: return new Color(0, 201, 87);
		case 21: return new Color(188, 238, 104);
		case 22: return new Color(238, 230,133);
		case 23: return new Color(0, 206, 209);
		case 24: return new Color(61, 145, 64);
		case 25: return new Color(127,255,212);
		case 26: return new Color(0,0,128);
		case 27: return new Color(147,112,219);
		case 28: return new Color(255,250,205);
		case 29: return new Color(113,198,113);
		case 30: return new Color(139,131,120);
		case 31: return new Color(224,102,255);
		
		
		
		
		case 100: return new Color (255,62, 150);
		
		default : return new Color(0,0,0);
	}
		
		
	}
	
	

}
