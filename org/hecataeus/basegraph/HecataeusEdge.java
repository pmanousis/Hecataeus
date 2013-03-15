/**
 *
 * @author Fotini Anagnostou, National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

/**
 * The class implements an edge of a graph. The graph represents a database and the queries to it.
 */
public class HecataeusEdge {
	
	/**
	 * The properties of the edge
	 */
	protected String _Name = null;
	protected int _Type = -1;
	protected String _EdgeKey = null;
	private HecataeusNode _FromNode = null;
	private HecataeusNode _ToNode = null;

	/**
	 * Just create the edge and set afterwards its properties
	 */
	public HecataeusEdge() {

	}

	/**
	 * Creates an edge with specific name,type,fromNode and toNode
	 */
	public HecataeusEdge(String Name, int Type, HecataeusNode FromNode, HecataeusNode ToNode) {
		this._Name = Name;
		this._Type = Type;
		this._FromNode = FromNode;
		this._ToNode = ToNode;
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
	public int getType() {
		return this._Type;
	}

	/**
	 * Sets the type of the edge
	 */
	public void setType(int Value) {
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

	/**
	 * Returns the fromNode of the edge
	 */
	public HecataeusNode getFromNode() {
		return this._FromNode;
	}

	/**
	 * Sets the fromNode of the edge
	 */
	public void setFromNode(HecataeusNode Value) {
		this._FromNode = Value;
	}

	/**
	 * Returns the toNode of the edge
	 */
	public HecataeusNode getToNode() {
		return this._ToNode;
	}

	/**
	 * Sets the toNode of the edge
	 */
	public void setToNode(HecataeusNode Value) {
		this._ToNode = Value;
	}
}