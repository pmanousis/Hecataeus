package edu.ntua.dblab.hecataeus;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalityType;

@SuppressWarnings("serial")
public class alterSemanticsDialog extends JDialog {
	String whereClause;
	String groupbyClause;

	/**
	 * Create the dialog.
	 */
	public alterSemanticsDialog(String w, String gb, String node) {
		setTitle("Give new semantics for node: ");
		setModalityType(ModalityType.DOCUMENT_MODAL);
		
		whereClause=w;
		groupbyClause=gb;
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel where_lbl = new JLabel("WHERE");
		GridBagConstraints gbc_where_lbl = new GridBagConstraints();
		gbc_where_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_where_lbl.gridx = 0;
		gbc_where_lbl.gridy = 0;
		panel.add(where_lbl, gbc_where_lbl);
		
		final JEditorPane where_editorPane = new JEditorPane();
		GridBagConstraints gbc_where_editorPane = new GridBagConstraints();
		gbc_where_editorPane.insets = new Insets(0, 0, 5, 0);
		gbc_where_editorPane.fill = GridBagConstraints.BOTH;
		gbc_where_editorPane.gridx = 1;
		gbc_where_editorPane.gridy = 0;
		panel.add(where_editorPane, gbc_where_editorPane);
		
		JLabel groupby_lbl = new JLabel("GROUP BY");
		GridBagConstraints gbc_groupby_lbl = new GridBagConstraints();
		gbc_groupby_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_groupby_lbl.gridx = 0;
		gbc_groupby_lbl.gridy = 1;
		panel.add(groupby_lbl, gbc_groupby_lbl);
		
		final JEditorPane groupby_editorPane = new JEditorPane();
		GridBagConstraints gbc_groupby_editorPane = new GridBagConstraints();
		gbc_groupby_editorPane.insets = new Insets(0, 0, 5, 0);
		gbc_groupby_editorPane.fill = GridBagConstraints.BOTH;
		gbc_groupby_editorPane.gridx = 1;
		gbc_groupby_editorPane.gridy = 1;
		panel.add(groupby_editorPane, gbc_groupby_editorPane);
		
		JButton ok_btn = new JButton("OK");
		ok_btn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				whereClause=where_editorPane.getText();
				groupbyClause=groupby_editorPane.getText();
				setVisible(false);
			}
		});
		GridBagConstraints gbc_ok_btn = new GridBagConstraints();
		gbc_ok_btn.gridx = 1;
		gbc_ok_btn.gridy = 2;
		panel.add(ok_btn, gbc_ok_btn);
		
		where_editorPane.setText(whereClause);
		groupby_editorPane.setText(groupbyClause);
		setTitle(getTitle()+node);
	}
	
	public String getWhere()
	{
		return(whereClause);
	}
	
	public String getGroupby()
	{
		return(groupbyClause);
	}
}
