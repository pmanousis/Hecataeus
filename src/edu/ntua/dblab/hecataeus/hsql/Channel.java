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
 * Channel.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.sql.*;
import java.util.Vector;

public class Channel {
	private Database dDatabase;
	private User uUser;
	private Vector tTransaction;
	private boolean bAutoCommit;
	private boolean bNestedTransaction;
	private boolean bNestedOldAutoCommit;
	private int iNestedOldTransIndex;
	private boolean bReadOnly;
	private int iMaxRows;
	private int iLastIdentity;
	private boolean bClosed;
	private int iId;

	public void finalize() throws SQLException {
		disconnect();
	}
	public Channel(Channel c,int id) {
		this(c.dDatabase,c.uUser,true,c.bReadOnly,id);
	}
	public Channel(Database db,User user,boolean autocommit,boolean readonly,int id) {
		iId=id;
		dDatabase=db;
		uUser=user;
		tTransaction=new Vector();
		bAutoCommit=autocommit;
		bReadOnly=readonly;
	}
	int getId() {
		return iId;
	}
	void disconnect() throws SQLException {
		if(bClosed) {
			return;
		}
		rollback();
		dDatabase=null;
		uUser=null;
		tTransaction=null;
		bClosed=true;
	}
	boolean isClosed() {
		return bClosed;
	}
	void setLastIdentity(int i) {
		iLastIdentity=i;
	}
	int getLastIdentity() {
		return iLastIdentity;
	}
	Database getDatabase() {
		return dDatabase;
	}
	String getUsername() {
		return uUser.getName();
	}
	void setUser(User user) {
		uUser=user;
	}
	void checkAdmin() throws SQLException {
		uUser.checkAdmin();
	}
	void check(String object,int right) throws SQLException {
		uUser.check(object,right);
	}
	void checkReadWrite() throws SQLException {
		Trace.check(!bReadOnly,Trace.DATABASE_IS_READONLY);
	}
	void setPassword(String s) {
		uUser.setPassword(s);
	}
	void addTransactionDelete(Table table,Object row[])
	throws SQLException {
		if(!bAutoCommit) {
			Transaction t=new Transaction(true,table,row);
			tTransaction.addElement(t);
		}
	}
	void addTransactionInsert(Table table,Object row[])
	throws SQLException {
		if(!bAutoCommit) {
			Transaction t=new Transaction(false,table,row);
			tTransaction.addElement(t);
		}
	}
	void setAutoCommit(boolean autocommit) throws SQLException {
		commit();
		bAutoCommit=autocommit;
	}
	void commit() throws SQLException {
		tTransaction.removeAllElements();
	}
	void rollback() throws SQLException {
		int i=tTransaction.size()-1;
		while(i>=0) {
			Transaction t=(Transaction)tTransaction.elementAt(i);
			t.rollback();
			i--;
		}
		tTransaction.removeAllElements();
	}
	void beginNestedTransaction() throws SQLException {
		Trace.verify(!bNestedTransaction,"beginNestedTransaction");
		bNestedOldAutoCommit=bAutoCommit;
		// now all transactions are logged
		bAutoCommit=false;
		iNestedOldTransIndex=tTransaction.size();
		bNestedTransaction=true;
	}
	void endNestedTransaction(boolean rollback) throws SQLException {
		Trace.verify(bNestedTransaction,"endNestedTransaction");
		int i=tTransaction.size()-1;
		if(rollback) {
			while(i>=iNestedOldTransIndex) {
				Transaction t=(Transaction)tTransaction.elementAt(i);
				t.rollback();
				i--;
			}
		}
		bNestedTransaction=false;
		bAutoCommit=bNestedOldAutoCommit;
		if(bAutoCommit==true) {
			tTransaction.setSize(iNestedOldTransIndex);
		}
	}
	void setReadOnly(boolean readonly) {
		bReadOnly=readonly;
	}
	boolean isReadOnly() {
		return bReadOnly;
	}
	void setMaxRows(int max) {
		iMaxRows=max;
	}
	int getMaxRows() {
		return iMaxRows;
	}
	boolean isNestedTransaction() {
		return bNestedTransaction;
	}
}

