package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualFileColor {
	
	
	protected static List<String> files;
	protected static HashMap<String, Color> FileColor;
	
	public VisualFileColor(){
	}
	
	public void setFileNames(List<String> files){
		this.files = new ArrayList<String>(files);
		this.FileColor = new HashMap<String, Color>(setColorForFiles(this.files));
	}
	
	protected Color getColorForFile(String fileName){
		return this.FileColor.get(fileName);
	}
	
	public HashMap<String, Color> getFileColorMap(){
		return this.FileColor;
	}
	
	private HashMap<String, Color> setColorForFiles(List<String> fileNames){
		HashMap<String, Color> FileColor = new HashMap<String, Color>();
		int cnt = 0;
		for(String name : fileNames){
			FileColor.put(name, getColor(cnt%6));
			cnt++;
		}
		
		for(Map.Entry<String, Color> entry : FileColor.entrySet()){
			System.out.println("file name" + entry.getKey() +" file color " + entry.getValue() );
		}
		return FileColor;
	}
	
	
	private Color getColor(int c){
		
		switch (c){
		case 0: return new Color(16, 78, 139);
		case 1: return new Color(255,102,102);
		case 2: return new Color(255,178,102);
		case 3: return new Color(178,255,102);
		case 4: return new Color(102,178,255);
		case 5: return new Color(178,102,255);
		case 6: return new Color(255,102,102);
		
		
		default : return new Color(0,0,0);
		}
	}
}
