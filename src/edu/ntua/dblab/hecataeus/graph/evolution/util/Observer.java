package edu.ntua.dblab.hecataeus.graph.evolution.util;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public interface Observer {

	public boolean evolutionEdgeAdded(EvolutionEdge addedEdge);

	public boolean evolutionEdgeRemoved(EvolutionEdge removedEdge);

	public boolean evolutionVertexAdded(EvolutionNode addedNode);

	public boolean evolutionVertexRemoved(EvolutionNode removedNode);

}
