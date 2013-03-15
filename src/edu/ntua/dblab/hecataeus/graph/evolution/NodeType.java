/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */

package edu.ntua.dblab.hecataeus.graph.evolution;

/**
 * The class gathers the possible types of a node
 *
 */
public enum NodeType {
	
	NODE_TYPE_RELATION 	(NodeCategory.MODULE) ,
	NODE_TYPE_VIEW		(NodeCategory.MODULE) ,
	NODE_TYPE_QUERY 	(NodeCategory.MODULE) ,
	NODE_TYPE_ATTRIBUTE (NodeCategory.SCHEMA),
	NODE_TYPE_CONDITION (NodeCategory.SEMANTICS),
	NODE_TYPE_OPERAND 	(NodeCategory.SEMANTICS),
	NODE_TYPE_CONSTANT	(NodeCategory.SEMANTICS),
	NODE_TYPE_GROUP_BY	(NodeCategory.SEMANTICS),
	NODE_TYPE_FUNCTION	(NodeCategory.SEMANTICS),
	NODE_TYPE_STORED_PROCEDURE	(NodeCategory.CONTAINER),
	NODE_TYPE_VARIABLE	(NodeCategory.SCHEMA)
	;
	

	private NodeCategory _category;
	
	private NodeType(NodeCategory category) {
		this._category = category;
	}
	
	public NodeCategory getCategory() {
		return this._category ;
	}
	
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
	public static NodeType toNodeType(String value) {
		return valueOf(value);
	}
}
