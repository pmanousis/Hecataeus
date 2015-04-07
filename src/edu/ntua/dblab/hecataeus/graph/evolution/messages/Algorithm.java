package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicies;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

public abstract class Algorithm<V extends EvolutionNode<E>,E extends EvolutionEdge>
{
	abstract void execute(Message<V,E> message, PriorityQueue<Message<V,E>> queue);
	
	/**
	 * @author pmanousi
	 * {@link VisualNode} to be asked for his policy about the {@link EventType}.
	 * */
	public StatusType askNode(V node, EventType event)
	{
		EvolutionPolicies plcs=node.getPolicies();
		EvolutionPolicy<V> plc=plcs.get(event);
		if(plc!=null)
		{
			if(plc.getPolicyType()==PolicyType.BLOCK)
			{
				node.setStatus(StatusType.BLOCKED,false);
			}
			else if(plc.getPolicyType()==PolicyType.PROPAGATE)
			{
				node.setStatus(StatusType.PROPAGATE,false);
			}
			else if(plc.getPolicyType()==PolicyType.PROMPT)
			{
				node.setStatus(StatusType.PROMPT,false);
			}
		}
		return node.getStatus();
	}
	
	/**
	 * @author pmanousi
	 * @param omsg Message that my maestro handles.
	 * @param queue Global queue of what if analysis.
	 * Creates ALTER_SEMANTICS messages for all consumers of this maestro and enqueues them to queue.
	 * */
	@SuppressWarnings("unchecked")
	private void sendNewAlterSMTXMessages(Message<V,E> omsg, PriorityQueue<Message<V,E>> queue)
	{
		for(int i=0;i<omsg.toNode.getOutEdges().size();i++)
		{
			if(omsg.toNode.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_OUTPUT)
			{
				V outNode=(V) omsg.toNode.getOutEdges().get(i).getToNode();
				for(int j=0;j<outNode.getInEdges().size();j++)
				{
					if(outNode.getInEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
					{
						V toS=(V) outNode.getInEdges().get(j).getFromNode();	// Just to be able to write smaller line of code.
						V toN=(V) toS.getInEdges().get(0).getFromNode();
						EventType evt=EventType.ALTER_SEMANTICS;
						String par="";
						queue.add(new Message<V,E>(toN,toS,evt,par));
					}
				}
			}
		}
	}
	
	/**
	 * @author pmanousi
	 * @param omsg Message that my maestro handles.
	 * @param queue Global queue of what if analysis.
	 * Creates messages for all consumers of this maestro and enqueues them to queue.
	 * */
	@SuppressWarnings("unchecked")
	private void sendNewChangeOutputMessages(Message<V,E> omsg, PriorityQueue<Message<V,E>> queue)
	{
		for(int i=0;i<omsg.toNode.getOutEdges().size();i++)
		{
			if(omsg.toNode.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_OUTPUT)
			{
				V outNode=(V) omsg.toNode.getOutEdges().get(i).getToNode();
				for(int j=0;j<outNode.getInEdges().size();j++)
				{
					if(outNode.getInEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
					{
						V toS=(V) outNode.getInEdges().get(j).getFromNode();
						V toN=(V) toS.getInEdges().get(0).getFromNode();
						if(omsg.event==EventType.RENAME_SELF)
						{
							if(omsg.parameter.equals("")||omsg.parameter.endsWith("_SCHEMA")||omsg.parameter.endsWith("_OUT"))
							{
								queue.add(new Message<V,E>(toN,toS,EventType.RENAME_PROVIDER,omsg.toNode.getName()+"_OUT"));
							}
							else
							{
								queue.add(new Message<V,E>(toN,toS,EventType.RENAME_PROVIDER,omsg.parameter));
							}
						}
						if(omsg.event==EventType.DELETE_SELF)
						{
							if(omsg.parameter.equals("")||omsg.parameter.endsWith("_SCHEMA")||omsg.parameter.endsWith("_OUT"))
							{
								queue.add(new Message<V,E>(toN,toS,EventType.DELETE_PROVIDER,omsg.toNode.getName()+"_OUT"));
							}
							else
							{
								queue.add(new Message<V,E>(toN,toS,EventType.DELETE_PROVIDER,omsg.parameter));
							}
						}
						if(omsg.event==EventType.ADD_ATTRIBUTE||omsg.event==EventType.ADD_ATTRIBUTE_PROVIDER)
						{
							queue.add(new Message<V,E>(toN,toS,EventType.ADD_ATTRIBUTE_PROVIDER,""));
						}
					}
				}
			}
		}
	}
	
	/**
	 * @author pmanousi
	 * @param omsg Message that my maestro handles.
	 * @param queue Global queue of what if analysis.
	 * Creates messages for consumers of this maestro that have a connection to attribute that changes and enqueues them to queue.
	 * */
	@SuppressWarnings("unchecked")
	private void sendNewChangeAttributeMessages(Message<V,E> omsg, PriorityQueue<Message<V,E>> queue)
	{
		for(int i=0;i<omsg.toNode.getOutEdges().size();i++)
		{
			if(omsg.toNode.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_OUTPUT)
			{	// Finding output node
				V outNode=(V) omsg.toNode.getOutEdges().get(i).getToNode();
				for(int j=0;j<outNode.getOutEdges().size();j++)
				{
					if(outNode.getOutEdges().get(j).getToNode().getName().equals(omsg.parameter))
					{	// Finding output node child that is named as parameter.
						V attrNode=(V) outNode.getOutEdges().get(j).getToNode();
						for(int k=0;k<attrNode.getInEdges().size();k++)
						{
							if(attrNode.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_MAPPING)
							{	// I have consumers.
								V toAttrN=(V) attrNode.getInEdges().get(k).getFromNode();
								for(int l=0;l<toAttrN.getInEdges().size();l++)
								{
									if(toAttrN.getInEdges().get(l).getType()==EdgeType.EDGE_TYPE_INPUT)
									{
										V toS=(V) toAttrN.getInEdges().get(l).getFromNode();
										V toN=(V) toS.getInEdges().get(0).getFromNode();
										String par=toAttrN.getName();
										EventType evt=omsg.event;
										if(evt==EventType.DELETE_SELF)
										{
											evt=EventType.DELETE_PROVIDER;
										}
										if(evt==EventType.RENAME_SELF)
										{
											evt=EventType.RENAME_PROVIDER;
										}
										if(evt==EventType.ADD_ATTRIBUTE)
										{
											evt=EventType.ADD_ATTRIBUTE_PROVIDER;
										}
										queue.add(new Message<V,E>(toN,toS,evt,par));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void notifyConsumers(Message<V,E> message, PriorityQueue<Message<V,E>> queue)
	{
		if(message.toSchema.getStatus()==StatusType.PROPAGATE)
		{
			switch (message.event) {
			case ALTER_SEMANTICS:
				sendNewAlterSMTXMessages(message, queue);
				break;
			default:
				if(message.parameter.equals("")||message.parameter.endsWith("_SCHEMA")||message.parameter.endsWith("_OUT"))	// Whole schema change.
				{
					sendNewChangeOutputMessages(message,queue);
				}
				else
				{
					sendNewChangeAttributeMessages(message,queue);
				}
				break;
			}
		}
	}
}
