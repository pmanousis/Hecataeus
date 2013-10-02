package edu.ntua.dblab.hecataeus.graph.evolution;

/**
 * 
 * @author George Papastefanatos
 *  /  National Technical University of Athens
 *
 * @return
 * Enumeration for categories of nodes. 
 * 
 * Nodes categories are abstract groups of node types.
 * An node type belongs to an node category
 * 
 */
public enum NodeCategory {
	CONTAINER	,
	MODULE		,
	SCHEMA		,
	/**
	 * @author pmanousi
	 */
	INOUTSCHEMA,
	SEMANTICS	;
	
	

	public String toString() {
		return name();
	}

	public static NodeCategory toNodeCategory(String value) {
		return valueOf(value);
        
	}
	
}
