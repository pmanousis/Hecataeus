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

public class VisualCirclingClustersLayout extends VisualCircleLayout{

	
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
	private VisualTotalClusters clusterList;
	protected VisualCirclingClustersLayout(VisualGraph g, double endC) {
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
        
        int singleQinCl = nodes.size() - rc.size() - outQ(nodes).size() - vc.size() - queriesWithViews(qc).size();
        Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>(getRSimilarity(qc));
        Map<ArrayList<VisualNode>, Integer> viewSet = new HashMap<ArrayList<VisualNode>, Integer>(getVSimilarity(vc));
        if(relationsInCluster(nodes).size()>4){
        	Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
        	Map<ArrayList<VisualNode>, Integer> sortedViews = sortByComparator(viewSet);
            ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>(getSortedArray(sorted, rc));
            ArrayList<VisualNode> sortedV = new ArrayList<VisualNode>(getSortedArray(sortedViews, vc));
            rc.clear();vc.clear();
            rc.addAll(sortedR);vc.addAll(sortedV);
            
        }
 
        double relationRad = 1.9*getSmallRad(rc);
        double qRad = getQueryRad(nodes.size() - rc.size()- vc.size());
        int Q = singleQinCl;
        double qAngle = 0;
        double sAngle = 0;
        double newAngle = 2*Math.PI/rc.size();
        ArrayList<VisualNode> multyV = new ArrayList<VisualNode>(vc);//getmultyViews
        double viewBand = getViewBandSize(multyV, relationRad);
        if(qRad <= viewBand){
        	qRad = viewBand+40;
        }
        if(qRad <= relationRad){
        	qRad = relationRad+40;
        }
        for(VisualNode r : rc){
        	ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>(getQueriesforR(r));
        	qAngle = placeQueries(queriesforR, cx, cy, qRad, qAngle, Q);
        	sAngle += qAngle;
        	placeRelation(r, qAngle, sAngle, relationRad, cx, cy, newAngle);
        }
        placeViews(vc, relationRad, qRad, cx, cy);
        placeOutQueries(nodes, qRad, cx, cy);
        placeQueriesWithViews(qc, cx, cy, qRad);
        placeMultyViews(multyV, cx, cy, relationRad+10);
		clusterId++;
		VisualCluster cluster = new VisualCluster(qRad, rc, vc, qc, cx, cy, clusterId);
		clusterList.addCluster(cluster);
		cluster.printInClusterEdges();
		edgelenngthforGraph += cluster.getLineLength();
	}
	
//	private double checkRad(ArrayList<ArrayList<VisualNode>> SoC, double myRad){
//		double tempRad = 0;double ccircleR = 0;
//		for(ArrayList<VisualNode>listaC: SoC){
//			tempRad += getSmallRad(listaC);
//		}	
//		ccircleR = 2*tempRad;
//		if(ccircleR>=2*Math.PI*myRad){
//			return (ccircleR/2*Math.PI);
//		}
//		else{
//			return myRad;
//		}
//	}
	
	
	protected void CirclingCusters(){
		clusterList = new VisualTotalClusters();
		clusterList.clearList();
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
		if(endC == 1){
			Collections.sort(vertices, new ListComparator());
			sortedV.addAll(vertices);
		}else{
			sortedV.addAll(vertices);
		}
		
		
		double myRad = 1.0;
		
		ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();
		while((int) Math.pow(2, myRad)<sortedV.size()){
			ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), (int) Math.pow(2, myRad)));
			sublistofClusters.add(tmpVl);
			myRad++;
		}
		ArrayList<ArrayList<VisualNode>> tmpVl=new ArrayList<ArrayList<VisualNode>>(sortedV.subList((int) Math.pow(2, myRad-1), sortedV.size()));
		if(!tmpVl.isEmpty()){
			sublistofClusters.add(tmpVl);
			
		}
		sublistofClusters.add(0, new ArrayList<ArrayList<VisualNode>>(sortedV.subList(0, 1)));

		

		double bigCircleRad = 0.0;
		double bigClusterRad = 0.0;
//		System.out.println(sublistofClusters);
		
		
		for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
			ArrayList<ArrayList<VisualNode>> tmp ;
			if (sublistofClusters.indexOf(listaC)!=sublistofClusters.size()-1){
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)+1));
			}
			else{
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)));	
			}

			if(tmp.size()>1&& listaC.size()>1){
				bigClusterRad += getSmallRad(tmp.get(tmp.size()-1));
				bigCircleRad = (bigClusterRad)*2;
			}
			else{
				bigClusterRad += getSmallRad(listaC.get(0));
				bigCircleRad = (bigClusterRad)*2;
			}
			
//			System.out.println("TELIKI aktina megalou kiklou"+bigCircleRad);
			
			
			
			double angle = 0.0, sum = 0.0;
			int a = 0;
			for(ArrayList<VisualNode> lista : listaC){
				List<VisualNode> nodes = new ArrayList<VisualNode>();
				Collections.sort(lista, new CustomComparator());
				nodes.addAll(lista);
				//correct angle
				angle = (2*Math.PI*a)/listaC.size();
				double cx = Math.cos(angle) * bigCircleRad*1.2;// 1.8 is used for white space borders
				
				double cy =	Math.sin(angle) * bigCircleRad*1.2;
//				System.out.println("ANGLEEE   " + angle);
				int m = 0;
				a++;
				sum+=angle;
				circles(nodes, cx, cy);
				
				
			}
			//System.out.println("oli i gonia tou kuklou logika 2p   " + sum+angle);
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