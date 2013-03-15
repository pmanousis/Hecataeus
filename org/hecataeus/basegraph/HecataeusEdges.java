/**
 *
 * @author Fotini Anagnostou, National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

/**
 * The class is a collection of graph edges.
 */
public class HecataeusEdges extends java.util.ArrayList<HecataeusEdge> {
	
	/**
	 * Creates new edge.
	 */
	public HecataeusEdges(){
		
	}

	/**
	 * Returns the edge with the specific position in the list.
	 */
   	public HecataeusEdge getEdge(int Index) {
		return (HecataeusEdge) this.get(Index);
	}
	/**
   	*adds an edge on the specific position in the list
   	*/
	public HecataeusEdge setEdge(HecataeusEdge Value, int Index) {
		return (HecataeusEdge) this.set(Index,Value);
                       
	}
}
