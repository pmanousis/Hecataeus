/**
 * @author George Papastefanatos, National Technical University of Athens
 */

package edu.ntua.dblab.hecataeus.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.evolution.util.GraphUtilities;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

public class HecataeusMetricManager {
	
	public HecataeusMetricManager(){
		
	}
	 
	private static double coupling(VisualNode v, int t) {
		if(t > 1 && v.getNumberOfUsesEdges() < t) {
			return(1 - Math.log(v.getNumberOfUsesEdges()) / Math.log(t));
		}
		return(0);
	}
	
	private static double tblCoupling(VisualNode v) {
		double providersTotal = 0;
		double nodesInputTotal = 0;
		for(VisualEdge e: v.getOutEdges()) {
			if(e.getType() == EdgeType.EDGE_TYPE_USES) {
				providersTotal += e.getToNode().getOutEdges().get(0).getToNode().getOutEdges().size();
			}
			if(e.getType() == EdgeType.EDGE_TYPE_INPUT) {
				nodesInputTotal += e.getToNode().getOutEdges().size() - 1 /* there is always an EDGE_TYPE_FROM */;
			}
		}
		return(nodesInputTotal / providersTotal);
		
	}
	
	static int lastOrdinalIndexOf(String str, String substr, int n) {
	    int pos = str.lastIndexOf(substr);
	    while (--n > 0 && pos != -1)
	        pos = str.lastIndexOf(substr, pos + 1);
	    return pos;
	}
	
	static List<List<VisualNode>> groupby(List<VisualNode> queriesAndViews, int levelFromGround) {
		List<List<VisualNode>> queriesAndViewsRepresentatives = new ArrayList<List<VisualNode>>();
		queriesAndViewsRepresentatives.add(new ArrayList<VisualNode>());
		queriesAndViewsRepresentatives.get(0).add(queriesAndViews.get(0));	// initialization the first node goes to the first group
		for(VisualNode vn : queriesAndViews) {
			boolean found = false;
			for(int i = 0; i < queriesAndViewsRepresentatives.size(); i++) {
				if(queriesAndViewsRepresentatives.get(i).get(0).getFileName().substring(0, lastOrdinalIndexOf(queriesAndViewsRepresentatives.get(i).get(0).getFileName(), "/", levelFromGround)).equals(vn.getFileName().substring(0, lastOrdinalIndexOf(vn.getFileName(), "/", levelFromGround))) && queriesAndViewsRepresentatives.contains(vn) == false) { // find if it should be part of a group AND not already present
					queriesAndViewsRepresentatives.get(i).add(vn);
					found = true;
					break;
				}
			}
			if (found == false) { // otherwise append to a new group
				queriesAndViewsRepresentatives.add(new ArrayList<VisualNode>());
				queriesAndViewsRepresentatives.get(queriesAndViewsRepresentatives.size() - 1).add(vn);
			}
		}
		return(queriesAndViewsRepresentatives);
	}
	
	/** @author pmanousi 1 - log_(1/t)(d(s)/t) as described in paper.
	 * ability to have a level selection goes here or in the viewer?
	 * I shall put it here, and I will talk to pv for making sure. 
	 * */
	 public static String coupling(VisualGraph activeGraph) {
		 String toReturn = "";
		 List<VisualNode> rels = activeGraph.getVertices(NodeType.NODE_TYPE_RELATION);
		 List<VisualNode> queriesAndViews = activeGraph.getVertices(NodeType.NODE_TYPE_QUERY);
		 queriesAndViews.addAll(activeGraph.getVertices(NodeType.NODE_TYPE_VIEW));
		 
		 List<List<VisualNode>> qvgroupreps = groupby(queriesAndViews, 1);
		 
		 // Queries
		 int t = rels.size();
		 for(List<VisualNode> lvn : qvgroupreps) {
			 for(VisualNode v: lvn) {
				 toReturn += "file: " + v.getFileName()
				 + " query: " + v.getSQLDefinition()
//				 + " V: " + v.getName()
				 + " db-coupling: " + coupling(v, t)
//				 + " tbls-coupling: " + tblCoupling(v)
				 + "\n";
			 }
		 }
		 
		 // Relations
//		 int [][] relationsQueries = new int[rels.size()][qvgroupreps.size()];
//		 for(VisualNode r: rels) {
//			 System.err.print(r.getName() + ",");
//		 }
//		 System.err.println("");
//		 List<String> fnames = new ArrayList<String>();
//		 for(List<VisualNode> lvn : qvgroupreps) {
//			 for(VisualNode qv: lvn) {
//				 for(VisualNode r: rels) {
//					 int val = 0;
//					 for(VisualEdge e: qv.getOutEdges()) {
//						 
//						 if(e.getToNode().equals(r)) {
//							 val = 1;
//						 }
//					 }
//					 System.err.print(val + ",");
//				 }
//				 System.err.println(qv.getName());
//			 }
//		 }
		 return(toReturn);
	 }
	
