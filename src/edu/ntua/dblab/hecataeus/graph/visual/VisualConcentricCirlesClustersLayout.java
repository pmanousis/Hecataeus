package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
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
	private VisualTotalClusters clusterList;
	
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
	 */
	protected void CirclingCusters(){
		/**
		 * @author eva
		 * @attribute clusterList :  list with all clusters new for every visualizing algo
		 * 	needs to be cleared before used
		 * @attribute clusters: list with clusters created with clustering algo
		 * @attribute vertices: list with clusters created with clustering algo
		 * @attribute sublistofClusters : list of clusters with 2^i clusters
		 * @attribute bigCircleRad : radius of curent concetric circle
		 */
		clusterList = new VisualTotalClusters();
		clusterList.clearList();
		clusterId = 0;
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		Dimension d = getSize();
		double w = d.getWidth();
		double h = d.getHeight();
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
		if(endC == 1){
			Collections.sort(vertices, new ListComparator());
			sortedV.addAll(vertices);
		}else{
			sortedV.addAll(vertices);
		}
		
		
		double conCircle = 1.0;
		
		ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
		while((int) Math.pow(2, conCircle)<sortedV.size()){
			ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, conCircle-1), (int) Math.pow(2, conCircle)));
			sublistofClusters.add(tmpVl);
			conCircle++;
		}
		ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, conCircle-1), sortedV.size()));
		if(!tmpVl.isEmpty()){
			sublistofClusters.add(tmpVl);
			
		}
		sublistofClusters.add(0, new ArrayList<ArrayList<VisualNode>>(sortedV.subList(0, 1)));

		

		double bigCircleRad = 0.0, tempCircleRad=0;

		for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
			
			double periferia=0;
			for(ArrayList<VisualNode> lista : listaC){
				periferia+=getSmallRad(lista);
			}
			
			tempCircleRad=bigCircleRad+periferia/Math.PI;
			if(tempCircleRad<bigCircleRad+getSmallRad(listaC.get(listaC.size()-1))){
				tempCircleRad=bigCircleRad+getSmallRad(listaC.get(listaC.size()-1));
			}
			
			if(periferia > tempCircleRad){
				bigCircleRad=periferia*1.2;
			}
			else{
				bigCircleRad=tempCircleRad;
			}
		
			double angle = 0.0, sum = 0.0;
			int a = 0;
			for(ArrayList<VisualNode> lista : listaC){
				List<VisualNode> nodes = new ArrayList<VisualNode>();
				Collections.sort(lista, new CustomComparator());
				nodes.addAll(lista);
				angle = (2*Math.PI*a)/listaC.size();
				double cx = Math.cos(angle) * bigCircleRad*1.2;// 1.8 is used for white space borders
				
				double cy =	Math.sin(angle) * bigCircleRad*1.2;
				int m = 0;
				a++;
				sum+=angle;
				circles(nodes, cx, cy, clusterList);
			}
		}
		HecataeusViewer.getActiveViewer().repaint();
		//TODO: FIX THIS
		HecataeusViewer.getActiveViewerZOOM().repaint();
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
		//StopWatch visTimer = new StopWatch();
		//visTimer.start();
		CirclingCusters();
		//visTimer.stop();
		//System.out.println("Visualization TIMER " + visTimer.toString());
		//System.out.print(clusterTimer.toString() + "\t" + visTimer.toString() + "\t");
		//end visualization
		HecataeusViewer.hecMap.createMap();

	}

	@Override
	public void reset() {
		initialize();
	}

}