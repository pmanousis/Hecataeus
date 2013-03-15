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
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;

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
		 for (  EvolutionNode<E> node: nodes) {
			 for (EvolutionPolicy p : node.getPolicies()) {
     			if (p.getPolicyType().equals(policyType)) {
     				policies++;
     			}
     		}
		 }
		 return policies;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int countEvents(List<V> nodes) {
		 int events =0;
		 for (EvolutionNode<E> node: nodes) {
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
		 for (EvolutionEdge edge : node.getInEdges()) {
			if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outDegree( V node, EdgeType edgeType) {
		 int countedges = 0;
		 for (EvolutionEdge edge :  node.getOutEdges()) {
				 if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int degree( V node, EdgeType edgeType) {
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
	 
	 /**
	  * calculates the strength between two modules (subGraphs) as
	  * the number of all dependency edges between these modules
	  * directing from the fromModule towards the toModule
	  * @param fromModule is the parent node of outgoing edges module
	  * @param toModule is the parent node of incoming edges module
	  * @return strength
	  */
	 private static <V extends EvolutionNode<E>,E extends EvolutionEdge> int strength(List<V> fromModule, List<V> toModule) {
		 List<EvolutionEdge> strength = new ArrayList<EvolutionEdge>();
		 for ( EvolutionNode<E> node : fromModule) {
			 for (EvolutionEdge edge : node.getOutEdges()) {
				 if (edge.isProvider()
						 &&toModule.contains(edge.getToNode())
						 &&(!strength.contains(edge))) {
					 strength.add(edge);
				 }
			 }
		 }
		return strength.size();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inStrength(V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( V toNode: graph.getVertices()){
					if (toNode.getType().getCategory()== NodeCategory.MODULE) {
						nodeStrength+= HecataeusMetricManager.strength(graph.getModule(toNode), graph.getModule(node));
					}
				}
			}
			return nodeStrength;
	 }
	
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outStrength( V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( V toNode : graph.getVertices()) {
					if (toNode.getType().getCategory()== NodeCategory.MODULE) {
						nodeStrength+= HecataeusMetricManager.strength(graph.getModule(node), graph.getModule(toNode));
					}
				}
			}
			return nodeStrength;
	 }
	
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int strength(V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE){
				for (V toNode : graph.getVertices()) {
					if (toNode.getType().getCategory()== NodeCategory.MODULE) {
						nodeStrength+= HecataeusMetricManager.strength(graph.getModule(node), graph.getModule(toNode));
						nodeStrength+= HecataeusMetricManager.strength(graph.getModule(toNode), graph.getModule(node));
					}
				}
			}
			return nodeStrength;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge>int inWeightedStrength( V node, EvolutionGraph<V,E> graph) {
		 int nodeStrength = 0;
			if (node.getType().getCategory()== NodeCategory.MODULE) {
				for ( V toNode : graph.getVertices()) {
					if (toNode.getType().getCategory()== NodeCategory.MODULE) {
						nodeStrength+= toNode.getFrequency()*HecataeusMetricManager.strength(graph.getModule(toNode), graph.getModule(node));
					}
				}
			}
			return nodeStrength;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inWeightedDegree( V node) {
		 return node.getFrequency();
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inTransitiveDegree(V node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE) {
			 for (EvolutionEdge edge: node.getInEdges()) {
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=inTransitiveDegree((EvolutionNode<E>)edge.getFromNode());
				 }
			 }
		 }else {
			 for (EvolutionEdge edge: node.getInEdges()) {
				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 countDegree+=inTransitiveDegree(( EvolutionNode<E>) edge.getFromNode());
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outTransitiveDegree( V node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE) {
			 for (EvolutionEdge edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=outTransitiveDegree(( EvolutionNode<E>) edge.getToNode());
				 }
			 }
		 }else {
			 for (EvolutionEdge edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 countDegree+=outTransitiveDegree(( EvolutionNode<E>) edge.getToNode());
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int inPolicyTransitiveDegree( V node) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE) {
			 for (EvolutionEdge edge: node.getInEdges()) {
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 
					 if (node.getPolicies().size()>0) {
						
						 //TODO: matsakonia,ypothetw oti gia kathe kombo 
						 //yparxei mia mono policy h opoia einai block
						 //FIX: prepei na kanei iterate over all policies.
						 if (node.getPolicies().get(0).getPolicyType()!=PolicyType.BLOCK) { 
							 countDegree++;
							 countDegree+=inPolicyTransitiveDegree(( EvolutionNode<E>) edge.getFromNode());}
					 }else {countDegree+=inPolicyTransitiveDegree(( EvolutionNode<E>) edge.getFromNode());
					 countDegree++;
					 }
				 }
			 }
		 }else {
			 for (EvolutionEdge edge: node.getInEdges()) {
				 if (edge.isProvider() && edge.getType()!=EdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 if (node.getPolicies().size()>0) {
					 //TODO: matsakonia,ypothetw oti gia kathe kombo 
					 //yparxei mia mono policy h opoia einai block
					 //FIX: prepei na kanei iterate over all policies.
					 if (node.getPolicies().get(0).getPolicyType()!=PolicyType.BLOCK) 
						 countDegree+=inPolicyTransitiveDegree(( EvolutionNode<E>) edge.getFromNode());
				 }else countDegree+=inPolicyTransitiveDegree(( EvolutionNode<E>) edge.getFromNode());
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> int outPolicyTransitiveDegree( V node, EvolutionEvent event) {
		 int countDegree =0;
		 if (node.getType().getCategory()== NodeCategory.MODULE) {
			 for (EvolutionEdge edge: node.getOutEdges()) {
				 if (edge.isProvider() && edge.getType()==EdgeType.EDGE_TYPE_FROM) {
					 if (edge.getToNode().getPolicies().size()>0) {
						 for (int j=0; j<edge.getToNode().getPolicies().size();j++) {
							 EvolutionPolicy p = edge.getToNode().getPolicies().get(j);
							 //TODO: matsakonia,ypothetw oti gia kathe kombo 
							 //yparxei mia mono policy h opoia einai block
							 //FIX: prepei na kanei iterate over all policies.
							 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=PolicyType.BLOCK) {
								 countDegree++;
								 countDegree+=outPolicyTransitiveDegree(( EvolutionNode<E>) edge.getToNode(),event);
							 }
						 }
					 }else {
						 countDegree+=outPolicyTransitiveDegree(( EvolutionNode<E>) edge.getToNode(),event);
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
					 for (int j=0; j<edge.getToNode().getPolicies().size();j++) {
						 EvolutionPolicy p = edge.getToNode().getPolicies().get(j);
						 //TODO: matsakonia,ypothetw oti gia kathe kombo 
						 //yparxei mia mono policy h opoia einai block
						 //FIX: prepei na kanei iterate over all policies.
						 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=PolicyType.BLOCK) {
							 countDegree++;
							 countDegree+=outPolicyTransitiveDegree(( EvolutionNode<E>) edge.getToNode(),event);
						 }
					 }
				 }else countDegree+=outPolicyTransitiveDegree(( EvolutionNode<E>) edge.getToNode(), event);
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge> float entropyGraph(EvolutionGraph<V,E> graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<V> nodes = new ArrayList<V>(graph.getVertices());
		 double[] noPaths= new double[nodes.size()];
		 for ( V srcNode: nodes) {
			 if (srcNode.getType().getCategory()== NodeCategory.MODULE) {
				 noPaths[nodes.indexOf(srcNode)]=0;
				 for (V toNode : nodes) {
					 if (!toNode.equals(srcNode)
							 &&(toNode.getType().getCategory()== NodeCategory.MODULE)){
						 noPaths[nodes.indexOf(srcNode)] += graph.getPaths(srcNode, toNode);
					 }
				 }
				 totalPaths +=noPaths[nodes.indexOf(srcNode)];
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
	 
	 
	 public static <V extends EvolutionNode<E>,E extends EvolutionEdge>float entropyOutPerNode( V srcNode,EvolutionGraph<V,E> graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 List<V> nodes = new ArrayList<V>(graph.getVertices());
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
		 List<V> nodes = new ArrayList<V>(graph.getVertices());
		 double[] noPaths= new double[nodes.size()];
		 
		 for ( V fromNode : nodes) {
			 if ((!fromNode.equals(trgNode))
					 &&((fromNode.getType().getCategory()== NodeCategory.MODULE))){
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
		 
		 List<EvolutionNode<EvolutionEdge>> oddGroup = new ArrayList<EvolutionNode<EvolutionEdge>>();
		 List<EvolutionNode<EvolutionEdge>> evenGroup = new ArrayList<EvolutionNode<EvolutionEdge>>();
		 EvolutionNode<EvolutionEdge> v= (EvolutionNode<EvolutionEdge>) graph.getVertices().iterator().next();
		 oddGroup.add(v);
		 evaluateBipartite(v,oddGroup, evenGroup);
		 for (EvolutionEdge edge : graph.getEdges()) {
			 if ((oddGroup.contains(edge.getFromNode())&& oddGroup.contains(edge.getToNode()))
					 ||(evenGroup.contains(edge.getFromNode())&& evenGroup.contains(edge.getToNode())))
				 return false;
		 }
		 return true;
	 }
	 
	 private static void evaluateBipartite( EvolutionNode<EvolutionEdge> v, List<EvolutionNode<EvolutionEdge>> oddGroup, List<EvolutionNode<EvolutionEdge>> evenGroup) {
		 for(EvolutionEdge e :v.getInEdges()) {
			 EvolutionNode<EvolutionEdge> y = ( EvolutionNode<EvolutionEdge>) e.getFromNode();
			 if (oddGroup.contains(v)) { 
				 if (!evenGroup.contains(y)) {
					 evenGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
			 else {
				 if (!oddGroup.contains(y)) {
					 oddGroup.add(y);
					 evaluateBipartite(y,oddGroup, evenGroup);}}
		 }
		 for (int k = 0; k < v.getOutEdges().size(); k++) {
			  EvolutionNode<EvolutionEdge> y= ( EvolutionNode<EvolutionEdge>) v.getOutEdges().get(k).getToNode();
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

