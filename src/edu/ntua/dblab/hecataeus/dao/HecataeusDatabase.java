package edu.ntua.dblab.hecataeus.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import edu.ntua.dblab.hecataeus.HecataeusException;


public class HecataeusDatabase {
	
	private Connection _connection;
	private DatabaseMetaData _metaData;
	private ResultSet _resultSet; 
	private HecataeusDatabaseSettings _dbSettings=null;
	
	public HecataeusDatabase(HecataeusDatabaseSettings settings) throws HecataeusException {
		try {
			this._dbSettings = settings;
			Class.forName(settings.getJdbcDriver());
			this._connection = DriverManager.getConnection(settings.getConnectionString(), settings.getUsername(), settings.getPassword());
			this._metaData = this._connection.getMetaData();
		}
		catch( Exception e ) {
			throw new HecataeusException(e.getMessage());
		}

	}

	public ArrayList<String> getTables() throws HecataeusException {
		
		ArrayList<String>  data = new ArrayList<String>();
		try
		{
			ResultSet res = _metaData.getTables(_connection.getCatalog(), _dbSettings.getDefaultSchema(), "%", 
					new String[] {"TABLE"});
			while (res.next()) {
				data.add(res.getString("TABLE_NAME"));
			}
			return data;
		}
		catch( Exception e ) {
			throw new HecataeusException(e.getMessage());
		}
		
	}
	
	public  ArrayList<String> getViews() throws HecataeusException {
		
		 ArrayList<String> data = new  ArrayList<String>();
		try
		{
			ResultSet res = _metaData.getTables(_connection.getCatalog(), _dbSettings.getDefaultSchema(), "%", 
					new String[] {"VIEW"});
			while (res.next()) {
				data.add(res.getString("TABLE_NAME"));
			}
			return data;
		}
		catch( Exception e ) {
			throw new HecataeusException(e.getMessage());
		}

	}
	/***
	 * Gets a table name and returns the DDL definition of the table
	 * @param tableName: the name of the table
	 * @return the DDL definition of the Table
	 */
	public String getTableDefinition(String tableName) throws HecataeusException {
		return this.getTableDefinition(tableName,false);
		
	}
	
