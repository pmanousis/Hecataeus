package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public abstract class Algorithm {
	abstract void execute(Message message, PriorityQueue<Message> queue);

	public StatusType askNode(final EvolutionNode node, final EventType event) {
		final EvolutionPolicy plc = node.getPolict(event);
		if (plc != null)
			if (plc.getPolicyType() == PolicyType.BLOCK)
				node.setStatus(StatusType.BLOCKED, false);
			else if (plc.getPolicyType() == PolicyType.PROPAGATE)
				node.setStatus(StatusType.PROPAGATE, false);
			else if (plc.getPolicyType() == PolicyType.PROMPT)
				node.setStatus(StatusType.PROMPT, false);
		return node.getStatus();
	}

	public void notifyConsumers(final Message message, final PriorityQueue<Message> queue) {
		if (message.toSchema.getStatus() == StatusType.PROPAGATE)
			switch (message.event) {
			case ALTER_SEMANTICS:
				sendNewAlterSMTXMessages(message, queue);
				break;
			default:
				if (message.parameter.equals("") || message.parameter.endsWith("_SCHEMA") ||
					message.parameter.endsWith("_OUT"))
					sendNewChangeOutputMessages(message, queue);
				else
					sendNewChangeAttributeMessages(message, queue);
				break;
			}
	}

	/**
	 * @author pmanousi
	 * @param omsg
	 *            Message that my maestro handles.
	 * @param queue
	 *            Global queue of what if analysis. Creates ALTER_SEMANTICS messages
	 *            for all consumers of this maestro and enqueues them to queue.
	 */
	private void sendNewAlterSMTXMessages(final Message omsg, final PriorityQueue<Message> queue) {
		for (int i = 0; i < omsg.toNode.getOutEdges().size(); i++)
			if (omsg.toNode.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_OUTPUT) {
				final EvolutionNode outNode = omsg.toNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < outNode.getInEdges().size(); j++)
					if (outNode.getInEdges().get(j).getType() == EdgeType.EDGE_TYPE_FROM) {
						final EvolutionNode toS = outNode.getInEdges().get(j).getFromNode(); // Just to be able to write smaller line of code.
						final EvolutionNode toN = toS.getInEdges().get(0).getFromNode();
						final EventType evt = EventType.ALTER_SEMANTICS;
						final String par = "";
						queue.add(new Message(toN, toS, evt, par));
					}
			}
	}

	/**
	 * @author pmanousi
	 * @param omsg
	 *            Message that my maestro handles.
	 * @param queue
	 *            Global queue of what if analysis. Creates messages for consumers of
	 *            this maestro that have a connection to attribute that changes and
	 *            enqueues them to queue.
	 */
	private void sendNewChangeAttributeMessages(final Message omsg, final PriorityQueue<Message> queue) {
		for (int i = 0; i < omsg.toNode.getOutEdges().size(); i++)
			if (omsg.toNode.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_OUTPUT) { // Finding output node
				final EvolutionNode outNode = omsg.toNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < outNode.getOutEdges().size(); j++)
					if (outNode.getOutEdges().get(j).getToNode().getName().equals(omsg.parameter)) { // Finding output node child that is named as parameter.
						final EvolutionNode attrNode = outNode.getOutEdges().get(j).getToNode();
						for (int k = 0; k < attrNode.getInEdges().size(); k++)
							if (attrNode.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_MAPPING) { // I have consumers.
								final EvolutionNode toAttrN = attrNode.getInEdges().get(k).getFromNode();
								for (int l = 0; l < toAttrN.getInEdges().size(); l++)
									if (toAttrN.getInEdges().get(l).getType() == EdgeType.EDGE_TYPE_INPUT) {
										final EvolutionNode toS = toAttrN.getInEdges().get(l).getFromNode();
										final EvolutionNode toN = toS.getInEdges().get(0).getFromNode();
										final String par = toAttrN.getName();
										EventType evt = omsg.event;
										if (evt == EventType.DELETE_SELF)
											evt = EventType.DELETE_PROVIDER;
										if (evt == EventType.RENAME_SELF)
											evt = EventType.RENAME_PROVIDER;
										if (evt == EventType.ADD_ATTRIBUTE)
											evt = EventType.ADD_ATTRIBUTE_PROVIDER;
										queue.add(new Message(toN, toS, evt, par));
									}
							}
					}
			}
	}

	/**
	 * @author pmanousi
	 * @param omsg
	 *            Message that my maestro handles.
	 * @param queue
	 *            Global queue of what if analysis. Creates messages for all
	 *            consumers of this maestro and enqueues them to queue.
	 */
	private void sendNewChangeOutputMessages(final Message omsg, final PriorityQueue<Message> queue) {
		for (int i = 0; i < omsg.toNode.getOutEdges().size(); i++)
			if (omsg.toNode.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_OUTPUT) {
				final EvolutionNode outNode = omsg.toNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < outNode.getInEdges().size(); j++)
					if (outNode.getInEdges().get(j).getType() == EdgeType.EDGE_TYPE_FROM) {
						final EvolutionNode toS = outNode.getInEdges().get(j).getFromNode();
						final EvolutionNode toN = toS.getInEdges().get(0).getFromNode();
						if (omsg.event == EventType.RENAME_SELF)
							if (omsg.parameter.equals("") || omsg.parameter.endsWith("_SCHEMA") ||
								omsg.parameter.endsWith("_OUT"))
								queue.add(
									new Message(toN, toS, EventType.RENAME_PROVIDER, omsg.toNode.getName() + "_OUT"));
							else
								queue.add(new Message(toN, toS, EventType.RENAME_PROVIDER, omsg.parameter));
						if (omsg.event == EventType.DELETE_SELF)
							if (omsg.parameter.equals("") || omsg.parameter.endsWith("_SCHEMA") ||
								omsg.parameter.endsWith("_OUT"))
								queue.add(
									new Message(toN, toS, EventType.DELETE_PROVIDER, omsg.toNode.getName() + "_OUT"));
							else
								queue.add(new Message(toN, toS, EventType.DELETE_PROVIDER, omsg.parameter));
						if (omsg.event == EventType.ADD_ATTRIBUTE || omsg.event == EventType.ADD_ATTRIBUTE_PROVIDER)
							queue.add(new Message(toN, toS, EventType.ADD_ATTRIBUTE_PROVIDER, ""));
					}
			}
	}
}
