package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeFactory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFactory;

public class ModuleNode<V extends EvolutionNode<E>, E extends EvolutionEdge>
{
	public V en;
	public int neededRewrites;
	public PriorityQueue<Message<V, E>> messages;
	List<ModuleNode<V, E>> emeis;
	EventType arxikoEvent;
	boolean marked;

	public ModuleNode(V e, PriorityQueue<Message<V, E>> m, EventType initialEvent)
	{
		en = e;
		neededRewrites = 1;
		messages = m;
		arxikoEvent = initialEvent;
		marked=false;
	}

	@SuppressWarnings("unchecked")
	private V cloneNode(V nd, EvolutionGraph<V, E> graph)
	{
		V nn = (V) VisualNodeFactory.create();
		nn.setName(nd.getName());
		nn.setType(nd.getType());
		nn.ID = nd.ID + 0.4;
		graph.addVertex(nn);
		return (nn);
	}

	@SuppressWarnings("unchecked")
	private E cloneEdge(V fromNode, V toNode, String name, EdgeType type, EvolutionGraph<V, E> graph)
	{
		E e = (E) VisualEdgeFactory.create();
		e.setFromNode(fromNode);
		e.setToNode(toNode);
		e.setType(type);
		e.setName(name);
		graph.addEdge(e, fromNode, toNode);
		return (e);
	}
	
	@SuppressWarnings("unchecked")
	private V findNewProvider(V node)
	{
		for (int k = 0; k < node.getOutEdges().size(); k++)
		{
			if (node.getOutEdges().get(k).getType() == EdgeType.EDGE_TYPE_OUTPUT)
			{
				return (V) (node.getOutEdges().get(k).getToNode());
			}
		}
		return (null);
	}

	@SuppressWarnings("unchecked")
	private V findNewProviderNode(V node, String onoma)
	{
		for (int k = 0; k < node.getOutEdges().size(); k++)
		{
			if (node.getOutEdges().get(k).getType() == EdgeType.EDGE_TYPE_OUTPUT)
			{
				V otv = (V) node.getOutEdges().get(k).getToNode();
				for (int i = 0; i < otv.getOutEdges().size(); i++)
				{
					if (otv.getOutEdges().get(i).getToNode().getName().equals(onoma))
					{
						return (V) (otv.getOutEdges().get(i).getToNode());
					}
				}
			}
		}
		return (null);
	}

