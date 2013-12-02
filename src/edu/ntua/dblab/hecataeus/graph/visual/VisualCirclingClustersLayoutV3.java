package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class VisualCirclingClustersLayoutV3 extends VisualCircleLayout{

	
	protected VisualGraph graph;
	protected double endC;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	private static int clusterId = 0;
	private double edgelenngthforGraph = 0;
	protected List<String> files;
	private List<VisualNode> RQV;
	protected VisualCircleLayout vcl;
	
	protected VisualCirclingClustersLayoutV3(VisualGraph g, double endC) {
		super(g);
		this.graph = g;
		this.endC = endC;
		vcl = new VisualCircleLayout(this.graph);
		
		queries = new ArrayList<VisualNode>(vcl.getQueries());
		relations = new ArrayList<VisualNode>(vcl.getRelations());
		views = new ArrayList<VisualNode>(vcl.getViews());
		
		RQV = new ArrayList<VisualNode>(vcl.RQV);
		files = new ArrayList<String>(vcl.files);
		
	}
//
//	private void circles(List<VisualNode> nodes, double cx, double cy){
//		int b = 0;
//		ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
//		ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
//		ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
//		for(VisualNode v : nodes){
//			if(v.getType() == NodeType.NODE_TYPE_RELATION){
//				rc.add(v);
//				double smallRad = 1.3*getSmallRad(relationsInCluster(nodes));
//				Point2D coord = transform(v);
//				double angleA = (2 * Math.PI ) / relationsInCluster(nodes).size();
//				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
//				v.setLocation(coord);
//				HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
//				HecataeusViewer.getActiveViewer().repaint();
//			}else{
//				if(v.getType() == NodeType.NODE_TYPE_QUERY){
//					qc.add(v);
//				}
//				else if(v.getType() == NodeType.NODE_TYPE_VIEW){
//					vc.add(v);
//				}
//				double smallRad = getSmallRad(nodes);
//				Point2D coord = transform(v);
//				double angleA = 0.0;
//				if(relationsInCluster(nodes).size() > 1){
//					angleA = (2 * Math.PI ) / (nodes.size()-relationsInCluster(nodes).size());
//				}else{
//					angleA = (2 * Math.PI ) / nodes.size();
//				}
//				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
//				v.setLocation(coord);
//				HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
//				HecataeusViewer.getActiveViewer().repaint();
//			}
//			b++;
//		}
//		clusterId++;
//		VisualCluster cluster = new VisualCluster(getSmallRad(nodes), rc, vc, qc, cx, cy, clusterId);
//		cluster.printInClusterEdges();
//	}
	private void circles(List<VisualNode> nodes, double cx, double cy){
        int b = 0, relwithoutQ = 0;
        ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
        ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
        ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
        
        
        
        rc.addAll(relationsInCluster(nodes));
        qc.addAll(queriesInCluster(nodes));
        vc.addAll(viewsInCluster(nodes));
        
        int singleQinCl = nodes.size() - rc.size() - outQ(nodes).size() - vc.size();
        Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>(getRSimilarity(qc));
        if(relationsInCluster(nodes).size()>4){
        	Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
            ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>(getSortedArray(sorted, rc));
            rc.clear();
            rc.addAll(sortedR);
        }
 
        double relationRad = 1.9*getSmallRad(rc);
        double qRad = getQueryRad(nodes.size() - rc.size()- vc.size());
        int Q = singleQinCl;
        double qAngle = 0;
        double sAngle = 0;
        for(VisualNode r : rc){
        	ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>(getQueriesforR(r));
        	qAngle = placeQueries(queriesforR, cx, cy, qRad, qAngle, Q);
        	sAngle += qAngle;
        	placeRelation(r, qAngle, sAngle, relationRad, cx, cy);
        }
        placeViews(vc, relationRad, qRad, cx, cy);
        placeOutQueries(nodes, qRad, cx, cy);
		clusterId++;
		VisualCluster cluster = new VisualCluster(qRad, rc, vc, qc, cx, cy, clusterId);
		cluster.printInClusterEdges();
		edgelenngthforGraph += cluster.getLineLength();
	}
	protected void CirclingCusters(){
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
		Collections.sort(vertices, new ListComparator());
		sortedV.addAll(vertices);

//		System.out.println(sortedV);
		
		double myRad = 1.0;
		
		ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
		while((int) Math.pow(2, myRad)<sortedV.size()){
			//new ebala to -1 gia na ksekinaei apo 0
			ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) (Math.pow(2, myRad-1)), (int) (Math.pow(2, myRad))));
			sublistofClusters.add(tmpVl);
			myRad++;
		}
		ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), sortedV.size()));
		sublistofClusters.add(tmpVl);
//		int counter = 0, p = 1;
//		do{
//			int start = counter;
//			int size = (int)Math.pow(2, p);
//			int end =  (start+(size) >= sortedV.size() ? sortedV.size()-1 : start+(size));
//			ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList(start, end));
//			sublistofClusters.add(tmpVl);
//			counter += size;
//			p++;
//		}while(counter < sortedV.size());
		

		Collections.reverse(sublistofClusters);

		double bigCircleRad = 0.0;
		double bigClusterRad = 0.0;
		double prevRad =0.0;
		for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
			ArrayList<ArrayList<VisualNode>> tmp ;
			tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(0));
			bigClusterRad += getSmallRad(tmp.get(tmp.size()-1));
			bigCircleRad = prevRad+ bigClusterRad*1.2;
//			System.out.println("TELIKI aktina megalou kiklou"+bigCircleRad);
			
			
			
			double angle = 0.0, sum = 0.0;
			int a = 0;
			for(ArrayList<VisualNode> lista : listaC){
				List<VisualNode> nodes = new ArrayList<VisualNode>();
				Collections.sort(lista, new CustomComparator());
				nodes.addAll(lista);
				int temps = listaC.size();
				//correct angle
				angle = (2*Math.PI*a)/temps;
				
				double cx = Math.cos(angle) * bigCircleRad*1.8;// 1.8 is used for white space borders
				
				double cy =	Math.sin(angle) * bigCircleRad*1.8;
//				System.out.println("ANGLEEE   " + angle);
				int m = 0;
				a++;
				sum+=angle;
				circles(nodes, cx, cy);
				
				
			}
		}
		HecataeusViewer.getActiveViewer().repaint();
	}
	
	
	
	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		
		CirclingCusters();
		
	}

	@Override
	public void reset() {
		initialize();
	}

}