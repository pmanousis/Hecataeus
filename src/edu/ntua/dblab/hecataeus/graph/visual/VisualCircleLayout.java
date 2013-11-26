package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualCircleLayout extends AbstractLayout<VisualNode, VisualEdge>{

	protected VisualGraph graph;
	
	private List<VisualNode> nodes;
	private List<VisualNode> queries = new ArrayList<VisualNode>();
	private List<VisualNode> relations = new ArrayList<VisualNode>();
	private List<VisualNode> views = new ArrayList<VisualNode>();
	
	
	protected List<String> files = new ArrayList<String>();
	protected List<VisualNode> RQV = new ArrayList<VisualNode>();
	
	
	public VisualCircleLayout(VisualGraph g) {
		super(g);
		this.graph = g;
		
		nodes = new ArrayList<VisualNode>((Collection<? extends VisualNode>) g.getVertices());
		for(VisualNode v : nodes){
			
			if(v.getType().getCategory() == NodeCategory.MODULE ){
				
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_CONTAINS){
						if(files.contains(e.getFromNode().getName())==false){
							files.add(e.getFromNode().getName());
						}
						
					}
				}
			}
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				getQueries().add(v);
			}
			else if(v.getType() == NodeType.NODE_TYPE_RELATION){
				List<VisualEdge> edges = new ArrayList<VisualEdge>(v._inEdges);
				for(VisualEdge e : edges){
					if(e.getType() == EdgeType.EDGE_TYPE_USES){
						if(getRelations().contains(v)==false){
							getRelations().add(v);
						}
					}
				}
			}
			else if(v.getType() == NodeType.NODE_TYPE_VIEW){
				getViews().add(v);
			}
		}
		
		
		for(VisualNode r : getRelations()){
			for(int i =0; i < r.getInEdges().size(); i++){
				if(r.getInEdges().get(i).getType()== EdgeType.EDGE_TYPE_USES){
					if(RQV.contains(r) == false){
						RQV.add(r);
					}
				}
			}
		}

		for(VisualNode q: getQueries()){
			RQV.add(q);
		}

		for(VisualNode v:getViews()){
			if(RQV.contains(v)==false){
				RQV.add(v);
			}
		}
		
		VisualFileColor vfc = new VisualFileColor();
		vfc.setFileNames(files);
		
	}

	
	protected double getSmallRad(List<VisualNode> komboi){
		return(Math.log(komboi.size()*komboi.size()*komboi.size())+2*komboi.size());
	}
	protected double getQuery(int numOfNodes){
		return(Math.log(numOfNodes*numOfNodes*numOfNodes)+2*numOfNodes);
	}
	
	
	protected ArrayList<VisualNode> relationsInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> relations = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_RELATION){
				relations.add(v);
			}
		}
		return relations;
	}
	
	protected ArrayList<VisualNode> queriesInCluster(List<VisualNode> nodes){
		ArrayList<VisualNode> queries = new ArrayList<VisualNode>();
		for(VisualNode v : nodes){
			if(v.getType() == NodeType.NODE_TYPE_QUERY){
				queries.add(v);
			}
		}
		return queries;
	}
	
	public List<String> getFileNames(){
		if(files!=null){
			return files;
		}
		else{
			return null;
		}
	}
	
	@Override
	public void initialize() {
	}

	@Override
	public void reset() {
	}


	public List<VisualNode> getRelations() {
		return relations;
	}


	public void setRelations(List<VisualNode> relations) {
		this.relations = relations;
	}


	public List<VisualNode> getQueries() {
		return queries;
	}


	public void setQueries(List<VisualNode> queries) {
		this.queries = queries;
	}


	public List<VisualNode> getViews() {
		return views;
	}


	public void setViews(List<VisualNode> views) {
		this.views = views;
	}

}
