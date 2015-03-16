/**
 * @author Stefanos Geraggelos
 * Created on: Jun 29, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Expression;
import edu.ntua.dblab.hecataeus.hsql.Parser;
import edu.ntua.dblab.hecataeus.hsql.Result;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class View extends Statement{
	String name;
	private Select sSelect;
	
	View(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		line=HecataeusSQLParser.currentLine;
	}
	
	void setName(String na){
		name=na;
	}
	
	void setSelect(Select sel){
		sSelect=sel;
	}
	
	String getName(){
		return name;
	}
	
	Select getSelect(){
		return sSelect;
	}
	
	
	/**
	 * parses a create view statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	void parse(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence) ;
		//get word "CREATE"
		String viewName =  tokenizer.getString();
		
		//get word "VIEW"
		while (!viewName.equals("VIEW")){
			viewName=tokenizer.getString();
		}
		//get viewName
		viewName=tokenizer.getString();
		setName(viewName);
		
		//get word AS
		tokenizer.getString();
		// current parser does not support CREATE VIEW
		// rewrite CREATE VIEW to CREATE TABLE and execute in database
		String tableHeader="CREATE TABLE "+viewName.toString()+" (";
		
		// isolate SELECT clause
		String viewHeader="";
		String selectDefinition="";
		if (sentence.startsWith("CREATE VIEW")){
			viewHeader="CREATE VIEW "+viewName.toString()+" AS ";
			selectDefinition=sentence.substring(viewHeader.length());
		}
		else if (sentence.startsWith("CREATE OR REPLACE VIEW")){
			viewHeader="CREATE OR REPLACE VIEW "+viewName.toString()+" AS ";
			selectDefinition=sentence.substring(viewHeader.length());
		}
		
		try{
			this.validateSQL(selectDefinition);
			tokenizer.getString();
			Parser p=new Parser(d,tokenizer,channel);
			Select sSelect=new Select();
			sSelect=p.parseSelect();
			
			setName(viewName);
			setDefinition(sentence);
			setSelect(sSelect);
			
			//Dimiourgia eikonikou pinaka
			this.createViewSchema(sSelect,tableHeader);
		}
		catch (SQLException e){
			e.printStackTrace();
			String message = "[CreateViewGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n" + sentence.toString();
			System.out.println(message);         
			throw new SQLException(message);
		}
	}
	
	
	/**
	 * 
	 * Synartisi pou ftiaxnei ena eikoniko me to onoma tis view kai ta pedia apo to select kommati to opoio
	 * den optikopoiite ston grafo.Xrisimpopoieitai mono gia validation.
	 * 
	 **/
	private void createViewSchema(Select sSelect,String table_def) throws SQLException{
	
		Expression expr;

		for (int i=0;i<sSelect.eColumn.length-sSelect.iOrderLen-sSelect.iGroupLen;i++){
			if(i>0) 
				table_def=table_def+", ";
			expr=sSelect.eColumn[i];
			table_def=table_def+expr.getAlias()+" integer(1)";
		}
		table_def=table_def+" ) ";
		Result result=d.execute(table_def,channel);

		if(result.iMode==Result.ERROR)
		{
			System.out.println("");
			String message = "[CreateTableForView]\nThe following command failed:\n"+table_def+"\nError description:\n"+result.sError;
			System.out.println(message);
			throw new SQLException(message);
		}
		/*else
		{
			System.out.println("[CreateTableForView]The following command executed successfully:");
			System.out.println(table_def);
		}*/

	}
}
