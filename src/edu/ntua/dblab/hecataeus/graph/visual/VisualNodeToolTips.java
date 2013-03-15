package edu.ntua.dblab.hecataeus.graph.visual;


import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;

/**
 * 
 * class for setting tooltip on the graph
 */
public final class VisualNodeToolTips implements Transformer<VisualNode,String> {

	public String transform(VisualNode node) {
		if (node.getVisible()) {
			String tooltip="<html>";
			
			if ((node.getSQLDefinition()!=null)&&(!node.getSQLDefinition().isEmpty())) {
				tooltip += "<b>SQL Definition</b><br>";
				String definition = node.getSQLDefinition();
				definition= definition.replace("\n","<br>");
				
				tooltip += definition;
				tooltip += "<hr>";
				
			}
			
			if (node.getHasPolicies()) {
				for (int i = 0; i < node.getPolicies().size(); i++) {
					EvolutionPolicy p = node.getPolicies().get(i);
					tooltip += "<i>Policy:</i>" + p;
					tooltip += "<br>";
				}
			}
			if (node.getHasEvents()) {
				for (int i = 0; i < node.getEvents().size(); i++) {
					EvolutionEvent e = node.getEvents().get(i);
					tooltip += "<i>Event:</i> " + e;
					tooltip += "<br>";
				}
			}
			
			tooltip += "<i>Status: </i>"
				+ node.getStatus();
			tooltip += "</html>";

			return tooltip;
		}
		return null;
		
	}
}