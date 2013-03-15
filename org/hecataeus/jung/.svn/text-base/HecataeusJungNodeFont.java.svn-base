/**
 * @author George Papastefanatos, National Technical University of Athens
 */
package org.hecataeus.jung;

import java.awt.Font;

import org.hecataeus.evolution.HecataeusNodeType;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexFontFunction;


public class HecataeusJungNodeFont implements VertexFontFunction{
	 protected Font font;

	public HecataeusJungNodeFont(Font defaultFont) {
		 this.font = defaultFont;
	}
	 
	public Font getFont(Vertex v)
	{
		HecataeusNodeType type = ((HecataeusJungNode) v).getType();

		if (type ==HecataeusNodeType.NODE_TYPE_QUERY
				||type ==HecataeusNodeType.NODE_TYPE_RELATION
				||type ==HecataeusNodeType.NODE_TYPE_VIEW)
			return new Font(font.getFamily(), Font.BOLD ,font.getSize()*2);
		else
			return font;
	}
}
