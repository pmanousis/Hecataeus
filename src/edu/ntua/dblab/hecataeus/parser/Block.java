/**
 * @author Stefanos Geraggelos
 * Created on: Jun 03, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import edu.ntua.dblab.hecataeus.hsql.Channel;
import edu.ntua.dblab.hecataeus.hsql.Database;
import edu.ntua.dblab.hecataeus.parser.AnonymousBlock.typeStartsWith;
import edu.ntua.dblab.hecataeus.parser.EmbeddedStatement.SpecificEmbedded;
import edu.ntua.dblab.hecataeus.parser.PlainSQL.SpecificOperator;

public abstract class Block{
	protected int line;
	private Vector<Statement> stmts;
	private Vector<Block> blcks;
	protected static Database  d;
	protected Channel channel;
	protected FileContainer file;
	protected boolean isSelectInto;
	private Assignment assignment;
	protected static int openBegins;
	
	public Block(Database db,Channel ch,FileContainer f,boolean into){
		stmts=new Vector<Statement>();
		blcks=new Vector<Block>();
		d=db;
		channel=ch;
		file=f;
		isSelectInto=into;
	}	
	
	void setIsSelectInto(boolean into){
		isSelectInto=into;
	}
	
	void setAssignment(Assignment as){
		assignment=as;
	}
	
	boolean getIsSelectInto(){
		return isSelectInto;
	}
	
	int getLine(){
		return line;
	}
	
	Vector<Statement> getStatements(){
		return stmts;
	}
	
	Vector<Block> getBlocks(){
		return blcks;
	}
	
	abstract String getDescription();
	
	void addStatement(Statement node){
		stmts.add(node);
	}
	
	void addBlock(Block node){
		blcks.add(node);
	}
	
	abstract void parse(String sentence,BufferedReader reader)throws IOException,SQLException,Exception;

	
	/**
	 * processes an SQL sentence inside a block
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param parent
	 * @param reader
	 * @throws SQLException
	 * @throws IOException
	 * @exception Exception
	 */
	protected void processSentence(String sentence,Block parent,BufferedReader reader) throws SQLException,IOException,Exception{
		//convert to upper case
		sentence=sentence.toUpperCase();
		
		if (!sentence.isEmpty()){
			if (sentence.startsWith("CREATE TABLE")){
				Relation rel=new Relation(sentence,d,channel,file,isSelectInto);
				rel.parse(sentence,parent);
				parent.addStatement(rel);
			}
			else if (sentence.startsWith("CREATE VIEW")
					|| sentence.startsWith("CREATE OR REPLACE VIEW")){
				
				sentence=sentence.replaceAll("\\\\'","'");
				
				View view=new View(sentence,d,channel,file,isSelectInto);
				view.parse(sentence,parent);
				parent.addStatement(view);
			}
			else if (sentence.startsWith("SELECT")){
				sentence=sentence.replaceAll("\\\\'","'");
				
				PlainSQL query=new PlainSQL(sentence,d,channel,file,isSelectInto);
				query.setOperator(SpecificOperator.SELECT);
				query.parse(sentence,parent);
				parent.addStatement(query);
				if (assignment!=null)	parent.addStatement(assignment);
				assignment=null;
			}
			else if (sentence.startsWith("INSERT")){
				sentence=sentence.replaceAll("\\\\'","'");
				
				PlainSQL ins=new PlainSQL(sentence,d,channel,file,isSelectInto);
				ins.setOperator(SpecificOperator.INSERT);
				ins.parse(sentence,parent);
				parent.addStatement(ins);
			}
			else if (sentence.startsWith("DELETE")){
				sentence=sentence.replaceAll("\\\\'","'");
				
				PlainSQL del=new PlainSQL(sentence,d,channel,file,isSelectInto);
				del.setOperator(SpecificOperator.DELETE);
				del.parse(sentence,parent);
				parent.addStatement(del);
			}
			else if (sentence.startsWith("UPDATE")){
				sentence=sentence.replaceAll("\\\\'","'");
				
				PlainSQL upd=new PlainSQL(sentence,d,channel,file,isSelectInto);
				upd.setOperator(SpecificOperator.UPDATE);
				upd.parse(sentence,parent);
				parent.addStatement(upd);
			}
			else if (sentence.startsWith("CURSOR")){
				sentence=sentence.replaceAll("\\\\'","'");
				
				Cursor cur=new Cursor(sentence,d,channel,file,isSelectInto);
				cur.setIsDynamicCursor(false);
				cur.parse(sentence,parent);
				parent.addStatement(cur);
			}
			else if (sentence.matches("(.|n)*%TYPE(.|n)*")){
				Variable var=new Variable(sentence,d,channel,file,isSelectInto);
				var.setIsType(true);
				var.parse(sentence,parent);
				parent.addStatement(var);
			}
			else if (sentence.matches("(.|n)*%ROWTYPE(.|n)*")){
				Variable var=new Variable(sentence,d,channel,file,isSelectInto);
				var.setIsType(false);
				var.parse(sentence,parent);
				parent.addStatement(var);
			}
			else if (sentence.matches("(\\s|\n)*OPEN(\\s|\n)+(.|\n)*FOR(\\s|\n|\\()+(.|\n)*")){
				sentence=sentence.replaceAll("\\\\'","'");
				
				Cursor cur=new Cursor(sentence,d,channel,file,isSelectInto);
				cur.setIsDynamicCursor(true);
				cur.parse(sentence,parent);
				parent.addStatement(cur);
			}
			else if (sentence.startsWith("FETCH")){
				Assignment assig=new Assignment(sentence,d,channel,file,isSelectInto);
				assig.parse(sentence,parent);
				parent.addStatement(assig);
			}
			else if (sentence.startsWith("MERGE")){
				MergeInto merge=new MergeInto(sentence,d,channel,file,isSelectInto);
				merge.parse(sentence,parent);
				parent.addStatement(merge);
			}
			else if (sentence.startsWith("BEGIN")){		
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
//				throw new SQLException("Unsupported statement: " + sentence);
			}
		}
	}
	
	
	/**
	 * clears the keywords from a sentence
	 * @author Stefanos Geraggelos
	 * @param sentence
	 */
	protected String clearSentence(String sentence){
		sentence=sentence.toUpperCase();
		
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		sentence=sentence.trim();
		
		//if it's not a view
		if (!sentence.startsWith("CREATE VIEW") 
			&& !sentence.startsWith("CREATE OR REPLACE VIEW")){
			sentence=sentence.replaceAll("CREATE(\\s|\n)+(.|\n)*ON(\\s|\n)+(.|\n)*(FOR(\\s)+EACH(\\s)+ROW)?(\\s|\n)+WHEN(\\s|\n)+(.|\n)*BEGIN(\\s|\n)+","");
			sentence=sentence.replaceAll("CREATE(\\s|\n)+(.|\n)*AS(\\s|\n)+","");
			sentence=sentence.replaceAll("CREATE(\\s|\n)+(.|\n)*IS(\\s|\n)+","");
			
			sentence=sentence.replaceAll("CREATE(\\s|\n)+OR(\\s)+REPLACE(\\s)+(.|\n)*AS(\\s|\n)+","");
			sentence=sentence.replaceAll("CREATE(\\s|\n)+OR(\\s)+REPLACE(\\s)+(.|\n)*IS(\\s|\n)+","");
		}
		
		sentence=sentence.replaceAll("CREATE(\\s|\n)+OR(\\s)+REPLACE(\\s)+(.|\n)*ON(\\s|\n)+(.|\n)*(FOR(\\s)+EACH(\\s)+ROW)?(\\s|\n)+WHEN(\\s|\n)+(.|\n)*BEGIN(\\s|\n)+","");
		
		//replace first IS (or AS) in order to match something like 'PROCEDURE .... IS ... CURSOR IS' and not to throw out the cursor
		sentence=replaceFirstGivenWordOfSentence(sentence,"IS","_FIRST_IS_");
		sentence=replaceFirstGivenWordOfSentence(sentence,"AS","_FIRST_AS_");
		
		sentence=sentence.replaceAll("PROCEDURE(\\s)+(.|\n)*(\\s)+_FIRST_IS_\\b","");
		sentence=sentence.replaceAll("PROCEDURE(\\s)+(.|\n)*(\\s)+_FIRST_AS_\\b","");
		sentence=sentence.replaceAll("FUNCTION(\\s)+(.|\n)*(\\s)+_FIRST_IS_\\b","");
		sentence=sentence.replaceAll("FUNCTION(\\s)+(.|\n)*(\\s)+_FIRST_AS_\\b","");
		sentence=sentence.replaceAll("TRIGGER(\\s|\n)+(.|\n)*ON(\\s|\n)+(.|\n)*(FOR(\\s)+EACH(\\s)+ROW)?(\\s|\n)+WHEN(\\s|\n)+(.|\n)*BEGIN(\\s|\n)+","");
		sentence=sentence.replaceAll("PACKAGE(\\s)+(.|\n)*(\\s)+_FIRST_IS_\\b","");
		sentence=sentence.replaceAll("PACKAGE(\\s)+(.|\n)*(\\s)+_FIRST_AS_\\b","");
		sentence=sentence.replaceAll("ELSIF(\\s|\n)+(.|\n)*THEN(\\s|\n)+","");
		sentence=sentence.replaceAll("IF(\\s|\n)+(.|\n)*THEN(\\s|\n)+","");
		sentence=sentence.replaceAll("\\bELSE\\b","");
		sentence=sentence.replaceAll("WHILE(\\s|\n)+(.|\n)*LOOP(\\s|\n|;)+","");
		sentence=sentence.replaceAll("FOR(\\s|\n)(.|\n)*(\\s|\n)+IN(\\s|\n)+(.|\n)*LOOP(\\s|\n|;)+","");
		sentence=sentence.replaceAll("FOR(\\s|\n)+UPDATE(.|\n)*;",";");
		sentence=sentence.replaceAll("(\\s|\n)+WHERE(\\s|\n)+CURRENT(\\s|\n)+OF(\\s|\n)+(.|\n)*;",";");
		sentence=sentence.replaceAll("\\bLOOP\\b","");
		sentence=sentence.replaceAll("\\bDECLARE\\b","");
		sentence=sentence.replaceAll("\\bEXCEPTION\\b","");
/**@author pmanousi added (\\s|\n)+ before RETURN */
		sentence=sentence.replaceAll("(\\s|\n)+RETURN(\\s)+(.|\n)*AS(\\s|\n)+","");
		sentence=sentence.replaceAll("(\\s|\n)+RETURN(\\s)+(.|\n)*IS(\\s|\n)+","");
		
		if (sentence.matches("(\\s|\n)*SELECT(\\s|\n)+(.|\n)*INTO(\\s|\n)+(.|\n)*")){
			isSelectInto=true;
		}else {
			isSelectInto=false;
		}
		
		//replace := in order to distinguish from variable call :<variable_name>
		sentence=sentence.replaceAll("( )*:( )*=( )*","<VARIABLE_DECLARATION>");
		
		//if there is a variable call (:<variable_name>)
		if (sentence.indexOf(":")>=0 && !sentence.matches("(.|\n)*DBMS_OUTPUT.PUT_LINE(.|\n)*")) sentence=replaceVarsWithQuotes(sentence);
		
		sentence=sentence.replaceAll("<VARIABLE_DECLARATION>",":=");
		
		//replace integer with varchar, in order the transformation of variable call (e.g. :<variable_name>) to quotes ('variable_name') to work
		sentence=sentence.replaceAll("\\bINTEGER\\b","VARCHAR(25)");
		
		//replace numeric with varchar, in order the transformation of variable call (e.g. :<variable_name>) to quotes ('variable_name') to work
		sentence=sentence.replaceAll("\\bNUMERIC(\\s)*\\([0-9]+((\\s)*,(\\s)*[0-9]+)?\\)","VARCHAR(25)");
		sentence=sentence.replaceAll("\\bNUMERIC\\b","VARCHAR(25)");
		
		sentence=sentence.replaceAll("\\bNUMBER(\\s)*\\([0-9]+((\\s)*,(\\s)*[0-9]+)?\\)","VARCHAR(25)");
		sentence=sentence.replaceAll("\\bNUMBER\\b","VARCHAR(25)");
		
		sentence=sentence.replaceAll("\\bVARCHAR(\\s)*\\([0-9]+((\\s)*,(\\s)*[0-9]+)?\\)","VARCHAR(25)");
		sentence=sentence.replaceAll("\\bVARCHAR2(\\s)*\\([0-9]+((\\s)*,(\\s)*[0-9]+)?\\)","VARCHAR(25)");
		
		sentence=sentence.replaceAll("\\bDATE(\\s)*\\([0-9]+((\\s)*,(\\s)*[0-9]+)?\\)","VARCHAR(25)");
		sentence=sentence.replaceAll("\\bDATE\\b","VARCHAR(25)");
		
		sentence=sentence.replaceAll("\\bDATETIME(\\s)*\\([0-9]+((\\s)*,(\\s)*[0-9]+)?\\)","VARCHAR(25)");
		sentence=sentence.replaceAll("\\bDATETIME\\b","VARCHAR(25)");

		sentence=sentence.replaceAll("_FIRST_IS_","IS");
		sentence=sentence.replaceAll("_FIRST_AS_","AS");
		
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		sentence=sentence.replaceAll("( |\\r\\t)*;",";");
		
		//remove empty lines		
		sentence=sentence.trim();

		
		return sentence;
	}
	
	
	/**
	 * replace the variable call (:) with quotes
	 * @author Stefanos Geraggelos
	 * @param sentence
	 */
	static private String replaceVarsWithQuotes(String sentence){
		sentence=sentence.trim();
		
		//fix lines
		String[] linesBefore=sentence.split("\r\n|\r|\n");
		
		sentence=sentence.replaceAll("(\\s)*,(\\s)*",", ");
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		
		String[] words1=sentence.split("\\s+");
		String newSentence="";
		for (int i=0;i<words1.length;i++){
			String tmp="";
			
			String word1=words1[i];
			String[] words2=word1.split(",");
			
			int counter=0;
			int commas=0;
			while (counter<word1.length()){
				if (word1.charAt(counter)==',')	commas++;
			
				counter++;
			}
			
			for (int j=0;j<words2.length;j++){
				String word2=words2[j];
				int index=word2.indexOf(":");
			
				if (index>=0){
					word2=word2.replace(":","'");
					word2=word2.replaceAll("(\\s)+"," ");
					
					char lastChar=word2.charAt(word2.length()-1);
					char lastChar2=word2.charAt(word2.length()-2);
					
					if (lastChar==';' && lastChar2==')'){
						word2=word2.replaceAll("\\)\\;","')");
						word2=word2.concat(lastChar+"");						
					}
					else if (lastChar==',' || lastChar==')'){
						word2=word2.replace(lastChar,'\'');
						word2=word2.concat(lastChar+"");
					}
					else {
						word2+="'";
					}
				}
				if (commas>0){
					tmp+=word2+", ";
					commas--;
				}
				else	tmp+=word2+" ";
			}
			word1=tmp;
			
			newSentence+=word1+" ";
		}
		
		sentence=newSentence;
		
		sentence=sentence.replaceAll("( |\r\t)+"," ");
		
		String[] linesAfter=sentence.split("\r\n|\r|\n");
		
		HecataeusSQLParser.linesLost=linesBefore.length-linesAfter.length;
		
		return sentence;
	}
	
	
	int retrieveLastCursorAssignment(){
		int id=0;
		
		for (int i=0;i<stmts.size();i++){
			if (stmts.get(i) instanceof Cursor){
				Cursor cur=(Cursor)stmts.get(i);
				
				if (id<cur.getId())		id=cur.getId();
			}
		}	
		
		
		return id;
	}
	
	Variable retrieveVariableByName(String na){
		
		for (int i=0;i<stmts.size();i++){
			Statement st=stmts.get(i);
			
			if (st instanceof Variable){
				Variable var=(Variable)st;
				
				if (var.getName().equals(na))	return var;
			}
		}
			
		return null;		
	}
	

	/**
	 * returns the first index of a substring into a string
	 * @author Stefanos Geraggelos
	 * @param str
	 * @param searchStr
	 * @param startPos
	 */
	static int indexOf(String str,String searchStr,int startPos){
		if (str==null || searchStr==null){
			return -1;
	    }
	    if (searchStr.length()==0 && startPos>=str.length()){
	        return str.length();
	    }
	      
	    return str.indexOf(searchStr,startPos);
	}
	
	
	/**
	 * remove the first time word found
	 * @author Stefanos Geraggelos
	 * @param sentence
	 * @param word
	 * @param replacement
	 */
	static String replaceFirstGivenWordOfSentence(String sentence,String word,String replacement){
		int start=indexOf(sentence,word,0);
		
		if (start<0)	return sentence;
		sentence=sentence.substring(0,start)+replacement+sentence.substring(word.length()+start,sentence.length());
		
		return sentence;
	}
}
