package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewDeleteSelfMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewDeleteSelfMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new AACSSNO<V,E>());
	}
	@Override
	void propagateMessage(Message<V,E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT)
			{
				msg.toNode.getOutEdges().get(i).setStatus(msg.toNode.getOutEdges().get(i).getToNode().getStatus(),false);
			}
		}
		setModuleStatus(msg);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{	//clean smtx
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				for(int j=0;j<msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().size();j++)
				{
					if(msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_WHERE)
					{	// where clause
						treeCleaner((V) msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode(), graph);
					}
					else if(msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_GROUP_BY)
					{	// group by clause
						graph.removeVertex((V) msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode());
					}
				}
			}
		}
		
		List<E> mschemata=new LinkedList<E>();
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()!=EdgeType.EDGE_TYPE_USES)
			{
				mschemata.add(msg.toNode.getOutEdges().get(i));
			}
		}
		for(int i=0;i<mschemata.size();i++)
		{	//clean in and out
			List<E> mattrs=new LinkedList<E>();
			for(int j=0;j<mschemata.get(i).getToNode().getOutEdges().size();j++)
			{
				if(mschemata.get(i).getToNode().getOutEdges().get(j).getType()!=EdgeType.EDGE_TYPE_FROM)
				{
					mattrs.add((E) mschemata.get(i).getToNode().getOutEdges().get(j));
				}
			}
			for(int j=0;j<mattrs.size();j++)
			{
				graph.removeVertex((V) mattrs.get(j).getToNode());
			}
			graph.removeVertex((V) mschemata.get(i).getToNode());
		}
		mschemata.clear();
		graph.removeVertex(msg.toNode);
		return("");
	}
}
