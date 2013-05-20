/**
 * @author George Papastefanatos, @affiliation National Technical University of Athens
 * @author Fotini Anagnostou, @affiliation National Technical University of Athens
 */
package edu.ntua.dblab.hecataeus;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.ntua.dblab.hecataeus.dao.HecataeusDatabaseSettings;
import edu.ntua.dblab.hecataeus.dao.HecataeusDatabaseType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualAggregateLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualLayoutType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeIcon;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible.VisibleLayer;
import edu.ntua.dblab.hecataeus.metrics.HecataeusMetricManager;
import edu.ntua.dblab.hecataeus.parser.HecataeusSQLExtensionParser;
import edu.ntua.dblab.hecataeus.parser.HecataeusSQLParser;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
/**
 * @author pmanousi
 */

public class HecataeusViewer{

	// a dummy counter for disposing or exiting application  
	private static int countOpenViewers = 0;
	private static int countOpenTabs = 0;
	
	// the frame and swing objects of the application
	final HecataeusFrame frame  ;
	final Container content;
	
	// the visual graph object
/**@author pmanousi Needed for topologicalTravel so became public. */
	public VisualGraph graph;
//	public VisualSubGraph grafos;
	public Viewers viewer;
	
	public static HecataeusViewer myViewer;
	// the scale object for zoom capabilities 
	private final ScalingControl scaler = new CrossoverScalingControl();
	
	protected VisualAggregateLayout layout ;
	protected VisualAggregateLayout containerLayout;
	protected VisualAggregateLayout subLayout ;

	
	// the visual component
	protected final VisualizationViewer<VisualNode, VisualEdge> vv;
	protected VisualizationViewer<VisualNode, VisualEdge> vv1;
	public static VisualizationViewer<VisualNode, VisualEdge> vv2;
//	protected final VisualizationViewer<VisualNode, VisualEdge> vv3;
	public static VisualizationViewer<VisualNode, VisualEdge> vveva;

/**
 * @author pmanousi
 * Now user can see the policies in a widget next to the Layouts, also have a topological sort of IDs of nodes.
 * */
	protected HecataeusProjectConfiguration projectConf;
	protected HecataeusPolicyManagerGUI policyManagerGui;
	protected VisualNode epilegmenosKombos;
	protected HecataeusEventManagerGUI eventManagerGui;
	protected JTabbedPane managerTabbedPane;

	protected JTabbedPane tabbedPane;
	
	private static final String frameTitle = "HECATAEUS";
	private static final String frameIconUrl = "resources/hecataeusIcon.png";
	public final JTabbedPane JTabbedPane = null;
	
	
	//private String curPath = "AppData";


