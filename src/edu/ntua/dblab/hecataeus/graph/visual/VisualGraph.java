/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.File;
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

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
//import edu.uci.ics.jung.graph.Vertex;

public class VisualGraph extends EvolutionGraph<VisualNode,VisualEdge>{
	protected VisualizationViewer<VisualNode, VisualEdge> myViewer;
    protected List<VisualNode> myNodes;
    
    /** 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VisualGraph() {
	}

	public VisualGraph(VisualGraph subGraph){
		File file = new File("eva");
		subGraph.exportToXML(file);
		this.importFromXML(file);
		file.delete();
	}
	
	
	public VisualGraph(VisualizationViewer<VisualNode, VisualEdge> viewer){
		this.myViewer = viewer;
		

	}
	
	public void setViewerToGraph(VisualizationViewer<VisualNode, VisualEdge> viewer){
		this.myViewer = viewer;
	}
	
	
	public VisualizationViewer<VisualNode, VisualEdge> getMyViewer(){
		return this.myViewer;
	}
	
	/**
	 * returns the dimension of the graph layout 
	 * The dimension of the graph is calculated as the Dimension(dx,dy), where 
	 * dx = distance between the left-most and the right-most node x coordinate
	 * dy = distance between the top-most and the bottom-most node y coordinate 
	 * @return the dimension of the graph
	 */
	public Dimension getSize(){
		return new Dimension(1200, 800);
//		if (this.getVertexCount()>0) {
//			
//			//initialize coords with first vertex location
//			
//			VisualNode v = new VisualNode();
//			
//			
//			
//			v = this.getVertices().get(0); 
////			VisualNode v = this.getVertices().get(0); 
////			myNodes.add(v);
//			double minX = this.getLocation(v).getX();
//			double minY = this.getLocation(v).getY();
//			double maxX = this.getLocation(v).getX();
//			double maxY = this.getLocation(v).getY();
//			
//			
//
//			for (VisualNode jungNode: this.getVertices()) {
//				if (jungNode.getVisible()) {
//					Point2D p = this.getLocation(jungNode);
//					minX=(minX>p.getX()?p.getX():minX);
//					minY=(minY>p.getY()?p.getY():minY);
//					maxX=(maxX<p.getX()?p.getX():maxX);
//					maxY=(maxY<p.getY()?p.getY():maxY);
//				}
//			}
//
//			/*return new Dimension((int)(maxX-minX),(int)(maxY-minY));*/
//		}
//		//else return default
//		return new Dimension(1200, 800);
		
	}

	/**
	 * returns the center of the graph layout as a Point 2D
	 * The center of the graph is calculated as the median 
	 * of x and y coordinates of all nodes
	 * @return the center of the graph
	 */
	public Point2D getCenter(){
		if (this.getVertexCount()>0) {
			
			//initialize coords with first vertex location
			VisualNode v = this.getVertices().get(0); 
			double minX = this.getLocation(v).getX();
			double minY = this.getLocation(v).getY();
			double maxX = this.getLocation(v).getX();
			double maxY = this.getLocation(v).getY();

			for (VisualNode jungNode: this.getVertices()) {
				if (jungNode.getVisible()) {
					Point2D p = this.getLocation(jungNode);
					minX=(minX>p.getX()?p.getX():minX);
					minY=(minY>p.getY()?p.getY():minY);
					maxX=(maxX<p.getX()?p.getX():maxX);
					maxY=(maxY<p.getY()?p.getY():maxY);
				}
			}

			return new Point2D.Double((maxX + minX)/2,(maxY+minY)/2);
		}
		//else return default
		return new Point2D.Double();
	} 
	
