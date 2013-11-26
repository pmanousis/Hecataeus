package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualPizzaSliceClusterLayout extends VisualCircleLayout{

	
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
	
	protected VisualPizzaSliceClusterLayout(VisualGraph g, double endC) {
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

	private void circles(List<VisualNode> nodes, double cx, double cy){
		int b = 0;
		ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
		ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
		ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				rc.add(v);
				double smallRad = 1.8*getSmallRad(relationsInCluster(nodes));
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / relationsInCluster(nodes).size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				v.setLocation(coord);
				HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
				HecataeusViewer.getActiveViewer().repaint();
			}else{
				if(v.getType() == NodeType.NODE_TYPE_QUERY){
					qc.add(v);
				}
				else if(v.getType() == NodeType.NODE_TYPE_VIEW){
					vc.add(v);
				}
				double smallRad = getSmallRad(nodes);
				Point2D coord = transform(v);
				double angleA = 0.0;
				if(relationsInCluster(nodes).size() > 1){
					angleA = (2 * Math.PI ) / (nodes.size()-relationsInCluster(nodes).size());
				}else{
					angleA = (2 * Math.PI ) / nodes.size();
				}
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				v.setLocation(coord);
				HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
				HecataeusViewer.getActiveViewer().repaint();
			}
			b++;
		}
		clusterId++;
		VisualCluster cluster = new VisualCluster(getSmallRad(nodes), rc, vc, qc, cx, cy, clusterId);
		VisualNode clusterNode = new VisualNode("TEST"+clusterId,NodeType.NODE_TYPE_CLUSTER);
		cluster.printInClusterEdges();
	}
	
	private double checkRad(ArrayList<ArrayList<VisualNode>> SoC, double myRad){
		double tempRad = 0;double ccircleR = 0;
		for(ArrayList<VisualNode>listaC: SoC){
			tempRad += getSmallRad(listaC);
		}	
		ccircleR = 2*tempRad;
		if(ccircleR>=2*Math.PI*myRad){
			return (ccircleR/2*Math.PI);
		}
		else{
			return myRad;
		}
	}
	
	
	protected void CirclingCusters(){
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
		Collections.sort(vertices, new ListComparator());
		sortedV.addAll(vertices);
		
		double myRad = 1.0;
		
		ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
		while((int) Math.pow(2, myRad)<sortedV.size()){
			ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), (int) Math.pow(2, myRad)));
			sublistofClusters.add(tmpVl);
			myRad++;
		}
		ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), sortedV.size()));
		sublistofClusters.add(tmpVl);
		

		int a = 0;

		double bigCircleRad = 0.0;
		double bigClusterRad = 0.0;
	//	System.out.println(sublistofClusters);
		for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
			ArrayList<ArrayList<VisualNode>> tmp ;
			if (sublistofClusters.indexOf(listaC)!=sublistofClusters.size()-1){
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)+1));
			}
			else{
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)));	
			}

			bigClusterRad += getSmallRad(tmp.get(tmp.size()-1));
			bigCircleRad = checkRad(listaC, bigClusterRad)*1.3;
		//	System.out.println("TELIKI aktina megalou kiklou"+bigCircleRad);
			double angle = 0.0, sum = 0.0;
			for(ArrayList<VisualNode> lista : listaC){
				
				List<VisualNode> nodes = new ArrayList<VisualNode>();
				Collections.sort(lista, new CustomComparator());
				nodes.addAll(lista);
				angle = (Math.acos(  (2*bigCircleRad*bigCircleRad - getSmallRad(nodes)*getSmallRad(nodes)*0.94)/(2*bigCircleRad*bigCircleRad )))*2;   // 0.94 is used simulate strait lines to curves
				double cx = Math.cos(sum+angle/2) * bigCircleRad*1.8;// 1.8 is used for white space borders
				double cy =	Math.sin(sum+angle/2) * bigCircleRad*1.8;
		//		System.out.println("Node name    " + lista.get(0).getName()  + "   cx:    " +cx + " cy: " +cy+ " my angle: " +angle );
				sum+=angle;
				circles(nodes, cx, cy);
				a++;
				
			}
		}
		
		HecataeusViewer.getActiveViewer().repaint();
	}
	
	
	
	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		
		CirclingCusters();
		
	}

	@Override
	public void reset() {
		initialize();
	}

}

