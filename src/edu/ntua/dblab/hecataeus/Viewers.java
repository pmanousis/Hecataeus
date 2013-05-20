/**
 * 
 */
package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeLabel;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeToolTips;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFont;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeIcon;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeLabel;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeShape;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeToolTips;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.LayoutLensShapePickSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * @author eva
 *
 */
public class Viewers extends VisualizationViewer<VisualNode, VisualEdge> {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public HecataeusViewer hec;

	public VisualizationViewer<VisualNode, VisualEdge> vv;
	public Viewers(Layout<VisualNode, VisualEdge> layout) {
		super(layout);

		vv = new VisualizationViewer<VisualNode, VisualEdge>(layout);
//		layout = new VisualAggregateLayout(hec.graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		 //the visualization viewer
		Dimension prefferedSize = Toolkit.getDefaultToolkit().getScreenSize(); 
        vv = new VisualizationViewer<VisualNode, VisualEdge>(layout);
		vv.setSize(new Dimension((int)prefferedSize.getWidth()/2,(int)prefferedSize.getHeight()/2));
        vv.setBackground(Color.white);
		vv.setPickSupport(new LayoutLensShapePickSupport<VisualNode, VisualEdge>(vv));
		
		vv.setVertexToolTipTransformer(new VisualNodeToolTips());
		vv.setEdgeToolTipTransformer(new VisualEdgeToolTips());
		// the renderer of the vv
		RenderContext<VisualNode, VisualEdge>  pr = vv.getRenderContext();
		// the labels of the Vertices
		pr.setVertexLabelTransformer(new VisualNodeLabel());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO); 
		// the fonts of the vertices
		pr.setVertexFontTransformer(new VisualNodeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8)));
		//the shape of the edges
		pr.setEdgeShapeTransformer(new EdgeShape.QuadCurve<VisualNode, VisualEdge>());
		// the labels of the Edges
		pr.setEdgeLabelTransformer(new VisualEdgeLabel());
		// call the setVertexPaintFunction to paint the nodes
		pr.setVertexFillPaintTransformer(new VisualNodeColor(vv.getPickedVertexState()));
		// call the setEdgePaintFunction to paint the edges
		pr.setEdgeFillPaintTransformer( new VisualEdgeColor(vv.getPickedEdgeState()));
		// call the setVertexShapeFunction to set the shape of the nodes
		pr.setVertexShapeTransformer(new VisualNodeShape());
		// call the setNodeVisible to set the shape of the nodes according to
		pr.setVertexIncludePredicate(new VisualNodeVisible());
		pr.setVertexIconTransformer(new VisualNodeIcon());
		
		vv.getRenderContext().getMultiLayerTransformer().addChangeListener(vv);
		HecataeusModalGraphMouse gm = new HecataeusModalGraphMouse();
		vv.setGraphMouse(gm);
		vv.addGraphMouseListener(gm);
		
	}
	

}
