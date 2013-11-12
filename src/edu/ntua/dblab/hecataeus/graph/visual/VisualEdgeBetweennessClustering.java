
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;


@SuppressWarnings("serial")
public class VisualEdgeBetweennessClustering  extends VisualCircleLayout{

	public static JPanel south = null;
	
	public final static  Color[] similarColors = {
		new Color(216, 134, 134),
		new Color(135, 137, 211),
		new Color(134, 206, 189),
		new Color(206, 176, 134),
		new Color(194, 204, 134),
		new Color(145, 214, 134),
		new Color(133, 178, 209),
		new Color(103, 148, 255),
		new Color(60, 220, 220),
		new Color(30, 250, 100)
	};

	private VisualGraph graph;
	
	private VisualGraph RQ;

	protected VisualCircleLayout vcl;
	private double endC;
	
		@SuppressWarnings({ "unchecked", "rawtypes" })
	static Map<VisualNode,Paint> vertexPaints = LazyMap.<VisualNode,Paint>decorate(new HashMap<VisualNode,Paint>(), new ConstantTransformer(Color.white));
		@SuppressWarnings({ "unchecked", "rawtypes" })
	static Map<VisualEdge,Paint> edgePaints = LazyMap.<VisualEdge,Paint>decorate(new HashMap<VisualEdge,Paint>(), new ConstantTransformer(Color.blue));

	public VisualEdgeBetweennessClustering(VisualGraph g, double endC) {
		super(g);
		this.graph = g;
		this.endC = endC;
		vcl = new VisualCircleLayout(this.graph);
		List<VisualNode> queries = new ArrayList<VisualNode>();
		List<VisualNode> relations = new ArrayList<VisualNode>();
		List<VisualNode> views = new ArrayList<VisualNode>();
		
		
		relations = new ArrayList<VisualNode>(vcl.getRelations());
		queries =new ArrayList<VisualNode>( vcl.getQueries());
		views = new ArrayList<VisualNode>(vcl.getViews());
	
		RQ = new VisualGraph();
		
		for(VisualNode r : relations){
			RQ.addVertex(r);
			
			for(VisualEdge e : r.getInEdges()){
				if(e.getFromNode().getType()== NodeType.NODE_TYPE_QUERY){
					RQ.addEdge(e);
				}
			}
		}
		
		for(VisualNode q : queries){
			RQ.addVertex(q);
			
			for(VisualEdge e : q.getOutEdges()){
				if(e.getToNode().getType()== NodeType.NODE_TYPE_RELATION || e.getToNode().getType()== NodeType.NODE_TYPE_VIEW){
					RQ.addEdge(e);
				}
			}
		}
		
		for(VisualNode v : views){
			RQ.addVertex(v);
			
			for(VisualEdge e : v.getOutEdges()){
				if(e.getToNode().getType()== NodeType.NODE_TYPE_RELATION || e.getToNode().getType()== NodeType.NODE_TYPE_VIEW){
					RQ.addEdge(e);
				}
			}
		}
		
		setUpView();
	}
	
