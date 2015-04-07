package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

/**
 * @author pmanousi
 * It does a topological sorting in IDs of high level nodes (Queries, Views, Relations).
 * */
public class TopologicalTravel
{
	HecataeusViewer v;
	VisualGraph graph;
	List<VisualEdge> removedEdges;
	List<VisualNode> removedNodes;
	TreeMap<Integer, List<VisualNode>> strata;
	
	public TopologicalTravel(HecataeusViewer viewer)
	{
		removedEdges = new ArrayList<VisualEdge>();
		removedNodes = new ArrayList<VisualNode>();
		v=viewer;
	}

	public TopologicalTravel(VisualGraph g)
	{
		removedEdges = new ArrayList<VisualEdge>();
		removedNodes = new ArrayList<VisualNode>();
		graph = g;
	}
	
	public int numberOfIncomingUsesEdges(VisualNode node, List<VisualNode> hln)
	{
		int toReturn = 0;
		for(VisualEdge e : node.getInEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()))
			{
				toReturn++;
			}
		}
		return(toReturn);
	}
	
	public void removeIncomingUsesEdgesAtConnectedNodes(VisualNode node)
	{
		List<VisualNode> connectedNodes = new ArrayList<VisualNode>();
		for(VisualEdge e : node.getOutEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES)
			{
				connectedNodes.add(e.getToNode());
			}
		}
		for(VisualNode n : connectedNodes)
		{
			List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
			for(VisualEdge e : n.getInEdges())
			{
				if(e.getType() == EdgeType.EDGE_TYPE_USES && e.getFromNode() == node)
				{
					edgesToRemove.add(e);
				}
			}
			removedEdges.addAll(edgesToRemove);
			n.getInEdges().removeAll(edgesToRemove);
		}
	}
	
	/**
	 * First are the nodes with incoming edges = 0 (startNode).
	 * They are removed with their outgoing edges (EDGE_TYPE_USES).
	 * Others (parentNode) that had only 1 incoming edge become startNode.
	 * Continue till you have no more nodes to check.
	 * */
	public void travel()
	{
		strata = new TreeMap<Integer, List<VisualNode>>();
		for(int i=0;i<graph.getVertices().size();i++)
		{
			graph.getVertices().get(i).ID=0;
		}
		List<VisualNode> highLevelNodes=graph.getVertices(NodeType.NODE_TYPE_QUERY);
		highLevelNodes.addAll(graph.getVertices(NodeType.NODE_TYPE_RELATION));
		highLevelNodes.addAll(graph.getVertices(NodeType.NODE_TYPE_VIEW));
		int highLevelNodeCounter = highLevelNodes.size();
		int strataCounter = 0;
		while(highLevelNodes.size()>0)
		{
			for(int i=0;i<highLevelNodes.size();i++)
			{
				if(numberOfIncomingUsesEdges(highLevelNodes.get(i), highLevelNodes) == 0)
				{ // Found start of graph
					VisualNode startNode=highLevelNodes.get(i);
					
					if(startNode.getType() == NodeType.NODE_TYPE_VIEW)
					{
						if(strata.get(strataCounter) == null)
						{
							strata.put(strataCounter, new ArrayList<VisualNode>());
						}
						strata.get(strataCounter).add(startNode);
					}
					
					// Assign IDs (maybe later call a function to assign keys to its children)
					graph.findVertexByName(startNode.getName()).ID=highLevelNodeCounter;
					highLevelNodeCounter--;
					removeIncomingUsesEdgesAtConnectedNodes(highLevelNodes.get(i));
					removedNodes.add(highLevelNodes.remove(i)); // Remove startNode from list.
				}
			}
			if(strata.get(strataCounter) != null)
			{
				strataCounter++;
			}
		}
		for(VisualNode vn: removedNodes)
		{
			graph.addVertex(vn);
		}
		for(VisualEdge ve: removedEdges)
		{
			if(graph.containsEdge(ve) != false)
			{
				graph.removeEdge(ve);
			}
			graph.addEdge(ve);
		}
	}
	
	
	public TreeMap<Integer, List<VisualNode>> viewStratificationLevels()
	{
		if(strata == null)
		{
			travel();
		}
		return strata;
	}
}
