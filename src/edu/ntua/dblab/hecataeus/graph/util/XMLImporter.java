package edu.ntua.dblab.hecataeus.graph.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public class XMLImporter {

	public XMLImporter() {
	}

	public EvolutionGraph importFromXML(File file) {
		final EvolutionGraph graph = new EvolutionGraph();

		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(file);

			doc.getDocumentElement().normalize();

			importNodes(graph, doc);

			importEdges(graph, doc);

			importAndSetKeyGenerator(graph, doc);

			return graph;
		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());
			return null;

		} catch (SAXException e) {
			e.printStackTrace();
			return null;

		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

	}

	private void importNodes(EvolutionGraph graph, Document doc) {
		String nKey = null;
		String nName = null;
		String nType = null;
		String nSQLDefinition = null;
		StatusType status = null;
		int nID = 0;

		NodeList listOfNodes = doc.getElementsByTagName("HNodes");
		Element NodesElement = (Element) listOfNodes.item(0);

		listOfNodes = NodesElement.getElementsByTagName("HNode");

		for (int s = 0; s < listOfNodes.getLength(); s++) {
			Node firstNode = listOfNodes.item(s);
			if (firstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element firstNodeElement = (Element) firstNode;

				//-------                    
				NodeList KeyList = firstNodeElement.getElementsByTagName("Key");
				Element KeyElement = (Element) KeyList.item(0);

				NodeList textKeyList = KeyElement.getChildNodes();
				nKey = ((Node) textKeyList.item(0)).getNodeValue();

				//-------                    
				NodeList NameList = firstNodeElement.getElementsByTagName("Name");
				Element NameElement = (Element) NameList.item(0);

				NodeList textNameList = NameElement.getChildNodes();
				nName = ((Node) textNameList.item(0)).getNodeValue();

				//-------                    
				NodeList StatusList = firstNodeElement.getElementsByTagName("Status");
				Element StatusElement = (Element) StatusList.item(0);

				NodeList textStatusList = StatusElement.getChildNodes();
				status = StatusType.toStatus(((Node) textStatusList.item(0)).getNodeValue());

				//----                    
				NodeList TypeList = firstNodeElement.getElementsByTagName("Type");
				Element TypeElement = (Element) TypeList.item(0);

				NodeList textTypeList = TypeElement.getChildNodes();
				nType = ((Node) textTypeList.item(0)).getNodeValue();

				//----                    
				NodeList SQLDefinitionList = firstNodeElement.getElementsByTagName("SQLDefinition");
				Element SQLDefinitionElement = (Element) SQLDefinitionList.item(0);
				if (SQLDefinitionElement != null) {
					NodeList textSQLDefinitionList = SQLDefinitionElement.getChildNodes();
					nSQLDefinition = ((Node) textSQLDefinitionList.item(0)).getNodeValue();
				} else
					nSQLDefinition = "";

				EvolutionNode v = new EvolutionNode();
				v.setName(nName);
				v.setType(NodeType.valueOf(nType));
				v.setStatus(status, true);

				v.setSQLDefinition(nSQLDefinition);
				graph.addVertex(v);
				//shift node key by current key
				graph.setNodeKey(v, EvolutionGraph.getKeyGenerator() + new Integer(nKey));
				v.setID(nID);

			} //end of if clause

		} //end of for loop for nodes
	}

	private void importEdges(EvolutionGraph graph, Document doc) {
		String eKey = null;
		String eName = null;
		String eType = null;
		String eFromNode = null;
		String eToNode = null;

		NodeList listOfEdges = doc.getElementsByTagName("HEdges");
		Element EdgesElement = (Element) listOfEdges.item(0);

		listOfEdges = EdgesElement.getElementsByTagName("HEdge");

		for (int s = 0; s < listOfEdges.getLength(); s++) {

			Node firstEdge = listOfEdges.item(s);
			if (firstEdge.getNodeType() == Node.ELEMENT_NODE) {
				Element firstEdgeElement = (Element) firstEdge;

				//-------                    
				NodeList KeyList = firstEdgeElement.getElementsByTagName("Key");
				Element KeyElement = (Element) KeyList.item(0);

				NodeList textKeyList = KeyElement.getChildNodes();
				eKey = ((Node) textKeyList.item(0)).getNodeValue();

				//-------                    
				NodeList NameList = firstEdgeElement.getElementsByTagName("Name");
				Element NameElement = (Element) NameList.item(0);

				NodeList textNameList = NameElement.getChildNodes();
				eName = ((Node) textNameList.item(0)).getNodeValue();

				//----                    
				NodeList TypeList = firstEdgeElement.getElementsByTagName("Type");
				Element TypeElement = (Element) TypeList.item(0);

				NodeList textTypeList = TypeElement.getChildNodes();
				eType = ((Node) textTypeList.item(0)).getNodeValue();

				NodeList FromNodeList = firstEdgeElement.getElementsByTagName("FromNode");
				Element FromNodeElement = (Element) FromNodeList.item(0);

				NodeList textFromNodeList = FromNodeElement.getChildNodes();
				eFromNode = ((Node) textFromNodeList.item(0)).getNodeValue();

				NodeList ToNodeList = firstEdgeElement.getElementsByTagName("ToNode");
				Element ToNodeElement = (Element) ToNodeList.item(0);

				NodeList textToNodeList = ToNodeElement.getChildNodes();
				eToNode = ((Node) textToNodeList.item(0)).getNodeValue();

				// add edge
				EvolutionEdge e = new EvolutionEdge(eName, EdgeType.toEdgeType(eType),
					graph.findVertex(EvolutionGraph.getKeyGenerator() + new Integer(eFromNode)),
					graph.findVertex(EvolutionGraph.getKeyGenerator() + new Integer(eToNode)));

				graph.addEdge(e);
				graph.setEdgeKey(e, EvolutionGraph.getKeyGenerator() + new Integer(eKey));

			} //end of if clause

		} //end of for loop for edges
	}

	private void importAndSetKeyGenerator(EvolutionGraph graph,
		Document doc) {
		String nKey = null;

		//get the last element - graph keygenerator
		NodeList keyGen = doc.getElementsByTagName("HKeyGen");
		Element keyGenElement = (Element) keyGen.item(0);

		NodeList textkeyGen = keyGenElement.getChildNodes();
		nKey = ((Node) textkeyGen.item(0)).getNodeValue();

		graph.setKeyGenerator(EvolutionGraph.getKeyGenerator() + new Integer(nKey));
	}

}