package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;

/**
 * a static layout  
 * used only for the overview map where each node gets the location 
 * of the cluster it represents in the Architecture Graph view
 * 
 */
public class VisualStaticLayout extends AbstractLayout<Object, Object>{

	@SuppressWarnings({ "unused", "rawtypes" })
	private Graph graph;
	ArrayList<VisualNode> nodes;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public VisualStaticLayout(Graph graph, ArrayList<VisualNode> nodes) {
		super(graph);
		this.graph =  graph;
		this.nodes = new ArrayList<VisualNode>(nodes);
	}

	@Override
	public void initialize() {
		for(VisualNode n :  nodes){
			Point2D coord = transform(n);
			Point2D loc = n.getLocation();
			coord.setLocation( loc.getX(), loc.getY());
			n.setLocation(coord);
		}
	}

	@Override
	public void reset() {
		initialize();
	}

}
