package edu.ntua.dblab.hecataeus.graph.visual;



import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VisualNodeStroke<V,E> implements Transformer<VisualNode,Stroke> {
	
	private static int i = 0;
	float[] dotting = {1.0f, 3.0f};
	float[] dashing = {5.0f};
	protected PickedInfo<VisualNode> picked;
	protected VisualNode myNode;
	protected boolean highlight = true;
	protected Stroke heavy = new BasicStroke(5);
	//protected Stroke medium = new BasicStroke(3);
	
	
	
	protected Stroke mediumIn = new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dotting, 0f);
	
	protected Stroke mediumOut = new BasicStroke(2f,BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1.0f, dashing, 0f);
	
	
 	protected Stroke light = new BasicStroke(0);
 	protected VisualGraph graph;
 	protected Collection<VisualNode> neighbors;
 	
 	protected VisualizationViewer viewer;
 	
	public VisualNodeStroke(PickedInfo<VisualNode> pi, VisualGraph g ,VisualizationViewer vv){
		this.picked = pi;
		this.graph = g;
		this.viewer = vv;
		
	}
	
	public Stroke transform(VisualNode v){
//		i = 0;
		if(HecataeusViewer.nodeSize){
			mediumIn = new BasicStroke(3f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2.0f, dotting, 0f);
			mediumOut = new BasicStroke(3f,BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 2.0f, dashing, 0f);
		}
		if (picked.isPicked(v)){
//			System.out.println("PICKED  " + v.toString());
			return heavy;
		}
		else{
			Collection<VisualNode> toNodes = new ArrayList<VisualNode>();
			Collection<VisualNode> fromNodes = new ArrayList<VisualNode>();
			
			List<VisualEdge> inE = new ArrayList<VisualEdge>(v._inEdges);
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
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
			