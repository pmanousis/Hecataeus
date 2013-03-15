/**
 * @author Stefanos Geraggelos
 * Created on: Jun 29, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Result;
import edu.ntua.dblab.hecataeus.hsql.Table;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class Relation extends Statement{
	String name;
	private Table tTable;
	
	Relation(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		line=HecataeusSQLParser.currentLine;
	}
	
	void setTable(Table tab){
		tTable=tab;
	}
	
	Table getTable(){
		return tTable;
	}
	
	
	/**
	 * parses a create relation statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	void parse(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer = new Tokenizer(sentence) ;
		//get word "CREATE"
		String tableName=tokenizer.getString();
		//get word "TABLE"
		tableName=tokenizer.getString();
		//get TABLE NAME
		tableName=tokenizer.getString();
		
		name=tableName;
		
		Result result=d.execute(sentence, channel);
		if (result.iMode==Result.ERROR){
			System.out.println("");
			String message = "[ExecuteDLL]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") failed:\n"+sentence.toString()+"\nError description:\n"+result.sError;
			System.out.println(message);
			throw new SQLException(message);
		}
		else {
			//get table of current schema
			Table tTable=d.getTable(tableName, channel);
			setDefinition(sentence);
			setTable(tTable);
			
			System.out.println("[ExecuteDLL]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
			System.out.println(sentence.toString());
		}   
	}
}
