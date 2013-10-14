package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;

public class VisualStarLayout extends VisualCircleLayout{

	protected double endC;
	protected VisualGraph graph;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	
	
	protected List<String> files;
	private List<VisualNode> RQV;
	
	protected VisualCircleLayout vcl;
	
	protected VisualStarLayout(VisualGraph g,  double endC) {
		super(g);
		this.graph = g;
		this.endC = endC;
		vcl = new VisualCircleLayout(this.graph);
		this.queries = new ArrayList<VisualNode>(vcl.queries);
		this.relations = new ArrayList<VisualNode>(vcl.relations);
		this.views = new ArrayList<VisualNode>(vcl.views);
		
		this.files = new ArrayList<String>(vcl.files);
		this.RQV = new ArrayList<VisualNode>(vcl.RQV);
	}
	
	
	private void star(){
		
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();       //lista me ta clusters 
		ArrayList<ArrayList<VisualNode>> V = new ArrayList<ArrayList<VisualNode>>();   // tin xrisimopoio gia na anakatevw tin vertices gia na min einai olla ta megala cluster mazi
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
			Collections.sort(vertices, new ListComparator());
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
		int a = 0;double angle = 0.0, sum = 0.0;
		double bigClusterRad = getSmallRad(V.get(V.size()-1));
		angle = (2 * Math.PI) / V.size();
		for(ArrayList<VisualNode> lista : V){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			double cx = Math.cos(sum+angle/2) * myRad*1.8;// 1.8 is used for white space borders
			double cy =	Math.sin(sum+angle/2) * myRad*1.8;
			int m = 0;
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx + m, cy);
			System.out.println("Node name    " + lista.get(0).getName()  + "   cx:    " +cx + " cy: " +cy+ " my angle: " +angle );
			sum+=angle;
			double angleS = (Math.PI) / V.get(V.size()-1).size();
			drawclusters(nodes, cx, cy, 2*bigClusterRad, sum+angle/2, angleS);
			a++;
		}
		
	}
	
	private void drawclusters(List<VisualNode> nodes, double cx, double cy, double bcr, double angle , double angleS){
		int b = 0;
	//	double angleS = (Math.PI) / nodes.size();
		
		for(int i = 1; i < nodes.size(); i++){
			double circleCenterX, circleCenterY;
			//if(cx<0 && cy>0){
				circleCenterX =  cx + bcr*Math.cos(angle);
				circleCenterY = cy + bcr*Math.sin(angle);
				
//			}else if(cx < 0 && cy < 0){
//				circleCenterX =  cx;
//				circleCenterY = cy - bcr;
//			}else if(cx > 0 && cy < 0){
//				circleCenterX =  cx;
//				circleCenterY = cy - bcr;
//			}else {
//				circleCenterX =  cx + bcr;
//				circleCenterY = cy;
//			}
			
			double x = circleCenterX+(Math.cos(angleS*b)*bcr);
			double y = circleCenterY+(Math.sin(angleS*b)*bcr);
			
			
//			double x = cx+Math.cos(angle)*nodes.size(); 
//			double y = cy+Math.sin(angle)*10*nodes.size();
			
			
			
			
//			double x = cx+(2*getSmallRad(nodes)*Math.pow(Math.sin(angle), 2));
//			double y = cy+(2*getSmallRad(nodes)*Math.pow(Math.cos(angle), 2)*Math.tan(angle));
//			cx = x; cy = y;
			Point2D coord = transform(nodes.get(i));
			coord.setLocation(x, y);
			
			System.out.println( "   x:    " +x + " y: " +y+ " my angle: " +angle );
			b++;
		}
	}
	
	
	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		star();
	}

	@Override
	public void reset() {
		initialize();
		
	}

}
