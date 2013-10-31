package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;

import edu.ntua.dblab.hecataeus.HecataeusInputDialog;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.visual.VisualCircleClusteredLayout.ClusterE;
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

public enum VisualLayoutType {
	StaticLayout,
	CircleLayout,
	BalloonLayout,
	Right2LeftTopologicalLayout,
	Top2DownTopologicalLayout,
	Left2RightTopologicalLayout,
	Left2RightInverseTopologicalLayout,
	Down2TopTopologicalLayout,
	RadialTreeLayout,
	KKLayout,
	FRLayout,
	ISOMLayout,
	SpringLayout,
	EvaTestLayout,
	AnOtherEvaTestLayout,
	EvaTestLayout1,
	ConcentricCircleLayout,
	EvaSpringTestLayout,
	EvaDAGTestLayout,
	ClusteredCircleLayout,
	ClusteredCircleLayoutQ,
	ClusteredCircleLayoutV,
	ClusteredCircleLayoutR,
	ClusteredCircleLayoutC,
	GridEvaLayout,
	ClustersonaCircleLayout,
	CirclingClusterLayout,
	DebianCircleLayout,
	HalfCircleLayout,
	StarLayout,
	PizzaSliceLayout,
	EdgeBetweennessClustering;

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
			case Right2LeftTopologicalLayout:
				return "Right to Left Topological Layout";
			case Top2DownTopologicalLayout:
				return "Top Down Topological Layout";
			case Left2RightTopologicalLayout:
				return "Left to Right Topological Layout";
			case Left2RightInverseTopologicalLayout:
				return "Left to Right Topological Layout (Inverse)";
			case Down2TopTopologicalLayout:
				return "Bottom Up Topological Layout";
			case EvaTestLayout:
				return "EvaTestLayout";
			case AnOtherEvaTestLayout:
				return "AnOtherEvaTestLayout";
			case EvaTestLayout1:
				return "EvaTestLayout1";
			case ConcentricCircleLayout:
				return "ConcentricCircleLayout";
			case EvaSpringTestLayout:
				return "EvaSpringTestLayout";
			case EvaDAGTestLayout:
				return "EvaDagTestLayout";
			case ClusteredCircleLayout:
				return "Clustered Circle Layout";
			case GridEvaLayout:
				return "GridEvaLayout";
			case ClusteredCircleLayoutQ:
				return "";
			case ClusteredCircleLayoutV:
				return "";
			case ClusteredCircleLayoutR:
				return "";
			case ClusteredCircleLayoutC:
				return "";
			case ClustersonaCircleLayout:
				return "Clusters on a Circle";
			case CirclingClusterLayout:
				return "Clusters in Circles";
			case DebianCircleLayout:
				return "Spiral";
			case HalfCircleLayout:
				return "Half Cilcle";
			case StarLayout:
				return "Star Layout";
			case PizzaSliceLayout:
				return "Pizza Slice Cluster Layout";
			case EdgeBetweennessClustering:
				return "Edge Betweenness Clustering";
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
			case Right2LeftTopologicalLayout: 
				layout = new VisualTopologicalLayout(g, Orientation.RIGHT2LEFT);
				layout.setInitialPosition(initialPosition);
				return layout;
			case Top2DownTopologicalLayout : 
				layout = new VisualTopologicalLayout(g, Orientation.TOP2DOWN);
				layout.setInitialPosition(initialPosition);
				return layout;
			case Left2RightInverseTopologicalLayout: 
				layout = new VisualTopologicalLayout(g, Orientation.INVERSELEFT2RIGHT);
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
			case Right2LeftTopologicalLayout: 
				return new VisualTopologicalLayout(g, Orientation.RIGHT2LEFT);
			case Top2DownTopologicalLayout : 
				return new VisualTopologicalLayout(g, Orientation.TOP2DOWN);
			case Down2TopTopologicalLayout: 
				return new VisualTopologicalLayout(g, Orientation.DOWN2TOP);
			case Left2RightTopologicalLayout : 
				return new VisualTopologicalLayout(g, Orientation.LEFT2RIGHT);
			case Left2RightInverseTopologicalLayout : 
				return new VisualTopologicalLayout(g, Orientation.INVERSELEFT2RIGHT);
			case RadialTreeLayout:
				return new RadialTreeLayout<VisualNode, VisualEdge>(new DelegateForest<VisualNode, VisualEdge>(g));
			case EvaTestLayout:
				return new VisualTopologicalLayout(g, Orientation.evaTest);
			case AnOtherEvaTestLayout:
				return new VisualTopologicalLayout(g, Orientation.evaTest1);
			case EvaTestLayout1:
				return new VisualTopologicalLayout(g, Orientation.evaTest2);
			case ConcentricCircleLayout:
				return new VisualConcentricCircleLayout(g);
			case EvaSpringTestLayout:
				return new VisualNewSpringLayout<VisualNode, VisualEdge>(g);
			case EvaDAGTestLayout:
				return new VisualDagLayout(g);
			case ClusteredCircleLayoutQ:
				return new VisualCircleClusteredLayout(g, ClusterE.Queries);
			case ClusteredCircleLayoutV:
				return new VisualCircleClusteredLayout(g, ClusterE.Views);
			case ClusteredCircleLayoutR:
				return new VisualCircleClusteredLayout(g, ClusterE.Relations);
			case ClusteredCircleLayoutC:
				return new VisualCircleClusteredLayout(g, ClusterE.Circle);	
			case GridEvaLayout:
				return new VisualGridEvaLayout(g);
			case ClustersonaCircleLayout:
				HecataeusInputDialog d1 = new HecataeusInputDialog(HecataeusViewer.getHecFrame(), "Clustering Condition");
				return new VisualClustersOnACircleLayout(g, d1.getC());
			case CirclingClusterLayout:
				HecataeusInputDialog d2 = new HecataeusInputDialog(HecataeusViewer.getHecFrame(), "Clustering Condition");
				return new VisualCirclingClustersLayout(g, d2.getC());
			case DebianCircleLayout:
				HecataeusInputDialog d3 = new HecataeusInputDialog(HecataeusViewer.getHecFrame(), "Clustering Condition");
				return new VisualDebianCircleLayout(g, d3.getC());
			case HalfCircleLayout:
				HecataeusInputDialog d4 = new HecataeusInputDialog(HecataeusViewer.getHecFrame(), "Clustering Condition");
				return new VisualHalfCircleClusters(g, d4.getC());
			case StarLayout:
				HecataeusInputDialog d5 = new HecataeusInputDialog(HecataeusViewer.getHecFrame(), "Clustering Condition");
				return new VisualStarLayout(g, d5.getC());
			case PizzaSliceLayout:
				HecataeusInputDialog d6 = new HecataeusInputDialog(HecataeusViewer.getHecFrame(), "Clustering Condition");
				return new VisualPizzaSliceClusterLayout(g, d6.getC());
			case EdgeBetweennessClustering:
				return new VisualEdgeBetweennessClustering(g);
			default: 
				return new StaticLayout<VisualNode, VisualEdge>(g);
			}
		}
		
}