	public HecataeusViewer(VisualGraph inGraph) {
		
		
		// assign the graph
		projectConf=new HecataeusProjectConfiguration();
		this.graph = inGraph;
//		this.grafos = new VisualSubGraph();
		Dimension prefferedSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		
		frame = new HecataeusFrame(frameTitle);
	 	frame.setIconImage(new ImageIcon(frameIconUrl).getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		content = frame.getContentPane();
				
		// the layout
		layout = new VisualAggregateLayout(graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		 
        
	//	containerLayout = new VisualAggregateLayout(grafos, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		subLayout = new VisualAggregateLayout(graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		//the visualization viewer
		vv = new Viewers(layout).vv;
		vv1 = new Viewers(layout).vv;
		vv2 = new Viewers(layout).vv;
//		vv3 = new Viewers(containerLayout).vv;
		vveva = new Viewers(layout).vv;
		

		content.add(getMainPanel("Logical Layout"));

		frame.setJMenuBar(this.getMenuBar());
		frame.pack();
		frame.setExtendedState(frame.getExtendedState()| JFrame.MAXIMIZED_BOTH);
	 	frame.setVisible(true);	
	 	
		countOpenViewers ++;
/**
 * @author pmanousi
 * Inform mouse plugins for the viewer (and have access to epilegmenosKombos in order to update managers. 
 */
		HecataeusModalGraphMouse gm = new HecataeusModalGraphMouse();
		vv.setGraphMouse(gm);
		gm.HecataeusViewerPM(this);

	}

/**
 * @author pmanousi updates the manager that should get updated.
 * */
public void updateManagers()
{
	if(managerTabbedPane.getSelectedComponent()==policyManagerGui)
	{
		policyManagerGui.UPDATE();
	}
	else if(managerTabbedPane.getSelectedComponent()==eventManagerGui)
	{
		eventManagerGui.UPDATE();
	}
	else
	{	//Should never happen but just in case.
		policyManagerGui.UPDATE();
		eventManagerGui.UPDATE();
	}
}	
	
/**
 * @author pmanousi
 * Creates a new project.
 */
private void newProject()
{
	FileDialog fd=new FileDialog((Dialog)null, "Select the name and possition of new project", FileDialog.SAVE);
	fd.setVisible(true);
	if(projectConf==null)
	{
		projectConf=new HecataeusProjectConfiguration();
	}
	else
	{
		projectConf.clearProject();
	}
	if(fd.getFile()!=null||fd.getFile().trim().isEmpty()==false)
	{
		projectConf.curPath=fd.getDirectory()+"/"+fd.getFile();
		projectConf.projectName=fd.getFile().trim();
		projectConf.clearArrayLists();
		File projectDirectory=new File(projectConf.curPath);
		if(projectDirectory.mkdir()==false)
		{
			JOptionPane.showMessageDialog(null, "Could not create project directory,\nplease check your write permitions\nor check if the project already exists.");
			return;
		}			
		frame.setTitle( frameTitle + " - "+projectConf.projectName);
		projectDirectory=new File(projectConf.curPath+"/XML");
		projectDirectory.mkdir();
		projectDirectory=new File(projectConf.curPath+"/SQLS");
		projectDirectory.mkdir();
		projectDirectory=new File(projectConf.curPath+"/POLICIES");
		projectDirectory.mkdir();
		projectDirectory=new File(projectConf.curPath+"/OTHER");
		projectDirectory.mkdir();
		projectConf.setDefaultPolicies();
		policyManagerGui.UPDATE();
		eventManagerGui.UPDATE();
		projectConf.writeConfig();
	}
}

/**
 * @author pmanousi
 * Saves xml.
 */
private void saveProject()
{
	JFileChooser chooser = new JFileChooser(projectConf.curPath+"/XML/");
	FileFilterImpl filter = new FileFilterImpl("xml");
	chooser.addChoosableFileFilter(filter);
	int option = chooser.showSaveDialog(content);
	if (option == JFileChooser.APPROVE_OPTION) {
		String fileDescription = chooser.getSelectedFile().getAbsolutePath();
		if (!fileDescription.endsWith("xml"))
			fileDescription += ".xml";		
		File file = new File(fileDescription);
		if (file.exists()) {
			int response = JOptionPane.showConfirmDialog(null,"The file will be overriden! Do you agree? Answer with y or n","Warning!", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if (response == 0)
				try {
					frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					//get the location of the vertices from the layout of the graph
					HecataeusViewer.this.getLayoutPositions();
					//TODO: this only saves the logical graph, Fix for physical graph as well
					layout.getGraph().exportToXML(file);
					frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showMessageDialog(null,"The file was created successfully","Information",JOptionPane.INFORMATION_MESSAGE);
				} catch (RuntimeException e1) {}
			else
				;
		} else {
			try {
				frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				//get the location of the vertices from the layout of the graph
				HecataeusViewer.this.getLayoutPositions();
				//TODO: this only saves the logical graph, Fix for physical graph as well
				layout.getGraph().exportToXML(file);
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(null,"The file was created successfully","Information",JOptionPane.INFORMATION_MESSAGE);
			} catch (RuntimeException e1) {}
		}
	}
}

public void saveXmlForWhatIf(String event, String node)
{
	String fileDescription = this.projectConf.curPath+"XML/"+event+"_of_"+node+".xml";
	File file = new File(fileDescription);
	try {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		HecataeusViewer.this.getLayoutPositions();
		layout.getGraph().exportToXML(file);
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//		JOptionPane.showMessageDialog(null,"Previous graph saved under: "+fileDescription,"Information",JOptionPane.INFORMATION_MESSAGE);
	} catch (RuntimeException e1) {}
}

/**
 * @author pmanousi
 * Adds a file to the project.
 */
private void addFile()
{
	if(projectConf.projectName.isEmpty())
	{	/** @author pmanousi No active project yet */
		return;
	}
	JFileChooser chooser = new JFileChooser(projectConf.curPath);
	chooser.setMultiSelectionEnabled(true);			//added by sgerag

	FileFilterImpl filter = new FileFilterImpl("sql","SQL File");
	chooser.addChoosableFileFilter(filter);
	int option = chooser.showOpenDialog(content);
	if (option == JFileChooser.APPROVE_OPTION) {
		//modification gy sgerag
		File[] files = chooser.getSelectedFiles();
		//let the append file option to use multiple files
		HecataeusSQLParser reader = new HecataeusSQLParser(graph);
		try
		{																										
			for (int i=0;i<files.length;i++)
			{
				frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));													
				reader.processFile(files[i]);
				projectConf.copySQLFile(files[i]);	/** @author pmanousi Copying files to SQLS directory of project and insert them to configuration file. */
				policyManagerGui.UPDATE();
				eventManagerGui.UPDATE();
			}
			projectConf.writeConfig();
			graph = reader.getParsedGraph();
			// set the layout of the graph
			HecataeusViewer.this.setLayout(layout.getTopLayoutType(),layout.getSubLayoutType());
			//get new layout's positions
			HecataeusViewer.this.getLayoutPositions();	
			HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
			//HecataeusViewer.this.centerAt(containerLayout.getGraph().getCenter());
			HecataeusViewer.this.zoomToWindow();
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} catch (IOException e1) {
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Error description: ", e1.getMessage());
		} catch (SQLException e1) {
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Error description: ", e1.getMessage());
		} catch (Exception e1) {											//added by sgerag
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Error description: ", e1.getMessage());
		}
	}
}

/**
 * @author pmanousi
 * Exports the graph to a jpg image.
 */
private void exportToJpg()
{
	JFileChooser chooser = new JFileChooser(projectConf.curPath+"/OTHER");
	FileFilterImpl filter = new FileFilterImpl("jpg","Image File");
	chooser.addChoosableFileFilter(filter);
	if ((chooser.showSaveDialog(frame)) == JFileChooser.APPROVE_OPTION) {
		String fileDescription = chooser.getSelectedFile().getAbsolutePath();
		if (!fileDescription.endsWith(".jpg"))
			fileDescription += ".jpg";				
		HecataeusViewer.this.writeJPEGImage(new File(fileDescription));
	}
}

/**
 * @author pmanousi
 * Opens an existing project.
 */
private void openProject()
{
	FileDialog fd=new FileDialog((Dialog)null, "Select the project you want to open.", FileDialog.LOAD);
	fd.setVisible(true);
	if(fd.getFile()!=null)
	{
		graph.clear();
		projectConf.clearProject();
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		projectConf.readConfig(fd.getDirectory(), fd.getFile());	/** Reading configuration */
		boolean selection=false;
		if(JOptionPane.showConfirmDialog(null, "Do you want to open an already saved xml file (YES),\nor start with a clean project configuration(NO)")==0)
		{
			JFileChooser chooser = new JFileChooser(projectConf.curPath+"/XML/");
			FileFilterImpl filter = new FileFilterImpl("xml");
			chooser.addChoosableFileFilter(filter);
			int option = chooser.showOpenDialog(content);
//			String Name = HecataeusPopupGraphMousePlugin.Gname();
			if (option == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				graph = graph.importFromXML(file);
				//pass the location of the vertices to the layout of the graph
				HecataeusViewer.this.setLayout(VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
				//in the XML file case, the locations are also defined and they must pass to the graph.
				HecataeusViewer.this.setLayoutPositions();
				selection=true;
			}
			else
			{
				selection=false;
				JOptionPane.showMessageDialog(null, "You will start with a clean project configuration.");
			}
		}
		if(selection==false)
		{	/** @author pmanousi parse files of configuration. */
			HecataeusSQLParser reader = new HecataeusSQLParser(graph);
			frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			for(int i=0;i<projectConf.sqls.size();i++)
			{
				try
				{																										
					reader.processFile(new File(projectConf.curPath+projectConf.sqls.get(i)));
					graph = reader.getParsedGraph();
					HecataeusViewer.this.setLayout(VisualLayoutType.Top2DownTopologicalLayout, VisualLayoutType.Top2DownTopologicalLayout);
				}
				catch (IOException e1) {}
				catch (SQLException e1) {}
				catch (Exception e1) {}
			}
		}
		frame.setTitle(frameTitle + " - "+projectConf.projectName);
		policyManagerGui.UPDATE();
		
		//get new layout's positions
		HecataeusViewer.this.getLayoutPositions();
		HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
		//HecataeusViewer.this.centerAt(containerLayout.getGraph().getCenter());
		HecataeusViewer.this.zoomToWindow();
		eventManagerGui.UPDATE();
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}


/**
 * @author pmanousi
 * Closes the project.
 */
private void closeProject()
{
	layout.getGraph().clear();
	//containerLayout.getGraph().clear();
	graph.clear();
	vv.repaint();
	//vvContainer.repaint();
	projectConf.clearProject();
	policyManagerGui.UPDATE();
	eventManagerGui.UPDATE();
}

/**
 * @author pmanousi
 * Exits Hecataeus.
 */
private void exitHecataeus()
{
	frame.dispose();
}

	/***
	 * Constructs the JPanel
	 * @return
	 */
	private JComponent getMainPanel(String name) {
//		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab(name, null, new GraphZoomScrollPane(vv), "Displays the logical dependencies between modules");
//		tabbedPane.addTab("TEST", null, new GraphZoomScrollPane(vveva), "Displays the logical dependencies between modules");

/** @author pmanousi		tabbedPane.addTab("Physical Layout", null , new GraphZoomScrollPane(vvContainer), "Displays the physical dependencies between modules");*/

		//return splitPane;
		
		final JSplitPane jsp=new JSplitPane();
		jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		jsp.setOneTouchExpandable(true);
		jsp.setLeftComponent(tabbedPane);

		managerTabbedPane = new JTabbedPane();
		policyManagerGui=new HecataeusPolicyManagerGUI(projectConf,this);
		managerTabbedPane.addTab("Policy",policyManagerGui);
		eventManagerGui=new HecataeusEventManagerGUI(this);
		managerTabbedPane.addTab("Event",eventManagerGui);

		jsp.setRightComponent(managerTabbedPane);
		
		jsp.setDividerLocation(0.8);
		jsp.setResizeWeight(0.5);
		return(jsp);
	}	
	

	
	
	/***
	 * Constructs the Menu bar
	 * @return
	 */
	private JMenuBar getMenuBar() {
		// create the menu bar and add the menus
		JMenuBar menuBar = new JMenuBar();
/**
 * @author pmanousi
 */		
JMenu mnProject = new JMenu("Project");
menuBar.add(mnProject);

JMenuItem mntmNewProject = new JMenuItem("New project");
mntmNewProject.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		newProject();
	}
});

mnProject.add(mntmNewProject);

JMenuItem mntmOpenProject = new JMenuItem("Open project");
mntmOpenProject.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		openProject();
	}
});
mnProject.add(mntmOpenProject);

