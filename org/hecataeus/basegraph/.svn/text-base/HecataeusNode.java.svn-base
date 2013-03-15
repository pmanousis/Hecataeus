/**
 *
 * @author Fotini Anagnostou, graduate of National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

/**
 * The class implements a node of a graph. The graph represents a database and the queries to it.
 */
public class HecataeusNode {
	
	/**
	 * The properties of the node
	 */
	protected String _Name = null;
	protected int _Type = -1;
	protected String _NodeKey = null;
	private HecataeusEdges _outEdges = null;
	private HecataeusEdges _inEdges= null;

	/**
	 * Just create the node and set afterwards its properties
	 */
	public HecataeusNode() {
		this._inEdges = new HecataeusEdges();
		this._outEdges = new HecataeusEdges();
	}

	/**
	 * Creates a node with specific name and type
	 */
	public HecataeusNode(String Name, int Type) {
		this._inEdges = new HecataeusEdges();
		this._outEdges = new HecataeusEdges();
		this._Name = Name;
		this._Type = Type;
	}


	/**
	 * Returns the outgoing edges 
	 */
	public HecataeusEdges getOutEdges() {
		return this._outEdges;
	}

	/**
	 * Returns the incoming edges 
	 */
	public HecataeusEdges getInEdges() {
		return this._inEdges;
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
	public int getType() {
		return this._Type;
	}

	/**
	 * Sets the type of the node
	 */
	public void setType(int Value) {
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
}
