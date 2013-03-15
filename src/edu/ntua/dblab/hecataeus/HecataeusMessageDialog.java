package edu.ntua.dblab.hecataeus;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HecataeusMessageDialog extends JDialog implements	HyperlinkListener{

	private static final long serialVersionUID = 1L;
	private	JEditorPane	textField;

	/**
	 * Constructs a new message object
	 * @param title : The title of the message window
	 * @param msg : The message shown
	 * @param type : A string for type of text , i.e. "text/plain", "text/html". Parameter is optional with default value "text/plain". 
	 */
	HecataeusMessageDialog(Frame owner, String title, String msg, String type){
		super(owner, title, true);
		setSize(600,600);
		JPanel content = new JPanel();
		if (type.equals(HecataeusMessageDialog.HTML_FILE))
			try {
				textField = new JEditorPane();
				// Create an HTML viewer to display the URL
				textField.setContentType(HecataeusMessageDialog.HTML_TEXT);
				textField.read(new FileReader(msg), null);
		    	textField.addHyperlinkListener( this );
			} catch (IOException e) {
				textField = new JEditorPane(HecataeusMessageDialog.HTML_TEXT, "Help file "+ msg + " is missing");
			}
			else 
				textField = new JEditorPane(type, msg);



		JScrollPane pane = new JScrollPane(textField);
		content.setLayout(new BorderLayout());
		content.add(pane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setContentPane(content);
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
	}

	/**
	 * Constructs a new message object with default "text/plain" text type
	 * @param title : The title of the message window
	 * @param msg : The message shown
	 */
	HecataeusMessageDialog(Frame owner, String title, String msg) {
		this(owner,title, msg, HecataeusMessageDialog.PLAIN_TEXT);
	}
	
	/**
	 * Constructs a new message object with default "text/plain" text type and no owner
	 * @param title : The title of the message window
	 * @param msg : The message shown
	 */
	HecataeusMessageDialog(String title, String msg) {
		this(null,title, msg, HecataeusMessageDialog.PLAIN_TEXT);
	}

	final static String PLAIN_TEXT = "text/plain";
	final static String HTML_TEXT = "text/html";
	final static String RTF_TEXT = "text/rtf";
	final static String HTML_FILE = "file/html";

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if( event.getEventType() == HyperlinkEvent.EventType.ACTIVATED )
		{
			// Load some cursors
			Cursor cursor = textField.getCursor();
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			this.setCursor(waitCursor);
			textField.setText(event.getURL().toExternalForm());
			this.setCursor(cursor);
		}
	}

}

