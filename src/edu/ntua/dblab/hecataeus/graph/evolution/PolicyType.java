/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

public enum PolicyType {

	PROPAGATE,
	BLOCK,
	PROMPT  ;
	
	public String toString() {
		return name();
	}

	public static PolicyType toPolicyType(String value) {
		return valueOf(value);

	}
}