package edu.ntua.dblab.hecataeus;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import edu.ntua.dblab.hecataeus.graph.visual.VisualFileColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class HecataeusFileStructureGUI extends JPanel 
{
	protected JTree  m_tree;
	protected DefaultTreeModel m_model;
	protected HecataeusViewer viewer;
	protected VisualizationViewer vv;
	protected VisualizationViewer vvZOOM;
	protected VisualGraph g;
	protected JScrollPane s; 
	DefaultMutableTreeNode top;
	boolean firstTime=true;

	public HecataeusFileStructureGUI(HecataeusViewer v)
	{
		super();
		this.viewer=v;
		this.vv=this.viewer.getActiveViewer();
		this.g=this.viewer.graph;
		//TODO: FIX THIS
		this.vvZOOM=this.viewer.getActiveViewerZOOM();
	}
	
	private DefaultMutableTreeNode createNodes(String folder, Color color)
	{
		VisualFileColor vfs = new VisualFileColor();
		HashMap<String, Color> FileColor = new HashMap<String, Color>(vfs.getFileColorMap());
		DefaultMutableTreeNode node=null;
		CreateIcon ic=new CreateIcon();
		if(firstTime)
		{
			firstTime=false;
			ic.setAttributesOfIcon(color, "folder");
			node= new DefaultMutableTreeNode(new IconData(ic, null, folder.substring(0, folder.indexOf("SQLS/"))));
		}
		else
		{
			ic.setAttributesOfIcon(color, "folder");
			String folderName=folder.substring(folder.indexOf("SQLS/")+5);
			if(folderName.contains("/"))
			{
				folderName=folderName.substring(folderName.lastIndexOf("/")+1);
			}
			node= new DefaultMutableTreeNode(new IconData(ic, null, folderName));
		}
		File fld=new File(folder);
		File[] roots =  fld.listFiles();
		Arrays.sort(roots, new Comparator<File>()
		{
			@Override
			public int compare(File o1, File o2)
			{
				return(o1.getAbsoluteFile().compareTo(o2.getAbsoluteFile()));
			}
		});
		for (int k=0; k<roots.length; k++)
		{
			Color value = FileColor.get(roots[k].getAbsolutePath());
			ic=new CreateIcon();
			if(roots[k].isFile())
			{
				ic.setAttributesOfIcon(value,"file");
				node.add(new DefaultMutableTreeNode(new IconData(ic, null, new FileNode(roots[k]))));
			}
			else if(roots[k].isDirectory())
			{
				ic.setAttributesOfIcon(value,"folder");
				node.add(createNodes(roots[k].getAbsolutePath(), value));
			}
		}
		return node;
	}
  
	public void createPanel(String folder)
	{
		while(getComponentCount()>0)
		{
			remove(0);
		}
		top=createNodes(folder, null);
		m_model = new DefaultTreeModel(top);
		m_tree = new JTree(m_model);
		m_tree.putClientProperty("JTree.lineStyle", "Angled");
		TreeCellRenderer renderer = new IconCellRenderer();
		m_tree.setCellRenderer(renderer);
		m_tree.addTreeExpansionListener(new DirExpansionListener());
		m_tree.addTreeSelectionListener(new DirSelectionListener());
		m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 
		m_tree.setShowsRootHandles(true);
		m_tree.setEditable(false);
		m_tree.setRootVisible(false);
		s = new JScrollPane(m_tree);
		s.setPreferredSize(new Dimension(320, this.viewer.getHeight()-this.viewer.informationPanel.getHeight()-50));
		s.setVisible(true);
        add(s);
	}

	DefaultMutableTreeNode getTreeNode(TreePath path)
	{
		return (DefaultMutableTreeNode)(path.getLastPathComponent());
	}

	FileNode getFileNode(DefaultMutableTreeNode node)
	{
		if (node == null)
		{
			return null;
		}
		Object obj = node.getUserObject();
		if (obj instanceof IconData)
			obj = ((IconData)obj).getObject();
		if (obj instanceof FileNode)
			return (FileNode)obj;
		else
		{
			return null;
		}
	}

    // Make sure expansion is threaded and updating the tree model
    // only occurs within the event dispatching thread.
    class DirExpansionListener implements TreeExpansionListener
    {
        public void treeExpanded(TreeExpansionEvent event)
        {
            final DefaultMutableTreeNode node = getTreeNode(
                event.getPath());
            final FileNode fnode = getFileNode(node);

            Thread runner = new Thread() 
            {
              public void run() 
              {
                if (fnode != null && fnode.expand(node)) 
                {
                  Runnable runnable = new Runnable() 
                  {
                    public void run() 
                    {
                       m_model.reload(node);
                    }
                  };
                  SwingUtilities.invokeLater(runnable);
                }
              }
            };
            runner.start();
        }
        public void treeCollapsed(TreeExpansionEvent event) {}
    }

    
    /**
    * Used for highlighting of modules when a file is clicked in File Structure tab.
    * @author pmanousi
    */
	class DirSelectionListener implements TreeSelectionListener 
	{
		public void valueChanged(TreeSelectionEvent event)
		{
			viewer.setTextToInformationArea("");
			DefaultMutableTreeNode node = getTreeNode(event.getPath());
			FileNode fnode = getFileNode(node);
			PickedState<VisualNode> pickedVertexState = vv.getPickedVertexState();
			pickedVertexState.clear();
			//TODO: FIX THIS
			PickedState<VisualNode> pickedVertexStateZOOM = vvZOOM.getPickedVertexState();
			pickedVertexStateZOOM.clear();
			if(pickedVertexState != null)
			{
				for(VisualNode v : g.getVertices())
				{
					if(fnode!=null && v.getFileName().equals(fnode.getFile().getAbsolutePath()))
					{
						pickedVertexState.pick(v, true);
						pickedVertexStateZOOM.pick(v, true);
					}
				}
			}
		}
	}
}

