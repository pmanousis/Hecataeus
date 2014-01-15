package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusClusterMap;
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

    private VisualTotalClusters clusterList;
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
	 * 
	 * 
	 */
    protected void CirclingCusters(){
    	/**
		 * @author eva
		 * @attribute clusterList :  list with all clusters new for every visualizing algo
		 * 	needs to be cleared before used
		 * @attribute clusters: list with clusters created with clustering algo
		 * @attribute vertices: list with clusters created with clustering algo sorted 
		 * @attribute sublistofClusters : list of clusters with 2^i clusters
		 * @attribute bigCircleRad : radius of curent concetric circle
		 */
        clusterList = new VisualTotalClusters();
        clusterList.clearList();
        clusterId = 0;
        Dimension d = getSize();
        double w = d.getWidth();
        double h = d.getHeight();
        List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
        ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
        for(Cluster cl : clusters){
            vertices.add(cl.getNode());
        }

        Collections.sort(vertices, new ListComparator());

        
        double myRad = 1.0;
        
        ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
        while((int) Math.pow(2, myRad)<vertices.size()){
            ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(vertices.subList((int) Math.pow(2, myRad-1), (int) Math.pow(2, myRad)));
            sublistofClusters.add(tmpVl);
            myRad++;
        }
        ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(vertices.subList((int) Math.pow(2, myRad-1), vertices.size()));
        if(!tmpVl.isEmpty()){
            sublistofClusters.add(tmpVl);
        }
        sublistofClusters.add(0, new ArrayList<ArrayList<VisualNode>>(vertices.subList(0, 1)));

        double bigCircleRad = 0.0;
        double bigClusterRad = 0.0;
        double tempCircleRad = 0.0;
        
        for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
            ArrayList<ArrayList<VisualNode>> tmp ;
            if (sublistofClusters.indexOf(listaC)!=sublistofClusters.size()-1){
                tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)+1));
            }
            else{
                tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)));    
            }

            
            double periferia=0;
            for(ArrayList<VisualNode> lista : listaC){
                periferia+=getSmallRad(lista);
            }
            
            tempCircleRad=bigCircleRad+periferia/Math.PI;
            if(tempCircleRad<bigCircleRad+getSmallRad(listaC.get(listaC.size()-1))){
                tempCircleRad=bigCircleRad+getSmallRad(listaC.get(listaC.size()-1));
            }
            if(tempCircleRad<periferia){
                bigCircleRad=periferia/0.90;
            }
            else{
                bigCircleRad=tempCircleRad;
            }
            
            double angle = 0.0, sum = 0.0;
            for(ArrayList<VisualNode> lista : listaC){
                List<VisualNode> nodes = new ArrayList<VisualNode>();
                Collections.sort(lista, new CustomComparator());
                nodes.addAll(lista);
                double temp =   (2*Math.pow(bigCircleRad, 2)- Math.pow(getSmallRad(nodes), 2)*0.9)/(2*Math.pow(bigCircleRad, 2));
                if(Math.abs(temp)>1){
                    temp = 1;
                }
                angle = (Math.acos(temp))*1.2;
                double cx = Math.cos((sum+angle/2)/2*Math.PI) * bigCircleRad*1.8 ;// 1.8 is used for white space borders
                double cy =    Math.sin((sum+angle/2)/2*Math.PI) * bigCircleRad*1.8 ;
                sum+=angle;
                circles(nodes, cx, cy, clusterList);
            }
        }
        HecataeusViewer.getActiveViewer().repaint();
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
		//System.out.println("Cluster TIMER " + clusterTimer.toString());
		//end clustering
		//begin visualization
		StopWatch visTimer = new StopWatch();
		visTimer.start();
        CirclingCusters();
        visTimer.stop();
		//System.out.println("Visualization TIMER " + visTimer.toString());
		System.out.print(clusterTimer.toString() + "\t" + visTimer.toString() + "\t");
		//end visualization
        HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
		HecataeusViewer.hecMap.createMap();
    }

    @Override
    public void reset() {
        initialize();
    }

}