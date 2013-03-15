/**
 * @author George Papastefanatos, @affiliation National Technical University of Athens
 * @author Fotini Anagnostou, @affiliation National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon; 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;


import edu.ntua.dblab.hecataeus.dao.HecataeusDatabaseSettings;
import edu.ntua.dblab.hecataeus.dao.HecataeusDatabaseType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualAggregateLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeToolTips;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualLayoutType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFont;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeShape;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeToolTips;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible.VisibleLayer;
import edu.ntua.dblab.hecataeus.metrics.HecataeusMetricManager;
import edu.ntua.dblab.hecataeus.parser.HecataeusSQLExtensionParser;
import edu.ntua.dblab.hecataeus.parser.HecataeusSQLParser;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.picking.LayoutLensShapePickSupport;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class HecataeusViewer{

	// a dummy counter for disposing or exiting application  
	private static int countOpenViewers = 0;
	
	// the frame of the application
	final HecataeusFrame frame  ;
	 
	final Container content;
	// the visual graph object
	protected VisualGraph graph;
	// the mouse object for handling mouse events 
	private final HecataeusModalGraphMouse graphMouse;
	// the scale object for zoom capabilities 
	private final ScalingControl scaler = new CrossoverScalingControl();
	
	protected VisualAggregateLayout layout ;
	
	// the visual component
	protected final VisualizationViewer<VisualNode, VisualEdge> vv;
	
	// the renderer of the graph
	RenderContext<VisualNode, VisualEdge>  pr;
	
	private static final String frameTitle = "HECATAEUS";
	private static final String frameIconUrl = "resources\\hecataeusIcon.png";
	private String curPath = ".";

	public HecataeusViewer(VisualGraph inGraph) {
		
		// assign the graph
		this.graph = inGraph;

		frame = new HecataeusFrame(frameTitle);
	 	frame.setIconImage(new ImageIcon(frameIconUrl).getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		content = frame.getContentPane();
		// the layout
		layout = new VisualAggregateLayout(graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		// the visualization viewer
        vv = new VisualizationViewer<VisualNode, VisualEdge>(layout);
		vv.setBackground(Color.white);
		vv.setPickSupport(new LayoutLensShapePickSupport<VisualNode, VisualEdge>(vv));
	
		pr = vv.getRenderContext();
		// the labels of the Vertices
		pr.setVertexLabelTransformer(new ToStringLabeller<VisualNode>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR); 
		
		// the fonts of the vertices
		VisualNodeFont vff = new VisualNodeFont(new Font(
				Font.SANS_SERIF, Font.PLAIN, 8));
		pr.setVertexFontTransformer(vff);

		//the shape of the edges
		pr.setEdgeShapeTransformer(new EdgeShape.QuadCurve<VisualNode, VisualEdge>());
		// the labels of the Edges
		pr.setEdgeLabelTransformer(new ToStringLabeller<VisualEdge>());
		 
		// call the setVertexPaintFunction to paint the nodes
		PickedState<VisualNode> picked_node_state = vv.getPickedVertexState();
		VisualNodeColor vpf = new VisualNodeColor(picked_node_state);
		pr.setVertexFillPaintTransformer(vpf);

		// call the setEdgePaintFunction to paint the edges
		PickedState<VisualEdge> picked_edge_state = vv.getPickedEdgeState();
		VisualEdgeColor epf = new VisualEdgeColor(picked_edge_state);
		pr.setEdgeFillPaintTransformer(epf);

		// call the setVertexShapeFunction to set the shape of the nodes
		// according to their type
		VisualNodeShape vsf = new VisualNodeShape();
		pr.setVertexShapeTransformer(vsf);

		// call the setNodeVisible to set the shape of the nodes according to
		// their type
		VisualNodeVisible vdf = new VisualNodeVisible();
		pr.setVertexIncludePredicate(vdf);
		
		graphMouse = new HecataeusModalGraphMouse();
		frame.setJMenuBar(this.getJMenuBar());
		
		vv.setGraphMouse(graphMouse);
		vv.addGraphMouseListener(graphMouse);
		
		// the scrollbars panel for the graph
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		content.add(panel);
					
		vv.setVertexToolTipTransformer(new VisualNodeToolTips());
		vv.setEdgeToolTipTransformer(new VisualEdgeToolTips());
		
		frame.pack();
		frame.setExtendedState(frame.getExtendedState()| JFrame.MAXIMIZED_BOTH);
	 	frame.setVisible(true);	
		
		countOpenViewers ++;
	}

	
	/***
	 * Constructs the Menu bar
	 * @return
	 */
	private JMenuBar getJMenuBar() {
		// create the menu bar and add the menus
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(this.getMenuFile());
		menuBar.add(this.getMenuMode());
		menuBar.add(this.getMenuLayout());
		menuBar.add(this.getMenuFind());
		menuBar.add(this.getMenuZoom());
		menuBar.add(this.getMenuManage());
		menuBar.add(this.getMenuMetrics());
		menuBar.add(this.getMenuHelp());
		return menuBar;
	}	
	private JMenu getMenuFile() {
		// Defines a new JMenu containing file operations
		 final JMenu menuFile = new JMenu("File");


		menuFile.add(new AbstractAction("New Graph from Script") {
			public void actionPerformed(ActionEvent e) {
				final FileChooser selectFrame = new FileChooser(frame);

			}
		});

		menuFile.add(new AbstractAction("New Graph from DB Connection") {
			public void actionPerformed(ActionEvent e) {
			
				final ConnectionChooser o = new ConnectionChooser();
							
			}
		});
		
		menuFile.add(new AbstractAction("Open Graph") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					graph.clear();
					frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					graph = graph.importFromXML(file);
					frame.setTitle(frameTitle + " - " + file.getName());
					// set the layout of the graph
					HecataeusViewer.this.setLayout(VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
					//pass the location of the vertices to the layout of the graph
					HecataeusViewer.this.setLayoutPositions();
					HecataeusViewer.this.centerAt(graph.getCenter());
					HecataeusViewer.this.zoomToWindow();
					frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					curPath = chooser.getSelectedFile().getPath();

				}
			}
		});
		
		
		menuFile.add(new AbstractAction("Append Files") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("sql",
						"SQL File");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					
					HecataeusSQLParser reader = new HecataeusSQLParser(graph);
					try {
						frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
						reader.processFile(file);
						graph = reader.getParsedGraph();
						// set the layout of the graph
						HecataeusViewer.this.setLayout(layout.getTopLayoutType(),layout.getSubLayoutType());
						//get new layout's positions
						HecataeusViewer.this.getLayoutPositions();	
						HecataeusViewer.this.centerAt(graph.getCenter());
						HecataeusViewer.this.zoomToWindow();
						
						frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		 
					} catch (IOException e1) {
						frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						final HecataeusMessageDialog p = new HecataeusMessageDialog(
								frame, "Error description: ", e1.getMessage());
					} catch (SQLException e1) {
						frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						final HecataeusMessageDialog p = new HecataeusMessageDialog(
								frame, "Error description: ", e1.getMessage());
					}
					curPath = chooser.getSelectedFile().getPath();
				}
			}
		});
		
		menuFile.add(new AbstractAction("Append Graph") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					graph = graph.importFromXML(file);
					// set the layout of the graph
					HecataeusViewer.this.setLayout(VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
					//pass the location of the vertices to the layout of the graph
					HecataeusViewer.this.setLayoutPositions();
					
					frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					curPath = chooser.getSelectedFile().getPath();
				}
			}
		});

		menuFile.addSeparator();

		menuFile.add(new AbstractAction("Save Graph") {
			public void actionPerformed(ActionEvent e) {
				// helps to choose a file

				JFileChooser chooser = new JFileChooser(curPath);

				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showSaveDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {

					String fileDescription = chooser.getSelectedFile().getAbsolutePath();
					if (!fileDescription.endsWith("xml"))
						fileDescription += ".xml";		
					
					File file = new File(fileDescription);

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
								//get the location of the vertices from the layout of the graph
								HecataeusViewer.this.getLayoutPositions();
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
							e1.printStackTrace();
						}
					}
					curPath = chooser.getSelectedFile().getPath();
				}
			}
		});

		menuFile.add(new AbstractAction("Save Graph as Image") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("jpg","Image File");

				chooser.addChoosableFileFilter(filter);
				if ((chooser.showSaveDialog(frame)) == JFileChooser.APPROVE_OPTION) {
					String fileDescription = chooser.getSelectedFile().getAbsolutePath();
					if (!fileDescription.endsWith(".jpg"))
						fileDescription += ".jpg";				
					writeJPEGImage(new File(fileDescription));
					curPath = chooser.getSelectedFile().getPath();
				}
			}
		});

		menuFile.addSeparator();

		menuFile.add(new AbstractAction("Clear Graph") {
			public void actionPerformed(ActionEvent e) {
				graph.clear();
				vv.repaint();
			}
		});

		menuFile.add(new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
					frame.dispose();
			}
		});
		
		return menuFile;
	}
	
	private JMenu getMenuMode() {
		JMenu menuMode = graphMouse.getModeMenu();
		return menuMode;
	}

	private JMenu getMenuLayout() {
		/*
		 * Menu for setting different Layouts on the graph
		 */
		JMenu menuLayout = new JMenu("Layout");
		JMenu submenuLayout = new JMenu("Show");
		submenuLayout.add(new AbstractAction("Top-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.MODULE);
				vv.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Mid-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.SCHEMA);
				vv.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Low-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.SEMANTICS);
				vv.repaint();
			}
		});
		submenuLayout.addSeparator();
		submenuLayout.add(new AbstractAction("Nodes with Status") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.STATUS);
				vv.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Nodes with Policy") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.POLICIES);
				vv.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Nodes with Event") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) pr.getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.EVENTS);
				vv.repaint();
			}
		});
		menuLayout.add(submenuLayout);
		
		submenuLayout = new JMenu("Algorithms");
		for (final VisualLayoutType layoutType : VisualLayoutType.values()) {
			submenuLayout.add(new AbstractAction(layoutType.toString()) {
				public void actionPerformed(ActionEvent e) {
					// update the top layout of the graph
					layout.setTopLayoutType(layoutType);
					//update the new layout's positions
					HecataeusViewer.this.getLayoutPositions();
					HecataeusViewer.this.centerAt(graph.getCenter());
					HecataeusViewer.this.zoomToWindow();
				}
			});
		}
		submenuLayout.addSeparator();
		submenuLayout.add(new AbstractAction("Revert") {
			public void actionPerformed(ActionEvent e) {
				//update the new layout's positions
				HecataeusViewer.this.getLayoutPositions();
				HecataeusViewer.this.centerAt(graph.getCenter());
				HecataeusViewer.this.zoomToWindow();
			}
		});
		menuLayout.add(submenuLayout);
		return menuLayout;
	}

	private JMenu getMenuFind() {
		final JMenu menuFind = new JMenu("Find");
		menuFind.setMnemonic(KeyEvent.VK_F);
		menuFind.add(new AbstractAction("Find Node") {
			public void actionPerformed(ActionEvent e) {
				// obtain user input from JOptionPane input dialogs
				String nodeNames = JOptionPane
						.showInputDialog( "The name of nodes to find (separated with ,): ");
				StringTokenizer token = new StringTokenizer("");
				if (nodeNames != null) {
					token = new StringTokenizer(nodeNames);
				}
				vv.getPickedVertexState().clear();
				VisualNode v = null;

				while (token.hasMoreTokens()) {
					String name = token.nextToken(",");
					for (VisualNode u : graph.getVertices()) {
						if (u.getName().equals(name.trim().toUpperCase())
								&& u.getVisible()) {
							vv.getPickedVertexState().pick(u, true);
							v = u;
						}
					}
				}
				if (v != null)
					centerAt(v.getLocation());
			}
		});

		menuFind.add(new AbstractAction("Find Edge") {
			public void actionPerformed(ActionEvent e) {
				// obtain user input from JOptionPane input dialogs
				String name = JOptionPane
						.showInputDialog("The name of the edge to find: ");
				vv.getPickedEdgeState().clear();
				for (VisualEdge edge: graph.getEdges()) {
					if (edge.getName().equals(name)) {
						vv.getPickedEdgeState().pick(edge, true);
					}
				}
			}
		});
		
		menuFind.add(new AbstractAction("Select All") {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode u : graph.getVertices()) {
					if (u.getVisible()) {
						vv.getPickedVertexState().pick(u, true);
					}
				}
			}
		});
		
		return menuFind;
	}
	
	private JMenu getMenuZoom() {
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
		menuZoom.add(new AbstractAction("Zoom in Window") {
			public void actionPerformed(ActionEvent e) {

		 			centerAt(graph.getCenter());
		 			
		 			zoomToWindow();

			}
		});

		return menuZoom;
	}
	
	private JMenu getMenuManage() {
		JMenu menuManage = new JMenu("Manage");
		menuManage.add(new AbstractAction("Events") {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_EVENT);
			}
		});
		menuManage.add(new AbstractAction("Policies") {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_POLICY);
			}
		});
		menuManage.add(new AbstractAction("Import Policy File") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(curPath);
				FileFilterImpl filter = new FileFilterImpl("txt");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					HecataeusSQLExtensionParser parser = new HecataeusSQLExtensionParser(graph, file);
					try {
						parser.processFile();
					} catch (HecataeusException ex) {
						final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Error importing Policies", ex.getMessage());
					}
					vv.repaint();
					
				}
			}
		});
		
		menuManage.add(new AbstractAction("Show Default Policies") {
			public void actionPerformed(ActionEvent e) {  
				
				String msg = "";
				
				for (int i = 0; i < graph.getDefaultPolicyDecsriptions().size(); i++) {
					msg += graph.getDefaultPolicyDecsriptions().get(i) + "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame,  "Default Policies", msg);
			}
		});
		
		menuManage.addSeparator();
		JMenu menuEventImpact = new JMenu("Output Events Flooding");
		menuEventImpact.add(new AbstractAction("Per Event") {
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				int countEvents = 0;
				for (VisualNode v : graph.getVertices()) {
					if (v.getHasEvents()) {
						for (EvolutionEvent event : v.getEvents()) {
							countEvents++;
							msg += "Event " + countEvents + ": "
									+ event.getEventType().toString() + "\tOn "
									+ event.getEventNode().getName() + "\n";
							msg += "Module\tNode Name\tNode Type\tStatus\n";
							// set status =NO_Status
							for (VisualNode n : graph.getVertices()) {
								n.setStatus(StatusType.NO_STATUS);
							}
							// run algo
							graph.initializeChange(event);
							// output
							for (VisualNode node : graph.getVertices(NodeCategory.MODULE)) {
								for (VisualNode evNode : graph.getModule(node)) {
									if (evNode.getStatus() != StatusType.NO_STATUS) {
										msg += graph.getTopLevelNode(evNode).getName()
										+ "\t"
										+ evNode.getName()
										+ "\t"
										+ evNode.getType().toString()
										+ "\t"
										+ evNode.getStatus().toString() + "\n";
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
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame,  "Output Events ", msg);
			}
		});
		menuEventImpact.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				int countEvents = 0;
				msg += "EventID\tEventtype\tEventNode\tNodekey\tModule\tNode Name\tNode Type\tStatus\n";

				for (VisualNode v : graph.getVertices()) {
					if (v.getHasEvents()) {
						for (EvolutionEvent event : v.getEvents()) {
							countEvents++;
							String strEvent = "Event " + countEvents + "\t"
									+ event.getEventType().toString() + "\t"
									+ event.getEventNode().getName();
							// set status =NO_Status
							for (VisualNode n : graph.getVertices()) {
								n.setStatus(StatusType.NO_STATUS);
							}
							// run algo
							graph.initializeChange(event);
							// output
							for (VisualNode node : graph.getVertices(NodeCategory.MODULE)) {
								for (VisualNode evNode : graph.getModule(node)) {
										if (evNode.getStatus() != StatusType.NO_STATUS) {
											msg += strEvent
													+ "\t"
													+ evNode.getKey()
													+ "\t"
													+ graph.getTopLevelNode(evNode).getName()
													+ "\t"
													+ evNode.getName()
													+ "\t"
													+ evNode.getType().toString()
													+ "\t"
													+ evNode.getStatus().toString() + "\n";
										}
									}
								
							}
						}
					}

				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Output Events ", msg);
			}
		});
		menuManage.add(menuEventImpact);
		menuManage.add(new AbstractAction("Clear All Policies") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane
						.showConfirmDialog(
								frame,
								"All policies on the graph will be deleted. Are you sure?",
								"Warning!", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (response == 0) {
					for (VisualNode v : graph.getVertices()) {
						v.getPolicies().clear();
					}
					//empty default policies definitions
					graph.getDefaultPolicyDecsriptions().clear();
					//repaint
					vv.repaint();
				}
			}
		});
		menuManage.add(new AbstractAction("Clear All Events ") {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane
						.showConfirmDialog(
								frame,
								"All events on the graph will be deleted. Are you sure?",
								"Warning!", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (response == 0) {
					if (response == 0) {
						for (VisualNode v : graph.getVertices()) {
							v.getEvents().clear();
						}
						vv.repaint();
					}
				}
			}
		});

		menuManage.add(new AbstractAction("Clear All Statuses") {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					v.setStatus(StatusType.NO_STATUS);
				}
				for (VisualEdge edge : graph.getEdges()) {
					edge.setStatus(StatusType.NO_STATUS);
				}

				vv.repaint();
			}
		});

		menuManage.add(new AbstractAction("Inverse Policies") {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					if (v.getHasPolicies()) {
						for (EvolutionPolicy p : v.getPolicies()) {
							switch (p.getPolicyType()) {
							case PROPAGATE:
								p.setPolicyType(PolicyType.BLOCK);
								break;
							case BLOCK:
								p.setPolicyType(PolicyType.PROPAGATE);
								break;
							default:
								p.setPolicyType(PolicyType.PROMPT);
								break;
							}
						}
					}
				}
				vv.repaint();

			}
		});

		menuManage.add(new AbstractAction("Propagate All") {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					if (v.getHasPolicies()) {
						for (EvolutionPolicy p : v.getPolicies()) {
							p.setPolicyType(PolicyType.PROPAGATE);
						}
					}
				}
				vv.repaint();

			}
		});
		return menuManage;
	}

	private JMenu getMenuMetrics() {
		JMenu menuMetrics = new JMenu("Metrics");

		JMenu menuMetricCount = new JMenu("Count");
		menuMetricCount.add(new AbstractAction("Nodes") {
			public void actionPerformed(ActionEvent e) {

				int countNodes = 0;
				for (VisualNode v : graph.getVertices()) {
					if (v.getVisible())
						countNodes++;
				}
				JOptionPane.showMessageDialog(content, countNodes);

			}
		});

		menuMetricCount.add(new AbstractAction("Policies") {
			public void actionPerformed(ActionEvent e) {
				int propagatePolicies = HecataeusMetricManager.countPolicies(graph.getVertices(), PolicyType.PROPAGATE);
				int blockPolicies = HecataeusMetricManager.countPolicies(graph.getVertices(), PolicyType.BLOCK);
				int promptPolicies = HecataeusMetricManager.countPolicies(graph.getVertices(),PolicyType.PROMPT);

				String message = "Propagate: " + propagatePolicies;
				message += "\nBlock: " + blockPolicies;
				message += "\nPrompt: " + promptPolicies;
				JOptionPane.showMessageDialog(content, message);

			}
		});
		menuMetricCount.add(new AbstractAction("Events") {
			public void actionPerformed(ActionEvent e) {
				int events = HecataeusMetricManager.countEvents(graph.getVertices());
				JOptionPane.showMessageDialog(content, events);

			}
		});
		menuMetrics.add(menuMetricCount);

		JMenu menuMetricOutput = new JMenu("Output For All Nodes");
		menuMetricOutput.add(new AbstractAction("Degree Total") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.degree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree Total", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.inDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.outDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree Out", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Transitive Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tDegree\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.inTransitiveDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Transitive Degree In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Transitive Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tTranDegreeOut\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.outTransitiveDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Transitive Degree Out" , message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Strength Total") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tStrength\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.strength(evNode, graph);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}
					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Strength Total", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Strength In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tStrength\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.inStrength(evNode, graph);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}
					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Strength In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Strength Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tStrength\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.outStrength(evNode, graph);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() + "\t" + metric
									+ "\n";
						}
					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Strength Out", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Weighted Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tWeighted Degree In\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE){
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.inWeightedDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Weighted Degree In", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Weighted Strength In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tWeighted Strength In\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE){
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.inWeightedStrength(evNode, graph);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}
					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Weighted Strength In", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Policy Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tPDegree\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						for (VisualNode evNode : graph.getModule(node)) {
							int metric = HecataeusMetricManager.inPolicyTransitiveDegree(evNode);
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Policy Degree In", message);
			}
		});
		/**
		 * debug reasons only
		 */
		menuMetricOutput.add(new AbstractAction("Policy Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tPDegreeOut\n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						List<VisualNode> nodes = graph.getModule(node);
						for (VisualNode evNode : nodes) {
							int metric = 0;
							for (VisualNode aNode : nodes) {
								for (EvolutionEvent anEvent: aNode.getEvents()) {
									metric += HecataeusMetricManager.outPolicyTransitiveDegree(evNode, anEvent);
								}
							}
							message += evNode.getKey()
									+ "\t"
									+ graph.getTopLevelNode(evNode).getName()
									+ "\t" + evNode.getName() 
									+ "\t" + metric
									+ "\n";
						}

					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Policy Degree Out", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Entropy") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tEntropy \n";
				for (VisualNode node: graph.getVertices()) {
					if (node.getType().getCategory() == NodeCategory.MODULE) {
						double metric = HecataeusMetricManager.entropyOutPerNode(node, graph);
						message += node.getKey()
								+ "\t"
								+ graph.getTopLevelNode(node).getName() 
								+ "\t" + node.getName()
								+ "\t'" + metric+ "\n";
					}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Entropy", message);
			}
		});

		menuMetrics.add(menuMetricOutput);
		menuMetrics.addSeparator();

		menuMetrics.add(new AbstractAction("Entropy Of Graph") {
			public void actionPerformed(ActionEvent e) {
				float entropy = HecataeusMetricManager.entropyGraph(graph);
				String message = "Entropy Of the Graph: " + entropy;
				JOptionPane.showMessageDialog(content, message);

			}
		});
		menuMetrics.add(new AbstractAction("Maximum Entropy") {
			public void actionPerformed(ActionEvent e) {
				float entropy = 0;
				VisualNode maxEntropyNode = null;
				for (VisualNode node: graph.getVertices()) {
					float nodeEntropy = HecataeusMetricManager.entropyOutPerNode(node, graph);
					if (nodeEntropy >= entropy) {
						maxEntropyNode = node;
						entropy = nodeEntropy;
					}
				}
				if (maxEntropyNode != null) {
					String message = "Node: " + maxEntropyNode.getName();
					message += "\nEntropy: " + entropy;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxEntropyNode.getLocation());
				}
			}
		});
		JMenu menuMetricDegree = new JMenu("Maximum Degree");
		menuMetricDegree.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				int degree = 0;
				VisualNode maxDegreeNode = null;
				VisualNode maxEntropyNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeDegree = HecataeusMetricManager.degree(node);
					if (nodeDegree >= degree) {
						maxDegreeNode = node;
						degree = nodeDegree;
					}
				}
				if (maxDegreeNode != null) {
					String message = "Node: " + maxDegreeNode.getName();
					message += "\nDegree: " + degree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxDegreeNode.getLocation());
				}
			}
		});
		menuMetricDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int degree = 0;
				VisualNode maxDegreeNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeDegree = HecataeusMetricManager.inDegree(node);
					if (nodeDegree >= degree) {
						maxDegreeNode = node;
						degree = nodeDegree;
					}
				}
				if (maxDegreeNode != null) {
					String message = "Node: " + maxDegreeNode.getName();
					message += "\nIn Degree: " + degree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxDegreeNode.getLocation());
				}

			}
		});
		menuMetricDegree.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				int degree = 0;
				VisualNode maxDegreeNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeDegree = HecataeusMetricManager.outDegree(node);
					if (nodeDegree >= degree) {
						maxDegreeNode = node;
						degree = nodeDegree;
					}
				}
				if (maxDegreeNode != null) {
					String message = "Node: " + maxDegreeNode.getName();
					message += "\nOut Degree: " + degree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxDegreeNode.getLocation());
				}

			}
		});
		menuMetrics.add(menuMetricDegree);

		JMenu menuMetricStrength = new JMenu("Maximum Strength");
		menuMetricStrength.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				int strength = 0;
				VisualNode maxStrengthNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeStrength = HecataeusMetricManager.strength(node,graph);
					if (nodeStrength >= strength) {
						maxStrengthNode = node;
						strength = nodeStrength;
					}
				}

				if (maxStrengthNode != null) {
					String message = "Node: " + maxStrengthNode.getName();
					message += "\nDegree: " + strength;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxStrengthNode.getLocation());
				}
			}
		});
		menuMetricStrength.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int strength = 0;
				VisualNode maxStrengthNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeStrength = HecataeusMetricManager.inStrength(node, graph);
					if (nodeStrength >= strength) {
						maxStrengthNode = node;
						strength = nodeStrength;
					}
				}

				if (maxStrengthNode != null) {
					String message = "Node: " + maxStrengthNode.getName();
					message += "\nDegree: " + strength;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxStrengthNode.getLocation());
				}
			}
		});
		menuMetricStrength.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				int strength = 0;
				VisualNode maxStrengthNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeStrength = HecataeusMetricManager.outStrength(node, graph);
					if (nodeStrength >= strength) {
						maxStrengthNode = node;
						strength = nodeStrength;
					}
				}
				if (maxStrengthNode != null) {
					String message = "Node: " + maxStrengthNode.getName();
					message += "\nDegree: " + strength;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxStrengthNode.getLocation());
				}
			}
		});
		menuMetrics.add(menuMetricStrength);

		JMenu menuWeightedDegree = new JMenu("Maximum Weighted Degree");
		menuWeightedDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int maxwDegree = 0;
				VisualNode maxWDegreeNode = null;
				for (VisualNode node: graph.getVertices()) {
					if (node.getFrequency() >= maxwDegree) {
						maxWDegreeNode = node;
						maxwDegree = node.getFrequency();
					}
				}
				if (maxWDegreeNode != null) {
					String message = "Node: " + maxWDegreeNode.getName();
					message += "\nWeighted Degree: " + maxwDegree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxWDegreeNode.getLocation());
				}
			}
		});
		menuMetrics.add(menuWeightedDegree);

		JMenu menuTransitiveDegree = new JMenu("Maximum Transitive Degree");
		menuTransitiveDegree.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				int maxTDegree = 0;
				VisualNode maxTDegreeNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeDegree = HecataeusMetricManager.inTransitiveDegree(node)
							+ HecataeusMetricManager.outTransitiveDegree(node);
					if (nodeDegree >= maxTDegree) {
						maxTDegreeNode = node;
						maxTDegree = nodeDegree;
					}
				}
				if (maxTDegreeNode != null) {
					String message = "Node: " + maxTDegreeNode.getName();
					message += "\nDegree: " + maxTDegree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxTDegreeNode.getLocation());
				}
			}
		});
		menuTransitiveDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				int maxTDegree = 0;
				VisualNode maxTDegreeNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeDegree = HecataeusMetricManager.inTransitiveDegree(node);
					if (nodeDegree >= maxTDegree) {
						maxTDegreeNode = node;
						maxTDegree = nodeDegree;
					}
				}

				if (maxTDegreeNode != null) {
					String message = "Node: " + maxTDegreeNode.getName();
					message += "\nDegree: " + maxTDegree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxTDegreeNode.getLocation());
				}
			}
		});
		menuTransitiveDegree.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				int maxTDegree = 0;
				VisualNode maxTDegreeNode = null;
				for (VisualNode node: graph.getVertices()) {
					int nodeDegree = HecataeusMetricManager.outTransitiveDegree(node);
					if (nodeDegree >= maxTDegree) {
						maxTDegreeNode = node;
						maxTDegree = nodeDegree;
					}
				}

				if (maxTDegreeNode != null) {
					String message = "Node: " + maxTDegreeNode.getName();
					message += "\nDegree: " + maxTDegree;
					JOptionPane.showMessageDialog(content, message);
					centerAt(maxTDegreeNode.getLocation());
				}
			}
		});
		menuMetrics.add(menuTransitiveDegree);

		/*menuMetrics.add(new AbstractAction("Evaluate Bipartite") {
			public void actionPerformed(ActionEvent e) {
//				VisualEdge isBipartite = graph.getEdge(HecataeusMetricManager.isBipartite(graph.getHecataeusEvolutionGraph()));
//				PickedState pickedState = vv.getPickedState();
//				pickedState.pick(isBipartite, true);
				boolean isBipartite = HecataeusMetricManager.isBipartite(graph.getHecataeusEvolutionGraph());
				String bipartite = (isBipartite? "Yes":"No");
				String message = "Is Graph Bipartite: " + bipartite;
				JOptionPane.showMessageDialog(content, message);
			}
		});	*/	
		
		return menuMetrics;
	}
	
	private JMenu getMenuHelp() {
		JMenu menuHelp = new JMenu("Help");
		menuHelp.add(new AbstractAction("Contents") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Help
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Credits", "resources\\briefhelp.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		menuHelp.add(new AbstractAction("Color Index") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Color Inedx
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Color Index", "resources\\colorindex.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		menuHelp.add(new AbstractAction("Import external policy file") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Import external policy file
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Instructions for importing policies from file", "resources\\ImportPolicyFile.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
//		menuHelp.add(new AbstractAction("Options") {
//			public void actionPerformed(ActionEvent e) {
//				/**
//				 * sets Options for Hecataeus
//				 */
//				//final ConnectionChooser p = new ConnectionChooser();
//			}
//		});
		menuHelp.addSeparator();
		
		menuHelp.add(new AbstractAction("About Hecataeus II") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Credits
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Credits", "resources\\credits.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		return menuHelp;
	}
	
	/**
	 * It passes the locations of the layout to the graph vertices
	 */
	protected void getLayoutPositions() {
		for (VisualNode node: graph.getVertices()) {
			node.setLocation(layout.transform(node));
		}
	}

	/**
	 * It passes the locations of the vertices to the layout of the graph 
	 */
	protected void setLayoutPositions() {
		for (VisualNode node: graph.getVertices()) {
			layout.setLocation(node, node.getLocation());
		}
	}
	
	/**
	 * sets the layout of the graph 
	 */
	protected void setLayout(VisualLayoutType topLayoutType, VisualLayoutType subLayoutType) {
		// pass the graph to the layout 
		layout.setGraph(graph);
		//set the module-level layout
		layout.setTopLayoutType(topLayoutType);
		//set the low-level layout
		layout.setSubLayoutType(subLayoutType);
	}
	
	/**
	 * Animated zoom out the graph till no vertex is out of screen view
	 */
	protected void zoomToWindow() {
		
		// pass properties to visualization viewer
		Runnable animator = new Runnable() {
			public void run() {
				Shape r = vv.getBounds();
				Point2D vvcenter = vv.getCenter();
				for (VisualNode jungNode: graph.getVertices()) {
					if (jungNode.getVisible()) {
						Point2D p = vv.getRenderContext().getMultiLayerTransformer().transform(jungNode.getLocation());
						while (!r.contains(p)) 
							{
							scaler.scale(vv, 1 / 1.1f, vvcenter);
							p = vv.getRenderContext().getMultiLayerTransformer().transform(jungNode.getLocation());
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) {
							}
						}
					}
								
					
				}
			}
		};
		Thread thread = new Thread(animator);
		thread.start();
		
		
	}
	
	/**
	 * Moves and centers the graph on the given point
	 * 
	 * @param Point to center at
	 */
	protected void centerAt(Point2D layoutPoint) {
		
		/*Point2D screenPoint = vv.getRenderContext().getMultiLayerTransformer().transform(Point2D layoutPoint);
		Point2D layoutPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Point2D screenPoint);
		 */
		vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
		Point2D vvCenter = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(vv.getCenter());
		final double dx = (vvCenter.getX() - layoutPoint.getX()) / 5;
		final double dy = (vvCenter.getY() - layoutPoint.getY()) / 5;

		Runnable animator = new Runnable() {
			public void run() {
				for (int i = 0; i < 5; i++) {
					vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
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

	/**
	 * copy the visible part of the graph to a file as a jpeg image
	 * 
	 * @param file
	 */
	private void writeJPEGImage(File file) {
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
	 * The main frame of the application
	 * @author gpapas
	 *
	 */
	private final class HecataeusFrame extends JFrame{
		
		public HecataeusFrame(String frametitle){
			super(frametitle);
		}
	
		public void dispose() {
			if (countOpenViewers>1){
				super.dispose();
				countOpenViewers--;}
			else 
				System.exit(0);
			
		};
	}
	
	
	/**
	 * Class for selecting input sql and DDl files 
	 * @author gpapas
	 *
	 */
	private class FileChooser extends JDialog {

		protected JTextField textFieldDDL;
		protected JTextField textFieldSQL;
		protected JButton buttonDDL;
		protected JButton buttonSQL;
		protected JButton okButton;

		public FileChooser(final JFrame frame) {
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
						textFieldDDL.setText(chooser.getSelectedFile().getPath());
						curPath = chooser.getSelectedFile().getPath();
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
						textFieldSQL.setText(chooser.getSelectedFile().getPath());
						curPath = chooser.getSelectedFile().getPath();
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
					graph.clear();

					HecataeusSQLParser reader = new HecataeusSQLParser();
					try {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						if (!textFieldDDL.getText().isEmpty())
							reader.processFile(new File(textFieldDDL.getText()));
						if (!textFieldSQL.getText().isEmpty())
							reader.processFile(new File(textFieldSQL.getText()));
						graph =  (VisualGraph) reader.getParsedGraph();
						//set the layout of the graph
						HecataeusViewer.this.setLayout(VisualLayoutType.HorizontalTopologicalLayout, VisualLayoutType.RadialTreeLayout);
						//get new layout's positions
						HecataeusViewer.this.getLayoutPositions();
						HecataeusViewer.this.centerAt(graph.getCenter());
						HecataeusViewer.this.zoomToWindow();
						
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						dispose();

					} catch (IOException e1) {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						e1.printStackTrace();
						final HecataeusMessageDialog p = new HecataeusMessageDialog(
								frame, "Error description: ", e1.getMessage());
					} catch (SQLException e1) {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						e1.printStackTrace();
						final HecataeusMessageDialog p = new HecataeusMessageDialog(
								frame, "Error description: ", e1.getMessage());
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

	/**
	 * Class for filtering files sql and DDL files in File chooser 
	 * @author gpapas
	 *
	 */
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
		 */
		public boolean isExtensionListInDescription() {
			return useExtensionsInDescription;
		}
	}

	/**
	 * Class for configuring a connection with a data source
	 * @author  gpapas
	 */
	private final class ConnectionChooser extends JDialog {
		protected JPanel connectionTab;
		
		protected JComboBox txtDbms;
		protected JTextField txtHost;
		protected JTextField txtDB;
		protected JTextField txtUsername;
		protected JPasswordField txtPassword;
		
		protected JButton btnOK;

		ConnectionChooser dialog = this;
		
		public ConnectionChooser() {
			super(frame, "Options", true);
			this.setSize(400,300);
			this.setTitle("Connect to a data source");
			this.setModal(true);
			this.setLocationRelativeTo(vv); 
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			// tab for setting db connection
			connectionTab = new JPanel();
			GridBagLayout gridbag = new GridBagLayout();
			HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
			connectionTab.setLayout(gridbag);
			
			//add border
			TitledBorder connBorder = BorderFactory.createTitledBorder("Configure Connection Properties");
		    connBorder.setTitleJustification(TitledBorder.LEFT);
		    connectionTab.setBorder(connBorder);
			
		    // label for selecting DBMS
			constraints.reset(0,0,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel lblDbms = new JLabel("DBMS: ",JLabel.LEFT);
			gridbag.setConstraints(lblDbms,constraints);
			connectionTab.add(lblDbms);
			
			
			// text field for selecting dbType name
			constraints.reset(1,0,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			txtDbms= new JComboBox();
			//fill enumerators for db types
			for (HecataeusDatabaseType dbType: HecataeusDatabaseType.values()){
				txtDbms.addItem(dbType.toString());
			}
			gridbag.setConstraints(txtDbms, constraints);
			connectionTab.add(txtDbms);
			
			// label for selecting hostname
			constraints.reset(0,1,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel lblHost = new JLabel("Host Name: ",JLabel.LEFT);
			gridbag.setConstraints(lblHost,constraints);
			connectionTab.add(lblHost);
			
			// text field for selecting hostname
			constraints.reset(1, 1, 2, 1, 90, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			txtHost= new JTextField("<Hostname>[:<port>]");
			txtHost.setSelectionStart(0);
			txtHost.setSelectionEnd(txtHost.getText().length());
			gridbag.setConstraints(txtHost, constraints);
			connectionTab.add(txtHost);
			
			// label for selecting service name
			constraints.reset(0,2,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel lblDb = new JLabel("Service Name: ",JLabel.LEFT);
			gridbag.setConstraints(lblDb,constraints);
			connectionTab.add(lblDb);
			
			// text field for selecting service name
			constraints.reset(1, 2, 2, 1, 90, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			txtDB= new JTextField("<DatabaseName>");
			txtDB.setSelectionStart(0);
			txtDB.setSelectionEnd(txtDB.getText().length());
			gridbag.setConstraints(txtDB, constraints);
			connectionTab.add(txtDB);
			
			// label for selecting username
			constraints.reset(0,3,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel lblUsername = new JLabel("Username: ",JLabel.LEFT);
			gridbag.setConstraints(lblUsername,constraints);
			connectionTab.add(lblUsername);
			
			// text field for selecting username
			constraints.reset(1, 3, 2, 1, 90, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			txtUsername= new JTextField("<UserName>");
			txtUsername.setSelectionStart(0);
			txtUsername.setSelectionEnd(txtUsername.getText().length());
			gridbag.setConstraints(txtUsername, constraints);
			connectionTab.add(txtUsername);
			
			// label for selecting password
			constraints.reset(0,4,1,1,0,40);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JLabel lblPassword = new JLabel("Password: ",JLabel.LEFT);
			gridbag.setConstraints(lblPassword,constraints);
			connectionTab.add(lblPassword);
			
			// text field for selecting password
			constraints.reset(1, 4, 2, 1, 90, 0);
			constraints.fill = GridBagConstraints.HORIZONTAL;
			txtPassword = new JPasswordField();
			txtPassword= new JPasswordField("<Password>");
			txtPassword.setSelectionStart(0);
			txtPassword.setSelectionEnd(txtPassword.getPassword().length);
			gridbag.setConstraints(txtPassword, constraints);
			connectionTab.add(txtPassword);
					
			// OK button
			constraints.reset(1, 5, 1, 1, 0, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.WEST;
			btnOK = new JButton("OK");
			gridbag.setConstraints(btnOK, constraints);
			connectionTab.add(btnOK);
			btnOK.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					HecataeusDatabaseSettings DBsettings = new HecataeusDatabaseSettings(
							HecataeusDatabaseType.toType(txtDbms.getSelectedItem().toString()),
							txtHost.getText().toString(), 
							txtDB.getText().toString(),
							txtUsername.getText().toString(),
							new String(txtPassword.getPassword()));
					
					try {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						HecataeusObjectChooser objChooser = new HecataeusObjectChooser(dialog, DBsettings, graph);
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						
					
						if (objChooser.getStatements().size()>0) {
							 // clear the graph to visualize the new one
							 graph.clear();
							 HecataeusSQLParser reader = new HecataeusSQLParser(graph);

							 setCursor(new Cursor(Cursor.WAIT_CURSOR));
							 for (String statement: objChooser.getStatements()) {
								 try {
									 reader.processSentence(statement);
								 }  catch (SQLException ex) {
									 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
									 throw new HecataeusException(ex.getMessage());
								 }
							 } 
							 //createJungGraph from EvolutionGraph
							 graph= (VisualGraph) reader.getParsedGraph();
							 //set the layout of the graph
							 HecataeusViewer.this.setLayout(VisualLayoutType.HorizontalTopologicalLayout, VisualLayoutType.RadialTreeLayout);
							//get new layout's positions
							 HecataeusViewer.this.getLayoutPositions();
							 HecataeusViewer.this.centerAt(graph.getCenter());
							 HecataeusViewer.this.zoomToWindow();
							 
							 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							 dispose();
						 }
						
					} catch (HecataeusException e2) {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						e2.printStackTrace();
						final HecataeusMessageDialog p = new HecataeusMessageDialog(
								frame, "Error description: ", e2.getMessage());
					}
					
				}

			});

			// Cancel button
			constraints.reset(2, 5, 1, 1, 0, 20);
			constraints.fill = GridBagConstraints.NONE;
			constraints.anchor = GridBagConstraints.EAST;
			JButton cancelButton = new JButton("Cancel");
			gridbag.setConstraints(cancelButton, constraints);
			cancelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			connectionTab.add(cancelButton);
		
			this.setContentPane(connectionTab);
			this.setVisible(true);

		}
		
		
	}
	
	
	/**
	 * Class for choosing a node of the graph
	 * @author gpapas
	 *
	 */
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
			comboBoxNode.addItem(null);
			for (VisualNode node: graph.getVertices()) {
				 if (node.getType().getCategory() == NodeCategory.MODULE){
					comboBoxNode.addItem(node);
				}
			}
			// add action listener to fill childcombo
			comboBoxNode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// get parent node
					VisualNode parentNode= (VisualNode) comboBoxNode.getSelectedItem();
					// reset child combo
					comboBoxChildNode.removeAllItems();
					if (parentNode != null) {
						// get descendant nodes
						for (VisualNode node: graph.getModule(parentNode)) {
							comboBoxChildNode.addItem(node);
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
					VisualNode node = (VisualNode) comboBoxChildNode.getSelectedItem();
					if (node!= null) {
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
	
	
	/**
	 * a driver for this viewer
	 */
	public static void main(String[] args) {
		HecataeusViewer viewer = new HecataeusViewer(new VisualGraph());
	}
}
