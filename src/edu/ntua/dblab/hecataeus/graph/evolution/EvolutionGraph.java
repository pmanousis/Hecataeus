/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import sun.misc.Queue;

public class EvolutionGraph<V extends EvolutionNode<E>,E extends EvolutionEdge> extends DirectedSparseGraph<V, E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int _KeyGenerator;

	//used by function initializeChange() to increase the SID(Session ID) by one
	static int SIDGenerator = 0;

	public EvolutionGraph() {
	}
		
	/**
	 * adds a new EvolutionNode
	 *  
	 **/
	public boolean addVertex(V Node) {
		// assign key
		Node.setKey(++EvolutionGraph._KeyGenerator);
		return super.addVertex(Node);
	}

	
	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(E Edge, V fromNode, V toNode) {
		return this.addEdge(Edge);		
	}
	
	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(E Edge) {
		Edge.setKey(++EvolutionGraph._KeyGenerator);
		// add edge to incoming edges of ToNode
		V fromNode = (V) Edge.getFromNode();
		if (!fromNode.getOutEdges().contains(Edge))
			fromNode.getOutEdges().add(Edge);
		// add edge to outgoing edges of FromNode
		V toNode = (V) Edge.getToNode();
		if (!toNode.getInEdges().contains(Edge))
			toNode.getInEdges().add(Edge);
		return super.addEdge(Edge, fromNode, toNode);
	}

	public boolean removeEdge(E Edge) {
		// remove edge from inEdges
		Edge.getToNode().getInEdges().remove(Edge);
		// remove edge from outEdges
		Edge.getFromNode().getOutEdges().remove(Edge);
		return super.removeEdge(Edge);
	}
	
	public boolean removeVertex(V Vertex) {
		Vertex.getInEdges().clear();
		Vertex.getOutEdges().clear();
		return super.removeVertex(Vertex);
	}

	/**
	 *  clears all nodes and edges from the graph
	 **/
	public void clear() {
			
		for (E e:this.getEdges()) {
    		this.removeEdge(e);
    	}
		
		for (V v:this.getVertices()) {
    		this.removeVertex(v);
    	}
	
	}
	
	/**
	 * get Vertex by Key
	 **/
	public V findVertex(int Key) {
		 for (V v : this.getVertices()) {
			 if (v.getKey()==Key)
			 return v;
		 }
		 return null;
	}


	/**
	 *  get node by its name, for more than one occurrences, the first is returned
	 **/
	public V findVertexByName(String name) {
		for (V u: this.getVertices()) {
			if (u.getName().toUpperCase().equals(name.toUpperCase())) {
				return u;
			}
		}
		
		return null;
	}

	
	/**
	 * get edge by key
	 **/
	public E findEdge(int Key) {
		for (E e : this.getEdges()) {
			 if (e.getKey()==Key)
			 return e;
		 }
		 return null;	
	}

	/**
	 * get edge by name and type
	 **/
	public E findEdge(String Name, EdgeType Type) {
		for (E u: this.getEdges()) {
			if ((u.getName().toUpperCase().equals(Name.toUpperCase()) && u.getType() == Type ) ) {
				return u;
			}
		}
		return null;
	}

	/**
	 * used for finding explicitly an attribute by its name and its parent relation, view or query node
	 **/
	public V getAttributeNode(String TableName, String AttributeName) {
		for (V u: this.getVertices()) {
			if (u.getName().toUpperCase().equals(TableName)
					&&(u.getType().getCategory() == NodeCategory.MODULE)){
				for (E e :  this.getOutEdges(u)) {
					if ( this.getDest(e).getName().toUpperCase().equals(AttributeName.toUpperCase()) ) {
						return this.getDest(e);
					}
				}
			}
		}
		
		return null;
	}


	/**
	 * calculates the path of nodes that are connected via in edges with a node
	 **/
	public void getInTree(V node, List<V> InNodes) {
		// add itself

		if ( !(InNodes.contains(node)) ) {
			InNodes.add(node);
		}		
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (E e: this.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(InNodes.contains(this.getSource(e))) ) {
				InNodes.add(this.getSource(e)) ;
				// call recursively for each adjacent node
				getInTree(this.getSource(e), InNodes);
			}
		}
	}


	/**
	 * calculates the path of nodes that are connected via out edges with a node
	 **/
	private void getOutTree(V node, List<V> OutNodes) {
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (E e: this.getOutEdges(node)) {
			// count only once if multiple paths found
			if ( !(OutNodes.contains(this.getDest(e))) ) {
				OutNodes.add(this.getDest(e));
				// call recursively for each adjacent node
				this.getOutTree(this.getDest(e), OutNodes);
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
	private void getInPath(V node, List<E> InEdges) {
		// for each node edge add in edge to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (E e : this.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(InEdges.contains(e))) {
				InEdges.add(e);
				// call recursively for each adjacent node
				getInPath(this.getSource(e), InEdges);
			}
		}
	}


	/**
	 * calculates the path of outgoing edges that are connected with a node
	 **/
	private void getOutPath(V node, List<E> OutEdges) {
		// for each node edge add out edge to collection
		// only adjacent nodes connected via a directed edge
		// FROM the affecting node are affected
		for (E e : this.getInEdges(node)) {
			// count only once if multiple paths found
			if ( !(OutEdges.contains(e)) ) {
				OutEdges.add(e);
				// call recursively for each adjacent node
				getOutPath(this.getDest(e), OutEdges);
			}
		}
	}

	/**
	 *  returns the set of nodes affected by an event forced on the affecting_node
	 **/
	public List<V> getAffectedNodes(V affecting_node, EventType eventType) {
		List<V> AffectedNodes = new ArrayList<V>();
		getInTree(affecting_node, AffectedNodes);
		return AffectedNodes;
	}

	/**
	 *  returns the set of incoming edges affected by an event forced on the affecting_node
	 **/
	public List<E> getAffectedEdges(V affecting_node, EventType eventType) {
		List<E> AffectedEdges = new ArrayList<E>();
		getInPath(affecting_node, AffectedEdges);
		return AffectedEdges;
	}

	/**
	 *  makes the necessary initializations to execute propagateChanges()
	 **/
	public void initializeChange(EvolutionEvent event){

		SIDGenerator = SIDGenerator + 1;
		int SID = SIDGenerator;
		PolicyType Default_System_Policy = PolicyType.PROPAGATE;
		Queue queue = new Queue();
		EvolutionMessage firstMessage = new EvolutionMessage(SID,null,event.getEventNode(),event,null,Default_System_Policy);
		queue.enqueue(firstMessage);
		System.out.println("in initializeChange Before call propagateChanges");
		List<V> nodesVisited = new ArrayList<V>();
		propagateChanges(queue, nodesVisited);

	}

//	/**
//	 *  determines the status given that prevailing policy is propagate 
//	 *  and the raised event of a node according to
//	 *  1. the type of event
//	 *  2. the scope of event (self, child, provider)
//	 *  3. the type of node
//	 **/
//	private void determineStatus(EvolutionMessage message) {
//		int SID = message.get_SID();
//		EvolutionNode ns = message.getNodeSender();
//		EvolutionNode nr = message.getNodeReceiver();
//		EvolutionEvent event = message.getEvent();
//		EvolutionEdge edge = message.getEdge();
//		PolicyType policyType = message.getPolicyType();
//		
//		//initialize the event of the next message as the old event
//		EventType newEventType = event.getEventType();
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
//				nr.setStatus(StatusType.TO_ADD_CHILD);
//				newEventType = EventType.ADD_ATTRIBUTE;
//				break;
//			case NODE_TYPE_VIEW:
//				 //both from provider or self the reaction is the same
//				nr.setStatus(StatusType.TO_ADD_CHILD);
//				newEventType = EventType.ADD_ATTRIBUTE;
//				break;
//			case NODE_TYPE_QUERY:
//				 //both from provider or self the reaction is the same
//				nr.setStatus(StatusType.TO_ADD_CHILD);
//				newEventType = EventType.ADD_ATTRIBUTE;
//				break;
//			case NODE_TYPE_OPERAND:
//				 //the add attribute came from a subquery
//				nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
//				newEventType = EventType.MODIFY_CONDITION;
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
//				nr.setStatus(StatusType.TO_ADD_CHILD);
//				newEventType = EventType.ADD_CONDITION;
//				break;
//			case NODE_TYPE_VIEW:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
//					newEventType = EventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(StatusType.TO_ADD_CHILD);
//					newEventType = EventType.ADD_CONDITION;
//				}
//				break;
//			case NODE_TYPE_QUERY:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
//					newEventType = EventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(StatusType.TO_ADD_CHILD);
//					newEventType = EventType.ADD_CONDITION;
//				}
//				break;
//			case NODE_TYPE_ATTRIBUTE:
//				 //a constraint is added to 
//				nr.setStatus(StatusType.TO_ADD_CHILD);
//				newEventType = EventType.ADD_CONDITION;
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
//					nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
//					newEventType = EventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(StatusType.TO_ADD_CHILD);
//					newEventType = EventType.ADD_CONDITION;
//				}
//				break;
//			case NODE_TYPE_QUERY:
//				 //examine whether add condition comes from self or provider edge
//				if (edge.isProvider()) {
//					nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
//					newEventType = EventType.MODIFY_CONDITION;
//				}else {
//					nr.setStatus(StatusType.TO_ADD_CHILD);
//					newEventType = EventType.ADD_CONDITION;
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
	private void propagateChanges(Queue queue, List<V> nodesVisited) {

		while (!queue.isEmpty()){
			try {
				EvolutionMessage currentMessage = (EvolutionMessage) queue.dequeue();

				int SID = currentMessage.get_SID();
				V ns = (V)currentMessage.getNodeSender();
				V nr = (V)currentMessage.getNodeReceiver();
				EvolutionEvent event = currentMessage.getEvent();
				E edge = (E)currentMessage.getEdge();
				PolicyType policyType = currentMessage.getPolicyType();

				if (!nodesVisited.contains(nr)) {
					//initialize the event of the next message as the old event
					EventType newEventType = event.getEventType();

					//determine policy for current node
					policyType = determinePolicy(event,nr,edge,policyType);

					if (policyType==PolicyType.PROMPT){
						System.out.println("Policy=prompt");
						nr.setStatus(StatusType.PROMPT);
					}else if(policyType==PolicyType.BLOCK){
						System.out.println("Policy=block");
						nr.setStatus(StatusType.BLOCKED);
					}else {
						//policy is propagate
						//determine status for node according to eventType
						switch (event.getEventType()) {

						case ADD_ATTRIBUTE:
							System.out.println("in add_Attribute");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							//if receiver node is operand then add_attribute occurred in subquery
							if (nr.getType()==NodeType.NODE_TYPE_OPERAND) {
								//new event is modify condition
								newEventType = EventType.MODIFY_CONDITION;
								nr.setStatus(StatusType.TO_MODIFY_PROVIDER);	
							}else {
								//new event is add attribute
								//only through schema and top-level provider edges
								newEventType = EventType.ADD_ATTRIBUTE;
								nr.setStatus(StatusType.TO_ADD_CHILD);
								
							}
							break;

						case ADD_CONDITION:
							System.out.println("in Add_Condition");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
							
							//propagate the same event
							newEventType = event.getEventType();
							//new event is add condition only through schema and top-level provider edges
							break;
						case ADD_RELATION:
							nr.setStatus(StatusType.PROMPT);
							break;
						case MODIFY_CONDITION:
							System.out.println("in Modify_Condition");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
							//new event is modify condition only through schema and top-level provider edges
							//propagate the same event
							newEventType = event.getEventType();
							break;
						case MODIFYDOMAIN_ATTRIBUTE:

							System.out.println("in MODIFYDOMAIN_ATTRIBUTE");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							if (nr.getType()==NodeType.NODE_TYPE_ATTRIBUTE) {
								nr.setStatus(StatusType.TO_MODIFY);
								//new event to modify domain
							}
							else nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
							//propagate the same event
							newEventType = event.getEventType();
							break;
						case RENAME_ATTRIBUTE:
							System.out.println("in rename_Attribute");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							if (nr.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
								nr.setStatus(StatusType.TO_RENAME);
							//new event = rename attribute
							else nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
							//new event = rename attribute	
							//propagate the same event
							newEventType = event.getEventType();
							break;

						case RENAME_RELATION:
							System.out.println("in Rename_Relation");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(StatusType.TO_RENAME);
							break;

						case DELETE_ATTRIBUTE:
							System.out.println("in Remove_Attribute");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							if ((nr.getType()==NodeType.NODE_TYPE_QUERY)
									||(nr.getType()==NodeType.NODE_TYPE_VIEW)
									||(nr.getType()==NodeType.NODE_TYPE_RELATION))

							{
								/**
								 *event has come from other view and not from attribute 
								 *correct algorithm in determine next to signal
								 **/
								if (edge!=null&&edge.isProvider()) {
									nodesVisited.remove(nr);
									break;
								}

								newEventType = EventType.DELETE_RELATION;
								nr.setStatus(StatusType.TO_DELETE);
								//new event = remove relation
								//check if has any child with no delete
								for (E e : this.getOutEdges(nr)) {
									if(e.isPartOf()&&e.getToNode().getStatus()!=StatusType.TO_DELETE) {
										newEventType = EventType.DELETE_ATTRIBUTE;
										nr.setStatus(StatusType.TO_DELETE_CHILD);
										//new event = remove attribute
									}
								}
							}
							else if ((nr.getType()==NodeType.NODE_TYPE_GROUP_BY)
									||(nr.getType()==NodeType.NODE_TYPE_FUNCTION)
							)
							{
								newEventType = EventType.DELETE_ATTRIBUTE;
								nr.setStatus(StatusType.TO_DELETE);
								//new event = REMOVE_ATTRIBUTE relation
								//check if has any child as constant
								for (E e : this.getOutEdges(nr)) {
									if(e.getToNode().getType()==NodeType.NODE_TYPE_ATTRIBUTE && e.getToNode().getStatus()!=StatusType.TO_DELETE) {
										nr.setStatus(StatusType.TO_DELETE_CHILD);
										newEventType = EventType.MODIFY_CONDITION;
										//new event = remove attribute
									}
								}
							}


							else if (nr.getType()==NodeType.NODE_TYPE_OPERAND) {
								newEventType = EventType.DELETE_CONDITION;
								nr.setStatus(StatusType.TO_DELETE);
							}
							//new event = remove condition
							else if (nr.getType()==NodeType.NODE_TYPE_ATTRIBUTE) {
								newEventType= EventType.DELETE_ATTRIBUTE;
								nr.setStatus(StatusType.TO_DELETE);
							}
							//new event = remove attribute				
							break;

						case DELETE_CONDITION:
							System.out.println("in Remove_Condition");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");
							nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
							//new event is remove condition only through schema and top-level provider edges
							break;

						case DELETE_RELATION:
							System.out.println("in Remove_Relation");
							System.out.println("nr is: "+nr.getName());
							System.out.println("Policy=propagate");

							List<E> outEdges = new ArrayList<E>(this.getOutEdges(nr));
							if (ns==null)
								for (E schemaEdge : outEdges){
									this.getDest(schemaEdge).setStatus(StatusType.TO_DELETE);
									initializeChange(new EvolutionEvent(schemaEdge.getToNode(),EventType.DELETE_ATTRIBUTE));
								}

							Boolean flag = false;
							for (E schemaEdge : outEdges)
								if (!(this.getDest(schemaEdge).getStatus()==StatusType.TO_DELETE))
										flag = true;
							if (flag) 
								nr.setStatus(StatusType.TO_MODIFY_PROVIDER);
							else nr.setStatus(StatusType.TO_DELETE);
							break;

						default: nr.setStatus(StatusType.PROMPT);
						}
					}

					//flag node receiver as visited
					nodesVisited.add(nr);

					//determine next to signal
					List<E> inEdges =  new ArrayList<E>(this.getInEdges(nr));
					EvolutionEvent newEvent;
					//if status <>block prepare next message for queue according to status an node type
					if (nr.getStatus()!= StatusType.BLOCKED){

						//get first provider edges
						for (E e : inEdges){
							if (e.isProvider()) {
								e.setStatus(nr.getStatus());
								if ((event.getEventType()==EventType.ADD_ATTRIBUTE) 
										||(event.getEventType()==EventType.ADD_CONDITION)
										||(event.getEventType()==EventType.MODIFY_CONDITION)
										||(event.getEventType()==EventType.RENAME_RELATION)
										||(event.getEventType()==EventType.DELETE_CONDITION)
										||(event.getEventType()==EventType.DELETE_RELATION)
								){
									newEvent = new EvolutionEvent(nr,newEventType);
									EvolutionMessage newMessage = new EvolutionMessage(SID,nr,e.getFromNode(),newEvent,e,policyType);
									queue.enqueue(newMessage);
								}else
								{
									if ((nr.getType()!=NodeType.NODE_TYPE_QUERY)
											&&(nr.getType()!=NodeType.NODE_TYPE_VIEW)
											&&(nr.getType()!=NodeType.NODE_TYPE_RELATION)){
										newEvent = new EvolutionEvent(e.getFromNode(),newEventType);
										EvolutionMessage newMessage = new EvolutionMessage(SID,nr,e.getFromNode(),newEvent,e,policyType);
										queue.enqueue(newMessage);
									}
								}
							}
						}
					}

					//get second schema edges, inform then nodes
					for (E e: inEdges){
						if (e.isPartOf()) {
							e.setStatus(nr.getStatus());
							newEvent = new EvolutionEvent(nr,newEventType);
							EvolutionMessage newMessage = new EvolutionMessage(SID,nr,e.getFromNode(),newEvent,e,policyType);
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
	private PolicyType determinePolicy(EvolutionEvent event, V nr, E edge, PolicyType previousPolicyType) {

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
	private PolicyType getPrevailingPolicy(EvolutionEvent event, V nr, PolicyType previousPolicyType) {

		EvolutionPolicies policies = nr.getPolicies();
		//if policy for this event exist override previousPolicy coming from provider node
		for (EvolutionPolicy nrPolicy : policies){
			if ((nrPolicy.getSourceEvent().getEventNode().equals(event.getEventNode()))
					&&(nrPolicy.getSourceEvent().getEventType()==event.getEventType())){
				return nrPolicy.getPolicyType();
			}
		}

		//If no policy is returned check parents' policy for this event to override provider's policy
		if (this.getParentNode(nr)!=null) {
			EvolutionEvent newEvent = new EvolutionEvent(nr,event.getEventType());
			return getPrevailingPolicy(newEvent,this.getParentNode(nr),previousPolicyType);
		}

		//if no self or parents policy exists then return provider's policy
		return previousPolicyType;	
	}

	/**
	 * used for finding the providerEdges of a node (through from, map-select, operand edges, gb edges)
	 **/
	public List<E> getProviderEdges(V node) {
	
		List<E> providerEdges= new ArrayList<E>();

		for (E e: this.getInEdges(node)){
			if ((e.getType()==EdgeType.toEdgeType("EDGE_TYPE_MAPPING"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_FROM"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_GROUP_BY"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_OPERATOR"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_ALIAS"))){
				providerEdges.add(e);
			}
		}
		return providerEdges;	
	}
	/**
	 * used for getting the subgraph of a module (query, relation, view)
	 **/
	public List<V> getModule(V parentNode) {
		List<V> subGraph = new ArrayList<V>();
		subGraph.add(parentNode);
		return this.subGraph(parentNode, subGraph);

	}


	/**
	 * used for getting the subgraph of a parent node (query, relation, view)
	 **/
	private List<V> subGraph(V node, List<V> subGraph) {

		for (E e : this.getOutEdges(node)){
			//if edge is intramodule then add tonode
			if (e.isPartOf()) {
				if ( !(subGraph.contains(this.getDest(e)))) {
					subGraph.add(this.getDest(e));
				}
				// call recursively for each adjacent node
				this.subGraph(this.getDest(e), subGraph);
			}
		}
		return subGraph;

	}
		
	/**
	 * propagates the frequency of a query node towards the graph
	 * set the frequency of all provider nodes of a query
	 * @param node
	 */
	public void propagateFrequency(V node) {
		
		List<V> subGraph =this.getModule(node); 
		for (V evNode : subGraph) {
			evNode.setFrequency(node.getFrequency());
		}
		for (E edge : this.getOutEdges(node)) {
			if (edge.isProvider()) {
				edge.getToNode().setFrequency(edge.getToNode().getFrequency()+node.getFrequency());
				this.propagateFrequency(this.getDest(edge));
			}
		}
	}
	/**
	 * used for finding the top_level node of each module, given an containing node
	 * @param node
	 * @return
	 */
	public V getTopLevelNode(V node) {
		if (node.getType().getCategory()==NodeCategory.MODULE)
			return node;
		for (E e : this.getInEdges(node)){
			if (e.isPartOf()){
				return getTopLevelNode(this.getSource(e));
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
	 public  boolean isConnected(List<V> fromModule, List<V> toModule) {
		 for (V node : fromModule) {
			 for (E edge : this.getOutEdges(node)) {
				 if (edge.isProvider()&&toModule.contains(edge.getToNode())) 
					 return true;
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
	 public int getPaths(V fromNode, V toNode) {
		 int paths=0;
		 for ( E edge : this.getOutEdges(fromNode)) {
			 if(edge.getToNode().equals(toNode)) {
				 paths++;
			 }
			paths += this.getPaths(this.getDest(edge), toNode);
		 }
		 return paths;
	 }

	 /**
	  * @return
	  * all nodes of the graph of specific type category NodeCategory
	  */
	 public List<V> getVertices(NodeCategory category) {
		 List<V> nodes =  new ArrayList<V>();
		 for (V node: this.getVertices()) {
			 if (node.getType().getCategory()== category) {
				 nodes.add(node);
			 }
		}
		return nodes;
	 }
	 
	 /**
	  * @return
	  * all nodes of the graph of specific type NodeType
	  */
	 public List<V> getVertices(NodeType type) {
		 List<V> nodes =  new ArrayList<V>();
		 for (V node: this.getVertices()) {
			 if (node.getType()== type) {
				 nodes.add(node);
			 }
		}
		return nodes;
	 }

	 /***
	  * 
	  * @return
	  * all edges of the graph of specific type EdgeType
	  */
	 public List<E> getEdges(EdgeType type) {
		 List<E> edges = new ArrayList<E>();
		 for (E edge: this.getEdges()) {
			 if (edge.getType()== type) {
				 edges.add(edge);
			 }
		}
		return edges;

	 }
		
	 /**
	  * used for finding the parent of a node (query, view, relation)
	  **/
	 public V getParentNode(V node) {
		 for (E e: this.getInEdges(node)){
			 //if node is attribute then 
			 if (((node.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
					 && (e.getType()==EdgeType.EDGE_TYPE_SCHEMA))
					 ||((node.getType()==NodeType.NODE_TYPE_CONDITION)
							 && (e.getType()==EdgeType.EDGE_TYPE_OPERATOR))		
							 ||((node.getType()==NodeType.NODE_TYPE_OPERAND)
									 && ((e.getType()==EdgeType.EDGE_TYPE_OPERATOR)
											 ||(e.getType()==EdgeType.EDGE_TYPE_WHERE)))		
											 ||(node.getType()==NodeType.NODE_TYPE_CONSTANT)		
											 ||((node.getType()==NodeType.NODE_TYPE_GROUP_BY)
													 && (e.getType()==EdgeType.EDGE_TYPE_GROUP_BY))		
													 ||(node.getType()==NodeType.NODE_TYPE_FUNCTION)
			 )
				 return this.getSource(e);
		 }
		 return null;
	 }
		

	/**
	 * sets the keyGenerator to zero, to start counting the elements from the beginning
	 */
	public void resetKeyGenerator() {
		EvolutionGraph._KeyGenerator = 0;
	}

	/**
	 * returns the value of the keyGenerator, that is the number of elements the graph has
	 */
	public int getKeyGenerator() {
		return EvolutionGraph._KeyGenerator;
	}
	/**
	 * sets explicitly the value of the keyGenerator, that is the number of elements the graph has
	 */
	public void setKeyGenerator(int value) {
		EvolutionGraph._KeyGenerator = value;
	}

	//collection of string for holding default policy definitions
	private ArrayList<String> _defaultPolicyClauses = new ArrayList<String>(); 
	public ArrayList<String> getDefaultPolicyDecsriptions(){
		return this._defaultPolicyClauses;
	}
	
	/***
	 * Creates a new graph object containing the nodes of the argument
	 * It does not clone the nodes / edges. It just creates a new graph
	 * and adds them to the collection of graph's nodes and edges 
	 * @return a graph object
	 */
	public <G extends EvolutionGraph<V,E>> G toGraph(List<V> nodes){
		G subGraph;
		try {
			subGraph = (G) this.getClass().newInstance();
			for(V vertex : nodes) {
				subGraph.addVertex(vertex);
				Collection<E> incidentEdges = this.getIncidentEdges(vertex);
				for(E edge : incidentEdges) {
					Pair<V> endpoints = this.getEndpoints(edge);
					if(nodes.containsAll(endpoints)) {
						// put this edge into the subgraph
						subGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
					}
				}
			}
			return subGraph;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
