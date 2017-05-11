/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenu;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
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
//			System.out.println("oi komboi    "   + g);
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
		String eol=System.getProperty("line.separator");
		if(me.getClickCount()==1 && me.getButton()==MouseEvent.BUTTON1)
		{
			this.viewer.setTextToInformationArea("");
			if(node.getType()==NodeType.NODE_TYPE_RELATION)
			{
				List<String> filenames=new ArrayList<String>();
				for (VisualEdge inEdge : node.getInEdges())
				{
					if(filenames.contains(inEdge.getFromNode().getFileName())==false)
					{
						filenames.add(inEdge.getFromNode().getFileName());
					}
				}
				filenames.remove(node.getFileName());
				Collections.sort(filenames);
				String output=new String();
				for(String filename: filenames)
				{
					output+=filename.substring(filename.indexOf(viewer.projectConf.projectName)+viewer.projectConf.projectName.length()+5)+eol;
				}
				this.viewer.setTextToInformationArea("Scripts using relation "+node.getName()+":"+eol+output);
			}
			if(node.getType()==NodeType.NODE_TYPE_QUERY||node.getType()==NodeType.NODE_TYPE_VIEW)
			{
				String input=new String();
				for(VisualEdge ve: node.getInEdges())
				{
					if(ve.getType()==EdgeType.EDGE_TYPE_USES)
					{
						input+=ve.getFromNode().getName()+" "+ve.getFromNode().getType().toString().replace("NODE_TYPE_", "").toLowerCase()+eol;
					}
				}
				if(input.isEmpty())
				{
					input="none";
				}
				String output=new String();
				
				List<String> outS = new ArrayList<String>();
				
				for(VisualEdge ve: node.getOutEdges())
				{
					if(ve.getType()==EdgeType.EDGE_TYPE_USES)
					{
						//output+=ve.getToNode().getName()+" "+ve.getToNode().getType().toString().replace("NODE_TYPE_", "").toLowerCase()+eol;
						outS.add(ve.getToNode().getName()+" "+ve.getToNode().getType().toString().replace("NODE_TYPE_", "").toLowerCase());
					}
				}
				Collections.sort(outS);
				for(String s: outS)
				{
					output += s+eol;
				}
				if(output.isEmpty())
				{
					output="none";
				}
				if(node.getType() == NodeType.NODE_TYPE_QUERY)
				{
					this.viewer.setTextToInformationArea("Query: "+node.getName()+" uses:"+eol+output);
				}
				else
				{
					this.viewer.setTextToInformationArea("View: "+node.getName()+" is used by:"+eol+input+eol+"and uses:"+eol+output);
				}
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

