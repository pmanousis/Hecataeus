package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
import edu.uci.ics.jung.graph.util.EdgeType;

public class VisualConcetricCirclesClustersLayoutV2 extends VisualCircleLayout{

	protected VisualGraph graph;
	protected double endC;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	private static int clusterId = 0;
	private VisualTotalClusters clusterList;
	protected List<String> files;
	private List<VisualNode> RQV;
	protected VisualCircleLayout vcl;
	private double edgelenngthforGraph = 0;
	protected VisualConcetricCirclesClustersLayoutV2(VisualGraph g, double endC) {
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
        	qAngle = placeQueries(queriesforR, cx, cy, qRad, Q);
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
	
	protected void CirclingCusters(){
		clusterList = new VisualTotalClusters();
		clusterList.clearList();
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		ArrayList<ArrayList<VisualNode>> sortedV = new ArrayList<ArrayList<VisualNode>>();
		Collections.sort(vertices, new ListComparator());
		sortedV.addAll(vertices);
		//new
		Collections.sort(sortedV,new ReverseListComparator());
//		System.out.println(sortedV);
		
		double myRad = 1.0;
		
		ArrayList<ArrayList<ArrayList<VisualNode>>> sublistofClusters = new ArrayList<ArrayList<ArrayList<VisualNode>>>();

		int counter = 0, p = 1;
		do{
			int start = counter;
			int size = (int)Math.pow(2, p);
			int end =  (start+(size) >= sortedV.size() ? sortedV.size()-1 : start+(size));
			ArrayList<ArrayList<VisualNode>> tmpVl = new ArrayList<ArrayList<VisualNode>>(sortedV.subList(start, end));
			sublistofClusters.add(tmpVl);
			counter += size;
			p++;
		}while(counter < sortedV.size());
		sublistofClusters.add(sublistofClusters.size(), new ArrayList<ArrayList<VisualNode>>(sortedV.subList(sortedV.size()-1, sortedV.size())));

		double bigCircleRad = 0.0;
		double bigClusterRad = 0.0;
//		System.out.println(sublistofClusters);
		double biggestClusterRad = getSmallRad(sublistofClusters.get(0).get(0));
		int diam = 0;
		double prevRad =0.0;
		for(ArrayList<ArrayList<VisualNode>> listaC: sublistofClusters){
			ArrayList<ArrayList<VisualNode>> tmp ;
			if (sublistofClusters.indexOf(listaC)!=sublistofClusters.size()-1){
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)+1));
			}
			else{
				tmp = new ArrayList<ArrayList<VisualNode>>(sublistofClusters.get(sublistofClusters.indexOf(listaC)));	
			}

			if(diam>0){
				bigClusterRad += getSmallRad(tmp.get(0));
			//	bigCircleRad = 2*bigClusterRad+2*biggestClusterRad;
				bigCircleRad = prevRad+ bigClusterRad*2;
			}else{
				bigClusterRad += getSmallRad(tmp.get(0)) + getSmallRad(tmp.get(1));
				bigCircleRad = (bigClusterRad)*1.2;// white space between circle layers
			}
			prevRad = bigCircleRad;
			
			
			diam++;
		
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
				int m = 0;
				a++;
				sum+=angle;
				circles(nodes, cx, cy);
			}
		}
		HecataeusViewer.getActiveViewer().repaint();
		//TODO: FIX THIS
		HecataeusViewer.getActiveViewerZOOM().repaint();
	}
	
	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		CirclingCusters();
		HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewer().getPickedVertexState()));
		HecataeusViewer.getActiveViewer().repaint();
		//TODO: FIX THIS
		HecataeusViewer.getActiveViewerZOOM().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(HecataeusViewer.getActiveViewerZOOM().getPickedVertexState()));
		HecataeusViewer.getActiveViewerZOOM().repaint();
		HecataeusViewer.hecMap.createMap();
	}

	@Override
	public void reset() {
		initialize();
	}

}