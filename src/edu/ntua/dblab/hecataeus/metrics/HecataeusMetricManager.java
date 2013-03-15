/**
 * @author George Papastefanatos, National Technical University of Athens
 */

package edu.ntua.dblab.hecataeus.metrics;

import java.util.ArrayList;
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

public class HecataeusMetricManager {
	
	public HecataeusMetricManager(){
		
	}
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int countNodes(EvolutionGraph<V, E> graph) {
		 return graph.getVertices().size();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int countEdges(EvolutionGraph<V, E> graph) {
		 return graph.getEdges().size();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int countPolicies(List<V> nodes, PolicyType policyType) {
		 int policies =0;
		 for (V node: nodes) {
			 for (EvolutionPolicy<V> p : node.getPolicies()) {
     			if (p.getPolicyType().equals(policyType)) {
     				policies++;
     			}
     		}
		 }
		 return policies;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int countEvents(List<V> nodes) {
		 int events =0;
		 for (V node: nodes) {
			 events += node.getEvents().size();
		 }
		 return events;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inDegree(V  node) {
		 return node.getInEdges().size();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outDegree(V  node) {
		 return node.getOutEdges().size();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int degree(V node) {
		 return node.getInEdges().size()+node.getOutEdges().size();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inDegree( V node, EdgeType edgeType) {
		 int countedges = 0;
		 for (E edge : node.getInEdges()) {
			if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outDegree( V node, EdgeType edgeType) {
		 int countedges = 0;
		 for (E edge :  node.getOutEdges()) {
				 if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int degree( V node, EdgeType edgeType) {
		 int countInedges = 0;
		 for (E edge :  node.getInEdges()) {
				 if (edge.getType()==edgeType) {
				 countInedges++;
			 }
		}
		 int countOutedges = 0;
		 for (E edge :  node.getOutEdges()) {
			 if (edge.getType()==edgeType) {
				 countOutedges++;
			 }
		}		
		return countInedges+countOutedges;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inStrength(V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( V toNode: graph.getVertices(NodeCategory.MODULE)){
						nodeStrength+= graph.getConnections(graph.getModule(toNode), graph.getModule(node));
				}
			}
			return nodeStrength;
	 }
	
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outStrength( V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( V toNode : graph.getVertices(NodeCategory.MODULE)) {
						nodeStrength+= graph.getConnections(graph.getModule(node), graph.getModule(toNode));
				}
			}
			return nodeStrength;
	 }
	
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int strength(V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE){
				for (V toNode : graph.getVertices(NodeCategory.MODULE)) {
						nodeStrength+= graph.getConnections(graph.getModule(node), graph.getModule(toNode));
						nodeStrength+= graph.getConnections(graph.getModule(toNode), graph.getModule(node));
				}
			}
			return nodeStrength;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge>int inWeightedStrength( V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( V toNode : graph.getVertices()) {
					if (toNode.getType().getCategory()== NodeCategory.MODULE) {
						nodeStrength+= toNode.getFrequency()*graph.getConnections(graph.getModule(toNode), graph.getModule(node));
					}
				}
			}
			return nodeStrength;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inWeightedDegree( V node) {
		 return node.getFrequency();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int transitiveDegree(V node) {
		   return inTransitiveDegree(node) + outTransitiveDegree(node);
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inTransitiveDegree(V node) {
		 int countDegree =0;
		 for (E edge: node.getInEdges()) {
			 countDegree++;
			 countDegree+=inTransitiveDegree(edge.getFromNode());
		 }
//		 if (node.getType().getCategory()== NodeCategory.MODULE) {
//			 for (E edge: node.getInEdges()) {
//				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//					 countDegree+=inTransitiveDegree(edge.getFromNode());
//				 }
//			 }
//		 }else {
//			 for (E edge: node.getInEdges()) {
//				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//				 }
//				 countDegree+=inTransitiveDegree(edge.getFromNode());
//					
//			}	
//		 }
		 	 
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outTransitiveDegree( V node) {
		 int countDegree =0;
		 for (E edge: node.getOutEdges()) {
			 countDegree++;
			 countDegree+=outTransitiveDegree(edge.getToNode());
		 }
//		 if (node.getType().getCategory()== NodeCategory.MODULE) {
//			 for (E edge: node.getOutEdges()) {
//				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
//					 countDegree++;
//					 countDegree+=outTransitiveDegree(edge.getToNode());
//				 }
//			 }
//		 }else {
//			 for (E edge: node.getOutEdges()) {
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
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outTransitiveModuleDegree(V node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (E edge: node.getOutEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=outTransitiveModuleDegree(edge.getToNode());
				 }
		 return countDegree;
	 }
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inTransitiveModuleDegree(V node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (E edge: node.getInEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=inTransitiveModuleDegree(edge.getFromNode());
				 }
		 return countDegree;
	 }
		 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outTransitiveStrength(V node, EvolutionGraph<V,E> graph) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (E edge: node.getOutEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 V toNode = (V) edge.getToNode();
					 countDegree+=graph.getConnections(graph.getModule(node), graph.getModule(toNode));
					 countDegree+=outTransitiveStrength(toNode,graph);
				 }
		 return countDegree;
	 }
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inTransitiveStrength(V node, EvolutionGraph<V,E> graph) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE)
			 for (E edge: node.getInEdges()) 
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 V fromNode = (V) edge.getFromNode();
					 countDegree+=graph.getConnections(graph.getModule(fromNode), graph.getModule(node));
					 countDegree+=inTransitiveStrength(fromNode,graph);
				 }
		 return countDegree;
	 }
	 
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inPolicyTransitiveDegree(EvolutionEvent<V> event, EvolutionGraph<V,E> graph) {
		 int countDegree =0;
		 //clear all statuses
		 for (V aNode : graph.getVertices())
			 aNode.setStatus(StatusType.NO_STATUS,true);
		 for (E aEdge: graph.getEdges())
			 aEdge.setStatus(StatusType.NO_STATUS,true);
		 graph.initializeChange(event);
		 
		 for (V aNode : graph.getVertices())
			 if (aNode.getStatus()!=StatusType.BLOCKED
					 &&aNode.getStatus()!=StatusType.NO_STATUS)
				 countDegree ++;
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outPolicyTransitiveDegree( V node, EvolutionEvent<V> event) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE) {
			 for (E edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 if (edge.getToNode().getPolicies().size()>0) {
						 for ( EvolutionPolicy<V> p : edge.getToNode().getPolicies()) {
							 //TODO: matsakonia,ypothetw oti gia kathe kombo 
							 //yparxei mia mono policy h opoia einai block
							 //FIX: prepei na kanei iterate over all policies.
							 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=PolicyType.BLOCK) {
								 countDegree++;
								 countDegree+=outPolicyTransitiveDegree((V) edge.getToNode(),event);
							 }
						 }
					 }else {
						 countDegree+=outPolicyTransitiveDegree((V) edge.getToNode(),event);
						 countDegree++;
					 }
				 }
			 }
		 }else {
			 for (E edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 if (edge.getToNode().getPolicies().size()>0) {
					 for (EvolutionPolicy<V> p: edge.getToNode().getPolicies()) {
						 //TODO: matsakonia,ypothetw oti gia kathe kombo 
						 //yparxei mia mono policy h opoia einai block
						 //FIX: prepei na kanei iterate over all policies.
						 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=PolicyType.BLOCK) {
							 countDegree++;
							 countDegree+=outPolicyTransitiveDegree((V) edge.getToNode(),event);
						 }
					 }
				 }else countDegree+=outPolicyTransitiveDegree((V) edge.getToNode(), event);
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> float entropyGraph(EvolutionGraph<V,E> graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<V> nodes = new ArrayList<V>(graph.getVertices(NodeCategory.MODULE));
		 double[] noPaths= new double[nodes.size()];
		 for ( V srcNode: nodes) {
				 noPaths[nodes.indexOf(srcNode)]=0;
				 for (V toNode : nodes) {
					 if (!toNode.equals(srcNode)){
						 noPaths[nodes.indexOf(srcNode)] += graph.getPaths(srcNode, toNode);
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
	 
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge>float entropyOutPerNode( V srcNode,EvolutionGraph<V,E> graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<V> nodes = new ArrayList<V>(graph.getVertices(NodeCategory.MODULE));
		 double[] noPaths= new double[nodes.size()];
		 
		 for (V toNode:nodes) {
			 if ((!toNode.equals(srcNode)))
//					 &&((toNode.getType()==NodeType.NODE_TYPE_QUERY
//							 ||toNode.getType()==NodeType.NODE_TYPE_RELATION
//							 ||toNode.getType()==NodeType.NODE_TYPE_VIEW)))
			 {
				 noPaths[nodes.indexOf(toNode)] = graph.getPaths(srcNode, toNode);
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
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge>float entropyInPerNode(V trgNode,EvolutionGraph<V,E> graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<V> nodes = new ArrayList<V>(graph.getVertices(NodeCategory.MODULE));
		 double[] noPaths= new double[nodes.size()];
		 
		 for ( V fromNode : nodes) {
			 if (!fromNode.equals(trgNode)){
				 noPaths[nodes.indexOf(fromNode)] = graph.getPaths(fromNode, trgNode);
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
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> boolean isBipartite(EvolutionGraph<V, E> graph) {
		 
		 List<V> oddGroup = new ArrayList<V>();
		 List<V> evenGroup = new ArrayList<V>();
		 V v=  graph.getVertices().iterator().next();
		 oddGroup.add(v);
		 evaluateBipartite(v,oddGroup, evenGroup);
		 for (E edge : graph.getEdges()) {
			 if ((oddGroup.contains(edge.getFromNode())&& oddGroup.contains(edge.getToNode()))
					 ||(evenGroup.contains(edge.getFromNode())&& evenGroup.contains(edge.getToNode())))
				 return false;
		 }
		 return true;
	 }
	 
	 private static <V extends EvolutionNode<E>,E extends EvolutionEdge> void evaluateBipartite( V v, List<V> oddGroup, List<V> evenGroup) {
		 for(E e :v.getInEdges()) {
			 V y =  (V) e.getFromNode();
			 if (oddGroup.contains(v)) { 
				 if (!evenGroup.contains(y)) {
					 evenGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
			 else {
				 if (!oddGroup.contains(y)) {
					 oddGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
		 }
		 for (E e : v.getOutEdges()) {
			 V y= (V) e.getToNode();
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

