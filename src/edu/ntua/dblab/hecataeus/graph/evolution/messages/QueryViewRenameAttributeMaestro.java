package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class QueryViewRenameAttributeMaestro extends MaestroAbstract {
	public QueryViewRenameAttributeMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new FCASS());
		policyCheckAlgorithms.add(new FCASSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		final List<EvolutionNode> toRen = new LinkedList<>();
		for (int i = 0; i < msg.toSchema.getOutEdges().size(); i++)
			if (msg.toSchema.getOutEdges().get(i).getToNode().getName().equals(msg.parameter))
				toRen.add(msg.toSchema.getOutEdges().get(i).getToNode());
		final List<EvolutionNode> outConnections = connectionWithOutputSearch(msg);
		if (outConnections != null)
			for (int i = 0; i < outConnections.size(); i++)
				if (outConnections.get(i).getName().equals(msg.parameter))
					toRen.add(outConnections.get(i));
		for (int i = 0; i < toRen.size(); i++) {
			toRen.get(i).setName(tempParam);
			if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
				mr.iaQ++;
			else
				mr.iaV++;
		}
		toRen.clear();
		return tempParam;
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		final List<EvolutionNode> outConnections = connectionWithOutputSearch(msg);
		if (outConnections != null)
			for (int i = 0; i < outConnections.size(); i++)
				if (outConnections.get(i).getName().equals(msg.parameter)) { // maybe connected to output but renamed AS sth
					goToOutputNode(msg);
					policyCheckAlgorithms.get(1).execute(msg, myGQueue);
					break;
				}
		setModuleStatus(msg);
	}
}
