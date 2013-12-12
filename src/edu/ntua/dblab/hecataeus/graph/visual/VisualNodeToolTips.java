package edu.ntua.dblab.hecataeus.graph.visual;


import java.util.ArrayList;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

/**
 * 
 * class for setting tooltip on the graph
 */
public final class VisualNodeToolTips implements Transformer<VisualNode,String> {

	public String transform(VisualNode node) {
		if (node.getVisible()) {
			
			if(node.getType()==NodeType.NODE_TYPE_CLUSTER){
				
				String tooltip="<html>";
				String cat = node.getType().toString();
				tooltip += cat + "  ";

				int clusterId = Integer.parseInt(node.getName());
				String nN = node.getName();
				VisualTotalClusters vtc = new VisualTotalClusters();
				ArrayList<VisualCluster> myClusters = vtc.getClusters();
				VisualCluster myCluster = new VisualCluster();
				for(VisualCluster cl : myClusters){
					if(cl.getClusterId()==clusterId){
						myCluster = cl;
					}
				}
				ArrayList<VisualNode> rel = new ArrayList<VisualNode>(myCluster.getRelationsInCluster());
				ArrayList<VisualNode> view = new ArrayList<VisualNode>(myCluster.getViewsInCluster());
				String relNames = "";
				for(VisualNode v : rel){
					relNames += v.getName()+"\n <br>";
				}
				String viewNames = "";
				for(VisualNode v : view){
					viewNames += v.getName()+"\n<br> ";
				}
				tooltip += "<br><b>CLUSTER NAME:</b>  <b>" +  node.getNodeLabel() + "</b><br>";
				tooltip += "\n<br><b>Realions in cluster:</b><br> " + relNames;
				tooltip += "\n<br><b>Views in cluster:</b><br>" + viewNames;
				tooltip += "</html>";
				return tooltip;
			}
			else{
				String tooltip="<html>";
				String cat = node.getType().getCategory().toString();
				tooltip += cat + "  ";
				if(node.getType() == NodeType.NODE_TYPE_QUERY){     //gia na kserw to arxeio pou einai to query
					tooltip+="<br>From file <b>"+ node.getFileName() +"</b><br>";
				}
				if ((node.getSQLDefinition()!=null)&&(!node.getSQLDefinition().isEmpty())) {
					tooltip += "<b>SQL Definition</b><br>";
					String definition = node.getSQLDefinition();
					definition= definition.replace("\n","<br>");
					
					tooltip += definition;
					tooltip += "<hr>";
					
				}
				
				if (node.getHasPolicies()) {
					for (EvolutionPolicy<VisualNode> p : node.getPolicies()) {
						tooltip += "<i>Policy:</i>" + p;
						tooltip += "<br>";
					}
				}
				if (node.getHasEvents()) {
					for (EvolutionEvent<VisualNode> e: node.getEvents()) {
						tooltip += "<i>Event:</i> " + e;
						tooltip += "<br>";
					}
				}
				
				if (node.getType()== NodeType.NODE_TYPE_FILE){		//
					tooltip += "<i>Path: </i>"+node.getPath()+"<br/>";		//added by sgerag
				}																						//
				else { 																					//
					tooltip += "<i>Line: </i>"+node.getLine()+"<br/>";		//added by sgerag
				}	
				tooltip += "<i>Status: </i>"
					+ node.getStatus();
				tooltip += "</html>";
			return tooltip;
			}
		}
		return null;
		
	}
}