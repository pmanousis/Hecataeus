/**
 * 
 */
package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.gui.HecataeusException;

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

	private EvolutionGraph evoGraph;
	private File inputFile;
	
	public HecataeusSQLExtensionParser(	EvolutionGraph graph,
										File inputFile) {
		this.evoGraph =  graph;
		this.inputFile = inputFile ;
	};
	
	public void processFile() throws HecataeusException
	{
		String sentence = "";	// holds each separate sentence
		ArrayList<String> whateverSentences = new ArrayList<String>();	//holds policies for specific nodes.
		ArrayList<String> whateverStarSentences = new ArrayList<String>();	//holds policies containing * for specific modules
		ArrayList<String> databaseSentences = new ArrayList<String>();	//holds database wide policies (NODE)
		
		ArrayList<String> relationSentences = new ArrayList<String>();	//holds database wide policies for relations
		ArrayList<String> viewSentences = new ArrayList<String>();	//holds database wide policies for views
		ArrayList<String> querySentences = new ArrayList<String>();	//holds database wide policies for queries
		
		ArrayList<String> relationTempSentences = new ArrayList<String>();	//holds database wide policies for relations that are to be concatenated with relationSentences
		ArrayList<String> viewTempSentences = new ArrayList<String>();	//holds database wide policies for views that are to be concatenated with viewSentences
		ArrayList<String> queryTempSentences = new ArrayList<String>();	//holds database wide policies for queries that are to be concatenated with querySentences
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(this.inputFile )); 
			this.clearPolicies();
			while (reader.ready())
			{
				sentence=this.readSentence(reader);
				if(sentence.endsWith(";"))
				{
					sentence = sentence.toUpperCase().replace(";", "");
					StringTokenizer token = new StringTokenizer(sentence);
					String nodeName = token.nextToken(":");	//get node name
					
					if (nodeName.trim().equals("NODE"))	//check whether it is for all nodes.
					{
						databaseSentences.add(sentence);
					}					
					else if (nodeName.trim().startsWith("RELATION."))	//check whether it is relation wide policy
					{
						relationTempSentences.add(sentence);
					}
					else if(nodeName.trim().startsWith("VIEW."))	//check whether it is view wide policy
					{
						viewTempSentences.add(sentence);
					}
					else if(nodeName.trim().startsWith("QUERY."))	//check whether it is query wide policy
					{
						queryTempSentences.add(sentence);
					}
					else
					{
						if(sentence.contains("*"))
						{
							whateverStarSentences.add(sentence);
						}
						else
						{
							whateverSentences.add(sentence);
						}
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
			if ((relationTempSentences.size()+viewTempSentences.size()+queryTempSentences.size()+whateverSentences.size()+databaseSentences.size()+whateverStarSentences.size())<=0)
			{
				throw new Exception("Input file is not in the correct format or empty file");
			}
			else
			{
				Collections.sort(databaseSentences, new Comparator<String>() {	// We initially put the sentences that contain '*'
				    public int compare(String a, String b) {
				    	if(a.contains("*")==true && b.contains("*")==false)
				    	{
				    		return (-1);
				    	}
				    	else if(a.contains("*")==false && b.contains("*")==true)
				    	{
				    		return (1);
				    	}
						return 0;
				    }
				});
				for (int i = 0; i < databaseSentences.size(); i++)	//parse first the global policies
				{	// add to relationSentences, viewSentences and querySentences all the needed policies.
					sentence = databaseSentences.get(i);
					this.parseDatabasePolicy(sentence, relationSentences, viewSentences, querySentences);
				}
				for (int i = 0; i < whateverStarSentences.size(); i++)	//parse first the <module>: on * then <policy>; lines
				{
					Collections.sort(whateverStarSentences, new Comparator<String>() {	// We initially put the sentences that contain '*'
					    public int compare(String a, String b) {
					    	if((a.contains("RELATION:")==true && b.contains("RELATION:")==false)||(a.contains("VIEW:")==true && b.contains("VIEW:")==false)||(a.contains("QUERY:")==true && b.contains("QUERY:")==false))
					    	{
					    		return (-1);
					    	}
					    	else if((a.contains("RELATION:")==false && b.contains("RELATION:")==true)||(a.contains("VIEW:")==false && b.contains("VIEW:")==true)||(a.contains("QUERY:")==false && b.contains("QUERY:")==true))
					    	{
					    		return (1);
					    	}
							return 0;
					    }
					});
					sentence= whateverStarSentences.get(i);
					//find if it is a module or schema or attribute
					StringTokenizer token = new StringTokenizer(sentence);
					String ndName = token.nextToken(":");
					if(ndName.equals("RELATION"))
					{
						this.parseRelationPolicy(sentence, relationSentences);
					}
					else if(ndName.equals("VIEW"))
					{
						this.parseViewPolicy(sentence, viewSentences);
					}
					else if(ndName.equals("QUERY"))
					{
						this.parseQueryPolicy(sentence, querySentences);
					}
					else if(this.evoGraph.findVertexByName(ndName,NodeCategory.MODULE)!=null)
					{
						this.parseModulePolicy(sentence, whateverSentences);
					}
					else if(this.evoGraph.findVertexByName(ndName,NodeCategory.INOUTSCHEMA)!=null)
					{
						this.parseSchemaPolicy(sentence, whateverSentences);
					}
					else if(this.evoGraph.findVertexByName(ndName,NodeCategory.SCHEMA)!=null)
					{
						this.parseAttributesPolicy(sentence, whateverSentences);
					}
				}
				relationSentences.addAll(relationTempSentences);
				relationTempSentences.clear();
				viewSentences.addAll(viewTempSentences);
				viewTempSentences.clear();
				querySentences.addAll(queryTempSentences);
				queryTempSentences.clear();
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
				for (int i = 0; i < whateverSentences.size(); i++)	//parse then the node policies
				{
					sentence = whateverSentences.get(i).toUpperCase();
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
	
		for (EvolutionNode node : this.evoGraph.getVertices()) {
			node.getPolicies().clear();	
		}
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to a specific node.
	 * @throws HecataeusException 
	 */
	private void setPolicy(String policy, EvolutionNode evolutionNode)
		throws HecataeusException
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
		evolutionNode.addPolicy(eventType, policyType);
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to relation node or relation's attributes nodes.
	 */
	@SuppressWarnings("unchecked")
	private void parseRelationPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String selfOrAttributes = policyClause.nextToken(":").trim();	//Relation policies always start with reserved word RELATION.OUT.(SELF|ATTRIBUTES)
		
		List<EvolutionNode> nodes = this.evoGraph.getVertices(NodeType.NODE_TYPE_RELATION);
		String tmpString = policyClause.nextToken().trim();
		if(selfOrAttributes.endsWith(".OUT.ATTRIBUTES")==true)
		{ // For all RELATIONS of graph go to their ATTRS.
			for(int i=0;i<nodes.size();i++)
			{
				EvolutionNode schemaNode =
					(EvolutionNode) nodes.get(i).getOutEdges().get(0).getToNode(); // RELATION.SCHEMA
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
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to view's: smtx, in, out nodes or in's, out's attributes.
	 */
	@SuppressWarnings("unchecked")
	private void parseViewPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String selfOrAttributes = policyClause.nextToken(":").trim();	//View policies always start with reserved word VIEW.(IN|SMTX|OUT).(SELF|ATTRIBUTES)
		
		List<EvolutionNode> nodes = this.evoGraph.getVertices(NodeType.NODE_TYPE_VIEW);
		
		String tmpString = policyClause.nextToken().trim();
		if(selfOrAttributes.endsWith(".OUT.ATTRIBUTES"))
		{ // For all VIEWS of graph go to their ATTRS.
			for(int i=0;i<nodes.size();i++)
			{
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_OUTPUT)
					{
						EvolutionNode outNode = (EvolutionNode) edges.get(j).getToNode(); // VIEW.OUT
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_INPUT)
					{
						EvolutionNode outNode = (EvolutionNode) edges.get(j).getToNode(); // VIEW.IN
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
	}
	
	/**
	 * @author pmanousi
	 * Sets policy to query's: smtx, in, out nodes or in's, out's attributes.
	 */
	@SuppressWarnings("unchecked")
	private void parseQueryPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String selfOrAttributes = policyClause.nextToken(":").trim();	//Query policies always start with reserved word QUERY.(IN|SMTX|OUT).(SELF|ATTRIBUTES)
		
		List<EvolutionNode> nodes = this.evoGraph.getVertices(NodeType.NODE_TYPE_QUERY);
		
		if(selfOrAttributes.endsWith(".OUT.ATTRIBUTES"))
		{ // For all QUERIES of graph go to their ATTRS.
			String tmpString = policyClause.nextToken().trim();
			for(int i=0;i<nodes.size();i++)
			{
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_OUTPUT)
					{
						EvolutionNode outNode = (EvolutionNode) edges.get(j).getToNode(); // QUERY.OUT
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_INPUT)
					{
						EvolutionNode outNode = (EvolutionNode) edges.get(j).getToNode(); // QUERY.IN
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
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
				List<EvolutionEdge> edges = nodes.get(i).getOutEdges();
				for(int j=0;j<edges.size();j++)
				{
					if(edges.get(j).getType()==EdgeType.EDGE_TYPE_SEMANTICS)
					{
						setPolicy(tmpString, edges.get(j).getToNode());
					}
				}
			}
		}
	}

	/**
	 * @author pmanousi
	 * Sets policy to all nodes.
	 * @throws Exception if there is unrecognized event type.
	 */
	private void parseDatabasePolicy(String policyStringClause, ArrayList<String> relationSentences, ArrayList<String> viewSentences, ArrayList<String> querySentences) throws Exception{
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		policyClause.nextToken(": ").trim();	//These policies always start with reserved word NODE:
		policyClause.nextToken(" ").trim();
		String evtString = policyClause.nextToken().trim();	// This is for ON keyword
		evtString = policyClause.nextToken().trim();	// This is for the event.
		String plcStr= policyClause.nextToken().trim();	// This is for the THEN keyword
		plcStr = policyClause.nextToken().trim();	// This is the policy;
		
		if(evtString.equals("*")==true)
		{	// For all events.
			relationSentences.add("RELATION.OUT.SELF: on ADD_ATTRIBUTE then "+plcStr);
			relationSentences.add("RELATION.OUT.SELF: on DELETE_SELF then "+plcStr);
			relationSentences.add("RELATION.OUT.SELF: on RENAME_SELF then "+plcStr);
			relationSentences.add("RELATION.OUT.ATTRIBUTES: on DELETE_SELF then "+plcStr);
			relationSentences.add("RELATION.OUT.ATTRIBUTES: on RENAME_SELF then "+plcStr);
			viewSentences.add("VIEW.OUT.SELF: on ADD_ATTRIBUTE then "+plcStr);
			viewSentences.add("VIEW.OUT.SELF: on DELETE_SELF then "+plcStr);
			viewSentences.add("VIEW.OUT.SELF: on RENAME_SELF then "+plcStr);
			viewSentences.add("VIEW.OUT.SELF: on ADD_ATTRIBUTE_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.OUT.ATTRIBUTES: on DELETE_SELF then "+plcStr);
			viewSentences.add("VIEW.OUT.ATTRIBUTES: on RENAME_SELF then "+plcStr);
			viewSentences.add("VIEW.OUT.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.OUT.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.IN.SELF: on DELETE_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.IN.SELF: on RENAME_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.IN.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.IN.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
			viewSentences.add("VIEW.SMTX.SELF: on ALTER_SEMANTICS then "+plcStr);
			querySentences.add("QUERY.OUT.SELF: on ADD_ATTRIBUTE then "+plcStr);
			querySentences.add("QUERY.OUT.SELF: on ADD_ATTRIBUTE_PROVIDER then "+plcStr);
			querySentences.add("QUERY.OUT.SELF: on DELETE_SELF then "+plcStr);
			querySentences.add("QUERY.OUT.SELF: on RENAME_SELF then "+plcStr);
			querySentences.add("QUERY.OUT.ATTRIBUTES: on DELETE_SELF then "+plcStr);
			querySentences.add("QUERY.OUT.ATTRIBUTES: on RENAME_SELF then "+plcStr);
			querySentences.add("QUERY.OUT.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
			querySentences.add("QUERY.OUT.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
			querySentences.add("QUERY.IN.SELF: on DELETE_PROVIDER then "+plcStr);
			querySentences.add("QUERY.IN.SELF: on RENAME_PROVIDER then "+plcStr);
			querySentences.add("QUERY.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then "+plcStr);
			querySentences.add("QUERY.IN.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
			querySentences.add("QUERY.IN.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
			querySentences.add("QUERY.SMTX.SELF: on ALTER_SEMANTICS then "+plcStr);
		}
		else
		{
			if(evtString.equals(EventType.ADD_ATTRIBUTE.toString()))
			{
				relationSentences.add("RELATION.OUT.SELF: on ADD_ATTRIBUTE then "+plcStr);
				viewSentences.add("VIEW.OUT.SELF: on ADD_ATTRIBUTE then "+plcStr);
				querySentences.add("QUERY.OUT.SELF: on ADD_ATTRIBUTE then "+plcStr);
			}
			else if(evtString.equals(EventType.ADD_ATTRIBUTE_PROVIDER.toString()))
			{
				viewSentences.add("VIEW.IN.SELF: on ADD_ATTRIBUTE_PROVIDER "+plcStr);
				querySentences.add("QUERY.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then "+plcStr);
			}
			else if(evtString.equals(EventType.DELETE_SELF.toString()))
			{
				relationSentences.add("RELATION.OUT.SELF: on DELETE_SELF then "+plcStr);
				relationSentences.add("RELATION.OUT.ATTRIBUTES: on DELETE_SELF then "+plcStr);
				viewSentences.add("VIEW.OUT.SELF: on DELETE_SELF then "+plcStr);
				viewSentences.add("VIEW.OUT.ATTRIBUTES: on DELETE_SELF then "+plcStr);
				querySentences.add("QUERY.OUT.SELF: on DELETE_SELF then "+plcStr);
				querySentences.add("QUERY.OUT.ATTRIBUTES: on DELETE_SELF then "+plcStr);
			}
			else if(evtString.equals(EventType.DELETE_PROVIDER.toString()))
			{
				viewSentences.add("VIEW.IN.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
				viewSentences.add("VIEW.IN.SELF: on DELETE_PROVIDER then "+plcStr);
				viewSentences.add("VIEW.OUT.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
				querySentences.add("QUERY.IN.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
				querySentences.add("QUERY.IN.SELF: on DELETE_PROVIDER then "+plcStr);
				querySentences.add("QUERY.OUT.ATTRIBUTES: on DELETE_PROVIDER then "+plcStr);
			}
			else if(evtString.equals(EventType.RENAME_SELF.toString()))
			{
				relationSentences.add("RELATION.OUT.SELF: on RENAME_SELF then "+plcStr);
				relationSentences.add("RELATION.OUT.ATTRIBUTES: on RENAME_SELF then "+plcStr);
				viewSentences.add("VIEW.OUT.SELF: on RENAME_SELF then "+plcStr);
				viewSentences.add("VIEW.OUT.ATTRIBUTES: on RENAME_SELF then "+plcStr);
				querySentences.add("QUERY.OUT.SELF: on RENAME_SELF then "+plcStr);
				querySentences.add("QUERY.OUT.ATTRIBUTES: on RENAME_SELF then "+plcStr);
			}
			else if(evtString.equals(EventType.RENAME_PROVIDER.toString()))
			{
				viewSentences.add("VIEW.IN.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
				viewSentences.add("VIEW.IN.SELF: on RENAME_PROVIDER then "+plcStr);
				viewSentences.add("VIEW.OUT.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
				querySentences.add("QUERY.IN.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
				querySentences.add("QUERY.IN.SELF: on RENAME_PROVIDER then "+plcStr);
				querySentences.add("QUERY.OUT.ATTRIBUTES: on RENAME_PROVIDER then "+plcStr);
			}
			else if(evtString.equals(EventType.ALTER_SEMANTICS.toString()))
			{
				viewSentences.add("VIEW.SMTX.SELF: on ALTER_SEMANTICS then "+plcStr);
				querySentences.add("QUERY.SMTX.SELF: on ALTER_SEMANTICS then "+plcStr);
			}
			else
			{
				throw new Exception("Input file is not in the correct format or empty file");
			}
		}
	}
	
	/**
	 * @author pmanousi
	 * Sets policy for a global query nodes containing * in event parameter of policy file.
	 * @param policyStringClause
	 * @throws HecataeusException
	 */
	private void parseQueryPolicy(String policyStringClause, ArrayList<String> querySentences) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		//get node on which policy is applied
		String currentWord = policyClause.nextToken(" ").trim(); 	// It is *
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type (actually it is *)
		currentWord = policyClause.nextToken().trim();
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
		querySentences.add("QUERY.OUT.SELF: on ADD_ATTRIBUTE then "+policyType.toString());
		querySentences.add("QUERY.OUT.SELF: on DELETE_SELF then "+policyType.toString());
		querySentences.add("QUERY.OUT.SELF: on RENAME_SELF then "+policyType.toString());
		querySentences.add("QUERY.OUT.ATTRIBUTES: on DELETE_SELF then "+policyType.toString());
		querySentences.add("QUERY.OUT.ATTRIBUTES: on RENAME_SELF then "+policyType.toString());
		querySentences.add("QUERY.OUT.ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.OUT.ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.IN.SELF: on DELETE_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.IN.SELF: on RENAME_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.IN.ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.IN.ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
		querySentences.add("QUERY.SMTX.SELF: on ALTER_SEMANTICS then "+policyType.toString());
	};
	
	/**
	 * @author pmanousi
	 * Sets policy for a global view nodes containing * in event parameter of policy file.
	 * @param policyStringClause
	 * @throws HecataeusException
	 */
	private void parseViewPolicy(String policyStringClause, ArrayList<String> viewSentences) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		//get node on which policy is applied
		String currentWord = policyClause.nextToken(" ").trim(); 	// It is *
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type (actually it is *)
		currentWord = policyClause.nextToken().trim();
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
		viewSentences.add("VIEW.OUT.SELF: on ADD_ATTRIBUTE then "+policyType.toString());
		viewSentences.add("VIEW.OUT.SELF: on DELETE_SELF then "+policyType.toString());
		viewSentences.add("VIEW.OUT.SELF: on RENAME_SELF then "+policyType.toString());
		viewSentences.add("VIEW.OUT.ATTRIBUTES: on DELETE_SELF then "+policyType.toString());
		viewSentences.add("VIEW.OUT.ATTRIBUTES: on RENAME_SELF then "+policyType.toString());
		viewSentences.add("VIEW.OUT.ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.OUT.ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.IN.SELF: on DELETE_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.IN.SELF: on RENAME_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.IN.ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.IN.ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
		viewSentences.add("VIEW.SMTX.SELF: on ALTER_SEMANTICS then "+policyType.toString());
	};
	
	/**
	 * @author pmanousi
	 * Sets policy for a global relation nodes containing * in event parameter of policy file.
	 * @param policyStringClause
	 * @throws HecataeusException
	 */
	private void parseRelationPolicy(String policyStringClause, ArrayList<String> relationSentences) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		//get node on which policy is applied
		String currentWord = policyClause.nextToken(" ").trim(); 	// It is *
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type (actually it is *)
		currentWord = policyClause.nextToken().trim();
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
		relationSentences.add("RELATION.OUT.SELF: on ADD_ATTRIBUTE then "+policyType.toString());
		relationSentences.add("RELATION.OUT.SELF: on DELETE_SELF then "+policyType.toString());
		relationSentences.add("RELATION.OUT.SELF: on RENAME_SELF then "+policyType.toString());
		relationSentences.add("RELATION.OUT.ATTRIBUTES: on DELETE_SELF then "+policyType.toString());
		relationSentences.add("RELATION.OUT.ATTRIBUTES: on RENAME_SELF then "+policyType.toString());
	};
	
	/**
	 * @author pmanousi
	 * Sets policy for a specific schema node containing * in event parameter of policy file.
	 * @param policyStringClause
	 * @throws HecataeusException
	 */
	private void parseModulePolicy(String policyStringClause, ArrayList<String> whateverSentences) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String nodeName = policyClause.nextToken(":").trim();
		//get node on which policy is applied
		EvolutionNode node;
		node = this.evoGraph.findVertexByName(nodeName,NodeCategory.MODULE);
		if (node==null)
			throw new HecataeusException("Unknown Node: " + nodeName);
		String currentWord = policyClause.nextToken(" ").trim();
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type (actually it is *)
		currentWord = policyClause.nextToken().trim();
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
		switch (node.getType()) {
		case NODE_TYPE_QUERY:
		case NODE_TYPE_VIEW:
			whateverSentences.add(0,node.getName()+"_OUT: on ADD_ATTRIBUTE then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_OUT: on DELETE_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_OUT: on RENAME_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_OUT.ATTRIBUTES: on DELETE_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_OUT.ATTRIBUTES: on RENAME_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_OUT.ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_OUT.ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_SMTX: on ALTER_SEMANTICS then "+policyType.toString());
			// This should be done foreach of the input schemata.
			for(int i=0;i<node.getOutEdges().size();i++)
			{
				if(node.getOutEdges().get(i).getType().equals(EdgeType.EDGE_TYPE_INPUT))
				{
					whateverSentences.add(0,node.getOutEdges().get(i).getToNode().getName()+": on DELETE_PROVIDER then "+policyType.toString());
					whateverSentences.add(0,node.getOutEdges().get(i).getToNode().getName()+": on RENAME_PROVIDER then "+policyType.toString());
					whateverSentences.add(0,node.getOutEdges().get(i).getToNode().getName()+": on ADD_ATTRIBUTE_PROVIDER then "+policyType.toString());
					whateverSentences.add(0,node.getOutEdges().get(i).getToNode().getName()+".ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
					whateverSentences.add(0,node.getOutEdges().get(i).getToNode().getName()+".ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
				}
			}
			break;
		case NODE_TYPE_RELATION:
			whateverSentences.add(0,node.getName()+"_SCHEMA: on ADD_ATTRIBUTE then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_SCHEMA: on DELETE_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_SCHEMA: on RENAME_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_SCHEMA.ATTRIBUTES: on DELETE_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+"_SCHEMA.ATTRIBUTES: on RENAME_SELF then "+policyType.toString());
			break;
		default:
			break;
		}
	};
	
	/**
	 * @author pmanousi
	 * Sets policy for a specific node containing * in event parameter of policy file.
	 * @param policyStringClause
	 * @throws HecataeusException
	 */
	private void parseAttributesPolicy(String policyStringClause, ArrayList<String> whateverSentences) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String nodeName = policyClause.nextToken(":").trim();
		//get node on which policy is applied
		EvolutionNode node = this.evoGraph.findVertexByName(nodeName,NodeCategory.SCHEMA);
		if (node==null)
			throw new HecataeusException("Unknown Node: " + nodeName);
		String currentWord = policyClause.nextToken(" ").trim();
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type (actually it is *)
		currentWord = policyClause.nextToken().trim();
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
		if(node.getParentNode() != null)
		{
			switch (node.getParentNode().getType()) {
			case NODE_TYPE_INPUT:
				whateverSentences.add(0,node.getName()+": on DELETE_PROVIDER then "+policyType.toString());
				whateverSentences.add(0,node.getName()+": on RENAME_PROVIDER then "+policyType.toString());
				break;
			case NODE_TYPE_OUTPUT:
				switch (node.getParentNode().getParentNode().getType()) {
				case NODE_TYPE_RELATION:
					whateverSentences.add(0,node.getName()+": on DELETE_SELF then "+policyType.toString());
					whateverSentences.add(0,node.getName()+": on RENAME_SELF then "+policyType.toString());
					break;
				default:
					whateverSentences.add(0,node.getName()+": on DELETE_SELF then "+policyType.toString());
					whateverSentences.add(0,node.getName()+": on RENAME_SELF then "+policyType.toString());
					whateverSentences.add(0,node.getName()+": on DELETE_PROVIDER then "+policyType.toString());
					whateverSentences.add(0,node.getName()+": on RENAME_PROVIDER then "+policyType.toString());
					break;
				}
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * @author pmanousi
	 * Sets policy for a specific module containing * in event parameter of policy file.
	 * @param policyStringClause
	 * @throws HecataeusException
	 */
	private void parseSchemaPolicy(String policyStringClause, ArrayList<String> whateverSentences) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String nodeName = policyClause.nextToken(":").trim();
		//get node on which policy is applied
		EvolutionNode node = this.evoGraph.findVertexByName(nodeName, NodeCategory.INOUTSCHEMA);
		if (node==null)
			throw new HecataeusException("Unknown Node: " + nodeName);
		String currentWord = policyClause.nextToken(" ").trim();
		currentWord = policyClause.nextToken(" ").trim();
		//Expect ON keyword
		if (!(currentWord.equalsIgnoreCase("ON"))){
			throw new HecataeusException( "Unknown Token: " + currentWord + "\nExpected: ON");
		}
		//Get event type (actually it is *)
		currentWord = policyClause.nextToken().trim();
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
		switch (node.getType()) {
		case NODE_TYPE_OUTPUT:
			whateverSentences.add(0,node.getName()+": on ADD_ATTRIBUTE then "+policyType.toString());
			whateverSentences.add(0,node.getName()+": on DELETE_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+": on RENAME_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+".ATTRIBUTES: on DELETE_SELF then "+policyType.toString());
			whateverSentences.add(0,node.getName()+".ATTRIBUTES: on RENAME_SELF then "+policyType.toString());
			if(node.getParentNode() != null)
			{
				if(node.getParentNode().getType().equals(NodeType.NODE_TYPE_RELATION)==false)
				{
					whateverSentences.add(0,node.getName()+".ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
					whateverSentences.add(0,node.getName()+".ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
				}
			}
		case NODE_TYPE_SEMANTICS:
			whateverSentences.add(0,node.getName()+": on ALTER_SEMANTICS then "+policyType.toString());
			// This should be done foreach of the input schemata.
			break;
		case NODE_TYPE_INPUT:
			whateverSentences.add(0,node.getName()+": on DELETE_PROVIDER then "+policyType.toString());
			whateverSentences.add(0,node.getName()+": on RENAME_PROVIDER then "+policyType.toString());
			whateverSentences.add(0,node.getName()+": on ADD_ATTRIBUTE_PROVIDER then "+policyType.toString());
			whateverSentences.add(0,node.getName()+".ATTRIBUTES: on DELETE_PROVIDER then "+policyType.toString());
			whateverSentences.add(0,node.getName()+".ATTRIBUTES: on RENAME_PROVIDER then "+policyType.toString());
			break;
		default:
			break;
		}
	};
	
	
	
	/**
	 * @author pmanousi
	 */
	private void parseNodeAttributesPolicy(String policyStringClause) throws HecataeusException {
		StringTokenizer policyClause = new StringTokenizer(policyStringClause);
		String nodeName = policyClause.nextToken(":").trim();	//get node on which policy is applied
		EvolutionNode node = this.getNodeByName(nodeName);
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
		EvolutionNode node;
		if(nodeName.contains("."))
			node = this.evoGraph.findVertexByNameParent(nodeName);
		else
			node = this.evoGraph.findVertexByName(nodeName);
		if (node==null)
			throw new HecataeusException("Unknown Node: " + nodeName);
		if (node.getType().getCategory().equals(NodeCategory.MODULE))
			throw new HecataeusException("Wrong type in policy rule"+policyStringClause);
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
	
	private EvolutionNode getNodeByName(String name) {
		//check if name is of the form TABLENAME.ATTRIBUTE
		String result[]= name.split("\\.");
		EvolutionNode node = this.evoGraph.findVertexByName(result[0].trim().toUpperCase());
		if (result.length>1) {
			for (EvolutionNode childNode : this.evoGraph.getModule(node)) {
				if (childNode.getName().equalsIgnoreCase(result[1].trim()))
					return childNode;
			}
		}
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


