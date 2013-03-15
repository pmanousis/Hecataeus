package org.hecataeus;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.hecataeus.evolution.HecataeusEventType;
import org.hecataeus.evolution.HecataeusEvolutionEvent;
import org.hecataeus.evolution.HecataeusEvolutionNodes;
import org.hecataeus.evolution.HecataeusEvolutionPolicies;
import org.hecataeus.evolution.HecataeusEvolutionPolicy;
import org.hecataeus.evolution.HecataeusNodeType;
import org.hecataeus.evolution.HecataeusPolicyType;
import org.hecataeus.jung.HecataeusJungEdge;
import org.hecataeus.jung.HecataeusJungGraph;
import org.hecataeus.jung.HecataeusJungNode;
import org.hecataeus.jung.HecataeusJungNodes;

import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/*
 * Frame fore Setting the Policies
 * 
 */
public final class HecataeusNodePolicies extends JDialog {
	
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
	
	protected final HecataeusJungNode node;
	protected final VisualizationViewer vv;
	protected final HecataeusJungGraph graph;
	protected JTabbedPane jTabbedPane ;
	
		
	public HecataeusNodePolicies(final VisualizationViewer vv, final HecataeusJungNode node){
		this.setSize(400,500);
		this.setTitle("Policies for: "+node.getName());
		this.setModal(true);
		setLocationRelativeTo(vv); 
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.node=node;
		this.vv = vv;
	 	
		//get current graph
		Layout ll = vv.getGraphLayout();
		graph = (HecataeusJungGraph) ll.getGraph();
		
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
		comboBoxEventNodeParent.addItem("");
		//fill comboBox for Node selection
		for (int i=0; i<graph.getJungNodes().size(); i++){
			HecataeusJungNode parentNode = graph.getJungNodes().get(i);
			if ((parentNode.getType()==HecataeusNodeType.NODE_TYPE_RELATION)
					||(parentNode.getType()==HecataeusNodeType.NODE_TYPE_QUERY)
					||(parentNode.getType()==HecataeusNodeType.NODE_TYPE_VIEW)) {
					
				comboBoxEventNodeParent.addItem(parentNode.getName()+" ("+parentNode.getType().toString()+") "+(parentNode.getKey()));
			}
		}
		//add action listener to fill childcombo
		comboBoxEventNodeParent.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					    // get key for parent node
						String parentNodeString = (String) comboBoxEventNodeParent.getSelectedItem();
						if (parentNodeString!=null && parentNodeString.trim() !="") {
							//then get the string
							StringTokenizer tokenizer = new StringTokenizer(parentNodeString.trim());
							//get  key as the last token in string
							String parentKey="";
							while (tokenizer.hasMoreTokens()) {
								parentKey = tokenizer.nextToken();
							}
							//get parent node
							HecataeusJungNode parentNode = graph.getNode(parentKey.trim());
							//get descendant nodes 
							HecataeusJungNodes subGraph = graph.getJungNodesFromEvolution(graph.getHecataeusEvolutionGraph().getSubGraph(parentNode.getHecataeusEvolutionNode()));
							//reset child combo 
							comboBoxEventNodeChild.removeAllItems();
							for (int i = 0; i < subGraph.size(); i++) {
								HecataeusJungNode childNode = subGraph.get(i);
								//filter only these nodes that are adjacent to node assigned the policy
								//including the node itself
								if ((node.equals(childNode))) {
									comboBoxEventNodeChild.addItem(childNode.getName()+ " ("+ childNode.getType().toString()+ ") " + (childNode.getKey()));									
								} else {
									for (Iterator<HecataeusJungEdge> iterator = node.getOutEdges().iterator(); iterator.hasNext();){
										HecataeusJungEdge outEdge = (HecataeusJungEdge)iterator.next();
										if ((node.equals(childNode))||(outEdge.getToNode().equals(childNode)))
											comboBoxEventNodeChild.addItem(childNode.getName()+ " ("+ childNode.getType().toString()+ ") " + (childNode.getKey()));											
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
		//add action listener to fill childcombo
		comboBoxEventNodeChild.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String eventNodeString = (String) comboBoxEventNodeChild.getSelectedItem();
						if (eventNodeString!=null && eventNodeString.trim()!="") {
							//then get the string
							StringTokenizer tokenizer = new StringTokenizer(eventNodeString.trim());
							//get  key as the last token in string
							String nodeKey="";
							while (tokenizer.hasMoreTokens()) {
								nodeKey = tokenizer.nextToken();
							}
							//get  node
							HecataeusJungNode eventNode = graph.getNode(nodeKey.trim());
							//fill appropriate event types
							//reset event combo 
							comboBoxEventType.removeAllItems();
							for (HecataeusEventType eventType: HecataeusEventType.values(eventNode.getType())) {
								comboBoxEventType.addItem(eventType.toString());
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
				String eventType = (String) comboBoxEventType.getSelectedItem();
				// get policy type
				String policyType = (String) comboBoxPolicyType.getSelectedItem();
				
				// get event node
				String eventNodeString = (String) comboBoxEventNodeChild.getSelectedItem();
				if (eventNodeString!=null && eventNodeString.trim()!="") {
					StringTokenizer tokenizer = new StringTokenizer(eventNodeString);
					tokenizer.nextToken("("); // name
					tokenizer.nextToken(")"); // type
					//get  key
					String nodeKey = tokenizer.nextToken();
					//get  node
					HecataeusJungNode eventNode = graph.getNode(nodeKey.trim());
					// create and add selected policy
					HecataeusEvolutionPolicy newPolicy = new HecataeusEvolutionPolicy(HecataeusEventType.toEventType(eventType),eventNode.getHecataeusEvolutionNode(),HecataeusPolicyType.toPolicyType(policyType));
					HecataeusEvolutionPolicies policies = node.getHecataeusEvolutionNode().getPolicies();
					HecataeusEvolutionPolicy existingPolicy = policies.get(HecataeusEventType.toEventType(eventType), eventNode.getHecataeusEvolutionNode());
					if (existingPolicy!=null) {
						if (JOptionPane.showConfirmDialog(null,"This policy already exists! Do you want to replace it?","Warning Message",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
							node.getHecataeusEvolutionNode().addPolicy(newPolicy);
					}else {
						node.getHecataeusEvolutionNode().addPolicy(newPolicy);
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
					String policyString = (String) comboBoxPolicies.getSelectedItem();
					StringTokenizer st = new StringTokenizer(policyString);
					policyString= st.nextToken(); // "On"
					
					String eventType = st.nextToken(); // event type
					policyString=st.nextToken(); // "To"
					policyString=st.nextToken(); // eventNodeName
					String eventNodeKey = st.nextToken(); // eventNodeKey
					eventNodeKey = eventNodeKey.replace('(',' ');
					eventNodeKey = eventNodeKey.replace(')',' ');	
					policyString=st.nextToken(); // "then"
					String policyType = st.nextToken(); // policy type
					//get  node
					HecataeusJungNode eventNode = graph.getNode(eventNodeKey.trim());
					//get node's policies
					HecataeusEvolutionPolicies policies = node.getHecataeusEvolutionNode().getPolicies();
					
					HecataeusEvolutionPolicy policyToRemove = policies.get(HecataeusEventType.toEventType(eventType), eventNode.getHecataeusEvolutionNode());
					node.getHecataeusEvolutionNode().removePolicy(policyToRemove);
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
		for (HecataeusPolicyType policyType: HecataeusPolicyType.values()){
			comboBoxPolicyType.addItem(policyType.toString());
		}
		//fill existing policies for node
		if (node.getHasPolicies()){
			HecataeusEvolutionPolicies policies = node.getHecataeusEvolutionNode().getPolicies();
			for (int j=0; j<policies.size(); j++) {
				HecataeusEvolutionPolicy policy= policies.get(j);
				HecataeusEvolutionEvent policyEvent= policy.getSourceEvent();
				jTextArea.append(" - On "+policyEvent.getEventType().toString()+" To "+policyEvent.getEventNode().getName()+" then "+policy.getPolicyType().toString()+"\n\n");
				comboBoxPolicies.addItem("On "+policyEvent.getEventType().toString()+" To "+policyEvent.getEventNode().getName()+" ("+ policyEvent.getEventNode().getKey()+") then "+policy.getPolicyType().toString());
			} 

		}else{
			jTextArea.setText(" No policies exist for node: "+ node.getName()); 
		}
		jTabbedPane.setSelectedComponent(view);
	 }
}

