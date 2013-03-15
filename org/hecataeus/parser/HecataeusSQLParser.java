/**
 * @author George Papastefanatos, National Technical University of Athens
 * 
 */
package org.hecataeus.parser;

import java.sql.SQLException;
import java.io.*;

import org.hecataeus.hsql.*;


/**
 * @author  gpapas
 */

public class HecataeusSQLParser {
	public HecataeusSQLParser()
	{
	}

	private String DDL_filename;
	private String SQL_filename;
	private Database  d;
	private User user;
	private Channel channel;

	public HecataeusGraphCreator graphCreator;

	public HecataeusSQLParser(String DDL, String SQL) {
		DDL_filename = DDL;
		SQL_filename = SQL;
		graphCreator = new HecataeusGraphCreator();

		//create a new database
		try {
			d = new Database("d");
		}
		catch (Exception e){
			String message = "[OpenDatabase]\nThe following command failed:\n" + e.getMessage();
			System.out.println(message);
		}
		user = new User("", "", true, user);
		channel = new Channel(d, user, true, false, 0);

	}

	public void processFile()throws IOException,SQLException {
		this.createRelationSchema();
		this.parseRelations();
		this.parseViews();
		this.parseQueries();
	}

	private void createRelationSchema() throws IOException,SQLException {

		try {
			BufferedReader DDL_file = new BufferedReader(new FileReader(DDL_filename));
			String sentence = "";
			while (DDL_file.ready()) {
				sentence=this.readSentence(DDL_file); 
				Tokenizer tokenizer = new Tokenizer(sentence) ;
				String token =  tokenizer.getString();
				if ( token.equals("CREATE") ) {
					token = tokenizer.getString();
					if ( token.equals("TABLE") ) {
						Result result = d.execute(sentence, channel);

						if (result.iMode == Result.ERROR) {
							System.out.println("");
							String message = "[ExecuteDLL]\nThe following command failed:\n" + sentence.toString() +  "\nError description:\n" + result.sError;
							System.out.println(message);
							throw new SQLException(message);
						}
						else {
							System.out.println("[ExecuteDLL]The following command executed successfully:");
							System.out.println(sentence.toString());
						}
					}
				}

			}
			DDL_file.close();
			
		}
		catch (IOException e){
			System.out.println("File Not Found {1}:" + e.getMessage().toString());
		}    
		
	}



	private void parseRelations()  throws IOException {
		try {
			BufferedReader DDL_file = new BufferedReader(new FileReader(DDL_filename));
			String sentence = "";
			while (DDL_file.ready()) {
				sentence = readSentence(DDL_file);
				Tokenizer tokenizer = new Tokenizer(sentence);
				String token = tokenizer.getString();
				if ( token.equals("CREATE") ) {
					token =tokenizer.getString();
					if ( token.equals("TABLE") ) {
						String TableName = tokenizer.getString();
						//get table of current schema
						Table tTable = d.getTable(TableName, channel);

						try {
							graphCreator.add_table(tTable);
						}catch (SQLException e){
							String message = "[CreateDDLGraph]\nThe following command not supported:\n" + sentence.toString();
							System.out.println(message);         
							throw new SQLException(message);
						} 
					}
				}
			}
			DDL_file.close();
			
		}
		catch (Exception e){
			System.out.println("File Not Found {1}:" + e.getMessage().toString());
		}    
		
	}



	private void parseViews()  throws IOException,SQLException {

		try {
			BufferedReader SQL_file = new BufferedReader(new FileReader(SQL_filename));
			String sentence = "";
			while (SQL_file.ready()) {


				String view_def = "CREATE";// Periexei tin proti grammi ths view
				String table_def = "CREATE TABLE ";// Periexei ton orismo tou eikonikou pinaka
				String view_name = "";// Periexei to onoma ths view

				//get next sentence (CREATE VIEW AS SELECT ...etc)
				sentence = readSentence(SQL_file);

				Tokenizer tokenizer = new Tokenizer(sentence);
				//get CREATE
				String token = tokenizer.getString();
				if ( token.equals("CREATE") ) {
					//get VIEW
					token =tokenizer.getString();// token="view"
					view_def = view_def + " " + token;

					//get view-name
					token = tokenizer.getString();// token=<view_name>
					view_name = view_name + token;

					view_def = view_def +" "+ token;//view-def = CREATE VIEW view-name
					table_def = table_def + token + " ("; //table-def = CREATE TABLE view-name (

					token = tokenizer.getString();//token="as"
					view_def = view_def + " " + token;

					sentence = sentence.substring(view_def.length() + 1);//sentence = To Select kommati ths view

					//Optikopoiisi tou select kommatiou tis VIEW
					Tokenizer tokenizer2 = new Tokenizer(sentence);
					tokenizer2.getString();

					validateSQL(sentence);
		
					Parser p = new Parser(d, tokenizer2, channel);
					Select sSelect = new Select();
					sSelect = p.parseSelect();

					try{
						//add graph for view
						graphCreator.add_view(sSelect,view_name); 
					}catch (SQLException e){
						String message = "[CreateViewGraph]\nThe following command not supported:\n" + sentence.toString();
						System.out.println(message);         
						throw new SQLException(message);
					} 
					//Dimiourgia eikonokou pinaka
					this.createViewSchema(sSelect,table_def);

				}
			}

			SQL_file.close();
			
		}
		catch (IOException e){
			System.out.println("File Not Found {1}:" + e.getMessage().toString());
		}    
		
	}


