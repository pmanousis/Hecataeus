package edu.ntua.dblab.hecataeus.dao;

import java.util.jar.JarException;

/**
 * Class for holding Database settings
 * @author gpapas
 *
 */
public class HecataeusDatabaseSettings {

	private  HecataeusDatabaseType _DBtype ;
	private  String _username;
	private  String _password;
	private  String _hostname;
	private  String _databaseName;
		
	public HecataeusDatabaseSettings(HecataeusDatabaseType DBtype, 
								String hostname, 
								String databaseName, 
								String username, 
								String password) {
		
		this._DBtype = DBtype;
		this._username= username;
		this._password = password;
		this._hostname = hostname;
		this._databaseName = databaseName;
	}
	
	String getJdbcDriver() throws JarException {
		switch (this._DBtype) {
		case MS_SQL_Server:
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
		case Oracle:
			return "oracle.jdbc.driver.OracleDriver";
		case DB2:
			return "com.ibm.db2.jcc.DB2Driver";
		case MySQL:
			return "com.mysql.jdbc.Driver";
		case HSQL:
			return "org.hsqldb.jdbcDriver";
		case Ingres:
			return "com.ingres.jdbc.IngresDriver";
		case Postgres:
			return "org.postgresql.Driver";
		default:
			break;
		}
		
		throw new JarException("The .jar file for " + this._DBtype.ToString() +" Jdbc Driver not found");
	
	}
	
	String getConnectionString() {
		switch (this._DBtype) {
		case MS_SQL_Server:
			return String.format("jdbc:sqlserver://%1$s;databaseName=%2$s", this._hostname, this._databaseName); 
		case Oracle:
			return String.format("jdbc:oracle:thin:@%1$s:%2$s", this._hostname, this._databaseName);
		case DB2:
			return String.format("jdbc:db2://%1$s/%2$s", this._hostname, this._databaseName);
		case MySQL:
			return String.format("jdbc:mysql://%1$s/%2$s", this._hostname, this._databaseName);
		case HSQL:
			return String.format("jdbc:hsqldb:hsql:%1$s", this._databaseName);
		case Ingres:
			//return String.format("jdbc:ingres://localhost:II7/demodb" );
			return String.format("jdbc:ingres://%1$s/%2$s", this._hostname, this._databaseName);
		case Postgres:
			return String.format("jdbc:postgresql://%1$s/%2$s", this._hostname, this._databaseName);
		default:
			break;
		}
		return null;
	}

	String getUsername() {
		return this._username;		
	}
	
	String getPassword(){
		return this._password;
	}
	
	String getQueryForViews() {
		switch (this._DBtype) {
		case MS_SQL_Server:
			return "select VIEW_DEFINITION from INFORMATION_SCHEMA.views WHERE TABLE_NAME = '%1$s'"; 
		case Oracle:
			return "select text VIEW_DEFINITION from all_views where view_name = '%1$s'";
		case DB2:
			return null;
		case MySQL:
			return null;
		case HSQL:
			return null;
		case Ingres:
			//return String.format("jdbc:ingres://localhost:II7/demodb" );
			return null;
		case Postgres:
			return null;
		default:
			break;
		}
		return null;
	}

	String getQueryForViewDependencies() {
		switch (this._DBtype) {
		case MS_SQL_Server:
			return "SELECT DISTINCT table_name TABLE_NAME FROM INFORMATION_SCHEMA.VIEW_TABLE_USAGE where VIEW_NAME = '%1$s'"; 
		case Oracle:
			return "SELECT DISTINCT REFERENCED_NAME table_name FROM ALL_DEPENDENCIES WHERE TYPE = 'VIEW' AND NAME= '%1$s'";
		case DB2:
			return null;
		case MySQL:
			return null;
		case HSQL:
			return null;
		case Ingres:
			//return String.format("jdbc:ingres://localhost:II7/demodb" );
			return null;
		case Postgres:
			return null;
		default:
			break;
		}
		return null;
	}
	
	String getDefaultSchema() {
		switch (this._DBtype) {
		case MS_SQL_Server:
			return "dbo"; 
		case Oracle:
			return this._username.toUpperCase();
		case DB2:
			return null;
		case MySQL:
			return null;
		case HSQL:
			return null;
		case Ingres:
			//return String.format("jdbc:ingres://localhost:II7/demodb" );
			return null;
		case Postgres:
			return null;
		default:
			break;
		}
		return null;
	}
	
	String formatViewDefinition(String dbSentence, String viewName) {
		switch (this._DBtype) {
		case MS_SQL_Server:
			return dbSentence.replace("dbo.", ""); 
		case Oracle:
			return "CREATE VIEW " +viewName+ " AS " + dbSentence.replace("WITH READ ONLY", "");
		case DB2:
			return null;
		case MySQL:
			return null;
		case HSQL:
			return null;
		case Ingres:
			//return String.format("jdbc:ingres://localhost:II7/demodb" );
			return null;
		case Postgres:
			return null;
		default:
			break;
		}
		return null; 
		
	}
}