	public VisualGraph importFromXML(File file) {

		//holds the current key of the graph
		int currentKey = this.getKeyGenerator();
		myNodes = new ArrayList<VisualNode>();
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

					myNodes.add(v);
					
	            }//end of if clause
	
	        }//end of for loop for nodes
	
	        NodeList listOfEdges = doc.getElementsByTagName("HEdges");
	        Element EdgesElement = (Element)listOfEdges.item(0);
	    	
	        listOfEdges = EdgesElement.getElementsByTagName("HEdge");
	        
	        for(int s=0; s<listOfEdges.getLength() ; s++){
	
	            Node firstEdge = listOfEdges.item(s);
	            if(firstEdge.getNodeType() == Node.ELEMENT_NODE){
	                Element firstEdgeElement = (Element)firstEdge;
	
	                //-------                    
	                NodeList KeyList = firstEdgeElement.getElementsByTagName("Key");
	                Element KeyElement = (Element)KeyList.item(0);
	
	                NodeList textKeyList = KeyElement.getChildNodes();
	                eKey = ((Node)textKeyList.item(0)).getNodeValue();
	
	                //-------                    
	                NodeList NameList = firstEdgeElement.getElementsByTagName("Name");
	                Element NameElement = (Element)NameList.item(0);
	
	                NodeList textNameList = NameElement.getChildNodes();
	                eName = ((Node)textNameList.item(0)).getNodeValue();
	
	                //----                    
	                NodeList TypeList = firstEdgeElement.getElementsByTagName("Type");
	                Element TypeElement = (Element)TypeList.item(0);
	
	                NodeList textTypeList = TypeElement.getChildNodes();
	                eType = ((Node)textTypeList.item(0)).getNodeValue();
	                
	                NodeList FromNodeList = firstEdgeElement.getElementsByTagName("FromNode");
	                Element FromNodeElement = (Element)FromNodeList.item(0);
	
	                NodeList textFromNodeList = FromNodeElement.getChildNodes();
	                eFromNode = ((Node)textFromNodeList.item(0)).getNodeValue();
	           	                
	                NodeList ToNodeList = firstEdgeElement.getElementsByTagName("ToNode");
	                Element ToNodeElement = (Element)ToNodeList.item(0);
	
	                NodeList textToNodeList = ToNodeElement.getChildNodes();
	                eToNode = ((Node)textToNodeList.item(0)).getNodeValue();

	                // add edge
					VisualEdge e = new VisualEdge(eName,EdgeType.toEdgeType(eType), this.findVertex(currentKey+new Integer(eFromNode)), this.findVertex(currentKey+new Integer(eToNode)));
					this.addEdge(e);
					this.setKey(e,currentKey+new Integer(eKey));
					 
								
	            }//end of if clause
	
	        }//end of for loop for edges
	        
/*	        // find the policies (if any exists) for nodes
	        NodeList listOfPolicies = doc.getElementsByTagName("HPolicies");
	        Element PoliciesElement = (Element)listOfPolicies.item(0);
	    	
	        listOfPolicies = PoliciesElement.getElementsByTagName("HPolicy");
	        
    		if (listOfPolicies.getLength()>0) {
            	for(int i=0; i<listOfPolicies.getLength() ; i++){

            		Node firstPolicy = listOfPolicies.item(i);
            		if(firstPolicy.getNodeType() == Node.ELEMENT_NODE){

            			Element firstPolicyElement = (Element)firstPolicy;

            			//-------                    
            			NodeList HNodeList = firstPolicyElement.getElementsByTagName("HNode");
            			Element HNodeElement = (Element)HNodeList.item(0);

            			NodeList textHNodeList = HNodeElement.getChildNodes();
            			String nHNode = ((Node)textHNodeList.item(0)).getNodeValue();

            			//-------                    
            			NodeList HEventList = firstPolicyElement.getElementsByTagName("HEvent");
            			Element HEventElement = (Element)HEventList.item(0);

            			NodeList HEventNodeList = HEventElement.getElementsByTagName("HEventNode");
            			Element HEventNodeElement = (Element)HEventNodeList.item(0);

            			NodeList textHEventNodeList = HEventNodeElement.getChildNodes();
            			String nHEventNode = ((Node)textHEventNodeList.item(0)).getNodeValue();
            			
            			NodeList HEventTypeList = HEventElement.getElementsByTagName("HEventType");
            			Element HEventTypeElement = (Element)HEventTypeList.item(0);

            			NodeList textHEventTypeList = HEventTypeElement.getChildNodes();
            			String nHEventType = ((Node)textHEventTypeList.item(0)).getNodeValue();
            
            			//----                    
            			NodeList HPolicyTypeList = firstPolicyElement.getElementsByTagName("HPolicyType");
            			Element HPolicyTypeElement = (Element)HPolicyTypeList.item(0);

            			NodeList textHPolicyTypeList = HPolicyTypeElement.getChildNodes();
            			String nHPolicyType = ((Node)textHPolicyTypeList.item(0)).getNodeValue();

            			VisualNode HNode = this.findVertex(currentKey+new Integer(nHNode));
            			VisualNode HEventNode = this.findVertex(currentKey+new Integer(nHEventNode));
            			EventType HEventType = EventType.toEventType(nHEventType);
            			PolicyType HPolicyType = PolicyType.toPolicyType(nHPolicyType);

            			HNode.addPolicy(HEventType, HEventNode, HPolicyType);
            		}//end of if clause

            	}//end of for loop for policies

            }//end of if policies exist
    		
    		//get events
	        NodeList listOfEvents = doc.getElementsByTagName("HEvents");
	        Element EventsElement = (Element)listOfEvents.item(0);
	    	
	        listOfEvents = EventsElement.getElementsByTagName("HEvent");
			
            if (listOfEvents.getLength()>0) {
            	for(int i=0; i<listOfEvents.getLength() ; i++){
            		Node firstEvent = listOfEvents.item(i);
            		if(firstEvent.getNodeType() == Node.ELEMENT_NODE){
            			Element firstEventElement = (Element)firstEvent;

            			//-------                    
            			NodeList HNodeList = firstEventElement.getElementsByTagName("HNode");
            			Element HNodeElement = (Element)HNodeList.item(0);

            			NodeList textHNodeList = HNodeElement.getChildNodes();
            			String nHNode = ((Node)textHNodeList.item(0)).getNodeValue();

            			//-------                    
            			NodeList HEventNodeList = firstEventElement.getElementsByTagName("HEventNode");
            			Element HEventNodeElement = (Element)HEventNodeList.item(0);

            			NodeList textHEventNodeList = HEventNodeElement.getChildNodes();
            			String nHEventNode = ((Node)textHEventNodeList.item(0)).getNodeValue();
            			
            			//-------                    
            			NodeList HEventTypeList = firstEventElement.getElementsByTagName("HEventType");
            			Element HEventTypeElement = (Element)HEventTypeList.item(0);

            			NodeList textHEventTypeList = HEventTypeElement.getChildNodes();
            			String nHEventType = ((Node)textHEventTypeList.item(0)).getNodeValue();
            			
            			VisualNode HNode = this.findVertex(currentKey+new Integer(nHNode));
            			VisualNode HEventNode = this.findVertex(currentKey+new Integer(nHEventNode));
            			EventType HEventType = EventType.toEventType(nHEventType);
            			
            			HNode.addEvent(HEventType);            			
            		}
            	}//end of for loop for events
            }//end of if events exist
*/
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
	
	public VisualGraph toGraph(List<VisualNode> nodes){
		VisualGraph subGraph = super.toGraphE(nodes);
		for (VisualNode v : nodes)
			subGraph.setLocation(v, this.getLocation(v)); 
		return subGraph;
	}
	
