package edu.ntua.dblab.hecataeus;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

import edu.ntua.dblab.hecataeus.graph.visual.VisualCircleLayout;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;

public class HecataeusFileColorListGUI  extends JPanel{
	public JList<String> fileColorList;
	private DefaultListModel<String> listModel;
	private VisualGraph g;
	
	public HecataeusFileColorListGUI(HecataeusViewer v){
		this.g = (VisualGraph) v.getActiveViewer().getGraphLayout().getGraph();
		listModel = new DefaultListModel<String>();
//		listModel.addElement("eva");
		//JScrollPane listScrollPane = new JScrollPane();
		fileColorList = new JList<String>(listModel);
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
		
		VisualCircleLayout vcl = new VisualCircleLayout(graph);
		List<String> files = new ArrayList<String>(vcl.getFileNames());
		listModel.removeAllElements();
		for(String f : files){
			listModel.addElement(f);
		}
		fileColorList.setCellRenderer(new HecataeusjListCellColor());
		this.validate();
		this.repaint();
	}
	
}
