package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


class HecataeusjListCellColor extends DefaultListCellRenderer {

	private HashMap<String, Color> map;
	
	public HecataeusjListCellColor(HashMap<String, Color> map){
		this.map = new HashMap<String, Color>(map);
	}
	
	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {  
		Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
		index = index %1;
		switch (index){
			case 0: c.setBackground(this.map.get(value));break;
			default : c.setBackground(new Color(255,255,255));
		}
		return c;
	}  
}
