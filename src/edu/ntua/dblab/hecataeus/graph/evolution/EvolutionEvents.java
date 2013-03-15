/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

public class EvolutionEvents extends java.util.ArrayList<EvolutionEvent>{
		
		public EvolutionEvents(){
		
		}

		/*
		 * gets an event by its type
		 */
		public <V extends EvolutionNode> EvolutionEvent<V> get(EventType type){
			for (EvolutionEvent<V> event : this){
				if (event.getEventType()==type)
					return event;
			}
			return null;
		}
}
