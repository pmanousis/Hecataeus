package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public abstract class MaestroAbstract<V extends EvolutionNode<E>,E extends EvolutionEdge>
{
	List<Algorithm<V,E>> policyCheckAlgorithms;
	PriorityQueue<Message<V,E>> myGQueue;
	
	public MaestroAbstract(PriorityQueue<Message<V,E>> q)
	{
		this.policyCheckAlgorithms=new LinkedList<Algorithm<V,E>>();
		myGQueue=q;
	}
	
	@SuppressWarnings("unchecked")
	protected void colorizeSmtx(V start, StatusType smtxStatus, V smtxNode)
	{
		start.setStatus(smtxStatus,false);
		for(int i=0;i<start.getInEdges().size();i++)
		{
			if(start.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_OPERATOR)
			{
				start.getInEdges().get(i).setStatus(smtxStatus,false);
				colorizeSmtx((V) start.getInEdges().get(i).getFromNode(), smtxStatus, smtxNode);
			}
			if(start.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_WHERE)
			{
				start.getInEdges().get(i).setStatus(smtxStatus,false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void goToSmtxNode(Message<V,E> message)
	{
		for(int i=0;i<message.toNode.getOutEdges().size();i++)
		{
			if(message.toNode.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_SEMANTICS)
			{
				message.toSchema=(V) message.toNode.getOutEdges().get(i).getToNode();
			}
		}
	}
	
	@SuppressWarnings("unchecked")	
	protected List<V> connectionWithSmtxSearch(Message<V,E> message)
	{	// Searching in IN node for OP or GROUP BY edges (they come from SMTX node), if there is a connection, I move message.toSchema to SMTX node.
		List<V> toRet=new LinkedList<V>();
		for(int j=0;j<message.toSchema.getOutEdges().size();j++)
		{	// For nodes of input schema.
			V prosElegxo=(V) message.toSchema.getOutEdges().get(j).getToNode();
			if(prosElegxo.getName().equals(message.parameter))
			{	// Found node that gets a change.
				for(int k=0;k<prosElegxo.getInEdges().size();k++)
				{
					if(prosElegxo.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_OPERATOR||
							prosElegxo.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_GROUP_BY||
							prosElegxo.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_ORDER_BY||
							prosElegxo.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
					{
						toRet.add((V) prosElegxo.getInEdges().get(k).getFromNode());
					}
				}
			}
		}
		if(toRet.size()==0)
		{
			return(null);
		}
		return(toRet);
	}
	
	@SuppressWarnings("unchecked")
	public void goToOutputNode(Message<V,E> message)
	{
		for(int i=0;i<message.toNode.getOutEdges().size();i++)
		{
			if(message.toNode.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_OUTPUT)
			{
				message.toSchema=(V) message.toNode.getOutEdges().get(i).getToNode();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<V> connectionWithOutputSearch(Message<V,E> message)
	{	// Searching in IN node for MAP-SELECT edges (they come from OUT node), if there is a connection, I move message.toSchema to OUT node.
		List<V> toRet=new LinkedList<V>();
		for(int j=0;j<message.toSchema.getOutEdges().size();j++)
		{	// For nodes of input schema.
			V prosElegxo=(V) message.toSchema.getOutEdges().get(j).getToNode();
			if(prosElegxo.getName().equals(message.parameter))
			{	// Found node that gets a change.
				for(int k=0;k<prosElegxo.getInEdges().size();k++)
				{
					if(prosElegxo.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_MAPPING)
					{	//Go to OUTPUT
						if(prosElegxo.getInEdges().get(k).getFromNode().getInEdges().get(0).getType()==EdgeType.EDGE_TYPE_SCHEMA||prosElegxo.getInEdges().get(k).getFromNode().getInEdges().get(0).getType()==EdgeType.EDGE_TYPE_OUTPUT)
						{	// Output value
							toRet.add((V) prosElegxo.getInEdges().get(k).getFromNode());
						}
						else
						{	// Aggregate function renamed AS sth
							V function = (V) prosElegxo.getInEdges().get(k).getFromNode();
							for(int i=0;i<function.getInEdges().size();i++)
							{
								if(function.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_MAPPING)
								{
									V outV = (V) function.getInEdges().get(i).getFromNode();
									if(outV.getInEdges().get(0).getType()==EdgeType.EDGE_TYPE_SCHEMA||outV.getInEdges().get(0).getType()==EdgeType.EDGE_TYPE_OUTPUT)
									{
										toRet.add(outV);
									}
								}
							}
						}
					}
				}
			}
		}
		if(toRet.size()==0)
		{
			return(null);
		}
		return(toRet);
	}
	
	@SuppressWarnings("unchecked")
	void treeCleaner(V kombos, EvolutionGraph<V, E> graph)
	{
		V left=null;
		V right=null;
		if(kombos==null)
		{
			return;
		}
		if(kombos.getType()==NodeType.NODE_TYPE_ATTRIBUTE||kombos.getType()==NodeType.NODE_TYPE_CONSTANT)
		{
			List<E> prosDiagrafi=new LinkedList<E>(); 
			for(int i=0;i<kombos.getInEdges().size();i++)
			{
				if(kombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_OPERATOR)
				{
					prosDiagrafi.add(kombos.getInEdges().get(i));
				}
			}
			for(int i=0;i<prosDiagrafi.size();i++)
			{
				graph.removeEdge(prosDiagrafi.get(i));
			}
			if(kombos.getType()==NodeType.NODE_TYPE_CONSTANT)
			{
				graph.removeVertex(kombos);
			}
			return;
		}
		for(int i=0;i<kombos.getOutEdges().size();i++)
		{
			if(kombos.getOutEdges().get(i).getName().equals("op1"))
			{
				left=(V) kombos.getOutEdges().get(i).getToNode();
			}
			if(kombos.getOutEdges().get(i).getName().equals("op2"))
			{
				right=(V) kombos.getOutEdges().get(i).getToNode();
			}
		}
		treeCleaner(left,graph);
		treeCleaner(right, graph);
		graph.removeVertex(kombos);
	}
	
	/**
	 * @author pmanousi
	 * Checks if module is ok and informs his consumers with the list of messages he generated.
	 * */
	public void setModuleStatus(Message<V,E> msg)
	{
		if(msg.toNode.getStatus() != StatusType.BLOCKED)
		{
			msg.toNode.setStatus(StatusType.PROPAGATE,true);
		}
		
		for(E e : msg.toNode.getOutEdges())
		{
			if(e.getType()==EdgeType.EDGE_TYPE_OUTPUT||
					e.getType()==EdgeType.EDGE_TYPE_INPUT||
					e.getType()==EdgeType.EDGE_TYPE_SEMANTICS||
					e.getType()==EdgeType.EDGE_TYPE_SCHEMA)
			{
				msg.toNode.setStatus(e.getToNode().getStatus(),false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<V> cleanUpUnneededAttrs(V kombos)
	{
		List<V> toRet=new LinkedList();
		for(int i=0;i<kombos.getOutEdges().size();i++)
		{
			if(kombos.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				V inSchema= (V) kombos.getOutEdges().get(i).getToNode();
				{
					for(int j=0;j<inSchema.getOutEdges().size();j++)
					{
						if(inSchema.getOutEdges().get(j).getToNode().getInEdges().size()==1)
						{
							toRet.add((V) inSchema.getOutEdges().get(j).getToNode());
						}
					}
				}
			}
		}
		return(toRet);
	}
	
	@SuppressWarnings("unchecked")
	protected List<V> cleanUpUnneededIns(V kombos)
	{
		List<V> toRet=new LinkedList();
		for(int i=0;i<kombos.getOutEdges().size();i++)
		{
			if(kombos.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				V inSchema= (V) kombos.getOutEdges().get(i).getToNode();
				if(inSchema.getOutEdges().size()==1)
				{
					toRet.add(inSchema);
				}
			}
		}
		return(toRet);
	}
	
	abstract void propagateMessage(Message<V,E> msg);
	
	abstract String doRewrite(Message<V,E> currentMessage, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr);
}
