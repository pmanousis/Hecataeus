package edu.ntua.dblab.hecataeus;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import edu.ntua.dblab.hecataeus.dao.HecataeusDatabase;
import edu.ntua.dblab.hecataeus.dao.HecataeusDatabaseSettings;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.HecataeusException;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for selecting db objects from a DB connection
 * @author gpapas
 *
 */
public final class HecataeusObjectChooser extends JDialog  {

	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane = null;
	private JScrollPane jTablesScrollPane = null;
	private JTable jTablesGrid = null;
	private JScrollPane jViewsScrollPane = null;
	private JTable jViewsGrid = null;
	private JSplitPane jSplitPane= null;
	private JPanel jButtonPanel = null;
	private JButton jOKButton = null;
	private JButton jCancelButton = null;
	
	private HecataeusDatabase _database = null;
	ArrayList<String> _statements = new ArrayList<String> ();  //  @jve:decl-index=0:
	
	VisualGraph _graph = null;
	private JCheckBox jchkDependencies = null;
	public HecataeusObjectChooser(JDialog owner, HecataeusDatabaseSettings DBsettings, VisualGraph graph) throws HecataeusException {
		super(owner,"");
		this._database= new HecataeusDatabase(DBsettings);
		this._graph= graph;
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() throws  HecataeusException {
		this.setSize(500,600);
		this.setTitle("Select DB Objects to Include");
		this.setContentPane(getJSplitPane());
		this.setModal(true);
		this.setResizable(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null); 
		this.setVisible(true);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.jSplitPane	
	 */
	private JSplitPane getJSplitPane() throws  HecataeusException{
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setOneTouchExpandable(false);
			jSplitPane.setBottomComponent(getJButtonPanel());
			jSplitPane.setTopComponent(getJTabbedPane());
			jSplitPane.setDividerLocation(450);
	}
		return jSplitPane;
	}
	
	
	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() throws  HecataeusException{
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Tables", null, getJTablesScrollPane(), null);
			jTabbedPane.addTab("Views", null, getJViewsScrollPane(), null);
			
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jTablesScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJTablesScrollPane() throws  HecataeusException {
		if (jTablesScrollPane == null) {
			jTablesScrollPane = new JScrollPane();
			jTablesScrollPane.setViewportView(getJTablesGrid());
		}
		return jTablesScrollPane;
	}

	/**
	 * This method initializes jTablesGrid	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTablesGrid() throws  HecataeusException{
		final int COLUMN_COUNT = 2;

		if (jTablesGrid == null) {
			//fetch columns
			List<Object> columns = new ArrayList<Object>();
			columns.add(new Boolean(false));
			columns.add("Table Name");
			
			//fetch rows
			List<Object> rows = new ArrayList<Object>();
			ArrayList<String>  names = this._database.getTables();
			for (int i = 0; i < names.size(); i++) {
				Object record[] = new Object[COLUMN_COUNT];
				record[0] = Boolean.FALSE;
				for (int j = 1; j < COLUMN_COUNT; j++) {
					record[j] = new String(names.get(i));
				}
				rows.add(record);
			}
			//populate table
			jTablesGrid = new JTable(new CheckBoxTableModel(rows,columns));
			
			//set columns renderer
			TableColumnModel model = jTablesGrid.getColumnModel();
			CheckBoxHeaderRenderer renderer= new CheckBoxHeaderRenderer();
			model.getColumn(0).setHeaderRenderer(renderer);
			model.getColumn(0).setMaxWidth(50);
		    //set columns listener
			JTableHeader header = jTablesGrid.getTableHeader();
			header.addMouseListener(new HeaderListener(header, renderer));
			    
		}
		return jTablesGrid;
	}

	/**
	 * This method initializes jViewsScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJViewsScrollPane()throws  HecataeusException {
		if (jViewsScrollPane == null) {
			jViewsScrollPane = new JScrollPane();
			jViewsScrollPane.setViewportView(getJViewsGrid());
		}
		return jViewsScrollPane;
	}

	/**
	 * This method initializes jViewsGrid	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJViewsGrid() throws  HecataeusException{
		final int COLUMN_COUNT = 2;

		if (jViewsGrid == null) {
			//fetch columns
			List<Object> columns = new ArrayList<Object>();
			columns.add(new Boolean(false));
			columns.add("View Name");
			//fetch rows
			List<Object> rows = new ArrayList<Object>();
			ArrayList<String>  names = this._database.getViews();
			for (int i = 0; i < names.size(); i++) {
				Object record[] = new Object[COLUMN_COUNT];
				record[0] = Boolean.FALSE;
				for (int j = 1; j < COLUMN_COUNT; j++) {
					record[j] = new String(names.get(i));
				}
				rows.add(record);
			}
			//populate table
			jViewsGrid = new JTable(new CheckBoxTableModel(rows,columns));
			
			//set columns renderer
			TableColumnModel model = jViewsGrid.getColumnModel();
			CheckBoxHeaderRenderer renderer= new CheckBoxHeaderRenderer();
			model.getColumn(0).setHeaderRenderer(renderer);
			model.getColumn(0).setMaxWidth(50);
			
			//set columns listener
			JTableHeader header = jViewsGrid.getTableHeader();
			header.addMouseListener(new HeaderListener(header, renderer));
			    
			
		}
		return jViewsGrid;
	}

	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() throws HecataeusException {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.setLayout(new GridBagLayout());
			HecataeusGridBagConstraints grdCons = new HecataeusGridBagConstraints();
			grdCons.insets = new Insets(0,0,20,100);
			grdCons.gridy = 0;
			grdCons.gridx = 0;
			jButtonPanel.add(getJchkDependencies(), grdCons);
			grdCons.insets = new Insets(0,50,0,50);
			grdCons.gridy = 1;
			grdCons.gridx = 0;
			jButtonPanel.add(getJOKButton(), grdCons);
			grdCons.gridx = 1;
			jButtonPanel.add(getJCancelButton(), grdCons);
			
		}
		return jButtonPanel;
	}

	/**
	 * This method initializes jchkDependencies	
	 * @return  javax.swing.JCheckBox
	 */
	private JCheckBox getJchkDependencies() {
		if (jchkDependencies == null) {
			jchkDependencies = new JCheckBox("Include Foreign Keys");
		}
		return jchkDependencies;
	}

	/**
	 * This method initializes jOKButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJOKButton() throws HecataeusException {
		if (jOKButton == null) {
			jOKButton = new JButton();
			jOKButton.setPreferredSize(new Dimension(80, 30));
			jOKButton.setText("OK");
			jOKButton.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent arg0)  {
					 setCursor(new Cursor(Cursor.WAIT_CURSOR));
					 ArrayList<String> proccessedTables = new ArrayList<String> ();
					 
					 for (int i = 0; i < jTablesGrid.getRowCount(); i++) {
						 if ((Boolean)jTablesGrid.getValueAt(i, 0)) {
							 try {
								 String tableName = jTablesGrid.getValueAt(i, 1).toString();
								 addStatement(proccessedTables, tableName);
							} catch (HecataeusException e) {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
								final HecataeusMessageDialog p = new HecataeusMessageDialog( "Error description: ", e.getMessage());
							}
						 }
					 }
					 
					 for (int i = 0; i < jViewsGrid.getRowCount(); i++) {
						 if ((Boolean)jViewsGrid.getValueAt(i, 0)) {
							 try {
								 String viewName = jViewsGrid.getValueAt(i, 1).toString();
								 for (int j = 0; j < _database.getViewDependencies(viewName).size(); j++) {
									 addStatement(proccessedTables, _database.getViewDependencies(viewName).get(j));									 
								 }
								_statements.add(_database.getViewDefinition(viewName));
							} catch (HecataeusException e) {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
								final HecataeusMessageDialog p = new HecataeusMessageDialog( "Error description: ", e.getMessage());							}
						 }
					 }
					 dispose();
					 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				 }
			});
		}
		return jOKButton;
	}

	
	private void addStatement(ArrayList<String> proccessedTables, String tableName) throws HecataeusException{
		 if (!proccessedTables.contains(tableName)) {
			 proccessedTables.add(tableName);
			 
			 if (jchkDependencies.isSelected()) {
				 ArrayList<String> dependencies = _database.getTableDependencies(tableName);
				 for (int i = 0; i < dependencies.size(); i++) {
					 String referencedTable = dependencies.get(i).toString();
					 //exclude dependencies for self reference foreign keys and already proccessed tables
					 if((!proccessedTables.contains(referencedTable)) 
							 && (referencedTable!=null)
							 &&(!tableName.equals(referencedTable))) {
						 //add referenced tables 
						 proccessedTables.add(referencedTable);
						 _statements.add(_database.getTableDefinition(referencedTable));
						 //for more than 1 level of dependencies
						 // this.addStatement(proccessedTables, dependencies.get(i).toString());
					 }

				 }	
			 }		 
			 _statements.add(_database.getTableDefinition(tableName, jchkDependencies.isSelected()));
		 }
		
	}
	
	public File getDBFile(){
		File f = null;
		try {
			f = File.createTempFile("DatabaseFile", "db");
			FileWriter fop=new FileWriter(f); 
			BufferedWriter bf = new BufferedWriter(fop);  
			for (String statement:this._statements) {
				bf.write(statement+";");
			}
			bf.close();
			f.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}
		 
	/**
	 * This method initializes jCancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCancelButton() {
		if (jCancelButton == null) {
			jCancelButton = new JButton();
			jCancelButton.setPreferredSize(new Dimension(80, 30));
			jCancelButton.setText("Cancel");
			jCancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
		}
		return jCancelButton;
	}
	
	
	class CheckBoxTableModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private   List<Object> data;
		private   List<Object> columns;
		
		public CheckBoxTableModel(List<Object> data, List<Object> columns) {
			this.data = data;
			this.columns = columns;
		}
	
        public int getColumnCount() {
        	return columns == null ? 0 : columns.size();
        }

        public int getRowCount() {
        	return data == null ? 0 : data.size();
        }

        public String getColumnName(int col) {
            return columns.get(col).toString();
        }

        public Object getValueAt(int row, int col) {
        	return ((Object[])data.get(row))[col];
        }
        public Class getColumnClass(int c) {
        	if (data == null || data.size() == 0) {
        		return Object.class;
        	}
        	Object o = getValueAt(0, c);
        	return o == null ? Object.class : o.getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col >0) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
        	((Object[])data.get(row))[col] = value;
        	super.fireTableCellUpdated(row, col);
        }
        
        public void valueChanged(ListSelectionEvent e) {
        	if (!e.getValueIsAdjusting()) {
        		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        		int index = lsm.getMinSelectionIndex();
        		//model.setValueAt(Boolean.TRUE, index, 0);
        	}
        }

    }

	
	 class CheckBoxHeaderRenderer extends JCheckBox implements TableCellRenderer {

		 private static final long serialVersionUID = 1L;
		
		 private boolean isChecked =false;
		 
		 protected CheckBoxHeaderRenderer() {
			 this.setHorizontalAlignment(SwingConstants.CENTER);
		 }

		 public Component getTableCellRendererComponent(JTable table,
				 Object value, boolean isSelected, boolean hasFocus, int row,
				 int column) {
			 getModel().setSelected(isChecked);
			 return this;
		 }
		 
		 protected void click() {
			 isChecked =!isChecked;
		 }
		 
		 protected boolean isHeaderSelected() {
			 return isChecked;
		 }

	 }
	 
	 /**
	 * @author  gpapas
	 */
	class HeaderListener extends MouseAdapter {
		    JTableHeader header;

		    CheckBoxHeaderRenderer renderer;

		    HeaderListener(JTableHeader header, CheckBoxHeaderRenderer renderer) {
		      this.header = header;
		      this.renderer = renderer;
		    }

		    public void mouseClicked(MouseEvent e) {
		      
		      int colIndex = header.columnAtPoint(e.getPoint());
		      JTable table = header.getTable();
		      
		      TableColumn column = table.getColumnModel().getColumn(colIndex);
		      
		      if (colIndex==0) {
		    	  renderer.click();
		    	  header.repaint();
		    	  for (int i = 0; i < table.getRowCount(); i++) {
		    		   table.getModel().setValueAt(renderer.isHeaderSelected(), i, 0);
		    	  }
		    	  table.repaint();
		      }else{
		    	  
		    	  /*List<Object> rows = null;
		    	  table.getColumnModel().g
		    	  Collections.sort();
		    	  for (int i = 0; i < table.getRowCount(); i++) {
		    		  table.getModel().setValueAt(column.getHeaderValue(), i, 0);
		    	  }
		    	  table.repaint();*/
		      }
		    }

		  }

	
}

