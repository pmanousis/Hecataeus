/**
 *
 * @author Fotini Anagnostou, National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

import java.io.File;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

import HecataeusEvolutionManager.HecataeusEvolutionEdge;
import HecataeusEvolutionManager.HecataeusEvolutionNode;

/**
* The class implements a graph that represetns a database and the queries to it
*/
public class HecataeusGraph {
	
		/**
        *collection of HecataeusNodes
        */
        private HecataeusNodes _Nodes = null;

        /**
         * collection of HecataeusEdges
         */
        private HecataeusEdges _Edges = null;
        
        /**
        *counter generating unique ids (integers) for graph elements(nodes and edges)
        */
         protected static int _KeyGenerator;

    /**
    * Creates a new graph that represetns a database and the queries to it
    */         
	public HecataeusGraph() {
		_Nodes = new HecataeusNodes();
		_Edges = new HecataeusEdges();
		HecataeusGraph._KeyGenerator = 0;
	}

	/**
	* adds node to the graph by name and type
	*/
	public void add_node(String Name, int Type) {
		// declare new node
		HecataeusNode newNode = new HecataeusNode(Name,Type);
		// increase elements counter by one 
		++HecataeusGraph._KeyGenerator;
		// assign key
		newNode.setKey(new Integer(HecataeusGraph._KeyGenerator).toString());
		// add new node to graph
		_Nodes.add(newNode);
	}

	/**
	* adds node to the graph by HecataeusNode
	*/
	public void add_node(HecataeusNode Node) {
		// increase elements counter by one 
		++HecataeusGraph._KeyGenerator;
		// assign key
		Node.setKey(new Integer(HecataeusGraph._KeyGenerator).toString());
		// add new node to graph
	}

	/**
	* removes a node from the graph
	*/
	public void remove_node(HecataeusNode Node) {
		// remove node
		this._Nodes.remove(Node);
		// remove all in edges
		Node.getInEdges().clear();
		// remove all out edges
		Node.getOutEdges().clear();
	}

	/**
	* adds edge to the graph by name, type, from and to nodes
	*/
	public void add_edge(String Name, int Type, HecataeusNode FromNode, HecataeusNode ToNode) {
		// declare new edge
		HecataeusEdge newEdge = new HecataeusEdge(Name,Type,FromNode,ToNode);
		// increase elements counter by one 
		++HecataeusGraph._KeyGenerator;
		// assign key
		newEdge.setKey(new Integer(HecataeusGraph._KeyGenerator).toString());
		// add new edge to graph
		this._Edges.add(newEdge);
		// add edge to incoming edges of ToNode
		ToNode.getInEdges().add(newEdge);
		// add edge to outgoing edges of FromNode
		FromNode.getOutEdges().add(newEdge);
	}

	/**
	* adds edge to the graph by HecataeusEdge
	*/
	public void add_edge(HecataeusEdge Edge) {
		// increase element counter by one
		++HecataeusGraph._KeyGenerator;
		// assign key
		Edge.setKey(new Integer(HecataeusGraph._KeyGenerator).toString());
		// add new node to graph
		this._Edges.add(Edge);
		// add edge to incoming edges of ToNode
		Edge.getToNode().getInEdges().add(Edge);
		// add edge to outgoing edges of FromNode
		Edge.getFromNode().getOutEdges().add(Edge);
	}

	/**
	* removes an edge from the graph
	*/
	public boolean remove_edge(HecataeusEdge Edge) {
		// remove edge from graph
		this._Edges.remove(Edge);
		// remove edge from inEdges
		Edge.getToNode().getInEdges().remove(Edge);
		// remove edge from outEdges
		Edge.getFromNode().getOutEdges().remove(Edge);
		return true;
	}

	/**
	*  clears all nodes and edges from the graph
	*/
	public boolean clear_graph() {
		// destroy objects
		// clear collections
		 this._Nodes.clear();
		 this._Edges.clear();
                 
         return true;
	}

