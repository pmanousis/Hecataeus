package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualFileColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class HecataeusFileColorListGUI  extends JPanel{
	public JList fileColorList;
	private DefaultListModel listModel;
	private VisualGraph g;
	private VisualizationViewer vv;
	
	public HecataeusFileColorListGUI(HecataeusViewer v){
		this.g = (VisualGraph) v.getActiveViewer().getGraphLayout().getGraph();
		this.vv = v.getActiveViewer();
		
		listModel = new DefaultListModel();
		fileColorList = new JList(listModel);
		fileColorList.setBackground(UIManager.getColor("background"));
		fileColorList.setVisibleRowCount(57);
		fileColorList.setOpaque(true);
		fileColorList.setValueIsAdjusting(false);
		fileColorList.setSize(20,700);
		this.add(fileColorList);
		this.validate();
		this.repaint();
		fileColorList.addMouseListener(new ActionJList(fileColorList));
	}
	
	public void createPanel(VisualGraph graph){
		
		VisualFileColor vfs = new VisualFileColor();
		HashMap<String, Color> FileColor = new HashMap<String, Color>(vfs.getFileColorMap());
		listModel.removeAllElements();
		for(Map.Entry<String, Color> entry : FileColor.entrySet()){
			listModel.addElement(entry.getKey());
		}
		fileColorList.setCellRenderer(new HecataeusjListCellColor(FileColor));
		this.validate();
		this.repaint();
	}
	
	/**
	 * Used for highlighting of modules when a file is clicked in Colors tab.
	 * @author pmanousi
	 */
	class ActionJList extends MouseAdapter{
		protected JList list;
		
		public ActionJList(JList l)
		{
			list = l;
		}
		  
		public void mouseClicked(MouseEvent e)
		{
			int index = list.locationToIndex(e.getPoint());
			PickedState<VisualNode> pickedVertexState = vv.getPickedVertexState();
			GraphElementAccessor<VisualNode,VisualEdge> pickSupport = vv.getPickSupport();
			if(pickedVertexState != null)
			{
				pickedVertexState.clear();
			}
			for(VisualNode v : g.getVertices())
			{
				if(v.getFileName().equals(listModel.getElementAt(index)))
				{
					pickedVertexState.pick(v, true);
				}
			}
		}
	}
	
	
}
