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
 * Trace.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;

class Trace extends PrintWriter {
//#ifdef TRACE
/*
  public static final boolean TRACE=true;
*/
//#else
  public static final boolean TRACE=false;
//#endif
  public static final boolean STOP=false;
  public static final boolean ASSERT=false;
  private static Trace tTracer=new Trace();
  private static int iLine;
  private static String sTrace;
  private static int iStop=0;
  final static int
    DATABASE_ALREADY_IN_USE=0;
final static int CONNECTION_IS_CLOSED=1;
final static int CONNECTION_IS_BROKEN=2;
final static int DATABASE_IS_SHUTDOWN=3;
final static int COLUMN_COUNT_DOES_NOT_MATCH=4;
final static int DIVISION_BY_ZERO=5;
final static int INVALID_ESCAPE=6;
final static int INTEGRITY_CONSTRAINT_VIOLATION=7;
final static int VIOLATION_OF_UNIQUE_INDEX=8;
final static int TRY_TO_INSERT_NULL=9;
final static int UNEXPECTED_TOKEN=10;
final static int UNEXPECTED_END_OF_COMMAND=11;
final static int UNKNOWN_FUNCTION=12;
final static int NEED_AGGREGATE=13;
final static int SUM_OF_NON_NUMERIC=14;
final static int WRONG_DATA_TYPE=15;
final static int SINGLE_VALUE_EXPECTED=16;
final static int SERIALIZATION_FAILURE=17;
final static int TRANSFER_CORRUPTED=18;
final static int FUNCTION_NOT_SUPPORTED=19;
final static int TABLE_ALREADY_EXISTS=20;
final static int TABLE_NOT_FOUND=21;
final static int INDEX_ALREADY_EXISTS=22;
final static int SECOND_PRIMARY_KEY=23;
final static int DROP_PRIMARY_KEY=24;
final static int INDEX_NOT_FOUND=25;
final static int COLUMN_ALREADY_EXISTS=26;
final static int COLUMN_NOT_FOUND=27;
final static int FILE_IO_ERROR=28;
final static int WRONG_DATABASE_FILE_VERSION=29;
final static int DATABASE_IS_READONLY=30;
final static int ACCESS_IS_DENIED=31;
final static int INPUTSTREAM_ERROR=32;
final static int NO_DATA_IS_AVAILABLE=33;
final static int USER_ALREADY_EXISTS=34;
final static int USER_NOT_FOUND=35;
final static int ASSERT_FAILED=36;
final static int EXTERNAL_STOP=37;
final static int GENERAL_ERROR=38;
final static int WRONG_OUT_PARAMETER=39;
final static int ERROR_IN_FUNCTION=40;
  private static String sDescription[]= {
    "08001 The database is already in use by another process",
    "08003 Connection is closed",
    "08003 Connection is broken",
    "08003 The database is shutdown",
    "21S01 Column count does not match",
    "22012 Division by zero",
    "22019 Invalid escape character",
    "23000 Integrity constraint violation",
    "23000 Violation of unique index",
    "23000 Try to insert null into a non-nullable column",
    "37000 Unexpected token",
    "37000 Unexpected end of command",
    "37000 Unknown function",
    "37000 Need aggregate function or group by",
    "37000 Sum on non-numeric data not allowed",
    "37000 Wrong data type",
    "37000 Single value expected",
    "40001 Serialization failure",
    "40001 Transfer corrupted",
    "IM001 This function is not supported",
    "S0001 Table already exists",
    "S0002 Table not found",
    "S0011 Index already exists",
    "S0011 Attempt to define a second primary key",
    "S0011 Attempt to drop the primary key",
    "S0012 Index not found",
    "S0021 Column already exists",
    "S0022 Column not found",
    "S1000 File input/output error",
    "S1000 Wrong database file version",
    "S1000 The database is in read only mode",
    "S1000 Access is denied",
    "S1000 InputStream error",
    "S1000 No data is available",
    "S1000 User already exists",
    "S1000 User not found",
    "S1000 Assert failed",
    "S1000 External stop request",
    "S1000 General error",
    "S1009 Wrong OUT parameter",
    "S1010 Error in function"
  };

  static SQLException getError(int code,String add) {
    String s=getMessage(code);
    if(add!=null) {
      s+=": "+add;
    }
    return getError(s);
  }
  static String getMessage(int code) {
    return sDescription[code];
  }
  static String getMessage(SQLException e) {
    return e.getSQLState()+" "+e.getMessage();
  }
  static SQLException getError(String msg) {
    return new SQLException(msg.substring(6),msg.substring(0,5));
  }
  static SQLException error(int code) {
    return getError(code,null);
  }
  static SQLException error(int code,String s) {
    return getError(code,s);
  }
  static SQLException error(int code,int i) {
    return getError(code,""+i);
  }
  static void verify(boolean condition) throws SQLException {
    verify(condition,null);
  }
  static void verify(boolean condition,String error) throws SQLException {
    if(!condition) {
      printStack();
      throw getError(ASSERT_FAILED,error);
    }
  }
  static void check(boolean condition,int code) throws SQLException {
    check(condition,code,null);
  }
  static void check(boolean condition,int code,String s) throws SQLException {
    if(!condition) {
      throw getError(code,s);
    }
  }
  // for the PrinterWriter interface
  public void println(char c[]) {
    if(iLine++==2) {
      String s=new String(c);
      int i=s.indexOf('.');
      if(i!=-1) {
        s=s.substring(i+1);
      }
      i=s.indexOf('(');
      if(i!=-1) {
        s=s.substring(0,i);
      }
      sTrace=s;
    }
  }
  Trace() {
    super(System.out);
  }
  static void trace(long l) {
    traceCaller(""+l);
  }
  static void trace(int i) {
    traceCaller(""+i);
  }
  static void trace() {
    traceCaller("");
  }
  static void trace(String s) {
    traceCaller(s);
  }
  static void stop() throws SQLException {
    stop(null);
  }
  static void stop(String s) throws SQLException {
    if(iStop++ % 10000 != 0) {
      return;
    }
    if(new File("trace.stop").exists()) {
      printStack();
      throw getError(EXTERNAL_STOP,s);
    }
  }
  static private void printStack() {
    Exception e=new Exception();
    e.printStackTrace();
  }
  static private void traceCaller(String s) {
    Exception e=new Exception();
    iLine=0;
    e.printStackTrace(tTracer);
    s=sTrace+"\t"+s;
    // trace to System.out is handy if only trace messages of hsql are required
//#ifdef TRACESYSTEMOUT
    System.out.println(s);
//#else
/*
    DriverManager.println(s);
*/
//#endif
  }
}

