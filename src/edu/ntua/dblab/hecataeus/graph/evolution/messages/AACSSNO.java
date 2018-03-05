package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;

class AACSSNO extends Algorithm {
	public AACSSNO() {
	}

	@Override
	public void execute(final Message msg, final PriorityQueue<Message> queue) {
		final List<EvolutionEdge> lista = msg.toSchema.getOutEdges();
		for (int i = 0; i < lista.size(); i++) {
			msg.toSchema.setStatus(askNode(lista.get(i).getToNode(), msg.event), false);
			lista.get(i).setStatus(msg.toSchema.getStatus(), false);
		}
		msg.toSchema.setStatus(askNode(msg.toSchema, msg.event), false);
		notifyConsumers(msg, queue);
	}
}
