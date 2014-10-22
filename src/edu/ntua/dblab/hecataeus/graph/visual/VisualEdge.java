/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;

/**
 * The class implements a visual edge of the graph
 * It holds the visual properties of the edge
 * 
 * @author  gpapas
 * 
 */

public class VisualEdge extends EvolutionEdge{
	
		
	/**
	 * Creates an edge Object, with specific name,type,fromNode and toNode.
	 * 
	 */
	public VisualEdge(String name, EdgeType type , VisualNode fromNode, VisualNode toNode) {
		super(name, type, fromNode, toNode);
	}

	public VisualEdge() {
    	super();
    }
	
    public VisualNode getFromNode() {
        return (VisualNode) super.getFromNode();
    }
    
    
    public VisualNode getToNode() {
        return (VisualNode) super.getToNode();
    }
    

    public VisualEdge clone(){
    	
    	VisualEdge edge = new VisualEdge();
    	
    	edge.setName(this.getName());
    	edge.setFromNode(this.getFromNode());
    	edge.setToNode(this.getToNode());
    	edge.setPolicies(this.getPolicies());
    	edge.setStatus(this.getStatus(), true);
    	edge.setType(this.getType());
    	return edge;
    }
}