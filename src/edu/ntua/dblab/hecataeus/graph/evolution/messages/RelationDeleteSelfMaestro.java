package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationDeleteSelfMaestro extends MaestroAbstract {
	public RelationDeleteSelfMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new AACSSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		while (msg.toNode.getOutEdges().size() > 0) {
			while (msg.toNode.getOutEdges().get(0).getToNode().getOutEdges().size() > 0) {
				while (msg.toNode
					.getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode().getOutEdges().size() > 0)
					graph.removeVertex(msg.toNode
						.getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode().getOutEdges().get(0)
						.getToNode());
				graph.removeVertex(msg.toNode.getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode());
			}
			graph.removeVertex(msg.toNode.getOutEdges().get(0).getToNode());
		}
		graph.removeVertex(msg.toNode);
		return "";
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
