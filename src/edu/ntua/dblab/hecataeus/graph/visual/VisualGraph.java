/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus.graph.visual;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;

//import edu.uci.ics.jung.graph.Vertex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import java.awt.Dimension;
import java.awt.geom.Point2D;

public class VisualGraph extends EvolutionGraph<VisualNode,VisualEdge>{
	
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VisualGraph() {
    }
   
	 
//	/**
//	*  gets the EvolutionGraph and visualizes it
//	*  
//	**/
//	public void setHierarchicalLayout(){
//		
//		/*
//		 * @param graphLastPosition = the start location of the graph
//		 */
//		Point2D initialLocation = new Point2D.Double(1000,0);
//		/*
//		 * @param relationOFFSET = offset from the last vertex of the relation tree
//		 */
//		final Point2D relationOFFSET = new Point2D.Double(0,50);
//		//holds the nodes that have been located, they must be drawn once
//		List<VisualNode> nodesLocated = new ArrayList<VisualNode>();
//		for (VisualNode relationNode : this.getVertices()) {
//			if (relationNode.getType()==NodeType.NODE_TYPE_RELATION) {
//				//set relation node location
//				relationNode.setLocation(initialLocation.getX(),initialLocation.getY());
//				nodesLocated.add(relationNode);
//				//draw the relation tree
//				initialLocation = this.drawHierarchical(relationNode,nodesLocated);
//				//set the location of the next relation
//				initialLocation.setLocation(initialLocation.getX()+relationOFFSET.getX(), initialLocation.getY()+relationOFFSET.getY());
//				
//			}
//		}
//		
//	}
//	
//	/**
//	*   For each relation draws the whole hierarchical JungGraph 
//	**/
//	private Point2D drawHierarchical(VisualNode parentNode, List<VisualNode> nodesLocated){
//
//		/*
//		 * OFFSET 
//		 */
//		final Point2D OFFSET = new Point2D.Double(-100,0);
//
//		/*
//		 * holds the last position of child nodes (attribute, operand,etc.) 
//		 * of each relation,query, view
//		 */
//		Point2D childGraphLocation = parentNode.getLocation();
//		
//		/*
//		 * holds the position of the last query or view drawn
//		 */
//		Point2D treeGraphLocation = parentNode.getLocation();
//
//		/*
//		 * draw the subgraph of the relation,query,view
//		 */
//		childGraphLocation = this.draw_subGraph(parentNode, nodesLocated);
//
//		//get each query, view dependent on the relation
//		for(VisualEdge edge: this.getInEdges(parentNode)){
//			if (edge.getType()==EdgeType.EDGE_TYPE_FROM){
//				VisualNode dependentNode = (VisualNode)edge.getFromNode();
//				//if it has not been located
//				if (!nodesLocated.contains(dependentNode)){
//					//set location of the dependent node
//					dependentNode.setLocation(parentNode.getLocation().getX() + OFFSET.getX(), treeGraphLocation.getY() + OFFSET.getY());
//					nodesLocated.add(dependentNode);
//					//draw its graph
//					treeGraphLocation = this.drawHierarchical(dependentNode, nodesLocated);
//				}
//			}
//		}
//		//return the position with the longest distance Y from the parent node
//		return (treeGraphLocation.getY()> childGraphLocation.getY()?treeGraphLocation:childGraphLocation);
//		
//	
//	}
//	
//	/**
//	*  gets the EvolutionGraph and visualizes it
//	*  
//	**/
//	public void setTreeLayout(){
//		
//		/*
//		 * @param initialPosition = the start location of the graph
//		 */
//		Point2D initialLocation = new Point2D.Double(800,200);
//				
//		/*
//		 * @param relationOFFSET = offset from the last vertex of the relation tree
//		 */
//		final Point2D relationOFFSET = new Point2D.Double(0,100);
//		//holds the nodes that have been located, they must be drawn once
//		List<VisualNode> nodesLocated = new ArrayList<VisualNode>();
//		for (VisualNode relationNode: this.getVertices()) {
//			if (relationNode.getType()==NodeType.NODE_TYPE_RELATION) {
//				//draw the relation tree
//				initialLocation = this.drawTree(relationNode,initialLocation, nodesLocated);
//				//set the location of the next relation
//				initialLocation.setLocation(initialLocation.getX()+relationOFFSET.getX(), relationNode.getLastChildLocation().getY()+relationOFFSET.getY());
//				
//			}
//		}
//		 
//	}
//	
//	/**
//	*   For each relation draws the whole tree JungGraph 
//	 * @param startLocation
//	**/
//	private Point2D drawTree(VisualNode curNode, Point2D startLocation, List<VisualNode> nodesLocated){
//
//		/*
//		 * OFFSET 
//		 */
//		final Point2D OFFSET = new Point2D.Double(-300,100);
//
//		/*
//		 * holds the location of the first child in the parent node's subgraph
//		 */
//		Point2D firstChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());
//		
//		/*
//		 * holds the location of the last child in the parent node's subgraph
//		 */
//		Point2D lastChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());
//		
//		/*
//		 * holds the location of the current child drawn
//		 */
//		Point2D curChildLocation = new Point2D.Double(startLocation.getX()+OFFSET.getX(),startLocation.getY());				
//		
//		//holds the number of childs parsed
//		int k=0;
//		//get each query, view dependent on the relation
//		for(VisualEdge edge: this.getInEdges(curNode)){
//			if (edge.getType()==EdgeType.EDGE_TYPE_FROM){
//				VisualNode dependentNode = (VisualNode) this.findVertex(edge.getFromNode().getKey());
//				//if it has not been located
//				if (!nodesLocated.contains(dependentNode)){
//					k++;
//					//draw its graph
//					curChildLocation = this.drawTree(dependentNode, curChildLocation, nodesLocated);
//					if (k==1)
//						firstChildLocation.setLocation(dependentNode.getLocation().getX(),dependentNode.getLocation().getY());
//					//reset the location of the next child node, hold the same x for all child node in the same level
//					curChildLocation.setLocation(dependentNode.getLocation().getX(), dependentNode.getLastChildLocation().getY()+OFFSET.getY());
//					//set the current child node location
//					lastChildLocation.setLocation(dependentNode.getLocation().getX(),dependentNode.getLocation().getY());
//				}
//			}
//		}
//		
//		//set relation node location
//		curNode.setLocation(startLocation.getX(),(firstChildLocation.getY()+lastChildLocation.getY())/2);
//		nodesLocated.add(curNode);
//		
//		/*
//		 * draw the subgraph of the relation,query,view
//		 */
//		this.draw_subGraph(curNode, nodesLocated);
//		//return the location of the node
//		return curNode.getLocation();
//	
//	}
//	
//	/**
//	*   draws the JungGraph Subgraph given a parentNode (relation, query, view)
//	**/
//	private Point2D draw_subGraph(VisualNode parentNode, List<VisualNode> nodesLocated){
//		
//
//		final Double xConditionOffset = new Double(0);
//		final Double yConditionOffset = new Double(10);
//		final Double xAttributeOffset = new Double(0);
//		final Double yAttributeOffset = new Double(10);
//		final Double xConstantOffset = new Double(-5);
//		final Double yConstantOffset = new Double(5);
//		final Double xGroupByOffset = new Double(-10);
//		final Double yGroupByOffset = new Double(20);
//		final Double xOperandOffset = new Double(-20);		
//		final Double yOperandOffset = new Double(10);
//		final Double xFunctionOffset = new Double(-20);		
//		final Double yFunctionOffset = new Double(10);
//		
//		// initial locations of nodes
//		Double xCondition = parentNode.getLocation().getX()+30;
//		Double yCondition = parentNode.getLocation().getY();
//		Double xAttribute = parentNode.getLocation().getX()-50;
//		Double yAttribute = parentNode.getLocation().getY()+50;
//		Double xGroupBy = new Double(-50);
//		Double yGroupBy = new Double(30);
//		Double xOperand = parentNode.getLocation().getX();
//		Double yOperand = parentNode.getLocation().getY();
//		Double xFunction = new Double(-50);		
//		Double yFunction = new Double(10);
//		Double xConstant = parentNode.getLocation().getX();	
//		Double yConstant = parentNode.getLocation().getY()+50;		
//		
//		//holds position of lastNode 
//		Point2D returnLocation = new Point2D.Double(parentNode.getLocation().getX(),parentNode.getLocation().getY());
//		//for each query, relation, view get the subgraph
//		List<VisualNode> subGraph = this.getModule(parentNode);
//		//get each VisualNode after the parentNode s>=1
//		for(VisualNode v : subGraph){
//			if (!(v==null)&&!nodesLocated.contains(v)){
//				if (v.getType()==NodeType.NODE_TYPE_ATTRIBUTE) {
//					xAttribute = xAttribute + xAttributeOffset;
//					yAttribute = yAttribute + yAttributeOffset;
//					v.setLocation(xAttribute,yAttribute);
//				}else if (v.getType()==NodeType.NODE_TYPE_CONDITION){
//					xCondition = xCondition + xConditionOffset;
//					yCondition = yCondition + yConditionOffset;
//					v.setLocation(xCondition,yCondition);
//				}else if (v.getType()==NodeType.NODE_TYPE_OPERAND){
//					//get first incident node
//					VisualEdge incidentEdge = v.getInEdges().get(0);
//					VisualNode incidentNode = incidentEdge.getFromNode();
//					if (incidentEdge.getName().equals("op1")){
//						xOperand = incidentNode.getLocation().getX()+ xOperandOffset;
//						yOperand = incidentNode.getLocation().getY()+ yOperandOffset;
//					}else if (incidentEdge.getName().equals("op2")){
//						xOperand = incidentNode.getLocation().getX()+ xOperandOffset;
//						yOperand = incidentNode.getLocation().getY()- yOperandOffset;
//					}else{
//						xOperand = incidentNode.getLocation().getX()+ xOperandOffset;
//						yOperand = incidentNode.getLocation().getY()+ yOperandOffset;
//					}
//					v.setLocation(xOperand,yOperand);
//				}else if (v.getType()==NodeType.NODE_TYPE_GROUP_BY){
//					VisualNode incidentNode = v.getInEdges().get(0).getFromNode();
//					xGroupBy = incidentNode.getLocation().getX()+xGroupByOffset;
//					yGroupBy = incidentNode.getLocation().getY()+yGroupByOffset;
//					v.setLocation(xGroupBy,yGroupBy);
//				}else if (v.getType()==NodeType.NODE_TYPE_CONSTANT){
//					VisualNode incidentNode = v.getInEdges().get(0).getFromNode();
//					xConstant = incidentNode.getLocation().getX()+ xConstantOffset;
//					yConstant = incidentNode.getLocation().getY()+ yConstantOffset;
//					v.setLocation(xConstant,yConstant);
//				}else if (v.getType()==NodeType.NODE_TYPE_FUNCTION){
//					VisualNode incidentNode = v.getInEdges().get(0).getFromNode();
//					xFunction = incidentNode.getLocation().getX()+xFunctionOffset;
//					yFunction = incidentNode.getLocation().getY()+yFunctionOffset;
//					v.setLocation(xFunction,yFunction);
//				}
//				nodesLocated.add(v);
//				returnLocation =(returnLocation.getY()>v.getLocation().getY()?returnLocation:v.getLocation());
//				
//			}
//    	}
//		parentNode.setLastChildLocation(returnLocation.getX(), returnLocation.getY());
//		return parentNode.getLastChildLocation();
//    	
//    }
	
