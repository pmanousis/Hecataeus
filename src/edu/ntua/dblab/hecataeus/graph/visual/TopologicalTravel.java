package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.gui.HecataeusViewer;

/**
 * @author pmanousi It does a topological sorting in IDs of high level nodes
 *         (Queries, Views, Relations).
 */
public class TopologicalTravel {
	HecataeusViewer v;
	VisualGraph graph;
	List<VisualEdge> removedEdges;
	List<VisualNode> removedNodes;

	public TopologicalTravel(final HecataeusViewer viewer) {
		removedEdges = new ArrayList<>();
		removedNodes = new ArrayList<>();
		v = viewer;
	}

	public TopologicalTravel(final VisualGraph g) {
		removedEdges = new ArrayList<>();
		removedNodes = new ArrayList<>();
		graph = g;
	}

	/**
	 * First are the nodes with incoming edges = 0 (startNode). They are removed with
	 * their outgoing edges (EDGE_TYPE_USES). Others (parentNode) that had only 1
	 * incoming edge become startNode. Continue till you have no more nodes to check.
	 */
	public void travel() {
		for (int i = 0; i < graph.getVertices().size(); i++)
			graph.getVertices().get(i).setID(0);
		final List<VisualNode> highLevelNodes = graph.getVertices(NodeType.NODE_TYPE_QUERY);
		highLevelNodes.addAll(graph.getVertices(NodeType.NODE_TYPE_RELATION));
		highLevelNodes.addAll(graph.getVertices(NodeType.NODE_TYPE_VIEW));
		int highLevelNodeCounter = highLevelNodes.size();
		while (highLevelNodes.size() > 0) {
			final List<VisualEdge> edgesToRemove = new ArrayList<>();
			final List<VisualNode> nodesToRemove = new ArrayList<>();
			for (final VisualNode n : highLevelNodes)
				if (numberOfIncomingUsesEdges(n, highLevelNodes) == 0 && removedNodes.contains(n) == false) { // Found start of graph
					graph.findVertexByName(n.getName()).setID(highLevelNodeCounter);
					highLevelNodeCounter--;
					edgesToRemove.addAll(removeIncomingUsesEdgesAtConnectedNodes(n, highLevelNodes));
					if (removedNodes.contains(n) == false)
						nodesToRemove.add(n);
				}
			for (final VisualEdge e : edgesToRemove)
				removedEdges.add(e);
			for (final VisualNode n : nodesToRemove) {
				removedNodes.add(n);
				highLevelNodes.remove(n);
			}
		}
	}

	/**
	 * In order to place the views in the space between the relations and the single
	 * relation queries, we need to know how many levels of views over views etc we
	 * have. We have in the same level all the nodes that depend to relations. Next
	 * we have those that depend to a view (and maybe relations)... Then those that
	 * depend to a view over view (and of course the previous stratum)...
	 * 
	 * @author pmanousi
	 * @return A tree map with the stratified levels of views.
	 */
	public TreeMap<Integer, List<VisualNode>> viewStratificationLevels() {
		final TreeMap<Integer, List<VisualNode>> toReturn = new TreeMap<>();
		final List<VisualNode> highLevelNodes = graph.getVertices(NodeType.NODE_TYPE_VIEW);
		int strataCounter = 0;
		while (highLevelNodes.size() > 0) {
			final List<VisualEdge> edgesToRemove = new ArrayList<>();
			final List<VisualNode> nodesToRemove = new ArrayList<>();
			for (final VisualNode n : highLevelNodes)
				if (numberOfOutgoingUsesEdges(n, highLevelNodes) == 0 && removedNodes.contains(n) == false) {
					if (toReturn.get(strataCounter) == null)
						toReturn.put(strataCounter, new ArrayList<VisualNode>());
					toReturn.get(strataCounter).add(n);
					edgesToRemove.addAll(removeOutgoingUsesEdgesAtConnectedNodes(n, highLevelNodes));
					if (removedNodes.contains(n) == false)
						nodesToRemove.add(n);
				}
			for (final VisualEdge e : edgesToRemove)
				removedEdges.add(e);
			for (final VisualNode n : nodesToRemove) {
				removedNodes.add(n);
				highLevelNodes.remove(n);
			}
			strataCounter++;
		}
		return toReturn;
	}

	private int numberOfIncomingUsesEdges(final VisualNode node, final List<VisualNode> hln) {
		int toReturn = 0;
		for (final VisualEdge e : node.getInEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) &&
				removedEdges.contains(e) == false)
				toReturn++;
		return toReturn;
	}

	private int numberOfOutgoingUsesEdges(final VisualNode node, final List<VisualNode> hln) {
		int toReturn = 0;
		for (final VisualEdge e : node.getOutEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) &&
				removedEdges.contains(e) == false)
				toReturn++;
		return toReturn;
	}

	private List<VisualEdge> removeIncomingUsesEdgesAtConnectedNodes(final VisualNode node,
		final List<VisualNode> hln) {
		final List<VisualNode> connectedNodes = new ArrayList<>();
		for (final VisualEdge e : node.getOutEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) &&
				removedEdges.contains(e) == false)
				connectedNodes.add(e.getToNode());
		final List<VisualEdge> edgesToRemove = new ArrayList<>();
		for (final VisualNode n : connectedNodes)
			for (final VisualEdge e : n.getInEdges())
				if (e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) && e.getFromNode() == node &&
					removedEdges.contains(e) == false)
					edgesToRemove.add(e);
		return edgesToRemove;
	}

	private List<VisualEdge> removeOutgoingUsesEdgesAtConnectedNodes(final VisualNode node,
		final List<VisualNode> hln) {
		final List<VisualNode> connectedNodes = new ArrayList<>();
		for (final VisualEdge e : node.getInEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getFromNode()) &&
				removedEdges.contains(e) == false)
				connectedNodes.add(e.getFromNode());
		final List<VisualEdge> edgesToRemove = new ArrayList<>();
		for (final VisualNode n : connectedNodes)
			for (final VisualEdge e : n.getOutEdges())
				if (e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getFromNode()) && e.getToNode() == node &&
					removedEdges.contains(e) == false)
					edgesToRemove.add(e);
		return edgesToRemove;
	}
}
