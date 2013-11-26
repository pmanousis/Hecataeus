package edu.ntua.dblab.hecataeus.graph.visual;

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
import java.util.Map.Entry;

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
//		
//	}
//	
	
	
	private static Map sortByComparator(Map unsortedMap) {
		List list = new LinkedList(unsortedMap.entrySet());
		Collections.sort(list, new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	
	private void circles(List<VisualNode> nodes, double cx, double cy){
		int b = 0;
		ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
		ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
		ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
		rc.addAll(relationsInCluster(nodes));
		
		ArrayList<VisualNode> jq = new ArrayList<VisualNode>();
		
		Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>();
		
		for(VisualNode n : nodes){
			if(n.getType()==NodeType.NODE_TYPE_QUERY){
				qc.add(n);
			}
		}
		
		for(VisualNode q : qc){
			ArrayList<VisualEdge> qEdges = new ArrayList<VisualEdge>(q.getOutEdges());
			int toRelation = 0;
			for(VisualEdge ed : qEdges){
				if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					toRelation++;
				}
			}
			if(toRelation > 1){
				ArrayList<VisualNode> QtR = new ArrayList<VisualNode>();
				for(VisualEdge ed : qEdges){
					if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
						QtR.add(ed.getToNode());
					}
				}
				for(int r1 = 0; r1 < QtR.size(); r1++){
					for(int r2 = r1+1; r2 < QtR.size(); r2++){
						ArrayList<VisualNode> pair = new ArrayList<VisualNode>();
						pair.add(QtR.get(r1));
						pair.add(QtR.get(r2));
						if(set.containsKey(pair)){
							set.put(pair, set.get(pair)+1);
						}else{
							set.put(pair, 1);
						}
						
					}
				}
			}
		}
		for(Entry e : set.entrySet()){
			System.out.println("   m  " + e.getValue().toString() + " no  " + e.getKey().toString());
		}
		if(relationsInCluster(nodes).size()>4){
			Map<ArrayList<VisualNode>, Integer> sorted = sortByComparator(set);
			for(Entry e : sorted.entrySet()){
				System.out.println("   value   " + e.getValue().toString() + " key  " + e.getKey().toString());
			}
			
			ArrayList<VisualNode> sortedR = new ArrayList<VisualNode>();
			for(Entry e : sorted.entrySet()){
				ArrayList<VisualNode> temp = ((ArrayList<VisualNode>)e.getKey());
				for(VisualNode node : temp){
					if(!sortedR.contains(node)){
						sortedR.add(node);
					}
					
				}
					
			}
			System.out.println("sorted relations  " + sortedR);
			rc.clear();
			rc.addAll(sortedR);
		}
		
		
		
		double relationRad = 1.9*getSmallRad(rc);
	//	double relationAngle = (2 * Math.PI ) / rc.size();
		double qRad = getQuery(nodes.size() - rc.size());
		int Q = nodes.size() - rc.size();
		double qAngle = 0;
		double sAngle = 0;
		for(VisualNode r : rc){
			
			ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>();
			for(VisualEdge e : r.getInEdges()){
				if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY){
					VisualNode q = e.getFromNode();
					ArrayList<VisualEdge> qEdges = new ArrayList<VisualEdge>(q.getOutEdges());
					int cnt = 0;
					for(VisualEdge ed : qEdges){
						if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
							cnt++;
						}
					}
					if(cnt==1){
						queriesforR.add(q);
					}
					else{
						jq.add(q);
					}
					
				}
			}
			qAngle = placeQueries(queriesforR, cx, cy, qRad, qAngle, Q);
			
			Point2D coord = transform(r);
			sAngle += qAngle;
