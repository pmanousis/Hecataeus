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
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class QueryViewAddAttributeProviderMaestro extends MaestroAbstract {

	public QueryViewAddAttributeProviderMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new ASSS());
		policyCheckAlgorithms.add(new ASSSNO());
		policyCheckAlgorithms.add(new ASSSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) { // OUT schema (if already existing name there ask for new name, else make new nodes)
		EvolutionNode outNode = null;
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT) {
				outNode = msg.toNode.getOutEdges().get(i).getToNode();
				break;
			}

		// IN schema
		final EvolutionNode nvn = new EvolutionNode();
		nvn.setName(tempParam);
		nvn.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvn);
		final EvolutionEdge eie = new EvolutionEdge();
		eie.setName("S");
		eie.setType(EdgeType.EDGE_TYPE_INPUT);
		eie.setFromNode(msg.toSchema);
		eie.setToNode(nvn);
		graph.addEdge(eie);
		// To provider of other view or relation
		final EvolutionEdge eietp = new EvolutionEdge();
		eietp.setName("map-select");
		eietp.setType(EdgeType.EDGE_TYPE_MAPPING);
		eietp.setFromNode(nvn);
		for (int i = 0; i < msg.toSchema.getOutEdges().size(); i++)
			if (msg.toSchema.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_FROM) {
				final EvolutionNode prov = msg.toSchema.getOutEdges().get(i).getToNode();
				for (int j = 0; j < prov.getOutEdges().size(); j++)
					if (prov.getOutEdges().get(j).getToNode().getName().equals(tempParam)) {
						eietp.setToNode(prov.getOutEdges().get(j).getToNode());
						break;
					}
			}
		graph.addEdge(eietp);
		// Smtx
		EvolutionNode smtxNode = null;
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
				smtxNode = msg.toNode.getOutEdges().get(i).getToNode();
				break;
			}
		for (int i = 0; i < smtxNode.getOutEdges().size(); i++)
			if (smtxNode.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_GROUP_BY) {
				stw.stop();
				//int reply=JOptionPane.showConfirmDialog(null, "Should "+tempParam+" be used as grouper in group by of "+msg.toNode.getName()+" ?",tempParam,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				final int reply = JOptionPane.NO_OPTION;
				stw.start();
				if (reply == JOptionPane.YES_OPTION) {
					final EvolutionEdge ese = new EvolutionEdge();
					ese.setName("group by " + (smtxNode.getOutEdges().get(i).getToNode().getOutEdges().size() + 1));
					ese.setType(EdgeType.EDGE_TYPE_GROUP_BY);
					ese.setFromNode(smtxNode.getOutEdges().get(i).getToNode());
					ese.setToNode(nvn);
					graph.addEdge(ese);
				} else if (reply == JOptionPane.NO_OPTION) {
					final String[] choices = { "MIN", "MAX", "AVG", "COUNT", "SUM" }; // Aggregate functions.
					stw.stop();
					String apantisi = null;//(String) JOptionPane.showInputDialog(null, "Select aggregate function (MIN, MAX, AVG, COUNT, SUM) that "+tempParam+" should be used at "+msg.toNode.getName()+"\nDefault value is: "+choices[0], tempParam, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					stw.start();
					if (apantisi == null)
						apantisi = new String("COUNT");

					final EvolutionNode nvnouto = new EvolutionNode();
					nvnouto.setName(tempParam);
					nvnouto.setType(NodeType.NODE_TYPE_ATTRIBUTE);
					graph.addVertex(nvnouto);

					if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
						mr.iaQ++;
					else
						mr.iaV++;

					final EvolutionEdge eoeo = new EvolutionEdge();
					eoeo.setName("S");
					eoeo.setType(EdgeType.EDGE_TYPE_SCHEMA);
					eoeo.setFromNode(outNode);
					eoeo.setToNode(nvnouto);
					graph.addEdge(eoeo);

					final EvolutionNode nvnoutf = new EvolutionNode();
					nvnoutf.setName(apantisi);
					nvnoutf.setType(NodeType.NODE_TYPE_FUNCTION);
					graph.addVertex(nvnoutf);

					if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
						mr.iaQ++;
					else
						mr.iaV++;

					final EvolutionEdge eoe = new EvolutionEdge();
					eoe.setName("map-select");
					eoe.setType(EdgeType.EDGE_TYPE_MAPPING);
					eoe.setFromNode(nvnouto);
					eoe.setToNode(nvnoutf);
					graph.addEdge(eoe);

					final EvolutionEdge eos = new EvolutionEdge();
					eos.setName("S");
					eos.setType(EdgeType.EDGE_TYPE_SEMANTICS);
					eos.setFromNode(smtxNode);
					eos.setToNode(nvnoutf);
					graph.addEdge(eos);

					final EvolutionEdge ebn = new EvolutionEdge();
					ebn.setName("map-select");
					ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
					ebn.setFromNode(nvnoutf);
					ebn.setToNode(nvn);
					graph.addEdge(ebn);
					return nvnouto.getName();
				}
			}

		String apantisi = tempParam;
		for (int i = 0; i < outNode.getOutEdges().size(); i++)
			if (outNode.getOutEdges().get(i).getToNode().getName().equals(tempParam)) { // Ask for new name? select x, x as x1?
				final List<String> yparxousesOnomasies = new LinkedList<>();
				for (int k = 0; k < outNode.getOutEdges().size(); k++)
					yparxousesOnomasies.add(outNode.getOutEdges().get(k).getToNode().getName());
				while (yparxousesOnomasies.contains(apantisi)) {
					String forMessage = new String();
					for (int i1 = 0; i1 < yparxousesOnomasies.size(); i1++)
						forMessage += "\n" + yparxousesOnomasies.get(i1);
					stw.stop();
					//apantisi=JOptionPane.showInputDialog("Name: "+tempParam+" for node: "+msg.toNode.getName()+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
					apantisi = UUID.randomUUID().toString();
					stw.start();
					while (apantisi == null) {
						stw.stop();
						//tempParam=JOptionPane.showInputDialog("Name: "+tempParam+" for node: "+msg.toNode.getName()+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
						apantisi = UUID.randomUUID().toString();
						stw.start();
					}
				}
			}

		final EvolutionNode nvnout = new EvolutionNode();
		nvnout.setName(apantisi);
		nvnout.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvnout);

		if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
			mr.iaQ++;
		else
			mr.iaV++;

		final EvolutionEdge eoe = new EvolutionEdge();
		eoe.setName("S");
		eoe.setType(EdgeType.EDGE_TYPE_SCHEMA);
		eoe.setFromNode(outNode);
		eoe.setToNode(nvnout);
		graph.addEdge(eoe);

		final EvolutionEdge ebn = new EvolutionEdge();
		ebn.setName("map-select");
		ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
		ebn.setFromNode(nvnout);
		ebn.setToNode(nvn);
		graph.addEdge(ebn);

		return tempParam;
	}

	@Override
	void propagateMessage(final Message msg) {
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		final EventType evt = msg.event;
		msg.event = EventType.ALTER_SEMANTICS;
		goToSmtxNode(msg);
		for (int i = 0; i < msg.toSchema.getOutEdges().size(); i++)
			if (msg.toSchema.getOutEdges().get(i).getToNode().getType() == NodeType.NODE_TYPE_GROUP_BY)
				policyCheckAlgorithms.get(1).execute(msg, myGQueue);
		msg.event = evt;
		goToOutputNode(msg);
		policyCheckAlgorithms.get(2).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
