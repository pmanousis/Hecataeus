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
	
	public TopologicalTravel(HecataeusViewer viewer)
	{
		v=viewer;
	}

	public TopologicalTravel(VisualGraph g)
	{
		graph = g;
	}
	
	/**
	 * First are the nodes with incoming edges = 0 (startNode).
	 * They are removed with their outgoing edges (EDGE_TYPE_USES).
	 * Others (parentNode) that had only 1 incoming edge become startNode.
	 * Continue till you have no more nodes to check.
	 * */
	public void travel()
	{
		for(int i=0;i<v.graph.getVertices().size();i++)
		{
			v.graph.getVertices().get(i).ID=0;
		}
		List<VisualNode> highLevelNodes=v.graph.getVertices(NodeType.NODE_TYPE_QUERY);
		highLevelNodes.addAll(v.graph.getVertices(NodeType.NODE_TYPE_RELATION));
		highLevelNodes.addAll(v.graph.getVertices(NodeType.NODE_TYPE_VIEW));
		int counter=highLevelNodes.size();
		for(int i=0;i<highLevelNodes.size();i++)
		{
			for(int j=0;j<highLevelNodes.get(i).getInEdges().size();j++)
			{	// Remove EDGE_TYPE_CONTAINS since later they gave InEdges.size()=1, not 0.
				if(highLevelNodes.get(i).getInEdges().get(j).getType()==EdgeType.EDGE_TYPE_CONTAINS)
				{
					highLevelNodes.get(i).getInEdges().remove(j);
				}
			}
		}
		while(highLevelNodes.size()>0)
		{
			for(int i=0;i<highLevelNodes.size();i++)
			{
				if(highLevelNodes.get(i).getInEdges().size()==0)
				{	// Found start of graph
					VisualNode startNode=highLevelNodes.get(i);	// Just initialising
					// Assign IDs (maybe later call a function to assign keys to its children)
					v.graph.findVertexByName(startNode.getName()).ID=counter;
					/**
					 * If PMTopologicalTravel becomes abstruct,
					 * the previous code could be a function (in case I want to give IDs inside modules)
					 * and give an abstruct step over here to do the work.
					 * */
					counter--;
					for(int j=0;j<startNode.getOutEdges().size();j++)
					{	// Go to his parents
						if(startNode.getOutEdges().get(j).getType()==EdgeType.EDGE_TYPE_USES)
						{	// Found one of his parents.
							String name=startNode.getOutEdges().get(j).getToNode().getName();
							VisualNode parentNode=startNode;	// Just initialising
							for(int k=0;k<highLevelNodes.size();k++)
							{
								if(highLevelNodes.get(k).getName().equals(name))
								{
									parentNode=highLevelNodes.get(k);
									for(int l=0;l<parentNode.getInEdges().size();l++)
									{	// Go to parentNode, remove the edge to eventually become startNode 
										if(parentNode.getInEdges().get(l).getFromNode().equals(startNode))
										{	// Be sure that you remove the correct edge (although unneeded).
											parentNode.getInEdges().remove(l);
											break;
										}
									}
								}
							}
						}
					}
					highLevelNodes.remove(i);	// Remove startNode from list.
				}
			}
		}
	}
	
	
	public TreeMap<Double, ArrayList<VisualNode>> travelLevel()
	{
		List<VisualNode> highLevelNodes= graph.getVertices(NodeType.NODE_TYPE_VIEW);
		Double counter= (double)highLevelNodes.size();
		for(int i=0;i<highLevelNodes.size();i++)
		{
			for(int j=0;j<highLevelNodes.get(i).getInEdges().size();j++)
			{	// Remove EDGE_TYPE_CONTAINS since later they gave InEdges.size()=1, not 0.
				if(highLevelNodes.get(i).getInEdges().get(j).getType()==EdgeType.EDGE_TYPE_CONTAINS)
				{
					highLevelNodes.get(i).getInEdges().remove(j);
				}
			}
		}
		TreeMap<Double, ArrayList<VisualNode>> layers = new TreeMap<Double, ArrayList<VisualNode>>(); 
		double layerCounter = 0.0;
		double eva = 0.0;
		while(highLevelNodes.size()>0)
		{
			ArrayList<VisualNode> sameLayerNodes = new ArrayList<VisualNode>();
			for(int i=0;i<highLevelNodes.size();i++)
			{
				if(highLevelNodes.get(i).getInEdges().size()==layerCounter)
				{
					sameLayerNodes.add(highLevelNodes.get(i));
				}
			}
			if(sameLayerNodes.size()>0){
				highLevelNodes.removeAll(sameLayerNodes);	// Remove startNode from list.
				layers.put(eva, sameLayerNodes);
				eva++;
			}
			layerCounter++;
		}
		return layers;
	}
}
