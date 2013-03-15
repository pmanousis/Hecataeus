/**
 * @author George Papastefanatos, National Technical University of Athens
 * 
 */
package edu.ntua.dblab.hecataeus.parser;


import java.sql.SQLException;
import java.io.*;


import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.hsql.*;
import edu.ntua.dblab.hecataeus.parser.AnonymousBlock.typeStartsWith;
import edu.ntua.dblab.hecataeus.parser.EmbeddedStatement.SpecificEmbedded;


/**
 * @author  gpapas
 * @author  sgerag
 */

public class HecataeusSQLParser{
	
	private static Database  d;
	private User user;
	private Channel channel;
	private HecataeusGraphCreator  graphCreator;

	//added by sgerag
	static private FileContainer file;
	
	static int currentLine;
	static int totalLines;
	static int linesInComment;
	static int linesLost;
	String path;
	boolean isSelectInto;
	static int globalCounter;
	
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
	 * @exception Exception
	 */
	public void processFile(File f)throws IOException,SQLException,Exception{				//add Exception by sgerag
		try {
			BufferedReader reader=new BufferedReader(new FileReader(f));
			
			path=f.getAbsolutePath();
			currentLine=1;
			totalLines=1;
			linesInComment=0;
			linesLost=0;
			
			file=new FileContainer(path);
			
			file.setName(f.getName());
			
			while (reader.ready()) {
				String sentence=HecataeusSQLParser.readSentence(reader);
				sentence=sentence.toUpperCase();
				sentence=sentence.trim();
				
				sentence=sentence.replaceAll("( |\r\t)+"," ");
				if (sentence.startsWith("CREATE"))	sentence=removeExtraNewLines(sentence,"CREATE");
				else if (sentence.startsWith("EXECUTE"))	sentence=removeExtraNewLines(sentence,"EXECUTE");
				
				if (sentence.startsWith("BEGIN")){		
					AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
					anon.setStartType(typeStartsWith.BEGIN);
					anon.parse(sentence,reader);
					file.addBlock(anon);
				}
				else if (sentence.startsWith("DECLARE")){		
					AnonymousBlock anon=new AnonymousBlock(d,channel,file,isSelectInto);
					anon.setStartType(typeStartsWith.DECLARE);
					anon.parse(sentence,reader);
					file.addBlock(anon);
				}
				else if (sentence.startsWith("CREATE TABLE")
						|| sentence.startsWith("CREATE VIEW")
						|| sentence.startsWith("CREATE OR REPLACE VIEW")
						|| sentence.startsWith("SELECT")
						|| sentence.startsWith("INSERT")
						|| sentence.startsWith("DELETE")
						|| sentence.startsWith("UPDATE")
						|| sentence.startsWith("MERGE")){

					Script scri=new Script(d,channel,file,isSelectInto);
					scri.parse(sentence,reader);
					file.addBlock(scri);
				}
				else if (sentence.startsWith("PROCEDURE") 
						|| sentence.startsWith("CREATE PROCEDURE")
						|| sentence.startsWith("CREATE OR REPLACE PROCEDURE")){
					
					StoredProcedure proc=new StoredProcedure(d,channel,file,isSelectInto);
					proc.parse(sentence,reader);
					file.addBlock(proc);
				}
				else if (sentence.startsWith("FUNCTION") 
						|| sentence.startsWith("CREATE FUNCTION")
						|| sentence.startsWith("CREATE OR REPLACE FUNCTION")){

					StoredFunction func=new StoredFunction(d,channel,file,isSelectInto);
					func.parse(sentence,reader);
					file.addBlock(func);
				}
				else if (sentence.startsWith("TRIGGER") 
						|| sentence.startsWith("CREATE TRIGGER")
						|| sentence.startsWith("CREATE OR REPLACE TRIGGER")){

					Trigger trig=new Trigger(d,channel,file,isSelectInto);
					trig.parse(sentence,reader);
					file.addBlock(trig);
				}
				else if (sentence.startsWith("PACKAGE") 
						|| sentence.startsWith("CREATE PACKAGE")
						|| sentence.startsWith("CREATE OR REPLACE PACKAGE")){

					Package pack=new Package(d,channel,file,isSelectInto);
					pack.parse(sentence,reader);
					file.addBlock(pack);
				}
				else if (sentence.startsWith("EXECUTE IMMEDIATE")){
					EmbeddedStatement emb=new EmbeddedStatement(d,channel,file,isSelectInto);
					emb.setEmbedded(SpecificEmbedded.EXECUTE_IMMEDIATE);
					emb.setParent(null);
					emb.parse(sentence,reader);
					file.addBlock(emb);
				}
				else if (sentence.startsWith("DBMS_SQL.EXECUTE")){
					EmbeddedStatement emb=new EmbeddedStatement(d,channel,file,isSelectInto);
					emb.setEmbedded(SpecificEmbedded.DBMS_SQL_EXECUTE);
					emb.setParent(null);
					emb.parse(sentence,reader);
					file.addBlock(emb);
				}
				else	{
					
				}
			}
			
			if (currentLine!=totalLines)	totalLines--;
			
			reader.close();
			
			graphCreator.addFile(file);
		}
		catch (IOException e){
			System.out.println("File Not Found {1}:" + e.getMessage().toString());			
		}   
	}
	
