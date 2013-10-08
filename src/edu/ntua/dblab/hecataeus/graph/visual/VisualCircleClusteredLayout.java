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

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNewCircleLayout.CircleVertexData;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
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
	
	private List<VisualNode> RQV = new ArrayList<VisualNode>();
	
	private List<VisualNode> nodes;
	
	private ClusterSet cs;
	
	private ArrayList<ArrayList<VisualNode>> vertices;

	private int[][] distRQ;private int[][] distRV;private int[][] distVQ;
	
	private String content;
	
//	private VisualGraph testG = new VisualGraph();
	
	
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
			
			if(v.getType().getCategory() == NodeCategory.MODULE ){
				
			
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
			
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
						if(files.contains(e.getFromNode().getName())==false)
						{
							files.add(e.getFromNode().getName());
						}
						
					}
				}
			}
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
		
		for(VisualNode r : relations){
			for(int i =0; i < r.getInEdges().size(); i++){
				if(r.getInEdges().get(i).getType()== EdgeType.EDGE_TYPE_USES)
				{
//					if(testG.containsVertex(r)==false)
//					{
//						testG.addVertex(r);
//					}
					
					if(RQV.contains(r) == false){
						RQV.add(r);
					}
				}
			}
		}

		for(VisualNode q: queries)
		{
//			testG.addVertex(q);
			RQV.add(q);
		}

		for(VisualNode v:views)
		{
			if(RQV.contains(v)==false)
			{
//				testG.addVertex(v);
				RQV.add(v);
			}
		}
	}
	

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
		
		for(VisualNode v : relations){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
			for(VisualEdge e : edges){
		//		if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY){
				if(e.getType() == EdgeType.EDGE_TYPE_USES){
					if(wtf.contains(v)==false){
						wtf.add(v);
					}
				}
			}
			
		}
		
		
		
		
//		System.out.println(edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations.graphToSparseMatrix(testG));
		
//		SparseDoubleMatrix2D adjMatrixofSubgraph = edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations.graphToSparseMatrix(testG);

		
		
		String eva = " \n";
		
//		System.out.println("RELATIONS   " +  wtf.size() + "  VIEWS   "+ views.size() + "  QUERIES   " + queries.size());
		
		
		System.out.println("RELATIONS   " +  wtf);
		
		System.out.println("  VIEWS   "+ views);
		
		System.out.println("  QUERIES   " + queries);
		
		
		int [][] matrix = createAdjMatrix();
		
		
		
		
		for(int i = 0; i < RQV.size(); i++){
			for(int j = 0; j < RQV.size(); j++){
				eva += matrix[i][j] + ",";
			}
			eva = eva.substring(0, eva.length()-1);
			eva += "\n";
		}
		
//		System.out.println(eva);
		
		
//		distRQ = createDistanceMatix(wtf, queries, NodeType.NODE_TYPE_RELATION);     // Distance relations - queries
//		
//		if(views.size()>0){
//			distRV = createDistanceMatix(wtf, views, NodeType.NODE_TYPE_RELATION);     // Distance relations - views
//			distVQ = createDistanceMatix(views, queries, NodeType.NODE_TYPE_VIEW);     // Distance views - queries
//		}
		String tableNames = StringUtils.strip(wtf.toString(), "[");
		tableNames = StringUtils.strip(tableNames, "]");
		String viewNames = StringUtils.strip(views.toString(), "[]");
		String qn = StringUtils.strip(queries.toString(), "[");
		qn = StringUtils.strip(qn, "]");
		
		try {
			 
			content = 	"%STRICT FORMAT \n " +
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
								"VIEWS = " + views.size() + "\n" +
								"ViewNames = " + viewNames + "\n"+
								"QUERIES = "+ queries.size() + "\n"+
								"QueryNames = "+ qn + "\n\n"+
								"% TABLES_X_QUERIES MATRIX \n"+
 
								eva;

			
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
	
	private double getSmallRad(List<VisualNode> komboi){
		return(Math.log(komboi.size()*komboi.size()*komboi.size())+2*komboi.size());
	}
	
	private void clustersOnaCircle(){
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		vertices = new ArrayList<ArrayList<VisualNode>>();       //lista me ta clusters 
		ArrayList<ArrayList<VisualNode>> V = new ArrayList<ArrayList<VisualNode>>();   // tin xrisimopoio gia na anakatevw tin vertices gia na min einai olla ta megala cluster mazi
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
			Collections.shuffle(vertices);
		}
		V.addAll(vertices);
		double myRad = 0.0;
		double RAD = 0;
		//taksinomei tin lista --> prwta ta relations meta ta upoloipa k briskei aktina
		for(ArrayList<VisualNode> lista : vertices){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			RAD += getSmallRad(nodes);
		}
		myRad = RAD/Math.PI;
		double diametros = 0;
		int a = 0;double angle = 0.0, sum = 0.0;
		
		if(clusters.size() < 2){
			Dimension d = getSize();
			double height = d.getHeight();
			double width = d.getWidth();
			
			//for(ArrayList<VisualNode> lista : V){
				int k = 0;
				for(VisualNode v : V.get(0)){
					Point2D coord = transform(v);				
					double angle1 = (2 * Math.PI) / V.get(0).size();
					coord.setLocation(Math.cos(angle1*k) * myRad + width / 2, Math.sin(angle1*k) * myRad + height / 2);k++;
				}
				
			//}
		}
		else{
			for(ArrayList<VisualNode> lista : V){
				List<VisualNode> nodes = new ArrayList<VisualNode>();
				Collections.sort(lista, new CustomComparator());
				nodes.addAll(lista);
				diametros = 2*getSmallRad(nodes);
				angle = (Math.acos(  (2*myRad*myRad - getSmallRad(nodes)*getSmallRad(nodes)*0.94)/(2*myRad*myRad )))*2;   // 0.94 is used simulate strait lines to curves
				double cx = Math.cos(sum+angle/2) * myRad*1.8;// 1.8 is used for white space borders
				double cy =	Math.sin(sum+angle/2) * myRad*1.8;
				int m = 0;
				Point2D coord1 = transform(nodes.get(0));
				coord1.setLocation(cx + m, cy);
				System.out.println("Node name    " + lista.get(0).getName()  + "   cx:    " +cx + " cy: " +cy+ " my angle: " +angle );
				sum+=angle;
				
				circles(nodes, cx, cy);

				a++;
			}
		}
		System.out.println("LOGIKA 2*Math.Pi: "+Math.PI*2+" and it is: " + sum);
	}
	
	private ArrayList<VisualNode> relationsInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> relations = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				relations.add(v);
			}
		}
		return relations;
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
	
	
	
