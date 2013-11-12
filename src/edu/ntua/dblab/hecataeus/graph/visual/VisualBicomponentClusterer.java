package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;

public class VisualBicomponentClusterer implements Transformer<VisualGraph, Set<Set<VisualNode>>> {
	protected Map<VisualNode,Number> dfs_num;
	protected Map<VisualNode,Number> high;
	protected Map<VisualNode,VisualNode> parents;
	protected Stack<VisualEdge> stack;
	protected int converse_depth;

	    /**
	     * Constructs a new bicomponent finder
	     */
	public VisualBicomponentClusterer() {
	}
	@Override
	public Set<Set<VisualNode>> transform(VisualGraph theGraph) {
		Set<Set<VisualNode>> bicomponents = new LinkedHashSet<Set<VisualNode>>();

		if (theGraph.getVertices().isEmpty())
			return bicomponents;

		// initialize DFS number for each vertex to 0
		dfs_num = new HashMap<VisualNode,Number>();
		for (VisualNode v : theGraph.getVertices()){
			dfs_num.put(v, 0);
		}

		for (VisualNode v : theGraph.getVertices()){
			if (dfs_num.get(v).intValue() == 0){ // if we haven't hit this vertex yet...  
				high = new HashMap<VisualNode,Number>();
				stack = new Stack<VisualEdge>();
				parents = new HashMap<VisualNode,VisualNode>();
				converse_depth = theGraph.getVertexCount();
				// find the biconnected components for this subgraph, starting from v
				findBiconnectedComponents(theGraph, v, bicomponents);

				// if we only visited one vertex, this method won't have
				// ID'd it as a biconnected component, so mark it as one
				if (theGraph.getVertexCount() - converse_depth == 1){
					Set<VisualNode> s = new HashSet<VisualNode>();
					s.add(v);
					bicomponents.add(s);
				}
			}
		}

		return bicomponents;
	}
	
	
	protected void findBiconnectedComponents(VisualGraph g, VisualNode v, Set<Set<VisualNode>> bicomponents){
		int v_dfs_num = converse_depth;
		dfs_num.put(v, v_dfs_num);
		converse_depth--;
		high.put(v, v_dfs_num);

		for (VisualNode w : g.getNeighbors(v)){
			int w_dfs_num = dfs_num.get(w).intValue();//get(w, dfs_num);
			VisualEdge vw = g.findEdge(v,w);
			if (w_dfs_num == 0){ // w hasn't yet been visited

				parents.put(w, v); // v is w's parent in the DFS tree
				stack.push(vw);
				findBiconnectedComponents(g, w, bicomponents);
				int w_high = high.get(w).intValue();//get(w, high);
				int wtf = 1;
				if (w_high <= v_dfs_num){
					
					Set<VisualNode> bicomponent = new HashSet<VisualNode>();
					VisualEdge e;
					for(int i = 0; i < stack.size(); i++){
						if(stack.get(i) == null){
							stack.remove(i);
						}
					}
					if(!stack.empty()){
						do{
							
						//	System.out.println("stack size   " + stack.size());
							e = stack.pop();
							for(int i = 0; i < stack.size(); i++){
								if(stack.get(i) == null){
									stack.remove(i);
								}
							}
							
							if(e.getType() != EdgeType.EDGE_TYPE_USES ){
								
							
						//		System.out.println("edge ");
								VisualNode t1 = e.getFromNode();
								VisualNode t2 = e.getToNode();
								bicomponent.add(e.getFromNode());
								bicomponent.add(e.getToNode());
					//			bicomponent.addAll(g.getIncidentVertices(e));
							}
							if(stack.size()==0){
								wtf = 0;
								break;
							}
						}
						while (e != vw || stack.size()==0 || e==null);
						if(wtf != 0){
							bicomponents.add(bicomponent);
						}
						wtf = 1;
					}
				//	wtf = 1;
				}
				high.put(v, Math.max(w_high, high.get(v).intValue()));
			}
			else if (w != parents.get(v)) // (v,w) is a back or a forward edge
				high.put(v, Math.max(w_dfs_num, high.get(v).intValue()));
		}
	}

}
