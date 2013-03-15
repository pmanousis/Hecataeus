package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;


public class Message<V extends EvolutionNode<E>, E extends EvolutionEdge> implements Comparable<Message<V,E>>
{
	public V toNode;
	public V toSchema;
	public EventType event;
	public String parameter;
	public Message(V toN, V toS, EventType ev, String par)
	{
		this.event=ev;
		this.toNode=toN;
		this.toSchema=toS;
		this.parameter=par;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if(this.toNode==((Message<V,E>)obj).toNode && this.toSchema==((Message<V,E>)obj).toSchema && this.parameter==((Message<V,E>)obj).parameter && this.event==((Message<V,E>)obj).event)
		{
			return(true);
		}
		return false;
	}
	
	@Override
	public int compareTo(Message<V,E> o)
	{
		 if (this.toNode.ID < o.toNode.ID)
	    {
	        return -1;
	    }
	    if (this.toNode.ID > o.toNode.ID)
	    {
	        return 1;
	    }
	    if(this.event != EventType.ALTER_SEMANTICS)
	    {	// AlterSMTX messages are the last to check.
	    	return -1;
	    }
	    if(this.toSchema.getName().endsWith("_OUT"))
	    {
	    	return(-1);
	    }
		return 0;
	}
	
	@Override
	public Message<V,E> clone() throws CloneNotSupportedException
	{
		return(new Message<V,E>(this.toNode,this.toSchema,this.event,this.parameter));
	}
}
