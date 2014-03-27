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
 * Expression.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

public class Expression {
	// leaf types

	//gpapas modification -->make public
	public final static int VALUE=1;

	public final static int COLUMN=2;

	public final static int QUERY=3;

	public final static int TRUE=4;

	public final static int VALUELIST=5;

	public final static int ASTERIX=6;

	public final static int FUNCTION=7;

	// operations
	//gpapas modification -->make public
	public final static int NEGATE=9;

	public final static int ADD=10;

	public final static int SUBTRACT=11;

	public final static int MULTIPLY=12;

	public final static int DIVIDE=14;

	public final static int CONCAT=15;

	// logical operations
	//gpapas modification -->make public
	public final static int NOT=20;

	public final static int EQUAL=21;

	public final static int BIGGER_EQUAL=22;

	public final static int BIGGER=23;

	public final static int SMALLER=24;

	public final static int SMALLER_EQUAL=25;

	public final static int NOT_EQUAL=26;

	public final static int LIKE=27;

	public final static int AND=28;

	public final static int OR=29;

	public final static int IN=30;

	public final static int EXISTS=31;

	// aggregate functions
	//gpapas modification -->make public
	public final static int COUNT=40;

	public final static int SUM=41;

	public final static int MIN=42;

	public final static int MAX=43;

	public final static int AVG=44;

	// system functions
	//gpapas modification -->make public
	public final static int IFNULL=60;

	public final static int CONVERT=61;

	public final static int CASEWHEN=62;

	// temporary used during paring
	//gpapas modification -->make public
	public final static int PLUS=100;

	public final static int OPEN=101;

	public final static int CLOSE=102;

	public final static int SELECT=103;

	public final static int COMMA=104;

	public final static int STRINGCONCAT=105;

	public final static int BETWEEN=106;

	public final static int CAST=107;

	public final static int END=108;

	//gpapas modification -->make public
	public  int iType;

	// nodes
	//gpapas modification -->make public
	public Expression eArg;

	public Expression eArg2;

	// VALUE, VALUELIST
	private Object oData;
	private Hashtable hList;

	//gpapas modification -->make public
	public  int iDataType;

	// QUERY (correlated subquery)
	//gpapas modification -->make public
	public  Select sSelect;

	// FUNCTION
	private Function fFunction;

	// LIKE
	private char cLikeEscape;

	// COLUMN
	//gpapas modification -->make public
	public String sTable;

	public String sColumn;

	//gpapas modification -->make public
	public TableFilter tFilter;   // null if not yet resolved
	//gpapas modification -->make public
	public int iColumn;
	private String sAlias;          // if it is a column of a select column list
	private boolean bDescending;    // if it is a column in a order by

	public Expression(Function f) {
		iType=FUNCTION;
		fFunction=f;
	}
	public Expression(Expression e) {
		iType=e.iType;
		iDataType=e.iDataType;
		eArg=e.eArg;
		eArg2=e.eArg2;
		cLikeEscape=e.cLikeEscape;
		sSelect=e.sSelect;
		fFunction=e.fFunction;
	}
	public Expression(Select s) {
		iType=QUERY;
		sSelect=s;
	}
	public  Expression(Vector v) {
		iType=VALUELIST;
		iDataType=Column.VARCHAR;
		int len=v.size();
		hList=new Hashtable(len);
		for(int i=0;i<len;i++) {
			Object o=v.elementAt(i);
			if(o!=null) {
				hList.put(o,this); // todo: don't use such dummy objects
			}
		}
	}
	public Expression(int type,Expression e,Expression e2) {
		iType=type;
		eArg=e;
		eArg2=e2;
	}
	public Expression(String table,String column) {
		sTable=table;
		if(column==null) {
			iType=ASTERIX;
		} else {
			iType=COLUMN;
			sColumn=column;
		}
	}
	public Expression(int datatype,Object o) {
		iType=VALUE;
		iDataType=datatype;
		oData=o;
	}
	void setLikeEscape(char c) {
		cLikeEscape=c;
	}
	void setDataType(int type) {
		iDataType=type;
	}
	void setTrue() {
		iType=TRUE;
	}
	boolean isAggregate() {
		if(iType==COUNT || iType==MAX || iType==MIN || iType==SUM || iType==AVG) {
			return true;
		}
		// todo: recurse eArg and eArg2; maybe they are grouped.
		// grouping 'correctly' would be quite complex
		return false;
	}
	void setDescending() {
		bDescending=true;
	}
	boolean isDescending() {
		return bDescending;
	}
	void setAlias(String s) {
		sAlias=s;
	}
	public String getAlias() {
		if(sAlias!=null) {
			return sAlias;
		}
		if(iType==VALUE) {
			return "";
		}
		if(iType==COLUMN) {
			return sColumn;
		}
		// todo
		return "";
	}

