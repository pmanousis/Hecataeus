package edu.ntua.dblab.hecataeus;



import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.MyDefaultEdgeLaberRenderer;
import edu.ntua.dblab.hecataeus.graph.visual.VisualAggregateLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualCircleLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdgeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualLayoutType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeIcon;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeNeighborColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeShape;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeStroke;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeStrokeColor;
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

public class HecataeusViewer {
	
	// a dummy counter for disposing or exiting application  
	private static int countOpenViewers = 0;
	public static int countOpenTabs = 0;
	private static int cnt = 0;
	private static int cnt2 = 0;
	private static int cnt3 = 0;
	private static int cnt4 = 0;
	private static int cnt5 = 0;
	private static int cnt6 = 0;
	// the visual graph object
/**@author pmanousi Needed for topologicalTravel so became public. */
	public VisualGraph graph;
//	public VisualSubGraph grafos;
	public Viewers viewer;
	
	protected Container content;
	protected HecataeusProjectConfiguration projectConf;
	protected HecataeusPolicyManagerGUI policyManagerGui;
	protected HecataeusEventManagerGUI eventManagerGui;
	protected VisualNode epilegmenosKombos;
	
	protected JTabbedPane managerTabbedPane;
	protected JTabbedPane tabbedPane;
	
	//private ClosableTabbedPane tabbedPane;
	
	protected JTabbedPane sourceTabbedPane;
	protected int sourceTabbedPaneIndex;
	public static JFrame frame;
	public static boolean nodeSize;
	public static HecataeusViewer myViewer;
	// the scale object for zoom capabilities 
	private final ScalingControl scaler = new CrossoverScalingControl();
	
	protected VisualAggregateLayout layout ;
	protected VisualAggregateLayout containerLayout;
	protected VisualAggregateLayout subLayout ;
	
	private static final String frameTitle = "HECATAEUS";
	// the visual component
	public static VisualizationViewer<VisualNode, VisualEdge> vv;
	protected VisualizationViewer<VisualNode, VisualEdge> vv1;
	public static VisualizationViewer<VisualNode, VisualEdge> vv2;
//	protected final VisualizationViewer<VisualNode, VisualEdge> vv3;
	public static VisualizationViewer<VisualNode, VisualEdge> vveva;
	
	protected List<VisualizationViewer<VisualNode, VisualEdge>> viewers;
	
	private static final String frameIconUrl = "resources/hecataeusIcon.png";
	protected Viewers VisualizationViewer;
	protected MouseListener ml;
	
	protected VisualNodeStroke<Integer,Number> vsh;
	protected VisualNodeStrokeColor vnsc;
	protected MyDefaultEdgeLaberRenderer er;
	protected VisualNodeNeighborColor nnc;
	protected VisualEdgeColor ec;
	
	
	public JList<String> fileColorList;
	private DefaultListModel<String> listModel;
	private JPanel panel_3;
	
	protected final static Object TRANSPARENCY = "transparency";
	public static Map<VisualNode,Number> transparency = new HashMap<VisualNode,Number>();
	
