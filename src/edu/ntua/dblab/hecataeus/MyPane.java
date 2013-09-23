package edu.ntua.dblab.hecataeus;

import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;

@SuppressWarnings("serial")
public class MyPane extends GraphZoomScrollPane{

	//protected VisualGraph g;
	
	
	public MyPane (VisualNode head, VisualizationViewer<VisualNode, VisualEdge> vvPane, VisualGraph subGraph){
		super(vvPane);

		//g = subGraph;
		ArrayList<VisualNode> mySet = new ArrayList<VisualNode>();
		mySet.add(head);
		
		
		
//		for(int i = 0; i < head.getOutEdges().size(); i++){
//			if(head.getOutEdges().get(1).getType() == EdgeType.EDGE_TYPE_SEMANTICS){
//				
//			}
//		}
		List<VisualNode> VisibleNodes = new ArrayList<VisualNode>();
		
		
		
		VisibleNodes = subGraph.getNodes();
		
	//	VisualNode thisNode = new VisualNode();
		
		//for(VisualNode thisNode : VisibleNodes){
//		for(int i = 0; i < VisibleNodes.size(); i++){
//			VisualNode thisNode = new VisualNode();
//			thisNode = VisibleNodes.get(i);
//			thisNode.setVisible(true);
//		}
//			
		
		
		List<VisualNode> nodes = new ArrayList<VisualNode>();
		List<VisualNode> newNodes = new ArrayList<VisualNode>();
		VisualNode myNode;
//		for(VisualNode node :mySet) {
//			
//			
//			nodes = subGraph.getModule(node);
//			
//			System.out.println(" nodes  "   + nodes);
//			if(subGraph.getMyViewer().getName()!="full zoom"){
//			if (node.getType().getCategory()== NodeCategory.MODULE||node.getType().getCategory()== NodeCategory.INOUTSCHEMA) {
//				List<VisualNode> module = subGraph.getModule(node);
//				for (VisualNode child : module) {
//					myNode = new VisualNode();
//					myNode = child;
//					myNode.setVisible(true);
//					newNodes.add(myNode);
//					//child.setVisible(!child.getVisible());
//				}
//				node.setVisible(true);
//			}
//			if (node.getType().getCategory()==NodeCategory.CONTAINER) {
//				List<VisualNode> module = subGraph.getModule(node);
//				for (VisualNode child : module) {
//					myNode = new VisualNode();
//					myNode = child;
//					myNode.setVisible(true);
//					newNodes.add(myNode);
//					//child.setVisible(!child.getVisible());
//				}
//				//node.setVisible(true);
//			}
//			}
//		}
//
//		
//		subGraph = g.toGraph(new ArrayList<VisualNode>(nodes));
//		//vvPane.
	
	}


	
}
