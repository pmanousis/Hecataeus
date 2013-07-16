package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeFactory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFactory;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewAddAttributeProviderMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{

	public QueryViewAddAttributeProviderMaestro(PriorityQueue<Message<V, E>> q) {
		super(q);
		policyCheckAlgorithms.add(new ASSS<V,E>());
		policyCheckAlgorithms.add(new ASSSNO<V,E>());
		policyCheckAlgorithms.add(new ASSSNO<V,E>());
	}

	@SuppressWarnings("unchecked")
	@Override
	void propagateMessage(Message<V, E> msg)
	{
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		EventType evt=msg.event;
		msg.event=EventType.ALTER_SEMANTICS;
		goToSmtxNode(msg);
		for(int i=0;i<msg.toSchema.getOutEdges().size();i++)
		{
			if(msg.toSchema.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_GROUP_BY)
			{	// Only gb nodes are affected
				policyCheckAlgorithms.get(1).execute(msg, this.myGQueue);
			}
		}
		msg.event=evt;
		goToOutputNode(msg);
		policyCheckAlgorithms.get(2).execute(msg, this.myGQueue);
		setModuleStatus(msg);
	}

	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V, E> msg, String tempParam, EvolutionGraph<V, E> graph, StopWatch stw, MetriseisRewrite mr)
	{	// OUT schema (if already existing name there ask for new name, else make new nodes)
		V outNode=null;
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT)
			{
				outNode=(V) msg.toNode.getOutEdges().get(i).getToNode();
				break;
			}
		}
		
		// IN schema
		V nvn=(V) VisualNodeFactory.create();
		nvn.setName(tempParam);
		nvn.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvn);
		E eie=(E) VisualEdgeFactory.create();
		eie.setName("S");
		eie.setType(EdgeType.EDGE_TYPE_INPUT);
		eie.setFromNode(msg.toSchema);
		eie.setToNode(nvn);
		graph.addEdge(eie);
		// To provider of other view or relation
		E eietp=(E) VisualEdgeFactory.create();
		eietp.setName("map-select");
		eietp.setType(EdgeType.EDGE_TYPE_MAPPING);
		eietp.setFromNode(nvn);
		for(int i=0;i<msg.toSchema.getOutEdges().size();i++)
		{
			if(msg.toSchema.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_FROM)
			{
				V prov=(V) msg.toSchema.getOutEdges().get(i).getToNode();
				for(int j=0;j<prov.getOutEdges().size();j++)
				{
					if(prov.getOutEdges().get(j).getToNode().getName().equals(tempParam))
					{
						eietp.setToNode(prov.getOutEdges().get(j).getToNode());
						break;
					}
				}
			}
		}
		graph.addEdge(eietp);
		// Smtx
		V smtxNode=null;
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				smtxNode=(V) msg.toNode.getOutEdges().get(i).getToNode();
				break;
			}
		}
		for(int i=0;i<smtxNode.getOutEdges().size();i++)
		{
			if(smtxNode.getOutEdges().get(i).getToNode().getType()==NodeType.NODE_TYPE_GROUP_BY)
			{
				stw.stop();
				//int reply=JOptionPane.showConfirmDialog(null, "Should "+tempParam+" be used as grouper in group by of "+msg.toNode.getName()+" ?",tempParam,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				int reply=JOptionPane.NO_OPTION;
				stw.start();
				if(reply==JOptionPane.YES_OPTION)
				{
					E ese=(E) VisualEdgeFactory.create();
					ese.setName("group by "+(smtxNode.getOutEdges().get(i).getToNode().getOutEdges().size()+1));
					ese.setType(EdgeType.EDGE_TYPE_GROUP_BY);
					ese.setFromNode(smtxNode.getOutEdges().get(i).getToNode());
					ese.setToNode(nvn);
					graph.addEdge(ese);
				}
				else if(reply==JOptionPane.NO_OPTION)
				{
					String[] choices={"MIN","MAX","AVG","COUNT","SUM"};	// Aggregate functions.
					stw.stop();
					String apantisi=null;//(String) JOptionPane.showInputDialog(null, "Select aggregate function (MIN, MAX, AVG, COUNT, SUM) that "+tempParam+" should be used at "+msg.toNode.getName()+"\nDefault value is: "+choices[0], tempParam, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					stw.start();
					if(apantisi==null)
					{
						apantisi=new String("COUNT");
					}
					
					V nvnouto=(V) VisualNodeFactory.create();
					nvnouto.setName(tempParam);
					nvnouto.setType(NodeType.NODE_TYPE_ATTRIBUTE);
					graph.addVertex(nvnouto);
					
					if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
					{
						mr.iaQ++;
					}
					else
					{
						mr.iaV++;
					}
					
					E eoeo=(E) VisualEdgeFactory.create();
					eoeo.setName("S");
					eoeo.setType(EdgeType.EDGE_TYPE_SCHEMA);
					eoeo.setFromNode(outNode);
					eoeo.setToNode(nvnouto);
					graph.addEdge(eoeo);
					
					V nvnoutf=(V) VisualNodeFactory.create();
					nvnoutf.setName(apantisi);
					nvnoutf.setType(NodeType.NODE_TYPE_FUNCTION);
					graph.addVertex(nvnoutf);
					
					if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
					{
						mr.iaQ++;
					}
					else
					{
						mr.iaV++;
					}
					
					E eoe=(E) VisualEdgeFactory.create();
					eoe.setName("map-select");
					eoe.setType(EdgeType.EDGE_TYPE_MAPPING);
					eoe.setFromNode(nvnouto);
					eoe.setToNode(nvnoutf);
					graph.addEdge(eoe);
					
					E eos=(E) VisualEdgeFactory.create();
					eos.setName("S");
					eos.setType(EdgeType.EDGE_TYPE_SEMANTICS);
					eos.setFromNode(smtxNode);
					eos.setToNode(nvnoutf);
					graph.addEdge(eos);
					
					E ebn=(E) VisualEdgeFactory.create();
					ebn.setName("map-select");
					ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
					ebn.setFromNode(nvnoutf);
					ebn.setToNode(nvn);
					graph.addEdge(ebn);
					return(nvnouto.getName());
				}
			}
		}
		
		String apantisi=tempParam;
		for(int i=0;i<outNode.getOutEdges().size();i++)
		{
			if(outNode.getOutEdges().get(i).getToNode().getName().equals(tempParam))
			{	// Ask for new name? select x, x as x1?
				List<String> yparxousesOnomasies=new LinkedList<String>();
				for(int k=0;k<outNode.getOutEdges().size();k++)
				{
					yparxousesOnomasies.add(outNode.getOutEdges().get(k).getToNode().getName());
				}
				while(yparxousesOnomasies.contains(apantisi))
				{
					String forMessage=new String();
					for(int i1=0;i1<yparxousesOnomasies.size();i1++)
					{
						forMessage+="\n"+yparxousesOnomasies.get(i1);
					}
					stw.stop();
					//apantisi=JOptionPane.showInputDialog("Name: "+tempParam+" for node: "+msg.toNode.getName()+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
					apantisi=UUID.randomUUID().toString();
					stw.start();
					while(apantisi==null)
					{
						stw.stop();
						//tempParam=JOptionPane.showInputDialog("Name: "+tempParam+" for node: "+msg.toNode.getName()+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
						apantisi=UUID.randomUUID().toString();
						stw.start();
					}
				}
			}
		}
		
		V nvnout=(V) VisualNodeFactory.create();
		nvnout.setName(apantisi);
		nvnout.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvnout);
		
		if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
		{
			mr.iaQ++;
		}
		else
		{
			mr.iaV++;
		}
		
		E eoe=(E) VisualEdgeFactory.create();
		eoe.setName("S");
		eoe.setType(EdgeType.EDGE_TYPE_SCHEMA);
		eoe.setFromNode(outNode);
		eoe.setToNode(nvnout);
		graph.addEdge(eoe);
		
		E ebn=(E) VisualEdgeFactory.create();
		ebn.setName("map-select");
		ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
		ebn.setFromNode(nvnout);
		ebn.setToNode(nvn);
		graph.addEdge(ebn);
		
		return(tempParam);
	}
}
