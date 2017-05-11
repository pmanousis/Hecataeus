package edu.ntua.dblab.hecataeus.graph.visual;



import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;
/**
 * @author eva
 * stroke type for nodes
 */
public class VisualNodeStroke  implements Transformer<VisualNode,Stroke> {
	
	private float[] dotting = { 1.0f, 3.0f };
	private float[] dashing = { 5.0f };
	private PickedInfo<VisualNode> picked;
	private Stroke heavy = new BasicStroke(5);
	private Stroke mediumIn = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dotting, 0f);
	private Stroke mediumOut = new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1.0f, dashing, 0f);
	private Stroke light = new BasicStroke(0);
	private Collection<VisualNode> neighbors;
 	
	public VisualNodeStroke(PickedInfo<VisualNode> pi){
		this.picked = pi;
	}
	
	public Stroke transform(VisualNode v){
		if (picked.isPicked(v)){
			return heavy;
		}
		else{
			Collection<VisualNode> toNodes = new ArrayList<VisualNode>();
			Collection<VisualNode> fromNodes = new ArrayList<VisualNode>();
			
			List<VisualEdge> inE = new ArrayList<VisualEdge>(v.getInEdges());
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v.getOutEdges());
			neighbors = new ArrayList<VisualNode>();
			
			for(VisualEdge edgeIndx : inE){
				if(edgeIndx.getFromNode()!=null){
					fromNodes.add(edgeIndx.getFromNode());
					neighbors.add(edgeIndx.getFromNode());
				}
			}
			
			
			for(VisualEdge edgeIndx : outE){
				if(edgeIndx.getToNode()!=null){
				toNodes.add(edgeIndx.getToNode());
				neighbors.add(edgeIndx.getToNode());
				}
			}	
			for(VisualNode w : toNodes){
				if (picked.isPicked(w)){
					return mediumIn;
				}
			}
			for(VisualNode w : fromNodes){
				if (picked.isPicked(w)){
					return mediumOut;
				}
			}
			return light;
		}
	}
}
			