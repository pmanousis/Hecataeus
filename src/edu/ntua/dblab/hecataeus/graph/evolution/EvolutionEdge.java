/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;

/**
 * @author  George Papastefanatos
 */

public class EvolutionEdge{
     
	private String name = null;
	private EdgeType type;
	private EvolutionNode fromNode = null;
	private EvolutionNode toNode = null;
	private StatusType status = StatusType.NO_STATUS;

    public EvolutionEdge() {
    }
        
    public EvolutionEdge(String name, EdgeType type,  EvolutionNode fromNode,  EvolutionNode  toNode) {
        this.name = name;
        this.type = type;
        this.fromNode =   fromNode;
        this.toNode =   toNode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String Value) {
		this.name = Value;
	}

	public EdgeType getType() {
		return this.type;
	}

	public void setType(EdgeType Value) {
		this.type = Value;
	}

	public StatusType getStatus() {
		return this.status ;
	}
	
	public void setStatus(StatusType status, boolean cl) {
		if(cl==true)
		{
			this.status=status;
		}
		else
		{
			if(this.status==StatusType.BLOCKED||status==StatusType.NO_STATUS)
			{
				return;
			}
			this.status = status;
		}
	}
	
    public  EvolutionNode getFromNode() {
        return this.fromNode;
    }
    
    public void setFromNode(EvolutionNode Value) {
		this.fromNode = Value;
    }
    
    public EvolutionNode getToNode() {
        return this.toNode;
    }
    
    public void setToNode(EvolutionNode Value) {
        this.toNode = Value;
    }
    
    public boolean isPartOf() {
    	if ((this.getType()==EdgeType.EDGE_TYPE_SCHEMA) 
    			||(this.getType()==EdgeType.EDGE_TYPE_SEMANTICS) 
    			||(this.getType()==EdgeType.EDGE_TYPE_INPUT)
    			||(this.getType()==EdgeType.EDGE_TYPE_OUTPUT)
    			||(this.getType()==EdgeType.EDGE_TYPE_WHERE)
    			||((this.getType()==EdgeType.EDGE_TYPE_OPERATOR)
    					&&(this.getToNode().getType()!=NodeType.NODE_TYPE_ATTRIBUTE)
    					&&(this.getToNode().getType()!=NodeType.NODE_TYPE_QUERY))
    					||((this.getType()==EdgeType.EDGE_TYPE_GROUP_BY)
    							&&(this.getToNode().getType()!=NodeType.NODE_TYPE_ATTRIBUTE))
    							||((this.getType()==EdgeType.EDGE_TYPE_MAPPING)
    									&&(this.getToNode().getType()!=NodeType.NODE_TYPE_ATTRIBUTE))
    	    							||(this.getType()==EdgeType.EDGE_TYPE_CONTAINS)
    	){
    		return true;
    	}        
    	return false;
    }
    
    public boolean isProvider(){
    	return (!this.isPartOf());
    }
    
    public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EvolutionEdge))
			return false;

		EvolutionEdge oth = (EvolutionEdge) other;
		if (this.fromNode.equals(oth.getFromNode()) && this.toNode.equals(oth.getToNode()) && super.equals(other))
			return true;
		return false;
	}

	public VisualEdge produceVisualEdge() {
		return new VisualEdge(this);
	}
	
}