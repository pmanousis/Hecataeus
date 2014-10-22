/**
 * @author George Papastefanatos, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Font;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;


public class VisualNodeFont implements Transformer<VisualNode, Font>{
	protected Font font;

	public VisualNodeFont(Font defaultFont) {
		 this.font = defaultFont;
	}
	 
	public Font transform(VisualNode v)
	{
		NodeType type = (v.getType());

		if (type.getCategory()== NodeCategory.MODULE ||type.getCategory()== NodeCategory.CONTAINER)
		{	// TODO: check if I can have the zoom level to set the size, too.
			//return new Font(font.getFamily(), Font.PLAIN ,font.getSize());
			return new Font(font.getFamily(), Font.PLAIN ,font.getSize());
		}
		else
			return font;
	}
}
