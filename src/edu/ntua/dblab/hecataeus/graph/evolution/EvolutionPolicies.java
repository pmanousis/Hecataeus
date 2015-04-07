/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

public class EvolutionPolicies extends java.util.ArrayList<EvolutionPolicy> {

	public EvolutionPolicies(){}
	
	public <V extends EvolutionNode> EvolutionPolicy<V> get(EventType eventType) {
		for (EvolutionPolicy<V> u : this) {
			if ( u.getSourceEvent().getEventType()==eventType) {
				return u;
			}
		}
		return null;
	}
   	
}