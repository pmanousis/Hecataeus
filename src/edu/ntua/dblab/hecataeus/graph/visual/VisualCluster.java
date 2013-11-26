package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class VisualCluster {

	private double rad;
	private ArrayList<VisualNode> relations;
	private ArrayList<VisualNode> views;
	private ArrayList<VisualNode> queries;
	private double cx;
	private double cy;
	private int id;
	
	private int edgeCrossCluster = 0;
	
	public VisualCluster(double r, ArrayList<VisualNode> rn, ArrayList<VisualNode>vn, ArrayList<VisualNode>qn, double x, double y, int id){
		this.rad = r;
		this.relations = new ArrayList<VisualNode>(rn);
		this.views = new ArrayList<VisualNode>(vn);
		this.queries = new ArrayList<VisualNode>(qn);
		this.cx = x;
		this.cy = y;
		this.id = id;
	}
	
	protected double getClusterRad(){
		return this.rad;
	}
	
	protected ArrayList<VisualNode> getRelationsInCluster(){
		return this.relations;
	}
	
	protected ArrayList<VisualNode> getViewsInCluster(){
		return this.views;
	}
	
	protected ArrayList<VisualNode> getQueriesInCluster(){
		return this.queries;
	}
	
	protected double getCenterXOfCluster(){
		return this.cx;
	}
	
	protected double getCenterYOfCluster(){
		return this.cy;
	}
	
	protected void printClusterData(){
		
	}
	
	protected void printInClusterEdges(){

		ArrayList<MyPair> myEdges = new ArrayList<MyPair>();
		
		
	//	System.out.println("Cluster id " + this.id);
		int arrCnt = 0;
		// edge :  query to relation
//		System.out.println("Edge ;  query to relation");
		for(VisualNode v : this.queries){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : edges){
				if(this.relations.contains(e.getToNode())){
//					System.out.println(v.getName() +"; x ; " + v.getLocation().getX() +  "; y  ; "+ v.getLocation().getY() +"; : ;" +e.getToNode().getName()+ "; x ; " +e.getToNode().getLocation().getX()+ "; y ; " +e.getToNode().getLocation().getY());
					MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
					myEdges.add(p);
					
//					myArray[arrCnt][0] = v.getLocation();
//					myArray[arrCnt][1] = e.getToNode().getLocation();
//					arrCnt++;
				}
			}
		}
		
		
		// edge :  query to view
		if(this.views!= null){
//			System.out.println("Edge ;  query to view");
			for(VisualNode v : this.queries){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
				for(VisualEdge e : edges){
					if(this.views.contains(e.getToNode())){
//						System.out.println(v.getName() +"; x ;" + v.getLocation().getX() +  "; y  ; "+ v.getLocation().getY() +"; : ;" +e.getToNode().getName()+ "; x ; " +e.getToNode().getLocation().getX()+ "; y ; " +e.getToNode().getLocation().getY());				
						MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
						myEdges.add(p);
					}
				}
			}
		}
		
		
		// edge :  query to view
		if(this.views!= null){
//			System.out.println("Edge ;  view to relation");
			for(VisualNode v : this.views){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
				for(VisualEdge e : edges){
					if(this.relations.contains(e.getToNode())){
//						System.out.println(v.getName() +"; x ; " + v.getLocation().getX() +  "; y  ; "+ v.getLocation().getY() +"; : ;" +e.getToNode().getName()+ "; x ; " +e.getToNode().getLocation().getX()+ "; y ; " +e.getToNode().getLocation().getY());
						MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
						myEdges.add(p);
					}
				}
			}
			
			
			//view to view
			for(VisualNode v : this.views){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
				for(VisualEdge e : edges){
					if(this.views.subList(views.indexOf(v), views.size()) .contains(e.getToNode())){
//						System.out.println(v.getName() +"; x ; " + v.getLocation().getX() +  "; y  ; "+ v.getLocation().getY() +"; : ;" +e.getToNode().getName()+ "; x ; " +e.getToNode().getLocation().getX()+ "; y ; " +e.getToNode().getLocation().getY());
						MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
						myEdges.add(p);
					}
				}
			}
		}
		for(int i = 0; i < myEdges.size(); i++){
			Line2D line1 = new Line2D.Double(myEdges.get(i).getFirstPoint(), myEdges.get(i).getSecondPoint());
			for(int j = 0; j < myEdges.size(); j++){
				if(j==i){
					continue;
				}
				Line2D line2 = new Line2D.Double(myEdges.get(j).getFirstPoint(), myEdges.get(j).getSecondPoint());
				boolean result = line2.intersectsLine(line1);
				if(result && (myEdges.get(i).getSecondPoint()!= myEdges.get(j).getSecondPoint()) && (myEdges.get(i).getFirstPoint()!= myEdges.get(j).getFirstPoint())){
					edgeCrossCluster++;
				}
				
			}
		}
		
		System.out.println((this.edgeCrossCluster/2));
	}
	
	public int getInterClusterCrossings(){
		
		return this.edgeCrossCluster/2;
	}
	
}
