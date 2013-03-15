/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */

package org.hecataeus.evolution;

/**
 * The class gathers the possible types of a node
 *
 */
public enum HecataeusNodeType {
	
	NODE_TYPE_RELATION ,
	NODE_TYPE_VIEW,
	NODE_TYPE_QUERY ,
	NODE_TYPE_ATTRIBUTE ,
	NODE_TYPE_CONDITION ,
	NODE_TYPE_OPERAND ,
	NODE_TYPE_CONSTANT,
	NODE_TYPE_GROUP_BY,
	NODE_TYPE_FUNCTION,
	NODE_TYPE_STORED_PROCEDURE,
	NODE_TYPE_APPLICATION,
	NODE_TYPE_VARIABLE
	;
	

	/**
	 * Converts from the enum representation of a type to the corresponding String representation
	 *
	 */
	public String toString() {
		return name();
	}

	/**
	 * Converts from the String representation of a type to the corresponding enum representation
	 *
	 */
	public static HecataeusNodeType toNodeType(String value) {
		return valueOf(value);
	}
}
