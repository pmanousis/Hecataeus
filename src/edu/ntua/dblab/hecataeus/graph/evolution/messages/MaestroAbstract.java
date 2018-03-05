package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public abstract class MaestroAbstract {
	List<Algorithm> policyCheckAlgorithms;
	PriorityQueue<Message> myGQueue;

	public MaestroAbstract(final PriorityQueue<Message> q) {
		policyCheckAlgorithms = new LinkedList<>();
		myGQueue = q;
	}

	abstract String doRewrite(Message currentMessage,
		String tempParam,
		EvolutionGraph graph,
		StopWatch stw,
		MetriseisRewrite mr);

	abstract void propagateMessage(Message msg);

	void treeCleaner(final EvolutionNode kombos, final EvolutionGraph graph) {
		EvolutionNode left = null;
		EvolutionNode right = null;
		if (kombos == null)
			return;
		if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE || kombos.getType() == NodeType.NODE_TYPE_CONSTANT) {
			final List<EvolutionEdge> prosDiagrafi = new LinkedList<>();
			for (int i = 0; i < kombos.getInEdges().size(); i++)
				if (kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_OPERATOR)
					prosDiagrafi.add(kombos.getInEdges().get(i));
			for (int i = 0; i < prosDiagrafi.size(); i++)
				graph.removeEdge(prosDiagrafi.get(i));
			if (kombos.getType() == NodeType.NODE_TYPE_CONSTANT)
				graph.removeVertex(kombos);
			return;
		}
		for (int i = 0; i < kombos.getOutEdges().size(); i++) {
			if (kombos.getOutEdges().get(i).getName().equals("op1"))
				left = kombos.getOutEdges().get(i).getToNode();
			if (kombos.getOutEdges().get(i).getName().equals("op2"))
				right = kombos.getOutEdges().get(i).getToNode();
		}
		treeCleaner(left, graph);
		treeCleaner(right, graph);
		graph.removeVertex(kombos);
	}

	protected List<EvolutionNode> cleanUpUnneededAttrs(final EvolutionNode kombos) {
		final List<EvolutionNode> toRet = new LinkedList<>();
		for (int i = 0; i < kombos.getOutEdges().size(); i++)
			if (kombos.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT) {
				final EvolutionNode inSchema = kombos.getOutEdges().get(i).getToNode();
				{
					for (int j = 0; j < inSchema.getOutEdges().size(); j++)
						if (inSchema.getOutEdges().get(j).getToNode().getInEdges().size() == 1)
							toRet.add(inSchema.getOutEdges().get(j).getToNode());
				}
			}
		return toRet;
	}

	protected List<EvolutionNode> cleanUpUnneededIns(final EvolutionNode kombos) {
		final List<EvolutionNode> toRet = new LinkedList<>();
		for (int i = 0; i < kombos.getOutEdges().size(); i++)
			if (kombos.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT) {
				final EvolutionNode inSchema = kombos.getOutEdges().get(i).getToNode();
				if (inSchema.getOutEdges().size() == 1)
					toRet.add(inSchema);
			}
		return toRet;
	}

	protected void colorizeSmtx(final EvolutionNode start, final StatusType smtxStatus, final EvolutionNode smtxNode) {
		start.setStatus(smtxStatus, false);
		for (int i = 0; i < start.getInEdges().size(); i++) {
			if (start.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_OPERATOR) {
				start.getInEdges().get(i).setStatus(smtxStatus, false);
				colorizeSmtx(start.getInEdges().get(i).getFromNode(), smtxStatus, smtxNode);
			}
			if (start.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_WHERE)
				start.getInEdges().get(i).setStatus(smtxStatus, false);
		}
	}

	protected List<EvolutionNode> connectionWithOutputSearch(final Message message) { // Searching in IN node for MAP-SELECT edges (they come from OUT node), if there is a connection, I move message.toSchema to OUT node.
		final List<EvolutionNode> toRet = new LinkedList<>();
		for (int j = 0; j < message.toSchema.getOutEdges().size(); j++) { // For nodes of input schema.
			final EvolutionNode prosElegxo = message.toSchema.getOutEdges().get(j).getToNode();
			if (prosElegxo.getName().equals(message.parameter))
				for (int k = 0; k < prosElegxo.getInEdges().size(); k++)
					if (prosElegxo.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_MAPPING)
						if (prosElegxo
							.getInEdges().get(k).getFromNode().getInEdges().get(0)
							.getType() == EdgeType.EDGE_TYPE_SCHEMA ||
							prosElegxo
								.getInEdges().get(k).getFromNode().getInEdges().get(0)
								.getType() == EdgeType.EDGE_TYPE_OUTPUT)
							toRet.add(prosElegxo.getInEdges().get(k).getFromNode());
						else { // Aggregate function renamed AS sth
							final EvolutionNode function = prosElegxo.getInEdges().get(k).getFromNode();
							for (int i = 0; i < function.getInEdges().size(); i++)
								if (function.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_MAPPING) {
									final EvolutionNode outV = function.getInEdges().get(i).getFromNode();
									if (outV.getInEdges().get(0).getType() == EdgeType.EDGE_TYPE_SCHEMA ||
										outV.getInEdges().get(0).getType() == EdgeType.EDGE_TYPE_OUTPUT)
										toRet.add(outV);
								}
						}
		}
		if (toRet.size() == 0)
			return null;
		return toRet;
	}

	protected List<EvolutionNode> connectionWithSmtxSearch(final Message message) { // Searching in IN node for OP or GROUP BY edges (they come from SMTX node), if there is a connection, I move message.toSchema to SMTX node.
		final List<EvolutionNode> toRet = new LinkedList<>();
		for (int j = 0; j < message.toSchema.getOutEdges().size(); j++) { // For nodes of input schema.
			final EvolutionNode prosElegxo = message.toSchema.getOutEdges().get(j).getToNode();
			if (prosElegxo.getName().equals(message.parameter))
				for (int k = 0; k < prosElegxo.getInEdges().size(); k++)
					if (prosElegxo.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_OPERATOR ||
						prosElegxo.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_GROUP_BY ||
						prosElegxo.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_ORDER_BY ||
						prosElegxo.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
						toRet.add(prosElegxo.getInEdges().get(k).getFromNode());
		}
		if (toRet.size() == 0)
			return null;
		return toRet;
	}

	public void goToOutputNode(final Message message) {
		for (int i = 0; i < message.toNode.getOutEdges().size(); i++)
			if (message.toNode.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_OUTPUT)
				message.toSchema = message.toNode.getOutEdges().get(i).getToNode();
	}

	public void goToSmtxNode(final Message message) {
		for (int i = 0; i < message.toNode.getOutEdges().size(); i++)
			if (message.toNode.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_SEMANTICS)
				message.toSchema = message.toNode.getOutEdges().get(i).getToNode();
	}

	/**
	 * @author pmanousi Checks if module is ok and informs his consumers with the
	 *         list of messages he generated.
	 */
	public void setModuleStatus(final Message msg) {
		//msg.toNode.setStatus(StatusType.PROPAGATE,false);
		for (final EvolutionEdge e : msg.toNode.getOutEdges())
			if (e.getType() == EdgeType.EDGE_TYPE_OUTPUT || e.getType() == EdgeType.EDGE_TYPE_INPUT ||
				e.getType() == EdgeType.EDGE_TYPE_SEMANTICS || e.getType() == EdgeType.EDGE_TYPE_SCHEMA)
				msg.toNode.setStatus(e.getToNode().getStatus(), false);
	}
}
