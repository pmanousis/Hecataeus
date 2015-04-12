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
 * concentric arcs clustering layout 
 * places clusters on concentric arch each cluster is a different circle
 * First we divide the clusters in groups of 2^i
 * then for each group we place its clusters on a different arc
 * We optimize the radius of each concetric circle with our radius optimization technique
 * 
 */
public class VisualConcentricArcsClusterLayout extends VisualCircleLayout{

    protected VisualGraph graph;
    protected double endC;
    private List<VisualNode> queries;
    private List<VisualNode> relations;
    private List<VisualNode> views;
    private ClusterSet cs;
    protected List<String> files;
    private List<VisualNode> RQV;
    protected VisualCircleLayout vcl;
    
    protected VisualConcentricArcsClusterLayout(VisualGraph g, double endC) {
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
	 */
    protected void arcs(){
    	ArrayList<ArrayList<Cluster>> listOfClusters = createTwoToISegments(cs.getClusters());
        for(ArrayList<Cluster> sublistOfClusters: listOfClusters)
        {
            double circumference = 0;
    		for(Cluster cl: sublistOfClusters)
    		{	// simulate placement to find maximum radius for each cluster
    			circumference += getMaxRadius(cl.getNodesOfCluster());
    		}
    		Collections.shuffle(sublistOfClusters);
    		double angle = 0.0, sum = 0.0, myRad = (circumference / Math.PI) * 4;	// * 4: because we want pi/4 arcs.
    		for(Cluster cl: sublistOfClusters)
    		{
    			angle = Math.acos(1 - (Math.pow(getMaxRadius(cl.getNodesOfCluster()), 2)) / (2 * Math.pow(myRad, 2)));
    			circles(cl.getNodesOfCluster(), new Point2D.Double(Math.cos(sum + angle) * myRad, Math.sin(sum + angle) * myRad));
    			sum += angle * 2;
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
        arcs();
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