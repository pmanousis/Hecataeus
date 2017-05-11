
package edu.ntua.dblab.hecataeus.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

/**
 * This class is responsible for producing a {@link VisualGraph} from a given
 * {@link EvolutionGraph}. This process needs to be done, because when an
 * <code>EvolutionNode or an <code>EvolutionEdge</code> producing their
 * corresponding Visual "self", can not reproduce some of their field.
 * <p>
 * For example: When an <code>EvolutionEdge</code> producing its VisualEdge can not
 * assign to it a VisualNode fromNode or VisualNode toNode, because it can not
 * produce visual nodes. That is <code>EvolutionNode's job. So create
 * everything inside here, and then assign proper values to their fields. And finally
 * we produce the final VisualGraph.
 */
public class EvolutionToVisualConverter {

	private HashMap<EvolutionNode, VisualNode> nodeMap = new HashMap<>();
	private HashMap<EvolutionEdge, VisualEdge> edgeMap = new HashMap<>();
	private EvolutionGraph evolutionGraph = null;

	public EvolutionToVisualConverter() {

	}

	public VisualGraph productVisualGraph(EvolutionGraph evoGraph) {
		return productVisualGraphWithDifferentParent(evoGraph, evoGraph);
	}

	public VisualGraph productVisualGraphWithDifferentParent(
		EvolutionGraph evoGraph,
		EvolutionGraph parent) {

		evolutionGraph = evoGraph;
		List<VisualNode> visualVertices = produceVisualNodes();
		List<VisualEdge> visualEdges = pruduceVisualEdges();
		initVisualGraphComponents();


		VisualGraph vGraph =
			new VisualGraph(parent, 
				visualVertices, 
				visualEdges, 
				new HashMap<EvolutionNode, VisualNode>(nodeMap),
				new HashMap<EvolutionEdge, VisualEdge>(edgeMap));

		nodeMap = new HashMap<>();
		edgeMap = new HashMap<>();

		parent.addObserver(vGraph);
		return vGraph;
	}

	private List<VisualNode> produceVisualNodes() {
		Collection<EvolutionNode> evolutionVertices = evolutionGraph.getVertices();
		List<VisualNode> visualVertices = new ArrayList<>(evolutionVertices.size());

		for (EvolutionNode evoVertice : evolutionVertices) {
			final VisualNode producedVisualNode = evoVertice.produceVisualNode();
			visualVertices.add(producedVisualNode);
			nodeMap.put(evoVertice, producedVisualNode);
		}

		return visualVertices;
	}

	private List<VisualEdge> pruduceVisualEdges() {

		Collection<EvolutionEdge> evolutionEdges = evolutionGraph.getEdges();
		List<VisualEdge> visualEdges = new ArrayList<>(evolutionEdges.size());

		for (EvolutionEdge evoEdge : evolutionEdges) {
			final VisualEdge producedVisualEdge = evoEdge.produceVisualEdge();
			visualEdges.add(producedVisualEdge);
			edgeMap.put(evoEdge, producedVisualEdge);
		}

		return visualEdges;
	}

	private void initVisualGraphComponents() {
		initVisualEdgeFields();
		initVisualNodeFields();
	}

	private void initVisualNodeFields() {
		for (EvolutionNode evoNode : nodeMap.keySet()) {
			final Collection<EvolutionEdge> evolutionEdges = evolutionGraph.getEdges();
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

	private void initVisualEdgeFields() {
		for (EvolutionEdge evoEdge : edgeMap.keySet()) {
			final EvolutionNode fromNode = (EvolutionNode) evoEdge.getFromNode();
			final EvolutionNode toNode = (EvolutionNode) evoEdge.getToNode();
			final VisualEdge visualEdge = edgeMap.get(evoEdge);

			visualEdge.setFromNode(nodeMap.get(fromNode));
			visualEdge.setToNode(nodeMap.get(toNode));
		}
	}

}
