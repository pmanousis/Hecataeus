package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationDeleteSelfMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public RelationDeleteSelfMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new AACSSNO<V,E>());
	}
	@Override
	void propagateMessage(Message<V,E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		setModuleStatus(msg);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		while(msg.toNode.getOutEdges().size()>0) {
			while(msg.toNode.getOutEdges().get(0).getToNode().getOutEdges().size()>0) {
				while(msg.toNode.getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode().getOutEdges().size()>0){
					graph.removeVertex((V) msg.toNode.getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode());
				}
				graph.removeVertex((V) msg.toNode.getOutEdges().get(0).getToNode().getOutEdges().get(0).getToNode());
			}
			graph.removeVertex((V) msg.toNode.getOutEdges().get(0).getToNode());
		}
		graph.removeVertex(msg.toNode);
		return("");
	}
}
