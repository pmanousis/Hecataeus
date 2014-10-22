package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class QueryViewRenameProviderMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewRenameProviderMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new ASSS<V,E>());
	}
	
	@Override
	public void propagateMessage(Message<V,E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		setModuleStatus(msg);
	}
	
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		msg.toSchema.setName(msg.toSchema.getName().replace(msg.parameter.replace("_OUT",""),tempParam));
		if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
		{
			mr.iaQ++;
		}
		else
		{
			mr.iaV++;
		}
		return(tempParam);
	}
}