	//gpapas modification -->make public
	public int getType() {
		return iType;
	}
	int getColumnNr() {
		return iColumn;
	}
	//gpapas modification -->make public
	public Expression getArg() {
		return eArg;
	}
	//gpapas modification -->make public
	public Expression getArg2() {
		return eArg2;
	}
	//gpapas modification -->make public
	public TableFilter getFilter() {
		return tFilter;
	}
	void checkResolved() throws SQLException {
		Trace.check(iType!=COLUMN || tFilter!=null,Trace.COLUMN_NOT_FOUND,sColumn);
		if(eArg!=null) {
			eArg.checkResolved();
		}
		if(eArg2!=null) {
			eArg2.checkResolved();
		}
		if(sSelect!=null) {
			sSelect.checkResolved();
		}
		if(fFunction!=null) {
			fFunction.checkResolved();
		}
	}
	void resolve(TableFilter f) throws SQLException {
		if(f!=null && iType==COLUMN) {
			if(sTable==null || f.getName().equals(sTable)) {
				int i=f.getTable().searchColumn(sColumn);
				if(i!=-1) {
					// todo: other error message: multiple tables are possible
					Trace.check(tFilter==null||tFilter==f,Trace.COLUMN_NOT_FOUND,sColumn);
					tFilter=f;
					iColumn=i;
					sTable=f.getName();
					iDataType=f.getTable().getColumnType(i);
				}
			}
		}
		// currently sets only data type
		// todo: calculate fixed expressions if possible
		if(eArg!=null) {
			eArg.resolve(f);
		}
		if(eArg2!=null) {
			eArg2.resolve(f);
		}
		if(sSelect!=null) {
			sSelect.resolve(f,false);
			sSelect.resolve();
		}
		if(fFunction!=null) {
			fFunction.resolve(f);
		}
		if(iDataType!=0) {
			return;
		}
		switch(iType) {
		case FUNCTION:
			iDataType=fFunction.getReturnType();
			break;
		case QUERY:
			iDataType=sSelect.eColumn[0].iDataType;
			break;
		case NEGATE:
			iDataType=eArg.iDataType;
			break;
		case ADD:
		case SUBTRACT:
		case MULTIPLY:
		case DIVIDE:
			iDataType=eArg.iDataType;
			break;
		case CONCAT:
			iDataType=Column.VARCHAR;
			break;
		case NOT:
		case EQUAL:
		case BIGGER_EQUAL:
		case BIGGER:
		case SMALLER:
		case SMALLER_EQUAL:
		case NOT_EQUAL:
		case LIKE:
		case AND:
		case OR:
		case IN:
		case EXISTS:
			iDataType=Column.BIT;
			break;
		case COUNT:
			iDataType=Column.INTEGER;
			break;
		case MAX:
		case MIN:
		case SUM:
		case AVG:
			iDataType=eArg.iDataType;
			break;
		case CONVERT:
			// it is already set
			break;
		case IFNULL:
		case CASEWHEN:
			iDataType=eArg2.iDataType;
			break;
		}
	}
	boolean isResolved() {
		if(iType==VALUE)  {
			return true;
		}
		if(iType==COLUMN) {
			return tFilter!=null;
		}
		// todo: could recurse here, but never miss a 'false'!
		return false;
	}
	static boolean isCompare(int i) {
		switch(i) {
		case EQUAL:
		case BIGGER_EQUAL:
		case BIGGER:
		case SMALLER:
		case SMALLER_EQUAL:
		case NOT_EQUAL:
			return true;
		}
		return false;
	}
	//gpapas modification -->make public
	public String getTableName() {
		if(iType==ASTERIX) {
			return sTable;
		}
		if(iType==COLUMN) {

			//gpapas modification --> comment
			//    	if(tFilter==null) {
			return sTable; 
			//    	} else {
			//    		return tFilter.getTable().getName();
			//    	}
			//gpapas modification -->comment
		}
		// todo
		return "";
	}
	//gpapas modification -->add public method
	public Function getFunction() {
		return fFunction;
	}

