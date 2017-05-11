package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class MaestroFactory {
	/**
	 * @author pmanousi
	 *
	 *         Given an message returns the needed maestro.
	 */
	public static MaestroAbstract create(final Message msg, final PriorityQueue<Message> q) {
		if (msg.event == EventType.DELETE_ATTRIBUTE)
			return new RelationDeleteAttributeMaestro(q);
		if (msg.event == EventType.RENAME_ATTRIBUTE)
			return new RelationRenameAttributeMaestro(q);
		if (msg.event == EventType.ADD_ATTRIBUTE)
			if (msg.toNode.getType() == NodeType.NODE_TYPE_RELATION)
				return new RelationAddAttributeMaestro(q);
			else
				return new QueryViewAddAttributeMaestro(q);
		if (msg.event == EventType.DELETE_SELF)
			if (msg.toNode.getType() == NodeType.NODE_TYPE_RELATION)
				return new RelationDeleteSelfMaestro(q);
			else
				return new QueryViewDeleteSelfMaestro(q);
		if (msg.event == EventType.RENAME_SELF)
			if (msg.toNode.getType() == NodeType.NODE_TYPE_RELATION)
				return new RelationRenameSelfMaestro(q);
			else
				return new QueryViewRenameSelfMaestro(q);
		if (msg.event == EventType.DELETE_PROVIDER)
			if (msg.parameter != null && (msg.parameter.endsWith("_OUT") || msg.parameter.endsWith("_SCHEMA")))
				return new QueryViewDeleteProviderMaestro(q);
			else if (msg.parameter != null)
				return new QueryViewDeleteAttributeMaestro(q);
		if (msg.event == EventType.RENAME_PROVIDER)
			if (msg.parameter != null && (msg.parameter.endsWith("_OUT") || msg.parameter.endsWith("_SCHEMA")))
				return new QueryViewRenameProviderMaestro(q);
			else if (msg.parameter != null)
				return new QueryViewRenameAttributeMaestro(q);
		if (msg.event == EventType.ADD_ATTRIBUTE_PROVIDER)
			return new QueryViewAddAttributeProviderMaestro(q);
		if (msg.event == EventType.ALTER_SEMANTICS)
			return new QueryViewAlterSemanticsMaestro(q);
		return null;
	}
}
