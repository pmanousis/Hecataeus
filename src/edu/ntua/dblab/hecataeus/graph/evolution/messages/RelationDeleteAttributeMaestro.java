package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationDeleteAttributeMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public RelationDeleteAttributeMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new FCASSNO<V,E>());
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
		List<E> lista=msg.toSchema.getOutEdges();
		for(int i=0;i<lista.size();i++)
		{
			if(lista.get(i).getToNode().getName().equals(msg.parameter))
			{
				while(lista.get(i).getToNode().getOutEdges().size()>0)
				{	// Remove foreign or primary key nodes.
					graph.removeVertex((V) lista.get(i).getToNode().getOutEdges().get(0).getToNode());
				}
				graph.removeVertex((V) lista.get(i).getToNode());
				mr.iaR++;
				return(msg.parameter);
			}
		}
		return("");
	}
}
