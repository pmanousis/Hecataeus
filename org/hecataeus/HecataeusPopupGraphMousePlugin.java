/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */
package org.hecataeus;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PickSupport;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.SettableVertexLocationFunction;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.hecataeus.evolution.HecataeusEventType;
import org.hecataeus.evolution.HecataeusEvolutionEdge;
import org.hecataeus.evolution.HecataeusPolicyType;
import org.hecataeus.evolution.HecataeusEdgeType;
import org.hecataeus.evolution.HecataeusStatusType;
import org.hecataeus.evolution.HecataeusEvolutionEdges;
import org.hecataeus.evolution.HecataeusEvolutionEvent;
import org.hecataeus.evolution.HecataeusEvolutionEvents;
import org.hecataeus.evolution.HecataeusEvolutionMessage;
import org.hecataeus.evolution.HecataeusEvolutionNode;
import org.hecataeus.evolution.HecataeusEvolutionNodes;
import org.hecataeus.evolution.HecataeusEvolutionPolicies;
import org.hecataeus.evolution.HecataeusEvolutionPolicy;
import org.hecataeus.evolution.HecataeusNodeType;
import org.hecataeus.evolution.HecataeusMetricManager;
import org.hecataeus.jung.HecataeusJungEdge;
import org.hecataeus.jung.HecataeusJungEdges;
import org.hecataeus.jung.HecataeusJungGraph;
import org.hecataeus.jung.HecataeusJungNode;
import org.hecataeus.jung.HecataeusJungNodeVisible;
import org.hecataeus.jung.HecataeusJungNodes;
import org.hecataeus.jung.HecataeusJungNodeVisible.VisibleLayer;

import sun.misc.Queue;


public class HecataeusPopupGraphMousePlugin extends AbstractPopupGraphMousePlugin{

	SettableVertexLocationFunction vertexLocations;	
	protected VisualizationViewer vv = null;
	protected Set picked;

	public HecataeusPopupGraphMousePlugin(SettableVertexLocationFunction vertexLocations) {
		this.vertexLocations = vertexLocations;
	}


