/**
 * 
 */
package org.hecataeus.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.hecataeus.evolution.*;
import org.hecataeus.*;

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
public final class HecataeusSQLExtensionParser {

	private HecataeusEvolutionGraph _graph;
	private File _inputFile;
	
	/**
	 * Additional event types used for parsing Database wide policies
	 */
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
	 * Additional Node types used only for parsing
	 * Default Database Default Policies
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
	public HecataeusSQLExtensionParser(HecataeusEvolutionGraph graph, File inputFile) {
		this._graph = graph;
		this._inputFile = inputFile ;
	}

	public void processFile() throws HecataeusException{
		// holds each separate sentence
		String sentence = "";
		//holds database wide policies, so that to parse them last
		ArrayList<String> nodeSentences = new ArrayList<String>() ;
		

		try {
			BufferedReader reader = new BufferedReader(new FileReader(this._inputFile ));
			
			this.clearPolicies();
			
			while (reader.ready()) {
				sentence=this.readSentence(reader);
				
				if(sentence.endsWith(";")){
					StringTokenizer token = new StringTokenizer(sentence.toUpperCase().replace(";", ""));
					
					//get node name
					String nodeName = token.nextToken(":");
					//check whether it is database wide or node policy
					if (nodeName.trim().equalsIgnoreCase("DATABASE")) {
						this.parseDefaultPolicy(token.nextToken());
					}else {
						nodeSentences.add(sentence.toUpperCase().replace(";", ""));
						
					}
				}
			}
				
			reader.close();
			
			//parse last the node policies
			for (int i = 0; i < nodeSentences.size(); i++) {
				String nodeSentence = nodeSentences.get(i);
				this.parseNodePolicy(nodeSentence);
			}
		}
		catch (HecataeusException e){
			throw new HecataeusException("Error parsing sentence: " + sentence + "\n" + e.getMessage());
		} 
		catch (IOException e){
			throw new HecataeusException(e.getMessage());
		}  
				
	}
	

	/**
	 * 
	 * clear all policies in graph  
	 */
	private void clearPolicies() {
		HecataeusEvolutionNodes nodes = this._graph.getNodes();
		for (int i = 0; i < nodes.size(); i++) {
			HecataeusEvolutionNode node = nodes.get(i);
			node.getPolicies().clear();
		}
	}
	
	private void parseDefaultPolicy(String policyStringClause) throws HecataeusException {
		
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		
		String currentWord = policyClause.nextToken().trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException("Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type
		currentWord = policyClause.nextToken().trim();
		HecataeusEventType eventType;
		try {
			eventType = HecataeusEventType.toEventType(currentWord.toUpperCase());
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
		HecataeusPolicyType policyType;
		try {
			policyType = HecataeusPolicyType.toPolicyType(currentWord.toUpperCase());
		
		} catch (Exception e) {
			throw new HecataeusException( "Unknown Policy: " + currentWord); 
		}
		
		HecataeusEvolutionNodes nodes;
		switch (eventNodeType) {
		//all nodes
		case NODE: 
			nodes = this._graph.getNodes();
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
			}
			break;
		//top-level nodes
		case RELATION:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_RELATION);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
				if (eventType!=HecataeusEventType.ADD_ATTRIBUTE
						&&eventType!=HecataeusEventType.ADD_CONDITION
						&&eventType!=HecataeusEventType.RENAME_RELATION) {
					HecataeusEvolutionNodes tree = this._graph.getSubGraph(node);
					for (int j = 0; j < tree.size(); j++) {
						HecataeusEvolutionNode child = tree.get(j);
						child.addPolicy(eventType, child, policyType);
				}
				}
			}
			break;
		case QUERY :
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_QUERY);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
				
