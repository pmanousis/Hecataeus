/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.evolution;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import javax.print.attribute.standard.NumberOfInterveningJobs;
import javax.swing.JPanel;

import javax.swing.JOptionPane;

import edu.ntua.dblab.hecataeus.graph.evolution.messages.Message;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.MessageCompare;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.ModuleNode;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.ModuleMaestro;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.ModuleMaestroRewrite;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.StopWatch;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.Pair;

public class EvolutionGraph<V extends EvolutionNode<E>,E extends EvolutionEdge> extends DirectedSparseGraph<V, E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static int _KeyGenerator;
	
	protected Map<V, Integer> nodeKeys;
	protected Map<E, Integer> edgeKeys;

	//used by function initializeChange() to increase the SID(Session ID) by one
	static int SIDGenerator = 0;

	public EvolutionGraph() {
		nodeKeys = new HashMap<V, Integer>();
		edgeKeys = new HashMap<E, Integer>();
	}
		
	/**
	 * adds a new EvolutionNode
	 *  
	 **/
	public boolean addVertex(V Node) {
		// assign key
		nodeKeys.put(Node, ++EvolutionGraph._KeyGenerator);
		
		return super.addVertex(Node);
	}

	public boolean addVertexEVA(VisualGraph g) {
		Hypergraph hg = g;
		hg.addVertex(g);
		return true;
	}
		
	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(E Edge, V fromNode, V toNode) {
		return this.addEdge(Edge);		
	}
	
	/**
	 * adds edge by HecataeusEdge
	 **/
	public boolean addEdge(E Edge) {
		edgeKeys.put(Edge, ++EvolutionGraph._KeyGenerator);
		// add edge to incoming edges of ToNode
		V fromNode = (V) Edge.getFromNode();
		if (!fromNode.getOutEdges().contains(Edge))
			fromNode.getOutEdges().add(Edge);
		// add edge to outgoing edges of FromNode
		V toNode = (V) Edge.getToNode();
		if (!toNode.getInEdges().contains(Edge))
			toNode.getInEdges().add(Edge);
		return super.addEdge(Edge, fromNode, toNode);
	}

	public boolean removeEdge(E Edge) {
		// remove edge from inEdges
		Edge.getToNode().getInEdges().remove(Edge);
		// remove edge from outEdges
		Edge.getFromNode().getOutEdges().remove(Edge);
		edgeKeys.remove(Edge);
		return super.removeEdge(Edge);
	}
	
	public boolean removeVertex(V Vertex) {
		Vertex.getInEdges().clear();
		Vertex.getOutEdges().clear();
		nodeKeys.remove(Vertex);
		return super.removeVertex(Vertex);
	}

	/**
	 *  clears all nodes and edges from the graph
	 **/
	public void clear() {
			
		edgeKeys.clear();
		for (E e:this.getEdges()) {
    		this.removeEdge(e);
    	}
		nodeKeys.clear();
		for (V v:this.getVertices()) {
    		this.removeVertex(v);
    	}
		
	
	}
	
	/**
	 * get Vertex by Key
	 **/
	public V findVertex(int key) {
		for (V v : this.getVertices()) {
			if (nodeKeys.get(v)==key) 
			 return v;
		 }
		 return null;
	}

	/**
	 * get Key of a Vertex
	 **/
	public Integer getKey(V node) {
		return nodeKeys.get(node);
	}

	/**
	 * set Key of a Vertex
	 **/
	public void setKey(V node, Integer key) {
		nodeKeys.remove(node);
		nodeKeys.put(node,key);
	}

	
	/**
	 *  get node by its name, for more than one occurrences, the first is returned
	 **/
	public V findVertexByName(String name) {
		for (V u: this.getVertices()) {
			if (u.getName().toUpperCase().equals(name.toUpperCase())) {
				return u;
			}
		}
		
		return null;
	}
	
/**
 *  get node by its name, after finding his parent
 *  OTHERWISE return node
 **/
