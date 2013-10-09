package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.map.LazyMap;
import org.apache.commons.lang3.StringUtils;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;





public class VisualCircleClusteredLayout extends VisualCircleLayout {
	
	public enum ClusterE{
		Queries,
		Views,
		Relations,
		Circle;
	}
	
	protected ClusterE clusterType;
	protected VisualGraph graph;

	private List<String> files = new ArrayList<String>();
	
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();

	
	private List<VisualNode> RQV = new ArrayList<VisualNode>();
	

	
	private ClusterSet cs;
	
	private ArrayList<ArrayList<VisualNode>> vertices;

	protected VisualCircleLayout vcl;

	
	public VisualCircleClusteredLayout(VisualGraph g, ClusterE cluster){
		super(g);
		this.graph = g;
		this.clusterType = cluster;
		
		vcl = new VisualCircleLayout(this.graph);
		
		queries = new ArrayList<VisualNode>(vcl.queries);
		relations = new ArrayList<VisualNode>(vcl.relations);
		views = new ArrayList<VisualNode>(vcl.views);
		
		RQV = new ArrayList<VisualNode>(vcl.RQV);
		files = new ArrayList<String>(vcl.files);
		
	}
	

	@Override
	public void initialize() {

		HAggloEngine engine = new HAggloEngine(this.graph); 
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(1);
		
		
		switch (this.clusterType) {
		case Queries:
			clusterQueries();
			break;
		case Views:
			clusterViews();
			break;
		case Relations:
			clusterRelations();
			break;
		default:
			whatever();
		}
		
	}

	private void whatever() {
		System.out.println("WTF");
		
	}

	
	private void clusterRelations() {
		// TODO Auto-generated method stub
		//omokentroi kikloi
		
		
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}


		double width = 0.0;
		double height = 0.0;
		double myRad = 0.0;
		Dimension d = getSize();
		height = d.getHeight();
		width = d.getWidth();
		myRad = 0.45 * (height < width ? height/2 : width/2);

		int a = 0;
		for(ArrayList<VisualNode> lista : vertices){
			
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);

			
			double angle = (2 * Math.PI )/ vertices.size();
			
			
			
			double cx = Math.cos(angle*a) * myRad;// + width / 2;
			double cy =	Math.sin(angle*a) * myRad;// + height/2;

			int b = 0, c =0;
			if(nodes.size() <= 15){
				for(VisualNode v : nodes){
					double smallRad = nodes.size()*2;
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					
					coord.setLocation(Math.cos(angleA*c+(cx))*smallRad + (a*nodes.size()),Math.sin(angleA*c+(cy))*smallRad +(a*nodes.size()));
					c++;
				}
			}else{
			for(VisualNode v : nodes){
					double smallRad = a*(nodes.size()/2);
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					
					coord.setLocation(Math.cos(angleA*b)*smallRad,Math.sin(angleA*b)*smallRad);
				b++;
			}
		}
			a++;
		}
	}

	private void clusterViews() {
	}

	private void clusterQueries() {
		
		
		
		
		
	}

	
	
	private double checkRad(ArrayList<ArrayList<VisualNode>> SoC, double myRad){
		double tempRad = 0;double ccircleR = 0;
		for(ArrayList<VisualNode>listaC: SoC){
			tempRad += getSmallRad(listaC);
		}	
			ccircleR = 2*tempRad;
			if(ccircleR>=2*Math.PI*myRad){
				System.out.println("AKTINA MEGALOU KIKLOY  "+(ccircleR/2*Math.PI));
				return (ccircleR/2*Math.PI);
			}
			else{
				System.out.println("aktina megalou kiklou"+myRad);
				return myRad;
			}
	}
	

	
	private void circles(List<VisualNode> nodes, double cx, double cy){
		int b = 0;
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				
				double smallRad = getSmallRad(relationsInCluster(nodes));
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / relationsInCluster(nodes).size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();
			}else{
				double smallRad = getSmallRad(nodes);
				Point2D coord = transform(v);
				double angleA = 0.0;
				if(relationsInCluster(nodes).size() > 1){
					angleA = (2 * Math.PI ) / (nodes.size()-relationsInCluster(nodes).size());
				}else{
					angleA = (2 * Math.PI ) / nodes.size();
				}
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();
			}
			b++;
		}
		
	}
	
	
	
	
	private void old(){
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		
//		System.out.println(vertices);

		double width = 0.0;
		double height = 0.0;
		double myRad = 0.0;
		Dimension d = getSize();
		height = d.getHeight();
		width = d.getWidth();
//		relationRadius = 0.45 * (height < width ? height/3 : width/3);
//		queryRadius = 0.45 * (height < width ? queries.size()/3 : queries.size()/3);
		myRad = 0.45 * (height < width ? height/2 : width/2);

		
		int eva = 0;
		int a = 0;
		for(ArrayList<VisualNode> lista : vertices){
			
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
//			System.out.println(nodes);
			
			double angle = (2 * Math.PI )/ vertices.size();
			
			myRad = Math.exp(a+1);
			
			double cx = Math.cos(angle*a) * myRad;// + width / 2;
			double cy =	Math.sin(angle*a) * myRad;// + height/2;
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);
			System.out.println("  node   " + nodes.get(0).getType()) ;
			int yloc = 100;
			int b = 0;
			for(VisualNode v : nodes){
				
				double smallRad = 0.45 * nodes.size();
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / nodes.size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));

			//	HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
			//	HecataeusViewer.vv.repaint();					
				b++;
			}
			a++;
		}
	}

	@Override
	public void reset() {
		initialize();
		
	}

}
