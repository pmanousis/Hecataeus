package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;

public class VisualAggregateLayout extends AggregateLayout<VisualNode, VisualEdge>{

	VisualLayoutType topLayoutType;
	VisualLayoutType subLayoutType;
	VisualGraph graph;
	
	public VisualAggregateLayout(VisualGraph graph, VisualLayoutType topLayoutType, VisualLayoutType subLayoutType) {
		super(VisualLayoutType.getLayoutFor(graph, topLayoutType));
		this.graph = graph;
		this.setTopLayoutType(topLayoutType);
		this.setSubLayoutType(subLayoutType);
	}
	
	public VisualLayoutType getTopLayoutType() {
		return topLayoutType;
	}

	public void setTopLayoutType(VisualLayoutType topLayoutType) {
		this.topLayoutType = topLayoutType;
		// the container layout
		
		//get the top - level graph
		VisualGraph topGraph = graph.toGraph(graph.getVertices(NodeCategory.MODULE));
		//create the layout only for top level nodes
		Layout<VisualNode,VisualEdge> topLayout = VisualLayoutType.getLayoutFor(topGraph, topLayoutType);
		//pass the old size to the new layout
		topLayout.setSize(this.getSize());
		//set the new layout as delegate layout
		super.setDelegate(topLayout);
	}

	public VisualLayoutType getSubLayoutType() {
		return subLayoutType;
	}

	public void setGraph(VisualGraph graph) {
		super.setGraph(graph);
		this.graph = graph;
	}
	
	public VisualGraph getGraph() {
		return graph;
	}
	
	public void setSubLayoutType(VisualLayoutType subLayoutType) {
		this.subLayoutType = subLayoutType;
		super.layouts.clear();
		//set the layout for the low level nodes
		List<VisualNode> topLevelNodes = graph.getVertices(NodeCategory.MODULE); 
		for (VisualNode parentNode : topLevelNodes) {
			List<VisualNode> subGraphNodes = graph.getModule(parentNode);
			subGraphNodes.remove(parentNode);
			VisualGraph subGraph =  graph.toGraph(subGraphNodes);
			Layout<VisualNode, VisualEdge> newsubLayout = VisualLayoutType.getLayoutFor(subGraph, subLayoutType);
			if (newsubLayout.getSize()==null) {
				Dimension newSize = new Dimension();
				newSize.setSize(super.delegate.getSize().getHeight()/4, this.delegate.getSize().getWidth()/4);
				newsubLayout.setSize(newSize);
			}
			this.put(newsubLayout,parentNode.getLocation());
		}
	}
	public void setLocation(VisualNode v, Point2D location) {
		super.setLocation(v, location);
		v.setLocation(super.transform(v));
		
	}
}