	public static  List<VisualGraph> graphs;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	//	myViewer = new HecataeusViewer(new VisualGraph());
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HecataeusViewer window = new HecataeusViewer(new VisualGraph());
					myViewer = window;
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public void startHecataeus(){
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HecataeusViewer window = new HecataeusViewer(new VisualGraph());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public HecataeusViewer(VisualGraph inGraph) {
		
		projectConf=new HecataeusProjectConfiguration();
		this.graph = inGraph;
		
		this.myViewer = myViewer;
		graphs = new ArrayList<VisualGraph>();
		Dimension prefferedSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		
		viewers = new ArrayList<VisualizationViewer<VisualNode, VisualEdge>>() ;

		// the layout
		layout = new VisualAggregateLayout(graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		
		
//		subLayout = new VisualAggregateLayout(graph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		//the visualization viewer
		
		VisualizationViewer = new Viewers();
		
		vv = VisualizationViewer.SetViewers(layout, this);
		vv.setName("full zoom");
	
		
		viewers.add(vv);
		
		
		countOpenViewers ++;
		
		/**
		 * @author pmanousi
		 * Inform mouse plugins for the viewer (and have access to epilegmenosKombos in order to update managers. 
		 */
				HecataeusModalGraphMouse gm = new HecataeusModalGraphMouse();
				vv.setGraphMouse(gm);
				
				gm.HecataeusViewerPM(this);
		

		
				
		vsh = new VisualNodeStroke<Integer,Number>(vv.getPickedVertexState(), graph, vv);
		vv.getRenderContext().setVertexStrokeTransformer(vsh);
		
		vnsc = new VisualNodeStrokeColor(vv.getPickedVertexState());
		//vv.getRenderContext().setVertexStrokeTransformer(vnsc);
		//vv.
		
		er = new MyDefaultEdgeLaberRenderer(Color.BLUE, Color.GREEN);
		vv.getRenderContext().setEdgeLabelRenderer(er);
				
		initialize();
		
		for(VisualNode nodes : graph.getVertices()) {
			transparency.put(nodes, new Double(0.9));
		}
		
		nnc = new VisualNodeNeighborColor(vv.getPickedVertexState());
		vv.getRenderContext().setVertexFillPaintTransformer(nnc);
	}



	
	/*
	 * 
	 * mipos to update managers prepei na kanei update k ta 2 panes???????????????????
	 * 
	 * 
	 */
	
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
	 * Opens an existing project.
	 */
	private void openProject(){

//		JFileChooser chooser = new JFileChooser(projectConf.curPath+"/OTHER");
//		if ((chooser.showSaveDialog(frame)) == JFileChooser.OPEN_DIALOG) {
			
		FileDialog fd=new FileDialog((Dialog)null, "Select the project you want to open.", FileDialog.LOAD);
		fd.setDirectory(projectConf.curPath);
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
				JFileChooser chooser2 = new JFileChooser(projectConf.curPath+"/XML/");
				FileFilterImpl filter = new FileFilterImpl("xml");
				chooser2.addChoosableFileFilter(filter);
				int option = chooser2.showOpenDialog(content);
//				String Name = HecataeusPopupGraphMousePlugin.Gname();
				if (option == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser2.getSelectedFile();
					graph = graph.importFromXML(file);
					this.graphs.add(graph);
					//TODO theloun allages edw
					//pass the location of the vertices to the layout of the graph
					HecataeusViewer.this.setLayout(VisualLayoutType.ClusteredCircleLayoutC, VisualLayoutType.ClusteredCircleLayoutC);
			//		layout = new VisualLayout(graph);
					
					
//					GraphZoomScrollPane test = new MyPane(vv, graph);
//					test.setSize(5000, 5000);
//					tabbedPane.getTabComponentAt(countOpenTabs);
//					tabbedPane.setTabComponentAt(countOpenTabs, test);

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
						this.graphs.add(graph);
						//TODO theloun allages edw
//						GraphZoomScrollPane test = new MyPane(vv, graph);
//						test.setSize(5000, 5000);
//
//						tabbedPane.setTabComponentAt(countOpenTabs, test);
//						
						HecataeusViewer.this.setLayout(VisualLayoutType.ClusteredCircleLayoutC, VisualLayoutType.ClusteredCircleLayoutC);
						
					///	layout = new VisualLayout(graph);
					}
					catch (IOException e1) {}
					catch (SQLException e1) {}
					catch (Exception e1) {}
				}
			}
			frame.setTitle(frameTitle + " - "+projectConf.projectName);
			VisualCircleLayout vcl = new VisualCircleLayout(this.graph);
			
			
			
			List<String> files = new ArrayList<String>(vcl.getFileNames());
			listModel.removeAllElements();
			for(String f : files){
				listModel.addElement(f);
			}
			fileColorList.setCellRenderer(new HecataeusjListCellColor());
			panel_3.repaint();
			policyManagerGui.UPDATE();
			//TODO theloun allages edw
			//get new layout's positions
			HecataeusViewer.this.getLayoutPositions();
			HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
//			HecataeusViewer.this.zoomToWindow();
			HecataeusViewer.this.zoomToWindow(vv);
			eventManagerGui.UPDATE();
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
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
				//TODO theloun allages edw
				// set the layout of the graph
//				HecataeusViewer.this.setLayout(layout.getTopLayoutType(),layout.getSubLayoutType());
				//get new layout's positions
//				HecataeusViewer.this.getLayoutPositions();	
//				HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
				//HecataeusViewer.this.centerAt(containerLayout.getGraph().getCenter());
//				HecataeusViewer.this.zoomToWindow();
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
	
	public void saveXmlForWhatIf(String event, String node){
		String fileDescription = this.projectConf.curPath+"XML/"+event+"_of_"+node+".xml";
		File file = new File(fileDescription);
		try {
			frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			HecataeusViewer.this.getLayoutPositions();
			layout.getGraph().exportToXML(file);
			frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//			JOptionPane.showMessageDialog(null,"Previous graph saved under: "+fileDescription,"Information",JOptionPane.INFORMATION_MESSAGE);
		} catch (RuntimeException e1) {}
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
//TODO thelei allagi						
						//HecataeusViewer.this.getLayoutPositions();
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
//TODO thelei allagi
					//HecataeusViewer.this.getLayoutPositions();
					//TODO: this only saves the logical graph, Fix for physical graph as well
					layout.getGraph().exportToXML(file);
					frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showMessageDialog(null,"The file was created successfully","Information",JOptionPane.INFORMATION_MESSAGE);
				} catch (RuntimeException e1) {}
			}
		}
	}
	
	
	/**
	 * @author pmanousi
	 * Exits Hecataeus.
	 */
	private void exitHecataeus()
	{
		frame.dispose();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("HECATAEUS");
		Dimension prefferedSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(new Dimension(2004, 1053));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon(frameIconUrl).getImage());
	//	frame.getContentPane().setLayout(new MigLayout("wrap", "[1889.00]", "[999.00]"));
		frame.getContentPane().setLayout(new MigLayout("", "[pref!][][grow,fill]", "[984.00]15[]"));
