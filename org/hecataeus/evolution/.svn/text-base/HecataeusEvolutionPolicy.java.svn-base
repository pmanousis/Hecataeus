/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

/**
 * @author  FOTINI
 */
public class HecataeusEvolutionPolicy {
    /**
	* ON event TO x node THEN raise Event to y Node
	**/
    
   
	//what event occurred: Attribute_Addition	
	// to which node : x Node
	 private HecataeusEvolutionEvent _sourceEvent = null;
     
     // which policy : propagate, block, prompt
	 private HecataeusPolicyType _policyType = null;
	   
	 //what event raised: Attribute_Addition	
	 // to which node : x Node
	 private HecataeusEvolutionEvent _raisedEvent = null;

 

	public HecataeusEvolutionPolicy() {
		// just create the policy and set afterwards its properties
	}

	public HecataeusEvolutionPolicy(HecataeusEventType eventType, HecataeusEvolutionNode eventNode, HecataeusPolicyType policyType) {
		HecataeusEvolutionEvent event = new HecataeusEvolutionEvent(eventNode, eventType);
		this._sourceEvent = event;
		this._policyType = policyType;
	}

	public HecataeusEvolutionPolicy(HecataeusEvolutionEvent event, HecataeusPolicyType policyType) {
		this._sourceEvent = event;
		this._policyType = policyType;
	}
	
	public HecataeusEvolutionEvent getSourceEvent() {
		return this._sourceEvent;
	}

	public void setSourceEvent(HecataeusEvolutionEvent Value) {
		this._sourceEvent = Value;
	}
	
	public HecataeusEvolutionEvent getRaisedEvent() {
		return this._raisedEvent;
	}

	public void setRaisedEvent(HecataeusEvolutionEvent Value) {
		this._raisedEvent = Value;
	}

	public HecataeusPolicyType getPolicyType() {
		return this._policyType;
	}

	public void setPolicyType(HecataeusPolicyType Value) {
		this._policyType = Value;
	}
}