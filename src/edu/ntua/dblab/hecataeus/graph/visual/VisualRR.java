package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clusters.EngineConstructs.ClusterSet;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;

public class VisualRR extends VisualCircleLayout{
	
	protected VisualGraph graph;
	protected double endC;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	private static int clusterId = 0;
	
	protected List<String> files;
	private List<VisualNode> RQV;
	protected VisualCircleLayout vcl;
	

	
	protected VisualRR(VisualGraph g, double endC) {
		super(g);
		this.graph = g;
		this.endC = endC;
		vcl = new VisualCircleLayout(this.graph);
		
		queries = new ArrayList<VisualNode>(vcl.getQueries());
		relations = new ArrayList<VisualNode>(vcl.getRelations());
		views = new ArrayList<VisualNode>(vcl.getViews());
		
		RQV = new ArrayList<VisualNode>(vcl.RQV);
		files = new ArrayList<String>(vcl.files);
		
	}

}