//			System.out.println("relation " + r.getName());
//			System.out.println("qangle  " + qAngle + "   to  deg  " + Math.toDegrees(qAngle));
//			System.out.println("Sangle  " + sAngle+ "   to  deg  " + Math.toDegrees(sAngle));
			double rx = Math.cos(sAngle-(qAngle/2))*relationRad+(cx);
			double ry = Math.sin(sAngle-(qAngle/2))*relationRad+(cy);
			coord.setLocation(rx, ry);
			r.setLocation(coord);
			r.setNodeAngle(sAngle-(qAngle/2));
			System.out.println("set node angle    " + (sAngle-(qAngle/2)));
			System.out.println("get node angle name"+ r.getName() + "   angle  "+ r.getNodeAngle());


			
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(r, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
		double jqRad = qRad + 30;
		double jqAngle = (2 * Math.PI ) / jq.size();
		int c = 0;
		for(VisualNode v : jq){
			
			ArrayList<VisualEdge> edgesToR = new ArrayList<VisualEdge>();
			ArrayList<VisualNode> myR = new ArrayList<VisualNode>();
			for(VisualEdge myEdge : v.getOutEdges()){
				if(myEdge.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					myR.add(myEdge.getToNode());
				}
			}
			double myAngle = 0.0;
			for(VisualNode rel : myR){
				myAngle += rel.getNodeAngle();
				System.out.println("rel name  "+ rel.getName() +"  angle  "+  rel.getNodeAngle());
			}
			
			myAngle = myAngle/2;
			
			Point2D coord = transform(v);
			double jqx = Math.cos(myAngle)*jqRad+(cx);
			double jqy = Math.sin(myAngle)*jqRad+(cy);
			coord.setLocation(jqx, jqy);
			c++;
			v.setLocation(coord);
		//	v.setNodeAngle(angle);
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		clusterId++;
		VisualCluster cluster = new VisualCluster(getSmallRad(nodes), rc, vc, qc, cx, cy, clusterId);
		cluster.printInClusterEdges();
		
	}
	
	private double placeQueries(ArrayList<VisualNode> queriesforR, double cx, double cy, double qRad, double qAngle, int Q) {
		
		double sAngle = 0.0;
		double Angle = ((2 * Math.PI ) / Q);   //+qAngle;
		for(VisualNode q : queriesforR){
			Point2D coord = transform(q);
			
			sAngle+=Angle;
			coord.setLocation(Math.cos(Angle*a)*qRad+cx, Math.sin(Angle*a)*qRad+cy);

			q.setLocation(coord);
			q.setNodeAngle(Angle*a);
			a++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(q, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
		return sAngle;
		
	}
	
	
	
	


	private void clustersOnaCircle(double endC){
		
		ArrayList<Cluster> Clusters;
		double [][] distances = cs.getClusterDistances();
		
//		SortedArrayList<Cluster> Clusters = new SortedArrayList<Cluster>();
//		SortedArrayList sl = new SortedArrayList();
//		ArrayList<Cluster> Clusters = new ArrayList<Cluster>(sl.insertSorted(cs.getClusters(), distances));
		
		//for(int i = 0; i < cs.getClusters().size(); i++){
		//	Clusters.insertSorted(cs.getClusters(), distances);
		//}
//		for(int i = 0; i < cs.getClusters().size(); i++){
//			System.out.println("CLUSTERS " + Clusters.get(i).getName(Clusters.get(i)));
//		}

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
			
		// only if clustering algo won't produce more than one clusters
//		if(Clusters.size() < 2){
//			Dimension d = getSize();
//			double height = d.getHeight();
//			double width = d.getWidth();
//			
//			//for(ArrayList<VisualNode> lista : V){
//				int k = 0;
//				for(VisualNode v : V.get(0)){
//					Point2D coord = transform(v);				
//					double angle1 = (2 * Math.PI) / V.get(0).size();
//					coord.setLocation(Math.cos(angle1*k) * myRad + width / 2, Math.sin(angle1*k) * myRad + height / 2);k++;
//					v.setLocation(coord);
//				}
//				
//			//}
//		}
//		else{
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
				double cx = Math.cos(sum+angle/2) * myRad*1.8;// 1.8 is used for white space borders
				double cy =	Math.sin(sum+angle/2) * myRad*1.8;
				Point2D coord1 = transform(nodes.get(0));
				coord1.setLocation(cx, cy);
	//			System.out.println("Node name    " + lista.get(0).getName()  + "   cx:    " +cx + " cy: " +cy+ " my angle: " +angle );
				sum+=angle;
				circles(nodes, cx, cy);
				lista.get(0).setLocation(coord1);
				a++;
			}
		//}
//		System.out.println("LOGIKA 2*Math.Pi: "+Math.PI*2+" and it is: " + sum);
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
