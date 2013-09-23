package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;
import java.util.UUID;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationRenameSelfMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public RelationRenameSelfMaestro(PriorityQueue<Message<V,E>> q)
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
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		stw.stop();
		//String neoOnoma=JOptionPane.showInputDialog("Give new name for: "+msg.toNode.getName());
		String neoOnoma=UUID.randomUUID().toString();
		stw.start();
		msg.toSchema.setName(msg.toSchema.getName().replace(msg.toNode.getName(), neoOnoma));
		for(int i=0;i<msg.toSchema.getOutEdges().size();i++)
		{
			V sn=(V) msg.toSchema.getOutEdges().get(i).getToNode();
			for(int j=0;j<sn.getOutEdges().size();j++)
			{
				sn.getOutEdges().get(j).getToNode().setName(sn.getOutEdges().get(j).getToNode().getName().replace(msg.toNode.getName(), neoOnoma));
			}
		}
		msg.toNode.setName(neoOnoma);
		return(neoOnoma);
	}
}
