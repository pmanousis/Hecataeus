/*
 Copyright (c) 1995-2000 by the Hypersonic SQL Group. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the Hypersonic SQL Group nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE HYPERSONIC SQL GROUP, OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

This software consists of voluntary contributions made by many individuals on behalf of the Hypersonic SQL Group. 

 */
/*
 * Function.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class Function {
	private Channel cChannel;
	private String sFunction;
	private Method mMethod;
	private int iReturnType;
	private int iArgCount;
	private int iArgType[];
	private boolean bArgNullable[];
	private Object oArg[];

	//gpapas modification -->convert to public
	public Expression eArg[];

	private boolean bConnection;
	Function(String function,Channel channel) throws SQLException {
		cChannel=channel;
		sFunction=function;
		int i=function.lastIndexOf('.');
		Trace.check(i!=-1,Trace.UNEXPECTED_TOKEN,function);
		String classname=function.substring(0,i);
		channel.check("CLASS \""+classname+"\"",Access.ALL);
		String methodname=function.substring(i+1);
		Class classinstance=null;
		try {
			classinstance=Class.forName(classname);
		} catch(Exception e) {
			throw Trace.error(Trace.ERROR_IN_FUNCTION,classname+" "+e);
		}
		Method method[]=classinstance.getMethods();
		for(i=0;i<method.length;i++) {
			Method m=method[i];
			if(m.getName().equals(methodname)) {
				Trace.check(mMethod==null,Trace.UNKNOWN_FUNCTION,methodname);
				mMethod=m;
			}
		}
		Trace.check(mMethod!=null,Trace.UNKNOWN_FUNCTION,methodname);
		Class returnclass=mMethod.getReturnType();
		iReturnType=Column.getTypeNr(returnclass.getName());
		Class arg[]=mMethod.getParameterTypes();
		iArgCount=arg.length;
		iArgType=new int[iArgCount];
		bArgNullable=new boolean[iArgCount];
		for(i=0;i<arg.length;i++) {
			Class a=arg[i];
			String type=a.getName();
			if(i==0 && type.equals("java.sql.Connection")) {
				// only the first parameter can be a Connection
				bConnection=true;
			} else {
				if(type.equals("[B")) {
					type="byte[]";
				}
				iArgType[i]=Column.getTypeNr(type);
				bArgNullable[i]=!a.isPrimitive();
			}
		}
		eArg=new Expression[iArgCount];
		oArg=new Object[iArgCount];
	}
	Object getValue() throws SQLException {
		int i=0;
		/*if(bConnection) {
      oArg[i]=new jdbcConnection(cChannel);
      i++;
    }*/
		for(;i<iArgCount;i++) {
			Expression e=eArg[i];
			Object o=null;
			if(e!=null) {
				// no argument: null
				o=e.getValue(iArgType[i]);
			}
			if(o==null && !bArgNullable[i]) {
				// null argument for primitive datatype: don't call & return null
				return null;
			}
			oArg[i]=o;
		}
		try {
			return mMethod.invoke(null,oArg);
		} catch(Exception e) {
			String s=sFunction+": "+e.toString();
			throw Trace.getError(Trace.FUNCTION_NOT_SUPPORTED,s);
		}
	}
	int getArgCount() {
		return iArgCount - (bConnection?1:0);
	}
	void resolve(TableFilter f) throws SQLException {
		for(int i=0;i<iArgCount;i++) {
			if(eArg[i]!=null) {
				eArg[i].resolve(f);
			}
		}
	}
	void checkResolved() throws SQLException {
		for(int i=0;i<iArgCount;i++) {
			if(eArg[i]!=null) {
				eArg[i].checkResolved();
			}
		}
	}
	int getArgType(int i) {
		return iArgType[i];
	}
	int getReturnType() {
		return iReturnType;
	}
	void setArgument(int i,Expression e) {
		if(bConnection) {
			i++;
		}
		eArg[i]=e;
	}

	/*
	 * gpapas Modification
	 * Add public Method to return Function's short Name
	 */
	public String getName(){
		String[] s = Library.sNumeric;
		for(int i=0;i<s.length;i+=2) {
			if(s[i+1]==sFunction){
				return s[i];
			}
		}
		s = Library.sString;
		for(int i=0;i<s.length;i+=2) {
			if(s[i+1]==sFunction){
				return s[i];
			}
		}
		s = Library.sTimeDate;
		for(int i=0;i<s.length;i+=2) {
			if(s[i+1]==sFunction){
				return s[i];
			}
		}
		s = Library.sSystem;
		for(int i=0;i<s.length;i+=2) {
			if(s[i+1]==sFunction){
				return s[i];
			}
		}
		return sFunction;

	}


}
