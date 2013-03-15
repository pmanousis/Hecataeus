/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.jung;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hecataeus.evolution.HecataeusEventType;
import org.hecataeus.evolution.HecataeusPolicyType;
import org.hecataeus.evolution.HecataeusEdgeType;
import org.hecataeus.evolution.HecataeusEvolutionEdge;
import org.hecataeus.evolution.HecataeusEvolutionEdges;
import org.hecataeus.evolution.HecataeusEvolutionEvent;
import org.hecataeus.evolution.HecataeusEvolutionEvents;
import org.hecataeus.evolution.HecataeusEvolutionGraph;
import org.hecataeus.evolution.HecataeusEvolutionNode;
import org.hecataeus.evolution.HecataeusEvolutionNodes;
import org.hecataeus.evolution.HecataeusEvolutionPolicies;
import org.hecataeus.evolution.HecataeusEvolutionPolicy;
import org.hecataeus.evolution.HecataeusNodeType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.awt.geom.Point2D;

public class HecataeusJungGraph extends DirectedSparseGraph{
	
    //collection of HecataeusJungNodes
    private HecataeusJungNodes _Nodes = null;
    //collection of HecataeusJungEdges
    private HecataeusJungEdges _Edges = null;
    
    //the corresponding HecataeusEvolutionGraph object 
    private HecataeusEvolutionGraph evGraph;
    
   public HecataeusJungGraph() {
		
		evGraph = new HecataeusEvolutionGraph();
		this._Nodes = new HecataeusJungNodes();
		this._Edges = new HecataeusJungEdges();
		
	}
	
	/**
	* returns the corresponding HecataeusEvolutionGraph object 
	**/
	public HecataeusEvolutionGraph getHecataeusEvolutionGraph(){
		return evGraph;
	}
	  
