/**
 * @author Stefanos Geraggelos
 * Created on: Jun 23, 2010
 */

package edu.ntua.dblab.hecataeus.parser;

import java.util.Vector;

public class FileContainer {
	private String path;
	private String name;
	private Vector<Block> blocks;
	
	FileContainer(String pa){
		path=pa;
		blocks=new Vector<Block>();
	}
	
	void setPath(String pa){
		path=pa;
	}
	
	void setName(String na){
		name=na;
	}
	
	String getPath(){
		return path;
	}
	
	String getName(){
		return name;
	}
	
	Vector<Block> getBlocks(){
		return blocks;
	}
	
	void addBlock(Block bl){
		blocks.add(bl);
	}
}
