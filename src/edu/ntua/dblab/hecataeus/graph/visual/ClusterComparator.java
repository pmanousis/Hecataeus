package edu.ntua.dblab.hecataeus.graph.visual;
import java.util.Comparator;
import clusters.EngineConstructs.Cluster;

public class ClusterComparator implements Comparator <Cluster> {
	@Override
	public int compare(Cluster vert1, Cluster vert2) {
		if(vert1.getNodesOfCluster().size() > vert2.getNodesOfCluster().size()){
			return(1);
		}
		else if(vert1.getNodesOfCluster().size() < vert2.getNodesOfCluster().size())
		{
			return(-1);
		}
		return(0);
	}

}
