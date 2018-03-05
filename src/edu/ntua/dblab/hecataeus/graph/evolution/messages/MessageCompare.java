package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.Comparator;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;

public class MessageCompare implements Comparator<Message> {
	@Override
	public int compare(final Message x, final Message y) {
		if (x.toNode.getID() < y.toNode.getID())
			return -1;
		if (x.toNode.getID() > y.toNode.getID())
			return 1;
		if (x.event != EventType.ALTER_SEMANTICS)
			return -1;
		if (x.toSchema.getName().endsWith("_OUT"))
			return -1;
		return 0;
	}
}
