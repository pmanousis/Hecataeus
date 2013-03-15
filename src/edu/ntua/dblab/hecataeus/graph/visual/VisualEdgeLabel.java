package edu.ntua.dblab.hecataeus.graph.visual;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class VisualEdgeLabel extends ToStringLabeller<VisualEdge> {

	/**
	 * Returns the label of the node
	 */
	public String transform(VisualEdge e) {
		switch (e.getType()) {
		case EDGE_TYPE_SCHEMA:  
		case EDGE_TYPE_OUTPUT:
		case EDGE_TYPE_INPUT:
		case EDGE_TYPE_SEMANTICS:
			return "";
		default: return e.toString(); 
		}
	}

}