	@SuppressWarnings("unchecked")
	private V cloneInput(V parent, V node, EvolutionGraph<V, E> graph)
	{
		V n = cloneNode(node, graph);
		V pp = graph.findVertexById(parent.ID + 0.4);
		for (int i = 0; i < node.getOutEdges().size(); i++)
		{
			if(node.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				V in = cloneNode((V) node.getOutEdges().get(i).getToNode(), graph);
				cloneEdge(n, in, node.getOutEdges().get(i).getName(), node.getOutEdges().get(i).getType(), graph);
				V prov = null;
				if (pp == null)
				{
					for(int j=0;j<node.getOutEdges().size();j++)
					{
						if(node.getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
						{
							for(int k=0;k<node.getOutEdges().get(j).getToNode().getInEdges().size();k++)
							{
								if(node.getOutEdges().get(j).getToNode().getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_OUTPUT)
								{
									pp=(V) node.getOutEdges().get(j).getToNode().getInEdges().get(k).getFromNode();
								}
							}
						}
					}
				}
				prov = findNewProviderNode(pp, in.getName());
				cloneEdge(in, prov, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				continue;
			}
			if(node.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_FROM)
			{
				if(pp==null)
				{
					cloneEdge(n, (V) node.getOutEdges().get(i).getToNode(), "from", EdgeType.EDGE_TYPE_FROM, graph);
				}
				else
				{
					cloneEdge(n, findNewProvider(pp), "from", EdgeType.EDGE_TYPE_FROM, graph);
				}
			}
		}
		return (n);
	}

	private String paterasKombos(V kombos)
	{
		for (int i = 0; i < kombos.getInEdges().size(); i++)
		{
			if (kombos.getInEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT)
			{
				return (kombos.getInEdges().get(i).getFromNode().getName().substring(kombos.getInEdges().get(i).getFromNode().getName().indexOf("_IN_") + 4) + "." + kombos.getName());
			}
		}
		return (null);
	}
	
	private String paterasKombou(V kombos)
	{
		if(paterasKombos(kombos)!=null)
		{
			return(paterasKombos(kombos).substring(0,paterasKombos(kombos).indexOf(".")));
		}
		return(null);
	}

	@SuppressWarnings("unchecked")
	private V existsInInputSchema(V head, String tableName, String nodeName, EvolutionGraph<V, E> graph)
	{
		if(tableName==null)
		{
			return(null);
		}
		for (int i = 0; i < head.getOutEdges().size(); i++)
		{
			if (head.getOutEdges().get(i).getToNode().getName().equals(head.getName() + "_IN_" + tableName))
			{
				V in = (V) head.getOutEdges().get(i).getToNode();
				for (int j = 0; j < in.getOutEdges().size(); j++)
				{
					if (in.getOutEdges().get(j).getToNode().getName().equals(nodeName))
					{
						return (V) (in.getOutEdges().get(j).getToNode());
					}
				}
			}
		}
		return (null);
	}
	
	@SuppressWarnings("unchecked")
	private V existsInSMTXSchema(V head, String nodeName, EvolutionGraph<V, E> graph)
	{
		for (int i = 0; i < head.getOutEdges().size(); i++)
		{
			if (head.getOutEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				V in = (V) head.getOutEdges().get(i).getToNode();
				for (int j = 0; j < in.getOutEdges().size(); j++)
				{
					if (in.getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_SEMANTICS && in.getOutEdges().get(j).getToNode().getName().equals(nodeName))
					{
						return (V) (in.getOutEdges().get(j).getToNode());
					}
				}
			}
		}
		return (null);
	}

	@SuppressWarnings("unchecked")
	private V smtxWhereTraversal(V kombos, EvolutionGraph<V, E> graph)
	{
		V left = null;
		V right = null;
		if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE || kombos.getType() == NodeType.NODE_TYPE_CONSTANT)
		{
			if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE)
			{
				return (existsInInputSchema(graph.findVertexById(en.ID+0.4), paterasKombou(kombos), kombos.getName(), graph));
			}
			else
			{
				return (cloneNode(kombos, graph));
			}
		}
		for (int i = 0; i < kombos.getOutEdges().size(); i++)
		{
			if (kombos.getOutEdges().get(i).getName().equals("op1"))
			{
				left = (V) kombos.getOutEdges().get(i).getToNode();
			}
			if (kombos.getOutEdges().get(i).getName().equals("op2"))
			{
				right = (V) kombos.getOutEdges().get(i).getToNode();
			}
		}
		V k = cloneNode(kombos, graph);
		cloneEdge(k, smtxWhereTraversal(left, graph), "op1", EdgeType.EDGE_TYPE_OPERATOR, graph);
		cloneEdge(k, smtxWhereTraversal(right, graph), "op2", EdgeType.EDGE_TYPE_OPERATOR, graph);
		return (k);
	}

