package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.security.ntlm.Client;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class VisualClustersOnACircleLayout extends VisualCircleLayout {
	
	protected double endC;
	protected VisualGraph graph;
	private List<VisualNode> queries;
	private List<VisualNode> relations;
	private List<VisualNode> views;
	private ClusterSet cs;
	private static int clusterId = 0;
	static int a = 0;
	protected List<String> files;
	private List<VisualNode> RQV;
	private double edgelenngthforGraph = 0;
	
	
	private VisualTotalClusters clusterList;
	protected VisualCircleLayout vcl;
	
	
	
	public VisualClustersOnACircleLayout(VisualGraph g, double endC) {
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
    	vcl.a = 0; vcl.rCnt=0; vcl.c = 0;
        int b = 0, relwithoutQ = 0;
        ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
        ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
        ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
        
        
        
        rc.addAll(relationsInCluster(nodes));
        qc.addAll(queriesInCluster(nodes));
        vc.addAll(viewsInCluster(nodes));
        
        int singleQinCl = nodes.size() - rc.size() - outQ(nodes).size() - vc.size() - queriesWithViews(qc).size();
        Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>(getRSimilarity(qc));
        Map<ArrayList<VisualNode>, Integer> Vset = new HashMap<ArrayList<VisualNode>, Integer>(getVSimilarity(vc));
        if(relationsInCluster(nodes).size()>3){
        	Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
        	Map<ArrayList<VisualNode>, Integer> Vsorted = sortByComparator(Vset);
            ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>(getSortedArray(sorted, rc));
            ArrayList<VisualNode> sortedV = new ArrayList<VisualNode>(getSortedArray(Vsorted, vc));
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
        
        if(rc.isEmpty()){
        	placeQueries(qc, cx, cy, qRad, qAngle, qc.size());
        	placeRelations(vc, cx, cy);
        }else{
	        for(VisualNode r : rc){
	        	
	        	ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>(getQueriesforR(r));
				if(queriesforR.isEmpty()){
					placeRelation(r, qAngle, 0, relationRad, cx, cy, newAngle);
				}else{
					qAngle = placeQueries(queriesforR, cx, cy, qRad, qAngle, Q);
		        	sAngle += qAngle;
		        	placeRelation(r, qAngle, sAngle, relationRad, cx, cy, newAngle);
				}
	        }
	        placeOutQueries(nodes, qRad, cx, cy);
	        placeQueriesWithViews(qc, cx, cy, qRad);
	        placeMultyViews(multyV, cx, cy, relationRad+10);
        }
        //placeOutQueries(nodes, qRad, cx, cy);
        //placeQueriesWithViews(qc, cx, cy, qRad);
        //placeMultyViews(multyV, cx, cy, relationRad+10);
		clusterId++;
		VisualCluster cluster = new VisualCluster(qRad, rc, vc, qc, cx, cy, clusterId);
		
		clusterList.addCluster(cluster);
		cluster.printInClusterEdges();
		edgelenngthforGraph += cluster.getLineLength();
	}
    
//    protected void drawCircles(List<VisualNode> nodes, double cx, double cy){
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
//		
//		clusterList.addCluster(cluster);
//		cluster.printInClusterEdges();
//		edgelenngthforGraph += cluster.getLineLength();
//	}

    protected void setCl(ArrayList<VisualNode> nodes, ArrayList<VisualNode> rc,ArrayList<VisualNode> vc,ArrayList<VisualNode> qc, double cx, double cy){
    	clusterId++;
		VisualCluster cluster = new VisualCluster(getSmallRad(nodes), rc, vc, qc, cx, cy, clusterId);
		clusterList.addCluster(cluster);
		cluster.printInClusterEdges();
		edgelenngthforGraph += cluster.getLineLength();
    }
    
	private void clustersOnaCircle(double endC){
		clusterList = new VisualTotalClusters();
		clusterList.clearList();
		ArrayList<Cluster> Clusters;
		double [][] distances = cs.getClusterDistances();
		//if(endC != 1){
		//	SortedArrayList sl = new SortedArrayList();
		//	Clusters = new ArrayList<Cluster>(sl.insertSorted(cs.getClusters(), distances));
			
		//}
		//else{
			Clusters = new ArrayList<Cluster>(cs.getClusters());
		//}
		System.out.println("    " + Clusters);
		ArrayList<ArrayList<VisualNode>> vertices = new ArrayList<ArrayList<VisualNode>>();       //lista me ta clusters 
		ArrayList<ArrayList<VisualNode>> V = new ArrayList<ArrayList<VisualNode>>();   // tin xrisimopoio gia na anakatevw tin vertices gia na min einai olla ta megala cluster mazi
		for(Cluster cl : Clusters){
			vertices.add(cl.getNode());
			Collections.shuffle(vertices);
		}
		V.addAll(vertices);
		double myRad = 0.0;
		double RAD = 0;
		//taksinomei tin lista --> prwta ta relations meta ta upoloipa k briskei aktina
		for(ArrayList<VisualNode> lista : vertices){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
	//		System.out.println(lista);
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			RAD += getSmallRad(nodes);
		}
		myRad = RAD/Math.PI;
		int a = 0;double angle = 0.0, sum = 0.0;
		Dimension d = getSize();
		double w = d.getWidth();
		double h = d.getHeight();
		for(ArrayList<VisualNode> lista : V){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			double temp =   (2*myRad*myRad - getSmallRad(nodes)*getSmallRad(nodes)*0.94)/(2*myRad*myRad );
			if(Math.abs(temp)>1){
				temp = 0.9;
			}
			angle = (Math.acos( temp))*2;   // 0.94 is used simulate strait lines to curves
			double cx = Math.cos(sum+angle/2) * myRad*1.8+(w/2);// 1.8 is used for white space borders
			double cy =	Math.sin(sum+angle/2) * myRad*1.8+(h/2);
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);

			sum+=angle;
		//	if(endC != 1){
		//		drawCircles(nodes, cx, cy);
		//	}
		//	else{
				circles(nodes, cx, cy);
		///	}
				
				
			lista.get(0).setLocation(coord1);
			a++;
		}
//		System.out.println("diametros = "+ (myRad*1.8*2));
//		System.out.println("AVG EDGE LENGTH FOR GRAPG = "+ edgelenngthforGraph);
		w=d.getSize().getWidth();
		h=d.getSize().getHeight();
		System.out.println("windth/2  "+ w/2);System.out.println("height/2  "+ h/2);
		System.out.println("dim  "+ graph.getSize());
	}


	@Override
	public void initialize() {
		HAggloEngine engine = new HAggloEngine(this.graph);
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);		
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(endC);
		clustersOnaCircle(endC);
	}

	@Override
	public void reset() {
		initialize();
		
	}
	
}
