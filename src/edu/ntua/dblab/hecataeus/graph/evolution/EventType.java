/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.util.ArrayList;

/**
 * 
 * @author George Papastefanatos
 * <br> National Technical University of Athens
 * <p>
 * 
 * Enumeration for all types of events 
 *
 */
public enum EventType {
	ADD_ATTRIBUTE 	(EventCategory.ADDITION)  ,      
	ADD_CONDITION 	(EventCategory.ADDITION),
	ADD_GROUP_BY 	(EventCategory.ADDITION) ,
	ADD_ORDER_BY	(EventCategory.ADDITION) ,
	ADD_VIEW		(EventCategory.ADDITION)		   ,
	ADD_QUERY		(EventCategory.ADDITION)		   ,
	ADD_RELATION    (EventCategory.ADDITION)       ,
	DELETE_ATTRIBUTE    (EventCategory.DELETION)   ,
	DELETE_CONDITION   (EventCategory.DELETION)    ,
	DELETE_GROUP_BY	(EventCategory.DELETION)	   ,
	DELETE_FUNCTION	(EventCategory.DELETION)	   ,
	DELETE_CONSTANT	(EventCategory.DELETION)	   ,
	DELETE_RELATION (EventCategory.DELETION)      ,
	DELETE_VIEW		(EventCategory.DELETION)	   ,
	DELETE_QUERY	(EventCategory.DELETION)	   ,
	MODIFYDOMAIN_ATTRIBUTE (EventCategory.MODIFICATION),
	MODIFY_CONDITION (EventCategory.MODIFICATION)      ,
	MODIFY_CONSTANT(EventCategory.MODIFICATION)   ,
	MODIFY_GROUP_BY	(EventCategory.MODIFICATION)   ,
	RENAME_ATTRIBUTE   (EventCategory.RENAME)  ,
	RENAME_CONDITION (EventCategory.RENAME)   ,
	RENAME_RELATION (EventCategory.RENAME); 
	
	private EventCategory _category;
	
	private EventType(EventCategory category) {
		this._category = category;
	}
	
	public String toString() {
		return name();
	}

	public static EventType toEventType(String value) {
		return valueOf(value);
        
	}
	
	/**
	 * 
	 * @param nodeType: Type of Node
	 * @return the set of types of events allowed for the specific type of node
	 */
	public static ArrayList<EventType> values(NodeType nodeType) {
		ArrayList<EventType> eventTypes = new ArrayList<EventType>();
		
		switch (nodeType) {
		case NODE_TYPE_RELATION:
			eventTypes.add(EventType.ADD_ATTRIBUTE);
			eventTypes.add(EventType.ADD_CONDITION);
			eventTypes.add(EventType.DELETE_RELATION);
			eventTypes.add(EventType.RENAME_RELATION);
			break;
		case NODE_TYPE_QUERY:
			eventTypes.add(EventType.ADD_ATTRIBUTE);
			eventTypes.add(EventType.ADD_CONDITION);
			eventTypes.add(EventType.ADD_GROUP_BY);
			eventTypes.add(EventType.ADD_ORDER_BY);
			eventTypes.add(EventType.DELETE_QUERY);
			break;
		case NODE_TYPE_VIEW:
			eventTypes.add(EventType.ADD_ATTRIBUTE);
			eventTypes.add(EventType.ADD_CONDITION);
			eventTypes.add(EventType.ADD_GROUP_BY);
			eventTypes.add(EventType.ADD_ORDER_BY);
			eventTypes.add(EventType.DELETE_VIEW);
			break;
		case NODE_TYPE_ATTRIBUTE:
			eventTypes.add(EventType.DELETE_ATTRIBUTE);
			eventTypes.add(EventType.RENAME_ATTRIBUTE);
			eventTypes.add(EventType.MODIFYDOMAIN_ATTRIBUTE);
			break;
		case NODE_TYPE_CONDITION:
			eventTypes.add(EventType.DELETE_CONDITION);
			eventTypes.add(EventType.MODIFY_CONDITION);
			eventTypes.add(EventType.RENAME_CONDITION);
			break;
		case NODE_TYPE_OPERAND:
			eventTypes.add(EventType.DELETE_CONDITION);
			eventTypes.add(EventType.MODIFY_CONDITION);
			break;
		case NODE_TYPE_CONSTANT:
			eventTypes.add(EventType.DELETE_CONSTANT);
			eventTypes.add(EventType.MODIFY_CONSTANT);
			break;
		case NODE_TYPE_FUNCTION:
			eventTypes.add(EventType.DELETE_FUNCTION);
			break;
		case NODE_TYPE_GROUP_BY:
			eventTypes.add(EventType.DELETE_GROUP_BY);
			eventTypes.add(EventType.MODIFY_GROUP_BY);
			break;

		default:
			//get otherwise all type of nodes
			for (EventType eventType: EventType.values()) {
				eventTypes.add(eventType);
			}
			break;
		}
		
		return eventTypes;
	}
	
	/**
	 * @return  the EventCategory of the event, i.e. ADDITION, DELETION, MODIFICATION, RENAME
	 */
	public EventCategory getCategory(){
		return this._category;
	}

}