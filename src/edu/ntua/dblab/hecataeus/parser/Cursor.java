/**
 * @author Stefanos Geraggelos
 * Created on: Jun 11, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;
import java.util.StringTokenizer;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Expression;
import edu.ntua.dblab.hecataeus.hsql.Parser;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class Cursor extends Statement{
	private int cursorId;		//for dynamic cursors (for multiple assignments)
	private String name;
	private boolean isDynamicCursor;
	private Select sSelect;
	
	Cursor(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		cursorId=0;
		isDynamicCursor=false;
		line=HecataeusSQLParser.currentLine;
	}
	
	void setId(int id){
		cursorId=id;
	}
	
	void setName(String na){
		name=na;
	}
	
	void setIsDynamicCursor(boolean dyn){
		isDynamicCursor=dyn;
	}
	
	void setSelect(Select sel){
		sSelect=sel;
	}
	
	int getId(){
		return cursorId;
	}
	
	String getName(){
		return name;
	}
	
	boolean getIsDynamicCursor(){
		return isDynamicCursor;
	}
	
	Select getSelect(){
		return sSelect;
	}

	
	/**
	 * parses a cursor statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	public void parse(String sentence,Block parent) throws SQLException{
		if (isDynamicCursor)	parseDynamicCursor(sentence,parent);
		else	parseStaticCursor(sentence,parent);
	}
	
	
	/**
	 * parses a static cursor statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	private void parseStaticCursor(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser=new Parser(d,tokenizer,channel);
		
		//get CURSOR
		tokenizer.getString();
		
		//get cursor's name
		String cursor_name=tokenizer.getString();
		setName(cursor_name);
		
		//get IS or AS
		tokenizer.getString();
		
		String finalSelect="";
		
		StringTokenizer validateTok=new StringTokenizer(sentence);
		
		//get SELECT
		while (!validateTok.nextToken().equals("SELECT")){}
		
		finalSelect+="SELECT ";
		while (validateTok.hasMoreTokens()){finalSelect+=validateTok.nextToken()+" ";}
		
		try{
			this.validateSQL(finalSelect);
		}catch (SQLException e){
			String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
			System.out.println(message);         
			throw new SQLException(message);
		}
		
		System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println(finalSelect);
		
		//get SELECT
		while (!tokenizer.getString().equals("SELECT")){}
		
		Select sSelect=parser.parseSelect();
		setSelect(sSelect);
		
		//create a relative table in database
		String create="CREATE TABLE "+cursor_name+"(";
		
		Expression[] columns=sSelect.eColumn;
		
		for (int i=0;i<columns.length;i++){
			create+=columns[i].getColumnName()+" INTEGER, ";
		}
		
		//replace the last comma in order the syntax to be right
		create+="###";
		create=create.replaceAll(", ###",");");
		
		d.execute(create,channel);
	}
	
	
	/**
	 * parses a dynamic cursor statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	private void parseDynamicCursor(String sentence,Block parent) throws SQLException{
		HecataeusSQLParser.globalCounter++;
		
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser=new Parser(d,tokenizer,channel);
		
		//get OPEN
		tokenizer.getString();
		
		//get cursor's name
		String cursor_name=tokenizer.getString();
		setName(cursor_name);
		
		//get FOR
		tokenizer.getString();
		
		String finalSelect="";
		
		StringTokenizer validateTok=new StringTokenizer(sentence);
		
		//get SELECT
		while (!validateTok.nextToken().equals("SELECT")){}
		
		finalSelect+="SELECT ";
		while (validateTok.hasMoreTokens()){finalSelect+=validateTok.nextToken()+" ";}
		
		try{
			this.validateSQL(finalSelect);
		}catch (SQLException e){
			String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
			System.out.println(message);         
			throw new SQLException(message);
		}
		
		System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println(finalSelect);
		
		//get SELECT
		while (!tokenizer.getString().equals("SELECT")){}		

		Select sSelect=parser.parseSelect();
		setSelect(sSelect);
		setId(HecataeusSQLParser.globalCounter);
		
		//create a relative table in database
		String create="CREATE TABLE "+cursor_name+"_"+HecataeusSQLParser.globalCounter+"(";
		
		Expression[] columns=sSelect.eColumn;
		
		for (int i=0;i<columns.length;i++){
			create+=columns[i].getColumnName()+" INTEGER, ";
		}
		
		//replace the last comma in order the syntax to be right
		create+="###";
		create=create.replaceAll(", ###",");");
		
		d.execute(create,channel);
		
		System.out.println("[CreateTableForDynamicCursor]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println(create.toString());
	}
}