	private void setUpView(){
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! EDGE BETWEENNESS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		final Graph<VisualNode, VisualEdge> graph = this.RQ;
		
		final AggregateLayout<VisualNode, VisualEdge> layout = new AggregateLayout<VisualNode, VisualEdge>(new VisualClustersOnACircleLayout((VisualGraph)graph, endC));
		
		HecataeusViewer.getActiveViewer().setGraphLayout(layout);
		// i use the same transformers used on other layouts
		HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(MapTransformer.<VisualNode,Paint>getInstance(vertexPaints));
		
		HecataeusViewer.getActiveViewer().getRenderContext().setVertexShapeTransformer(new VisualNodeShape());

//		HecataeusViewer.vv.getRenderContext().setEdgeDrawPaintTransformer(MapTransformer.<VisualEdge,Paint>getInstance(edgePaints));
		
//		HecataeusViewer.vv.getRenderContext().setVertexStrokeTransformer(new Transformer<VisualNode,Stroke>() {
//			protected final Stroke THIN = new BasicStroke(1);
//				protected final Stroke THICK= new BasicStroke(2);
//				public Stroke transform(VisualNode v){
//					Paint c = edgePaints.get(v);
//					if (c == Color.LIGHT_GRAY)
//						return THIN;
//					else 
//						return THICK;
//				}
//		    });
		
		final JToggleButton groupVertices = new JToggleButton("Group Clusters");
		
		//Create slider to adjust the number of edges to remove when clustering
		final JSlider edgeBetweennessSlider = new JSlider(JSlider.HORIZONTAL);
		edgeBetweennessSlider.setBackground(Color.WHITE);
		edgeBetweennessSlider.setPreferredSize(new Dimension(500, 50));
		edgeBetweennessSlider.setPaintTicks(true);
		edgeBetweennessSlider.setMaximum(graph.getEdgeCount());
		edgeBetweennessSlider.setMinimum(0);
		edgeBetweennessSlider.setValue(0);
		edgeBetweennessSlider.setMajorTickSpacing(10);
		edgeBetweennessSlider.setPaintLabels(true);
		edgeBetweennessSlider.setPaintTicks(true);
		//edgeBetweennessSlider.setBorder(BorderFactory.createLineBorder(Color.black));


		final JPanel eastControls = new JPanel();
		eastControls.setOpaque(true);
		eastControls.setLayout(new BoxLayout(eastControls, BoxLayout.Y_AXIS));
		eastControls.add(Box.createVerticalGlue());
		eastControls.add(edgeBetweennessSlider);
		
		final String COMMANDSTRING = "Edges removed for clusters: ";
		final String eastSize = COMMANDSTRING + edgeBetweennessSlider.getValue();
		
		final TitledBorder sliderBorder = BorderFactory.createTitledBorder(eastSize);
		eastControls.setBorder(sliderBorder);
		//eastControls.add(eastSize);
		eastControls.add(Box.createVerticalGlue());
		
		groupVertices.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			cluster(layout, edgeBetweennessSlider.getValue(), e.getStateChange() == ItemEvent.SELECTED);
			HecataeusViewer.getActiveViewer().repaint();
		}});
		
		
		cluster(layout, 0, groupVertices.isSelected());
		
		edgeBetweennessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int numEdgesToRemove = source.getValue();
					cluster(layout, numEdgesToRemove, groupVertices.isSelected());
					sliderBorder.setTitle(COMMANDSTRING + edgeBetweennessSlider.getValue());
					eastControls.repaint();
					HecataeusViewer.getActiveViewer().validate();
					HecataeusViewer.getActiveViewer().repaint();
				}
			}
		});
		

//		south = new JPanel();
		JPanel grid = new JPanel(new GridLayout(2,1));
		grid.add(groupVertices);
		south.add(grid);
		south.add(eastControls);
		
		
	}
	
	public static void cluster(AggregateLayout<VisualNode,VisualEdge> layout, int numEdgesToRemove, boolean groupClusters) {
		
		Graph<VisualNode,VisualEdge> g = layout.getGraph();
		layout.removeAll();
		
		EdgeBetweennessClusterer<VisualNode,VisualEdge> clusterer = new EdgeBetweennessClusterer<VisualNode,VisualEdge>(numEdgesToRemove);
		Set<Set<VisualNode>> clusterSet = clusterer.transform(g);
		List<VisualEdge> edges = clusterer.getEdgesRemoved();
		
		int i = 0;
		//Clusters have same color
		for (Iterator<Set<VisualNode>> cIt = clusterSet.iterator(); cIt.hasNext();) {
		
			Set<VisualNode> vertices = cIt.next();
			Color c = similarColors[i % similarColors.length];
			
			colorCluster(vertices, c);
			if(groupClusters == true) {
				groupCluster(layout, vertices);
			}
			i++;
		}
//		for (VisualEdge e : g.getEdges()) {
//			
//			if (edges.contains(e)) {
//				edgePaints.put(e, Color.lightGray);
//			} else {
//				edgePaints.put(e, Color.black);
//			}
//		}
	}
	
	private static void colorCluster(Set<VisualNode> vertices, Color c) {
		for (VisualNode v : vertices) {
			vertexPaints.put(v, c);
		}
	}
	
	private static void groupCluster(AggregateLayout<VisualNode,VisualEdge> layout, Set<VisualNode> vertices) {
		if(vertices.size() < layout.getGraph().getVertexCount()) {
			Point2D center = layout.transform(vertices.iterator().next());
			Graph<VisualNode,VisualEdge> subGraph = DirectedSparseGraph.<VisualNode,VisualEdge>getFactory().create();
			for(VisualNode v : vertices) {
				subGraph.addVertex(v);
			}
			Layout<VisualNode,VisualEdge> subLayout =  new VisualCirclesLayout(subGraph);
			subLayout.setInitializer(HecataeusViewer.getActiveViewer().getGraphLayout());
			subLayout.setSize(new Dimension(40,40));
			
			layout.put(subLayout,center);
			HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(MapTransformer.<VisualNode,Paint>getInstance(vertexPaints));
			HecataeusViewer.getActiveViewer().repaint();
		}
	}
}