package edu.ntua.dblab.hecataeus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class PopUpMenu extends JPopupMenu {

	protected JMenuItem popZoom;
	protected JMenuItem popSelect;

	public PopUpMenu() {
		popZoom = new JMenuItem("ZOOM");
		popSelect = new JMenuItem("SELECT");
		add(popZoom);
		add(popSelect);
	}
}
