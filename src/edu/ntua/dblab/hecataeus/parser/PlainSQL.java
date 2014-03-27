/**
 * @author Stefanos Geraggelos
 * Created on: May 27, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Parser;
import edu.ntua.dblab.hecataeus.hsql.Result;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;

public class PlainSQL extends Statement{
	private SpecificOperator op;
	private Select sSelect;
	private String name;		//this is used only in SELECT INTO where a pseudo-table 
								//is created in order to connect assignment nodes
	
	enum SpecificOperator{
		SELECT,
		INSERT,
		DELETE,
		UPDATE;
	}
	
	PlainSQL(String def,Database db,Channel ch,FileContainer f,boolean into){
		super(def,db,ch,f,into);
		line=HecataeusSQLParser.currentLine;
	}
	
	void setOperator(SpecificOperator s){
		op=s;
	}
	
	void setSelect(Select sel){
		sSelect=sel;
	}
	
	void setName(String na){
		name=na;
		isSelectInto=true;
	}
	
	SpecificOperator getOperator(){
		return op;
	}
	
	Select getSelect(){
		return sSelect;
	}
	
	boolean isSelectInto(){
		return isSelectInto;
	}
	
	String getName(){
		return name;
	}
	
	
	/**
	 * parses a plain sql statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	public void parse(String sentence,Block parent) throws SQLException{
		if (this.op==SpecificOperator.SELECT)	parseSelect(sentence,parent);
		else if (this.op==SpecificOperator.INSERT)	parseInsert(sentence,parent);
		else if (this.op==SpecificOperator.DELETE)	parseDelete(sentence,parent);
		else if (this.op==SpecificOperator.UPDATE)	parseUpdate(sentence,parent);
	}
	

	/**
	 * isolates variables like <table_name>.<var_name> ---> <var_name>
	 * @author Stefanos Geraggelos
	 * @param var
	 */
	private String isolateVar(String var){
		int dot=var.indexOf('.');
		
		if (dot<0)	return var;
		var=var.substring(0,dot-1)+var.substring(dot+1,var.length());
		
		return var;
	}
	
	/**
	 * parses a select statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	private void parseSelect(String sentence,Block parent) throws SQLException{
		if (isSelectInto){
			HecataeusSQLParser.globalCounter++;
			
			Tokenizer tokenizer=new Tokenizer(sentence);
			Assignment assig=new Assignment(sentence,d,channel,file,isSelectInto);
			Vector<String> selArgs=new Vector<String>();
			
			String token=tokenizer.getString();
			
			while (!token.equals("INTO")){
				
				if (!token.equals("SELECT") && !token.equals(",")){
					if (token.startsWith("'"))	selArgs.add(token+"'");
					else {
						//isolate variables like <table_name>.<var_name> ---> <var_name> 
						selArgs.add(isolateVar(token));
					}
				}
				token=tokenizer.getString();
			}
			
			token=tokenizer.getString();
			
			Vector<String> vars=new Vector<String>();
			
			while (!token.equals("FROM")){
				if (!token.equals(",")) {
					if (token.startsWith("'"))	vars.add(token+"'");
					else	vars.add(token);
				}
				
				token=tokenizer.getString().trim();
			}
			
			sentence=sentence.replaceAll("INTO(\\s|\n)+(.|\n)*FROM(\\s|\n)+","FROM ");
			
			String varSel="";
			
			//equivalent CREATE TABLE for select
			String eqCreate="CREATE TABLE QUERY_"+HecataeusSQLParser.globalCounter+"(";
			
			for (int i=0;i<vars.size();i++){
				varSel+=selArgs.get(i)+" AS "+vars.get(i)+", ";
				eqCreate+=selArgs.get(i)+" integer(1), ";
			}
			
			//replace the last comma in order the syntax to be right
			varSel+="###";
			varSel=varSel.replaceAll(", ###","");
			
			//replace the last comma in order the syntax to be right
			eqCreate+="###";
			eqCreate=eqCreate.replaceAll(", ###",");");
			
			varSel+=" FROM QUERY_"+HecataeusSQLParser.globalCounter+";";System.out.println("parseSelectINTO:"+varSel);System.out.println("parseSelectINTO:"+eqCreate);
			
			Result result = d.execute(eqCreate,channel);
			if (result.iMode==Result.ERROR) {
				System.out.println("");
				String message="[CreateTableForSelectInto]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+assig.getLine()+") failed:\n"+eqCreate.toString()+ "\nError getDescription:\n"+result.sError;
				System.out.println(message);
				throw new SQLException(message);
			}
			
			System.out.println("[CreateTableForSelectInto]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+assig.getLine()+") executed successfully:");
			System.out.println(eqCreate.toString());
			
			String select=sentence.replaceAll("INTO(\\s|\n)+(.|\n)*FROM(\\s|\n)+","FROM ");
			
			tokenizer=new Tokenizer(select);
			//get SELECT
			tokenizer.getString();
			Parser parser=new Parser(d,tokenizer,channel);
			Select sSelect=parser.parseSelect();
			setSelect(sSelect);
			setName("QUERY_"+HecataeusSQLParser.globalCounter);
			
			try{
				this.validateSQL("SELECT "+varSel);
			}catch (SQLException e){
				String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString();
				System.out.println(message);
				throw new SQLException(message);
			}
			
			System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
			System.out.println("SELECT "+varSel);
			
			tokenizer=new Tokenizer(varSel);System.out.println("edw:"+varSel);
			parser=new Parser(d,tokenizer,channel);
			sSelect=parser.parseSelect();
			assig.setSelect(sSelect);
			
			parent.setAssignment(assig);
		}
		else {
			Tokenizer tokenizer=new Tokenizer(sentence);
			Parser parser=new Parser(d,tokenizer,channel);
			
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
			setSelect(parser.parseSelect());
		}		
	}
	
	
	/**
	 * parses an insert statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	private void parseInsert(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser=new Parser(d,tokenizer,channel);
		Select sSelect;
		String eqSelect="";
		String where="";
		
		//get INSERT
		tokenizer.getString();
		
		//get INTO
		tokenizer.getString();
		
		//get table name
		String table_name=tokenizer.getString();
		
		String token=tokenizer.getString();
		
		Vector<String> attrs=new Vector<String>();
		Vector<String> values=new Vector<String>();
		
		if (token.equals("(")){
			token=tokenizer.getString();
			if (token.equals("SELECT") || token.equals("(")){
				Result res=d.execute("SELECT * FROM "+table_name,channel);
				String names[]=res.sName;
				
				where+=" WHERE (";
				for (int i=0;i<res.iColumnCount;i++){
					eqSelect+=names[i]+", ";
					
					where+=names[i]+", ";
				}
				
				//replace the last comma in order the syntax to be right
				eqSelect+="###";
				eqSelect=eqSelect.replaceAll(", ###","");
				
				//replace the last comma in order the syntax to be right
				where+="###";
				where=where.replaceAll(", ###","");
				
				where+=") = ((";
				while (!token.equals(";")){
					if (token.startsWith("'"))	where+=token+"' ";
					else	where+=token+" ";
					
					token=tokenizer.getString();
				}
				where+=")";
			}
			else {
				while (!token.equals(")")){
					if (token.startsWith("'"))	attrs.add(token+"'");
					else	attrs.add(token);
					
					token=tokenizer.getString();
					if (token.equals(")")){
						break;
					}
					else {
						//was comma
						
						token=tokenizer.getString();
					}
				}
				
				token=tokenizer.getString();
				
				if (token.equals("VALUES")){
					//get (
					tokenizer.getString();
					
					token=tokenizer.getString();

					while (!token.equals(")")){
						if (token.startsWith("'")){
							
							values.add(token+"'");
						}
						else {
							values.add(token);
						}
			
						token=tokenizer.getString();System.out.println("lll_next:"+token);
							
						if (token.equals(")")){
							break;
						}
						else {
							//was comma
								
							token=tokenizer.getString();
						}
					}
					
					where+=where+" WHERE ";
					
					for (int i=0;i<attrs.size();i++){
						eqSelect+=attrs.get(i)+", ";
						
						where+=attrs.get(i)+"="+values.get(i)+" AND ";
					}
					
					//replace the last comma in order the syntax to be right
					eqSelect+="###";
					eqSelect=eqSelect.replaceAll(", ###","");
					
					//replace the last AND in order the syntax to be right
					where+="###";
					where=where.replaceAll("AND ###","");				
				}
				else {					
					where+=" WHERE (";
					for (int i=0;i<attrs.size();i++){
						eqSelect+=attrs.get(i)+", ";
						
						where+=attrs.get(i)+", ";
					}
									
					//replace the last comma in order the syntax to be right
					eqSelect+="###";
					eqSelect=eqSelect.replaceAll(", ###","");
					
					//replace the last AND in order the syntax to be right
					where+="###";
					where=where.replaceAll(", ###","");
					
					where+=") = (";
					
					while (!token.equals(";")){
						if (token.startsWith("'"))	where+=token+"' ";
						else	where+=token+" ";
						
						token=tokenizer.getString();
					}
					where+=")";
				}
			}
		}
		else if (token.equals("SELECT")){
			Result res=d.execute("SELECT * FROM "+table_name,channel);
			String names[]=res.sName;
			
			where+=" WHERE (";
			for (int i=0;i<res.iColumnCount;i++){
				eqSelect+=names[i]+", ";
				
				where+=names[i]+", ";
			}
			
			//replace the last comma in order the syntax to be right
			eqSelect+="###";
			eqSelect=eqSelect.replaceAll(", ###","");
			
			//replace the last comma in order the syntax to be right
			where+="###";
			where=where.replaceAll(", ###","");
			
			where+=") = (";
			while (!token.equals(";")){
				if (token.startsWith("'"))	where+=token+"' ";
				else	where+=token+" ";
				
				token=tokenizer.getString();
			}
			where+=")";		
		}
		else {
			Result res=d.execute("SELECT * FROM "+table_name,channel);
			String names[]=res.sName;
			
			for (int i=0;i<res.iColumnCount;i++){
				attrs.add(names[i]);
			}
			
			//get (
			tokenizer.getString();
			
			token=tokenizer.getString();

			while (!token.equals(")")){
				if (token.startsWith("'")){
					
					values.add(token+"'");
				}
				else {
					values.add(token);
				}
	
				token=tokenizer.getString();System.out.println("lll_next:"+token);
					
				if (token.equals(")")){
					break;
				}
				else {
					//was comma
						
					token=tokenizer.getString();
				}
			}
			
			where+=where+" WHERE ";
			
			for (int i=0;i<attrs.size();i++){
				eqSelect+=attrs.get(i)+", ";
				
				where+=attrs.get(i)+"="+values.get(i)+" AND ";
			}
			
			//replace the last comma in order the syntax to be right
			eqSelect+="###";
			eqSelect=eqSelect.replaceAll(", ###","");
			
			//replace the last AND in order the syntax to be right
			where+="###";
			where=where.replaceAll("AND ###","");
		}

		String finalSelect=eqSelect+" FROM "+table_name+where+";";
		
		try{
			this.validateSQL("SELECT "+finalSelect);
		}catch (SQLException e){
			String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
			System.out.println(message);         
			throw new SQLException(message);
		}
		
		System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println("SELECT "+finalSelect);
		
		//create equivalent select
		Tokenizer selTokenizer=new Tokenizer(finalSelect);
		parser=new Parser(d,selTokenizer,channel);

		sSelect=parser.parseSelect();
		
		setSelect(sSelect);
	}
	
	
	/**
	 * parses a delete statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	private void parseDelete(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser;
		Select sSelect;
		String eqSelect="";
		
		//get DELETE
		tokenizer.getString();
		
		//get FROM
		tokenizer.getString();
		
		//get table name
		String table_name=tokenizer.getString();
				
		//get WHERE and condition
		
		if (tokenizer.getString().equals("WHERE")){
			eqSelect+=" WHERE ";
			String next=tokenizer.getString();
			while (!next.equals(";")){
				if (next.startsWith("'"))	eqSelect+=next+"' ";
				else	eqSelect+=next+" ";
	
				next=tokenizer.getString();
			}
		}
		
		String finalSelect=" NULL FROM "+table_name+eqSelect+";";
		
		try{
			this.validateSQL("SELECT "+finalSelect);
		}catch (SQLException e){
			String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
			System.out.println(message);         
			throw new SQLException(message);
		}
		
		System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println("SELECT "+finalSelect);
		
		//create the equivalent select statement
		Tokenizer selTokenizer=new Tokenizer(finalSelect);
		parser=new Parser(d,selTokenizer,channel);

		sSelect=parser.parseSelect();
		
	
		setSelect(sSelect);		
	}
	
	
	/**
	 * parses an update statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @exception SQLException 
	 */
	private void parseUpdate(String sentence,Block parent) throws SQLException{
		Tokenizer tokenizer=new Tokenizer(sentence);
		Parser parser;
		Select sSelect;
		String eqSelect="";
		String where="";
		
		//get UPDATE
		tokenizer.getString();
		
		//get table name
		String table_name=tokenizer.getString();
		
		//get SET
		tokenizer.getString();
		
		String token=tokenizer.getString();
		
		Vector<String> attrs=new Vector<String>();
		Vector<String> values=new Vector<String>();
		
		String query="";
		
		while (!token.equals("WHERE") && !token.equals(";")){
			if (token.startsWith("'"))	attrs.add(token+"'");
			else	attrs.add(token);
			
			//get =
			tokenizer.getString();
			
			//get value
			token=tokenizer.getString();
			if (token.equals("(")){
				query=token;
				
				token=tokenizer.getString();
				
				while (!token.equals(")")){
					query+=" "+token;
					token=tokenizer.getString();
				}
				
				//add the last )
				query+=" "+token;
				
				values.add(query);
			}
			else	{
				if (token.startsWith("'")){
					
					values.add(token+"'");
				}
				else	values.add(token);
			}
			
			token=tokenizer.getString();
			if (token.equals(","))	token=tokenizer.getString();
		}
		
		boolean hasWhere=false;
		
		if (token.equals("WHERE")){
			hasWhere=true;
			where+=" WHERE ";
			String next=tokenizer.getString();
			while (!next.equals(";")){
				if (next.startsWith("'"))	where+=next+"' ";
				else	where+=next+" ";
	
				next=tokenizer.getString();
			}
		}
		
		//creating the equivalent select query				
		for (int i=0;i<attrs.size();i++){
			if (hasWhere)	where+=" AND "+attrs.get(i)+"="+values.get(i);
			eqSelect+=" "+attrs.get(i)+", ";
		}
		
		//replace the last comma in order the syntax to be right
		eqSelect+="###";
		eqSelect=eqSelect.replaceAll(", ###","");
		
		String finalSelect=eqSelect+" FROM "+table_name+where+";";
		
		try{
			this.validateSQL("SELECT "+finalSelect);
		}catch (SQLException e){
			String message="[CreateSQLGraph]\nThe following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") not supported:\n"+sentence.toString()+"\n"+e.getMessage();
			System.out.println(message);         
			throw new SQLException(message);
		}
		
		System.out.println("[CreateSQLGraph]The following command ("+file.getPath()+", "+parent.getDescription()+", line "+getLine()+") executed successfully:");
		System.out.println("SELECT "+finalSelect);
		
		Tokenizer selTokenizer=new Tokenizer(finalSelect);
		parser=new Parser(d,selTokenizer,channel);

		sSelect=parser.parseSelect();
		
	
		setSelect(sSelect);
	}
}
