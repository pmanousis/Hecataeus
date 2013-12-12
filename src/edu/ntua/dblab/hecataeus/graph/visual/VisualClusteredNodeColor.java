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
				if(node.getType() != NodeType.NODE_TYPE_RELATION){
					if(node.getType() == NodeType.NODE_TYPE_VIEW){
						//ta views einai prasina
						this.color = 150;
						node.setNodeColor(getColor(this.color));
					}
					else{ //QUERY
						String fileName = node.getFileName();
						Color toReturn = vfs.getColorForFile(fileName);
						return toReturn;
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
			case 100: return new Color (255,62, 150);
			case 150: return new Color(46,139,87);
			default : return new Color(0,0,0);
		}
		
		
	}
	
	

}
