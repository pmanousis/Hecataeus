package edu.ntua.dblab.hecataeus;

import java.util.*;
import java.io.*;
import java.awt.*;

import javax.swing.JOptionPane;

public class HecataeusProjectConfiguration
{
	protected String policy="";
	protected String projectName="";
	protected ArrayList<String> sqls;
	protected ArrayList<String> policies;
	protected String curPath="";
	protected String curFile="";
	
	public HecataeusProjectConfiguration()
	{
		policy="";
		projectName="";
		sqls=new ArrayList<String>();
		policies=new ArrayList<String>();
		curPath="AppData";
	}
	
	public void clearArrayLists()
	{
		sqls.clear();
		policies.clear();
	}
	
	public void setDefaultPolicies()
	{
		policy = "QUERY.OUT.SELF: on ADD_ATTRIBUTE then BLOCK;\n" +
				 "QUERY.OUT.SELF: on ADD_ATTRIBUTE_PROVIDER then BLOCK;\n" +
				 "QUERY.OUT.SELF: on DELETE_SELF then BLOCK;\n" +
				 "QUERY.OUT.SELF: on RENAME_SELF then BLOCK;\n" +
				 "QUERY.OUT.ATTRIBUTES: on DELETE_SELF then BLOCK;\n" +
				 "QUERY.OUT.ATTRIBUTES: on RENAME_SELF then BLOCK;\n" +
				 "QUERY.OUT.ATTRIBUTES: on DELETE_PROVIDER then BLOCK;\n" +
				 "QUERY.OUT.ATTRIBUTES: on RENAME_PROVIDER then BLOCK;\n" +
				 "QUERY.IN.SELF: on DELETE_PROVIDER then BLOCK;\n" +
				 "QUERY.IN.SELF: on RENAME_PROVIDER then BLOCK;\n" +
				 "QUERY.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then BLOCK;\n" +
				 "QUERY.IN.ATTRIBUTES: on DELETE_PROVIDER then BLOCK;\n" +
				 "QUERY.IN.ATTRIBUTES: on RENAME_PROVIDER then BLOCK;\n" +
				 "QUERY.SMTX.SELF: on ALTER_SEMANTICS then BLOCK;\n" +
				 
				 "VIEW.OUT.SELF: on ADD_ATTRIBUTE then BLOCK;\n" +
				 "VIEW.OUT.SELF: on ADD_ATTRIBUTE_PROVIDER then BLOCK;\n" +
				 "VIEW.OUT.SELF: on DELETE_SELF then BLOCK;\n" +
				 "VIEW.OUT.SELF: on RENAME_SELF then BLOCK;\n" +
				 "VIEW.OUT.ATTRIBUTES: on DELETE_SELF then BLOCK;\n" +
				 "VIEW.OUT.ATTRIBUTES: on RENAME_SELF then BLOCK;\n" +
				 "VIEW.OUT.ATTRIBUTES: on DELETE_PROVIDER then BLOCK;\n" +
				 "VIEW.OUT.ATTRIBUTES: on RENAME_PROVIDER then BLOCK;\n" +
				 "VIEW.IN.SELF: on DELETE_PROVIDER then BLOCK;\n" +
				 "VIEW.IN.SELF: on RENAME_PROVIDER then BLOCK;\n" +
				 "VIEW.IN.SELF: on ADD_ATTRIBUTE_PROVIDER then BLOCK;\n" +
				 "VIEW.IN.ATTRIBUTES: on DELETE_PROVIDER then BLOCK;\n" +
				 "VIEW.IN.ATTRIBUTES: on RENAME_PROVIDER then BLOCK;\n" +
				 "VIEW.SMTX.SELF: on ALTER_SEMANTICS then BLOCK;\n" +
				 
				 "RELATION.OUT.SELF: on ADD_ATTRIBUTE then BLOCK;\n" +
				 "RELATION.OUT.SELF: on DELETE_SELF then BLOCK;\n" +
				 "RELATION.OUT.SELF: on RENAME_SELF then BLOCK;\n" +
				 "RELATION.OUT.ATTRIBUTES: on DELETE_SELF then BLOCK;\n" +
				 "RELATION.OUT.ATTRIBUTES: on RENAME_SELF then BLOCK;\n";
		
		policies.add("POLICIES/DefaultPolicy.plc");
		writeDefaultPolicy();
	}
	
	public String getLastQueries()
	{
		return(sqls.get(sqls.size()-1));
	}
	
	public void clearProject()
	{
		policy="";
		projectName="";
		clearArrayLists();
	}

