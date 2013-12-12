package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;




public class VisualCluster {

	private double rad;
	private ArrayList<VisualNode> relations;
	private ArrayList<VisualNode> views;
	private ArrayList<VisualNode> queries;
	private double cx;
	private double cy;
	private int id;
	private double area;
	private double lineLenght = 0;
	private String label;
	
	private int edgeCrossCluster = 0;
	public VisualCluster(){
		
	}
	
	public VisualCluster(double r, ArrayList<VisualNode> rn, ArrayList<VisualNode>vn, ArrayList<VisualNode>qn, double x, double y, int id){
		this.rad = r;
		this.relations = new ArrayList<VisualNode>(rn);
		this.views = new ArrayList<VisualNode>(vn);
		this.queries = new ArrayList<VisualNode>(qn);
		this.cx = x;
		this.cy = y;
		this.id = id;
		this.label = getDominatingRealationsInCluster(this);
	}
	
	
	
	public String getClusterLabel(VisualCluster cl){
		return this.label;
	}
	
	protected double getArea(){
		return Math.PI*Math.pow(this.rad, 2);
	}
	
	public double getClusterSize(VisualCluster cluster){
		double size = cluster.getRelationsInCluster().size() + cluster.getViewsInCluster().size() + cluster.getQueriesInCluster().size();
		return size;
	}
	
	protected double getClusterRad(){
		double radToReturn = (double) Math.round(this.rad * 100) / 100;
		return radToReturn;
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
	
	public double getCenterXOfCluster(){
		return this.cx;
	}
	
	public double getCenterYOfCluster(){
		return this.cy;
	}
	
	protected void printClusterData(){
		
	}
	
	public int getClusterId(){
		return this.id;
	}
	
	private String getDominatingRealationsInCluster(VisualCluster cl){
		if(cl.getRelationsInCluster().size() == 1){
			return cl.getRelationsInCluster().get(0).getName();
		}
		else{
			String names = "-";
			Map<VisualNode,Integer> relations = new HashMap<VisualNode,Integer>();
			ArrayList<VisualNode> myR = new ArrayList<VisualNode>(cl.getRelationsInCluster());
			
			for(VisualNode v : myR){
				int value = 0;
				ArrayList<VisualEdge> myE = new ArrayList<VisualEdge>(v.getInEdges());
				for(VisualEdge e : myE){
					if(e.getType() == EdgeType.EDGE_TYPE_USES){
						value++;
					}
				}
				relations.put(v, value);
			}
			int sum = 0;
			for(Map.Entry<VisualNode, Integer> entry : relations.entrySet()){
				System.out.println("key  " + entry.getKey() + " value "+ entry.getValue());
				sum+=entry.getValue();
			}
			int avg = (int)sum/relations.size();
			for(Map.Entry<VisualNode, Integer> entry : relations.entrySet()){
				if(entry.getValue() >= avg){
					names+=entry.getKey()+"-";
				}
			}
			System.out.println("names  " + names);
			return names;
		}
	}
	
	protected void printInClusterEdges(){

		ArrayList<MyPair> myEdges = new ArrayList<MyPair>();
		int arrCnt = 0;
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
	
	protected double getLineLength(){
		printInClusterEdges();
		double length = (double) Math.round(lineLenght * 100) / 100;
		return length;
	}
	
	public int getInterClusterCrossings(){
		printInClusterEdges();
		return this.edgeCrossCluster;
	}
	
}
