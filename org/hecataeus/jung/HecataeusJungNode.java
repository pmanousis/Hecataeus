/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus.jung;

import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import java.awt.geom.Point2D;

import org.hecataeus.evolution.HecataeusEvolutionNode;
import org.hecataeus.evolution.HecataeusNodeType;

/**
 * @author  FOTINI, gpapas
 */
public class HecataeusJungNode extends DirectedSparseVertex{

	
	private Point2D location;
	private Point2D lastChildLocation;
	private Boolean isVisible  = true;
	
	//the corresponding HecataeusEvolutionNode object
	private HecataeusEvolutionNode hecataeusEvolutionNode = null;
	
	public HecataeusJungNode(){
		this.hecataeusEvolutionNode = new HecataeusEvolutionNode();
		this.location = new Point2D.Double();
		this.lastChildLocation = new Point2D.Double();
	}
	
	public HecataeusJungNode(String name, HecataeusNodeType type) {
		this.hecataeusEvolutionNode = new HecataeusEvolutionNode(name, type);
		this.location = new Point2D.Double();
		this.lastChildLocation = new Point2D.Double();
	}
	
	public HecataeusJungNode(HecataeusEvolutionNode hecEvNode) {
		this.hecataeusEvolutionNode = hecEvNode;
		this.location = new Point2D.Double();
		this.lastChildLocation = new Point2D.Double();
	}
	
	/**
	 * @return  the hecataeusEvolutionNode
	 * @uml.property  name="hecataeusEvolutionNode"
	 */
	public HecataeusEvolutionNode getHecataeusEvolutionNode() {
		return hecataeusEvolutionNode;
	}
	
	public void setLocation(Double x,Double y){
		this.location.setLocation(x, y);
	}
	
	/**
	 * @return  the location
	 * @uml.property  name="location"
	 */
	public Point2D getLocation(){
		Double x = this.location.getX();
		Double y = this.location.getY();
		return new Point2D.Double(x,y);
	}
	
	public void setLastChildLocation(Double x,Double y){
		this.lastChildLocation.setLocation(x, y);
	}
	
	/**
	 * @return  the location
	 * @uml.property  name="location"
	 */
	public Point2D getLastChildLocation(){
		if (!(this.lastChildLocation==null)){
			return new Point2D.Double(this.lastChildLocation.getX(),this.lastChildLocation.getY());
		}else return null;
	}
	
	//these are used only to paint right the nodes
	/**
	 * @return  the hasPolicies
	 * @uml.property  name="hasPolicies"
	 */
	public Boolean getHasPolicies(){
		return (this.hecataeusEvolutionNode.getPolicies().size()>0? true:false);
	}
	
	/**
	 * @return  the hasEvents
	 * @uml.property  name="hasEvents"
	 */
	public Boolean getHasEvents(){
		return (this.hecataeusEvolutionNode.getEvents().size()>0? true:false);
	}
	
	public String getName() {
		return hecataeusEvolutionNode.getName();
	}

	public void setName(String Value) {
		this.hecataeusEvolutionNode.setName(Value);
	}

	public HecataeusNodeType getType() {
		return hecataeusEvolutionNode.getType();
	}

	public void setType(HecataeusNodeType Value) {
		this.hecataeusEvolutionNode.setType(Value);
	}

	public String getKey() {
		return hecataeusEvolutionNode.getKey();
	}

	public void setKey(String Value) {
		hecataeusEvolutionNode.setKey(Value);
	}

	public void setVisible(Boolean Value) {
		this.isVisible = Value;
	}
	
	public Boolean getVisible() {
		return this.isVisible ;
	}
}
