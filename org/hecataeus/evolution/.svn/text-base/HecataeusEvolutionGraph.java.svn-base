/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;


import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hecataeus.HecataeusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sun.misc.Queue;

public class HecataeusEvolutionGraph  {

	//collection of HecataeusEvolutionNodes 
	private HecataeusEvolutionNodes _Nodes = null;

	//collection of HecataeusEvolutionEdges
	private HecataeusEvolutionEdges _Edges = null;

	/**
	 *counter generating unique ids (integers) for graph elements(nodes and edges)
	 */
	private int _KeyGenerator;


	//used by function initializeChange() to increase the SID(Session ID) by one
	static int SIDGenerator = 0;

	//used if the policy is prompt and the application runs graphical 
	static boolean flagForJung = false;

	public HecataeusEvolutionGraph() {
		this._KeyGenerator = 0;
		_Nodes = new HecataeusEvolutionNodes();
		_Edges = new HecataeusEvolutionEdges();
	}

	/**
	 * adds node by name and type
	 **/
	public void addNode(String Name, HecataeusNodeType Type) {
		// declare new node
		HecataeusEvolutionNode newNode = new HecataeusEvolutionNode(Name,Type);
		// increase key
		this._KeyGenerator = this._KeyGenerator + 1 ;
		// assign key
		newNode.setKey(new Integer(this._KeyGenerator).toString());
		// add new node to graph
		_Nodes.add(newNode);
	}

	/**
	 * adds node by HecataeusNode
	 **/
	public void addNode(HecataeusEvolutionNode Node) {
		// increase key
		this._KeyGenerator = this._KeyGenerator + 1;
		// assign key
		Node.setKey(new Integer(this._KeyGenerator).toString());
		// add new node to graph
		_Nodes.add(Node);
	}

	/**
	 * removes a node from graph
	 **/
	public void removeNode(HecataeusEvolutionNode Node) {
		// remove node
		this._Nodes.remove(Node);
		//remove in edges
		for (int i =0 ; i<Node.getInEdges().size() ; i++) {
			this.removeEdge(Node.getInEdges().get(i));
		}
		//remove out edges
		for (int i =0 ; i<Node.getOutEdges().size() ; i++) {
			this.removeEdge(Node.getOutEdges().get(i));
		}
		//clear collections
		Node.getInEdges();
		Node.getOutEdges();
	}

	/**
	 * adds edge by name, type, from and to nodes
	 **/
	public void addEdge(String Name, HecataeusEdgeType Type, HecataeusEvolutionNode FromNode, HecataeusEvolutionNode ToNode) {
		// declare new edge
		HecataeusEvolutionEdge newEdge = new HecataeusEvolutionEdge(Name,Type,FromNode,ToNode);
		// increase key
		this._KeyGenerator = this._KeyGenerator + 1 ;
		// assign key
		newEdge.setKey(new Integer(this._KeyGenerator).toString());
		// add new edge to graph
		this._Edges.add(newEdge);
		// add edge to incoming edges of ToNode
		ToNode.getInEdges().add(newEdge);
		// add edge to outgoing edges of FromNode
		FromNode.getOutEdges().add(newEdge);

	}

	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(HecataeusEvolutionEdge Edge) {
		// increase key
		this._KeyGenerator = this._KeyGenerator + 1 ;
		// assign key
		Edge.setKey(new Integer(this._KeyGenerator).toString());
		// add new node to graph
		this._Edges.add(Edge);
		// add edge to incoming edges of ToNode
		Edge.getToNode().getInEdges().add(Edge) ;
		// add edge to outgoing edges of FromNode
		Edge.getFromNode().getOutEdges().add(Edge);
		return true;
	}

	public void removeEdge(HecataeusEvolutionEdge Edge) {
		// remove edge from graph
		this._Edges.remove(Edge);
		// remove edge from inEdges
		Edge.getToNode().getInEdges().remove(Edge);
		// remove edge from outEdges
		Edge.getFromNode().getOutEdges().remove(Edge);

	}

	/**
	 *  clears all nodes and edges from the graph
	 **/
	public boolean clearGraph() {
		// destroy objects
		// clear collections
		this._Nodes.clear() ;
		this._Edges.clear();
		return true;
	}

