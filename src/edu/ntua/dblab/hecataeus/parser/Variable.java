/**
 * @author Stefanos Geraggelos
 * Created on: Jun 23, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.IOException;
import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Parser;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class Variable extends Statement{
	String name;
	private Select sSelect;
	boolean isType;
	
	Variable(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		line=HecataeusSQLParser.currentLine;
	}
	
	void setName(String na){
		name=na;
	}
	
	void setIsType(boolean type){
		isType=type;
	}
	
	void setSelect(Select sel){
		sSelect=sel;
	}
	
	String getName(){
		return name;
	}
	
	boolean getIsType(){
		return isType;
	}
	
	Select getSelect(){
		return sSelect;
	}
	
	
	/**
	 * parses a variable declaration statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	public void parse(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser;

		if (isType){
			//get name
			String token=tokenizer.getString();
			
			setName(token);
			
			token=tokenizer.getString();
			
			String[] array=token.split("\\.");
			
			String table_name=array[0];
			
			String attribute=array[1];
			
			String finalSelect=attribute+" FROM "+table_name+";";
			
			try{
				this.validateSQL("SELECT "+finalSelect);
			}catch (SQLException e){
				String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
				System.out.println(message);         
				throw new SQLException(message);
			}
			
			/*System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
			System.out.println("SELECT "+finalSelect);*/
			
			Tokenizer selTokenizer=new Tokenizer(finalSelect);
			parser=new Parser(d,selTokenizer,channel);
			
			try {
				setSelect(parser.parseSelect());
			} catch (IOException e) {
				throw(new SQLException(e.getMessage()));
			}
		}
		else {
			//get name
			String token=tokenizer.getString();
			
			setName(token);
			
			String table_name=tokenizer.getString();
			
			String finalSelect=" * FROM "+table_name+";";
			
			try{
				this.validateSQL("SELECT "+finalSelect);
			}catch (SQLException e){
				String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
				System.out.println(message);         
				throw new SQLException(message);
			}
			
			/*System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
			System.out.println("SELECT "+finalSelect);*/
			
			Tokenizer selTokenizer=new Tokenizer(finalSelect);
			parser=new Parser(d,selTokenizer,channel);
			
			try {
				setSelect(parser.parseSelect());
			} catch (IOException e) {
				throw(new SQLException(e.getMessage()));
			}
		}
	}
}