	/**
	* get Node by Key
	*/
	public HecataeusNode getNode(String Key) {
		HecataeusNode u;
		// look in the collection of the graph nodes
		for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
			u = this._Nodes.get(forEachVar0);
			//check if the given key is the same with the current node key
			if ( u.getKey().equals(Key) ) {
				return u;
			}
		}
                return null;
	}

	/**
	*  get node by its name and type
	*/
	public HecataeusNode getNode(String Name, int Type) {
		HecataeusNode u;
		// look in the collection of the graph nodes
		for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
			u = this._Nodes.get(forEachVar0);
			//check if the given name and type are the same with the current node name and type
			if ( ( u.getName().equals(Name) & u.getType() == Type ) ) {
				return u;
			}
		}
                return null;
	}

	/**
	* get edge by key
	*/
	public HecataeusEdge getEdge(String Key) {
		HecataeusEdge u;
		// look in the collection of the graph nodes
		for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
			u = this._Edges.get(forEachVar0);
			//check if the given key is the same with the current edge key
			if ( u.getKey().equals(Key) ) {
				return u;
			}
		}
                return null;
	}

	/**
	* get edge by name and type
	*/
	public HecataeusEdge getEdge(String Name, int Type) {
		HecataeusEdge u;
		// look in the collection of the graph nodes
		for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
			u =this._Edges.get(forEachVar0);
			//check if the given name and type are the same with the current edge name and type
			if ( ( u.getName().equals(Name) & u.getType() == Type ) ) {
				return u;
			}
		}
                return null;
	}

    /**
	    * used for finding explicitly an attribute by its name and its parent relation node
	    **/
	    public HecataeusNode getAttributeNode(String TableName, String AttributeName) {
	        HecataeusNode getAttributeNode = null;
	        HecataeusNode u = (HecataeusNode) this.getNode(TableName, HecataeusNodeType.NODE_TYPE_RELATION);
	        HecataeusEdge e = null;
	        if ( !(u == null) ) {
	            for (int forEachVar0 = 0; forEachVar0 < u.getOutEdges().size(); forEachVar0++) {
	                e =  u.getOutEdges().getEdge(forEachVar0);
	                if ( e.getToNode().getName().equals(AttributeName) ) {
	                    return e.getToNode();
	                }
	            }
	        }
	        
	        u = (HecataeusNode) this.getNode(TableName, HecataeusNodeType.NODE_TYPE_VIEW);
	        if ( !(u == null) ) {
	            for (int forEachVar0 = 0; forEachVar0 < u.getOutEdges().size(); forEachVar0++) {
	                e =  u.getOutEdges().getEdge(forEachVar0);
	                if ( e.getToNode().getName().equals(AttributeName) ) {
	                    return e.getToNode();
	                }
	            }
	        }
	        
	        return getAttributeNode;
	    }

	/**
	* gets a table node and creates an alias graph
	*/
	public HecataeusNode createTableAlias(HecataeusNode srcTable, String tblAlias) {
		HecataeusEdge edgeAttribute = null;
		// create table alias node
		HecataeusNode aliasTable = new HecataeusNode(tblAlias,HecataeusNodeType.NODE_TYPE_RELATION);
		// add node to graph
		add_node(aliasTable);
		// create edge for alias
		this.add_edge("alias", HecataeusEdgeType.EDGE_TYPE_ALIAS, aliasTable, srcTable);
		//  for each attribute in source table create new attribute nodes
		for (int forEachVar0 = 0; forEachVar0 < srcTable.getOutEdges().size(); forEachVar0++) {
			edgeAttribute = srcTable.getOutEdges().get(forEachVar0+1);
			if ( (new Integer(edgeAttribute.getType())).equals(HecataeusEdgeType.EDGE_TYPE_SCHEMA) ) {
				HecataeusNode aliasAttr = new HecataeusNode(edgeAttribute.getToNode().getName(),HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
				add_node(aliasAttr);
				this.add_edge("S", HecataeusEdgeType.EDGE_TYPE_SCHEMA, aliasTable, aliasAttr);
			}
		}
		return aliasTable;
	}

	/**
	* imports a graph from an xml file
	*/
	public boolean importFromXML(String filename) {
		
		String nKey = null;
		String nName = null;
		String nType = null;
		String eKey = null;
		String eName = null;
		String eType = null;
		String eFromNode = null;
		String eToNode = null;
		String gKeyGen = null;
		
		int s;
		
	try {

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse (new File(filename));

        // normalize text representation            
        doc.getDocumentElement ().normalize ();
        
        NodeList listOfNodes = doc.getElementsByTagName("HNode");

        for(s=0; s<listOfNodes.getLength() ; s++){

            Node firstNode = listOfNodes.item(s);
            if(firstNode.getNodeType() == Node.ELEMENT_NODE){


                Element firstNodeElement = (Element)firstNode;

                //Get the key                      
                NodeList KeyList = firstNodeElement.getElementsByTagName("Key");
                Element KeyElement = (Element)KeyList.item(0);

                NodeList textKeyList = KeyElement.getChildNodes();
                System.out.println("Key : " + 
                       ((Node)textKeyList.item(0)).getNodeValue().trim());
                nKey = ((Node)textKeyList.item(0)).getNodeValue();

                //Get the name                         
                NodeList NameList = firstNodeElement.getElementsByTagName("Name");
                Element NameElement = (Element)NameList.item(0);

                NodeList textNameList = NameElement.getChildNodes();
                System.out.println("Name : " + 
                       ((Node)textNameList.item(0)).getNodeValue().trim());
                nName = ((Node)textNameList.item(0)).getNodeValue();

                //-------                    
                NodeList TypeList = firstNodeElement.getElementsByTagName("Type");
                Element TypeElement = (Element)TypeList.item(0);

                //Get the type      
                NodeList textTypeList = TypeElement.getChildNodes();
                System.out.println("Type : " + 
                       ((Node)textTypeList.item(0)).getNodeValue().trim());
                nType = ((Node)textTypeList.item(0)).getNodeValue();
                
				// add node to the graph
				HecataeusNode v = new HecataeusNode();
				v.setName(nName);
				v.setType(HecataeusNodeType.convertStringToNodeType(nType));
				System.out.println("nType is: "+nType);
				System.out.println("Type is: "+v.getType());
				add_node(v);
				v.setKey(nKey);

            }//end of if clause

        }//end of for loop for nodes

        NodeList listOfEdges = doc.getElementsByTagName("HEdge");

        // for each "HEdge" tag
        for(s=0; s<listOfEdges.getLength() ; s++){

            Node firstEdge = listOfEdges.item(s);
            if(firstEdge.getNodeType() == Node.ELEMENT_NODE){


                Element firstEdgeElement = (Element)firstEdge;

                //Get the key                 
                NodeList KeyList = firstEdgeElement.getElementsByTagName("Key");
                Element KeyElement = (Element)KeyList.item(0);

                NodeList textKeyList = KeyElement.getChildNodes();
                System.out.println("Key : " + 
                       ((Node)textKeyList.item(0)).getNodeValue().trim());
                eKey = ((Node)textKeyList.item(0)).getNodeValue();

                //Get the name                 
                NodeList NameList = firstEdgeElement.getElementsByTagName("Name");
                Element NameElement = (Element)NameList.item(0);

                NodeList textNameList = NameElement.getChildNodes();
                System.out.println("Name : " + 
                       ((Node)textNameList.item(0)).getNodeValue().trim());
                eName = ((Node)textNameList.item(0)).getNodeValue();

                //Get the type                 
                NodeList TypeList = firstEdgeElement.getElementsByTagName("Type");
                Element TypeElement = (Element)TypeList.item(0);

                NodeList textTypeList = TypeElement.getChildNodes();
                System.out.println("Type : " + 
                       ((Node)textTypeList.item(0)).getNodeValue().trim());
                eType = ((Node)textTypeList.item(0)).getNodeValue();
                
                //Get fromNode
                NodeList FromNodeList = firstEdgeElement.getElementsByTagName("FromNode");
                Element FromNodeElement = (Element)FromNodeList.item(0);

                NodeList textFromNodeList = FromNodeElement.getChildNodes();
                System.out.println("FromNode : " + 
                       ((Node)textFromNodeList.item(0)).getNodeValue().trim());
                eFromNode = ((Node)textFromNodeList.item(0)).getNodeValue();
                
                //Get toNode
                NodeList ToNodeList = firstEdgeElement.getElementsByTagName("ToNode");
                Element ToNodeElement = (Element)ToNodeList.item(0);

                NodeList textToNodeList = ToNodeElement.getChildNodes();
                System.out.println("ToNode : " + 
                       ((Node)textToNodeList.item(0)).getNodeValue().trim());
                eToNode = ((Node)textToNodeList.item(0)).getNodeValue();
                
				// add edge to the graph
				HecataeusEdge e = new HecataeusEdge();
				e.setName(eName);
				e.setType(HecataeusEdgeType.convertStringToEdgeType(eType));
				e.setFromNode(getNode(eFromNode));
				e.setToNode(getNode(eToNode));
				add_edge(e);
				e.setKey(eKey);

            }//end of if clause

        }//end of for loop for edges
        
		// read HKeyGen Tag
        NodeList HGenKeyList = doc.getElementsByTagName("HGenKey");
	    Element HGenKeyElement = (Element)HGenKeyList.item(0);
	        
	    NodeList textHGenKeyList = HGenKeyElement.getChildNodes();
	    System.out.println("HGenKey : " + 
            ((Node)textHGenKeyList.item(0)).getNodeValue().trim());
	        gKeyGen = ((Node)textHGenKeyList.item(0)).getNodeValue();

	    //sets the keyGenerator to the value readed from the file
	    HecataeusGraph._KeyGenerator = (new Integer(gKeyGen));

    }catch (SAXParseException err) {
    System.out.println ("** Parsing error" + ", line " 
         + err.getLineNumber () + ", uri " + err.getSystemId ());
    System.out.println(" " + err.getMessage ());
    return false;

    }catch (SAXException e) {
    	Exception x = e.getException ();
    	((x == null) ? e : x).printStackTrace ();
    return false;

    }catch (Throwable t) {
    	t.printStackTrace ();
    return false;
    }
    return true;
}       	

	/**
	* exports the graph to an xml file
	*/
	public boolean exportToXML(String filename) {
		
		Boolean flagForAnswer = false;
		Boolean flagForContinue = true;
		
		// create/find the file with the given filename
		File file = new File(filename);
		if (file.exists()) {

			//if the file already exists, ask the user if he agrees to override it
			Scanner scanner = new Scanner(System.in);
			while (!flagForAnswer) {	
				System.out.println("The file will be overriden! Do you agree? Answer with y or n");
				String answer = scanner.next();
				if (answer.equals("n")) {
					flagForContinue = false;
					return false;
				}else if (answer.equals("y"))
					flagForAnswer = true;
					flagForContinue = true;
			}
		}

		// if the user agrees with the option to override the existing file or the file is  a new one
		if (flagForContinue) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				
				//write tag "HGraph"
				Element rootElement = document.createElement("HGraph");
				document.appendChild(rootElement);
				
				//write tags "HNodes" and "HEdges"
				Element elementHnodes = document.createElement("HNodes");
				Element elementHedges = document.createElement("HEdges");
				
				// write nodes
				HecataeusNode v = null;
				for (int forEachVar0 = 0; forEachVar0 < this._Nodes.size(); forEachVar0++) {
					v = _Nodes.get(forEachVar0);
					// write HNode
					Element elementHnode = document.createElement("HNode");
					elementHnodes.appendChild(elementHnode);
					rootElement.appendChild(elementHnodes);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(v.getKey()));
					elementHnode.appendChild(elementKey);
					// write element name
					Element elementName = document.createElement("Name");
					elementName.appendChild(document.createTextNode(v.getName()));
					elementHnode.appendChild(elementName);
					// write element type
					Element elementType = document.createElement("Type");
					elementType.appendChild(document.createTextNode(HecataeusNodeType.convertNodeTypeToString(v.getType())));
					elementHnode.appendChild(elementType);
					// end element HNode
				}
		    
				// write edges
				HecataeusEdge e = null;
				for (int forEachVar0 = 0; forEachVar0 < this._Edges.size(); forEachVar0++) {
					e = _Edges.get(forEachVar0);
					// write HNode
					Element elementHedge = document.createElement("HEdge");
					elementHedges.appendChild(elementHedge);
					rootElement.appendChild(elementHedges);
					// write element key
					Element elementKey = document.createElement("Key");
					elementKey.appendChild(document.createTextNode(e.getKey()));
					elementHedge.appendChild(elementKey);
					// write element name
					Element elementName = document.createElement("Name");
					elementName.appendChild(document.createTextNode(e.getName()));
					elementHedge.appendChild(elementName);
					// write element type
					Element elementType = document.createElement("Type");
					elementType.appendChild(document.createTextNode(HecataeusEdgeType.convertEdgeTypeToString(e.getType())));
					elementHedge.appendChild(elementType);
					// write element fromNode
					Element elementFromNode = document.createElement("FromNode");
					elementFromNode.appendChild(document.createTextNode(e.getFromNode().getKey()));
					elementHedge.appendChild(elementFromNode);
					// write element toNode
					Element elementToNode = document.createElement("ToNode");
					elementToNode.appendChild(document.createTextNode(e.getToNode().getKey()));
					elementHedge.appendChild(elementToNode);
					// end element HEdge
				}
				
				//write the number of elements
				Element elementHKeyGen = document.createElement("HKeyGen");
				elementHKeyGen.appendChild(document.createTextNode((new Integer(_KeyGenerator)).toString()));
				rootElement.appendChild(elementHKeyGen);
				
				//write to file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(document);
		        StreamResult result =  new StreamResult(file);
		        transformer.transform(source, result);
				
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return false;
			}
			 catch (TransformerConfigurationException e) {
				e.printStackTrace();
				return false;
			}
			 catch (TransformerException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
		
	/**
	* returns the nodes of the graph
	*/
	public HecataeusNodes getNodes() {
		return this._Nodes;
	}

	/**
	* returns the edges of the graph
	*/
	public HecataeusEdges getEdges() {
		return this._Edges;
	}
	
	/**
	* sets the keyGenerator to zero, to start counting the elements from the begining
	*/
	public void resetKeyGenerator() {
		_KeyGenerator = 0;
	}
	
	/**
	* returns the value of the keyGenerator, that is the number of elements the graph has
	*/
	public int getKeyGenerator() {
		return _KeyGenerator;
	}
}
