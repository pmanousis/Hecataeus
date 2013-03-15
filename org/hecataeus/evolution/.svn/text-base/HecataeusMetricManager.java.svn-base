/**
 * @author George Papastefanatos, National Technical University of Athens
 */

package org.hecataeus.evolution;

public class HecataeusMetricManager {
	
	public HecataeusMetricManager(){
		
	}
	 
	 public static int countNodes(HecataeusEvolutionGraph graph) {
		 return graph.getNodes().size();
	 }
	 
	 public static int countEdges(HecataeusEvolutionGraph graph) {
		 return graph.getEdges().size();
	 }
	 
	 public static int countPolicies(HecataeusEvolutionNodes nodes, HecataeusPolicyType policyType) {
		 int policies =0;
		 for (int i=0; i< nodes.size();i++) {
			 HecataeusEvolutionNode node =nodes.get(i);
			 for (int j=0;j<node.getPolicies().size();j++) {
     			HecataeusEvolutionPolicy p = node.getPolicies().get(j);
     			if (p.getPolicyType().equals(policyType)) {
     				policies++;
     			}
     		}
		 }
		 return policies;
	 }
	 
	 public static int countEvents(HecataeusEvolutionNodes nodes) {
		 int events =0;
		 for (int i=0; i< nodes.size();i++) {
			 HecataeusEvolutionNode node =nodes.get(i);
			 events += node.getEvents().size();
		 }
		 return events;
	 }
	 
	 public static int inDegree(HecataeusEvolutionNode node) {
		 return node.getInEdges().size();
	 }
	 
	 public static int outDegree(HecataeusEvolutionNode node) {
		 return node.getOutEdges().size();
	 }
	 
	 public static int degree(HecataeusEvolutionNode node) {
		 return node.getInEdges().size()+node.getOutEdges().size();
	 }
	 
