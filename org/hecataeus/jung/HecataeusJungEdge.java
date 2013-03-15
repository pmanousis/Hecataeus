/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.jung;

import org.hecataeus.evolution.*;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;

/**
 * The class implements an edge of a graph that represents a database and the queries to it. The edge can be visualised.
 * Each edge Object of this class has a corresponding HecataeusEvolutionEdge Object, in order to use the methods provided
 * by the HecataeusEvolutionManager package,using HecataeusEvolutionEdge.
 * 
 */

public class HecataeusJungEdge extends DirectedSparseEdge{
	
	//the properties of the edge
	private HecataeusJungNode _fromNode = null;
	private HecataeusJungNode _toNode = null;
	
	// the corresponding HecataeusEvolutionEdge Object
	protected HecataeusEvolutionEdge hecataeusEvolutionEdge = null;
	
	/**
	 * Creates an edge Object, with specific fromNode and toNode.
	 * 
	 */
	public HecataeusJungEdge(Vertex from, Vertex to){
		super(from, to);
		//creates the corresponding HecataeusEvolutionEdge Object
		this.hecataeusEvolutionEdge = new HecataeusEvolutionEdge();
		setFromNode((HecataeusJungNode) from);
		setToNode((HecataeusJungNode) to);
	}

	/**
	 * Creates an edge Object, with specific name,type,fromNode and toNode.
	 * 
	 */
	public HecataeusJungEdge(String name, HecataeusEdgeType type , HecataeusJungNode fromNode, HecataeusJungNode toNode) {
		super(fromNode, toNode);
		//creates the corresponding HecataeusEvolutionEdge Object
		this.hecataeusEvolutionEdge = new HecataeusEvolutionEdge(name, type, fromNode.getHecataeusEvolutionNode(), toNode.getHecataeusEvolutionNode());
		setFromNode(fromNode);
		setToNode(toNode);
	}
	/**
	 * Creates an edge Object, from an existing HecataeusEvolutionEdge
	 * 
	 */
	public HecataeusJungEdge (HecataeusEvolutionEdge hecEvEdge, HecataeusJungNode fromNode, HecataeusJungNode toNode) {
		super(fromNode, toNode);
		//map the corresponding HecataeusEvolutionEdge Object
		this.hecataeusEvolutionEdge = hecEvEdge;
		setFromNode(fromNode);
		setToNode(toNode);
		
	}
	/**
	 * returns  the corresponding hecataeusEvolutionEdge
	 * 
	 */
	public HecataeusEvolutionEdge getHecataeusEvolutionEdge() {
		return hecataeusEvolutionEdge;
	}
	
	/**
	 * returns the edge name
	 * 
	 */
	public String getName() {
		return hecataeusEvolutionEdge.getName();
	}

	/**
	 * sets the edge name 
	 * 
	 */
	public void setName(String Value) {
		hecataeusEvolutionEdge.setName(Value);
		
	}

	/**
	 * returns the edge type
	 * 
	 */
	public HecataeusEdgeType getType() {
		return hecataeusEvolutionEdge.getType();
	}

	/**
	 * sets the edge type
	 * 
	 */
	public void setType(HecataeusEdgeType Value) {
		hecataeusEvolutionEdge.setType(Value);
	}

	/**
	 * returns the edge unique key
	 * 
	 */
	public String getKey() {
		return hecataeusEvolutionEdge.getKey();
	}

	/**
	 * sets the edge unique key
	 * 
	 */
	public void setKey(String Value) {
		hecataeusEvolutionEdge.setKey(Value);
	}

    public HecataeusJungNode getFromNode() {
        return this._fromNode;
    }
    
    public void setFromNode(HecataeusJungNode Value) {
        this._fromNode = Value;
        HecataeusEvolutionNode hecEvNodeFrom = Value.getHecataeusEvolutionNode();
        // set HecataeusEvolutionEdgeFromNode
        hecataeusEvolutionEdge.setFromNode(hecEvNodeFrom);
    }
    
    public HecataeusJungNode getToNode() {
        return this._toNode;
    }
    
    public void setToNode(HecataeusJungNode Value) {
        this._toNode = Value;
        HecataeusEvolutionNode hecEvNodeTo = Value.getHecataeusEvolutionNode();
        // set HecataeusEvolutionEdgeToNode
        hecataeusEvolutionEdge.setToNode(hecEvNodeTo);
    }
}