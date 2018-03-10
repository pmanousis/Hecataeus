package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class ModuleMaestroRewrite extends ModuleMaestro {
	public ModuleMaestroRewrite(final PriorityQueue<Message> globalQueue) {
		super(globalQueue);
	}

	public String doRewrite(String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		while (myQueue.peek() != null) {
			final Message currentMessage = myQueue.poll();
			final MaestroAbstract maestro = MaestroFactory.create(currentMessage, forGlobal);
			tempParam = maestro.doRewrite(currentMessage, tempParam, graph, stw, mr);
		}
		return tempParam;
	}

	public void moveToNewInputsIfExist(final EvolutionGraph graph, final EvolutionNode node) {
		for (final EvolutionEdge e : node.getOutEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_INPUT) {
				final EvolutionNode inNode = e.getToNode();
				final EvolutionNode pprov = findProvider(inNode);
				final EvolutionNode newpprov = graph.findVertexById(pprov.getID() + 0.4);
				if (newpprov != null) { // All children of inNode need to move to their edges to newpprov.
					final EvolutionNode newprovOut = findProvidersOutput(newpprov);
					moveUsesEdgeToNewProvider(node, newpprov, pprov, graph);
					moveInEdgesToNewProviderOuts(inNode, newpprov, newprovOut);
				}
			}
	}

	private EvolutionNode findProvider(final EvolutionNode inNode) {
		EvolutionNode pprov = null;
		for (int i = 0; i < inNode.getOutEdges().size(); i++)
			if (inNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_FROM) {
				final EvolutionNode prov = inNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < prov.getInEdges().size(); j++)
					if (prov.getInEdges().get(j).getType() == EdgeType.EDGE_TYPE_OUTPUT)
						pprov = prov.getInEdges().get(j).getFromNode();
			}
		return pprov;
	}

	private EvolutionNode findProvidersOutput(final EvolutionNode newProvider) {
		EvolutionNode providersOutput = null;
		for (int i = 0; i < newProvider.getOutEdges().size(); i++)
			if (newProvider.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT)
				providersOutput = newProvider.getOutEdges().get(i).getToNode();
		return providersOutput;
	}

	private void moveInEdgesToNewProviderOuts(final EvolutionNode inNode,
		final EvolutionNode newpprov,
		final EvolutionNode newprovOut) {
		for (int i = 0; i < inNode.getOutEdges().size(); i++) {
			if (inNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT) {
				final EvolutionNode attr = inNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < attr.getOutEdges().size(); j++)
					if (attr.getOutEdges().get(j).getType() == EdgeType.EDGE_TYPE_MAPPING)
						for (int k = 0; k < newprovOut.getOutEdges().size(); k++)
							if (newprovOut.getOutEdges().get(k).getToNode().getName().equals(
								attr.getOutEdges().get(j).getToNode().getName()))
								attr.getOutEdges().get(j).setToNode(newprovOut.getOutEdges().get(k).getToNode());
			}
			if (inNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_FROM)
				inNode.getOutEdges().get(i).setToNode(newprovOut);
		}
		inNode.setName(inNode.getName().substring(0, inNode.getName().lastIndexOf("_") + 1) + newpprov.getName());
	}

	private void moveUsesEdgeToNewProvider(final EvolutionNode node,
		final EvolutionNode newpprov,
		final EvolutionNode pprov,
		final EvolutionGraph graph) {
		for (final EvolutionEdge e : node.getOutEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_USES && e.getToNode() == graph.findVertexById(pprov.getID()))
				if (e.getToNode() == pprov)
					e.setToNode(newpprov);
	}
}
