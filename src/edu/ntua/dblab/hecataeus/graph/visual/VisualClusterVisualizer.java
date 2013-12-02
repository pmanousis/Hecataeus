package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.List;

public abstract class VisualClusterVisualizer {

	public abstract double getSmallRad(List<VisualNode> komboi);
	public abstract double getQueryRad(int numOfNodes);
	public abstract ArrayList<VisualNode> relationsInCluster(List<VisualNode> nodes);
	public abstract ArrayList<VisualNode> queriesInCluster(List<VisualNode> nodes);
	public abstract ArrayList<VisualNode> viewsInCluster(List<VisualNode> nodes);
	public abstract List<String> getFileNames();
	public abstract List<VisualNode> getRelations();
	
}