	 public static int inDegree(HecataeusEvolutionNode node, HecataeusEdgeType edgeType) {
		 int countedges = 0;
		 HecataeusEvolutionEdges edges = node.getInEdges();
		 for (int i = 0; i < edges.size(); i++) {
			 HecataeusEvolutionEdge edge = edges.get(i);
			 if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static int outDegree(HecataeusEvolutionNode node, HecataeusEdgeType edgeType) {
		 int countedges = 0;
		 HecataeusEvolutionEdges edges = node.getOutEdges();
		 for (int i = 0; i < edges.size(); i++) {
			 HecataeusEvolutionEdge edge = edges.get(i);
			 if (edge.getType()==edgeType) {
				 countedges++;
			 }
		}
		return countedges;
	 }
	 
	 public static int degree(HecataeusEvolutionNode node, HecataeusEdgeType edgeType) {
		 int countInedges = 0;
		 HecataeusEvolutionEdges edges = node.getInEdges();
		 for (int i = 0; i < edges.size(); i++) {
			 HecataeusEvolutionEdge edge = edges.get(i);
			 if (edge.getType()==edgeType) {
				 countInedges++;
			 }
		}
		 int countOutedges = 0;
		 edges = node.getOutEdges();
		 for (int i = 0; i < edges.size(); i++) {
			 HecataeusEvolutionEdge edge = edges.get(i);
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
	 private static int strength(HecataeusEvolutionNodes fromModule, HecataeusEvolutionNodes toModule) {
		 HecataeusEvolutionEdges strength = new HecataeusEvolutionEdges();
		 for (int i = 0; i < fromModule.size(); i++) {
			 HecataeusEvolutionNode node = fromModule.get(i);
			 for (int j = 0; j < node.getOutEdges().size(); j++) {
				 HecataeusEvolutionEdge edge = node.getOutEdges().get(j);
				 if (edge.isProvider()
						 &&toModule.contains(edge.getToNode())
						 &&(!strength.contains(edge))) {
					 strength.add(edge);
				 }
			 }
		 }
		return strength.size();
	 }
	 
	 public static int inStrength(HecataeusEvolutionNode node, HecataeusEvolutionGraph graph) {
		 int nodeStrength = 0;
			if (node.getType()== HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()== HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()== HecataeusNodeType.NODE_TYPE_VIEW) {
				for (int j = 0; j < graph.getNodes().size(); j++) {
					HecataeusEvolutionNode toNode = graph.getNodes().get(j);
					if (toNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
						nodeStrength+= HecataeusMetricManager.strength(graph.getSubGraph(toNode), graph.getSubGraph(node));
					}
				}
			}
			return nodeStrength;
	 }
	
	 public static int outStrength(HecataeusEvolutionNode node, HecataeusEvolutionGraph graph) {
		 int nodeStrength = 0;
			if (node.getType()== HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()== HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()== HecataeusNodeType.NODE_TYPE_VIEW) {
				for (int j = 0; j < graph.getNodes().size(); j++) {
					HecataeusEvolutionNode toNode = graph.getNodes().get(j);
					if (toNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
						nodeStrength+= HecataeusMetricManager.strength(graph.getSubGraph(node), graph.getSubGraph(toNode));
					}
				}
			}
			return nodeStrength;
	 }
	
	 public static int strength(HecataeusEvolutionNode node, HecataeusEvolutionGraph graph) {
		 int nodeStrength = 0;
			if (node.getType()== HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()== HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()== HecataeusNodeType.NODE_TYPE_VIEW) {
				for (int j = 0; j < graph.getNodes().size(); j++) {
					HecataeusEvolutionNode toNode = graph.getNodes().get(j);
					if (toNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
						nodeStrength+= HecataeusMetricManager.strength(graph.getSubGraph(node), graph.getSubGraph(toNode));
						nodeStrength+= HecataeusMetricManager.strength(graph.getSubGraph(toNode), graph.getSubGraph(node));
					}
				}
			}
			return nodeStrength;
	 }
	 
	 public static int inWeightedStrength(HecataeusEvolutionNode node, HecataeusEvolutionGraph graph) {
		 int nodeStrength = 0;
			if (node.getType()== HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()== HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()== HecataeusNodeType.NODE_TYPE_VIEW) {
				for (int j = 0; j < graph.getNodes().size(); j++) {
					HecataeusEvolutionNode toNode = graph.getNodes().get(j);
					if (toNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
							||toNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
						nodeStrength+= toNode.getFrequency()*HecataeusMetricManager.strength(graph.getSubGraph(toNode), graph.getSubGraph(node));
					}
				}
			}
			return nodeStrength;
	 }
	 
	 public static int inWeightedDegree(HecataeusEvolutionNode node) {
		 return node.getFrequency();
	 }
	 
	 public static int inTransitiveDegree(HecataeusEvolutionNode node) {
		 int countDegree =0;
		 if (node.getType()==HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()==HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
			 HecataeusEvolutionEdges edges = node.getInEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=inTransitiveDegree(edge.getFromNode());
				 }
			 }
		 }else {
			 HecataeusEvolutionEdges edges = node.getInEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()!=HecataeusEdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 countDegree+=inTransitiveDegree(edge.getFromNode());
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static int outTransitiveDegree(HecataeusEvolutionNode node) {
		 int countDegree =0;
		 if (node.getType()==HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()==HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
			 HecataeusEvolutionEdges edges = node.getOutEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
					 countDegree+=outTransitiveDegree(edge.getToNode());
				 }
			 }
		 }else {
			 HecataeusEvolutionEdges edges = node.getOutEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()!=HecataeusEdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 countDegree+=outTransitiveDegree(edge.getToNode());
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static int inPolicyTransitiveDegree(HecataeusEvolutionNode node) {
		 int countDegree =0;
		 if (node.getType()==HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()==HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
			 HecataeusEvolutionEdges edges = node.getInEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
					 
					 if (node.getPolicies().size()>0) {
						
						 //TODO: matsakonia,ypothetw oti gia kathe kombo 
						 //yparxei mia mono policy h opoia einai block
						 //FIX: prepei na kanei iterate over all policies.
						 if (node.getPolicies().get(0).getPolicyType()!=HecataeusPolicyType.BLOCK) { 
							 countDegree++;
							 countDegree+=inPolicyTransitiveDegree(edge.getFromNode());}
					 }else {countDegree+=inPolicyTransitiveDegree(edge.getFromNode());
					 countDegree++;
					 }
				 }
			 }
		 }else {
			 HecataeusEvolutionEdges edges = node.getInEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()!=HecataeusEdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 if (node.getPolicies().size()>0) {
					 //TODO: matsakonia,ypothetw oti gia kathe kombo 
					 //yparxei mia mono policy h opoia einai block
					 //FIX: prepei na kanei iterate over all policies.
					 if (node.getPolicies().get(0).getPolicyType()!=HecataeusPolicyType.BLOCK) 
						 countDegree+=inPolicyTransitiveDegree(edge.getFromNode());
				 }else countDegree+=inPolicyTransitiveDegree(edge.getFromNode());
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static int outPolicyTransitiveDegree(HecataeusEvolutionNode node, HecataeusEvolutionEvent event) {
		 int countDegree =0;
		 if (node.getType()==HecataeusNodeType.NODE_TYPE_QUERY
					||node.getType()==HecataeusNodeType.NODE_TYPE_RELATION
					||node.getType()==HecataeusNodeType.NODE_TYPE_VIEW) {
			 HecataeusEvolutionEdges edges = node.getOutEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
					 if (edge.getToNode().getPolicies().size()>0) {
						 for (int j=0; j<edge.getToNode().getPolicies().size();j++) {
							 HecataeusEvolutionPolicy p = edge.getToNode().getPolicies().get(j);
							 //TODO: matsakonia,ypothetw oti gia kathe kombo 
							 //yparxei mia mono policy h opoia einai block
							 //FIX: prepei na kanei iterate over all policies.
							 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=HecataeusPolicyType.BLOCK) {
								 countDegree++;
								 countDegree+=outPolicyTransitiveDegree(edge.getToNode(),event);
							 }
						 }
					 }else {
						 countDegree+=outPolicyTransitiveDegree(edge.getToNode(),event);
						 countDegree++;
					 }
				 }
			 }
		 }else {
			 HecataeusEvolutionEdges edges = node.getOutEdges();
			 for (int i = 0; i < edges.size(); i++) {
				 HecataeusEvolutionEdge edge = edges.get(i);
				 if (edge.isProvider() && edge.getType()!=HecataeusEdgeType.EDGE_TYPE_FROM) {
					 countDegree++;
				 }
				 if (edge.getToNode().getPolicies().size()>0) {
					 for (int j=0; j<edge.getToNode().getPolicies().size();j++) {
						 HecataeusEvolutionPolicy p = edge.getToNode().getPolicies().get(j);
						 //TODO: matsakonia,ypothetw oti gia kathe kombo 
						 //yparxei mia mono policy h opoia einai block
						 //FIX: prepei na kanei iterate over all policies.
						 if (p.getSourceEvent().getEventType()==event.getEventType()&&p.getPolicyType()!=HecataeusPolicyType.BLOCK) {
							 countDegree++;
							 countDegree+=outPolicyTransitiveDegree(edge.getToNode(),event);
						 }
					 }
				 }else countDegree+=outPolicyTransitiveDegree(edge.getToNode(), event);
					
			}	
		 }
		 	 
		 return countDegree;
	 }
	 
	 public static float entropyGraph(HecataeusEvolutionGraph graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 double[] noPaths= new double[graph.getNodes().size()];
		 		 
		 for (int i = 0; i < graph.getNodes().size(); i++) {
			 HecataeusEvolutionNode srcNode = graph.getNodes().get(i);
			 if (srcNode.getType()== HecataeusNodeType.NODE_TYPE_RELATION
					 ||srcNode.getType()== HecataeusNodeType.NODE_TYPE_QUERY
					 ||srcNode.getType()== HecataeusNodeType.NODE_TYPE_VIEW) {
				 noPaths[i]=0;
				 for (int j = 0; j < graph.getNodes().size(); j++) {
					 HecataeusEvolutionNode toNode = graph.getNodes().get(j);
					 if ((!toNode.equals(srcNode))
							 &&((toNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
									 ||toNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
									 ||toNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW))){
						 noPaths[i] += graph.getPaths(srcNode, toNode);
					 }
				 }
				 totalPaths +=noPaths[i];
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
	 
	 
	 public static float entropyOutPerNode(HecataeusEvolutionNode srcNode,HecataeusEvolutionGraph graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 double[] noPaths= new double[graph.getNodes().size()];
		
		 for (int j = 0; j < graph.getNodes().size(); j++) {
			 HecataeusEvolutionNode toNode = graph.getNodes().get(j);
			 if ((!toNode.equals(srcNode)))
//					 &&((toNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
//							 ||toNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
//							 ||toNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW)))
			 {
				 noPaths[j] = graph.getPaths(srcNode, toNode);
				 totalPaths +=noPaths[j];
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
	 
	 public static float entropyInPerNode(HecataeusEvolutionNode trgNode,HecataeusEvolutionGraph graph) {
		 double totalPaths =0;
		 float countEntropy=0;
		 double[] noPaths= new double[graph.getNodes().size()];
		
		 for (int j = 0; j < graph.getNodes().size(); j++) {
			 HecataeusEvolutionNode fromNode = graph.getNodes().get(j);
			 if ((!fromNode.equals(trgNode))
					 &&((fromNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY
							 ||fromNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION
							 ||fromNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW))){
				 noPaths[j] = graph.getPaths(fromNode, trgNode);
				 totalPaths +=noPaths[j];
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
}
