/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author George Papastefanatos
 * <br> National Technical University of Athens
 * <p>
 * 
 * Enumeration for all types of events 
 *
 */
public enum HecataeusEventType {
	ADD_ATTRIBUTE 	(HecataeusEventCategory.ADDITION)  ,      
	ADD_CONDITION 	(HecataeusEventCategory.ADDITION),
	ADD_GROUP_BY 	(HecataeusEventCategory.ADDITION) ,
	ADD_ORDER_BY	(HecataeusEventCategory.ADDITION) ,
	ADD_VIEW		(HecataeusEventCategory.ADDITION)		   ,
	ADD_QUERY		(HecataeusEventCategory.ADDITION)		   ,
	ADD_RELATION    (HecataeusEventCategory.ADDITION)       ,
	DELETE_ATTRIBUTE    (HecataeusEventCategory.DELETION)   ,
	DELETE_CONDITION   (HecataeusEventCategory.DELETION)    ,
	DELETE_GROUP_BY	(HecataeusEventCategory.DELETION)	   ,
	DELETE_FUNCTION	(HecataeusEventCategory.DELETION)	   ,
	DELETE_CONSTANT	(HecataeusEventCategory.DELETION)	   ,
	DELETE_RELATION (HecataeusEventCategory.DELETION)      ,
	DELETE_VIEW		(HecataeusEventCategory.DELETION)	   ,
	DELETE_QUERY	(HecataeusEventCategory.DELETION)	   ,
	MODIFYDOMAIN_ATTRIBUTE (HecataeusEventCategory.MODIFICATION),
	MODIFY_CONDITION (HecataeusEventCategory.MODIFICATION)      ,
	MODIFY_CONSTANT(HecataeusEventCategory.MODIFICATION)   ,
	MODIFY_GROUP_BY	(HecataeusEventCategory.MODIFICATION)   ,
	RENAME_ATTRIBUTE   (HecataeusEventCategory.RENAME)  ,
	RENAME_CONDITION (HecataeusEventCategory.RENAME)   ,
	RENAME_RELATION (HecataeusEventCategory.RENAME); 
	
	private HecataeusEventCategory _category;
	
	private HecataeusEventType(HecataeusEventCategory category) {
		this._category = category;
	}
	
	public String toString() {
		return name();
	}

	public static HecataeusEventType toEventType(String value) {
		return valueOf(value);
        
	}
	
	/**
	 * 
	 * @param nodeType: Type of Node
	 * @return the set of types of events allowed for the specific type of node
	 */
	public static ArrayList<HecataeusEventType> values(HecataeusNodeType nodeType) {
		ArrayList<HecataeusEventType> eventTypes = new ArrayList<HecataeusEventType>();
		
		switch (nodeType) {
		case NODE_TYPE_RELATION:
			eventTypes.add(HecataeusEventType.ADD_ATTRIBUTE);
			eventTypes.add(HecataeusEventType.ADD_CONDITION);
			eventTypes.add(HecataeusEventType.DELETE_RELATION);
			eventTypes.add(HecataeusEventType.RENAME_RELATION);
			break;
		case NODE_TYPE_QUERY:
			eventTypes.add(HecataeusEventType.ADD_ATTRIBUTE);
			eventTypes.add(HecataeusEventType.ADD_CONDITION);
			eventTypes.add(HecataeusEventType.ADD_GROUP_BY);
			eventTypes.add(HecataeusEventType.ADD_ORDER_BY);
			eventTypes.add(HecataeusEventType.DELETE_QUERY);
			break;
		case NODE_TYPE_VIEW:
			eventTypes.add(HecataeusEventType.ADD_ATTRIBUTE);
			eventTypes.add(HecataeusEventType.ADD_CONDITION);
			eventTypes.add(HecataeusEventType.ADD_GROUP_BY);
			eventTypes.add(HecataeusEventType.ADD_ORDER_BY);
			eventTypes.add(HecataeusEventType.DELETE_VIEW);
			break;
		case NODE_TYPE_ATTRIBUTE:
			eventTypes.add(HecataeusEventType.DELETE_ATTRIBUTE);
			eventTypes.add(HecataeusEventType.RENAME_ATTRIBUTE);
			break;
		case NODE_TYPE_CONDITION:
			eventTypes.add(HecataeusEventType.DELETE_CONDITION);
			eventTypes.add(HecataeusEventType.MODIFY_CONDITION);
			eventTypes.add(HecataeusEventType.RENAME_CONDITION);
			break;
		case NODE_TYPE_OPERAND:
			eventTypes.add(HecataeusEventType.DELETE_CONDITION);
			eventTypes.add(HecataeusEventType.MODIFY_CONDITION);
			break;
		case NODE_TYPE_CONSTANT:
			eventTypes.add(HecataeusEventType.DELETE_CONSTANT);
			eventTypes.add(HecataeusEventType.MODIFY_CONSTANT);
			break;
		case NODE_TYPE_FUNCTION:
			eventTypes.add(HecataeusEventType.DELETE_FUNCTION);
			break;
		case NODE_TYPE_GROUP_BY:
			eventTypes.add(HecataeusEventType.DELETE_GROUP_BY);
			eventTypes.add(HecataeusEventType.MODIFY_GROUP_BY);
			break;

		default:
			//get otherwise all type of nodes
			for (HecataeusEventType eventType: HecataeusEventType.values()) {
				eventTypes.add(eventType);
			}
			break;
		}
		
		return eventTypes;
	}
	
	/**
	 * @return  the HecataeusEventCategory of the event, i.e. ADDITION, DELETION, MODIFICATION, RENAME
	 */
	public HecataeusEventCategory getCategory(){
		return this._category;
	}

}