
package edu.ntua.dblab.hecataeus.graph.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.graph.util.Pair;

public class EvolutionToVisualConverter {

	private HashMap<EvolutionNode, VisualNode> nodeMap = null;
	private HashMap<EvolutionEdge, VisualEdge> edgeMap = null;
	private ArrayList<EvolutionNode> evolutionNodes = null;
	private HashSet<EvolutionEdge> evolutionEdges = null;

	public EvolutionToVisualConverter() {

	}
	
	public VisualGraph productVisualGraph(Collection<EvolutionNode> evoNodes, EvolutionGraph parent) {
		evolutionNodes = new ArrayList<>();
		evolutionEdges = new HashSet<>();
		nodeMap = new HashMap<>();
		edgeMap = new HashMap<>();
		
		evolutionNodes.addAll(evoNodes);
		addAllEdges(evoNodes,parent);
		
		List<VisualNode> visualVertices = produceVisualNodes();
		List<VisualEdge> visualEdges = pruduceVisualEdges();
		initVisualGraphComponents();

		VisualGraph vGraph =
			new VisualGraph(parent, 
				visualVertices, 
				visualEdges, 
				new HashMap<EvolutionNode, VisualNode>(nodeMap),
				new HashMap<EvolutionEdge, VisualEdge>(edgeMap));

		parent.addObserver(vGraph);
		return vGraph;
	}
	
	private void addAllEdges(Collection<EvolutionNode> evoNodes, EvolutionGraph graph){
		for (EvolutionNode vertex : evoNodes) {
			Collection<EvolutionEdge> incidentEdges = graph.getIncidentEdges(vertex);
			for (EvolutionEdge edge : incidentEdges) {
				Pair<EvolutionNode> endpoints = graph.getEndpoints(edge);
				if (evolutionNodes.containsAll(endpoints)) {
					evolutionEdges.add(edge);
				} 
			}
		}
	}

	private List<VisualNode> produceVisualNodes() {
		List<VisualNode> visualVertices = new ArrayList<>(evolutionNodes.size());

		for (EvolutionNode evoVertice : evolutionNodes) {
			final VisualNode producedVisualNode = produceVisualNode(evoVertice);
			visualVertices.add(producedVisualNode);
			nodeMap.put(evoVertice, producedVisualNode);
		}

		return visualVertices;
	}

	private List<VisualEdge> pruduceVisualEdges() {
		List<VisualEdge> visualEdges = new ArrayList<>(evolutionEdges.size());

		for (EvolutionEdge evoEdge : evolutionEdges) {
			final VisualEdge producedVisualEdge = produceVisualEdge(evoEdge);
			visualEdges.add(producedVisualEdge);
			edgeMap.put(evoEdge, producedVisualEdge);
		}

		return visualEdges;
	}

	private void initVisualGraphComponents() {
		//initVisualEdgeFields();
		initVisualNodeFields();
	}

	private void initVisualNodeFields() {
		for (EvolutionNode evoNode : nodeMap.keySet()) {
			final List<EvolutionEdge> outEdges = evoNode.getOutEdges();
			final List<EvolutionEdge> inEdges = evoNode.getInEdges();
			final List<VisualEdge> visualOutEdges = new ArrayList<>(outEdges.size());
			final List<VisualEdge> visualInEdges = new ArrayList<>(inEdges.size());
			final VisualNode visualNode = nodeMap.get(evoNode);

			for (EvolutionEdge evoEdge : outEdges) {
				if (!evolutionEdges.contains(evoEdge))
					continue;
				visualOutEdges.add(edgeMap.get(evoEdge));
			}
			visualNode.setOutEdges(visualOutEdges);
			
			for (EvolutionEdge inEdge : inEdges) {
				if (!evolutionEdges.contains(inEdge))
					continue;
				visualInEdges.add(edgeMap.get(inEdge));
			}
			visualNode.setInEdges(visualInEdges);
		}
	}
	
	public VisualEdge produceVisualEdge(EvolutionEdge evoEdge) {
		return new VisualEdge(evoEdge);
	}
	
	public VisualNode produceVisualNode(EvolutionNode evoNode) {
		return new VisualNode(evoNode);
	}

/*	private void initVisualEdgeFields() {
		for (EvolutionEdge evoEdge : edgeMap.keySet()) {
			final EvolutionNode fromNode = (EvolutionNode) evoEdge.getFromNode();
			final EvolutionNode toNode = (EvolutionNode) evoEdge.getToNode();
			final VisualEdge visualEdge = edgeMap.get(evoEdge);

			visualEdge.setFromNode(nodeMap.get(fromNode));
			visualEdge.setToNode(nodeMap.get(toNode));
		}
	}*/

}