	/***
	 * Gets a table name and returns the DDL definition of the table
	 * @param tableName: the name of the table
	 * @param includeDependencies: Whether foreign keys will be included in the table definition
	 * @return the DDL definition of the Table
	 */
	public String getTableDefinition(String tableName, boolean includeDependencies) throws HecataeusException {
		
		String statement = "";
		final String CREATE_TABLE = "CREATE TABLE %1$s(%2$s);";
		final String CONSTRAINT_PRIMARY = "CONSTRAINT %1$s PRIMARY KEY(%2$s)";
		final String CONSTRAINT_UNIQUE = "CONSTRAINT %1$s UNIQUE(%2$s)";
		final String CONSTRAINT_CHECK = "CONSTRAINT %1$s CHECK(%2$s)";
		final String CONSTRAINT_FOREIGN = "CONSTRAINT %1$s FOREIGN KEY(%2$s) REFERENCES %3$s(%4$s)";
		final String CONSTRAINT_NOTNULL = "NOT NULL";
		
		try {
			
			ResultSet res = _metaData.getTables(_connection.getCatalog(), _dbSettings.getDefaultSchema(), tableName , 
					new String[] {"TABLE"});
			
			String strTable="";
			while (res.next()) {
				strTable = res.getString("TABLE_NAME");
				
				/*System.out.println(
						"   "+res.getString("TABLE_CAT") 
						+ ", "+res.getString("TABLE_SCHEM")
						+ ", "+res.getString("TABLE_NAME")
						+ ", "+res.getString("TABLE_TYPE")
						+ ", "+res.getString("REMARKS")); 
				*/
				
				ResultSet resCol = _metaData.getColumns(_connection.getCatalog(), null, strTable, null);
				String strColumns="";
				while (resCol.next()) {
					strColumns += resCol.getString("COLUMN_NAME")+" varchar(25) " + (resCol.getString("NULLABLE").equals("0")? CONSTRAINT_NOTNULL :"") + " ,";
					
					/*
					System.out.println(
							"   "+resCol.getString("TABLE_CAT") 
							+ ", "+resCol.getString("TABLE_SCHEM")
							+ ", "+resCol.getString("TABLE_NAME")
							+ ", "+resCol.getString("COLUMN_NAME")
							+ ", "+resCol.getString("TYPE_NAME")
							+ ", "+resCol.getString("NULLABLE")
							+ ", "+resCol.getString("COLUMN_DEF")
							+ ", "+resCol.getString("ORDINAL_POSITION")
							//+ ", "+resCol.getString("SCOPE_CATLOG")
							//+ ", "+resCol.getString("SCOPE_SCHEMA")
							//+ ", "+resCol.getString("SCOPE_TABLE")
							//+ ", "+resCol.getString("SOURCE_DATA_TYPE")
					);
					*/
				}
				resCol.close();
				
				//when constraints added, no need for trimming last ,
				strColumns =  strColumns.trim().substring(0,strColumns.trim().length()-1);
				//statement = statement.replace(",)", ");");
				
				/**********primary keys******************/
				ResultSet resPrimaryKeys = _metaData.getPrimaryKeys(_connection.getCatalog(), _dbSettings.getDefaultSchema(), strTable);
				String  strPrimaryKey = "";
				String  strPrimaryKeyName = "";
				String  strPrimaryKeyColumns= "";
				boolean hasPKConstraints = false;
				while (resPrimaryKeys.next()) {
					hasPKConstraints = true;
					strPrimaryKeyName = resPrimaryKeys.getString("PK_NAME");
					strPrimaryKeyColumns +=resPrimaryKeys.getString("COLUMN_NAME") + ",";
					/*
					System.out.println(
							"   "+resPrimaryKeys.getString("TABLE_CAT") 
							+ ", "+resPrimaryKeys.getString("TABLE_SCHEM")
							+ ", "+resPrimaryKeys.getString("TABLE_NAME")
							+ ", "+resPrimaryKeys.getString("COLUMN_NAME")
							+ ", "+resPrimaryKeys.getString("KEY_SEQ")
							+ ", "+resPrimaryKeys.getString("PK_NAME")
					);
					*/
				}
				if (hasPKConstraints) {
					strPrimaryKeyColumns = strPrimaryKeyColumns.trim().substring(0,strPrimaryKeyColumns.trim().length()-1);
					strPrimaryKey = String.format(CONSTRAINT_PRIMARY, strPrimaryKeyName,strPrimaryKeyColumns );
				}
				resPrimaryKeys.close();
				
				//System.out.println("**********unique keys******************");
				ResultSet resUniqueKeys = _metaData.getIndexInfo(_connection.getCatalog(), _dbSettings.getDefaultSchema(), res.getString("TABLE_NAME"),true,true);
				String  strUniqueKey = "";
				String  strUniqueKeyName = "";
				String  strUniqueKeyColumns= "";
				String  strUniqueKeyPreviousName = "";
				boolean hasUKConstraints = false;
				while (resUniqueKeys.next()) {
					
					// exclude default unique index with null column name and unique index for primary key
					if ((resUniqueKeys.getString("COLUMN_NAME")!=null) && (!resUniqueKeys.getString("INDEX_NAME").equals(strPrimaryKeyName))) {
						hasUKConstraints = true;
						//there are multiple unique keys, they are identified by the index_name,
						strUniqueKeyName = resUniqueKeys.getString("INDEX_NAME");
						if (strUniqueKeyName.equals(strUniqueKeyPreviousName)) {
							strUniqueKeyColumns +=resUniqueKeys.getString("COLUMN_NAME") + ",";
						}else{
							//built the previous constraint clause
							if (strUniqueKeyColumns.trim().length()>0){
								strUniqueKeyColumns = strUniqueKeyColumns.trim().substring(0,strUniqueKeyColumns.trim().length()-1);
								strUniqueKey += String.format(CONSTRAINT_UNIQUE, strUniqueKeyPreviousName,strUniqueKeyColumns ) + ",";
							}
							//initialize the new 
							strUniqueKeyPreviousName = strUniqueKeyName;
							strUniqueKeyColumns =resUniqueKeys.getString("COLUMN_NAME") + ",";
						};
						
						System.out.println(
								"   "+resUniqueKeys.getString("TABLE_CAT") 
								+ ", "+resUniqueKeys.getString("TABLE_SCHEM")
								+ ", "+resUniqueKeys.getString("TABLE_NAME")
								+ ", "+resUniqueKeys.getString("COLUMN_NAME")
								+ ", "+resUniqueKeys.getString("INDEX_NAME")
						);
						
					}
				} 
				if (hasUKConstraints) {
					// the last column processed
					strUniqueKeyColumns = strUniqueKeyColumns.trim().substring(0,strUniqueKeyColumns.trim().length()-1);
					// the last Unique key processed
					strUniqueKey += String.format(CONSTRAINT_UNIQUE, strUniqueKeyPreviousName,strUniqueKeyColumns );	
					strUniqueKey = strUniqueKey.trim();
				}				
				resUniqueKeys.close();
				
				//System.out.println("/**********foreign keys******************/");
				ResultSet resForeignKeys = _metaData.getImportedKeys(_connection.getCatalog(), _dbSettings.getDefaultSchema(), res.getString("TABLE_NAME"));
				String  strForeignKey = "";
				String  strForeignKeyName = "";
				String  strForeignKeyColumns= "";
				String  strReferencedColumns= "";
				String  strReferencedTableName= "";
				String  strForeignKeyPreviousName = "";
				boolean hasFKConstraints = false;
				while (resForeignKeys.next()) {
					//there are multiple foreign keys, they are identified by the FK_NAME,
					
					if (resForeignKeys.getString("FKCOLUMN_NAME")!=null) {
						hasFKConstraints = true;
						//there are multiple unique keys, they are identified by the index_name,
						strForeignKeyName = resForeignKeys.getString("FK_NAME");
						if (strForeignKeyName.equals(strForeignKeyPreviousName)) {
							strForeignKeyColumns +=resForeignKeys.getString("FKCOLUMN_NAME") + ",";
							strReferencedColumns +=resForeignKeys.getString("PKCOLUMN_NAME") + ",";
							strReferencedTableName = resForeignKeys.getString("PKTABLE_NAME");
						}else{
							//built the previous constraint clause
							if (strForeignKeyColumns.trim().length()>0){
								strForeignKeyColumns = strForeignKeyColumns.trim().substring(0,strForeignKeyColumns.trim().length()-1);
								strReferencedColumns = strReferencedColumns.trim().substring(0,strReferencedColumns.trim().length()-1);
								strForeignKey += String.format(CONSTRAINT_FOREIGN, strForeignKeyPreviousName,strForeignKeyColumns,strReferencedTableName, strReferencedColumns) + ",";
							}
							//initialize the new 
							strForeignKeyPreviousName = strForeignKeyName;
							strForeignKeyColumns =resForeignKeys.getString("FKCOLUMN_NAME") + ",";
							strReferencedColumns =resForeignKeys.getString("PKCOLUMN_NAME") + ",";
							strReferencedTableName = resForeignKeys.getString("PKTABLE_NAME");
						};
					}
				
					/*
					System.out.println(
							"   "+resForeignKeys.getString("PKTABLE_CAT") 
							+ ", "+resForeignKeys.getString("PKTABLE_SCHEM")
							+ ", "+resForeignKeys.getString("PKTABLE_NAME")
							+ ", "+resForeignKeys.getString("PKCOLUMN_NAME")
							+ ", "+resForeignKeys.getString("FKTABLE_CAT") 
							+ ", "+resForeignKeys.getString("FKTABLE_SCHEM")
							+ ", "+resForeignKeys.getString("FKTABLE_NAME")
							+ ", "+resForeignKeys.getString("FKCOLUMN_NAME")
							+ ", "+resForeignKeys.getString("FK_NAME")
					);*/
				} 
				if (hasFKConstraints) {
					// the last column processed
					strForeignKeyColumns = strForeignKeyColumns.trim().substring(0,strForeignKeyColumns.trim().length()-1);
					strReferencedColumns = strReferencedColumns.trim().substring(0,strReferencedColumns.trim().length()-1);
					// the last Unique key processed
					strForeignKey += String.format(CONSTRAINT_FOREIGN, strForeignKeyPreviousName,strForeignKeyColumns,strReferencedTableName, strReferencedColumns) ;	
					strForeignKey = strForeignKey.trim();
				}
				System.out.println(strForeignKey);
				resForeignKeys.close();
				
				
				if (hasPKConstraints)
					strColumns += " , " + strPrimaryKey;
				if (hasUKConstraints)
					strColumns += " , " + strUniqueKey;
				if (includeDependencies && hasFKConstraints)
					strColumns += " , " + strForeignKey;
				
				statement = String.format(CREATE_TABLE, strTable,strColumns );
				

			}
			res.close();
			
		}catch( Exception e ) {
			throw new HecataeusException(e.getMessage());       
		}
		return statement;
	}
	
