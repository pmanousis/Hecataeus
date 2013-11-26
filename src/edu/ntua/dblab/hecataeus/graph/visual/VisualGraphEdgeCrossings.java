package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.graph.util.EdgeType;

public class VisualGraphEdgeCrossings {

	protected VisualGraph graph;
	protected int numberOfEdges;
	
	public VisualGraphEdgeCrossings(VisualGraph g){
		this.graph = g;
	}
	
	public int getGraphEdgeCrossings(){
		int crossings = 0;
		ArrayList<MyPair> myEdges = new ArrayList<MyPair>();
		ArrayList<VisualNode> queries = new ArrayList<VisualNode>();
		ArrayList<VisualNode> views = new ArrayList<VisualNode>();
		
		for(VisualNode v : graph.getVertices()){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
			}
		}
		
		for(VisualNode v : queries){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : edges){
				if(e.getToNode().getType()==NodeType.NODE_TYPE_RELATION || e.getToNode().getType()==NodeType.NODE_TYPE_VIEW){
					MyPair p = new MyPair(v.getLocation(), e.getToNode().getLocation());
					myEdges.add(p);
				}
			}
		}
		
		for(VisualNode v : views){
			List<VisualEdge> edges = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : edges){
				if(e.getToNode().getType()==NodeType.NODE_TYPE_RELATION || e.getToNode().getType()==NodeType.NODE_TYPE_VIEW){
					MyPair p = new MyPair(v.getLocation(), e.getToNode().getLocation());
					myEdges.add(p);
				}
			}
		}
		int num = 0;
		for(int i = 0; i < myEdges.size(); i++){
			Line2D line1 = new Line2D.Double(myEdges.get(i).getFirstPoint(), myEdges.get(i).getSecondPoint());
			
			for(int j = 0; j < myEdges.size(); j++){
				if(j==i){
					continue;
				}
				Line2D line2 = new Line2D.Double(myEdges.get(j).getFirstPoint(), myEdges.get(j).getSecondPoint());
				boolean result = line2.intersectsLine(line1);
				if(result && (myEdges.get(i).getSecondPoint()!= myEdges.get(j).getSecondPoint()) && (myEdges.get(i).getFirstPoint()!= myEdges.get(j).getFirstPoint())){
					crossings++;
				}
				num++;
			}
		}
		
		System.out.println("TOTAL EDGE CROSSINGS :: " + crossings/2);
		numberOfEdges = num/2;
		return crossings/2;
	}
	
	
	public int getNumberOfEdges(){
		this.numberOfEdges = this.graph.getEdges(edu.ntua.dblab.hecataeus.graph.evolution.EdgeType.EDGE_TYPE_USES).size();	
		return this.numberOfEdges;
	}
}
