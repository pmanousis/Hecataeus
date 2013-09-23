/**
 * 
 */
package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import edu.ntua.dblab.hecataeus.HecataeusException;
import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
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
		INSERT,				/**added by sgerag*/
		DELETE,				/**added by sgerag*/
		UPDATE,				/**added by sgerag*/
		// FIXME: ADD AND HANDLE ALL TYPES OF NODES IN POLICY PARSING
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

	public void processFile() throws HecataeusException
	{
		String sentence = "";	// holds each separate sentence
		ArrayList<String> nodeSentences = new ArrayList<String>();	//holds database wide policies, so that to parse them last
		//ArrayList<String> databaseSentences = new ArrayList<String>();	//holds database wide policies, so that to parse them last
		ArrayList<String> relationSentences = new ArrayList<String>();	//holds database wide policies, so that to parse them last
		ArrayList<String> viewSentences = new ArrayList<String>();	//holds database wide policies, so that to parse them last
		ArrayList<String> querySentences = new ArrayList<String>();	//holds database wide policies, so that to parse them last
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(this._inputFile )); 
			this.clearPolicies();
			while (reader.ready())
			{
				sentence=this.readSentence(reader);
				if(sentence.endsWith(";"))
				{
					sentence = sentence.toUpperCase().replace(";", "");
					StringTokenizer token = new StringTokenizer(sentence);
					String nodeName = token.nextToken(":");	//get node name
					if (nodeName.trim().startsWith("RELATION."))	//check whether it is relation wide policy
					{
						relationSentences.add(sentence);
					}
					else if(nodeName.trim().startsWith("VIEW."))	//check whether it is view wide policy
					{
						viewSentences.add(sentence);
					}
					else if(nodeName.trim().startsWith("QUERY."))	//check whether it is query wide policy
					{
						querySentences.add(sentence);
					}
					else
					{
						nodeSentences.add(sentence);
					}
				}
			}
			reader.close();
		}
		catch (IOException e)
		{
			throw new HecataeusException(e.getMessage());
		}
		try
		{
			if ((relationSentences.size()+viewSentences.size()+querySentences.size()+nodeSentences.size())<=0)
			{
				throw new Exception("Input file is not in the correct format or empty file");
			}
			else
			{
				for (int i = 0; i < relationSentences.size(); i++)	//parse first the global policies
				{
					sentence = relationSentences.get(i);
					this.parseRelationPolicy(sentence);
				}
				for (int i = 0; i < viewSentences.size(); i++)	//parse first the global policies
				{
					sentence = viewSentences.get(i);
					this.parseViewPolicy(sentence);
				}
				for (int i = 0; i < querySentences.size(); i++)	//parse first the global policies
				{
					sentence = querySentences.get(i);
					this.parseQueryPolicy(sentence);
				}
				for (int i = 0; i < nodeSentences.size(); i++)	//parse then the node policies
				{
					sentence = nodeSentences.get(i).toUpperCase();
					if(sentence.contains(".ATTRIBUTES"))
					{
						this.parseNodeAttributesPolicy(sentence);
					}
					else
					{
						this.parseNodePolicy(sentence);
					}
				}
			}
		}
		catch (HecataeusException e)
		{
			throw new HecataeusException("Error parsing sentence: " + sentence + "\n" + e.getMessage());
		}
		catch (Exception e)
		{
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
	
	/**
	 * @author pmanousi
	 * Sets policy to a specific node.
	 * @throws HecataeusException 
	 */
	private void setPolicy(String policy, VisualNode node) throws HecataeusException
	{
		StringTokenizer policyClause= new StringTokenizer(policy);
		String currentWord = policyClause.nextToken(" ").trim();
		//currentWord = policyClause.nextToken(" ").trim();
		if (!(currentWord.equalsIgnoreCase("ON")))	//Expect ON keyword
		{
			throw new HecataeusException("Unknown Token: " + currentWord + "\nExpected: ON");
		}
		currentWord = policyClause.nextToken().trim();	//Get event type
		EventType eventType;
		try
		{
			eventType = EventType.toEventType(currentWord.toUpperCase());
		}
		catch (Exception e)
		{
			throw new HecataeusException( "Unknown Event: " + currentWord); 
		}
		currentWord = policyClause.nextToken().trim();	//Expect THEN keyword
		if (!(currentWord.equalsIgnoreCase("THEN")))
		{
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: THEN");
		}
		currentWord = policyClause.nextToken().trim();//Get Policy Type
		PolicyType policyType;
		try
		{
			policyType = PolicyType.toPolicyType(currentWord.toUpperCase());
		}
		catch (Exception e)
		{
			throw new HecataeusException( "Unknown Policy: " + currentWord); 
		}
		node.addPolicy(eventType, policyType);
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to relation node or relation's attributes nodes.
	 */
	private void parseRelationPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String selfOrAttributes = policyClause.nextToken(":").trim();	//Relation policies always start with reserved word RELATION.OUT.(SELF|ATTRIBUTES)
		
		List<VisualNode> nodes = this._graph.getVertices(NodeType.NODE_TYPE_RELATION);
		String tmpString = policyClause.nextToken().trim();
		if(selfOrAttributes.endsWith(".OUT.ATTRIBUTES")==true)
		{ // For all RELATIONS of graph go to their ATTRS.
			for(int i=0;i<nodes.size();i++)
			{
				VisualNode schemaNode=nodes.get(i).getOutEdges().get(0).getToNode();	// RELATION.SCHEMA
				for(int j=0;j<schemaNode.getOutEdges().size();j++)
				{
					setPolicy(tmpString, schemaNode.getOutEdges().get(j).getToNode());
				}
			}
		}
		else if(selfOrAttributes.endsWith(".OUT.SELF")==true)
		{ // For all RELATIONS of graph
			for(int i=0;i<nodes.size();i++)
			{
				setPolicy(tmpString, nodes.get(i).getOutEdges().get(0).getToNode());
			}
		}
		this._graph.getDefaultPolicyDecsriptions().add(policyStringClause);
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to view's: smtx, in, out nodes or in's, out's attributes.
	 */
	private void parseViewPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String selfOrAttributes = policyClause.nextToken(":").trim();	//View policies always start with reserved word VIEW.(IN|SMTX|OUT).(SELF|ATTRIBUTES)
		
		List<VisualNode> nodes = this._graph.getVertices(NodeType.NODE_TYPE_VIEW);
		
		String tmpString = policyClause.nextToken().trim();
		if(selfOrAttributes.endsWith(".OUT.ATTRIBUTES"))
		{ // For all VIEWS of graph go to their ATTRS.
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_OUTPUT)
					{
						VisualNode outNode=edges.get(j).getToNode();	// VIEW.OUT
						for(int k=0;k<outNode.getOutEdges().size();k++)
						{
							setPolicy(tmpString, outNode.getOutEdges().get(k).getToNode());
						}
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".OUT.SELF"))
		{ // For all VIEWS of graph
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_OUTPUT)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".IN.ATTRIBUTES"))
		{ // For all VIEWS of graph go to their ATTRS.
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_INPUT)
					{
						VisualNode outNode=edges.get(j).getToNode();	// VIEW.IN
						for(int k=0;k<outNode.getOutEdges().size();k++)
						{
							if(outNode.getOutEdges().get(k).getType()!=EdgeType.EDGE_TYPE_FROM)
							{
								setPolicy(tmpString, outNode.getOutEdges().get(k).getToNode());
							}
						}
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".IN.SELF"))
		{ // For all VIEWS of graph
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_INPUT)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".SMTX.SELF"))
		{ // For all VIEWS of graph
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
		this._graph.getDefaultPolicyDecsriptions().add(policyStringClause);
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to query's: smtx, in, out nodes or in's, out's attributes.
	 */
	private void parseQueryPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String selfOrAttributes = policyClause.nextToken(":").trim();	//Query policies always start with reserved word QUERY.(IN|SMTX|OUT).(SELF|ATTRIBUTES)
		
		List<VisualNode> nodes = this._graph.getVertices(NodeType.NODE_TYPE_QUERY);
		
		if(selfOrAttributes.endsWith(".OUT.ATTRIBUTES"))
		{ // For all QUERIES of graph go to their ATTRS.
			String tmpString = policyClause.nextToken().trim();
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_OUTPUT)
					{
						VisualNode outNode=edges.get(j).getToNode();	// QUERY.OUT
						for(int k=0;k<outNode.getOutEdges().size();k++)
						{
							setPolicy(tmpString, outNode.getOutEdges().get(k).getToNode());
						}
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".OUT.SELF"))
		{ // For all QUERIES of graph
			String tmpString = policyClause.nextToken().trim();
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_OUTPUT)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".IN.ATTRIBUTES"))
		{ // For all QUERIES of graph go to their ATTRS.
			String tmpString = policyClause.nextToken().trim();
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_INPUT)
					{
						VisualNode outNode=edges.get(j).getToNode();	// QUERY.IN
						for(int k=0;k<outNode.getOutEdges().size();k++)
						{
							if(outNode.getOutEdges().get(k).getType()!=EdgeType.EDGE_TYPE_FROM)
							{
								setPolicy(tmpString, outNode.getOutEdges().get(k).getToNode());
							}
						}
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".IN.SELF"))
		{ // For all QUERIES of graph
			String tmpString = policyClause.nextToken().trim();
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_INPUT)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
		else if(selfOrAttributes.endsWith(".SMTX.SELF"))
		{ // For all QUERIES of graph
			String tmpString = policyClause.nextToken().trim();
			for(int i=0;i<nodes.size();i++)
			{
				List<VisualEdge> edges=nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
		this._graph.getDefaultPolicyDecsriptions().add(policyStringClause);
	}
	
	/**
	 * @author pmanousi
	 */
	private void parseNodeAttributesPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String nodeName = policyClause.nextToken(":").trim();	//get node on which policy is applied
		VisualNode node;
		node = this.getNodeByName(nodeName);
		if (node==null)
			throw new HecataeusException("Unknown Node: " + nodeName);
		String tmpString = policyClause.nextToken().trim();
		for(int i=0;i<node.getOutEdges().size();i++)
		{
			setPolicy(tmpString, node.getOutEdges().get(i).getToNode());
		}
	};
	
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
		node.addPolicy(eventType, policyType);
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


