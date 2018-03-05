/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.util.Observer;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class VisualGraph extends DirectedSparseGraph<VisualNode, VisualEdge> implements Observer {
	private static final long serialVersionUID = 1L;

	/*
	 * We need these two maps in order to know if the added evolution edge is added
	 * to nodes represented by this visual graph. For example. When a EvolutionEdge
	 * is added to evolution graph, it notifies all visual graphs to add this edge.
	 * So we need to be in position to determine if this edge added to our nodes.
	 * This is the fastest way to *get* or determine *if existing* a parent node of
	 * one visual node without iterating the whole collection of node.
	 */
	private final HashMap<EvolutionNode, VisualNode> parentNodesMap;
	private final HashMap<EvolutionEdge, VisualEdge> parentEdgeMap;
	
	private final EvolutionGraph parentEvolutionGraph;
	private Map<VisualNode, Point2D> nodeLocations = new HashMap<VisualNode, Point2D>();

	public VisualGraph() {
		this.parentEvolutionGraph = null;
		this.parentNodesMap = null;
		this.parentEdgeMap = null;
	}

	public VisualGraph(	EvolutionGraph evoGraph, List<VisualNode> nodeList, List<VisualEdge> edgeList,
						HashMap<EvolutionNode, VisualNode> parentNodesMap,
						HashMap<EvolutionEdge, VisualEdge> parentEdgeMap) {
		this.parentNodesMap = parentNodesMap;
		this.parentEvolutionGraph = evoGraph;
		this.parentEdgeMap = parentEdgeMap;
		addVisualNodes(nodeList);
		addVisualEdges(edgeList);
	}

	/**
	 * returns the dimension of the graph layout The dimension of the graph is
	 * calculated as the Dimension(dx,dy), where dx = distance between the left-most
	 * and the right-most node x coordinate dy = distance between the top-most and
	 * the bottom-most node y coordinate
	 * 
	 * @return the dimension of the graph
	 */
	public Dimension getSize() {
		// return new Dimension(1200, 800);
		if (this.getVertexCount() > 0) {

			double minX = Double.MAX_VALUE;
			double minY = Double.MAX_VALUE;
			double maxX = Double.MIN_VALUE;
			double maxY = Double.MIN_VALUE;
			for (VisualNode jungNode : this.getVertices()) {
				if (jungNode.getVisible()) {
					Point2D p = this.getLocation(jungNode);
					minX = (minX > p.getX()
						? p.getX()
						: minX);
					minY = (minY > p.getY()
						? p.getY()
						: minY);
					maxX = (maxX < p.getX()
						? p.getX()
						: maxX);
					maxY = (maxY < p.getY()
						? p.getY()
						: maxY);
				}
			}

			return new Dimension((int) (maxX - minX), (int) (maxY - minY));
		}
		//else return default
		return new Dimension(1200, 800);

	}

	/**
	 * returns the center of the graph layout as a Point 2D The center of the graph
	 * is calculated as the median of x and y coordinates of all nodes
	 * 
	 * @return the center of the graph
	 */
	public Point2D getCenter() {
		if (this.getVertexCount() > 0) {

			double minX = Double.MAX_VALUE;
			double minY = Double.MAX_VALUE;
			double maxX = Double.MIN_VALUE;
			double maxY = Double.MIN_VALUE;
			for (VisualNode jungNode : this.getVertices()) {
				if (jungNode.getVisible()) {
					Point2D p = this.getLocation(jungNode);
					minX = (minX > p.getX()
						? p.getX()
						: minX);
					minY = (minY > p.getY()
						? p.getY()
						: minY);
					maxX = (maxX < p.getX()
						? p.getX()
						: maxX);
					maxY = (maxY < p.getY()
						? p.getY()
						: maxY);
				}
			}

			return new Point2D.Double((maxX + minX) / 2, (maxY + minY) / 2);
		}
		//else return default
		return new Point2D.Double();
	}
	
	public VisualNode getEquivalentVisualNode(EvolutionNode parent) {
		return parentNodesMap.get(parent);
	}

	public VisualEdge getEquivalentVisualEdge(EvolutionEdge parent) {
		return parentEdgeMap.get(parent);
	}

	public EvolutionGraph getParentGraph() {
		return parentEvolutionGraph;
	}

	public List<VisualEdge> getEdges(EdgeType type) {
		List<VisualEdge> edges = new ArrayList<>();
		for (VisualEdge edge : this.getEdges()) {
			if (edge.getType() == type) {
				edges.add(edge);
			}
		}
		return edges;
	}

	public List<VisualNode> getModule(VisualNode parentNode) {
		List<VisualNode> subGraph = new ArrayList<>();
		subGraph.add(parentNode);
		return this.subGraph(parentNode, subGraph);
	}

	public List<VisualNode> getVertices(NodeType type) {
		List<VisualNode> nodes = new ArrayList<>();
		for (VisualNode node : this.getVertices()) {
			if (node.getType() == type) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public List<VisualNode> getVertices(NodeCategory category) {
		List<VisualNode> nodes = new ArrayList<>();
		for (VisualNode node : this.getVertices()) {
			if (node.getType().getCategory() == category) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public VisualNode findVertexByName(String name, NodeType nt) {
		for (VisualNode u : this.getVertices()) {
			if (u.getName().toUpperCase().equals(name.toUpperCase()) && u.getType() == nt) {
				return u;
			}
		}
		return null;
	}

	public VisualNode findVertexByNameParent(String name) {
		String parent = "";
		String node = "";
		if (name.contains(".")) {
			parent = name.substring(0, name.indexOf("."));
			node = name.substring(name.indexOf(".") + 1);
			for (VisualNode u : this.getVertices()) {
				if (u.getName().toUpperCase().equals(parent.toUpperCase())) {
					for (int i = 0; i < u.getOutEdges().size(); i++) {
						if (u.getOutEdges().get(i).getToNode().getName().equals(node.toUpperCase())) {
							return u.getOutEdges().get(i).getToNode();
						}
					}
				}
			}
		} else {
			return (findVertexByName(name));
		}
		return null;
	}

	public VisualNode findVertexByName(String name) {
		for (VisualNode u : this.getVertices()) {
			if (u.getName().toUpperCase().equals(name.toUpperCase())) {
				return u;
			}
		}
		return null;
	}

	public void setLocation(VisualNode v, Point2D location) {
		this.nodeLocations.put(v, location);
	}

	public Point2D getLocation(VisualNode v) {
		if (this.nodeLocations.get(v) != null)
			return this.nodeLocations.get(v);
		return (new Point2D.Double());
	}

	@Override
	public boolean evolutionEdgeAdded(EvolutionEdge addedEdge) {
		VisualNode fromNode = getEquivalentVisualNode(addedEdge.getFromNode());
		VisualNode toNode = getEquivalentVisualNode(addedEdge.getToNode());
		if (fromNode == null || toNode == null) //Some VisualGraphs may not have the given Edge
			return false;

		parentEdgeMap.put(addedEdge, new VisualEdge(addedEdge));
		VisualEdge edge = getEquivalentVisualEdge(addedEdge);
		
		boolean added = super.addEdge(edge, fromNode, toNode);
		if(added){
			edge.setVisualGraphInWhichContained(this);
		}
		return added;
	}

	@Override
	public boolean evolutionVertexAdded(EvolutionNode addedNode) {
		parentNodesMap.put(addedNode, new VisualNode(addedNode));
		return super.addVertex(getEquivalentVisualNode(addedNode));
	}

	@Override
	public boolean evolutionEdgeRemoved(EvolutionEdge removedEdge) {
		VisualEdge edge = getEquivalentVisualEdge(removedEdge);
		if (edge == null) //Some VisualGraphs may not have the given Edge
			return false;
		if (edge.getToNode() == null || edge.getFromNode() == null) {
			return false;
		}
		edge.getToNode().getInEdges().remove(edge);
		edge.getFromNode().getOutEdges().remove(edge);

		parentNodesMap.remove(removedEdge);
		return super.removeEdge(edge);
	}

	@Override
	public boolean evolutionVertexRemoved(EvolutionNode removedNode) {
		VisualNode node = getEquivalentVisualNode(removedNode);
		if (node == null) //Some VisualGraphs may not have the given Node
			return false;
		node.getInEdges().forEach(edge -> removeEdge(edge));
		node.getOutEdges().forEach(edge -> removeEdge(edge));
		return super.removeVertex(node);
	}

	public boolean addEdge(VisualEdge edge, Collection<? extends VisualNode> nodes) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}

	public boolean addEdge(VisualEdge edge, Pair<? extends VisualNode> endpoints) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}

	public boolean addEdge(VisualEdge edge, Collection<? extends VisualNode> nodes, EdgeType type) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}

	public boolean addEdge(VisualEdge edge, Pair<? extends VisualNode> endpoints, EdgeType type) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}

	public boolean addEdge(VisualEdge edge, VisualNode node1, VisualNode node2) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}

	public boolean addEdge(VisualEdge edge, VisualNode node1, VisualNode node2, EdgeType type) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}
	
	public boolean addVertex(VisualNode node) {
		throw new Error("Only EvolutionGraph can add nodes. Visual is just for its representation.");
	}

	public List<VisualEdge> getInEdges(VisualNode vertex) {
		return new ArrayList<VisualEdge>(super.getInEdges(vertex));
	}

	public List<VisualEdge> getOutEdges(VisualNode vertex) {
		return new ArrayList<VisualEdge>(super.getOutEdges(vertex));
	}

	public List<VisualNode> getVertices() {
		return new ArrayList<VisualNode>(super.getVertices());
	}

	public List<VisualEdge> getEdges() {
		return new ArrayList<VisualEdge>(super.getEdges());
	}

	private void addVisualNodes(List<VisualNode> nodeList) {
		nodeList.forEach(node -> super.addVertex(node));
	}

	private void addVisualEdges(List<VisualEdge> edgeList) {
		edgeList.forEach(edge -> {
			edge.setVisualGraphInWhichContained(this);
			VisualNode fromNode = getEquivalentVisualNode(edge.getParentEvolutionEdge().getFromNode());
			VisualNode toNode = getEquivalentVisualNode(edge.getParentEvolutionEdge().getToNode());
			super.addEdge(edge,fromNode,toNode);
			});
	}

	private List<VisualNode> subGraph(VisualNode node, List<VisualNode> subGraph) {

		for (VisualEdge e : this.getOutEdges(node)) {
			if (e.isPartOf()) {
				if (!(subGraph.contains(this.getDest(e)))) {
					subGraph.add(this.getDest(e));
				}
				this.subGraph(this.getDest(e), subGraph);
			}
		}
		return subGraph;

	}

}
