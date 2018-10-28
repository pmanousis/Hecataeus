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
 * Result.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class Result {
	private Record rTail;
	private int iSize;
	//sgerag modification --> make public
	public int iColumnCount;
	public final static int UPDATECOUNT=0;
	public final static int ERROR=1;
	public final static int DATA=2;
	public int iMode;
	public String sError;
	int iUpdateCount;
	Record rRoot;
	String sLabel[];
	String sTable[];
	//sgerag modification --> make public
	public String sName[];
	int iType[];

	public Result() {
		iMode=UPDATECOUNT;
		iUpdateCount=0;
	}
	public Result(String error) {
		iMode=ERROR;
		sError=error;
	}
	public Result(int columns) {
		prepareData(columns);
		iColumnCount=columns;
	}
	int getSize() {
		return iSize;
	}
	void setColumnCount(int columns) {
		iColumnCount=columns;
	}
	int getColumnCount() {
		return iColumnCount;
	}
	void append(Result a) {
		if(rRoot==null) {
			rRoot=a.rRoot;
		} else {
			rTail.next=a.rRoot;
		}
		rTail=a.rTail;
		iSize+=a.iSize;
	}
	void add(Object d[]) {
		Record r=new Record();
		r.data=d;
		if(rRoot==null) {
			rRoot=r;
		} else {
			rTail.next=r;
		}
		rTail=r;
		iSize++;
	}
	Result(byte b[]) throws SQLException {
		ByteArrayInputStream bin=new ByteArrayInputStream(b);
		DataInputStream in=new DataInputStream(bin);
		try {
			iMode=in.readInt();
			if(iMode==ERROR) {
				throw Trace.getError(in.readUTF());
			} else if(iMode==UPDATECOUNT) {
				iUpdateCount=in.readInt();
			} else if(iMode==DATA) {
				int l=in.readInt();
				prepareData(l);
				iColumnCount=l;
				for(int i=0;i<l;i++) {
					iType[i]=in.readInt();
					sLabel[i]=in.readUTF();
					sTable[i]=in.readUTF();
					sName[i]=in.readUTF();
				}
				while(in.available()!=0) {
					add(Column.readData(in,l));
				}
			}
		} catch(IOException e) {
			throw(Trace.error(Trace.TRANSFER_CORRUPTED));
		}
	}
	byte[] getBytes() throws SQLException {
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		DataOutputStream out=new DataOutputStream(bout);
		try {
			out.writeInt(iMode);
			if(iMode==UPDATECOUNT) {
				out.writeInt(iUpdateCount);
			} else if(iMode==ERROR) {
				out.writeUTF(sError);
			} else {
				int l=iColumnCount;
				out.writeInt(l);
				Record n=rRoot;
				for(int i=0;i<l;i++) {
					out.writeInt(iType[i]);
					out.writeUTF(sLabel[i]);
					out.writeUTF(sTable[i]);
					out.writeUTF(sName[i]);
				}
				while(n!=null) {
					Column.writeData(out,l,iType,n.data);
					n=n.next;
				}
			}
			return bout.toByteArray();
		} catch(IOException e) {
			throw Trace.error(Trace.TRANSFER_CORRUPTED);
		}
	}
	private void prepareData(int columns) {
		iMode=DATA;
		sLabel=new String[columns];
		sTable=new String[columns];
		sName=new String[columns];
		iType=new int[columns];
	}
}

