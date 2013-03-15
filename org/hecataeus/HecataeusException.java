package org.hecataeus;

/**
 * 
 * 
 * @author George Papastefanatos 
 * @affiliation National Technical University of Athens
 * 
 * Custom Exception Class overrides Exception
 *
 */
public class HecataeusException extends Exception{


	private static final long serialVersionUID = 0L;

	public HecataeusException(String message) {
		super(message);
	}
}
