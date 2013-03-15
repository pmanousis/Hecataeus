package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class MaestroFactory<V extends EvolutionNode<E>,E extends EvolutionEdge>
{
	/**
	 * @author pmanousi
	 * 
	 * Given an message returns the needed maestro.
	 * */
	public static <V extends EvolutionNode<E>,E extends EvolutionEdge> MaestroAbstract<V,E> create(Message<V,E> msg,PriorityQueue<Message<V,E>> q)
	{
		if(msg.event==EventType.DELETE_ATTRIBUTE)
		{
			return(new RelationDeleteAttributeMaestro<V,E>(q));
		}
		if(msg.event==EventType.RENAME_ATTRIBUTE)
		{
			return(new RelationRenameAttributeMaestro<V,E>(q));
		}
		if(msg.event==EventType.ADD_ATTRIBUTE)
		{
			if(msg.toNode.getType()==NodeType.NODE_TYPE_RELATION)
			{
				return(new RelationAddAttributeMaestro<V,E>(q));
			}
			else
			{
				return(new QueryViewAddAttributeMaestro<V,E>(q));
			}
		}
		if(msg.event==EventType.DELETE_SELF)
		{
			if(msg.toNode.getType()==NodeType.NODE_TYPE_RELATION)
			{
				return(new RelationDeleteSelfMaestro<V,E>(q));
			}
			else
			{
				return(new QueryViewDeleteSelfMaestro<V,E>(q));
			}
		}
		if(msg.event==EventType.RENAME_SELF)
		{
			if(msg.toNode.getType()==NodeType.NODE_TYPE_RELATION)
			{
				return(new RelationRenameSelfMaestro<V,E>(q));
			}
			else
			{
				return(new QueryViewRenameSelfMaestro<V,E>(q));
			}
		}
		if(msg.event==EventType.DELETE_PROVIDER)
		{
			if(msg.parameter!=null&&(msg.parameter.endsWith("_OUT")||msg.parameter.endsWith("_SCHEMA")))
			{
				return(new QueryViewDeleteProviderMaestro<V,E>(q));
			}
			else if(msg.parameter!=null)
			{
				return(new QueryViewDeleteAttributeMaestro<V,E>(q));
			}
		}
		if(msg.event==EventType.RENAME_PROVIDER)
		{
			if(msg.parameter!=null&&(msg.parameter.endsWith("_OUT")||msg.parameter.endsWith("_SCHEMA")))
			{
				return(new QueryViewRenameProviderMaestro<V,E>(q));
			}
			else if(msg.parameter!=null)
			{
				return(new QueryViewRenameAttributeMaestro<V,E>(q));
			}
		}
		if(msg.event==EventType.ADD_ATTRIBUTE_PROVIDER)
		{
			return(new QueryViewAddAttributeProviderMaestro<V, E>(q));
		}
		if(msg.event==EventType.ALTER_SEMANTICS)
		{
			return(new QueryViewAlterSemanticsMaestro<V,E>(q));
		}
		return null;
	}
}
