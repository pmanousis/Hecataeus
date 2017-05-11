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

public class ModuleNode {
	public EvolutionNode module;
	public int neededRewrites;
	public PriorityQueue<Message> messages;
	List<ModuleNode> emeis;
	EventType arxikoEvent;
	boolean marked;

	public ModuleNode(final EvolutionNode e, final PriorityQueue<Message> m, final EventType initialEvent) {
		module = e;
		neededRewrites = 1;
		messages = m;
		arxikoEvent = initialEvent;
		marked = false;
	}

	public void backPropagation() { // Set in this PMModuleNode neededRewrites=2
		if (marked == true)
			return;
		marked = true;
		if (arxikoEvent == EventType.ADD_ATTRIBUTE)
			neededRewrites = 1;
		else
			neededRewrites = 2;
		for (final EvolutionEdge eoe : module.getOutEdges())
			if (eoe.getType() == EdgeType.EDGE_TYPE_USES && eoe.getToNode().getStatus() == StatusType.PROPAGATE)
				for (final ModuleNode mn : emeis)
					if (mn.module == eoe.getToNode() && mn.neededRewrites == 1)
						mn.backPropagation(); // Visit that node.
	}

	public EvolutionNode cloneQVModule(final EvolutionGraph graph) {
		final EvolutionNode n = cloneNode(module, graph);
		for (final EvolutionEdge enoe : module.getOutEdges())
			if (enoe.getType() == EdgeType.EDGE_TYPE_INPUT) {
				EvolutionNode pateras = null;
				for (final EvolutionEdge enoetoe : enoe.getToNode().getOutEdges())
					if (enoetoe.getType() == EdgeType.EDGE_TYPE_FROM)
						for (final EvolutionEdge enoetoeie : enoetoe.getToNode().getInEdges())
							if (enoetoeie.getType() == EdgeType.EDGE_TYPE_OUTPUT)
								pateras = enoetoeie.getFromNode();
				final EvolutionNode in = cloneInput(pateras, enoe.getToNode(), graph);
				for (final EvolutionEdge enioe : in.getOutEdges())
					if (enioe.getType() == EdgeType.EDGE_TYPE_FROM)
						for (final EvolutionEdge enioeti : enioe.getToNode().getInEdges())
							if (enioeti.getType() == EdgeType.EDGE_TYPE_OUTPUT)
								cloneEdge(n, enioeti.getFromNode(), "uses", EdgeType.EDGE_TYPE_USES, graph);
				cloneEdge(n, in, n.getName() + "IN_S", EdgeType.EDGE_TYPE_INPUT, graph);
			}
		for (final EvolutionEdge enoe : module.getOutEdges())
			if (enoe.getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
				cloneEdge(n, cloneSmtx(enoe.getToNode(), graph), n.getName() + "SMTX_S", EdgeType.EDGE_TYPE_SEMANTICS,
					graph);
				break;
			}
		for (final EvolutionEdge enoe : module.getOutEdges())
			if (enoe.getType() == EdgeType.EDGE_TYPE_OUTPUT) {
				cloneEdge(n, cloneOut(enoe.getToNode(), graph), n.getName() + "OUT_S", EdgeType.EDGE_TYPE_OUTPUT,
					graph);
				break;
			}
		// TODO: rename to -new?
		String toAppend = "-new";
		while (checkIfAlreadyExistsNodeWithThatName(graph, n.getName() + toAppend) == true)
			toAppend = UUID.randomUUID().toString();
		for (final EvolutionEdge ne : n.getOutEdges())
			if (ne.getType() != EdgeType.EDGE_TYPE_USES)
				ne.getToNode().setName(ne.getToNode().getName().replace(n.getName(), n.getName() + toAppend));
		n.setName(n.getName().replace(n.getName(), n.getName() + toAppend));
		return n;
	}

	public StatusType getStatus() {
		return module.getStatus();
	}

	public void setEmeis(final List<ModuleNode> em) {
		emeis = em;
	}

	private boolean checkIfAlreadyExistsNodeWithThatName(final EvolutionGraph graph, final String nameToAdd) {
		boolean toReturn = false;
		for (final EvolutionNode n : graph.getVertices())
			if (n.getName().equals(nameToAdd))
				toReturn = true;
		return toReturn;
	}

