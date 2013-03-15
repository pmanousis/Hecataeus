/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;


public class EvolutionEvent {
	
	private EventType eventType;
	private EvolutionNode eventNode;
	
	public EvolutionEvent(EvolutionNode node,EventType type){
		this.eventNode = node;
		this.eventType = type;
	}
	
	public void setEventType(EventType type){
		this.eventType = type;
	}
	
	public EventType getEventType(){
		return this.eventType;
	}
	
	public void setEventNode(EvolutionNode node){
		this.eventNode = node;
	}
	
	public EvolutionNode getEventNode(){
		return this.eventNode;
	}
	
	public EventCategory getEventCategory(){
		return this.eventType.getCategory();
	}
	
	public String toString() {
		return this.eventType.toString();
	}
	
}