//	public Graph toG(VisualGraph inGraph){
//		return inGraph;
//	}
	
	
	
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
				
				
/*				// write policies
				Element elementHPolicies = document.createElement("HPolicies");
				// write events
				Element elementHEvents = document.createElement("HEvents");
				
				for (VisualNode v : this.getVertices()) {
					for (EvolutionPolicy<VisualNode> p: v.getPolicies()) {
						//start tag HPolicy
						Element elementHPolicy = document.createElement("HPolicy");
						elementHPolicies.appendChild(elementHPolicy);
						//write Node having the policy
						Element elementHNode = document.createElement("HNode");
						elementHNode.appendChild(document.createTextNode(this.getKey(v).toString()));
						elementHPolicy.appendChild(elementHNode);
						//write event handled by the policy
						Element elementEvent = document.createElement("HEvent");
						elementHPolicy.appendChild(elementEvent);
						
						Element elementEventNode = document.createElement("HEventNode");
						elementEventNode.appendChild(document.createTextNode(this.getKey(p.getSourceEvent().getEventNode()).toString()));
						elementEvent.appendChild(elementEventNode);
						
						Element elementEventType = document.createElement("HEventType");
						elementEventType.appendChild(document.createTextNode(p.getSourceEvent().getEventType().toString()));
						elementEvent.appendChild(elementEventType);
						//write policy type
						Element elementPolicyType = document.createElement("HPolicyType");
						elementPolicyType.appendChild(document.createTextNode(p.getPolicyType().toString()));
						elementHPolicy.appendChild(elementPolicyType);
						// end element HPolicy
					}
					for (EvolutionEvent<VisualNode> e : v.getEvents()) {
						//write element event
						Element elementHEvent = document.createElement("HEvent");
						elementHEvents.appendChild(elementHEvent);
						//write Node having the event
						Element elementHNode = document.createElement("HNode");
						elementHNode.appendChild(document.createTextNode(this.getKey(v).toString()));
						elementHEvent.appendChild(elementHNode);
						
						Element elementEventNode = document.createElement("HEventNode");
						elementEventNode.appendChild(document.createTextNode(this.getKey(e.getEventNode()).toString()));
						elementHEvent.appendChild(elementEventNode);
						Element elementEventType = document.createElement("HEventType");
						elementEventType.appendChild(document.createTextNode(e.getEventType().toString()));
						elementHEvent.appendChild(elementEventType);
						// end element HEvent
						
					}
				}
				rootElement.appendChild(elementHPolicies);
				rootElement.appendChild(elementHEvents);
			
	*/
				//
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
    
    public List<VisualNode> getNodes(){
    	return myNodes;
    }
	
}
