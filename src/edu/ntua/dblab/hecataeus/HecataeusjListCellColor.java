package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


class HecataeusjListCellColor extends DefaultListCellRenderer {  


	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {  
		Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
		index = index %6;
		switch (index){
			case 0: c.setBackground( new Color(16, 78, 139));break;
			case 1: c.setBackground( new Color(255,102,102));break;
			case 2: c.setBackground( new Color(255,178,102));break;
			case 3: c.setBackground(new Color(178,255,102));break;
			case 4: c.setBackground(new Color(102,178,255));break;
			case 5: c.setBackground(new Color(178,102,255));break;
			case 6: c.setBackground(new Color(255,102,102));break;
//			case 7: c.setBackground(new Color(255,255,102));break;
//			case 8: c.setBackground(new Color(102,102,255));break;
//			case 9: c.setBackground(new Color(255,153,024));break;
//			case 10: c.setBackground(new Color(102,204,0));break;
//			case 11: c.setBackground(new Color(153,0,153));break;
//			case 12: c.setBackground(new Color(102,0,0));break;
//			case 13: c.setBackground(new Color(0,51,102));break;
//			case 14: c.setBackground(new Color(204,204,255));break;
//			case 15: c.setBackground(new Color(0,128,255));break;
//			case 16: c.setBackground(new Color(255,153,51));break;
//			case 17: c.setBackground(new Color(255,192,203));break;
//			case 18: c.setBackground(new Color(198, 226, 255));break;
//			case 19: c.setBackground(new Color(30, 199,	40));break;
//			case 20: c.setBackground(new Color(0, 201, 87));break;
//			case 21: c.setBackground(new Color(188, 238, 104));break;
//			case 22: c.setBackground(new Color(238, 230,133));break;
//			case 23: c.setBackground(new Color(0, 206, 209));break;
//			case 24: c.setBackground(new Color(61, 145, 64));break;
//			case 25: c.setBackground(new Color(127,255,212));break;
//			case 26: c.setBackground(new Color(0,0,128));break;
//			case 27: c.setBackground(new Color(147,112,219));break;
//			case 28: c.setBackground(new Color(255,250,205));break;
//			case 29: c.setBackground(new Color(113,198,113));break;
//			case 30: c.setBackground(new Color(139,131,120));break;
//			case 31: c.setBackground(new Color(224,102,255));break;
			
			default : c.setBackground(new Color(255,255,255));
		}
		return c;
	}  
}