	@SuppressWarnings("serial")
	@Override
	protected void handlePopup(MouseEvent e) {

		vv = (VisualizationViewer)e.getSource();
		final Layout layout = vv.getGraphLayout();
		final PluggableRenderer pr = (PluggableRenderer) vv.getRenderer();
		final HecataeusJungGraph graph = (HecataeusJungGraph) layout.getGraph();
		final Point2D p = e.getPoint();
		final Point2D ivp = vv.inverseViewTransform(e.getPoint());

		PickSupport pickSupport = vv.getPickSupport();

		//if mouse clicked on a vertex or an edge
		if(pickSupport != null) {
			final HecataeusJungNode vertex = (HecataeusJungNode) pickSupport.getVertex(ivp.getX(), ivp.getY());
			final HecataeusJungEdge edge = (HecataeusJungEdge) pickSupport.getEdge(ivp.getX(), ivp.getY());
			final PickedState pickedState = vv.getPickedState();
			JPopupMenu popup = new JPopupMenu();

			//if mouse click on vertex
			if(vertex != null) {
				picked = pickedState.getPickedVertices();

				//if more than one vertex were clicked
				//create direct edge
				if(picked.size() > 1) {
					JMenu directedMenu = new JMenu("Create Directed Edge");
					for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
						final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
						//my code
						if (!other.equals(vertex)) {
							
							popup.add(directedMenu);
							directedMenu.add(new AbstractAction("["+other.getName()+","+vertex.getName()+"]") {
								public void actionPerformed(ActionEvent e) {

									final NameType nameType = new NameType(false);
									nameType.okButton.addActionListener(

											new ActionListener() {

												public void actionPerformed(ActionEvent e) {

													String name = nameType.textFieldName.getText();
													HecataeusEdgeType type = HecataeusEdgeType.valueOf((String)nameType.comboBoxType.getSelectedItem());
													if (!name.equals("")) {
														HecataeusJungEdge newEdge = new HecataeusJungEdge(other, vertex);
														graph.add_edge(newEdge);
														newEdge.setName(name);
														newEdge.setType(type);
														vv.repaint();
														nameType.dispose();
													}
													else {
														JOptionPane.showMessageDialog(null,"Set the name and type of the\n edge before creating it!","Message",JOptionPane.WARNING_MESSAGE); 
													}

												} // end actionPerformed
											}); // end actionListener

								}
							});
						}
					}
					//if more than one vertex were clicked
					//delete vertices

					popup.add(new AbstractAction("Delete Vertices") {
						public void actionPerformed(ActionEvent e) {
							int response = JOptionPane.showConfirmDialog(null,"Delete these Vertices?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
							// response = yes
							if (response==0){
								for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
									final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
									System.out.println("other' s name: "+other.getName());
									graph.remove_node(other);
									vv.repaint();
								}
//								if (!picked.contains(vertex)){
//								graph.remove_node(vertex);
//								vv.repaint();
//								}
							}
							else ;
						}
					});
					//if more than one vertex were clicked
					//add policy 
					popup.add(new AbstractAction("Add Policy") {
						public void actionPerformed(ActionEvent e) {

							final AddPolicy Policyframe = new AddPolicy();

						}
					});
					//if more than one vertex were clicked
					//add policy 
					popup.add(new AbstractAction("Add Event") {
						public void actionPerformed(ActionEvent e) {

							final AddEvent Eventframe = new AddEvent();

						}
					});
					//if more than one vertex were clicked
					//delete policy 
					popup.add(new AbstractAction("Delete Policy") {
						public void actionPerformed(ActionEvent e) {
							int response = JOptionPane.showConfirmDialog(null,"Delete All Policies?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
							// response = yes
							if (response==0){
								for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
									final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
									final HecataeusEvolutionNode evNode = other.getHecataeusEvolutionNode();
									for(int i = 0; i< evNode.getPolicies().size();i++) {
										HecataeusEvolutionPolicy p = evNode.getPolicies().get(i);
										evNode.removePolicy(p);
									}
								}
								vv.repaint();

							}

						}
					});

				}
				//create multiple edges edge
				if(picked.size() > 2) {
					JMenu directedMenu = new JMenu("Create Multiple Edges");
					popup.add(directedMenu);
					directedMenu.add(new AbstractAction("[{selection}"+vertex.getName()+"]") {
						public void actionPerformed(ActionEvent e) {
							final NameType nameType = new NameType(false);
							nameType.okButton.addActionListener(
									new ActionListener() {
										public void actionPerformed(ActionEvent e) {

											String name = nameType.textFieldName.getText();
											HecataeusEdgeType type = HecataeusEdgeType.valueOf((String)nameType.comboBoxType.getSelectedItem());
											if (!name.equals("")) {
												for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
													final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
													//my code
													if (!other.equals(vertex)) {
														HecataeusJungEdge newEdge = new HecataeusJungEdge(other, vertex);
														graph.add_edge(newEdge);
														newEdge.setName(name);
														newEdge.setType(type);
													}
												}
												vv.repaint();
												nameType.dispose();
											}
											else {
												JOptionPane.showMessageDialog(null,"Set the name and type of the\n edge before creating it!","Message",JOptionPane.WARNING_MESSAGE); 
											}

										} // end actionPerformed
									}); // end actionListener
						}
					});
					directedMenu.add(new AbstractAction("["+vertex.getName()+",{selection}]") {
						public void actionPerformed(ActionEvent e) {
							final NameType nameType = new NameType(false);
							nameType.okButton.addActionListener(
									new ActionListener() {
										public void actionPerformed(ActionEvent e) {

											String name = nameType.textFieldName.getText();
											HecataeusEdgeType type = HecataeusEdgeType.valueOf((String)nameType.comboBoxType.getSelectedItem());
											if (!name.equals("")) {
												for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
													final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
													//my code
													if (!other.equals(vertex)) {
														HecataeusJungEdge newEdge = new HecataeusJungEdge(vertex, other);
														graph.add_edge(newEdge);
														newEdge.setName(name);
														newEdge.setType(type);
													}
												}
												vv.repaint();
												nameType.dispose();
											}
											else {
												JOptionPane.showMessageDialog(null,"Set the name and type of the\n edge before creating it!","Message",JOptionPane.WARNING_MESSAGE); 
											}

										} // end actionPerformed
									}); // end actionListener
						}
					});	
				}
				
					
				if (picked.size() == 1) {
					popup.add(new AbstractAction("Delete Vertex") {
						public void actionPerformed(ActionEvent e) {
							int response = JOptionPane.showConfirmDialog(null,
									"Delete this Vertex?", "",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
							// response = yes
							if (response == 0) {
								pickedState.pick(vertex, false);
								graph.remove_node(vertex);
								vv.repaint();
							} else
								;

						}
					});
					popup.add(new AbstractAction("Edit Vertex") {
						public void actionPerformed(ActionEvent e) {
							pickedState.pick(vertex, false);
							final NameType edit = new NameType(true);
							edit.textFieldName.setText(vertex.getName());
							edit.comboBoxType.setSelectedItem(vertex.getType().toString());

							edit.okButton.addActionListener(

							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									String name = edit.textFieldName.getText();
									if (!name.equals("")) {
										vertex.setName(name);
										vertex.setType(HecataeusNodeType.valueOf((String) edit.comboBoxType.getSelectedItem()));
										vv.repaint();
										edit.dispose();
									} else {
										JOptionPane.showMessageDialog(null,
												"The name cannot be null!",
												"Message",
												JOptionPane.WARNING_MESSAGE);
									}

								} // end actionPerformed
							}); // end actionListener

							vv.repaint();
						}
					});
										