	/**
	* get Node by Key
	**/
	public HecataeusJungNode getNode(String Key) {
		for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
			HecataeusJungNode u = this._Nodes.get(forEachVar0);
			if ( u.getKey().equals(Key)){
				return u;
			}
		}
     return null;
	}

	/**
	*  get node by its name and type
	**/
	public HecataeusJungNode getNode(String Name, HecataeusNodeType Type) {
		HecataeusJungNode u;
		for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
			u = this._Nodes.get(forEachVar0);
			if ( ( u.getName().toUpperCase().equals(Name.toUpperCase()) && u.getType() == Type ) ) {
				return u;
			}
		}
	             return null;
	}

	/**
	* returns the HecataeusJungNode object from the corresponding HecataeusEvolutionNode object 
	**/
	public HecataeusJungNode getNode(HecataeusEvolutionNode hecEvNode){
		return (HecataeusJungNode) this.getNode(hecEvNode.getKey());
	}

	/**
	* get edge by key
	**/
	public HecataeusJungEdge getEdge(String Key) {
		for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
			HecataeusJungEdge u = this._Edges.get(forEachVar0);
			if ( u.getKey().equals(Key) ) {
				return u;
			}
		}
	             return null;
	}

	/**
	* get edge by name and type
	**/
	public HecataeusJungEdge getEdge(String Name, HecataeusEdgeType Type) {
		HecataeusJungEdge u;
		for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
			u = this._Edges.get(forEachVar0);
			if ( ( u.getName().toUpperCase().equals(Name.toUpperCase()) && u.getType() == Type ) ) {
				return u;
			}
		}
	             return null;
	}

	/**
    * returns the HecataeusJungEdge object from the corresponding HecataeusEvolutionEdge object
	**/
    public HecataeusJungEdge getEdge(HecataeusEvolutionEdge hecEvEdge){
    	return (HecataeusJungEdge) this.getEdge(hecEvEdge.getKey());
    }
    
	/**
	* gets the nodes of the graph 
	**/
	public HecataeusJungNodes getJungNodes() {
		return this._Nodes;
	}

	/**
	 *
	 * get all nodes of the graph of specific type HecataeusNodeType
	 * @return
	 **/
	public HecataeusJungNodes getJungNodes(HecataeusNodeType type) {
		HecataeusJungNodes nodes =  new HecataeusJungNodes();
		 for (int i = 0; i < this._Nodes.size(); i++) {
			 HecataeusJungNode node =  this._Nodes.get(i);
			 if (node.getType()== type) {
				 nodes.add(node);
			 }
		}
		return nodes;
	}
	
	 
	
	/**
	* gets the edges of the graph
	**/
	public HecataeusJungEdges getJungEdges() {
		return this._Edges;
	}

	/**
    * returns the HecataeusJungNodes object from the corresponding HecataeusEvolutionNodes object
	**/
    public HecataeusJungNodes getJungNodesFromEvolution(HecataeusEvolutionNodes hecEvNodes){
    	HecataeusJungNodes hecJuNodes = new HecataeusJungNodes(); 
    	for (int forEachVar0 = 0; forEachVar0 < hecEvNodes.size(); forEachVar0++) {
    		HecataeusEvolutionNode hecEvNode = hecEvNodes.get(forEachVar0);
    		HecataeusJungNode hecJuNode = getNode(hecEvNode);
    		hecJuNodes.add(hecJuNode);
    	}
    	return hecJuNodes;
    }
    
	/**
    * returns the HecataeusJungEdges object from the corresponding HecataeusEvolutionEdges object
	**/
    public HecataeusJungEdges getJungEdgesFromEvolution(HecataeusEvolutionEdges hecEvEdges){
    	HecataeusJungEdges hecJuEdges = new HecataeusJungEdges(); 
    	for (int forEachVar0 = 0; forEachVar0 < hecEvEdges.size(); forEachVar0++) {
    		HecataeusEvolutionEdge hecEvEdge = hecEvEdges.get(forEachVar0);
    		HecataeusJungEdge hecJuEdge = getEdge(hecEvEdge);
    		hecJuEdges.add(hecJuEdge);
    	}
    	return hecJuEdges;
    }
	
	/**
	* returns the HecataeusEvolutionNodes object from the corresponding HecataeusJungNodes object
	**/
	public HecataeusEvolutionNodes getEvolutionNodesFromJung(HecataeusJungNodes hecJuNodes){
		HecataeusEvolutionNodes hecEvNodes = new HecataeusEvolutionNodes();  	
		for (int forEachVar0 = 0; forEachVar0 < hecJuNodes.size(); forEachVar0++) {
			HecataeusJungNode hecJungNode = hecJuNodes.get(forEachVar0);
			HecataeusEvolutionNode hecEvNode = hecJungNode.getHecataeusEvolutionNode();
			hecEvNodes.add(hecEvNode);
		}
		return hecEvNodes;
	}

	/**
	* returns the HecataeusEvolutionEdges object from the corresponding HecataeusJungEdges object
	**/
	public HecataeusEvolutionEdges getEvolutionEdgesFromJung(HecataeusJungEdges hecJuEdges){
		HecataeusEvolutionEdges hecEvEdges = new HecataeusEvolutionEdges();  	
		for (int forEachVar0 = 0; forEachVar0 < hecJuEdges.size(); forEachVar0++) {
			HecataeusJungEdge hecJungEdge = hecJuEdges.get(forEachVar0);
			HecataeusEvolutionEdge hecEvEdge = hecJungEdge.getHecataeusEvolutionEdge();
			hecEvEdges.add(hecEvEdge);
		}
		return hecEvEdges;
	}

	/**
    * adds new node to the graph
	**/
	public boolean add_node(String Name, HecataeusNodeType Type) {
		// declare new node
		HecataeusJungNode newNode = new HecataeusJungNode(Name,Type);
		// find the evolution node
		HecataeusEvolutionNode hecEvNode = newNode.getHecataeusEvolutionNode();
		//add to evolution graph
		evGraph.addNode(hecEvNode);
		// assign key
		newNode.setKey(hecEvNode.getKey());
		// add vertex to layout
		addVertex(newNode);
		// add new node to jung graph
		_Nodes.add(newNode);
		return true;
	}

	/**
	* adds node by HecataeusJungNode
	**/
	public boolean add_node(HecataeusJungNode node) {
		// find the evolution node
		HecataeusEvolutionNode hecEvNode = node.getHecataeusEvolutionNode();
		//add to evolution graph
		evGraph.addNode(hecEvNode);
		// assign key
		node.setKey(hecEvNode.getKey());
		// add vertex to layout
		addVertex((Vertex)node);
		//add new node to jung graph
		_Nodes.add(node);
		return true;
	}
	
	/**
	* adds node by HecataeusJungNode with specific key
	**/
	public boolean add_node(HecataeusJungNode node, String key) {	
		// find the evolution node
		HecataeusEvolutionNode hecEvNode = node.getHecataeusEvolutionNode();
		//add to evolution graph
		evGraph.addNode(hecEvNode);
		//
		hecEvNode.setKey(key);
		// assign key
		node.setKey(key);
		System.out.println("New node added,key: "+node.getKey());
		// add vertex to layout
		addVertex((Vertex)node);
		//add new node to jung graph
		_Nodes.add(node);
		return true;
	}
	
	/**
    * adds HecataeusJungNode by a HecataeusEvolutionNode
	**/
	public HecataeusJungNode add_node(HecataeusEvolutionNode hecEvNode) {
		// declare new node
		HecataeusJungNode newNode = new HecataeusJungNode(hecEvNode);
		// add vertex to layout
		this.addVertex(newNode);
		// add new node to jung graph
		this._Nodes.add(newNode);
		
		return newNode;
	}

	/**
	* removes a node from graph
	**/
	public void remove_node(HecataeusJungNode node) {
		
		//remove in edges
		for (Iterator iterator= node.getInEdges().iterator(); iterator.hasNext();)
			this.remove_edge((HecataeusJungEdge) iterator.next());
		//remove out edges
		for (Iterator iterator=node.getOutEdges().iterator(); iterator.hasNext();){
			this.remove_edge((HecataeusJungEdge) iterator.next());
		}
		// remove node from layout
		removeVertex(node);
		// remove node from Jung
		this._Nodes.remove(node);
		
		// remove node from evolution
		HecataeusEvolutionNode hecEvNode = node.getHecataeusEvolutionNode();
		this.getHecataeusEvolutionGraph().removeNode(hecEvNode);
		
	}

	/**
	* adds edge by name, type, from and to nodes
	**/
	public boolean add_edge(String Name, HecataeusEdgeType Type, HecataeusJungNode FromNode, HecataeusJungNode ToNode) {
		// declare new edge
		HecataeusJungEdge newEdge = new HecataeusJungEdge(Name,Type,FromNode,ToNode);
		// find evolution edge
		HecataeusEvolutionEdge hecEvEdge = newEdge.getHecataeusEvolutionEdge();
		// add edge to evolution
		evGraph.addEdge(hecEvEdge);
		// assign key
		newEdge.setKey(hecEvEdge.getKey());
		// add edge to layout
		addEdge(newEdge);
		//add edge to jung graph
		_Edges.add(newEdge);
		return true;
	}

	/**
	* adds edge by HecataeusEvolutionEdge
	**/
	public boolean add_edge(HecataeusJungEdge edge) {
		// find evolution edge
		HecataeusEvolutionEdge hecEvEdge = edge.getHecataeusEvolutionEdge();
		// add edge to evolution
		evGraph.addEdge(hecEvEdge);
		// assign key
		edge.setKey(hecEvEdge.getKey());
		// add edge to layout
		addEdge(edge);
		// add edge to jung graph
		_Edges.add(edge);		
		return true;
	}
	
	/**
	* adds edge by HecataeusEdge with specific key
	**/
	public boolean add_edge(HecataeusJungEdge edge,String key) {
		// find evolution edge
		HecataeusEvolutionEdge hecEvEdge = edge.getHecataeusEvolutionEdge();
		// add edge to evolution
		evGraph.addEdge(hecEvEdge);
		//
		hecEvEdge.setKey(key);
		// assign key
		edge.setKey(key);
		// add edge to layout
		addEdge(edge);
		// add edge to jung graph
		_Edges.add(edge);		
		return true;
	}

	/**
	* adds new HecataeusJungEdge by HecataeusEvolutionEdge
	**/
	public HecataeusJungEdge add_edge(HecataeusEvolutionEdge hecEvEdge) {
		
			HecataeusJungNode jungFromNode = this.getNode(hecEvEdge.getFromNode());
			HecataeusJungNode jungToNode = this.getNode(hecEvEdge.getToNode());
			// declare new edge
			HecataeusJungEdge newEdge = new HecataeusJungEdge(hecEvEdge,jungFromNode,jungToNode);
				
			// add edge to layout
			this.addEdge(newEdge);
			
			// add edge to jung graph
			this._Edges.add(newEdge);	
			
			return newEdge;
		
	}

	/**
	 * removes an edge from graph
	**/
	public void remove_edge(HecataeusJungEdge edge) {
		// remove edge from layout
		removeEdge(edge);
		// remove edge from Junggraph
		this._Edges.remove(edge);
		// remove Edge from evolution
		HecataeusEvolutionEdge hecEvEdge = edge.getHecataeusEvolutionEdge();
		this.getHecataeusEvolutionGraph().removeEdge(hecEvEdge);

	}

	/**
	*  clears all nodes and edges from the graph
	**/
	public boolean clear_graph() {
		// clear the layout
		for (int i=0; i<_Nodes.size(); i++){
			removeVertex(_Nodes.get(i));
		}
		// clear jung graph
		this._Nodes.clear() ;
		this._Edges.clear();
		
		//clear Evolution graph
		HecataeusEvolutionGraph evGraph = this.getHecataeusEvolutionGraph();
		//clear the policies and the events
    	
		for (int i=0; i<evGraph.getNodes().size(); i++) {
    		HecataeusEvolutionNode v =evGraph.getNodes().get(i);  
			v.getEvents().clear();
    		v.getPolicies().clear();
    	}
		// clear evolution graph
		evGraph.getNodes().clear();
		evGraph.getEdges().clear();
		//set KeyGenerator to zero
		getHecataeusEvolutionGraph().resetKeyGenerator();
		return true;
	}
	
	/**
	*   create the JungGraph given an existing EvolutionGraph
	**/
	public void createJungGraphfromEvolution(HecataeusEvolutionGraph evolGraph){
		this.evGraph = evolGraph;
		//add a node for each HecataeusevolutionNode
    	for(int s=0; s  < this.evGraph.getNodes().size(); s++){
    		HecataeusEvolutionNode evNode = this.evGraph.getNodes().get(s);
    		// add node to Jung graph
    		HecataeusJungNode v = this.add_node(evNode);
    	}
    	//get each HecataeusEvolutionEdge
    	for(int s=0; s  < this.evGraph.getEdges().size(); s++){
    		HecataeusEvolutionEdge evEdge = this.evGraph.getEdges().get(s);
    		// add edge to Jung graph
    		HecataeusJungEdge e = this.add_edge(evEdge);
    	}
	}
	/**
	*  gets the HecataeusEvolutionGraph and visualizes it
	*  
	**/
	public void setHierarchicalLayout(){
		
		/*
		 * @param graphLastPosition = the start location of the graph
		 */
		Point2D initialLocation = new Point2D.Double(1000,0);
		/*
		 * @param relationOFFSET = offset from the last vertex of the relation tree
		 */
		final Point2D relationOFFSET = new Point2D.Double(0,50);
		//holds the nodes that have been located, they must be drawn once
		HecataeusJungNodes nodesLocated = new HecataeusJungNodes();
		for (int i = 0; i < this._Nodes.size(); i++) {
			HecataeusJungNode relationNode = this._Nodes.get(i);
			if (relationNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION) {
				//set relation node location
				relationNode.setLocation(initialLocation.getX(),initialLocation.getY());
				nodesLocated.add(relationNode);
				//draw the relation tree
				initialLocation = this.drawHierarchical(relationNode,nodesLocated);
				//set the location of the next relation
				initialLocation.setLocation(initialLocation.getX()+relationOFFSET.getX(), initialLocation.getY()+relationOFFSET.getY());
				
			}
		}
		//determine the color of graph
		this.color_graph();
		//determine the shape of nodes
		this.shape_graph();
		
	}
	
	/**
	*   For each relation draws the whole hierarchical JungGraph 
	**/
	private Point2D drawHierarchical(HecataeusJungNode parentNode, HecataeusJungNodes nodesLocated){

		/*
		 * OFFSET 
		 */
		final Point2D OFFSET = new Point2D.Double(-100,0);

		/*
		 * holds the last position of child nodes (attribute, operand,etc.) 
		 * of each relation,query, view
		 */
		Point2D childGraphLocation = parentNode.getLocation();
		
		/*
		 * holds the position of the last query or view drawn
		 */
		Point2D treeGraphLocation = parentNode.getLocation();

		/*
		 * draw the subgraph of the relation,query,view
		 */
		childGraphLocation = this.draw_subGraph(parentNode, nodesLocated);

		//get each query, view dependent on the relation
		for(int s=0; s  < parentNode.getHecataeusEvolutionNode().getInEdges().size(); s++){
			HecataeusEvolutionEdge hecEvEdge = parentNode.getHecataeusEvolutionNode().getInEdges().get(s);
			if (hecEvEdge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM){
				HecataeusJungNode dependentNode = this.getNode(hecEvEdge.getFromNode());
				//if it has not been located
				if (!nodesLocated.contains(dependentNode)){
					//set location of the dependent node
					dependentNode.setLocation(parentNode.getLocation().getX() + OFFSET.getX(), treeGraphLocation.getY() + OFFSET.getY());
					nodesLocated.add(dependentNode);
					//draw its graph
					treeGraphLocation = this.drawHierarchical(dependentNode, nodesLocated);
				}
			}
		}
		//return the position with the longest distance Y from the parent node
		return (treeGraphLocation.getY()> childGraphLocation.getY()?treeGraphLocation:childGraphLocation);
		
	
	}
	
	
	/**
	*  gets the HecataeusEvolutionGraph and visualizes it
	*  
	**/
	public void setTreeLayout(){
		
		/*
		 * @param initialPosition = the start location of the graph
		 */
		Point2D initialLocation = new Point2D.Double(1000,0);
				
		/*
		 * @param relationOFFSET = offset from the last vertex of the relation tree
		 */
		final Point2D relationOFFSET = new Point2D.Double(0,100);
		//holds the nodes that have been located, they must be drawn once
		HecataeusJungNodes nodesLocated = new HecataeusJungNodes();
		for (int i = 0; i < this._Nodes.size(); i++) {
			HecataeusJungNode relationNode = this._Nodes.get(i);
			if (relationNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION) {
				//draw the relation tree
				initialLocation = this.drawTree(relationNode,initialLocation, nodesLocated);
				//set the location of the next relation
				initialLocation.setLocation(initialLocation.getX()+relationOFFSET.getX(), relationNode.getLastChildLocation().getY()+relationOFFSET.getY());
				
			}
		}
		//determine the color of graph
		this.color_graph();
		//determine the shape of nodes
		this.shape_graph();
		
	}
	
	/**
	*   For each relation draws the whole tree JungGraph 
	 * @param startLocation
	**/
	private Point2D drawTree(HecataeusJungNode curNode, Point2D startLocation,  HecataeusJungNodes nodesLocated){

		/*
		 * OFFSET 
		 */
		final Point2D OFFSET = new Point2D.Double(-300,100);

		/*
		 * holds the location of the first child in the parent node's subgraph
		 */
		Point2D firstChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());
		
		/*
		 * holds the location of the first child in the parent node's subgraph
		 */
		Point2D lastChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());
		
		/*
		 * holds the location of the current child drawn
		 */
		Point2D curChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());				
		
		//holds the number of childs parsed
		int k=0;
		//get each query, view dependent on the relation
		for(int s=0; s  < curNode.getHecataeusEvolutionNode().getInEdges().size(); s++){
			HecataeusEvolutionEdge hecEvEdge = curNode.getHecataeusEvolutionNode().getInEdges().get(s);
			if (hecEvEdge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM){
				HecataeusJungNode dependentNode = this.getNode(hecEvEdge.getFromNode());
				//if it has not been located
				if (!nodesLocated.contains(dependentNode)){
					k++;
					//draw its graph
					curChildLocation = this.drawTree(dependentNode, curChildLocation, nodesLocated);
					if (k==1)
						firstChildLocation.setLocation(dependentNode.getLocation().getX(),dependentNode.getLocation().getY());
					//reset the location of the next child node, hold the same x for all child node in the same level
					curChildLocation.setLocation(dependentNode.getLocation().getX(), dependentNode.getLastChildLocation().getY()+OFFSET.getY());
					//set the current child node location
					lastChildLocation.setLocation(dependentNode.getLocation().getX(),dependentNode.getLocation().getY());
				}
			}
		}
		
		//set relation node location
		curNode.setLocation(startLocation.getX(),(firstChildLocation.getY()+lastChildLocation.getY())/2);
		nodesLocated.add(curNode);
		
		/*
		 * draw the subgraph of the relation,query,view
		 */
		this.draw_subGraph(curNode, nodesLocated);
		//return the location of the node
		return curNode.getLocation();
	
	}
	
	/**
	*   draws the JungGraph Subgraph given a parentNode (relation, query, view)
	**/
	private Point2D draw_subGraph(HecataeusJungNode parentNode, HecataeusJungNodes nodesLocated){
		

		final Double xConditionOffset = new Double(0);
		final Double yConditionOffset = new Double(10);
		final Double xAttributeOffset = new Double(0);
		final Double yAttributeOffset = new Double(10);
		final Double xConstantOffset = new Double(-5);
		final Double yConstantOffset = new Double(5);
		final Double xGroupByOffset = new Double(-10);
		final Double yGroupByOffset = new Double(20);
		final Double xOperandOffset = new Double(-20);		
		final Double yOperandOffset = new Double(10);
		final Double xFunctionOffset = new Double(-20);		
		final Double yFunctionOffset = new Double(10);
		
		// initial locations of nodes
		Double xCondition = parentNode.getLocation().getX()+30;
		Double yCondition = parentNode.getLocation().getY();
		Double xAttribute = parentNode.getLocation().getX()-50;
		Double yAttribute = parentNode.getLocation().getY()+50;
		Double xGroupBy = new Double(-50);
		Double yGroupBy = new Double(30);
		Double xOperand = parentNode.getLocation().getX();
		Double yOperand = parentNode.getLocation().getY();
		Double xFunction = new Double(-50);		
		Double yFunction = new Double(10);
		Double xConstant = parentNode.getLocation().getX();	
		Double yConstant = parentNode.getLocation().getY()+50;		
		
		//holds position of lastNode 
		Point2D returnLocation = new Point2D.Double(parentNode.getLocation().getX(),parentNode.getLocation().getY());
		//for each query, relation, view get the subgraph
		HecataeusJungNodes subGraph = this.getJungNodesFromEvolution(this.getHecataeusEvolutionGraph().getSubGraph(parentNode.getHecataeusEvolutionNode()));
		//get each HecataeusJungNode after the parentNode s>=1
		for(int s=1; s  < subGraph.size(); s++){

			HecataeusJungNode v = subGraph.get(s);
			if (!nodesLocated.contains(v)){
				if (v.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE) {
					xAttribute = xAttribute + xAttributeOffset;
					yAttribute = yAttribute + yAttributeOffset;
					v.setLocation(xAttribute,yAttribute);
				}else if (v.getType()==HecataeusNodeType.NODE_TYPE_CONDITION){
					xCondition = xCondition + xConditionOffset;
					yCondition = yCondition + yConditionOffset;
					v.setLocation(xCondition,yCondition);
				}else if (v.getType()==HecataeusNodeType.NODE_TYPE_OPERAND){
					HecataeusJungNode incidentNode = this.getNode(v.getHecataeusEvolutionNode().getInEdges().get(0).getFromNode());
					HecataeusJungEdge incidentEdge = this.getEdge(v.getHecataeusEvolutionNode().getInEdges().get(0));
					if (incidentEdge.getName().equals("op1")){
						xOperand = incidentNode.getLocation().getX()+ xOperandOffset;
						yOperand = incidentNode.getLocation().getY()+ yOperandOffset;
					}else if (incidentEdge.getName().equals("op2")){
						xOperand = incidentNode.getLocation().getX()+ xOperandOffset;
						yOperand = incidentNode.getLocation().getY()- yOperandOffset;
					}else{
						xOperand = incidentNode.getLocation().getX()+ xOperandOffset;
						yOperand = incidentNode.getLocation().getY()+ yOperandOffset;
					}
					v.setLocation(xOperand,yOperand);
				}else if (v.getType()==HecataeusNodeType.NODE_TYPE_GROUP_BY){
					HecataeusJungNode incidentNode = this.getNode(v.getHecataeusEvolutionNode().getInEdges().get(0).getFromNode());
					xGroupBy = incidentNode.getLocation().getX()+xGroupByOffset;
					yGroupBy = incidentNode.getLocation().getY()+yGroupByOffset;
					v.setLocation(xGroupBy,yGroupBy);
				}else if (v.getType()==HecataeusNodeType.NODE_TYPE_CONSTANT){
					HecataeusJungNode incidentNode = this.getNode(v.getHecataeusEvolutionNode().getInEdges().get(0).getFromNode());
					xConstant = incidentNode.getLocation().getX()+ xConstantOffset;
					yConstant = incidentNode.getLocation().getY()+ yConstantOffset;
					v.setLocation(xConstant,yConstant);
				}else if (v.getType()==HecataeusNodeType.NODE_TYPE_FUNCTION){
					HecataeusJungNode incidentNode = this.getNode(v.getHecataeusEvolutionNode().getInEdges().get(0).getFromNode());
					xFunction = incidentNode.getLocation().getX()+xFunctionOffset;
					yFunction = incidentNode.getLocation().getY()+yFunctionOffset;
					v.setLocation(xFunction,yFunction);
				}
				nodesLocated.add(v);
				returnLocation =(returnLocation.getY()>v.getLocation().getY()?returnLocation:v.getLocation());
				
			}
    	}
		parentNode.setLastChildLocation(returnLocation.getX(), returnLocation.getY());
		return parentNode.getLastChildLocation();
    	
    }
	
	/**
	*  draws sets the coloring of Nodes and edges in JungGraph
	**/
	protected void color_graph(){
		
		
	}
	
	/**
	*  draws sets the shaping of Nodes in JungGraph
	**/
	protected void shape_graph(){
		
		
		
	}

	public HecataeusJungGraph importFromXML(File file) {
					
			String nKey = null;
			String nName = null;
			String nType = null;
			String eKey = null;
			String eName = null;
			String eType = null;
			String eFromNode = null;
			String eToNode = null;
				
		try {
	
	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse (file);
	
	        // normalize text representation            
	        doc.getDocumentElement ().normalize ();
	        
	        NodeList listOfNodes = doc.getElementsByTagName("HNodes");
	        Element NodesElement = (Element)listOfNodes.item(0);
	    	
	        listOfNodes = NodesElement.getElementsByTagName("HNode");
	
	        for(int s=0; s<listOfNodes.getLength() ; s++){
	
	            Node firstNode = listOfNodes.item(s);
	            if(firstNode.getNodeType() == Node.ELEMENT_NODE){
	
	            	Element firstNodeElement = (Element)firstNode;
	                Double nodeX = new Double(firstNodeElement.getAttribute("x"));
	                Double nodeY = new Double(firstNodeElement.getAttribute("y"));
	                
	                //-------                    
	                NodeList KeyList = firstNodeElement.getElementsByTagName("Key");
	                Element KeyElement = (Element)KeyList.item(0);
	
	                NodeList textKeyList = KeyElement.getChildNodes();
	                nKey = ((Node)textKeyList.item(0)).getNodeValue();
	
	            
	                //-------                    
	                NodeList NameList = firstNodeElement.getElementsByTagName("Name");
	                Element NameElement = (Element)NameList.item(0);
	
	                NodeList textNameList = NameElement.getChildNodes();
	                nName = ((Node)textNameList.item(0)).getNodeValue();
	
	                //----                    
	                NodeList TypeList = firstNodeElement.getElementsByTagName("Type");
	                Element TypeElement = (Element)TypeList.item(0);
	
	                NodeList textTypeList = TypeElement.getChildNodes();
	                nType = ((Node)textTypeList.item(0)).getNodeValue();
	                
					// add node
					HecataeusJungNode v = new HecataeusJungNode();
					v.setLocation(nodeX, nodeY);
					this.add_node(v);
					v.setName(nName);
					v.setType(HecataeusNodeType.valueOf(nType));
					v.setKey(nKey);
					
	            }//end of if clause
	
	        }//end of for loop for nodes
	
	        NodeList listOfEdges = doc.getElementsByTagName("HEdges");
	        Element EdgesElement = (Element)listOfEdges.item(0);
	    	
	        listOfEdges = EdgesElement.getElementsByTagName("HEdge");
	        
	        for(int s=0; s<listOfEdges.getLength() ; s++){
	
	            Node firstEdge = listOfEdges.item(s);
	            if(firstEdge.getNodeType() == Node.ELEMENT_NODE){
	
	
	                Element firstEdgeElement = (Element)firstEdge;
	
	                //-------                    
	                NodeList KeyList = firstEdgeElement.getElementsByTagName("Key");
	                Element KeyElement = (Element)KeyList.item(0);
	
	                NodeList textKeyList = KeyElement.getChildNodes();
	                eKey = ((Node)textKeyList.item(0)).getNodeValue();
	
	                //-------                    
	                NodeList NameList = firstEdgeElement.getElementsByTagName("Name");
	                Element NameElement = (Element)NameList.item(0);
	
	                NodeList textNameList = NameElement.getChildNodes();
	                eName = ((Node)textNameList.item(0)).getNodeValue();
	
	                //----                    
	                NodeList TypeList = firstEdgeElement.getElementsByTagName("Type");
	                Element TypeElement = (Element)TypeList.item(0);
	
	                NodeList textTypeList = TypeElement.getChildNodes();
	                eType = ((Node)textTypeList.item(0)).getNodeValue();
	                
	                NodeList FromNodeList = firstEdgeElement.getElementsByTagName("FromNode");
	                Element FromNodeElement = (Element)FromNodeList.item(0);
	
	                NodeList textFromNodeList = FromNodeElement.getChildNodes();
	                eFromNode = ((Node)textFromNodeList.item(0)).getNodeValue();
	                
	                NodeList ToNodeList = firstEdgeElement.getElementsByTagName("ToNode");
	                Element ToNodeElement = (Element)ToNodeList.item(0);
	
	                NodeList textToNodeList = ToNodeElement.getChildNodes();
	                eToNode = ((Node)textToNodeList.item(0)).getNodeValue();
	                
					// add edge
	                HecataeusJungEdge e = new HecataeusJungEdge(this.getNode(eFromNode), this.getNode(eToNode));
	                this.add_edge(e);
	                e.setName(eName);
	                e.setType(HecataeusEdgeType.toEdgeType(eType));
	                e.setKey(eKey);


								
	            }//end of if clause
	
	        }//end of for loop for edges
	        
	        // find the policies (if any exists) for nodes
	        NodeList listOfPolicies = doc.getElementsByTagName("HPolicies");
	        Element PoliciesElement = (Element)listOfPolicies.item(0);
	    	
	        listOfPolicies = PoliciesElement.getElementsByTagName("HPolicy");
	        
    		if (listOfPolicies.getLength()>0) {
            	for(int i=0; i<listOfPolicies.getLength() ; i++){

            		Node firstPolicy = listOfPolicies.item(i);
            		if(firstPolicy.getNodeType() == Node.ELEMENT_NODE){

            			Element firstPolicyElement = (Element)firstPolicy;

            			//-------                    
            			NodeList HNodeList = firstPolicyElement.getElementsByTagName("HNode");
            			Element HNodeElement = (Element)HNodeList.item(0);

            			NodeList textHNodeList = HNodeElement.getChildNodes();
            			String nHNode = ((Node)textHNodeList.item(0)).getNodeValue();

            			//-------                    
            			NodeList HEventList = firstPolicyElement.getElementsByTagName("HEvent");
            			Element HEventElement = (Element)HEventList.item(0);

            			NodeList HEventNodeList = HEventElement.getElementsByTagName("HEventNode");
            			Element HEventNodeElement = (Element)HEventNodeList.item(0);

            			NodeList textHEventNodeList = HEventNodeElement.getChildNodes();
            			String nHEventNode = ((Node)textHEventNodeList.item(0)).getNodeValue();
            			
            			NodeList HEventTypeList = HEventElement.getElementsByTagName("HEventType");
            			Element HEventTypeElement = (Element)HEventTypeList.item(0);

            			NodeList textHEventTypeList = HEventTypeElement.getChildNodes();
            			String nHEventType = ((Node)textHEventTypeList.item(0)).getNodeValue();
            
            			//----                    
            			NodeList HPolicyTypeList = firstPolicyElement.getElementsByTagName("HPolicyType");
            			Element HPolicyTypeElement = (Element)HPolicyTypeList.item(0);

            			NodeList textHPolicyTypeList = HPolicyTypeElement.getChildNodes();
            			String nHPolicyType = ((Node)textHPolicyTypeList.item(0)).getNodeValue();

            			HecataeusEvolutionNode HNode = this.getHecataeusEvolutionGraph().getNode(nHNode);
            			HecataeusEvolutionNode HEventNode = this.getHecataeusEvolutionGraph().getNode(nHEventNode);
            			HecataeusEventType HEventType = HecataeusEventType.toEventType(nHEventType);
            			HecataeusPolicyType HPolicyType = HecataeusPolicyType.toPolicyType(nHPolicyType);

            			HNode.addPolicy(HEventType, HEventNode, HPolicyType);
            		}//end of if clause

            	}//end of for loop for policies

            }//end of if policies exist
    		
    		//get events
	        NodeList listOfEvents = doc.getElementsByTagName("HEvents");
	        Element EventsElement = (Element)listOfEvents.item(0);
	    	
	        listOfEvents = EventsElement.getElementsByTagName("HEvent");
			
            if (listOfEvents.getLength()>0) {
            	for(int i=0; i<listOfEvents.getLength() ; i++){
            		Node firstEvent = listOfEvents.item(i);
            		if(firstEvent.getNodeType() == Node.ELEMENT_NODE){
            			Element firstEventElement = (Element)firstEvent;

            			//-------                    
            			NodeList HNodeList = firstEventElement.getElementsByTagName("HNode");
            			Element HNodeElement = (Element)HNodeList.item(0);

            			NodeList textHNodeList = HNodeElement.getChildNodes();
            			String nHNode = ((Node)textHNodeList.item(0)).getNodeValue();

            			//-------                    
            			NodeList HEventNodeList = firstEventElement.getElementsByTagName("HEventNode");
            			Element HEventNodeElement = (Element)HEventNodeList.item(0);

            			NodeList textHEventNodeList = HEventNodeElement.getChildNodes();
            			String nHEventNode = ((Node)textHEventNodeList.item(0)).getNodeValue();
            			
            			//-------                    
            			NodeList HEventTypeList = firstEventElement.getElementsByTagName("HEventType");
            			Element HEventTypeElement = (Element)HEventTypeList.item(0);

            			NodeList textHEventTypeList = HEventTypeElement.getChildNodes();
            			String nHEventType = ((Node)textHEventTypeList.item(0)).getNodeValue();
            			
            			HecataeusEvolutionNode HNode = this.getHecataeusEvolutionGraph().getNode(nHNode);
            			HecataeusEvolutionNode HEventNode = this.getHecataeusEvolutionGraph().getNode(nHEventNode);
            			HecataeusEventType HEventType = HecataeusEventType.toEventType(nHEventType);
            			
            			HNode.addEvent(HEventType);            			
            		}
            	}//end of for loop for events
            }//end of if events exist
            
	        //get the last element - graph keygenerator
	        NodeList keyGen = doc.getElementsByTagName("HKeyGen");
	        Element keyGenElement = (Element)keyGen.item(0);
	
	        NodeList textkeyGen = keyGenElement.getChildNodes();
	        nKey = ((Node)textkeyGen.item(0)).getNodeValue();
	        //	set the key 
	        this.getHecataeusEvolutionGraph().setKeyGenerator(new Integer(nKey));
	      return this; 
	      
		}catch (SAXParseException err) {
	    System.out.println ("** Parsing error" + ", line " 
	         + err.getLineNumber () + ", uri " + err.getSystemId ());
	    System.out.println(" " + err.getMessage ());
	    return null;
	
	    }catch (SAXException e) {
	    Exception x = e.getException ();
	    ((x == null) ? e : x).printStackTrace ();
	    return null;
	
	    }catch (Throwable t) {
	    t.printStackTrace ();
	    return null;
	    }
	
	}

	public void exportToXML(File file) {
		
		
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("HGraph");
				document.appendChild(rootElement);
				
				Element elementHnodes = document.createElement("HNodes");
				Element elementHedges = document.createElement("HEdges");
				
				// write nodes
				for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
					HecataeusJungNode v = _Nodes.get(forEachVar0);
					// write HNode
					Element elementHnode = document.createElement("HNode");
					
					elementHnode.setAttribute("x", new Double(v.getLocation().getX()).toString());
					elementHnode.setAttribute("y", new Double(v.getLocation().getY()).toString());
					elementHnodes.appendChild(elementHnode);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(v.getKey()));
					elementHnode.appendChild(elementKey);
					// write element name
					Element elementName = document.createElement("Name");
					elementName.appendChild(document.createTextNode(v.getName()));
					elementHnode.appendChild(elementName);
					// write element type
					Element elementType = document.createElement("Type");
					elementType.appendChild(document.createTextNode(v.getType().toString()));
					elementHnode.appendChild(elementType);
					
				}
				rootElement.appendChild(elementHnodes);
				
				// write edges
				for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
					HecataeusJungEdge e = this._Edges.get(forEachVar0);
					// write HEdge
					Element elementHedge = document.createElement("HEdge");
					elementHedges.appendChild(elementHedge);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(e.getKey()));
					elementHedge.appendChild(elementKey);
					// write element name
					Element elementName = document.createElement("Name");
					elementName.appendChild(document.createTextNode(e.getName()));
					elementHedge.appendChild(elementName);
					// write element type
					Element elementType = document.createElement("Type");
					elementType.appendChild(document.createTextNode(e.getType().ToString()));
					elementHedge.appendChild(elementType);
					// write element fromNode
					Element elementFromNode = document.createElement("FromNode");
					elementFromNode.appendChild(document.createTextNode(e.getFromNode().getKey()));
					elementHedge.appendChild(elementFromNode);
					// write element toNode
					Element elementToNode = document.createElement("ToNode");
					elementToNode.appendChild(document.createTextNode(e.getToNode().getKey()));
					elementHedge.appendChild(elementToNode);
					// end element HEdge
				}
				rootElement.appendChild(elementHedges);
				
				
				// write policies
				Element elementHPolicies = document.createElement("HPolicies");
				// write events
				Element elementHEvents = document.createElement("HEvents");
				
				for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
					HecataeusJungNode v = _Nodes.get(forEachVar0);
					HecataeusEvolutionPolicies policies = v.getHecataeusEvolutionNode().getPolicies();
					for (int i=0; i<policies.size(); i++) {
						HecataeusEvolutionPolicy p = policies.get(i);
						//start tag HPolicy
						Element elementHPolicy = document.createElement("HPolicy");
						elementHPolicies.appendChild(elementHPolicy);
						//write Node having the policy
						Element elementHNode = document.createElement("HNode");
						elementHNode.appendChild(document.createTextNode(v.getKey().toString()));
						elementHPolicy.appendChild(elementHNode);
						//write event handled by the policy
						Element elementEvent = document.createElement("HEvent");
						elementHPolicy.appendChild(elementEvent);
						
						Element elementEventNode = document.createElement("HEventNode");
						elementEventNode.appendChild(document.createTextNode(p.getSourceEvent().getEventNode().getKey()));
						elementEvent.appendChild(elementEventNode);
						
						Element elementEventType = document.createElement("HEventType");
						elementEventType.appendChild(document.createTextNode(p.getSourceEvent().getEventType().toString()));
						elementEvent.appendChild(elementEventType);
						//write policy type
						Element elementPolicyType = document.createElement("HPolicyType");
						elementPolicyType.appendChild(document.createTextNode(p.getPolicyType().toString()));
						elementHPolicy.appendChild(elementPolicyType);
						// end element HPolicy
					}
					HecataeusEvolutionEvents events = v.getHecataeusEvolutionNode().getEvents();
					for (int i=0; i<events.size(); i++) {
						HecataeusEvolutionEvent e = events.get(i);
						//write element event
						Element elementHEvent = document.createElement("HEvent");
						elementHEvents.appendChild(elementHEvent);
						//write Node having the event
						Element elementHNode = document.createElement("HNode");
						elementHNode.appendChild(document.createTextNode(v.getKey().toString()));
						elementHEvent.appendChild(elementHNode);
						
						Element elementEventNode = document.createElement("HEventNode");
						elementEventNode.appendChild(document.createTextNode(e.getEventNode().getKey()));
						elementHEvent.appendChild(elementEventNode);
						Element elementEventType = document.createElement("HEventType");
						elementEventType.appendChild(document.createTextNode(e.getEventType().toString()));
						elementHEvent.appendChild(elementEventType);
						// end element HEvent
						
					}
				}
				rootElement.appendChild(elementHPolicies);
				rootElement.appendChild(elementHEvents);
			
	
				//
				Element elementHKeyGen = document.createElement("HKeyGen");
				elementHKeyGen.appendChild(document.createTextNode((new Integer(this.getHecataeusEvolutionGraph().getKeyGenerator()).toString())));
				rootElement.appendChild(elementHKeyGen);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(document);
		        StreamResult result =  new StreamResult(file);
		        transformer.transform(source, result);
				
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				
			}
			 catch (TransformerConfigurationException e) {
				e.printStackTrace();
				
			}
			 catch (TransformerException e) {
				e.printStackTrace();
				
			}
		
	}

	/**
	* returns an ArrayList with the nodes of the graph represented in an xml file
	**/
	public ArrayList<HecataeusJungNode> importNodesFromXML(File file) {
		
		// node and edge attributes
		String nKey = null;
		String nName = null;
		String nType = null;
		String nEventNode = null;
		String nEventType = null;
		String nPolicyType = null;
		
		int s;
		int i;
		
		// initial locations of nodes
		Double xQuery = new Double(-1000);
		Double yQuery = new Double(100);
		Double xRelation = new Double(1000);
		Double yRelation = new Double(800);
		Double xCondition = new Double(0);
		Double yCondition = new Double(0);
		Double xAttribute = new Double(0);
		Double yAttribute = new Double(0);
		Double xConstant = new Double(50);
		Double yConstant = new Double(100);
		Double xGrouBy = new Double(-50);
		Double yGroupBy = new Double(100);
		Double xOperand = new Double(-50);		
		Double yOperand = new Double(600);
		Double xView = new Double(100);
		Double yView = new Double(500);
		
    	boolean flagForAttribute = false;
    	boolean flagForRelation = false;
    	boolean flagForQuery = false;
    	boolean flagForView = false;
    	boolean flagForOperand = false;
    	
    	ArrayList<HecataeusJungNode> nodes = new ArrayList<HecataeusJungNode>();
		
	try {

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);

        // normalize text representation            
        doc.getDocumentElement ().normalize ();
        
        NodeList listOfNodes = doc.getElementsByTagName("HNode");
        
        for(s=0; s<listOfNodes.getLength() ; s++){

            Node firstNode = listOfNodes.item(s);
            if(firstNode.getNodeType() == Node.ELEMENT_NODE){

                Element firstNodeElement = (Element)firstNode;

                //-------                    
                NodeList KeyList = firstNodeElement.getElementsByTagName("Key");
                Element KeyElement = (Element)KeyList.item(0);

                NodeList textKeyList = KeyElement.getChildNodes();
                System.out.println("Key : " + 
                       ((Node)textKeyList.item(0)).getNodeValue().trim());
                nKey = ((Node)textKeyList.item(0)).getNodeValue();

                //-------                    
                NodeList NameList = firstNodeElement.getElementsByTagName("Name");
                Element NameElement = (Element)NameList.item(0);

                NodeList textNameList = NameElement.getChildNodes();
                System.out.println("Name : " + 
                       ((Node)textNameList.item(0)).getNodeValue().trim());
                nName = ((Node)textNameList.item(0)).getNodeValue();

                //----                    
                NodeList TypeList = firstNodeElement.getElementsByTagName("Type");
                Element TypeElement = (Element)TypeList.item(0);

                NodeList textTypeList = TypeElement.getChildNodes();
                System.out.println("Type : " + 
                       ((Node)textTypeList.item(0)).getNodeValue().trim());
                nType = ((Node)textTypeList.item(0)).getNodeValue();
          			
				// add node
				HecataeusJungNode v = new HecataeusJungNode();
				v.setName(nName);
				v.setType(HecataeusNodeType.valueOf(nType));
				
				if (v.getType()==HecataeusNodeType.NODE_TYPE_QUERY) {
					yQuery = yQuery+800;
					v.setLocation(xQuery,yQuery);
			    	flagForAttribute = false;
					flagForRelation = false;
					flagForView = false;
			    	flagForOperand = false;
					flagForQuery = true;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_RELATION) {
					yRelation = yRelation+2500;
					v.setLocation(xRelation,yRelation);
					flagForRelation = true;
					flagForQuery = false;
					flagForView = false;
			    	flagForAttribute = false;
			    	flagForOperand = false;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_VIEW){
					yView = yView+1150;
					v.setLocation(xView,yView);
			    	flagForAttribute = false;
					flagForRelation = false;
					flagForView = true;
					flagForQuery = false;
			    	flagForOperand = false;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE) {
					if (flagForRelation){
						xAttribute = xRelation-400;
						yAttribute = yRelation-1000;
					}
					else if (flagForQuery){
						xAttribute = xQuery+100;
						yAttribute = yQuery-500;
					}
					else if (flagForView){
						xAttribute = xView+400;
						yAttribute = yView-700;
					}
						yAttribute = yAttribute+30;
						v.setLocation(xAttribute,yAttribute);
						flagForAttribute = true; 
						flagForRelation = false;
						flagForQuery = false;
						flagForView = false;
				    	flagForOperand = false;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_CONDITION){
					if (flagForAttribute){
						xCondition = xAttribute-100;
						yCondition = yRelation-100;
					}
						yCondition = yCondition+100;
						v.setLocation(xCondition,yCondition);
				    	flagForOperand = false;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_OPERAND){
					if (flagForOperand){
						xOperand = xOperand-50;
						yOperand = yOperand+50;
					}
						yOperand = yOperand+50;
						v.setLocation(xOperand,yOperand);
						flagForOperand = true;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_GROUP_BY){
					yGroupBy = yGroupBy+50;
					v.setLocation(xGrouBy,yGroupBy);
			    	flagForOperand = false;
				}
				else if (v.getType()==HecataeusNodeType.NODE_TYPE_CONSTANT){
					if (flagForOperand){
						xConstant = xOperand+50;
						yConstant = yOperand-50;
					}
						yConstant = yConstant+50;
						v.setLocation(xConstant,yConstant);
				    	flagForOperand = false;
				}

				
				v.setKey(nKey);
				nodes.add(v);

				HecataeusEvolutionPolicies PoliciesList = new  HecataeusEvolutionPolicies();
                
                NodeList listOfPolicies = firstNodeElement.getElementsByTagName("HPolicy");

                if (listOfPolicies.getLength()>0) {
                for(i=0; i<listOfPolicies.getLength() ; i++){
 
                    Node firstPolicy = listOfPolicies.item(s);
                    if(firstPolicy.getNodeType() == Node.ELEMENT_NODE){


                        Element firstPolicyElement = (Element)firstPolicy;

                        //-------                    
                        NodeList eventNodeList = firstPolicyElement.getElementsByTagName("eventNode");
                        Element eventNodeElement = (Element)eventNodeList.item(0);

                        NodeList textEventNodeList = eventNodeElement.getChildNodes();
                        System.out.println("eventNode : " + 
                               ((Node)textEventNodeList.item(0)).getNodeValue().trim());
                        nEventNode = ((Node)textEventNodeList.item(0)).getNodeValue();

                        //-------                    
                        NodeList EventTypeList = firstNodeElement.getElementsByTagName("eventType");
                        Element EventTypeElement = (Element)EventTypeList.item(0);

                        NodeList textEventTypeList = EventTypeElement.getChildNodes();
                        System.out.println("eventType : " + 
                               ((Node)textEventTypeList.item(0)).getNodeValue().trim());
                        nEventType = ((Node)textEventTypeList.item(0)).getNodeValue();

                        //----                    
                        NodeList PolicyTypeList = firstNodeElement.getElementsByTagName("policyType");
                        Element PolicyTypeElement = (Element)PolicyTypeList.item(0);

                        NodeList textPolicyTypeList = PolicyTypeElement.getChildNodes();
                        System.out.println("Type : " + 
                               ((Node)textPolicyTypeList.item(0)).getNodeValue().trim());
                        nPolicyType = ((Node)textPolicyTypeList.item(0)).getNodeValue();
                     	
        				// find the node with (key = nEventNode)
        				HecataeusJungNode eventNode = null;
    					for (int forEachVar0 = 0; forEachVar0 < nodes.size(); forEachVar0++) {
    						HecataeusJungNode u = nodes.get(forEachVar0);
    						if ( u.getKey().equals(nEventNode) ) {
    							eventNode = u;
    						}
    					}
        				
    					HecataeusPolicyType policyType = HecataeusPolicyType.toPolicyType(nPolicyType);
        				
        				HecataeusEvolutionPolicy policy = new HecataeusEvolutionPolicy(HecataeusEventType.toEventType(nEventType),eventNode.getHecataeusEvolutionNode(),policyType);

        				PoliciesList.add(policy);

        				
                    }//end of if clause

                }//end of for loop for policies

				v.getHecataeusEvolutionNode().setPolicies(PoliciesList);

                }//end of if policies exist
                
				// find the events (if any exist) for this node
        		HecataeusEvolutionEvents EventsList = new  HecataeusEvolutionEvents();
                
                NodeList listOfEvents = firstNodeElement.getElementsByTagName("HEvent");
                if (listOfEvents.getLength()>0) {

                for(i=0; i<listOfEvents.getLength() ; i++){
 
                    Node firstEvent = listOfEvents.item(s);
//                    if(firstEvent.getNodeType() == Node.ELEMENT_NODE){

                        Element firstEventElement = (Element)firstEvent;

                        NodeList EventTypeList = firstNodeElement.getElementsByTagName("eventType");
                        Element EventTypeElement = (Element)EventTypeList.item(0);

                        NodeList textEventTypeList = EventTypeElement.getChildNodes();
                        System.out.println("eventType : " + 
                               ((Node)textEventTypeList.item(0)).getNodeValue().trim());
                        nEventType = ((Node)textEventTypeList.item(0)).getNodeValue();
                     				
        				EventsList.add(new HecataeusEvolutionEvent(v.getHecataeusEvolutionNode(),HecataeusEventType.toEventType(nEventType)));
//
//                    }//end of if clause

                }//end of for loop for events

				v.getHecataeusEvolutionNode().setEvents(EventsList);
                }//end of if events exist

            }//end of if clause

        }//end of for loop for nodes

        
//		// read HKeyGen Tag
//        NodeList HGenKeyList = doc.getElementsByTagName("HGenKey");
//	    Element HGenKeyElement = (Element)HGenKeyList.item(0);
//	        
//	    NodeList textHGenKeyList = HGenKeyElement.getChildNodes();
//	    System.out.println("HGenKey : " + 
//            ((Node)textHGenKeyList.item(0)).getNodeValue().trim());
//	        gKeyGen = ((Node)textHGenKeyList.item(0)).getNodeValue();
//
//	    evGraph.setKeyGenerator(new Integer(gKeyGen));

    }catch (SAXParseException err) {
    System.out.println ("** Parsing error" + ", line " 
         + err.getLineNumber () + ", uri " + err.getSystemId ());
    System.out.println(" " + err.getMessage ());
    return null;

    }catch (SAXException e) {
    Exception e1 = e.getException ();
    ((e1 == null) ? e : e1).printStackTrace ();
    return null;

    }catch (Throwable t) {
    t.printStackTrace ();
    return null;
    }
    return nodes;
}    
	/**
	* returns an ArrayList with the edges of the graph represented in an xml file
	**/
	public ArrayList<HecataeusJungEdge> importEdgesFromXML(File file) {
		
		String eKey = null;
		String eName = null;
		String eType = null;
		String eFromNode = null;
		String eToNode = null;
// 		gKeyGen = null;
		
		int s;
		
    	ArrayList<HecataeusJungEdge> edges = new ArrayList<HecataeusJungEdge>();
		
		try {

	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(file);

	        // normalize text representation            
	        doc.getDocumentElement ().normalize ();
	        
	        NodeList listOfEdges = doc.getElementsByTagName("HEdge");

		
	        for(s=0; s<listOfEdges.getLength() ; s++){

            Node firstEdge = listOfEdges.item(s);
            if(firstEdge.getNodeType() == Node.ELEMENT_NODE){


                Element firstEdgeElement = (Element)firstEdge;

                //-------                    
                NodeList KeyList = firstEdgeElement.getElementsByTagName("Key");
                Element KeyElement = (Element)KeyList.item(0);

                NodeList textKeyList = KeyElement.getChildNodes();
                System.out.println("Key : " + 
                       ((Node)textKeyList.item(0)).getNodeValue().trim());
                eKey = ((Node)textKeyList.item(0)).getNodeValue();

                //-------                    
                NodeList NameList = firstEdgeElement.getElementsByTagName("Name");
                Element NameElement = (Element)NameList.item(0);

                NodeList textNameList = NameElement.getChildNodes();
                System.out.println("Name : " + 
                       ((Node)textNameList.item(0)).getNodeValue().trim());
                eName = ((Node)textNameList.item(0)).getNodeValue();

                //----                    
                NodeList TypeList = firstEdgeElement.getElementsByTagName("Type");
                Element TypeElement = (Element)TypeList.item(0);

                NodeList textTypeList = TypeElement.getChildNodes();
                System.out.println("Type : " + 
                       ((Node)textTypeList.item(0)).getNodeValue().trim());
                eType = ((Node)textTypeList.item(0)).getNodeValue();
                
                NodeList FromNodeList = firstEdgeElement.getElementsByTagName("FromNode");
                Element FromNodeElement = (Element)FromNodeList.item(0);

                NodeList textFromNodeList = FromNodeElement.getChildNodes();
                System.out.println("FromNode : " + 
                       ((Node)textFromNodeList.item(0)).getNodeValue().trim());
                eFromNode = ((Node)textFromNodeList.item(0)).getNodeValue();
                
                NodeList ToNodeList = firstEdgeElement.getElementsByTagName("ToNode");
                Element ToNodeElement = (Element)ToNodeList.item(0);

                NodeList textToNodeList = ToNodeElement.getChildNodes();
                System.out.println("ToNode : " + 
                       ((Node)textToNodeList.item(0)).getNodeValue().trim());
                eToNode = ((Node)textToNodeList.item(0)).getNodeValue();
                
                
				// add edge
                HecataeusEdgeType type = HecataeusEdgeType.toEdgeType(eType);
              	HecataeusJungNode fromNode = getNode(eFromNode);
				HecataeusJungNode toNode = getNode(eToNode);
				System.out.println("FromNode: "+fromNode.getKey()+", ToNode: "+toNode.getKey());
				HecataeusJungEdge e = new HecataeusJungEdge(eName, type, fromNode, toNode);
				e.setName(eName);
				e.setType(HecataeusEdgeType.toEdgeType(eType));
				e.setFromNode(getNode(eFromNode));
				e.setToNode(getNode(eToNode));
				e.setKey(eKey);
				edges.add(e);

            }//end of if clause


        }//end of for loop for edges
		
    }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());
        return null;

        }catch (SAXException e) {
        Exception e1 = e.getException ();
        ((e1 == null) ? e : e1).printStackTrace ();
        return null;

        }catch (Throwable t) {
        t.printStackTrace ();
        return null;
        }
        return edges;
	}
   
	/**
	* returns an ArrayList with the nodes of the graph represented in an xml file,
	* taking special care of the keys, so that the graph that already exists in the 
	* layout will not be erased
	**/
	public ArrayList<HecataeusJungEdge> addSubgraphEdgesFromXML(File file,int maxKey) {
		
		String eKey = null;
		String eName = null;
		String eType = null;
		String eFromNode = null;
		String eToNode = null;
// 		gKeyGen = null;
		
		int s;
		
    	ArrayList<HecataeusJungEdge> edges = new ArrayList<HecataeusJungEdge>();
		
		try {

	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(file);

	        // normalize text representation            
	        doc.getDocumentElement ().normalize ();
	        
	        NodeList listOfEdges = doc.getElementsByTagName("HEdge");

		
	        for(s=0; s<listOfEdges.getLength() ; s++){

            Node firstEdge = listOfEdges.item(s);
            if(firstEdge.getNodeType() == Node.ELEMENT_NODE){


                Element firstEdgeElement = (Element)firstEdge;

                //-------                    
                NodeList KeyList = firstEdgeElement.getElementsByTagName("Key");
                Element KeyElement = (Element)KeyList.item(0);

                NodeList textKeyList = KeyElement.getChildNodes();
                System.out.println("Key : " + 
                       ((Node)textKeyList.item(0)).getNodeValue().trim());
                eKey = ((Node)textKeyList.item(0)).getNodeValue();

                //-------                    
                NodeList NameList = firstEdgeElement.getElementsByTagName("Name");
                Element NameElement = (Element)NameList.item(0);

                NodeList textNameList = NameElement.getChildNodes();
                System.out.println("Name : " + 
                       ((Node)textNameList.item(0)).getNodeValue().trim());
                eName = ((Node)textNameList.item(0)).getNodeValue();

                //----                    
                NodeList TypeList = firstEdgeElement.getElementsByTagName("Type");
                Element TypeElement = (Element)TypeList.item(0);

                NodeList textTypeList = TypeElement.getChildNodes();
                System.out.println("Type : " + 
                       ((Node)textTypeList.item(0)).getNodeValue().trim());
                eType = ((Node)textTypeList.item(0)).getNodeValue();
                
                NodeList FromNodeList = firstEdgeElement.getElementsByTagName("FromNode");
                Element FromNodeElement = (Element)FromNodeList.item(0);

                NodeList textFromNodeList = FromNodeElement.getChildNodes();
                System.out.println("FromNode : " + 
                       ((Node)textFromNodeList.item(0)).getNodeValue().trim());
                eFromNode = ((Node)textFromNodeList.item(0)).getNodeValue();
                
                NodeList ToNodeList = firstEdgeElement.getElementsByTagName("ToNode");
                Element ToNodeElement = (Element)ToNodeList.item(0);

                NodeList textToNodeList = ToNodeElement.getChildNodes();
                System.out.println("ToNode : " + 
                       ((Node)textToNodeList.item(0)).getNodeValue().trim());
                eToNode = ((Node)textToNodeList.item(0)).getNodeValue();
                
                
				// add edge
                HecataeusEdgeType type = HecataeusEdgeType.toEdgeType(eType);
                int fkey = Integer.parseInt(eFromNode);
                int tkey = Integer.parseInt(eToNode);
                System.out.println("fkey: "+fkey+" tkey: "+tkey);
                HecataeusJungNode fromNode = getNode(new Integer(fkey+maxKey).toString());
                HecataeusJungNode toNode = getNode(new Integer(tkey+maxKey).toString());
                System.out.println("FromNode: "+fromNode.getKey()+", ToNode"+toNode.getKey());
				HecataeusJungEdge e = new HecataeusJungEdge(eName, type, fromNode, toNode);
				e.setName(eName);
				e.setType(HecataeusEdgeType.toEdgeType(eType));
				e.setFromNode(fromNode);
				e.setToNode(toNode);
				edges.add(e);

            }//end of if clause


        }//end of for loop for edges
		
    }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());
        return null;

        }catch (SAXException e) {
        Exception e1 = e.getException ();
        ((e1 == null) ? e : e1).printStackTrace ();
        return null;

        }catch (Throwable t) {
        t.printStackTrace ();
        return null;
        }
        return edges;
	}
	
	
}
