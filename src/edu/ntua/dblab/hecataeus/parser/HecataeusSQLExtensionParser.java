/**
 * 
 */
package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;



import edu.ntua.dblab.hecataeus.*;
import edu.ntua.dblab.hecataeus.graph.evolution.*;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

/**
 * Class for parsing SQL language extensions that assign policies on nodes of a graph.  
 * It parses an input text file with clauses of the form: 
 * <p>
 * <i>Name of Node assigned with Policy</i>: ON <i>Type of Event</i> TO<i> Name of Node raising an Event</i> THEN<i> Type of Policy</i> ;
 * </p>
 * @author George Papastefanatos, National Technical University of Athens
 * 
 *
 */
public final class HecataeusSQLExtensionParser{

	private VisualGraph _graph;
	private File _inputFile;
	
//	private enum DBEvents { 
//		ADDITION, 
//		DELETION, 
//		MODIFICATION;
//
//		static DBEvents toType(String value) {
//			return valueOf(value);
//		}
//	}

	/**
	 * Additional event types used for parsing Database wide policies
	 */
	private enum DBNodes{
		NODE,
		RELATION,
		QUERY,
		VIEW, 
		ATTRIBUTE,
		CONDITION,
		CONSTANT,
		GROUP_BY,
		FUNCTION,
		PK,
		FK,
		NC,
		UC;
		
		static DBNodes toType(String value) {
			return valueOf(value);
		}
	}
	
	/**
	 * Creates a HecataeusSQLExtensionParser 
	 */
	public HecataeusSQLExtensionParser(VisualGraph graph, File inputFile) {
		this._graph =  graph;
		this._inputFile = inputFile ;
	}

