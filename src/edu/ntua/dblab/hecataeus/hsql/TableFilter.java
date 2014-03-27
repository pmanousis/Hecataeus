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
 * TableFilter.java
 */

package edu.ntua.dblab.hecataeus.hsql;
import java.sql.SQLException;

//gpapas modification -->make public
public class TableFilter {
	private Table tTable;
	private String sAlias;
	private Index iIndex;
	private Node nCurrent;
	private Object oEmptyData[];
	private Expression eStart;
	private Expression eEnd;
	private Expression eAnd;
	private boolean bOuterJoin;

	// this is public to improve performance
	Object oCurrentData[];

	// Object[] getCurrent() {
	//   return oCurrentData;
	// }
	TableFilter(Table t,String alias,boolean outerjoin) {
		tTable=t;
		iIndex=null;
		sAlias=alias!=null ? alias : t.getName();
		bOuterJoin=outerjoin;
		oEmptyData=tTable.getNewRow();
	}
	//gpapas modification -->make public
	public String getName() {
		return sAlias;
	}
	//gpapas modification -->make public
	public Table getTable() {
		return tTable;
	}
	void setCondition(Expression e) throws SQLException {
		int type=e.getType();
		Expression e1=e.getArg();
		Expression e2=e.getArg2();
		if(type==Expression.AND) {
			setCondition(e1);
			setCondition(e2);
			return;
		}
		int candidate;
		switch(type) {
		case Expression.NOT_EQUAL:
		case Expression.LIKE:  // todo: maybe use index
		case Expression.IN:
			candidate=0;
			break;
		case Expression.EQUAL:
			candidate=1;
			break;
		case Expression.BIGGER:
		case Expression.BIGGER_EQUAL:
			candidate=2;
			break;
		case Expression.SMALLER:
		case Expression.SMALLER_EQUAL:
			candidate=3;
			break;
		default:
			// not a condition so forget it
			return;
		}
		if(e1.getFilter()==this) {
			// ok include this
		} else if(e2.getFilter()==this && candidate!=0) {
			// swap and try again to allow index usage
			e.swapCondition();
			setCondition(e);
			return;
		} else {
			// unrelated: don't include
			return;
		}
		Trace.verify(e1.getFilter()==this,"setCondition");
		if(!e2.isResolved()) {
			return;
		}
		if(candidate==0) {
			addAndCondition(e);
			return;
		}
		int i=e1.getColumnNr();
		Index index=tTable.getIndexForColumn(i);
		if(index==null || (iIndex!=index && iIndex!=null)) {
			// no index or already another index is used
			addAndCondition(e);
			return;
		}
		iIndex=index;
		if(candidate==1) {
			// candidate for both start & end
			if(eStart!=null || eEnd!=null) {
				addAndCondition(e);
				return;
			}
			eStart=new Expression(e);
			eEnd=eStart;
		} else if(candidate==2) {
			// candidate for start
			if(eStart!=null) {
				addAndCondition(e);
				return;
			}
			eStart=new Expression(e);
		} else if(candidate==3) {
			// candidate for end
			if(eEnd!=null) {
				addAndCondition(e);
				return;
			}
			eEnd=new Expression(e);
		}
		e.setTrue();
	}
	boolean findFirst() throws SQLException {
		if(iIndex==null) {
			iIndex=tTable.getPrimaryIndex();
		}
		if(eStart==null) {
			nCurrent=iIndex.first();
		} else {
			int type=eStart.getArg().getDataType();
			Object o=eStart.getArg2().getValue(type);
			nCurrent=iIndex.findFirst(o,eStart.getType());
		}
		while(nCurrent!=null) {
			oCurrentData=nCurrent.getData();
			if(!test(eEnd)) {
				break;
			}
			if(test(eAnd)) {
				return true;
			}
			nCurrent=iIndex.next(nCurrent);
		}
		oCurrentData=oEmptyData;
		if(bOuterJoin) {
			return true;
		}
		return false;
	}
	boolean next() throws SQLException {
		if(bOuterJoin && nCurrent==null) {
			return false;
		}
		nCurrent=iIndex.next(nCurrent);
		while(nCurrent!=null) {
			oCurrentData=nCurrent.getData();
			if(!test(eEnd)) {
				break;
			}
			if(test(eAnd)) {
				return true;
			}
			nCurrent=iIndex.next(nCurrent);
		}
		oCurrentData=oEmptyData;
		return false;
	}

	private void addAndCondition(Expression e) {
		Expression e2=new Expression(e);
		if(eAnd==null) {
			eAnd=e2;
		} else {
			Expression and=new Expression(Expression.AND,eAnd,e2);
			eAnd=and;
		}
		e.setTrue();
	}
	private boolean test(Expression e) throws SQLException {
		if(e==null) {
			return true;
		}
		return e.test();
	}
}