	//gpapas modification -->make public
	public String getColumnName() {
		if(iType==COLUMN) {
			if(tFilter==null) {
				return sColumn;
			} else {
				return tFilter.getTable().getColumnName(iColumn);
			}
		}
		return getAlias();
	}
	void swapCondition() throws SQLException {
		int i=EQUAL;
		switch(iType) {
		case BIGGER_EQUAL:
			i=SMALLER_EQUAL;
			break;
		case SMALLER_EQUAL:
			i=BIGGER_EQUAL;
			break;
		case SMALLER:
			i=BIGGER;
			break;
		case BIGGER:
			i=SMALLER;
			break;
		case EQUAL:
			break;
		default:
			Trace.verify(false,"Expression.swapCondition");
		}
		iType=i;
		Expression e=eArg;
		eArg=eArg2;
		eArg2=e;
	}
	Object getValue(int type) throws SQLException {
		Object o=getValue();
		if(o==null || iDataType==type) {
			return o;
		}
		String s=Column.convertObject(o);
		return Column.convertString(s,type);
	}
	int getDataType() {
		return iDataType;
	}
	//gpapas modification -->make public
	public Object getValue() throws SQLException {
		switch(iType) {
		case VALUE:
			return oData;
		case COLUMN:
			try {
				return tFilter.oCurrentData[iColumn];
			} catch(NullPointerException e) {
				throw Trace.error(Trace.COLUMN_NOT_FOUND,sColumn);
			}
		case FUNCTION:
			return fFunction.getValue();
		case QUERY:
			return sSelect.getValue(iDataType);
		case NEGATE:
			return Column.negate(eArg.getValue(iDataType),iDataType);
		case COUNT:
			// count(*): sum(1); count(col): sum(col<>null)
			if(eArg.iType==ASTERIX || eArg.getValue()!=null) {
				return new Integer(1);
			}
			return new Integer(0);
		case MAX:
		case MIN:
		case SUM:
		case AVG:
			return eArg.getValue();
		case EXISTS:
			return new Boolean(test());
		case CONVERT:
			return eArg.getValue(iDataType);
		case CASEWHEN:
			if(eArg.test()) {
				return eArg2.eArg.getValue();
			} else {
				return eArg2.eArg2.getValue();
			}
		}
		// todo: simplify this
		Object a=null,b=null;
		if(eArg!=null) {
			a=eArg.getValue(iDataType);
		}
		if(eArg2!=null) {
			b=eArg2.getValue(iDataType);
		}
		switch(iType) {
		case ADD:
			return Column.add(a,b,iDataType);
		case SUBTRACT:
			return Column.subtract(a,b,iDataType);
		case MULTIPLY:
			return Column.multiply(a,b,iDataType);
		case DIVIDE:
			return Column.divide(a,b,iDataType);
		case CONCAT:
			return Column.concat(a,b);
		case IFNULL:
			return a==null ? b : a;
		default:
			// must be comparisation
			// todo: make sure it is
			return new Boolean(test());
		}
	}
	private boolean testValueList(Object o,int datatype) throws SQLException {
		if(iType==VALUELIST) {
			if(datatype!=iDataType) {
				o=Column.convertObject(o,iDataType);
			}
			return hList.containsKey(o);
		} else if(iType==QUERY) {
			// todo: convert to valuelist before if everything is resolvable
			Result r=sSelect.getResult(0);
			Record n=r.rRoot;
			int type=r.iType[0];
			if(datatype!=type) {
				o=Column.convertObject(o,type);
			}
			while(n!=null) {
				Object o2=n.data[0];
				if(o2!=null && o2==o) {
					return true;
				}
				n=n.next;
			}
			return false;
		}
		throw Trace.error(Trace.WRONG_DATA_TYPE);
	}
	boolean test() throws SQLException {
		switch(iType) {
		case TRUE:
			return true;
		case NOT:
			Trace.verify(eArg2==null,"Expression.test");
			return !eArg.test();
		case AND:
			return eArg.test() && eArg2.test();
		case OR:
			return eArg.test() || eArg2.test();
		case LIKE:
			// todo: now for all tests a new 'like' object required!
			String s=(String)eArg2.getValue(Column.VARCHAR);
			int type=eArg.iDataType;
			Like l=new Like(s,cLikeEscape,type==Column.VARCHAR_IGNORECASE);
			String c=(String)eArg.getValue(Column.VARCHAR);
			return l.compare(c);
		case IN:
			return eArg2.testValueList(eArg.getValue(),eArg.iDataType);
		case EXISTS:
			Result r=eArg.sSelect.getResult(1); // 1 is already enough
			return r.rRoot!=null;
		}
		Trace.check(eArg!=null,Trace.GENERAL_ERROR);
		Object o=eArg.getValue();
		int type=eArg.iDataType;
		Trace.check(eArg2!=null,Trace.GENERAL_ERROR);
		Object o2=eArg2.getValue(type);
		int result=Column.compare(o,o2,type);
		switch(iType) {
		case EQUAL:
			return result==0;
		case BIGGER:
			return result>0;
		case BIGGER_EQUAL:
			return result>=0;
		case SMALLER_EQUAL:
			return result<=0;
		case SMALLER:
			return result<0;
		case NOT_EQUAL:
			return result!=0;
		}
		Trace.verify(false,"Expression.test2");
		return false;
	}
}
