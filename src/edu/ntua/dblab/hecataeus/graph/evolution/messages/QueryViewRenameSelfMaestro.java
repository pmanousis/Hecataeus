package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;
import java.util.UUID;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewRenameSelfMaestro extends MaestroAbstract {
	public QueryViewRenameSelfMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new ASSSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		stw.stop();
		//String neoOnoma=JOptionPane.showInputDialog("Give new name for: "+msg.toNode.getName());
		final String neoOnoma = UUID.randomUUID().toString();
		stw.start();
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			msg.toNode.getOutEdges().get(i).getToNode().setName(
				msg.toNode.getOutEdges().get(i).getToNode().getName().replace(msg.toNode.getName(), neoOnoma));
		msg.toNode.setName(neoOnoma);
		return neoOnoma;
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
