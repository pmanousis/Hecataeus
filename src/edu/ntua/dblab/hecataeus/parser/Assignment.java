/**
 * @author Stefanos Geraggelos
 * Created on: Jun 22, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;
import java.util.Vector;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Parser;
import edu.ntua.dblab.hecataeus.hsql.Result;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class Assignment extends Statement{
	private Select sSelect;
	
	Assignment(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		line=HecataeusSQLParser.currentLine;
	}
	
	void setSelect(Select sel){
		sSelect=sel;
	}
	
	Select getSelect(){
		return sSelect;
	}
	
	
	/**
	 * parses a fetch cursor statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	public void parse(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser;
		
		//get FETCH
		String token=tokenizer.getString();
		
		//get cursor's name
		String cursor_name=tokenizer.getString();
		
		Result res=d.execute("SELECT * FROM "+cursor_name+"_"+parent.retrieveLastCursorAssignment(),channel);
		String names[]=res.sName;
		
		
		//get INTO
		tokenizer.getString();
		
		token=tokenizer.getString();
		
		Vector<String> vars=new Vector<String>();
		
		while (!token.equals(";")){
			if (!token.equals(",")){
				vars.add(token);
			}
			
			token=tokenizer.getString();
		}
		
		String eqSelect="";
		
		for (int i=0;i<vars.size();i++){
			eqSelect+=names[i]+" AS "+vars.get(i)+", ";
		}
		
		//replace the last comma in order the syntax to be right
		eqSelect+="###";
		eqSelect=eqSelect.replaceAll(", ###","");
		
		eqSelect+=" FROM "+cursor_name+"_"+parent.retrieveLastCursorAssignment()+";";
		
		try{
			this.validateSQL("SELECT "+eqSelect);
		}catch (SQLException e){
			String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
			System.out.println(message);         
			throw new SQLException(message);
		}
		
		System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println("SELECT "+eqSelect);
		
		tokenizer=new Tokenizer(eqSelect);System.out.println("fetch:"+eqSelect);
		parser=new Parser(d,tokenizer,channel);
		setSelect(parser.parseSelect());
	}
}