	/***
	 * Gets a table name and returns a list with all the referenced via fk tables
	 * @param tableName
	 * @return
	 */
	public ArrayList<String> getTableDependencies(String tableName) throws HecataeusException{
		
		ArrayList<String> dependencies= new ArrayList<String>();
		
		try {		
			ResultSet resForeignKeys = _metaData.getImportedKeys(_connection.getCatalog(), _dbSettings.getDefaultSchema(), tableName);
			while (resForeignKeys.next()) {
				if (resForeignKeys.getString("FKCOLUMN_NAME")!=null) {
					dependencies.add(resForeignKeys.getString("PKTABLE_NAME"));
				}
			} 
		}catch( Exception e ) {
			throw new HecataeusException(e.getMessage());       
		}
				
		return dependencies;
	}
	
	/***
	 * Gets a view name and returns the definition of the view
	 * @param tableName
	 * @return
	 */
	public String getViewDefinition(String viewName) throws HecataeusException{
	
		String statement = "";
		try {
			Statement stm = this._connection.createStatement();
			ResultSet res = stm.executeQuery(String.format(this._dbSettings.getQueryForViews(),viewName));

			while (res.next()) {
				statement = res.getString("VIEW_DEFINITION");
			}

			res.close();
			
		}catch( Exception e ) {
			throw new HecataeusException(e.getMessage());       
		}
		// Additional formatting is required for 
		return this._dbSettings.formatViewDefinition(statement, viewName);
	}
	
	/***
	 * Gets a view name and returns a list with all the referenced tables
	 * @param viewName
	 * @return
	 */
	public ArrayList<String> getViewDependencies(String viewName) throws HecataeusException{
		ArrayList<String> dependencies= new ArrayList<String>();
		try {		
			Statement stm = this._connection.createStatement();
			ResultSet res = stm.executeQuery(String.format(this._dbSettings.getQueryForViewDependencies(),viewName));
			while (res.next()) {
				dependencies.add(res.getString("TABLE_NAME"));
			}
			res.close();
		}catch( Exception e ) {
			throw new HecataeusException(e.getMessage());       
		}
		return dependencies;
	}
	
	public void finalize() {
		try {
			if (!this._connection.isClosed()){
				this._connection.close();
				this._connection=null;
			}
		}catch( Exception e ) {
			e.printStackTrace();        
		}
	}
	
	
}
