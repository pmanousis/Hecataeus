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
//	private static double sumAngle=0;
//	private ArrayList<VisualNode> jq = new ArrayList<VisualNode>();
	
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

		clusterList.addCluster(cluster);
		cluster.printInClusterEdges();
		edgelenngthforGraph += cluster.getLineLength();
	}
	

	private void clustersOnaCircle(double endC){
		clusterList = new VisualTotalClusters();
		ArrayList<Cluster> Clusters;
		double [][] distances = cs.getClusterDistances();

		ArrayList<ArrayList<VisualNode>> xa = new ArrayList<ArrayList<VisualNode>>();
		
		
		if(endC != 1){
			SortedArrayList sl = new SortedArrayList();
			Clusters = new ArrayList<Cluster>(sl.insertSorted(cs.getClusters(), distances));
			
		}
		else{
			Clusters = new ArrayList<Cluster>(cs.getClusters());
		}
		
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
		double diametros = 0;
		int a = 0;double angle = 0.0, sum = 0.0;
		Dimension d = getSize();
		double w = d.getWidth();
		double h = d.getHeight();
		for(ArrayList<VisualNode> lista : V){
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
			diametros = 2*getSmallRad(nodes);
			double temp =   (2*myRad*myRad - getSmallRad(nodes)*getSmallRad(nodes)*0.94)/(2*myRad*myRad );
			if(Math.abs(temp)>1){
				temp = 0.9;
			}
			angle = (Math.acos( temp))*2;   // 0.94 is used simulate strait lines to curves
			double cx = Math.cos(sum+angle/2) * myRad*1.8+(w/2)*(-1);// 1.8 is used for white space borders
			double cy =	Math.sin(sum+angle/2) * myRad*1.8+(h/2)*(-1);
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);

			sum+=angle;
			circles(nodes, cx, cy);
			lista.get(0).setLocation(coord1);
			a++;
		}
		System.out.println("diametros = "+ (myRad*1.8*2));
		System.out.println("AVG EDGE LENGTH FOR GRAPG = "+ edgelenngthforGraph);
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
