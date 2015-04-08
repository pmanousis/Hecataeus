package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.StopWatch;

/**
 * circular clustering layout 
 * places clusters on a single circle each cluster is a different circle
 * first we find the radius of the single circle in which all clusters fit without overlaps
 * Then we extend this radius in order to achieve white spaces between clusters
 * Then we randomly place the clusters on the periphery of the circle
 */
public class VisualClustersOnACircleLayout extends VisualCircleLayout {
	
	protected double endC;
	protected VisualGraph graph;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	static int a = 0;
	protected List<String> files;
	private List<VisualNode> RQV;
	protected VisualCircleLayout vcl;
	
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
	private void clustersOnaCircle()
	{
		ArrayList<Cluster> Clusters = new ArrayList<Cluster>(cs.getClusters());/** list with clusters created with clustering algo */
		double circumference = 0;
		double biggestCluster = 0;
		Cluster biggerCluster = null;
		for(Cluster cl: Clusters)
		{	// simulate placement to find maximum radius for each cluster
			biggestCluster = getMaxRadius(cl.getNodesOfCluster());
			circumference += biggestCluster;
			biggerCluster = cl;
		}
		Collections.shuffle(Clusters);
		if(biggestCluster > (circumference / 2))
		{
			circumference = 2 * biggestCluster;
			Cluster tmp = Clusters.get(Clusters.size()-1);
			int biggestCurrentPossition = Clusters.indexOf(biggerCluster);
			Clusters.set(Clusters.size()-1, Clusters.get(biggestCurrentPossition));	// put the bigger to end
			Clusters.set(biggestCurrentPossition, tmp);
		}
		double myRad = circumference / Math.PI;
		double angle = 0.0, sum = 0.0;
		for(Cluster cl: Clusters)
		{
			angle = Math.acos(1 - (Math.pow(getMaxRadius(cl.getNodesOfCluster()), 2)) / (2 * Math.pow(myRad, 2)));
			circles(cl.getNodesOfCluster(), new Point2D.Double(Math.cos(sum + angle) * myRad * 1.25, Math.sin(sum + angle) * myRad * 1.25));
			sum += angle * 2;
		}
	}

	@Override
	public void initialize() {
		//begin clustering
		StopWatch clusterTimer = new StopWatch();
		clusterTimer.start();
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		clusterTimer.stop();
	//	System.out.println("Cluster TIMER " + clusterTimer.toString());
		//end clustering
		//begin visualization
		StopWatch visTimer = new StopWatch();
		visTimer.start();
		clustersOnaCircle();
		visTimer.stop();
//		System.out.print(clusterTimer.toString() + "\t" + visTimer.toString() + "\t");
	//	System.out.println("Visualization TIMER " + visTimer.toString());
		//end visualization
		HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
		//TODO: FIX THIS
		HecataeusViewer.getActiveViewerZOOM().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewerZOOM().getPickedVertexState()));
		HecataeusViewer.getActiveViewerZOOM().repaint();
		HecataeusViewer.hecMap.createMap();
	}

	@Override
	public void reset() {
		initialize();
	}
	
}
