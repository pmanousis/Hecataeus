package edu.ntua.dblab.hecataeus;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

import edu.ntua.dblab.hecataeus.graph.visual.VisualFileColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;

public class HecataeusFileColorListGUI  extends JPanel{
	public JList fileColorList;
	private DefaultListModel listModel;
	private VisualGraph g;
	
	public HecataeusFileColorListGUI(HecataeusViewer v){
		this.g = (VisualGraph) v.getActiveViewer().getGraphLayout().getGraph();
		listModel = new DefaultListModel();
//		listModel.addElement("eva");
		//JScrollPane listScrollPane = new JScrollPane();
		fileColorList = new JList(listModel);
		fileColorList.setBackground(UIManager.getColor("background"));
		fileColorList.setVisibleRowCount(57);
		fileColorList.setOpaque(true);
		fileColorList.setValueIsAdjusting(false);
		fileColorList.setSize(20,700);
		this.add(fileColorList);
		this.validate();
		this.repaint();
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
	
}
