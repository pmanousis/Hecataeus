package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationRenameAttributeMaestro extends MaestroAbstract {
	public RelationRenameAttributeMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new FCASSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		final List<EvolutionEdge> lista = msg.toSchema.getOutEdges();
		for (int i = 0; i < lista.size(); i++)
			if (lista.get(i).getToNode().getName().equals(msg.parameter)) {
				final EvolutionNode en = lista.get(i).getToNode();
				stw.stop();
				//en.setName(JOptionPane.showInputDialog("Give new name for node: "+en.getName()));
				en.setName(UUID.randomUUID().toString());
				stw.start();
				mr.iaR++;
				return en.getName();
			}
		return "";
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
