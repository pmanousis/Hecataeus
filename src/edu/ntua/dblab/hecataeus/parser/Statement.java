/**
 * @author Stefanos Geraggelos
 * Created on: Jun 05, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Result;

public abstract class Statement{
	protected int line;
	protected String definition;
	protected static Database  d;
	protected Channel channel;
	protected FileContainer file;
	protected boolean isSelectInto;
	
	
	Statement(String def,Database db,Channel ch,FileContainer f,boolean into){
		definition=def;
		line=0;
		d=db;
		channel=ch;
		file=f;
		isSelectInto=into;
	}
	
	void setLine(int li){
		line=li;
	}
	
	void setDefinition(String def){
		definition=def;
	}
	
	int getLine(){
		return line;
	}
	
	String getDefinition(){
		return definition;
	}
	
	abstract void parse(String sentence,Block parent) throws SQLException;
	
	protected void validateSQL(String sentence) throws SQLException {
		Result result=d.execute(sentence,channel);
		if (result.iMode==Result.ERROR){
			System.out.println("");
			String message="[ValidateSQL]\nThe following command failed:\n"+sentence.toString()+"\nError description:\n" + result.sError;
			System.out.println(message);         
			throw new SQLException(message);
		}
		/*else {
			System.out.println("[ValidateSQL]The following command was executed successfully:");
			System.out.println(sentence);
		}*/
	}
}
