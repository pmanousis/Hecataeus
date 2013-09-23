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
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNewCircleLayout.CircleVertexData;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualCircleClusteredLayout extends AbstractLayout<VisualNode,VisualEdge> {
	
	public enum Cluster{
		Queries,
		Views,
		Relations;
	}
	
	protected Cluster clusterType;
	protected VisualGraph graph;
	
	private double radius;
	private double relationRadius;
	private double viewRadius;
	private double queryRadius;
	
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	private List<VisualNode> semantix = new ArrayList<VisualNode>();
	private List<VisualNode> wtf = new ArrayList<VisualNode>();
	private List<VisualNode> nodes;
	
	
	
	private List<VisualNode> cRelations = new ArrayList<VisualNode>();
	private List<VisualNode> Relations = new ArrayList<VisualNode>();
	
	
	
	private int[][] dist;
	
	Map<VisualNode, CircleVertexData> circleVertexDataMap =
			LazyMap.decorate(new HashMap<VisualNode,CircleVertexData>(), 
			new Factory<CircleVertexData>() {
				public CircleVertexData create() {
					return new CircleVertexData();
				}});	
	
	
	public VisualCircleClusteredLayout(VisualGraph g, Cluster cluster){
		super(g);
		this.graph = g;
		this.clusterType = cluster;
		
		nodes = new ArrayList<VisualNode>((Collection<? extends VisualNode>) g.getVertices());
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				relations.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_SEMANTICS){
				semantix.add(v);
			}
			else{
				wtf.add(v);
			}
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
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
		
	}

	private void clusterViews() {
		// TODO Auto-generated method stub
		
	}

	private void clusterQueries() {
		
		dist = new int[relations.size()][queries.size()];
		int pos = 0;
		int j = 0;
		for(VisualNode v : queries){
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
			for(VisualEdge e : outE){
				if(e.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					VisualNode r = e.getToNode();
					int i = 0;
					for(VisualNode n : relations){
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
		
		for(int i = 0; i < relations.size(); i++){
			for(int k = 0; k < queries.size(); k++){
				if(dist[i][k] != 1){
					dist[i][k] = 0;
				}
				System.out.print(dist[i][k]);
			}
			System.out.println();
		}
		String tableNames = StringUtils.strip(relations.toString(), "[");
		tableNames = StringUtils.strip(tableNames, "]");
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
								
								"TABLES = " + relations.size() + "\n" +
								"TableNames = " + tableNames + "\n"+
								"QUERIES = "+ queries.size() + "\n\n"+
								"% TABLES_X_QUERIES MATRIX \n";
 
			 
			for(int i = 0; i < relations.size(); i++){
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
		ClusterSet cs;
		HAggloEngine engine = new HAggloEngine(); 			   
		engine.executeParser();
		engine.buildFirstSolution();
		//cs = new ClusterSet(engine.execute(5).getId());
		cs = engine.execute(100);
		System.out.println("EEEEEEEEEEEEEEEEEe  " + cs.getCSDescriptionString());
		
//		
//		int crel = 0;
////		int size = relations.size();
////		for(int index = 0; (relations.get(index) != null) && (index < size);) {
//		for(VisualNode v : relations){
////			VisualNode v = relations.get(index);
//			if(cs.hasClusters(v)){
//				crel++;
//				cRelations.add(v);
////				relations.remove(index);
////				size--;
//			}else{
////				index++;
//				Relations.add(v);
//			}
//		}
		
		ArrayList<clusters.EngineConstructs.Cluster> clusters = new ArrayList<clusters.EngineConstructs.Cluster>(cs.getClusters());
		
		
		ArrayList<ArrayList<VisualNode>> eva = new ArrayList<ArrayList<VisualNode>>();
		List<VisualNode> lista = new ArrayList<VisualNode>();
		int malakies = 0;
		for (clusters.EngineConstructs.Cluster myCluster : clusters){
			
			//myCluster.cSize(clusters);
			
			
			VisualNode node = getVNode(myCluster.getName(myCluster).toString());
			System.out.println(myCluster.getName(myCluster));
			for(VisualNode v : this.graph.getNodes()){
				if(myCluster.getName(myCluster).toString().contains(v.getName())){
					lista.add(v);
				}
			}
			
			
			malakies++;
			
		}
		

		
//		
//		System.out.println(cRelations.toString());    //ta relations pou einai se cluster
//		System.out.println(Relations.toString());
		
		VisualNewCircleLayout cl = new VisualNewCircleLayout(this.graph);
		
		Dimension d = getSize();
		
		if (d != null) {

			double height = d.getHeight();
			double width = d.getWidth();

			List<Integer> sizes = new ArrayList<Integer>();
			sizes.add(queries.size());
			sizes.add(relations.size());
			sizes.add(views.size());
			Collections.sort(sizes);
			
			if(radius <= 0){
				radius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
			}
			
			
			if (relationRadius <= 0) {
				if(relations.size() == sizes.get(0)){           // ta relations einai ta ligotera
					relationRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				}else if(relations.size() == sizes.get(1)){     //ta relations einai ta mesaia
					relationRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				}else{    // ta relations einai ta perissotera
					relationRadius = 0.45 * (height < width ? height : width)+100;
				}
			}
			
			if (viewRadius <= 0) {	
				if(views.size() == sizes.get(0)){           // ta views einai ta ligotera
					viewRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				}else if(views.size() == sizes.get(1)){     //ta views einai ta mesaia
					viewRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				}else{    // ta views einai ta perissotera
					viewRadius = 0.45 * (height < width ? height : width)+100;
				}
			}
			
			if (queryRadius <= 0) {
				
				if(queries.size() == sizes.get(0)){           // ta queries einai ta ligotera
					queryRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				}else if(queries.size() == sizes.get(1)){     //ta queries einai ta mesaia
					queryRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				}else{    // ta queries einai ta perissotera
					queryRadius = 0.45 * (height < width ? height : width)+100;
				}
			}
			
			relations.clear();
			
			for(VisualNode v : (List<VisualNode>)Relations){
				relations.add(v);
			}
			
			for(VisualNode v : (List<VisualNode>)cRelations){
				relations.add(v);
			}
			
			
			int h = 0;
			for (VisualNode v : (List<VisualNode>)relations){
				Point2D coord = transform(v);				
				double angle = (2 * Math.PI * h) / relations.size();
				coord.setLocation(Math.cos(angle) * relationRadius + width / 2, Math.sin(angle) * relationRadius + height / 2);
				CircleVertexData data = cl.getCircleData(v);
				data.setAngle(angle);
				
			//	cl.dosomething(Math.cos(angle) * relationRadius + width / 2, Math.sin(angle) * relationRadius + height / 2, (VisualNode)v, 0);
				h++;
			}
				

			
			
			int k = 0;
			for (VisualNode v : (List<VisualNode>)views){
				Point2D coord = transform(v);				
				double angle = (2 * Math.PI * k) / views.size();
				coord.setLocation(Math.cos(angle) * viewRadius + width / 2,
						Math.sin(angle) * viewRadius + height / 2);
				CircleVertexData data = cl.getCircleData(v);
				data.setAngle(angle);
				cl.dosomething(Math.cos(angle) * viewRadius + width / 2, Math.sin(angle) * viewRadius + height / 2, (VisualNode)v, 0);
				k++;
			}
			
			
			
			
			int z = 0;
			for (VisualNode v : queries){
				Point2D coord = transform(v);				
				double angle = (2 * Math.PI * z) / queries.size();
				coord.setLocation(Math.cos(angle) * queryRadius + width / 2,
						Math.sin(angle) * queryRadius + height / 2);
				CircleVertexData data = cl.getCircleData(v);
				data.setAngle(angle);				
				cl.dosomething(Math.cos(angle) * queryRadius + width / 2, Math.sin(angle) * queryRadius + height / 2, (VisualNode)v, 0);
				z++;
			}
			
			
		}
		
		
		
	}

	
	public VisualNode getVNode (String name){
		for(VisualNode v : this.graph.getVertices()){
			if(name.contentEquals(v.getName())){
				return v;
			}
		}
		return null;
	}
	
	@Override
	public void reset() {
		initialize();
		
	}
}
