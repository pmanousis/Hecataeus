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
	
	public List<VisualEdge> removeIncomingUsesEdgesAtConnectedNodes(VisualNode node)
	{
		List<VisualNode> connectedNodes = new ArrayList<VisualNode>();
		for(VisualEdge e : node.getOutEdges())
		{
			if(e.getType() == EdgeType.EDGE_TYPE_USES)
			{
				connectedNodes.add(e.getToNode());
			}
		}
		List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
		for(VisualNode n : connectedNodes)
		{
			for(VisualEdge e : n.getInEdges())
			{
				if(e.getType() == EdgeType.EDGE_TYPE_USES && e.getFromNode() == node)
				{
					edgesToRemove.add(e);
				}
			}
			removedEdges.addAll(edgesToRemove);
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
			List<VisualEdge> edgesToRemove = new ArrayList<VisualEdge>();
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
					edgesToRemove.addAll(removeIncomingUsesEdgesAtConnectedNodes(highLevelNodes.get(i)));
					removedNodes.add(highLevelNodes.remove(i)); // Remove startNode from list.
				}
			}
			for(VisualEdge e: edgesToRemove)
			{
				graph.removeEdge(e);
			}
			edgesToRemove.clear();
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
	
	private boolean mapsComparison(TreeMap<Integer, List<VisualNode>> one, TreeMap<Integer, List<VisualNode>> two)
	{
		if(one.size() != two.size())
		{
			return(false);
		}
		boolean toReturn = true;
		ArrayList<Integer> keysOne = new ArrayList<Integer>(one.keySet());
		ArrayList<Integer> keysTwo = new ArrayList<Integer>(two.keySet());
		for(int i = 0; i < keysOne.size(); i++)
		{
		    if(one.get(keysOne.get(i)).equals(two.get(keysTwo.get(i))) == false || keysOne.get(i) != (keysTwo).get(i))
		    {
		    	toReturn = false;
		    }
		}
		return(toReturn);
	}
	
	/**
	 * In order to terminate early enough the "squizing" of TreeMaps, we check to see if there are no differences between steps. To achieve that, we need a deep copy clone of the TreeMap before the modifications we want to do.
	 * @author pmanousi
	 * @param original The TreeMap we want to copy
	 * @return A TreeMap that has the same contents with the original
	 */
	private TreeMap<Integer, List<VisualNode>> deepCopy(TreeMap<Integer, List<VisualNode>> original)
	{
		TreeMap<Integer, List<VisualNode>> toReturn = new TreeMap<Integer, List<VisualNode>>();
		for (Map.Entry<Integer, List<VisualNode>> entry : original.entrySet())
		{
			List<VisualNode> toAdd = new ArrayList<VisualNode>();
			for(VisualNode n: entry.getValue())
			{
				toAdd.add(n);
			}
			toReturn.put(entry.getKey(),toAdd);
		}
		return(toReturn);
	}
	
	/**
	 * Removes from obj the keys that have no contents.
	 * @author pmanousi
	 * @param obj The TreeMap that we want to reduce its set of keys.
	 */
	private void cleanUp(TreeMap<Integer, List<VisualNode>> obj)
	{
		List<Integer> toRemove = new ArrayList<Integer>();
		for (Map.Entry<Integer, List<VisualNode>> entry : obj.entrySet())
		{
			if(entry.getValue().isEmpty())
			{
				toRemove.add(entry.getKey());
			}
		}
		for(Integer i: toRemove)
		{
			obj.remove(i);
		}
	}
	
	/**
	 * In order to place the views in the space between the relations and the single relation queries, we need to know how many levels of views over views etc we have. This function (with the help of travel) has these values. We just have to "squize" them in order to have many of them in the relation-single relation space. 
	 * @author pmanousi
	 * @return A tree map with the stratified levels of views.
	 */
	public TreeMap<Integer, List<VisualNode>> viewStratificationLevels()
	{
		TreeMap<Integer, List<VisualNode>> toReturn = new TreeMap<Integer, List<VisualNode>>();
		if(strata == null)
		{
			travel();
		}
		ArrayList<Integer> keys = new ArrayList<Integer>(strata.keySet());
		for(int i=keys.size()-1; i>=0;i--)
		{
		    toReturn.put(keys.get(keys.size()-1)-keys.get(i), strata.get(keys.get(i)));
		}
		int contentsLength = 0;
		for(int i =0;i < toReturn.size();i++)
		{
			contentsLength += toReturn.get(i).size();
		}
		TreeMap<Integer, List<VisualNode>> toCompare = null;
		for(int j=0;j<keys.size()*contentsLength;j++)
		{
			toCompare = deepCopy(toReturn);
			for (int i=1; i<keys.size();i++)
			{
				boolean pushToPreviousLevel = true;
				VisualNode current = null;
				for(VisualNode vn1: toReturn.get(i))
				{
					pushToPreviousLevel = true;
					current = vn1;
					for(VisualEdge e1: current.getOutEdges())
					{
						for(VisualNode vn0: toReturn.get(i-1))
						{
							if(e1.getType() == EdgeType.EDGE_TYPE_USES && e1.getToNode() == vn0)
							{
								pushToPreviousLevel = false;
								break;
							}
						}
						if(pushToPreviousLevel == false)
						{
							break;
						}
					}
				}
				if(pushToPreviousLevel == true && current != null)
				{
					toReturn.get(i-1).add(current);
					toReturn.get(i).remove(current);
				}
			}
			if(mapsComparison(toCompare,toReturn) == true)
			{
				cleanUp(toReturn);
				break;
			}
		}
		return toReturn;
	}
}