	public void writeConfig()
	{
		try
		{
			File file=new File(curPath+"/"+projectName+".hec");
			FileWriter fstream = new FileWriter(file.getAbsoluteFile());
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("SQL: ");
			java.util.Iterator<String> iterator=sqls.iterator();
			String tmp="";
			while(iterator.hasNext())
			{
				tmp+=iterator.next()+",";
			}
			if(sqls.size()>0)
			{	/** @author pmanousi Maybe it was only ddls. */
				out.write(tmp.substring(0,tmp.length()-1));
			}
			out.write("\n");
			out.write("POL: ");
			iterator=policies.iterator();
			tmp="";
			while(iterator.hasNext())
			{
				tmp+=iterator.next()+",";
			}
			out.write(tmp.substring(0,tmp.length()-1));
			out.write("\n");
			out.close();
		}
		catch (IOException e1){}
	}
	
	public void readConfig(String dir, String file)
	{
		policy="";
		FileInputStream fstream = null;
		try
		{
			fstream = new FileInputStream(dir+"/"+file);
		}
		catch (FileNotFoundException e){}
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		curPath=dir;
		curFile=file;
		projectName=file.substring(0,file.indexOf(".hec"));
		in = new DataInputStream(fstream);
		br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		//Read File Line By Line
		int counter=0;
		try {
			while ((strLine = br.readLine()) != null)
			{
				counter++;
				StringTokenizer st=new StringTokenizer  ( strLine, ":" ) ;
				strLine=st.nextToken();
				strLine=st.nextToken();
				st=new StringTokenizer(strLine,",");
				while(st.hasMoreTokens())
				{
					String tempString=st.nextToken().trim();
					if(tempString.isEmpty())
					{
						continue;
					}
					switch(counter)
					{
						case 1:	/**SQL*/
							sqls.add(tempString);
							break;
						case 2:	/**POL*/
							policies.add(tempString);
							readPolicy(tempString);
							break;
				  	}
			  	}
			}
		}
		catch (IOException e1){}
		try
		{
			in.close();
		}
		catch (IOException e){}
	}

	public void copySQLFile(File fn)
	{
		sqls.add("SQLS/"+copyFile(fn, "SQLS"));
	}
	
	private String copyFile(File fn, String Category)
	{
		File inputFile = new File(fn.getAbsolutePath());
	    File outputFile = new File(curPath+"/"+Category+"/"+fn.getName());
	    while(outputFile.exists())
	    {
	    	int selection=JOptionPane.showConfirmDialog(null, "A file with this name: "+fn.getName()+", already exists in project\nOverwrite it or rename it?");
	    	if(selection==1)
	    	{
	    		outputFile=new File(curPath+"/"+Category+"/"+JOptionPane.showInputDialog("Give new name for file."));
	    	}
	    	else if(selection==0)
	    	{
	    		break;
	    	}
	    	else if(selection==2)
	    	{
	    		return("");
	    	}
	    }
	    FileReader in = null;
		try
		{
			in = new FileReader(inputFile);
		}
		catch (FileNotFoundException e) {}
	    FileWriter out = null;
		try
		{
			out = new FileWriter(outputFile);
		}
		catch (IOException e){}
	    int c;
	    try
	    {
			while ((c = in.read()) != -1)
			{
				out.write(c);
			}
		}
	    catch (IOException e){}
	    try
	    {
			in.close();
		}
	    catch (IOException e){}
	    try
	    {
			out.close();
		}
	    catch (IOException e){}
	    return(outputFile.getName());
	}
	
	public void readPolicy(String filename)
	{
		FileInputStream fstream = null;
		this.policy="";
		try
		{
			fstream = new FileInputStream(curPath+filename);
		}
		catch (FileNotFoundException e) {}
		DataInputStream in= new DataInputStream(fstream);
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String strLine;
		try
		{
			while((strLine=br.readLine())!=null)
			{
				this.policy+=strLine+"\n";
			}
		}
		catch (IOException e) {}
		try
		{
			in.close();
		}
		catch (IOException e) {}
	}
	
	private void writeDefaultPolicy()
	{
		File file=new File(curPath+"/POLICIES/DefaultPolicy.plc");
		try
		{
			file.createNewFile();
		}
		catch (IOException e2) {}
		FileWriter fstream = null;
		try 
		{
			fstream = new FileWriter(file.getAbsoluteFile());
		}
		catch (IOException e1) {}
		BufferedWriter out = new BufferedWriter(fstream);
		try
		{
			out.write(policy);
		}
		catch (IOException e){}
		try
		{
			out.close();
		}
		catch (IOException e){}
	}
}
