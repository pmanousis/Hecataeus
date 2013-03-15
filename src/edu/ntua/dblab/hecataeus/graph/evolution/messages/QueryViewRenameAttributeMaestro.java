package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewRenameAttributeMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewRenameAttributeMaestro (PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new FCASS<V,E>());
		policyCheckAlgorithms.add(new FCASSNO<V,E>());
	}
	
	@Override
	void propagateMessage(Message<V,E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		List<V> outConnections=connectionWithOutputSearch(msg); 
		if(outConnections!=null)
		{
			for(int i=0;i<outConnections.size();i++)
			{
				if(outConnections.get(i).getName().equals(msg.parameter))
				{	// maybe connected to output but renamed AS sth
					goToOutputNode(msg);
					policyCheckAlgorithms.get(1).execute(msg, this.myGQueue);
					break;
				}
			}
		}
		setModuleStatus(msg);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		List<V> toRen=new LinkedList<V>();
		for(int i=0;i<msg.toSchema.getOutEdges().size();i++)
		{
			if(msg.toSchema.getOutEdges().get(i).getToNode().getName().equals(msg.parameter))
			{
				toRen.add((V) msg.toSchema.getOutEdges().get(i).getToNode());
			}
		}
		List<V> outConnections = connectionWithOutputSearch(msg); 
		if(outConnections!=null)
		{
			for(int i=0;i<outConnections.size();i++)
			{
				if(outConnections.get(i).getName().equals(msg.parameter))
				{	// maybe connected to output but renamed AS sth
					toRen.add(outConnections.get(i));
				}
			}
		}
		for(int i=0;i<toRen.size();i++)
		{
			toRen.get(i).setName(tempParam);
			if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
			{
				mr.iaQ++;
			}
			else
			{
				mr.iaV++;
			}
		}
		toRen.clear();
		return(tempParam);
	}
}
