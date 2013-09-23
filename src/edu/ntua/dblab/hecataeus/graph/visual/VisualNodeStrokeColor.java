package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VisualNodeStrokeColor implements Transformer<VisualNode, Paint>{
	
	protected PickedInfo<VisualNode> picked;
	protected Collection<VisualNode> neighbors;
	protected List<VisualEdge> inE;
	protected List<VisualEdge> outE;
	public VisualNodeStrokeColor(PickedInfo<VisualNode> pi){
		this.picked = pi;
	}
	
	@Override
	public Paint transform(VisualNode v) {
		if (picked.isPicked(v)){
			System.out.println("PICKED  " + v.toString());

			
			
			return Color.BLACK;
		}
		else{
			Collection<VisualNode> toNodes = new ArrayList<VisualNode>();
			Collection<VisualNode> fromNodes = new ArrayList<VisualNode>();
			
			inE = new ArrayList<VisualEdge>(v._inEdges);
			outE = new ArrayList<VisualEdge>(v._outEdges);
			neighbors = new ArrayList<VisualNode>();
			
			for(VisualEdge edgeIndx : inE){
				if(edgeIndx.getFromNode()!=null){
					fromNodes.add(edgeIndx.getFromNode());
	//				neighbors.add(edgeIndx.getFromNode());
				}
			}
			
			
			for(VisualEdge edgeIndx : outE){
				if(edgeIndx.getToNode()!=null){
				toNodes.add(edgeIndx.getToNode());
	//			neighbors.add(edgeIndx.getToNode());
				}
			}	
			
			for(VisualNode w : fromNodes){
	//			System.out.println("NEIGHBORS  " + w.toString());
				if (picked.isPicked(w)){
	///				System.out.println("NEIGHBORS VISIBLE  " + w.toString());
	//				System.out.println("EDGE!!!!!  " + w._inEdges.get(i).getName());
	//				w._inEdges.get(i).setHighlight(true);
	//				viewer.getRenderContext().setEdgeFillPaintTransformer(new VisualEdgeColor(w._inEdges.get(i), Color.MAGENTA));
	//				i++;
	//				if(i>=w._inEdges.size()){
	//					i = 0;
	//				}
					return Color.MAGENTA;
				}
			}
			for(VisualNode w : toNodes){
	//			System.out.println("NEIGHBORS  " + w.toString());
				if (picked.isPicked(w)){
	///				System.out.println("NEIGHBORS VISIBLE  " + w.toString());
	//				System.out.println("EDGE!!!!!  " + w._inEdges.get(i).getName());
	//				w._inEdges.get(i).setHighlight(true);
	//				viewer.getRenderContext().setEdgeFillPaintTransformer(new VisualEdgeColor(w._inEdges.get(i), Color.MAGENTA));
	//				i++;
	//				if(i>=w._inEdges.size()){
	//					i = 0;
	//				}
					return Color.ORANGE;
				}
			}
			return Color.BLACK;
		}
	}

}
