/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

/**
 * The class implements a visual edge of the graph
 * It holds the visual properties of the edge
 * 
 * @author  gpapas
 * 
 */

public class VisualEdge {
	
	final private EvolutionEdge parentEvolutionEdge;
	private VisualGraph visualGraphInWhichContained;

	public VisualEdge(EvolutionEdge parentEvolutionEdge) {
		this.parentEvolutionEdge = parentEvolutionEdge;
    }
	
	public EvolutionEdge getParentEvolutionEdge() {
		return parentEvolutionEdge;
	}

	public void setVisualGraphInWhichContained(VisualGraph visualGraphInWhichContained) {
		this.visualGraphInWhichContained = visualGraphInWhichContained;
	}

    public VisualNode getFromNode() {
    	return visualGraphInWhichContained.getEquivalentVisualNode(parentEvolutionEdge.getFromNode());
    }
    
    public VisualNode getToNode() {
    	return visualGraphInWhichContained.getEquivalentVisualNode(parentEvolutionEdge.getToNode());
    }
    
	public String getName() {
		return parentEvolutionEdge.getName();
	}

	public EdgeType getType() {
		return parentEvolutionEdge.getType();
	}

	public StatusType getStatus() {
		return parentEvolutionEdge.getStatus();
	}

	public boolean isPartOf() {
		return parentEvolutionEdge.isPartOf();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof VisualEdge))
			return false;

		VisualEdge oth = (VisualEdge) other;

		if (this.getParentEvolutionEdge().equals(oth.getParentEvolutionEdge()) && super.equals(other))
			return true;
		return false;
	}

	public String toString(){
		return parentEvolutionEdge.toString();
	}

}