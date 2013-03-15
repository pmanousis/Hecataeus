/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;


import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

import java.awt.geom.Point2D; 

/**
 * The class implements a visual node of the graph
 * It holds the visual properties of the node
 * 
 * @author  gpapas
 */
public class VisualNode extends EvolutionNode<VisualEdge>{

	
	private Point2D location;
	private Point2D lastChildLocation;
	private Boolean isVisible  = true;
	//the corresponding EvolutionNode object
	//private EvolutionNode hecataeusEvolutionNode = null;
	
	public VisualNode(){
		super();
		this.location = new Point2D.Double();
		this.lastChildLocation = new Point2D.Double();
	}
	
	public VisualNode(String name, NodeType type) {
		super(name, type);
		this.location = new Point2D.Double();
		this.lastChildLocation = new Point2D.Double();
	}
	
	
//	public void setLocation(Point2D location){
//		this.location.setLocation(location);
//	}
//	
//	/**
//	 * @return  the node location
//	 */
//	public Point2D getLocation(){
//		return this.location; 
//	}
	
//	public void setLastChildLocation(double x,double y){
//		this.lastChildLocation.setLocation(x, y);
//	}
//	
//	/**
//	 * @return  the location of its child, if exists, otherwise null 
//	 */
//	public Point2D getLastChildLocation(){
//		if (!(this.lastChildLocation==null)){
//			return this.lastChildLocation;
//		}else return null;
//	}
	
	public void setVisible(boolean Value) {
		this.isVisible = Value;
	}
	
	public Boolean getVisible() {
		return this.isVisible ;
	}
	
	/**
	 * @return  true if node has Policies, false otherwise
	 */
	public Boolean getHasPolicies(){
		return (this.getPolicies().size()>0? true:false);
	}
	
	/**
	 * @return true if node has Events, false otherwise
	 */
	public Boolean getHasEvents(){
		return (this.getEvents().size()>0? true:false);
	}
	
		
	public VisualNode clone() {
		VisualNode node = new VisualNode();
		node.setName(this.getName());
		node.setType(this.getType());
//		Point2D newLoc = new Point2D.Double(this.location.getX(),this.location.getY());
//		node.setLocation(newLoc);
//		newLoc = new Point2D.Double(this.lastChildLocation.getX(),this.lastChildLocation.getY());
//		node.setLastChildLocation(newLoc.getX(),newLoc.getY());
		node.setSQLDefinition(this.getSQLDefinition());
		node.setEvents(this.getEvents());
		node.setPolicies(this.getPolicies());
		node.setVisible(this.getVisible());
		node.setStatus(this.getStatus());
		return node;
	}
	
	
}