	public void processFile() throws HecataeusException{
		// holds each separate sentence
		String sentence = "";
		//holds database wide policies, so that to parse them last
		ArrayList<String> nodeSentences = new ArrayList<String>() ;
		//holds database wide policies, so that to parse them last
		ArrayList<String> databaseSentences = new ArrayList<String>() ;
				

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this._inputFile )); 
		
			this.clearPolicies();
			
			while (reader.ready()) {
				sentence=this.readSentence(reader);
				
				if(sentence.endsWith(";")){
					sentence = sentence.toUpperCase().replace(";", "");
					StringTokenizer token = new StringTokenizer(sentence);
					
					//get node name
					String nodeName = token.nextToken(":");
					//check whether it is database wide or node policy
					if (nodeName.trim().equalsIgnoreCase("DATABASE")) {
						databaseSentences.add(sentence);
					}else {
						nodeSentences.add(sentence);
						
					}
				}
			}
				
			reader.close();
		}
		catch (IOException e){
			throw new HecataeusException(e.getMessage());
		}  
		
		try {
			if ((databaseSentences.size()+nodeSentences.size())<=0) {
				throw new Exception("Input file is not in the correct format");
			}else {
				//parse first the global policies
				for (int i = 0; i < databaseSentences.size(); i++) {
					sentence = databaseSentences.get(i);
					this.parseDefaultPolicy(sentence);
				}
				//parse then the node policies
				for (int i = 0; i < nodeSentences.size(); i++) {
					sentence = nodeSentences.get(i);
					this.parseNodePolicy(sentence);
				}
			}
			
			
		}
		catch (HecataeusException e){
			throw new HecataeusException("Error parsing sentence: " + sentence + "\n" + e.getMessage());
		}
		catch (Exception e){
			throw new HecataeusException(e.getMessage());
		}
		
				
	}
	

	/**
	 * 
	 * clear all policies in graph  
	 */
	private void clearPolicies() {
	
		for (VisualNode node: this._graph.getVertices()) {
			node.getPolicies().clear();
			this._graph.getDefaultPolicyDecsriptions().clear();
		}
	}
	
	private void parseDefaultPolicy(String policyStringClause) throws HecataeusException {
		
		
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		
		//Default policies always start with reserved word DATABASE
		policyClause.nextToken(":").trim();
		
		String currentWord = policyClause.nextToken(" ").trim();
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException("Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type
		currentWord = policyClause.nextToken().trim();
		EventType eventType;
		try {
			eventType = EventType.toEventType(currentWord.toUpperCase());
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Event: " + currentWord); 
		}
		
		//Expect TO keyword
		currentWord = policyClause.nextToken().trim();
		if (!(currentWord.equalsIgnoreCase("TO"))){
			throw new HecataeusException("Unknown Token: " + currentWord + "\nExpected: TO");
		}
		
		//Get Node on which the event is applied
		currentWord = policyClause.nextToken().trim();
		DBNodes eventNodeType;
		try {
			eventNodeType = DBNodes.toType(currentWord.toUpperCase());
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Node Type: " + currentWord); 
		}
		
		//Expect THEN keyword
		currentWord = policyClause.nextToken().trim();
		if (!(currentWord.equalsIgnoreCase("THEN"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: THEN");
		}
		
		//Get Policy Type
		currentWord = policyClause.nextToken().trim();
		PolicyType policyType;
		try {
			policyType = PolicyType.toPolicyType(currentWord.toUpperCase());
		
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Policy: " + currentWord); 
		}
		
		switch (eventNodeType) {
		//all nodes
		case NODE: 
			for (VisualNode node: this._graph.getVertices()) {
				node.addPolicy(eventType, node, policyType);
			}
			break;
		//top-level nodes
		case RELATION:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_RELATION)) {
				node.addPolicy(eventType, node, policyType);
				if (eventType!=EventType.ADD_ATTRIBUTE
						&&eventType!=EventType.ADD_CONDITION
						&&eventType!=EventType.RENAME_RELATION) {
					for (VisualNode child : this._graph.getModule(node)) {
						child.addPolicy(eventType, child, policyType);
				}
				}
			}
			break;
		case QUERY :
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_QUERY)) {
				node.addPolicy(eventType, node, policyType);
				
				//fill tree for certain events
				if (eventType!=EventType.ADD_ATTRIBUTE
						&&eventType!=EventType.ADD_CONDITION
						&&eventType!=EventType.RENAME_RELATION) {
					for (VisualNode child : this._graph.getModule(node)) {
						child.addPolicy(eventType, child, policyType);
					}
				}else
				{
					//declare policies for events coming form providers top level nodes
					//through FROM edges
					for (EvolutionEdge edge: this._graph.getOutEdges(node)) {
						if (edge.getType()==EdgeType.EDGE_TYPE_FROM) {
							node.addPolicy(eventType, edge.getToNode(), policyType);
						}
					}
				}
			}
			break;
		case VIEW:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_VIEW)) {
				node.addPolicy(eventType, node, policyType);
				if (eventType!=EventType.ADD_ATTRIBUTE
						&&eventType!=EventType.ADD_CONDITION
						&&eventType!=EventType.RENAME_RELATION) {
					for (VisualNode child : this._graph.getModule(node)) {
						child.addPolicy(eventType, child, policyType);
					}
				}else
				{
					//declare policies for events coming form providers toplevel nodes
					//through FROM edges
					for (EvolutionEdge edge: this._graph.getOutEdges(node)) {
						if (edge.getType()==EdgeType.EDGE_TYPE_FROM) {
							node.addPolicy(eventType, edge.getToNode(), policyType);
						}
					}
				}
			}
			break;
		//low-level nodes
		case ATTRIBUTE:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_ATTRIBUTE)) {
				node.addPolicy(eventType, node, policyType);
				for (VisualNode child : this._graph.getModule(node)) {
					child.addPolicy(eventType, child, policyType);
				}
			}
			break;
		case GROUP_BY:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_GROUP_BY)) {
				node.addPolicy(eventType, node, policyType);
			}
			break;
		case FUNCTION:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_FUNCTION)) {
				node.addPolicy(eventType, node, policyType);
			}
			break;
		case CONSTANT:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_CONSTANT)) {
				node.addPolicy(eventType, node, policyType);
			}
			break;
		case CONDITION:
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_OPERAND)) {
				node.addPolicy(eventType, node, policyType);
				for (VisualNode child : this._graph.getModule(node)) {
					child.addPolicy(eventType, child, policyType);
				}
			}
			break;
		default: //for PK, UC, FK, NC
			for (VisualNode node: this._graph.getVertices(NodeType.NODE_TYPE_CONDITION)) {
				if (node.getName().toUpperCase().contains(eventNodeType.toString().toUpperCase()))
					node.addPolicy(eventType, node, policyType);
			}
			break;
		}
		this._graph.getDefaultPolicyDecsriptions().add(policyStringClause);
	}
	
	private void parseNodePolicy(String policyStringClause) throws HecataeusException {
		
		
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		
		String nodeName = policyClause.nextToken(":").trim();
		
		//get node on which policy is applied
		VisualNode node;
		node = this.getNodeByName(nodeName);
		if (node==null)
			throw new HecataeusException("Unknown Node: " + nodeName);
		
		String currentWord = policyClause.nextToken(" ").trim();
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type
		currentWord = policyClause.nextToken().trim();
		EventType eventType;
		try {
			eventType = EventType.toEventType(currentWord.toUpperCase());
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Event: " + currentWord); 
		}
		
		//Expect TO keyword
		currentWord = policyClause.nextToken().trim();
		if (!(currentWord.equalsIgnoreCase("TO"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: TO");
		}
		
		//Get Node on which the event is applied
		currentWord = policyClause.nextToken().trim();
		VisualNode eventNode;
		try {
		 eventNode = this.getNodeByName(currentWord.toUpperCase());
			if (eventNode==null)
				throw new HecataeusException( "Unknown Node: " + currentWord);
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Node: " + currentWord); 
		}
		//Expect THEN keyword
		currentWord = policyClause.nextToken().trim();
		if (!(currentWord.equalsIgnoreCase("THEN"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: THEN");
		}
		
		//Get Policy Type
		currentWord = policyClause.nextToken().trim();
		PolicyType policyType;
		try {
			policyType = PolicyType.toPolicyType(currentWord.toUpperCase());
		
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Policy: " + currentWord); 
		}
	   
		if (policyClause.hasMoreTokens()) {
			throw new HecataeusException( "Unexpected Token: " + policyClause.nextToken());
			}
		
		//add new policy to Node, if not the same exists  
		
		node.addPolicy(eventType, eventNode, policyType);
		
		
		switch (node.getType()) {
		//top-level nodes
		case NODE_TYPE_RELATION :
			node.addPolicy(eventType, eventNode, policyType);
			if (eventType!=EventType.ADD_ATTRIBUTE
					&&eventType!=EventType.ADD_CONDITION
					&&eventType!=EventType.RENAME_RELATION) {
				for (VisualNode child : this._graph.getModule(eventNode)) {
					child.addPolicy(eventType, child, policyType);
				}
			}
			break;
		case NODE_TYPE_QUERY  :
				//fill tree for certain events
			node.addPolicy(eventType, eventNode, policyType);
			if (eventType!=EventType.ADD_ATTRIBUTE
					&&eventType!=EventType.ADD_CONDITION
					&&eventType!=EventType.RENAME_RELATION) {
				for (VisualNode child : this._graph.getModule(eventNode)) {
					child.addPolicy(eventType, child, policyType);
				}
			}else
			{
				//declare policies for events coming form providers top level nodes
				//through FROM edges
				for (EvolutionEdge edge: this._graph.getOutEdges(node)) {
					if (edge.getType()==EdgeType.EDGE_TYPE_FROM) {
						node.addPolicy(eventType, edge.getToNode(), policyType);
					}
				}
			}
			break;
		case NODE_TYPE_VIEW :
			node.addPolicy(eventType, eventNode, policyType);
			if (eventType!=EventType.ADD_ATTRIBUTE
					&&eventType!=EventType.ADD_CONDITION
					&&eventType!=EventType.RENAME_RELATION) {
				for (VisualNode child : this._graph.getModule(eventNode)) {
					child.addPolicy(eventType, child, policyType);
				}
			}else
			{
				//declare policies for events coming form providers toplevel nodes
				//through FROM edges
				for (EvolutionEdge edge: this._graph.getOutEdges(node)) {
					if (edge.getType()==EdgeType.EDGE_TYPE_FROM) {
						node.addPolicy(eventType, edge.getToNode(), policyType);
					}
				}
			}
			break;
		//low-level nodes
		case NODE_TYPE_ATTRIBUTE :
			node.addPolicy(eventType, eventNode, policyType);
			for (VisualNode child : this._graph.getModule(eventNode)) {
				child.addPolicy(eventType, child, policyType);
			}
			break;
		case NODE_TYPE_OPERAND  :
			node.addPolicy(eventType, eventNode, policyType);
			for (VisualNode child : this._graph.getModule(eventNode)) {
				child.addPolicy(eventType, child, policyType);
			}
			break;
		default: //for all other nodes
			node.addPolicy(eventType, eventNode, policyType);
		break;
		}
		
	};
	
	private VisualNode getNodeByName(String name) {
		//check if name is of the form TABLENAME.ATTRIBUTE
		String result[]= name.split("\\.");
		VisualNode node = null;
		node = this._graph.findVertexByName(result[0].trim().toUpperCase());
		if (result.length>1) {
			for (VisualNode childNode : this._graph.getModule(node)) {
				if (childNode.getName().equalsIgnoreCase(result[1].trim()))
					return childNode;
			}
		}else
			return node;
		
		return node;
		
	} 
	
		
	private  String readSentence(BufferedReader ffile) throws HecataeusException{
		String sentence="";
		boolean commentON = false;
		char c;
		char c_next;

		try 
		{
			while (ffile.ready()) 
			{
				c=(char)ffile.read();

				if (commentON)
				{
					//Read until for closing comment */
					if(c=='*'){ //potential comment
						c_next=(char)ffile.read();
						if (c_next == '/'){ //definitely closes comment
							commentON = false;
						}
					}
				}else{
					//Check for opening comment /*
					if(c=='/'){ //potential comment
						ffile.mark(1); //hold the position
						c_next=(char)ffile.read();
						if (c_next == '*'){ //definitely opens comment
							commentON = true;							
						}else{
							ffile.reset(); //it was not comment, go back to mark
						}
					}
					if (! commentON)
					{
						//Check for line comment --
						if(c=='-'){ //potential comment
							ffile.mark(1);
							c_next=(char)ffile.read();
							if (c_next == '-'){ //definitely rest line is comment
								ffile.readLine();
							}else{
								ffile.reset();
								sentence += c;
								if (c==';')
									return sentence.trim() ;
							}
						}else{
							sentence += c;
							if (c==';')
								return sentence.trim() ;
						}
					}
				}
			}
		}catch (Exception e)
		{
			throw new HecataeusException(e.getMessage());
		}
		return sentence.trim();
	}
}


