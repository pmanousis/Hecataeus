package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

public class ASSS extends Algorithm {
	public ASSS() {

	}

	@Override
	public void execute(final Message msg, final PriorityQueue<Message> queue) {
		askNode(msg.toSchema, msg.event);
	}
}
