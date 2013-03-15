/**
 * @author Stefanos Geraggelos
 * Created on: Jul 07, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Tokenizer;
import edu.ntua.dblab.hecataeus.parser.AnonymousBlock.typeStartsWith;

public class EmbeddedStatement extends Block{
	private Select sSelect;
	private SpecificEmbedded embed;
	private Block parent;
	private String resolvedSentence;
	private int totalFileLines;
	private int lastLine;
	
	
	enum SpecificEmbedded{
		EXECUTE_IMMEDIATE,
		DBMS_SQL_EXECUTE;
	}
	
	EmbeddedStatement(Database db,Channel ch,FileContainer f,boolean into){
		super(db,ch,f,into);
		resolvedSentence="";
		totalFileLines=0;
		lastLine=0;
	}
	
	void setEmbedded(SpecificEmbedded em){
		embed=em;
	}
	
	void setParent(Block par){
		parent=par;
	}
	
	void setSelect(Select sel){
		sSelect=sel;
	}
	
	SpecificEmbedded getEmbedded(){
		return embed;
	}
	
	Block getParent(){
		return parent;
	}
	
	Select getSelect(){
		return sSelect;
	}
	
	String getDescription(){
		return "Embedded Statetment";
	}
	

	/**
	 * resolves a string variable
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception IOException
	 */
	private int countLinesOfFile() throws IOException{
		
		try{
			int num=0;
			BufferedReader reader=new BufferedReader(new FileReader(file.getPath()));
			String sentence=reader.readLine();
			
			while (sentence!=null){
				num++;
				sentence=reader.readLine();
			}
			
			reader.close();
		
			return num;
		}
		catch (IOException e){
			throw e;
		}
	}

	
	/**
	 * removes comments from a string
	 * @author Stefanos Geraggelos
	 * @param sentence 
	 */
	private String removeComments(String sentence){
		int i=0;
		
		while (i>=0){
			sentence=replaceFirstGivenWordOfSentence(sentence,"/*"," _OPEN_COMMENT_ ");
			sentence=replaceFirstGivenWordOfSentence(sentence,"*/"," _CLOSE_COMMENT_ ");
			
			sentence=sentence.replaceAll("_OPEN_COMMENT_(\\s)+(.|\n)*(\\s)+_CLOSE_COMMENT_\\b","");
			
			i=sentence.indexOf("/*");
		}
		
		return sentence;
	}
	
	
	/**
	 * resolves a string variable
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception IOException
	 * @exception Exception
	 */
	private String resolveVariable(String var,ReverseFileReader reverse) throws IOException,Exception{
		try{
			int curLine=totalFileLines+1;
			String sentenceFromVar="";
			
			//go to the line of the parent block
			while (curLine>=lastLine){
				sentenceFromVar=reverse.readLine();
				curLine--;
			}
			
			while (curLine>=parent.line){
				sentenceFromVar=sentenceFromVar.toUpperCase();
				sentenceFromVar=sentenceFromVar.replaceAll("( |\\r\\t)+"," ");
				sentenceFromVar=sentenceFromVar.replaceAll("( |\\r\\t)*;",";");
				sentenceFromVar=sentenceFromVar.replaceAll("( )*:( )*=( )*",":=");
				sentenceFromVar=sentenceFromVar.trim();
				
				if (sentenceFromVar.startsWith(var+":=")){
					sentenceFromVar=reverse.readStatement();
					sentenceFromVar=sentenceFromVar.replaceAll("( |\\r\\t)+"," ");
					sentenceFromVar=sentenceFromVar.replaceAll("( |\\r\\t)*;",";");
					sentenceFromVar=sentenceFromVar.replaceAll("( )*:( )*=( )*",":=");
					sentenceFromVar=sentenceFromVar.trim();
					sentenceFromVar=sentenceFromVar.substring(((var+":=")).length());
					
					lastLine=curLine;
					
					break;
				}
				
				sentenceFromVar=reverse.readLine();
				sentenceFromVar=removeComments(sentenceFromVar);
				sentenceFromVar=clearSentence(sentenceFromVar);
				curLine--;
			}
			
			return sentenceFromVar;			
		}
		catch (IOException e){
			throw e;
		}		
	}

	
	/**
	 * resolves statements of embedded statement
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception IOException
	 * @exception Exception
	 */
	private void resolveStatementInQuotes(String param) throws IOException,Exception{
		param=param.trim();
		
		boolean commentON=false;
		boolean quotesON=false;
		char c;
		char c_next;
		StringBuffer buf=new StringBuffer(param);
		
		for (int i=0;i<buf.length();i++){
			c=(char)buf.charAt(i);
			char c_prev=' ';
			if (i>0) c_prev=(char)buf.charAt(i-1); 
			
			if (commentON)
			{
				//Read until for closing comment */
				if(c=='*'){ //potential comment
					c_next=(char)buf.charAt(i+1);
					if (c_next=='/'){ //definitely closes comment
						commentON=false;
						i++;
					}
				}
			}
			else {
				if(c=='/'){ //potential comment
					c_next=(char)buf.charAt(i+1);
					if (c_next=='*'){ //definitely opens comment
						commentON=true;
						i++;
					}
					else {
						
					}
				}
				if (! commentON){
					//Check for line comment --
					if(c=='-'){ //potential comment
						c_next=(char)buf.charAt(i+1);
						if (c_next=='-'){ //definitely rest line is comment
							i++;
							c_next=(char)buf.charAt(i);
							while (c_next!='\n'){
								i++;
								c_next=(char)buf.charAt(i);								
							}
						}
					}
					else {	
						if (c=='\'' && c_prev=='\\'){
							resolvedSentence+=c;
						}
						else if (c=='\'' && quotesON){
							quotesON=false;
						}
						else if (c=='\'' && !quotesON){
							quotesON=true;
						}
						else if (c==';' && ((i+1)!=buf.length()) && quotesON){
							resolvedSentence+=";";
						}
						else if (c==';' && !quotesON){
								
						}
						//concatenate characters || 
						else if (c=='|' && (char)buf.charAt(i+1)=='|'){
							i++;
						}
						else if (quotesON){
							resolvedSentence+=c;
						}
						else if (c=='\n'){
							resolvedSentence+='\n';
						}
						else if (c!=' '){
							c_next=(char)buf.charAt(i);
							String var="";
								
							while ((c_next!='|' && c_next!=';') || (c_next=='|' && (char)buf.charAt(i+1)!='|')){
								var+=c_next;
									
								i++;
								c_next=(char)buf.charAt(i);
							}
							i++;
								
							var=var.toUpperCase();
							var=var.trim();
								
							try{
								ReverseFileReader reverse=new ReverseFileReader(file.getPath());
								String resolvedVar=resolveVariable(var,reverse);
								resolveStatementInQuotes(resolvedVar);
								reverse.close();
							}
							catch (IOException e){
								throw e;
							}
							catch (Exception e){
								throw e;
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * parses an embedded statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception SQLException
	 * @exception IOException
	 * @exception Exception
	 */
	public void parse(String sentence, BufferedReader reader) throws IOException,SQLException,Exception{
		totalFileLines=countLinesOfFile();
		
		if (this.embed==SpecificEmbedded.EXECUTE_IMMEDIATE)	parseExecuteImmediate(sentence);
		else if (this.embed==SpecificEmbedded.DBMS_SQL_EXECUTE)	parseDBMSSqlExecute(sentence);
	}
	
	
	/**
	 * parses an execute immediate embedded statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception SQLException 
	 * @exception IOException
	 * @exception Exception
	 */
	private void parseExecuteImmediate(String sentence) throws IOException,SQLException,Exception{
		Tokenizer tokenizer=new Tokenizer(sentence);

		//get EXECUTE
		tokenizer.getString();
		
		//get IMMEDIATE
		tokenizer.getString();
		
		String[] linesBlockDeclare=sentence.split("\r\n|\r|\n");
		
		line=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-HecataeusSQLParser.linesLost-linesBlockDeclare.length+1;
		HecataeusSQLParser.linesLost=0;
		HecataeusSQLParser.linesInComment=0;
		lastLine=line;
		
		resolvedSentence="";
		resolveStatementInQuotes(sentence.substring(("EXECUTE IMMEDIATE").length()));

		BufferedReader reader=new BufferedReader(new StringReader(resolvedSentence));
		sentence=HecataeusSQLParser.readSentence(reader);
		HecataeusSQLParser.totalLines=line;
		
		while (!sentence.isEmpty()){
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r|\t)+"," ");
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
			else {
				sentence=clearSentence(sentence);
			
				HecataeusSQLParser.currentLine=line;
				HecataeusSQLParser.linesLost=0;
				processSentence(sentence,this,reader);
			}
			
			sentence=HecataeusSQLParser.readSentence(reader);
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.replaceAll("( |\\r\\t)*;",";");
			sentence=sentence.trim();
		}
	}
	
	
	/**
	 * parses an execute immediate embedded statement inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param reader
	 * @exception SQLException 
	 * @exception IOException
	 * @exception Exception
	 */
	private void parseDBMSSqlExecute(String sentence) throws IOException,SQLException,Exception{
		sentence=sentence.replaceAll("(\\s)*DBMS_SQL.EXECUTE(\\s)*\\(","");
		sentence=sentence.replaceAll("(\\s)*;",";");
		
		int lastParenthesis=sentence.lastIndexOf(')');
		sentence=sentence.substring(0,lastParenthesis)+sentence.substring(lastParenthesis+1);
		
		String[] linesBlockDeclare=sentence.split("\r\n|\r|\n");
		
		line=HecataeusSQLParser.totalLines-HecataeusSQLParser.linesInComment-HecataeusSQLParser.linesLost-linesBlockDeclare.length+1;
		HecataeusSQLParser.linesLost=0;
		HecataeusSQLParser.linesInComment=0;
		lastLine=line;
		
		resolvedSentence="";
		resolveStatementInQuotes(sentence);
		
		BufferedReader bufReader=new BufferedReader(new StringReader(resolvedSentence));
		sentence=HecataeusSQLParser.readSentence(bufReader);
		HecataeusSQLParser.totalLines=line;
		
		while (!sentence.isEmpty()){
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.trim();			

			//if's check if block inside block exist
			if (sentence.startsWith("BEGIN") || sentence.startsWith("DECLARE")){		
				AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
				anon.parse(sentence,bufReader);
				this.addBlock(anon);
			}
			else if (sentence.startsWith("PROCEDURE") 
					|| sentence.startsWith("CREATE PROCEDURE")
					|| sentence.startsWith("CREATE OR REPLACE PROCEDURE")){
			
				StoredProcedure proc=new StoredProcedure(d,channel,file,isSelectInto);
				proc.parse(sentence,bufReader);
				this.addBlock(proc);
			}
			else if (sentence.startsWith("FUNCTION") 
					|| sentence.startsWith("CREATE FUNCTION")
					|| sentence.startsWith("CREATE OR REPLACE FUNCTION")){

				StoredFunction func=new StoredFunction(d,channel,file,isSelectInto);
				func.parse(sentence,bufReader);
				this.addBlock(func);
			}
			else if (sentence.startsWith("TRIGGER") 
					|| sentence.startsWith("CREATE TRIGGER")
					|| sentence.startsWith("CREATE OR REPLACE TRIGGER")){

				Trigger trig=new Trigger(d,channel,file,isSelectInto);
				trig.parse(sentence,bufReader);
				this.addBlock(trig);
			}
			else if (sentence.startsWith("PACKAGE") 
					|| sentence.startsWith("CREATE PACKAGE")
					|| sentence.startsWith("CREATE OR REPLACE PACKAGE")){

				Package pack=new Package(d,channel,file,isSelectInto);
				pack.parse(sentence,bufReader);
				this.addBlock(pack);
			}
			else {
				sentence=clearSentence(sentence);
				
				HecataeusSQLParser.currentLine=line;
				HecataeusSQLParser.linesLost=0;
				processSentence(sentence,this,bufReader);
			}
			
			sentence=HecataeusSQLParser.readSentence(bufReader);
			sentence=sentence.toUpperCase();
			sentence=sentence.replaceAll("( |\r\t)+"," ");
			sentence=sentence.replaceAll("( |\\r\\t)*;",";");
			sentence=sentence.trim();
		}
	}
}
