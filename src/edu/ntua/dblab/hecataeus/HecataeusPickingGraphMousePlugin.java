package edu.ntua.dblab.hecataeus;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class HecataeusPickingGraphMousePlugin extends PickingGraphMousePlugin<VisualNode, VisualEdge>{

	public HecataeusPickingGraphMousePlugin() {
		super(); 
	}
	 
	protected void pickContainedVertices(VisualizationViewer<VisualNode, VisualEdge> vv,Point2D down, Point2D out, boolean clear) {
		Layout<VisualNode, VisualEdge> layout = vv.getGraphLayout();
		PickedState<VisualNode> pickedVertexState = vv.getPickedVertexState();

		Rectangle2D pickRectangle = new Rectangle2D.Double();
		pickRectangle.setFrameFromDiagonal(down,out);

		if(pickedVertexState != null) {
			if(clear) {
				pickedVertexState.clear();
			}
			GraphElementAccessor<VisualNode,VisualEdge> pickSupport = vv.getPickSupport();

			Collection<VisualNode> picked = pickSupport.getVertices(layout, pickRectangle);
			for(VisualNode v : picked) {
				if (v.getVisible())
				{
					pickedVertexState.pick(v, true);
				}
			}
		}
	}
}
