package edu.ntua.dblab.hecataeus.graph.visual;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

public class VisualCircleClusteredLayout extends AbstractLayout<VisualNode,VisualEdge> {
	
	public enum Cluster{
		Queries,
		Views,
		Relations;
	}
	
	protected Cluster clusterType;
	protected VisualGraph graph;
	
	public VisualCircleClusteredLayout(VisualGraph g, Cluster cluster){
		super(g);
		this.graph = g;
		this.clusterType = cluster;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		switch (this.clusterType) {
		case Queries:
			clusterQueries();
			break;
		case Views:
			clusterViews();
			break;
		case Relations:
			clusterRelations();
			break;
		default:
			whatever();
		}
		
	}

	private void whatever() {
		// TODO Auto-generated method stub
		
	}

	private void clusterRelations() {
		// TODO Auto-generated method stub
		
	}

	private void clusterViews() {
		// TODO Auto-generated method stub
		
	}

	private void clusterQueries() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		initialize();
		
	}
}
