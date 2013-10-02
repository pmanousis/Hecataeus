package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.Comparator;

public class ListComparator implements Comparator <ArrayList<VisualNode>> {

	@Override
	public int compare(ArrayList<VisualNode> vert1, ArrayList<VisualNode> vert2) {
		if(vert1.size() > vert2.size()){
			return -1;
		}
		else{
			return 0;
		}
	}

}