JMenuItem mntmAddFile = new JMenuItem("Add file");
mntmAddFile.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		addFile();
	}
});
mnProject.add(mntmAddFile);

JMenuItem mntmExportToJpg = new JMenuItem("Export to jpg");
mntmExportToJpg.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		exportToJpg();
	}
});
mnProject.add(mntmExportToJpg);

JMenuItem mntmCloseProject = new JMenuItem("Close project");
mntmCloseProject.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		closeProject();
	}
});
mnProject.add(mntmCloseProject);

JMenuItem mntmSaveProject = new JMenuItem("Save project");
mntmSaveProject.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		saveProject();
	}
});
mnProject.add(mntmSaveProject);

JMenuItem mntmExit = new JMenuItem("Exit");
mntmExit.addMouseListener(new MouseAdapter() {
	@Override
	public void mouseReleased(MouseEvent e) {
		exitHecataeus();
	}
});
mnProject.add(mntmExit);

		menuBar.add(this.getMenuVisualize());
		menuBar.add(this.getMenuTools());
		menuBar.add(this.getMenuManage());
		menuBar.add(this.getMenuMetrics());
		menuBar.add(this.getMenuHelp());
		return menuBar;
	}	
	
	private JMenu getMenuVisualize() {
		HecataeusModalGraphMouse gm = (HecataeusModalGraphMouse) vv.getGraphMouse();
		JMenu jm=gm.getModeMenu();
/**
 * @author pmanousi
 * Added Zoom menu, Abstraction level, Graph layout and Icons on/off.
 */
jm.addSeparator();
this.getSubMenuZoom(jm);
jm.addSeparator();
this.getSubMenuLayout(jm);
jm.addSeparator();
this.getSubMenuIcons(jm);
		return(jm);
	}

	private JMenuItem getSubMenuLayout(JMenu menuLayout) {
		/*
		 * Menu for setting different Layouts on the graph
		 */
		JMenu submenuLayout = new JMenu("Show");
		submenuLayout.add(new AbstractAction("Top-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) vv.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.MODULE);
				vv.repaint();
				//vvContainer.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Mid-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible)  vv.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.SCHEMA);
				vv.repaint();
				//vvContainer.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Low-level Nodes") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible)  vv.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.SEMANTICS);
				vv.repaint();
				//vvContainer.repaint();
			}
		});
		submenuLayout.addSeparator();
		submenuLayout.add(new AbstractAction("Nodes with Status") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) vv.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.STATUS);
				vv.repaint();
				//vvContainer.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Nodes with Policy") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) vv.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.POLICIES);
				vv.repaint();
				//vvContainer.repaint();
			}
		});
		submenuLayout.add(new AbstractAction("Nodes with Event") {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				VisualNodeVisible showAll = (VisualNodeVisible) vv.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.EVENTS);
				vv.repaint();
				//vvContainer.repaint();
			}
		});
		menuLayout.add(submenuLayout);
		
		submenuLayout = new JMenu("Algorithms");
		for (final VisualLayoutType layoutType : VisualLayoutType.values()) {
			submenuLayout.add(new AbstractAction(layoutType.toString()) {
				public void actionPerformed(ActionEvent e) {
					// update the top layout of the graph
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					layout.setTopLayoutType(layoutType);
					//containerLayout.setTopLayoutType(layoutType);
					//update the new layout's positions
					HecataeusViewer.this.getLayoutPositions();
					HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
					//HecataeusViewer.this.centerAt(containerLayout.getGraph().getCenter());
					HecataeusViewer.this.zoomToWindow();
				}
			});
		}
		submenuLayout.addSeparator();
		submenuLayout.add(new AbstractAction("Revert") {
			public void actionPerformed(ActionEvent e) {
				//update the new layout's positions
				// FIXME: restore original layout
				HecataeusViewer.this.getLayoutPositions();
				HecataeusViewer.this.centerAt(graph.getCenter());
				HecataeusViewer.this.zoomToWindow();
			}
		});
		menuLayout.add(submenuLayout);
		return menuLayout;
	}

	private JMenu getMenuFind(JMenu menuFind) {
		//final JMenu menuFind = new JMenu("Find");
		menuFind.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem mnuFindNode= new JMenuItem("Find Node");
		mnuFindNode.setAccelerator(KeyStroke.getKeyStroke("control F"));
		mnuFindNode.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				// obtain user input from JOptionPane input dialogs
				String nodeNames = JOptionPane
						.showInputDialog( "The name of nodes to find (separated with ,): ");
				StringTokenizer token = new StringTokenizer("");
				if (nodeNames != null) {
					token = new StringTokenizer(nodeNames);
				}
				activeViewer.getPickedVertexState().clear();
				VisualNode v = null;

				while (token.hasMoreTokens()) {
					String name = token.nextToken(",");
						for (VisualNode u : activeViewer.getGraphLayout().getGraph().getVertices()) {
							if (u.getName().equals(name.trim().toUpperCase())
									&& u.getVisible()) {
								activeViewer.getPickedVertexState().pick(u,
										true);
								v = u;
							}
					}
				}
				if (v != null)
				{
//					centerAt(v.getLocation());
					centerAt(activeViewer.getGraphLayout().transform(v));
					epilegmenosKombos=v;
					updateManagers();
				}
			}
		});

		menuFind.add(mnuFindNode);
		
		menuFind.add(new AbstractAction("Find Edge") {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				// obtain user input from JOptionPane input dialogs
				String name = JOptionPane
						.showInputDialog("The name of the edge to find: ");
				activeViewer.getPickedEdgeState().clear();
				for (VisualEdge edge: graph.getEdges()) {
					if (edge.getName().equals(name)) {
						activeViewer.getPickedEdgeState().pick(edge, true);
					}
				}
			}
		});
		
		JMenuItem mnuSelectAll= new JMenuItem("Select All");
		mnuSelectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
		
		mnuSelectAll.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				for (VisualNode u : graph.getVertices()) {
					if (u.getVisible()) {
						activeViewer.getPickedVertexState().pick(u, true);
					}
				}
			}
		});
		menuFind.add(mnuSelectAll);
		return menuFind;
	}
	
	private JMenuItem getSubMenuZoom(JMenu menuZoom) {
		//JMenu menuZoom = new JMenu("Zoom");
		JMenuItem mnuZoomIn = new JMenuItem("Zoom in");
		mnuZoomIn.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				scaler.scale(activeViewer, 1.1f, activeViewer.getCenter());

			}
		});
		mnuZoomIn.setAccelerator(KeyStroke.getKeyStroke(Character.valueOf('+')));
		menuZoom.add(mnuZoomIn);
		
		JMenuItem mnuZoomOut = new JMenuItem("Zoom Out");
		mnuZoomOut.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				scaler.scale(activeViewer, 1 / 1.1f, activeViewer.getCenter());
			}
		});
		mnuZoomOut.setAccelerator(KeyStroke.getKeyStroke(Character.valueOf('-')));
		menuZoom.add(mnuZoomOut);
		
		JMenuItem mnuZoomInWindow = new JMenuItem("Zoom in Window");
		mnuZoomInWindow.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				centerAt(((VisualGraph)activeViewer.getGraphLayout().getGraph()).getCenter());
		 		zoomToWindow();

			}
		});
		mnuZoomInWindow.setAccelerator(KeyStroke.getKeyStroke("control W"));
		menuZoom.add(mnuZoomInWindow);
		
		return menuZoom;
	}
	
	private JMenu getMenuManage() {
		JMenu menuManage = new JMenu("Manage");
		menuManage.add(new AbstractAction("Events") {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_EVENT);
			}
		});
		JMenu menuPolicies = new JMenu("Policies");
		menuPolicies.add(new AbstractAction("Edit") {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_POLICY);
			}
		});
		menuPolicies.add(new AbstractAction("Show Default") {
			public void actionPerformed(ActionEvent e) {  
				
				String msg = "";
				
				for (int i = 0; i < graph.getDefaultPolicyDecsriptions().size(); i++) {
					msg += graph.getDefaultPolicyDecsriptions().get(i) + "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame,  "Default Policies", msg);
			}
		});
		menuPolicies.addSeparator();
		menuPolicies.add(new AbstractAction("Import") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(projectConf.curPath);
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
					//vvContainer.repaint();
					
				}
			}
		});
		menuPolicies.add(new AbstractAction("Export") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(projectConf.curPath);

				FileFilterImpl filter = new FileFilterImpl("txt");
				chooser.addChoosableFileFilter(filter);
				int option = chooser.showSaveDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {

					String fileDescription = chooser.getSelectedFile().getAbsolutePath();
					if (!fileDescription.endsWith("txt"))
						fileDescription += ".txt";		
					
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
								layout.getGraph().exportPoliciesToFile(file);
								frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
								JOptionPane.showMessageDialog(null,"The file was created successfully","Information",JOptionPane.INFORMATION_MESSAGE);
							} catch (RuntimeException e1) {
								e1.printStackTrace();
							}
						else
							;
					} else {
						try {
							frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
							layout.getGraph().exportPoliciesToFile(file);
							frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							JOptionPane.showMessageDialog(null,"The file was created successfully","Information",JOptionPane.INFORMATION_MESSAGE);
						} catch (RuntimeException e1) {
							e1.printStackTrace();
						}
					}
					projectConf.curPath = chooser.getSelectedFile().getPath();
				}
			}
		});
		menuManage.add(menuPolicies);
		
		menuManage.addSeparator();
		JMenu menuEventImpact = new JMenu("Output Events Flooding");
		menuEventImpact.add(new AbstractAction("Per Event") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				
				String msg = "";
				int countEvents = 0;
				for (VisualNode v : activeGraph.getVertices()) {
					if (v.getHasEvents()) {
						for (EvolutionEvent<VisualNode> event : v.getEvents()) {
							countEvents++;
							
							msg += "Event " + countEvents + ": "
									+ event.getEventType().toString() + "\tOn "
									+ event.getEventNode().getName() + "\n";
							msg += "Module\tNode Name\tNode Type\tStatus\n";
							// set status =NO_Status
							for (VisualNode n : graph.getVertices()) {
								n.setStatus(StatusType.NO_STATUS,true);
							}
							// run algo
							activeGraph.initializeChange(event);
							// output
							for (VisualNode node : activeGraph.getVertices(NodeCategory.MODULE)) {
								for (VisualNode evNode : activeGraph.getModule(node)) {
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
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String msg = "";
				int countEvents = 0;
				msg += "EventID\tEventtype\tEventNode\tNodekey\tModule\tNode Name\tNode Type\tStatus\n";

				for (VisualNode v : activeGraph.getVertices()) {
					if (v.getHasEvents()) {
						for (EvolutionEvent<VisualNode> event : v.getEvents()) {
							

							countEvents++;
							 
							
							String strEvent = "Event " + countEvents + "\t"
									+ event.getEventType().toString() + "\t"
									+ event.getEventNode().getName();
							// set status =NO_Status
							for (VisualNode n : activeGraph.getVertices()) {
								n.setStatus(StatusType.NO_STATUS,true);
							}
							// run algo
							activeGraph.initializeChange(event);
							// output
							for (VisualNode node : activeGraph.getVertices(NodeCategory.MODULE)) {
								for (VisualNode evNode : activeGraph.getModule(node)) {
										if (evNode.getStatus() != StatusType.NO_STATUS) {
											msg += strEvent
													+ "\t"
													+ graph.getKey(evNode)
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
		menuEventImpact.add(new AbstractAction("Modules Total Affected") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String msg = "";
				int countEvents = 0;
				msg += "Nodekey\tModule\tNode Name\tNode Type\tAffected\n";
				List<VisualNode> modules = activeGraph.getVertices(NodeCategory.MODULE);
				int[] affectedModules = new int[modules.size()];
				
				for (VisualNode v : activeGraph.getVertices()) {
					if (v.getHasEvents()) {
						for (EvolutionEvent<VisualNode> event : v.getEvents()) {
							int r=1; 
							//uncomment and enter here the occurrences of attribute additions 
//							if (v.getName().equals("S4") && event.getEventType()==EventType.ADD_ATTRIBUTE)
//								r = 58;
//							if (v.getName().equals("S1") && event.getEventType()==EventType.ADD_ATTRIBUTE)
//								r = 14;
//							

							for (int i = 0; i < r; i++) {
								// set status =NO_Status
								for (VisualNode n : activeGraph.getVertices()) {
									n.setStatus(StatusType.NO_STATUS,true);
								}
								// run algo
								activeGraph.initializeChange(event);
								// output
								for (VisualNode evNode : modules) {
									if (evNode.getStatus() != StatusType.NO_STATUS)
										affectedModules[modules.indexOf(evNode)]++;
								}

							}
						}
					}
				}
				for (VisualNode evNode : modules) {
					msg +=graph.getKey(evNode)
					+ "\t"
					+ graph.getTopLevelNode(evNode).getName()
					+ "\t"
					+ evNode.getName()
					+ "\t"
					+ evNode.getType().toString()
					+ "\t"
					+ affectedModules[modules.indexOf(evNode)] + "\n";
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
					//vvContainer.repaint();
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
						//vvContainer.repaint();
					}
				}
			}
		});

		menuManage.add(new AbstractAction("Clear All Statuses") {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					v.setStatus(StatusType.NO_STATUS,true);
				}
				for (VisualEdge edge : graph.getEdges()) {
					edge.setStatus(StatusType.NO_STATUS,true);
				}

				vv.repaint();
				//vvContainer.repaint();
			}
		});

		menuManage.add(new AbstractAction("Inverse Policies") {
			public void actionPerformed(ActionEvent e) {
				/*for (VisualNode v : graph.getVertices()) {
					if (v.getHasPolicies()) {
						for (EvolutionPolicy<VisualNode> p : v.getPolicies()) {
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
				vv.repaint();*/
				//vvContainer.repaint();
				policyManagerGui.revertPolicies();
				vv.repaint();
			}
		});

		menuManage.add(new AbstractAction("Propagate All") {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					if (v.getHasPolicies()) {
						for (EvolutionPolicy<VisualNode> p : v.getPolicies()) {
							p.setPolicyType(PolicyType.PROPAGATE);
						}
					}
				}
				vv.repaint();
				//vvContainer.repaint();

			}
		});
		return menuManage;
	}

	private JMenu getMenuMetrics() {
		JMenu menuMetrics = new JMenu("Metrics");

		JMenu menuMetricCount = new JMenu("Count");
		menuMetricCount.add(new AbstractAction("Nodes") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph(); 
				int countNodes = 0;
				for (VisualNode v : activeGraph.getVertices()) {
					if (v.getVisible())
						countNodes++;
				}
				JOptionPane.showMessageDialog(content, countNodes);

			}
		});

		menuMetricCount.add(new AbstractAction("Policies") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int propagatePolicies = HecataeusMetricManager.countPolicies(activeGraph.getVertices(), PolicyType.PROPAGATE);
				int blockPolicies = HecataeusMetricManager.countPolicies(activeGraph.getVertices(), PolicyType.BLOCK);
				int promptPolicies = HecataeusMetricManager.countPolicies(activeGraph.getVertices(),PolicyType.PROMPT);

				String message = "Propagate: " + propagatePolicies;
				message += "\nBlock: " + blockPolicies;
				message += "\nPrompt: " + promptPolicies;
				JOptionPane.showMessageDialog(content, message);

			}
		});
		menuMetricCount.add(new AbstractAction("Events") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int events = HecataeusMetricManager.countEvents(activeGraph.getVertices());
				JOptionPane.showMessageDialog(content, events);

			}
		});
		menuMetrics.add(menuMetricCount);

		menuMetrics.add(new AbstractAction("Output For Module Nodes") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String message = "NodeKey\tModule\tNode Name\tNode Type\tDEGREE IN\tDEGREE OUT\tDEGREE TOTAL\tSTRENGTH IN\tSTRENGTH OUT\tSTRENGTH TOTAL\tTRAN DEGREE IN\tTRAN DEGREE OUT\tTRAN MODULE OUT\tTRAN STRENGTH OUT\tENTROPY OUT\n";
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
							int metric = HecataeusMetricManager.degree(topNode);
							message += activeGraph.getKey(topNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + topNode.getName() 
									+ "\t" + topNode.getType()
									+ "\t" + HecataeusMetricManager.inDegree(topNode)
									+ "\t" + HecataeusMetricManager.outDegree(topNode)
									+ "\t" + HecataeusMetricManager.degree(topNode)
									+ "\t" + HecataeusMetricManager.inStrength(topNode,activeGraph)
									+ "\t" + HecataeusMetricManager.outStrength(topNode,activeGraph)
									+ "\t" + HecataeusMetricManager.strength(topNode,activeGraph)
									+ "\t" + HecataeusMetricManager.inTransitiveDegree(topNode)
									+ "\t" + HecataeusMetricManager.outTransitiveDegree(topNode)
									+ "\t" + HecataeusMetricManager.outTransitiveModuleDegree(topNode)
									+ "\t" + HecataeusMetricManager.outTransitiveStrength(topNode, graph)
									+ "\t'" + HecataeusMetricManager.entropyOutPerNode(topNode,activeGraph)
									+ "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output All metrics for top level Nodes", message);
			}
		});
		
		JMenu menuMetricOutput = new JMenu("Output For All Nodes");
		
		menuMetricOutput.add(new AbstractAction("Degree Total") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String message = "NodeKey\tModule\tNode Name\tNode Type\tDegree\n";
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.degree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName() 
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree Total", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Degree In") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String message = "NodeKey\tModule\tNode Name\tNode Type\tDegree\n";
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.inDegree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Degree Out") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String message = "NodeKey\tModule\tNode Name\tNode Type\tDegree\n";
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.outDegree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree Out", message);
			}
		});
		
		menuMetricOutput.add(new AbstractAction("Transitive Degree") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String message = "NodeKey\tModule\tNode Name\tNode Type\tDegree\n";
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.transitiveDegree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Transitive Degree In", message);
			}
		});
		
		menuMetricOutput.add(new AbstractAction("Transitive Degree In") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				String message = "NodeKey\tModule\tNode Name\tNode Type\tDegree\n";
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.inTransitiveDegree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Transitive Degree In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Transitive Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tTranDegreeOut\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.outTransitiveDegree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Transitive Degree Out" , message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Strength Total") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tStrength\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode evNode: activeGraph.getVertices(NodeCategory.MODULE)) {
							int metric = HecataeusMetricManager.strength(evNode, activeGraph);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ evNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Strength Total", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Strength In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tStrength\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode evNode: activeGraph.getVertices(NodeCategory.MODULE)) {
							int metric = HecataeusMetricManager.inStrength(evNode, activeGraph);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ evNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Strength In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Strength Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tStrength\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode evNode: activeGraph.getVertices(NodeCategory.MODULE)) {
							int metric = HecataeusMetricManager.outStrength(evNode, activeGraph);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ evNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Strength Out", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Weighted Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tWeighted Degree In\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						for (VisualNode evNode : activeGraph.getModule(topNode)) {
							int metric = HecataeusMetricManager.inWeightedDegree(evNode);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Weighted Degree In", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Weighted Strength In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tWeighted Strength In\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode evNode: activeGraph.getVertices(NodeCategory.MODULE)) {
							int metric = HecataeusMetricManager.inWeightedStrength(evNode, activeGraph);
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ evNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Weighted Strength In", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Policy Degree In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tPDegree\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
					int metric = 0; 
					for (VisualNode evNode : activeGraph.getModule(topNode)) {
							for (EvolutionEvent<VisualNode> event : evNode.getEvents()) {
								 metric += HecataeusMetricManager.inPolicyTransitiveDegree(event, activeGraph);
							}
					}
					message += activeGraph.getKey(topNode)
					+ "\t"
					+ topNode.getName()
					+ "\t" + topNode.getName()
					+ "\t" + topNode.getType()
					+ "\t" + metric
					+ "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Policy Degree In", message);
			}
		});
		/**
		 * debug reasons only
		 */
		menuMetricOutput.add(new AbstractAction("Policy Degree Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tPDegreeOut\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode topNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						List<VisualNode> nodes = activeGraph.getModule(topNode);
						for (VisualNode evNode : nodes) {
							int metric = 0;
							for (VisualNode aNode : nodes) {
								for (EvolutionEvent<VisualNode> anEvent: aNode.getEvents()) {
									metric += HecataeusMetricManager.outPolicyTransitiveDegree(evNode, anEvent);
								}
							}
							message += activeGraph.getKey(evNode)
									+ "\t"
									+ topNode.getName()
									+ "\t" + evNode.getName()
									+ "\t" + evNode.getType()
									+ "\t" + metric
									+ "\n";
						}

				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Policy Degree Out", message);
			}
		});
		menuMetricOutput.add(new AbstractAction("Entropy In") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tEntropy In\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode evNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						double metric = HecataeusMetricManager.entropyInPerNode(evNode, activeGraph);
						message += activeGraph.getKey(evNode)
								+ "\t"
								+ evNode.getName() 
								+ "\t" + evNode.getName()
								+ "\t" + evNode.getType()
								+ "\t'" + metric+ "\n";
					}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Entropy In", message);
			}
		});

		menuMetricOutput.add(new AbstractAction("Entropy Out") {
			public void actionPerformed(ActionEvent e) {
				String message = "NodeKey\tModule\tNode Name\tNode Type\tEntropy Out\n";
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				for (VisualNode evNode: activeGraph.getVertices(NodeCategory.MODULE)) {
						double metric = HecataeusMetricManager.entropyOutPerNode(evNode, activeGraph);
						message += activeGraph.getKey(evNode)
								+ "\t"
								+ evNode.getName() 
								+ "\t" + evNode.getName()
								+ "\t" + evNode.getType()
								+ "\t'" + metric+ "\n";
					}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Entropy Out", message);
			}
		});
		
		menuMetrics.add(menuMetricOutput);
		menuMetrics.addSeparator();

		menuMetrics.add(new AbstractAction("Entropy Of Graph") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				float entropy = HecataeusMetricManager.entropyGraph(activeGraph);
				String message = "Entropy Of the Graph: " + entropy;
				JOptionPane.showMessageDialog(content, message);

			}
		});
		menuMetrics.add(new AbstractAction("Maximum Entropy") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				float entropy = 0;
				VisualNode maxEntropyNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxEntropyNode));