//	
//	private void makeCircle(List<VisualNode> nodes, double cx, double cy){
//		int b = 0; 
//		for(VisualNode v : nodes){
//			if(b != 0){
////				if(v.getType() == NodeType.NODE_TYPE_RELATION){
////					Point2D coord = transform(v);
////					coord.setLocation(cx + m, cy);
////					m+=0.5;
////				}
//				double smallRad = getSmallRad(nodes);
//				Point2D coord = transform(v);
//				double angleA = (2 * Math.PI ) / nodes.size();
//				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
//				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
//				HecataeusViewer.vv.repaint();
//			}
//			b++;
//		}
//		
//	}
	
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
			//	HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
			//	HecataeusViewer.vv.repaint();					
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
	
	
	protected int[][] createAdjMatrix(){
		
		int[][] adj = new int [RQV.size()][RQV.size()];
		int pos = 0, k = 0;
		for(int i = 0; i <RQV.size(); i++){
			VisualNode v = RQV.get(i);
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
			List<VisualEdge> inE = new ArrayList<VisualEdge>(v._inEdges);
			List<VisualEdge> allE = new ArrayList<VisualEdge>();
			allE.addAll(outE);
			allE.addAll(inE);
			for(VisualEdge e : outE){
		//		if(e.getToNode().getType() == NodeType.NODE_TYPE_VIEW || e.getToNode().getType() == NodeType.NODE_TYPE_QUERY || e.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
				if(e.getType() == EdgeType.EDGE_TYPE_USES){	
					VisualNode toNode = e.getToNode();
					k=RQV.indexOf(v);
					pos=RQV.indexOf(toNode);
					adj[pos][k] = 1;
					adj[k][pos] = 1;
				}
			}
		}
		for(int i = 0; i < RQV.size(); i++){
			for(int j = 0; j < RQV.size(); j++){
				if(adj[i][j] != 1){
					adj[i][j] = 0;
				}
			}
		}
		
		return adj;
		
		
	}
	
	
	protected int[][] createDistanceMatix(List<VisualNode> rows, List<VisualNode> cols, NodeType toNodeType){
		int[][] dist;
		
		dist = new int[rows.size()][cols.size()];
		int pos = 0;
		int j = 0;
		for(VisualNode v : cols){
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : outE){
				if(e.getToNode().getType() == toNodeType){     //
					VisualNode r = e.getToNode();
					int i = 0;
					for(VisualNode n : rows){
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
		for(int i = 0; i < rows.size(); i++){
			for(int k = 0; k < cols.size(); k++){
				if(dist[i][k] != 1){
					dist[i][k] = 0;
				}
			}
		}
		
		
		return dist;
	}
	
	
	public void addDMtoString(int[][] dist, List<VisualNode> rows, List<VisualNode> cols){
		for(int i = 0; i < rows.size(); i++){
			for(int k = 0; k < cols.size(); k++){
				if(k == 0){
					content += dist[i][k];
				}
				else{
					content += ", " +dist[i][k];
				}
			}				
			content += "\n";
		}
		
	}
	
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
