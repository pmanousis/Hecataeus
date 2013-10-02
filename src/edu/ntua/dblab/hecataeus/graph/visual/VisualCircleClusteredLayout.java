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

import com.panayotis.gnuplot.JavaPlot;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;

public class VisualCircleClusteredLayout extends AbstractLayout<VisualNode,VisualEdge> {
	
	public enum ClusterE{
		Queries,
		Views,
		Relations,
		Circle;
	}
	
	protected ClusterE clusterType;
	protected VisualGraph graph;
	
	private static int pos = 100;
	
	public static List<String> files = new ArrayList<String>();
	
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
			
//			System.out.println("NAME       "+ v.getName() +"    CATEGORY   " + v.getType().getCategory());
			if(v.getType().getCategory() == NodeCategory.MODULE ){
				
			
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
			
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
//						System.out.println("********* NAME       "+ e.getFromNode().getName() +"    CATEGORY   " + e.getFromNode().getType().getCategory());
						if(files.contains(e.getFromNode().getName())==false)
						{
							files.add(e.getFromNode().getName());
						}
						
					}
				}
			}
//			System.out.println(files);
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				relations.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
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
		case Circle:
			clustersOnaCircle();
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
//			System.out.println(nodes);
			
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
							
//							System.out.println("  node   " + v.getType()) ;
							
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
					
//					System.out.println("  node   " + v.getType()) ;
					
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
		

		
		
		
	}

	private void clusterQueries() {
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
		Collections.sort(vertices, new ListComparator());
		sortedV.addAll(vertices);
		
		double width = 0.0;
		double height = 0.0;
		double myRad = 0.0;
		Dimension d = getSize();
		height = d.getHeight();
		width = d.getWidth();
//		relationRadius = 0.45 * (height < width ? height/3 : width/3);
//		queryRadius = 0.45 * (height < width ? queries.size()/3 : queries.size()/3);
//		myRad = 0.45 * (height < width ? height/2 : width/2);

		myRad = 1;
		
		ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
		while((int) Math.pow(2, myRad)<sortedV.size())
		{
			ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), (int) Math.pow(2, myRad)));
			sublistofClusters.add(tmpVl);
			myRad++;
		}
		ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), sortedV.size()));
		sublistofClusters.add(tmpVl);
		

		int a = 0;

		double bigCircleRad = 0.0;
		double bigClusterRad = 0.0;
		System.out.println(sublistofClusters);
		for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
			ArrayList<ArrayList<VisualNode>> tmp ;
			if (sublistofClusters.indexOf(listaC)!=sublistofClusters.size()-1){
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)+1));
			}
			else{
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)));	
			}
			int bigClusterSize = tmp.get(tmp.size()-1).size();
			
			bigClusterRad += getClusterRadius(bigClusterSize);
			
			bigCircleRad = checkRad(listaC, bigClusterRad)*1.3;
			System.out.println("TELIKI aktina megalou kiklou"+bigCircleRad);
			for(ArrayList<VisualNode> lista : listaC){
				
				List<VisualNode> nodes = new ArrayList<VisualNode>();
				Collections.sort(lista, new CustomComparator());
				nodes.addAll(lista);
				
				double angle = (2 * Math.PI )/ listaC.size();
				
				double cx = Math.cos(angle*a) * bigCircleRad;// + width / 2;
				double cy =	Math.sin(angle*a) * bigCircleRad;// + height/2;
				CircleVertexData data;
				Point2D coord1 = transform(nodes.get(0));
				coord1.setLocation(cx, cy);
				data = getCircleData(nodes.get(0));
				data.setAngle(angle);
	
				int b = 0;double smallRad = 0;
				for(VisualNode v : nodes){
					if(b != 0){
						smallRad = getClusterRadius(nodes.size());
						Point2D coord = transform(v);
						double angleA = (2 * Math.PI ) / nodes.size();
						coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
						data = getCircleData(v);
						data.setAngle(angleA);
			//			HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
										
					}
					b++;
				}
				a++;
			}
		}
		ScalingControl scaler = new CrossoverScalingControl();
		scaler.scale(HecataeusViewer.vv, 1.1f, HecataeusViewer.vv.getCenter());
		//HecataeusViewer.vv.repaint();
	}

	private double getClusterRadius(int nodes){
		if(nodes <= 10 && nodes >=4){
			return 5 * nodes;
		}
		else{
			return 3 * nodes;
		}
	}
	
	private double checkRad(ArrayList<ArrayList<VisualNode>> SoC, double myRad){
		double tempRad = 0;double ccircleR = 0;
		for(ArrayList<VisualNode>listaC: SoC){
			tempRad += getClusterRadius(listaC.size());
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
	
	private void clustersOnaCircle(){
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
//		relationRadius = 0.45 * (height < width ? height/3 : width/3);
//		queryRadius = 0.45 * (height < width ? queries.size()/3 : queries.size()/3);
		myRad = 0.45 * (height < width ? height*2 : width*2);
//		double circle = 0;
//		for(ArrayList<VisualNode> lista : vertices){
//			List<VisualNode> nodes = new ArrayList<VisualNode>();
//			Collections.sort(lista, new CustomComparator());
//			nodes.addAll(lista);
//			circle += 2*(nodes.size()*1.3);
//			
//		}
//		if(circle <= 2*Math.PI*myRad){
//			myRad = circle/(2*Math.PI);
//		}
		double diametros = 0;
		int a = 0;
		double [][] evatest = new double [vertices.size()][2];
		for(ArrayList<VisualNode> lista : vertices){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
//			System.out.println(nodes);
//			if(a==0){
//				double angle = (2 * Math.PI )/vertices.size();
//				double cx = Math.cos(angle* a) * myRad;// + width / 2;
//				double cy =	Math.sin(angle* a) * myRad;// + height/2;
//			}
			
		
			
			
			diametros = 2*(nodes.size()*2);
//			double angle = (2 * Math.PI)-((2 * Math.PI)-diametros);
			double angle = 2*Math.pow((Math.cos((diametros/2)/myRad)), -1);
			double cx = Math.cos(angle*a) * myRad;
			double cy =	Math.sin(angle*a) * myRad;
			CircleVertexData data;
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);
	//		data = getCircleData(nodes.get(0));
	//		data.setAngle(angle);
			System.out.println(cx + "  " +cy+ " my angle  " +angle);
			evatest[a][0] = cx;
			evatest[a][1] = cy;
			
			
//			System.out.println("  node   " + nodes.get(0).getType()) ;
			int b = 0;
			for(VisualNode v : nodes){
				if(b != 0){
					double smallRad = nodes.size()*2;
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
			//		data = getCircleData(v);
			//		data.setAngle(angleA);
					HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));

				}
				b++;
			}
			a++;
		}
		JavaPlot p = new JavaPlot();
        p.addPlot(evatest);
        
        p.plot();
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
			CircleVertexData data;
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);
			data = getCircleData(nodes.get(0));
			data.setAngle(angle);
			System.out.println("  node   " + nodes.get(0).getType()) ;
			int yloc = 100;
			int b = 0;
			for(VisualNode v : nodes){
				
				double smallRad = 0.45 * nodes.size();
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / nodes.size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));

					
				data = getCircleData(v);
				data.setAngle(angleA);
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();					
				b++;
			}
			a++;
		}
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
