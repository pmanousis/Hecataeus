/**
 * @author Stefanos Geraggelos
 * Created on: Jun 14, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.parser.EmbeddedStatement.SpecificEmbedded;

public class AnonymousBlock extends Block{
	private typeStartsWith startType;
	
	enum typeStartsWith{
		BEGIN,
		DECLARE
	}
	
	AnonymousBlock(Database db,Channel ch,FileContainer f,boolean into){
		super(db,ch,f,into);
	}

	String getDescription() {
		return "Anonymous Block";
	}
	
	
	void setStartType(typeStartsWith t){
		startType=t;
	}
	
	
	typeStartsWith getStartType(){
		return startType;
	}
	
	
	/**
	 * parses an anonymous block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception IOException
	 * @exception SQLException 
	 * @exception Exception
	 */
	void parse(String sentence,BufferedReader reader) throws IOException,SQLException,Exception{
		String[] linesBlockDeclare=sentence.split("\r\n|\r|\n");
		
		line=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-HecataeusSQLParser.linesLost-linesBlockDeclare.length+1;
		HecataeusSQLParser.linesLost=0;
		HecataeusSQLParser.linesInComment=0;
		
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		sentence=sentence.replaceAll("( |\\r\\t)*;",";");
		sentence=sentence.trim();
		
		boolean firstBeginFound=false;

		while (!sentence.startsWith("END;")){
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.trim();
			
			if (sentence.startsWith("CREATE"))	sentence=HecataeusSQLParser.removeExtraNewLines(sentence,"CREATE");
			else if (sentence.startsWith("EXECUTE"))	sentence=HecataeusSQLParser.removeExtraNewLines(sentence,"EXECUTE");
			
			//if's check if block inside block exist
			if (sentence.startsWith("BEGIN")){
				if (firstBeginFound){
					AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
					anon.setStartType(typeStartsWith.BEGIN);
					anon.parse(sentence,reader);
					this.addBlock(anon);
				}
				else{
					sentence=replaceFirstGivenWordOfSentence(sentence,"BEGIN","");
					sentence=sentence.replaceAll("( |\r\t)+"," ");
					sentence=sentence.replaceAll("( |\\r\\t)*;",";");
					sentence=sentence.trim();
					
					if (sentence.matches("(\\s|\n)*SELECT(\\s|\n)+(.|\n)*INTO(\\s|\n)+(.|\n)*")){
						isSelectInto=true;
					}else {
						isSelectInto=false;
					}
					
					String[] linesAfterClear=sentence.split("\r\n|\r|\n");
					
					HecataeusSQLParser.currentLine=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-(linesAfterClear.length-1+HecataeusSQLParser.linesLost);
					HecataeusSQLParser.linesLost=0;
					processSentence(sentence,this,reader);
				}
				firstBeginFound=true;
			}
			else if (sentence.startsWith("DECLARE") && (startType!=typeStartsWith.DECLARE || (startType==typeStartsWith.DECLARE && firstBeginFound))){
				AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
				anon.setStartType(typeStartsWith.DECLARE);
				anon.parse(sentence,reader);
				this.addBlock(anon);
			}
			else if (sentence.startsWith("TRIGGER") 
					|| sentence.startsWith("CREATE TRIGGER")
					|| sentence.startsWith("CREATE OR REPLACE TRIGGER")){

				Trigger trig=new Trigger(d,channel,file,isSelectInto);
				trig.parse(sentence,reader);
				this.addBlock(trig);
			}
			else if (sentence.startsWith("EXECUTE IMMEDIATE")){
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
				
				if (sentence.startsWith("BEGIN") && !firstBeginFound){
					sentence=replaceFirstGivenWordOfSentence(sentence,"BEGIN","");
					sentence=sentence.replaceAll("( |\r\t)+"," ");
					sentence=sentence.replaceAll("( |\\r\\t)*;",";");
					sentence=sentence.trim();
					
					String[] linesAfterClear=sentence.split("\r\n|\r|\n");
					
					HecataeusSQLParser.currentLine=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-(linesAfterClear.length-1+HecataeusSQLParser.linesLost);
					HecataeusSQLParser.linesLost=0;
					processSentence(sentence,this,reader);
					
					firstBeginFound=true;
				}
				else{
					String[] linesAfterClear=sentence.split("\r\n|\r|\n");
					
					HecataeusSQLParser.currentLine=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-(linesAfterClear.length-1+HecataeusSQLParser.linesLost);
					HecataeusSQLParser.linesLost=0;
					processSentence(sentence,this,reader);
				}
			}
			
			sentence=HecataeusSQLParser.readSentence(reader);
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.replaceAll("( |\\r\\t)*;",";");
			sentence=sentence.trim();
		}
	}
}