	 public static   int countNodes(EvolutionGraph  graph) {
		 return graph.getVertices().size();
	 }
	 
	 public static   int countEdges(EvolutionGraph  graph) {
		 return graph.getEdges().size();
	 }
	 
	public static   int countPolicies(
		Collection<EvolutionNode> collection, PolicyType policyType) {
		 int policies =0;
		for (EvolutionNode node : collection) {
			for (EvolutionPolicy p : node.getPolicies()) {
     			if (p.getPolicyType().equals(policyType)) {
     				policies++;
     			}
     		}
		 }
		 return policies;
	 }
	 
	public static   int countEvents(
		Collection<EvolutionNode> collection) {
		 int events =0;
		for (EvolutionNode node : collection) {
			 events += node.getEvents().size();
		 }
		 return events;
	 }
	 
	 public static   int inDegree(EvolutionNode  node) {
		 return node.getInEdges().size();
	 }
	 
	 public static   int outDegree(EvolutionNode  node) {
		 return node.getOutEdges().size();
	 }
	 
	 public static   int degree(EvolutionNode node) {
		 return node.getInEdges().size()+node.getOutEdges().size();
	 }
	 
	 public static   int inDegree( EvolutionNode node, EdgeType edgeType) {
		 int countedges = 0;
		 for (EvolutionEdge edge : node.getInEdges()) {
			if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static   int outDegree( EvolutionNode node, EdgeType edgeType) {
		 int countedges = 0;
		 for (EvolutionEdge edge :  node.getOutEdges()) {
				 if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static   int degree( EvolutionNode node, EdgeType edgeType) {
		 int countInedges = 0;
		 for (EvolutionEdge edge :  node.getInEdges()) {
				 if (edge.getType()==edgeType) {
				 countInedges++;
			 }
		}
		 int countOutedges = 0;
		 for (EvolutionEdge edge :  node.getOutEdges()) {
			 if (edge.getType()==edgeType) {
				 countOutedges++;
			 }
		}		
		return countInedges+countOutedges;
	 }
	 
	 public static   int inStrength(EvolutionNode node, EvolutionGraph  graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( EvolutionNode toNode: graph.getVertices(NodeCategory.MODULE)){
				nodeStrength += GraphUtilities.getConnections(graph.getModule(toNode), graph.getModule(node));
				}
			}
			return nodeStrength;
	 }
	
	 public static   int outStrength( EvolutionNode node, EvolutionGraph  graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( EvolutionNode toNode : graph.getVertices(NodeCategory.MODULE)) {
				nodeStrength += GraphUtilities.getConnections(graph.getModule(node), graph.getModule(toNode));
				}
			}
			return nodeStrength;
	 }
	
	 public static   int strength(EvolutionNode node, EvolutionGraph  graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE){
				for (EvolutionNode toNode : graph.getVertices(NodeCategory.MODULE)) {
				nodeStrength += GraphUtilities.getConnections(graph.getModule(node), graph.getModule(toNode));
				nodeStrength += GraphUtilities.getConnections(graph.getModule(toNode), graph.getModule(node));
				}
			}
			return nodeStrength;
	 }
	 
	 public static  int inWeightedStrength( EvolutionNode node, EvolutionGraph  graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( EvolutionNode toNode : graph.getVertices()) {
					if (toNode.getType().getCategory()== NodeCategory.MODULE) {
					nodeStrength += toNode.getFrequency() *
						GraphUtilities.getConnections(graph.getModule(toNode), graph.getModule(node));
					}
				}
			}
			return nodeStrength;
	 }
	 
	 public static   int inWeightedDegree( EvolutionNode node) {
		 return node.getFrequency();
	 }
	 
	 public static   int transitiveDegree(EvolutionNode node) {
		   return inTransitiveDegree(node) + outTransitiveDegree(node);
	 }
	 
	 public static   int inTransitiveDegree(EvolutionNode node) {
		 int countDegree =0;
		 for (EvolutionEdge edge: node.getInEdges()) {
			 countDegree++;
			 countDegree+=inTransitiveDegree(edge.getFromNode());
		 }
//		 if (node.getType().getCategory()== NodeCategory.MODULE) {
//			 for (EvolutionEdge edge: node.getInEdges()) {
//				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//					 countDegree+=inTransitiveDegree(edge.getFromNode());
//				 }
//			 }
//		 }else {
//			 for (EvolutionEdge edge: node.getInEdges()) {
//				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//				 }
//				 countDegree+=inTransitiveDegree(edge.getFromNode());
//					
//			}	
//		 }
		 	 
		 return countDegree;
	 }
	 
