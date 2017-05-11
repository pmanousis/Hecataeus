package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class QueryViewRenameProviderMaestro extends MaestroAbstract {
	public QueryViewRenameProviderMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new ASSS());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		msg.toSchema.setName(msg.toSchema.getName().replace(msg.parameter.replace("_OUT", ""), tempParam));
		if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
			mr.iaQ++;
		else
			mr.iaV++;
		return tempParam;
	}

	@Override
	public void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
