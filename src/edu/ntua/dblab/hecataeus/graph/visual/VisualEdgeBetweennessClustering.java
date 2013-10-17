
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Stroke;
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

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import aurelienribon.slidinglayout.SLPanel;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;


@SuppressWarnings("serial")
public class VisualEdgeBetweennessClustering  extends VisualCircleLayout{

	
	//public static SLPanel south = null;

	public static JPanel south = null;
	
	public final  Color[] similarColors = {
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
	
		@SuppressWarnings({ "unchecked", "rawtypes" })
	static Map<VisualNode,Paint> vertexPaints = LazyMap.<VisualNode,Paint>decorate(new HashMap<VisualNode,Paint>(), new ConstantTransformer(Color.white));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		static Map<VisualEdge,Paint> edgePaints = LazyMap.<VisualEdge,Paint>decorate(new HashMap<VisualEdge,Paint>(), new ConstantTransformer(Color.blue));

	public VisualEdgeBetweennessClustering(VisualGraph g) {
		super(g);
		this.graph = g;
		
		vcl = new VisualCircleLayout(this.graph);
		List<VisualNode> queries = new ArrayList<VisualNode>();
		List<VisualNode> relations = new ArrayList<VisualNode>();
		List<VisualNode> views = new ArrayList<VisualNode>();
		
		
		relations = new ArrayList<VisualNode>(vcl.relations);
		queries =new ArrayList<VisualNode>( vcl.queries);
		views = new ArrayList<VisualNode>(vcl.views);
	
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
		
		final AggregateLayout<VisualNode, VisualEdge> layout = new AggregateLayout<VisualNode, VisualEdge>(new FRLayout<VisualNode, VisualEdge>(graph));
		
		HecataeusViewer.vv.setGraphLayout(layout);
		HecataeusViewer.vv.getRenderContext().setVertexFillPaintTransformer(MapTransformer.<VisualNode,Paint>getInstance(vertexPaints));
		
		HecataeusViewer.vv.getRenderContext().setVertexShapeTransformer(new VisualNodeShape());

		HecataeusViewer.vv.getRenderContext().setEdgeDrawPaintTransformer(MapTransformer.<VisualEdge,Paint>getInstance(edgePaints));
		
		HecataeusViewer.vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<VisualEdge,Stroke>() {
			protected final Stroke THIN = new BasicStroke(1);
				protected final Stroke THICK= new BasicStroke(2);
				public Stroke transform(VisualEdge e){
					Paint c = edgePaints.get(e);
					if (c == Color.LIGHT_GRAY)
						return THIN;
					else 
						return THICK;
				}
		    });
		
		
		
		//DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		//HecataeusViewer.vv.setGraphMouse(gm);
		
		final JToggleButton groupVertices = new JToggleButton("Group Clusters");
		
		//Create slider to adjust the number of edges to remove when clustering
		final JSlider edgeBetweennessSlider = new JSlider(JSlider.HORIZONTAL);
		edgeBetweennessSlider.setBackground(Color.WHITE);
		edgeBetweennessSlider.setPreferredSize(new Dimension(350, 50));
		edgeBetweennessSlider.setPaintTicks(true);
		edgeBetweennessSlider.setMaximum(graph.getEdgeCount());
		edgeBetweennessSlider.setMinimum(0);
		edgeBetweennessSlider.setValue(0);
		edgeBetweennessSlider.setMajorTickSpacing(10);
		edgeBetweennessSlider.setPaintLabels(true);
		edgeBetweennessSlider.setPaintTicks(true);
		
		//edgeBetweennessSlider.setBorder(BorderFactory.createLineBorder(Color.black));
		//TO DO: edgeBetweennessSlider.add(new JLabel("Node Size (PageRank With Priors):"));
		//I also want the slider value to appear
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
			clusterAndRecolor(layout, edgeBetweennessSlider.getValue(), similarColors, e.getStateChange() == ItemEvent.SELECTED);
			HecataeusViewer.vv.repaint();
		}});
		
		
		clusterAndRecolor(layout, 0, similarColors, groupVertices.isSelected());
		
		edgeBetweennessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int numEdgesToRemove = source.getValue();
					clusterAndRecolor(layout, numEdgesToRemove, similarColors,
					groupVertices.isSelected());
					sliderBorder.setTitle(
					COMMANDSTRING + edgeBetweennessSlider.getValue());
					eastControls.repaint();
					HecataeusViewer.vv.validate();
					HecataeusViewer.vv.repaint();
				}
			}
		});
		
		       // Container content = getContentPane();
		//frame.getContentPane().add(new GraphZoomScrollPane(vv));
		
		
//		south = new JPanel();
		JPanel grid = new JPanel(new GridLayout(2,1));
		grid.add(groupVertices);
		south.add(grid);
		south.add(eastControls);
		
		
	}
	
	public static void clusterAndRecolor(AggregateLayout<VisualNode,VisualEdge> layout, int numEdgesToRemove, Color[] colors, boolean groupClusters) {
		
		Graph<VisualNode,VisualEdge> g = layout.getGraph();
		layout.removeAll();
		
		EdgeBetweennessClusterer<VisualNode,VisualEdge> clusterer = new EdgeBetweennessClusterer<VisualNode,VisualEdge>(numEdgesToRemove);
		Set<Set<VisualNode>> clusterSet = clusterer.transform(g);
		List<VisualEdge> edges = clusterer.getEdgesRemoved();
		
		int i = 0;
		//Set the colors of each node so that each cluster's vertices have the same color
		for (Iterator<Set<VisualNode>> cIt = clusterSet.iterator(); cIt.hasNext();) {
		
			Set<VisualNode> vertices = cIt.next();
			Color c = colors[i % colors.length];
			
			colorCluster(vertices, c);
			if(groupClusters == true) {
				groupCluster(layout, vertices);
			}
			i++;
		}
		for (VisualEdge e : g.getEdges()) {
			
			if (edges.contains(e)) {
				edgePaints.put(e, Color.lightGray);
			} else {
				edgePaints.put(e, Color.black);
			}
		}
		
	}
	
	private static void colorCluster(Set<VisualNode> vertices, Color c) {
		for (VisualNode v : vertices) {
			vertexPaints.put(v, c);
		}
	}
	
	private static void groupCluster(AggregateLayout<VisualNode,VisualEdge> layout, Set<VisualNode> vertices) {
		if(vertices.size() < layout.getGraph().getVertexCount()) {
			Point2D center = layout.transform(vertices.iterator().next());
			Graph<VisualNode,VisualEdge> subGraph = SparseMultigraph.<VisualNode,VisualEdge>getFactory().create();
			for(VisualNode v : vertices) {
				subGraph.addVertex(v);
			}
			Layout<VisualNode,VisualEdge> subLayout =  new CircleLayout<VisualNode,VisualEdge>(subGraph);
			subLayout.setInitializer(HecataeusViewer.vv.getGraphLayout());
			subLayout.setSize(new Dimension(40,40));
			
			layout.put(subLayout,center);
			HecataeusViewer.vv.repaint();
		}
	}
}