/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.EvolutionToVisualConverter;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.Message;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.MessageCompare;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.ModuleMaestro;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.ModuleMaestroRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.ModuleNode;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.StopWatch;
import edu.ntua.dblab.hecataeus.graph.util.Observable;
import edu.ntua.dblab.hecataeus.graph.util.Observer;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class EvolutionGraph extends DirectedSparseGraph<EvolutionNode, EvolutionEdge> implements Observable {
	private static final long serialVersionUID = 1L;
	private static int KEY_GENERATOR;

	private final EvolutionToVisualConverter graphConverter = new EvolutionToVisualConverter();
	private final ArrayList<Observer> observers = new ArrayList<>();
	private final Map<EvolutionNode, Integer> nodeKeys = new HashMap<>();
	private final Map<EvolutionEdge, Integer> edgeKeys = new HashMap<>();

	private EvolutionNode arxikoModule = null;

	public EvolutionGraph() {
	}

	public boolean addVertex(EvolutionNode Node) {
		nodeKeys.put(Node, ++EvolutionGraph.KEY_GENERATOR);

		if (super.addVertex(Node)) {
			notifyObserversForVertexAdd(Node);
			return true;
		}
		return false;
	}

	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(EvolutionEdge Edge) {
		edgeKeys.put(Edge, ++EvolutionGraph.KEY_GENERATOR);
		// add edge to incoming edges of ToNode
		EvolutionNode fromNode =   Edge.getFromNode();
		if (fromNode == null || fromNode.getOutEdges() == null) {
			if (fromNode == null) {
				System.out.println("85, fromNode=NULL!!! on edge: " + Edge.getName() + " to node:" + Edge.getToNode());
			} else {
				System.out.println("89, line: " + fromNode.getName());
			}
		} else if (!fromNode.getOutEdges().contains(Edge))
			fromNode.getOutEdges().add(Edge);
		// add edge to outgoing edges of FromNode
		EvolutionNode toNode =   Edge.getToNode();
		if (!toNode.getInEdges().contains(Edge))
			toNode.getInEdges().add(Edge);

		if (super.addEdge(Edge, fromNode, toNode)) {
			notifyObserversForEdgeAdd(Edge);
			return true;
		}
		return false;
	}

	public boolean removeEdge(EvolutionEdge Edge) {
		// remove edge from inEdges
		Edge.getToNode().getInEdges().remove(Edge);
		// remove edge from outEdges
		Edge.getFromNode().getOutEdges().remove(Edge);
		edgeKeys.remove(Edge);

		if (super.removeEdge(Edge)) {
			notifyObserversForEdgeRemove(Edge);
			return true;
		}
		return false;
	}

	public boolean removeVertex(EvolutionNode Vertex) {
		Vertex.getInEdges().clear();
		Vertex.getOutEdges().clear();
		nodeKeys.remove(Vertex);

		if (super.removeVertex(Vertex)) {
			notifyObserversForVertexRemove(Vertex);
			return true;
		}
		return false;
	}

	/**
	 * clears all nodes and edges from the graph
	 **/
	public void clear() {
		for (EvolutionEdge e : new ArrayList<>(this.getEdges()))
			this.removeEdge(e);
		
		for (EvolutionNode v : new ArrayList<>(this.getVertices()))
			this.removeVertex(v);

		Iterator<Observer> obsIte = observers.iterator();
		while (obsIte.hasNext()) {
			obsIte.next();
			obsIte.remove();
		}
	}

	public EvolutionNode findVertex(int key) {
		for (EvolutionNode v : this.getVertices()) {
			if (nodeKeys.get(v) == key)
				return v;
		}
		return null;
	}

	public Integer getNodeKey(EvolutionNode node) {
		return nodeKeys.get(node);
	}

	public void setNodeKey(EvolutionNode node, Integer key) {
		nodeKeys.remove(node);
		nodeKeys.put(node, key);
	}

	/**
	 * get node by its name, for more than one occurrences, the first is returned
	 **/
	public EvolutionNode findVertexByName(String name) {
		for (EvolutionNode u : this.getVertices()) {
			if (u.getName().equalsIgnoreCase(name)) {
				return u;
			}
		}
		return null;
	}

	/**
	 * get node by its name and type category, for more than one occurrences, the
	 * first is returned
	 **/
	public EvolutionNode findVertexByName(String name, NodeCategory nc) {
		EvolutionNode node = findVertexByName(name);
		if (node != null && node.getType().getCategory() == nc)
			return node;
		return null;
	}

	/**
	 * get node by its name, after finding his parent OTHERWISE return node
	 **/
	public EvolutionNode findVertexByNameParent(String name) {
		String parent = "";
		String node = "";
		if (name.contains(".")) {
			parent = name.substring(0, name.indexOf("."));
			node = name.substring(name.indexOf(".") + 1);
			for (EvolutionNode u : this.getVertices()) {
				if (u.getName().toUpperCase().equals(parent.toUpperCase())) {
					for (int i = 0; i < u.getOutEdges().size(); i++) {
						if (u.getOutEdges().get(i).getToNode().getName().equals(node.toUpperCase())) {
							return   (u.getOutEdges().get(i).getToNode());
						}
					}
				}
			}
		} else {
			return (findVertexByName(name));
		}
		return null;
	}

	public EvolutionNode findVertexById(double iD) {
		for (EvolutionNode u : this.getVertices(NodeCategory.MODULE)) {
			if (u.getID() == iD) {
				return u;
			}
		}
		return null;
	}

	/**
	 * get Key of an edge
	 **/
	public Integer getEdgeKey(EvolutionEdge edge) {
		return edgeKeys.get(edge);
	}

	/**
	 * set Key of an edge
	 **/
	public void setEdgeKey(EvolutionEdge edge, Integer key) {
		edgeKeys.remove(edge);
		edgeKeys.put(edge, key);
	}

	/**
	 * makes the necessary initializations to execute propagateChanges()
	 **/
	public void initializeChange(EvolutionEvent event) {

		setArxikoModule(null);
		for (Entry<EvolutionNode, Pair<Map<EvolutionNode, EvolutionEdge>>> entry : this.vertices.entrySet()) { // Clear statuses of nodes.
			entry.getKey().setStatus(StatusType.NO_STATUS, true);
		}
		for (Entry<EvolutionEdge, Pair<EvolutionNode>> entry : this.edges.entrySet()) { // Clear statuses of edges.
			entry.getKey().setStatus(StatusType.NO_STATUS, true);
		}

		EvolutionNode node = event.getEventNode();
		EvolutionNode toNode = null;
		EvolutionNode toSchema = null;
		String parameter = "";
		switch (node.getType()) {
		case NODE_TYPE_RELATION:
		case NODE_TYPE_QUERY:
		case NODE_TYPE_VIEW:
			toNode = node;
			for (int i = 0; i < node.getOutEdges().size(); i++) {
				toSchema =   node.getOutEdges().get(i).getToNode();
				if (toSchema.getType() == NodeType.NODE_TYPE_OUTPUT) {
					parameter = node.getName();
					break;
				}
			}
			break;

		case NODE_TYPE_OUTPUT:
		case NODE_TYPE_SEMANTICS:
			toSchema = node;
			for (int i = 0; i < node.getInEdges().size(); i++) {
				if (node.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT ||
					node.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
					toNode =   node.getInEdges().get(i).getFromNode();
					break;
				}
				parameter = node.getName();
				if (event.getEventType() == EventType.ADD_ATTRIBUTE) {
					parameter = "";
				}

			}
			break;

		default:
			for (int i = 0; i < node.getInEdges().size(); i++) {
				if (node.getInEdges().get(i).getFromNode().getType() == NodeType.NODE_TYPE_OUTPUT) {
					toSchema =   node.getInEdges().get(i).getFromNode();
					for (int j = 0; j < toSchema.getInEdges().size(); j++) {
						if (toSchema.getInEdges().get(j).getFromNode().getType() == NodeType.NODE_TYPE_RELATION ||
							toSchema.getInEdges().get(j).getFromNode().getType() == NodeType.NODE_TYPE_QUERY ||
							toSchema.getInEdges().get(j).getFromNode().getType() == NodeType.NODE_TYPE_VIEW) {
							toNode =   toSchema.getInEdges().get(j).getFromNode();
						}
					}
					parameter = node.getName();
					if (event.getEventType() == EventType.DELETE_SELF) {
						event.setEventType(EventType.DELETE_ATTRIBUTE);
					} else if (event.getEventType() == EventType.RENAME_SELF) {
						event.setEventType(EventType.RENAME_ATTRIBUTE);
					}
					break;
				}
			}
			if (toSchema == null) {
				while (toNode == null) {
					toSchema =   node.getInEdges().get(0).getFromNode();
					if (toSchema.getType() == NodeType.NODE_TYPE_SEMANTICS) {
						event.setEventType(EventType.ALTER_SEMANTICS);
						toNode =   toSchema.getInEdges().get(0).getFromNode();
					}
				}
			}
			break;
		}
		Message  firstMessage = new Message (toNode, toSchema, event.getEventType(), parameter);
		propagateChanges(firstMessage);
	}

	private void setArxikoModule(EvolutionNode toNode) {
		if (arxikoModule == null || toNode == null) {
			arxikoModule = toNode;
		}
	}

	/**
	 * sets the status of the parts of the graph affected by an event
	 **/
	private void propagateChanges(Message  message) {
		setArxikoModule(message.toNode);
		EvolutionGraph  ograph = new EvolutionGraph ();
		ograph.vertices.putAll(this.vertices);
		int modulesAffected = 0;
		int internalsAffected = 0;
		int numberOfModules = 0;
		int numberOfNodes = 0;
		PriorityQueue<Message > queue = new PriorityQueue<Message >(1, new MessageCompare());
		queue.add(message);
		List<ModuleNode > epireasmenoi = new LinkedList<ModuleNode >();
		StopWatch step1 =
			new StopWatch(); /** @author pmanousi For time count of step 1. */
		step1.start();
		while (!queue.isEmpty()) {
			try {
				ModuleMaestro  maestro = new ModuleMaestro (queue);
				PriorityQueue<Message > messages = new PriorityQueue<Message >();
				messages.add(maestro.arxikoMinima.clone());
				Iterator<Message > i = maestro.myQueue.iterator();
				while (i.hasNext()) {
					Message  tmpPMMsg = i.next();
					if (messages.contains(tmpPMMsg) == false) {
						messages.add(tmpPMMsg.clone());
					}
				}
				epireasmenoi.add(new ModuleNode(maestro.arxikoMinima.toNode, messages, message.event));
				maestro.propagateMessages(); // Status determination
			} catch (Exception e) {
			}
		}
		step1.stop(); /** @author pmanousi For time count of step 1. */
		for (Entry<EvolutionNode, Pair<Map<EvolutionNode, EvolutionEdge>>> entry : this.vertices.entrySet()) {
			if (entry.getKey().getStatus() != StatusType.NO_STATUS &&
				entry.getKey().getType() != NodeType.NODE_TYPE_OPERAND) {
				entry.getKey();
				if (entry.getKey().getType().getCategory() == NodeCategory.MODULE && entry.getKey() != arxikoModule) {
					modulesAffected++;
				} else {
					if (entry.getKey().getType().getCategory() != NodeCategory.INOUTSCHEMA &&
						entry.getKey().getType().getCategory() != NodeCategory.SEMANTICS &&
						entry.getKey().getType().getCategory() != NodeCategory.MODULE) {
						internalsAffected++;
					}
				}
			}
		}
		numberOfModules =
			this.getVertices(NodeType.NODE_TYPE_QUERY).size() + this.getVertices(NodeType.NODE_TYPE_VIEW).size();
		numberOfNodes = this.getVertexCount();
		List<EvolutionNode> rel = this.getVertices(NodeType.NODE_TYPE_RELATION);
		for (int i = 0; i < rel.size(); i++) {
			rel.get(i);
			EvolutionNode schemaProsElegxo =   rel.get(i).getOutEdges().get(0).getToNode();
			schemaProsElegxo.getOutEdges().size();
		}
		for (int k = 0; k < epireasmenoi.size(); k++) {
			epireasmenoi.get(k).setEmeis(epireasmenoi);
		}
		// Check graph for block status
		StatusType graphStatus = StatusType.PROPAGATE;
		Iterator<ModuleNode > i = epireasmenoi.iterator();
		StopWatch step2 =
			new StopWatch(); /** @author pmanousi For time count of step 2. */
		step2.start();
		while (i.hasNext()) {
			ModuleNode  prosElegxo = i.next();
			if (prosElegxo.getStatus() == StatusType.BLOCKED) {
				graphStatus = StatusType.BLOCKED;
				prosElegxo.backPropagation();
				prosElegxo.neededRewrites = 0;
			}
		}
		step2.stop(); /** @author pmanousi For time count of step 2. */
		MetriseisRewrite mr = new MetriseisRewrite();
		int clonedModules = 0;
		int rewrittenModules = 0;
		StopWatch step3 =
			new StopWatch(); /** @author pmanousi For time count of step 3. */
		step3.start();
		if (graphStatus == StatusType.BLOCKED) {
			if (message.toNode.getType() == NodeType.NODE_TYPE_RELATION && message.event != EventType.ADD_ATTRIBUTE) { // Whatever happens to relation stops there!
				rewrittenModules = 0;
			} else {
				i = epireasmenoi.iterator();
				String tempParam = "";
				while (i.hasNext()) {
					ModuleNode  prosEpaneggrafi = i.next();
					if (prosEpaneggrafi.neededRewrites == 1) { // They move to new version.
						ModuleMaestroRewrite  m = new ModuleMaestroRewrite (prosEpaneggrafi.messages);
						tempParam = m.doRewrite(tempParam, this, step3, mr);
						m.moveToNewInputsIfExist(this, prosEpaneggrafi.module);
						rewrittenModules++;
					} else if (prosEpaneggrafi.neededRewrites == 2) { // They copy themselves and do rewrite on new version.
						EvolutionNode neos = prosEpaneggrafi.cloneQVModule(this);
						clonedModules++;
						Iterator<Message > j = prosEpaneggrafi.messages.iterator();
						while (j.hasNext()) { // messages are for neos node...
							Message  n = j.next();
							for (EvolutionEdge noe : neos.getOutEdges()) {
								if (noe.getToNode().getName().equals(
									n.toSchema.getName().replace(n.toNode.getName(), neos.getName()))) {
									n.toSchema =   noe.getToNode();
								}
							}
							n.toNode = neos;
						}
						prosEpaneggrafi.module = neos;
						ModuleMaestroRewrite  m = new ModuleMaestroRewrite (prosEpaneggrafi.messages);
						tempParam = m.doRewrite(tempParam, this, step3, mr);
						m.moveToNewInputsIfExist(this, prosEpaneggrafi.module);
						rewrittenModules++;
					}
				}
			}
		} else {
			i = epireasmenoi.iterator();
			String tempParam = "";
			while (i.hasNext()) {
				ModuleNode  prosEpaneggrafi = i.next();

				ModuleMaestroRewrite  rewriter = new ModuleMaestroRewrite (prosEpaneggrafi.messages);
				tempParam = rewriter.doRewrite(tempParam, this, step3, mr); // Rewrite
				rewrittenModules++;
			}
		}
		step3.stop(); /** * @author pmanousi For time count of step 3. */
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("time.csv", true)));
			out.println(message.event.toString() + ": " + message.toSchema.getName() + "." + message.parameter + "," +
				modulesAffected + "," + numberOfModules + "," + internalsAffected + "," + numberOfNodes + "," +
				rewrittenModules + "," + clonedModules + "," + step1.toString() + "," + step2.toString() + "," +
				step3.toString());
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * used for getting the subgraph of a module (query, relation, view)
	 **/
	public List<EvolutionNode> getModule(EvolutionNode parentNode) {
		List<EvolutionNode> subGraph = new ArrayList<EvolutionNode>();
		subGraph.add(parentNode);
		return this.subGraph(parentNode, subGraph);

	}

	/**
	 * used for getting the subgraph of a parent node (query, relation, view)
	 **/
	private List<EvolutionNode> subGraph(EvolutionNode node, List<EvolutionNode> subGraph) {

		for (EvolutionEdge e : this.getOutEdges(node)) {
			//if edge is intramodule then add tonode
			if (e.isPartOf()) {
				if (!(subGraph.contains(this.getDest(e)))) {
					subGraph.add(this.getDest(e));
				}
				// call recursively for each adjacent node
				this.subGraph(this.getDest(e), subGraph);
			}
		}
		return subGraph;

	}

	/**
	 * propagates the frequency of a query node towards the graph set the frequency
	 * of all provider nodes of a query
	 * 
	 * @param node
	 */
	public void propagateFrequency(EvolutionNode node) {
		List<EvolutionNode> subGraph = this.getModule(node);
		for (EvolutionNode evNode : subGraph) {
			evNode.setFrequency(node.getFrequency());
		}
		for (EvolutionEdge edge : this.getOutEdges(node)) {
			if (edge.isProvider()) {
				edge.getToNode().setFrequency(edge.getToNode().getFrequency() + node.getFrequency());
				this.propagateFrequency(this.getDest(edge));
			}
		}
	}

	/**
	 * used for finding the top_level node of each module, given an containing node
	 */
	public EvolutionNode getTopLevelNode(EvolutionNode node) {
		if (node.getType().getCategory() == NodeCategory.MODULE)
			return node;
		for (EvolutionEdge e : this.getInEdges(node)) {
			if (e.isPartOf()) {
				return getTopLevelNode(this.getSource(e));
			}
		}
		return null;
	}

	/**
	 * @return all nodes of the graph of specific type category NodeCategory
	 */
	public List<EvolutionNode> getVertices(NodeCategory category) {
		List<EvolutionNode> nodes = new ArrayList<EvolutionNode>();
		for (EvolutionNode node : this.getVertices()) {
			if (node.getType().getCategory() == category) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * @return all nodes of the graph of specific type NodeType
	 */
	public List<EvolutionNode> getVertices(NodeType type) {
		List<EvolutionNode> nodes = new ArrayList<EvolutionNode>();
		for (EvolutionNode node : this.getVertices()) {
			if (node.getType() == type) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/***
	 * @return all edges of the graph of specific type EdgeType
	 */
	public List<EvolutionEdge> getEdges(EdgeType type) {
		List<EvolutionEdge> edges = new ArrayList<EvolutionEdge>();
		for (EvolutionEdge edge : this.getEdges()) {
			if (edge.getType() == type) {
				edges.add(edge);
			}
		}
		return edges;
	}

	public static int getKeyGenerator() {
		return EvolutionGraph.KEY_GENERATOR;
	}

	public void setKeyGenerator(int value) {
		EvolutionGraph.KEY_GENERATOR = value;
	}

	/**
	 * Creates a new graph object containing the nodes of the argument. It does not
	 * create new nodes / edges; uses references. It just creates a new graph and
	 * adds them to the collection of graph's nodes and edges
	 * 
	 * @return a newly allocated EvolutionGraph object
	 */
	private EvolutionGraph  createSubGraph(List<EvolutionNode> nodes) {
		EvolutionGraph  subGraph;
		try {
			subGraph = this.getClass().newInstance();
			for (EvolutionNode vertex : nodes) {
				subGraph.addVertex(vertex);
				Collection<EvolutionEdge> incidentEdges = this.getIncidentEdges(vertex);
				for (EvolutionEdge edge : incidentEdges) {
					Pair<EvolutionNode> endpoints = this.getEndpoints(edge);
					if (nodes.containsAll(endpoints)) {
						// put this edge into the subgraph
						subGraph.addEdge(edge);
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


	/**
	 * Creates a visual graph from the given Evolution nodes. In order to create the
	 * visual subgraph, we need first to create a evolution subgraph from the given
	 * nodes. The parent of the produced visual graph is the caller of this method,
	 * not the SubGraph that gets created in the process.
	 */
	public VisualGraph produceVisualGraph(List<EvolutionNode> nodes) {
		return graphConverter.productVisualGraphWithDifferentParent(createSubGraph(nodes), this);
	}

	/**
	 * Creates a visual graph from the current evolution graph.
	 */
	public VisualGraph produceVisualGraph() {
		return graphConverter
			.productVisualGraph(this);
	}

	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObserversForEdgeAdd(EvolutionEdge addedEdge) {
		for (Observer o : observers)
			o.evolutionEdgeAdded(addedEdge);
	}

	@Override
	public void notifyObserversForVertexAdd(EvolutionNode addedNode) {
		for (Observer o : observers)
			o.evolutionVertexAdded(addedNode);
	}

	@Override
	public void notifyObserversForEdgeRemove(EvolutionEdge removedEdge) {
		for (Observer o : observers)
			o.evolutionEdgeRemoved(removedEdge);
	}

	@Override
	public void notifyObserversForVertexRemove(EvolutionNode removedNode) {
		for (Observer o : observers)
			o.evolutionVertexRemoved(removedNode);
	}

}
