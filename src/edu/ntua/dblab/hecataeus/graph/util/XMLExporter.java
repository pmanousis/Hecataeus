package edu.ntua.dblab.hecataeus.graph.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;

public class XMLExporter {

	public XMLExporter() {
	}

	public void exportToXML(File file, EvolutionGraph graph) {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("HGraph");
			document.appendChild(rootElement);

			exportNodes(graph, document, rootElement);

			exportEdges(graph, document, rootElement);

			Element elementHKeyGen = document.createElement("HKeyGen");
			elementHKeyGen.appendChild(document.createTextNode((new Integer(EvolutionGraph.getKeyGenerator()).toString())));
			rootElement.appendChild(elementHKeyGen);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();

		} catch (TransformerException e) {
			e.printStackTrace();

		}

	}

	private void exportNodes(EvolutionGraph graph,
		Document document,
		Element rootElement) {
		Element elementHnodes = document.createElement("HNodes");

		// write nodes
		for (EvolutionNode v : graph.getVertices()) {
			// write HNode
			Element elementHnode = document.createElement("HNode");
			//TODO: we need that? elementHnodes.appendChild(elementHnode);

			// write element key
			Element elementKey = document.createElement("Key");
			elementKey.appendChild(document.createTextNode(graph.getNodeKey(v).toString()));
			elementHnode.appendChild(elementKey);
			// write element name
			Element elementName = document.createElement("Name");
			elementName.appendChild(document.createTextNode(v.getName()));
			elementHnode.appendChild(elementName);

			// write element status
			Element elementStatus = document.createElement("Status");
			elementStatus.appendChild(document.createTextNode(v.getStatus().toString()));
			elementHnode.appendChild(elementStatus);

			// write element type
			Element elementType = document.createElement("Type");
			elementType.appendChild(document.createTextNode(v.getType().toString()));
			elementHnode.appendChild(elementType);

			if (!v.getSQLDefinition().isEmpty()) {
				Element elementSQLDefinition = document.createElement("SQLDefinition");
				elementSQLDefinition.appendChild(document.createTextNode(v.getSQLDefinition()));
				elementHnode.appendChild(elementSQLDefinition);
			}
		}
		rootElement.appendChild(elementHnodes);
	}

	private void exportEdges(EvolutionGraph graph,
		Document document,
		Element rootElement) {
		Element elementHedges = document.createElement("HEdges");
		// write edges
		for (EvolutionEdge e : graph.getEdges()) {
			// write HEdge
			Element elementHedge = document.createElement("HEdge");
			elementHedges.appendChild(elementHedge);
			// write element key
			Element elementKey = document.createElement("Key");
			elementKey.appendChild(document.createTextNode(graph.getEdgeKey(e).toString()));
			elementHedge.appendChild(elementKey);
			// write element name
			Element elementName = document.createElement("Name");
			elementName.appendChild(document.createTextNode(e.getName()));
			elementHedge.appendChild(elementName);
			// write element type
			Element elementType = document.createElement("Type");
			elementType.appendChild(document.createTextNode(e.getType().ToString()));
			elementHedge.appendChild(elementType);
			// write element fromNode
			Element elementFromNode = document.createElement("FromNode");
			elementFromNode.appendChild(
				document.createTextNode(graph.getNodeKey((EvolutionNode) e.getFromNode()).toString()));
			elementHedge.appendChild(elementFromNode);
			// write element toNode
			Element elementToNode = document.createElement("ToNode");
			elementToNode.appendChild(
				document.createTextNode(graph.getNodeKey((EvolutionNode) e.getToNode()).toString()));
			elementHedge.appendChild(elementToNode);
			// end element HEdge
		}
		rootElement.appendChild(elementHedges);

	}

}
