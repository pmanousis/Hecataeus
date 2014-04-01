/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;

import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;


public class HecataeusPopupGraphMousePlugin extends EditingPopupGraphMousePlugin<VisualNode,VisualEdge>{

	protected VisualizationViewer<VisualNode,VisualEdge> vv = null;
	protected VisualizationViewer<VisualNode,VisualEdge> newVv = null;
	protected Set<VisualNode> pickedNodes;
	protected Set<VisualEdge> pickedEdges;
	protected Point2D pointClicked ;
	protected VisualGraph graph;
	protected Layout<VisualNode, VisualEdge> layout; 
	protected JPopupMenu popup;
	protected PickedState<VisualNode> pickedNodeState;
	protected PickedState<VisualEdge> pickedEdgeState;
	protected PluggableRenderContext<VisualNode,VisualEdge> pr; 
	protected VisualNode clickedVertex ;
	protected VisualEdge clickedEdge ;
	protected PopUpClickListener popUpClickListener;
	
/**
 * @author pmanousi
 * Needed for the epilegmenosKombos variable.
 */
	protected HecataeusViewer viewer;

	public HecataeusPopupGraphMousePlugin() {
		super(new Factory<VisualNode>() {
			public VisualNode create() {
				
				return new VisualNode();
			}
		}, new Factory<VisualEdge>() {
			public VisualEdge create() {
				return new VisualEdge();
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
		vv = (VisualizationViewer<VisualNode,VisualEdge>)e.getSource();
		layout = vv.getGraphLayout();
		this.graph = (VisualGraph) layout.getGraph();
	}

}
