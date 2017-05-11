package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationDeleteAttributeMaestro extends MaestroAbstract {
	public RelationDeleteAttributeMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new FCASSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		final List<EvolutionEdge> lista = msg.toSchema.getOutEdges();
		for (int i = 0; i < lista.size(); i++)
			if (lista.get(i).getToNode().getName().equals(msg.parameter)) {
				while (lista.get(i).getToNode().getOutEdges().size() > 0)
					graph.removeVertex(lista.get(i).getToNode().getOutEdges().get(0).getToNode());
				graph.removeVertex(lista.get(i).getToNode());
				mr.iaR++;
				return msg.parameter;
			}
		return "";
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
