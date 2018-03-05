/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.gui;

import java.awt.event.MouseEvent;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;


public class HecataeusPopupGraphMousePlugin extends EditingPopupGraphMousePlugin<VisualNode,VisualEdge>{

	protected Set<VisualNode> pickedNodes;
	protected VisualGraph graph;
	protected Layout<VisualNode, VisualEdge> layout; 
	protected PickedState<VisualNode> pickedNodeState;
	protected VisualNode clickedVertex ;
	
/**
 * @author pmanousi
 * Needed for the epilegmenosKombos variable.
 */
	protected HecataeusViewer viewer;

	public HecataeusPopupGraphMousePlugin() {
		super(new Factory<VisualNode>() {
			public VisualNode create() {
				return new VisualNode(null);
			}
		}, new Factory<VisualEdge>() {
			public VisualEdge create() {
				return new VisualEdge(null);
			}
		});
	}
	
/**
 * @author pmanousi
 * @param v
 */
	public VisualNode getClickedVertex(){
		try{
			if(clickedVertex != null){
				pickedNodes = pickedNodeState.getPicked();
				return clickedVertex;
			}
		}catch(Exception e){
			e.getMessage();
		}
		return clickedVertex;
	}
	
	public void HecataeusViewerPM(HecataeusViewer v)
	{
		this.viewer = v;
	}
	
	@SuppressWarnings("unchecked")
	@Override	
	protected void handlePopup(MouseEvent e) {
		VisualizationViewer<VisualNode, VisualEdge> vv = (VisualizationViewer<VisualNode, VisualEdge>) e.getSource();
		layout = vv.getGraphLayout();
		this.graph = (VisualGraph) layout.getGraph();
	}

}
