/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

/**
 * The class implements a visual node of the graph It holds the visual properties of
 * the node
 * 
 * @author gpapas
 */
public class VisualNode {

	private final EvolutionNode parentEvolutionNode;
	private double angle;
	private Point2D location;
	private Boolean isVisible = true;
	private List<VisualEdge> outEdges = new ArrayList<>();
	private List<VisualEdge> inEdges = new ArrayList<>();

	public VisualNode(EvolutionNode parent) {
		this.location = new Point2D.Double();
		this.angle = 0.0;
		this.parentEvolutionNode = parent;
		new Color(255, 255, 255);
	}

	public List<VisualEdge> getInEdges() {
		return inEdges;
	}

	public List<VisualEdge> getOutEdges() {
		return outEdges;
	}

	public void setInEdges(List<VisualEdge> inEdges) {
		this.inEdges = inEdges;
	}

	public void setOutEdges(List<VisualEdge> outEdges) {
		this.outEdges = outEdges;
	}

	public void setLocation(Point2D loc) {
		this.location = loc;
	}

	public Point2D getLocation() {
		return this.location;
	}

	public void setNodeAngle(double a) {
		this.angle = a;
	}

	public double getNodeAngle() {
		return this.angle;
	}

	public ArrayList<EvolutionPolicy> getPolicies() {
		return parentEvolutionNode.getPolicies();
	}

	public File getFile() {
		return (parentEvolutionNode.getFile());
	}

	public String getFileName() {
		return (parentEvolutionNode.getFileName());
	}

	public void setVisible(boolean Value) {
		this.isVisible = Value;
	}

	public Boolean getVisible() {

		return this.isVisible;
	}

	public NodeType getType() {
		return parentEvolutionNode.getType();
	}

	public EvolutionNode getParentEvolutionNode() {
		if (parentEvolutionNode == null)
			throw new Error("parentEvolutionNode is null!");
		return parentEvolutionNode;
	}

	public Boolean getHasPolicies() {
		return (parentEvolutionNode.getPolicies().size() > 0
			? true : false);
	}

	public String getName() {
		return parentEvolutionNode.getName();
	}

	public Boolean getHasEvents() {
		return (parentEvolutionNode.getEvents().size() > 0
			? true : false);
	}

	public void setID(double ID) {
		parentEvolutionNode.setID(ID);
	}

	public double getID() {
		return parentEvolutionNode.getID();
	}

	public int getNumberOfUsesEdges() {
		int toReturn = 0;
		for (EvolutionEdge e : parentEvolutionNode.getOutEdges()) {
			if (e.getType() == EdgeType.EDGE_TYPE_USES) {
				toReturn++;
			}
		}
		return (toReturn);
	}

	public StatusType getStatus() {
		return parentEvolutionNode.getStatus();
	}

	public String getSQLDefinition() {
		return parentEvolutionNode.getSQLDefinition();
	}

	public ArrayList<EvolutionEvent> getEvents() {
		return parentEvolutionNode.getEvents();
	}

	public String getPath() {
		return parentEvolutionNode.getPath();
	}

	public int getLine() {
		return parentEvolutionNode.getLine();
	}

	public int getFrequency() {
		return parentEvolutionNode.getFrequency();
	}

	@Override
	public boolean equals(Object other){
		if(!(other instanceof VisualNode))
			return false;
		
		VisualNode oth = (VisualNode)other;
		
		if (this.getParentEvolutionNode().equals(oth.getParentEvolutionNode()) && super.equals(other))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return parentEvolutionNode.toString();
	}

}
