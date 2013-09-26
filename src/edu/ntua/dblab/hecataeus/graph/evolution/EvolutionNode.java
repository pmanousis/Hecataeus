/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.util.ArrayList;
import java.util.List;

import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

/**
 * @author  George Papastefanatos
 */
public class EvolutionNode<E extends EvolutionEdge>{
/** @author pmanousi To be used in topological sorting for messages. */
public double ID=0.0;
static int counter;

	private String _Name = null;
	private NodeType _Type ;
	private int _frequency = 0;
	
	private EvolutionPolicies  _policies = null;
	private EvolutionEvents _events = null;
	public List<E> _outEdges = null;
	public List<E> _inEdges = null;
	private StatusType _status = StatusType.NO_STATUS;

	public EvolutionNode() {
		// just create the node and set afterwards its properties
		this._outEdges = new ArrayList<E>();
		this._inEdges = new ArrayList<E>();
		this._policies = new EvolutionPolicies();
		this._events = new EvolutionEvents();
		counter++;
		ID=counter;
	}

	public EvolutionNode(String Name, NodeType Type) {
		this._Name= Name;
		this._Type = Type;
		this._outEdges = new ArrayList<E>();
		this._inEdges = new ArrayList<E>();
		this._policies = new EvolutionPolicies();
		this._events = new EvolutionEvents();
		counter++;
		ID=counter;
	}

	/**
	 * Returns the name of the node
	 */
	public String getName() {
		return this._Name;
	}

	/**
	 * Sets the name of the node
	 */
	public void setName(String Value) {
		this._Name = Value;
	}

	/**
	 * Returns the type of the node
	 */
	public NodeType getType() {
		return this._Type;
	}

	/**
	 * Sets the type of the node
	 */
	public void setType(NodeType Value) {
		this._Type = Value;
	}

		
	/**
	 * Returns the frequency of the node
	 */
	public int getFrequency() {
		return this._frequency ;
	}

	/**
	 * Sets the frequency of the node
	 */
	public void setFrequency(int Value) {
		this._frequency  = Value;
	}
	
	public void setPolicies(EvolutionPolicies policies) {
		this._policies = policies;
	}
	
	public EvolutionPolicies getPolicies() {
		return this._policies;
	}
        
	public void setEvents(EvolutionEvents events) {
		this._events = events;
	}
	
	
	public EvolutionEvents getEvents() {
		return this._events;
	}
	
	public void setStatus(StatusType status,boolean cl) {
		if(cl==true)
		{
			this._status = status;
		}
		else
		{
			if(this._status==StatusType.BLOCKED||status==StatusType.NO_STATUS)
			{
				return;
			}
			this._status = status;
		}
	}
	
	public StatusType getStatus() {
		return this._status ;
	}
	
    public List<E> getOutEdges() {
		return  this._outEdges;
	}

	public List<E> getInEdges() {
		return  this._inEdges;
	}
	
	
	/**
	*  creates and adds policy to node
	**/
	public void addPolicy(EventType eventType/*, EvolutionNode child*/, PolicyType policyType) {
		EvolutionPolicies policies = this._policies;
		EvolutionPolicy<EvolutionNode> policy = policies.get(eventType/*, child*/);
		if(policy!=null)
				policies.remove(policy);
		policies.add(new EvolutionPolicy<EvolutionNode>(eventType/*,child*/,policyType));
	}

	/**
	*  adds policy to node, if policy already exists then it replaces it
	**/
	public void addPolicy(EvolutionPolicy p) {
		EvolutionPolicies policies = this._policies;
		EvolutionPolicy policy = policies.get(p.getSourceEvent().getEventType()/*, p.getSourceEvent().getEventNode()*/);
		if(policy!=null)
				policies.remove(policy);
		policies.add(p);
	}
	
	/**
	*  removes policy from node
	**/
	public void removePolicy(EvolutionPolicy policy) {
		this._policies.remove(policy);
	}

	/**
	*  creates and adds a self event to node
	**/
	public void addEvent(EventType eventType) {
	
		EvolutionEvents events = this._events;
		for (int i=0; i<events.size(); i++){
			EvolutionEvent event = events.get(i);
			if ((event.getEventType()==eventType))
				events.remove(event);
		}
		events.add(new EvolutionEvent(/*this,*/ eventType));
	}

	/**
	*  adds event to node
	**/
	public void addEvent(EvolutionEvent e) {
		
		EvolutionEvents events = this._events;
		for (int i=0; i<events.size(); i++){
			EvolutionEvent event = events.get(i);
			if ((event.getEventType()==e.getEventType())&&(event.getEventNode().equals(e.getEventNode())))
				events.remove(event);
		}
		events.add(e);
	}
	/**
	*  removes event from node
	**/
	public void removeEvent(EvolutionEvent event) {
		this._events.remove(event);
	}

	
	private String _SQLDefinition="";
	/***
	 * Gets the sql definition that created the node
	 */
	public String getSQLDefinition() {
		return this._SQLDefinition;
	}
	/***
	 * Sets the sql definition that created the node
	 */
	public void setSQLDefinition(String value) {
		this._SQLDefinition = value;
	}
	
	@Override
	/***
	 * overrides object toString
	 */
	public String toString() {
		return this._Name;
	}
	
	private String _path="";									/*added by sgerag*/
	/***
	 * Gets path of the sql definition that created the node
	 */
	public String getPath() {									/*added by sgerag*/
		return this._path;
	}
	/***
	 * Sets the path of the sql definition that created the node
	 */
	public void setPath(String path) {							/*added by sgerag*/	
		this._path=path;
	}
	
	private int _line=0;										/*added by sgerag*/
	/***
	 * Gets line of the sql definition that created the node
	 */
	public int getLine() {										/*added by sgerag*/
		return this._line;
	}
	/***
	 * Sets the line of the sql definition that created the node
	 */
	public void setLine(int line) {								/*added by sgerag*/	
		this._line=line;
	}

	
}