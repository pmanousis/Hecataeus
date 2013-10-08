package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.TreeMap;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
/***
 * 
 * @author gpapas
 * VisualAggregateLayout extends Aggregate Layout 
 * It accepts 2 layout types, topLayoutType is for the delegate layout
 * and subLayoutType is for the sub layouts
 *
 */
public class VisualAggregateLayout extends AggregateLayout<VisualNode, VisualEdge> {
	
	VisualLayoutType topLayoutType;
	VisualLayoutType subLayoutType;
	VisualGraph graph;
	VisualGraph topLayoutGraph;
	Map<VisualNode, VisualGraph> subGraphs;
	
	/***
	 * Constructs a new VisualAggregateLayout class
	 * @param graph : initializes the graph of the layout 
	 * @param topLayoutType : initializes the type of top layout 
	 * @param subLayoutType : initializes the type of the sub layouts
	 */
	public VisualAggregateLayout(VisualGraph graph, VisualLayoutType topLayoutType, VisualLayoutType subLayoutType) {
		super(VisualLayoutType.getLayoutFor(graph, topLayoutType));
		this.graph = graph;
		this.topLayoutGraph = graph;
		this.setSize(this.graph.getSize());
		this.topLayoutType = topLayoutType;
		this.subLayoutType = subLayoutType;
//		this.subGraphs = new HashMap<VisualNode, VisualGraph>();
		this.subGraphs = new TreeMap<VisualNode, VisualGraph>();
	}
	
	/***
	 * Sets the type of top layout 
	 * @param topLayoutType: VisualLayoutType
	 */
	public void setTopLayoutType(VisualLayoutType topLayoutType) {
		this.topLayoutType = topLayoutType;
		//create the layout only for top level nodes
		Layout<VisualNode,VisualEdge> topLayout = VisualLayoutType.getLayoutFor(this.topLayoutGraph, this.topLayoutType);
		//pass the old size to the new layout
		topLayout.setSize(this.getSize());
		//set the new layout as delegate layout
		for (VisualNode v : this.topLayoutGraph.getVertices()){
			super.setLocation(v, topLayout.transform(v));
		}
	}
	
	/***
	 * Gets the type of the top layout
	 * @return VisualLayoutType
	 */
	public VisualLayoutType getTopLayoutType() {
		return topLayoutType;
	}
	
	/***
	 * Sets the graph of the top layout
	 * @param topGraph : the VisualGraph of the top layout
	 */
	public void setTopLayoutGraph(VisualGraph topGraph) {
		this.topLayoutGraph =  topGraph;
		subGraphs.clear();
	}
	
	/***
	 * Gets the graph of the top layout
	 * @param topGraph : the VisualGraph of the top layout
	 */
	public VisualGraph getTopLayoutGraph() {
		return this.topLayoutGraph ;
	}
	
	/***
	 * Gets the type of the sub layouts
	 * @return VisualLayoutType
	 */
	public VisualLayoutType getSubLayoutType() {
		return subLayoutType;
	}
	
	/***
	 * Sets the type of sub layouts 
	 * @param subLayoutType: VisualLayoutType
	 * @param initialPosition: the position of the first vertex placed in the layout
	 */
	public void setSubLayoutType(VisualLayoutType subLayoutType, Point2D initialPosition) {
		this.subLayoutType = subLayoutType;
		this.getLayouts().clear();
		for (VisualNode topNode : subGraphs.keySet()) {
			Layout<VisualNode, VisualEdge> newsubLayout = VisualLayoutType.getLayoutFor(subGraphs.get(topNode), subLayoutType,initialPosition);
			if (newsubLayout.getSize()==null) {
				Dimension newSize = new Dimension();
				newSize.setSize(super.delegate.getSize().getHeight()/4, this.delegate.getSize().getWidth()/4);
				newsubLayout.setSize(newSize);
			}
			this.put(newsubLayout,this.transform(topNode));
		}
		
	}
	
	/***
	 * Sets the type of sub layouts 
	 * @param subLayoutType: VisualLayoutType
	 * @param initialPosition: the position of the first vertex placed in the layout
	 */
	public void setSubLayoutType(VisualLayoutType subLayoutType) {
		this.setSubLayoutType(subLayoutType, null);	
	}
	
	
	/***
	 * Sets the graph of a sub layout according to the location of the top node
	 * @param subGraph : the VisualGraph of the sub layout
	 * @param topNode : the top node of the sub layout
	 */
	public void setSubLayoutGraph(VisualNode topNode, VisualGraph subGraph) {
		subGraphs.put(topNode, subGraph);
	}
	
	/***
	 * Gets the graph of a sub layout according to the top node
	 * @param topNode : the top node of the sub layout
	 */
	public VisualGraph getSubLayoutGraph(VisualNode topNode) {
		return subGraphs.get(topNode);
	}
	
	/***
	 * Sets the graph of the entire layout
	 * @param graph : the VisualGraph of the layout
	 */
	public void setGraph(VisualGraph graph) {
		super.setGraph(graph);
		this.graph = graph;
		this.topLayoutGraph = graph;
		subGraphs.clear();
	}
	/***
	 * Gets the graph of the entire layout
	 * @return the VisualGraph of the layout
	 */
	public VisualGraph getGraph() {
		return graph;
	}
	
	/***
	 * Sets the location of node v 
	 * @param v : VisualNode
	 * @param location : a Point2D location
	 */
	public void setLocation(VisualNode v, Point2D location) {
		super.setLocation(v,location);
		graph.setLocation(v,location);
	}
}
