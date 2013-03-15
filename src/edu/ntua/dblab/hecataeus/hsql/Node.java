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
 * Node.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.sql.*;
import java.io.*;

class Node {
	int iBalance; // currently, -2 means 'deleted'
	int iLeft;
	int iRight;
	int iParent;
	Node nLeft;
	Node nRight;
	Node nParent;
	private int iId;  // id of index this table
	Node nNext; // node of next index (nNext==null || nNext.iId=iId+1)
	Row rData;
	Node(Row r,DataInput in,int id) throws IOException,SQLException {
		iId=id;
		rData=r;
		iBalance=in.readInt();
		iLeft=in.readInt();
		iRight=in.readInt();
		iParent=in.readInt();
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
	}
	Node(Row r,int id) {
		iId=id;
		rData=r;
	}
	void delete() {
		iBalance=-2;
		nLeft=nRight=nParent=null;
		iLeft=iRight=iParent=0;
	}
	int getKey() {
		return rData.iPos;
	}
	Node getLeft() throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		if(iLeft==0) {
			return nLeft;
		}
		// rData.iLastAccess=Row.iCurrentAccess++;
		return rData.getNode(iLeft,iId);
	}
	void setLeft(Node n) throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		rData.changed();
		if(n==null) {
			iLeft=0;
			nLeft=null;
		} else if(n.rData.iPos!=0) {
			iLeft=n.rData.iPos;
		} else {
			nLeft=n;
		}
	}
	Node getRight() throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		if(iRight==0) {
			return nRight;
		}
		// rData.iLastAccess=Row.iCurrentAccess++;
		return rData.getNode(iRight,iId);
	}
	void setRight(Node n) throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		rData.changed();
		if(n==null) {
			iRight=0;
			nRight=null;
		} else if(n.rData.iPos!=0) {
			iRight=n.rData.iPos;
		} else {
			nRight=n;
		}
	}
	Node getParent() throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		if(iParent==0) {
			return nParent;
		}
		// rData.iLastAccess=Row.iCurrentAccess++;
		return rData.getNode(iParent,iId);
	}
	void setParent(Node n) throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		rData.changed();
		if(n==null) {
			iParent=0;
			nParent=null;
		} else if(n.rData.iPos!=0) {
			iParent=n.rData.iPos;
		} else {
			nParent=n;
		}
	}
	int getBalance() throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		// rData.iLastAccess=Row.iCurrentAccess++;
		return iBalance;
	}
	void setBalance(int b) throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		if(iBalance!=b) {
			rData.changed();
			iBalance=b;
		}
	}
	public Object[] getData() throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		return rData.getData();
	}
	boolean equals(Node n) throws SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		// rData.iLastAccess=Row.iCurrentAccess++;
		if(Trace.ASSERT) {
			if(n!=this) {
				Trace.verify(rData.iPos==0 || n==null || n.rData.iPos!=rData.iPos);
			} else {
				Trace.verify(n.rData.iPos==rData.iPos);
			}
		}
		return n==this;
	}
	void write(DataOutput out) throws IOException,SQLException {
		if(Trace.ASSERT) Trace.verify(iBalance!=-2);
		out.writeInt(iBalance);
		out.writeInt(iLeft);
		out.writeInt(iRight);
		out.writeInt(iParent);
		if(nNext!=null) {
			nNext.write(out);
		}
	}
}

