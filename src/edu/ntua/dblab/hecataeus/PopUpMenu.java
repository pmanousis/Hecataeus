package edu.ntua.dblab.hecataeus;


import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class PopUpMenu extends JPopupMenu{


	protected JMenuItem popZoom;
	//protected JMenu popSelect;
	protected JMenuItem popSelect;
	protected HecataeusPopupGraphMousePlugin hpgmp;
	protected PopUpClickListener test;
	public PopUpMenu(){
		test =  new PopUpClickListener();
		hpgmp = new HecataeusPopupGraphMousePlugin();
		popZoom = new JMenuItem("ZOOM");
		popSelect = new JMenuItem("SELECT");
		add(popZoom);
		add(popSelect);
	}
}