class IconCellRenderer extends JLabel implements TreeCellRenderer
{
	protected Color m_textSelectionColor;
	protected Color m_textNonSelectionColor;
	protected Color m_bkSelectionColor;
	protected Color m_bkNonSelectionColor;
	protected Color m_borderSelectionColor;
	protected boolean m_selected;
	
	public IconCellRenderer()
	{
		super();
		m_textSelectionColor = UIManager.getColor("Tree.selectionForeground");
		m_textNonSelectionColor = UIManager.getColor("Tree.textForeground");
		m_bkSelectionColor = UIManager.getColor("Tree.selectionBackground");
		m_bkNonSelectionColor = UIManager.getColor("Tree.textBackground");
		m_borderSelectionColor = UIManager.getColor("Tree.selectionBorderColor");
		setOpaque(false);
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Object obj = node.getUserObject();
		setText(obj.toString());
		if (obj instanceof Boolean)
			setText("Retrieving data...");
		if (obj instanceof IconData)
		{
			IconData idata = (IconData)obj;
			if (expanded)
			{
				setIcon(idata.getExpandedIcon());
			}
			else
			{
				setIcon(idata.getIcon());
			}
		}
		else
			setIcon(null);
		setFont(tree.getFont());
		setForeground(sel ? m_textSelectionColor : m_textNonSelectionColor);
		setBackground(sel ? m_bkSelectionColor : m_bkNonSelectionColor);
		m_selected = sel;
		return this;
	}
	    
	public void paintComponent(Graphics g) 
	{
		Color bColor = getBackground();
		Icon icon = getIcon();
		g.setColor(bColor);
		int offset = 0;
		if(icon != null && getText() != null) 
		offset = (icon.getIconWidth() + getIconTextGap());
		g.fillRect(offset, 0, getWidth() - 1 - offset,
		getHeight() - 1);
		if (m_selected) 
		{
			g.setColor(m_borderSelectionColor);
			g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
		}
		super.paintComponent(g);
	}
}

class IconData
{
	protected Icon   m_icon;
	protected Icon   m_expandedIcon;
	protected Object m_data;
	
