package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;


@SuppressWarnings("serial")
public class VisualSubGraph extends EvolutionGraph<VisualNode, VisualEdge>{

	protected VisualizationViewer<VisualNode, VisualEdge> myViewer;
	
	protected Map<VisualNode, Integer> nodeKeys;
	protected Map<VisualEdge, Integer> edgeKeys;
	
	
	
	public VisualSubGraph(VisualizationViewer<VisualNode, VisualEdge> viewer){
		this.myViewer = viewer;
		nodeKeys = new HashMap<VisualNode, Integer>();
		edgeKeys = new HashMap<VisualEdge, Integer>();
//		VisualGraph subGraph = graph;
//		for (VisualNode v : graph.getVertices())
//			subGraph.setLocation(v, this.getLocation(v)); 
//		for (VisualEdge e : graph.getEdges())
//			subGraph.addEdge(e);
	}
	
	public void setViewerToSubGraph(VisualizationViewer<VisualNode, VisualEdge> viewer){
		this.myViewer = viewer;
	}
	
//	public VisualSubGraph toSubGraph(List<VisualNode> nodes){
//		VisualSubGraph subGraph = (VisualSubGraph)toGraphE(nodes);
//		for (VisualNode v : nodes)
//			subGraph.setLocation(v, this.getLocation(v)); 
//		return subGraph;
//	}
	
	
	public boolean addVertex(VisualNode Node) {
		// assign key
		nodeKeys.put(Node, ++super._KeyGenerator);
		return super.addVertex(Node);
	}

	
	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(VisualEdge Edge, VisualNode fromNode, VisualNode toNode) {
		return this.addEdge(Edge);		
	}
	
	
	public VisualGraph toGraph(VisualSubGraph subGraph){
		Collection<VisualNode> nodes = new ArrayList<VisualNode>();
		List<VisualNode> newNodes = new ArrayList<VisualNode>();
		nodes = subGraph.getVertices();
		for(VisualNode test : nodes){
			newNodes.add(test);
		}
		VisualGraph graph = super.toGraphE(newNodes);
	//	VisualGraph graph = subGraph;
		return graph;
	}
}
