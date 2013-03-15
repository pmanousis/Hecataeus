/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

public class EvolutionMessage {

	/*
	 * session ID
	 */
	int SID = 0;
	/*
	 * sender node
	 */
	EvolutionNode ns = null;
	/*
	 * receiver node
	 */
	EvolutionNode nr = null;
	/*
	 * event
	 */
	EvolutionEvent event = null;
	/*
	 * edge connecting ns and nr
	 */
	EvolutionEdge edge = null;
	/*
	 * policy type of message
	 */
	PolicyType policyType = null;
	
	public EvolutionMessage(int SID, EvolutionNode ns, EvolutionNode nr, EvolutionEvent event, EvolutionEdge edge, PolicyType policyType){
		
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
	
	public EvolutionNode getNodeSender(){
		return this.ns;
	}
	
	public EvolutionNode getNodeReceiver(){
		return this.nr;
	}
	
	/**
	 * @return
	 */
	public EvolutionEvent getEvent(){
		return this.event;
	}
	
	/**
	 * @return
	 */
	public EvolutionEdge getEdge(){
		return this.edge;
	}
	
	/**
	 * @return
	 */
	public PolicyType getPolicyType(){
		return this.policyType;
	}
}
