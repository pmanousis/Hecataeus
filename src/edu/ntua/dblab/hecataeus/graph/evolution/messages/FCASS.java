package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public class FCASS<V extends EvolutionNode<E>,E extends EvolutionEdge> extends Algorithm<V,E>
{
	public FCASS()
	{
	}
	
	@SuppressWarnings("unchecked")
	public void execute(Message<V,E> msg, PriorityQueue<Message<V,E>> queue)
	{
		List<E> lista=msg.toSchema.getOutEdges();
		for(int i=0;i<lista.size();i++)
		{
			if(lista.get(i).getToNode().getName().equals(msg.parameter))
			{
				V en=(V) lista.get(i).getToNode();
				if(msg.event==EventType.DELETE_ATTRIBUTE)
				{
					msg.event=EventType.DELETE_SELF;
				}
				if(msg.event==EventType.RENAME_ATTRIBUTE)
				{
					msg.event=EventType.RENAME_SELF;
				}
				msg.toSchema.setStatus(askNode(en, msg.event),false);
				lista.get(i).setStatus(msg.toSchema.getStatus(),false);
				for(int j=0;j<en.getOutEdges().size();j++)
				{
					en.getOutEdges().get(j).setStatus(msg.toSchema.getStatus(),false);
				}
				return;
			}
		}
	}
}
