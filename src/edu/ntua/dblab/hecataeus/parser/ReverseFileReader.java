/**
 * @author Stefanos Geraggelos
 * Created on: Jun 15, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.io.*;
 
public class ReverseFileReader {	
		private RandomAccessFile randomfile;	
		private long position;
		
		public ReverseFileReader (String filename) throws Exception {		
			// Open up a random access file
			this.randomfile=new RandomAccessFile(filename,"r");
			// Set our seek position to the end of the file
			this.position=this.randomfile.length();
				
			// Seek to the end of the file
			this.randomfile.seek(this.position);
			//Move our pointer to the first valid position at the end of the file.
			String thisLine=this.randomfile.readLine();
			while(thisLine == null ) {
				this.position--;
				this.randomfile.seek(this.position);
				thisLine=this.randomfile.readLine();
				this.randomfile.seek(this.position);
			}
		}	
		
		// Reads one line from the current position towards the beginning
		public String readLine() throws Exception {		
			int thisCode;
			char thisChar;
			String finalLine="";
			
			// If our position is less than zero already, we are at the beginning
			// with nothing to return.
			if ( this.position < 0 ) {
					return null;
			}
			
			for(;;) {
				// we've reached the beginning of the file
				if ( this.position < 0 ) {
					break;
				}
				// Seek to the current position
				this.randomfile.seek(this.position);
				
				// Read the data at this position
				thisCode=this.randomfile.readByte();
				thisChar=(char)thisCode;
				
				// If this is a line break or carrige return, stop looking
				if (thisCode == 13 || thisCode == 10 ) {
					// Move the pointer for the next readline
					this.position--;
					break;
				} else {
					// This is a valid character append to the string
					finalLine=thisChar + finalLine;
				}
				// Move to the next char
				this.position--;
			}
			// return the line
			return finalLine;
		}
		
		// Reads whole statement until ;
		public String readStatement() throws Exception {		
			int code;
			char c=' ';
			char c_next;
			char c_prev;
			boolean commentON=false;
			boolean quotesON=false;
			String sentence="";
			
			position++;
			
			for(;;) {
				// we've reached the end of the file
				if (this.position>randomfile.length()){
					break;
				}
				// Seek to the current position
				this.randomfile.seek(this.position);
				
				// Read the data at this position
				code=this.randomfile.readByte();
				c_prev=c;
				c=(char)code;
				
				if (c=='\'' && !quotesON && c_prev!='\\'){
					quotesON=true;
				}
				else if (c=='\'' && quotesON && c_prev!='\\'){
					quotesON=false;
				}
				
				if (commentON)
				{
					//Read until for closing comment */
					if(c=='*'){ //potential comment
						position++;
						this.randomfile.seek(this.position);
						c_next=(char)this.randomfile.readByte();
						if (c_next=='/'){ //definitely closes comment
							commentON=false;
						}
					}
				}
				else{
					//Check for opening comment /*
					if(c=='/'){ //potential comment
						position++;
						this.randomfile.seek(this.position);
						c_next=(char)this.randomfile.readByte();
						if (c_next=='*'){ //definitely opens comment
							commentON=true;							
						}
						else {
							position--;			//it was not comment, go back
						}
					}
					if (!commentON)
					{
						//Check for line comment --
						if(c=='-'){ //potential comment
							position++;
							this.randomfile.seek(this.position);
							c_next=(char)this.randomfile.readByte();
							if (c_next=='-'){ //definitely rest line is comment
								position++;
								this.randomfile.seek(this.position);
								c_next=(char)this.randomfile.readByte();
								while (c_next!='\n'){
									position++;
									this.randomfile.seek(this.position);
									c_next=(char)this.randomfile.readByte();
								}
							}
							else {
								position--;
								sentence+=c;
							}
						}
						else {
							sentence+=c;
							if (c==';' && !quotesON){
								return sentence.trim() ;
							}
						}
					}
				}
				position++;
			}
			
			
			return sentence;
		}
		
		public void close() throws IOException{
			randomfile.close();
		}
}
