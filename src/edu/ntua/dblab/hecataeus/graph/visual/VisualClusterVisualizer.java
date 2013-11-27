package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.util.List;

public abstract class VisualClusterVisualizer {

	public abstract Point2D DrawClusterCircle(List<VisualNode> nodes, double cx, double cy);
	
}
