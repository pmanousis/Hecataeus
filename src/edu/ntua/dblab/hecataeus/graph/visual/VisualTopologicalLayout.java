package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
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

	private Point2D.Double OFFSET = new Point2D.Double();
	private Orientation orientation;
	private VisualGraph graph;
	private double maxYForInputSchemata;

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
	
    /**
     * Creates an instance for the specified graph and size with default orientation
     */
    public VisualTopologicalLayout(Graph<VisualNode, VisualEdge> graph) {
    	super(graph);
    	this.graph = (VisualGraph) graph;
    	this.orientation = Orientation.RIGHT2LEFT;
    	this.size=((VisualGraph)graph).getSize();
    }
	/**
     * Creates an instance for the specified graph and orientation
     */
    public VisualTopologicalLayout(Graph<VisualNode, VisualEdge> graph, Orientation orientation)  {
    	super(graph);
    	this.graph = (VisualGraph) graph;
    	this.orientation = orientation;
   		this.size=new Dimension(200, 200);
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
    		ZoomedLayoutForModules();
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
			if (kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT||
				kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT||
				kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
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
	private void qvZoomedLayoutForModules(List<VisualNode> vns, Point2D loc)
	{
		Point2D.Double OFFSETschemata = new Point2D.Double(140,40);
		List<VisualNode> schemata = new ArrayList<VisualNode>();
		Map<VisualNode, Point2D> schemataPositions = new HashMap<VisualNode, Point2D>();
		Point2D.Double lastOfOutput = new Point2D.Double(0,0);
		VisualNode vn = null;
		for(VisualNode v:vns)
		{
			if(v.getType().getCategory()==NodeCategory.MODULE)
			{
				vn=v;
				break;
			}
		}
		
		super.setLocation(vn, loc);
		vn.setLocation(loc);	// NOTE: this is not error, both are needed since super.setLocation does not set location to VisualNode and vn.setLocation does not set correct the location for the visual representation.
		//Draw schema nodes.
		for(int i=0;i<vn.getOutEdges().size();i++)
		{	// First we have OUT.
			if(vn.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_OUTPUT && vns.contains(vn.getOutEdges().get(i).getToNode()))
			{
				schemata.add(vn.getOutEdges().get(i).getToNode());
				break;
			}
		}
		for(int i=0;i<vn.getOutEdges().size();i++)
		{	// Then SMTX.
			if(vn.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_SEMANTICS && vns.contains(vn.getOutEdges().get(i).getToNode()))
			{
				schemata.add(vn.getOutEdges().get(i).getToNode());
				break;
			}
		}
		for(int i=0;i<vn.getOutEdges().size();i++)
		{	// Finally INs.
			if(vn.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_INPUT && vns.contains(vn.getOutEdges().get(i).getToNode()))
			{
				schemata.add(schemata.size(),vn.getOutEdges().get(i).getToNode());
			}
		}
		boolean once=true;
		for(VisualNode n : schemata)
		{	// Initially we draw the shema nodes.
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
					maxYForInputSchemata = loc.getY();
					for(VisualNode key : schemataPositions.keySet())
					{
						if(key.getType()==NodeType.NODE_TYPE_INPUT)
						{	//get max of position?
							if(schemataPositions.get(key).getY()<maxYForInputSchemata)
							{
								maxYForInputSchemata=schemataPositions.get(key).getY();
							}
						}
					}
					if(once==true)
					{
						loc.setLocation(loc.getX()+OFFSET.getX()*2,maxYForInputSchemata);
						once=false;
					}
					else
					{
						loc.setLocation(loc.getX(),maxYForInputSchemata);
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
		{	// Then we draw their attributes
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
						if(vns.contains(e.getToNode()))
						{
							super.setLocation(e.getToNode(), new Point2D.Double(outloc.getX(),outloc.getY()+OFFSETschemata.getY()));
							e.getToNode().setLocation(new Point2D.Double(outloc.getX(),outloc.getY()+OFFSETschemata.getY()));
							outloc.setLocation(new Point2D.Double(outloc.getX(),outloc.getY()+OFFSETschemata.getY()));
							lastOfOutput.setLocation(outloc);
							VisualNode outputValueNode=e.getToNode();
							for(VisualEdge eo : outputValueNode.getOutEdges())
							{
								if(eo.getType().equals(EdgeType.EDGE_TYPE_MAPPING))
								{
									VisualNode inNd=eo.getToNode();
									for(VisualNode inputSchema : schemata)
									{
										if(inputSchema.getType().equals(NodeType.NODE_TYPE_INPUT))
										{
											for(VisualEdge inputSchemaToInputAttribute: inputSchema.getOutEdges())
											{
												if(inputSchemaToInputAttribute.getToNode().equals(inNd))
												{	// found parent schema node
													Point2D inloc=e.getToNode().getLocation();
													inloc.setLocation(inputSchema.getLocation().getX(), e.getToNode().getLocation().getY());
													super.setLocation(inNd, inloc);
													inNd.setLocation(inloc);
												}
											}
										}
									}
									
								}
								else
								{	// Aggregate function?
									
								}
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
						if(vns.contains(e.getToNode()))
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
					}
					break;
				case NODE_TYPE_INPUT:
					for(VisualEdge ve: n.getOutEdges())
					{
						if(vns.contains(ve.getToNode()))
						{
							if(ve.getToNode().getLocation().equals(new Point2D.Double(0,0)))
							{
								VisualNode inpNode=paterasKombos(ve.getToNode());
								Point2D inloc=inpNode.getLocation();
								if(lastOfOutput.equals(new Point2D.Double(0,0))==false)
								{
									inloc.setLocation(inloc.getX(),lastOfOutput.getY()+OFFSET.getY());
								}
								else
								{
									inloc.setLocation(inloc.getX(),inloc.getY()+OFFSET.getY());
								}
								super.setLocation(ve.getToNode(), inloc);
								ve.getToNode().setLocation(inloc);
								lastOfOutput.setLocation(inloc);
							}
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
	 * @param vns The visual nodes that are to be drawn.
	 * @param loc The location in which the node should be drawn.
	 */
	private void relationSetLocation(List<VisualNode> vns, Point2D loc)
	{
		VisualNode vn=null;
		for(VisualNode v: vns)
		{
			if(v.getType().getCategory()==NodeCategory.MODULE)
			{
				vn=v;
				break;
			}
		}
		if(vn==null)
		{
			vn=vns.get(0);
		}
		VisualNode mn=null;
		super.setLocation(vn, loc);
		vn.setLocation(loc);	// NOTE: this is not error, both are needed since super.setLocation does not set location to VisualNode and vn.setLocation does not set correct the location for the visual representation.
		Point2D mloc=new Point2D.Double(loc.getX(),loc.getY()+OFFSET.getY());
		for(int i=0;i<vn.getOutEdges().size();i++)
		{
			mn=vn.getOutEdges().get(i).getToNode();
			if(mn.getLocation().equals(new Point2D.Double(0,0)))
			{
				List<VisualNode> tmpvns=new ArrayList<VisualNode>();
				tmpvns.add(mn);
				relationSetLocation(tmpvns, mloc);
				mloc.setLocation(mloc.getX()-OFFSET.getX(),mloc.getY());
			}
		}
	}
	
	/***
	 * Split for different representation of relation and query/view modules.
	 * @author pmanousi
	 */
	private void ZoomedLayoutForModules() {
		initialPosition = new Point2D.Double(this.getSize().getWidth(),this.getSize().getHeight());
		Point2D location = new Point2D.Double(initialPosition.getX(), initialPosition.getY());
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices(NodeType.NODE_TYPE_RELATION));
		nodes.addAll(this.graph.getVertices(NodeType.NODE_TYPE_VIEW));
		nodes.addAll(this.graph.getVertices(NodeType.NODE_TYPE_QUERY));
		
		int attrnum=0;
		int modnum=0;
		for(VisualNode n : nodes) {
			if(n.getType()==NodeType.NODE_TYPE_ATTRIBUTE) {
				attrnum++;
			}
			if(n.getType().getCategory()==NodeCategory.MODULE) {
				modnum++;
			}
		}
		if(attrnum==0) {
			attrnum++;
		}
		if(modnum==0) {
			modnum++;
		}
		OFFSET.setLocation(new Point2D.Double(size.width/modnum, size.height/attrnum));
		//nodes.sort(new CustomComparator());	// nodes are already sorted due to lines 677 - 679
		for(int i = 0; i < nodes.size(); i++) {	// Find module node to start graph from there.
			if(nodes.get(i).getType().getCategory()==NodeCategory.MODULE) {
				if(nodes.get(i).getType()==NodeType.NODE_TYPE_RELATION) {	// this will be the last one to be drawn.
					location.setLocation(2.3*(maxYForInputSchemata+OFFSET.getX()), initialPosition.getY());
					relationSetLocation(graph.getModule(nodes.get(i)), location);
				}
				else {
					qvZoomedLayoutForModules(graph.getModule(nodes.get(i)), location);
					location.setLocation(initialPosition.getX(), location.getY()+OFFSET.getY()*3);
				}
			}
		}
	}
	
//	private class CustomComparator implements Comparator<VisualNode> {
//	    @Override
//	    public int compare(VisualNode o1, VisualNode o2) {
//	        if(o1.getType() == NodeType.NODE_TYPE_RELATION)
//	        	return(1);
//	        return(-1);
//	    }
//	}
	
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

}
