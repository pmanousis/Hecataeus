package edu.ntua.dblab.hecataeus;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.graph.evolution.messages.TopologicalTravel;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeColor;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeFont;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeLabel;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeShape;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNodeVisible;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

@SuppressWarnings("serial")
public class HecataeusEventManagerGUI extends JPanel
{
	JButton pickNodeBtn;
	JButton highlightImpactBtn;
	Vector<String> events;
	JComboBox eventCombo;
	JLabel infoLbl;
	JLabel selectedNodeLbl;
	VisualNode epilegmenosKombos;
	HecataeusViewer viewer;
	
	JButton multipleEvetns;
	JButton multipleEvetnsPlay;
	Container content;
	JLabel evtlbl;
	File eventsFile;

	/**
	 * @author pmanousi
	 * Creates the GUI.
	 * */
	HecataeusEventManagerGUI(HecataeusViewer v)
	{
		this.setLayout(new java.awt.GridBagLayout());
		this.viewer=v;
		pickNodeBtn=new JButton("Pick a node");
		pickNodeBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e){
				final VisualizationViewer<VisualNode, VisualEdge> activeViewer = viewer.getActiveViewer();
				// obtain user input from JOptionPane input dialog
				String nodeName = JOptionPane.showInputDialog( "The name of node to find: ");
				activeViewer.getPickedVertexState().clear();
				VisualNode v = null;
				if(nodeName!=null)
				{
					for (VisualNode u : viewer.graph.getVertices())
					{
						if (u.getName().equals(nodeName.trim().toUpperCase()) && u.getVisible())
						{
							activeViewer.getPickedVertexState().pick(u, true);
							v = u;
						}
					}
				}
				if (v != null)
				{
					viewer.centerAt(activeViewer.getGraphLayout().transform(v));
					viewer.epilegmenosKombos=v;
					epilegmenosKombos=v;
					UPDATE();
				}
				// TODO: FIX THIS
				final VisualizationViewer<VisualNode, VisualEdge> activeViewerZOOM = viewer.getActiveViewerZOOM();
				// obtain user input from JOptionPane input dialog
				activeViewerZOOM.getPickedVertexState().clear();
				v = null;
				if(nodeName!=null)
				{
					for (VisualNode u : viewer.graph.getVertices())
					{
						if (u.getName().equals(nodeName.trim().toUpperCase()) && u.getVisible())
						{
							activeViewerZOOM.getPickedVertexState().pick(u, true);
							v = u;
						}
					}
				}
				if (v != null)
				{
					viewer.centerAt(activeViewerZOOM.getGraphLayout().transform(v));
				}
			}
        });
		infoLbl=new JLabel("Selected Node:\n");
		selectedNodeLbl =new JLabel(); 
		events=new Vector<String>();
		eventCombo=new JComboBox(events);
		highlightImpactBtn=new JButton("Highlight Impact");
		highlightImpactBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				SHOWIMPACT();
            }
        });
		
		multipleEvetns=new JButton("Pick file of events");
		multipleEvetns.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				
				JFileChooser chooser = new JFileChooser(viewer.projectConf.curPath);
				int option = chooser.showOpenDialog(content);
				if (option == JFileChooser.APPROVE_OPTION) {
					eventsFile = chooser.getSelectedFile();
					evtlbl.setText(eventsFile.getName());
				}
            }
		});
		evtlbl=new JLabel();
		multipleEvetnsPlay=new JButton("Play events");
		multipleEvetnsPlay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
            {
				BufferedReader br = null;
		        try
		        {
					br = new BufferedReader(new FileReader(eventsFile));
			        String line = br.readLine();
			        while(line!=null)
			        {
				        String name=line.substring(0,line.indexOf(","));
				        epilegmenosKombos=viewer.graph.findVertexByNameParent(name);
				        VisualNode pateras=null;
						for(int i=0;i<epilegmenosKombos.getInEdges().size();i++)
						{
							if(epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_INPUT||
									epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_OUTPUT||
									epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SEMANTICS||
									epilegmenosKombos.getInEdges().get(i).getType()==EdgeType.EDGE_TYPE_SCHEMA)
							{
								pateras=epilegmenosKombos.getInEdges().get(i).getFromNode();
							}
						}
						if(pateras!=null)
						{
							selectedNodeLbl.setText(pateras.getName()+"."+epilegmenosKombos.getName());
						}
				        events.clear();
				        events.add(line.substring(line.indexOf(",")+1));
				        eventCombo.setSelectedItem(line.substring(line.indexOf(",")+1));
				        SHOWIMPACT();
			        	line=br.readLine();
			        }
	            }
			    catch (Exception ex){
			    	System.err.println("188, "+ex.getLocalizedMessage());
			    }
			    finally
			    {
					try
					{
						br.close();
					}
					catch (IOException e1) {}
			    }
            }

		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.gridy=0;
		c.weightx=1;
		c.insets=new Insets(10, 0, 0, 0);
		this.add(pickNodeBtn,c);
		c.gridy++;
		this.add(infoLbl,c);
		c.insets=new Insets(0, 0, 10, 0);
		c.gridy++;
		this.add(selectedNodeLbl,c);
		c.insets=new Insets(10, 0, 10, 0);
		c.gridy++;
		this.add(eventCombo,c);
		c.gridy++;
		this.add(highlightImpactBtn,c);
		this.highlightImpactBtn.setEnabled(false);
		this.pickNodeBtn.setEnabled(false);
		
		c.gridy++;
		this.add(multipleEvetns,c);
		c.gridy++;
		this.add(evtlbl,c);
		c.gridy++;
		this.add(multipleEvetnsPlay,c);
		
		UPDATE();
	}

	protected void SHOWIMPACT()
	{
		TopologicalTravel pmtt=new TopologicalTravel(this.viewer.graphs.get(0));
		pmtt.travel();
		if(this.epilegmenosKombos!=null)
		{
			this.epilegmenosKombos=this.viewer.graphs.get(0).findVertexByNameParent(this.selectedNodeLbl.getText()); /** @author pmanousi Because of many panes, we need to say specifically to run events on the first one, with the original graph. */
			EvolutionEvent<VisualNode> event =new EvolutionEvent<VisualNode>(EventType.toEventType((String)this.eventCombo.getSelectedItem()));
			event.setEventNode(this.epilegmenosKombos);
			VisualGraph agraph = (VisualGraph)this.viewer.graphs.get(0);
/**
 * @author pmanousi
 * IMHO: This should better be a tree with all events that produced new graphs at a Manager in HecataeusViewer.
 */
			this.viewer.saveXmlForWhatIf(event.toString(), this.selectedNodeLbl.getText());
/**
 * @author pmanousi
 * For reports, if needed uncomment			
 */
//			try
//			{
//			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("time.csv", true)));
//			    out.print(viewer.projectConf.projectName+", "+viewer.policyManagerGui.currentPolicyFilename.substring(viewer.policyManagerGui.currentPolicyFilename.indexOf("/")+1)+", ");
//			    out.close();
//			} catch (IOException e)
//			{}
			
			agraph.initializeChange(event);
			//get new layout's positions
			this.viewer.getLayoutPositions();
			this.viewer.getArchitectureGraphActiveViewer().getRenderContext().setVertexShapeTransformer(new VisualNodeShape());
			this.viewer.getArchitectureGraphActiveViewer().repaint();
			this.viewer.policyManagerGui.loadPolicy();
			this.viewer.showImpact();
		}
	}

	public void UPDATE()
	{
		this.events.clear();
		if(this.viewer.projectConf.projectName.isEmpty()==true)
		{
			this.highlightImpactBtn.setEnabled(false);
			this.pickNodeBtn.setEnabled(false);
		}
		else
		{
			this.pickNodeBtn.setEnabled(true);
			if(this.viewer.epilegmenosKombos!=null)
			{
				this.highlightImpactBtn.setEnabled(true);
				this.epilegmenosKombos=this.viewer.epilegmenosKombos;
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
				if(pateras!=null)
				{
					this.selectedNodeLbl.setText(pateras.getName()+"."+this.epilegmenosKombos.getName());
				}
				ArrayList<EventType> temp=EventType.values(this.epilegmenosKombos.getType());
				temp.remove(EventType.DELETE_PROVIDER);	// These events
				temp.remove(EventType.RENAME_PROVIDER);	//  can not be
				temp.remove(EventType.ADD_ATTRIBUTE_PROVIDER);	// user generated.
				for(int i=0;i<this.epilegmenosKombos.getInEdges().size();i++)
				{
					if(this.epilegmenosKombos.getType()!=NodeType.NODE_TYPE_OUTPUT&&this.epilegmenosKombos.getInEdges().get(i).getFromNode().getType()==NodeType.NODE_TYPE_INPUT)
					{
						temp.remove(EventType.DELETE_SELF);
						temp.remove(EventType.RENAME_SELF);
					}
				}
				for(int i=0;i<temp.size();i++)
				{
					this.events.add(temp.get(i).toString());
				}
			}
		}
	}
}
