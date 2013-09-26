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
import java.util.Comparator;
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
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNewCircleLayout.CircleVertexData;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualCircleClusteredLayout extends AbstractLayout<VisualNode,VisualEdge> {
	
	public enum ClusterE{
		Queries,
		Views,
		Relations;
	}
	
	protected ClusterE clusterType;
	protected VisualGraph graph;
	
	
	public static List<VisualNode> files = new ArrayList<VisualNode>();
	
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	private List<VisualNode> wtf = new ArrayList<VisualNode>();
	private List<VisualNode> nodes;
	
	private ClusterSet cs;
	
	private ArrayList<ArrayList<VisualNode>> vertices;

	private int[][] dist;
	
	Map<VisualNode, CircleVertexData> circleVertexDataMap =
			LazyMap.decorate(new HashMap<VisualNode,CircleVertexData>(), 
			new Factory<CircleVertexData>() {
				public CircleVertexData create() {
					return new CircleVertexData();
				}});	
	
	
	public VisualCircleClusteredLayout(VisualGraph g, ClusterE cluster){
		super(g);
		this.graph = g;
		this.clusterType = cluster;
		
		nodes = new ArrayList<VisualNode>((Collection<? extends VisualNode>) g.getVertices());
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				System.out.println("  node       "+ v.getName() +"should be RELATION   " + v.getType()) ;
				relations.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_FILE){
				files.add(v);
			}
		}
	}
	

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
		
		for(VisualNode v : relations){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
			for(VisualEdge e : edges){
				if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY){
					if(wtf.contains(v)==false){
						wtf.add(v);
					}
				}
			}
			
		}
		//Create distance matrix
		dist = new int[wtf.size()][queries.size()];
		int pos = 0;
		int j = 0;
		for(VisualNode v : queries){
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : outE){
				if(e.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					VisualNode r = e.getToNode();
					int i = 0;
					for(VisualNode n : wtf){
						if(r.equals(n)){
							pos = i;
						}
						i++;
					}
					dist[pos][j] = 1;
				}
			}
			j++;
		}
		for(int i = 0; i < wtf.size(); i++){
			for(int k = 0; k < queries.size(); k++){
				if(dist[i][k] != 1){
					dist[i][k] = 0;
				}
			}
		}
		String tableNames = StringUtils.strip(wtf.toString(), "[");
		tableNames = StringUtils.strip(tableNames, "]");
		
		String qn = StringUtils.strip(queries.toString(), "[");
		qn = StringUtils.strip(qn, "]");
		
		try {
			 
			String content = 	"%STRICT FORMAT \n " +
								"% TABLES = number of tables \n" +
								"% TableNames = list of tables \n " +
								"% QUERIES = number of queries -- always AFTER the tables \n " +
								"% \n" +
								"% 0 or 1,... (as many lines as necessary with 0, 1, and commata-followedBy-space)\n" +
								"% \n" +
								"% NOTES: \n" +
								"% all commata are followed by space obligatorily\n" +
								"% Comments are lines starting with % followed by white space\n" +
								"% No line starting with white space is taken into account\n" +
								
								"TABLES = " + wtf.size() + "\n" +
								"TableNames = " + tableNames + "\n"+
								"QUERIES = "+ queries.size() + "\n"+
								"QueryNames = "+ qn + "\n\n"+
								"% TABLES_X_QUERIES MATRIX \n";
 
			 
			for(int i = 0; i < wtf.size(); i++){
				for(int k = 0; k < queries.size(); k++){
					if(k == 0){
						content += dist[i][k];
					}
					else{
						content += ", " +dist[i][k];
					}
				}				
				content += "\n";
			}
			
			File file = new File("/home/eva/clusters/test.ascii");
 
			// if file doesnt exist create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HAggloEngine engine = new HAggloEngine(this.graph); 			   
		engine.executeParser();
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

		int a = 0;
		for(ArrayList<VisualNode> lista : vertices){
			
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			System.out.println(nodes);
			
			double angle = (2 * Math.PI )/ vertices.size();
			
			
			
			double cx = Math.cos(angle*a) * myRad;// + width / 2;
			double cy =	Math.sin(angle*a) * myRad;// + height/2;
			CircleVertexData data;
	//		Point2D coord1 = transform(nodes.get(0));
	//		coord1.setLocation(cx, cy);
	//		data = getCircleData(nodes.get(0));
	//		data.setAngle(angle);
	//		System.out.println("  node   " + nodes.get(0).getType()) ;
			int b = 0, c =0;
			if(nodes.size() <= 15){
				for(VisualNode v : nodes){
					//	if(b != 0){
							double smallRad = nodes.size()*2;
							Point2D coord = transform(v);
							double angleA = (2 * Math.PI ) / nodes.size();
							
							coord.setLocation(Math.cos(angleA*c+(cx))*smallRad + (a*nodes.size()),Math.sin(angleA*c+(cy))*smallRad +(a*nodes.size()));
							
							System.out.println("  node   " + v.getType()) ;
							
							data = getCircleData(v);
							data.setAngle(angleA);
					//		HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v));
							
					//	}
						c++;
					}
			}else{
			for(VisualNode v : nodes){
			//	if(b != 0){
					double smallRad = a*(nodes.size()/2);
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					
					coord.setLocation(Math.cos(angleA*b)*smallRad,Math.sin(angleA*b)*smallRad);
					
					System.out.println("  node   " + v.getType()) ;
					
					data = getCircleData(v);
					data.setAngle(angleA);
			//		HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v));
					
			//	}
				b++;
			}
		}
			a++;
		}
	}

	private void clusterViews() {
		// TODO Auto-generated method stub
		
	}

	private void clusterQueries() {
		
		
		

		
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

		
		
		int a = 0;
		for(ArrayList<VisualNode> lista : vertices){
			
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			System.out.println(nodes);
			
			double angle = (2 * Math.PI )/ vertices.size();
			
			
			
			double cx = Math.cos(angle*a) * myRad;// + width / 2;
			double cy =	Math.sin(angle*a) * myRad;// + height/2;
			CircleVertexData data;
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);
			data = getCircleData(nodes.get(0));
			data.setAngle(angle);
			System.out.println("  node   " + nodes.get(0).getType()) ;
			int b = 0;
			for(VisualNode v : nodes){
				if(b != 0){
					double smallRad = 0.45 * nodes.size();
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					if(v.getType() == NodeType.NODE_TYPE_RELATION){
						coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
					}
					else{
						coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
					}
					System.out.println("  node   " + v.getType()) ;
					
					data = getCircleData(v);
					data.setAngle(angleA);
					HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v));
					
				}
				b++;
			}
			a++;
		}
			
		
		
		
		
		
