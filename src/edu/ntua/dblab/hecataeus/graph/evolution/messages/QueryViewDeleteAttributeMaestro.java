package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public class QueryViewDeleteAttributeMaestro extends MaestroAbstract {
	public QueryViewDeleteAttributeMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new FCASS());
		policyCheckAlgorithms.add(new ASSSNO());
		policyCheckAlgorithms.add(new FCASSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		final List<EvolutionNode> prosDiagrafi = new LinkedList<>();
		for (int i = 0; i < msg.toSchema.getOutEdges().size(); i++)
			if (msg.toSchema.getOutEdges().get(i).getToNode().getName().equals(msg.parameter))
				prosDiagrafi.add(msg.toSchema.getOutEdges().get(i).getToNode());
		final List<EvolutionNode> smtxConnections = connectionWithSmtxSearch(msg);
		if (smtxConnections != null)
			for (int i = 0; i < smtxConnections.size(); i++) {
				if (smtxConnections.get(i).getType() == NodeType.NODE_TYPE_GROUP_BY)
					continue;
				for (int j = 0; j < smtxConnections.get(i).getOutEdges().size(); j++)
					if (smtxConnections
						.get(i).getOutEdges().get(j).getToNode().getType() != NodeType.NODE_TYPE_ATTRIBUTE)
						treeCleaner(smtxConnections.get(i).getOutEdges().get(j).getToNode(), graph);
				while (smtxConnections.get(i).getOutEdges().size() > 0)
					graph.removeEdge(smtxConnections.get(i).getOutEdges().get(0));
				smtxConnections.get(i).setName(" = ");
				final EvolutionNode nvnlo = new EvolutionNode();
				if (smtxConnections.get(i).getInEdges().get(0).getFromNode().getName().equals(" AND ") ||
					smtxConnections.get(i).getInEdges().get(0).getFromNode().getName().endsWith("_SMTX"))
					nvnlo.setName("1");
				else
					nvnlo.setName("2");
				nvnlo.setType(NodeType.NODE_TYPE_CONSTANT);
				graph.addVertex(nvnlo);
				final EvolutionNode nvnro = new EvolutionNode();
				nvnro.setName("1");
				nvnro.setType(NodeType.NODE_TYPE_CONSTANT);
				graph.addVertex(nvnro);
				final EvolutionEdge eielo = new EvolutionEdge();
				eielo.setName("op1");
				eielo.setType(EdgeType.EDGE_TYPE_OPERATOR);
				eielo.setFromNode(smtxConnections.get(i));
				eielo.setToNode(nvnlo);
				graph.addEdge(eielo);
				final EvolutionEdge eiero = new EvolutionEdge();
				eiero.setName("op2");
				eiero.setType(EdgeType.EDGE_TYPE_OPERATOR);
				eiero.setFromNode(smtxConnections.get(i));
				eiero.setToNode(nvnro);
				graph.addEdge(eiero);
			}
		final List<EvolutionNode> outConnections = connectionWithOutputSearch(msg);
		if (outConnections != null)
			for (int i = 0; i < outConnections.size(); i++) {
				prosDiagrafi.add(outConnections.get(i));
				if (prosDiagrafi.contains(outConnections.get(i).getOutEdges().get(0).getToNode()) == false)
					prosDiagrafi.add(outConnections.get(i).getOutEdges().get(0).getToNode());
			}
		for (int i = 0; i < prosDiagrafi.size(); i++) {
			graph.removeVertex(prosDiagrafi.get(i));
			if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
				mr.iaQ++;
			else
				mr.iaV++;
		}
		prosDiagrafi.clear();
		prosDiagrafi.addAll(cleanUpUnneededAttrs(msg.toNode));
		for (int i = 0; i < prosDiagrafi.size(); i++) {
			graph.removeVertex(prosDiagrafi.get(i));
			if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
				mr.iaQ++;
			else
				mr.iaV++;
		}
		prosDiagrafi.clear();
		prosDiagrafi.addAll(cleanUpUnneededIns(msg.toNode));
		goToSmtxNode(msg);
		for (int i = 0; i < msg.toSchema.getOutEdges().size(); i++)
			if (msg.toSchema.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_GROUP_BY)
				if (msg.toSchema.getOutEdges().get(i).getToNode().getOutEdges().size() == 0)
					prosDiagrafi.add(msg.toSchema.getOutEdges().get(i).getToNode());
		for (int i = 0; i < prosDiagrafi.size(); i++) {
			graph.removeVertex(prosDiagrafi.get(i));
			if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
				mr.iaQ++;
			else
				mr.iaV++;
		}
		return "";
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		final EventType tempEvent = msg.event; // Needed for moving back to event since in semantics we may have ALTER_SEMANTICS.
		final EvolutionNode schema = msg.toSchema;
		final String tmppar = msg.parameter;
		final List<EvolutionNode> smtxConnections = connectionWithSmtxSearch(msg);
		if (smtxConnections != null) {
			msg.event = EventType.ALTER_SEMANTICS;
			goToSmtxNode(msg);
			policyCheckAlgorithms.get(1).execute(msg, myGQueue);
			final StatusType smtxStatus = msg.toSchema.getStatus();
			final EvolutionNode smtxNode = msg.toSchema;
			for (int i = 0; i < smtxConnections.size(); i++)
				colorizeSmtx(smtxConnections.get(i), smtxStatus, smtxNode);
			msg.toSchema.getInEdges().get(0).setStatus(smtxStatus, false);
			msg.event = tempEvent;
			msg.parameter = tmppar;
			msg.toSchema = schema;
		}
		final List<EvolutionNode> outConnections = connectionWithOutputSearch(msg);
		if (outConnections != null) {
			goToOutputNode(msg);
			for (int i = 0; i < outConnections.size(); i++) {
				msg.parameter = outConnections.get(i).getName();
				policyCheckAlgorithms.get(2).execute(msg, myGQueue);
			}
		}
		setModuleStatus(msg);
	}
}