					JMenu metricsMenu = new JMenu("Metrics");
					popup.add(metricsMenu);
					metricsMenu.add(new AbstractAction("Degree Total") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.degree(evNode);
							JOptionPane.showMessageDialog(null,"Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Degree In") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.inDegree(evNode);
							JOptionPane.showMessageDialog(null,"In Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Degree Out") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.outDegree(evNode);
							JOptionPane.showMessageDialog(null,"Out Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Transitive Degree In") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.inTransitiveDegree(evNode);
							JOptionPane.showMessageDialog(null,"Transitive In Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Transitive Degree Out") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.outTransitiveDegree(evNode);
							JOptionPane.showMessageDialog(null,"Transitive Out Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					
					metricsMenu.add(new AbstractAction("Strength Total") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.strength(evNode, graph.getHecataeusEvolutionGraph());
							JOptionPane.showMessageDialog(null,"Total Strength: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Strength In") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.inStrength(evNode, graph.getHecataeusEvolutionGraph());
							JOptionPane.showMessageDialog(null,"In Strength: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Strength Out") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.outStrength(evNode, graph.getHecataeusEvolutionGraph());
							JOptionPane.showMessageDialog(null,"Out Strength: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Weighted Degree In") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.inWeightedDegree(evNode);
							JOptionPane.showMessageDialog(null,"Weighted Degree: " + metric  ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						}
					});
					metricsMenu.add(new AbstractAction("Weighted Strength In") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							int metric = HecataeusMetricManager.inWeightedStrength(evNode, graph.getHecataeusEvolutionGraph());
							JOptionPane.showMessageDialog(null,"Weighted Strength In: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						
						}
					});
					metricsMenu.add(new AbstractAction("Entropy") {
						public void actionPerformed(ActionEvent e) {
							HecataeusEvolutionNode evNode = vertex.getHecataeusEvolutionNode();
							double metric = HecataeusMetricManager.entropyOutPerNode(evNode, graph.getHecataeusEvolutionGraph());
							JOptionPane.showMessageDialog(null,"Entropy In: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
						
						}
					});
					popup.addSeparator();
					
					popup.add(new AbstractAction("Set Frequency") {
						public void actionPerformed(ActionEvent e) {
							for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
								final HecataeusJungNode startNode = (HecataeusJungNode) iterator.next();
								try {
									int freq = new Integer(JOptionPane.showInputDialog(null, "Enter a frequency for: " +startNode.getName(), startNode.getHecataeusEvolutionNode().getFrequency()));
									startNode.getHecataeusEvolutionNode().setFrequency(freq);
									graph.getHecataeusEvolutionGraph().propagateFrequency(startNode.getHecataeusEvolutionNode());
								} catch (NumberFormatException e1) {
									
								}
								}
						}
					});
					
					popup.add(new AbstractAction("Set Policies") {
						public void actionPerformed(ActionEvent e) {
							// create a new JFrame to handle policies (edit,add and remove)
							final HecataeusNodePolicies policiesFrame = new HecataeusNodePolicies(
									vv, vertex);
						}
					});
					
					popup.add(new AbstractAction("Set Events") {
						public void actionPerformed(ActionEvent e) {
							// create a new JFrame to handle events (edit,highlight,find prevailing policy,add and remove)
							final HecataeusNodeEvents eventsFrame = new HecataeusNodeEvents(vv, vertex);
						}
					});
				}    
				if(picked.size() > 0) {
					
					popup.add(new AbstractAction("Set Frequency") {
						public void actionPerformed(ActionEvent e) {
							for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
								final HecataeusJungNode startNode = (HecataeusJungNode) iterator.next();
								try {
									int freq = new Integer(JOptionPane.showInputDialog(null, "Enter a frequency for: " +startNode.getName(), startNode.getHecataeusEvolutionNode().getFrequency()));
									startNode.getHecataeusEvolutionNode().setFrequency(freq);
									graph.getHecataeusEvolutionGraph().propagateFrequency(startNode.getHecataeusEvolutionNode());
								} catch (NumberFormatException e1) {
									
								}
								}
						}
					});
					popup.add(new AbstractAction("Show Subgraph") {
						public void actionPerformed(ActionEvent e) {
							//set first invisible all graph
							for (int i = 0; i < graph.getJungNodes().size(); i++) {
								HecataeusJungNode node = graph.getJungNodes().get(i);
								node.setVisible(false);
							}
							//get parentNode

							for(Iterator iterator=picked.iterator(); iterator.hasNext(); ) {
								final HecataeusJungNode other = (HecataeusJungNode) iterator.next();

								HecataeusEvolutionNode evNode = other.getHecataeusEvolutionNode();
								HecataeusEvolutionNodes evNodes = graph.getHecataeusEvolutionGraph().getSubGraph(evNode);
								HecataeusJungNodes subGraph = graph.getJungNodesFromEvolution(evNodes);
								HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr.getVertexIncludePredicate();
								showAll.setVisibleLevel(subGraph,VisibleLayer.CONDITION);
							}
						}
					});
				}
				/*****************************************************************************
				 * the following code is for debug reasons, omit in production version
				 *
				 ***********************************************************************/
