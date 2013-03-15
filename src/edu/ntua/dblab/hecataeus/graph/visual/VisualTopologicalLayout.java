package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;

public class VisualTopologicalLayout extends AbstractLayout<VisualNode,VisualEdge>{

	
	public enum Orientation{
		RIGHT2LEFT,
		BOTTOMUP,
		LEFT2RIGHT,
		UPBOTTOM;
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
    	switch (this.orientation) {
    	case RIGHT2LEFT:
    		initializeRight2Left();
    		break;
    	case BOTTOMUP:
    		initializeBottomUp();
    		break;
    	case LEFT2RIGHT:
    		initializeLeft2Right();
    		break;
    	case UPBOTTOM:
    		initializeUpBottom();
    		break;
    	default:
    		initializeRight2Left();
    	}
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
		
		// creates a clone of the graph 
		VisualGraph topLevelGraph = this.graph.toGraph(this.graph.getVertices());

		while (topLevelGraph.getVertices().size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: topLevelGraph.getVertices()) {
				if (topLevelGraph.getOutEdges(v).size()==0) {
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
				for(VisualEdge e: topLevelGraph.getInEdges(v)){
					topLevelGraph.removeEdge(e);
				}
				//remove each visualized node
				topLevelGraph.removeVertex(v);
				visualizedGroup.remove(v);
				
	    		//set the location of the next vertex in the group that is visualized
				location.setLocation(location.getX(), location.getY()+ OFFSET.getY());
			}
			//set the location of the next group that is visualized
			location.setLocation(location.getX()+ OFFSET.getX(), initialPosition.getY());
		}
	}	
	
	/***
	 * initialize the locations of nodes 
	 * according to the topological sort layout, 
	 * starting from the left side towards the right side of the screen  
	 */
	private void initializeLeft2Right(){
		/*
		 * @param initialPosition = the start location of the graph
		 */
		if (initialPosition  == null)
			initialPosition = new Point2D.Double(this.getSize().getWidth(),0);
		
		/*
		 * @param location = the current location of the graph
		 */
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		
		// creates a clone of the graph 
		VisualGraph topLevelGraph = this.graph.toGraph(this.graph.getVertices());

		while (topLevelGraph.getVertices().size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: topLevelGraph.getVertices()) {
				if (topLevelGraph.getOutEdges(v).size()==0) {
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
				for(VisualEdge e: topLevelGraph.getInEdges(v)){
					topLevelGraph.removeEdge(e);
				}
				//remove each visualized node
				topLevelGraph.removeVertex(v);
				visualizedGroup.remove(v);
				
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
		
		// creates a clone of the graph 
		VisualGraph topLevelGraph = this.graph.toGraph(this.graph.getVertices());
		
		while (topLevelGraph.getVertices().size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: topLevelGraph.getVertices()) {
				if (topLevelGraph.getOutEdges(v).size()==0) {
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
				for(VisualEdge e: topLevelGraph.getInEdges(v)){
					topLevelGraph.removeEdge(e);
				}
				//remove each visualized node
				topLevelGraph.removeVertex(v);
				visualizedGroup.remove(v);
				
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
	 * according to the topological sort layout, starting from the up side 
	 * towards the bottom side of the screen
	 */
	private void initializeUpBottom(){
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
		
		// creates a clone of the graph 
		VisualGraph topLevelGraph = this.graph.toGraph(this.graph.getVertices());
		
		while (topLevelGraph.getVertices().size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: topLevelGraph.getVertices()) {
				if (topLevelGraph.getOutEdges(v).size()==0) {
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
				for(VisualEdge e: topLevelGraph.getInEdges(v)){
					topLevelGraph.removeEdge(e);
				}
				//remove each visualized node
				topLevelGraph.removeVertex(v);
				visualizedGroup.remove(v);
				
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
		
		// creates a clone of the graph 
		VisualGraph topLevelGraph = this.graph.toGraph(this.graph.getVertices());
		
		while (topLevelGraph.getVertices().size()>0) {
			//hold the current group
			List<VisualNode> visualizedGroup = new ArrayList<VisualNode>();
			//create each group from all vertices with no outgoing edges
			for (VisualNode v: topLevelGraph.getVertices()) {
				if (topLevelGraph.getOutEdges(v).size()==0) {
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
				for(VisualEdge e: topLevelGraph.getInEdges(v)){
					topLevelGraph.removeEdge(e);
				}
				//remove each visualized node
				topLevelGraph.removeVertex(v);
				visualizedGroup.remove(v);
				
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
