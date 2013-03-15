/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

public class HecataeusEvolutionPolicies extends java.util.ArrayList<HecataeusEvolutionPolicy> {

	public HecataeusEvolutionPolicies(){
		
	}
	
	public HecataeusEvolutionPolicy get(HecataeusEventType eventType, HecataeusEvolutionNode eventNode) {
		HecataeusEvolutionPolicy u;
		for (int forEachVar0 = 0; forEachVar0 < this.size(); forEachVar0++) {
			u = this.get(forEachVar0);
			if ( u.getSourceEvent().getEventType()==eventType && u.getSourceEvent().getEventNode().equals(eventNode)) {
				return u;
			}
		}
                 return null;
	}
   	
}