	/**
	 * get Node by Key
	 **/
	public HecataeusEvolutionNode getNode(String Key) {
		HecataeusEvolutionNode u;
		for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
			u = this._Nodes.get(forEachVar0);
			if ( u.getKey().equals(Key) ) {
				return u;
			}
		}
		return null;
	}


	/**
	 *  get node by its name, for more than one occurrences, the first is returned
	 **/
	public HecataeusEvolutionNode getNodeByName(String name) {
		HecataeusEvolutionNode u;
		for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
			u = this._Nodes.get(forEachVar0);
			if (u.getName().toUpperCase().equals(name.toUpperCase())) {
				return u;
			}
		}
		return null;
	}


	/**
	 * get edge by key
	 **/
	public HecataeusEvolutionEdge getEdge(String Key) {
		HecataeusEvolutionEdge u;
		for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
			u = this._Edges.get(forEachVar0);
			if ( u.getKey().equals(Key) ) {
				return u;
			}
		}
		return null;
	}

	/**
	 * get edge by name and type
	 **/
	public HecataeusEvolutionEdge getEdge(String Name, HecataeusEdgeType Type) {
		HecataeusEvolutionEdge u;
		for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
			u = this._Edges.get(forEachVar0);
			if ( ( u.getName().toUpperCase().equals(Name.toUpperCase()) && u.getType() == Type ) ) {
				return u;
			}
		}
		return null;
	}

	/**
	 * gets a table node and creates an alias graph
	 **/
	public HecataeusEvolutionNode createTableAlias(HecataeusEvolutionNode srcTable, String tblAlias) {
		HecataeusEvolutionEdge edgeAttribute = null;
		// create table alias node
		HecataeusEvolutionNode aliasTable = new HecataeusEvolutionNode(tblAlias, HecataeusNodeType.NODE_TYPE_RELATION);
		// add node to graph
		addNode(aliasTable);
		// create edge for alias
		this.addEdge("alias", HecataeusEdgeType.EDGE_TYPE_ALIAS, aliasTable, srcTable);
		//  for each attribute in source table create new attribute nodes
		for (int forEachVar0 = 0; forEachVar0 <  srcTable.getOutEdges().size(); forEachVar0++) {
			edgeAttribute = srcTable.getOutEdges().get(forEachVar0);
			if ( edgeAttribute.getType()==HecataeusEdgeType.EDGE_TYPE_SCHEMA)  {
				HecataeusEvolutionNode aliasAttr = new HecataeusEvolutionNode(edgeAttribute.getToNode().getName(),HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
				this.addNode(aliasAttr);
				this.addEdge("S", HecataeusEdgeType.EDGE_TYPE_SCHEMA, aliasTable, aliasAttr);
			}
		}
		return aliasTable;
	}


	//
	//	/**
	//	* used for finding explicitly an attribute by its name and its parent relation node
	//	**/
	public HecataeusEvolutionNode getAttributeNode(String TableName, String AttributeName) {
		HecataeusEvolutionNode getAttributeNode = null;
		HecataeusEvolutionNode u = (HecataeusEvolutionNode) this.getNodeByName(TableName);
		HecataeusEvolutionEdge e = null;
		if ( !(u == null) ) {
			for (int forEachVar0 = 0; forEachVar0 < u.getOutEdges().size(); forEachVar0++) {
				e =  u.getOutEdges().get(forEachVar0);
				if ( e.getToNode().getName().toUpperCase().equals(AttributeName.toUpperCase()) ) {
					return e.getToNode();
				}
			}
		}

		u = (HecataeusEvolutionNode) this.getNodeByName(TableName);
		if ( !(u == null) ) {
			for (int forEachVar0 = 0; forEachVar0 < u.getOutEdges().size(); forEachVar0++) {
				e =  u.getOutEdges().get(forEachVar0);
				if ( e.getToNode().getName().toUpperCase().equals(AttributeName.toUpperCase()) ) {
					return e.getToNode();
				}
			}
		}

		return getAttributeNode;
	}


	/**
	 * calculates the path of nodes that are connected via in edges with a node
	 **/
	public void getInTree(HecataeusEvolutionNode node, HecataeusEvolutionNodes InNodes) {
		// add itself

		if ( !(InNodes.contains(node)) ) {
			InNodes.add(node);
			System.out.println(node.getName()+" added");
		}		
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (int forEachVar0 = 0; forEachVar0 < node.getInEdges().size(); forEachVar0++) {
			HecataeusEvolutionEdge e =  node.getInEdges().get(forEachVar0);
			// count only once if multiple paths found
			if (!(InNodes.contains(e.getFromNode())) ) {
				InNodes.add(e.getFromNode()) ;
				System.out.println(e.getFromNode().getName()+" added!");
				// call recursively for each adjacent node
				getInTree(e.getFromNode(), InNodes);
			}
		}
	}


	/**
	 * calculates the path of nodes that are connected via out edges with a node
	 **/
	private void getOutTree(HecataeusEvolutionNode node, HecataeusEvolutionNodes OutNodes) {
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (int forEachVar0 = 0; forEachVar0 < node.getOutEdges().size(); forEachVar0++) {
			HecataeusEvolutionEdge e = node.getOutEdges().get(forEachVar0);
			// count only once if multiple paths found
			if ( !(OutNodes.contains(e.getToNode())) ) {
				OutNodes.add(e.getToNode());
				// call recursively for each adjacent node
				this.getOutTree(e.getToNode(), OutNodes);
			}
		}
		// add itself
		if ( !(OutNodes.contains(node)) ) {
			OutNodes.add(node);
		}
	}

	/**
	 * calculates the path of incoming edges that are connected with a node
	 **/
	private void getInPath(HecataeusEvolutionNode node, HecataeusEvolutionEdges InEdges) {
		// for each node edge add in edge to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (int forEachVar0 = 0; forEachVar0 < node.getInEdges().size(); forEachVar0++) {
			HecataeusEvolutionEdge e = node.getInEdges().get(forEachVar0);
			// count only once if multiple paths found
			if (!(InEdges.contains(e))) {
				InEdges.add(e);
				// call recursively for each adjacent node
				getInPath(e.getFromNode(), InEdges);
			}
		}
	}


	/**
	 * calculates the path of outgoing edges that are connected with a node
	 **/
	private void getOutPath(HecataeusEvolutionNode node, HecataeusEvolutionEdges OutEdges) {
		// for each node edge add out edge to collection
		// only adjacent nodes connected via a directed edge
		// FROM the affecting node are affected
		for (int forEachVar0 = 0; forEachVar0 < node.getOutEdges().size(); forEachVar0++) {
			HecataeusEvolutionEdge e = node.getOutEdges().get(forEachVar0);
			// count only once if multiple paths found
			if ( !(OutEdges.contains(e)) ) {
				OutEdges.add(e);
				// call recursively for each adjacent node
				getOutPath(e.getToNode(), OutEdges);
			}
		}
	}

	/**
	 *  returns the set of nodes affected by an event forced on the affecting_node
	 **/
	public HecataeusEvolutionNodes getAffectedNodes(HecataeusEvolutionNode affecting_node, HecataeusEventType eventType) {
		HecataeusEvolutionNodes AffectedNodes = new HecataeusEvolutionNodes();
		getInTree(affecting_node, AffectedNodes);
		return AffectedNodes;
	}

	/**
	 *  returns the set of incoming edges affected by an event forced on the affecting_node
	 **/
	public HecataeusEvolutionEdges getAffectedEdges(HecataeusEvolutionNode affecting_node, HecataeusEventType eventType) {
		HecataeusEvolutionEdges AffectedEdges = new HecataeusEvolutionEdges();
		getInPath(affecting_node, AffectedEdges);
		return AffectedEdges;
	}

	/**
	 *  makes the necessary initializations to execute propagateChanges()
	 **/
	public void initializeChange(HecataeusEvolutionEvent event){

		SIDGenerator = SIDGenerator + 1;
		int SID = SIDGenerator;
		HecataeusPolicyType Default_System_Policy = HecataeusPolicyType.PROPAGATE;
		Queue queue = new Queue();
		HecataeusEvolutionMessage firstMessage = new HecataeusEvolutionMessage(SID,null,event.getEventNode(),event,null,Default_System_Policy);
		queue.enqueue(firstMessage);
		System.out.println("in initializeChange Before call propagateChanges");
		HecataeusEvolutionNodes nodesVisited = new HecataeusEvolutionNodes();
		propagateChanges(queue, nodesVisited);

	}

