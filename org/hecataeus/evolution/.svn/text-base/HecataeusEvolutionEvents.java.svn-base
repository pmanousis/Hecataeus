/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

public class HecataeusEvolutionEvents extends java.util.ArrayList<HecataeusEvolutionEvent>{
		
		public HecataeusEvolutionEvents(){
		
		}

		/*
		 * gets an event by its type
		 */
		public HecataeusEvolutionEvent get(HecataeusEventType type){
			for (int i=0; i<this.size(); i++){
				if (this.get(i).getEventType()==type)
					return this.get(i);
			}
			return null;
		}
}