	 public static   int outTransitiveDegree( EvolutionNode node) {
		 int countDegree =0;
		 for (EvolutionEdge edge: node.getOutEdges()) {
			 countDegree++;
			 countDegree+=outTransitiveDegree(edge.getToNode());
		 }
//		 if (node.getType().getCategory()== NodeCategory.MODULE) {
//			 for (EvolutionEdge edge: node.getOutEdges()) {
//				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//					 countDegree+=outTransitiveDegree(edge.getToNode());
//				 }
//			 }
//		 }else {
//			 for (EvolutionEdge edge: node.getOutEdges()) {
//				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//				 }
//				 countDegree+=outTransitiveDegree(edge.getToNode());
//					
//			}	
//		 }
//		 	 
		 return countDegree;
	 }
	 
	 public static   int outTransitiveModuleDegree(EvolutionNode node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (EvolutionEdge edge: node.getOutEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=outTransitiveModuleDegree(edge.getToNode());
				 }
		 return countDegree;
	 }
	 public static   int inTransitiveModuleDegree(EvolutionNode node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (EvolutionEdge edge: node.getInEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=inTransitiveModuleDegree(edge.getFromNode());
				 }
		 return countDegree;
	 }
		 
	 public static   int outTransitiveStrength(EvolutionNode node, EvolutionGraph  graph) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (EvolutionEdge edge: node.getOutEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 EvolutionNode toNode =   edge.getToNode();
					countDegree += GraphUtilities.getConnections(graph.getModule(node), graph.getModule(toNode));
					 countDegree+=outTransitiveStrength(toNode,graph);
				 }
		 return countDegree;
	 }
	 public static   int inTransitiveStrength(EvolutionNode node, EvolutionGraph  graph) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (EvolutionEdge edge: node.getInEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 EvolutionNode fromNode =   edge.getFromNode();
					countDegree += GraphUtilities.getConnections(graph.getModule(fromNode), graph.getModule(node));
					 countDegree+=inTransitiveStrength(fromNode,graph);
				 }
		 return countDegree;
	 }
	 
	 
	 public static   int inPolicyTransitiveDegree(EvolutionEvent event, EvolutionGraph  graph) {
		 int countDegree =0;
		 //clear all statuses
		 for (EvolutionNode aNode : graph.getVertices())
			 aNode.setStatus(StatusType.NO_STATUS,true);
		 for (EvolutionEdge aEdge: graph.getEdges())
			 aEdge.setStatus(StatusType.NO_STATUS,true);
		 graph.initializeChange(event);
		 
		 for (EvolutionNode aNode : graph.getVertices())
			 if (aNode.getStatus()!=StatusType.BLOCKED
					 &&aNode.getStatus()!=StatusType.NO_STATUS)
				 countDegree ++;
		 return countDegree;
	 }
	 
	 public static   int outPolicyTransitiveDegree( EvolutionNode node, EvolutionEvent event) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE) {
			 for (EvolutionEdge edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 if (edge.getToNode().getPolicies().size()>0) {
						 for ( EvolutionPolicy p : edge.getToNode().getPolicies()) {
							 //TODO: matsakonia,ypothetw oti gia kathe kombo 
							 //yparxei mia mono policy h opoia einai block
							 //FIX: prepei na kanei iterate over all policies.
							 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=PolicyType.BLOCK) {
								 countDegree++;
								 countDegree+=outPolicyTransitiveDegree(  edge.getToNode(),event);
							 }
						 }
					 }else {
						 countDegree+=outPolicyTransitiveDegree(  edge.getToNode(),event);
						 countDegree++;
					 }
				 }
			 }
		 }else {
			 for (EvolutionEdge edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 if (edge.getToNode().getPolicies().size()>0) {
					 for (EvolutionPolicy p: edge.getToNode().getPolicies()) {
						 //TODO: matsakonia,ypothetw oti gia kathe kombo 
						 //yparxei mia mono policy h opoia einai block
						 //FIX: prepei na kanei iterate over all policies.
						 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=PolicyType.BLOCK) {
							 countDegree++;
							 countDegree+=outPolicyTransitiveDegree(  edge.getToNode(),event);
						 }
					 }
				 }else countDegree+=outPolicyTransitiveDegree(  edge.getToNode(), event);
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static   float entropyGraph(EvolutionGraph  graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<EvolutionNode> nodes = new ArrayList<EvolutionNode>(graph.getVertices(NodeCategory.MODULE));
		 double[] noPaths= new double[nodes.size()];
		 for ( EvolutionNode srcNode: nodes) {
				 noPaths[nodes.indexOf(srcNode)]=0;
				 for (EvolutionNode toNode : nodes) {
					 if (!toNode.equals(srcNode)){
					noPaths[nodes.indexOf(srcNode)] += GraphUtilities.getPaths(graph, srcNode, toNode);
					 }
				 }
				 totalPaths +=noPaths[nodes.indexOf(srcNode)];
			 
		 }
			 
		 //compute entropy
		 for (int k = 0; k < noPaths.length; k++) {
			 if (noPaths[k] > 0) {
				countEntropy += -(noPaths[k] / totalPaths) * (Math.log(noPaths[k] / totalPaths)/Math.log(2));
			 }
		 }
		return countEntropy;
		  
	 }
	 
	 
	 public static  float entropyOutPerNode( EvolutionNode srcNode,EvolutionGraph  graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<EvolutionNode> nodes = new ArrayList<EvolutionNode>(graph.getVertices(NodeCategory.MODULE));
		 double[] noPaths= new double[nodes.size()];
		 
		 for (EvolutionNode toNode:nodes) {
			 if ((!toNode.equals(srcNode)))
//					 &&((toNode.getType()==NodeType.NODE_TYPE_QUERY
//							 ||toNode.getType()==NodeType.NODE_TYPE_RELATION
//							 ||toNode.getType()==NodeType.NODE_TYPE_VIEW)))
			 {
				 noPaths[nodes.indexOf(toNode)] = GraphUtilities.getPaths(graph, srcNode, toNode);
				 totalPaths +=noPaths[nodes.indexOf(toNode)];
			 }
		 }
		 		
			 
		 //compute entropy
		 for (int k = 0; k < noPaths.length; k++) {
			 if (noPaths[k] > 0) {
				countEntropy += -(noPaths[k] / totalPaths) * (Math.log(noPaths[k] / totalPaths)/Math.log(2));
			 }
		 }
		return countEntropy;
		  
	 }
	 
	 public static  float entropyInPerNode(EvolutionNode trgNode,EvolutionGraph  graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<EvolutionNode> nodes = new ArrayList<EvolutionNode>(graph.getVertices(NodeCategory.MODULE));
		 double[] noPaths= new double[nodes.size()];
		 
		 for ( EvolutionNode fromNode : nodes) {
			 if (!fromNode.equals(trgNode)){
				noPaths[nodes.indexOf(fromNode)] = GraphUtilities.getPaths(graph, fromNode, trgNode);
				 totalPaths +=noPaths[nodes.indexOf(fromNode)];
			 }
		 }
		 		
			 
		 //compute entropy
		 for (int k = 0; k < noPaths.length; k++) {
			 if (noPaths[k] > 0) {
				countEntropy += -(noPaths[k] / totalPaths) * (Math.log(noPaths[k] / totalPaths)/Math.log(2));
			 }
		 }
		return countEntropy;
		  
	 }

	 /***
	  * Tests is a given graph is bipartite, ]
	  * It picks a random vertex and assigns all vertices with odd distance in 
	  * group 1 and even distance in group 2.
	  * It then evaluates whether all edges have adjacent vertices belonging to different groups 
	  * @param graph
	  * @return true if graph is bipartite
	  */
	 public static   boolean isBipartite(EvolutionGraph  graph) {
		 
		 List<EvolutionNode> oddGroup = new ArrayList<EvolutionNode>();
		 List<EvolutionNode> evenGroup = new ArrayList<EvolutionNode>();
		 EvolutionNode v=  graph.getVertices().iterator().next();
		 oddGroup.add(v);
		 evaluateBipartite(v,oddGroup, evenGroup);
		 for (EvolutionEdge edge : graph.getEdges()) {
			 if ((oddGroup.contains(edge.getFromNode())&& oddGroup.contains(edge.getToNode()))
					 ||(evenGroup.contains(edge.getFromNode())&& evenGroup.contains(edge.getToNode())))
				 return false;
		 }
		 return true;
	 }
	 
	 private static   void evaluateBipartite( EvolutionNode v, List<EvolutionNode> oddGroup, List<EvolutionNode> evenGroup) {
		 for(EvolutionEdge e :v.getInEdges()) {
			 EvolutionNode y =    e.getFromNode();
			 if (oddGroup.contains(v)) { 
				 if (!evenGroup.contains(y)) {
					 evenGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
			 else {
				 if (!oddGroup.contains(y)) {
					 oddGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
		 }
		 for (EvolutionEdge e : v.getOutEdges()) {
			 EvolutionNode y=   e.getToNode();
			 if (oddGroup.contains(v)) { 
				 if (!evenGroup.contains(y)) {
					 evenGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
			 else {
				 if (!oddGroup.contains(y)) {
					 oddGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
		 }
		 
	 }
}

