/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;

/**
 * The class implements a visual node of the graph
 * It holds the visual properties of the node
 * 
 * @author  gpapas
 */
public class VisualNode extends EvolutionNode<VisualEdge>{

	private double angle;
	private Point2D location;
	private Boolean isVisible  = true;
	private double nodeSize; // used only for node type cluster
	private Color nodeColor;
	private String label; //used only for node type cluster
	public int size;

	public VisualNode(){
		super();
		this.location = new Point2D.Double();
		this.angle = 0.0;
		this.nodeColor = new Color(255,255,255);
		this.nodeSize = 0.0;
		this.label = "";
	}
	
	public void setLocation(Point2D loc){
		this.location = loc;
	}
	
	public Point2D getLocation(){
		return this.location;
	}
	
	public void setNodeColor(Color c){
		this.nodeColor = c;
	}
	
	public Color getNodeColor(VisualNode v){
		return this.nodeColor;
	}
	
	public void setNodeAngle(double a){
		this.angle = a;
	}
	public double getNodeAngle(){
		return this.angle;
	}
	
	public void setNodeSize(double size){
		this.nodeSize = size;
	}
	
	public double getNodeSize(){
		return this.nodeSize;
	}
	
	public void setNodeLabel(String label){
		this.label = label;
	}
	
	public String getNodeLabel(){
		return this.label;
	}
	
	public void setFile(File fName){
		super.setFile(fName);
	}
	
	public File getFile(){
		return(super.getFile());
	}
	
	public String getFileName(){
		return (super.getFileName());
	}
	
	public VisualNode(String name, NodeType type, File fName) {
		super(name, type, fName);
		this.location = new Point2D.Double();
		this.angle = 0.0;
		this.nodeColor = new Color(255,255,255);
		this.nodeSize = 0.0;
		this.label = "";
	}

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
		node.setSQLDefinition(this.getSQLDefinition());
		node.setEvents(this.getEvents());
		node.setPolicies(this.getPolicies());
		node.setVisible(this.getVisible());
		node.setStatus(this.getStatus(),true);
		node._inEdges=this.getInEdges();
		node._outEdges= this.getOutEdges();
		node.setNodeAngle(this.angle);
		node.setNodeColor(this.nodeColor);
		node.setNodeSize(this.nodeSize);
		node.setLocation(this.getLocation());
		node.setFile(this.getFile());
		return node;
	}
}
