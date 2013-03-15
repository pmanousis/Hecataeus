package org.hecataeus.evolution;

/**
 * 
 * @author George Papastefanatos
 * @affiliation National Technical University of Athens
 *
 * Enumeration for categories of events. 
 * 
 * Event categories are more general than event types inducing a part of relationship.
 * An event type belongs to an event category
 * 
 */
public enum HecataeusEventCategory {
	ADDITION		,      
	DELETION		,
	RENAME			,
	MODIFICATION	;

	
	public String toString() {
		return name();
	}

	public static HecataeusEventCategory toEventType(String value) {
		return valueOf(value);
        
	}
}
