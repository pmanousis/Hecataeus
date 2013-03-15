package edu.ntua.dblab.hecataeus.graph.evolution.messages;
import java.util.Comparator;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;

@SuppressWarnings("unchecked")
public class MessageCompare implements Comparator<Message>
{
	@Override
	public int compare(Message x, Message y)
	{
	    if (x.toNode.ID < y.toNode.ID)
	    {
	        return -1;
	    }
	    if (x.toNode.ID > y.toNode.ID)
	    {
	        return 1;
	    }
	    if(x.event != EventType.ALTER_SEMANTICS)
	    {	// AlterSMTX messages are the last to check.
	    	return -1;
	    }
	    if(x.toSchema.getName().endsWith("_OUT"))
	    {
	    	return(-1);
	    }
		return 0;
	}
}
