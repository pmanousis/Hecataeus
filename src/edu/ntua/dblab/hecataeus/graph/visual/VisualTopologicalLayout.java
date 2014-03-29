package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;

public class VisualTopologicalLayout extends AbstractLayout<VisualNode,VisualEdge>{

	public static int cntQuery = 100;
	public static int cntRelation = 100;
	public static int cntView = 100;
	public static int cnt4 = 0;
	public static int cnt5 = 0;
	
	public enum Orientation{
		RIGHT2LEFT,
		DOWN2TOP,
		LEFT2RIGHT,
		TOP2DOWN,
		INVERSELEFT2RIGHT,
		ZoomedLayoutForModules,
		ZoomedLayoutForModulesV2
		;
	}
	
	private Point2D.Double OFFSET = new Point2D.Double();
	
	private Orientation orientation;
	
	VisualGraph graph;
	
    /**
     * Creates an instance for the specified graph and size with default orientation
     */
    public VisualTopologicalLayout(Graph<VisualNode, VisualEdge> graph) {
    	super(graph);
    	this.graph = (VisualGraph) graph;
    	this.orientation = Orientation.RIGHT2LEFT;
    }
	/**
     * Creates an instance for the specified graph and orientation
     */
    public VisualTopologicalLayout(Graph<VisualNode, VisualEdge> graph, Orientation orientation)  {
    	super(graph);
    	this.graph = (VisualGraph) graph;
    	this.orientation = orientation;
    }

    /**
     * Creates an instance for the specified graph, orientation , and initialPosition of the first vertex
     */
    public VisualTopologicalLayout(Graph<VisualNode, VisualEdge> graph, Orientation orientation, Point2D.Double initialLocation)  {
    	super(graph);
    	this.graph = (VisualGraph) graph;
    	this.orientation = orientation;
    }

    
    /**
     * Creates an instance for the specified graph, size and orientation.
     */
    public VisualTopologicalLayout(Graph<VisualNode, VisualEdge> graph, Dimension size, Orientation orientation)  {
    	super(graph, size);
    	this.graph = (VisualGraph) graph;
    	this.orientation = orientation;
    	
    }
    
    public void initialize() {
//    	System.out.println("orientation   " + this.orientation.toString());
    	switch (this.orientation) {
    	case RIGHT2LEFT:
    		initializeRight2Left();
    		break;
    	case DOWN2TOP:
    		initializeBottomUp();
    		break;
    	case LEFT2RIGHT:
    		initializeLeft2Right();
    		break;
    	case INVERSELEFT2RIGHT:
    		initializeInverseLeft2Right();
    		break;
    	case TOP2DOWN:
    		initializeUpBottom();
    		break;
    	case ZoomedLayoutForModulesV2:
    		init();
    		ZoomedLayoutForModulesV2();
    		break;
    	case ZoomedLayoutForModules:
    		init();
    		ZoomedLayoutForModules();
    		break;
    	default:
    		initializeRight2Left();
    	}
    }

    private void init(){
    	cntView = 100;
    	cntQuery = 100;
    	cntRelation = 100;
    }
    
	public void reset() {
		this.initialize();
	}
	
	private Point2D initialPosition = new Point2D.Double();
	
	// Gets the initial location of the vertex in the layout
	public Point2D getInitialPosition() {
		return initialPosition;
	}
	public void setInitialPosition(Point2D initialPosition2) {
		this.initialPosition = initialPosition2;
	}
	