	/**
	 * returns the dimension of the graph layout 
	 * The dimension of the graph is calculated as the Dimension(dx,dy), where 
	 * dx = distance between the left-most and the right-most node x coordinate
	 * dy = distance between the top-most and the bottom-most node y coordinate 
	 * @return the dimension of the graph
	 */
	public Dimension getSize(){
		if (this.getVertexCount()>0) {
			
			//initialize coords with first vertex location
			double minX=this.getVertices().get(0).getLocation().getX();
			double minY=this.getVertices().get(0).getLocation().getY();
			double maxX=this.getVertices().get(0).getLocation().getX();
			double maxY=this.getVertices().get(0).getLocation().getY();

			for (VisualNode jungNode: this.getVertices()) {
				if (jungNode.getVisible()) {
					Point2D p = jungNode.getLocation();
					minX=(minX>p.getX()?p.getX():minX);
					minY=(minY>p.getY()?p.getY():minY);
					maxX=(maxX<p.getX()?p.getX():maxX);
					maxY=(maxY<p.getY()?p.getY():maxY);
				}
			}

			return new Dimension((int)(maxX-minX),(int)(maxY-minY));
		}
		//else return default
		return new Dimension(1200, 800);
		
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
			double minX = v.getLocation().getX();
			double minY = v.getLocation().getY();
			double maxX = v.getLocation().getX();
			double maxY = v.getLocation().getY();

			for (VisualNode jungNode: this.getVertices()) {
				if (jungNode.getVisible()) {
					Point2D p = jungNode.getLocation();
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

		String nKey = null;
		String nName = null;
		String nType = null;
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
					v.setLocation(new Point2D.Double(nodeX,nodeY));
					this.addVertex(v);
					v.setName(nName);
					//shift node key by current key
					v.setKey(currentKey+new Integer(nKey));
					v.setType(NodeType.valueOf(nType));
					v.setSQLDefinition(nSQLDefinition);
					
					
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
					e.setKey(currentKey+new Integer(eKey));
					 
								
	            }//end of if clause
	
	        }//end of for loop for edges
	        
	        // find the policies (if any exists) for nodes
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
					
					elementHnode.setAttribute("x", new Double(v.getLocation().getX()).toString());
					elementHnode.setAttribute("y", new Double(v.getLocation().getY()).toString());
					elementHnodes.appendChild(elementHnode);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(new Integer(v.getKey()).toString()));
					elementHnode.appendChild(elementKey);
					// write element name
					Element elementName = document.createElement("Name");
					elementName.appendChild(document.createTextNode(v.getName()));
					elementHnode.appendChild(elementName);
					// write element type
					Element elementType = document.createElement("Type");
					elementType.appendChild(document.createTextNode(v.getType().toString()));
					elementHnode.appendChild(elementType);
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
					elementKey.appendChild(document.createTextNode(new Integer(e.getKey()).toString()));
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
					elementFromNode.appendChild(document.createTextNode(new Integer(e.getFromNode().getKey()).toString()));
					elementHedge.appendChild(elementFromNode);
					// write element toNode
					Element elementToNode = document.createElement("ToNode");
					elementToNode.appendChild(document.createTextNode(new Integer(e.getToNode().getKey()).toString()));
					elementHedge.appendChild(elementToNode);
					// end element HEdge
				}
				rootElement.appendChild(elementHedges);
				
				
				// write policies
				Element elementHPolicies = document.createElement("HPolicies");
				// write events
				Element elementHEvents = document.createElement("HEvents");
				
				for (VisualNode v : this.getVertices()) {
					for (EvolutionPolicy p: v.getPolicies()) {
						//start tag HPolicy
						Element elementHPolicy = document.createElement("HPolicy");
						elementHPolicies.appendChild(elementHPolicy);
						//write Node having the policy
						Element elementHNode = document.createElement("HNode");
						elementHNode.appendChild(document.createTextNode(new Integer(v.getKey()).toString()));
						elementHPolicy.appendChild(elementHNode);
						//write event handled by the policy
						Element elementEvent = document.createElement("HEvent");
						elementHPolicy.appendChild(elementEvent);
						
						Element elementEventNode = document.createElement("HEventNode");
						elementEventNode.appendChild(document.createTextNode(new Integer(p.getSourceEvent().getEventNode().getKey()).toString()));
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
					for (EvolutionEvent e : v.getEvents()) {
						//write element event
						Element elementHEvent = document.createElement("HEvent");
						elementHEvents.appendChild(elementHEvent);
						//write Node having the event
						Element elementHNode = document.createElement("HNode");
						elementHNode.appendChild(document.createTextNode(new Integer(v.getKey()).toString()));
						elementHEvent.appendChild(elementHNode);
						
						Element elementEventNode = document.createElement("HEventNode");
						elementEventNode.appendChild(document.createTextNode(new Integer(e.getEventNode().getKey()).toString()));
						elementHEvent.appendChild(elementEventNode);
						Element elementEventType = document.createElement("HEventType");
						elementEventType.appendChild(document.createTextNode(e.getEventType().toString()));
						elementHEvent.appendChild(elementEventType);
						// end element HEvent
						
					}
				}
				rootElement.appendChild(elementHPolicies);
				rootElement.appendChild(elementHEvents);
			
	
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
    
  
    
	
}
