package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualGridEvaLayout extends AbstractLayout<VisualNode,VisualEdge>{
	
	protected VisualGraph graph;
	
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	private List<VisualNode> nodes;
	public static List<String> files = new ArrayList<String>();
	
	
	
	
	
	
	public VisualGridEvaLayout(VisualGraph g){
		super(g);
		this.graph = g;
		
		
		nodes = new ArrayList<VisualNode>((Collection<? extends VisualNode>) g.getVertices());
		for(VisualNode v : nodes){

			if(v.getType().getCategory() == NodeCategory.MODULE ){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
			
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
						if(files.contains(e.getFromNode().getName())==false)
						{
							files.add(e.getFromNode().getName());
						}
						
					}
				}
			}
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				relations.add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				views.add(v);
			}
		}
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		

	    int distX=100;
	    int distY=100;



	    int i = 0;int operatingNode = 1;
	    for(VisualNode v : relations){
	    	Point2D coord = transform(v);
	    	coord.setLocation(distX, distY+i);
	    	i++;
	    }
	    distY+=i;
	    
	    for(VisualNode n : queries){
    		Point2D coordN = transform(n);
    		coordN.setLocation(distX+operatingNode, distY);
	    	operatingNode++;
    	}
	    
	    
	    for(VisualNode v : relations){
	    	Point2D locr = v.getLocation();
	    	Point2D locq;
	    	List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
	    	for(VisualEdge edgeIndx : outE){
				if(edgeIndx.getToNode()!=null){
					if(edgeIndx.getToNode().getType() == NodeType.NODE_TYPE_QUERY){
						locq = edgeIndx.getToNode().getLocation();
						VisualNode eva = new VisualNode();
						Point2D newLoc = new Point2D.Double(locq.getX(), locr.getY());
						eva.setLocation(newLoc);
						eva.setVisible(true);
						this.graph.addVertex(eva);
					}
				}
			}
	    }
	    
//	    int operatingNode = 0;
//
//	    for (int i=0;i<numRows;i++) {
//	        for (int j=0;j<numColumns;j++) {
//	            layout.setLocation(String.valueOf(operatingNode++), i*distX, j*distY);
//	        }
//	    }        
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}


}