	/**
	 * 
	 * Synartisi pou ftiaxnei ena eikoniko me to onoma tis view kai ta pedia apo to select kommati to opoio
	 * den optikopoiite ston grafo.Xrisimpopoieitai mono gia validation.
	 * 
	 **/
	private void createViewSchema(Select sSelect, String table_def) throws FileNotFoundException, SQLException {
	
		Expression expr;

		for (int i = 0; i < sSelect.eColumn.length - sSelect.iOrderLen - sSelect.iGroupLen; i++ ) {
			if(i>0) 
				table_def = table_def + ", ";
			expr = sSelect.eColumn[i];
			table_def = table_def + expr.getAlias()+ " integer(1)";
		}
		table_def = table_def + " ) ";
		Result result = d.execute(table_def,channel);

		if(result.iMode == Result.ERROR)
		{
			System.out.println("");
			String message = "[CreateTableForView]\nThe following command failed:\n" + table_def + "\nError description:\n" + result.sError;
			System.out.println(message);
			throw new SQLException(message);
		}
		else
		{
			System.out.println("[CreateTableForView]The following command executed successfully:");
			System.out.println(table_def);
		}

	}

	private void parseQueries()  throws IOException,SQLException{
		try {
			BufferedReader SQL_file = new BufferedReader(new FileReader(SQL_filename));
			String sentence = "";

			while (SQL_file.ready()) {

				sentence = readSentence(SQL_file);
				
				Tokenizer tokenizer = new Tokenizer(sentence) ;
				String token = tokenizer.getString() ;
				if ( token.equals("SELECT") ) {
					
					validateSQL(sentence);
					
					Parser p = new Parser(d, tokenizer, channel);
					Select sSelect = new Select();
					sSelect = p.parseSelect();
					try {
						graphCreator.add_query(sSelect);
					}catch (SQLException e){
						String message = "[CreateSQLGraph]\nThe following command not supported:\n" + sentence.toString();
						System.out.println(message);         
						throw new SQLException(message);
					} 
				}
			}

			SQL_file.close();

		
		}catch (IOException e){
			System.out.println("File Not Found {1}:" + e.getMessage().toString());
		}
		
	}


	private void validateSQL(String sentence) throws SQLException {
		Result result = d.execute(sentence, channel);
		if (result.iMode == Result.ERROR){
			System.out.println("");
			String message = "[ValidateSQL]\nThe following command failed:\n" + sentence.toString()+  "\nError description:\n" + result.sError;
			System.out.println(message);         
			throw new SQLException(message);

		}
		else {
			System.out.println("[ValidateSQL]The following command was executed successfully:");
			System.out.println(sentence);
		}

	}

	
	private String readSentence(BufferedReader ffile ) {
		String sentence="";
		boolean commentON = false;
		char c;
		char c_next;
		
		try 
		{
			while (ffile.ready()) 
			{
				c=(char)ffile.read();
				
				if (commentON)
				{
					//Read until for closing comment */
					if(c=='*'){ //potential comment
						c_next=(char)ffile.read();
						if (c_next == '/'){ //definitely closes comment
							commentON = false;
						}
					}
				}else{
					//Check for opening comment /*
					if(c=='/'){ //potential comment
						ffile.mark(1); //hold the position
						c_next=(char)ffile.read();
						if (c_next == '*'){ //definitely opens comment
							commentON = true;							
						}else{
							ffile.reset(); //it was not comment, go back to mark
						}
					}
					if (! commentON)
					{
						//Check for line comment --
						if(c=='-'){ //potential comment
							ffile.mark(1);
							c_next=(char)ffile.read();
							if (c_next == '-'){ //definitely rest line is comment
								ffile.readLine();
							}else{
								ffile.reset();
								sentence += c;
								if (c==';')
									return sentence.trim() ;
							}
						}else{
							sentence += c;
							if (c==';')
								return sentence.trim() ;
						}
					}
				}
			}
		}catch (Exception e)
		{
			System.out.println("[ReadSentence]The process failed: {0}" + e.getMessage().toString());
		}
		return sentence.trim();
		}
}