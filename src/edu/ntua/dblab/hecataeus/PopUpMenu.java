package edu.ntua.dblab.hecataeus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class PopUpMenu extends JPopupMenu{


	protected JMenuItem popZoom;
	protected JMenu popSelect;
	protected HecataeusPopupGraphMousePlugin hpgmp;
	
	public PopUpMenu(){

		hpgmp = new HecataeusPopupGraphMousePlugin();
		popZoom = new JMenuItem("ZOOM");
		popSelect = new JMenu("SELECT");
		add(popZoom);
		add(popSelect);
		popZoom.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 System.out.println("Item clicked: "+arg0.getActionCommand());
			}
		});
		

		
//		popSelect.add("tessst");

			
		
//		popSelect.addActionListener(new ActionListener(){
			
//			public void actionPerformed(ActionEvent arg0) {
//				System.out.println("Item clicked: "+arg0.getActionCommand());
//				popSelect.addMouseListener(new MouseListener() {
					
//					@Override
//					public void mouseReleased(MouseEvent e) {
//						// TODO Auto-generated method stub
//						handlePopup(e);
//					}
//					
//					@Override
//					public void mousePressed(MouseEvent e) {
//						// TODO Auto-generated method stub
//						handlePopup(e);
//					}
//					
//					@Override
//					public void mouseExited(MouseEvent e) {
//						// TODO Auto-generated method stub
//						handlePopup(e);
//					}
//					
//					@Override
//					public void mouseEntered(MouseEvent e) {
//						// TODO Auto-generated method stub
//						handlePopup(e);
//					}
//					
//					@Override
//					public void mouseClicked(MouseEvent e) {
//						// TODO Auto-generated method stub
//						handlePopup(e);
//					}
//				});
				 
//			}
//		});
		
	}
	
	

	
	
}
