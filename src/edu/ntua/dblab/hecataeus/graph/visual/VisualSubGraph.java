/**
 * 
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.ntua.dblab.hecataeus.HecataeusPopupGraphMousePlugin;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;

/**
 * @author eva
 *
 */
public class VisualSubGraph extends VisualGraph{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public VisualGraph graph;
	public VisualNode clickedVertex;
	protected HecataeusViewer viewer;
	protected HecataeusPopupGraphMousePlugin plug;
	HecataeusViewer v;
	public VisualSubGraph(HecataeusViewer viewer){
		this.v = viewer;
	}
	/**
	 * 
	 */
	public VisualSubGraph() {
		// TODO Auto-generated constructor stub
	}

	
	/*   
	 * 
	 *  
	 *  get the sub graph and mpla mpla    */
	
	
	public void createSubGraph(){
		this.plug = new HecataeusPopupGraphMousePlugin();
		List<VisualNode> nodes = new ArrayList<VisualNode>();
		for (VisualNode node : this.getVertices()) {
			if(node.getType() == NodeType.NODE_TYPE_PACKAGE){
				String test = node.getName();
				String myName = plug.getCursor().getName();
				
			}
		}
	}
	
	
	public VisualSubGraph getSubGraphs(){
		
		this.plug = new HecataeusPopupGraphMousePlugin();
		String nodeName;
		VisualNode node;

//		this.viewer.epilegmenosKombos = plug.getclicked();
//		node = plug.getclicked();
		if(this.plug==null){
			System.out.println("einai null to plug");
		}
//		HecataeusPopupGraphMousePlugin.getclicked();
//		node = HecataeusPopupGraphMousePlugin.getclicked();
		if(node==null){
			System.out.println("einai null");
		}
		System.out.println(node.getName());

	//	graph.degree(vertex);

		nodeName = node.getName();
		dsubGraph(node);
		
		return this;
	}
	
	public void dsubGraph(VisualNode node){
		List<VisualEdge> nodes = node.getOutEdges();
//		VisualGraph g = HecataeusPopupGraphMousePlugin.graph;
		plug = new HecataeusPopupGraphMousePlugin();
		VisualNode myNode = plug.getClickedVertex();
		List<VisualNode> module = g.getModule(node);
		
		
		for(int i = 0; i < nodes.size(); i++){
			//node.setVisible(false);
			nodes.get(i).getToNode().setVisible(false);
		}
		for (VisualNode child : module) {
			child.setVisible(!child.getVisible());
			child.setVisible(true);
		}
		node.setVisible(false);
	}
	
	public VisualSubGraph importFromXML(File file) {

		//holds the current key of the graph
		int currentKey = this.getKeyGenerator();

		String nKey = null;
		String nName = null;
		String nType = null;
		int nID=0;
		String eKey = null;
		String eName = null;
		String eType = null;
		String eFromNode = null;
		String eToNode = null;
		String nSQLDefinition = null;
				
		try {
	
	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse (file);
	
	        // normalize text representation            
	        doc.getDocumentElement ().normalize ();
	        
	        NodeList listOfNodes = doc.getElementsByTagName("HNodes");
	        Element NodesElement = (Element)listOfNodes.item(0);
	    	
	        listOfNodes = NodesElement.getElementsByTagName("HNode");
	
	        for(int s=0; s<listOfNodes.getLength() ; s++){
	        	Node firstNode = listOfNodes.item(s);
	            if(firstNode.getNodeType() == Node.ELEMENT_NODE){
	
	            	Element firstNodeElement = (Element)firstNode;
	                double nodeX = new Double(firstNodeElement.getAttribute("x"));
	                double nodeY = new Double(firstNodeElement.getAttribute("y"));
	                
	                //-------                    
	                NodeList KeyList = firstNodeElement.getElementsByTagName("Key");
	                Element KeyElement = (Element)KeyList.item(0);
	
	                NodeList textKeyList = KeyElement.getChildNodes();
	                nKey = ((Node)textKeyList.item(0)).getNodeValue();
		            
	                //-------                    
	                NodeList NameList = firstNodeElement.getElementsByTagName("Name");
	                Element NameElement = (Element)NameList.item(0);
	
	                NodeList textNameList = NameElement.getChildNodes();
	                nName = ((Node)textNameList.item(0)).getNodeValue();
	
	                //----                    
	                NodeList TypeList = firstNodeElement.getElementsByTagName("Type");
	                Element TypeElement = (Element)TypeList.item(0);
	
	                NodeList textTypeList = TypeElement.getChildNodes();
	                nType = ((Node)textTypeList.item(0)).getNodeValue();
/** @author pmanousi ID NOW NOT NEEDED (TopologicalTravel) */
//NodeList IDList = firstNodeElement.getElementsByTagName("ID");
//Element IDElement = (Element)IDList.item(0);

//NodeList textIDList = IDElement.getChildNodes();
//nID = Integer.parseInt(((Node)textIDList.item(0)).getNodeValue());

	                //----                    
	                NodeList SQLDefinitionList = firstNodeElement.getElementsByTagName("SQLDefinition");
	                Element SQLDefinitionElement = (Element)SQLDefinitionList.item(0);
	                if (SQLDefinitionElement!=null){
	                	NodeList textSQLDefinitionList = SQLDefinitionElement.getChildNodes();
	                	nSQLDefinition = ((Node)textSQLDefinitionList.item(0)).getNodeValue();
	                }else
	                	nSQLDefinition="";


					// add node
					VisualNode v = new VisualNode();
					v.setName(nName);
					v.setType(NodeType.valueOf(nType));
//					v.setLocation(new Point2D.Double(nodeX,nodeY));
					this.setLocation(v,new Point2D.Double(nodeX,nodeY));
					v.setSQLDefinition(nSQLDefinition);
					this.addVertex(v);
					//shift node key by current key
					this.setKey(v,currentKey+new Integer(nKey));
					v.ID=nID;

					
					
	            }//end of if clause
	
	        }//end of for loop for nodes
	
	        NodeList listOfEdges = doc.getElementsByTagName("HEdges");
	        Element EdgesElement = (Element)listOfEdges.item(0);
	    	
	        listOfEdges = EdgesElement.getElementsByTagName("HEdge");
	        

            //get the last element - graph keygenerator
            NodeList keyGen = doc.getElementsByTagName("HKeyGen");
            Element keyGenElement = (Element)keyGen.item(0);

            NodeList textkeyGen = keyGenElement.getChildNodes();
            nKey = ((Node)textkeyGen.item(0)).getNodeValue();
            //	set the key 
            this.setKeyGenerator(currentKey+new Integer(nKey));
            return this; 

		}catch (SAXParseException err) {
	    System.out.println ("** Parsing error" + ", line " 
	         + err.getLineNumber () + ", uri " + err.getSystemId ());
	    System.out.println(" " + err.getMessage ());
	    return null;
	
	    }catch (SAXException e) {
	    Exception x = e.getException ();
	    ((x == null) ? e : x).printStackTrace ();
	    return null;
	
	    }catch (Throwable t) {
	    t.printStackTrace ();
	    return null;
	    }
	
	}
	
