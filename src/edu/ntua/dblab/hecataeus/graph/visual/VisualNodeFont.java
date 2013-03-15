/**
 * @author George Papastefanatos, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Font;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

import org.apache.commons.collections15.Transformer; 


public class VisualNodeFont implements Transformer<VisualNode, Font>{
	protected Font font;

	public VisualNodeFont(Font defaultFont) {
		 this.font = defaultFont;
	}
	 
	public Font transform(VisualNode v)
	{
		NodeType type = (v.getType());

		if (type.getCategory()== NodeCategory.MODULE)
			return new Font(font.getFamily(), Font.BOLD ,font.getSize()*2);
		else
			return font;
	}
}
