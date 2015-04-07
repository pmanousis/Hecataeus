package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

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
	public V module;
	public int neededRewrites;
	public PriorityQueue<Message<V, E>> messages;
	List<ModuleNode<V, E>> emeis;
	EventType arxikoEvent;
	boolean marked;

	public ModuleNode(V e, PriorityQueue<Message<V, E>> m, EventType initialEvent)
	{
		module = e;
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
		nn.setStatus(StatusType.PROPAGATE, true);
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
		e.setStatus(StatusType.PROPAGATE, true);
		graph.addEdge(e, fromNode, toNode);
		return (e);
	}
	
	@SuppressWarnings("unchecked")
	private V findNewProvider(V node)
	{
		for (E oe: node.getOutEdges())
		{
			if (oe.getType() == EdgeType.EDGE_TYPE_OUTPUT)
			{
				return ((V) oe.getToNode());
			}
		}
		return (null);
	}

	@SuppressWarnings("unchecked")
	private V findNewProviderNode(V node, String onoma)
	{
		for (E oe: node.getOutEdges())
		{
			if (oe.getType() == EdgeType.EDGE_TYPE_OUTPUT)
			{
				V otv = (V) oe.getToNode();
				for (E otvoe : otv.getOutEdges())
				{
					if (otvoe.getToNode().getName().equals(onoma))
					{
						return((V) otvoe.getToNode());
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
		for (E oe: node.getOutEdges())
		{
			if(oe.getType()==EdgeType.EDGE_TYPE_INPUT)
			{
				V in = cloneNode((V) oe.getToNode(), graph);
				cloneEdge(n, in, oe.getName(), oe.getType(), graph);
				V prov = null;
				if (pp == null)
				{
					for(E ioe : node.getOutEdges())
					{
						if(ioe.getType()==EdgeType.EDGE_TYPE_FROM)
						{
							for(EvolutionEdge ioeie: ioe.getToNode().getInEdges())
							{
								if(ioeie.getType()==EdgeType.EDGE_TYPE_OUTPUT)
								{
									pp=(V) ioeie.getFromNode();
								}
							}
						}
					}
				}
				prov = findNewProviderNode(pp, in.getName());
				cloneEdge(in, prov, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				continue;
			}
			if(oe.getType()==EdgeType.EDGE_TYPE_FROM)
			{
				if(pp==null)
				{
					cloneEdge(n, (V) oe.getToNode(), "from", EdgeType.EDGE_TYPE_FROM, graph);
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
		for (E ie: kombos.getInEdges())
		{
			if (ie.getType() == EdgeType.EDGE_TYPE_INPUT)
			{
				return (ie.getFromNode().getName().substring(ie.getFromNode().getName().indexOf("_IN_") + 4) + "." + kombos.getName());
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
	private V existsInInputSchema(V head, String providerName, String nodeName, EvolutionGraph<V, E> graph)
	{
		if(providerName==null)
		{
			return(null);
		}
		for (E hoe: head.getOutEdges())
		{
			if (hoe.getToNode().getName().equals(head.getName() + "_IN_" + providerName))
			{
				V in = (V) hoe.getToNode();
				for (E hoeoe: in.getOutEdges())
				{
					if (hoeoe.getToNode().getName().equals(nodeName))
					{
						return (V) (hoeoe.getToNode());
					}
				}
			}
		}
		return (null);
	}
	
	@SuppressWarnings("unchecked")
	private V existsInSMTXSchema(V head, String nodeName, EvolutionGraph<V, E> graph)
	{
		for (E hoe: head.getOutEdges())
		{
			if (hoe.getType()==EdgeType.EDGE_TYPE_SEMANTICS)
			{
				V in = (V) hoe.getToNode();
				for (E hoeoe: in.getOutEdges())
				{
					if (hoeoe.getType()==EdgeType.EDGE_TYPE_SEMANTICS && hoeoe.getToNode().getName().equals(nodeName))
					{
						return (V) (hoeoe.getToNode());
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
				return (existsInInputSchema(graph.findVertexById(module.ID+0.4), paterasKombou(kombos), kombos.getName(), graph));
			}
			else
			{
				return (cloneNode(kombos, graph));
			}
		}
		for (E koe: kombos.getOutEdges())
		{
			if (koe.getName().equals("op1"))
			{
				left = (V) koe.getToNode();
			}
			if (koe.getName().equals("op2"))
			{
				right = (V) koe.getToNode();
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
		for (E noe: node.getOutEdges())
		{
			if (noe.getType() == EdgeType.EDGE_TYPE_WHERE)
			{
				V nw = smtxWhereTraversal((V) noe.getToNode(), graph);
				cloneEdge(n, nw, "where", EdgeType.EDGE_TYPE_WHERE, graph);
			}
			if (noe.getType() == EdgeType.EDGE_TYPE_GROUP_BY)
			{
				V ngb = cloneNode((V) noe.getToNode(), graph);
				cloneEdge(n, ngb, "group by", EdgeType.EDGE_TYPE_GROUP_BY, graph);
				for (EvolutionEdge noetoe: noe.getToNode().getOutEdges())
				{
					V tgb = (V) noetoe.getToNode();
					cloneEdge(ngb, existsInInputSchema(graph.findVertexById(module.ID+0.4), paterasKombou(tgb), tgb.getName(), graph), noetoe.getName(), EdgeType.EDGE_TYPE_GROUP_BY, graph);
				}
			}
			if (noe.getType() == EdgeType.EDGE_TYPE_SEMANTICS)
			{
				V af = cloneNode((V) noe.getToNode(), graph);
				cloneEdge(n, af, af.getName(), EdgeType.EDGE_TYPE_SEMANTICS, graph);
				V xm=existsInInputSchema(graph.findVertexById(module.ID+0.4), paterasKombou((V) noe.getToNode().getOutEdges().get(0).getToNode()), noe.getToNode().getOutEdges().get(0).getToNode().getName(), graph);
				cloneEdge( af, xm, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
			}
		}
		return (n);
	}

	@SuppressWarnings("unchecked")
	private V cloneOut(V node, EvolutionGraph<V, E> graph)
	{
		V n = cloneNode(node, graph);
		for (E aoe : node.getOutEdges())
		{
			V no = cloneNode((V) aoe.getToNode(), graph);
			cloneEdge(n, no, aoe.getName(), EdgeType.EDGE_TYPE_OUTPUT, graph);
			for (EvolutionEdge aiefoe : aoe.getToNode().getOutEdges())
			{
				V inatr = existsInInputSchema(graph.findVertexById(module.ID+0.4), paterasKombou((V) aiefoe.getToNode()), aiefoe.getToNode().getName(), graph);
				if (inatr != null)
				{
					cloneEdge(no, inatr, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				}
				else
				{
					cloneEdge(no, existsInSMTXSchema(graph.findVertexById(module.ID+0.4), aiefoe.getToNode().getName(), graph), "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				}
			}
		}
		return (n);
	}

	@SuppressWarnings("unchecked")
	public V cloneQVModule(EvolutionGraph<V, E> graph)
	{
		V n = cloneNode(module, graph);
		for (E enoe: module.getOutEdges())
		{
			if (enoe.getType() == EdgeType.EDGE_TYPE_INPUT)
			{
				V pateras = null;
				for(EvolutionEdge enoetoe: enoe.getToNode().getOutEdges())
				{
					if(enoetoe.getType()==EdgeType.EDGE_TYPE_FROM)
					{
						for(EvolutionEdge enoetoeie: enoetoe.getToNode().getInEdges())
						{
							if(enoetoeie.getType()==EdgeType.EDGE_TYPE_OUTPUT)
							{
								pateras=(V) enoetoeie.getFromNode();
							}
						}
					}
				}
				V in = cloneInput(pateras, (V) enoe.getToNode(), graph);
				for(E enioe: in.getOutEdges())
				{
					if(enioe.getType()==EdgeType.EDGE_TYPE_FROM)
					{
						for(EvolutionEdge enioeti: enioe.getToNode().getInEdges())
						{
							if(enioeti.getType()==EdgeType.EDGE_TYPE_OUTPUT)
							{
								cloneEdge(n, (V) enioeti.getFromNode(), "uses", EdgeType.EDGE_TYPE_USES, graph);
							}
						}
					}
				}
				cloneEdge(n, in, n.getName()+"IN_S", EdgeType.EDGE_TYPE_INPUT, graph);
			}
		}
		for (E enoe: module.getOutEdges())
		{
			if (enoe.getType() == EdgeType.EDGE_TYPE_SEMANTICS)
			{
				cloneEdge(n, cloneSmtx((V) enoe.getToNode(), graph), n.getName()+"SMTX_S", EdgeType.EDGE_TYPE_SEMANTICS, graph);
				break;
			}
		}
		for (E enoe: module.getOutEdges())
		{
			if (enoe.getType() == EdgeType.EDGE_TYPE_OUTPUT)
			{
				cloneEdge(n, cloneOut((V) enoe.getToNode(), graph), n.getName()+"OUT_S", EdgeType.EDGE_TYPE_OUTPUT, graph);
				break;
			}
		}
		// TODO: rename to -new?
		String toAppend = "-new";
		while(checkIfAlreadyExistsNodeWithThatName(graph, n.getName()+toAppend) == true)
		{
			toAppend = UUID.randomUUID().toString();
		}
		for(E ne: n.getOutEdges())
		{
			if(ne.getType()!=EdgeType.EDGE_TYPE_USES)
			{
				ne.getToNode().setName(ne.getToNode().getName().replace(n.getName(), n.getName() + toAppend));
			}
		}
		n.setName(n.getName().replace(n.getName(), n.getName() + toAppend));
		return (n);
	}
	
	private boolean checkIfAlreadyExistsNodeWithThatName(EvolutionGraph<V, E> graph, String nameToAdd)
	{
		boolean toReturn = false;
		for(V n: graph.getVertices())
		{
			if(n.getName().equals(nameToAdd))
			{
				toReturn = true;
			}
		}
		return(toReturn);
	}

	public StatusType getStatus()
	{
		return (module.getStatus());
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
		for (E eoe: this.module.getOutEdges())
		{
			if (eoe.getType() == EdgeType.EDGE_TYPE_USES && eoe.getToNode().getStatus() == StatusType.PROPAGATE)
			{ // Get this nodes providers that have been affected by the change
				for (ModuleNode<V, E> mn: this.emeis)
				{
					if (mn.module == eoe.getToNode() && mn.neededRewrites == 1)
					{ // Get for that node its map @ emeis list AND if it was not visited (neededRewrites==1)
						mn.backPropagation(); // Visit that node.
					}
				}
			}
		}
	}
}
