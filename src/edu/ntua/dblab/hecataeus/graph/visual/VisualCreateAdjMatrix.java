package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;

public class VisualCreateAdjMatrix {

	private List<VisualNode> RQV;
	
	
	public VisualCreateAdjMatrix(List<VisualNode> RQV){
		
		this.RQV = new ArrayList<VisualNode>(RQV);
	}
	
	protected int[][] createAdjMatrix(){
		System.out.println("----- creating adj matrix  -------");
		int[][] adj = new int [this.RQV.size()][this.RQV.size()];
		int pos = 0, k = 0;
		for(int i = 0; i < this.RQV.size(); i++){
			VisualNode v = this.RQV.get(i);
			List<VisualEdge> outE = new ArrayList<VisualEdge>(v._outEdges);
			List<VisualEdge> inE = new ArrayList<VisualEdge>(v._inEdges);
			List<VisualEdge> allE = new ArrayList<VisualEdge>();
			allE.addAll(outE);
			allE.addAll(inE);
			for(VisualEdge e : outE){
				if(e.getType() == EdgeType.EDGE_TYPE_USES){	
					VisualNode toNode = e.getToNode();
					k = this.RQV.indexOf(v);
					pos = this.RQV.indexOf(toNode);
					adj[pos][k] = 1;
					adj[k][pos] = 1;
				}
			}
		}
		System.out.println("----- done !!----------");
		return adj;
	}
}