	/***
	 * initialize the locations of nodes 
	 * according to the topological sort layout, 
	 * starting from the right side towards the left side of the screen
	 * First, nodes with <b>no outgoing</b> edges are placed at the right side 
	 * Then all nodes connected via an <b>outgoing edge</b> with placed nodes  are placed at the left 
	 */
	private void initializeRight2Left(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),0);
		else
			initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the outEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> outEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			outEdges.put(v, new ArrayList<VisualEdge>(this.graph.getOutEdges(v)));
			
		while (nodes.size()>0) {
			//hold the current group of nodes for the layout
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: nodes) {
				if (outEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			//Initialize the offset according to the number of vertices in current group
			OFFSET.setLocation(new Point2D.Double(-this.getSize().width/2,Math.max(this.getSize().getHeight()/visualizedGroup.size(),60)));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// set the location of the current vertex
				super.setLocation(v,location);
				// remove in edges
				for(VisualEdge e: this.graph.getInEdges(v)){
					outEdges.get(e.getFromNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
				
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX()/2, location.getY()-100+ OFFSET.getY());   //TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			}
			//set the location of the next group that is visualized
			location.setLocation(location.getX()+ OFFSET.getX(), initialPosition.getY());
		}
	}	
	
	/***
	 * initialize the locations of nodes according to the topological sort layout, 
	 * starting from the left side towards the right side of the screen
	 * First, nodes with <b>no incoming</b> edges are placed at the left side 
	 * Then all nodes connected via an <b>outgoing edge</b> with placed nodes are placed at the right 
	 */
	private void initializeInverseLeft2Right(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),0);
		else
			initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the inEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> inEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			inEdges.put(v, new ArrayList<VisualEdge>(this.graph.getInEdges(v)));
			
		while (nodes.size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: nodes) {
				if (inEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			//Initialize the offset according to the number of vertices in current group
			OFFSET.setLocation(new Point2D.Double(this.getSize().width/2,Math.max(this.getSize().getHeight()/visualizedGroup.size(),60)));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// set the location of the current vertex
				super.setLocation(v,location);
				// remove out edges
				for(VisualEdge e: this.graph.getOutEdges(v)){
					inEdges.get(e.getToNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
								
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX(), location.getY()+ OFFSET.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(location.getX()+ OFFSET.getX(), initialPosition.getY());
		}
	}	
	/***
	 * initialize the locations of nodes according to the topological sort layout, 
	 * starting from the left side towards the right side of the screen
	 * First, nodes with <b>no outgoing</b> edges are placed at the left side 
	 * Then all nodes connected via an <b>outgoing edge</b> with placed nodes are placed at the right 
	 */
	private void initializeLeft2Right(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		if (initialPosition  == null)
			initialPosition = new Point2D.Double(0,0);
		else
			initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the outEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> outEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			outEdges.put(v, new ArrayList<VisualEdge>(this.graph.getOutEdges(v)));
			
		while (nodes.size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: nodes) {
				if (outEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			//Initialize the offset according to the number of vertices in current group
			OFFSET.setLocation(new Point2D.Double(this.getSize().width/2,Math.max(this.getSize().getHeight()/visualizedGroup.size(),60)));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// set the location of the current vertex
				super.setLocation(v,location);
				// remove in edges
				for(VisualEdge e: this.graph.getInEdges(v)){
					outEdges.get(e.getFromNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
				
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX(), location.getY()+ OFFSET.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(location.getX()+ OFFSET.getX(), initialPosition.getY());
		}
	}	
	

	/***
	 * initialize the locations of nodes 
	 * according to the topological sort layout, starting from the bottom side 
	 * towards the up side of the screen
	 */
	private void initializeBottomUp(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());
		else
			initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the outEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> outEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			outEdges.put(v, new ArrayList<VisualEdge>(this.graph.getOutEdges(v)));
			
		while (nodes.size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: nodes) {
				if (outEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			//Initialize the offset according to the number of vertices in current group
			OFFSET.setLocation(new Point2D.Double(-Math.max(this.getSize().width/visualizedGroup.size(),60),-this.getSize().height/2));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// remove in edges
				for(VisualEdge e: this.graph.getInEdges(v)){
					outEdges.get(e.getFromNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
				
				// set the location of the current vertex
				super.setLocation(v,location);
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX()+ OFFSET.getX(), location.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(initialPosition.getX(), location.getY()+ OFFSET.getY());
		}
	}
	
	
	private void ZoomedLayoutForModulesV2(){
		//if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());
		//else
			//initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the outEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> outEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			outEdges.put(v, new ArrayList<VisualEdge>(this.graph.getOutEdges(v)));
		List<VisualNode> queries = new ArrayList<VisualNode>();
		List<VisualNode> relations = new ArrayList<VisualNode>();
		List<VisualNode> views = new ArrayList<VisualNode>();
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
		
		for(VisualNode q : queries){
			OFFSET.setLocation(new Point2D.Double(this.getSize().width/2,Math.max(this.getSize().getHeight()/queries.size(),60)));
			location = new Point2D.Double(graph.getCenter().getX()+200, cntQuery);
			super.setLocation(q,location);
			location.setLocation(graph.getCenter().getX()+200, cntQuery);
			q.setLocation(location);
			graph.setLocation(q, location);
			Point2D loc1 = graph.getLocation(q);
			cntQuery+=100;
		}
		
		for(VisualNode r : relations){
			OFFSET.setLocation(new Point2D.Double(this.getSize().width/2,Math.max(this.getSize().getHeight()/relations.size(),60)));
			location = new Point2D.Double(graph.getCenter().getX(), cntRelation);
			super.setLocation(r,location);
			location.setLocation(graph.getCenter().getX(), cntRelation);
			r.setLocation(location);
			graph.setLocation(r, location);
			Point2D loc1 = graph.getLocation(r);
			cntRelation+=100;
		}
		
		for(VisualNode v : views){
			OFFSET.setLocation(new Point2D.Double(this.getSize().width/2,Math.max(this.getSize().getHeight()/views.size(),60)));
			location = new Point2D.Double(graph.getCenter().getX()+400, cntView);
			super.setLocation(v,location);
			
			location.setLocation(graph.getCenter().getX()+400, cntView);
			v.setLocation(location);
			graph.setLocation(v, location);
			Point2D loc1 = graph.getLocation(v);
			int temp = cntView;

			cntView = cntRelation-100;
			System.out.println("---- "+v.getName()   + "  y  " + temp);
			List<VisualNode> viewToQuery = new ArrayList<VisualNode>();
			List<VisualNode> viewToRelation = new ArrayList<VisualNode>();
			
		}
	
	}
	
	
	private void ZoomedLayoutForModules(){
		
		//if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());
		//else
			//initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());

		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the inEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> inEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			inEdges.put(v, new ArrayList<VisualEdge>(this.graph.getInEdges(v)));
			
		while (nodes.size()>0) {
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			for (VisualNode v: nodes) {
				if (inEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			OFFSET.setLocation(new Point2D.Double(this.getSize().width/3,Math.max(this.getSize().getHeight()/visualizedGroup.size(),60)));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// set the location of the current vertex
				super.setLocation(v,location);
				// remove out edges
				for(VisualEdge e: this.graph.getOutEdges(v)){
					inEdges.get(e.getToNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX(), location.getY()+ OFFSET.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(location.getX()+ OFFSET.getX(), initialPosition.getY());
		}
	}

	/***
	 * initialize the locations of nodes 
	 * according to the topological sort layout, starting from the up side 
	 * towards the bottom side of the screen
	 */
	private void initializeUpBottom(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),0);
		else
			initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the outEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> outEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			outEdges.put(v, new ArrayList<VisualEdge>(this.graph.getOutEdges(v)));
			
		while (nodes.size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: nodes) {
				if (outEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			//Initialize the offset according to the number of vertices in current group
			OFFSET.setLocation(new Point2D.Double(-Math.max(this.getSize().width/visualizedGroup.size(),60),this.getSize().height/2));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// remove in edges
				for(VisualEdge e: this.graph.getInEdges(v)){
					outEdges.get(e.getFromNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
				
				// set the location of the current vertex
				super.setLocation(v,location);
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX()+ OFFSET.getX(), location.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(initialPosition.getX(), location.getY()+ OFFSET.getY());
		}
	}
	
	/***
	 * initialize the locations of nodes 
	 * according to the topological sort layout, starting from inner circle
	 * towards outer circles on the screen
	 */
	private void initializeCircleOut(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		Point2D initialLocation = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialLocation.getX(), initialLocation.getY());
		
		//use a list to add/remove nodes from the graph for layout reasons
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		//use a temporary HashMap to hold the outEdges for each node , in order to remove them according to the topological algo
		Map<VisualNode, List<VisualEdge>> outEdges= new HashMap<VisualNode,List<VisualEdge>>();
		for (VisualNode v: nodes) 
			outEdges.put(v, new ArrayList<VisualEdge>(this.graph.getOutEdges(v)));
			
		while (nodes.size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: nodes) {
				if (outEdges.get(v).size()==0) {
					visualizedGroup.add(v);
				}
			}
			//Initialize the offset according to the number of vertices in current group
			OFFSET.setLocation(new Point2D.Double(-Math.max(this.getSize().width/visualizedGroup.size(),60),-this.getSize().height/2));
			//visualize the current group
			while (!visualizedGroup.isEmpty()) {
				//get each vertex in group
				VisualNode v = visualizedGroup.get(0);
				// remove in edges
				for(VisualEdge e: this.graph.getInEdges(v)){
					outEdges.get(e.getFromNode()).remove(e);
				}
				//remove each visualized node
				visualizedGroup.remove(v);
				nodes.remove(v);
				
				// set the location of the current vertex
				super.setLocation(v,location);
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX()+ OFFSET.getX(), location.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(initialLocation.getX(), location.getY()+ OFFSET.getY());
		}
	}
// 	/**
//	*  
//	*  
//	**/
//	protected void initializeLocations(){
//		
//		/*
//		 * @param initialPosition = the start location of the graph
//		 */
//		Point2D initialPosition = new Point2D.Double(this.getSize().getWidth(),0);
//				
//		/*
//		 * @param relationOFFSET = offset from the last vertex of the relation tree
//		 */
//		int countRelations = graph.getVertices(NodeType.NODE_TYPE_RELATION).size();
//		//define offset y as at least 60 pixels 
//		OFFSET.setLocation(new Point2D.Double(-this.getSize().width/2,Math.max(this.getSize().getHeight()/countRelations,60)));
//
//		//holds the nodes that have been located, they must be drawn once
//		List<VisualNode> nodesLocated = new ArrayList<VisualNode>();
//		for (VisualNode relationNode: graph.getVertices(NodeType.NODE_TYPE_RELATION)) {
//				//draw the relation tree
//				initialPosition = this.drawTree(relationNode,initialPosition, nodesLocated);
//				//set the location of the next relation
//				initialPosition.setLocation(initialPosition.getX(), initialPosition.getY()+OFFSET.getY());
//		}
//	}
//	
//	/**
//	*   For each relation draws the whole tree JungGraph 
//	 * @param startLocation
//	**/
//	private Point2D drawTree(VisualNode curNode, Point2D startLocation, List<VisualNode> nodesLocated){
//
//		/*
//		 * holds the location of the first child in the parent node's subgraph
//		 */
//		Point2D firstChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());
//		
//		/*
//		 * holds the location of the last child in the parent node's subgraph
//		 */
//		Point2D lastChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());
//		/*
//		 * holds the location of the current child drawn
//		 */
//		Point2D curChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());				
//		
//		//holds the number of children visualized
//		int k=0;
//		//get each query, view dependent on the relation
//		for(VisualEdge edge: graph.getInEdges(curNode)){
//			if (edge.getType()==EdgeType.EDGE_TYPE_FROM){
//				VisualNode dependentNode = graph.getSource(edge);
//				//if it has not been located
////				if (!nodesLocated.contains(dependentNode)){
//					k++;
//					//draw its graph
//					curChildLocation = this.drawTree(dependentNode, curChildLocation, nodesLocated);
//					if (k==1)
//						firstChildLocation.setLocation(curChildLocation.getX(),curChildLocation.getY());
//					//set the current child node location
//					lastChildLocation.setLocation(curChildLocation.getX(),curChildLocation.getY());
//					//reset the location of the next child node, hold the same x for all child node in the same level
//					curChildLocation.setLocation(curChildLocation.getX(), curChildLocation.getY()+OFFSET.getY());
////				}
//			}
//		}
//		
//		//set relation node location
//		curNode.setLocation(Math.min(startLocation.getX(),curNode.getLocation().getX()),(firstChildLocation.getY()+lastChildLocation.getY())/2);
//		nodesLocated.add(curNode);
//		//return the location of the node
//		return new Point2D.Double(curNode.getLocation().getX(),curNode.getLocation().getY());
//	}

}
