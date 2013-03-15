/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;


public class EvolutionEvent<V extends EvolutionNode> {
	
	private EventType eventType;
	private V eventNode;
/**@author pmanousi*/	
	public EvolutionEvent(/*V node,*/EventType type){
		//this.eventNode = node;
		this.eventType = type;
	}
	
	public void setEventType(EventType type){
		this.eventType = type;
	}
	
	public EventType getEventType(){
		return this.eventType;
	}
	
	public void setEventNode(V node){
		this.eventNode = node;
	}
	
	public V getEventNode(){
		return this.eventNode;
	}
	
	public EventCategory getEventCategory(){
		return this.eventType.getCategory();
	}
	
	public String toString() {
		return this.eventType.toString();
	}
	
}