//		MigLayout layout = new MigLayout(
//				 "", // Layout Constraints
//				 "[grow][][grow]", // Column constraints
//				 "[][shrink 0]"); 
		frame.setSize(prefferedSize);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		content = frame.getContentPane();
		
		JMenu mnNewMenu = new JMenu("Project");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewProject = new JMenuItem("New Project");
		mntmNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newProject();
			}
		});
		mnNewMenu.add(mntmNewProject);
		
		JMenuItem mntmOpenProject = new JMenuItem("Open Project");
		mntmOpenProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openProject();
			}
		});
		mnNewMenu.add(mntmOpenProject);
		
		JMenuItem mntmAddFile = new JMenuItem("Add File");
		mntmAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFile();
			}
		});
		mnNewMenu.add(mntmAddFile);
		
		JMenuItem mntmExportToJpg = new JMenuItem("Export to jpg");
		mntmExportToJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				exportToJpg();
			}
		});
		mnNewMenu.add(mntmExportToJpg);
		
		JMenuItem mntmCloseProject = new JMenuItem("Close Project");
		mntmCloseProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeProject();
			}
		});
		mnNewMenu.add(mntmCloseProject);
		
		JMenuItem mntmSaveProject = new JMenuItem("Save Project");
		mntmSaveProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProject();
			}
		});
		mnNewMenu.add(mntmSaveProject);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitHecataeus();
			}
		});
		mnNewMenu.add(mntmExit);
		
		JMenu mnVisualize = new JMenu("Visualize");
		menuBar.add(mnVisualize);
		
		JRadioButtonMenuItem rdbtnmntmMoveGraph = new JRadioButtonMenuItem("Move Graph");
		mnVisualize.add(rdbtnmntmMoveGraph);
		
		JRadioButtonMenuItem rdbtnmntmPick = new JRadioButtonMenuItem("Pick");
		rdbtnmntmPick.setSelected(true);
		mnVisualize.add(rdbtnmntmPick);
		
		mnVisualize.addSeparator();
		
		JMenuItem mntmZoomIn = new JMenuItem("Zoom in");
		mntmZoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				scaler.scale(activeViewer, 1.1f, activeViewer.getCenter());
			}
		});
		mnVisualize.add(mntmZoomIn);
		
		JMenuItem mntmZoomOut = new JMenuItem("Zoom out");
		mntmZoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				scaler.scale(activeViewer, 1 / 1.1f, activeViewer.getCenter());
			}
		});
		mnVisualize.add(mntmZoomOut);
		
		JMenuItem mntmZoomInWindow = new JMenuItem("Zoom in window");
		mntmZoomInWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				centerAt(((VisualGraph)activeViewer.getGraphLayout().getGraph()).getCenter());
				zoomToWindow(activeViewer);
			}
		});
		mnVisualize.add(mntmZoomInWindow);
		
		mnVisualize.addSeparator();
		
		JMenu mnShow = new JMenu("Show");
		mnVisualize.add(mnShow);
		
		JMenuItem mntmToplevelNodes = new JMenuItem("Top-Level Nodes");
		mntmToplevelNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = getActiveViewer();
				VisualNodeVisible showAll = (VisualNodeVisible) activeViewer.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.MODULE);
				System.out.println("active viewer Top-Level Nodes  " + activeViewer.getName());
				activeViewer.repaint();
				//vvContainer.repaint();
			}
		});
		mnShow.add(mntmToplevelNodes);
		
		JMenuItem mntmMidlevelNodes = new JMenuItem("Mid-Level Nodes");
		mntmMidlevelNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = getActiveViewer();
				VisualNodeVisible showAll = (VisualNodeVisible) activeViewer.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.SCHEMA);
				System.out.println("active viewer Mid-Level Nodes  " + activeViewer.getName());
				activeViewer.repaint();
			}
		});
		mnShow.add(mntmMidlevelNodes);
		
		JMenuItem mntmLowlevelNodes = new JMenuItem("Low-Level Nodes");
		mntmLowlevelNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = getActiveViewer();
				VisualNodeVisible showAll = (VisualNodeVisible)  activeViewer.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.SEMANTICS);
				System.out.println("active viewer Low-Level Nodes  " + activeViewer.getName());
				activeViewer.repaint();
			}
		});
		mnShow.add(mntmLowlevelNodes);
		
		mnShow.addSeparator();
		
		JMenuItem mntmNodesWithStatus = new JMenuItem("Nodes with Status");
		mntmNodesWithStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// reset to default Hecataeus Layout
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = getActiveViewer();
				VisualNodeVisible showAll = (VisualNodeVisible) activeViewer.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.STATUS);
				activeViewer.repaint();
			}
		});
		mnShow.add(mntmNodesWithStatus);
		
		JMenuItem mntmNodesWithPolicy = new JMenuItem("Nodes with Policy");
		mntmNodesWithPolicy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = getActiveViewer();
				VisualNodeVisible showAll = (VisualNodeVisible) activeViewer.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.POLICIES);
				activeViewer.repaint();
			}
		});
		mnShow.add(mntmNodesWithPolicy);
		
		JMenuItem mntmNodesWithEvent = new JMenuItem("Nodes with Event");
		mntmNodesWithEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = getActiveViewer();
				VisualNodeVisible showAll = (VisualNodeVisible) activeViewer.getRenderContext().getVertexIncludePredicate();
				showAll.setVisibleLevel(graph.getVertices(),VisibleLayer.EVENTS);
				activeViewer.repaint();
			}
		});
		mnShow.add(mntmNodesWithEvent);
		
		JMenu mnAlgorithms = new JMenu("Algorithms");
		
		for (final VisualLayoutType layoutType : VisualLayoutType.values()) {
			if(layoutType == VisualLayoutType.ClusteredCircleLayout){
				JMenu mnClusteredCircleLayout = new JMenu("Clustered Circle Layout");
				mnAlgorithms.add(mnClusteredCircleLayout);
				
				JMenuItem mntmQueries = new JMenuItem("Queries");
				mntmQueries.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
						VisualLayoutType layoutType = VisualLayoutType.ClusteredCircleLayoutQ;
						getLayout(activeViewer).setTopLayoutType(layoutType);
						HecataeusViewer.this.getLayoutPositions();
						HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
						HecataeusViewer.this.zoomToWindow(activeViewer);
					}
				});
				mnClusteredCircleLayout.add(mntmQueries);
				
				JMenuItem mntmViews = new JMenuItem("Views");
				mntmViews.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
						VisualLayoutType layoutType = VisualLayoutType.ClusteredCircleLayoutV;
						getLayout(activeViewer).setTopLayoutType(layoutType);
						HecataeusViewer.this.getLayoutPositions();
						HecataeusViewer.this.centerAt(layout.getGraph().getCenter());
						HecataeusViewer.this.zoomToWindow(activeViewer);
					}
				});
				mnClusteredCircleLayout.add(mntmViews);
				
				JMenuItem mntmRelations = new JMenuItem("Relations");
				mntmRelations.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
						VisualLayoutType layoutType = VisualLayoutType.ClusteredCircleLayoutR;
						getLayout(activeViewer).setTopLayoutType(layoutType);
						HecataeusViewer.this.getLayoutPositions();
						centerAt(((VisualGraph)activeViewer.getGraphLayout().getGraph()).getCenter());
						zoomToWindow(activeViewer);
					}
				});
				mnClusteredCircleLayout.add(mntmRelations);
				
				JMenuItem mntmCoC = new JMenuItem("Clusters on a circle");
				mntmCoC.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
						VisualLayoutType layoutType = VisualLayoutType.ClusteredCircleLayoutC;
						getLayout(activeViewer).setTopLayoutType(layoutType);
						HecataeusViewer.this.getLayoutPositions();
						centerAt(((VisualGraph)activeViewer.getGraphLayout().getGraph()).getCenter());
						zoomToWindow(activeViewer);
					}
				});
				mnClusteredCircleLayout.add(mntmCoC);
			}
			else{
				mnAlgorithms.add(new AbstractAction(layoutType.toString()) {
					public void actionPerformed(ActionEvent e) {
						// update the top layout of the graph
						final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
						System.out.println("O ACTIVE VIEWER    "  + activeViewer.getName());
						getLayout(activeViewer).setTopLayoutType(layoutType);   //TODO ksexoriszei ton arxiko apo olous tous allous
						HecataeusViewer.this.getLayoutPositions();
						centerAt(((VisualGraph)activeViewer.getGraphLayout().getGraph()).getCenter());
						zoomToWindow(activeViewer);
					}
				});
			}
		}
		
		mnVisualize.add(mnAlgorithms);
		
		

		
		mnAlgorithms.addSeparator();
		
		JMenuItem mntmRevert = new JMenuItem("Revert");
		mntmRevert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				//update the new layout's positions
				// FIXME: restore original layout
				HecataeusViewer.this.getLayoutPositions();
				HecataeusViewer.this.centerAt(graph.getCenter());
				HecataeusViewer.this.zoomToWindow(activeViewer);
			}
		});
		mnAlgorithms.add(mntmRevert);
		
		mnVisualize.addSeparator();
		
		JCheckBoxMenuItem chckbxmntmNewCheckItem = new JCheckBoxMenuItem("Icons On");
		chckbxmntmNewCheckItem.setSelected(false);
		chckbxmntmNewCheckItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AbstractButton aButton = (AbstractButton) event.getSource();
				boolean selected = aButton.getModel().isSelected();
				if (selected) {
					vv.getRenderContext().setVertexIconTransformer(new VisualNodeIcon());
					vv.repaint();
				} else {
					vv.getRenderContext().setVertexIconTransformer(null);

				    // customize the renderer
			
					vv.repaint();
					//vvContainer.getRenderContext().setVertexIconTransformer(null);
					//vvContainer.repaint();
				}
			}
		});
		mnVisualize.add(chckbxmntmNewCheckItem);
		vv.getRenderContext().setVertexIconTransformer(null);
		vv.repaint();
		
		JCheckBoxMenuItem chckbxmntmBigNodes = new JCheckBoxMenuItem("Big Nodes");
		chckbxmntmBigNodes.setSelected(true);
		chckbxmntmBigNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AbstractButton aButton = (AbstractButton) arg0.getSource();
				boolean selected = aButton.getModel().isSelected();
				if (selected) {
					nodeSize = true;
					new VisualNodeShape();
					vv.repaint();
				}else{
					nodeSize = false;
					new VisualNodeShape();
					vv.repaint();
				}
			}
		});
		chckbxmntmBigNodes.setSelected(true);
		mnVisualize.add(chckbxmntmBigNodes);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmFindNodes = new JMenuItem("Find Node");
		mntmFindNodes.setAccelerator(KeyStroke.getKeyStroke("control F"));
		mntmFindNodes.addActionListener(new ActionListener() {
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
		mnTools.add(mntmFindNodes);
		
		JMenuItem mntmFindEdge = new JMenuItem("Find Edge");
		mntmFindEdge.addActionListener(new ActionListener() {
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
		mnTools.add(mntmFindEdge);
		
		JMenuItem mntmSellectAll = new JMenuItem("Select All");
		mntmSellectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
				for (VisualNode u : graph.getVertices()) {
					if (u.getVisible()) {
						activeViewer.getPickedVertexState().pick(u, true);
					}
				}
			}
		});
		mnTools.add(mntmSellectAll);
		
		mnTools.addSeparator();
		
		JMenuItem mntmModuleSynopsys = new JMenuItem("Module Synopsis");
		mntmModuleSynopsys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Help
				 */
//				final ModuleChooser p = new ModuleChooser();
			}
		});
		mnTools.add(mntmModuleSynopsys);
		
		JMenuItem mntmOutputModuleStructure = new JMenuItem("Output Module Structure");
		mntmOutputModuleStructure.addActionListener(new ActionListener() {
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
		mnTools.add(mntmOutputModuleStructure);
		
		JMenu mnManage = new JMenu("Manage");
		menuBar.add(mnManage);
		
		JMenuItem mntmEvents = new JMenuItem("Events");
		mntmEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_EVENT);
			}
		});
		mnManage.add(mntmEvents);
		
		JMenu mnPolicies = new JMenu("Policies");
		mnManage.add(mnPolicies);
		
		mnManage.addSeparator();
		
		JMenuItem mntmEdit = new JMenuItem("Edit");
		mntmEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NodeChooser ndc = new NodeChooser(NodeChooser.FOR_POLICY);
			}
		});
		mnPolicies.add(mntmEdit);
		
		JMenuItem mntmShow = new JMenuItem("Show Default");
		mntmShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				
				for (int i = 0; i < graph.getDefaultPolicyDecsriptions().size(); i++) {
					msg += graph.getDefaultPolicyDecsriptions().get(i) + "\n";
				}
				final HecataeusMessageDialog m = new HecataeusMessageDialog(frame,  "Default Policies", msg);
			}
		});
		mnPolicies.add(mntmShow);
		
		mnPolicies.addSeparator();
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.addActionListener(new ActionListener() {
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
//					vvContainer.repaint();
				}
			}
		});
		mnPolicies.add(mntmImport);
		
		JMenuItem mntmExport = new JMenuItem("Export");
		mntmExport.addActionListener(new ActionListener() {
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
								// TODO Auto-generated catch block
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
		mnPolicies.add(mntmExport);
		
		JMenu mnOutputEventsHandin = new JMenu("Output Events Flooding");
		mnManage.add(mnOutputEventsHandin);
		
		JMenuItem mntmPerEvent = new JMenuItem("Per Event");
		mntmPerEvent.addActionListener(new ActionListener() {
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
								n.setStatus(StatusType.NO_STATUS, true);
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
		mnOutputEventsHandin.add(mntmPerEvent);
		
		JMenuItem mntmTotal = new JMenuItem("Total");
		mntmTotal.addActionListener(new ActionListener() {
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
								n.setStatus(StatusType.NO_STATUS, true);
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
		mnOutputEventsHandin.add(mntmTotal);
		
		JMenuItem mntmModulesTotalAfected = new JMenuItem("Modules Total Affected");
		mntmModulesTotalAfected.addActionListener(new ActionListener() {
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
									n.setStatus(StatusType.NO_STATUS, true);
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
		mnOutputEventsHandin.add(mntmModulesTotalAfected);
		
		JMenuItem mntmClearAllPolicies = new JMenuItem("Clear All Policies");
		mntmClearAllPolicies.addActionListener(new ActionListener() {
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
//					vvContainer.repaint();
				}
			}
		});
		mnManage.add(mntmClearAllPolicies);
		
		JMenuItem mntmClearAllEvents = new JMenuItem("Clear All Events");
		mntmClearAllEvents.addActionListener(new ActionListener() {
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
//						vvContainer.repaint();
					}
				}
			}
		});
		mnManage.add(mntmClearAllEvents);
		
		JMenuItem mntmClearAllStatuces = new JMenuItem("Clear All Statuses");
		mntmClearAllStatuces.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					v.setStatus(StatusType.NO_STATUS, true);
				}
				for (VisualEdge edge : graph.getEdges()) {
					edge.setStatus(StatusType.NO_STATUS, true);
				}

				vv.repaint();
//				vvContainer.repaint();
			}
		});
		mnManage.add(mntmClearAllStatuces);
		
		JMenuItem mntmInversePolicies = new JMenuItem("Inverse Policies");
		mntmInversePolicies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
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
				vv.repaint();
