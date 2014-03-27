package edu.ntua.dblab.hecataeus;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicies;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionPolicy;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.PolicyType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/*
 * Frame fore Setting the Policies
 * 
 */
public final class HecataeusNodePolicies extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTextArea jTextArea;
	protected JComboBox comboBoxEventType;
	protected JComboBox comboBoxEventNodeParent;
	protected JComboBox comboBoxEventNodeChild;
	protected JComboBox comboBoxPolicyType;
	protected JButton okAddButton;
	protected JComboBox comboBoxPolicies;
	protected JButton okRemoveButton;
	protected JPanel remove;
	protected JPanel add;
	protected JScrollPane view;
	protected final VisualNode node;
	protected final VisualizationViewer<VisualNode,VisualEdge> vv;
	protected final VisualGraph graph;
	protected JTabbedPane jTabbedPane ;
		
	public HecataeusNodePolicies(final VisualizationViewer<VisualNode,VisualEdge> vv, final VisualNode node){
		this.setSize(500,500);
		this.setTitle("Policies for: "+node.getName());
		this.setModal(true);
		setLocationRelativeTo(vv); 
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.node=node;
		this.vv = vv;
	 	
		//get current graph
		Layout<VisualNode, VisualEdge> ll = vv.getGraphLayout();
		graph = (VisualGraph) ll.getGraph();
		
		jTabbedPane = new JTabbedPane();
	
		// tab for edit existing policies
		view = new JScrollPane();
		jTextArea = new JTextArea();			
		jTextArea.setEditable(false);
    			
		view.setViewportView((Component)jTextArea);
		jTabbedPane.addTab(" View ",view);
	
		// tab for add policy
		add = new JPanel();
		jTabbedPane.addTab(" Add ",add);
		GridBagLayout gridbag = new GridBagLayout();
		HecataeusGridBagConstraints constraints = new HecataeusGridBagConstraints();
		add.setLayout(gridbag);
		
		
		// label for top level node
		constraints.reset(0,0,1,1,0,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelNode = new JLabel("Choose a top level node: ",JLabel.LEFT);
		gridbag.setConstraints(labelNode,constraints);
		add.add(labelNode);
		
		// combo box for top level node
		comboBoxEventNodeParent = new JComboBox();			
		constraints.reset(1,0,1,1,0,0);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		gridbag.setConstraints(comboBoxEventNodeParent,constraints);
		comboBoxEventNodeParent.addItem(null);
		//fill comboBox for Node selection
		for (VisualNode parentNode: graph.getVertices()){
			if (parentNode.getType().getCategory()==NodeCategory.MODULE) {
				comboBoxEventNodeParent.addItem(parentNode);
			}
		}
		//add action listener to fill childcombo
		comboBoxEventNodeParent.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					    // get key for parent node
						VisualNode parentNode= (VisualNode) comboBoxEventNodeParent.getSelectedItem();
						//reset child combo 
						comboBoxEventNodeChild.removeAllItems();
						if (parentNode!=null) {
							//get descendant nodes 
							List<VisualNode> subGraph = graph.getModule(parentNode);
							for (VisualNode childNode: subGraph) { 
								//filter only these nodes that are adjacent to node assigned the policy
								//including the node itself
								if ((node.equals(childNode))) {
									comboBoxEventNodeChild.addItem(childNode);									
								} else {
									for (VisualEdge outEdge: node.getOutEdges()){
										if ((node.equals(childNode))||(outEdge.getToNode().equals(childNode)))
											comboBoxEventNodeChild.addItem(childNode);											
									}
								}
							}
					}
		}});
		add.add(comboBoxEventNodeParent);
		
		
		// label for low level node
		constraints.reset(0,1,1,1,0,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		labelNode = new JLabel("Set the event node: ",JLabel.LEFT);
		gridbag.setConstraints(labelNode,constraints);
		add.add(labelNode);
		
		// combo box for low level node
		constraints.reset(1,1,1,1,0,0);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		comboBoxEventNodeChild = new JComboBox();			
		gridbag.setConstraints(comboBoxEventNodeChild,constraints);
		//add action listener to fill event types
		comboBoxEventNodeChild.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						VisualNode eventNode = (VisualNode) comboBoxEventNodeChild.getSelectedItem();
						//reset event combo 
						comboBoxEventType.removeAllItems();
						if (eventNode!=null) {
							//fill appropriate event types
							for (EventType eventType: EventType.values(eventNode.getType())) {
								comboBoxEventType.addItem(eventType);
							}
						}
					}
		});
		add.add(comboBoxEventNodeChild);

		// label for event type
		constraints.reset(0,2,1,1,0,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelEventType = new JLabel("Set the event type: ",JLabel.LEFT);
		gridbag.setConstraints(labelEventType,constraints);
		add.add(labelEventType);
		
		// combo box fox event type 
		constraints.reset(1,2,1,1,90,0);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		comboBoxEventType = new JComboBox();			
		 gridbag.setConstraints(comboBoxEventType,constraints);
		add.add(comboBoxEventType);
		
		// label for policy type
		constraints.reset(0,3,1,1,0,0);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelPolicyType = new JLabel("Set the policy type: ",JLabel.LEFT);
		gridbag.setConstraints(labelPolicyType,constraints);
		add.add(labelPolicyType);
		
		// combo box for policy type
		constraints.reset(1,3,1,1,0,0);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		comboBoxPolicyType = new JComboBox();			
		gridbag.setConstraints(comboBoxPolicyType,constraints);
		add.add(comboBoxPolicyType);
	
		// OK button
		constraints.reset(0,4,2,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		okAddButton = new JButton("Add Policy");
		gridbag.setConstraints(okAddButton,constraints);
		add.add(okAddButton);
		okAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get informations from comboBoxes
				// get event type
				EventType eventType = (EventType) comboBoxEventType.getSelectedItem();
				// get policy type
				PolicyType policyType = (PolicyType) comboBoxPolicyType.getSelectedItem();
				// get event node
				VisualNode eventNode  = (VisualNode) comboBoxEventNodeChild.getSelectedItem();
				if (eventNode!=null) {
					// create and add selected policy
					EvolutionPolicy<VisualNode> newPolicy = new EvolutionPolicy<VisualNode>(eventType/*,eventNode*/, policyType);
					EvolutionPolicies policies = node.getPolicies();
					if (policies.get(eventType/*, eventNode*/)!=null) {
						if (JOptionPane.showConfirmDialog(null,"This policy already exists! Do you want to replace it?","Warning Message",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
							node.addPolicy(newPolicy);
					}else {
						node.addPolicy(newPolicy);
					}
					vv.repaint();
					initialize();
				}
			} // end actionPerformed
		}); // end actionListener
		
		// Cancel button
		constraints.reset(1,4,2,1,0,20);
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
		
		
		// tab for remove policy
		remove = new JPanel();
		jTabbedPane.addTab(" Remove ",remove);
		remove.setLayout(gridbag);

		// label for remove policy
		constraints.reset(0,0,1,1,10,40);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		JLabel labelPolicy = new JLabel("Choose policy to remove: ",JLabel.LEFT);
		gridbag.setConstraints(labelPolicy,constraints);
		remove.add(labelPolicy);

		// combo box fox policy
		constraints.reset(1,0,1,1,90,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		comboBoxPolicies = new JComboBox();			
		gridbag.setConstraints(comboBoxPolicies,constraints);
		remove.add(comboBoxPolicies);

		// OK button
		constraints.reset(0,2,2,1,0,20);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		okRemoveButton = new JButton("Remove policy");
		gridbag.setConstraints(okRemoveButton,constraints);
		remove.add(okRemoveButton);
		okRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EvolutionPolicy<VisualNode> policyToRemove= (EvolutionPolicy<VisualNode>) comboBoxPolicies.getSelectedItem();
				//get  node
				node.removePolicy(policyToRemove);
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
		comboBoxEventType.removeAllItems();
		comboBoxPolicies.removeAllItems();
		comboBoxPolicyType.removeAllItems();
		//fill enumerators for policy types
		for (PolicyType policyType: PolicyType.values()){
			comboBoxPolicyType.addItem(policyType);
		}
		//fill existing policies for node
		if (node.getHasPolicies()){
			for (EvolutionPolicy<VisualNode> policy : node.getPolicies()) {
				jTextArea.append(" - " + policy +"\n\n");
				comboBoxPolicies.addItem(policy);
			} 
		}else{
			jTextArea.setText(" No policies exist for node: "+ node); 
		}
		jTabbedPane.setSelectedComponent(view);
	 }
}

