package edu.ntua.dblab.hecataeus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualAggregateLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualLayoutType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible.VisibleLayer;
import edu.ntua.dblab.hecataeus.metrics.HecataeusMetricManager;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;

public class PopUpClickListener extends MouseAdapter{
	protected VisualizationViewer<VisualNode,VisualEdge> vv = null;
	protected Layout<VisualNode, VisualEdge> layout; 
	protected PluggableRenderContext<VisualNode,VisualEdge> pr; 
	protected Point2D pointClicked ;
	protected VisualGraph graph;
	protected PickedState<VisualEdge> pickedEdgeState;
	protected HecataeusPopupGraphMousePlugin hpgmp;
	protected VisualAggregateLayout containerLayout;
	protected HecataeusViewer viewer;
	public static VisualNode clickedVertex ;
	protected VisualEdge clickedEdge ;
	protected Set<VisualNode> pickedNodes;
	protected PickedState<VisualNode> pickedNodeState;
	protected PopUpMenu menu;

	public void mouseReleased(MouseEvent e){
		if (e.isPopupTrigger())
			doPop(e);
		}

	public void doPop(MouseEvent e){
		if(e.getButton()== MouseEvent.BUTTON3){
			menu = new PopUpMenu();
			handlePopup(e);
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void handlePopup(MouseEvent e) {
		hpgmp = new HecataeusPopupGraphMousePlugin();
		vv = (VisualizationViewer<VisualNode,VisualEdge>)e.getSource();
		layout = vv.getGraphLayout();	
		pr = (PluggableRenderContext<VisualNode, VisualEdge>) vv.getRenderContext();
		graph = (VisualGraph) layout.getGraph();
		pointClicked = e.getPoint();
		viewer = HecataeusViewer.myViewer;
		GraphElementAccessor<VisualNode, VisualEdge> pickSupport = vv.getPickSupport();
		//if mouse clicked on a vertex or an edge
		if(pickSupport != null) {
			clickedVertex = pickSupport.getVertex(layout, pointClicked.getX(), pointClicked.getY());
			clickedEdge = pickSupport.getEdge(layout, pointClicked.getX(), pointClicked.getY());
			pickedNodeState = vv.getPickedVertexState();
			pickedEdgeState = vv.getPickedEdgeState();
			//if mouse click on vertex
			if(clickedVertex != null) {
				pickedNodeState.pick(clickedVertex, true);
				pickedNodes = pickedNodeState.getPicked();
				if (pickedNodes.size() == 1) {
					viewer.epilegmenosKombos = clickedVertex;
					menu.popZoom.addActionListener(zoomToNewModuleTab());
					menu.popSelect.addActionListener(selectNode());
				}

			}
		}
	}

	class ClusterVertexShapeFunction<V> extends EllipseVertexShapeTransformer<V> {

		ClusterVertexShapeFunction() {
			setSizeTransformer(new ClusterVertexSizeFunction<V>(20));
		}
		@Override
		public Shape transform(V v) {
			if(v instanceof Graph) {
				int size = ((Graph)v).getVertexCount();
				int sides = Math.max(10, 3);
				return factory.getRegularPolygon(v, sides);
			}
			return super.transform(v);
			}
		}
		class ClusterVertexSizeFunction<V> implements Transformer<V,Integer> {
			int size;
			public ClusterVertexSizeFunction(Integer size) {
				this.size = size;
			}

			public Integer transform(V v) {
				if(v instanceof Graph) {
					return 30;
				}
				return size;
			}
		}

	protected AbstractAction zoomToNewModuleTab(){
		return new AbstractAction("Zoom -> Module level") {
			final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.myViewer.getActiveViewer();
			public void actionPerformed(ActionEvent e) {
				VisualGraph sub;
				List<VisualNode> parent = new ArrayList<VisualNode>();
				//get parentNode
				for(final VisualNode node :pickedNodes) {
					parent.addAll(graph.getModule(node));
				}
				VisualGraph GV = new VisualGraph(graph.toGraph(parent));
				sub =  new VisualGraph(GV);
				HecataeusViewer.myViewer.zoomToModuleTab(parent, sub);
			}
		};
	}
	
	protected AbstractAction selectNode(){
		return new AbstractAction("select") {
			final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.myViewer.getActiveViewer();
			public void actionPerformed(ActionEvent e) {
				viewer.epilegmenosKombos = clickedVertex;
				viewer.updateManagers();
			}
		};
	}
}
