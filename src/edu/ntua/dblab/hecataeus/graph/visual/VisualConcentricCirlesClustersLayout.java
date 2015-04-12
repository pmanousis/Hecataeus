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
 * circular concentric clustering layout 
 * places clusters on concentric circles each cluster is a different circle
 * First we divide the clusters in groups of 2^i
 * then for each group we place its clusters on a different circle
 * We optimize the radius of each concetric circle with our radius optimization technique
 */

public class VisualConcentricCirlesClustersLayout extends VisualCircleLayout{
	protected VisualGraph graph;
	protected double endC;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	protected List<String> files;
	private List<VisualNode> RQV;
	protected VisualCircleLayout vcl;
	public double totalArea=0;
	
	protected VisualConcentricCirlesClustersLayout(VisualGraph g, double endC) {
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
	 * @attribute clusters: list with clusters created with clustering algo
	 * @attribute vertices: list with clusters created with clustering algo
	 * @attribute sublistofClusters : list of clusters with 2^i clusters
	 * @attribute bigCircleRad : radius of curent concetric circle
	 */
	protected void multipleCircles()
	{
		ArrayList<ArrayList<Cluster>> listOfClusters = createTwoToISegments(cs.getClusters());
		double currentOutterCircleRad = 0.0, nextOutterCircleRad = 0;
		for(ArrayList<Cluster> sublistOfClusters: listOfClusters)
		{
			if(nextOutterCircleRad < currentOutterCircleRad + getMaxRadius(sublistOfClusters.get(sublistOfClusters.size()-1).getNodesOfCluster())){
				nextOutterCircleRad = currentOutterCircleRad + getMaxRadius(sublistOfClusters.get(sublistOfClusters.size()-1).getNodesOfCluster());
			}
			double nextCircumference = sublistOfClusters.size() * getMaxRadius(sublistOfClusters.get(sublistOfClusters.size()-1).getNodesOfCluster());	// as if all same size
			if(nextCircumference > (nextOutterCircleRad * (Math.PI * 2)))
			{
				nextOutterCircleRad = nextCircumference / (Math.PI * 2);
			}
			currentOutterCircleRad = nextOutterCircleRad;
			double angle = 0.0;
			int a = 0;
			for(Cluster cluster : sublistOfClusters)
			{
				angle = (2 * Math.PI * a) / sublistOfClusters.size();
				a++;
				Point2D clusterCenter = new Point2D.Double(Math.cos(angle) * currentOutterCircleRad, Math.sin(angle) * currentOutterCircleRad);
				circles(cluster.getNodesOfCluster(), clusterCenter);
			}
		}
		HecataeusViewer.getActiveViewer().repaint();
		//TODO: FIX THIS
		HecataeusViewer.getActiveViewerZOOM().repaint();
	}

	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
        VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
        engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
        engine.buildFirstSolution();
        cs = engine.execute(endC);
        multipleCircles();
        HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
		HecataeusViewer.getActiveViewerZOOM().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewerZOOM().getPickedVertexState()));
		HecataeusViewer.getActiveViewerZOOM().repaint();
		HecataeusViewer.hecMap.createMap();
	}

	@Override
	public void reset() {
		initialize();
	}

}