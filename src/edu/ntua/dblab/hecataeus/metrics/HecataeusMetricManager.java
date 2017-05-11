/**
 * @author George Papastefanatos, National Technical University of Athens
 */

package edu.ntua.dblab.hecataeus.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.util.GraphUtilities;

public class HecataeusMetricManager {
	
	public HecataeusMetricManager(){
		
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

