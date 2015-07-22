package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


/**
 * @author eva
 * calculates cluster data
 * calculates labels for clusters
 * calculates the number of edges inside a cluster and their crossings
 * calculates edge length in a cluster
 */
public class VisualCluster {
	private ArrayList<VisualNode> relations;
	private ArrayList<VisualNode> views;
	private ArrayList<VisualNode> queries;
	private double lineLenght = 0;
	private int edgeCrossCluster = 0;
	
	public VisualCluster(){
	}
	
	public VisualCluster(double r, ArrayList<VisualNode> rn, ArrayList<VisualNode>vn, ArrayList<VisualNode>qn, double x, double y, int id){
		this.relations = new ArrayList<VisualNode>(rn);
		this.views = new ArrayList<VisualNode>(vn);
		this.queries = new ArrayList<VisualNode>(qn);
	}
	
	public ArrayList<VisualNode> getRelationsInCluster(){
		return this.relations;
	}
	
	public ArrayList<VisualNode> getViewsInCluster(){
		return this.views;
	}
	
	public ArrayList<VisualNode> getQueriesInCluster(){
		return this.queries;
	}
	
	protected void printInClusterEdges(){
		ArrayList<MyPair> myEdges = new ArrayList<MyPair>();
		// edge :  query to relation
		for(VisualNode v : this.queries){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : edges){
				if(this.relations.contains(e.getToNode())){
					MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
					myEdges.add(p);
				}
			}
		}
		// edge :  query to view
		if(this.views!= null){
			for(VisualNode v : this.queries){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
				for(VisualEdge e : edges){
					if(this.views.contains(e.getToNode())){
						MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
						myEdges.add(p);
					}
				}
			}
		}
		// edge :  view to relation
		if(this.views!= null){
			for(VisualNode v : this.views){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
				for(VisualEdge e : edges){
					if(this.relations.contains(e.getToNode())){
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
						MyPair p= new MyPair(v.getLocation(), e.getToNode().getLocation());
						myEdges.add(p);
					}
				}
			}
		}
		for(int i = 0; i < myEdges.size(); i++){
			Line2D line1 = new Line2D.Double(myEdges.get(i).getFirstPoint(), myEdges.get(i).getSecondPoint());
			Point2D fp = myEdges.get(i).getFirstPoint();
			Point2D sp = myEdges.get(i).getSecondPoint();
			lineLenght += fp.distance(sp);
			for(int j = 0; j < myEdges.size(); j++){
				if(j==i){
					continue;
				}
				Line2D line2 = new Line2D.Double(myEdges.get(j).getFirstPoint(), myEdges.get(j).getSecondPoint());
				Point2D fp2 = myEdges.get(j).getFirstPoint();
				Point2D sp2 = myEdges.get(j).getSecondPoint();
				lineLenght += fp2.distance(sp2);
				boolean result = line2.intersectsLine(line1);
				if(result && (myEdges.get(i).getSecondPoint()!= myEdges.get(j).getSecondPoint()) && (myEdges.get(i).getFirstPoint()!= myEdges.get(j).getFirstPoint())){
					edgeCrossCluster++;
				}
				
			}
		}
		lineLenght = lineLenght/myEdges.size();
		this.lineLenght = this.lineLenght/2;
		this.edgeCrossCluster = this.edgeCrossCluster/2;
	}
}
