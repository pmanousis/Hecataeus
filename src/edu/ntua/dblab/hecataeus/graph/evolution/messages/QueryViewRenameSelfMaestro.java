package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;
import java.util.UUID;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewRenameSelfMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewRenameSelfMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new ASSSNO<V,E>());
	}
	@Override
	void propagateMessage(Message<V,E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		setModuleStatus(msg);
	}
	
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		stw.stop();
		//String neoOnoma=JOptionPane.showInputDialog("Give new name for: "+msg.toNode.getName());
		String neoOnoma=UUID.randomUUID().toString();
		stw.start();
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			msg.toNode.getOutEdges().get(i).getToNode().setName(msg.toNode.getOutEdges().get(i).getToNode().getName().replace(msg.toNode.getName(), neoOnoma));
		}
		msg.toNode.setName(neoOnoma);
		return(neoOnoma);
	}
}
