package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class VisualClusteredNodeColor  implements Transformer<VisualNode, Paint>{

//	protected List<VisualNode> nodeList = new ArrayList<VisualNode>();
	
	protected VisualNode myNode;
	protected ArrayList<String> eva = new ArrayList<String>();
	public VisualClusteredNodeColor(VisualNode node){
//		for(VisualNode v : nodes){
//			this.nodeList.add(v);
//		}
		this.myNode =  node;
		this.eva.add("modules.txt");
		this.eva.add("scripts.txt");
	}
	
	@Override
	public Paint transform(VisualNode node) {
		this.myNode= node;
		List<VisualEdge> edges = new ArrayList<VisualEdge>(node.getInEdges());
		VisualNode file = null;
		for(VisualEdge e : edges){
			if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
				file = e.getFromNode();
			}
		}
		for(int j = 0; j < 2; j++){
			if(file.getName().equals(this.eva.get(0))){
//				Random rand = new Random();
//				final float hue = j*(float)0.1;
//				// Saturation between 0.1 and 0.3
//				final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
//				final float luminance = 0.9f;
//				final Color color = Color.getHSBColor(hue, saturation, luminance);
//				return color;
				return Color.PINK;
			}
			else if(file.getName().equals(this.eva.get(1))){
//				Random rand = new Random();
//				final float hue = rand.nextFloat();
//				// Saturation between 0.1 and 0.3
//				final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
//				final float luminance = 0.9f;
//				final Color color = Color.getHSBColor(hue, saturation, luminance);
//				return color;
				return Color.GREEN;
			}
		}
		return null;
//		int i = 0;
//		for(VisualNode v : VisualCircleClusteredLayout.files){
//			if(v.equals(file)){
//				Random rand = new Random();
//	//			final float hue = rand.nextFloat();
//				final float hue = i*(float)0.1;
//				i++;
//				// Saturation between 0.1 and 0.3
//				final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
//				final float luminance = 0.9f;
//				final Color color = Color.getHSBColor(hue, saturation, luminance);
//				return color;
//			}
//			else{
//				Random rand = new Random();
//				final float hue = rand.nextFloat();
//				// Saturation between 0.1 and 0.3
//				final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
//				final float luminance = 0.9f;
//				final Color color = Color.getHSBColor(hue, saturation, luminance);
//				return color;
//			}
//		}
//		return null;
////		
//		Random rand = new Random();
//		final float hue = rand.nextFloat();
//		// Saturation between 0.1 and 0.3
//		final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
//		final float luminance = 0.9f;
//		final Color color = Color.getHSBColor(hue, saturation, luminance);
//		return color;
	}

}
