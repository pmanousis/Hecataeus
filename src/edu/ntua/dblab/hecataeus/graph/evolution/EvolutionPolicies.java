/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

public class EvolutionPolicies extends java.util.ArrayList<EvolutionPolicy> {

	public EvolutionPolicies(){
		
	}
	
	public EvolutionPolicy get(EventType eventType, EvolutionNode eventNode) {
		EvolutionPolicy u;
		for (int forEachVar0 = 0; forEachVar0 < this.size(); forEachVar0++) {
			u = this.get(forEachVar0);
			if ( u.getSourceEvent().getEventType()==eventType && u.getSourceEvent().getEventNode().equals(eventNode)) {
				return u;
			}
		}
                 return null;
	}
   	
}