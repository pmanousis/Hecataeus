package edu.ntua.dblab.hecataeus;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.parser.HecataeusSQLExtensionParser;

@SuppressWarnings("serial")
public class HecataeusPolicyManagerGUI extends JPanel
{
	JPanel filePanel;
	JPanel nodePolicyPanel;
	JPanel policyPanel;

	JTextPane policyEditor;
	JButton saveBtn;
	JButton newBtn;

	JTable policyFiles;
	JButton loadBtn;

	HecataeusViewer viewer;
	DefaultTableModel model;
	private HecataeusProjectConfiguration projectConf;
	JComboBox eventTypeCb;
	JComboBox policyTypeCb;
	JComboBox nodePoliciesCb;
	Vector<String> eventType=new Vector<String>();
	Vector<String> policyType=new Vector<String>();
	Vector<String> nodePolicies=new Vector<String>();
	JTabbedPane tabPane;
	JButton okBtn;
	JLabel epilegmenosKombosLabel;
	VisualNode epilegmenosKombos;
	
	String currentPolicyFilename;

	/**
	 * @author pmanousi
	 * Creates the GUI.
	 * */
	HecataeusPolicyManagerGUI(HecataeusProjectConfiguration projectConfiguration, HecataeusViewer v)
	{
		this.viewer=v;
		projectConf=projectConfiguration;
		String[] colNames={"File"};
		model = new DefaultTableModel(null,colNames);
		this.policyFiles=new JTable(model);
		loadBtn=new JButton("Load");
		loadBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				LOAD();
				UPDATE();
            }
        });
		loadBtn.setEnabled(false);
		filePanel=new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.gridy=0;
		c.gridx=0;
		c.weighty=1;
		c.weightx=1;
		filePanel.add(policyFiles,c);
		c.gridwidth=1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy=1;
		c.weighty=0.01;
		c.gridx=0;
		filePanel.add(loadBtn,c);
		policyPanel=new JPanel(new GridBagLayout());
		policyEditor=new JTextPane();
		policyEditor.setEditable(false);
		saveBtn=new JButton("Save");
		saveBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				SAVE();
				UPDATE();
            }
        });
		saveBtn.setEnabled(false);
		newBtn=new JButton("New");
		newBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				NEW();
				UPDATE();
            }
        });
		newBtn.setEnabled(false);
		JScrollPane editorSlider = new JScrollPane(policyEditor);
		c.fill=GridBagConstraints.BOTH;
		c.gridwidth=2;
		c.weighty=1;
		c.weightx=1;
		c.gridy=0;
		c.gridx=0;
		policyPanel.add(editorSlider,c);		
		c.anchor=GridBagConstraints.PAGE_END;
		c.gridy=1;
		c.weighty=0.01;
		c.gridwidth=1;
		policyPanel.add(newBtn,c);
		c.gridx=1;
		policyPanel.add(saveBtn,c);
		this.epilegmenosKombosLabel=new JLabel();
		tabPane=new JTabbedPane();
		JPanel jpAddPolicy=new JPanel(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.CENTER;

		c.gridy=0;
		c.gridx=0;
		c.weightx=0.01;
		jpAddPolicy.add(new JLabel("Set the event type: "),c);
		c.gridx=1;
		c.weightx=1;
		eventTypeCb=new JComboBox(eventType);
		jpAddPolicy.add(eventTypeCb,c);
		c.gridy=1;
		c.gridx=0;
		c.weightx=0.01;
		jpAddPolicy.add(new JLabel("Set the policy type: "),c);
		c.gridx=1;
		c.weightx=1;
		policyTypeCb=new JComboBox(policyType);
		jpAddPolicy.add(policyTypeCb,c);
		JPanel jpDeletePolicy=new JPanel(new GridBagLayout());
		c.gridy=0;
		c.gridx=0;
		c.weightx=0.01;
		jpDeletePolicy.add(new JLabel("Choose policy to remove: "),c);
		c.gridx=1;
		c.weightx=1;
		nodePoliciesCb=new JComboBox(nodePolicies);
		jpDeletePolicy.add(nodePoliciesCb,c);
		tabPane.addTab("Add policy",jpAddPolicy);
		tabPane.addTab("Delete policy",jpDeletePolicy);
		this.nodePolicyPanel=new JPanel(new GridBagLayout());
		c.fill=GridBagConstraints.CENTER;
		c.gridy=0;
		c.gridx=0;
		c.gridwidth=2;
		c.weighty=0.01;
		this.nodePolicyPanel.add(this.epilegmenosKombosLabel,c);
		c.fill=GridBagConstraints.BOTH;
		c.gridy=1;
		c.gridx=0;
		c.weighty=1;
		this.nodePolicyPanel.add(tabPane,c);
		okBtn=new JButton("OK");
		okBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				OK();
				UPDATE();
            }
        });
		c.weightx=0.5;
		c.gridy=2;
		c.gridwidth=1;
		c.weighty=0.01;
		c.gridx=0;
		this.nodePolicyPanel.add(okBtn,c);
		this.add(filePanel,0);
		this.add(policyPanel,1);
		this.add(nodePolicyPanel,2);
		this.setLayout(new java.awt.GridLayout(3,1));
		UPDATE();
	}

	/**
	 * @author pmanousi
	 * Reverts the policy of the nodes.
	 */
	public void revertPolicies()
	{
		String temp=this.policyEditor.getText();
		temp=temp.replaceAll(" (?i)BLOCK;", " PMANOUSISTEMP ");
		temp=temp.replaceAll(" (?i)PROPAGATE;", " BLOCK;");
		temp=temp.replaceAll(" PMANOUSISTEMP ", " PROPAGATE;");
		this.policyEditor.setText(temp);
	}
	
	
	/**
	 * @author pmanousi
	 * When LOAD or SAVE functions are called they call this function to load to our graph the new policy (the one that is shown in policyEditor).
	 * */
	void loadPolicy()
	{
		File file = new File(this.projectConf.curPath+"/"+this.currentPolicyFilename);
		HecataeusSQLExtensionParser parser = new HecataeusSQLExtensionParser(this.viewer.graph, file);
		try
		{
			parser.processFile();
		}
		catch (HecataeusException ex)
		{
			new HecataeusMessageDialog(this.viewer.frame, "Error importing Policies", ex.getMessage());
		}
		this.viewer.vv.repaint();
		//this.viewer.vvContainer.repaint();
	}

	/**
	 * @author pmanousi
	 * Writes the new policy of policyEditor to the currentPolicyFilename.
	 * */
	void writeNewPolicy()
	{
		File file=new File(this.projectConf.curPath+"/"+this.currentPolicyFilename);
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			System.err.print(e.getMessage());
		}
		FileWriter fstream = null;
		try 
		{
			fstream = new FileWriter(file.getAbsoluteFile());
		}
		catch (IOException e)
		{
			System.err.print(e.getMessage());
		}
		BufferedWriter out = new BufferedWriter(fstream);
		try
		{
			out.write(this.policyEditor.getText());
		}
		catch (IOException e)
		{
			System.err.print(e.getMessage());
		}
		try
		{
			out.close();
		}
		catch (IOException e)
		{
			System.err.print(e.getMessage());
		}
	}

	/**
	 * @author pmanousi
	 * When a new policy for a specific node (epilegmenosKobmos) is requested.
	 * */
	void OK()
	{
		if(tabPane.getSelectedIndex()==0||tabPane.getSelectedIndex()==-1)
		{	// Adding policy
			this.policyEditor.setText("");
			if(this.epilegmenosKombos.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
			{
				for(int i=0;i<this.epilegmenosKombos.getInEdges().size();i++)
				{
					if(this.epilegmenosKombos.getInEdges().get(i).getName().equals("S"))
					{
						this.projectConf.policy+=this.policyEditor.getText()+this.epilegmenosKombos.getInEdges().get(i).getFromNode().getName()+"."+this.epilegmenosKombos.getName()+": on "+((String)eventTypeCb.getSelectedItem())+" then "+((String)policyTypeCb.getSelectedItem())+";\n";						
					}
				}
			}
			else
			{
				this.projectConf.policy+=this.policyEditor.getText()+this.epilegmenosKombos.getName()+": on "+((String)eventTypeCb.getSelectedItem())+" then "+((String)policyTypeCb.getSelectedItem())+";\n";
			}
		}
		else
		{	// Deleting policy
			String[] temp=this.policyEditor.getText().split(";");
			this.projectConf.policy="";
			for(int i=0;i<temp.length;i++)
			{
				if(temp[i].equals(this.nodePoliciesCb.getSelectedItem().toString())==false)
				{
					this.projectConf.policy=this.projectConf.policy+temp[i].toString()+";";
				}
			}
			if(this.projectConf.policy.endsWith("\n;"))
			{
				this.projectConf.policy=this.projectConf.policy.substring(0,this.projectConf.policy.length()-2);	/* I don't know why but there maybe a '\n;' in the end. */
			}
		}
		this.saveBtn.setEnabled(true);
	}

	/**
	 * @author pmanousi
	 * When a new policy file is created.
	 * */
	void NEW()
	{
		
		if((this.currentPolicyFilename=JOptionPane.showInputDialog(this, "Give filename for the new policy file.", "New file name", JOptionPane.PLAIN_MESSAGE)).isEmpty()==false)
		{
			this.policyEditor.setEditable(true);
			this.saveBtn.setEnabled(true);
			if(this.currentPolicyFilename.endsWith(".plc")==false)
			{
				this.currentPolicyFilename+=".plc";
			}
			this.currentPolicyFilename="POLICIES/"+this.currentPolicyFilename;
			this.projectConf.policies.add(this.currentPolicyFilename);
			this.projectConf.policy=this.policyEditor.getText();
			this.projectConf.writeConfig();
			writeNewPolicy();
		}
	}

	/**
	 * @author pmanousi
	 * When user has changed some thing in his policy and wants to save and see it in graph.
	 * */
	void SAVE()
	{
		this.policyEditor.setEditable(false);
		this.saveBtn.setEnabled(false);
		this.projectConf.policy=this.policyEditor.getText();
		writeNewPolicy();
		loadPolicy();
	}

	/**
	 * @author pmanousi
	 * When user wants to load an already existing policy file to graph.
	 * */
	void LOAD()
	{
		currentPolicyFilename=(String)this.policyFiles.getValueAt(this.policyFiles.getSelectedRow(), this.policyFiles.getSelectedColumn());
		this.projectConf.readPolicy(currentPolicyFilename);
		loadPolicy();
	}

	/**
	 * @author pmanousi
	 * Updates the GUI.
	 * */
	void UPDATE()
	{
		//this.policyEditor.setText(projectConf.policy);
		while(this.model.getRowCount()>0)
		{
			this.model.removeRow(0);
		}
		for(int i=0;i<projectConf.policies.toArray().length;i++)
		{
			Vector<String> temp = new Vector<String>();
			temp.add(projectConf.policies.get(i));
			model.insertRow(i, temp);
		}
		if(currentPolicyFilename==null)
		{
			if(this.projectConf.policies.size()>0)
			{
				loadBtn.setEnabled(true);
				newBtn.setEnabled(true);
				currentPolicyFilename=projectConf.policies.get(projectConf.policies.size()-1);
				loadPolicy();
			}
		}
		this.policyEditor.setText(this.projectConf.policy);
		
		this.epilegmenosKombosLabel.setText("NODE: ");
		ChangeEventsComboBox();

		this.eventType.clear();
		this.policyType.clear();
		this.nodePolicies.clear();
		this.eventTypeCb.removeAllItems();
		this.policyTypeCb.removeAllItems();
		this.nodePoliciesCb.removeAllItems();
		this.okBtn.setEnabled(false);
		if(this.viewer.epilegmenosKombos!=null)
		{
			this.epilegmenosKombos=this.viewer.epilegmenosKombos;
			this.okBtn.setEnabled(true);
			
			VisualNode pateras=null;			
			for(int i=0;i<this.epilegmenosKombos.getInEdges().size();i++)
			{
				if(this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT||
						this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT||
						this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS||
						this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SCHEMA)
				{
					pateras=this.epilegmenosKombos.getInEdges().get(i).getFromNode();
				}
			}
			if(epilegmenosKombos.getType().getCategory()== NodeCategory.MODULE){
				this.epilegmenosKombosLabel.setText(this.epilegmenosKombos.getName());
			}
			else{
				this.epilegmenosKombosLabel.setText(this.epilegmenosKombosLabel.getText()+pateras.getName()+"."+this.epilegmenosKombos.getName());
			}
			ChangeEventsComboBox();
			this.policyType.add("PROPAGATE");
			this.policyType.add("BLOCK");
			this.policyType.add("PROMPT");
			
			if(this.epilegmenosKombos!=null && this.policyEditor.getText().contains(this.epilegmenosKombos.getName()+": ")==true)
			{
				if(this.epilegmenosKombos.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
				{
					for(int i=0;i<this.epilegmenosKombos.getInEdges().size();i++)
					{
						if(this.epilegmenosKombos.getInEdges().get(i).getName().equals("S"))
						{
							String[] temp=this.policyEditor.getText().split(";");
							for(int j=0;j<temp.length;j++)
							{
								if(temp[j].contains(this.epilegmenosKombos.getInEdges().get(i).getFromNode().getName()+"."+this.epilegmenosKombos.getName()+": ")==true)
								{
									nodePolicies.add(temp[j]);
								}
							}
						}
					}
				}
				else
				{
					String[] temp=this.policyEditor.getText().split(";");
					for(int i=0;i<temp.length;i++)
					{
						if(temp[i].contains(this.epilegmenosKombos.getName()+": ")==true)
						{
							nodePolicies.add(temp[i]);
						}
					}
				}
			}
		}
		this.validate();
		this.repaint();
	}
	
	/**
	 * @author pmanousi
	 * When user selects another event node, a new list of event types should be shown.
	 * */
	private void ChangeEventsComboBox()
	{
		this.eventType.clear();
		this.eventTypeCb.removeAllItems();
		ArrayList<edu.ntua.dblab.hecataeus.graph.evolution.EventType>temp;
		this.epilegmenosKombos = PopUpClickListener.clickedVertex;
		if(this.epilegmenosKombos==null)
		{
			return;	
		}
		VisualNode pateras=null;			
		for(int i=0;i<this.epilegmenosKombos.getInEdges().size();i++)
		{
			if(this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT||
					this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT||
					this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS||
					this.epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SCHEMA)
			{
				pateras=this.epilegmenosKombos.getInEdges().get(i).getFromNode();
			}
		}
		temp=edu.ntua.dblab.hecataeus.graph.evolution.EventType.values(this.epilegmenosKombos.getType());
		if(pateras != null){
			for(int i=0;i<this.epilegmenosKombos.getInEdges().size();i++)
			{
				if(pateras.getType()==NodeType.NODE_TYPE_RELATION)
				{
					temp.remove(EventType.DELETE_PROVIDER);
					temp.remove(EventType.RENAME_PROVIDER);
					temp.remove(EventType.ADD_ATTRIBUTE_PROVIDER);
				}
				if(this.epilegmenosKombos.getType()!=NodeType.NODE_TYPE_OUTPUT&&pateras.getType()==NodeType.NODE_TYPE_INPUT)
				{
					temp.remove(EventType.DELETE_SELF);
					temp.remove(EventType.RENAME_SELF);
				}
			}
			for(int i=0;i<temp.size();i++)
			{
				this.eventType.add(temp.get(i).toString());
			}
		}
		this.validate();
		this.repaint();
	}
}
