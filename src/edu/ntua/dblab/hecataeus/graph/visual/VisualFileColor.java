package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class VisualFileColor {
	private static List<String> files;
	private static HashMap<String, Color> FileColor;
	private static List<colorDepth> fName;
	
	public VisualFileColor(){
		fName=new ArrayList<colorDepth>();
	}
	
	public void setFileNames(List<String> files){
		VisualFileColor.files = new ArrayList<String>(files);
		VisualFileColor.FileColor = new HashMap<String, Color>(setColorForFiles(VisualFileColor.files));
	}
	
	protected Color getColorForFile(String fileName){
		return VisualFileColor.FileColor.get(fileName);
	}
	
	public HashMap<String, Color> getFileColorMap(){
		return VisualFileColor.FileColor;
	}
	
	private HashMap<String, Color> setColorForFiles(List<String> fileNames){
		List<String> foldersAndFiles=new ArrayList<>();
		String prjFolder= fileNames.get(0);
		HashMap<String, Color> FileColor = new HashMap<String, Color>();
		for(String name : fileNames)
		{
			String fNames = name.substring(name.indexOf("SQLS/")+5);
			String[] fs = fNames.split("/");
			boolean flag=true;
			for(int i=0;i<fs.length;i++)
			{
				if(flag)
				{
					flag=false;
					foldersAndFiles.add(fs[i]);
				}
				else
				{
					foldersAndFiles.add(foldersAndFiles.get(foldersAndFiles.size()-1)+"/"+fs[i]);
				}
			}
		}
		HashSet<String> hs = new HashSet<String>();	// Eliminate duplicates;
		hs.addAll(foldersAndFiles);
		foldersAndFiles.clear();
		foldersAndFiles.addAll(hs);
		Collections.sort(foldersAndFiles);
		int cnt=0;
		for(String pf : foldersAndFiles)	// Colorize
		{	// Use recolorize for files of folders.
			Color c=null;
			if(pf.contains("/"))
			{	// take your parents color
				c=FileColor.get(prjFolder.substring(0, prjFolder.indexOf("SQLS/")+5)+pf.substring(0,pf.lastIndexOf("/")));
			}
			if(c==null)
			{	// your father doesn't have color, so get a color
				FileColor.put(prjFolder.substring(0, prjFolder.indexOf("SQLS/")+5)+pf, getColor(cnt%7));
			}
			else
			{	// since your father had a color, just change it a little bit
				FileColor.put(prjFolder.substring(0, prjFolder.indexOf("SQLS/")+5)+pf, recolorize(c, cnt, prjFolder.substring(0, prjFolder.indexOf("SQLS/")+5)+pf));
			}
			cnt++;
		}
		return FileColor;
	}
	
	private Color recolorize(Color c, int counter, String folderName)
	{
		int color=c.getRGB();
		colorDepth index = null;
		int paceOfRecoloring=1;
		for(Iterator<colorDepth> i=fName.iterator(); i.hasNext();)
		{
			index=i.next();
			if(index.fn.equals(folderName.substring(0, folderName.lastIndexOf("/"))))
			{
				index.pace++;
				paceOfRecoloring=index.pace;
				break;
			}
		}
		if(paceOfRecoloring==1)
		{
			index=new colorDepth(folderName.substring(0, folderName.lastIndexOf("/")),1);
			fName.add(index);
		}
		
		for(int i=0; i<paceOfRecoloring; i++)
		{
			color+=4;
			color+=2*128;
		}
		c=new Color(color%16777215);
		return(c);
	}
	
	private Color getColor(int c){
		switch (c){
		case 0: return new Color(200,181,200);	// Purple
		//case 1: return new Color(102,178,255);	// Light Blue
		case 1: return new Color(10,70,137);	// Dark Blue
		case 2: return new Color(178,255,102);	// Nice Green
		case 3: return new Color(255,178, 102);	// Orange
		//case 3: return new Color(127,80,55);	// Brown
		case 4: return new Color(16, 78, 139);	// Mildy Dark Blue
		case 5: return new Color(127,63,0);	// Dark Orange
		case 6: return new Color(255,102,178);	// Darker Purple?
		default : return new Color(0,0,0);
		}
	}
}

class colorDepth
{
	String fn;
	int pace;
	
	public colorDepth(String f, int p)
	{
		this.fn=f;
		this.pace=p;
	}
}
