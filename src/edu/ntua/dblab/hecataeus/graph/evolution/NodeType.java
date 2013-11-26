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
	NODE_TYPE_INSERT	(NodeCategory.MODULE) ,					/**added by sgerag*/
	NODE_TYPE_DELETE	(NodeCategory.MODULE) ,					/**added by sgerag*/
	NODE_TYPE_UPDATE	(NodeCategory.MODULE) ,					/**added by sgerag*/
	NODE_TYPE_MERGE_INTO (NodeCategory.MODULE) ,					/**added by sgerag*/
	NODE_TYPE_CURSOR	(NodeCategory.MODULE) ,					/**added by sgerag*/
	NODE_TYPE_ASSIGNMENT(NodeCategory.MODULE), 					/**added by sgerag*/
	NODE_TYPE_VARIABLE	(NodeCategory.MODULE),					/**added by sgerag*/
	
	NODE_TYPE_ATTRIBUTE (NodeCategory.SCHEMA),

/**
 * @author pmanousi
 */
NODE_TYPE_INPUT (NodeCategory.INOUTSCHEMA),
NODE_TYPE_OUTPUT (NodeCategory.INOUTSCHEMA),
NODE_TYPE_SEMANTICS (NodeCategory.INOUTSCHEMA),

	NODE_TYPE_CONDITION (NodeCategory.SEMANTICS),
	NODE_TYPE_OPERAND 	(NodeCategory.SEMANTICS),
	NODE_TYPE_CONSTANT	(NodeCategory.SEMANTICS),
	NODE_TYPE_GROUP_BY	(NodeCategory.SEMANTICS),
	NODE_TYPE_FUNCTION	(NodeCategory.SEMANTICS),
	
	NODE_TYPE_FILE 				(NodeCategory.CONTAINER),				/**added by sgerag*/
	NODE_TYPE_ANONYMOUS_BLOCK	(NodeCategory.CONTAINER),		/**added by sgerag*/
	NODE_TYPE_SCRIPT			(NodeCategory.CONTAINER),				/**added by sgerag*/
	NODE_TYPE_STORED_PROCEDURE	(NodeCategory.CONTAINER),			/**added by sgerag*/
	NODE_TYPE_STORED_FUNCTION	(NodeCategory.CONTAINER),				/**added by sgerag*/
	NODE_TYPE_TRIGGER			(NodeCategory.CONTAINER),				/**added by sgerag*/
	NODE_TYPE_PACKAGE			(NodeCategory.CONTAINER),				/**added by sgerag*/
	NODE_TYPE_EMBEDDED_STATEMENT(NodeCategory.CONTAINER),				/**added by sgerag*/

	
	NODE_TYPE_CLUSTER	(NodeCategory.CONTAINER),						/**added by evakont*/
	
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
