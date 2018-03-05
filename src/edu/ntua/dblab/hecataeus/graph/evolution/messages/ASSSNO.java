package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

public class ASSSNO extends Algorithm {
	public ASSSNO() {

	}

	@Override
	public void execute(final Message msg, final PriorityQueue<Message> queue) {
		askNode(msg.toSchema, msg.event);
		notifyConsumers(msg, queue);
	}

}
