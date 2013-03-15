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
 * ByteArray.java
 */

package org.hecataeus.hsql;
import java.io.*;
import java.sql.*;

class ByteArray {
  private byte data[];

  ByteArray(String s) {
    data=StringConverter.hexToByte(s);
  }
  byte[] byteValue() {
    return data;
  }
  int compareTo(ByteArray o) {
    int len=data.length;
    int lenb=o.data.length;
    for(int i=0;;i++) {
      int a=0,b=0;
      if(i<len) {
        a=((int)data[i]) & 0xff;
      } else if(i>=lenb) {
        return 0;
      }
      if(i<lenb) {
        b=((int)o.data[i]) & 0xff;
      }
      if(a>b) {
        return 1;
      }
      if(b>a) {
        return -1;
      }
    }
  }
  static byte[] serialize(Object s) throws SQLException {
    ByteArrayOutputStream bo=new ByteArrayOutputStream();
    try {
      ObjectOutputStream os=new ObjectOutputStream(bo);
      os.writeObject(s);
      return bo.toByteArray();
    } catch(Exception e) {
      throw Trace.error(Trace.SERIALIZATION_FAILURE,e.getMessage());
    }
  }
  static String serializeToString(Object s) throws SQLException {
    return createString(serialize(s));
  }
  Object deserialize() throws SQLException {
    try {
      ByteArrayInputStream bi=new ByteArrayInputStream(data);
      ObjectInputStream is=new ObjectInputStream(bi);
      return is.readObject();
    } catch(Exception e) {
      throw Trace.error(Trace.SERIALIZATION_FAILURE,e.getMessage());
    }
  }
  static String createString(byte b[]) {
    return StringConverter.byteToHex(b);
  }
  public String toString() {
    return createString(data);
  }
  public int hashCode() {
    return data.hashCode();
  }
}

