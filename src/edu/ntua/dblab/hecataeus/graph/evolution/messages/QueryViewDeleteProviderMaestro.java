package edu.ntua.dblab.hecataeus.graph.evolution.messages;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeFactory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFactory;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewDeleteProviderMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewDeleteProviderMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new FCASS<V,E>());
		policyCheckAlgorithms.add(new ASSSNO<V,E>());
		policyCheckAlgorithms.add(new FCASSNO<V,E>());
	}
	@Override
	public void propagateMessage(Message<V,E> msg)
	{
		V inputNode=msg.toSchema;
		for(int p=0;p<inputNode.getOutEdges().size();p++)
		{
			if(inputNode.getOutEdges().get(p).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				msg.parameter=inputNode.getOutEdges().get(p).getToNode().getName();
				// Just like PMQueryViewDeleteAttributeMaestro.
				policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
				EventType tempEvent=msg.event;	// Needed for moving back to event since in semantics we may have ALTER_SEMANTICS.
				String tmppar=msg.parameter;
				List<V> smtxConnections = connectionWithSmtxSearch(msg);
				if(smtxConnections!=null)
				{
					msg.event=EventType.ALTER_SEMANTICS;
					goToSmtxNode(msg);
					policyCheckAlgorithms.get(1).execute(msg, this.myGQueue);
					StatusType smtxStatus=msg.toSchema.getStatus();
					V smtxNode=msg.toSchema;
					for(int i=0;i<smtxConnections.size();i++)
					{
						colorizeSmtx(smtxConnections.get(i), smtxStatus, smtxNode);
					}
					msg.toSchema.getInEdges().get(0).setStatus(smtxStatus,false);
					msg.event=tempEvent;
					msg.parameter=tmppar;
					msg.toSchema=inputNode;
				}
				List<V> outConnections = connectionWithOutputSearch(msg); 
				if(outConnections!=null)
				{
					goToOutputNode(msg);
					for(int i=0;i<outConnections.size();i++)
					{
						msg.parameter=outConnections.get(i).getName();
						policyCheckAlgorithms.get(2).execute(msg, myGQueue);
					}
					msg.toSchema=inputNode;
					msg.event=tempEvent;
					msg.parameter=tmppar;
				}
				setModuleStatus(msg);
			}
		}
	}

	/**
	 * @author pmanousi
	 * TODO: check to see if all attributes of output schema have been deleted
	 * Actually this should happen in previous step (message propagation but never mind).
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		V inputNode=msg.toSchema;
		List<V> prosDiagrafi=new LinkedList<V>();
		prosDiagrafi.add(msg.toSchema);
		for(int p=0;p<inputNode.getOutEdges().size();p++)
		{
			if(inputNode.getOutEdges().get(p).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				msg.parameter=inputNode.getOutEdges().get(p).getToNode().getName();
				// Just like PMQueryViewDeleteAttributeMaestro.
				
				for(int i=0;i<msg.toSchema.getOutEdges().size();i++)
				{
					if(msg.toSchema.getOutEdges().get(i).getToNode().getName().equals(msg.parameter))
					{
						prosDiagrafi.add((V) msg.toSchema.getOutEdges().get(i).getToNode());
					}
				}
				List<V> smtxConnections = connectionWithSmtxSearch(msg);
				if(smtxConnections!=null)
				{
					for(int i=0;i<smtxConnections.size();i++)
					{
						for(int j=0;j<smtxConnections.get(i).getOutEdges().size();j++)
						{
							if(smtxConnections.get(i).getOutEdges().get(j).getToNode().getType()!=NodeType.NODE_TYPE_ATTRIBUTE)
							{
								treeCleaner((V) smtxConnections.get(i).getOutEdges().get(j).getToNode(), graph);
							}
						}
						while(smtxConnections.get(i).getOutEdges().size()>0)
						{
							graph.removeEdge(smtxConnections.get(i).getOutEdges().get(0));
						}
						smtxConnections.get(i).setName(" = ");
						V nvnlo=(V) VisualNodeFactory.create();
						if(smtxConnections.get(i).getInEdges().get(0).getFromNode().getName().equals(" AND ")||smtxConnections.get(i).getInEdges().get(0).getFromNode().getName().endsWith("_SMTX"))
						{	// Always true
							nvnlo.setName("1");
						}
						else
						{	// Always false
							nvnlo.setName("2");
						}
						nvnlo.setType(NodeType.NODE_TYPE_CONSTANT);
						graph.addVertex(nvnlo);
						V nvnro=(V) VisualNodeFactory.create();
						nvnro.setName("1");
						nvnro.setType(NodeType.NODE_TYPE_CONSTANT);
						graph.addVertex(nvnro);
						E eielo=(E) VisualEdgeFactory.create();
						eielo.setName("op1");
						eielo.setType(EdgeType.EDGE_TYPE_OPERATOR);
						eielo.setFromNode(smtxConnections.get(i));
						eielo.setToNode(nvnlo);
						graph.addEdge(eielo);
						E eiero=(E) VisualEdgeFactory.create();
						eiero.setName("op2");
						eiero.setType(EdgeType.EDGE_TYPE_OPERATOR);
						eiero.setFromNode(smtxConnections.get(i));
						eiero.setToNode(nvnro);
						graph.addEdge(eiero);
					}
				}
				List<V> outConnections = connectionWithOutputSearch(msg); 
				if(outConnections!=null)
				{
					for(int i=0;i<outConnections.size();i++)
					{
						prosDiagrafi.add(outConnections.get(i));
						if(prosDiagrafi.contains(outConnections.get(i).getOutEdges().get(0).getToNode())==false)
						{	// aggregateFunction AS name 
							prosDiagrafi.add((V) outConnections.get(i).getOutEdges().get(0).getToNode());
						}
					}
				}
			}
		}
		goToSmtxNode(msg);
		for(int i=0;i<msg.toSchema.getOutEdges().size();i++)
		{
			if(msg.toSchema.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_GROUP_BY)
			{
				if(msg.toSchema.getOutEdges().get(i).getToNode().getOutEdges().size()==0)
				{
					prosDiagrafi.add((V) msg.toSchema.getOutEdges().get(i).getToNode());
				}
			}
		}
		for(int i=0;i<prosDiagrafi.size();i++)
		{
			graph.removeVertex(prosDiagrafi.get(i));
			if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
			{
				mr.iaQ++;
			}
			else
			{
				mr.iaV++;
			}
		}
		return("");
	}
}
