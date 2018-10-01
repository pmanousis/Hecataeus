package edu.ntua.dblab.hecataeus.gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualAggregateLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.gui.util.GraphConverterUtilities;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class PopUpClickListener extends MouseAdapter {
	protected VisualizationViewer<VisualNode, VisualEdge> vv = null;
	protected Layout<VisualNode, VisualEdge> layout;
	protected PluggableRenderContext<VisualNode, VisualEdge> pr;
	protected Point2D pointClicked;
	protected VisualGraph graph;
	protected PickedState<VisualEdge> pickedEdgeState;
	protected HecataeusPopupGraphMousePlugin hpgmp;
	protected VisualAggregateLayout containerLayout;
	protected HecataeusViewer viewer;
	public static VisualNode clickedVertex;
	protected VisualEdge clickedEdge;
	protected Set<VisualNode> pickedNodes;
	protected PickedState<VisualNode> pickedNodeState;
	protected PopUpMenu menu;

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			doPop(e);
	}

	public void doPop(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			menu = new PopUpMenu();
			handlePopup(e);
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	protected void handlePopup(MouseEvent e) {
		hpgmp = new HecataeusPopupGraphMousePlugin();
		vv = (VisualizationViewer<VisualNode, VisualEdge>) e.getSource();
		layout = vv.getGraphLayout();
		pr = (PluggableRenderContext<VisualNode, VisualEdge>) vv.getRenderContext();
		graph = (VisualGraph) layout.getGraph();
		pointClicked = e.getPoint();
		viewer = HecataeusViewer.myViewer;
		GraphElementAccessor<VisualNode, VisualEdge> pickSupport = vv.getPickSupport();
		// if mouse clicked on a vertex or an edge
		if (pickSupport != null) {
			clickedVertex = pickSupport.getVertex(layout, pointClicked.getX(), pointClicked.getY());
			clickedEdge = pickSupport.getEdge(layout, pointClicked.getX(), pointClicked.getY());
			pickedNodeState = vv.getPickedVertexState();
			pickedEdgeState = vv.getPickedEdgeState();
			// if mouse click on vertex
			if (clickedVertex != null) {
				pickedNodeState.pick(pickSupport.getVertex(layout, pointClicked.getX(), pointClicked.getY()), true);
				pickedNodes = pickedNodeState.getPicked();
				if (pickedNodes.size() == 1) {
					viewer.epilegmenosKombos = clickedVertex;
					menu.popZoom.addActionListener(zoomToNewModuleTab());
					menu.popSelect.addActionListener(selectNode());
				}
			}
		}
	}

	@SuppressWarnings("serial")
	protected AbstractAction zoomToNewModuleTab() {
		return new AbstractAction("Zoom -> Module level") {
			public void actionPerformed(ActionEvent e) {
				String onoma = new String();
				List<EvolutionNode> parent = new ArrayList<>();
				EvolutionGraph evoGraph = viewer.getEvolutionGraph();
				// get parentNode
				for (final VisualNode node : pickedNodes) {
					parent.addAll(evoGraph.getModule(node.getParentEvolutionNode()));
					onoma += node.getName() + " ";
				}
				VisualGraph GV = GraphConverterUtilities.convertEvolutionGraphToVisual(evoGraph, parent);
				HecataeusViewer.myViewer.zoomToModuleTab(GV, onoma.trim());
			}
		};
	}

	protected AbstractAction selectNode() {
		return new AbstractAction("select") {
			public void actionPerformed(ActionEvent e) {
				viewer.epilegmenosKombos = clickedVertex;
				viewer.updateManagers();
			}
		};
	}
}
