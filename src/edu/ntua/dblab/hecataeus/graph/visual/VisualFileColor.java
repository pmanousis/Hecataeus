package edu.ntua.dblab.hecataeus.graph.visual;

import java.util.ArrayList;
import java.util.List;

public class VisualFileColor {
	
	
	protected static List<String> files;
	
	
	public VisualFileColor(){
	
	}
	
	protected void setFileNames(List<String> files){
		this.files = new ArrayList<String>(files);
	}
	
	protected List<String> getFileNames(){
		return this.files;
	}
	
}
