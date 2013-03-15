/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;


/**
 * The class retains the possible statuses of a node or edge
 *
 */
public enum HecataeusStatusType {
	NO_STATUS,
	TO_ADD_CHILD,
	TO_DELETE,
	TO_DELETE_CHILD,
	TO_MODIFY,
	TO_MODIFY_CHILD,
	TO_MODIFY_PROVIDER,
	TO_RENAME,
	TO_RENAME_CHILD,
	TO_RENAME_PROVIDER,
	BLOCKED,
	PROMPT;     
	
	public String toString() {
		return name();
	}

	public HecataeusStatusType toStatus(String value) {
		try {
            return valueOf(value);
        } 
        catch (Exception ex) {
            return HecataeusStatusType.NO_STATUS;
        }
	}

}
