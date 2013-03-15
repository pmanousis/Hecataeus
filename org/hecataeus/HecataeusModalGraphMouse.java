/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus;

import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

public class HecataeusModalGraphMouse extends EditingModalGraphMouse {
    
    /**
     * create an instance with default values
     *
     */
    public HecataeusModalGraphMouse() {
    	super(1.2f, 1/1.2f);
    }
    
    /**
     * create an instance with passed values
     * @param in override value for scale in
     * @param out override value for scale out
     */
    public HecataeusModalGraphMouse(float in, float out) {
    	super(in, out); 
    
    }
    
    protected void loadPlugins() { 
    	super.loadPlugins(); 
    	// add an additional translating plugin 
    	// that is never removed by other modes: 
    	add(new TranslatingGraphMousePlugin(InputEvent.BUTTON2_MASK)); 
    } 
    /**
	 * create (if necessary) and return a menu that will change the mode
	 * @return  the menu
	 * @uml.property  name="modeMenu"
	 */
    public JMenu getModeMenu() {
    	if(modeMenu == null) {
        	modeMenu = new JMenu("Graph");

            final JRadioButtonMenuItem transformingButton = 
                new JRadioButtonMenuItem("Move");
            transformingButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        setMode(Mode.TRANSFORMING);
                    }
                }});
            
            final JRadioButtonMenuItem pickingButton =
                new JRadioButtonMenuItem("Pick");
            pickingButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        setMode(Mode.PICKING);
                    }
                }});

            ButtonGroup radio = new ButtonGroup();
            radio.add(transformingButton);
            radio.add(pickingButton);
            transformingButton.setSelected(true);
            modeMenu.add(transformingButton);
            modeMenu.add(pickingButton);
            modeMenu.setToolTipText("Menu for setting Mouse Mode");
            addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						if(e.getItem() == Mode.TRANSFORMING) {
							transformingButton.setSelected(true);
						} else if(e.getItem() == Mode.PICKING) {
							pickingButton.setSelected(true);
						} 
					}
				}});
        }
        return modeMenu;
    }

}
