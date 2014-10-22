package edu.ntua.dblab.hecataeus;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;



public class HecataeusInputDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static double  clustering;

	
	
	public HecataeusInputDialog(Frame owner, String title) {
		super(owner, title, true);
		setSize(400,280);
		JPanel content = new JPanel();
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setContentPane(content);
		content.setLayout(null);
		
		
		JLabel lblPouNaStamataei = new JLabel("Poso ksena tha einai ta clusters");
		lblPouNaStamataei.setBounds(12, 12, 351, 30);
		content.add(lblPouNaStamataei);
		
		final JSlider slider = new JSlider();
		
		
		Hashtable<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
		DecimalFormat f = new DecimalFormat("0.0");
		for(int i = 0; i <= 10; i++){
			JLabel label = new JLabel(f.format(i*0.1));
			label.setFont(label.getFont().deriveFont(Font.PLAIN));
			labels.put(i,label);
		 }
		
		
		slider.setLabelTable(labels);
		slider.setMajorTickSpacing(1);
		slider.setValue(10);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setBounds(12, 78, 374, 82);
		slider.setMaximum(10);
		content.add(slider);
		
		JButton btnOk = new JButton("Ok");
		
		 
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	System.out.println(slider.getValue()*0.1);
				clustering = slider.getValue()*0.1;
				dispose();
			}
		});
		btnOk.setBounds(269, 179, 117, 30);
		content.add(btnOk);
		
		JLabel lblDen = new JLabel("0 - den kanei clusters 1 - ola ta clusters einai ksena");
		lblDen.setBounds(12, 36, 374, 30);
		content.add(lblDen);
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}
	
	public double getC(){
		return clustering;
	}
}
