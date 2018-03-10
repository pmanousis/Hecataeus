package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewDeleteSelfMaestro extends MaestroAbstract {
	public QueryViewDeleteSelfMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new AACSSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
				for (int j = 0; j < msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().size(); j++)
					if (msg.toNode
						.getOutEdges().get(i).getToNode().getOutEdges().get(j).getType() == EdgeType.EDGE_TYPE_WHERE)
						treeCleaner(msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode(),
							graph);
					else if (msg.toNode
						.getOutEdges().get(i).getToNode().getOutEdges().get(j).getType() == EdgeType.EDGE_TYPE_GROUP_BY)
						graph
							.removeVertex(msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode());

		final List<EvolutionEdge> mschemata = new LinkedList<>();
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() != EdgeType.EDGE_TYPE_USES)
				mschemata.add(msg.toNode.getOutEdges().get(i));
		for (int i = 0; i < mschemata.size(); i++) { //clean in and out
			final List<EvolutionEdge> mattrs = new LinkedList<>();
			for (int j = 0; j < mschemata.get(i).getToNode().getOutEdges().size(); j++)
				if (mschemata.get(i).getToNode().getOutEdges().get(j).getType() != EdgeType.EDGE_TYPE_FROM)
					mattrs.add(mschemata.get(i).getToNode().getOutEdges().get(j));
			for (int j = 0; j < mattrs.size(); j++)
				graph.removeVertex(mattrs.get(j).getToNode());
			graph.removeVertex(mschemata.get(i).getToNode());
		}
		mschemata.clear();
		graph.removeVertex(msg.toNode);
		return "";
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT)
				msg.toNode.getOutEdges().get(i).setStatus(msg.toNode.getOutEdges().get(i).getToNode().getStatus(),
					false);
		setModuleStatus(msg);
	}
}
