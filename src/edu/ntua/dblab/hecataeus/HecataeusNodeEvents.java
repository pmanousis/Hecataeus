package edu.ntua.dblab.hecataeus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvents;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/*
 * Frame fore Setting the Events
 * 
 */
public final class HecataeusNodeEvents extends JDialog {

	protected JTextArea jTextArea;
	protected JComboBox comboBoxEventNode;
	protected JComboBox comboBoxEventType;
	protected JComboBox comboBoxEvents;
	protected JComboBox comboBoxHighlight;
	protected JButton btnAdd;
	protected JButton btnRemove;
	protected JButton btnHighlight;
	protected JButton btnShowImpact;
	protected JPanel remove;
	protected JPanel highlight;
	protected JScrollPane view;
	protected JPanel add;
	JTabbedPane jTabbedPane;
	
	protected final VisualNode node;
	final VisualizationViewer<VisualNode,VisualEdge> vv;
	
	public HecataeusNodeEvents(final VisualizationViewer<VisualNode,VisualEdge> vv, final VisualNode node){
		this.setSize(400,500);
		this.setTitle("Events for: "+node.getName());
		this.setModal(true);
		this.setLocationRelativeTo(vv);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.node=node;
		this.vv = vv;
		jTabbedPane = new JTabbedPane();
	
		// tab for edit existing events
		view = new JScrollPane();
		jTextArea = new JTextArea();			
		jTextArea.setEditable(false);
		view.setViewportView((Component)jTextArea);
		jTabbedPane.addTab(" View ",view);
		
		
		// tab for highlight nodes and edges
		highlight = new JPanel();
		jTabbedPane.addTab(" Highlight ",highlight);
		GridBagLayout gridbag = new GridBagLayout();
		HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
		highlight.setLayout(gridbag);
		

		// label for highlight 
		constraints.reset(0,0,1,1,10,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelHighlight = new JLabel("Choose event: ",JLabel.LEFT);
		gridbag.setConstraints(labelHighlight,constraints);
		highlight.add(labelHighlight);

		// combo box for event
		constraints.reset(1,0,1,1,90,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		comboBoxHighlight = new JComboBox();			
		gridbag.setConstraints(comboBoxHighlight,constraints);
		highlight.add(comboBoxHighlight);
		

		// ok button for highlighting 
		// this is set hidden
		constraints.reset(2,0,1,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		btnHighlight = new JButton("Highlight");
		btnHighlight .setVisible(false);
		gridbag.setConstraints(btnHighlight,constraints);
		highlight.add(btnHighlight);
		btnHighlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get event
				EvolutionEvent<VisualNode> event = (EvolutionEvent<VisualNode>) comboBoxHighlight.getSelectedItem();
				//get current graph
				Layout<VisualNode,VisualEdge> ll = vv.getGraphLayout();
				VisualGraph graph = (VisualGraph) ll.getGraph();
				//clear previous status of nodes
				for (VisualNode n: graph.getVertices()){
        			n.setStatus(StatusType.NO_STATUS,true);
        		}	
				for (VisualEdge edge : graph.getEdges()) {
					edge.setStatus(StatusType.NO_STATUS,true);
        		}	
				//initialize algorithm
				graph.initializeChange(event);
				vv.repaint();
				
			}
		});
		
		// button for showing impact
		constraints.reset(3,0,2,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		btnShowImpact = new JButton("Show impact");
		gridbag.setConstraints(btnShowImpact,constraints);
		highlight.add(btnShowImpact);
		btnShowImpact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get event
				EvolutionEvent<VisualNode> event = (EvolutionEvent<VisualNode>) comboBoxHighlight.getSelectedItem();
				//get current graph
				Layout<VisualNode,VisualEdge> ll = vv.getGraphLayout();
				VisualGraph graph = (VisualGraph) ll.getGraph();
				//clear previous status of nodes
				for (VisualNode u : graph.getVertices()) {
					u.setStatus(StatusType.NO_STATUS,true);
				}	
				for (VisualEdge edge : graph.getEdges()) {
					edge.setStatus(StatusType.NO_STATUS,true);
				}	
				//initialize algorithm
				graph.initializeChange(event);
				vv.repaint();
				//final showImpact s = new showImpact(eventToHighlight);
			}
		});
		
		// tab for add event
		add = new JPanel();
		jTabbedPane.addTab(" Add ",add);
		add.setLayout(gridbag);
		
		// label for event type
		constraints.reset(0,0,1,1,0,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelEventType = new JLabel("Set event type: ",JLabel.LEFT);
		gridbag.setConstraints(labelEventType,constraints);
		add.add(labelEventType);
		
		// combo box fox event type 
		constraints.reset(1,0,1,1,90,0);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		comboBoxEventType = new JComboBox();			
		gridbag.setConstraints(comboBoxEventType,constraints);
		add.add(comboBoxEventType);
	
		// OK button
		constraints.reset(0,3,2,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		btnAdd = new JButton("Create event");
		gridbag.setConstraints(btnAdd,constraints);
		add.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		// get event type from the combo box
				EventType eventType = (EventType) comboBoxEventType.getSelectedItem();
				// create and add selected event
				EvolutionEvent<VisualNode> newEvent = new EvolutionEvent<VisualNode>(/*node,*/eventType);
            	boolean eventExists = false;
            	EvolutionEvents events = node.getEvents();
        		            	
            	for (EvolutionEvent<VisualNode> event : events){
        			if ((event.getEventType()==newEvent.getEventType())&&(event.getEventNode().equals(newEvent.getEventNode())))
        				eventExists=true;
        		}
				if (!eventExists) {
					node.addEvent(newEvent);
					vv.repaint();
					initialize();
				}else
					JOptionPane.showMessageDialog(null,"This event already exists!","Warning Message",JOptionPane.WARNING_MESSAGE);
			} 
        }); 

		
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
		add.add(cancelAddButton);


		// tab for remove event
		remove = new JPanel();
		jTabbedPane.addTab(" Remove ",remove);
		remove.setLayout(gridbag);

		// label for remove event
		constraints.reset(0,0,1,1,10,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelEvent = new JLabel("Choose event to remove: ",JLabel.LEFT);
		gridbag.setConstraints(labelEvent,constraints);
		remove.add(labelEvent);

		// combo box for event
		constraints.reset(1,0,1,1,90,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		comboBoxEvents = new JComboBox();			
		gridbag.setConstraints(comboBoxEvents,constraints);
		remove.add(comboBoxEvents);


		// OK button
		constraints.reset(0,2,2,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		btnRemove = new JButton("Remove event");
		gridbag.setConstraints(btnRemove,constraints);
		remove.add(btnRemove);
		btnRemove.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EvolutionEvent<VisualNode> eventToRemove  = (EvolutionEvent<VisualNode>) comboBoxEvents.getSelectedItem();
						node.removeEvent(eventToRemove);
						vv.repaint();
						initialize();
			}	
		});

		// Cancel button
		constraints.reset(1,2,2,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		JButton cancelRemoveButton = new JButton("Cancel");
		gridbag.setConstraints(cancelRemoveButton,constraints);
		cancelRemoveButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});					
		remove.add(cancelRemoveButton);

		initialize();
		setContentPane(jTabbedPane);
		setVisible(true);
		
	}// end constructor

	protected final void initialize(){
		//clear fields
		jTextArea.setText("");
		comboBoxHighlight.removeAllItems();
		comboBoxEvents.removeAllItems();
		comboBoxEventType.removeAllItems();
		//fill event types
		for (EventType eventType: EventType.values(node.getType())) {
			comboBoxEventType.addItem(eventType);
		}
		//fill existing events of node
		if (node.getHasEvents()){
			EvolutionEvents events = node.getEvents();
			for (EvolutionEvent<VisualNode> event:events) {
				jTextArea.append(" - "+event.getEventType()+"\n\n");
				comboBoxHighlight.addItem(event);
				comboBoxEvents.addItem(event);
			} 
		}  else {
			jTextArea.setText(" No events exist for node: "+ node); 
			
		}
		jTabbedPane.setSelectedComponent(view);
		
	 }
	//class for presenting the error
	private class showImpact extends JFrame{
		
		public showImpact(EvolutionEvent<VisualNode> event){
			this.setTitle("Impact of event");
			//this.setModal(true);
			JPanel content = new JPanel();
			JTextArea textFieldError = new JTextArea();
			JScrollPane pane = new JScrollPane(textFieldError);
	
			content.setLayout(new BorderLayout());
	        content.add(pane, BorderLayout.CENTER);
	        
	        //get current graph
			VisualGraph graph = (VisualGraph) vv.getGraphLayout().getGraph();
			
			String msg = "Event: " +event.getEventType().toString()+"\tOn "+event.getEventNode().getName()+"\n";
			msg += "Module\t\tNode Name\t\tStatus\n" ;
	        
			for (VisualNode node : graph.getVertices()) {
				if (node.getType().getCategory()==NodeCategory.MODULE) {
					List<VisualNode> nodes = graph.getModule(node);
					for (VisualNode evNode : nodes) {
						if (evNode.getStatus()!=StatusType.NO_STATUS) {
							msg += graph.getTopLevelNode(evNode).getName()+"\t\t"+evNode.getName()+"\t\t"+evNode.getStatus().toString()+"\n" ;
						}
					}
				}
			}
			
			textFieldError.setLineWrap(true);
			textFieldError.setEditable(false);
			textFieldError.setText(msg);
			
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setContentPane(content);
			this.setSize(vv.getWidth(),vv.getHeight()/3);
			this.setLocation(vv.getX(),vv.getHeight()-this.getHeight());
			this.setVisible(true);
		}	
	}
}
