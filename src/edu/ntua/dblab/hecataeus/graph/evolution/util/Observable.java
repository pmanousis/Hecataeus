package edu.ntua.dblab.hecataeus.graph.evolution.util;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public interface Observable {

	public void addObserver(Observer o);

	public void removeObserver(Observer o);

	public void notifyObserversForEdgeAdd(EvolutionEdge addedEdge);

	public void notifyObserversForEdgeRemove(EvolutionEdge removeEdge);

	public void notifyObserversForVertexAdd(EvolutionNode addedNode);

	public void notifyObserversForVertexRemove(EvolutionNode removeNode);

}
