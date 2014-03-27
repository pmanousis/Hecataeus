/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus;

import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AnimatedPickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.RotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ShearingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;


public class HecataeusModalGraphMouse extends DefaultModalGraphMouse<VisualNode,VisualEdge> 
		implements GraphMouseListener<VisualNode> {

	protected TranslatingGraphMousePlugin  translatingPluginMiddleButton;
	protected HecataeusPopupGraphMousePlugin  popupEditingPlugin;

/**
 * @author pmanousi
 * Needed for the epilegmenosKombos variable.
 */
protected HecataeusViewer viewer;

     /**
     * create an instance with default values
     *
     */
    public HecataeusModalGraphMouse() {
    	super();
    }
    
    /**
     * create an instance with passed values
     * @param in override value for scale in
     * @param out override value for scale out
     */
    public HecataeusModalGraphMouse( float in, float out) {
    	super(in, out); 
    }
    
    protected void loadPlugins() {
    	pickingPlugin = new HecataeusPickingGraphMousePlugin();
    	animatedPickingPlugin = new AnimatedPickingGraphMousePlugin<VisualNode,VisualEdge>();
    	// add an additional translating plugin 
    	// for middle mouse button that is never removed by other modes 
    	translatingPluginMiddleButton = new TranslatingGraphMousePlugin(InputEvent.BUTTON2_MASK);
    	popupEditingPlugin = new HecataeusPopupGraphMousePlugin();
    	translatingPlugin = new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK);
    	scalingPlugin = new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, in, out);
    	rotatingPlugin = new RotatingGraphMousePlugin();
    	shearingPlugin = new ShearingGraphMousePlugin();
    	add(scalingPlugin);
    	add(translatingPluginMiddleButton);
    	add(popupEditingPlugin);
/** @author pmanousi changed from Mode.TRANSFORMING to Mode.PICKING */
    	setMode(Mode.PICKING);
    }

	/**
	 * @author pmanousi
	 * @param v The viewer that has epilegmenosKobmos.
	 */
	public void HecataeusViewerPM(HecataeusViewer v)
	{
	   	this.viewer=v;
	   	popupEditingPlugin.HecataeusViewerPM(this.viewer);
	}
    
       
    public JMenu getModeMenu() {
    	if(modeMenu == null) {
/**
 * @author pmanousi
 * Changed from "Graph" to "Visualize"
 */
        	modeMenu = new JMenu("Visualize");
			
            final JRadioButtonMenuItem transformingButton =
/**
 * @author pmanousi
 * Changed from "Move" to "Move Graph"
 */
                new JRadioButtonMenuItem("Move Graph");
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
/** @author pmanousi transformingButton.setSelected(true);*/
            pickingButton.setSelected(true);
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

	@Override
	/***
	 * 	Double click on a graph vertex, 
	 *  If vertex is a top-level module, then it sets visible/invisible
	 *  the subgraph of the module    
	 */
	public void graphClicked(VisualNode node, MouseEvent me) {
		if (me.getClickCount()==2 && me.getButton()== MouseEvent.BUTTON1) {
			HecataeusViewer testV = viewer;
			@SuppressWarnings("unchecked")
			VisualizationViewer<VisualNode,VisualEdge> vv = (VisualizationViewer<VisualNode,VisualEdge>) me.getSource();
			VisualGraph g = (VisualGraph) vv.getGraphLayout().getGraph();
			
			System.out.println("oi komboi    "   + g);
			
			if (node.getType().getCategory()== NodeCategory.MODULE||node.getType().getCategory()== NodeCategory.INOUTSCHEMA) {
				List<VisualNode> module = g.getModule(node);
				for (VisualNode child : module) {
					child.setVisible(!child.getVisible());
						
				}
				node.setVisible(true);
			}
			if (node.getType().getCategory()==NodeCategory.CONTAINER) {
				List<VisualNode> module = g.getModule(node);
				for (VisualNode child : module) {
					child.setVisible(!child.getVisible());
				}
				node.setVisible(true);
			}
			
		}
	}
	
	@Override
	public void graphPressed(VisualNode v, MouseEvent me) {
		
	
	}

	@Override
	public void graphReleased(VisualNode v, MouseEvent me) {
		PopUpClickListener pucl = new PopUpClickListener();
		if(me.getButton() == MouseEvent.BUTTON3){
			pucl.doPop(me);
		}
	}
}

