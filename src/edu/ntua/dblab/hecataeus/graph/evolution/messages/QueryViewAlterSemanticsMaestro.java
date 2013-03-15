package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.alterSemanticsDialog;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeFactory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFactory;
import edu.ntua.dblab.hecataeus.graph.evolution.MetriseisRewrite;

public class QueryViewAlterSemanticsMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge> extends MaestroAbstract<V,E>
{
	public QueryViewAlterSemanticsMaestro(PriorityQueue<Message<V,E>> q)
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
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				msg.toSchema=(V) msg.toNode.getOutEdges().get(i).getToNode();
			}
		}
		policyCheckAlgorithms.get(0).execute(msg, this.myGQueue);
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				msg.toNode.getOutEdges().get(i).setStatus(msg.toNode.getOutEdges().get(i).getToNode().getStatus(),false);
			}
		}
		setModuleStatus(msg);
	}
	
	String paterasKombos(V kombos)
	{
		for(int i=0;i<kombos.getInEdges().size();i++)
		{
			if(kombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				return(kombos.getInEdges().get(i).getFromNode().getName().substring(kombos.getInEdges().get(i).getFromNode().getName().indexOf("_IN_")+4)+"."+kombos.getName());
			}
		}
		return(null);
	}
	
	@SuppressWarnings("unchecked")
	String inOrderSmtxTraversal(V kombos)
	{
		V left=null;
		V right=null;
		String toRet=new String();
		if(kombos.getType()==NodeType.NODE_TYPE_ATTRIBUTE||kombos.getType()==NodeType.NODE_TYPE_CONSTANT)
		{
			if(kombos.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
			{
				return(paterasKombos(kombos));
			}
			try{
				Double.parseDouble(kombos.getName());
			}
			catch(Exception e)
			{
				return("\'"+kombos.getName()+"\'");
			}
			return(kombos.getName());
		}
		for(int i=0;i<kombos.getOutEdges().size();i++)
		{
			if(kombos.getOutEdges().get(i).getName().equals("op1"))
			{
				left=(V) kombos.getOutEdges().get(i).getToNode();
			}
			if(kombos.getOutEdges().get(i).getName().equals("op2"))
			{
				right=(V) kombos.getOutEdges().get(i).getToNode();
			}
		}
		toRet+=inOrderSmtxTraversal(left);
		toRet+=kombos.getName();
		if(right.getName().equals(" AND ")||right.getName().equals(" OR "))
		{
			toRet+=" ( "+inOrderSmtxTraversal(right)+" ) ";
		}
		else
		{
			toRet+=inOrderSmtxTraversal(right);
		}
		return(toRet);
	}
	
	@SuppressWarnings("unchecked")
	private V add_node(String Label, NodeType Type, EvolutionGraph<V,E> graph) {
		V v = (V) VisualNodeFactory.create();
		v.setName(Label);
		v.setType(Type);
		graph.addVertex(v);
		return v;
	}
	
	@SuppressWarnings("unchecked")
	private E add_edge(V u, V v, EdgeType Type, String Label, EvolutionGraph<V,E> graph) {
		E e = (E) VisualEdgeFactory.create();
		e.setName(Label);
		e.setType(Type);
		e.setFromNode(u);
		e.setToNode(v);
		graph.addEdge(e);
		return  e ;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private V existsInInputSchema(V head, String tableName, String nodeName, EvolutionGraph<V, E> graph)
	{
		V x=null;
		E e=null;
		for(int i=0;i<head.getOutEdges().size();i++)
		{
			if(head.getOutEdges().get(i).getToNode().getName().equals(head.getName()+"_IN_"+tableName))
			{
				V in=(V) head.getOutEdges().get(i).getToNode();
				for(int j=0;j<in.getOutEdges().size();j++)
				{
					if(in.getOutEdges().get(j).getToNode().getName().equals(nodeName))
					{
						return (V) (in.getOutEdges().get(j).getToNode());
					}
				}
				x=add_node(nodeName, NodeType.NODE_TYPE_ATTRIBUTE, graph);
				e=add_edge(in, x, EdgeType.EDGE_TYPE_INPUT, "S", graph);
				for(int j=0;j<in.getOutEdges().size();j++)
				{
					if(in.getOutEdges().get(j).getName().equals("from"))
					{
						for(int k=0;k<in.getOutEdges().get(j).getToNode().getOutEdges().size();k++)
						{
							if(in.getOutEdges().get(j).getToNode().getOutEdges().get(k).getToNode().getName().equals(nodeName))
							{
								e=add_edge(x, (V) in.getOutEdges().get(j).getToNode().getOutEdges().get(k).getToNode(), EdgeType.EDGE_TYPE_MAPPING, "map-select", graph);
								break;
							}
						}
					}
				}
				break;
			}
		}
		return(x);
	}
	
	String getTableName(String expr)
	{
		return(expr.substring(0,expr.indexOf(".")).trim());
	}
	
	String getColumnName(String expr)
	{
		return(expr.substring(expr.indexOf(".")+1).trim());
	}
	
	@SuppressWarnings("unchecked")
	private V varNode(String expr, EvolutionGraph<V,E> graph, V kombosSmtx)
	{
		V lc=null;
		try
		{
			Double.parseDouble(expr);
			lc=add_node(expr.trim(), NodeType.NODE_TYPE_CONSTANT, graph);
		}
		catch (Exception e)
		{
			lc=existsInInputSchema((V) kombosSmtx.getInEdges().get(0).getFromNode(), getTableName(expr), getColumnName(expr), graph);
		}
		return(lc);
	}
	
	private V add_math_expression(String expr, EvolutionGraph<V,E>graph, V head, V kombosSmtx)
	{
		String plus = new String(" ");
		String minus = new String(" ");
		String multiply = new String(" ");
		String devide = new String(" ");
		int fromIndex=0;
		int fromIndex2=0;
		while((fromIndex=expr.indexOf(" + ", fromIndex2))!=-1)
		{
			plus+=fromIndex+" ";
			fromIndex2=fromIndex+1;
		}
		fromIndex=0;
		fromIndex2=0;
		while((fromIndex=expr.indexOf(" - ", fromIndex2))!=-1)
		{
			minus+=fromIndex+" ";
			fromIndex2=fromIndex+1;
		}
		fromIndex=0;
		fromIndex2=0;
		while((fromIndex=expr.indexOf(" * ", fromIndex2))!=-1)
		{
			multiply+=fromIndex+" ";
			fromIndex2=fromIndex+1;
		}
		fromIndex=0;
		fromIndex2=0;
		while((fromIndex=expr.indexOf(" / ", fromIndex2))!=-1)
		{
			devide+=fromIndex+" ";
			fromIndex2=fromIndex+1;
		}
		String[] arithmeticSigns=plus.concat(minus).concat(multiply).concat(devide).trim().split(" ");
		for(int i=0;i<arithmeticSigns.length;i++)
		{
			if(plus.contains(" "+arithmeticSigns[i]+" "))
			{
				arithmeticSigns[i]=" + ";
				continue;
			}
			if(minus.contains(" "+arithmeticSigns[i]+" "))
			{
				arithmeticSigns[i]=" - ";
				continue;
			}
			if(multiply.contains(" "+arithmeticSigns[i]+" "))
			{
				arithmeticSigns[i]=" * ";
				continue;
			}
			if(devide.contains(" "+arithmeticSigns[i]+" "))
			{
				arithmeticSigns[i]=" / ";
				continue;
			}
		}
		String[] tokens=expr.split("[+|-|*|/]", 2);
		V h=head;
		int i=0;
		while(tokens.length>1)
		{
			V nh=add_node(arithmeticSigns[i++], NodeType.NODE_TYPE_OPERAND, graph);
			if(i==1)
			{
				head=nh;
			}
			V b=varNode(tokens[0], graph, kombosSmtx);
			add_edge(nh, b, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
			add_edge(h, nh, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
			h=nh;
			tokens=tokens[1].split("[+|-|*|/]", 2);
		}
		if(tokens.length==1)
		{
			V nh=varNode(tokens[0], graph, kombosSmtx);
			if(i==0)
			{
				head=nh;
			}
			add_edge(h, nh, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
		}
		return(head);
	}
	
	@SuppressWarnings("unchecked")
	private V add_expression(String expr, EvolutionGraph<V,E> graph , V kombosSmtx)
	{
		V v=null;
		if(expr.trim().isEmpty())
		{
			return(v);
		}
		if(expr.contains("="))
		{
			v=add_node(" = ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		if(expr.contains(">"))
		{
			v=add_node(" > ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		if(expr.contains("<"))
		{
			v=add_node(" < ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		if(expr.contains("<="))
		{
			v=add_node(" <= ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		if(expr.contains(">="))
		{
			v=add_node(" >= ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		if(expr.contains("<>"))
		{
			v=add_node(" <> ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		if(expr.contains("!="))
		{
			v=add_node(" != ", NodeType.NODE_TYPE_OPERAND, graph);
		}
		String[] values=expr.split("=|<|>|<>|<=|>=|!=");
		V lc=null;
		try
		{
			Double.parseDouble(values[0]);
			lc=add_node(values[0], NodeType.NODE_TYPE_CONSTANT, graph);
		}
		catch (Exception e)
		{
			if(values[0].contains("'"))
			{
				lc=add_node(values[0].trim().replace("'",""), NodeType.NODE_TYPE_CONSTANT, graph);
			}
			else if(values[0].contains(" + ")||values[0].contains(" - ")||values[0].contains(" * ")||values[0].contains(" / "))
			{
				lc=add_math_expression(values[0], graph, v, kombosSmtx);
			}
			else
			{
				lc=existsInInputSchema((V) kombosSmtx.getInEdges().get(0).getFromNode(), getTableName(values[0]), getColumnName(values[0]), graph);
			}
		}
		add_edge(v, lc, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
		V rc=null;
		try
		{
			Double.parseDouble(values[values.length-1]);
			rc=add_node(values[values.length-1], NodeType.NODE_TYPE_CONSTANT, graph);
		}
		catch(Exception e)
		{
			if(values[values.length-1].contains("'"))
			{
				rc=add_node(values[1].trim().replace("'",""), NodeType.NODE_TYPE_CONSTANT, graph);
			}
			else if(values[values.length-1].contains(" + ")||values[values.length-1].contains(" - ")||values[values.length-1].contains(" * ")||values[values.length-1].contains(" / "))
			{
				rc=add_math_expression(values[values.length-1], graph, v, kombosSmtx);
			}
			else
			{
				rc=existsInInputSchema((V) kombosSmtx.getInEdges().get(0).getFromNode(), getTableName(values[values.length-1]), getColumnName(values[values.length-1]), graph);
			}
		}
		add_edge(v, rc, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
		return(v);
	}
	
	private V addAndOrExpression(String expr, EvolutionGraph<V, E> graph, V head, V smtxNode)
	{
		if(expr==null||expr.trim().isEmpty())
		{
			return(null);
		}
		String and = new String(" ");
		String or = new String(" ");
		int fromIndex=0;
		int fromIndex2=0;
		while((fromIndex=expr.indexOf(" AND ", fromIndex2))!=-1)
		{
			and+=fromIndex+" ";
			fromIndex2=fromIndex+1;
		}
		fromIndex=0;
		fromIndex2=0;
		while((fromIndex=expr.indexOf(" OR ", fromIndex2))!=-1)
		{
			or+=fromIndex+" ";
			fromIndex2=fromIndex+1;
		}
		String[] andsOrs=and.trim().concat(or).trim().split(" ");
		for(int i=0;i<andsOrs.length;i++)
		{
			if(and.contains(" "+andsOrs[i]+" "))
			{
				andsOrs[i]=" AND ";
				continue;
			}
			if(or.contains(" "+andsOrs[i]+" "))
			{
				andsOrs[i]=" OR ";
				continue;
			}
		}
		String[] tokens=expr.concat(" ").split(" AND | OR ", 2);
		V h=head;
		int i=0;
		V nc=null;
		V pc=null;
		while(tokens.length>1)
		{
			V b=add_expression(tokens[0], graph, smtxNode);
			if(b!=null)
			{
				nc=add_node(andsOrs[i], NodeType.NODE_TYPE_OPERAND, graph);
				i++;
				if(pc!=null)
				{
					add_edge(pc, b, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
					add_edge(nc, pc, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
				}
				else
				{
					add_edge(nc, b, EdgeType.EDGE_TYPE_OPERATOR, "op1", graph);
				}
				pc=nc;
			}
			tokens=tokens[1].concat(" ").split(" AND | OR ", 2);
		}
		if(tokens.length==1)
		{
			nc=add_expression(tokens[0], graph, smtxNode);
			if(pc!=null)
			{
				add_edge(pc, nc, EdgeType.EDGE_TYPE_OPERATOR, "op2", graph);
				add_edge(head, pc, EdgeType.EDGE_TYPE_WHERE, "where", graph);
			}
			else if(nc!=null)
			{
				add_edge(head, nc, EdgeType.EDGE_TYPE_WHERE, "where", graph);
			}
		}
		return(head);
	}
	
	@SuppressWarnings("unchecked")
	private void addGroupByNodes(V head, String grbyClause, EvolutionGraph<V,E> graph, StopWatch stw)
	{
		if(grbyClause==null||grbyClause.trim().isEmpty())
		{
			return;
		}
		V gbh=add_node("GB", NodeType.NODE_TYPE_GROUP_BY, graph);
		add_edge(head, gbh, EdgeType.EDGE_TYPE_GROUP_BY, "group by", graph);
		String[] gbs=grbyClause.split(",");
		for(int i=0;i<gbs.length;i++)
		{
			add_edge(gbh, existsInInputSchema((V) head.getInEdges().get(0).getFromNode(), getTableName(gbs[i]), getColumnName(gbs[i]), graph), EdgeType.EDGE_TYPE_GROUP_BY, "group by"+(i+1), graph);
		}
		// TODO: check if output is okay with new group by clause!!!
		V moduleNode=(V) head.getInEdges().get(0).getFromNode();
		for(int i=0;i<moduleNode.getOutEdges().size();i++)
		{
			if(moduleNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT)
			{
				V outNode=(V) moduleNode.getOutEdges().get(i).getToNode();
				for(int j=0;j<outNode.getOutEdges().size();j++)
				{	// This node should be an aggregate function.
					if(outNode.getOutEdges().get(j).getToNode().getType()==NodeType.NODE_TYPE_FUNCTION)
					{
						continue;
					}
					else
					{ // Go to one aggregate function OR go to a node that has a group by node.
						V prosElegxo=(V) outNode.getOutEdges().get(j).getToNode().getOutEdges().get(0).getToNode();
						if(prosElegxo.getType()==NodeType.NODE_TYPE_FUNCTION)
						{
							continue;
						}
						else
						{
							Boolean grouper=false;
							for(int k=0;k<prosElegxo.getInEdges().size();k++)
							{
								if(prosElegxo.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_GROUP_BY)
								{
									grouper=true;
									break;
								}
							}
							if(grouper==false)
							{	// No aggregate function, no raname as aggregate function, no grouper. Inform user and put it on groupers!
								stw.stop();
								//JOptionPane.showMessageDialog(null, prosElegxo.getName()+" is not a grouper and is in output schema of "+ moduleNode.getName()+"\nWe will force it to become a grouper!");
								stw.start();
								add_edge(gbh,prosElegxo, EdgeType.EDGE_TYPE_GROUP_BY, "group by"+(gbh.getOutEdges().size()+1), graph);
							}
						}
					}
				}
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	String doRewrite(Message<V,E> msg, String tempParam, EvolutionGraph<V,E> graph, StopWatch stw, MetriseisRewrite mr)
	{
		V smtxNode=null;
		for(int i=0;i<msg.toNode.getOutEdges().size();i++)
		{	// Find semantics node.
			if(msg.toNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				smtxNode=(V) msg.toNode.getOutEdges().get(i).getToNode();
				break;
			}
		}
		if(smtxNode==msg.toSchema)
		{	// User asked to change the semantics part.
			String alterSmtx=new String();
			String groupBy=null;
			for(int i=0;i<smtxNode.getOutEdges().size();i++)
			{
				if(smtxNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_WHERE)
				{
					alterSmtx+=inOrderSmtxTraversal((V) smtxNode.getOutEdges().get(i).getToNode());
					continue;
				}
				if(smtxNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_GROUP_BY)
				{
					groupBy=new String();
					for(int j=0;j<smtxNode.getOutEdges().get(i).getToNode().getOutEdges().size();j++)
					{
						if(smtxNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getName().equals("group by"+(j+1))||smtxNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getName().equals("group by "+(j+1)))
						{
							if(j>0)
							{
								groupBy+=",";
							}
							groupBy+=" "+paterasKombos((V) smtxNode.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode());
						}
					}
				}
				continue;
			}
			for(int i=0;i<smtxNode.getOutEdges().size();i++)
			{
				if(smtxNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_WHERE)
				{
					treeCleaner((V) smtxNode.getOutEdges().get(i).getToNode(),graph);
					break;
				}
			}
			for(int i=0;i<smtxNode.getOutEdges().size();i++)
			{
				if(smtxNode.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_GROUP_BY)
				{	// What happens with group by edges?
					graph.removeVertex((V) smtxNode.getOutEdges().get(i).getToNode());
					break;
				}
			}
			int diagrafes=0;
			for(int i=0;i<smtxNode.getOutEdges().size()-diagrafes;i++)
			{
				if(smtxNode.getOutEdges().get(i).getToNode().getInEdges().size()==1)
				{
					graph.removeVertex((V) smtxNode.getOutEdges().get(i).getToNode());
					i=0;
					diagrafes++;
				}
			}
			
			List<V> prosDiagrafi=new LinkedList<V>();	// Remove unneeded from IN
			for(int k=0;k<msg.toNode.getOutEdges().size();k++)
			{
				if(msg.toNode.getOutEdges().get(k).getType()==EdgeType.EDGE_TYPE_INPUT)
				{
					V inNode=(V) msg.toNode.getOutEdges().get(k).getToNode();
					for(int j=0;j<inNode.getOutEdges().size();j++)
					{
						if(inNode.getOutEdges().get(j).getToNode().getInEdges().size()==1)
						{	// Output does not need this attr of input schema.
							prosDiagrafi.add((V) inNode.getOutEdges().get(j).getToNode());
						}
					}
				}
			}
			for(int k=0;k<prosDiagrafi.size();k++)
			{
				graph.removeVertex(prosDiagrafi.get(k));
			}
			stw.stop();
			//alterSemanticsDialog asd=new alterSemanticsDialog(alterSmtx, groupBy, msg.toNode.getName());
			//asd.setSize(500, 500);
			//asd.setModal(true);
			//asd.setVisible(true);
			stw.start();
			String new_whereClause="";//asd.getWhere().toUpperCase();
			String new_groupClause="";//asd.getGroupby().toUpperCase();
			V whereClause=msg.toSchema;
			V smtx=addAndOrExpression(new_whereClause, graph, whereClause, msg.toSchema);	// Reconstruct smtx tree.
			if(smtx!=null)
			{
				for(int k=0;k<smtx.getInEdges().size();k++)
				{
					if(smtx.getInEdges().get(k).getFromNode().equals(msg.toSchema))
					{
						smtx.getInEdges().get(k).setType(EdgeType.EDGE_TYPE_WHERE);
						smtx.getInEdges().get(k).setName("where");
					}
				}
			}
			addGroupByNodes(msg.toSchema, new_groupClause, graph, stw);	// Reconstruct group by.
		}
		if(msg.toNode.getType()==NodeType.NODE_TYPE_QUERY)	/** I don't know how to count! Just add one. */
		{
			mr.iaQ++;
		}
		else
		{
			mr.iaV++;
		}
		return(tempParam);
	}
}
