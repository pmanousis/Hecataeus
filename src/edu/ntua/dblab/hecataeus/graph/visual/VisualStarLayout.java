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
		this.queries = new ArrayList<VisualNode>(vcl.getQueries());
		this.relations = new ArrayList<VisualNode>(vcl.getRelations());
		this.views = new ArrayList<VisualNode>(vcl.getViews());
		
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
			lista.get(0).setLocation(coord1);
			System.out.println("Node name    " + lista.get(0).getName()  + "   cx:    " +cx + " cy: " +cy+ " my angle: " +angle );
			drawclusters(nodes, cx, cy, 2*bigClusterRad, sum+angle/2 , myRad*1.8);
			sum+=angle;
			a++;
		}
		
	}
	
	private void drawclusters(List<VisualNode> nodes, double cx, double cy, double bcr, double angle, double rad){
		int b = 0;
		double angleS = angle;
		
		for(int i = 1; i < nodes.size(); i++){
			
			rad += getSmallRad(nodes);
			cx = Math.cos(angleS) * rad;// 1.8 is used for white space borders
			cy = Math.sin(angleS) * rad;
			
			angleS+=angleFunc(i, nodes.size());
			
			Point2D coord1 = transform(nodes.get(i));
			nodes.get(i).setLocation(coord1);
			coord1.setLocation(cx, cy);

			b++;
		}
	}
	
	private double angleFunc(int i, int j){
		double nAngle;
		if(i < j/2){
			nAngle = Math.toRadians(5);
		}
		else{
			nAngle = Math.toRadians(5)*(-1);
		}
		return nAngle;
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
