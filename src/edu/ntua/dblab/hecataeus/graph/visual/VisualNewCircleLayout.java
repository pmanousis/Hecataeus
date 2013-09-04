package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.map.LazyMap;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;

public class VisualNewCircleLayout <V, E> extends AbstractLayout<V,E> {

	private double radius;
	private double relationRadius;
	private double viewRadius;
	private double queryRadius;
	
	private List<V> vertex_ordered_list;
	
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	private List<VisualNode> nodes;
	
	Map<V, CircleVertexData> circleVertexDataMap =
			LazyMap.decorate(new HashMap<V,CircleVertexData>(), 
			new Factory<CircleVertexData>() {
				public CircleVertexData create() {
					return new CircleVertexData();
				}});	
	
	/**
	 * Creates an instance for the specified graph.
	 */
	public VisualNewCircleLayout(Graph<V,E> g) {
		super(g);
		nodes = new ArrayList<VisualNode>((Collection<? extends VisualNode>) g.getVertices());
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				relations.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
			}
		}
	}

	/**
	 * Returns the radius of the circle.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the circle.  Must be called before
	 * {@code initialize()} is called.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Sets the order of the vertices in the layout according to the ordering
	 * specified by {@code comparator}.
	 */
//	public void setVertexOrder(Comparator<V> comparator)
//	{
//	    if (vertex_ordered_list == null)
//	        vertex_ordered_list = new ArrayList<V>(getGraph().getVertices());
//	    Collections.sort(vertex_ordered_list, comparator);
//	}

	
	public void setRelationVertexOrder(Comparator<V> comparator){
	    if (relations == null){
	    	for(VisualNode v : nodes){
	    		if(v.getType() == NodeType.NODE_TYPE_RELATION)
					relations.add(v);
	    	}
	    }
	    Collections.sort((List)relations, comparator);
	}
	
	
	public void setViewVertexOrder(Comparator<V> comparator){
	    if (views == null){
	    	for(VisualNode v : nodes){
	    		if(v.getType() == NodeType.NODE_TYPE_VIEW)
					views.add(v);
	    	}
	    }
	    Collections.sort((List)views, comparator);
	}
	
	
	public void setQueryVertexOrder(Comparator<V> comparator)
	{
	    if (queries == null){
	    	for(VisualNode v : nodes){
	    		if(v.getType() == NodeType.NODE_TYPE_QUERY)
					queries.add(v);
	    	}
	    }
	    Collections.sort((List)queries, comparator);
	}
	
    /**
     * Sets the order of the vertices in the layout according to the ordering
     * of {@code vertex_list}.
     */
	public void setVertexOrder(List<V> vertex_list)
	{
	    if (!vertex_list.containsAll(getGraph().getVertices())) 
	        throw new IllegalArgumentException("Supplied list must include " +
	        		"all vertices of the graph");
	    this.vertex_ordered_list = vertex_list;
	}
	
	public void reset() {
		initialize();
	}

	public void initialize() 
	{
		Dimension d = getSize();
		
		if (d != null) {
//			if (vertex_ordered_list == null) 
//				setVertexOrder(new ArrayList<V>(getGraph().getVertices()));

			double height = d.getHeight();
			double width = d.getWidth();

			
//			if (radius <= 0) {
//				radius = 0.45 * (height < width ? height : width);
//			}
			double max = 0;
			double medium = 0;
			double small = 0;

			List<Integer> sizes = new ArrayList<Integer>();
			sizes.add(queries.size());
			sizes.add(relations.size());
			sizes.add(views.size());

			Collections.sort(sizes);
			
			if (relationRadius <= 0) {
//				relationRadius = 0.45 * (height < width ? height/3 : width/3);
//				relationRadius = 0.45 * (height < width ? height : width);
				if(relations.size() == sizes.get(0)){           // ta relations einai ta ligotera
					relationRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				}else if(relations.size() == sizes.get(1)){     //ta relations einai ta mesaia
					relationRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				}else{    // ta relations einai ta perissotera
					relationRadius = 0.45 * (height < width ? height : width);
				}
			}
			
			if (viewRadius <= 0) {
//				viewRadius = 0.45 * (height < width ? height/2 : width/2);
//				viewRadius = 0.45 * (height < width ? height*2 : width*2);
//				viewRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				if(views.size() == sizes.get(0)){           // ta views einai ta ligotera
					viewRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				}else if(views.size() == sizes.get(1)){     //ta views einai ta mesaia
					viewRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				}else{    // ta views einai ta perissotera
					viewRadius = 0.45 * (height < width ? height : width);
				}
			}
			
			if (queryRadius <= 0) {
//				queryRadius = 0.45 * (height < width ? height : width);
//				queryRadius = 0.45 * (height < width ? height*3 : width*3);
//				queryRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				if(queries.size() == sizes.get(0)){           // ta queries einai ta ligotera
					queryRadius = 0.45 * (height < width ? height*sizes.get(0)/sizes.get(1) : width*sizes.get(0)/sizes.get(1));
				}else if(queries.size() == sizes.get(1)){     //ta queries einai ta mesaia
					queryRadius = 0.45 * (height < width ? height*sizes.get(1)/sizes.get(2) : width*sizes.get(1)/sizes.get(2));
				}else{    // ta queries einai ta perissotera
					queryRadius = 0.45 * (height < width ? height : width);
				}
			}
			
			
//			int i = 0;
//			for (V v : vertex_ordered_list){
//				Point2D coord = transform(v);
//				
//				double angle = (2 * Math.PI * i) / vertex_ordered_list.size();
//
//				coord.setLocation(Math.cos(angle) * radius + width / 2,
//						Math.sin(angle) * radius + height / 2);
//
//				CircleVertexData data = getCircleData(v);
//				data.setAngle(angle);
//				i++;
//			}
			
			
			int j = 0;
			for (V v : (List<V>)relations){
				Point2D coord = transform(v);
				
				double angle = (2 * Math.PI * j) / relations.size();

				coord.setLocation(Math.cos(angle) * relationRadius + width / 2,
						Math.sin(angle) * relationRadius + height / 2);

				CircleVertexData data = getCircleData(v);
				data.setAngle(angle);
				j++;
			}
			
			
			
			
			int k = 0;
			for (V v : (List<V>)views){
				Point2D coord = transform(v);
				
				double angle = (2 * Math.PI * k) / views.size();

				coord.setLocation(Math.cos(angle) * viewRadius + width / 2,
						Math.sin(angle) * viewRadius + height / 2);

				CircleVertexData data = getCircleData(v);
				data.setAngle(angle);
				k++;
			}
			
			
			
			
			int z = 0;
			for (V v : (List<V>)queries){
				Point2D coord = transform(v);
				
				double angle = (2 * Math.PI * z) / queries.size();

				coord.setLocation(Math.cos(angle) * queryRadius + width / 2,
						Math.sin(angle) * queryRadius + height / 2);

				CircleVertexData data = getCircleData(v);
				data.setAngle(angle);
				z++;
			}
			
		}
	}

	protected CircleVertexData getCircleData(V v) {
		return circleVertexDataMap.get(v);
	}

	protected static class CircleVertexData {
		private double angle;

		protected double getAngle() {
			return angle;
		}

		protected void setAngle(double angle) {
			this.angle = angle;
		}

		@Override
		public String toString() {
			return "CircleVertexData: angle=" + angle;
		}
	}
}
