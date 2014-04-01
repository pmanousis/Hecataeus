package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
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
//	private List<Point2D> listOfKnown = new ArrayList<Point2D>();
	
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
				location.setLocation(location.getX()/2, location.getY()-100+ OFFSET.getY());   //TODO!
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
	
	/**
	 * Returns the input schema node of a node. 
	 * @param kombos The node who's input schema we search
	 * @return The input schema node of the node.
	 */
	private VisualNode paterasKombos(VisualNode kombos)
	{
		for (int i = 0; i < kombos.getInEdges().size(); i++)
		{
			if (kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT)
			{
				return (kombos.getInEdges().get(i).getFromNode());
			}
		}
		return (null);
	}
	
	/**
	 * Creates the semantics tree.
	 * @author pmanousi
	 * @param vn The root node of the tree/subtree. 
	 * @param loc Where to place the root node.
	 */
	private void smtxTreeSetLocation(VisualNode vn, Point2D loc)
	{
		VisualNode mn=null;
		if(vn.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
		{
			return;
		}
		super.setLocation(vn, loc);
		vn.setLocation(loc);	// NOTE: this is not error, both are needed since super.setLocation does not set location to VisualNode and vn.setLocation does not set correct the location for the visual representation.
		Point2D mlocLeft=new Point2D.Double(loc.getX()+OFFSET.getX()/15,loc.getY()+OFFSET.getY());
		Point2D mlocRight=new Point2D.Double(loc.getX()-OFFSET.getX()/15,loc.getY()+OFFSET.getY());
		
		for(int i=0;i<vn.getOutEdges().size();i++)
		{
			mn=vn.getOutEdges().get(i).getToNode();
			if(mn.getLocation().equals(new Point2D.Double(0,0))&& i==0)
			{
				smtxTreeSetLocation(mn, mlocLeft);
			}
			if(mn.getLocation().equals(new Point2D.Double(0,0))&& i==1)
			{
				smtxTreeSetLocation(mn, mlocRight);
			}
		}
	}
	
	/**
	 * *
	 * @author pmanousi
	 * Describes better (with straight lines when possible) the query/view modules. 
	 * @param vn The module node.
	 * @param loc The location to place the module node.
	 */
	private void qvZoomedLayoutForModules(VisualNode vn, Point2D loc)
	{
		Point2D.Double OFFSETschemata = new Point2D.Double(140,40);
		List<VisualNode> schemata = new ArrayList<VisualNode>();
		Map<VisualNode, Point2D> schemataPositions = new HashMap<VisualNode, Point2D>();
		Point2D.Double lastOfOutput = new Point2D.Double(0,0);
		VisualNode mn=null;
		super.setLocation(vn, loc);
		vn.setLocation(loc);	// NOTE: this is not error, both are needed since super.setLocation does not set location to VisualNode and vn.setLocation does not set correct the location for the visual representation.
		//Draw schema nodes.
		for(int i=0;i<vn.getOutEdges().size();i++)
		{	// First we have OUT.
			if(vn.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_OUTPUT)
			{
				schemata.add(vn.getOutEdges().get(i).getToNode());
			}
		}
		for(int i=0;i<vn.getOutEdges().size();i++)
		{	// Then SMTX.
			if(vn.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_SEMANTICS)
			{
				schemata.add(vn.getOutEdges().get(i).getToNode());
			}
		}
		for(int i=0;i<vn.getOutEdges().size();i++)
		{	// Finally INs.
			if(vn.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_INPUT)
			{
				schemata.add(schemata.size(),vn.getOutEdges().get(i).getToNode());
			}
		}
		boolean once=true;
		for(VisualNode n : schemata)
		{
			switch(n.getType())
			{
				case NODE_TYPE_OUTPUT:
					Point2D outloc=new Point2D.Double(loc.getX(),loc.getY()+OFFSETschemata.getY());
					super.setLocation(n, outloc);
					n.setLocation(outloc);
					schemataPositions.put(n, outloc);
					loc.setLocation(outloc);	// Move to smtx.
					break;
				case NODE_TYPE_SEMANTICS:
					Point2D smtxloc=new Point2D.Double(loc.getX()+OFFSETschemata.getX()*2,loc.getY());
					super.setLocation(n, smtxloc);
					n.setLocation(smtxloc);
					schemataPositions.put(n, smtxloc);
					loc.setLocation(smtxloc);	// Move to inputs.
					break;
				case NODE_TYPE_INPUT:
					double maxXForInputSchemata = loc.getY();
					for(VisualNode key : schemataPositions.keySet())
					{
						if(key.getType()==NodeType.NODE_TYPE_INPUT)
						{	//get max of position?
							if(schemataPositions.get(key).getY()<maxXForInputSchemata)
							{
								maxXForInputSchemata=schemataPositions.get(key).getY();
							}
						}
					}
					if(once==true)
					{
						loc.setLocation(loc.getX()+OFFSET.getX()*2,maxXForInputSchemata);
						once=false;
					}
					else
					{
						loc.setLocation(loc.getX(),maxXForInputSchemata);
					}
					Point2D inloc=new Point2D.Double(loc.getX()+OFFSETschemata.getX(),loc.getY());
					super.setLocation(n, inloc);
					n.setLocation(inloc);
					schemataPositions.put(n, inloc);
					loc.setLocation(inloc);	// Move to other inputs.
					break;
			default:
				break;
			}
		}
		for(VisualNode n : schemata)
		{
			switch(n.getType())
			{
				case NODE_TYPE_OUTPUT:
					Point2D outloc = new Point2D.Double(0,0);
					Set st = schemataPositions.entrySet();
					Iterator it = st.iterator();
					while (it.hasNext())
					{
						Map.Entry m = (Map.Entry) it.next();
						VisualNode kombosElegxoy = (VisualNode) m.getKey();
						if(kombosElegxoy.getType()==NodeType.NODE_TYPE_OUTPUT)
						{
							outloc.setLocation((Point2D) m.getValue());
							break;
						}
					}
					for(VisualEdge e : n.getOutEdges())
					{	// Input attributes connected to output.
						super.setLocation(e.getToNode(), new Point2D.Double(outloc.getX(),outloc.getY()+OFFSETschemata.getY()));
						e.getToNode().setLocation(new Point2D.Double(outloc.getX(),outloc.getY()+OFFSETschemata.getY()));
						outloc.setLocation(new Point2D.Double(outloc.getX(),outloc.getY()+OFFSETschemata.getY()));
						lastOfOutput.setLocation(outloc);
						VisualNode outputValueNode=e.getToNode();
						for(VisualEdge eo : outputValueNode.getOutEdges())
						{
							VisualNode inNd=eo.getToNode();
							if(inNd.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
							{	// Attribute
								VisualNode inpNode=paterasKombos(inNd);
								Point2D inloc=inpNode.getLocation();
								inloc.setLocation(inloc.getX(),outloc.getY());
								super.setLocation(inNd, inloc);
								inNd.setLocation(inloc);
							}
							else
							{	// Aggregate function?
								
							}
						}
					}
				break;
				case NODE_TYPE_SEMANTICS:
					Point2D smtxloc = new Point2D.Double(0,0);
					st = schemataPositions.entrySet();
					it = st.iterator();
					while (it.hasNext())
					{
						Map.Entry m = (Map.Entry) it.next();
						VisualNode kombosElegxoy = (VisualNode) m.getKey();
						if(kombosElegxoy.getType()==NodeType.NODE_TYPE_SEMANTICS)
						{
							smtxloc.setLocation((Point2D) m.getValue());
							break;
						}
					}
					for(VisualEdge e : n.getOutEdges())
					{
						if(e.getType()==EdgeType.EDGE_TYPE_GROUP_BY)
						{	// Group by clause
							super.setLocation(e.getToNode(), new Point2D.Double(smtxloc.getX(),smtxloc.getY()-OFFSET.getY()));
							e.getToNode().setLocation(new Point2D.Double(smtxloc.getX(),smtxloc.getY()-OFFSET.getY()));
						}
						else if(e.getType()==EdgeType.EDGE_TYPE_WHERE)
						{	// Where clause
							VisualNode smtxBegin=e.getToNode();
							smtxloc.setLocation(new Point2D.Double(smtxloc.getX(),smtxloc.getY()+OFFSETschemata.getY()));
							smtxTreeSetLocation(smtxBegin, smtxloc);
						}
						else
						{	// Aggregate functions that were not set?
							super.setLocation(e.getToNode(), new Point2D.Double(smtxloc.getX(),smtxloc.getY()-OFFSET.getY()));
							e.getToNode().setLocation(new Point2D.Double(smtxloc.getX(),smtxloc.getY()-OFFSET.getY()));
						}
					}
					break;
				case NODE_TYPE_INPUT:
					for(VisualEdge ve: n.getOutEdges())
					{
						if(ve.getToNode().getLocation().equals(new Point2D.Double(0,0)))
						{
							VisualNode inpNode=paterasKombos(ve.getToNode());
							Point2D inloc=inpNode.getLocation();
							inloc.setLocation(inloc.getX(),lastOfOutput.getY()+OFFSET.getY());
							super.setLocation(ve.getToNode(), inloc);
							ve.getToNode().setLocation(inloc);
							lastOfOutput.setLocation(inloc);
						}
					}
					break;
			default:
				break;
			}
		}
	}

	/***
	 * Recursive function to draw (if possible) the relation modules in straight lines.
	 * @author pmanousi
	 * @param vn The visual node that is to be drawn (initially it is the module visual node).
	 * @param loc The location in which the node should be drawn.
	 */
	private void relationSetLocation(VisualNode vn, Point2D loc)
	{
		VisualNode mn=null;
		super.setLocation(vn, loc);
		vn.setLocation(loc);	// NOTE: this is not error, both are needed since super.setLocation does not set location to VisualNode and vn.setLocation does not set correct the location for the visual representation.
		Point2D mloc=new Point2D.Double(loc.getX()+OFFSET.getX(),loc.getY());
		for(int i=0;i<vn.getOutEdges().size();i++)
		{
			mn=vn.getOutEdges().get(i).getToNode();
			if(mn.getLocation().equals(new Point2D.Double(0,0)))
			{
				relationSetLocation(mn, mloc);
				mloc.setLocation(mloc.getX(),mloc.getY()+OFFSET.getY());
			}
		}
	}
	
	/***
	 * Split for different representation of relation and query/view modules.
	 * @author pmanousi
	 */
	private void ZoomedLayoutForModules()
	{
		initialPosition = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		int attrnum=0;
		for(VisualNode n : nodes)
		{
			if(n.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
			{
				attrnum++;
			}
		}
		OFFSET.setLocation(new Point2D.Double(this.getSize().width/2,this.getSize().height/attrnum));
		VisualNode moduleNode = null;
		for(int i=0;i<nodes.size();i++)
		{	// Find module node to start graph from there.
			if(nodes.get(i).getType().getCategory()==NodeCategory.MODULE)
			{
				moduleNode=nodes.get(i);
				break;
			}
		}
		if(moduleNode.getType()==NodeType.NODE_TYPE_RELATION)
		{
			relationSetLocation(moduleNode, location);
		}
		else
		{
			qvZoomedLayoutForModules(moduleNode, location);
		}
	}

	/***
	 * initialize the locations of nodes 
	 * according to the topological sort layout, starting from the up side 
	 * towards the bottom side of the screen
	 */
	private void initializeUpBottom(){
		if (initialPosition  == null)		// @param initialPosition = the start location of the graph
			initialPosition = new Point2D.Double(this.getSize().getWidth(),0);
		else
			initialPosition = new Point2D.Double(this.getSize().getWidth()/2+initialPosition.getX(),this.getSize().getHeight()/2 + initialPosition.getY());
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());		// @param location = the current location of the graph
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
		Point2D initialLocation = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());		// @param initialPosition = the start location of the graph
		Point2D location = new Point2D.Double(initialLocation.getX(), initialLocation.getY());		// @param location = the current location of the graph
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
