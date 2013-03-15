/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

public class HecataeusEvolutionMessage {

	/*
	 * session ID
	 */
	int SID = 0;
	/*
	 * sender node
	 */
	HecataeusEvolutionNode ns = null;
	/*
	 * receiver node
	 */
	HecataeusEvolutionNode nr = null;
	/*
	 * event
	 */
	HecataeusEvolutionEvent event = null;
	/*
	 * edge connecting ns and nr
	 */
	HecataeusEvolutionEdge edge = null;
	/*
	 * policy type of message
	 */
	HecataeusPolicyType policyType = null;
	
	public HecataeusEvolutionMessage(int SID, HecataeusEvolutionNode ns, HecataeusEvolutionNode nr, HecataeusEvolutionEvent event, HecataeusEvolutionEdge edge, HecataeusPolicyType policyType){
		
		this.SID = SID;
		this.ns = ns;
		this.nr = nr;
		this.event = event;
		this.edge = edge;
		this.policyType = policyType;
	}
	
	public int get_SID(){
		return this.SID;
	}
	
	public HecataeusEvolutionNode getNodeSender(){
		return this.ns;
	}
	
	public HecataeusEvolutionNode getNodeReceiver(){
		return this.nr;
	}
	
	public HecataeusEvolutionEvent getEvent(){
		return this.event;
	}
	
	public HecataeusEvolutionEdge getEdge(){
		return this.edge;
	}
	
	public HecataeusPolicyType getPolicyType(){
		return this.policyType;
	}
}
