package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;

import org.apache.commons.collections15.Transformer;

public class VisualNodeLocation implements Transformer<VisualNode, Point2D>{

	@Override
	public Point2D transform(VisualNode node) {
		return node.getLocation();
	}
}
