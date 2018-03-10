package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public class AACSS extends Algorithm {
	public AACSS() {
	}

	@Override
	public void execute(final Message msg, final PriorityQueue<Message> queue) {
		final List<EvolutionEdge> lista = msg.toSchema.getOutEdges();
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getType() == EdgeType.EDGE_TYPE_FROM)
				continue;
			final EvolutionNode en = lista.get(i).getToNode();
			msg.toSchema.setStatus(askNode(en, msg.event), false);
			lista.get(i).setStatus(msg.toSchema.getStatus(), false);
		}
		if (msg.toSchema.getStatus() != StatusType.BLOCKED)
			msg.toSchema.setStatus(askNode(msg.toSchema, msg.event), false);
	}
}
