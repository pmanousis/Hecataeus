package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;



public class VertexCollapser {
	
	
	GraphCollapser collapser;
	private VisualGraph originalGraph;
	public VertexCollapser(Set<VisualNode> picked, VisualGraph graph, VisualizationViewer<VisualNode,VisualEdge> vv, Layout<VisualNode, VisualEdge> layout){
	//	Collection pickedN = new HashSet(picked);
		this.originalGraph = graph;
		List<VisualNode> pickedN = new ArrayList<VisualNode>(picked);
		
		
		VisualGraph myclusterGraph = getClusterGraph(graph, pickedN);
		
		VisualGraph myNewGraph = collapse(graph, myclusterGraph);
		
		List<VisualEdge> inedges = new ArrayList<VisualEdge>();
		List<VisualEdge> outedges = new ArrayList<VisualEdge>();
		String name = "";
		NodeType type = null;
		if(pickedN.size() > 1) {
			double x = 0;
			double y = 0;
			
			for(VisualNode v : pickedN){
				inedges = v._inEdges;
				outedges = v._outEdges;
				name += v.getName();
				x += v.getLocation().getX();
				y += v.getLocation().getY();
				type = v.getType();
				v.setVisible(false);
			}
			
	//		VisualNode newNode = new VisualNode();

	//		newNode._inEdges = inedges;
	//		newNode._outEdges = outedges;
	//		graph.setLocation(newNode,new Point2D.Double(1000, 1000) );
	//		newNode.setLocation(new Point2D.Double(100/2, 100/2));
			
	//		newNode.setName(name);
	//		newNode.setType(type);
			
//			vv.getRenderContext().setVertexShapeTransformer(new ClusterVertexShapeFunction());
	//		newNode.setVisible(true);
		//	layout.transform(newNode);

			Point2D cp = new Point2D.Double(x/picked.size(), y/picked.size());
			vv.getRenderContext().getParallelEdgeIndexFunction().reset();
			layout.setGraph(myNewGraph);
			((Layout)layout).setLocation(myclusterGraph, cp);

 			vv.getPickedVertexState().clear();
			
//			graph.addVertex(newNode);
//			layout.setGraph(graph);
//			System.out.println(newNode._inEdges + "     " + newNode._outEdges);
//			System.out.println(graph.getVertices());
//			System.out.println(" thesi   " + graph.getLocation(newNode));
			
			
			vv.repaint();
			

		}
		
		
		
		
		

	}
	VisualGraph createGraph() throws InstantiationException, IllegalAccessException {
		return (VisualGraph)originalGraph.getClass().newInstance();
	}

	
	public VisualGraph getClusterGraph(VisualGraph inGraph, List<VisualNode> pickedNodes) {
		VisualGraph clusterGraph;
		try {
			clusterGraph = createGraph();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		for(VisualNode v : pickedNodes) {
			clusterGraph.addVertex(v);
			List<VisualEdge> edges = new ArrayList<VisualEdge>(inGraph.getIncidentEdges(v));
		//	Collection edges = inGraph.getIncidentEdges(v);
			for(VisualEdge edge : edges) {
				Pair endpoints = inGraph.getEndpoints(edge);
				VisualNode v1 = (VisualNode) endpoints.getFirst();
				VisualNode v2 = (VisualNode) endpoints.getSecond();
				if(pickedNodes.containsAll(endpoints)) {
					clusterGraph.addEdge(edge, v1, v2, inGraph.getEdgeType(edge));
				}
			}
		}
		return clusterGraph;
	}
	
	
	
	public VisualGraph collapse(VisualGraph inGraph, VisualGraph clusterGraph) {
		
	//	Graph evaInGraph = inGraph;
	//	Graph evaClusterGraph = clusterGraph.toG(clusterGraph);
		
		
//		if(evaClusterGraph.getVertexCount() < 2) return inGraph;
		
		//VisualGraph graph = inGraph;
		//Graph graph = evaInGraph;
//			try {
//				graph = createGraph();
//			} catch(Exception ex) {
//				ex.printStackTrace();
//			}
		Collection cluster = clusterGraph.getVertices();
		
		// add all vertices in the delegate, unless the vertex is in the
		// cluster.
		for(VisualNode v : inGraph.getVertices()) {
			if(cluster.contains(v) == false) {
				inGraph.addVertex(v);
			}
		}
		// add the clusterGraph as a vertex
		
		
		inGraph.addVertexEVA(clusterGraph);
		
		//DirectedSparseGraph hgraph = (DirectedSparseGraph)inGraph;
		
		//hgraph.addVertex(clusterGraph);
		
		//Object o=hgraph.getClass().getSuperclass().getSuperclass().addVertex(clusterGraph);
		
		//add all edges from the inGraph, unless both endpoints of
		// the edge are in the cluster
//		for(VisualEdge e : inGraph.getEdges()) {
//			Pair endpoints = inGraph.getEndpoints(e);
//			// don't add edges whose endpoints are both in the cluster
//			if(cluster.containsAll(endpoints) == false) {
//		
//				if(cluster.contains(endpoints.getFirst())) {
//					
//					((Graph)inGraph).addEdge(e, clusterGraph, endpoints.getSecond(), inGraph.getEdgeType(e));
//					
//				} else if(cluster.contains(endpoints.getSecond())) {
//					((Graph)inGraph).addEdge(e, endpoints.getFirst(), clusterGraph, inGraph.getEdgeType(e));
//		
//				} else {
//					((Graph)inGraph).addEdge(e, endpoints.getFirst(), endpoints.getSecond(), inGraph.getEdgeType(e));
//				}
//			}
//		}
//		VisualGraph finalG = new VisualGraph();
//		List<VisualNode> myList = new ArrayList<VisualNode>(graph.getVertices());
//		
//		finalG = inGraph.toGraph(myList);
		return inGraph;
	}
	
	
	
	class ClusterVertexShapeFunction<V> extends EllipseVertexShapeTransformer<V> {
	 	 
		ClusterVertexShapeFunction() {
			setSizeTransformer(new ClusterVertexSizeFunction<V>(20));
		}
		@Override
		public Shape transform(V v) {
			
			return factory.getRegularStar(v, 8);


		}
		
		class ClusterVertexSizeFunction<V> implements Transformer<V,Integer> {
			int size;
			public ClusterVertexSizeFunction(Integer size) {
				this.size = size;
			}
	 	 
			public Integer transform(V v) {
				
				return  	30 	;

			}
		}
		
	}
	
	
	
	
	
	
	
	
}
