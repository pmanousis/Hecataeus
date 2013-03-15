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
 * Constraint.java
 */

package org.hecataeus.hsql;
import java.sql.SQLException;

//gpapas@21112007 modification -->make public
public class Constraint {
  
  //gpapas@21112007 modification -->make public
    public final static int 
          FOREIGN_KEY=0,
          MAIN   =1,
          UNIQUE =2,

//gpapas@21112007 modification -->ADD ADDITIONAL ENUMERATIONS
          PRIMARY_KEY= 3,
          NOT_NULL= 4,
          CHECK  = 5;
  private int iType;
  private int iLen;
  // Main is the table that is referenced
  private Table tMain;
  private int iColMain[];
  private Index iMain;
  private Object oMain[];
  // Ref is the table that has a reference to the main table
  private Table tRef;
  private int iColRef[];
  private Index iRef;
  private Object oRef[];

  Constraint(int type,Table t,int col[]) {
    iType=type;
    tMain=t;
    iColMain=col;
    iLen=col.length;
  }
  Constraint(int type,Table main,Table ref,int cmain[],int cref[])
  throws SQLException {
    iType=type;
    tMain=main;
    tRef=ref;
    iColMain=cmain;
    iColRef=cref;
    iLen=cmain.length;
    if(Trace.ASSERT) Trace.verify(cmain.length==cref.length);
    oMain=tMain.getNewRow();
    oRef=tRef.getNewRow();
    iMain=tMain.getIndexForColumns(cmain);
    iRef=tRef.getIndexForColumns(cref);
  }
  
  //gpapas addition 
  public Index getIndexMain() {
    return iMain;
  }  
//gpapas addition 
  public Index getIndexRef() {
    return iRef;
  }
  //gpapas modification -->make public
  public int getType() {
    return iType;
  }
    //gpapas modification -->make public
  public Table getMain() {
    return tMain;
  }
    //gpapas modification -->make public
  public Table getRef() {
    return tRef;
  }
    //gpapas modification -->make public
  public int[] getMainColumns() {
    return iColMain;
  }
   //gpapas modification -->make public
  public int[] getRefColumns() {
    return iColRef;
  }
  void replaceTable(Table old,Table n) throws SQLException {
    if(old==tMain) {
      tMain=n;
    } else if(old==tRef) {
      tRef=n;
    } else {
      Trace.verify(false,"could not replace");
    }
  }
  void checkInsert(Object row[]) throws SQLException {
    if(iType==MAIN || iType==UNIQUE) {
      // inserts in the main table are never a problem
      // unique constraints are checked by the unique index
      return;
    }
    // must be called synchronized because of oMain
    for(int i=0;i<iLen;i++) {
      Object o=row[iColRef[i]];
      if(o==null) {
        // if one column is null then integrity is not checked
        return;
      }
      oMain[iColMain[i]]=o;
    }
    // a record must exist in the main table
    Trace.check(iMain.find(oMain)!=null,Trace.INTEGRITY_CONSTRAINT_VIOLATION);
  }
  void checkDelete(Object row[]) throws SQLException {
    if(iType==FOREIGN_KEY || iType==UNIQUE) {
      // deleting references are never a problem
      // unique constraints are checked by the unique index
      return;
    }
    // must be called synchronized because of oRef
    for(int i=0;i<iLen;i++) {
      Object o=row[iColMain[i]];
      if(o==null) {
        // if one column is null then integrity is not checked
        return;
      }
      oRef[iColRef[i]]=o;
    }
    // there must be no record in the 'slave' table
    Trace.check(iRef.find(oRef)==null,Trace.INTEGRITY_CONSTRAINT_VIOLATION);
  }
  void checkUpdate(int col[],Result deleted,Result inserted)
  throws SQLException {
    if(iType==UNIQUE) {
      // unique constraints are checked by the unique index
      return;
    }
    if(iType==MAIN) {
      if(!isAffected(col,iColMain,iLen)) {
        return;
      }
      // check deleted records
      Record r=deleted.rRoot;
      while(r!=null) {
        // if a identical record exists we don't have to test
        if(iMain.find(r.data)==null) {
          checkDelete(r.data);
        }
        r=r.next;
      }
    } else if(iType==FOREIGN_KEY) {
      if(!isAffected(col,iColMain,iLen)) {
        return;
      }
      // check inserted records
      Record r=inserted.rRoot;
      while(r!=null) {
        checkInsert(r.data);
        r=r.next;
      }
    }
  }

  private boolean isAffected(int col[],int col2[],int len) {
    if(iType==UNIQUE) {
      // unique constraints are checked by the unique index
      return false;
    }
    for(int i=0;i<col.length;i++) {
      int c=col[i];
      for(int j=0;j<len;j++) {
        if(c==col2[j]) {
          return true;
        }
      }
    }
    return false;
  }
}
