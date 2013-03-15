/**
 *
 * @author Fotini Anagnostou, graduate of National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

public class HecataeusNodes extends java.util.ArrayList<HecataeusNode> {
	
	public HecataeusNodes(){
		
	}

	public HecataeusNode getItem(int Index) {
		return (HecataeusNode) this.get(Index);
	}

	public HecataeusNode setItem(HecataeusNode Value,  int Index) {
		return (HecataeusNode) this.set(Index,Value);
	}
}
