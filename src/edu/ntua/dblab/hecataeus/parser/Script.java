/**
 * @author Stefanos Geraggelos
 * Created on: Jun 14, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.parser.AnonymousBlock.typeStartsWith;
import edu.ntua.dblab.hecataeus.parser.EmbeddedStatement.SpecificEmbedded;

public class Script extends Block{
	
	Script(Database db,Channel ch,FileContainer f,boolean into){
		super(db,ch,f,into);
	}

	String getDescription(){
		return "Script";
	}
	
	
	/**
	 * parses a script
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception IOException
	 * @exception SQLException
	 * @exception Exception
	 */
	void parse(String sentence,BufferedReader reader) throws IOException,SQLException,Exception{
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		sentence=sentence.replaceAll("( |\\r\\t)*;",";");
		sentence=sentence.trim();
		
		while (reader.ready()||!sentence.isEmpty()){
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.trim();
			
			if (sentence.startsWith("CREATE"))	sentence=HecataeusSQLParser.removeExtraNewLines(sentence,"CREATE");
			else if (sentence.startsWith("EXECUTE"))	sentence=HecataeusSQLParser.removeExtraNewLines(sentence,"EXECUTE");
			
			//if's check if block inside block exist
			if (sentence.startsWith("BEGIN")){		
				AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
				anon.setStartType(typeStartsWith.BEGIN);
				anon.parse(sentence,reader);
				this.addBlock(anon);
			}
			else if (sentence.startsWith("DECLARE")){		
				AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
				anon.setStartType(typeStartsWith.DECLARE);
				anon.parse(sentence,reader);
				this.addBlock(anon);
			}
			else if (sentence.startsWith("PROCEDURE") 
					|| sentence.startsWith("CREATE PROCEDURE")
					|| sentence.startsWith("CREATE OR REPLACE PROCEDURE")){
			
				StoredProcedure proc=new StoredProcedure(d,channel,file,isSelectInto);
				proc.parse(sentence,reader);
				this.addBlock(proc);
			}
			else if (sentence.startsWith("FUNCTION") 
					|| sentence.startsWith("CREATE FUNCTION")
					|| sentence.startsWith("CREATE OR REPLACE FUNCTION")){

				StoredFunction func=new StoredFunction(d,channel,file,isSelectInto);
				func.parse(sentence,reader);
				this.addBlock(func);
			}
			else if (sentence.startsWith("TRIGGER") 
					|| sentence.startsWith("CREATE TRIGGER")
					|| sentence.startsWith("CREATE OR REPLACE TRIGGER")){

				Trigger trig=new Trigger(d,channel,file,isSelectInto);
				trig.parse(sentence,reader);
				this.addBlock(trig);
			}
			else if (sentence.startsWith("PACKAGE") 
					|| sentence.startsWith("CREATE PACKAGE")
					|| sentence.startsWith("CREATE OR REPLACE PACKAGE")){

				Package pack=new Package(d,channel,file,isSelectInto);
				pack.parse(sentence,reader);
				this.addBlock(pack);
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
				
				String[] linesAfterClear=sentence.split("\r\n|\r|\n");
				
				HecataeusSQLParser.currentLine=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-(linesAfterClear.length-1+HecataeusSQLParser.linesLost);
				HecataeusSQLParser.linesLost=0;
				processSentence(sentence,this,reader);
			}
			sentence=HecataeusSQLParser.readSentence(reader);
		}
		
		Vector<Statement> vec=getStatements();		
		System.out.println("nodes:"+vec.size());
		
		System.out.println("lines:"+HecataeusSQLParser.totalLines);
	}
}
