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
	


	
	
	
	
	private Map sortByComparator(Map unsortedMap) {
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
	
	
	private int QwithOutEdges(List<VisualNode> nodes){  //posa q rotane panw apo mia r
		int queriesInCluster = 0;
		List<VisualNode> q = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				int cnt = 0;
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v.getOutEdges());
				for(VisualEdge e : edges){
					if(e.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
						cnt++;
						if(cnt > 1){
							if(!q.contains(v)){
								q.add(v);
							}
						}
					}
				}
			}
		}
		queriesInCluster = q.size();
		return queriesInCluster;
	}
	
	
	
    private void circles(List<VisualNode> nodes, double cx, double cy){
        int b = 0, relwithoutQ = 0;
        ArrayList<VisualNode> rc = new ArrayList<VisualNode>();
        ArrayList<VisualNode> qc = new ArrayList<VisualNode>();
        ArrayList<VisualNode> vc = new ArrayList<VisualNode>();
        rc.addAll(relationsInCluster(nodes));
        System.out.println("   to RC   " + rc);
        ArrayList<VisualNode> jq = new ArrayList<VisualNode>();
        
        Map<ArrayList<VisualNode>, Integer> set = new HashMap<ArrayList<VisualNode>, Integer>();
        
        for(VisualNode n : nodes){
                if(n.getType()==NodeType.NODE_TYPE_QUERY){
                        qc.add(n);
                }
                else if(n.getType()==NodeType.NODE_TYPE_VIEW){
                	vc.add(n);
                }
        }
        int singleQinCl = nodes.size() - rc.size() - QwithOutEdges(nodes) - vc.size();

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
                        if(set.containsKey(QtR)){
                            set.put(QtR, set.get(QtR)+1);
                        }else{
                            set.put(QtR, 1);
                        }
                }
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
                for(VisualNode node1 : rc){
                	if(!sortedR.contains(node1)){
                		sortedR.add(node1);
                	}
                }
                rc.clear();
                rc.addAll(sortedR);
        }
        
        
        
        double relationRad = 1.9*getSmallRad(rc);
//        double relationAngle = (2 * Math.PI ) / rc.size();
        double qRad = getQueryRad(nodes.size() - rc.size()- vc.size());
        int Q = singleQinCl;//nodes.size() - rc.size();
        double qAngle = 0;
        double sAngle = 0;
        for(VisualNode r : rc){
                
                ArrayList<VisualNode> queriesforR = new ArrayList<VisualNode>();
//                ArrayList<VisualNode> viewsforR = new ArrayList<VisualNode>();
                for(VisualEdge e : r.getInEdges()){
                        if(e.getFromNode().getType() == NodeType.NODE_TYPE_QUERY ){
                                VisualNode q = e.getFromNode();
                                ArrayList<VisualEdge> qEdges = new ArrayList<VisualEdge>(q.getOutEdges());
                                int cnt = 0;
                                for(VisualEdge ed : qEdges){
                                        if(ed.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
                                                cnt++;
                                        }
                                }
                                if(cnt==1){ //&&e.getFromNode().getType() != NodeType.NODE_TYPE_VIEW){
                                        queriesforR.add(q);
                                }
//                                if(e.getFromNode().getType() == NodeType.NODE_TYPE_VIEW)
//                                {
//                                	viewsforR.add(q);
//                                }
                                else{
                                        jq.add(q);
                                }
                                
                        }
                }
                System.out.println("  rel name " + r.getName() + "  my q  " + queriesforR);
                
                qAngle = placeQueries(queriesforR, cx, cy, qRad, qAngle, Q);
                placeViews(vc, relationRad, qRad, cx, cy);

	                Point2D coord = transform(r);
	                sAngle += qAngle;
	                double rx = 0;
	                double ry = 0;
//	                if(cx < 0){
//	                	rx = Math.cos((-1)*(sAngle-(qAngle/2)))*relationRad+(cx);
//	 	                ry = Math.sin((sAngle-(qAngle/2)))*relationRad+(cy);
//	                }else{
	                rx = Math.cos(sAngle-(qAngle/2))*relationRad+(cx);
	                ry = Math.sin(sAngle-(qAngle/2))*relationRad+(cy);
//	                }
	                coord.setLocation(rx, ry);
	                r.setLocation(coord);
	                r.setNodeAngle(sAngle-(qAngle/2));
	//                System.out.println("set node angle    " + (sAngle-(qAngle/2)));
	//                System.out.println("get node angle name"+ r.getName() + "   angle  "+ r.getNodeAngle());

 //               }
                
                HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(r, HecataeusViewer.getActiveViewer().getPickedVertexState()));
                HecataeusViewer.getActiveViewer().repaint();
        }
		
		double jqRad = qRad + 40;
		//double jqAngle = (2 * Math.PI ) / jq.size();
		double c = 0;
		for(VisualNode v : jq){
			
	//		ArrayList<VisualEdge> edgesToR = new ArrayList<VisualEdge>();
			ArrayList<VisualNode> myR = new ArrayList<VisualNode>();
			for(VisualEdge myEdge : v.getOutEdges()){
				if(myEdge.getToNode().getType() == NodeType.NODE_TYPE_RELATION){
					myR.add(myEdge.getToNode());
				}
			}
			double myAngle = 0.0;
			for(VisualNode rel : myR){
				myAngle += rel.getNodeAngle();
			//	System.out.println("rel name  "+ rel.getName() +"  angle  "+  rel.getNodeAngle());
			}
			//myAngle = myAngle/myR.size() + c;
			myAngle = myAngle/2 + c;
			c += 0.09;
			
			Point2D coord = transform(v);
			double jqx = Math.cos(myAngle)*jqRad+(cx);
			double jqy = Math.sin(myAngle)*jqRad+(cy);
			coord.setLocation(jqx, jqy);

			v.setLocation(coord);
			v.setNodeAngle(myAngle);
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
		clusterId++;
		VisualCluster cluster = new VisualCluster(qRad, rc, vc, qc, cx, cy, clusterId);
		cluster.printInClusterEdges();
		
	}
	
	private double placeQueries(ArrayList<VisualNode> queriesforR,  double cx, double cy, double qRad, double qAngle, int Q) {
		
		double sAngle = 0.0;
		double Angle = ((2 * Math.PI ) / Q);   //+qAngle;
		qRad = getQueryRad(Q)*1.1;
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
	
	private void placeViews(ArrayList<VisualNode> vc, double relationRad, double queryRad, double cx, double cy){
		double viewRad = (queryRad + relationRad)/2;
		double angle = (2*Math.PI)/vc.size();
		int va =0;
		for(VisualNode v : vc){
			Point2D coord = transform(v);
			coord.setLocation((Math.cos(angle*va)*viewRad + cx), (Math.sin(angle*va)*viewRad + cy));
			v.setLocation(coord);
			v.setNodeAngle(angle);
			va++;
			HecataeusViewer.getActiveViewer().getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.getActiveViewer().getPickedVertexState()));
			HecataeusViewer.getActiveViewer().repaint();
		}
		
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
