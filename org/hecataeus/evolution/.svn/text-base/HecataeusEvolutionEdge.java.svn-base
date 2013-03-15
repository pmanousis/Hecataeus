/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.evolution;

/**
 * @author  FOTINI, gpapas
 */
public class HecataeusEvolutionEdge {
     
	/**
	 * The properties of the edge
	 */
	protected String _Name = null;
	protected HecataeusEdgeType _Type;
	protected String _EdgeKey = null;
	private HecataeusEvolutionNode _FromNode = null;
    private HecataeusEvolutionNode _ToNode = null;
    private HecataeusEvolutionPolicies _policies = null;
	private HecataeusStatusType _status = HecataeusStatusType.NO_STATUS;
      
    public HecataeusEvolutionEdge() {
    	// just create the edge and set afterwards its properties
    }
    
    public HecataeusEvolutionEdge(String name, HecataeusEdgeType type, HecataeusEvolutionNode fromNode, HecataeusEvolutionNode toNode) {
        this._Name = name;
        this._Type = type;
        this._FromNode = fromNode;
        this._ToNode = toNode;
        this._policies = new HecataeusEvolutionPolicies();
    }
    
    /**
	 * Returns the name of the edge
	 */
	public String getName() {
		return this._Name;
	}

	/**
	 * Sets the name of the edge
	 */
	public void setName(String Value) {
		this._Name = Value;
	}

	/**
	 * Returns the type of the edge
	 */
	public HecataeusEdgeType getType() {
		return this._Type;
	}

	/**
	 * Sets the type of the edge
	 */
	public void setType(HecataeusEdgeType Value) {
		this._Type = Value;
	}

	/**
	 * Returns the unique key of the edge
	 */
	public String getKey() {
		return this._EdgeKey;
	}

	/**
	 * Sets the unique key of the edge
	 */
	public void setKey(String Value) {
		this._EdgeKey = Value;
	}
	
	public HecataeusStatusType getStatus() {
		return this._status ;
	}
	
	public void setStatus(HecataeusStatusType status) {
		this._status = status ;
	}
	
    
    public HecataeusEvolutionNode getFromNode() {
        return this._FromNode;
    }
    
    public void setFromNode(HecataeusEvolutionNode Value) {
        this._FromNode = Value;
        
    }
    
    public HecataeusEvolutionNode getToNode() {
        return this._ToNode;
    }
    
    public void setToNode(HecataeusEvolutionNode Value) {
        this._ToNode = Value;
    }
    
   	public HecataeusEvolutionPolicies getPolicies() {
		return this._policies;
	}
	
   	public void setPolicies(HecataeusEvolutionPolicies policies) {
			this._policies = policies;
   	}

   	/**
	*  creates and adds policy to edge
	**/
	public void addPolicy(HecataeusEventType eventType, HecataeusEvolutionNode eventNode, HecataeusPolicyType policyType) {
		HecataeusEvolutionPolicies policies = this._policies;
		HecataeusEvolutionPolicy policy = policies.get(eventType, eventNode);
		if(policy!=null)
				policies.remove(policy);
		policies.add(new HecataeusEvolutionPolicy(eventType,eventNode,policyType));
	}

	/**
	*  adds policy to edge, if policy already exists then it replaces it
	**/
	public void addPolicy(HecataeusEvolutionPolicy p) {
		HecataeusEvolutionPolicies policies = this._policies;
		HecataeusEvolutionPolicy policy = policies.get(p.getSourceEvent().getEventType(), p.getSourceEvent().getEventNode());
		if(policy!=null)
				policies.remove(policy);
		policies.add(p);
	}
	
	/**
	*  removes policy from edge
	**/
	public void removePolicy(HecataeusEvolutionPolicy policy) {
		this._policies.remove(policy);
	}

	
    public boolean isPartOf() {
    	if ((this.getType()==HecataeusEdgeType.EDGE_TYPE_SCHEMA) 
    			||(this.getType()==HecataeusEdgeType.EDGE_TYPE_WHERE)
    			||((this.getType()==HecataeusEdgeType.EDGE_TYPE_OPERATOR)
    					&&(this.getToNode().getType()!=HecataeusNodeType.NODE_TYPE_ATTRIBUTE)
    					&&(this.getToNode().getType()!=HecataeusNodeType.NODE_TYPE_QUERY))
    					||((this.getType()==HecataeusEdgeType.EDGE_TYPE_GROUP_BY)
    	    					&&(this.getToNode().getType()!=HecataeusNodeType.NODE_TYPE_ATTRIBUTE))
    	    					||((this.getType()==HecataeusEdgeType.EDGE_TYPE_MAPPING)
    	    							&&(this.getToNode().getType()!=HecataeusNodeType.NODE_TYPE_ATTRIBUTE))
    	){
    		return true;
    	}        
    	return false;
    }
    
    
    public boolean isProvider(){
    	return (!this.isPartOf());
    }
    


}