	public IconData(Icon icon, Object data)
	{
		m_icon = icon;
		m_expandedIcon = null;
		m_data = data;
	}
	
	public IconData(Icon icon, Icon expandedIcon, Object data)
	{
		m_icon = icon;
		m_expandedIcon = expandedIcon;
		m_data = data;
	}
	
	public Icon getIcon() 
	{ 
		return m_icon;
	}
	
	public Icon getExpandedIcon() 
	{ 
		return m_expandedIcon!=null ? m_expandedIcon : m_icon;
	}
	
	public Object getObject() 
	{ 
		return m_data;
	}
	
	public String toString() 
	{ 
		return m_data.toString();
	}
}

class FileNode
{
	protected File m_file;
	public FileNode(File file)
	{
		m_file = file;
	}

	public File getFile() 
	{ 
		return m_file;
	}

	public String toString() 
	{ 
		return m_file.getName().length() > 0 ? m_file.getName() : m_file.getPath();
	}

	public boolean expand(DefaultMutableTreeNode parent)
	{
		DefaultMutableTreeNode flag = (DefaultMutableTreeNode)parent.getFirstChild();
		if (flag==null)    // No flag
			return false;
		Object obj = flag.getUserObject();
		if (!(obj instanceof Boolean))
			return false;      // Already expanded
		parent.removeAllChildren();  // Remove Flag
		File[] files = listFiles();
		if (files == null)
			return true;
		Vector v = new Vector();
		for (int k=0; k<files.length; k++)
		{
			File f = files[k];
			if (!(f.isDirectory()))
				continue;
			FileNode newNode = new FileNode(f);
			boolean isAdded = false;
			for (int i=0; i<v.size(); i++)
			{
				FileNode nd = (FileNode)v.elementAt(i);
				if (newNode.compareTo(nd) < 0)
				{
					v.insertElementAt(newNode, i);
					isAdded = true;
					break;
				}
			}
			if (!isAdded)
				v.addElement(newNode);
		}
		for (int i=0; i<v.size(); i++)
		{
			FileNode nd = (FileNode)v.elementAt(i);
			IconData idata = new IconData(new CreateIcon(), new CreateIcon(), nd);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(idata);
			parent.add(node);
			if (nd.hasSubDirs())
				node.add(new DefaultMutableTreeNode(new Boolean(true)));
		}
		return true;
	}
	
	public boolean hasSubDirs()
	{
		File[] files = listFiles();
		if (files == null)
			return false;
		for (int k=0; k<files.length; k++)
		{
			if (files[k].isDirectory())
			return true;
		}
		return false;
	}
	  
	public int compareTo(FileNode toCompare)
	{
		return  m_file.getName().compareToIgnoreCase(toCompare.m_file.getName() ); 
	}

	protected File[] listFiles()
	{
		if (!m_file.isDirectory())
		{
			return null;
		}
		try
		{
			return m_file.listFiles();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Error reading directory "+m_file.getAbsolutePath(), "Warning", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
}

class CreateIcon implements Icon
{
    private int width = 32;
    private int height = 32;
    private BasicStroke stroke = new BasicStroke(2);
    Color color;
    String type;
    
    public void setAttributesOfIcon(Color c, String t)
    {
    	color=c;
    	type=t;
    }

	public void paintIcon(Component c, Graphics g, int x, int y)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.setStroke(stroke);
        Polygon p =new Polygon();
        if(type=="file")
        {
        	p.addPoint(0,27);
	        p.addPoint(0,10);
	        p.addPoint(17,10);
	        p.addPoint(20,13);
	        p.addPoint(20,27);
        }
        else // if(type=="folder")
        {
	        p.addPoint(0,27);
	        p.addPoint(0,10);
	        p.addPoint(5,5);
	        p.addPoint(18,5);
	        p.addPoint(23,10);
	        p.addPoint(30,10);
	        p.addPoint(32,12);
	        p.addPoint(32,27);
        }
        g2d.fillPolygon(p);
        g2d.dispose();
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }
}