	@SuppressWarnings("unchecked")
	private V cloneSmtx(V node, EvolutionGraph<V, E> graph)
	{
		V n = cloneNode(node, graph);
		for (int i = 0; i < node.getOutEdges().size(); i++)
		{
			if (node.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_WHERE)
			{
				V nw = smtxWhereTraversal((V) node.getOutEdges().get(i).getToNode(), graph);
				cloneEdge(n, nw, "where", EdgeType.EDGE_TYPE_WHERE, graph);
			}
			if (node.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_GROUP_BY)
			{
				V ngb = cloneNode((V) node.getOutEdges().get(i).getToNode(), graph);
				cloneEdge(n, ngb, "group by", EdgeType.EDGE_TYPE_GROUP_BY, graph);
				for (int j = 0; j < node.getOutEdges().get(i).getToNode().getOutEdges().size(); j++)
				{
					V tgb = (V) node.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode();
					cloneEdge(ngb, existsInInputSchema(graph.findVertexById(en.ID+0.4), paterasKombou(tgb), tgb.getName(), graph), node.getOutEdges().get(i).getToNode().getOutEdges().get(j).getName(), EdgeType.EDGE_TYPE_GROUP_BY, graph);
				}
			}
			if (node.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
			{
				V af = cloneNode((V) node.getOutEdges().get(i).getToNode(), graph);
				cloneEdge(n, af, af.getName(), EdgeType.EDGE_TYPE_SEMANTICS, graph);
				V xm=existsInInputSchema(graph.findVertexById(en.ID+0.4), paterasKombou((V) node.getOutEdges().get(i).getToNode().getOutEdges().get(0).getToNode()), node.getOutEdges().get(i).getToNode().getOutEdges().get(0).getToNode().getName(), graph);
				cloneEdge( af, xm, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
			}
		}
		return (n);
	}

	@SuppressWarnings("unchecked")
	private V cloneOut(V node, EvolutionGraph<V, E> graph)
	{
		V n = cloneNode(node, graph);
		for (int i = 0; i < node.getOutEdges().size(); i++)
		{
			V no = cloneNode((V) node.getOutEdges().get(i).getToNode(), graph);
			cloneEdge(n, no, node.getOutEdges().get(i).getName(), EdgeType.EDGE_TYPE_OUTPUT, graph);
			for (int j = 0; j < node.getOutEdges().get(i).getToNode().getOutEdges().size(); j++)
			{
				V inatr = existsInInputSchema(graph.findVertexById(en.ID+0.4), paterasKombou((V) node.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode()), node.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode().getName(), graph);
				if (inatr != null)
				{
					cloneEdge(no, inatr, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				}
				else
				{
					cloneEdge(no, existsInSMTXSchema(graph.findVertexById(en.ID+0.4), node.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode().getName(), graph), "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				}
			}
		}
		return (n);
	}

	@SuppressWarnings("unchecked")
	public V cloneQVModule(EvolutionGraph<V, E> graph)
	{
		V n = cloneNode(en, graph);
		for (int i = 0; i < en.getOutEdges().size(); i++)
		{
			if (en.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_INPUT)
			{
				V pateras = null;
				for(int j=0;j<en.getOutEdges().get(i).getToNode().getOutEdges().size();j++)
				{
					if(en.getOutEdges().get(i).getToNode().getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
					{
						V pat=(V) en.getOutEdges().get(i).getToNode().getOutEdges().get(j).getToNode();
						for(int k=0;k<pat.getInEdges().size();k++)
						{
							if(pat.getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_OUTPUT)
							{
								pateras=(V) pat.getInEdges().get(k).getFromNode();
							}
						}
					}
				}
				V in = cloneInput(pateras, (V) en.getOutEdges().get(i).getToNode(), graph);
				for(int j=0;j<in.getOutEdges().size();j++)
				{
					if(in.getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_FROM)
					{
						for(int k=0;k<in.getOutEdges().get(j).getToNode().getInEdges().size();k++)
						{
							if(in.getOutEdges().get(j).getToNode().getInEdges().get(k).getType()==EdgeType.EDGE_TYPE_OUTPUT)
							{
								cloneEdge(n, (V) in.getOutEdges().get(j).getToNode().getInEdges().get(k).getFromNode(), "uses", EdgeType.EDGE_TYPE_USES, graph);
							}
						}
					}
				}
				cloneEdge(n, in, n.getName()+"IN_S", EdgeType.EDGE_TYPE_INPUT, graph);
			}
		}
		for (int i = 0; i < en.getOutEdges().size(); i++)
		{
			if (en.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_SEMANTICS)
			{
				V smtx = cloneSmtx((V) en.getOutEdges().get(i).getToNode(), graph);
				cloneEdge(n, smtx, n.getName()+"SMTX_S", EdgeType.EDGE_TYPE_SEMANTICS, graph);
				break;
			}
		}
		for (int i = 0; i < en.getOutEdges().size(); i++)
		{
			if (en.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_OUTPUT)
			{
				cloneEdge(n, cloneOut((V) en.getOutEdges().get(i).getToNode(), graph), n.getName()+"OUT_S", EdgeType.EDGE_TYPE_OUTPUT, graph);
				break;
			}
		}
		// TODO: rename to _new?
		for(int i=0;i<n.getOutEdges().size();i++)
		{
			if(n.getOutEdges().get(i).getType()!=EdgeType.EDGE_TYPE_USES)
			{
				n.getOutEdges().get(i).getToNode().setName(n.getOutEdges().get(i).getToNode().getName().replace(n.getName(), n.getName()+"_new"));
			}
		}
		n.setName(n.getName().replace(n.getName(), n.getName()+"_new"));
		return (n);
	}

	public StatusType getStatus()
	{
		return (en.getStatus());
	}

	public void setEmeis(List<ModuleNode<V, E>> em)
	{
		this.emeis = em;
	}

	public void backPropagation()
	{ // Set in this PMModuleNode neededRewrites=2
		if(this.marked==true)
		{
			return;
		}
		this.marked=true;
		if(arxikoEvent==EventType.ADD_ATTRIBUTE)
		{
			this.neededRewrites=1;
		}
		else
		{
			this.neededRewrites = 2;
		}
		for (int i = 0; i < this.en.getOutEdges().size(); i++)
		{
			if (this.en.getOutEdges().get(i).getType() == EdgeType.EDGE_TYPE_USES
					&& this.en.getOutEdges().get(i).getToNode().getStatus() == StatusType.PROPAGATE)
			{ // Get this nodes providers that have been affected by the change
				for (int j = 0; j < this.emeis.size(); j++)
				{
					if (this.emeis.get(j).en == this.en.getOutEdges().get(i).getToNode()
							&& this.emeis.get(j).neededRewrites == 1)
					{ // Get for that node its map @ emeis list AND if it was
						// not visited (neededRewrites==1)
						this.emeis.get(j).backPropagation(); // Visit that node.
					}
				}
			}
		}
	}
}
