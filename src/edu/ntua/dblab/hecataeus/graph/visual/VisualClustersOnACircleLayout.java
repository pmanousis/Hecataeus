package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.gui.HecataeusViewer;

/**
 * circular clustering layout 
 * places clusters on a single circle each cluster is a different circle
 * first we find the radius of the single circle in which all clusters fit without overlaps
 * Then we extend this radius in order to achieve white spaces between clusters
 * Then we randomly place the clusters on the periphery of the circle
 */
public class VisualClustersOnACircleLayout extends VisualCircleLayout {
	
	private double endC;
	private VisualGraph graph;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	private List<VisualNode> RQV;
	private VisualCircleLayout vcl;
	
	public VisualClustersOnACircleLayout(VisualGraph g, double endC) {
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

/**
 * Implements what the class is about
 * @author eva
 * @attribute clusterList :  list with all clusters new for every visualizing algo
 * 	needs to be cleared before used
 * @attribute Clusters: list with clusters created with clustering algo
 * @attribute vertices: list with clusters created with clustering algo shuffled
 * @attribute halfCircumference : 1/2 circle circumference 
 * @attribute myRad: the optimal radius of the single circle 
 */
	private void oneCircle()
	{
		ArrayList<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());/** list with clusters created with clustering algo */
		double circumference = 0;
		double biggestCircumference = 0;
		Cluster biggestCluster = null;
		for(Cluster cl: clusters)
		{	// simulate placement to find maximum radius for each cluster
			biggestCircumference = getMaxRadius(cl.getNodesOfCluster());
			circumference += biggestCircumference;
			biggestCluster = cl;
		}
		Collections.shuffle(clusters);
		if(biggestCircumference > (circumference / 2))
		{
			circumference = 2 * biggestCircumference;
			Collections.swap(clusters, 0, clusters.indexOf(biggestCluster));	// put biggest in the beginning
		}
		double angle = 0.0, sum = 0.0, myRad = circumference / Math.PI;
		for(Cluster cl: clusters)
		{
			angle = Math.acos(1 - (Math.pow(getMaxRadius(cl.getNodesOfCluster()), 2)) / (2 * Math.pow(myRad, 2)));
			circles(cl.getNodesOfCluster(), new Point2D.Double(Math.cos(sum + angle) * myRad, Math.sin(sum + angle) * myRad));
			sum += angle * 2;
		}
	}

	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		oneCircle();
		HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
		HecataeusViewer.getActiveViewerZOOM().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewerZOOM().getPickedVertexState()));
		HecataeusViewer.getActiveViewerZOOM().repaint();
	}

	@Override
	public void reset() {
		initialize();
	}
	
}