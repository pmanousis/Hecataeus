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
		public EvolutionEvent get(EventType type){
			for (int i=0; i<this.size(); i++){
				if (this.get(i).getEventType()==type)
					return this.get(i);
			}
			return null;
		}
}
