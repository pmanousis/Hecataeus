package edu.ntua.dblab.hecataeus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualAggregateLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualLayoutType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible.VisibleLayer;
import edu.ntua.dblab.hecataeus.metrics.HecataeusMetricManager;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.PluggableRenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;

public class PopUpClickListener extends MouseAdapter{
	protected VisualizationViewer<VisualNode,VisualEdge> vv = null;
	protected Layout<VisualNode, VisualEdge> layout; 
	protected PluggableRenderContext<VisualNode,VisualEdge> pr; 
	protected Point2D pointClicked ;
	protected VisualGraph graph;
	protected PickedState<VisualEdge> pickedEdgeState;

	protected HecataeusPopupGraphMousePlugin hpgmp;

	protected VisualAggregateLayout containerLayout;
	protected HecataeusViewer viewer;
	public static VisualNode clickedVertex ;
	protected VisualEdge clickedEdge ;
	protected Set<VisualNode> pickedNodes;
	protected PickedState<VisualNode> pickedNodeState;
	protected PopUpMenu menu;


	public void mouseReleased(MouseEvent e){
		if (e.isPopupTrigger())
			doPop(e);
		}

	public void doPop(MouseEvent e){
		if(e.getButton()== MouseEvent.BUTTON3){
			menu = new PopUpMenu();
			handlePopup(e);
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	
	
	@SuppressWarnings("unchecked")
	protected void handlePopup(MouseEvent e) {
		
		hpgmp = new HecataeusPopupGraphMousePlugin();
		vv = (VisualizationViewer<VisualNode,VisualEdge>)e.getSource();
		layout = vv.getGraphLayout();	
		pr = (PluggableRenderContext<VisualNode, VisualEdge>) vv.getRenderContext();
		graph = (VisualGraph) layout.getGraph();
//		graph = HecataeusViewer.graphs.get(0);
		pointClicked = e.getPoint();

		viewer = HecataeusViewer.myViewer;
		
		
		GraphElementAccessor<VisualNode, VisualEdge> pickSupport = vv.getPickSupport();

		//if mouse clicked on a vertex or an edge
		if(pickSupport != null) {
			clickedVertex = pickSupport.getVertex(layout, pointClicked.getX(), pointClicked.getY());
			clickedEdge = pickSupport.getEdge(layout, pointClicked.getX(), pointClicked.getY());
			pickedNodeState = vv.getPickedVertexState();
			pickedEdgeState = vv.getPickedEdgeState();

			
			
			//if mouse click on vertex
			if(clickedVertex != null) {
				pickedNodeState.pick(clickedVertex, true);
				pickedNodes = pickedNodeState.getPicked();
				System.out.println("picked   nodes  " + pickedNodes);
			
				//if more than 2 vertices picked
				if(pickedNodes.size() > 2) {
					//add menu for creating multiple edges
					menu.popSelect.add(getMenuCreateMultipleEdges());
				}
				//if more than one vertex were picked
				if(pickedNodes.size() > 1) {
					//add menu for creating Directed edges
					menu.popSelect.add(getMenuCreateDirectedEdges());
					//add menu for deleting nodes
					menu.popSelect.add(getMenuDeleteNodes());
					//add policy in bulk 
					menu.popSelect.add(getMenuAddPolicy());
					//add event in bulk 
					menu.popSelect.add(getMenuAddEvent());
					//delete policy in bulk
					menu.popSelect.add(getMenuDeletePolicy());
					
					
					menu.popZoom.addActionListener(zoomToNewModuleTab());
					
					menu.popCollapse.addActionListener(collapser());
				}

				//if only 1 vertex is picked
				if (pickedNodes.size() == 1) {
					viewer.epilegmenosKombos = clickedVertex;
					menu.popZoom.addActionListener(zoomToNewModuleTab());
					menu.popSelect.add(getMenuSelectNode(clickedVertex));
					menu.popSelect.add(getMenuDeleteNode());
					menu.popSelect.add(getMenuEditNode());
					menu.popSelect.add(getMenuMetrics());
					menu.popSelect.addSeparator();
					menu.popSelect.add(getMenuSetPolicies());
					menu.popSelect.add(getMenuSetEvents());
					
//					clickedVertex.addMouseListener(new PopUpClickListener(), e);
//					clickedVertex.addMouseListener(new PopUpClickListener(), e);
					
					if (clickedVertex.getHasEvents())menu.popSelect.add(getMenuApplyEvent());
				}
				//for any vertex 
				if(pickedNodes.size() > 0) {
					menu.popSelect.add(getMenuSetFrequency());
					menu.popSelect.add(getMenuShowModule());
					menu.popSelect.add(getMenuSelectModule());
					menu.popSelect.add(getMenuShowInNewWindow());
				}

				//if edge is selected
			} else 
				if(clickedEdge != null) {
					pickedEdgeState.pick(clickedEdge, true);
					menu.popSelect.add(getMenuDeleteEdge());
					menu.popSelect.add(getMenuEditEdge());
					//if empty space clicked
				} else {
					menu.popSelect.add(getMenuCreateNode());// end AbstractAction("Create Vertex") 
				}// end else
			if(menu.popSelect.getComponentCount() > 0) {
/** @author pmanousi				popup.show(vv, e.getX(), e.getY());*/
			}
		}
	}
	
	
	
	
	protected JMenu getMenuCreateDirectedEdges(){
		JMenu directedMenu = new JMenu("Create Directed Edge");
		for(final VisualNode other: pickedNodes ) {
			if (!other.equals(clickedVertex)) {
				directedMenu.add(new AbstractAction("["+other.getName()+","+clickedVertex.getName()+"]") {
					public void actionPerformed(ActionEvent e) {
						final NameType nameType = new NameType(false);
						nameType.okButton.addActionListener(

								new ActionListener() {

									public void actionPerformed(ActionEvent e) {

										String name = nameType.textFieldName.getText();
										EdgeType type = (EdgeType)nameType.comboBoxType.getSelectedItem();
										if (!name.equals("")) {
											VisualEdge newEdge = new VisualEdge(name,type,other, clickedVertex);
											graph.addEdge(newEdge);
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
		return directedMenu;
	}

	
	/**
	 * @author eva
	 */
	
	
	
	protected AbstractAction collapser(){
		return new AbstractAction("Collapse Nodes") {
			final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.getActiveViewer();

		
			public void actionPerformed(ActionEvent e) {
				VisualLayoutType layoutType = VisualLayoutType.ConcentricCircleLayout;
				containerLayout = new VisualAggregateLayout(graph, layoutType, layoutType);
				if(pickedNodes.size() > 2) {
					VisualGraph gr = (VisualGraph) layout.getGraph();
					
					
					
					List<VisualNode> physicalNodes = graph.getVertices(NodeCategory.CONTAINER);
					VisualGraph containerGraph= graph.toGraph(physicalNodes);
					for (VisualNode node1: containerGraph.getVertices()) {
						for (VisualNode node2: containerGraph.getVertices()) {
							if (!node1.equals(node2) && graph.isConnected(graph.getModule(node1), graph.getModule(node2))) {
								VisualEdge newEdge = new VisualEdge("from",EdgeType.EDGE_TYPE_FROM, node1, node2); 
								containerGraph.addEdge(newEdge);
							}				
						}
					}
							
					GraphCollapser collapser = new GraphCollapser(graph);
				//	 pass the graph to the layout 
					VisualGraph g = (VisualGraph) collapser.collapse(graph, containerGraph);
					containerLayout.setGraph(g);
				//	set the module-level layout
					containerLayout.setTopLayoutType(containerLayout.getTopLayoutType());
				//	set the low-level layout
					containerLayout.setSubLayoutType(containerLayout.getSubLayoutType());
				
					
					gr = (VisualGraph) containerLayout.getGraph();
					// first update location of top - level node
					for (VisualNode node: containerLayout.getDelegate().getGraph().getVertices()) {
						layout.setLocation(node, gr.getLocation(node));
					}
					 //then update locations of all other nodes
					for (VisualNode node: gr.getVertices()) {
						containerLayout.setLocation(node, gr.getLocation(node));
					}
				}
				
			}
			
			
			
		};
	}

	class ClusterVertexShapeFunction<V> extends EllipseVertexShapeTransformer<V> {

		ClusterVertexShapeFunction() {
			setSizeTransformer(new ClusterVertexSizeFunction<V>(20));
		}
		@Override
		public Shape transform(V v) {
			if(v instanceof Graph) {
				int size = ((Graph)v).getVertexCount();
				
				int sides = Math.max(10, 3);
				
				return factory.getRegularPolygon(v, sides);
				
//				if (size < 8) {   
//					int sides = Math.max(size, 3);
//					return factory.getRegularPolygon(v, sides);
//				}
//				else {
//					return factory.getRegularStar(v, size);
//				}
			}
			return super.transform(v);
			}
		}
		class ClusterVertexSizeFunction<V> implements Transformer<V,Integer> {
			int size;
			public ClusterVertexSizeFunction(Integer size) {
				this.size = size;
			}

			public Integer transform(V v) {
				if(v instanceof Graph) {
					return 30;
				}
				return size;
			}
		}
	
	
	
	
	
	
	protected AbstractAction zoomToNewModuleTab(){
		return new AbstractAction("Zoom -> Module level") {
			final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.myViewer.getActiveViewer();
			public void actionPerformed(ActionEvent e) {
	
//				VisualSubGraph sub = new VisualSubGraph(activeViewer); \
				VisualGraph sub;
				List<VisualNode> parent = new ArrayList<VisualNode>();
				//List<VisualNode> nodes = null;
				//get parentNode
				
				for(final VisualNode node :pickedNodes) {
					System.out.println("PICKED NODES   "  + pickedNodes);
					parent.addAll(graph.getModule(node));
		//			sub.addVertex(node);
				}
//				for(VisualNode node : parent){
//					sub.addVertex(node);
//					
//				}
				//nodes = parent;
				//sub = graph.toGraph(nodes);
				
				VisualGraph GV = new VisualGraph(graph.toGraph(parent));
				sub =  new VisualGraph(GV);
				
				HecataeusViewer.myViewer.zoomToModuleTab(parent, sub);
			}
		};
	}
	
	protected AbstractAction getMenuDeleteNodes(){
		
		return new AbstractAction("Delete Nodes"){
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null,"Delete these Nodes?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				// response = yes
				if (response==0){
					for(final VisualNode other: pickedNodes) {
						graph.removeVertex(other);
						vv.repaint();
					}
				}
				else ;
			}
		};
	}
	
	protected AbstractAction getMenuEditNode(){
		return new AbstractAction("Edit Node") {
			public void actionPerformed(ActionEvent e) {
				pickedNodeState.pick(clickedVertex, false);
				final NameType edit = new NameType(true);
				edit.textFieldName.setText(clickedVertex.getName());
				edit.comboBoxType.setSelectedItem(clickedVertex.getType());
				edit.textSQLDef.setText(clickedVertex.getSQLDefinition().replace("\t", ""));
				edit.okButton.addActionListener(
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String name = edit.textFieldName.getText();
								if (!name.equals("")) {
									clickedVertex.setName(name);
									clickedVertex.setType((NodeType) edit.comboBoxType.getSelectedItem());
									clickedVertex.setSQLDefinition(edit.textSQLDef.getText());
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
		};
	}
	
	

	/**
	 * @author eva
	 */
	
	protected AbstractAction getMenuSelectNode(VisualNode v){
		return new AbstractAction("Select Node") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * @author pmanousi
				 */
				System.out.println("SELECTED NODE    " + clickedVertex.getName() );
				viewer.epilegmenosKombos = clickedVertex;
				viewer.updateManagers();
				
			}
		};
	}
	
	protected AbstractAction getMenuDeleteNode(){
		return new AbstractAction("Delete Node") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null,
						"Delete this Node?", "",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				// response = yes
				if (response == 0) {
					graph.removeVertex(clickedVertex);
					vv.repaint();
				} else
					;

			}
		};
	}
	
	protected AbstractAction getMenuCreateNode(){
		return new AbstractAction("Create Node") {
			public void actionPerformed(ActionEvent e) {
				final NameType nameType = new NameType(true);
				nameType.okButton.addActionListener(
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String name = nameType.textFieldName.getText();
								if (!name.equals("")) {
									VisualNode newVertex = new VisualNode();
									newVertex.setName(name);
									newVertex.setType((NodeType) nameType.comboBoxType.getSelectedItem());
									graph.addVertex(newVertex);
									layout.setLocation(newVertex, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(pointClicked));
									nameType.dispose();
								}
								else {
									JOptionPane.showMessageDialog(null,"Set the name and type of the\n node before creating it!","Message",JOptionPane.WARNING_MESSAGE); 
								}
							} // end actionPerformed
						}); // end actionListener
			} //end actionPerformed 
		};
	}
	
	protected AbstractAction getMenuDeleteEdge(){
		return new AbstractAction("Delete Edge") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null,"Delete this Edge?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				// response = yes
				if (response==0){
					graph.removeEdge(clickedEdge);
					vv.repaint();
				}
				else ;
			}};
	}
	
	protected AbstractAction getMenuEditEdge(){
		return new AbstractAction("Edit Edge") {
			public void actionPerformed(ActionEvent e) {
				final NameType edit = new NameType(false);
				edit.textFieldName.setText(clickedEdge.getName());
				edit.comboBoxType.setSelectedItem(clickedEdge.getType());
				edit.okButton.addActionListener(
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String name = edit.textFieldName.getText();
								EdgeType type = (EdgeType) edit.comboBoxType.getSelectedItem();
								if (!name.equals("")) {
									clickedEdge.setName(name);
									clickedEdge.setType(type);
									vv.repaint();
									edit.dispose();
								}
								else {
									JOptionPane.showMessageDialog(null,"The name cannot be null!","Message",JOptionPane.WARNING_MESSAGE); 
								}

							} // end actionPerformed
						}); // end actionListener
				vv.repaint();
			}};
	}
	
	protected AbstractAction getMenuAddPolicy(){
		return new AbstractAction("Add Policy") {
			public void actionPerformed(ActionEvent e) {
				final AddPolicy Policyframe = new AddPolicy();
			}
		};
	}
	
	protected AbstractAction getMenuAddEvent(){
		return new AbstractAction("Add Event") {
			public void actionPerformed(ActionEvent e) {
				final AddEvent Eventframe = new AddEvent();
			}
		};
	}
	
	protected AbstractAction getMenuSetFrequency(){
		return new AbstractAction("Set Frequency") {
			public void actionPerformed(ActionEvent e) {
				for(final VisualNode startNode : pickedNodes) {
					try {
						int freq = new Integer(JOptionPane.showInputDialog(null, "Enter a frequency for: " +startNode.getName(), startNode.getFrequency()));
						startNode.setFrequency(freq);
						graph.propagateFrequency(startNode);
					} catch (NumberFormatException e1) {
					}
				}
			}
		};
	}
	
	protected AbstractAction getMenuSetPolicies(){
		return new AbstractAction("Set Policies") {
			public void actionPerformed(ActionEvent e) {
				// create a new JFrame to handle policies (edit,add and remove)
				final HecataeusNodePolicies policiesFrame = new HecataeusNodePolicies(
						vv, clickedVertex);
			}
		};
	}
	
	protected AbstractAction getMenuSetEvents(){
		return new AbstractAction("Set Events") {
			public void actionPerformed(ActionEvent e) {
				// create a new JFrame to handle events (edit,highlight,find prevailing policy,add and remove)
				final HecataeusNodeEvents eventsFrame = new HecataeusNodeEvents(vv, clickedVertex);
			}
		};
	}
	
	protected JMenu getMenuApplyEvent(){
		JMenu mnuApplyEvent = new JMenu("Apply Event");
		
		for (final EvolutionEvent<VisualNode> event: clickedVertex.getEvents()) {
			mnuApplyEvent.add(new AbstractAction(event.toString()) {
				public void actionPerformed(ActionEvent e) {
					// initialize the event
					//clear previous status of nodes if exist
					for (VisualNode u : graph.getVertices()) {
						u.setStatus(StatusType.NO_STATUS,true);
					}	
					for (VisualEdge edge : graph.getEdges()) {
						edge.setStatus(StatusType.NO_STATUS,true);
					}	
					//initialize algorithm
					graph.initializeChange(event);
					vv.repaint();
				}});
		}
		
		
		return mnuApplyEvent;
		 
	}
	
	
	protected AbstractAction getMenuDeletePolicy(){
		return new AbstractAction("Delete Policy") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null,"Delete All Policies?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				// response = yes
				if (response==0){
					for(final VisualNode node: pickedNodes) 
						node.getPolicies().clear();
				}
				vv.repaint();
			}
		};
	}
	
	protected AbstractAction getMenuShowModule(){
		return new AbstractAction("Show Module") {
			public void actionPerformed(ActionEvent e) {
				//set first invisible all graph
				for (final VisualNode node :graph.getVertices()) {
					node.setVisible(false);
				}
				//get parentNode
				for(final VisualNode other :pickedNodes) {
					List<VisualNode> subGraph =  graph.getModule(other);
					VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
					showAll.setVisibleLevel(subGraph,VisibleLayer.SEMANTICS);
				}
				vv.repaint();
			}
		};
	}
	
	protected AbstractAction getMenuSelectModule(){
		return new AbstractAction("Select Module") {
			public void actionPerformed(ActionEvent e) {
				//get parentNode
				for (final VisualNode node :pickedNodes) {
					List<VisualNode> subGraph = graph.getModule(node);
					for (final VisualNode aNode:subGraph) {
						pickedNodeState.pick(aNode, true);
					}; 

				}
			}
		};
	}
	
	protected AbstractAction getMenuShowInNewWindow(){
		return new AbstractAction("Show in new Tab") {
			public void actionPerformed(ActionEvent e) {
				VisualGraph subGraph = graph.toGraph(new ArrayList<VisualNode>(pickedNodes));
//				HecataeusViewer nvv = new HecataeusViewer(subGraph);
//				
//				 //set the layout of the graph
//				nvv.setLayout(VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
//				//pass layout's positions to the graph
//				nvv.setLayoutPositions();
//				nvv.centerAt(subGraph.getCenter());
				HecataeusViewer.myViewer.zoomToTab(subGraph);
		//		nvv.zoomToWindow();
				
			}
		};
	}
	
	protected JMenu getMenuCreateMultipleEdges(){
		JMenu directedMenu = new JMenu("Create Multiple Edges");
		directedMenu.add(new AbstractAction("[{selection}"+clickedVertex.getName()+"]") {
			public void actionPerformed(ActionEvent e) {
				final NameType nameType = new NameType(false);
				nameType.okButton.addActionListener(
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String name = nameType.textFieldName.getText();
								EdgeType type = (EdgeType)nameType.comboBoxType.getSelectedItem();
								if (!name.equals("")) {
									for(final VisualNode node:pickedNodes) {
										if (!node.equals(clickedVertex)) {
											VisualEdge newEdge = new VisualEdge(name,type,node, clickedVertex);
											graph.addEdge(newEdge);
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
		//add opposite direction
		directedMenu.add(new AbstractAction("["+clickedVertex.getName()+",{selection}]") {
			public void actionPerformed(ActionEvent e) {
				final NameType nameType = new NameType(false);
				nameType.okButton.addActionListener(
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {

								String name = nameType.textFieldName.getText();
								EdgeType type = (EdgeType)nameType.comboBoxType.getSelectedItem();
								if (!name.equals("")) {
									for(final VisualNode node:pickedNodes) {
										if (!node.equals(clickedVertex)) {
											VisualEdge newEdge = new VisualEdge(name, type, clickedVertex, node);
											graph.addEdge(newEdge);
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
		return directedMenu;
	}
	
	protected JMenu getMenuMetrics() {
		JMenu metricsMenu = new JMenu("Metrics");
		metricsMenu.add(new AbstractAction("Degree Total") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.degree(clickedVertex);
				JOptionPane.showMessageDialog(null,"Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Degree In") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.inDegree(clickedVertex);
				JOptionPane.showMessageDialog(null,"In Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Degree Out") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.outDegree(clickedVertex);
				JOptionPane.showMessageDialog(null,"Out Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Transitive Degree In") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.inTransitiveDegree((EvolutionNode)clickedVertex);
				JOptionPane.showMessageDialog(null,"Transitive In Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Transitive Degree Out") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.outTransitiveDegree((EvolutionNode)clickedVertex);
				JOptionPane.showMessageDialog(null,"Transitive Out Degree: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});

		metricsMenu.add(new AbstractAction("Strength Total") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.strength((EvolutionNode)clickedVertex,(EvolutionGraph) graph);
				JOptionPane.showMessageDialog(null,"Total Strength: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Strength In") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.inStrength((EvolutionNode)clickedVertex,(EvolutionGraph) graph);
				JOptionPane.showMessageDialog(null,"In Strength: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Strength Out") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.outStrength(clickedVertex, graph);
				JOptionPane.showMessageDialog(null,"Out Strength: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Weighted Degree In") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.inWeightedDegree((EvolutionNode)clickedVertex);
				JOptionPane.showMessageDialog(null,"Weighted Degree: " + metric  ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		metricsMenu.add(new AbstractAction("Weighted Strength In") {
			public void actionPerformed(ActionEvent e) {
				int metric = HecataeusMetricManager.inWeightedStrength((EvolutionNode)clickedVertex,(EvolutionGraph) graph);
				JOptionPane.showMessageDialog(null,"Weighted Strength In: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);

			}
		});
		metricsMenu.add(new AbstractAction("Entropy") {
			public void actionPerformed(ActionEvent e) {
				double metric = HecataeusMetricManager.entropyOutPerNode((EvolutionNode)clickedVertex,(EvolutionGraph) graph);
				JOptionPane.showMessageDialog(null,"Entropy In: " + metric ,"Metric Manager",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		return metricsMenu;
	}
	
	public class NameType extends JFrame {

		protected JTextField textFieldName;
		protected JComboBox comboBoxType;
		protected JButton okButton;
		protected JTextArea textSQLDef;

		public NameType(Boolean flagForNode){
			super("Set Name and Type");
			setSize(400,250);
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
				for (NodeType nodeType: NodeType.values()){
					comboBoxType.addItem(nodeType);
				}
			else 
				for (EdgeType edgeType: EdgeType.values()){
					comboBoxType.addItem(edgeType);
				}
			gridbag.setConstraints(comboBoxType,constraints);
			pane.add(comboBoxType);

			if (flagForNode) 
			{
				// label for SQL Definition
				constraints.reset(0,2,1,1,0,40);
				constraints.fill = GridBagConstraints.NONE;
				constraints.anchor = GridBagConstraints.EAST;
				JLabel labelSQLDef = new JLabel("Set SQL Definition: ",JLabel.LEFT);
				gridbag.setConstraints(labelSQLDef,constraints);
				pane.add(labelSQLDef);

				// text field SQL Definition
				constraints.reset(1,2,1,1,90,0);
				constraints.fill = GridBagConstraints.HORIZONTAL;
				textSQLDef = new JTextArea();
				//textSQLDef.setSize(90, 100);
				gridbag.setConstraints(textSQLDef,constraints);
				pane.add(textSQLDef);
			};

			// OK button
			constraints.reset(0,3,2,1,0,20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			okButton = new JButton("OK");
			gridbag.setConstraints(okButton,constraints);
			pane.add(okButton);

			// Cancel button
			constraints.reset(1,3,2,1,0,20);
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
			for (EventType eventType: EventType.values()) {
				comboBoxEventType.addItem(eventType);
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
			for (PolicyType policyType: PolicyType.values()){
				comboBoxPolicyType.addItem(policyType);
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
					EventType eventType = (EventType) comboBoxEventType.getSelectedItem();

					PolicyType policyType = (PolicyType) comboBoxPolicyType.getSelectedItem();

					// create and add selected policy
					for( VisualNode other: pickedNodes) {
						if ((eventType==EventType.ADD_ATTRIBUTE) 
/** @author pmanousi						||(eventType==EventType.ADD_CONDITION)
								||(eventType==EventType.MODIFY_CONDITION)
								||(eventType==EventType.RENAME_RELATION)
								||(eventType==EventType.DELETE_CONDITION)
								||(eventType==EventType.DELETE_RELATION)*/
						){for (VisualEdge edge :  other.getOutEdges()) {
							if (edge.isProvider()) {
								other.addPolicy(eventType/*,edge.getToNode()*/,policyType);
							}
						}
						}else{
							other.addPolicy(eventType/*,other*/,policyType);
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
			for (EventType eventType: EventType.values()) {
				comboBoxEventType.addItem(eventType);
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
					EventType eventType = (EventType) comboBoxEventType.getSelectedItem();

					// create and add selected policy
					for(final VisualNode other: pickedNodes) {
						other.addEvent(eventType);
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

