package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;
import java.util.UUID;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class RelationAddAttributeMaestro extends MaestroAbstract {
	public RelationAddAttributeMaestro(final PriorityQueue<Message> q) {
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
		//String tmpStr=JOptionPane.showInputDialog("Give name for new node.").toUpperCase();
		String tmpStr = UUID.randomUUID().toString();
		stw.start();
		while (ifAlreadyAtOutput(msg.toNode, tmpStr, stw)) {
			stw.stop();
			//tmpStr=JOptionPane.showInputDialog("Give name for new node.").toUpperCase();
			tmpStr = UUID.randomUUID().toString();
			stw.start();
		}
		final EvolutionNode nvn = new EvolutionNode();
		nvn.setName(tmpStr);
		nvn.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvn);
		mr.iaR++;
		final EvolutionEdge eie = new EvolutionEdge();
		eie.setName("S");
		eie.setType(EdgeType.EDGE_TYPE_INPUT);
		eie.setFromNode(msg.toSchema);
		eie.setToNode(nvn);
		graph.addEdge(eie);
		return tmpStr;
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}

	private Boolean ifAlreadyAtOutput(final EvolutionNode head, final String onoma, final StopWatch stw) {
		for (int i = 0; i < head.getOutEdges().size(); i++)
			if (head.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT) {
				final EvolutionNode outSchema = head.getOutEdges().get(i).getToNode();
				for (int j = 0; j < outSchema.getOutEdges().size(); j++)
					if (outSchema.getOutEdges().get(i).getToNode().getName().equals(onoma)) {
						stw.stop();
						//JOptionPane.showMessageDialog(null, "There is already an attribute named: "+onoma+" at the output schema of "+head.getName());
						stw.start();
						return true;
					}
			}
		return false;
	}
}