//					centerAt(maxEntropyNode.getLocation());
				}
			}
		});
		JMenu menuMetricDegree = new JMenu("Maximum Degree");
		menuMetricDegree.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int degree = 0;
				VisualNode maxDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxDegreeNode));
										
				}
			}
		});
		menuMetricDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int degree = 0;
				VisualNode maxDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxDegreeNode));
				}

			}
		});
		menuMetricDegree.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int degree = 0;
				VisualNode maxDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxDegreeNode));
				}

			}
		});
		menuMetrics.add(menuMetricDegree);

		JMenu menuMetricStrength = new JMenu("Maximum Strength");
		menuMetricStrength.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int strength = 0;
				VisualNode maxStrengthNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxStrengthNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxStrengthNode));
				}
			}
		});
		menuMetricStrength.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int strength = 0;
				VisualNode maxStrengthNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxStrengthNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxStrengthNode));
				}
			}
		});
		menuMetricStrength.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int strength = 0;
				VisualNode maxStrengthNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxStrengthNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxStrengthNode));
				}
			}
		});
		menuMetrics.add(menuMetricStrength);

		JMenu menuWeightedDegree = new JMenu("Maximum Weighted Degree");
		menuWeightedDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int maxwDegree = 0;
				VisualNode maxWDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
					if (node.getFrequency() >= maxwDegree) {
						maxWDegreeNode = node;
						maxwDegree = node.getFrequency();
					}
				}
				if (maxWDegreeNode != null) {
					String message = "Node: " + maxWDegreeNode.getName();
					message += "\nWeighted Degree: " + maxwDegree;
					JOptionPane.showMessageDialog(content, message);
//					centerAt(maxWDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxWDegreeNode));
				}
			}
		});
		menuMetrics.add(menuWeightedDegree);

		JMenu menuTransitiveDegree = new JMenu("Maximum Transitive Degree");
		menuTransitiveDegree.add(new AbstractAction("Total") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int maxTDegree = 0;
				VisualNode maxTDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxTDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxTDegreeNode));
				}
			}
		});
		menuTransitiveDegree.add(new AbstractAction("In") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int maxTDegree = 0;
				VisualNode maxTDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxTDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxTDegreeNode));
				}
			}
		});
		menuTransitiveDegree.add(new AbstractAction("Out") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int maxTDegree = 0;
				VisualNode maxTDegreeNode = null;
				for (VisualNode node: activeGraph.getVertices()) {
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
//					centerAt(maxTDegreeNode.getLocation());
					final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
					centerAt(activeViewer.getGraphLayout().transform(maxTDegreeNode));
				}
			}
		});
		menuMetrics.add(menuTransitiveDegree);

		/*menuMetrics.add(new AbstractAction("Evaluate Bipartite") {
			public void actionPerformed(ActionEvent e) {
//				VisualEdge isBipartite = activeGraph.getEdge(HecataeusMetricManager.isBipartite(activeGraph.getHecataeusEvolutionGraph()));
//				PickedState pickedState = vv.getPickedState();
//				pickedState.pick(isBipartite, true);
				boolean isBipartite = HecataeusMetricManager.isBipartite(activeGraph.getHecataeusEvolutionGraph());
				String bipartite = (isBipartite? "Yes":"No");
				String message = "Is Graph Bipartite: " + bipartite;
				JOptionPane.showMessageDialog(content, message);
			}
		});	*/	
		
		return menuMetrics;
	}
	
	private JMenu getMenuTools() {
		JMenu menuTools= new JMenu("Tools");
		this.getMenuFind(menuTools);
		menuTools.addSeparator();
		menuTools.add(new AbstractAction("Module Synopsis") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Help
				 */
				final ModuleChooser p = new ModuleChooser();
			}
		});
		menuTools.add(new AbstractAction("Output Module Structure") {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				
				List<VisualNode> modules = activeGraph.getVertices(NodeType.NODE_TYPE_RELATION);
				modules.addAll(activeGraph.getVertices(NodeType.NODE_TYPE_VIEW));
				modules.addAll(activeGraph.getVertices(NodeType.NODE_TYPE_QUERY));
				String message = "\n";
				message = "\t";
				for (VisualNode aNode: modules) {
					message += aNode + "\t";
				}
				for (VisualNode aNode: modules) {
					message +="\n" + aNode; 
					for (VisualNode bNode: modules) {
						message +="\t" + activeGraph.getConnections(activeGraph.getModule(aNode),activeGraph.getModule(bNode));
					}	
				}
				
				
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame, "Graph Metrics - Output for: Degree Total", message);
			}
		});
		return menuTools;
	}
		
	private JMenuItem getSubMenuIcons(JMenu menuTools) {
		
		JCheckBoxMenuItem icons = new JCheckBoxMenuItem("Icons On");
		icons.setSelected(true);
		icons.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent event) {
		          AbstractButton aButton = (AbstractButton) event.getSource();
		          boolean selected = aButton.getModel().isSelected();
		          if (selected) {
		        	  vv.getRenderContext().setVertexIconTransformer(new VisualNodeIcon());
		        	  vv.repaint();
		        	 // vvContainer.getRenderContext().setVertexIconTransformer(new VisualNodeIcon());
		        	  //vvContainer.repaint();
		          } else {
		        	  vv.getRenderContext().setVertexIconTransformer(null);
		        	  vv.repaint();
		        	  //vvContainer.getRenderContext().setVertexIconTransformer(null);
		        	  //vvContainer.repaint();
		          }
		      }
		});
		menuTools.add(icons);
		return menuTools;
	}
	
	private JMenu getMenuHelp() {
		JMenu menuHelp = new JMenu("Help");
		menuHelp.add(new AbstractAction("Contents") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Help
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Contents", "resources/briefhelp.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		menuHelp.add(new AbstractAction("Color Index") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Color Inedx
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Color Index", "resources/colorindex.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		menuHelp.add(new AbstractAction("Import external policy file") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Import external policy file
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Instructions for importing policies from file", "resources/ImportPolicyFile.html", HecataeusMessageDialog.HTML_FILE);
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
		
		menuHelp.add(new AbstractAction("About Hecataeus") {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Credits
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Credits", "resources/credits.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		return menuHelp;
	}
	
	/**
	 * It passes the locations of the layout to the graph vertices
	 */
	protected void getLayoutPositions() {
		VisualGraph gr = (VisualGraph) layout.getGraph();
		for (VisualNode node:gr.getVertices()) {
			gr.setLocation(node, layout.transform(node));
		}
		//gr = (VisualGraph) containerLayout.getGraph();
		//for (VisualNode node:gr.getVertices()) {
		//	gr.setLocation(node, containerLayout.transform(node));
		//}
	}

	/**
	 * It passes the locations of the vertices to the layout of the graph 
	 */
	protected void setLayoutPositions() {
		// get layout graph 
		VisualGraph gr = (VisualGraph) layout.getGraph();
		// first update location of top - level node
		for (VisualNode node: layout.getDelegate().getGraph().getVertices()) {
			layout.setLocation(node, gr.getLocation(node));
		}
		// then update locations of all other nodes
		for (VisualNode node: gr.getVertices()) {
			layout.setLocation(node, gr.getLocation(node));
		}
		 
		//gr = (VisualGraph) containerLayout.getGraph();
		// first update location of top - level node
		//for (VisualNode node: containerLayout.getDelegate().getGraph().getVertices()) {
			//layout.setLocation(node, gr.getLocation(node));
		//}
		// then update locations of all other nodes
		//for (VisualNode node: gr.getVertices()) {
			//containerLayout.setLocation(node, gr.getLocation(node));
		//}
				
	}
	
	/**
	 * sets the layout of the graph 
	 */
	protected void setLayout(VisualLayoutType topLayoutType, VisualLayoutType subLayoutType) {
		//create the logical graph with only module, schema and semantics nodes
		List<VisualNode> logicalNodes= graph.getVertices(NodeCategory.MODULE);
		logicalNodes.addAll(graph.getVertices(NodeCategory.SCHEMA));
		logicalNodes.addAll(graph.getVertices(NodeCategory.SEMANTICS));	
/**
 * @author pmanousi	
 */
logicalNodes.addAll(graph.getVertices(NodeCategory.INOUTSCHEMA));
		VisualGraph logicalGraph= graph.toGraph(logicalNodes);
		// pass the graph to the layout 
		layout.setGraph(logicalGraph);
		//create the top layout graph
		List<VisualNode> topLogicalNodes= logicalGraph.getVertices(NodeCategory.MODULE);
		VisualGraph topLogicalGraph= logicalGraph.toGraph(topLogicalNodes);
		layout.setTopLayoutGraph(topLogicalGraph);
		//set the module-level layout
		layout.setTopLayoutType(topLayoutType);
		//create the sub graphs
		for (VisualNode topNode : topLogicalNodes) {
			List<VisualNode> subGraphNodes = logicalGraph.getModule(topNode);
			subGraphNodes.remove(topNode);
			VisualGraph subGraph =  logicalGraph.toGraph(subGraphNodes);
			layout.setSubLayoutGraph(topNode, subGraph);
		}
		//set the low-level layout
		layout.setSubLayoutType(subLayoutType);
		
		//create the physical graph with only container and module nodes
		List<VisualNode> physicalNodes= graph.getVertices(NodeCategory.CONTAINER);
		physicalNodes.addAll(graph.getVertices(NodeCategory.MODULE));
		VisualGraph physicalGraph= graph.toGraph(physicalNodes);
		// pass the graph to the layout 
		//containerLayout.setGraph(physicalGraph);
		//create the top layout graph
		List<VisualNode> topPhysicalNodes= physicalGraph.getVertices(NodeCategory.CONTAINER);
		VisualGraph topPhysicalGraph= physicalGraph.toGraph(topPhysicalNodes);
		//containerLayout.setTopLayoutGraph(topPhysicalGraph);
		//set the module-level layout
		//containerLayout.setTopLayoutType(topLayoutType);
		//create the sub graphs
		for (VisualNode topNode : topPhysicalNodes) {
			List<VisualNode> subGraphNodes = physicalGraph.getModule(topNode);
			subGraphNodes.remove(topNode);
			VisualGraph subGraph =  physicalGraph.toGraph(subGraphNodes);
			//containerLayout.setSubLayoutGraph(topNode, subGraph);
		}
		//set the low-level layout
		//containerLayout.setSubLayoutType(subLayoutType);

		
//		List<VisualNode> physicalNodes= graph.getVertices(NodeCategory.CONTAINER);
//		VisualGraph containerGraph= graph.toGraph(physicalNodes);
//		for (VisualNode node1: containerGraph.getVertices()) {
//			for (VisualNode node2: containerGraph.getVertices()) {
//				if (!node1.equals(node2)
//						&&graph.isConnected(graph.getModule(node1), graph.getModule(node2))) {
//					VisualEdge newEdge = new VisualEdge("from",EdgeType.EDGE_TYPE_FROM, node1, node2); 
//					containerGraph.addEdge(newEdge);
//				}				
//			}
//		}
				 
//		GraphCollapser collapser = new GraphCollapser(graph);
		// pass the graph to the layout 
//		VisualGraph g = collapser.collapse(graph, containerGraph);
//		containerLayout.setGraph(graph);
		//set the module-level layout
//		containerLayout.setTopLayoutType(containerLayout.getTopLayoutType());
		//set the low-level layout
//		containerLayout.setSubLayoutType(containerLayout.getSubLayoutType());
		
		VisualNodeVisible showAll = (VisualNodeVisible)  vv.getRenderContext().getVertexIncludePredicate();
		showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.MODULE);

//		gpapas not needed here......
		//vv.repaint();
//		vvContainer.repaint();
		
	}
	
	public VisualizationViewer<VisualNode, VisualEdge> getActiveViewer(){
		//if (vv.getWidth()>0) 
			return vv; 
		//else 
			//return vvContainer;
	}
	
	
	protected void zoomToTab(VisualGraph subGraph){
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		Point2D p;String name = null;
		Shape r = activeViewer.getBounds();
		Point2D vvcenter = activeViewer.getCenter();
		VisualNode myNode = null;
//		vv1 = activeViewer;
//		for (VisualNode jungNode: activeViewer.getGraphLayout().getGraph().getVertices()) {
		for(VisualNode jungNode: subGraph.getVertices()){
			System.out.println("o selected kombos " + jungNode.getName());
			name = jungNode.getName();
			if (jungNode.getVisible()) {
				myNode = jungNode;
//				p = activeViewer.getRenderContext().getMultiLayerTransformer().transform(activeViewer.getGraphLayout().transform(jungNode));
//				while (!r.contains(p)) 
//					{
//					scaler.scale(activeViewer, 1 / 1.1f, vvcenter);
//					p = activeViewer.getRenderContext().getMultiLayerTransformer().transform(activeViewer.getGraphLayout().transform(jungNode));
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException ex) {
//					}
//				}
			}
		}
		myNode.setVisible(true);


		subLayout = new VisualAggregateLayout(subGraph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		

		subLayout.setLocation(myNode, subGraph.getCenter());

		vv1 = new Viewers(layout).vv;

		
		vv1.setGraphLayout(subLayout);
		tabbedPane.addTab(name, null, new GraphZoomScrollPane(vv1), "Displays the logical dependencies between modules");

		
		
		
	}
	
	/**
	 * Animated zoom out the graph till no vertex is out of screen view
	 */
	protected void zoomToWindow() {
		
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		// pass properties to visualization viewer
		
		
		Runnable animator = new Runnable() {
			public void run() {
				Shape r = activeViewer.getBounds();
				Point2D vvcenter = activeViewer.getCenter();
				for (VisualNode jungNode: activeViewer.getGraphLayout().getGraph().getVertices()) {
					if (jungNode.getVisible()) {
						Point2D p = activeViewer.getRenderContext().getMultiLayerTransformer().transform(activeViewer.getGraphLayout().transform(jungNode));
						while (!r.contains(p)) 
							{
							scaler.scale(activeViewer, 1 / 1.1f, vvcenter);
							p = activeViewer.getRenderContext().getMultiLayerTransformer().transform(activeViewer.getGraphLayout().transform(jungNode));
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
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		activeViewer.getRenderContext().getMultiLayerTransformer().setToIdentity();
		Point2D vvCenter = activeViewer.getRenderContext().getMultiLayerTransformer().inverseTransform(activeViewer.getCenter());
		final double dx = (vvCenter.getX() - layoutPoint.getX()) / 5;
		final double dy = (vvCenter.getY() - layoutPoint.getY()) / 5;

		Runnable animator = new Runnable() {
			public void run() {
				for (int i = 0; i < 5; i++) {
					activeViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
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
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		
		int width = activeViewer.getWidth();
		int height = activeViewer.getHeight();

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		activeViewer.paint(graphics);
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
					JFileChooser chooser = new JFileChooser(projectConf.curPath);
					FileFilterImpl filter = new FileFilterImpl("sql",
							"DDL File");
					chooser.addChoosableFileFilter(filter);
					int option = chooser.showOpenDialog(content);
					if (option == JFileChooser.APPROVE_OPTION) {
						textFieldDDL.setText(chooser.getSelectedFile().getPath());
						projectConf.curPath = chooser.getSelectedFile().getPath();
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
					JFileChooser chooser = new JFileChooser(projectConf.curPath);
					FileFilterImpl filter = new FileFilterImpl("sql",
							"SQL File");
					chooser.addChoosableFileFilter(filter);
					int option = chooser.showOpenDialog(content);
					if (option == JFileChooser.APPROVE_OPTION) {
						textFieldSQL.setText(chooser.getSelectedFile().getPath());
						projectConf.curPath = chooser.getSelectedFile().getPath();
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
						HecataeusViewer.this.setLayout(VisualLayoutType.Right2LeftTopologicalLayout, VisualLayoutType.Top2DownTopologicalLayout);
						//get new layout's positions
						HecataeusViewer.this.getLayoutPositions();
						HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
						//HecataeusViewer.this.centerAt(containerLayout.getGraph().getCenter());
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
					} catch (Exception e1) {
						 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
			this.setLocationRelativeTo(this.getParent()); 
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
						
					
						if (objChooser.getDBFile()!=null) {
							 // clear the graph to visualize the new one
							 graph.clear();
							 HecataeusSQLParser reader = new HecataeusSQLParser(graph);

							 setCursor(new Cursor(Cursor.WAIT_CURSOR));
							 try {
								 reader.processFile(objChooser.getDBFile());
								 
							 }  catch (SQLException ex) {
								 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
								 throw new HecataeusException(ex.getMessage());
							 } catch (Exception ex) {
								 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
								 throw new HecataeusException(ex.getMessage());
							 }
							 //createJungGraph from EvolutionGraph
							 graph= reader.getParsedGraph();
							 //set the layout of the graph
							 HecataeusViewer.this.setLayout(VisualLayoutType.Right2LeftTopologicalLayout, VisualLayoutType.Left2RightInverseTopologicalLayout);
							//get new layout's positions
							 HecataeusViewer.this.getLayoutPositions();
							 HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
							 //HecataeusViewer.this.centerAt(containerLayout.getGraph().getCenter());
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
						final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
						if (mode == NodeChooser.FOR_EVENT) {
							HecataeusNodeEvents nde = new HecataeusNodeEvents(
									activeViewer, node);
						}
						if (mode == NodeChooser.FOR_POLICY) {
							HecataeusNodePolicies ndp = new HecataeusNodePolicies(
									activeViewer, node);
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
	 * Class for choosing a module of the graph
	 * @author gpapas
	 *
	 */
	private final class ModuleChooser extends JDialog{
		
		final JTree objectTree ;
		
		ModuleChooser(){
			super(frame, "Select a module", true);
			this.setSize(160,440);
			this.setLocationRelativeTo(frame);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setModal(false);
			
			objectTree = new JTree(createTree());
			final MouseListener ml = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) objectTree.getLastSelectedPathComponent();
					if((node !=null)&&(node.getUserObject() instanceof VisualNode)) {
						if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount() == 2) {
							VisualNode u  = (VisualNode) node.getUserObject();
							if (u != null&&u.getVisible()) {
								vv.getPickedVertexState().clear();
								vv.getPickedVertexState().pick(u, true);
//								centerAt(u.getLocation());
								final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
								centerAt(activeViewer.getGraphLayout().transform(u));
							}
						}
					}
				}
			};
			objectTree.addMouseListener(ml);
			 
			final JScrollPane objectsScrollPane = new JScrollPane();
			objectsScrollPane.setViewportView(objectTree);
			this.setContentPane(objectsScrollPane);
			this.setVisible(true);
		}
		 
		 protected TreeNode createTree() {
			 DefaultMutableTreeNode root = new DefaultMutableTreeNode("Modules");
			 
			 for (NodeType nodeType: NodeType.values()) {
				 if (nodeType.getCategory()==NodeCategory.MODULE) {
					 DefaultMutableTreeNode type = new DefaultMutableTreeNode(nodeType);
					 for (VisualNode v : graph.getVertices(nodeType)) {
						 DefaultMutableTreeNode child = new DefaultMutableTreeNode(v);
						 type.add(child);
					 }
					 if (type.getChildCount()>0)
						 root.add(type); 
				 }
			 }
			 return root;
		 }

		 

	}
	
	/**
	 * a driver for this viewer
	 */
	public static void main(String[] args) {
		myViewer = new HecataeusViewer(new VisualGraph());
	}
}