//		int i = 0;
//		for(VisualNode v : relations){
//			Point2D coord = transform(v);				
//			double angle = (2 * Math.PI * j) / relations.size();
//			coord.setLocation(Math.cos(angle) * relationRadius + width / 2,
//					Math.sin(angle) * relationRadius + height / 2);
//			CircleVertexData data = getCircleData(v);
//			data.setAngle(angle);
////			dosomething(Math.cos(angle) * relationRadius + width / 2, Math.sin(angle) * relationRadius + height / 2, (VisualNode)v, 0);
//			j++;
//		}
//		
//		int z = 0;
//		for (VisualNode v : queries){
//			Point2D coord = transform(v);				
//			double angle = (2 * Math.PI * z) / queries.size();
//			coord.setLocation(Math.cos(angle) * queryRadius + width / 2,
//					Math.sin(angle) * queryRadius + height / 2);
//			CircleVertexData data = getCircleData(v);
//			data.setAngle(angle);				
////			dosomething(Math.cos(angle) * queryRadius + width / 2, Math.sin(angle) * queryRadius + height / 2, (VisualNode)v, 0);
//			z++;
//		}
		
//		for(ArrayList<VisualNode> lista : vertices){
//			myRad = 0.45 * (height < width ? height*lista.size() : width*lista.size());
//			myRad = myRad/10.0;
//			System.out.println(myRad);
//			double center = 0.0;
////			if(lista.size()>1){
//				int h = 0;
//				for (VisualNode v : (List<VisualNode>)lista){
//					Point2D coord = transform(v);				
//					double angle = (2 * Math.PI * h) / lista.size();
//					coord.setLocation(Math.cos(angle) * myRad + width / 2,
//							Math.sin(angle) * myRad + height / 2);
//					CircleVertexData data = getCircleData(v);
//					data.setAngle(angle);
//					h++;
//				}
//				
//				
//				
////			}else{
////				myRad = 0.45 * (height < width ? height*lista.size() : width*lista.size());
////				int metritis = 0;
////				for(VisualNode v : lista){
////					Point2D coord = transform(v);				
////					double angle = (2 * Math.PI * metritis) / lista.size();
////					coord.setLocation(Math.cos(angle) * myRad/5 + width / 2, Math.sin(angle) * myRad/5 + height / 2);
////					CircleVertexData data = getCircleData(v);
////					data.setAngle(angle);
////					metritis++;
////				}	
////			
////			
////			}
//		}


		
	}

//	protected void dosomething(double x, double y, VisualNode node, int mode){
//		int a = 0;
//
//		ArrayList<VisualNode> sem = new ArrayList<VisualNode>();
//		int i = 0;
//		for(ArrayList<VisualNode> lista : vertices){
//			System.out.println("############## " + lista);
//			if(lista.contains(node.getName())&& lista.size()>1){
//				System.out.println("!!!!!!!!!!!!!!!!!!!!!  ");
//				sem.add(lista.get(i));
//			}
//			i++;
//		}
//		
//		
//		for (VisualNode v : sem){
//			Point2D coord = transform(v);
//			double angle = (2 * Math.PI * a) / sem.size();
//			if(mode == 0){
//				coord.setLocation(Math.cos(angle) * 40 +x, Math.sin(angle) * 40+ y);
//			}
//			else{
//				coord.setLocation(Math.cos(angle) * 65 +x, Math.sin(angle) * 65+ y);
//			}
//			CircleVertexData data = getCircleData(v);
//			data.setAngle(angle);
//			dosomething(x, y, (VisualNode)v,1);
//			a++;
//		}
//	}
//	
//	
//	public VisualNode getVNode (String name){
//		for(VisualNode v : this.graph.getVertices()){
//			if(name.contentEquals(v.getName())){
//				return v;
//			}
//		}
//		return null;
//	}
	
	@Override
	public void reset() {
		initialize();
		
	}

	protected CircleVertexData getCircleData(VisualNode v) {
		return circleVertexDataMap.get(v);
	}

	protected static class CircleVertexData {
		private double angle;

		protected double getAngle() {
			return angle;
		}

		protected void setAngle(double angle) {
			this.angle = angle;
		}

		@Override
		public String toString() {
			return "CircleVertexData: angle=" + angle;
		}
	}
}
