package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

class AACSSNO<V extends EvolutionNode<E>,E extends EvolutionEdge> extends Algorithm<V,E>
{
	public AACSSNO()
	{
	}
	
	@SuppressWarnings("unchecked")
	public void execute(Message<V,E> msg, PriorityQueue<Message<V,E>> queue)
	{
		List<E> lista=msg.toSchema.getOutEdges();
		for(int i=0;i<lista.size();i++)
		{
			msg.toSchema.setStatus(askNode((V) lista.get(i).getToNode(),msg.event),false);
			lista.get(i).setStatus(msg.toSchema.getStatus(),false);
		}
		msg.toSchema.setStatus(askNode(msg.toSchema, msg.event),false);
		notifyConsumers(msg, queue);
	}
}
