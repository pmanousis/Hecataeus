package edu.ntua.dblab.hecataeus.graph.visual;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;


public class VisualNodeIcon implements Transformer<VisualNode, Icon> {
	
	String path = "resources/";
	@Override
	public Icon transform(VisualNode v) {
		NodeType type = (v.getType());
		if (type ==NodeType.NODE_TYPE_QUERY) 
				return new  ImageIcon(path+"table_selection_row.png");
		if (type ==NodeType.NODE_TYPE_INSERT)
			return new ImageIcon(path+"gear_add.png");
		if (type ==NodeType.NODE_TYPE_DELETE)
			return new ImageIcon(path+"gear_delete.png");
		if (type ==NodeType.NODE_TYPE_UPDATE)
			return new ImageIcon(path+"gear_replace.png");
		if (type ==NodeType.NODE_TYPE_RELATION)
			return new ImageIcon(path+"data.png");
		if (type ==NodeType.NODE_TYPE_STORED_PROCEDURE)
			return new ImageIcon(path+"stored_function.png");
		if (type ==NodeType.NODE_TYPE_STORED_FUNCTION)
			return new ImageIcon(path+"stored_function.png");
		if (type ==NodeType.NODE_TYPE_CURSOR)
			return new ImageIcon(path+"cursor.png");
		if (type ==NodeType.NODE_TYPE_VIEW)
			return new ImageIcon(path+"table_selection_block.png");
		if (type ==NodeType.NODE_TYPE_FILE)
			return new ImageIcon(path+"folder.png");
		if (type ==NodeType.NODE_TYPE_ANONYMOUS_BLOCK)
			return new ImageIcon(path+"block.png");
		return null;
	}
}