//	/**
//	 *  determines the status given that prevailing policy is propagate 
//	 *  and the raised event of a node according to
//	 *  1. the type of event
//	 *  2. the scope of event (self, child, provider)
//	 *  3. the type of node
//	 **/
//	private void determineStatus(HecataeusEvolutionMessage message) {
//		int SID = message.get_SID();
//		HecataeusEvolutionNode ns = message.getNodeSender();
//		HecataeusEvolutionNode nr = message.getNodeReceiver();
//		HecataeusEvolutionEvent event = message.getEvent();
//		HecataeusEvolutionEdge edge = message.getEdge();
//		HecataeusPolicyType policyType = message.getPolicyType();
//		
//		//initialize the event of the next message as the old event
//		HecataeusEventType newEventType = event.getEventType();
//		/*
//		 * the determination of status is according to the following order
//		 * --Examine each event Type
//		 * 		--Examine each node Type
//		 *  		--Examine the scope of event
//		 */
//		
//		//examine each possible event type
//		switch (event.getEventType()) {
//		
//		//case addition 
//		case ADD_RELATION: //none affected
//			break;
//		case ADD_VIEW: //none affected
//			break;
//		case ADD_QUERY: //none affected
//			break;
//		case ADD_ATTRIBUTE:
//			//examine the type of node affected by the event 
//			switch (nr.getType()) {
//			case NODE_TYPE_RELATION:
//				 //add attribute always comes from null edge
//				nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//				newEventType = HecataeusEventType.ADD_ATTRIBUTE;
//				break;
//			case NODE_TYPE_VIEW:
//				 //both from provider or self the reaction is the same
//				nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//				newEventType = HecataeusEventType.ADD_ATTRIBUTE;
//				break;
//			case NODE_TYPE_QUERY:
//				 //both from provider or self the reaction is the same
//				nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//				newEventType = HecataeusEventType.ADD_ATTRIBUTE;
//				break;
//			case NODE_TYPE_OPERAND:
//				 //the add attribute came from a subquery
//				nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
//				newEventType = HecataeusEventType.MODIFY_CONDITION;
//				break;
//			default: 
//				throw new HecataeusException("Unhandled Error Occured- Event: " + event.getEventType());
//				break;
//			}
//			break;
//		case ADD_CONDITION:
//			//examine the type of node affected by the event 
//			switch (nr.getType()) {
//			case NODE_TYPE_RELATION:
//				 //add condition comes from child edge
//				nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//				newEventType = HecataeusEventType.ADD_CONDITION;
//				break;
//			case NODE_TYPE_VIEW:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
//					newEventType = HecataeusEventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//					newEventType = HecataeusEventType.ADD_CONDITION;
//				}
//				break;
//			case NODE_TYPE_QUERY:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
//					newEventType = HecataeusEventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//					newEventType = HecataeusEventType.ADD_CONDITION;
//				}
//				break;
//			case NODE_TYPE_ATTRIBUTE:
//				 //a constraint is added to 
//				nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//				newEventType = HecataeusEventType.ADD_CONDITION;
//				break;
//			}
//			break;
//			
//		case ADD_ORDER_BY:
//			//examine the type of node affected by the event 
//			switch (nr.getType()) {
//			case NODE_TYPE_VIEW:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
//					newEventType = HecataeusEventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//					newEventType = HecataeusEventType.ADD_CONDITION;
//				}
//				break;
//			case NODE_TYPE_QUERY:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
//					newEventType = HecataeusEventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
//					newEventType = HecataeusEventType.ADD_CONDITION;
//				}
//				break;
//			break;
//		case ADD_GROUP_BY:
//			break;
//			
//			//case deletion 
//		
//		
//		}	
//	}
	
	/**
	 *  sets the status of the parts of the graph affected by an event
	 **/
	private void propagateChanges(Queue queue, HecataeusEvolutionNodes nodesVisited) {

		while (!queue.isEmpty()){
			try {
				HecataeusEvolutionMessage currentMessage = (HecataeusEvolutionMessage) queue.dequeue();

				int SID = currentMessage.get_SID();
				HecataeusEvolutionNode ns = currentMessage.getNodeSender();
				HecataeusEvolutionNode nr = currentMessage.getNodeReceiver();
				HecataeusEvolutionEvent event = currentMessage.getEvent();
				HecataeusEvolutionEdge edge = currentMessage.getEdge();
				HecataeusPolicyType policyType = currentMessage.getPolicyType();

				if (!nodesVisited.contains(nr)) {
					//initialize the event of the next message as the old event
					HecataeusEventType newEventType = event.getEventType();

					//determine policy for current node
					policyType = determinePolicy(event,nr,edge,policyType);

					if (policyType==HecataeusPolicyType.PROMPT){
						System.out.println("Policy=prompt");
						nr.setStatus(HecataeusStatusType.PROMPT);
					}else if(policyType==HecataeusPolicyType.BLOCK){
						System.out.println("Policy=block");
						nr.setStatus(HecataeusStatusType.BLOCKED);
					}else {
						//policy is propagate
						//determine status for node according to eventType
						switch (event.getEventType()) {

						case ADD_ATTRIBUTE:
							System.out.println("in add_Attribute");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							//if receiver node is operand then add_attribute occurred in subquery
							if (nr.getType()==HecataeusNodeType.NODE_TYPE_OPERAND) {
								//new event is modify condition
								newEventType = HecataeusEventType.MODIFY_CONDITION;
								nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);	
							}else {
								//new event is add attribute
								//only through schema and top-level provider edges
								newEventType = HecataeusEventType.ADD_ATTRIBUTE;
								nr.setStatus(HecataeusStatusType.TO_ADD_CHILD);
								
							}
							break;

						case ADD_CONDITION:
							System.out.println("in Add_Condition");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
							
							//propagate the same event
							newEventType = event.getEventType();
							//new event is add condition only through schema and top-level provider edges
							break;
						case ADD_RELATION:
							nr.setStatus(HecataeusStatusType.PROMPT);
							break;
						case MODIFY_CONDITION:
							System.out.println("in Modify_Condition");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
							//new event is modify condition only through schema and top-level provider edges
							//propagate the same event
							newEventType = event.getEventType();
							break;
						case MODIFYDOMAIN_ATTRIBUTE:

							System.out.println("in MODIFYDOMAIN_ATTRIBUTE");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							if (nr.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE) {
								nr.setStatus(HecataeusStatusType.TO_MODIFY);
								//new event to modify domain
							}
							else nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
							//propagate the same event
							newEventType = event.getEventType();
							break;
						case RENAME_ATTRIBUTE:
							System.out.println("in rename_Attribute");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							if (nr.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE)
								nr.setStatus(HecataeusStatusType.TO_RENAME);
							//new event = rename attribute
							else nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
							//new event = rename attribute	
							//propagate the same event
							newEventType = event.getEventType();
							break;

						case RENAME_RELATION:
							System.out.println("in Rename_Relation");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(HecataeusStatusType.TO_RENAME);
							break;

						case DELETE_ATTRIBUTE:
							System.out.println("in Remove_Attribute");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							if ((nr.getType()==HecataeusNodeType.NODE_TYPE_QUERY)
									||(nr.getType()==HecataeusNodeType.NODE_TYPE_VIEW)
									||(nr.getType()==HecataeusNodeType.NODE_TYPE_RELATION))

							{
								/**
								 *event has come from other view and not from attribute 
								 *correct algorithm in determine next to signal
								 **/
								if (edge!=null&&edge.isProvider()) {
									nodesVisited.remove(nr);
									break;
								}

								newEventType = HecataeusEventType.DELETE_RELATION;
								nr.setStatus(HecataeusStatusType.TO_DELETE);
								//new event = remove relation
								//check if has any child with no delete
								for (int i=0; i<nr.getOutEdges().size(); i++) {
									HecataeusEvolutionEdge e = nr.getOutEdges().get(i);
									if(e.isPartOf()&&e.getToNode().getStatus()!=HecataeusStatusType.TO_DELETE) {
										newEventType = HecataeusEventType.DELETE_ATTRIBUTE;
										nr.setStatus(HecataeusStatusType.TO_DELETE_CHILD);
										//new event = remove attribute
									}
								}
							}
							else if ((nr.getType()==HecataeusNodeType.NODE_TYPE_GROUP_BY)
									||(nr.getType()==HecataeusNodeType.NODE_TYPE_FUNCTION)
							)
							{
								newEventType = HecataeusEventType.DELETE_ATTRIBUTE;
								nr.setStatus(HecataeusStatusType.TO_DELETE);
								//new event = REMOVE_ATTRIBUTE relation
								//check if has any child as constant
								for (int i=0; i<nr.getOutEdges().size(); i++) {
									HecataeusEvolutionEdge e = nr.getOutEdges().get(i);
									if(e.getToNode().getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE && e.getToNode().getStatus()!=HecataeusStatusType.TO_DELETE) {
										nr.setStatus(HecataeusStatusType.TO_DELETE_CHILD);
										newEventType = HecataeusEventType.MODIFY_CONDITION;
										//new event = remove attribute
									}
								}
							}


							else if (nr.getType()==HecataeusNodeType.NODE_TYPE_OPERAND) {
								newEventType = HecataeusEventType.DELETE_CONDITION;
								nr.setStatus(HecataeusStatusType.TO_DELETE);
							}
							//new event = remove condition
							else if (nr.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE) {
								newEventType= HecataeusEventType.DELETE_ATTRIBUTE;
								nr.setStatus(HecataeusStatusType.TO_DELETE);
							}
							//new event = remove attribute				
							break;

						case DELETE_CONDITION:
							System.out.println("in Remove_Condition");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
							//new event is remove condition only through schema and top-level provider edges
							break;

						case DELETE_RELATION:
							System.out.println("in Remove_Relation");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");

							HecataeusEvolutionEdges outEdges = nr.getOutEdges();
							if (ns==null)
								for (int i=0; i<outEdges.size(); i++){
									((HecataeusEvolutionNode) outEdges.get(i).getToNode()).setStatus(HecataeusStatusType.TO_DELETE);
									initializeChange(new HecataeusEvolutionEvent((HecataeusEvolutionNode) outEdges.get(i).getToNode(),HecataeusEventType.DELETE_ATTRIBUTE));
								}

							Boolean flag = false;
							for (int i=0; i<outEdges.size(); i++)
								if (!(((HecataeusEvolutionNode) outEdges.get(i).getToNode()).getStatus()==HecataeusStatusType.TO_DELETE))
									flag = true;
							if (flag) 
								nr.setStatus(HecataeusStatusType.TO_MODIFY_PROVIDER);
							else nr.setStatus(HecataeusStatusType.TO_DELETE);
							break;

						default: nr.setStatus(HecataeusStatusType.PROMPT);
						}
					}

					//flag node receiver as visited
					nodesVisited.add(nr);

					//determine next to signal
					HecataeusEvolutionEdges inEdges = nr.getInEdges();
					HecataeusEvolutionEvent newEvent;
					//if status <>block prepare next message for queue according to status an node type
					if (nr.getStatus()!= HecataeusStatusType.BLOCKED){

						//get first provider edges
						for (int j=0; j<inEdges.size(); j++){
							//set status for edges
							HecataeusEvolutionEdge e = inEdges.get(j);
							if (e.isProvider()) {
								e.setStatus(nr.getStatus());
								if ((event.getEventType()==HecataeusEventType.ADD_ATTRIBUTE) 
										||(event.getEventType()==HecataeusEventType.ADD_CONDITION)
										||(event.getEventType()==HecataeusEventType.MODIFY_CONDITION)
										||(event.getEventType()==HecataeusEventType.RENAME_RELATION)
										||(event.getEventType()==HecataeusEventType.DELETE_CONDITION)
										||(event.getEventType()==HecataeusEventType.DELETE_RELATION)
								){
									newEvent = new HecataeusEvolutionEvent(nr,newEventType);
									HecataeusEvolutionMessage newMessage = new HecataeusEvolutionMessage(SID,nr,e.getFromNode(),newEvent,e,policyType);
									queue.enqueue(newMessage);
								}else
								{
									if ((nr.getType()!=HecataeusNodeType.NODE_TYPE_QUERY)
											&&(nr.getType()!=HecataeusNodeType.NODE_TYPE_VIEW)
											&&(nr.getType()!=HecataeusNodeType.NODE_TYPE_RELATION)){
										newEvent = new HecataeusEvolutionEvent(e.getFromNode(),newEventType);
										HecataeusEvolutionMessage newMessage = new HecataeusEvolutionMessage(SID,nr,e.getFromNode(),newEvent,e,policyType);
										queue.enqueue(newMessage);
									}
								}
							}
						}
					}

					//get second schema edges, inform then nodes
					for (int j=0; j<inEdges.size(); j++){
						//set status for edges
						HecataeusEvolutionEdge e = inEdges.get(j);
						if (e.isPartOf()) {
							e.setStatus(nr.getStatus());
							newEvent = new HecataeusEvolutionEvent(nr,newEventType);
							HecataeusEvolutionMessage newMessage = new HecataeusEvolutionMessage(SID,nr,e.getFromNode(),newEvent,e,policyType);
							queue.enqueue(newMessage);
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	/**
	 * used for finding the policy of a node
	 * @param1 = event,
	 * @param2 = Node receiving the message
	 * @param3 = The previous prevailing policy
	 **/
	private HecataeusPolicyType determinePolicy(HecataeusEvolutionEvent event, HecataeusEvolutionNode nr, HecataeusEvolutionEdge edge, HecataeusPolicyType previousPolicyType) {

		//  policy hierarchy
		// 1. query.condition, 2. query.attribute, 3. query, 4. relation.condition, 5. relation.attribute, 6. relation
		//  search for policy towards the path 

		System.out.println("determining policy for : " + nr.getName());
		
		if (nr.getName().equalsIgnoreCase("P_Budget")){
			System.out.println("determining policy for : " + nr.getName());
			
		}
		//if edge is part of and child node has policy
		//then override parent's policy 
		if (edge!=null && edge.isPartOf()){
			return previousPolicyType;
		}

		//if edge is dependency then get prevailing policy
		return getPrevailingPolicy(event,nr,previousPolicyType);

	}

	/*
	 * gets the prevailing policy in a module (e.g. a query, relation, view)
	 */
	private HecataeusPolicyType getPrevailingPolicy(HecataeusEvolutionEvent event, HecataeusEvolutionNode nr, HecataeusPolicyType previousPolicyType) {

		HecataeusEvolutionPolicies policies = nr.getPolicies();
		//if policy for this event exist override previousPolicy coming from provider node
		for (int i=0; i<policies.size(); i++){
			HecataeusEvolutionPolicy nrPolicy = policies.get(i);
			if ((nrPolicy.getSourceEvent().getEventNode().equals(event.getEventNode()))
					&&(nrPolicy.getSourceEvent().getEventType()==event.getEventType())){
				return nrPolicy.getPolicyType();
			}
		}

		//If no policy is returned check parents' policy for this event to override provider's policy
		if (nr.getParentNode()!=null) {
			HecataeusEvolutionEvent newEvent = new HecataeusEvolutionEvent(nr,event.getEventType());
			return getPrevailingPolicy(newEvent,nr.getParentNode(),previousPolicyType);
		}

		//if no self or parents policy exists then return provider's policy
		return previousPolicyType;	
	}

	/**
	 * used for finding the providerEdges of a node (through from, map-select, operand edges, gb edges)
	 **/
	public HecataeusEvolutionEdges getProviderEdges(HecataeusEvolutionNode node) {
		HecataeusEvolutionEdges inEdges = node.getInEdges();
		HecataeusEvolutionEdges providerEdges= new HecataeusEvolutionEdges();

		for (int i=0; i<inEdges.size(); i++){
			if ((inEdges.get(i).getType()==HecataeusEdgeType.toEdgeType("EDGE_TYPE_MAPPING"))
					||(inEdges.get(i).getType()==HecataeusEdgeType.toEdgeType("EDGE_TYPE_FROM"))
					||(inEdges.get(i).getType()==HecataeusEdgeType.toEdgeType("EDGE_TYPE_GROUP_BY"))
					||(inEdges.get(i).getType()==HecataeusEdgeType.toEdgeType("EDGE_TYPE_OPERATOR"))
					||(inEdges.get(i).getType()==HecataeusEdgeType.toEdgeType("EDGE_TYPE_ALIAS"))){
				providerEdges.add(inEdges.get(i));
			}
		}
		return providerEdges;	
	}
	/**
	 * used for getting the subgraph of a parent node (query, relation, view)
	 **/
	public HecataeusEvolutionNodes getSubGraph(HecataeusEvolutionNode parentNode) {
		HecataeusEvolutionNodes subGraph = new HecataeusEvolutionNodes();
		subGraph.add(parentNode);
		return this.subGraph(parentNode, subGraph);

	}


	/**
	 * used for getting the subgraph of a parent node (query, relation, view)
	 **/
	private HecataeusEvolutionNodes subGraph(HecataeusEvolutionNode node, HecataeusEvolutionNodes subGraph) {

		HecataeusEvolutionEdges outEdges = node.getOutEdges();

		for (int i=0; i<outEdges.size(); i++){
			//if edge is intramodule then add tonode
			HecataeusEvolutionEdge e = outEdges.get(i);
			if (e.isPartOf()) {
				if ( !(subGraph.contains(e.getToNode())) ) {
					subGraph.add(e.getToNode());
				}
				// call recursively for each adjacent node
				this.subGraph(e.getToNode(), subGraph);
			}
		}
		return subGraph;

	}


	public HecataeusEvolutionGraph importFromXML(File file) {

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
					HecataeusEvolutionNode v = new HecataeusEvolutionNode();
					this.addNode(v);
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
					HecataeusEvolutionEdge e = new HecataeusEvolutionEdge();
					e.setFromNode(getNode(eFromNode));
					e.setToNode(getNode(eToNode));
					this.addEdge(e);
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

						HecataeusEvolutionNode HNode = this.getNode(nHNode);
						HecataeusEvolutionNode HEventNode = this.getNode(nHEventNode);
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

						HecataeusEvolutionNode HNode = this.getNode(nHNode);
						HecataeusEvolutionNode HEventNode = this.getNode(nHEventNode);
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
			this.setKeyGenerator(new Integer(nKey));
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

	public void  exportToXML(File file) {
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
				HecataeusEvolutionNode v = _Nodes.get(forEachVar0);
				// write HNode
				Element elementHnode = document.createElement("HNode");
				
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
				HecataeusEvolutionEdge e = this._Edges.get(forEachVar0);
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
				HecataeusEvolutionNode v = _Nodes.get(forEachVar0);
				HecataeusEvolutionPolicies policies = v.getPolicies();
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
				HecataeusEvolutionEvents events = v.getEvents();
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
			
			
			Element elementHKeyGen = document.createElement("HKeyGen");
			elementHKeyGen.appendChild(document.createTextNode((new Integer(_KeyGenerator)).toString()));
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
	 * propagates the frequency of a query node towards the graph
	 * set the frequency of all provider nodes of a query
	 * @param node
	 */
	public void propagateFrequency(HecataeusEvolutionNode node) {
		
		HecataeusEvolutionNodes subGraph =this.getSubGraph(node); 
		for (int i=0; i< subGraph.size();i++) {
			HecataeusEvolutionNode evNode = subGraph.get(i);
			evNode.setFrequency(node.getFrequency());
		}
		for (int i=0; i< node.getOutEdges().size();i++) {
			HecataeusEvolutionEdge edge = node.getOutEdges().get(i);
			if (edge.isProvider()) {
				edge.getToNode().setFrequency(edge.getToNode().getFrequency()+node.getFrequency());
				this.propagateFrequency(edge.getToNode());
			}
		}
	}
	/**
	 * used for finding the top_level node of each module, given an containing node
	 * @param node
	 * @return
	 */
	public HecataeusEvolutionNode getTopLevelNode(HecataeusEvolutionNode node) {
		if ((node.getType()==HecataeusNodeType.NODE_TYPE_QUERY)
				||(node.getType()==HecataeusNodeType.NODE_TYPE_RELATION )
				||(node.getType()==HecataeusNodeType.NODE_TYPE_VIEW))return node;
		HecataeusEvolutionEdges inEdges = node.getInEdges();
		for (int i=0; i<inEdges.size(); i++){
			HecataeusEvolutionEdge e = inEdges.get(i);
			if (e.isPartOf()){
				return getTopLevelNode(e.getFromNode());
			}
		}
		return null;
	}
	
	 /**
	  * get two set of Nodes (Modules) and return true if they are connected 
	  * @param fromModule
	  * @param toModule
	  * @return
	  */
	 public  boolean isConnected(HecataeusEvolutionNodes fromModule, HecataeusEvolutionNodes toModule) {
		 for (int i = 0; i < fromModule.size(); i++) {
			 HecataeusEvolutionNode node = fromModule.get(i);
			 for (int j = 0; j < node.getOutEdges().size(); j++) {
				 HecataeusEvolutionEdge edge = node.getOutEdges().get(j);
				 if (edge.isProvider()
						 &&toModule.contains(edge.getToNode())
						 ) {
					 return true;
				 }
			 }
		 }
		return false;
	 }
	 /**
	  * get two  Nodes and return the number of paths between them
	  * @param fromNode: starting node 
	  * @param toNode : ending node
	  * @return True if there is a path from fromNode towards toNode, False otherwise
	  */
	 public int getPaths(HecataeusEvolutionNode fromNode, HecataeusEvolutionNode toNode) {
		 int paths=0;
		 for (int i = 0; i < fromNode.getOutEdges().size(); i++) {
			 HecataeusEvolutionEdge edge = fromNode.getOutEdges().get(i);
			 if(edge.getToNode().equals(toNode)) {
				 paths++;
			 }
			paths += this.getPaths(edge.getToNode(), toNode);
		 }
		 return paths;
	 }
		
	 /**
	  * get all nodes of the graph of specific type HecataeusNodeType
	  * @return
	  */
	 public HecataeusEvolutionNodes getNodes(HecataeusNodeType type) {
		 HecataeusEvolutionNodes nodes =  new HecataeusEvolutionNodes();
		 for (int i = 0; i < this._Nodes.size(); i++) {
			 HecataeusEvolutionNode node =  this._Nodes.get(i);
			 if (node.getType()== type) {
				 nodes.add(node);
			 }
		}
		return nodes;
	 }

	 /***
	  * get all edges of the graph of specific type HecataeusEdgeType
	  * @return
	  */
	 public HecataeusEvolutionEdges getEdges(HecataeusEdgeType type) {
		 HecataeusEvolutionEdges edges = new HecataeusEvolutionEdges();
		 for (int i = 0; i < this._Edges.size(); i++) {
			 HecataeusEvolutionEdge edge=  this._Edges.get(i);
			 if (edge.getType()== type) {
				 edges.add(edge);
			 }
		}
		return edges;

	 }
		 
	 /**
	 * get all nodes of the graph
	 * @return
	 */
	 public HecataeusEvolutionNodes getNodes() {
		return this._Nodes;
	}

	/***
	 * get all edges of the graph
	 * @return
	 */
	 public HecataeusEvolutionEdges getEdges() {
		return this._Edges;

	}
	/**
	 * sets the keyGenerator to zero, to start counting the elements from the beginning
	 */
	public void resetKeyGenerator() {
		_KeyGenerator = 0;
	}

	/**
	 * returns the value of the keyGenerator, that is the number of elements the graph has
	 */
	public int getKeyGenerator() {
		return _KeyGenerator;
	}
	/**
	 * sets explicitly the value of the keyGenerator, that is the number of elements the graph has
	 */
	public void setKeyGenerator(int value) {
		this._KeyGenerator = value;
	}

}
