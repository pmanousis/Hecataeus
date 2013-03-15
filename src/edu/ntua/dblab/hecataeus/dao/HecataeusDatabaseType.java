package edu.ntua.dblab.hecataeus.dao;


public enum HecataeusDatabaseType {
	MS_SQL_Server,
	Oracle,
	DB2,
	MySQL,
	HSQL,
	Ingres,
	Postgres;
	
	/**
	 * Converts from the enum representation of a type to the corresponding String representation
	 *
	 */
	public String ToString() {
		return name();
	}

	/**
	 * Converts from the String representation of a type to the corresponding enum representation
	 *
	 */
	public static HecataeusDatabaseType toType(String value) {
		return valueOf(value);
	}
}
	
