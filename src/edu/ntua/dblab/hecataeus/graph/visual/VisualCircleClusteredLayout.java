package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.map.LazyMap;

import clusters.HAggloEngine;
import clusters.EngineConstructs.Cluster;
import clusters.EngineConstructs.ClusterSet;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;





public class VisualCircleClusteredLayout extends VisualCircleLayout {
	
	public enum ClusterE{
		Queries,
		Views,
		Relations,
		Circle;
	}
	
	protected ClusterE clusterType;
	protected VisualGraph graph;

	private List<String> files = new ArrayList<String>();
	
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();

	
	private List<VisualNode> RQV = new ArrayList<VisualNode>();
	

	
	private ClusterSet cs;
	
	private ArrayList<ArrayList<VisualNode>> vertices;

	protected VisualCircleLayout vcl;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	Map<VisualNode,Paint> vertexPaints =
			LazyMap.<VisualNode,Paint>decorate(new HashMap<VisualNode,Paint>(),
					new ConstantTransformer(Color.white));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<VisualEdge,Paint> edgePaints =
		LazyMap.<VisualEdge,Paint>decorate(new HashMap<VisualEdge,Paint>(),
				new ConstantTransformer(Color.blue));

	
	public VisualCircleClusteredLayout(VisualGraph g, ClusterE cluster){
		super(g);
		this.graph = g;
		this.clusterType = cluster;
		
		vcl = new VisualCircleLayout(this.graph);
		
		queries = new ArrayList<VisualNode>(vcl.queries);
		relations = new ArrayList<VisualNode>(vcl.relations);
		views = new ArrayList<VisualNode>(vcl.views);
		
		RQV = new ArrayList<VisualNode>(vcl.RQV);
		files = new ArrayList<String>(vcl.files);
		
	}
	

	@Override
	public void initialize() {

		HAggloEngine engine = new HAggloEngine(this.graph); 
		VisualCreateAdjMatrix cAdjM = new VisualCreateAdjMatrix(RQV);
		engine.executeParser(relations, queries, views, cAdjM.createAdjMatrix());
		engine.buildFirstSolution();
		cs = engine.execute(1);
		
		
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
		//omokentroi kikloi
		
		
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}


		double width = 0.0;
		double height = 0.0;
		double myRad = 0.0;
		Dimension d = getSize();
		height = d.getHeight();
		width = d.getWidth();
		myRad = 0.45 * (height < width ? height/2 : width/2);

		int a = 0;
		for(ArrayList<VisualNode> lista : vertices){
			
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);

			
			double angle = (2 * Math.PI )/ vertices.size();
			
			
			
			double cx = Math.cos(angle*a) * myRad;// + width / 2;
			double cy =	Math.sin(angle*a) * myRad;// + height/2;

