/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

/**
 * @author  FOTINI
 */
public class HecataeusEvolutionNode {
	
	/**
	 * The properties of the node
	 */
	private String _Name = null;
	private HecataeusNodeType _Type ;
	private String _NodeKey = null;
	private int _frequency = 0;
	

	private HecataeusEvolutionPolicies _policies = null;
	private HecataeusEvolutionEvents _events = null;
	private HecataeusEvolutionEdges _outEdges = null;
	private HecataeusEvolutionEdges _inEdges = null;
	private HecataeusStatusType _status = HecataeusStatusType.NO_STATUS;

	public HecataeusEvolutionNode() {
		// just create the node and set afterwards its properties
		this._outEdges = new HecataeusEvolutionEdges();
		this._inEdges = new HecataeusEvolutionEdges();
		this._policies = new HecataeusEvolutionPolicies();
		this._events = new HecataeusEvolutionEvents();
	}

	public HecataeusEvolutionNode(String Name, HecataeusNodeType Type) {
		this._Name= Name;
		this._Type = Type;
		this._outEdges = new HecataeusEvolutionEdges();
		this._inEdges = new HecataeusEvolutionEdges();
		this._policies = new HecataeusEvolutionPolicies();
		this._events = new HecataeusEvolutionEvents();
	}

	/**
	 * Returns the name of the node
	 */
	public String getName() {
		return this._Name;
	}

	/**
	 * Sets the name of the node
	 */
	public void setName(String Value) {
		this._Name = Value;
	}

	/**
	 * Returns the type of the node
	 */
	public HecataeusNodeType getType() {
		return this._Type;
	}

	/**
	 * Sets the type of the node
	 */
	public void setType(HecataeusNodeType Value) {
		this._Type = Value;
	}

	/**
	 * Returns the unique key of the node
	 */
	public String getKey() {
		return this._NodeKey;
	}

	/**
	 * Sets the unique key of the node
	 */
	public void setKey(String Value) {
		this._NodeKey = Value;
	}
	
	/**
	 * Returns the frequency of the node
	 */
	public int getFrequency() {
		return this._frequency ;
	}

	/**
	 * Sets the frequency of the node
	 */
	public void setFrequency(int Value) {
		this._frequency  = Value;
	}
	
	public void setPolicies(HecataeusEvolutionPolicies policies) {
		this._policies = policies;
	}
	
	public HecataeusEvolutionPolicies getPolicies() {
		return this._policies;
	}
        
	public void setEvents(HecataeusEvolutionEvents events) {
		this._events = events;
	}
	
	
	public HecataeusEvolutionEvents getEvents() {
		return this._events;
	}
	
	public void setStatus(HecataeusStatusType status) {
		this._status = status ;
	}
	
	public HecataeusStatusType getStatus() {
		return this._status ;
	}
	
    public HecataeusEvolutionEdges getOutEdges() {
		return this._outEdges;
	}

	public HecataeusEvolutionEdges getInEdges() {
		return this._inEdges;
	}
	
	
	/**
	*  creates and adds policy to node
	**/
	public void addPolicy(HecataeusEventType eventType, HecataeusEvolutionNode eventNode, HecataeusPolicyType policyType) {
		HecataeusEvolutionPolicies policies = this._policies;
		HecataeusEvolutionPolicy policy = policies.get(eventType, eventNode);
		if(policy!=null)
				policies.remove(policy);
		policies.add(new HecataeusEvolutionPolicy(eventType,eventNode,policyType));
	}

	/**
	*  adds policy to node, if policy already exists then it replaces it
	**/
	public void addPolicy(HecataeusEvolutionPolicy p) {
		HecataeusEvolutionPolicies policies = this._policies;
		HecataeusEvolutionPolicy policy = policies.get(p.getSourceEvent().getEventType(), p.getSourceEvent().getEventNode());
		if(policy!=null)
				policies.remove(policy);
		policies.add(p);
	}
	
	/**
	*  removes policy from node
	**/
	public void removePolicy(HecataeusEvolutionPolicy policy) {
		this._policies.remove(policy);
	}

	/**
	*  creates and adds a self event to node
	**/
	public void addEvent(HecataeusEventType eventType) {
	
		HecataeusEvolutionEvents events = this._events;
		for (int i=0; i<events.size(); i++){
			HecataeusEvolutionEvent event = events.get(i);
			if ((event.getEventType()==eventType))
				events.remove(event);
		}
		events.add(new HecataeusEvolutionEvent(this, eventType));
	}

	/**
	*  adds event to node
	**/
	public void addEvent(HecataeusEvolutionEvent e) {
		
		HecataeusEvolutionEvents events = this._events;
		for (int i=0; i<events.size(); i++){
			HecataeusEvolutionEvent event = events.get(i);
			if ((event.getEventType()==e.getEventType())&&(event.getEventNode().equals(e.getEventNode())))
				events.remove(event);
		}
		events.add(e);
	}
	/**
	*  removes event from node
	**/
	public void removeEvent(HecataeusEvolutionEvent event) {
		this._events.remove(event);
	}

	
	
	/**
	 * used for finding the parent of a node (query, view, relation)
	 **/
	public HecataeusEvolutionNode getParentNode() {
		HecataeusEvolutionEdges inEdges = this.getInEdges();
		for (int i=0; i<inEdges.size(); i++){
			HecataeusEvolutionEdge e = inEdges.get(i);
			//if node is attribute then 
			if (((this.getType()==HecataeusNodeType.NODE_TYPE_ATTRIBUTE)
					&& (e.getType()==HecataeusEdgeType.EDGE_TYPE_SCHEMA))
					||((this.getType()==HecataeusNodeType.NODE_TYPE_CONDITION)
							&& (e.getType()==HecataeusEdgeType.EDGE_TYPE_OPERATOR))		
							||((this.getType()==HecataeusNodeType.NODE_TYPE_OPERAND)
									&& ((e.getType()==HecataeusEdgeType.EDGE_TYPE_OPERATOR)
											||(e.getType()==HecataeusEdgeType.EDGE_TYPE_WHERE)))		
											||(this.getType()==HecataeusNodeType.NODE_TYPE_CONSTANT)		
											||((this.getType()==HecataeusNodeType.NODE_TYPE_GROUP_BY)
													&& (e.getType()==HecataeusEdgeType.EDGE_TYPE_GROUP_BY))		
													||(this.getType()==HecataeusNodeType.NODE_TYPE_FUNCTION)
			)
				return e.getFromNode();
		}
		return null;
	}
}