	//hold the location of each node in the graph
	protected Map<VisualNode, Point2D> nodeLocations = new HashMap<VisualNode, Point2D>();
	
	public void setLocation(VisualNode v, Point2D location){
		this.nodeLocations.put(v,location);
	}
	
	/**
	 * @return  the node location
	 */
	public Point2D getLocation(VisualNode v){
		return this.nodeLocations.get(v); 
	}
	
	public VisualSubGraph toGraph(List<VisualNode> nodes){
		VisualSubGraph subGraph = super.toGraphE(nodes);
		for (VisualNode v : nodes)
			subGraph.setLocation(v, this.getLocation(v)); 
		return subGraph;
	}
	
	public void exportPoliciesToFile(File file) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (VisualNode v : this.getVertices()){
				for (EvolutionPolicy<VisualNode> p : v.getPolicies()) {
					out.write(v + ": " + p.toString() + ";");
					out.newLine();
				}	
			}
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void exportToXML(File file) {
		
		
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("HGraph");
				document.appendChild(rootElement);
				
				Element elementHnodes = document.createElement("HNodes");
				Element elementHedges = document.createElement("HEdges");
				
				// write nodes
				for (VisualNode v : this.getVertices()) {
					// write HNode
					Element elementHnode = document.createElement("HNode");
					
					elementHnode.setAttribute("x", new Double(this.getLocation(v).getX()).toString());
					elementHnode.setAttribute("y", new Double(this.getLocation(v).getY()).toString());
					elementHnodes.appendChild(elementHnode);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(this.getKey(v).toString()));
					elementHnode.appendChild(elementKey);
					// write element name
					Element elementName = document.createElement("Name");
					elementName.appendChild(document.createTextNode(v.getName()));
					elementHnode.appendChild(elementName);
					// write element type
					Element elementType = document.createElement("Type");
					elementType.appendChild(document.createTextNode(v.getType().toString()));
					elementHnode.appendChild(elementType);
/** @author pmanousi write element key NOW IT IS NOT NEEDED (TopologicalTravel). */
//Element elementID = document.createElement("ID");
//elementID.appendChild(document.createTextNode(String.valueOf(v.ID)));
//elementHnode.appendChild(elementID);
					// write element SQL Definition
					if (!v.getSQLDefinition().isEmpty())
					{
						Element elementSQLDefinition = document.createElement("SQLDefinition");
						elementSQLDefinition.appendChild(document.createTextNode(v.getSQLDefinition()));
						elementHnode.appendChild(elementSQLDefinition);
					}
				}
				rootElement.appendChild(elementHnodes);
				
				// write edges
				for (VisualEdge e: this.getEdges()) {
					// write HEdge
					Element elementHedge = document.createElement("HEdge");
					elementHedges.appendChild(elementHedge);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(this.getKey(e).toString()));
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
					elementFromNode.appendChild(document.createTextNode(this.getKey(e.getFromNode()).toString()));
					elementHedge.appendChild(elementFromNode);
					// write element toNode
					Element elementToNode = document.createElement("ToNode");
					elementToNode.appendChild(document.createTextNode(this.getKey(e.getToNode()).toString()));
					elementHedge.appendChild(elementToNode);
					// end element HEdge
				}
				rootElement.appendChild(elementHedges);

				Element elementHKeyGen = document.createElement("HKeyGen");
				elementHKeyGen.appendChild(document.createTextNode((new Integer(this.getKeyGenerator()).toString())));
				rootElement.appendChild(elementHKeyGen);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(document);
		        StreamResult result =  new StreamResult(file);
		        transformer.transform(source, result);
				
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				
			}
			 catch (TransformerConfigurationException e) {
				e.printStackTrace();
				
			}
			 catch (TransformerException e) {
				e.printStackTrace();
				
			}
		
	}

	public List<VisualEdge> getInEdges(VisualNode vertex)
    {
        return new ArrayList<VisualEdge>(super.getInEdges(vertex));
    }

    public List<VisualEdge> getOutEdges(VisualNode vertex)
    {
    	return new ArrayList<VisualEdge>(super.getOutEdges(vertex));
    }

    public List<VisualNode> getVertices()
    {
        return new ArrayList<VisualNode>(super.getVertices());
    }

    public List<VisualEdge> getEdges()
    {
    	return new ArrayList<VisualEdge>(super.getEdges());
    }
}
