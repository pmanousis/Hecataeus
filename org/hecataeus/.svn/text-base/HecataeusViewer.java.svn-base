/**
 * @author George Papastefanatos, @affiliation National Technical University of Athens
 * @author Fotini Anagnostou, @affiliation National Technical University of Athens
 */
package org.hecataeus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.hecataeus.evolution.HecataeusEvolutionEvent;
import org.hecataeus.evolution.HecataeusEvolutionGraph;
import org.hecataeus.evolution.HecataeusEvolutionNode;
import org.hecataeus.evolution.HecataeusEvolutionNodes;
import org.hecataeus.evolution.HecataeusEvolutionPolicies;
import org.hecataeus.evolution.HecataeusEvolutionPolicy;
import org.hecataeus.evolution.HecataeusMetricManager;
import org.hecataeus.evolution.HecataeusNodeType;
import org.hecataeus.evolution.HecataeusPolicyType;
import org.hecataeus.evolution.HecataeusStatusType;
import org.hecataeus.jung.HecataeusJungEdge;
import org.hecataeus.jung.HecataeusJungEdgeColor;
import org.hecataeus.jung.HecataeusJungGraph;
import org.hecataeus.jung.HecataeusJungNode;
import org.hecataeus.jung.HecataeusJungNodeColor;
import org.hecataeus.jung.HecataeusJungNodeFont;
import org.hecataeus.jung.HecataeusJungNodeShape;
import org.hecataeus.jung.HecataeusJungNodeVisible;
import org.hecataeus.jung.HecataeusJungNodes;
import org.hecataeus.jung.HecataeusJungNodeVisible.VisibleLayer;
import org.hecataeus.parser.HecataeusSQLExtensionParser;
import org.hecataeus.parser.HecataeusSQLParser;

import samples.graph.MultiViewDemo;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.ToolTipFunctionAdapter;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.AbstractLayout;
import edu.uci.ics.jung.visualization.DefaultSettableVertexLocationFunction;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.SettableVertexLocationFunction;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;

public class HecataeusViewer extends JFrame {

	/**
	 * the visual component,the layout and renderer for the graph
	 */
	VisualizationViewer vv;

	AbstractLayout layout;
	static PluggableRenderer pr;

	/**
	 * the location of the vertices on the layout
	 */
	final SettableVertexLocationFunction vertexLocations;

	final JFrame frame;
	final Container content;
	protected HecataeusJungGraph graph;
	private String curPath = "C:\\PHD\\Thesis\\Figures\\JPEGS\\chapter6";

