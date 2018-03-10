package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

public class QueryViewAlterSemanticsMaestro extends MaestroAbstract {
	public QueryViewAlterSemanticsMaestro(final PriorityQueue<Message> q) {
		super(q);
		policyCheckAlgorithms.add(new ASSSNO());
	}

	@Override
	String doRewrite(final Message msg,
		final String tempParam,
		final EvolutionGraph graph,
		final StopWatch stw,
		final MetriseisRewrite mr) {
		EvolutionNode smtxNode = null;
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
				smtxNode = msg.toNode.getOutEdges().get(i).getToNode();
				break;
			}
		if (smtxNode == msg.toSchema) { // User asked to change the semantics part.
			String groupBy = null;
			for (int i = 0; i < smtxNode.getOutEdges().size(); i++) {
				if (smtxNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_WHERE) {
					continue;
				}
				if (smtxNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_GROUP_BY) {
					groupBy = new String();
					for (int j = 0; j < smtxNode.getOutEdges().get(i).getToNode().getOutEdges().size(); j++)
						if (smtxNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getName().equals(
							"group by" + (j + 1)) ||
							smtxNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getName().equals(
								"group by " + (j + 1))) {
							if (j > 0)
								groupBy += ",";
							groupBy += " " + paterasKombos(
								smtxNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode());
						}
				}
				continue;
			}
			for (int i = 0; i < smtxNode.getOutEdges().size(); i++)
				if (smtxNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_WHERE) {
					treeCleaner(smtxNode.getOutEdges().get(i).getToNode(), graph);
					break;
				}
			for (int i = 0; i < smtxNode.getOutEdges().size(); i++)
				if (smtxNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_GROUP_BY) { // What happens with group by edges?
					graph.removeVertex(smtxNode.getOutEdges().get(i).getToNode());
					break;
				}
			int diagrafes = 0;
			for (int i = 0; i < smtxNode.getOutEdges().size() - diagrafes; i++)
				if (smtxNode.getOutEdges().get(i).getToNode().getInEdges().size() == 1) {
					graph.removeVertex(smtxNode.getOutEdges().get(i).getToNode());
					i = 0;
					diagrafes++;
				}

			final List<EvolutionNode> prosDiagrafi = new LinkedList<>(); // Remove unneeded from IN
			for (int k = 0; k < msg.toNode.getOutEdges().size(); k++)
				if (msg.toNode.getOutEdges().get(k).getType() == EdgeType.EDGE_TYPE_INPUT) {
					final EvolutionNode inNode = msg.toNode.getOutEdges().get(k).getToNode();
					for (int j = 0; j < inNode.getOutEdges().size(); j++)
						if (inNode.getOutEdges().get(j).getToNode().getInEdges().size() == 1)
							prosDiagrafi.add(inNode.getOutEdges().get(j).getToNode());
				}
			for (int k = 0; k < prosDiagrafi.size(); k++)
				graph.removeVertex(prosDiagrafi.get(k));
			stw.stop();
			//alterSemanticsDialog asd=new alterSemanticsDialog(alterSmtx, groupBy, msg.toNode.getName());
			//asd.setSize(500, 500);
			//asd.setModal(true);
			//asd.setVisible(true);
			stw.start();
			final String new_whereClause = "";//asd.getWhere().toUpperCase();
			final String new_groupClause = "";//asd.getGroupby().toUpperCase();
			final EvolutionNode whereClause = msg.toSchema;
			final EvolutionNode smtx = addAndOrExpression(new_whereClause, graph, whereClause, msg.toSchema); // Reconstruct smtx tree.
			if (smtx != null)
				for (int k = 0; k < smtx.getInEdges().size(); k++)
					if (smtx.getInEdges().get(k).getFromNode().equals(msg.toSchema)) {
						smtx.getInEdges().get(k).setType(EdgeType.EDGE_TYPE_WHERE);
						smtx.getInEdges().get(k).setName("where");
					}
			addGroupByNodes(msg.toSchema, new_groupClause, graph, stw); // Reconstruct group by.
		}
		if (msg.toNode.getType() == NodeType.NODE_TYPE_QUERY)
			mr.iaQ++;
		else
			mr.iaV++;
		return tempParam;
	}

	String getColumnName(final String expr) {
		return expr.substring(expr.indexOf(".") + 1).trim();
	}

	String getTableName(final String expr) {
		return expr.substring(0, expr.indexOf(".")).trim();
	}

	@SuppressWarnings("unchecked")
	String inOrderSmtxTraversal(final EvolutionNode kombos) {
		EvolutionNode left = null;
		EvolutionNode right = null;
		String toRet = new String();
		if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE || kombos.getType() == NodeType.NODE_TYPE_CONSTANT) {
			if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE)
				return paterasKombos(kombos);
			try {
				Double.parseDouble(kombos.getName());
			} catch (final Exception e) {
				return "\'" + kombos.getName() + "\'";
			}
			return kombos.getName();
		}
		for (int i = 0; i < kombos.getOutEdges().size(); i++) {
			if (kombos.getOutEdges().get(i).getName().equals("op1"))
				left = kombos.getOutEdges().get(i).getToNode();
			if (kombos.getOutEdges().get(i).getName().equals("op2"))
				right = kombos.getOutEdges().get(i).getToNode();
		}
		toRet += inOrderSmtxTraversal(left);
		toRet += kombos.getName();
		if (right.getName().equals(" AND ") || right.getName().equals(" OR "))
			toRet += " ( " + inOrderSmtxTraversal(right) + " ) ";
		else
			toRet += inOrderSmtxTraversal(right);
		return toRet;
	}

	String paterasKombos(final EvolutionNode kombos) {
		for (int i = 0; i < kombos.getInEdges().size(); i++)
			if (kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT)
				return kombos.getInEdges().get(i).getFromNode().getName().substring(
					kombos.getInEdges().get(i).getFromNode().getName().indexOf("_IN_") + 4) + "." + kombos.getName();
		return null;
	}

	@Override
	void propagateMessage(final Message msg) {
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
				msg.toSchema = msg.toNode.getOutEdges().get(i).getToNode();
		policyCheckAlgorithms.get(0).execute(msg, myGQueue);
		for (int i = 0; i < msg.toNode.getOutEdges().size(); i++)
			if (msg.toNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
				msg.toNode.getOutEdges().get(i).setStatus(msg.toNode.getOutEdges().get(i).getToNode().getStatus(),
					false);
		setModuleStatus(msg);
	}

	private EvolutionEdge add_edge(final EvolutionNode u,
		final EvolutionNode v,
		final EdgeType Type,
		final String Label,
		final EvolutionGraph graph) {
		final EvolutionEdge e = new EvolutionEdge();
		e.setName(Label);
		e.setType(Type);
		e.setFromNode(u);
		e.setToNode(v);
		graph.addEdge(e);
		return e;
	}

	private EvolutionNode add_expression(final String expr,
		final EvolutionGraph graph,
		final EvolutionNode kombosSmtx) {
		EvolutionNode v = null;
		if (expr.trim().isEmpty())
			return v;
		if (expr.contains("="))
			v = add_node(" = ", NodeType.NODE_TYPE_OPERAND, graph);
		if (expr.contains(">"))
			v = add_node(" > ", NodeType.NODE_TYPE_OPERAND, graph);
		if (expr.contains("<"))
			v = add_node(" < ", NodeType.NODE_TYPE_OPERAND, graph);
		if (expr.contains("<="))
			v = add_node(" <= ", NodeType.NODE_TYPE_OPERAND, graph);
		if (expr.contains(">="))
			v = add_node(" >= ", NodeType.NODE_TYPE_OPERAND, graph);
		if (expr.contains("<>"))
			v = add_node(" <> ", NodeType.NODE_TYPE_OPERAND, graph);
		if (expr.contains("!="))
			v = add_node(" != ", NodeType.NODE_TYPE_OPERAND, graph);
		final String[] values = expr.split("=|<|>|<>|<=|>=|!=");
		EvolutionNode lc = null;
		try {
			Double.parseDouble(values[0]);
			lc = add_node(values[0], NodeType.NODE_TYPE_CONSTANT, graph);
		} catch (final Exception e) {
			if (values[0].contains("'"))
				lc = add_node(values[0].trim().replace("'", ""), NodeType.NODE_TYPE_CONSTANT, graph);
			else if (values[0].contains(" + ") || values[0].contains(" - ") || values[0].contains(" * ") ||
				values[0].contains(" / "))
				lc = add_math_expression(values[0], graph, v, kombosSmtx);
			else
				lc = existsInInputSchema(kombosSmtx.getInEdges().get(0).getFromNode(), getTableName(values[0]),
					getColumnName(values[0]), graph);
		}
		add_edge(v, lc, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
		EvolutionNode rc = null;
		try {
			Double.parseDouble(values[values.length - 1]);
			rc = add_node(values[values.length - 1], NodeType.NODE_TYPE_CONSTANT, graph);
		} catch (final Exception e) {
			if (values[values.length - 1].contains("'"))
				rc = add_node(values[1].trim().replace("'", ""), NodeType.NODE_TYPE_CONSTANT, graph);
			else if (values[values.length - 1].contains(" + ") || values[values.length - 1].contains(" - ") ||
				values[values.length - 1].contains(" * ") || values[values.length - 1].contains(" / "))
				rc = add_math_expression(values[values.length - 1], graph, v, kombosSmtx);
			else
				rc = existsInInputSchema(kombosSmtx.getInEdges().get(0).getFromNode(),
					getTableName(values[values.length - 1]), getColumnName(values[values.length - 1]), graph);
		}
		add_edge(v, rc, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
		return v;
	}

	private EvolutionNode add_math_expression(final String expr,
		final EvolutionGraph graph,
		EvolutionNode head,
		final EvolutionNode kombosSmtx) {
		String plus = new String(" ");
		String minus = new String(" ");
		String multiply = new String(" ");
		String devide = new String(" ");
		int fromIndex = 0;
		int fromIndex2 = 0;
		while ((fromIndex = expr.indexOf(" + ", fromIndex2)) != -1) {
			plus += fromIndex + " ";
			fromIndex2 = fromIndex + 1;
		}
		fromIndex = 0;
		fromIndex2 = 0;
		while ((fromIndex = expr.indexOf(" - ", fromIndex2)) != -1) {
			minus += fromIndex + " ";
			fromIndex2 = fromIndex + 1;
		}
		fromIndex = 0;
		fromIndex2 = 0;
		while ((fromIndex = expr.indexOf(" * ", fromIndex2)) != -1) {
			multiply += fromIndex + " ";
			fromIndex2 = fromIndex + 1;
		}
		fromIndex = 0;
		fromIndex2 = 0;
		while ((fromIndex = expr.indexOf(" / ", fromIndex2)) != -1) {
			devide += fromIndex + " ";
			fromIndex2 = fromIndex + 1;
		}
		final String[] arithmeticSigns = plus.concat(minus).concat(multiply).concat(devide).trim().split(" ");
		for (int i = 0; i < arithmeticSigns.length; i++) {
			if (plus.contains(" " + arithmeticSigns[i] + " ")) {
				arithmeticSigns[i] = " + ";
				continue;
			}
			if (minus.contains(" " + arithmeticSigns[i] + " ")) {
				arithmeticSigns[i] = " - ";
				continue;
			}
			if (multiply.contains(" " + arithmeticSigns[i] + " ")) {
				arithmeticSigns[i] = " * ";
				continue;
			}
			if (devide.contains(" " + arithmeticSigns[i] + " ")) {
				arithmeticSigns[i] = " / ";
				continue;
			}
		}
		String[] tokens = expr.split("[+|-|*|/]", 2);
		EvolutionNode h = head;
		int i = 0;
		while (tokens.length > 1) {
			final EvolutionNode nh = add_node(arithmeticSigns[i++], NodeType.NODE_TYPE_OPERAND, graph);
			if (i == 1)
				head = nh;
			final EvolutionNode b = varNode(tokens[0], graph, kombosSmtx);
			add_edge(nh, b, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
			add_edge(h, nh, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
			h = nh;
			tokens = tokens[1].split("[+|-|*|/]", 2);
		}
		if (tokens.length == 1) {
			final EvolutionNode nh = varNode(tokens[0], graph, kombosSmtx);
			if (i == 0)
				head = nh;
			add_edge(h, nh, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
		}
		return head;
	}

	private EvolutionNode add_node(final String Label, final NodeType Type, final EvolutionGraph graph) {
		final EvolutionNode v = new EvolutionNode();
		v.setName(Label);
		v.setType(Type);
		graph.addVertex(v);
		return v;
	}

	private EvolutionNode addAndOrExpression(final String expr,
		final EvolutionGraph graph,
		final EvolutionNode head,
		final EvolutionNode smtxNode) {
		if (expr == null || expr.trim().isEmpty())
			return null;
		String and = new String(" ");
		String or = new String(" ");
		int fromIndex = 0;
		int fromIndex2 = 0;
		while ((fromIndex = expr.indexOf(" AND ", fromIndex2)) != -1) {
			and += fromIndex + " ";
			fromIndex2 = fromIndex + 1;
		}
		fromIndex = 0;
		fromIndex2 = 0;
		while ((fromIndex = expr.indexOf(" OR ", fromIndex2)) != -1) {
			or += fromIndex + " ";
			fromIndex2 = fromIndex + 1;
		}
		final String[] andsOrs = and.trim().concat(or).trim().split(" ");
		for (int i = 0; i < andsOrs.length; i++) {
			if (and.contains(" " + andsOrs[i] + " ")) {
				andsOrs[i] = " AND ";
				continue;
			}
			if (or.contains(" " + andsOrs[i] + " ")) {
				andsOrs[i] = " OR ";
				continue;
			}
		}
		String[] tokens = expr.concat(" ").split(" AND | OR ", 2);
		int i = 0;
		EvolutionNode nc = null;
		EvolutionNode pc = null;
		while (tokens.length > 1) {
			final EvolutionNode b = add_expression(tokens[0], graph, smtxNode);
			if (b != null) {
				nc = add_node(andsOrs[i], NodeType.NODE_TYPE_OPERAND, graph);
				i++;
				if (pc != null) {
					add_edge(pc, b, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
					add_edge(nc, pc, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
				} else
					add_edge(nc, b, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
				pc = nc;
			}
			tokens = tokens[1].concat(" ").split(" AND | OR ", 2);
		}
		if (tokens.length == 1) {
			nc = add_expression(tokens[0], graph, smtxNode);
			if (pc != null) {
				add_edge(pc, nc, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
				add_edge(head, pc, EdgeType.EDGE_TYPE_WHERE, "where", graph);
			} else if (nc != null)
				add_edge(head, nc, EdgeType.EDGE_TYPE_WHERE, "where", graph);
		}
		return head;
	}

	private void addGroupByNodes(final EvolutionNode head,
		final String grbyClause,
		final EvolutionGraph graph,
		final StopWatch stw) {
		if (grbyClause == null || grbyClause.trim().isEmpty())
			return;
		final EvolutionNode gbh = add_node("GB", NodeType.NODE_TYPE_GROUP_BY, graph);
		add_edge(head, gbh, EdgeType.EDGE_TYPE_GROUP_BY, "group by", graph);
		final String[] gbs = grbyClause.split(",");
		for (int i = 0; i < gbs.length; i++)
			add_edge(gbh, existsInInputSchema(head.getInEdges().get(0).getFromNode(), getTableName(gbs[i]),
				getColumnName(gbs[i]), graph), EdgeType.EDGE_TYPE_GROUP_BY, "group by" + (i + 1), graph);
		// TODO: check if output is okay with new group by clause!!!
		final EvolutionNode moduleNode = head.getInEdges().get(0).getFromNode();
		for (int i = 0; i < moduleNode.getOutEdges().size(); i++)
			if (moduleNode.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT) {
				final EvolutionNode outNode = moduleNode.getOutEdges().get(i).getToNode();
				for (int j = 0; j < outNode.getOutEdges().size(); j++)
					if (outNode.getOutEdges().get(j).getToNode().getType() == NodeType.NODE_TYPE_FUNCTION)
						continue;
					else { // Go to one aggregate function OR go to a node that has a group by node.
						final EvolutionNode prosElegxo =
							outNode.getOutEdges().get(j).getToNode().getOutEdges().get(0).getToNode();
						if (prosElegxo.getType() == NodeType.NODE_TYPE_FUNCTION)
							continue;
						else {
							Boolean grouper = false;
							for (int k = 0; k < prosElegxo.getInEdges().size(); k++)
								if (prosElegxo.getInEdges().get(k).getType() == EdgeType.EDGE_TYPE_GROUP_BY) {
									grouper = true;
									break;
								}
							if (grouper == false) { // No aggregate function, no raname as aggregate function, no grouper. Inform user and put it on groupers!
								stw.stop();
								//JOptionPane.showMessageDialog(null, prosElegxo.getName()+" is not a grouper and is in output schema of "+ moduleNode.getName()+"\nWe will force it to become a grouper!");
								stw.start();
								add_edge(gbh, prosElegxo, EdgeType.EDGE_TYPE_GROUP_BY,
									"group by" + (gbh.getOutEdges().size() + 1), graph);
							}
						}
					}
				break;
			}
	}

	private EvolutionNode existsInInputSchema(final EvolutionNode head,
		final String tableName,
		final String nodeName,
		final EvolutionGraph graph) {
		EvolutionNode x = null;
		EvolutionEdge e = null;
		for (int i = 0; i < head.getOutEdges().size(); i++)
			if (head.getOutEdges().get(i).getToNode().getName().equals(head.getName() + "_IN_" + tableName)) {
				final EvolutionNode in = head.getOutEdges().get(i).getToNode();
				for (int j = 0; j < in.getOutEdges().size(); j++)
					if (in.getOutEdges().get(j).getToNode().getName().equals(nodeName))
						return in.getOutEdges().get(j).getToNode();
				x = add_node(nodeName, NodeType.NODE_TYPE_ATTRIBUTE, graph);
				e = add_edge(in, x, EdgeType.EDGE_TYPE_INPUT, "S", graph);
				for (int j = 0; j < in.getOutEdges().size(); j++)
					if (in.getOutEdges().get(j).getName().equals("from"))
						for (int k = 0; k < in.getOutEdges().get(j).getToNode().getOutEdges().size(); k++)
							if (in.getOutEdges().get(j).getToNode().getOutEdges().get(k).getToNode().getName().equals(
								nodeName)) {
								e = add_edge(x, in.getOutEdges().get(j).getToNode().getOutEdges().get(k).getToNode(),
									EdgeType.EDGE_TYPE_MAPPING, "map-select", graph);
								break;
							}
				break;
			}
		return x;
	}

	@SuppressWarnings("unchecked")
	private EvolutionNode varNode(final String expr, final EvolutionGraph graph, final EvolutionNode kombosSmtx) {
		EvolutionNode lc = null;
		try {
			Double.parseDouble(expr);
			lc = add_node(expr.trim(), NodeType.NODE_TYPE_CONSTANT, graph);
		} catch (final Exception e) {
			lc = existsInInputSchema(kombosSmtx.getInEdges().get(0).getFromNode(), getTableName(expr),
				getColumnName(expr), graph);
		}
		return lc;
	}
}