//				vvContainer.repaint();
				policyManagerGui.revertPolicies();
			}
		});
		mnManage.add(mntmInversePolicies);
		
		JMenuItem mntmPropagateAll = new JMenuItem("Propagate All");
		mntmPropagateAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (VisualNode v : graph.getVertices()) {
					if (v.getHasPolicies()) {
						for (EvolutionPolicy<VisualNode> p : v.getPolicies()) {
							p.setPolicyType(PolicyType.PROPAGATE);
						}
					}
				}
				vv.repaint();
//				vvContainer.repaint();
			}
		});
		mnManage.add(mntmPropagateAll);
		
		JMenu mnMetsics = new JMenu("Metrics");
		menuBar.add(mnMetsics);
		
		JMenu mnCount = new JMenu("Count");
		mnMetsics.add(mnCount);
		
		JMenuItem mntmNodes = new JMenuItem("Nodes");
		mntmNodes.addActionListener(new ActionListener() {
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
		mnCount.add(mntmNodes);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Policies");
		mntmNewMenuItem.addActionListener(new ActionListener() {
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
		mnCount.add(mntmNewMenuItem);
		
		JMenuItem mntmEvents_1 = new JMenuItem("Events");
		mntmEvents_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				int events = HecataeusMetricManager.countEvents(activeGraph.getVertices());
				JOptionPane.showMessageDialog(content, events);
			}
		});
		mnCount.add(mntmEvents_1);
		
		JMenuItem mntmOutpoutForModule = new JMenuItem("Outpout For Module Nodes");
		mntmOutpoutForModule.addActionListener(new ActionListener() {
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
		mnMetsics.add(mntmOutpoutForModule);
		
		JMenu mnOutputForAll = new JMenu("Output For All Nodes");
		mnMetsics.add(mnOutputForAll);
		
		mnMetsics.addSeparator();
		
		JMenuItem mntmDegreeTotal = new JMenuItem("Degree Total");
		mntmDegreeTotal.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmDegreeTotal);
		
		JMenuItem mntmDegreeIn = new JMenuItem("Degree In");
		mntmDegreeIn.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmDegreeIn);
		
		JMenuItem mntmDegreeOut = new JMenuItem("Degree Out");
		mntmDegreeOut.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmDegreeOut);
		
		JMenuItem mntmTransitiveDegree = new JMenuItem("Transitive Degree");
		mntmTransitiveDegree.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmTransitiveDegree);
		
		JMenuItem mntmTransitiveDegreeIn = new JMenuItem("Transitive Degree In");
		mntmTransitiveDegreeIn.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmTransitiveDegreeIn);
		
		JMenuItem mntmTransitiveDegreeOut = new JMenuItem("Transitive Degree Out");
		mntmTransitiveDegreeOut.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmTransitiveDegreeOut);
		
		JMenuItem mntmStrengthTotal = new JMenuItem("Strength Total");
		mntmStrengthTotal.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmStrengthTotal);
		
		JMenuItem mntmStrengthIn = new JMenuItem("Strength In ");
		mntmStrengthIn.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmStrengthIn);
		
		JMenuItem mntmStrengthOut = new JMenuItem("Strength Out");
		mntmStrengthOut.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmStrengthOut);
		
		JMenuItem mntmWeightedDegreeIn = new JMenuItem("Weighted Degree In");
		mntmWeightedDegreeIn.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmWeightedDegreeIn);
		
		JMenuItem mntmWeightedDegreeOut = new JMenuItem("Weighted Strenght In");
		mntmWeightedDegreeOut.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmWeightedDegreeOut);
		
		JMenuItem mntmPolicieDegreeIn = new JMenuItem("Policy Degree In");
		mntmPolicieDegreeIn.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmPolicieDegreeIn);
		
		JMenuItem mntmPolicyDegreeOut = new JMenuItem("Policy Degree Out");
		mntmPolicyDegreeOut.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmPolicyDegreeOut);
		
		JMenuItem mntmEntropyIn = new JMenuItem("Entropy In");
		mntmEntropyIn.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmEntropyIn);
		
		JMenuItem mntmEntropyOuy = new JMenuItem("Entropy Out");
		mntmEntropyOuy.addActionListener(new ActionListener() {
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
		mnOutputForAll.add(mntmEntropyOuy);
		
		JMenuItem mntmEntropyOfGraph = new JMenuItem("Entropy Of Graph");
		mntmEntropyOfGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final VisualGraph activeGraph=(VisualGraph) HecataeusViewer.this.getActiveViewer().getGraphLayout().getGraph();
				float entropy = HecataeusMetricManager.entropyGraph(activeGraph);
				String message = "Entropy Of the Graph: " + entropy;
				JOptionPane.showMessageDialog(content, message);
			}
		});
		mnMetsics.add(mntmEntropyOfGraph);
		
		JMenuItem mntmMaximunEntropy = new JMenuItem("Maximun Entropy");
		mntmMaximunEntropy.addActionListener(new ActionListener() {
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
		mnMetsics.add(mntmMaximunEntropy);
		
		JMenu mnMaximumDegree = new JMenu("Maximum Degree");
		mnMetsics.add(mnMaximumDegree);
		
		JMenuItem mntmTotal_1 = new JMenuItem("Total");
		mntmTotal_1.addActionListener(new ActionListener() {
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
		mnMaximumDegree.add(mntmTotal_1);
		
		JMenuItem mntmIn = new JMenuItem("In");
		mntmIn.addActionListener(new ActionListener() {
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
		mnMaximumDegree.add(mntmIn);
		
		JMenuItem mntmOut = new JMenuItem("Out");
		mntmOut.addActionListener(new ActionListener() {
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
		mnMaximumDegree.add(mntmOut);
		
		JMenu mnMaximumStrength = new JMenu("Maximum Strength");
		mnMetsics.add(mnMaximumStrength);
		
		JMenuItem mntmTotal_2 = new JMenuItem("Total");
		mntmTotal_2.addActionListener(new ActionListener() {
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
		mnMaximumStrength.add(mntmTotal_2);
		
		JMenuItem mntmIn_1 = new JMenuItem("In");
		mntmIn_1.addActionListener(new ActionListener() {
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
		mnMaximumStrength.add(mntmIn_1);
		
		JMenuItem mntmOut_1 = new JMenuItem("Out");
		mntmOut_1.addActionListener(new ActionListener() {
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
		mnMaximumStrength.add(mntmOut_1);
		
		JMenu mnMaximumWeightedDegree = new JMenu("Maximum Weighted Degree");
		mnMetsics.add(mnMaximumWeightedDegree);
		
		JMenuItem mntmIn_2 = new JMenuItem("In");
		mntmIn_2.addActionListener(new ActionListener() {
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
		mnMaximumWeightedDegree.add(mntmIn_2);
		
		JMenu mnMaximumTrasitiveDegree = new JMenu("Maximum Trasitive Degree");
		mnMetsics.add(mnMaximumTrasitiveDegree);
		
		JMenuItem mntmTotal_3 = new JMenuItem("Total");
		mntmTotal_3.addActionListener(new ActionListener() {
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
		mnMaximumTrasitiveDegree.add(mntmTotal_3);
		
		JMenuItem mntmIn_3 = new JMenuItem("In");
		mntmIn_3.addActionListener(new ActionListener() {
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
		mnMaximumTrasitiveDegree.add(mntmIn_3);
		
		JMenuItem mntmOut_2 = new JMenuItem("Out");
		mntmOut_2.addActionListener(new ActionListener() {
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
		mnMaximumTrasitiveDegree.add(mntmOut_2);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmContents = new JMenuItem("Contents");
		mntmContents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Help
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Contents", "resources/briefhelp.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		mnHelp.add(mntmContents);
		
		JMenuItem mntmColorIndex = new JMenuItem("Color Index");
		mntmColorIndex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Color Inedx
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Color Index", "resources/colorindex.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		mnHelp.add(mntmColorIndex);
		
		JMenuItem mntmImportExternalPolicy = new JMenuItem("Import External Policy File");
		mntmImportExternalPolicy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Import external policy file
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Instructions for importing policies from file", "resources/ImportPolicyFile.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		mnHelp.add(mntmImportExternalPolicy);
		
		mnHelp.addSeparator();
		
		JMenuItem mntmAboutHecataeus = new JMenuItem("About Hecataeus");
		mntmAboutHecataeus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * instructions which appear when ask for Credits
				 */
				final HecataeusMessageDialog p = new HecataeusMessageDialog(frame, "Credits", "resources/credits.html", HecataeusMessageDialog.HTML_FILE);
			}
		});
		mnHelp.add(mntmAboutHecataeus);
		
		panel_3 = new JPanel();
		panel_3.setBorder(BorderFactory.createTitledBorder("Colors"));
		listModel = new DefaultListModel<String>();
//		listModel.addElement("eva");
		JScrollPane listScrollPane = new JScrollPane();
		fileColorList = new JList<String>(listModel);
		fileColorList.setBackground(UIManager.getColor("background"));
		fileColorList.setVisibleRowCount(57);
		fileColorList.setValueIsAdjusting(true);
		listScrollPane.setViewportView(fileColorList);

		panel_3.add(listScrollPane);
		frame.getContentPane().add(panel_3, "cell 0 0,growy");
		
		
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, "cell 2 0,growy");

		splitPane.setOneTouchExpandable(true);
		
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(BorderFactory.createTitledBorder("Visual"));

		tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sourceTabbedPane = (JTabbedPane) arg0.getSource();
				sourceTabbedPaneIndex = sourceTabbedPane.getSelectedIndex();
				System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(sourceTabbedPaneIndex));
			}
		});
		splitPane.setLeftComponent(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		
		tabbedPane.addTab("full zoom", null, panel_1, null);

		managerTabbedPane = new JTabbedPane(JTabbedPane.TOP);

		splitPane.setRightComponent(managerTabbedPane);
		
		policyManagerGui = new HecataeusPolicyManagerGUI(projectConf,this);
		eventManagerGui = new HecataeusEventManagerGUI(this);

		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("PMG"));
		managerTabbedPane.addTab("Policy", null, policyManagerGui, null);
		
		JPanel panel_2 = new JPanel();
		managerTabbedPane.addTab("Event", null, eventManagerGui, null);
		
		splitPane.setDividerLocation(0.8);
		splitPane.setResizeWeight(1);
		
		
	}
	
	private JPanel createToolsPanel()
	  {
	    JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createTitledBorder("TOOLS"));
	    
	    Border b1 = BorderFactory.createTitledBorder("Tools");
	    panel.setSize(600, 10);
	    return panel;
	  }
	
	
//	private void addTabbedPane() {
//		// Create ClosableTabbedPane and override the tabAboutToClose
//		// to be notified when certain tab is going to close.
//		tabbedPane = new ClosableTabbedPane() {
//
//			public boolean tabAboutToClose(int tabIndex) {
//				String tab = tabbedPane.getTabTitleAt(tabIndex);
//				int choice = JOptionPane.showConfirmDialog(null,"You are about to close '" + tab+ "'\nDo you want to proceed ?","Confirmation Dialog", JOptionPane.INFORMATION_MESSAGE);
//				if(choice == 1){
//					countOpenTabs--;
//				}
//				return choice == 0; // if returned false tab closing will be
//									// canceled
//			}
//		};
//		frame.getContentPane().add(tabbedPane);
//	}
	
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
	 * copy the visible part of the graph to a file as a jpeg image
	 * 
	 * @param file
	 */
	private void writeJPEGImage(File file) {
		//final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = vv;
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
	
	
	public void zoomToWindow(VisualizationViewer<VisualNode, VisualEdge> currentViewer){
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer;
		
		activeViewer = currentViewer;
		
		Shape r = activeViewer.getBounds();
		Point2D vvcenter = activeViewer.getCenter();
		for (VisualNode jungNode: activeViewer.getGraphLayout().getGraph().getVertices()) {
			if (jungNode.getVisible()) {
				Point2D p = activeViewer.getRenderContext().getMultiLayerTransformer().transform(activeViewer.getGraphLayout().transform(jungNode));
				while (!r.contains(p)) 
					{
					scaler.scale(activeViewer, 1 / 1.1f, vvcenter);
					p = activeViewer.getRenderContext().getMultiLayerTransformer().transform(activeViewer.getGraphLayout().transform(jungNode));
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException ex) {
//					}
				}
			}
		}
		tabbedPane.setComponentAt(getActiveTab(),new GraphZoomScrollPane(currentViewer));
		//tabbedPane.add(new GraphZoomScrollPane(currentViewer));
	}

	
	/**
	 * sets the layout of the graph 
	 */
	protected void setLayout(VisualLayoutType topLayoutType, VisualLayoutType subLayoutType) {
		
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = HecataeusViewer.this.getActiveViewer();
		//create the logical graph with only module, schema and semantics nodes
		List<VisualNode> logicalNodes= graph.getVertices(NodeCategory.MODULE);
		logicalNodes.addAll(graph.getVertices(NodeCategory.SCHEMA));
		logicalNodes.addAll(graph.getVertices(NodeCategory.SEMANTICS));	
/**
 * @author pmanousi	
 */
		logicalNodes.addAll(graph.getVertices(NodeCategory.INOUTSCHEMA));
		VisualGraph logicalGraph = graph.toGraph(logicalNodes);

		// pass the graph to the layout 
		layout.setGraph(logicalGraph);
		//create the top layout graph
		
		final Dimension prefferedSize = Toolkit.getDefaultToolkit().getScreenSize();

		Point2D p2d;
		for(VisualNode node : graph.getVertices()){
			NodeType type = (node.getType());
			if(type.getCategory() == NodeCategory.SCHEMA){
				p2d = new Point2D.Double(prefferedSize.getWidth()-1000, 100+cnt);
				cnt+=40;
				node.setLocation(p2d);
				layout.setLocation(node, p2d);
			}
			else if (type.getCategory()== NodeCategory.MODULE){
				p2d = new Point2D.Double(prefferedSize.getWidth()-200, 100+cnt2);
				cnt2+=100;
				node.setLocation(p2d);layout.setLocation(node, p2d);
			}
			else if (type.getCategory()== NodeCategory.CONTAINER){
				p2d = new Point2D.Double(prefferedSize.getWidth()-600, 100+cnt3);
				cnt3+=40;
				node.setLocation(p2d);layout.setLocation(node, p2d);
			}
			else if (type.getCategory()== NodeCategory.INOUTSCHEMA){
				p2d = new Point2D.Double(prefferedSize.getWidth()-800, 100+cnt4);
				cnt4+=40;
				node.setLocation(p2d);layout.setLocation(node, p2d);
			}
			else if (node.getHasPolicies()){
				p2d = new Point2D.Double(prefferedSize.getWidth()-1000, 100+cnt5);
				cnt5+=40;
				node.setLocation(p2d);layout.setLocation(node, p2d);
			}
			else if (type.getCategory()== NodeCategory.SEMANTICS){
				p2d = new Point2D.Double(prefferedSize.getWidth()-1000, 100+cnt5);
				cnt5+=40;
				node.setLocation(p2d);layout.setLocation(node, p2d);
			}
			else{
				p2d = new Point2D.Double(prefferedSize.getWidth()-300, 100+cnt);
				node.setLocation(p2d);layout.setLocation(node, p2d);
			}
		}
		
		
		
		List<VisualNode> topLogicalNodes= logicalGraph.getVertices(NodeCategory.MODULE);
		VisualGraph topLogicalGraph= logicalGraph.toGraph(topLogicalNodes);


	//	layout.setTopLayoutGraph(topLogicalGraph);
		//set the module-level layout
	//	layout.setTopLayoutType(topLayoutType);
		//create the sub graphs
		for (VisualNode topNode : topLogicalNodes) {
			List<VisualNode> subGraphNodes = logicalGraph.getModule(topNode);
			subGraphNodes.remove(topNode);
			VisualGraph subGraph =  logicalGraph.toGraph(subGraphNodes);
	

	//		layout.setSubLayoutGraph(topNode, subGraph);
		}
		//set the low-level layout
	//	layout.setSubLayoutType(subLayoutType);
		
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
	
	public VisualizationViewer<VisualNode, VisualEdge> getActiveViewer(){
		
		String tabName = sourceTabbedPane.getTitleAt(getActiveTab());
		sourceTabbedPane.getComponentAt(getActiveTab());
//		String tabName = sourceTabbedPane.getTitleAt(sourceTabbedPaneIndex);
//		sourceTabbedPane.getComponentAt(sourceTabbedPaneIndex);
		for(VisualizationViewer<VisualNode, VisualEdge> viewer : viewers){
			if(viewer.getName().equals(tabName)){
				return viewer;
			}
		}
		return vv;
//		//if (vv.getWidth()>0) 
//			return vv; 
//		//else 
//			//return vvContainer;
	}
	
	public int getActiveTab(){
		return sourceTabbedPaneIndex;
	}
	
	public VisualAggregateLayout getLayout(VisualizationViewer<VisualNode, VisualEdge> activeViewer){
		if(activeViewer.getName().compareTo("full zoom") == 0){
			return layout;
		}else{
			return subLayout;
		}
	}
	
@SuppressWarnings("unused")
protected void zoomToModuleTab(List<VisualNode> subNodes, VisualGraph sub){	
		
		Point2D p;String name = null;
		Shape r = vv.getBounds();
		
		VisualGraph Sub = sub;
		this.graphs.add(Sub);
		
//		subLayout = new VisualAggregateLayout(Sub, VisualLayoutType.SpringLayout, VisualLayoutType.SpringLayout);
		subLayout = new VisualAggregateLayout(Sub, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		
		
		vv1 = VisualizationViewer.SetViewers(subLayout, this);

		Sub.setViewerToGraph(vv1);
		
//		Point2D vvcenter = vv1.getCenter();
//		for(int i=0; i < Sub.getVertices().size(); i++){
////			subLayout.setLocation(subNodes.get(i), vvcenter);	
//			
//			vv1.getRenderer().setVertexRenderer((Vertex<VisualNode, VisualEdge>) subNodes.get(i));
//		//	vv1.getRenderer().setVertexRenderer(new GradientVertexRenderer<Integer, Number>( new Color(175,224,228), new Color(133,170,173), true));
//		}

		

		subLayout = new VisualAggregateLayout(Sub, VisualLayoutType.EvaTestLayout1, VisualLayoutType.EvaTestLayout1);
		
		vv1 = VisualizationViewer.SetViewers(subLayout, this);
		GraphZoomScrollPane testPane = new MyPane(subNodes.get(0), vv1, Sub);
		vv1.setGraphLayout(subLayout);
		
		String onoma="";
		onoma+=subNodes.get(0).getName();
		for(int i=1;i<subNodes.size();i++)
		{
			if(subNodes.get(i).getType().getCategory()==NodeCategory.MODULE)
			{
				onoma+="-"+subNodes.get(i).getName();
			}
		}
		vv1.setName(onoma);
		viewers.add(vv1);
		tabbedPane.addTab(onoma, null, testPane, "Displays the logical dependencies between modules");
		
		

		countOpenTabs++;		
		tabbedPane.setSelectedIndex(countOpenTabs);
		tabbedPane.setTabComponentAt(countOpenTabs,new ButtonTabComponent(tabbedPane));
		
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		System.out.println("viewer  " + activeViewer.getName());
//		Sub.setViewerToGraph(vv1);
//		subLayout = new VisualAggregateLayout(Sub, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
//		vv1 = VisualizationViewer.SetViewers(subLayout);
		vv1.repaint();
		


	}
	

	protected void zoomToTab(VisualGraph subGraph){
		final VisualizationViewer<VisualNode, VisualEdge> activeViewer = this.getActiveViewer();
		Point2D p;String name = null;
		Shape r = activeViewer.getBounds();
		Point2D vvcenter = activeViewer.getCenter();
		VisualNode myNode = null;
		for(VisualNode jungNode: subGraph.getVertices()){
			System.out.println("o selected kombos " + jungNode.getName());
			name = jungNode.getName();
			if (jungNode.getVisible()) {
				myNode = jungNode;
			}
		}		
		myNode.setVisible(true);
		subLayout = new VisualAggregateLayout(subGraph, VisualLayoutType.StaticLayout, VisualLayoutType.StaticLayout);
		subLayout.setLocation(myNode, subGraph.getCenter());
//		vv1 = VisualizationViewer.SetViewers(subLayout);		
//		vv1.setGraphLayout(subLayout);
		
		tabbedPane.addTab(name, null, new GraphZoomScrollPane(vv), "Displays the logical dependencies between modules");
	}
	
	
	public static JFrame getHecFrame(){
		return frame;
	}
	
	
	public List<VisualGraph> getGraphs(){
		return this.graphs;
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
}