	public HecataeusViewer(HecataeusJungGraph inGraph) {

		// allows the precise setting of initial vertex locations
		vertexLocations = new DefaultSettableVertexLocationFunction();

		// assign the graph
		this.graph = inGraph;

		// the frame of the application
		final String frameTitle = "HECATAEUS VERSION II";
		frame = new JFrame(frameTitle);
		content = frame.getContentPane();

		// the renderer of the graph
		final PluggableRenderer pr = new PluggableRenderer();
		this.setRenderer(pr);

		// the layout
		this.layout = new StaticLayout(graph);

		// the visualization viewer
		vv = new VisualizationViewer(layout, pr);
		vv.setBackground(Color.white);
		vv.setPickSupport(new ShapePickSupport());
		pr.setEdgeShapeFunction(new EdgeShape.QuadCurve());

		// pass properties to visualization viewer
		for (int i = 0; i < graph.getJungNodes().size(); i++) {
			HecataeusJungNode jungNode = graph.getJungNodes().get(i);
			// for each vertex pass the location in the layout
			vertexLocations.setLocation(jungNode, vv.inverseTransform(jungNode
					.getLocation()));
		}
		// initialize the layout
		layout.initialize(new Dimension(1500, 1500), vertexLocations);

		// the labels of the Vertices
		pr.setVertexStringer(new VertexStringer() {

			public String getLabel(ArchetypeVertex v) {
				return ((HecataeusJungNode) v).getName();
			}
		});
		// the fonts of the vertices
		HecataeusJungNodeFont vff = new HecataeusJungNodeFont(new Font(
				Font.SANS_SERIF, Font.PLAIN, 8));
		pr.setVertexFontFunction(vff);

		// the labels of the Edges
		pr.setEdgeStringer(new EdgeStringer() {

			public String getLabel(ArchetypeEdge e) {
				return ((HecataeusJungEdge) e).getName();
			}
		});

		// the panel for the graph
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		content.add(panel);
		final HecataeusModalGraphMouse graphMouse = new HecataeusModalGraphMouse();

		// the HecataeusGraphMouse will pass mouse event coordinates to the
		// vertexLocations function to set the locations of the vertices as
		// they are created
		vv.setGraphMouse(graphMouse);
		final HecataeusPopupGraphMousePlugin jungEditingPopupGraphMousePlugin = new HecataeusPopupGraphMousePlugin(
				vertexLocations);
		graphMouse.add(jungEditingPopupGraphMousePlugin);
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

		// call the setVertexPaintFunction to paint the nodes
		PickedState picked_state = vv.getPickedState();
		HecataeusJungNodeColor vpf = new HecataeusJungNodeColor(picked_state);
		pr.setVertexPaintFunction(vpf);

		// call the setEdgePaintFunction to paint the edges
		HecataeusJungEdgeColor epf = new HecataeusJungEdgeColor(picked_state);
		pr.setEdgePaintFunction(epf);

		// call the setVertexShapeFunction to set the shape of the nodes
		// according to their type
		HecataeusJungNodeShape vsf = new HecataeusJungNodeShape();
		pr.setVertexShapeFunction(vsf);

		// call the setNodeVisible to set the shape of the nodes according to
		// their type
		HecataeusJungNodeVisible vdf = new HecataeusJungNodeVisible();
		pr.setVertexIncludePredicate(vdf);
		
		vv.setToolTipFunction(new GraphToolTips());

		// scalling control
		final ScalingControl scaler = new CrossoverScalingControl();

		// create the menu "File" and add elements
		final JMenu menuFile = new JMenu("File");

		// //imports
		// menuFile.add(new AbstractAction("Open Graph in new Window") {
		// public void actionPerformed(ActionEvent e) {
		// try {
		// HecataeusViewer nvv = new HecataeusViewer(graph);
		// } catch (RuntimeException e1) {
		// e1.printStackTrace();
		// }
		//
		// }
		// });

		menuFile.add(new AbstractAction("Import Graph") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					graph.clear_graph();
					frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					graph = graph.importFromXML(file);
					frame.setTitle(frameTitle + " - " + file.getName());
					redrawGraph();
					frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					curPath = file.getAbsolutePath();

				}
			}
		});

		menuFile.add(new AbstractAction("Import graph from Script") {
			public void actionPerformed(ActionEvent e) {
				final FileChooser selectFrame = new FileChooser(frame);

			}
		});

		menuFile.add(new AbstractAction("Add graph") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					addGraphFromXML(file);
					curPath = file.getAbsolutePath();
				}
			}
		});

		menuFile.addSeparator();

		menuFile.add(new AbstractAction("Export graph") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);

				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showSaveDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {

					File file = chooser.getSelectedFile();

					// get the current location of each node
					for (int i = 0; i < graph.getJungNodes().size(); i++) {
						HecataeusJungNode v = graph.getJungNodes().get(i);
						Point2D screenLocation = vv.transform(layout
								.getLocation(v));
						v.setLocation(screenLocation.getX(), screenLocation
								.getY());
					}
					if (file.exists()) {
						int response = JOptionPane
								.showConfirmDialog(
										null,
										"The file will be overriden! Do you agree? Answer with y or n",
										"Warning!", JOptionPane.YES_NO_OPTION,
										JOptionPane.WARNING_MESSAGE);
						if (response == 0)
							try {
								frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
								graph.exportToXML(file);
								frame.setCursor(new Cursor(
										Cursor.DEFAULT_CURSOR));
								frame.setTitle(frameTitle + " - "
										+ file.getName());
								JOptionPane.showMessageDialog(null,
										"The file was created successfully",
										"Information",
										JOptionPane.INFORMATION_MESSAGE);
							} catch (RuntimeException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						else
							;
					} else {
						try {
							frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
							graph.exportToXML(file);
							frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							frame.setTitle(frameTitle + " - " + file.getName());
							JOptionPane.showMessageDialog(null,
									"The file was created successfully",
									"Information",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (RuntimeException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});

		menuFile.add(new AbstractAction("Save Image") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(curPath);
				int option = chooser.showSaveDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					writeJPEGImage(file);
					curPath = file.getAbsolutePath();
				}
			}
		});

		menuFile.addSeparator();

		menuFile.add(new AbstractAction("Clear Graph") {
			public void actionPerformed(ActionEvent e) {
				graph.clear_graph();
			}
		});

		menuFile.add(new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				// System.exit(0);
			}
		});

		JMenu menuMode = graphMouse.getModeMenu();

		/*
		 * Menu for setting different Layouts on the graph
		 */
		JMenu menuLayout = new JMenu("Layout");
		JMenu submenuLayout = new JMenu("Show");
		submenuLayout.add(new AbstractAction("Top-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.RELATION);

			}
		});
		submenuLayout.add(new AbstractAction("Mid-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.ATTRIBUTE);

			}
		});
		submenuLayout.add(new AbstractAction("Low-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.CONDITION);

			}
		});
		submenuLayout.addSeparator();
		submenuLayout.add(new AbstractAction("Nodes with Status") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.STATUS);

			}
		});
		submenuLayout.add(new AbstractAction("Nodes with Policy") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.POLICIES);
			}
		});
		submenuLayout.add(new AbstractAction("Nodes with Event") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.EVENTS);
			}
		});
		menuLayout.add(submenuLayout);

		menuLayout.add(new AbstractAction("Hierarchical") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.CONDITION);
				graph.setHierarchicalLayout();
				redrawGraph();
			}
		});
		menuLayout.add(new AbstractAction("Tree") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				HecataeusJungNodeVisible showAll = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getJungNodes(),
						VisibleLayer.CONDITION);
				graph.setTreeLayout();
				redrawGraph();
			}
		});

		final JMenu menuFind = new JMenu("Find");
		menuFind.setMnemonic(KeyEvent.VK_F);
		menuFind.add(new AbstractAction("Find Node") {
			public void actionPerformed(ActionEvent e) {
				// obtain user input from JOptionPane input dialogs
				String nodeNames = JOptionPane
						.showInputDialog("The name of nodes to find (separated with ,): ");
				StringTokenizer token = new StringTokenizer("");
				if (nodeNames != null) {
					token = new StringTokenizer(nodeNames);
				}
				HecataeusJungNode v = null;

				while (token.hasMoreTokens()) {
					String name = token.nextToken(",");
					for (int i = 0; i < graph.getJungNodes().size(); i++) {
						HecataeusJungNode u = graph.getJungNodes().get(i);
						if (u.getName().equals(name.trim().toUpperCase())
								&& u.getVisible()) {
							PickedState pickedState = vv.getPickedState();
							pickedState.pick(u, true);
							v = u;
						}
					}
				}
				if (v != null)
					zoomToNode(v);
			}
		});

		menuFind.add(new AbstractAction("Find Edge") {
			public void actionPerformed(ActionEvent e) {
				// obtain user input from JOptionPane input dialogs
				String name = JOptionPane
						.showInputDialog("The name of the edge to find: ");
				for (int i = 0; i < graph.getJungEdges().size(); i++) {
					HecataeusJungEdge u = graph.getJungEdges().get(i);
					if (u.getName().equals(name)) {
						PickedState pickedState = vv.getPickedState();
						pickedState.pick(u, true);
					}
				}
			}
		});

		JMenu menuZoom = new JMenu("Zoom");
		menuZoom.add(new AbstractAction("Zoom in") {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());

			}
		});
		menuZoom.add(new AbstractAction("Zoom out") {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});
		menuZoom.add(new AbstractAction("Reset Zoom") {
			public void actionPerformed(ActionEvent e) {
				vv.getLayoutTransformer().setToIdentity();

			}
		});

		JMenu menuEvents = new JMenu("Manage");
		menuEvents.add(new AbstractAction("Events") {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_EVENT);
			}
		});
		menuEvents.add(new AbstractAction("Policies") {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_POLICY);
			}
		});
		menuEvents.add(new AbstractAction("Import Policy File") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("txt");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					HecataeusSQLExtensionParser parser = new HecataeusSQLExtensionParser(
							graph.getHecataeusEvolutionGraph(), file);
					try {
						parser.processFile();
					} catch (HecataeusException ex) {
						final showMessage p = new showMessage(
								"Error importing Policies", ex.getMessage());
					}
					vv.repaint();
					curPath = file.getAbsolutePath();
				}
			}
		});
		menuEvents.addSeparator();
		JMenu menuEventImpact = new JMenu("Output Events Flooding");
		menuEventImpact.add(new AbstractAction("Per Event") {
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				int countEvents = 0;
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode v = graph.getJungNodes().get(i);
					if (v.getHasEvents()) {
						for (int k = 0; k < v.getHecataeusEvolutionNode()
								.getEvents().size(); k++) {
							HecataeusEvolutionEvent event = v
									.getHecataeusEvolutionNode().getEvents()
									.get(k);
							countEvents++;
							msg += "Event " + countEvents + ": "
									+ event.getEventType().toString() + "\tOn "
									+ event.getEventNode().getName() + "\n";
							msg += "Module\tNode Name\tNode Type\tStatus\n";
							// set status =NO_Status
							for (int j = 0; j < graph.getJungNodes().size(); j++) {
								HecataeusJungNode n = graph.getJungNodes().get(
										j);
								n.getHecataeusEvolutionNode().setStatus(
										HecataeusStatusType.NO_STATUS);
							}
							// run algo
							graph.getHecataeusEvolutionGraph()
									.initializeChange(event);
							// output
							for (int l = 0; l < graph.getJungNodes().size(); l++) {
								HecataeusJungNode node = graph.getJungNodes()
										.get(l);
								if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
										|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
										|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
									HecataeusEvolutionNodes nodes = graph
											.getHecataeusEvolutionGraph()
											.getSubGraph(
													node
															.getHecataeusEvolutionNode());
									for (int s = 0; s < nodes.size(); s++) {
										HecataeusEvolutionNode evNode = nodes
												.get(s);
										if (evNode.getStatus() != HecataeusStatusType.NO_STATUS) {
											msg += graph
													.getHecataeusEvolutionGraph()
													.getTopLevelNode(evNode)
													.getName()
													+ "\t"
													+ evNode.getName()
													+ "\t"
													+ evNode.getType()
															.toString()
													+ "\t"
													+ evNode.getStatus()
															.toString() + "\n";
										}
									}
								}
							}
							msg += "--------------------------------------------------------\t";
							msg += "--------------------------------------------------------\t";
							msg += "--------------------------------------------------------\t";
							msg += "--------------------------------------------------------\n";
						}
					}

				}
				final showMessage m = new showMessage("Output Events ", msg);
			}
		});
		menuEventImpact.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				int countEvents = 0;
				msg += "EventID\tEventtype\tEventNode\tNodekey\tModule\tNode Name\tNode Type\tStatus\n";

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode v = graph.getJungNodes().get(i);
					if (v.getHasEvents()) {
						for (int k = 0; k < v.getHecataeusEvolutionNode()
								.getEvents().size(); k++) {
							HecataeusEvolutionEvent event = v
									.getHecataeusEvolutionNode().getEvents()
									.get(k);
							countEvents++;
							String strEvent = "Event " + countEvents + "\t"
									+ event.getEventType().toString() + "\t"
									+ event.getEventNode().getName();
							// set status =NO_Status
							for (int j = 0; j < graph.getJungNodes().size(); j++) {
								HecataeusJungNode n = graph.getJungNodes().get(
										j);
								n.getHecataeusEvolutionNode().setStatus(
										HecataeusStatusType.NO_STATUS);
							}
							// run algo
							graph.getHecataeusEvolutionGraph()
									.initializeChange(event);
							// output
							for (int l = 0; l < graph.getJungNodes().size(); l++) {
								HecataeusJungNode node = graph.getJungNodes()
										.get(l);
								if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
										|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
										|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
									HecataeusEvolutionNodes nodes = graph
											.getHecataeusEvolutionGraph()
											.getSubGraph(
													node
															.getHecataeusEvolutionNode());
									for (int s = 0; s < nodes.size(); s++) {
										HecataeusEvolutionNode evNode = nodes
												.get(s);
										if (evNode.getStatus() != HecataeusStatusType.NO_STATUS) {
											msg += strEvent
													+ "\t"
													+ evNode.getKey()
													+ "\t"
													+ graph
															.getHecataeusEvolutionGraph()
															.getTopLevelNode(
																	evNode)
															.getName()
													+ "\t"
													+ evNode.getName()
													+ "\t"
													+ evNode.getType()
															.toString()
													+ "\t"
													+ evNode.getStatus()
															.toString() + "\n";
										}
									}
								}
							}
						}
					}

				}
				final showMessage m = new showMessage("Output Events ", msg);
			}
		});
		menuEvents.add(menuEventImpact);
		menuEvents.add(new AbstractAction("Clear All Policies") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane
						.showConfirmDialog(
								frame,
								"All policies on the graph will be deleted. Are you sure?",
								"Warning!", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (response == 0) {
					for (int i = 0; i < graph.getJungNodes().size(); i++) {
						HecataeusJungNode v = graph.getJungNodes().get(i);
						v.getHecataeusEvolutionNode().getPolicies().clear();

					}
					vv.repaint();
				}
			}
		});
		menuEvents.add(new AbstractAction("Clear All Events ") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane
						.showConfirmDialog(
								frame,
								"All events on the graph will be deleted. Are you sure?",
								"Warning!", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (response == 0) {
					if (response == 0) {
						for (int i = 0; i < graph.getJungNodes().size(); i++) {
							HecataeusJungNode v = graph.getJungNodes().get(i);
							v.getHecataeusEvolutionNode().getEvents().clear();

						}
						vv.repaint();
					}
				}
			}
		});

		menuEvents.add(new AbstractAction("Clear All Statuses") {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					graph.getJungNodes().get(i).getHecataeusEvolutionNode()
							.setStatus(HecataeusStatusType.NO_STATUS);
				}
				for (int i = 0; i < graph.getJungEdges().size(); i++) {
					graph.getJungEdges().get(i).getHecataeusEvolutionEdge()
							.setStatus(HecataeusStatusType.NO_STATUS);
				}

				vv.repaint();
			}
		});

		menuEvents.add(new AbstractAction("Inverse Policies") {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode v = graph.getJungNodes().get(i);
					if (v.getHasPolicies()) {
						HecataeusEvolutionNode ev = v
								.getHecataeusEvolutionNode();
						for (int j = 0; j < ev.getPolicies().size(); j++) {
							HecataeusEvolutionPolicy p = ev.getPolicies()
									.get(j);
							switch (p.getPolicyType()) {
							case PROPAGATE:
								p.setPolicyType(HecataeusPolicyType.BLOCK);
								break;
							case BLOCK:
								p.setPolicyType(HecataeusPolicyType.PROPAGATE);
								break;
							default:
								p.setPolicyType(HecataeusPolicyType.PROMPT);
								break;
							}
						}
					}
				}
				vv.repaint();

			}
		});

		menuEvents.add(new AbstractAction("Propagate All") {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode v = graph.getJungNodes().get(i);
					if (v.getHasPolicies()) {
						HecataeusEvolutionNode ev = v
								.getHecataeusEvolutionNode();
						for (int j = 0; j < ev.getPolicies().size(); j++) {
							HecataeusEvolutionPolicy p = ev.getPolicies()
									.get(j);
							p.setPolicyType(HecataeusPolicyType.PROPAGATE);
						}
					}
				}
				vv.repaint();

			}
		});
		JMenu menuMetrics = new JMenu("Metrics");

		JMenu menuMetricCount = new JMenu("Count");
		menuMetricCount.add(new AbstractAction("Nodes") {
			public void actionPerformed(ActionEvent e) {

				// JOptionPane.showMessageDialog(content,
				// layout.getVisibleVertices().size());
				HecataeusJungNodeVisible p = (HecataeusJungNodeVisible) pr
						.getVertexIncludePredicate();
				int countNodes = 0;
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					if (p.evaluate(graph.getJungNodes().get(i)))
						countNodes++;
				}
				JOptionPane.showMessageDialog(content, countNodes);

			}
		});

		menuMetricCount.add(new AbstractAction("Policies") {
			public void actionPerformed(ActionEvent e) {
				int propagatePolicies = HecataeusMetricManager.countPolicies(
						graph.getHecataeusEvolutionGraph().getNodes(),
						HecataeusPolicyType.PROPAGATE);
				int blockPolicies = HecataeusMetricManager.countPolicies(graph
						.getHecataeusEvolutionGraph().getNodes(),
						HecataeusPolicyType.BLOCK);
				int promptPolicies = HecataeusMetricManager.countPolicies(graph
						.getHecataeusEvolutionGraph().getNodes(),
						HecataeusPolicyType.PROMPT);

				String message = "Propagate: " + propagatePolicies;
				message += "\nBlock: " + blockPolicies;
				message += "\nPrompt: " + promptPolicies;
				JOptionPane.showMessageDialog(content, message);

			}
		});
		menuMetricCount.add(new AbstractAction("Events") {
			public void actionPerformed(ActionEvent e) {
				int events = HecataeusMetricManager.countEvents(graph
						.getHecataeusEvolutionGraph().getNodes());
				JOptionPane.showMessageDialog(content, events);

			}
		});
		menuMetrics.add(menuMetricCount);

		JMenu menuMetricOutput = new JMenu("Output For All Nodes");
		menuMetricOutput.add(new AbstractAction("Degree Total") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager.degree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.inDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.outDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Transitive Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.inTransitiveDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Transitive Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tTranDegreeOut\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.outTransitiveDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Strength Total") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tStrength\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager.strength(
									evNode, graph.getHecataeusEvolutionGraph());
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}
					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Strength In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tStrength\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager.inStrength(
									evNode, graph.getHecataeusEvolutionGraph());
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}
					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Strength Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tStrength\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager.outStrength(
									evNode, graph.getHecataeusEvolutionGraph());
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}
					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Weighted Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tWeighted Degree In\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.inWeightedDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Weighted Strength In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tWeighted Strength In\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.inWeightedStrength(evNode, graph
											.getHecataeusEvolutionGraph());
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}
					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Policy Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tPDegree\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							HecataeusEvolutionNode evNode = nodes.get(j);
							int metric = HecataeusMetricManager
									.inPolicyTransitiveDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		/**
		 * debug reasons only
		 */
		menuMetricOutput.add(new AbstractAction("Policy Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tPDegreeOut\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						HecataeusEvolutionNodes nodes = graph
								.getHecataeusEvolutionGraph().getSubGraph(
										node.getHecataeusEvolutionNode());
						for (int j = 0; j < nodes.size(); j++) {
							int metric = 0;
							HecataeusEvolutionNode evNode = nodes.get(j);
							for (int k = 0; k < graph
									.getHecataeusEvolutionGraph().getNodes()
									.size(); k++) {
								for (int l = 0; l < graph
										.getHecataeusEvolutionGraph()
										.getNodes().get(k).getEvents().size(); l++) {
									HecataeusEvolutionEvent event = graph
											.getHecataeusEvolutionGraph()
											.getNodes().get(k).getEvents().get(
													l);
									metric += HecataeusMetricManager
											.outPolicyTransitiveDegree(evNode,
													event);
								}
							}
							message += evNode.getKey()
									+ "\t"
									+ graph.getHecataeusEvolutionGraph()
											.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}

					}
				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Entropy") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tEntropy In\n";
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					if ((node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
							|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
						double metric = HecataeusMetricManager
								.entropyOutPerNode(node
										.getHecataeusEvolutionNode(), graph
										.getHecataeusEvolutionGraph());
						message += node.getKey()
								+ "\t"
								+ graph
										.getHecataeusEvolutionGraph()
										.getTopLevelNode(
												node
														.getHecataeusEvolutionNode())
										.getName() + "\t" + node.getName()
								+ "\t'" + metric + "\n";
					}

				}
				final showMessage m = new showMessage("Graph Metrics", message);
			}
		});

		menuMetrics.add(menuMetricOutput);
		menuMetrics.addSeparator();

		menuMetrics.add(new AbstractAction("Entropy Of Graph") {
			public void actionPerformed(ActionEvent e) {
				float entropy = HecataeusMetricManager.entropyGraph(graph
						.getHecataeusEvolutionGraph());
				String message = "Entropy Of the Graph: " + entropy;
				JOptionPane.showMessageDialog(content, message);

			}
		});
		menuMetrics.add(new AbstractAction("Maximum Entropy") {
			public void actionPerformed(ActionEvent e) {
				float entropy = 0;
				HecataeusJungNode maxEntropyNode = null;
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					float nodeEntropy = HecataeusMetricManager
							.entropyOutPerNode(
									node.getHecataeusEvolutionNode(), graph
											.getHecataeusEvolutionGraph());
					if (nodeEntropy >= entropy) {
						maxEntropyNode = node;
						entropy = nodeEntropy;
					}
				}
				if (maxEntropyNode != null) {
					String message = "Node: " + maxEntropyNode.getName();
					message += "\nEntropy: " + entropy;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxEntropyNode);
				}
			}
		});
		JMenu menuMetricDegree = new JMenu("Maximum Degree");
		menuMetricDegree.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				int degree = 0;
				HecataeusJungNode maxDegreeNode = null;
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeDegree = HecataeusMetricManager.degree(node
							.getHecataeusEvolutionNode());
					if (nodeDegree >= degree) {
						maxDegreeNode = node;
						degree = nodeDegree;
					}
				}
				if (maxDegreeNode != null) {
					String message = "Node: " + maxDegreeNode.getName();
					message += "\nDegree: " + degree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxDegreeNode);
				}
			}
		});
		menuMetricDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int degree = 0;
				HecataeusJungNode maxDegreeNode = null;
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeDegree = HecataeusMetricManager.inDegree(node
							.getHecataeusEvolutionNode());
					if (nodeDegree >= degree) {
						maxDegreeNode = node;
						degree = nodeDegree;
					}
				}
				if (maxDegreeNode != null) {
					String message = "Node: " + maxDegreeNode.getName();
					message += "\nIn Degree: " + degree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxDegreeNode);
				}

			}
		});
		menuMetricDegree.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				int degree = 0;
				HecataeusJungNode maxDegreeNode = null;
				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeDegree = HecataeusMetricManager.outDegree(node
							.getHecataeusEvolutionNode());
					if (nodeDegree >= degree) {
						maxDegreeNode = node;
						degree = nodeDegree;
					}
				}
				if (maxDegreeNode != null) {
					String message = "Node: " + maxDegreeNode.getName();
					message += "\nOut Degree: " + degree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxDegreeNode);
				}

			}
		});
		menuMetrics.add(menuMetricDegree);

		JMenu menuMetricStrength = new JMenu("Maximum Strength");
		menuMetricStrength.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				int strength = 0;
				HecataeusJungNode maxStrengthNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeStrength = HecataeusMetricManager.strength(node
							.getHecataeusEvolutionNode(), graph
							.getHecataeusEvolutionGraph());
					if (nodeStrength >= strength) {
						maxStrengthNode = node;
						strength = nodeStrength;
					}
				}

				if (maxStrengthNode != null) {
					String message = "Node: " + maxStrengthNode.getName();
					message += "\nDegree: " + strength;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxStrengthNode);
				}
			}
		});
		menuMetricStrength.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int strength = 0;
				HecataeusJungNode maxStrengthNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeStrength = HecataeusMetricManager.inStrength(node
							.getHecataeusEvolutionNode(), graph
							.getHecataeusEvolutionGraph());
					if (nodeStrength >= strength) {
						maxStrengthNode = node;
						strength = nodeStrength;
					}
				}

				if (maxStrengthNode != null) {
					String message = "Node: " + maxStrengthNode.getName();
					message += "\nDegree: " + strength;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxStrengthNode);
				}
			}
		});
		menuMetricStrength.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				int strength = 0;
				HecataeusJungNode maxStrengthNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeStrength = HecataeusMetricManager.outStrength(node
							.getHecataeusEvolutionNode(), graph
							.getHecataeusEvolutionGraph());
					if (nodeStrength >= strength) {
						maxStrengthNode = node;
						strength = nodeStrength;
					}
				}
				if (maxStrengthNode != null) {
					String message = "Node: " + maxStrengthNode.getName();
					message += "\nDegree: " + strength;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxStrengthNode);
				}
			}
		});
		menuMetrics.add(menuMetricStrength);

		JMenu menuWeightedDegree = new JMenu("Maximum Weighted Degree");
		menuWeightedDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int maxwDegree = 0;
				HecataeusJungNode maxWDegreeNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int wDegree = node.getHecataeusEvolutionNode()
							.getFrequency();
					if (node.getHecataeusEvolutionNode().getFrequency() >= maxwDegree) {
						maxWDegreeNode = node;
						maxwDegree = wDegree;
					}
				}
				if (maxWDegreeNode != null) {
					String message = "Node: " + maxWDegreeNode.getName();
					message += "\nWeighted Degree: " + maxwDegree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxWDegreeNode);
				}
			}
		});
		menuMetrics.add(menuWeightedDegree);

		JMenu menuTransitiveDegree = new JMenu("Maximum Transitive Degree");
		menuTransitiveDegree.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				int maxTDegree = 0;
				HecataeusJungNode maxTDegreeNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeDegree = HecataeusMetricManager
							.inTransitiveDegree(node
									.getHecataeusEvolutionNode())
							+ HecataeusMetricManager.outTransitiveDegree(node
									.getHecataeusEvolutionNode());
					if (nodeDegree >= maxTDegree) {
						maxTDegreeNode = node;
						maxTDegree = nodeDegree;
					}
				}

				if (maxTDegreeNode != null) {
					String message = "Node: " + maxTDegreeNode.getName();
					message += "\nDegree: " + maxTDegree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxTDegreeNode);
				}
			}
		});
		menuTransitiveDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int maxTDegree = 0;
				HecataeusJungNode maxTDegreeNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeDegree = HecataeusMetricManager
							.inTransitiveDegree(node
									.getHecataeusEvolutionNode());
					if (nodeDegree >= maxTDegree) {
						maxTDegreeNode = node;
						maxTDegree = nodeDegree;
					}
				}

				if (maxTDegreeNode != null) {
					String message = "Node: " + maxTDegreeNode.getName();
					message += "\nDegree: " + maxTDegree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxTDegreeNode);
				}
			}
		});
		menuTransitiveDegree.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				int maxTDegree = 0;
				HecataeusJungNode maxTDegreeNode = null;

				for (int i = 0; i < graph.getJungNodes().size(); i++) {
					HecataeusJungNode node = graph.getJungNodes().get(i);
					int nodeDegree = HecataeusMetricManager
							.outTransitiveDegree(node
									.getHecataeusEvolutionNode());
					if (nodeDegree >= maxTDegree) {
						maxTDegreeNode = node;
						maxTDegree = nodeDegree;
					}
				}

				if (maxTDegreeNode != null) {
					String message = "Node: " + maxTDegreeNode.getName();
					message += "\nDegree: " + maxTDegree;
					JOptionPane.showMessageDialog(content, message);
					zoomToNode(maxTDegreeNode);
				}
			}
		});
		menuMetrics.add(menuTransitiveDegree);

		JMenu menuHelp = new JMenu("Help");
		menuHelp.add(new AbstractAction("Contents") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Help
				 */
				final showMessage p = new showMessage("Credits", "resources\\briefhelp.html", showMessage.HTML_FILE);
			}
		});
		menuHelp.add(new AbstractAction("Color Index") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Color Inedx
				 */
				final showMessage p = new showMessage("Color Index", "resources\\colorindex.html", showMessage.HTML_FILE);
			}
		});
		menuHelp.add(new AbstractAction("About Hecataeus II") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Credits
				 */
				final showMessage p = new showMessage("Credits", "resources\\credits.html", showMessage.HTML_FILE);
			}
		});

		// create the menu bar and add the menus
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuFile);
		menuBar.add(menuMode);
		menuBar.add(menuLayout);
		menuBar.add(menuFind);
		menuBar.add(menuZoom);
		menuBar.add(menuEvents);
		menuBar.add(menuMetrics);
		menuBar.add(menuHelp);
		frame.add(panel);
		frame.setJMenuBar(menuBar);

		frame.add(panel);
		frame.setJMenuBar(menuBar);

		frame
				.setExtendedState(frame.getExtendedState()
						| JFrame.MAXIMIZED_BOTH);

		// frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * gets an HecataeusEvolutionGraph and visualizes it
	 */
	protected void redrawGraph() {

		// pass properties to visualization viewer
		for (int i = 0; i < graph.getJungNodes().size(); i++) {

			HecataeusJungNode jungNode = graph.getJungNodes().get(i);
			// for each vertex pass the location in the layout
			vertexLocations.setLocation(jungNode, vv.inverseTransform(jungNode
					.getLocation()));
		}
		vv.getModel().restart();
		vv.repaint();

	}

	public PluggableRenderer getRenderer() {
		return pr;
	}

	public void setRenderer(Renderer pr) {
		this.pr = (PluggableRenderer) pr;
	}

	/**
	 * imports the graph represented in the xml file and keeps the graph on the
	 * layout
	 * 
	 * @param file
	 */
	public void addGraphFromXML(File file) {

		int maxKey = graph.getHecataeusEvolutionGraph().getKeyGenerator();
		System.out.println("maxKey: " + maxKey);

		// add the nodes
		ArrayList<HecataeusJungNode> nodes = graph.importNodesFromXML(file);
		for (int i = 0; i < nodes.size(); i++) {
			HecataeusJungNode newVertex = nodes.get(i);
			vertexLocations.setLocation(newVertex, vv
					.inverseTransform(newVertex.getLocation()));
			Layout layout = vv.getGraphLayout();

			for (Iterator iterator = graph.getVertices().iterator(); iterator
					.hasNext();) {
				layout.lockVertex((Vertex) iterator.next());
			}
			String key = newVertex.getKey();
			System.out.println("Key: " + key);
			graph.add_node(newVertex);
			newVertex.setKey((new Integer((new Integer(key)) + maxKey))
					.toString());
			System.out.println("vertexKey: " + newVertex.getKey());

			vv.getModel().restart();
			for (Iterator iterator = graph.getVertices().iterator(); iterator
					.hasNext();) {
				layout.unlockVertex((Vertex) iterator.next());
			}
			vv.repaint();
		}

		// add the edges
		ArrayList<HecataeusJungEdge> edges = graph.addSubgraphEdgesFromXML(
				file, maxKey);
		for (int i = 0; i < edges.size(); i++) {
			HecataeusJungEdge newEdge = edges.get(i);
			System.out.println("FromNodeNew: " + newEdge.getFromNode().getKey()
					+ ", ToNodeNew: " + newEdge.getToNode().getKey());
			graph.add_edge(newEdge);
		}

	}

	/**
	 * copy the visible part of the graph to a file as a jpeg image
	 * 
	 * @param file
	 */
	public void writeJPEGImage(File file) {
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		vv.paint(graphics);
		graphics.dispose();

		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * animates a zooming to a given Nodes
	 * 
	 * @param zoom:
	 *            node to zoom at
	 */
	public void zoomToNode(HecataeusJungNode zoom) {
		Point2D q = layout.getLocation(zoom);
		Point2D lvc = vv.inverseTransform(vv.getCenter());
		final double dx = (lvc.getX() - q.getX()) / 5;
		final double dy = (lvc.getY() - q.getY()) / 5;

		Runnable animator = new Runnable() {
			public void run() {
				for (int i = 0; i < 5; i++) {
					vv.getLayoutTransformer().translate(dx, dy);
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
					}
				}
			}
		};
		Thread thread = new Thread(animator);
		thread.start();

	}

	private class FileChooser extends JDialog {

		protected JTextField textFieldDDL;
		protected JTextField textFieldSQL;
		protected JButton buttonDDL;
		protected JButton buttonSQL;
		protected JButton okButton;

		public FileChooser(JFrame frame) {
			super(frame, "Choose DDL and SQL files", true);
			setSize(440, 110);
			setLocationRelativeTo(frame);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			GridBagLayout gridbag = new GridBagLayout();
			HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
			JPanel pane = new JPanel();
			pane.setLayout(gridbag);

			// label for DDL
			constraints.reset(0, 0, 1, 1, 10, 40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelName = new JLabel("DDL: ", JLabel.LEFT);
			gridbag.setConstraints(labelName, constraints);
			pane.add(labelName);

			// text field for DDL
			constraints.reset(1, 0, 1, 1, 90, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			textFieldDDL = new JTextField();
			gridbag.setConstraints(textFieldDDL, constraints);
			pane.add(textFieldDDL);

			// label for SQL
			constraints.reset(0, 1, 1, 1, 0, 40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelType = new JLabel("SQL: ", JLabel.LEFT);
			gridbag.setConstraints(labelType, constraints);
			pane.add(labelType);

			// text field for SQL
			constraints.reset(1, 1, 1, 1, 0, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			textFieldSQL = new JTextField();
			gridbag.setConstraints(textFieldSQL, constraints);
			pane.add(textFieldSQL);

			// buttonDDL
			constraints.reset(2, 0, 2, 1, 0, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			buttonDDL = new JButton("...");
			gridbag.setConstraints(buttonDDL, constraints);
			pane.add(buttonDDL);
			buttonDDL.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser(curPath);
					FileFilterImpl filter = new FileFilterImpl("sql",
							"DDL File");
					chooser.addChoosableFileFilter(filter);
					int option = chooser.showOpenDialog(content);
					if (option == JFileChooser.APPROVE_OPTION) {
						curPath = chooser.getSelectedFile().getPath();
						textFieldDDL.setText(curPath);

					}
				}
			});

			// buttonSQL
			constraints.reset(2, 1, 2, 1, 0, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			buttonSQL = new JButton("...");
			gridbag.setConstraints(buttonSQL, constraints);
			pane.add(buttonSQL);
			buttonSQL.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser(curPath);
					FileFilterImpl filter = new FileFilterImpl("sql",
							"SQL File");
					chooser.addChoosableFileFilter(filter);
					int option = chooser.showOpenDialog(content);
					if (option == JFileChooser.APPROVE_OPTION) {
						curPath = chooser.getSelectedFile().getPath();
						textFieldSQL.setText(curPath);
					}
				}
			});

			// OK button
			constraints.reset(1, 2, 2, 1, 0, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			okButton = new JButton("OK");
			gridbag.setConstraints(okButton, constraints);
			pane.add(okButton);
			okButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// clear the graph to visualize the new one
					graph.clear_graph();

					HecataeusSQLParser reader = new HecataeusSQLParser(
							textFieldDDL.getText(), textFieldSQL.getText());
					try {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						reader.processFile();
						HecataeusEvolutionGraph evGraph = reader.graphCreator.HGraph;
						// createJungGraph from EvolutionGraph
						graph.createJungGraphfromEvolution(evGraph);
						// sets vertices properties of JungGraph
						graph.setTreeLayout();
						redrawGraph();
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						dispose();

					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
						final showMessage p = new showMessage(
								"Error description", e1.getMessage());
						// JOptionPane.showMessageDialog(content,
						// e1.getMessage());;

					}
				}

			});

			// Cancel button
			constraints.reset(1, 2, 2, 1, 0, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JButton cancelButton = new JButton("Cancel");
			gridbag.setConstraints(cancelButton, constraints);
			cancelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			pane.add(cancelButton);

			setContentPane(pane);
			setVisible(true);

		}

	}

	private class FileFilterImpl extends FileFilter {

		private String TYPE_UNKNOWN = "Type Unknown";
		private String HIDDEN_FILE = "Hidden File";

		private Hashtable<String, FileFilterImpl> filters = null;
		private String description = null;
		private String fullDescription = null;
		private boolean useExtensionsInDescription = true;

		/**
		 * Creates a file filter. If no filters are added, then all files are
		 * accepted.
		 * 
		 * @see #addExtension
		 */
		public FileFilterImpl() {
			this.filters = new Hashtable<String, FileFilterImpl>();
		}

		/**
		 * Creates a file filter that accepts files with the given extension.
		 * Example: new ExampleFileFilter("jpg");
		 * 
		 * @see #addExtension
		 */
		public FileFilterImpl(String extension) {
			this(extension, null);
		}

		/**
		 * Creates a file filter that accepts the given file type. Example: new
		 * ExampleFileFilter("jpg", "JPEG Image Images");
		 * 
		 * Note that the "." before the extension is not needed. If provided, it
		 * will be ignored.
		 * 
		 * @see #addExtension
		 */
		public FileFilterImpl(String extension, String description) {
			this();
			if (extension != null)
				addExtension(extension);
			if (description != null)
				setDescription(description);
		}

		/**
		 * Creates a file filter from the given string array. Example: new
		 * ExampleFileFilter(String {"gif", "jpg"});
		 * 
		 * Note that the "." before the extension is not needed adn will be
		 * ignored.
		 * 
		 * @see #addExtension
		 */
		public FileFilterImpl(String[] filters) {
			this(filters, null);
		}

		/**
		 * Creates a file filter from the given string array and description.
		 * Example: new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG
		 * Images");
		 * 
		 * Note that the "." before the extension is not needed and will be
		 * ignored.
		 * 
		 * @see #addExtension
		 */
		public FileFilterImpl(String[] filters, String description) {
			this();
			for (int i = 0; i < filters.length; i++) {
				// add filters one by one
				addExtension(filters[i]);
			}
			if (description != null)
				setDescription(description);
		}

		/**
		 * Return true if this file should be shown in the directory pane, false
		 * if it shouldn't.
		 * 
		 * Files that begin with "." are ignored.
		 * 
		 * @see #getExtension
		 * @see FileFilter#accepts
		 */
		public boolean accept(File f) {
			if (f != null) {
				if (f.isDirectory()) {
					return true;
				}
				String extension = getExtension(f);
				if (extension != null && filters.get(getExtension(f)) != null) {
					return true;
				}
				;
			}
			return false;
		}

		/**
		 * Return the extension portion of the file's name .
		 * 
		 * @see #getExtension
		 * @see FileFilter#accept
		 */
		public String getExtension(File f) {
			if (f != null) {
				String filename = f.getName();
				int i = filename.lastIndexOf('.');
				if (i > 0 && i < filename.length() - 1) {
					return filename.substring(i + 1).toLowerCase();
				}
				;
			}
			return null;
		}

		/**
		 * Adds a filetype "dot" extension to filter against.
		 * 
		 * For example: the following code will create a filter that filters out
		 * all files except those that end in ".jpg" and ".tif":
		 * 
		 * ExampleFileFilter filter = new ExampleFileFilter();
		 * filter.addExtension("jpg"); filter.addExtension("tif");
		 * 
		 * Note that the "." before the extension is not needed and will be
		 * ignored.
		 */
		public void addExtension(String extension) {
			if (filters == null) {
				filters = new Hashtable<String, FileFilterImpl>(5);
			}
			filters.put(extension.toLowerCase(), this);
			fullDescription = null;
		}

		/**
		 * Returns the human readable description of this filter. For example:
		 * "JPEG and GIF Image Files (*.jpg, *.gif)"
		 * 
		 * @see setDescription
		 * @see setExtensionListInDescription
		 * @see isExtensionListInDescription
		 * @see FileFilter#getDescription
		 * @uml.property name="description"
		 */
		public String getDescription() {
			if (fullDescription == null) {
				if (description == null || isExtensionListInDescription()) {
					fullDescription = description == null ? "(" : description
							+ " (";
					// build the description from the extension list
					Enumeration<String> extensions = filters.keys();
					if (extensions != null) {
						fullDescription += "." + extensions.nextElement();
						while (extensions.hasMoreElements()) {
							fullDescription += ", ." + extensions.nextElement();
						}
					}
					fullDescription += ")";
				} else {
					fullDescription = description;
				}
			}
			return fullDescription;
		}

		/**
		 * Sets the human readable description of this filter. For example:
		 * filter.setDescription("Gif and JPG Images");
		 * 
		 * @see setDescription
		 * @see setExtensionListInDescription
		 * @see isExtensionListInDescription
		 * @uml.property name="description"
		 */
		public void setDescription(String description) {
			this.description = description;
			fullDescription = null;
		}

		/**
		 * Determines whether the extension list (.jpg, .gif, etc) should show
		 * up in the human readable description.
		 * 
		 * Only relevent if a description was provided in the constructor or
		 * using setDescription();
		 * 
		 * @see getDescription
		 * @see setDescription
		 * @see isExtensionListInDescription
		 */
		public void setExtensionListInDescription(boolean b) {
			useExtensionsInDescription = b;
			fullDescription = null;
		}

		/**
		 * Returns whether the extension list (.jpg, .gif, etc) should show up
		 * in the human readable description.
		 * 
		 * Only relevent if a description was provided in the constructor or
		 * using setDescription();
		 * 
		 * @see getDescription
		 * @see setDescription
		 * @see setExtensionListInDescription
		 */
		public boolean isExtensionListInDescription() {
			return useExtensionsInDescription;
		}
	}

	private final class NodeChooser extends JDialog {

		protected JComboBox comboBoxNode;
		protected JComboBox comboBoxChildNode;
		protected JComboBox comboBoxEvent;
		protected JButton okButton;
		final int mode = NodeChooser.FOR_EVENT;

		static final int FOR_EVENT = 1;
		static final int FOR_POLICY = 2;

		public NodeChooser(final int mode) {
			super(frame, "Select a node", true);
			setSize(440, 160);
			setLocationRelativeTo(frame);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			GridBagLayout gridbag = new GridBagLayout();
			HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
			JPanel pane = new JPanel();
			pane.setLayout(gridbag);

			// label for top level node
			constraints.reset(0, 0, 1, 1, 0, 40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel labelNode = new JLabel("Choose a top level node: ",
					JLabel.LEFT);
			gridbag.setConstraints(labelNode, constraints);
			pane.add(labelNode);

			// combo box for top level node
			comboBoxNode = new JComboBox();
			constraints.reset(1, 0, 2, 1, 0, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			gridbag.setConstraints(comboBoxNode, constraints);
			comboBoxNode.addItem("");
			// fill comboBox for Node selection
			for (int i = 0; i < graph.getJungNodes().size(); i++) {
				HecataeusJungNode node = graph.getJungNodes().get(i);
				if ((node.getType() == HecataeusNodeType.NODE_TYPE_RELATION)
						|| (node.getType() == HecataeusNodeType.NODE_TYPE_QUERY)
						|| (node.getType() == HecataeusNodeType.NODE_TYPE_VIEW)) {
					comboBoxNode.addItem(node.getName() + " ("
							+ node.getType().toString() + ") "
							+ (node.getKey()));
				}
			}
			// add action listener to fill childcombo
			comboBoxNode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// get key for parent node
					String parentNodeString = (String) comboBoxNode
							.getSelectedItem();
					if (parentNodeString != null
							&& parentNodeString.trim() != "") {
						StringTokenizer tokenizer = new StringTokenizer(
								parentNodeString);
						tokenizer.nextToken("("); // name
						tokenizer.nextToken(")"); // type
						// get parent key
						String parentKey = tokenizer.nextToken();
						// get parent node
						HecataeusJungNode parentNode = graph.getNode(parentKey
								.trim());
						// get descendant nodes
						HecataeusJungNodes subGraph = graph
								.getJungNodesFromEvolution(graph
										.getHecataeusEvolutionGraph()
										.getSubGraph(
												parentNode
														.getHecataeusEvolutionNode()));
						// reset child combo
						comboBoxChildNode.removeAllItems();
						for (int i = 0; i < subGraph.size(); i++) {
							HecataeusJungNode node = subGraph.get(i);
							comboBoxChildNode.addItem(node.getName() + " ("
									+ node.getType().toString() + ") "
									+ (node.getKey()));
						}
					}
				}
			});

			pane.add(comboBoxNode);

			// label for low level node
			constraints.reset(0, 1, 1, 1, 0, 40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			labelNode = new JLabel("Choose a child node: ", JLabel.LEFT);
			gridbag.setConstraints(labelNode, constraints);
			pane.add(labelNode);

			// combo box for low level node
			constraints.reset(1, 1, 2, 1, 0, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			comboBoxChildNode = new JComboBox();
			gridbag.setConstraints(comboBoxChildNode, constraints);
			pane.add(comboBoxChildNode);

			// OK button
			constraints.reset(1, 3, 1, 1, 100, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.CENTER;
			okButton = new JButton("OK");
			gridbag.setConstraints(okButton, constraints);
			// create the selected event on the selected node
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					// get key for node
					String nodeString = (String) comboBoxChildNode
							.getSelectedItem();
					if (nodeString != null && nodeString.trim() != "") {
						StringTokenizer tokenizer = new StringTokenizer(
								nodeString);
						tokenizer.nextToken("("); // name
						tokenizer.nextToken(")"); // type
						// get key
						String nodeKey = tokenizer.nextToken();
						// get node
						HecataeusJungNode node = graph.getNode(nodeKey.trim());
						if (mode == NodeChooser.FOR_EVENT) {
							HecataeusNodeEvents nde = new HecataeusNodeEvents(
									vv, node);
						}
						if (mode == NodeChooser.FOR_POLICY) {
							HecataeusNodePolicies ndp = new HecataeusNodePolicies(
									vv, node);
						}
						dispose();

					}
				}
			});// add actionListener for okButton
			pane.add(okButton);

			// Cancel button
			constraints.reset(2, 3, 1, 1, 100, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			JButton cancelButton = new JButton("Cancel");
			gridbag.setConstraints(cancelButton, constraints);
			cancelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			pane.add(cancelButton);

			setContentPane(pane);
			setVisible(true);
		}

	}

	/*
	 * class for setting tooltip on the graph
	 */
	private class GraphToolTips extends ToolTipFunctionAdapter {

		/**
		 * @param v
		 *            the Node
		 * @return getName() on the passed Node
		 */
		public String getToolTipText(Vertex v) {
			HecataeusJungNode node = (HecataeusJungNode) v;
			String tooltip;

			tooltip = "<html><i>Status: </i>"
					+ node.getHecataeusEvolutionNode().getStatus().toString();
			if (node.getHasPolicies()) {
				for (int i = 0; i < node.getHecataeusEvolutionNode()
						.getPolicies().size(); i++) {
					HecataeusEvolutionPolicy p = node
							.getHecataeusEvolutionNode().getPolicies().get(i);
					tooltip += "<br><i>Policy:</i> On "
							+ p.getSourceEvent().getEventType().toString()
							+ " To "
							+ p.getSourceEvent().getEventNode().getName()
							+ " Then " + p.getPolicyType().toString();
				}
			}
			if (node.getHasEvents()) {
				for (int i = 0; i < node.getHecataeusEvolutionNode()
						.getEvents().size(); i++) {
					HecataeusEvolutionEvent e = node
							.getHecataeusEvolutionNode().getEvents().get(i);
					tooltip += "<br><i>Event:</i> "
							+ e.getEventType().toString() + " To "
							+ e.getEventNode().getName();
				}
			}
			tooltip += "</html>";
			return tooltip;
		}

		/**
		 * @param e
		 *            the Edge
		 * @return getName() on the passed Edge
		 */
		public String getToolTipText(Edge e) {
			HecataeusJungEdge edge = (HecataeusJungEdge) e;
			String tooltip = "<html>" + edge.getFromNode().getName() + " <i>"
					+ edge.getName() + " </i>" + edge.getToNode().getName()
					+ "</html>";

			return tooltip;
		}

		public String getToolTipText(MouseEvent e) {
			return ((JComponent) e.getSource()).getToolTipText();
		}
	}

	/**
	 * a driver for this viewer
	 */
	public static void main(String[] args) {
		HecataeusViewer viewer = new HecataeusViewer(new HecataeusJungGraph());
	}

	// class for presenting a message
	private class showMessage extends JDialog {

		
		private static final long serialVersionUID = 1L;
		/**
		 * Constructs a new message object
		 * @param title : The title of the message window
		 * @param msg : The message shown
		 * @param type : A string for type of text , i.e. "text/plain", "text/html". Parameter is optional with default value "text/plain". 
		 */
		public showMessage(String title, String msg, String type) {
			super(frame, title, true);
			setSize(600,600);
			JPanel content = new JPanel();
			JEditorPane textField ;
			if (type.equals(this.HTML_FILE))
				try {
					textField = new JEditorPane();
					textField.setContentType(this.HTML_TEXT);
					textField.read(new FileReader(msg), null);
				} catch (IOException e) {
					textField = new JEditorPane(this.HTML_TEXT, "Help file "+ msg + " is missing");
				}
			else 
				textField = new JEditorPane(type, msg);
			
			
			textField.setEditable(false);
			JScrollPane pane = new JScrollPane(textField);
			content.setLayout(new BorderLayout());
	        content.add(pane, BorderLayout.CENTER);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setContentPane(content);
			this.setLocationRelativeTo(vv);
			this.setVisible(true);
		}
		
		/**
		 * Constructs a new message object with default "text/plain" text type
		 * @param title : The title of the message window
		 * @param msg : The message shown
		 */
		public showMessage(String title, String msg) {
			this(title, msg, showMessage.PLAIN_TEXT);
		}

		
		final static String PLAIN_TEXT = "text/plain";
		final static String HTML_TEXT = "text/html";
		final static String RTF_TEXT = "text/rtf";
		final static String HTML_FILE = "file/html";
			
	}
}