				//fill tree for certain events
				if (eventType!=HecataeusEventType.ADD_ATTRIBUTE
						&&eventType!=HecataeusEventType.ADD_CONDITION
						&&eventType!=HecataeusEventType.RENAME_RELATION) {
					HecataeusEvolutionNodes tree = this._graph.getSubGraph(node);
					for (int j = 0; j < tree.size(); j++) {
						HecataeusEvolutionNode child = tree.get(j);
						child.addPolicy(eventType, child, policyType);
					}
				}else
				{
					//declare policies for events coming form providers top level nodes
					//through FROM edges
					HecataeusEvolutionEdges fromEdges = node.getOutEdges();
					for (int j = 0; j < fromEdges.size(); j++) {
						HecataeusEvolutionEdge edge = fromEdges.get(j);
						if (edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
							node.addPolicy(eventType, edge.getToNode(), policyType);
						}
					}
				}
			}
			break;
		case VIEW:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_VIEW);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
				if (eventType!=HecataeusEventType.ADD_ATTRIBUTE
						&&eventType!=HecataeusEventType.ADD_CONDITION
						&&eventType!=HecataeusEventType.RENAME_RELATION) {
					HecataeusEvolutionNodes tree = this._graph.getSubGraph(node);
					for (int j = 0; j < tree.size(); j++) {
						HecataeusEvolutionNode child = tree.get(j);
						child.addPolicy(eventType, child, policyType);
					}
				}else
				{
					//declare policies for events coming form providers toplevel nodes
					//through FROM edges
					HecataeusEvolutionEdges fromEdges = node.getOutEdges();
					for (int j = 0; j < fromEdges.size(); j++) {
						HecataeusEvolutionEdge edge = fromEdges.get(j);
						if (edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
							node.addPolicy(eventType, edge.getToNode(), policyType);
						}
					}
				}
			}
			break;
		//low-level nodes
		case ATTRIBUTE:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
				HecataeusEvolutionNodes tree = this._graph.getSubGraph(node);
				for (int j = 0; j < tree.size(); j++) {
					HecataeusEvolutionNode child = tree.get(j);
					child.addPolicy(eventType, child, policyType);
				}
			}
			break;
		case GROUP_BY:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_GROUP_BY);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
			}
			break;
		case FUNCTION:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_FUNCTION);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
			}
			break;
		case CONSTANT:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_CONSTANT);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
			}
			break;
		case CONDITION:
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_OPERAND);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				node.addPolicy(eventType, node, policyType);
				HecataeusEvolutionNodes tree = this._graph.getSubGraph(node);
				for (int j = 0; j < tree.size(); j++) {
					HecataeusEvolutionNode child = tree.get(j);
					child.addPolicy(eventType, child, policyType);
				}
			}
			break;
		default: //for PK, UC, FK, NC
			nodes = this._graph.getNodes(HecataeusNodeType.NODE_TYPE_CONDITION);
			for (int i = 0; i < nodes.size(); i++) {
				HecataeusEvolutionNode node = nodes.get(i);
				if (node.getName().toUpperCase().contains(eventNodeType.toString().toUpperCase()))
					node.addPolicy(eventType, node, policyType);
			}
			break;
		}
	}
	
	private void parseNodePolicy(String policyStringClause) throws HecataeusException {
		
		
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		
		String nodeName = policyClause.nextToken(":").trim();
		
		//get node on which policy is applied
		HecataeusEvolutionNode node;
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
		HecataeusEventType eventType;
		try {
			eventType = HecataeusEventType.toEventType(currentWord.toUpperCase());
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
		HecataeusEvolutionNode eventNode;
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
		HecataeusPolicyType policyType;
		try {
			policyType = HecataeusPolicyType.toPolicyType(currentWord.toUpperCase());
		
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
			if (eventType!=HecataeusEventType.ADD_ATTRIBUTE
					&&eventType!=HecataeusEventType.ADD_CONDITION
					&&eventType!=HecataeusEventType.RENAME_RELATION) {
				HecataeusEvolutionNodes tree = this._graph.getSubGraph(eventNode);
				for (int j = 0; j < tree.size(); j++) {
					HecataeusEvolutionNode child = tree.get(j);
					child.addPolicy(eventType, child, policyType);
				}
			}
			break;
		case NODE_TYPE_QUERY  :
				//fill tree for certain events
			node.addPolicy(eventType, eventNode, policyType);
			if (eventType!=HecataeusEventType.ADD_ATTRIBUTE
					&&eventType!=HecataeusEventType.ADD_CONDITION
					&&eventType!=HecataeusEventType.RENAME_RELATION) {
				HecataeusEvolutionNodes tree = this._graph.getSubGraph(eventNode);
				for (int j = 0; j < tree.size(); j++) {
					HecataeusEvolutionNode child = tree.get(j);
					child.addPolicy(eventType, child, policyType);
				}
			}else
			{
				//declare policies for events coming form providers top level nodes
				//through FROM edges
				HecataeusEvolutionEdges fromEdges = node.getOutEdges();
				for (int j = 0; j < fromEdges.size(); j++) {
					HecataeusEvolutionEdge edge = fromEdges.get(j);
					if (edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
						node.addPolicy(eventType, edge.getToNode(), policyType);
					}
				}
			}
			break;
		case NODE_TYPE_VIEW :
			node.addPolicy(eventType, eventNode, policyType);
			if (eventType!=HecataeusEventType.ADD_ATTRIBUTE
					&&eventType!=HecataeusEventType.ADD_CONDITION
					&&eventType!=HecataeusEventType.RENAME_RELATION) {
				HecataeusEvolutionNodes tree = this._graph.getSubGraph(eventNode);
				for (int j = 0; j < tree.size(); j++) {
					HecataeusEvolutionNode child = tree.get(j);
					child.addPolicy(eventType, child, policyType);
				}
			}else
			{
				//declare policies for events coming form providers toplevel nodes
				//through FROM edges
				HecataeusEvolutionEdges fromEdges = node.getOutEdges();
				for (int j = 0; j < fromEdges.size(); j++) {
					HecataeusEvolutionEdge edge = fromEdges.get(j);
					if (edge.getType()==HecataeusEdgeType.EDGE_TYPE_FROM) {
						node.addPolicy(eventType, edge.getToNode(), policyType);
					}
				}
			}
			break;
		//low-level nodes
		case NODE_TYPE_ATTRIBUTE :
			node.addPolicy(eventType, eventNode, policyType);
			HecataeusEvolutionNodes tree = this._graph.getSubGraph(eventNode);
			for (int j = 0; j < tree.size(); j++) {
				HecataeusEvolutionNode child = tree.get(j);
				child.addPolicy(eventType, child, policyType);
			}
			break;
		case NODE_TYPE_OPERAND  :
			node.addPolicy(eventType, eventNode, policyType);
			tree = this._graph.getSubGraph(eventNode);
			for (int j = 0; j < tree.size(); j++) {
				HecataeusEvolutionNode child = tree.get(j);
				child.addPolicy(eventType, child, policyType);
			}
			break;
		default: //for all other nodes
			node.addPolicy(eventType, eventNode, policyType);
		break;
		}
		
	};
	
	private HecataeusEvolutionNode getNodeByName(String name) {
		//check if name is of the form TABLENAME.ATTRIBUTE
		String result[]= name.split("\\.");
		HecataeusEvolutionNode node = null;
		node = this._graph.getNodeByName(result[0].trim().toUpperCase());
		if (result.length>1) {
			HecataeusEvolutionNodes childNodes = this._graph.getSubGraph(node);
			for (int i = 0; i < childNodes.size(); i++) {
				HecataeusEvolutionNode childNode = childNodes.get(i);
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


