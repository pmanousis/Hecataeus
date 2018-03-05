package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class QueryViewAddAttributeMaestro extends MaestroAbstract {
	public QueryViewAddAttributeMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new ASSSNO());
	}

	@Override
	String doRewrite(final Message msg,
		String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		final List<String> yparxousesOnomasies = new LinkedList<>();
		EvolutionNode outNode = null;
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT) {
				outNode = msg.toNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < outNode.getOutEdges().size(); j++)
					yparxousesOnomasies.add(outNode.getOutEdges().get(j).getToNode().getName());
				break;
			}
		final List<String> providerAttrs = new LinkedList<>();
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT) {
				final EvolutionNode inNode = msg.toNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < inNode.getOutEdges().size(); j++)
					if (inNode.getOutEdges().get(j).getType() == EdgeType.EDGE_TYPE_FROM) {
						final EvolutionNode provNode = inNode.getOutEdges().get(j).getToNode();
						for (int k = 0; k < provNode.getOutEdges().size(); k++)
							providerAttrs
								.add(inNode.getName() + "." + provNode.getOutEdges().get(k).getToNode().getName());
					}
			}
		stw.stop();
		//String apantisi=(String) JOptionPane.showInputDialog(null, "Select attribute for output at "+msg.toNode.getName(), msg.toNode.getName(), JOptionPane.QUESTION_MESSAGE, null, providerAttrs.toArray(), providerAttrs.toArray()[0]);
		final Random rg = new Random();
		String apantisi = (String) providerAttrs.toArray()[rg.nextInt() % providerAttrs.size()];

		stw.start();
		if (apantisi == null)
			apantisi = new String((String) providerAttrs.toArray()[0]);
		EvolutionNode inputSchema = null;
		EvolutionNode epilegmenosKombos = null; // New attribute for output
		EvolutionNode provKombos = null;
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT && msg.toNode
				.getOutEdges().get(i).getToNode().getName().equals(apantisi.substring(0, apantisi.indexOf(".")))) {
				final EvolutionNode inNode = msg.toNode.getOutEdges().get(i).getToNode();
				inputSchema = inNode;
				for (int j = 0; j < inNode.getOutEdges().size(); j++)
					if (inNode.getOutEdges().get(j).getType() == EdgeType.EDGE_TYPE_FROM) {
						provKombos = inNode.getOutEdges().get(j).getToNode();
						for (int k = 0; k < provKombos.getOutEdges().size(); k++)
							if (provKombos.getOutEdges().get(k).getToNode().getName().equals(
								apantisi.substring(apantisi.indexOf(".") + 1)))
								epilegmenosKombos = provKombos.getOutEdges().get(k).getToNode();
					}
			}
		// SMTX?
		EvolutionNode exeiGb = null;
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
				for (int j = 0; j < msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().size(); j++)
					if (msg.toNode
						.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode()
						.getType() == NodeType.NODE_TYPE_GROUP_BY)
						exeiGb = msg.toNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode();
				break;
			}
		// IN schema
		EvolutionNode nvn = null;
		for (int i = 0; i < inputSchema.getOutEdges().size(); i++)
			if (inputSchema.getOutEdges().get(i).getToNode().getName().equals(epilegmenosKombos.getName())) {// Allready @inputSchema
				nvn = inputSchema.getOutEdges().get(i).getToNode();
				break;
			}
		if (nvn == null) { // Not in input, I create it.
			nvn = new EvolutionNode();
			nvn.setName(epilegmenosKombos.getName());
			nvn.setType(NodeType.NODE_TYPE_ATTRIBUTE);
			graph.addVertex(nvn);

			if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
				mr.iaQ++;
			else
				mr.iaV++;

			final EvolutionEdge eie = new EvolutionEdge();
			eie.setName("S");
			eie.setType(EdgeType.EDGE_TYPE_INPUT);
			eie.setFromNode(inputSchema);
			eie.setToNode(nvn);
			graph.addEdge(eie);
			// To provider of other view or relation
			final EvolutionEdge eietp = new EvolutionEdge();
			eietp.setName("map-select");
			eietp.setType(EdgeType.EDGE_TYPE_MAPPING);
			eietp.setFromNode(nvn);
			eietp.setToNode(epilegmenosKombos);
			graph.addEdge(eietp);
		}
		tempParam = apantisi;
		apantisi = apantisi.substring(apantisi.indexOf(".") + 1);
		if (exeiGb != null) {
			stw.stop();
			//int reply=JOptionPane.showConfirmDialog(null, "Should "+tempParam+" be used as grouper in group by of "+msg.toNode.getName()+" ?",tempParam,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			final int reply = JOptionPane.NO_OPTION;
			stw.start();
			if (reply == JOptionPane.YES_OPTION) {
				final EvolutionEdge ese = new EvolutionEdge();
				ese.setName("group by " + (exeiGb.getOutEdges().size() + 1));
				ese.setType(EdgeType.EDGE_TYPE_GROUP_BY);
				ese.setFromNode(exeiGb);
				ese.setToNode(nvn);
				graph.addEdge(ese);
			} else if (reply == JOptionPane.NO_OPTION) {
				final String[] choices = { "MIN", "MAX", "AVG", "COUNT", "SUM" }; // Aggregate functions.
				stw.stop();
				String apantisiF = null;//(String) JOptionPane.showInputDialog(null, "Select aggregate function (MIN, MAX, AVG, COUNT, SUM) that "+tempParam+" should be used at "+msg.toNode.getName()+"\nDefault value is: "+choices[0], tempParam, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				stw.start();
				if (apantisiF == null)
					apantisiF = new String("COUNT");
				final EvolutionNode nvnout = new EvolutionNode();
				nvnout.setName(apantisiF);
				nvnout.setType(NodeType.NODE_TYPE_FUNCTION);
				graph.addVertex(nvnout);

				if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
					mr.iaQ++;
				else
					mr.iaV++;

				final EvolutionEdge ebn = new EvolutionEdge();
				ebn.setName("map-select");
				ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
				ebn.setFromNode(nvnout);
				ebn.setToNode(nvn);
				graph.addEdge(ebn);
				if (yparxousesOnomasies.contains(nvn.getName()) == false)
					yparxousesOnomasies.add(nvn.getName());
				nvn = nvnout;
			}
		}
		while (yparxousesOnomasies.contains(apantisi)) {
			String forMessage = new String();
			for (int i = 0; i < yparxousesOnomasies.size(); i++)
				forMessage += "\n" + yparxousesOnomasies.get(i);
			stw.stop();
			//apantisi=JOptionPane.showInputDialog("Name: "+apantisi+" for node: "+tempParam+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
			apantisi = UUID.randomUUID().toString();
			stw.start();
			while (apantisi == null) {
				stw.stop();
				//apantisi=JOptionPane.showInputDialog("Name: "+apantisi+" for node: "+tempParam+" is allready in your output schema, you should rename it without using one of the folowing names:"+forMessage);
				apantisi = UUID.randomUUID().toString();
				stw.start();
			}
		}
		tempParam = apantisi;
		final EvolutionNode nvnout = new EvolutionNode();
		nvnout.setName(tempParam);
		nvnout.setType(NodeType.NODE_TYPE_ATTRIBUTE);
		graph.addVertex(nvnout);

		if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
			mr.iaQ++;
		else
			mr.iaV++;

		final EvolutionEdge eoe = new EvolutionEdge();
		eoe.setName("S");
		eoe.setType(EdgeType.EDGE_TYPE_OUTPUT);
		eoe.setFromNode(outNode);
		eoe.setToNode(nvnout);
		graph.addEdge(eoe);
		final EvolutionEdge ebn = new EvolutionEdge();
		ebn.setName("map-select");
		ebn.setType(EdgeType.EDGE_TYPE_MAPPING);
		ebn.setFromNode(nvnout);
		ebn.setToNode(nvn);
		graph.addEdge(ebn);
		return nvnout.getName();
	}

	@Override
	void propagateMessage(final Message msg) {
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT)
				msg.toSchema = msg.toNode.getOutEdges().get(i).getToNode();
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		setModuleStatus(msg);
	}
}
