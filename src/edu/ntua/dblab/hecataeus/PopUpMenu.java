package edu.ntua.dblab.hecataeus;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible.VisibleLayer;

@SuppressWarnings("serial")
public class PopUpMenu extends JPopupMenu{


	protected JMenuItem popZoom;
	protected JMenuItem popCollapse;
	protected JMenu popSelect;
	protected HecataeusPopupGraphMousePlugin hpgmp;
	protected PopUpClickListener test;
	public PopUpMenu(){

		test =  new PopUpClickListener();
		hpgmp = new HecataeusPopupGraphMousePlugin();
		popZoom = new JMenuItem("ZOOM");
		popCollapse = new JMenuItem("COLLAPSE");
		popSelect = new JMenu("SELECT");
		add(popZoom);
		add(popCollapse);
		add(popSelect);
//		popZoom.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e) {
//				//set first invisible all graph
//				for (final VisualNode node :test.graph.getVertices()) {
//					node.setVisible(false);
//				}
//				//get parentNode
//				for(final VisualNode other :test.pickedNodes) {
//					List<VisualNode> subGraph =  test.graph.getModule(other);
//					VisualNodeVisible showAll = (VisualNodeVisible) test.pr.getVertexIncludePredicate();
//					showAll.setVisibleLevel(subGraph,VisibleLayer.SEMANTICS);
//				}
//				test.vv.repaint();
//				 System.out.println("Item clicked: "+e.getActionCommand());
//			}
//		});
		

		
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
