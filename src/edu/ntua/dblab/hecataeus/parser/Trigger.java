/**
 * @author Stefanos Geraggelos
 * Created on: Juj 02, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;
import edu.ntua.dblab.hecataeus.parser.EmbeddedStatement.SpecificEmbedded;

public class Trigger extends Block{	
	private String name;
	
	Trigger(Database db,Channel ch,FileContainer f,boolean into){
		super(db,ch,f,into);
	}
	
	String getName(){
		return name;
	}

	String getDescription(){
		return "Trigger '"+name+"'";
	}
	
	
	/**
	 * parses a trigger
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception IOException
	 * @exception SQLException
	 * @exception Exception 
	 */
	void parse(String sentence,BufferedReader reader) throws IOException,SQLException,Exception{
		Tokenizer tokenizer=new Tokenizer(sentence);

		//get TRIGGER
		while (!tokenizer.getString().equals("TRIGGER")){	}
		
		name=tokenizer.getString();
		
		String[] linesBlockDeclare=sentence.split("\r\n|\r|\n");
		
		line=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-HecataeusSQLParser.linesLost-linesBlockDeclare.length+1;
		HecataeusSQLParser.linesLost=0;
		HecataeusSQLParser.linesInComment=0;
		
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		sentence=sentence.replaceAll("( |\\r\\t)*;",";");
		sentence=sentence.trim();

		while (!sentence.startsWith("END;") && !sentence.startsWith("END "+name+";")){
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.trim();
			
			if (sentence.startsWith("CREATE"))	sentence=HecataeusSQLParser.removeExtraNewLines(sentence,"CREATE");
			else if (sentence.startsWith("EXECUTE"))	sentence=HecataeusSQLParser.removeExtraNewLines(sentence,"EXECUTE");
			
			if (sentence.startsWith("EXECUTE IMMEDIATE")){
				EmbeddedStatement emb=new EmbeddedStatement(d,channel,file,isSelectInto);
				emb.setEmbedded(SpecificEmbedded.EXECUTE_IMMEDIATE);
				emb.setParent(this);
				emb.parse(sentence,reader);					
				this.addBlock(emb);
			}
			else if (sentence.startsWith("DBMS_SQL.EXECUTE")){
				EmbeddedStatement emb=new EmbeddedStatement(d,channel,file,isSelectInto);
				emb.setEmbedded(SpecificEmbedded.DBMS_SQL_EXECUTE);
				emb.setParent(this);
				emb.parse(sentence,reader);
				this.addBlock(emb);
			}
			else {
				sentence=clearSentence(sentence);
				
				String[] linesAfterClear=sentence.split("\r\n|\r|\n");
				
				HecataeusSQLParser.currentLine=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-(linesAfterClear.length-1);
				processSentence(sentence,this,reader);
			}
			
			sentence=HecataeusSQLParser.readSentence(reader);
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.replaceAll("( |\\r\\t)*;",";");
			sentence=sentence.trim();
		}
	}
}