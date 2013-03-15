package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public class AACSS<V extends EvolutionNode<E>,E extends EvolutionEdge> extends Algorithm<V,E>
{
	public AACSS()
	{
	}
	
	@SuppressWarnings("unchecked")
	public void execute(Message<V,E> msg, PriorityQueue<Message<V,E>> queue)
	{
		List<E> lista=msg.toSchema.getOutEdges();
		for(int i=0;i<lista.size();i++)
		{
			if(lista.get(i).getType()==EdgeType.EDGE_TYPE_FROM)
			{
				continue;
			}
			V en=(V) lista.get(i).getToNode();
			msg.toSchema.setStatus(askNode(en,msg.event),false);
			lista.get(i).setStatus(msg.toSchema.getStatus(),false);
		}
		if(msg.toSchema.getStatus()!=StatusType.BLOCKED)
		{
			msg.toSchema.setStatus(askNode(msg.toSchema, msg.event),false);
		}
	}
}
