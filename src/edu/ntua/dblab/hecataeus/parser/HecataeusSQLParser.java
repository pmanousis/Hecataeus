/**
 * @author George Papastefanatos, National Technical University of Athens
 * 
 */
package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;
import java.io.*;


import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.hsql.*;


/**
 * @author  gpapas
 */

public class HecataeusSQLParser{
	
	private static Database  d;
	private User user;
	private Channel channel;
	private HecataeusGraphCreator  graphCreator;

	public HecataeusSQLParser(VisualGraph HGraph) {
		graphCreator = new HecataeusGraphCreator (HGraph);
		this.connectDatabase();
	}
	
	public HecataeusSQLParser() {
		graphCreator = new HecataeusGraphCreator ();
		this.resetDatabase();
		this.connectDatabase();
	}

	private void resetDatabase() {
		user = null;
		channel =null;
		d = null;
	}
	
	private void connectDatabase() {
		//create a new database
		try {
			if (d==null) { 
			d = new Database("HecataeusDB");}
			
		}
		catch (Exception e){
			String message = "[OpenDatabase]\nThe following command failed:\n" + e.getMessage();
			System.out.println(message);
		}
		user = new User("", "", true, user);
		channel = new Channel(d, user, true, false, 0);
	}
	
	
	public VisualGraph getParsedGraph(){
		return this.graphCreator.HGraph;
	}
	
	/**
	 * processes a file with SQL sentences
	 * @param f
	 * @throws IOException
	 * @throws SQLException
	 */
	public void processFile(File f)throws IOException,SQLException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while (reader.ready()) {
				this.processSentence(this.readSentence(reader));
			}
			reader.close();
		}
		catch (IOException e){
			System.out.println("File Not Found {1}:" + e.getMessage().toString());
		}    
	}
	
	/**
	 * processes an SQL sentence
	 * @param f
	 * @throws IOException
	 * @throws SQLException
	 */
	public void processSentence(String sentence)throws SQLException {
		//convert to upper case
		sentence = sentence.toUpperCase(); 
		
		if (!sentence.isEmpty()){
			if (sentence.startsWith("CREATE TABLE")) 
				this.parseRelation(sentence);
			else if (sentence.startsWith("CREATE VIEW"))
				//move to parse view
				this.parseView(sentence);
			else if (sentence.startsWith("SELECT"))
				this.parseQuery(sentence);
			else 
				throw new SQLException("Unsupported stetement: " + sentence);
		}
	}
	
	void parseRelation(String sentence) throws SQLException {

		Tokenizer tokenizer = new Tokenizer(sentence) ;
		//get word "CREATE"
		String tableName =  tokenizer.getString();
		//get word "TABLE"
		tableName =  tokenizer.getString();
		//get TABLE NAME
		tableName =  tokenizer.getString();
		
		Result result = d.execute(sentence, channel);
		if (result.iMode == Result.ERROR) {
			System.out.println("");
			String message = "[ExecuteDLL]\nThe following command failed:\n" + sentence.toString() +  "\nError description:\n" + result.sError;
			System.out.println(message);
			throw new SQLException(message);
		}
		else {
			//get table of current schema
			Table tTable = d.getTable(tableName, channel);
			try {
				graphCreator.add_table(tTable, sentence);
			}catch (SQLException e){
				String message = "[CreateDDLGraph]\nThe following command not supported:\n" + sentence.toString() ;
				System.out.println(message);         
				throw new SQLException(message);
			} 
			
			System.out.println("[ExecuteDLL]The following command executed successfully:");
			System.out.println(sentence.toString());
		}    

	}

	void parseView(String sentence)  throws SQLException {
		
		Tokenizer tokenizer = new Tokenizer(sentence) ;
		//get word "CREATE"
		String viewName =  tokenizer.getString();
		//get word "VIEW"
		viewName =  tokenizer.getString();
		//get viewName
		viewName =  tokenizer.getString();

		//get word AS
		tokenizer.getString();
		// current parser does not support CREATE VIEW
		// rewrite CREATE VIEW to CREATE TABLE and execute in database
		String tableHeader= "CREATE TABLE " + viewName.toString() + " (";
		String viewHeader = "CREATE VIEW " + viewName.toString() + " AS ";
		// isolate SELECT clause
		String selectDefinition = sentence.substring(viewHeader.length());
		
		try{
			this.validateSQL(selectDefinition);
			tokenizer.getString();
			Parser p = new Parser(d, tokenizer, channel);
			Select sSelect = new Select();
			sSelect = p.parseSelect();

			//add graph for view
			graphCreator.add_view(sSelect,viewName, sentence); 
			//Dimiourgia eikonikou pinaka
			this.createViewSchema(sSelect,tableHeader);
		}catch (SQLException e){
			e.printStackTrace();
			String message = "[CreateViewGraph]\nThe following command not supported:\n" + sentence.toString();
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
	private void createViewSchema(Select sSelect, String table_def) throws SQLException {
	
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

	void parseQuery(String sentence) throws SQLException{
		
			Tokenizer tokenizer = new Tokenizer(sentence) ;
			tokenizer.getString();
			Parser p = new Parser(d, tokenizer, channel);
			Select sSelect = new Select();
			
			try{
				this.validateSQL(sentence);
				sSelect = p.parseSelect();
				graphCreator.add_query(sSelect, sentence);
			}catch (SQLException e){
				String message = "[CreateSQLGraph]\nThe following command not supported:\n" + sentence.toString();
				System.out.println(message);         
				throw new SQLException(message);
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