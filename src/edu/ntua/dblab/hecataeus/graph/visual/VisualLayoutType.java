package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import edu.ntua.dblab.hecataeus.graph.visual.VisualTopologicalLayout.Orientation;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

public enum VisualLayoutType {
	StaticLayout,
	CircleLayout,
	BalloonLayout,
	HorizontalTopologicalLayout,
	VerticalTopologicalLayout,
	InverseHorizontalTopologicalLayout,
	InverseVerticalTopologicalLayout,
	RadialTreeLayout,
	KKLayout,
	FRLayout,
	ISOMLayout,
	SpringLayout;
		
		/**
		 * Converts from the enum representation of a type to the corresponding String representation
		 *
		 */
		public String toString() {
			switch (this) {
			case BalloonLayout:
				return "Balloon Layout";
			case CircleLayout:
				return "Circle Layout";
			case FRLayout:
				return "FR Layout";
			case ISOMLayout:
				return "ISOM Layout";
			case KKLayout:
				return "KK Layout";
			case RadialTreeLayout:
				return "Radial Tree Layout";
			case SpringLayout:
				return "Spring Layout";
			case StaticLayout:
				return "Static Layout";
			case HorizontalTopologicalLayout:
				return "Horizontal Topological Layout";
			case VerticalTopologicalLayout:
				return "Vertical Topological Layout";
			case InverseHorizontalTopologicalLayout:
				return "Inverse Horizontal Topological Layout";
			case InverseVerticalTopologicalLayout:
				return "Inverse Topological Layout";
			default:
				return name();
			}
			
		}

		/**
		 * Converts from the String representation of a type to the corresponding enum representation
		 *
		 */
		public static VisualLayoutType toLayoutType(String value) {
			return valueOf(value);
		}
		
		public static Layout<VisualNode, VisualEdge> getLayoutFor(VisualGraph g, VisualLayoutType type, Point2D initialPosition) {
			VisualTopologicalLayout layout;
			switch (type) {
			case HorizontalTopologicalLayout: 
				layout = new VisualTopologicalLayout(g, Orientation.RIGHT2LEFT);
				layout.setInitialPosition(initialPosition);
				return layout;
			case VerticalTopologicalLayout : 
				layout = new VisualTopologicalLayout(g, Orientation.BOTTOMUP);
				layout.setInitialPosition(initialPosition);
				return layout;
			default: 
				return getLayoutFor(g, type); 
			}
			
		}
		public static Layout<VisualNode, VisualEdge> getLayoutFor(VisualGraph g, VisualLayoutType type) {
			switch (type) {
			case StaticLayout: 
				return new StaticLayout<VisualNode, VisualEdge>(g);
			case KKLayout: 
				return new KKLayout<VisualNode, VisualEdge>(g);
			case FRLayout: 
				return new FRLayout<VisualNode, VisualEdge>(g);
			case ISOMLayout: 
				return new ISOMLayout<VisualNode, VisualEdge>(g);
			case SpringLayout: 
				return new SpringLayout<VisualNode, VisualEdge>(g);
			case CircleLayout: 
				return new CircleLayout<VisualNode, VisualEdge>(g);
			case BalloonLayout: 
				return new BalloonLayout<VisualNode, VisualEdge>(new DelegateForest<VisualNode, VisualEdge>(g));
			case HorizontalTopologicalLayout: 
				return new VisualTopologicalLayout(g, Orientation.RIGHT2LEFT);
			case VerticalTopologicalLayout : 
				return new VisualTopologicalLayout(g, Orientation.BOTTOMUP);
			case RadialTreeLayout:
				return new RadialTreeLayout<VisualNode, VisualEdge>(new DelegateForest<VisualNode, VisualEdge>(g));
			default: 
				return new StaticLayout<VisualNode, VisualEdge>(g);
			}
		}
		
}
