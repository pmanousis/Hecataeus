package edu.ntua.dblab.hecataeus.parser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Parser;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class MergeInto extends Statement{
	private String intoName;
	private String usingName;
	private Select matchedSelect;
	private Select notMatchedSelect;
//	private Expression onExpression;
	private String onExpression;
	
	public MergeInto(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		line=HecataeusSQLParser.currentLine;
	}

	void setIntoName(String na){
		intoName=na;
	}
	
	void setUsingName(String na){
		usingName=na;
	}
	
	void setMatchedSelect(Select sel){
		matchedSelect=sel;
	}
	
	void setNotMatchedSelect(Select sel){
		notMatchedSelect=sel;
	}
	
	void setOnExpression(String ex){
		onExpression=ex;
	}
	
	String getIntoName(){
		return intoName;
	}
	
	String getUsingName(){
		return usingName;
	}
	
	Select getMatchedSelect(){
		return matchedSelect;
	}
	
	Select getNotMatchedSelect(){
		return notMatchedSelect;
	}
	
	String getOnExpression(){
		return onExpression;
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

		//get MERGE
		String token=tokenizer.getString();
		
		//get INTO
		token=tokenizer.getString();
		
		usingName=tokenizer.getString();

		//get USING
		token=tokenizer.getString();
		
		intoName=tokenizer.getString();
		
		//get ON
		token=tokenizer.getString();
		
		onExpression="";
		
		token=tokenizer.getString();

		while (!token.equals("WHEN")){
			onExpression+=token+" ";
			token=tokenizer.getString();
		}
		
		//get MATCHED
		token=tokenizer.getString();
		
		//get THEN
		token=tokenizer.getString();
			
		String finalSelect="";
		String where="WHERE ";
		
		//get UPDATE
		token=tokenizer.getString();
		
		//get SET
		token=tokenizer.getString();
		
		boolean attr=true;
		token=tokenizer.getString();
		
		while (!token.equals("WHEN") && !token.equals(";")){
			if (!token.equals("=") && !token.equals(",")){
				if (attr){
					finalSelect+=token+", ";
					where+=token+"=";
					attr=false;
				}
				else{
					where+=token+" AND ";
					attr=true;
				}
			}
			token=tokenizer.getString();
		}
		
		finalSelect=finalSelect.substring(0,finalSelect.length()-2);
		where=where.substring(0,where.length()-5);
		
		finalSelect+=" FROM "+usingName+", "+intoName+" "+where+";";
			
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
			setMatchedSelect(parser.parseSelect());
		} catch (IOException e1) {
			throw(new SQLException(e1.getMessage()));
		}
		
		if (!token.equals(";")){
			//get NOT
			token=tokenizer.getString();
			
			//get MATCHED
			token=tokenizer.getString();
			
			//get THEN
			token=tokenizer.getString();
			
			//get INSERT
			token=tokenizer.getString();
			
			finalSelect="";
			where="WHERE ";
			attr=true;
			
			Vector<String> attrs=new Vector<String>();
			Vector<String> values=new Vector<String>();
			
			token=tokenizer.getString();
			
			while (!token.equals(";")){
				if (!token.equals("(") && !token.equals(")") && !token.equals(",")){
					if (token.equals("VALUES")){
						attr=false;
					}
					else if (attr){
						attrs.add(token);
					}
					else{
						values.add(token);
					}
				}
				token=tokenizer.getString();			
			}
			
			for (int i=0;i<attrs.size();i++){
				finalSelect+=attrs.get(i)+", ";
				where+=attrs.get(i)+"="+values.get(i)+" AND ";
			}
			
			finalSelect=finalSelect.substring(0,finalSelect.length()-2);
			where=where.substring(0,where.length()-5);
			
			finalSelect+=" FROM "+usingName+", "+intoName+" "+where+";";
				
			try{
				this.validateSQL("SELECT "+finalSelect);
			}catch (SQLException e){
				String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
				System.out.println(message);         
				throw new SQLException(message);
			}
				
			/*System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
			System.out.println("SELECT "+finalSelect);*/
				
			selTokenizer=new Tokenizer(finalSelect);
			parser=new Parser(d,selTokenizer,channel);
				
			try {
				setNotMatchedSelect(parser.parseSelect());
			} catch (IOException e) {
				throw(new SQLException(e.getMessage()));
			}
		}
	}
}
