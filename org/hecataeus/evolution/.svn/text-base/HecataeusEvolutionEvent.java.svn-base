/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;


public class HecataeusEvolutionEvent {
	
	private HecataeusEventType eventType;
	private HecataeusEvolutionNode eventNode;
	
	public HecataeusEvolutionEvent(HecataeusEvolutionNode node,HecataeusEventType type){
		this.eventNode = node;
		this.eventType = type;
	}
	
	/**
	 * @param eventType  the eventType to set
	 * @uml.property  name="eventType"
	 */
	public void setEventType(HecataeusEventType type){
		this.eventType = type;
	}
	
	/**
	 * @return  the eventType
	 * @uml.property  name="eventType"
	 */
	public HecataeusEventType getEventType(){
		return this.eventType;
	}
	
	/**
	 * @param eventNode  the eventNode to set
	 * @uml.property  name="eventNode"
	 */
	public void setEventNode(HecataeusEvolutionNode node){
		this.eventNode = node;
	}
	
	/**
	 * @return  the eventNode
	 * @uml.property  name="eventNode"
	 */
	public HecataeusEvolutionNode getEventNode(){
		return this.eventNode;
	}
	
	/**
	 * @return  the HecataeusEventCategory of the event, i.e. ADDITION, DELETION, MODIFICATION, RENAME
	 */
	public HecataeusEventCategory getEventCategory(){
		return this.eventType.getCategory();
	}
	
}
