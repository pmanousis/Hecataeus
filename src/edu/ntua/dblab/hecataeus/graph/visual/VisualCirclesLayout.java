package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;

/**
 * @author eva
 * circular layout used only for edgebetweeness clusring
 */
public class VisualCirclesLayout extends VisualConcentricCircleLayout{

	private Graph graph;
	private double radius;
	private double width;
	private double height;
	
	public VisualCirclesLayout(Graph g) {
		super(g);
		this.graph = g;
	}
	@SuppressWarnings("unchecked")
	public void initialize() {
		Dimension d = getSize();
		
		if (d != null) {
			height = d.getHeight();
			width = d.getWidth();
		}
		
		List<VisualNode> nodes = new ArrayList<VisualNode>(this.graph.getVertices());
		drawCircles(nodes);
	}
	public void reset() {
		initialize();
	}
	
	public double setRadious(){
		this.radius = this.graph.getVertexCount()*3;
		return this.radius;
	}
	
	protected void drawCircles(List<VisualNode> nodes){
		int cnt = 0;
		double radius = setRadious();
		for(VisualNode n :  nodes){
			Point2D coord = transform(n);
			double angle = (2*Math.PI*cnt)/nodes.size();
			coord.setLocation(Math.cos(angle) * radius + width/2 , Math.sin(angle) * radius + height/2);
			n.setLocation(coord);
			cnt++;
		}
		
	}
	
}
