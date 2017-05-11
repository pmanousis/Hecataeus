package edu.ntua.dblab.hecataeus.graph.util;

import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public class GraphUtilities {

	private GraphUtilities() {
	}


	/**
	 * calculates the path of nodes that are connected via in edges with a node
	 **/
	public static   void getInTree(EvolutionGraph  graph,
		EvolutionNode node,
		List<EvolutionNode> InNodes) {
		if (!(InNodes.contains(node))) {
			InNodes.add(node);
		}
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (EvolutionEdge e : graph.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(InNodes.contains(graph.getSource(e)))) {
				InNodes.add(graph.getSource(e));
				// call recursively for each adjacent node
				getInTree(graph, graph.getSource(e), InNodes);
			}
		}
	}

	/**
	 * calculates the path of nodes that are connected via out edges with a node
	 **/
	public static   void getOutTree(EvolutionGraph  graph,
		EvolutionNode node,
		List<EvolutionNode> OutNodes) {
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (EvolutionEdge e : graph.getOutEdges(node)) {
			// count only once if multiple paths found
			if (!(OutNodes.contains(graph.getDest(e)))) {
				OutNodes.add(graph.getDest(e));
				// call recursively for each adjacent node
				getOutTree(graph, graph.getDest(e), OutNodes);
			}
		}
		// add itself
		if (!(OutNodes.contains(node))) {
			OutNodes.add(node);
		}
	}

	/**
	 * calculates the path of incoming edges that are connected with a node
	 **/
	public static   void getInPath(EvolutionGraph  graph,
		EvolutionNode node,
		List<EvolutionEdge> InEdges) {
		// for each node edge add in edge to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (EvolutionEdge e : graph.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(InEdges.contains(e))) {
				InEdges.add(e);
				// call recursively for each adjacent node
				getInPath(graph, graph.getSource(e), InEdges);
			}
		}
	}

	/**
	 * calculates the path of outgoing edges that are connected with a node
	 **/
	public static   void getOutPath(EvolutionGraph  graph,
		EvolutionNode node,
		List<EvolutionEdge> OutEdges) {
		// for each node edge add out edge to collection
		// only adjacent nodes connected via a directed edge
		// FROM the affecting node are affected
		for (EvolutionEdge e : graph.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(OutEdges.contains(e))) {
				OutEdges.add(e);
				// call recursively for each adjacent node
				getOutPath(graph, graph.getDest(e), OutEdges);
			}
		}
	}

	/**
	 * returns the set of nodes affected by an event forced on the affecting_node
	 **/
	public static   List<EvolutionNode> getAffectedNodes(
		EvolutionGraph  graph,
		EvolutionNode affecting_node,
		EventType eventType) {
		List<EvolutionNode> AffectedNodes = new ArrayList<EvolutionNode>();
		getInTree(graph, affecting_node, AffectedNodes);
		return AffectedNodes;
	}

	/**
	 * returns the set of incoming edges affected by an event forced on the
	 * affecting_node
	 **/
	public static   List<EvolutionEdge> getAffectedEdges(
		EvolutionGraph  graph,
		EvolutionNode affecting_node,
		EventType eventType) {
		List<EvolutionEdge> AffectedEdges = new ArrayList<>();
		getInPath(graph, affecting_node, AffectedEdges);
		return AffectedEdges;
	}

	/**
	 * used for finding the providerEdges of a node (through from, map-select,
	 * operand edges, gb edges)
	 **/
	public static   List<EvolutionEdge> getProviderEdges(
		EvolutionGraph  graph,
		EvolutionNode node) {

		List<EvolutionEdge> providerEdges = new ArrayList<>();

		for (EvolutionEdge e : graph.getInEdges(node)) {
			if ((e.getType() == EdgeType.toEdgeType("EDGE_TYPE_MAPPING")) ||
				(e.getType() == EdgeType.toEdgeType("EDGE_TYPE_FROM")) ||
				(e.getType() == EdgeType.toEdgeType("EDGE_TYPE_GROUP_BY")) ||
				(e.getType() == EdgeType.toEdgeType("EDGE_TYPE_OPERATOR")) ||
				(e.getType() == EdgeType.toEdgeType("EDGE_TYPE_ALIAS")) ||
				(e.getType() == EdgeType.toEdgeType("EDGE_TYPE_USES"))) {
				providerEdges.add(e);
			}
		}
		return providerEdges;
	}

	/**
	 * get two set of Nodes (Modules) and return true if they are connected
	 */
	public static   boolean isConnected(EvolutionGraph  graph,
		List<EvolutionNode> fromModule,
		List<EvolutionNode> toModule) {
		for (EvolutionNode node : fromModule) {
			for (EvolutionEdge edge : graph.getOutEdges(node)) {
				if (edge.isProvider() && toModule.contains(edge.getToNode()))
					return true;
			}
		}
		return false;
	}

	/**
	 * calculates the number of connections between two modules (subGraphs) as the
	 * number of all dependency edges between these modules directing from the
	 * fromModule towards the toModule
	 */
	public static   int getConnections(
		List<EvolutionNode> fromModule,
		List<EvolutionNode> toModule) {
		List<EvolutionEdge> connections = new ArrayList<>();
		for (EvolutionNode node : fromModule) {
			for (EvolutionEdge edge : node.getOutEdges()) {
				if (edge.isProvider() && toModule.contains(edge.getToNode()) && (!connections.contains(edge))) {
					connections.add(edge);
				}
			}
		}
		return connections.size();
	}

	/**
	 * get two Nodes and return the number of paths between them
	 */
	public static   int getPaths(EvolutionGraph  graph,
		EvolutionNode fromNode,
		EvolutionNode toNode) {
		int paths = 0;
		for (EvolutionEdge edge : graph.getOutEdges(fromNode)) {
			if (edge.getToNode().equals(toNode)) {
				paths++;
			}
			paths += getPaths(graph, graph.getDest(edge), toNode);
		}
		return paths;
	}

}
