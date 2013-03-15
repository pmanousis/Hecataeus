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
 * Access.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.sql.*;
import java.util.*;

class Access {
  final static int SELECT=1;
final static int DELETE=2;
final static int INSERT=4;
final static int UPDATE=8;
final static int ALL=15;
 
private Vector uUser;
private User uPublic;

  Access() throws SQLException {
    uUser=new Vector();
    uPublic=createUser("PUBLIC",null,false);
  }
  static int getRight(String right) throws SQLException {
    if(right.equals("ALL")) {
      return ALL;
    } else if(right.equals("SELECT")) {
      return SELECT;
    } else if(right.equals("UPDATE")) {
      return UPDATE;
    } else if(right.equals("DELETE")) {
      return DELETE;
    } else if(right.equals("INSERT")) {
      return INSERT;
    }
    throw Trace.error(Trace.UNEXPECTED_TOKEN,right);
  }
  static String getRight(int right) {
    if(right==ALL) {
      return "ALL";
    } else if(right==0) {
      return null;
    }
    StringBuffer b=new StringBuffer();
    if((right & SELECT)!=0) {
      b.append("SELECT,");
    }
    if((right & UPDATE)!=0) {
      b.append("UPDATE,");
    }
    if((right & DELETE)!=0) {
      b.append("DELETE,");
    }
    if((right & INSERT)!=0) {
      b.append("INSERT,");
    }
    String s=b.toString();
    return s.substring(0,s.length()-1);
  }
  User createUser(String name,String password,boolean admin)
  throws SQLException {
    for(int i=0;i<uUser.size();i++) {
      User u=(User)uUser.elementAt(i);
      if(u!=null && u.getName().equals(name)) {
        throw Trace.error(Trace.USER_ALREADY_EXISTS,name);
      }
    }
    User u=new User(name,password,admin,uPublic);
    uUser.addElement(u);
    return u;
  }
  void dropUser(String name) throws SQLException {
    Trace.check(!name.equals("PUBLIC"),Trace.ACCESS_IS_DENIED);
    for(int i=0;i<uUser.size();i++) {
      User u=(User)uUser.elementAt(i);
      if(u!=null && u.getName().equals(name)) {
        // todo: find a better way. Problem: removeElementAt would not
        // work correctly while others are connected
        uUser.setElementAt(null,i);
        u.revokeAll(); // in case the user is referenced in another way
        return;
      }
    }
    throw Trace.error(Trace.USER_NOT_FOUND,name);
  }
  User getUser(String name,String password) throws SQLException {
    Trace.check(!name.equals("PUBLIC"),Trace.ACCESS_IS_DENIED);
    if(name==null) {
      name="";
    }
    if(password==null) {
      password="";
    }
    User u=get(name);
    u.checkPassword(password);
    return u;
  }
  Vector getUsers() {
    return uUser;
  }
  void grant(String name,String object,int right) throws SQLException {
    get(name).grant(object,right);
  }
  void revoke(String name,String object,int right) throws SQLException {
    get(name).revoke(object,right);
  }

  private User get(String name) throws SQLException {
    for(int i=0;i<uUser.size();i++) {
      User u=(User)uUser.elementAt(i);
      if(u!=null && u.getName().equals(name)) {
        return u;
      }
    }
    throw Trace.error(Trace.USER_NOT_FOUND,name);
  }
}
