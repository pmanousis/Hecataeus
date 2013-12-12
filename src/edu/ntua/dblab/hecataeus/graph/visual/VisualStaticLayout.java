package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import edu.ntua.dblab.hecataeus.HecataeusClusterMap;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;


public class VisualStaticLayout extends AbstractLayout<Object, Object>{

	private Graph graph;
	ArrayList<VisualNode> nodes;
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
			int nodeSize = (int)n.getNodeSize();
			//HecataeusClusterMap.myViewer.getRenderContext().setVertexShapeTransformer(new VisualNodeShape(nodeSize));
		}
	}

	@Override
	public void reset() {
		initialize();
	}

}
