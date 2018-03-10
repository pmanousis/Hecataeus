/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.util.ArrayList;

public class EvolutionPoliciesManager {

	private ArrayList<EvolutionPolicy> allPolicies = new ArrayList<>();

	public EvolutionPoliciesManager(){}
	
	public EvolutionPolicy get(EventType eventType) {
		for (EvolutionPolicy u : allPolicies) {
			if ( u.getSourceEvent().getEventType()==eventType) {
				return u;
			}
		}
		return null;
	}

	public EvolutionPolicy get(int index) {
		return allPolicies.get(index);
	}

	public boolean add(EvolutionPolicy policy) {
		return this.allPolicies.add(policy);
	}

	public boolean remove(EvolutionPolicy policy) {
		return this.allPolicies.remove(policy);
	}

	public ArrayList<EvolutionPolicy> getPolicies() {
		return allPolicies;
	}
   	
}