public V findVertexByNameParent(String name) {
	String parent="";
	String node="";
	if(name.contains("."))
	{
		parent=name.substring(0, name.indexOf("."));
		node=name.substring(name.indexOf(".")+1);
		for (V u: this.getVertices()) {
			if (u.getName().toUpperCase().equals(parent.toUpperCase())) {
				for(int i=0;i<u.getOutEdges().size();i++)
				{
					if(u.getOutEdges().get(i).getToNode().getName().equals(node.toUpperCase()))
					{
						return (V) (u.getOutEdges().get(i).getToNode());
					}
				}
			}
		}
	}
	else
	{
		return(findVertexByName(name));
	}
	return null;
}
	
	/**
	 * @author pmanousi
	 * @param iD the number of id we are searching for
	 * @return node with ID=id
	 */
	public V findVertexById(double iD)
	{
		for (V u: this.getVertices(NodeCategory.MODULE)) {
			if (u.ID==iD)
			{
				return u;
			}
		}
		return null;
	}
	
	/**
	 * get edge by key
	 **/
	public E findEdge(int key) {
		for (E e : this.getEdges()) {
			if (edgeKeys.get(e)==key) 
				 return e;
		 }
		 return null;	
	}

	/**
	 * get Key of an edge
	 **/
	public Integer getKey(E edge) {
		return edgeKeys.get(edge);
	}

	/**
	 * set Key of an edge
	 **/
	public void setKey(E edge, Integer key) {
		edgeKeys.remove(edge);
		edgeKeys.put(edge,key);
	}
	
	/**
	 * get edge by name and type
	 **/
	public E findEdge(String Name, EdgeType Type) {
		for (E u: this.getEdges()) {
			if ((u.getName().toUpperCase().equals(Name.toUpperCase()) && u.getType() == Type ) ) {
				return u;
			}
		}
		return null;
	}

	/**
	 * used for finding explicitly an attribute by its name and its parent relation, view or query node
	 **/
	public V getAttributeNode(String TableName, String AttributeName) {
		for (V u: this.getVertices()) {
			if (u.getName().toUpperCase().equals(TableName)
/**
 * @author pmanousi
 * Changed it to work with INOUTSCHEMA
 */
&&((u.getType().getCategory() == NodeCategory.INOUTSCHEMA)||(u.getType().getCategory() == NodeCategory.MODULE))){
				for (E e :  this.getOutEdges(u)) {
					if ( this.getDest(e).getName().toUpperCase().equals(AttributeName.toUpperCase()) ) {
						return this.getDest(e);
					}
				}
			}
		}
		
		return null;
	}


	/**
	 * calculates the path of nodes that are connected via in edges with a node
	 **/
	public void getInTree(V node, List<V> InNodes) {
		// add itself

		if ( !(InNodes.contains(node)) ) {
			InNodes.add(node);
		}		
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (E e: this.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(InNodes.contains(this.getSource(e))) ) {
				InNodes.add(this.getSource(e)) ;
				// call recursively for each adjacent node
				getInTree(this.getSource(e), InNodes);
			}
		}
	}


	/**
	 * calculates the path of nodes that are connected via out edges with a node
	 **/
	private void getOutTree(V node, List<V> OutNodes) {
		// for each incoming edge add adjacent node to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (E e: this.getOutEdges(node)) {
			// count only once if multiple paths found
			if ( !(OutNodes.contains(this.getDest(e))) ) {
				OutNodes.add(this.getDest(e));
				// call recursively for each adjacent node
				this.getOutTree(this.getDest(e), OutNodes);
			}
		}
		// add itself
		if ( !(OutNodes.contains(node)) ) {
			OutNodes.add(node);
		}
	}

	/**
	 * calculates the path of incoming edges that are connected with a node
	 **/
	private void getInPath(V node, List<E> InEdges) {
		// for each node edge add in edge to collection
		// only adjacent nodes connected via a directed edge
		// TOWARDS the affecting node are affected
		for (E e : this.getInEdges(node)) {
			// count only once if multiple paths found
			if (!(InEdges.contains(e))) {
				InEdges.add(e);
				// call recursively for each adjacent node
				getInPath(this.getSource(e), InEdges);
			}
		}
	}


	/**
	 * calculates the path of outgoing edges that are connected with a node
	 **/
	private void getOutPath(V node, List<E> OutEdges) {
		// for each node edge add out edge to collection
		// only adjacent nodes connected via a directed edge
		// FROM the affecting node are affected
		for (E e : this.getInEdges(node)) {
			// count only once if multiple paths found
			if ( !(OutEdges.contains(e)) ) {
				OutEdges.add(e);
				// call recursively for each adjacent node
				getOutPath(this.getDest(e), OutEdges);
			}
		}
	}

	/**
	 *  returns the set of nodes affected by an event forced on the affecting_node
	 **/
	public List<V> getAffectedNodes(V affecting_node, EventType eventType) {
		List<V> AffectedNodes = new ArrayList<V>();
		getInTree(affecting_node, AffectedNodes);
		return AffectedNodes;
	}

	/**
	 *  returns the set of incoming edges affected by an event forced on the affecting_node
	 **/
	public List<E> getAffectedEdges(V affecting_node, EventType eventType) {
		List<E> AffectedEdges = new ArrayList<E>();
		getInPath(affecting_node, AffectedEdges);
		return AffectedEdges;
	}

	/**
	 *  makes the necessary initializations to execute propagateChanges()
	 **/
	public void initializeChange(EvolutionEvent<V> event){

for(Entry<V, Pair<Map<V, E>>> entry : this.vertices.entrySet())
{	// Clear statuses of nodes.
	entry.getKey().setStatus(StatusType.NO_STATUS,true);
}
for(Entry<E, Pair<V>> entry : this.edges.entrySet())
{	// Clear statuses of edges.
	entry.getKey().setStatus(StatusType.NO_STATUS,true);
}

V node= event.getEventNode();
V toNode = null;
V toSchema = null;
String parameter="";
switch(node.getType())
{
case NODE_TYPE_RELATION:
case NODE_TYPE_QUERY:
case NODE_TYPE_VIEW:
	toNode=node;
	for(int i=0;i<node.getOutEdges().size();i++)
	{
		toSchema= (V) node.getOutEdges().get(i).getToNode();
		if(toSchema.getType()==NodeType.NODE_TYPE_OUTPUT)
		{
			parameter=node.getName();
			break;
		}
	}
	break;

case NODE_TYPE_OUTPUT:
case NODE_TYPE_SEMANTICS:
	toSchema=node;
	for(int i=0;i<node.getInEdges().size();i++)
	{
		if(node.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT||node.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
		{
			toNode=(V) node.getInEdges().get(i).getFromNode();
			break;
		}
		parameter=node.getName();
		if(event.getEventType()==EventType.ADD_ATTRIBUTE)
		{
			parameter="";
		}
		
	}
	break;

default:
	for(int i=0;i<node.getInEdges().size();i++)
	{
		if(node.getInEdges().get(i).getFromNode().getType()==NodeType.NODE_TYPE_OUTPUT)
		{
			toSchema=(V) node.getInEdges().get(i).getFromNode();
			for(int j=0;j<toSchema.getInEdges().size();j++)
			{
				if(toSchema.getInEdges().get(j).getFromNode().getType()==NodeType.NODE_TYPE_RELATION||toSchema.getInEdges().get(j).getFromNode().getType()==NodeType.NODE_TYPE_QUERY||toSchema.getInEdges().get(j).getFromNode().getType()==NodeType.NODE_TYPE_VIEW)
				{
					toNode=(V) toSchema.getInEdges().get(j).getFromNode();
				}
			}
			parameter=node.getName();
			if(event.getEventType()==EventType.DELETE_SELF)
			{
				event.setEventType(EventType.DELETE_ATTRIBUTE);
			}
			else if(event.getEventType()==EventType.RENAME_SELF)
			{
				event.setEventType(EventType.RENAME_ATTRIBUTE);
			}
			break;
		}
	}
	if(toSchema==null)
	{
		while(toNode==null)
		{
			toSchema=(V) node.getInEdges().get(0).getFromNode();
			if(toSchema.getType()==NodeType.NODE_TYPE_SEMANTICS)
			{
				event.setEventType(EventType.ALTER_SEMANTICS);
				toNode=(V) toSchema.getInEdges().get(0).getFromNode();
			}
		}
	}
	break;
}
Message<V,E> firstMessage=new Message<V,E>(toNode,toSchema,event.getEventType(),parameter);
propagateChanges(firstMessage);
	}


	
	/**
	 *  sets the status of the parts of the graph affected by an event
	 **/
/** @author pmanousi private void propagateChanges(Queue queue, List<V> nodesVisited) */
@SuppressWarnings("unused")
private void propagateChanges(Message<V,E> message)
{
V arxikoModule=message.toNode;
EvolutionGraph<V, E> ograph = new EvolutionGraph<V,E>();
ograph.vertices.putAll(this.vertices);
int modulesAffected=0;
int internalsAffected=0;
int numberOfModules=0;
int numberOfNodes=0;

	PriorityQueue<Message<V,E>> queue= new PriorityQueue<Message<V,E>>(1, new MessageCompare());
	queue.add(message);
	List<ModuleNode<V,E>> epireasmenoi=new LinkedList<ModuleNode<V,E>>();
/**
 * @author pmanousi
 * For time count of step 1.
 */
StopWatch step1 = new StopWatch();
step1.start();
	while (!queue.isEmpty())
	{
		try
		{
			ModuleMaestro<V,E> maestro = new ModuleMaestro<V,E>(queue);
			PriorityQueue<Message<V, E>> mins=new PriorityQueue<Message<V,E>>();
			mins.add(maestro.arxikoMinima.clone());
			Iterator<Message<V,E>> i=maestro.myQueue.iterator();
			while(i.hasNext())
			{
				Message<V,E> tmpPMMsg=i.next();
				if(mins.contains(tmpPMMsg)==false)
				{
					mins.add(tmpPMMsg.clone());
				}
			}
			epireasmenoi.add(new ModuleNode(maestro.arxikoMinima.toNode,mins, message.event));
			maestro.propagateMessages();	// Status determination
		}
		catch(Exception e)
		{
		}
	}
/**
 * @author pmanousi
 * For time count of step 1.
 */
step1.stop();
/*
 * counting nodes with status! 
 */
for(Entry<V, Pair<Map<V, E>>> entry : this.vertices.entrySet())
{
	String name=entry.getKey().getName();
	if(entry.getKey().getStatus() != StatusType.NO_STATUS && name.contains(" AND ")==false&&name.contains(" OR ")==false)
	{
		if(entry.getKey().getType().getCategory()==NodeCategory.MODULE&&entry.getKey()!=arxikoModule)
		{
			modulesAffected++;
		}
		else
		{
			if(entry.getKey().getType().getCategory()!=NodeCategory.INOUTSCHEMA||entry.getKey().getType().getCategory()!=NodeCategory.SEMANTICS)
			{
				internalsAffected++;
			}
		}
	}
}
numberOfModules=this.getVertices(NodeType.NODE_TYPE_QUERY).size()+this.getVertices(NodeType.NODE_TYPE_VIEW).size();
numberOfNodes=this.getVertexCount();
int relNodes=0;
List<V> rel=this.getVertices(NodeType.NODE_TYPE_RELATION);
for(int i=0;i<rel.size();i++)
{
	relNodes+=2;
	V relationProsElegxo=rel.get(i);
	V schemaProsElegxo=(V) rel.get(i).getOutEdges().get(0).getToNode();
	relNodes+=schemaProsElegxo.getOutEdges().size();
}



	for(int k=0;k<epireasmenoi.size();k++)
	{
		epireasmenoi.get(k).setEmeis(epireasmenoi);
	}
	// Check graph for block status
	StatusType st=StatusType.PROPAGATE;
	Iterator<ModuleNode<V,E>> i=epireasmenoi.iterator();
/**
 * @author pmanousi
 * For time count of step 2.
 */
StopWatch step2 = new StopWatch();
step2.start();
	while (i.hasNext())
	{
		ModuleNode<V, E> prosElegxo=i.next();
		if(prosElegxo.getStatus()==StatusType.BLOCKED)
		{
			st=StatusType.BLOCKED;
			prosElegxo.backPropagation();
			prosElegxo.neededRewrites=0;
		}
	}
/**
 * @author pmanousi
 * For time count of step 2.
 */
step2.stop();
/**
 * @author pmanousi
 * For time count of step 3.
 */

MetriseisRewrite mr=new MetriseisRewrite();
int clonedModules=0;
int rewrittenModules=0;
StopWatch step3 = new StopWatch();
step3.start();

	if(st==StatusType.BLOCKED)
	{
		if(message.toNode.getType()==NodeType.NODE_TYPE_RELATION && message.event!=EventType.ADD_ATTRIBUTE)
		{	// Whatever happens to relation stops there!
			rewrittenModules=0;
		}
		else
		{
			i=epireasmenoi.iterator();
			String tempParam="";
			while (i.hasNext())
			{
				ModuleNode<V, E> prosEpaneggrafi=i.next();
				if(prosEpaneggrafi.neededRewrites==1)
				{	// They move to new version.
					ModuleMaestroRewrite<V, E> m=new ModuleMaestroRewrite<V, E>(prosEpaneggrafi.messages);
					m.moveToNewInputsIfExist(this, prosEpaneggrafi.en);
					tempParam=m.doRewrite(tempParam, this, step3,mr);
					rewrittenModules++;
				}
				if(prosEpaneggrafi.neededRewrites==2)
				{	// They copy themselves and do rewrite on new version.
					V neos=prosEpaneggrafi.cloneQVModule(this);
					clonedModules++;
					Iterator<Message<V, E>> j = prosEpaneggrafi.messages.iterator();
					while(j.hasNext())
					{	// messages are for neos node...
						Message<V, E> n=j.next();
						for(int k=0;k<neos.getOutEdges().size();k++)
						{
							if(neos.getOutEdges().get(k).getToNode().getName().equals(n.toSchema.getName().replace(n.toNode.getName(), neos.getName())))
							{
								n.toSchema=(V) neos.getOutEdges().get(k).getToNode();
							}
						}
						n.toNode=neos;
					}
					prosEpaneggrafi.en=neos;
					ModuleMaestroRewrite<V, E> m=new ModuleMaestroRewrite<V, E>(prosEpaneggrafi.messages);
					m.moveToNewInputsIfExist(this, prosEpaneggrafi.en);
					tempParam=m.doRewrite(tempParam, this, step3,mr);
					rewrittenModules++;
				}
			}
		}
	}
	else
	{
		String newParameter=new String();
		for(int j=0;j<epireasmenoi.size();j++)
		{
			ModuleMaestroRewrite<V,E> rewriter=new ModuleMaestroRewrite<V,E>(epireasmenoi.get(j).messages);
			newParameter=rewriter.doRewrite(newParameter, this, step3,mr);	// Rewrite
			rewrittenModules++;
		}
	}
/**
 * @author pmanousi
 * For time count of step 3.
 */
step3.stop();



try
{
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("time.csv", true)));
    out.println(message.event.toString()+": "+message.toSchema.getName()+"."+message.parameter+","+modulesAffected+","+numberOfModules+","+internalsAffected+","+numberOfNodes+","+rewrittenModules+","+clonedModules);
    out.close();
} catch (IOException e)
{}
}


	/**
	 * used for finding the policy of a node
	 * @param1 = event,
	 * @param2 = Node receiving the message
	 * @param3 = The previous prevailing policy
	 **/
	private PolicyType determinePolicy(EvolutionEvent<V> event, V nr, E edge, PolicyType previousPolicyType) {

		//  policy hierarchy
		// 1. query.condition, 2. query.attribute, 3. query, 4. relation.condition, 5. relation.attribute, 6. relation
		//  search for policy towards the path 

		System.out.println("determining policy for : " + nr.getName());
		//if edge is part of and child node has policy
		//then override parent's policy 
		if (edge!=null && edge.isPartOf()){
			return previousPolicyType;
		}

		//if edge is dependency then get prevailing policy
		return getPrevailingPolicy(event,nr,previousPolicyType);

	}

	/*
	 * gets the prevailing policy in a module (e.g. a query, relation, view)
	 */
	private PolicyType getPrevailingPolicy(EvolutionEvent<V> event, V nr, PolicyType previousPolicyType) {

		EvolutionPolicies policies = nr.getPolicies();
		//if policy for this event exist override previousPolicy coming from provider node
		for (EvolutionPolicy<V> nrPolicy : policies){
			if ((nrPolicy.getSourceEvent().getEventNode().equals(event.getEventNode()))
					&&(nrPolicy.getSourceEvent().getEventType()==event.getEventType())){
				return nrPolicy.getPolicyType();
			}
		}

		//If no policy is returned check parents' policy for this event to override provider's policy
		if (this.getParentNode(nr)!=null) {
			EvolutionEvent<V> newEvent = new EvolutionEvent<V>(/*nr,*/event.getEventType());
			return getPrevailingPolicy(newEvent,this.getParentNode(nr),previousPolicyType);
		}

		//if no self or parents policy exists then return provider's policy
		return previousPolicyType;	
	}

	/**
	 * used for finding the providerEdges of a node (through from, map-select, operand edges, gb edges)
	 **/
	public List<E> getProviderEdges(V node) {
	
		List<E> providerEdges= new ArrayList<E>();

		for (E e: this.getInEdges(node)){
			if ((e.getType()==EdgeType.toEdgeType("EDGE_TYPE_MAPPING"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_FROM"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_GROUP_BY"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_OPERATOR"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_ALIAS"))
					||(e.getType()==EdgeType.toEdgeType("EDGE_TYPE_USES"))){
				providerEdges.add(e);
			}
		}
		return providerEdges;	
	}
	/**
	 * used for getting the subgraph of a module (query, relation, view)
	 **/
	public List<V> getModule(V parentNode) {
		List<V> subGraph = new ArrayList<V>();
		subGraph.add(parentNode);
		return this.subGraph(parentNode, subGraph);

	}


	/**
	 * used for getting the subgraph of a parent node (query, relation, view)
	 **/
	private List<V> subGraph(V node, List<V> subGraph) {

		for (E e : this.getOutEdges(node)){
			//if edge is intramodule then add tonode
			if (e.isPartOf()) {
				if ( !(subGraph.contains(this.getDest(e)))) {
					subGraph.add(this.getDest(e));
				}
				// call recursively for each adjacent node
				this.subGraph(this.getDest(e), subGraph);
			}
		}
		return subGraph;

	}
		
	/**
	 * propagates the frequency of a query node towards the graph
	 * set the frequency of all provider nodes of a query
	 * @param node
	 */
	public void propagateFrequency(V node) {
		
		List<V> subGraph =this.getModule(node); 
		for (V evNode : subGraph) {
			evNode.setFrequency(node.getFrequency());
		}
		for (E edge : this.getOutEdges(node)) {
			if (edge.isProvider()) {
				edge.getToNode().setFrequency(edge.getToNode().getFrequency()+node.getFrequency());
				this.propagateFrequency(this.getDest(edge));
			}
		}
	}
	/**
	 * used for finding the top_level node of each module, given an containing node
	 * @param node
	 * @return
	 */
	public V getTopLevelNode(V node) {
		if (node.getType().getCategory()==NodeCategory.MODULE)
			return node;
		for (E e : this.getInEdges(node)){
			if (e.isPartOf()){
				return getTopLevelNode(this.getSource(e));
			}
		}
		return null;
	}
	
	 /**
	  * get two set of Nodes (Modules) and return true if they are connected 
	  * @param fromModule
	  * @param toModule
	  * @return
	  */
	 public  boolean isConnected(List<V> fromModule, List<V> toModule) {
		 for (V node : fromModule) {
			 for (E edge : this.getOutEdges(node)) {
				 if (edge.isProvider()&&toModule.contains(edge.getToNode())) 
					 return true;
			 }
		 }
		return false;
	 }
	 /**
	  * calculates the number of connections between two modules (subGraphs) as
	  * the number of all dependency edges between these modules
	  * directing from the fromModule towards the toModule
	  * @param fromModule is the parent node of outgoing edges module
	  * @param toModule is the parent node of incoming edges module
	  * @return strength
	  */
	 public int getConnections(List<V> fromModule, List<V> toModule) {
		 List<EvolutionEdge> connections = new ArrayList<EvolutionEdge>();
		 for ( V node : fromModule) {
			 for (E edge : node.getOutEdges()) {
				 if (edge.isProvider()
						 &&toModule.contains(edge.getToNode())
						 &&(!connections.contains(edge))) {
					 connections.add(edge);
				 }
			 }
		 }
		return connections.size();
	 }
	 /**
	  * get two  Nodes and return the number of paths between them
	  * @param fromNode: starting node 
	  * @param toNode : ending node
	  * @return True if there is a path from fromNode towards toNode, False otherwise
	  */
	 public int getPaths(V fromNode, V toNode) {
		 int paths=0;
		 for ( E edge : this.getOutEdges(fromNode)) {
			 if(edge.getToNode().equals(toNode)) {
				 paths++;
			 }
			paths += this.getPaths(this.getDest(edge), toNode);
		 }
		 return paths;
	 }

	 /**
	  * @return
	  * all nodes of the graph of specific type category NodeCategory
	  */
	 public List<V> getVertices(NodeCategory category) {
		 List<V> nodes =  new ArrayList<V>();
		 for (V node: this.getVertices()) {
			 if (node.getType().getCategory()== category) {
				 nodes.add(node);
			 }
		}
		return nodes;
	 }
	 
	 /**
	  * @return
	  * all nodes of the graph of specific type NodeType
	  */
	 public List<V> getVertices(NodeType type) {
		 List<V> nodes =  new ArrayList<V>();
		 for (V node: this.getVertices()) {
			 if (node.getType()== type) {
				 nodes.add(node);
			 }
		}
		return nodes;
	 }

	 /***
	  * 
	  * @return
	  * all edges of the graph of specific type EdgeType
	  */
	 public List<E> getEdges(EdgeType type) {
		 List<E> edges = new ArrayList<E>();
		 for (E edge: this.getEdges()) {
			 if (edge.getType()== type) {
				 edges.add(edge);
			 }
		}
		return edges;

	 }
		
	 /**
	  * used for finding the parent of a node (query, view, relation)
	  **/
	 public V getParentNode(V node) {
		 for (E e: this.getInEdges(node)){
			 //if node is attribute then 
			 if (((node.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
					 && (e.getType()==EdgeType.EDGE_TYPE_SCHEMA))
					 ||((node.getType()==NodeType.NODE_TYPE_CONDITION)
							 && (e.getType()==EdgeType.EDGE_TYPE_OPERATOR))		
							 ||((node.getType()==NodeType.NODE_TYPE_OPERAND)
									 && ((e.getType()==EdgeType.EDGE_TYPE_OPERATOR)
											 ||(e.getType()==EdgeType.EDGE_TYPE_WHERE)))		
											 ||(node.getType()==NodeType.NODE_TYPE_CONSTANT)		
											 ||((node.getType()==NodeType.NODE_TYPE_GROUP_BY)
													 && (e.getType()==EdgeType.EDGE_TYPE_GROUP_BY))		
													 ||(node.getType()==NodeType.NODE_TYPE_FUNCTION)
			 )
				 return this.getSource(e);
		 }
		 return null;
	 }
		

	/**
	 * sets the keyGenerator to zero, to start counting the elements from the beginning
	 */
	public void resetKeyGenerator() {
		EvolutionGraph._KeyGenerator = 0;
	}

	/**
	 * returns the value of the keyGenerator, that is the number of elements the graph has
	 */
	public int getKeyGenerator() {
		return EvolutionGraph._KeyGenerator;
	}
	/**
	 * sets explicitly the value of the keyGenerator, that is the number of elements the graph has
	 */
	public void setKeyGenerator(int value) {
		EvolutionGraph._KeyGenerator = value;
	}

	//collection of string for holding default policy definitions
	private ArrayList<String> _defaultPolicyClauses = new ArrayList<String>(); 
	public ArrayList<String> getDefaultPolicyDecsriptions(){
		return this._defaultPolicyClauses;
	}
	
	/***
	 * Creates a new graph object containing the nodes of the argument
	 * It does not clone the nodes / edges. It just creates a new graph
	 * and adds them to the collection of graph's nodes and edges 
	 * @return a graph object
	 */
	@SuppressWarnings("unchecked")
	public <G extends EvolutionGraph<V,E>> G toGraphE(List<V> nodes){
		G subGraph;
		try {
			subGraph = (G) this.getClass().newInstance();
			for(V vertex : nodes) {
				subGraph.addVertex(vertex);
				Collection<E> incidentEdges = this.getIncidentEdges(vertex);
				for(E edge : incidentEdges) {
					Pair<V> endpoints = this.getEndpoints(edge);
					if(nodes.containsAll(endpoints)) {
						// put this edge into the subgraph
						subGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
					}
				}
			}
			return subGraph;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void exportPoliciesToFile(File file) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (V v : this.getVertices()){
				for (EvolutionPolicy<V> p : v.getPolicies()) {
					out.write(v + ": " + p.toString() + ";");
					out.newLine();
				}	
			}
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
