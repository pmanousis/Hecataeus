package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;


public class VisualLayout /*extends VisualAggregateLayout implements Transformer<VisualNode, Point2D>*/{
	
	private static int cnt = 0;
	
//	public VisualLayout(VisualGraph graph){
	
//		super(graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
//		List<VisualNode> myNodes = new ArrayList<VisualNode>();
//		final Dimension prefferedSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//		
//		Transformer<VisualNode, Point2D> locationTransformer = new Transformer<VisualNode, Point2D>() {
//			@Override
//			public Point2D transform(VisualNode node) {
//				NodeType type = (node.getType());
//				Point2D p2d;
//				if(type.getCategory() == NodeCategory.SCHEMA){
//					p2d = new Point2D.Double(prefferedSize.getWidth()-200, 100+cnt);
//					cnt++;
//					return p2d;
//				}
//				else if (type.getCategory()== NodeCategory.MODULE){
//					p2d = new Point2D.Double(prefferedSize.getWidth()-400, 100+cnt);
//					cnt++;
//					return p2d;
//				}
//				else if (type.getCategory()== NodeCategory.CONTAINER){
//					p2d = new Point2D.Double(prefferedSize.getWidth()-600, 100+cnt);
//					cnt++;
//					return p2d;
//				}
//				else if (type.getCategory()== NodeCategory.INOUTSCHEMA){
//					p2d = new Point2D.Double(prefferedSize.getWidth()-800, 100+cnt);
//					cnt++;
//					return p2d;
//				}
//				else
//					p2d = new Point2D.Double(prefferedSize.getWidth()-300, 100+cnt);
//					return p2d;
//			}
//		};
//
//			
//		
//		
//	}
//
//	@Override
//	public Point2D transform(VisualNode arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
//	Transformer<Vertex, Point2D> locationTransformer = new Transformer<Vertex, Point2D>() {
//		@Override
//		public Point2D transform(VisualNode ) {
//			Point2D p2d = ;
//			return p2D;
//		}
//	};
	

}
