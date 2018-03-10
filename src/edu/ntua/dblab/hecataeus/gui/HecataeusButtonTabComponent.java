package edu.ntua.dblab.hecataeus.gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
 
/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
@SuppressWarnings("serial")
public class HecataeusButtonTabComponent extends JPanel {
	private final JTabbedPane pane;
	private final VisualGraph visualGraphInsideTab;

	public HecataeusButtonTabComponent(final JTabbedPane pane, VisualGraph visualGraphInsideTab) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		if (pane == null || visualGraphInsideTab == null) {
			throw new NullPointerException("TabbedPane is null");
		}
		this.visualGraphInsideTab = visualGraphInsideTab;
		this.pane = pane;
		setOpaque(false);

		//make JLabel read titles from JTabbedPane
		JLabel label = new JLabel() {
			public String getText() {
				int i = pane.indexOfTabComponent(HecataeusButtonTabComponent.this);
				if (i != -1) {
					return pane.getTitleAt(i);
				}
				return(pane.getTitleAt(0));
			}
		};

		add(label);
		//add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		//tab button
		JButton button = new TabButton();
		add(button);
		//add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}
 
	private class TabButton extends JButton implements ActionListener {
		public TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			setUI(new BasicButtonUI());
			//Make it transparent
			setContentAreaFilled(false);
			setFocusable(false);
			//    setBorder(BorderFactory.createEtchedBorder());          bazei to x mesa se tetragono -- den mou arese
			setBorderPainted(false);
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			addActionListener(this);
		}	
 
		public void actionPerformed(ActionEvent e) {
			int i = pane.indexOfTabComponent(HecataeusButtonTabComponent.this);
			String tab = pane.getTitleAt(i);
			int choice = JOptionPane.showConfirmDialog(null,"You are about to close '" + tab+ "'\nDo you want to proceed ?","Confirmation Dialog", JOptionPane.INFORMATION_MESSAGE);
			if(choice == JOptionPane.YES_OPTION){
				HecataeusViewer.myViewer.getEvolutionGraph().removeObserver(visualGraphInsideTab);
				HecataeusViewer.countOpenTabs--;
				if (i != -1) {
					pane.remove(i);
				}
			}
		}

		public void updateUI() {
		}
 
		//paint the x
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.white);
			}
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
	}
 
	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}
 
		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}