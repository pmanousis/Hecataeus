package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;
import java.util.UUID;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeFactory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFactory;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class RelationAddAttributeMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public RelationAddAttributeMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new ASSSNO<V,E>());
	}
	@Override
	void propagateMessage(Message<V,E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		setModuleStatus(msg);
	}
	
	@SuppressWarnings("unchecked")
	private Boolean ifAlreadyAtOutput(V head, String onoma, StopWatch stw)
	{
		for(int i=0;i<head.getOutEdges().size();i++)
		{
			if(head.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT)
			{
				V outSchema=(V) head.getOutEdges().get(i).getToNode();
				for(int j=0;j<outSchema.getOutEdges().size();j++)
				{
					if(outSchema.getOutEdges().get(i).getToNode().getName().equals(onoma))
					{
						stw.stop();
						//JOptionPane.showMessageDialog(null, "There is already an attribute named: "+onoma+" at the output schema of "+head.getName());
						stw.start();
						return(true);
					}
				}
			}
		}
		return(false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		stw.stop();
		//String tmpStr=JOptionPane.showInputDialog("Give name for new node.").toUpperCase();
		String tmpStr=UUID.randomUUID().toString();
		stw.start();
		while(ifAlreadyAtOutput(msg.toNode, tmpStr, stw))
		{
			stw.stop();
			//tmpStr=JOptionPane.showInputDialog("Give name for new node.").toUpperCase();
			tmpStr=UUID.randomUUID().toString();
			stw.start();
		}
		V nvn=(V) VisualNodeFactory.create();
		nvn.setName(tmpStr);
		nvn.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvn);
		mr.iaR++;
		E eie=(E) VisualEdgeFactory.create();
		eie.setName("S");
		eie.setType(EdgeType.EDGE_TYPE_INPUT);
		eie.setFromNode(msg.toSchema);
		eie.setToNode(nvn);
		graph.addEdge(eie);
		return(tmpStr);
	}
}
