package org.hecataeus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import org.hecataeus.jung.*;
import org.hecataeus.evolution.*;

import edu.uci.ics.jung.visualization.Layout;
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
	
	protected final HecataeusJungNode node;
	final VisualizationViewer vv;
	
	public HecataeusNodeEvents(final VisualizationViewer vv, final HecataeusJungNode node){
		this.setSize(400,500);
		this.setTitle("Events for: "+node.getName());
		this.setModal(true);
		setLocationRelativeTo(vv); 
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
		constraints.reset(2,0,1,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		btnHighlight = new JButton("Highlight");
		gridbag.setConstraints(btnHighlight,constraints);
		highlight.add(btnHighlight);
		btnHighlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get event				
				String eventString = (String) comboBoxHighlight.getSelectedItem();
				HecataeusEvolutionEvents events = node.getHecataeusEvolutionNode().getEvents();
				HecataeusEvolutionEvent eventToHighlight = events.get(HecataeusEventType.toEventType(eventString));
				//get current graph
				Layout ll = vv.getGraphLayout();
				HecataeusJungGraph graph = (HecataeusJungGraph) ll.getGraph();
				//clear previous status of nodes
				for (int i=0; i<graph.getJungNodes().size(); i++){
        			graph.getJungNodes().get(i).getHecataeusEvolutionNode().setStatus(HecataeusStatusType.NO_STATUS);
        		}	
        		for (int i=0; i<graph.getJungEdges().size(); i++){
        			graph.getJungEdges().get(i).getHecataeusEvolutionEdge().setStatus(HecataeusStatusType.NO_STATUS);
        		}	
				
				//initialize algorithm
				graph.getHecataeusEvolutionGraph().initializeChange(eventToHighlight);
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
				String eventString = (String) comboBoxHighlight.getSelectedItem();
				HecataeusEvolutionEvents events = node.getHecataeusEvolutionNode().getEvents();
				HecataeusEvolutionEvent eventToHighlight = events.get(HecataeusEventType.toEventType(eventString));
				//get current graph
				Layout ll = vv.getGraphLayout();
				HecataeusJungGraph graph = (HecataeusJungGraph) ll.getGraph();
				//clear previous status of nodes
				for (int i=0; i<graph.getJungNodes().size(); i++){
        			graph.getJungNodes().get(i).getHecataeusEvolutionNode().setStatus(HecataeusStatusType.NO_STATUS);
        		}	
        		for (int i=0; i<graph.getJungEdges().size(); i++){
        			graph.getJungEdges().get(i).getHecataeusEvolutionEdge().setStatus(HecataeusStatusType.NO_STATUS);
        		}	
				
				//initialize algorithm
				graph.getHecataeusEvolutionGraph().initializeChange(eventToHighlight);
				final showImpact s = new showImpact(eventToHighlight);
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
				String eventString = (String) comboBoxEventType.getSelectedItem();
				// create and add selected event
            	HecataeusEvolutionEvent newEvent = new HecataeusEvolutionEvent(node.getHecataeusEvolutionNode(),HecataeusEventType.toEventType(eventString));
            	boolean eventExists = false;
            	HecataeusEvolutionEvents events = node.getHecataeusEvolutionNode().getEvents();
        		
            	for (int i=0; i<events.size(); i++){
        			HecataeusEvolutionEvent event = events.get(i);
        			if ((event.getEventType()==newEvent.getEventType())&&(event.getEventNode().equals(newEvent.getEventNode())))
        				eventExists=true;
        		}
				if (!eventExists) {
					node.getHecataeusEvolutionNode().addEvent(newEvent);
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
						String eventString = (String) comboBoxEvents.getSelectedItem();
						HecataeusEvolutionEvents events = node.getHecataeusEvolutionNode().getEvents();
						HecataeusEvolutionEvent eventToRemove = events.get(HecataeusEventType.toEventType(eventString));
						node.getHecataeusEvolutionNode().removeEvent(eventToRemove);
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
		for (HecataeusEventType eventType: HecataeusEventType.values(node.getType())) {
			comboBoxEventType.addItem(eventType.toString());
		}
		//fill existing events of node
		if (node.getHasEvents()){
			HecataeusEvolutionEvents events = node.getHecataeusEvolutionNode().getEvents();
			for (int j=0; j<events.size(); j++) {
				HecataeusEvolutionEvent event = events.get(j);
				jTextArea.append(" - "+event.getEventType().toString()+"\n\n");
				comboBoxHighlight.addItem(""+event.getEventType().toString());
				comboBoxEvents.addItem(""+event.getEventType().toString());
            	
			} 
		}  else {
			jTextArea.setText(" No events exist for node: "+ node.getName()); 
			
		}
		jTabbedPane.setSelectedComponent(view);
		
	 }
	//class for presenting the error
	private class showImpact extends JFrame{
		
		public showImpact(HecataeusEvolutionEvent event){
			this.setTitle("Impact of event");
			//this.setModal(true);
			setSize(600,600);
			JPanel content = new JPanel();
			JTextArea textFieldError = new JTextArea();
			JScrollPane pane = new JScrollPane(textFieldError);
	
			content.setLayout(new BorderLayout());
	        content.add(pane, BorderLayout.CENTER);
	        
	        //get current graph
			Layout ll = vv.getGraphLayout();
			HecataeusJungGraph graph = (HecataeusJungGraph) ll.getGraph();
			
			String msg = "Event: " +event.getEventType().toString()+"\tOn "+event.getEventNode().getName()+"\n";
			msg += "Module\t\tNode Name\t\tStatus\n" ;
	        
			for (int i = 0; i < graph.getJungNodes().size(); i++) {
				HecataeusJungNode node = graph.getJungNodes().get(i);
				if ((node.getType()==HecataeusNodeType.NODE_TYPE_QUERY)
						||(node.getType()==HecataeusNodeType.NODE_TYPE_RELATION )
						||(node.getType()==HecataeusNodeType.NODE_TYPE_VIEW)) {
					HecataeusEvolutionNodes nodes = graph.getHecataeusEvolutionGraph().getSubGraph(node.getHecataeusEvolutionNode());
					for (int j = 0; j < nodes.size(); j++) {
						HecataeusEvolutionNode evNode = nodes.get(j);
						if (evNode.getStatus()!=HecataeusStatusType.NO_STATUS) {
							msg += graph.getHecataeusEvolutionGraph().getTopLevelNode(evNode).getName()+"\t\t"+evNode.getName()+"\t\t"+evNode.getStatus().toString()+"\n" ;
						}
					}
				}
			}
			
			textFieldError.setLineWrap(true);
			textFieldError.setEditable(false);
			textFieldError.setText(msg);
			
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setContentPane(content);
			this.setVisible(true);
		}	
	}
}