	private EvolutionEdge cloneEdge(final EvolutionNode fromNode,
		final EvolutionNode toNode,
		final String name,
		final EdgeType type,
		final EvolutionGraph graph) {
		final EvolutionEdge e = new EvolutionEdge();
		e.setFromNode(fromNode);
		e.setToNode(toNode);
		e.setType(type);
		e.setName(name);
		e.setStatus(StatusType.PROPAGATE, true);
		graph.addEdge(e);
		return e;
	}

	private EvolutionNode cloneInput(final EvolutionNode parent, final EvolutionNode node, final EvolutionGraph graph) {
		final EvolutionNode n = cloneNode(node, graph);
		EvolutionNode pp = graph.findVertexById(parent.getID() + 0.4);
		for (final EvolutionEdge oe : node.getOutEdges()) {
			if (oe.getType() == EdgeType.EDGE_TYPE_INPUT) {
				final EvolutionNode in = cloneNode(oe.getToNode(), graph);
				cloneEdge(n, in, oe.getName(), oe.getType(), graph);
				EvolutionNode prov = null;
				if (pp == null)
					for (final EvolutionEdge ioe : node.getOutEdges())
						if (ioe.getType() == EdgeType.EDGE_TYPE_FROM)
							for (final EvolutionEdge ioeie : ioe.getToNode().getInEdges())
								if (ioeie.getType() == EdgeType.EDGE_TYPE_OUTPUT)
									pp = ioeie.getFromNode();
				prov = findNewProviderNode(pp, in.getName());
				cloneEdge(in, prov, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				continue;
			}
			if (oe.getType() == EdgeType.EDGE_TYPE_FROM)
				if (pp == null)
					cloneEdge(n, oe.getToNode(), "from", EdgeType.EDGE_TYPE_FROM, graph);
				else
					cloneEdge(n, findNewProvider(pp), "from", EdgeType.EDGE_TYPE_FROM, graph);
		}
		return n;
	}

	private EvolutionNode cloneNode(final EvolutionNode nd, final EvolutionGraph graph) {
		final EvolutionNode nn = new EvolutionNode();
		nn.setName(nd.getName());
		nn.setType(nd.getType());
		nn.setID(nd.getID() + 0.4);
		nn.setStatus(StatusType.PROPAGATE, true);
		graph.addVertex(nn);
		return nn;
	}

	private EvolutionNode cloneOut(final EvolutionNode node, final EvolutionGraph graph) {
		final EvolutionNode n = cloneNode(node, graph);
		for (final EvolutionEdge aoe : node.getOutEdges()) {
			final EvolutionNode no = cloneNode(aoe.getToNode(), graph);
			cloneEdge(n, no, aoe.getName(), EdgeType.EDGE_TYPE_OUTPUT, graph);
			for (final EvolutionEdge aiefoe : aoe.getToNode().getOutEdges()) {
				final EvolutionNode inatr = existsInInputSchema(graph.findVertexById(module.getID() + 0.4),
					paterasKombou(aiefoe.getToNode()), aiefoe.getToNode().getName(), graph);
				if (inatr != null)
					cloneEdge(no, inatr, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
				else
					cloneEdge(no, existsInSMTXSchema(graph.findVertexById(module.getID() + 0.4),
						aiefoe.getToNode().getName(), graph), "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
			}
		}
		return n;
	}

	private EvolutionNode cloneSmtx(final EvolutionNode node, final EvolutionGraph graph) {
		final EvolutionNode n = cloneNode(node, graph);
		for (final EvolutionEdge noe : node.getOutEdges()) {
			if (noe.getType() == EdgeType.EDGE_TYPE_WHERE) {
				final EvolutionNode nw = smtxWhereTraversal(noe.getToNode(), graph);
				cloneEdge(n, nw, "where", EdgeType.EDGE_TYPE_WHERE, graph);
			}
			if (noe.getType() == EdgeType.EDGE_TYPE_GROUP_BY) {
				final EvolutionNode ngb = cloneNode(noe.getToNode(), graph);
				cloneEdge(n, ngb, "group by", EdgeType.EDGE_TYPE_GROUP_BY, graph);
				for (final EvolutionEdge noetoe : noe.getToNode().getOutEdges()) {
					final EvolutionNode tgb = noetoe.getToNode();
					cloneEdge(ngb, existsInInputSchema(graph.findVertexById(module.getID() + 0.4), paterasKombou(tgb),
						tgb.getName(), graph), noetoe.getName(), EdgeType.EDGE_TYPE_GROUP_BY, graph);
				}
			}
			if (noe.getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
				final EvolutionNode af = cloneNode(noe.getToNode(), graph);
				cloneEdge(n, af, af.getName(), EdgeType.EDGE_TYPE_SEMANTICS, graph);
				final EvolutionNode xm = existsInInputSchema(graph.findVertexById(module.getID() + 0.4),
					paterasKombou(noe.getToNode().getOutEdges().get(0).getToNode()),
					noe.getToNode().getOutEdges().get(0).getToNode().getName(), graph);
				cloneEdge(af, xm, "map-select", EdgeType.EDGE_TYPE_MAPPING, graph);
			}
		}
		return n;
	}

	private EvolutionNode existsInInputSchema(final EvolutionNode head,
		final String providerName,
		final String nodeName,
		final EvolutionGraph graph) {
		if (providerName == null)
			return null;
		for (final EvolutionEdge hoe : head.getOutEdges())
			if (hoe.getToNode().getName().equals(head.getName() + "_IN_" + providerName)) {
				final EvolutionNode in = hoe.getToNode();
				for (final EvolutionEdge hoeoe : in.getOutEdges())
					if (hoeoe.getToNode().getName().equals(nodeName))
						return hoeoe.getToNode();
			}
		return null;
	}

	private EvolutionNode existsInSMTXSchema(final EvolutionNode head,
		final String nodeName,
		final EvolutionGraph graph) {
		for (final EvolutionEdge hoe : head.getOutEdges())
			if (hoe.getType() == EdgeType.EDGE_TYPE_SEMANTICS) {
				final EvolutionNode in = hoe.getToNode();
				for (final EvolutionEdge hoeoe : in.getOutEdges())
					if (hoeoe.getType() == EdgeType.EDGE_TYPE_SEMANTICS && hoeoe.getToNode().getName().equals(nodeName))
						return hoeoe.getToNode();
			}
		return null;
	}

	private EvolutionNode findNewProvider(final EvolutionNode node) {
		for (final EvolutionEdge oe : node.getOutEdges())
			if (oe.getType() == EdgeType.EDGE_TYPE_OUTPUT)
				return oe.getToNode();
		return null;
	}

	private EvolutionNode findNewProviderNode(final EvolutionNode node, final String onoma) {
		for (final EvolutionEdge oe : node.getOutEdges())
			if (oe.getType() == EdgeType.EDGE_TYPE_OUTPUT) {
				final EvolutionNode otv = oe.getToNode();
				for (final EvolutionEdge otvoe : otv.getOutEdges())
					if (otvoe.getToNode().getName().equals(onoma))
						return otvoe.getToNode();
			}
		return null;
	}

	private String paterasKombos(final EvolutionNode kombos) {
		for (final EvolutionEdge ie : kombos.getInEdges())
			if (ie.getType() == EdgeType.EDGE_TYPE_INPUT)
				return ie.getFromNode().getName().substring(ie.getFromNode().getName().indexOf("_IN_") + 4) + "." +
					kombos.getName();
		return null;
	}

	private String paterasKombou(final EvolutionNode kombos) {
		if (paterasKombos(kombos) != null)
			return paterasKombos(kombos).substring(0, paterasKombos(kombos).indexOf("."));
		return null;
	}

	private EvolutionNode smtxWhereTraversal(final EvolutionNode kombos, final EvolutionGraph graph) {
		EvolutionNode left = null;
		EvolutionNode right = null;
		if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE || kombos.getType() == NodeType.NODE_TYPE_CONSTANT)
			if (kombos.getType() == NodeType.NODE_TYPE_ATTRIBUTE)
				return existsInInputSchema(graph.findVertexById(module.getID() + 0.4), paterasKombou(kombos),
					kombos.getName(), graph);
			else
				return cloneNode(kombos, graph);
		for (final EvolutionEdge koe : kombos.getOutEdges()) {
			if (koe.getName().equals("op1"))
				left = koe.getToNode();
			if (koe.getName().equals("op2"))
				right = koe.getToNode();
		}
		final EvolutionNode k = cloneNode(kombos, graph);
		cloneEdge(k, smtxWhereTraversal(left, graph), "op1", EdgeType.EDGE_TYPE_OPERATOR, graph);
		cloneEdge(k, smtxWhereTraversal(right, graph), "op2", EdgeType.EDGE_TYPE_OPERATOR, graph);
		return k;
	}
}
