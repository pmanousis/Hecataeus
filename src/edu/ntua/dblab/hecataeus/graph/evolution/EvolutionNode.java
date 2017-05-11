/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.EvolutionToVisualConverter;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

/**
 * @author George Papastefanatos
 */
public class EvolutionNode {

	private static int COUNTER = 0;

	private double ID = 0.0;

	private int line = 0;

	private int frequency = 0;

	private String name = null;

	private String SQLDefinition = "";

	private String path = "";

	private List<EvolutionEdge> inEdges = null;

	private List<EvolutionEdge> outEdges = null;

	private EvolutionPoliciesManager policies = null;

	private EvolutionEventsManager events = null;

	private StatusType status = StatusType.NO_STATUS;

	private NodeType type;

	private File file = null;

	public EvolutionNode() {
		this.outEdges = new ArrayList<EvolutionEdge>();
		this.inEdges = new ArrayList<EvolutionEdge>();
		this.policies = new EvolutionPoliciesManager();
		this.events = new EvolutionEventsManager();
		COUNTER++;
		ID = COUNTER;
	}

	public EvolutionNode(String Name, NodeType Type, File fName) {
		this();
		this.name = Name;
		this.file = fName;
		this.type = Type;
	}

	public String getFileName() {
		if (this.file != null) {
			return this.file.getAbsolutePath();
		}
		return ("null");
	}

	public void setID(double ID) {
		this.ID = ID;
	}
	
	public double getID() {
		return ID;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File fName) {
		this.file = fName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String Value) {
		this.name = Value;
	}

	public NodeType getType() {
		return this.type;
	}

	public void setType(NodeType Value) {
		this.type = Value;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void setFrequency(int Value) {
		this.frequency = Value;
	}

	public EvolutionPolicy getPolict(EventType eventType) {
		return policies.get(eventType);
	}

	public ArrayList<EvolutionPolicy> getPolicies() {
		return this.policies.getPolicies();
	}

	public ArrayList<EvolutionEvent> getEvents() {
		return this.events.getEvents();
	}

	public void setStatus(StatusType status, boolean enforceStatus) {
		if (enforceStatus == true) {
			this.status = status;
		} else //check if you are alredy BLOCKED, in which case you remain BLOCKED
		{
			if (this.status == StatusType.BLOCKED) {
				return;
			} else if (this.status == StatusType.NO_STATUS) {
				this.status = status;
			} else if ((this.status == StatusType.PROPAGATE) && (status == StatusType.NO_STATUS)) {
				return;
			}
		}
	}

	public StatusType getStatus() {
		return this.status;
	}

	public List<EvolutionEdge> getOutEdges() {
		return this.outEdges;
	}

	public List<EvolutionEdge> getInEdges() {
		return this.inEdges;
	}

	public void addPolicy(EventType eventType, PolicyType policyType) {
		EvolutionPoliciesManager policies = this.policies;
		EvolutionPolicy policy = policies.get(eventType);
		if (policy != null)
			policies.remove(policy);
		if (eventType == EventType.DELETE_PROVIDER || eventType == EventType.RENAME_PROVIDER) {
			if (this.getParentNode() != null) {
				if (this.getParentNode().getType() == NodeType.NODE_TYPE_RELATION) {
					return;
				}
			}
		}
		policies.add(new EvolutionPolicy(eventType, policyType));
	}

	public String getSQLDefinition() {
		return this.SQLDefinition;
	}

	public void setSQLDefinition(String value) {
		this.SQLDefinition = value;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getLine() {
		return this.line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public EvolutionNode getParentNode() {
		for (EvolutionEdge e : this.getInEdges()) {
			//if node is attribute then 
			if (((this.getType() == NodeType.NODE_TYPE_ATTRIBUTE) && (e.getType() == EdgeType.EDGE_TYPE_SCHEMA &&
				e.getFromNode().getType() != NodeType.NODE_TYPE_ATTRIBUTE)) ||
				((this.getType() == NodeType.NODE_TYPE_ATTRIBUTE) && (e.getType() == EdgeType.EDGE_TYPE_OUTPUT &&
					e.getFromNode().getType() == NodeType.NODE_TYPE_OUTPUT)) ||
				((this.getType() == NodeType.NODE_TYPE_CONDITION) && (e.getType() == EdgeType.EDGE_TYPE_OPERATOR)) ||
				((this.getType() == NodeType.NODE_TYPE_OPERAND) &&
					((e.getType() == EdgeType.EDGE_TYPE_OPERATOR) || (e.getType() == EdgeType.EDGE_TYPE_WHERE))) ||
				(this.getType() == NodeType.NODE_TYPE_CONSTANT) ||
				((this.getType() == NodeType.NODE_TYPE_GROUP_BY) && (e.getType() == EdgeType.EDGE_TYPE_GROUP_BY)) ||
				(this.getType() == NodeType.NODE_TYPE_FUNCTION) ||
				(this.getType() == NodeType.NODE_TYPE_INPUT && e.getType().equals(EdgeType.EDGE_TYPE_INPUT)) ||
				(this.getType() == NodeType.NODE_TYPE_ATTRIBUTE && e.getType().equals(EdgeType.EDGE_TYPE_INPUT)) ||
				(this.getType() == NodeType.NODE_TYPE_OUTPUT && e.getType().equals(EdgeType.EDGE_TYPE_OUTPUT)) ||
				(this.getType() == NodeType.NODE_TYPE_SEMANTICS && e.getType().equals(EdgeType.EDGE_TYPE_SEMANTICS)))
				return (EvolutionNode) e.getFromNode();
		}
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof EvolutionNode))
			return false;

		EvolutionNode oth = (EvolutionNode) other;
		if (this.getCounter() == oth.getCounter() && super.equals(oth))
			return true;
		return false;
	};

	/**
	 * Creates a visual node which has this <code>EvolutionNode as parent. This
	 * method is not responsible for adding inEdges and outEdges in the produced
	 * VisualNode. Responsible for this job is {@link EvolutionToVisualConverter}
	 */
	public VisualNode produceVisualNode() {
		return new VisualNode((EvolutionNode) this);
	}

	private int getCounter() {
		return COUNTER;
	}

}