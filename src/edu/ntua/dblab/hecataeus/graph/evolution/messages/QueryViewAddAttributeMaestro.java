package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFactory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeFactory;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import java.util.Random;

public class QueryViewAddAttributeMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewAddAttributeMaestro(PriorityQueue<Message<V,E>> q)
	{
		super(q);
		policyCheckAlgorithms.add(new ASSSNO<V,E>());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	void propagateMessage(Message<V,E> msg)
	{
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT)
			{
				msg.toSchema=(V) msg.toNode.getOutEdges().get(i).getToNode();
			}
		}
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		setModuleStatus(msg);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		List<String> yparxousesOnomasies=new LinkedList<String>();
		V outNode=null;
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT)
			{
				outNode=(V) msg.toNode.getOutEdges().get(i).getToNode();
				for(int j=0;j<outNode.getOutEdges().size();j++)
				{
					yparxousesOnomasies.add(outNode.getOutEdges().get(j).getToNode().getName());
				}
				break;
			}
		}
		List<String> providerAttrs=new LinkedList<String>();
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				V inNode=(V) msg.toNode.getOutEdges().get(i).getToNode();
				for(int j=0;j<inNode.getOutEdges().size();j++)
				{
					if(inNode.getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
					{
						V provNode=(V) inNode.getOutEdges().get(j).getToNode();
						for(int k=0;k<provNode.getOutEdges().size();k++)
						{
							providerAttrs.add(inNode.getName()+"."+provNode.getOutEdges().get(k).getToNode().getName());
						}
					}
				}
			}
		}
		stw.stop();
		//String apantisi=(String) JOptionPane.showInputDialog(null, "Select attribute for output at "+msg.toNode.getName(), msg.toNode.getName(), JOptionPane.QUESTION_MESSAGE, null, providerAttrs.toArray(), providerAttrs.toArray()[0]);
		Random rg=new Random();
		String apantisi=(String) providerAttrs.toArray()[rg.nextInt()%providerAttrs.size()];
		
		stw.start();
		if(apantisi==null)
		{	// TODO: Actually it could be value and just return!!!!
			apantisi=new String((String) providerAttrs.toArray()[0]);
		}
		V inputSchema = null;
		V epilegmenosKombos = null;	// New attribute for output
		V provKombos=null;
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT&&msg.toNode.getOutEdges().get(i).getToNode().getName().equals(apantisi.substring(0,apantisi.indexOf("."))))
			{
				V inNode=(V) msg.toNode.getOutEdges().get(i).getToNode();
				inputSchema=inNode;
				for(int j=0;j<inNode.getOutEdges().size();j++)
				{
					if(inNode.getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
					{
						provKombos=(V) inNode.getOutEdges().get(j).getToNode();
						for(int k=0;k<provKombos.getOutEdges().size();k++)
						{
							if(provKombos.getOutEdges().get(k).getToNode().getName().equals(apantisi.substring(apantisi.indexOf(".")+1)))
							{
								epilegmenosKombos=(V) provKombos.getOutEdges().get(k).getToNode();
							}
						}
					}
				}
			}
		}
		// SMTX?
		V exeiGb=null;
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				for(int j=0;j<msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().size();j++)
				{
					if(msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode().getType()==NodeType.NODE_TYPE_GROUP_BY)
					{
						exeiGb=(V) msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode();
					}
				}
				break;
			}
		}
		// IN schema
		V nvn = null;
		for(int i=0;i<inputSchema.getOutEdges().size();i++)
		{
			if(inputSchema.getOutEdges().get(i).getToNode().getName().equals(epilegmenosKombos.getName()))
			{// Allready @inputSchema
				nvn=(V) inputSchema.getOutEdges().get(i).getToNode();
				break;
			}
		}
		if(nvn==null)
		{	// Not in input, I create it.
			nvn=(V) VisualNodeFactory.create();
			nvn.setName(epilegmenosKombos.getName());
			nvn.setType(NodeType.NODE_TYPE_ATTRIBUTE);
			graph.addVertex(nvn);
			
			if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
			{
				mr.iaQ++;
			}
			else
			{
				mr.iaV++;
			}
			
			E eie=(E) VisualEdgeFactory.create();
			eie.setName("S");
			eie.setType(EdgeType.EDGE_TYPE_INPUT);
			eie.setFromNode(inputSchema);
			eie.setToNode(nvn);
			graph.addEdge(eie);
			// To provider of other view or relation
			E eietp=(E) VisualEdgeFactory.create();
			eietp.setName("map-select");
			eietp.setType(EdgeType.EDGE_TYPE_MAPPING);
			eietp.setFromNode(nvn);
			eietp.setToNode(epilegmenosKombos);
			graph.addEdge(eietp);
		}
		tempParam=apantisi;
		apantisi=apantisi.substring(apantisi.indexOf(".")+1);
		if(exeiGb!=null)
		{
			stw.stop();
			//int reply=JOptionPane.showConfirmDialog(null, "Should "+tempParam+" be used as grouper in group by of "+msg.toNode.getName()+" ?",tempParam,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			int reply=JOptionPane.NO_OPTION;
			stw.start();
			if(reply==JOptionPane.YES_OPTION)
			{
				E ese=(E) VisualEdgeFactory.create();
				ese.setName("group by "+(exeiGb.getOutEdges().size()+1));
				ese.setType(EdgeType.EDGE_TYPE_GROUP_BY);
				ese.setFromNode(exeiGb);
				ese.setToNode(nvn);
				graph.addEdge(ese);
			}
			else if(reply==JOptionPane.NO_OPTION)
			{
				String[] choices={"MIN","MAX","AVG","COUNT","SUM"};	// Aggregate functions.
				stw.stop();
				String apantisiF=null;//(String) JOptionPane.showInputDialog(null, "Select aggregate function (MIN, MAX, AVG, COUNT, SUM) that "+tempParam+" should be used at "+msg.toNode.getName()+"\nDefault value is: "+choices[0], tempParam, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				stw.start();
				if(apantisiF==null)
				{
					apantisiF=new String("COUNT");
				}
				V nvnout=(V) VisualNodeFactory.create();
				nvnout.setName(apantisiF);
				nvnout.setType(NodeType.NODE_TYPE_FUNCTION);
				graph.addVertex(nvnout);
				
				if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)
				{
					mr.iaQ++;
				}
				else
				{
					mr.iaV++;
				}
				
				E ebn=(E) VisualEdgeFactory.create();
				ebn.setName("map-select");
				ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
				ebn.setFromNode(nvnout);
				ebn.setToNode(nvn);
				graph.addEdge(ebn);
				if(yparxousesOnomasies.contains(nvn.getName())==false)
				{
					yparxousesOnomasies.add(nvn.getName());
				}
				nvn=nvnout;
			}
		}
		while(yparxousesOnomasies.contains(apantisi))
		{
			String forMessage=new String();
			for(int i=0;i<yparxousesOnomasies.size();i++)
			{
				forMessage+="\n"+yparxousesOnomasies.get(i);
			}
			stw.stop();
			//apantisi=JOptionPane.showInputDialog("Name: "+apantisi+" for node: "+tempParam+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
			apantisi=UUID.randomUUID().toString();
			stw.start();
			while(apantisi==null)
			{
				stw.stop();
				//apantisi=JOptionPane.showInputDialog("Name: "+apantisi+" for node: "+tempParam+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
				apantisi=UUID.randomUUID().toString();
				stw.start();
			}
		}
		tempParam=apantisi;
		V nvnout=(V) VisualNodeFactory.create();
		nvnout.setName(tempParam);
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
		eoe.setType(EdgeType.EDGE_TYPE_OUTPUT);
		eoe.setFromNode(outNode);
		eoe.setToNode(nvnout);
		graph.addEdge(eoe);
		E ebn=(E) VisualEdgeFactory.create();
		ebn.setName("map-select");
		ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
		ebn.setFromNode(nvnout);
		ebn.setToNode(nvn);
		graph.addEdge(ebn);
		return(nvnout.getName());
	}
}
