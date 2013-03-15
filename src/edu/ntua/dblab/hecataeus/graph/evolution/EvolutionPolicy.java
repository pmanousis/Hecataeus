/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

/**
 * @author  FOTINI
 */
public class EvolutionPolicy<V extends EvolutionNode> {
    /**
	 * ON event TO x node THEN raise Event to y Node
	 */
    
   
	//what event occurred: Attribute_Addition	
	// to which node : x Node
	 private EvolutionEvent<V> _sourceEvent = null;
     
     // which policy : propagate, block, prompt
	private PolicyType _policyType = null;
	   
	 //what event raised: Attribute_Addition	
	 // to which node : x Node
	private EvolutionEvent<V> _raisedEvent = null;

 

	public EvolutionPolicy() {
		// just create the policy and set afterwards its properties
	}

	public EvolutionPolicy(EventType eventType/*, V eventNode*/, PolicyType policyType) {
		EvolutionEvent<V> event = new EvolutionEvent<V>(/*eventNode,*/ eventType);
		this._sourceEvent = event;
		this._policyType = policyType;
	}

	public EvolutionPolicy(EvolutionEvent<V> event, PolicyType policyType) {
		this._sourceEvent = event;
		this._policyType = policyType;
	}
	
	public EvolutionEvent<V> getSourceEvent() {
		return this._sourceEvent;
	}

	public void setSourceEvent(EvolutionEvent<V> Value) {
		this._sourceEvent = Value;
	}
	
	public EvolutionEvent<V> getRaisedEvent() {
		return this._raisedEvent;
	}

	public void setRaisedEvent(EvolutionEvent<V> Value) {
		this._raisedEvent = Value;
	}

	public PolicyType getPolicyType() {
		return this._policyType;
	}

	public void setPolicyType(PolicyType Value) {
		this._policyType = Value;
	}
	
	public String toString() {
		return "On "+this._sourceEvent.getEventType()/*+" To "+this._sourceEvent.getEventNode()*/+" Then "+this._policyType;
	}
}