	/**
	 * removes extra new lines in a sentence
	 * @param sentence
	 * @param firstWord
	 * @throws IOException
	 * @throws SQLException
	 * @exception Exception
	 */
	static public String removeExtraNewLines(String sentence,String firstWord){
		int index=sentence.indexOf(firstWord);
		
		String[] before=sentence.split("\r\n|\r|\n");
		
		sentence=sentence.substring(index+firstWord.length(),sentence.length());
		sentence=sentence.trim();
		
		String[] after=sentence.split("\r\n|\r|\n");
		
		linesLost+=before.length-after.length;
		
		sentence=firstWord+" "+sentence;
		
		return sentence;
	}
	
	
	static public String readSentence(BufferedReader ffile){
		String sentence="";
		boolean commentON=false;
		boolean quotesON=false;								//added by sgerag
		boolean justClosedComment=false;					//added by sgerag
		char c_next;
		char c_prev;
		boolean sentenceHasLetters=false;
		linesInComment=0;
		
		try
		{
			char c=' ';
			int i=ffile.read();
			
			while (i!=-1 && ffile.ready())					//added by sgerag
//			while (ffile.ready()) 							//comment out by sgerag
			{
				c_prev=c;
				c=(char)i;
				
				if (c=='/' && !sentenceHasLetters){
					ffile.mark(1);
					c_next=(char)ffile.read();
					
					if (c_next!='/' && c_next!='*'){
						sentence+=c;
						return sentence;
					}
					ffile.reset();
				}

				if (c=='\n' && (commentON || justClosedComment) && sentenceHasLetters){
					totalLines++;
					linesInComment++;												//added by sgerag							
				}
				else if (c=='\n' && !commentON){
					totalLines++;												//added by sgerag							
				}
				else if (c=='\n'){
					totalLines++;												//added by sgerag							
				}
				else if (c=='\'' && !quotesON && c_prev!='\\'){									//added by sgerag
					quotesON=true;
					sentenceHasLetters=true;
				}
				else if (c=='\'' && quotesON && c_prev!='\\'){									//added by sgerag
					quotesON=false;
					sentenceHasLetters=true;
				}
				
				if (commentON)
				{
					//Read until for closing comment */
					if(c=='*'){ //potential comment
						ffile.mark(1);
						c_next=(char)ffile.read();
						if (c_next=='/'){ //definitely closes comment
							commentON=false;
							justClosedComment=true;										//added by sgerag
						}
						else {
							ffile.reset();
						}
					}
				}else{
					//Check for opening comment /*
					if(c=='/'){ //potential comment
						ffile.mark(1); //hold the position
						c_next=(char)ffile.read();
						if (c_next=='*'){ //definitely opens comment
							commentON=true;							
						}
						else {
							ffile.reset(); //it was not comment, go back to mark
						}
					}
					if (!commentON)
					{
						//Check for line comment --
						if(c=='-'){ //potential comment
							ffile.mark(1);
							c_next=(char)ffile.read();
							if (c_next=='-'){ //definitely rest line is comment
								ffile.readLine();
								sentence+="\n";										//added by sgerag
								totalLines++;											//added by sgerag
							}
							else {
								ffile.reset();
								sentence+=c;
							}
						}
						else{
							if (c=='\n' && justClosedComment){								//added by sgerag
								justClosedComment=false;
							}
							else {
								sentence+=c;
								
								if (!sentenceHasLetters && c!=' ' && c!='\n' && c!='\t' && c!='\r'){							//added by sgerag
									sentenceHasLetters=true;
								}
								
								if (c==';' && !quotesON){
									return sentence.trim() ;
								}
							}
						}
					}
				}
				
				i=ffile.read();
			}
		}catch (Exception e)
		{
			System.out.println("[ReadSentence]The process failed: {0}"+e.getMessage().toString());
		}
		return sentence.trim();
	}
	
	
}