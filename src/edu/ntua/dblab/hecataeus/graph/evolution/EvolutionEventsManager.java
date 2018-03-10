/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.util.ArrayList;

public class EvolutionEventsManager {

	private ArrayList<EvolutionEvent> allEvents = new ArrayList<>();

	public EvolutionEventsManager() {
	}

	public <V extends EvolutionNode> EvolutionEvent get(EventType type) {
		for (EvolutionEvent event : allEvents) {
			if (event.getEventType() == type)
				return event;
		}
		return null;
	}

	public ArrayList<EvolutionEvent> getEvents() {
		return allEvents;
	}
}
