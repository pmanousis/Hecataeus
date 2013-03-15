package edu.ntua.dblab.hecataeus.graph.visual;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;


/**
 * 
 * class for setting tooltip on the graph
 */
public final class VisualEdgeToolTips implements Transformer<VisualEdge,String> {

	public String transform(VisualEdge edge) {
		if (edge.getFromNode().getVisible()&&edge.getToNode().getVisible()) {
			String tooltip = "<html>" + edge.getFromNode().getName() + " <i>"
			+ edge.getName() + " </i>" + edge.getToNode().getName();

			if (edge.getStatus()!=StatusType.NO_STATUS) {
				tooltip += "<br>";
				tooltip += "<i>Status: </i>"
					+ edge.getStatus();
			}
			tooltip += "</html>";
			return tooltip;			
		}
		return null;
	}
}