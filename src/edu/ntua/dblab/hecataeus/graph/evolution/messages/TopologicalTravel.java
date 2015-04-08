package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.ArrayList;
import java.util.List;
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
	
	private int numberOfIncomingUsesEdges(VisualNode node, List<VisualNode> hln)
	{
		int toReturn = 0;
		for(VisualEdge e : node.getInEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) && removedEdges.contains(e) == false)
			{
				toReturn++;
			}
		}
		return(toReturn);
	}
	
	private int numberOfOutgoingUsesEdges(VisualNode node, List<VisualNode> hln)
	{
		int toReturn = 0;
		for(VisualEdge e : node.getOutEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) && removedEdges.contains(e) == false)
			{
				toReturn++;
			}
		}
		return(toReturn);
	}
	
	private List<VisualEdge> removeIncomingUsesEdgesAtConnectedNodes(VisualNode node, List<VisualNode> hln)
	{
		List<VisualNode> connectedNodes = new ArrayList<VisualNode>();
		for(VisualEdge e : node.getOutEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) && removedEdges.contains(e) == false)
			{
				connectedNodes.add(e.getToNode());
			}
		}
		List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
		for(VisualNode n : connectedNodes)
		{
			for(VisualEdge e : n.getInEdges())
			{
				if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getToNode()) && e.getFromNode() == node && removedEdges.contains(e) == false)
				{
					edgesToRemove.add(e);
				}
			}
		}
		return(edgesToRemove);
	}
	
	private List<VisualEdge> removeOutgoingUsesEdgesAtConnectedNodes(VisualNode node, List<VisualNode> hln)
	{
		List<VisualNode> connectedNodes = new ArrayList<VisualNode>();
		for(VisualEdge e : node.getInEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getFromNode()) && removedEdges.contains(e) == false)
			{
				connectedNodes.add(e.getFromNode());
			}
		}
		List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
		for(VisualNode n : connectedNodes)
		{
			for(VisualEdge e : n.getOutEdges())
			{
				if(e.getType() == EdgeType.EDGE_TYPE_USES && hln.contains(e.getFromNode()) && e.getToNode() == node && removedEdges.contains(e) == false)
				{
					edgesToRemove.add(e);
				}
			}
		}
		return(edgesToRemove);
	} 
	
	/**
	 * First are the nodes with incoming edges = 0 (startNode).
	 * They are removed with their outgoing edges (EDGE_TYPE_USES).
	 * Others (parentNode) that had only 1 incoming edge become startNode.
	 * Continue till you have no more nodes to check.
	 * */
	public void travel()
	{
		for(int i=0;i<graph.getVertices().size();i++)
		{
			graph.getVertices().get(i).ID=0;
		}
		List<VisualNode> highLevelNodes=graph.getVertices(NodeType.NODE_TYPE_QUERY);
		highLevelNodes.addAll(graph.getVertices(NodeType.NODE_TYPE_RELATION));
		highLevelNodes.addAll(graph.getVertices(NodeType.NODE_TYPE_VIEW));
		int highLevelNodeCounter = highLevelNodes.size();
		while(highLevelNodes.size() > 0)
		{
			List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
			List<VisualNode> nodesToRemove = new ArrayList<VisualNode>();
			for(VisualNode n: highLevelNodes)
			{
				if(numberOfIncomingUsesEdges(n, highLevelNodes) == 0 && removedNodes.contains(n) == false)
				{ // Found start of graph
					graph.findVertexByName(n.getName()).ID = highLevelNodeCounter;
					highLevelNodeCounter--;
					edgesToRemove.addAll(removeIncomingUsesEdgesAtConnectedNodes(n, highLevelNodes));
					if(removedNodes.contains(n) == false)
					{
						nodesToRemove.add(n);
					}
				}
			}
			for(VisualEdge e: edgesToRemove)
			{
				removedEdges.add(e);
			}
			for(VisualNode n: nodesToRemove)
			{
				removedNodes.add(n);
				highLevelNodes.remove(n);
			}
		}
	}
	
	/**
	 * In order to place the views in the space between the relations and the single relation queries, we need to know how many levels of views over views etc we have.
	 * We have in the same level all the nodes that depend to relations.
	 * Next we have those that depend to a view (and maybe relations)...
	 * Then those that depend to a view over view (and of course the previous stratum)... 
	 * @author pmanousi
	 * @return A tree map with the stratified levels of views.
	 */
	public TreeMap<Integer, List<VisualNode>> viewStratificationLevels()
	{
		TreeMap<Integer, List<VisualNode>> toReturn = new TreeMap<Integer, List<VisualNode>>();
		List<VisualNode> highLevelNodes = graph.getVertices(NodeType.NODE_TYPE_VIEW);
		int strataCounter = 0;
		while(highLevelNodes.size() > 0)
		{
			List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
			List<VisualNode> nodesToRemove = new ArrayList<VisualNode>();
			for(VisualNode n : highLevelNodes)
			{
				if(numberOfOutgoingUsesEdges(n, highLevelNodes) == 0 && removedNodes.contains(n) == false)
				{
					if(toReturn.get(strataCounter) == null)
					{
						toReturn.put(strataCounter, new ArrayList<VisualNode>());
					}
					toReturn.get(strataCounter).add(n);
					edgesToRemove.addAll(removeOutgoingUsesEdgesAtConnectedNodes(n, highLevelNodes));
					if(removedNodes.contains(n) == false)
					{
						nodesToRemove.add(n);
					}
				}
			}
			for(VisualEdge e: edgesToRemove)
			{
				removedEdges.add(e);
			}
			for(VisualNode n: nodesToRemove)
			{
				removedNodes.add(n);
				highLevelNodes.remove(n);
			}
			strataCounter++;
		}
		return toReturn;
	}
}