//				JMenu directedMenu = new JMenu("Create Decode Edges");
//				popup.add(directedMenu);
//				directedMenu.add(new AbstractAction("Connect decode") {
//					public void actionPerformed(ActionEvent e) {
//						String name = JOptionPane.showInputDialog( "The name of the parent node to find: " );  
//						String column = JOptionPane.showInputDialog( "The number of column: " );  
//						for (int i=0; i<graph.getJungNodes().size(); i++){
//		            		HecataeusJungNode u = graph.getJungNodes().get(i);
//		            		if (u.getName().equals(name)){
//		            			String[] columns = {};
//		            			if (name.equals("ETL1_ACT9")) {
//		            				if (column.equals("3")){
//		            					String[] col= {"G501",
//		            							"G502",
//		            							"G503",
//		            							"G504",
//		            							"G505",
//		            							"G506",
//		            							"G507",
//		            							"G508",
//		            							"G509",
//		            							"G510",
//		            							"G611",
//		            							"G612",
//		            							"G613",
//		            							"G614",
//		            							"G615" };
//		            					columns = col;
//		            				}
//		            			}
//		            			if (name.equals("ETL2_ACT4")) {
//		            				if (column.equals("3")){
//		            					String[] col= {
//		            							"G8013", 
//		            							"G8023", 
//		            							"G8033", 
//		            							"G8043", 
//		            							"G8053", 
//		            							"G8063", 
//		            							"G8073", 
//		            							"G8083", 
//		            							"G8093", 
//		            							"G8103", 
//		            							"G8113", 
//		            							"G8123", 
//		            							"G8133", 
//		            							"G8143", 
//		            							"G8153", 
//		            							"G8163", 
//		            							"G8173", 
//		            							"G8183", 
//		            							"G8193", 
//		            							"G8203", 
//		            							"G8213", 
//		            							"G8223", 
//		            							"G8233", 
//		            							"G8243", 
//		            							"G8253", 
//		            							"G8263", 
//		            							"G8273", 
//		            							"G8283", 
//		            							"G8293", 
//		            							"G8303", 
//		            							"G8313", 
//		            							"G8323", 
//		            							"G8333", 
//		            							"G8343", 
//		            							"G8353", 
//		            							"G8363", 
//		            							"G8373", 
//		            							"G8383", 
//		            							"G8393", 
//		            							"G8403", 
//		            							"G8413", 
//		            							"G8423", 
//		            							"G8433", 
//		            							"G8443", 
//		            							"G8453", 
//		            							"G8463", 
//		            							"G8473", 
//		            							"G8483", 
//		            							"G8493", 
//		            							"G8503", 
//		            							"G8513", 
//		            							"G8523", 
//		            							"G9533",	
//		            							"G10543",
//		            							"G10553",
//		            							"G10563"};
//		            					columns = col;
//		            				}
//		            				if (column.equals("4")){
//		            					String[] col= {
//		            							"G8014", 
//		            							"G8024", 
//		            							"G8034", 
//		            							"G8044", 
//		            							"G8054", 
//		            							"G8064", 
//		            							"G8074", 
//		            							"G8084", 
//		            							"G8094", 
//		            							"G8104", 
//		            							"G8114", 
//		            							"G8124", 
//		            							"G8134", 
//		            							"G8144", 
//		            							"G8154", 
//		            							"G8164", 
//		            							"G8174", 
//		            							"G8184", 
//		            							"G8194", 
//		            							"G8204", 
//		            							"G8214", 
//		            							"G8224", 
//		            							"G8234", 
//		            							"G8244", 
//		            							"G8254", 
//		            							"G8264", 
//		            							"G8274", 
//		            							"G8284", 
//		            							"G8294", 
//		            							"G8304", 
//		            							"G8314", 
//		            							"G8324", 
//		            							"G8334", 
//		            							"G8344", 
//		            							"G8354", 
//		            							"G8364", 
//		            							"G8374", 
//		            							"G8384", 
//		            							"G8394", 
//		            							"G8404", 
//		            							"G8414", 
//		            							"G8424", 
//		            							"G8434", 
//		            							"G8444", 
//		            							"G8454", 
//		            							"G8464", 
//		            							"G8474", 
//		            							"G8484",	
//		            							"G8494", 
//		            							"G8504", 
//		            							"G8514", 
//		            							"G8524", 
//		            							"G9534", 
//		            							"G10544",
//		            							"G10564" };
//		            					columns = col;
//		            				}
//		            				if (column.equals("5")){
//		            					String[] col= {
//		            							"G8015",
//		            							"G8025",
//		            							"G8035",
//		            							"G8045",
//		            							"G8055",
//		            							"G8065",
//		            							"G8075",
//		            							"G8085",
//		            							"G8095",
//		            							"G8105",
//		            							"G8115",
//		            							"G8125",
//		            							"G8135",
//		            							"G8145",
//		            							"G8155",
//		            							"G8165",
//		            							"G8175",
//		            							"G8185",
//		            							"G8195",
//		            							"G8205",
//		            							"G8215",
//		            							"G8225",
//		            							"G8235",
//		            							"G8245",
//		            							"G8255",
//		            							"G8265",
//		            							"G8275",
//		            							"G8285",
//		            							"G8295",
//		            							"G8305",
//		            							"G8315",
//		            							"G8325",
//		            							"G8335",
//		            							"G8345",
//		            							"G8355",
//		            							"G8365",
//		            							"G8375",
//		            							"G8385",
//		            							"G8395",
//		            							"G8405",
//		            							"G8415",
//		            							"G8425",
//		            							"G8435",
//		            							"G8445",
//		            							"G8455",
//		            							"G8465",
//		            							"G8525"};
//		            					columns = col;
//		            				}
//		            			}
//		            			if (name.equals("ETL3_ACT4")) {
//		            				if (column.equals("3")){
//		            					String[] col= {"G11573",
//		            							"G11583",
//		            							"G11593",
//		            							"G11603",
//		            							"G11613",
//		            							"G11623",
//		            							"G11633",
//		            							"G11643",
//		            							"G11653",
//		            							"G11663",
//		            							"G11673",
//		            							"G11683",
//		            							"G11693",
//		            							"G11703",
//		            							"G11713",
//		            							"G11723",
//		            							"G11733",
//		            							"G11743",
//		            							"G11753",
//		            							"G11763",
//		            							"G11773",
//		            							"G11783",
//		            							"G11793",
//		            							"G11803",
//		            							"G11813",
//		            							"G11823",
//		            							"G11833",
//		            							"G11843",
//		            							"G11853",
//		            							"G11863",
//		            							"G11873",
//		            							"G11883",
//		            							"G11893",
//		            							"G11903",
//		            							"G11913",
//		            							"G11923",
//		            							"G11933",
//		            							"G11943",
//		            							"G11953",
//		            							"G11963"};
//		            					columns = col;
//		            				}
//		            				if (column.equals("4")){
//		            					String[] col= {"G11574",
//		            							"G11584",
//		            							"G11594",
//		            							"G11604",
//		            							"G11614",
//		            							"G11624",
//		            							"G11634",
//		            							"G11644",
//		            							"G11654",
//		            							"G11664",
//		            							"G11674",
//		            							"G11684",
//		            							"G11694",
//		            							"G11704",
//		            							"G11714",
//		            							"G11724",
//		            							"G11734",
//		            							"G11744",
//		            							"G11754",
//		            							"G11764",
//		            							"G11774",
//		            							"G11784",
//		            							"G11794",
//		            							"G11804",
//		            							"G11814",
//		            							"G11824",
//		            							"G11834",
//		            							"G11844",
//		            							"G11854",
//		            							"G11864",
//		            							"G11874",
//		            							"G11884",
//		            							"G11894",
//		            							"G11904",
//		            							"G11914",
//		            							"G11924",
//		            							"G11934",
//		            							"G11944",
//		            							"G11954",
//		            							"G11964"};
//		            					columns = col;
//		            				}
//		            			}
//		            			if (name.equals("ETL4_ACT6")) {
//		            				if (column.equals("3")){
//		            					String[] col= { 
//		            							  "G1201",
//												  "G1202", 
//		            							  "G1203", 
//		            							  "G1204", 
//		            							  "G1205", 
//		            							  "G1306", 
//		            							  "G1407", 
//		            							  "G1408", 
//		            							  "G1409", 
//		            							  "G1410", 
//		            							  "G1411", 
//		            							  "G1412", 
//		            							  "G1413", 
//		            							  "G1414", 
//		            							  "G1415", 
//		            							  "G1416", 
//		            							  "G1417", 
//		            							  "G1418", 
//		            							  "G1419", 
//		            							  "G1420", 
//		            							  "G1521", 
//		            							  "G1522", 
//		            							  "G1523", 
//		            							  "G1524", 
//		            							  "G1525", 
//		            							  "G1626", 
//		            							  "G1627", 
//		            							  "G1628", 
//		            							  "G1629", 
//		            							  "G1630", 
//		            							  "G1631", 
//		            							  "G1632", 
//		            							  "G1733", 
//		            							  "G1734", 
//		            							  "G1735", 
//		            							  "G1736", 
//		            							  "G1737", 
//		            							  "G1801", 
//		            							  "G1902", 
//		            							  "G2003", 
//		            							  "G2004", 
//		            							  "G2005", 
//		            							  "G2006", 
//		            							  "G2007", 
//		            							  "G2008", 
//		            							  "G2009", 
//		            							  "G2010", 
//		            							  "G2011", 
//		            							  "G2012", 
//		            							  "G2013", 
//		            							  "G2014", 
//		            							  "G2115", 
//		            							  "G2116", 
//		            							  "G2117", 
//		            							  "G2118", 
//		            							  "G2219", 
//		            							  "G2220", 
//		            							  "G2221", 
//		            							  "G2322", 
//		            							  "G2323", 
//		            							  "G2324", 
//		            							  "G2425", 
//		            							  "G2426", 
//		            							  "G2427", 
//		            							  "G2428", 
//		            							  "G2429", 
//		            							  "G2430", 
//		            							  "G2431", 
//		            							  "G2432", 
//		            							  "G2433", 
//		            							  "G2534", 
//		            							  "G2535", 
//		            							  "G2536", 
//		            							  "G2637", 
//		            							  "G2842", 
//		            							  "G2843", 
//		            							  "G2844", 
//		            							  "G2845", 
//		            							  "G340",  
//		            							  "G341",  
//		            							  "G342",  
//		            							  "G343",  
//		            							  "G344",  
//		            							  "G345",  
//		            							  "G346",  
//		            							  "G347",  
//		            							  "G348"  };
//		            					columns = col;
//		            				}
//		            			}
//		            			for(int j = 0; j< columns.length;j++) {
//		            				for(Iterator iterator=u.getSuccessors().iterator(); iterator.hasNext(); ) {
//		            					HecataeusJungNode other = (HecataeusJungNode)iterator.next();
//		            					if (other.getName().equals(columns[j])) {
//		            						HecataeusJungEdge newEdge = new HecataeusJungEdge(vertex,other);
//		            						graph.add_edge(newEdge);
//		            						newEdge.setName("op"+(j+3));
//		            						newEdge.setType(HecataeusEdgeType.EDGE_TYPE_OPERATOR);
//		            					}
//		            				}
//		            			}
//		            		}
//						}
//						vv.repaint();
//					}
//				});
//				/*****************************************************************************
//				 * end of debug code
//				 ***********************************************************************/
//				
				
				//if edge is selected
			} else 
				if(edge != null) {
					popup.add(new AbstractAction("Delete Edge") {
						public void actionPerformed(ActionEvent e) {
							int response = JOptionPane.showConfirmDialog(null,"Delete this Edge?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
							// response = yes
							if (response==0){
								pickedState.pick(edge, false);
								graph.remove_edge(edge);
								vv.repaint();
							}
							else ;

						}});

					popup.add(new AbstractAction("Edit Edge") {
						public void actionPerformed(ActionEvent e) {
							final NameType edit = new NameType(false);
							edit.textFieldName.setText(edge.getName());
							edit.comboBoxType.setSelectedItem(edge.getType().ToString());

							edit.okButton.addActionListener(

									new ActionListener() {

										public void actionPerformed(ActionEvent e) {
											String name = edit.textFieldName.getText();
											HecataeusEdgeType type = HecataeusEdgeType.toEdgeType((String) edit.comboBoxType.getSelectedItem());
											if (!name.equals("")) {
												edge.setName(name);
												edge.setType(type);
												vv.repaint();
												edit.dispose();
											}
											else {
												JOptionPane.showMessageDialog(null,"The name cannot be null!","Message",JOptionPane.WARNING_MESSAGE); 
											}

										} // end actionPerformed
									}); // end actionListener

							vv.repaint();
						}});


					//if empty space clicked
				} else {
					popup.add(new AbstractAction("Create Vertex") {
						public void actionPerformed(ActionEvent e) {

							final NameType nameType = new NameType(true);
							nameType.okButton.addActionListener(

									new ActionListener() {

										public void actionPerformed(ActionEvent e) {
											String name = nameType.textFieldName.getText();
											if (!name.equals("")) {
												HecataeusJungNode newVertex = new HecataeusJungNode();
												vertexLocations.setLocation(newVertex, vv.inverseTransform(p));
												newVertex.setName(name);
												newVertex.setType(HecataeusNodeType.valueOf((String) nameType.comboBoxType.getSelectedItem()));
												Layout layout = vv.getGraphLayout();
												for(Iterator iterator=graph.getVertices().iterator(); iterator.hasNext(); ) {
													layout.lockVertex((Vertex)iterator.next());
												}
												graph.add_node(newVertex);
												vv.getModel().restart();
												for(Iterator iterator=graph.getVertices().iterator(); iterator.hasNext(); ) {
													layout.unlockVertex((Vertex)iterator.next());
												}
												vv.repaint();

												nameType.dispose();
											}
											else {
												JOptionPane.showMessageDialog(null,"Set the name and type of the\n node before creating it!","Message",JOptionPane.WARNING_MESSAGE); 
											}

										} // end actionPerformed
									}); // end actionListener

						} //end actionPerformed 
					});// end AbstractAction("Create Vertex") 
				}// end else
			if(popup.getComponentCount() > 0) {
				popup.show(vv, e.getX(), e.getY());
			}
		}
	}

	public class NameType extends JFrame {

		protected JTextField textFieldName;
		protected JComboBox comboBoxType;
		protected JButton okButton;

		public NameType(Boolean flagForNode){
			super("Set Name and Type");
			setSize(340,110);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			GridBagLayout gridbag = new GridBagLayout();
			HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
			JPanel pane = new JPanel();
			pane.setLayout(gridbag);

			// label for name
			constraints.reset(0,0,1,1,10,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelName = new JLabel("Set Name: ",JLabel.LEFT);
			gridbag.setConstraints(labelName,constraints);
			pane.add(labelName);

			// text field for name
			constraints.reset(1,0,1,1,90,0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			textFieldName = new JTextField();
			gridbag.setConstraints(textFieldName,constraints);
			pane.add(textFieldName);

			// label for type
			constraints.reset(0,1,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelType = new JLabel("Set Type: ",JLabel.LEFT);
			gridbag.setConstraints(labelType,constraints);
			pane.add(labelType);

			// combo box fox type selection
			constraints.reset(1,1,1,1,0,0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			comboBoxType = new JComboBox();			
			if (flagForNode)
				for (HecataeusNodeType nodeType: HecataeusNodeType.values()){
					comboBoxType.addItem(nodeType.toString());
				}
			else 
				for (HecataeusEdgeType edgeType: HecataeusEdgeType.values()){
					comboBoxType.addItem(edgeType.toString());
				}
			gridbag.setConstraints(comboBoxType,constraints);
			pane.add(comboBoxType);

			// OK button
			constraints.reset(0,2,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			okButton = new JButton("OK");
			gridbag.setConstraints(okButton,constraints);
			pane.add(okButton);

			// Cancel button
			constraints.reset(1,2,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JButton cancelButton = new JButton("Cancel");
			gridbag.setConstraints(cancelButton,constraints);
			cancelButton.addActionListener(
					new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});

			pane.add(cancelButton);

			setContentPane(pane);
			setVisible(true);

		}
	}

	public class AddPolicy extends JFrame {

		protected JComboBox comboBoxEventType;
		protected JComboBox comboBoxPolicyType;
		protected JButton okAddButton;
		protected JComboBox comboBoxPolicies;

		public AddPolicy(){

			setSize(400,500);
			setTitle("Add Policy");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			JPanel pane = new JPanel();

			GridBagLayout gridbag = new GridBagLayout();
			HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
			pane.setLayout(gridbag);

			// label for event type
			constraints.reset(0,0,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelEventType = new JLabel("Set event type: ",JLabel.LEFT);
			gridbag.setConstraints(labelEventType,constraints);
			pane.add(labelEventType);

			// combo box fox event type 
			constraints.reset(1,0,1,1,90,0);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			comboBoxEventType = new JComboBox();			
			//fill event types
			for (HecataeusEventType eventType: HecataeusEventType.values()) {
				comboBoxEventType.addItem(eventType.toString());
			}
			gridbag.setConstraints(comboBoxEventType,constraints);
			pane.add(comboBoxEventType);



			// label for policy type
			constraints.reset(0,2,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelPolicyType = new JLabel("Set policy type: ",JLabel.LEFT);
			gridbag.setConstraints(labelPolicyType,constraints);
			pane.add(labelPolicyType);

			// combo box for policy type
			constraints.reset(1,2,1,1,0,0);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			comboBoxPolicyType = new JComboBox();			
			//fill enumerators for policy types
			for (HecataeusPolicyType policyType: HecataeusPolicyType.values()){
				comboBoxPolicyType.addItem(policyType.toString());
			}
			gridbag.setConstraints(comboBoxPolicyType,constraints);
			pane.add(comboBoxPolicyType);

			// OK button
			constraints.reset(0,3,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			okAddButton = new JButton("Add Policy");
			gridbag.setConstraints(okAddButton,constraints);
			// add the selected policy to node' s policies collection
			okAddButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// get informations from comboBoxes
					// get event type
					String eventType = (String) comboBoxEventType.getSelectedItem();

					String policyType = (String) comboBoxPolicyType.getSelectedItem();

					// create and add selected policy
					for(Iterator iterator= picked.iterator(); iterator.hasNext(); ) {
						final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
						if ((HecataeusEventType.valueOf(eventType)==HecataeusEventType.ADD_ATTRIBUTE) 
								||(HecataeusEventType.valueOf(eventType)==HecataeusEventType.ADD_CONDITION)
								||(HecataeusEventType.valueOf(eventType)==HecataeusEventType.MODIFY_CONDITION)
								||(HecataeusEventType.valueOf(eventType)==HecataeusEventType.RENAME_RELATION)
								||(HecataeusEventType.valueOf(eventType)==HecataeusEventType.DELETE_CONDITION)
								||(HecataeusEventType.valueOf(eventType)==HecataeusEventType.DELETE_RELATION)
						){
							for (int i =0 ; i<other.getHecataeusEvolutionNode().getOutEdges().size() ;i++) {
								HecataeusEvolutionEdge edge= other.getHecataeusEvolutionNode().getOutEdges().get(i);
								if (edge.isProvider()) {
									other.getHecataeusEvolutionNode().addPolicy(HecataeusEventType.toEventType(eventType),edge.getToNode(),HecataeusPolicyType.toPolicyType(policyType));
								}
							}
						}else{
							other.getHecataeusEvolutionNode().addPolicy(HecataeusEventType.toEventType(eventType),other.getHecataeusEvolutionNode(),HecataeusPolicyType.toPolicyType(policyType));
						}
						vv.repaint();  							
					}
					dispose();
				} // end actionPerformed
			}); // end actionListener
			pane.add(okAddButton);

			// Cancel button
			constraints.reset(1,3,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			JButton cancelAddButton = new JButton("Cancel");
			gridbag.setConstraints(cancelAddButton,constraints);
			cancelAddButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});					
			pane.add(cancelAddButton);

			setContentPane(pane);
			setVisible(true);
		}
	}

	public class AddEvent extends JFrame {

		protected JComboBox comboBoxEventType;
		protected JButton okAddButton;
		protected JComboBox comboBoxEvents;

		public AddEvent(){

			setSize(400,500);
			setTitle("Add Event");
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			JPanel pane = new JPanel();

			GridBagLayout gridbag = new GridBagLayout();
			HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
			pane.setLayout(gridbag);

			// label for event type
			constraints.reset(0,0,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelEventType = new JLabel("Set event type: ",JLabel.LEFT);
			gridbag.setConstraints(labelEventType,constraints);
			pane.add(labelEventType);

			// combo box fox event type 
			constraints.reset(1,0,1,1,90,0);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			comboBoxEventType = new JComboBox();			
			//fill event types
			for (HecataeusEventType eventType: HecataeusEventType.values()) {
				comboBoxEventType.addItem(eventType.toString());
			}
			gridbag.setConstraints(comboBoxEventType,constraints);
			pane.add(comboBoxEventType);



			
			// OK button
			constraints.reset(0,3,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			okAddButton = new JButton("Add Event");
			gridbag.setConstraints(okAddButton,constraints);
			// add the selected policy to node' s policies collection
			okAddButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// get informations from comboBoxes
					// get event type
					String eventType = (String) comboBoxEventType.getSelectedItem();

					// create and add selected policy
					for(Iterator iterator= picked.iterator(); iterator.hasNext(); ) {
						final HecataeusJungNode other = (HecataeusJungNode) iterator.next();
						other.getHecataeusEvolutionNode().addEvent(HecataeusEventType.toEventType(eventType));
						vv.repaint();  							
						dispose();
						//end else
					}
				} // end actionPerformed
			}); // end actionListener
			pane.add(okAddButton);

			// Cancel button
			constraints.reset(1,3,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			JButton cancelAddButton = new JButton("Cancel");
			gridbag.setConstraints(cancelAddButton,constraints);
			cancelAddButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});					
			pane.add(cancelAddButton);

			setContentPane(pane);
			setVisible(true);
		}
	}

}
