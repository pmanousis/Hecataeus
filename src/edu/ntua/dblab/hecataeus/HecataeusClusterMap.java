package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualCluster;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFont;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeLabel;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeShape;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeToolTips;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.ntua.dblab.hecataeus.graph.visual.VisualStaticLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualTotalClusters;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.picking.LayoutLensShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class HecataeusClusterMap extends JPanel{
	
	public VisualizationViewer myViewer;
	private HecataeusViewer hec;
	private ArrayList<VisualNode> myNodes = new ArrayList<VisualNode>();
	
	public HecataeusClusterMap(HecataeusViewer hec){
		this.hec = hec;
	}

	public void createMap(){
		myNodes.clear();
		if(myViewer!=null){
			myViewer.removeAll();
		}
		this.removeAll();this.repaint();
		Graph<VisualNode, String> graph = new UndirectedSparseGraph();
		
		VisualTotalClusters vtc = new VisualTotalClusters();
		
		ArrayList<VisualCluster> myClusters = vtc.getClusters();
		
		for(VisualCluster cl : myClusters){
			String name = ""+cl.getClusterId();
			String label;
			label = cl.getClusterLabel(cl);
			VisualNode node = new VisualNode(name, NodeType.NODE_TYPE_CLUSTER);
			Point2D loc = new Point2D.Double(cl.getCenterXOfCluster(),cl.getCenterYOfCluster());
			node.setNodeSize(cl.getClusterSize(cl));
			node.setLocation(loc);
			node.setNodeLabel(label);
			
			myNodes.add(node);
		}
		

		for(VisualNode node : myNodes){
			graph.addVertex(node);
		}
		final Layout layout= new VisualStaticLayout(graph,myNodes);
		
		myViewer = new VisualizationViewer(layout);
		myViewer.setBackground(Color.white);
		myViewer.setPickSupport(new LayoutLensShapePickSupport<VisualNode, VisualEdge>(myViewer));
		myViewer.setVertexToolTipTransformer(new VisualNodeToolTips());
		RenderContext<VisualNode, VisualEdge>  pr = myViewer.getRenderContext();
		pr.setVertexLabelTransformer(new VisualNodeLabel());
		myViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO); 
		//myViewer.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(0.1f)));
		pr.setVertexFontTransformer(new VisualNodeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9)));
		//pr.setEdgeShapeTransformer(new EdgeShape.Line());  //quad 
		//pr.setEdgeLabelTransformer(new VisualEdgeLabel());
		pr.setVertexFillPaintTransformer(new VisualNodeColor(myViewer.getPickedVertexState()));
		//pr.setEdgeDrawPaintTransformer(new VisualEdgeDrawColor(myViewer.getPickedEdgeState()));
		//pr.setEdgeFillPaintTransformer( new VisualEdgeColor(myViewer.getPickedEdgeState()));
		pr.setVertexShapeTransformer(new VisualNodeShape());
		pr.setVertexIncludePredicate(new VisualNodeVisible());
		myViewer.getRenderContext().getMultiLayerTransformer().addChangeListener(myViewer);
		HecataeusModalGraphMouse gm = new HecataeusModalGraphMouse();
		gm.setMode(Mode.PICKING);
		myViewer.setGraphLayout(layout);
		myViewer.setGraphMouse(gm);
		
		GraphZoomScrollPane sp = new GraphZoomScrollPane(myViewer);
		sp.add(myViewer);
		this.add(sp);
	}
}
