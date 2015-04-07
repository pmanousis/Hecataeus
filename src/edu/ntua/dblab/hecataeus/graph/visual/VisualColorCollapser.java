package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.Collection;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;

public class VisualColorCollapser extends GraphCollapser{
	private VisualGraph originalGraph;
	
	public VisualColorCollapser(VisualGraph originalGraph) {
		super(originalGraph);
		this.originalGraph = originalGraph;
	}
	
	VisualGraph createGraph() throws InstantiationException, IllegalAccessException {
		return (VisualGraph)originalGraph.getClass().newInstance();
	}
	
	Graph createGraph1() throws InstantiationException, IllegalAccessException {
        return (Graph)originalGraph.getClass().newInstance();
    }
	
public VisualGraph collapse(VisualGraph inGraph, VisualGraph clusterGraph) {
	if(clusterGraph.getVertexCount() < 2) return inGraph;
		VisualGraph graph = inGraph;
		try {
		    graph = createGraph();
		} catch(Exception ex) {
		    ex.printStackTrace();
		}
		Collection cluster = clusterGraph.getVertices();
		// add all vertices in the delegate, unless the vertex is in the cluster.
		for(VisualNode v : inGraph.getVertices()) {
		    if(cluster.contains(v) == false) {
		        graph.addVertex(v);
		    }
		}
		// add the clusterGraph as a vertex
		graph.addVertex(clusterGraph);
		//add all edges from the inGraph, unless both endpoints of the edge are in the cluster
		for(VisualEdge e : inGraph.getEdges()) {
		Pair endpoints = inGraph.getEndpoints(e);
		// don't add edges whose endpoints are both in the cluster
			if(cluster.containsAll(endpoints) == false) {
			    if(cluster.contains(endpoints.getFirst())) {
			    	clusterGraph.addEdge(e);
			    } else if(cluster.contains(endpoints.getSecond())) {
			    	clusterGraph.addEdge(e);
			    } else {
			    	graph.addEdge(e,(VisualNode)endpoints.getFirst(), (VisualNode)endpoints.getSecond());
			    }
			}
		}
		return graph;
	}

    Object findVertex(Graph inGraph, Object vertex) {
        Collection vertices = inGraph.getVertices();
        if(vertices.contains(vertex)) {
            return vertex;
        }
        for(Object v : vertices) {
            if(v instanceof Graph) {
                Graph g = (Graph)v;
                if(contains(g, vertex)) {
                    return v;
                }
            }
        }
        return null;
    }
    
    private boolean contains(Graph inGraph, Object vertex) {
    	boolean contained = false;
    	if(inGraph.getVertices().contains(vertex)) return true;
    	for(Object v : inGraph.getVertices()) {
    		if(v instanceof Graph) {
    			contained |= contains((Graph)v, vertex);
    		}
    	}
    	return contained;
    }
    
    public Graph getClusterGraph(VisualGraph inGraph, VisualGraph picked) {
        Graph clusterGraph;
        try {
            clusterGraph = createGraph();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
        for(Object v : picked.getVertices()) {
        	clusterGraph.addVertex(v);
            Collection edges = inGraph.getIncidentEdges((VisualNode) v);
            for(Object edge : edges) {
                Pair endpoints = inGraph.getEndpoints((VisualEdge) edge);
                Object v1 = endpoints.getFirst();
                Object v2 = endpoints.getSecond();
                clusterGraph.addEdge(edge, v1, v2);
            }
        }
        return clusterGraph;
    }
}
