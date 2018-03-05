package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public class Message implements Comparable<Message> {
	public EvolutionNode toNode;
	public EvolutionNode toSchema;
	public EventType event;
	public String parameter;

	public Message(final EvolutionNode toN, final EvolutionNode toS, final EventType ev, final String par) {
		event = ev;
		toNode = toN;
		toSchema = toS;
		parameter = par;
	}

	@Override
	public Message clone() throws CloneNotSupportedException {
		return new Message(toNode, toSchema, event, parameter);
	}

	@Override
	public int compareTo(final Message o) {
		if (toNode.getID() < o.toNode.getID())
			return -1;
		if (toNode.getID() > o.toNode.getID())
			return 1;
		if (event != EventType.ALTER_SEMANTICS)
			return -1;
		if (toSchema.getName().endsWith("_OUT"))
			return -1;
		return 0;
	}

	@Override
	public boolean equals(final Object obj) {
		if (toNode == ((Message) obj).toNode && toSchema == ((Message) obj).toSchema &&
			parameter == ((Message) obj).parameter && event == ((Message) obj).event)
			return true;
		return false;
	}
}
