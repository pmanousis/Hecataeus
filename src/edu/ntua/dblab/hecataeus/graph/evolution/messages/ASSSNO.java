package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public class ASSSNO <V extends EvolutionNode<E>,E extends EvolutionEdge> extends Algorithm<V,E>
{
	public ASSSNO()
	{
		
	}
	
	public void execute(Message<V,E> msg, PriorityQueue<Message<V,E>> queue)
	{
		askNode(msg.toSchema, msg.event);
		notifyConsumers(msg, queue);
	}

}