			int b = 0, c =0;
			if(nodes.size() <= 15){
				for(VisualNode v : nodes){
					double smallRad = nodes.size()*2;
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					
					coord.setLocation(Math.cos(angleA*c+(cx))*smallRad + (a*nodes.size()),Math.sin(angleA*c+(cy))*smallRad +(a*nodes.size()));
					c++;
				}
			}else{
			for(VisualNode v : nodes){
					double smallRad = a*(nodes.size()/2);
					Point2D coord = transform(v);
					double angleA = (2 * Math.PI ) / nodes.size();
					
					coord.setLocation(Math.cos(angleA*b)*smallRad,Math.sin(angleA*b)*smallRad);
				b++;
			}
		}
			a++;
		}
	}

	private void clusterViews() {
		
		VisualEdgeBetweennessClustering cd = new VisualEdgeBetweennessClustering(this.graph);
		
		
	}

	
	public final Color[] similarColors =
		{
			new Color(216, 134, 134),
			new Color(135, 137, 211),
			new Color(134, 206, 189),
			new Color(206, 176, 134),
			new Color(194, 204, 134),
			new Color(145, 214, 134),
			new Color(133, 178, 209),
			new Color(103, 148, 255),
			new Color(60, 220, 220),
			new Color(30, 250, 100)
		};
	private void clusterQueries() {
		

		clusterAndRecolor(5, similarColors, true);
			
	//	colorCluster(queries, Color c)
		
//		groupCluster(AggregateLayout<Number,Number> layout, Set<Number> vertices)
				
	}

	public void clusterAndRecolor(int numEdgesToRemove, Color[] colors, boolean groupClusters) {
	
		Graph<VisualNode, VisualEdge> g = this.graph;
		AggregateLayout<VisualNode, VisualEdge> l = new AggregateLayout<VisualNode, VisualEdge>(new CircleLayout<VisualNode, VisualEdge>(g));
	
		EdgeBetweennessClusterer<VisualNode,VisualEdge> clusterer = new EdgeBetweennessClusterer<VisualNode,VisualEdge>(numEdgesToRemove);
		
		Set<Set<VisualNode>> clusterSet = clusterer.transform(g);
		
		List<VisualEdge> edges = clusterer.getEdgesRemoved();
		
		List<VisualNode> RQ = new ArrayList<VisualNode>();
		RQ.addAll(queries);
		RQ.addAll(relations);
		
		int i = 0;
	
	//	for (Iterator<Set<VisualNode>> cIt = clusterSet.iterator(); cIt.hasNext();) {
		for(VisualNode node :  RQ){
			Iterator<VisualNode> cIt = RQ.iterator();
			cIt.hasNext();
			VisualNode vertices = cIt.next();
			Color c = colors[i % colors.length];
	
			colorCluster(vertices, c);
			if(groupClusters == true) {
				groupCluster(l, RQ);
			}
			i++;
		}
		
		

		
		for (VisualEdge e : g.getEdges()) {
	
			if (edges.contains(e)) {
				edgePaints.put(e, Color.lightGray);
			} else {
				edgePaints.put(e, Color.black);
			}
		}
	
	}
	
	
	private void colorCluster(VisualNode v, Color c) {
		//for (VisualNode v : vertices) {
			vertexPaints.put(v, c);
		//}
	}

	private void groupCluster(AggregateLayout<VisualNode,VisualEdge> layout, List<VisualNode> vertices) {
		if(vertices.size() < this.graph.getVertexCount()) {
			Point2D center = layout.transform(vertices.iterator().next());
			Graph<VisualNode,VisualEdge> subGraph = SparseMultigraph.<VisualNode,VisualEdge>getFactory().create();
			for(VisualNode v : vertices) {
				subGraph.addVertex(v);
			}
			System.out.println(subGraph.getVertices());
			Layout<VisualNode,VisualEdge> subLayout = new CircleLayout<VisualNode,VisualEdge>(subGraph);
			subLayout.setInitializer(HecataeusViewer.vv.getGraphLayout());
			subLayout.setSize(new Dimension(40,40));

			layout.put(subLayout,center);
			HecataeusViewer.vv.setGraphLayout(subLayout);
			HecataeusViewer.vv.repaint();
		}
	}
	private double checkRad(ArrayList<ArrayList<VisualNode>> SoC, double myRad){
		double tempRad = 0;double ccircleR = 0;
		for(ArrayList<VisualNode>listaC: SoC){
			tempRad += getSmallRad(listaC);
		}	
			ccircleR = 2*tempRad;
			if(ccircleR>=2*Math.PI*myRad){
				System.out.println("AKTINA MEGALOU KIKLOY  "+(ccircleR/2*Math.PI));
				return (ccircleR/2*Math.PI);
			}
			else{
				System.out.println("aktina megalou kiklou"+myRad);
				return myRad;
			}
	}
	

	
	private void circles(List<VisualNode> nodes, double cx, double cy){
		int b = 0;
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				
				double smallRad = getSmallRad(relationsInCluster(nodes));
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / relationsInCluster(nodes).size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();
			}else{
				double smallRad = getSmallRad(nodes);
				Point2D coord = transform(v);
				double angleA = 0.0;
				if(relationsInCluster(nodes).size() > 1){
					angleA = (2 * Math.PI ) / (nodes.size()-relationsInCluster(nodes).size());
				}else{
					angleA = (2 * Math.PI ) / nodes.size();
				}
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));
				HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
				HecataeusViewer.vv.repaint();
			}
			b++;
		}
		
	}
	
	
	
	
	private void old(){
		List<Cluster> clusters = new ArrayList<Cluster>(cs.getClusters());
		vertices = new ArrayList<ArrayList<VisualNode>>();
		for(Cluster cl : clusters){
			vertices.add(cl.getNode());
		}
		
//		System.out.println(vertices);

		double width = 0.0;
		double height = 0.0;
		double myRad = 0.0;
		Dimension d = getSize();
		height = d.getHeight();
		width = d.getWidth();
//		relationRadius = 0.45 * (height < width ? height/3 : width/3);
//		queryRadius = 0.45 * (height < width ? queries.size()/3 : queries.size()/3);
		myRad = 0.45 * (height < width ? height/2 : width/2);

		
		int eva = 0;
		int a = 0;
		for(ArrayList<VisualNode> lista : vertices){
			
			List<VisualNode> nodes = new ArrayList<VisualNode>();
			Collections.sort(lista, new CustomComparator());
			nodes.addAll(lista);
//			System.out.println(nodes);
			
			double angle = (2 * Math.PI )/ vertices.size();
			
			myRad = Math.exp(a+1);
			
			double cx = Math.cos(angle*a) * myRad;// + width / 2;
			double cy =	Math.sin(angle*a) * myRad;// + height/2;
			Point2D coord1 = transform(nodes.get(0));
			coord1.setLocation(cx, cy);
			System.out.println("  node   " + nodes.get(0).getType()) ;
			int yloc = 100;
			int b = 0;
			for(VisualNode v : nodes){
				
				double smallRad = 0.45 * nodes.size();
				Point2D coord = transform(v);
				double angleA = (2 * Math.PI ) / nodes.size();
				coord.setLocation(Math.cos(angleA*b)*smallRad+(cx),Math.sin(angleA*b)*smallRad+(cy));

			//	HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(new VisualClusteredNodeColor(v, HecataeusViewer.vv.getPickedVertexState()));
			//	HecataeusViewer.vv.repaint();					
				b++;
			}
			a++;
		}
	}

	@Override
	public void reset() {
		initialize();
		
	}

}
