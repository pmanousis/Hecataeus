/**
 * @author George Papastefanatos, National Technical University of Athens
 * 
 */
package org.hecataeus.parser;

import java.sql.SQLException;
import java.util.*;
import java.io.*;

import org.hecataeus.evolution.*;
import org.hecataeus.hsql.*;

/**
 * @author  gpapas
 */

public class HecataeusGraphCreator {
	
	private int queries;
	private int tables;
	public HecataeusEvolutionGraph HGraph;

	public HecataeusGraphCreator() {
		tables = 0;
		queries = 0;
		HGraph = new HecataeusEvolutionGraph() ;
	}

	private HecataeusEvolutionNode add_node(String Label, HecataeusNodeType Type) {
		HecataeusEvolutionNode v = new HecataeusEvolutionNode(Label, Type);
		HGraph.addNode(v);
		return v;
	}

	private HecataeusEvolutionEdge add_edge( HecataeusEvolutionNode u, HecataeusEvolutionNode  v, HecataeusEdgeType Type, String Label) {
		HecataeusEvolutionEdge e = new HecataeusEvolutionEdge(Label, Type, u, v);
		HGraph.addEdge(e);
		return  e ;
	}

	public boolean add_table(Table tTable) throws SQLException {
		try {
			HecataeusEvolutionNode u  ;
			HecataeusEvolutionNode v  ;
			HecataeusEvolutionEdge e  ;
			// Add the relation node
			u = add_node(tTable.getName(), HecataeusNodeType.NODE_TYPE_RELATION);
			// Add the attribute nodes
			for (int i = 0; i < tTable.getColumnCount();i++) {
				// Add new attribute node and a new edge with the relation node
				v = add_node(tTable.getColumnName(i), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
				e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S");
			}
			addTableConstraints(tTable);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return true;
	}

        private void addTableConstraints(Table  tTable) {
            try { 
                // Add constraints
                for (int i=0;i<tTable.getConstraints().size();i++){
                    Constraint c = (Constraint) tTable.getConstraints().get(i);
                    HecataeusEvolutionNode w = addConstraintNode(tTable, c);
                }
            }
            catch(Exception e)
            {    //no constraints have been created    
                System.out.println(e.getMessage().toString());                   
            }                
        }

        
	private  HecataeusEvolutionNode addConstraintNode(Table tTable, Constraint cConstraint){
		HecataeusEvolutionNode u;
                int cols[] ; 
                if (cConstraint.getType() == Constraint.PRIMARY_KEY) {
                    //add constraint node
                    u = add_node(tTable.getName() + ".PK",HecataeusNodeType.NODE_TYPE_CONDITION);
                    //for each column in constraint node create edges
                    cols = cConstraint.getMainColumns();
                    for (int i = 0; i < cols.length; i++) {
                        int columnNumber = ((Integer)cols[i]).intValue();
                        HecataeusEvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
                        HecataeusEvolutionEdge e = add_edge(v, u, HecataeusEdgeType.EDGE_TYPE_OPERATOR, "op");
                    }
                    //return constraint node
                    return u;
		} else if (cConstraint.getType()==Constraint.UNIQUE)  {
                     //add constraint node
                    u = add_node(tTable.getName() + ".UC",HecataeusNodeType.NODE_TYPE_CONDITION);
                    //for each column in constraint node create edges
                    cols = cConstraint.getMainColumns();
                    for (int i = 0; i < cols.length; i++) {
                        int columnNumber = ((Integer)cols[i]).intValue();
                        HecataeusEvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
                        HecataeusEvolutionEdge e = add_edge(v, u, HecataeusEdgeType.EDGE_TYPE_OPERATOR, "op");
                    }
                    //return constraint node
                    return u;
		} else if (cConstraint.getType()==Constraint.NOT_NULL) {
                    //add constraint node
                    u = add_node(tTable.getName() + ".NC",HecataeusNodeType.NODE_TYPE_CONDITION);
                    //for each column in constraint node create edges
                    cols = cConstraint.getMainColumns();
                    for (int i = 0; i < cols.length; i++) {
                        int columnNumber = ((Integer)cols[i]).intValue();
                        HecataeusEvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
                        HecataeusEvolutionEdge e = add_edge(v, u, HecataeusEdgeType.EDGE_TYPE_OPERATOR, "op");
                    }
                    //return constraint node
                    return u;
		} else if (cConstraint.getType()==Constraint.FOREIGN_KEY) {
                    //add constraint node
                    u = add_node(tTable.getName() +  ".FK", HecataeusNodeType.NODE_TYPE_CONDITION);
                    //for each column in detail table create edges from attributes nodes to fk node
                    tTable = cConstraint.getRef();
                    cols = cConstraint.getRefColumns();
                    for (int i = 0; i < cols.length; i++) {
                        int columnNumber = ((Integer)cols[i]).intValue();
                        HecataeusEvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
                        //from attribute nodes (v) -->constraint node(u) 
                        HecataeusEvolutionEdge e = add_edge(v, u, HecataeusEdgeType.EDGE_TYPE_OPERATOR, "op1");
                    }

                    //for each column in master table create edges from fk node to attributes nodes
                    tTable = cConstraint.getMain();
                    cols = cConstraint.getMainColumns();
                    for (int i = 0; i < cols.length; i++) {
                        int columnNumber = ((Integer)cols[i]).intValue();
                        HecataeusEvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
                        //from constraint node(u) --> attribute nodes (v)
                        HecataeusEvolutionEdge e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_OPERATOR, "op2");
                    }
                    //return constraint node
                    return u ;
		}
		 else { //other constraints are not captured
			return null;
		}
	}
        
	private HecataeusEvolutionNode  find_attribute(String TableName, String Attribute) {
		HecataeusEvolutionNode u ;
		u = HGraph.getAttributeNode(TableName, Attribute);
		return u ;
	}

	private HecataeusEvolutionNode find_attribute(Select sSelect, String attribute) {
		Table tTable ;
		String foundTable = "";
		int foundColumns = 0;
                for (int i = 0; i < sSelect.tFilter.length; i++) {
                    tTable = sSelect.tFilter[i].getTable();
                    for (int j = 0; j < tTable.getColumnCount() ;j++) {
                        if (attribute==tTable.getColumnName(j)) {
                            foundColumns++;
                            foundTable = tTable.getName() ;
                        }
                    }
                }
                if ( foundColumns == 1 ) {
                    return find_attribute(foundTable, attribute);
                } else{
                    return null;
                }
        }

	public boolean add_query(Select sSelect) throws SQLException{
		try {
			HecataeusEvolutionNode u ;
			u = new_query(sSelect, false);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return true;
	}

	public boolean add_view(Select sSelect, String view_name) throws SQLException{
		try {
			HecataeusEvolutionNode u ;
			u = new_view(sSelect, false, view_name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return true;
	}

	private HecataeusEvolutionNode new_view(Select sSelect, boolean nested, String view_name) throws SQLException{
		Expression expression;
		String GroupByLabel = null;
		// TableFilter ta
		HecataeusEvolutionNode u ;
		HecataeusEvolutionNode v = null ;
		HecataeusEvolutionNode w = null ;
		HecataeusEvolutionNode x = null ;
		HecataeusEvolutionEdge e ;
		HecataeusEvolutionEdge e2;
		int tblIndex = 0;
		// __________________________________________________________________________________________________  
		// check for self-join
		boolean SJQuery = false;
		int len = sSelect.tFilter.length;
		int[] SelfJoin = new int[len]  ; // dim statement
		SelfJoin = checkSelfJoin(sSelect);
		
                if ( SelfJoin != null ) {
                    SJQuery = true;
                    for (int cur = 0; cur < len; cur++) {
                        if ( SelfJoin[cur] != -1 ) {
                            String tblName = sSelect.tFilter[cur].getTable().getName();
                        }
                    }
                }
		// __________________________________________________________________________________________________  
		// resolve tables names for all attributes
		// mentioned in the SELECT and WHERE clause
		Resolve(sSelect, SJQuery);
		// __________________________________________________________________________________________________  
		// __________________________________________________________________________________________________  
               //Add the view node
                u = add_node(view_name, HecataeusNodeType.NODE_TYPE_VIEW);
		// __________________________________________________________________________________________________  
		// Visualize the select part of a view
		// The following loop examines only the selected columns
                for (int i = 0; i < sSelect.eColumn.length - sSelect.iOrderLen - sSelect.iGroupLen; i++) {
                	expression = sSelect.eColumn[i];
                	// SHOULD ADD CHECK FOR ALIAS FOR SELF JOIN QUERIES
                	// Type COLUMN
                	if (expression.getType()== Expression.COLUMN) {
                		v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE) ;
                		e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                		x = find_attribute(expression.getTableName(), expression.getColumnName()) ;
                		e = add_edge(v,	x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;
                	}
                	// Type VALUE
                	if (expression.getType()== Expression.VALUE) {
                		try{
                			if (expression.getAlias()==""){
                				v = add_node(expression.getValue().toString(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE) ;
                			}else{
                				v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE) ;
                				HecataeusEvolutionNode v1 = add_node(expression.getValue().toString(), HecataeusNodeType.NODE_TYPE_CONSTANT) ;
                				e = add_edge(v, v1, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;
                			}
                			e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                		}
                		catch (Exception ex){
                			System.out.println("[Expression Object in new_view]"+ ex.getMessage() );
                		}
                	}
                	// Type Function
                	if (expression.getType()== Expression.FUNCTION) {
                		//add a recursive method for nested functions

                		Function function = expression.getFunction();
                		if (expression.getAlias()== "")
                			v = add_node(function.getName(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                		else 
                			v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);

                		w = add_node(function.getName(), HecataeusNodeType.NODE_TYPE_FUNCTION) ;
                		e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                		e = add_edge(v, w, HecataeusEdgeType.EDGE_TYPE_MAPPING,"map-select");

                		for (int j = 0; j <function.eArg.length ; j++) {
                        	HecataeusEvolutionNode arg =add_expression(function.eArg[j]);
                   			e = add_edge(w,	arg, HecataeusEdgeType.EDGE_TYPE_MAPPING, "op"+(j+1)) ;
                   		}
                	}
                	// Type COUNT (40), SUM (41), MIN (42), MAX (43), AVG (44)
                	if  (expression.getType() == Expression.COUNT
                			|| expression.getType() == Expression.SUM
                			|| expression.getType() == Expression.MIN
                			|| expression.getType() == Expression.MAX
                			|| expression.getType() == Expression.AVG) {
                		String strFunction = "";
                		switch (expression.getType()){
                		case Expression.COUNT :  
                			strFunction = "COUNT";
                			break;
                		case Expression.SUM :
                			strFunction = "SUM";
                			break;
                		case Expression.MIN :
                			strFunction = "MIN";
                			break;
                		case Expression.MAX :
                			strFunction = "MAX";
                			break;
                		case Expression.AVG :
                			strFunction = "AVG";
                			break;  
                		default: break;
                		}
                		if (expression.getAlias()== "")
                			v = add_node(strFunction, HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                		else 
                			v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                		
                		w = add_node(strFunction, HecataeusNodeType.NODE_TYPE_FUNCTION) ;
                		e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                		e = add_edge(v, w, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;

                		switch (expression.getArg().getType())
                		{
                		//if the aggregate function's argument is an asterix
                		case Expression.ASTERIX:
                			//if we have func(R.*)
                			if (expression.getArg().getTableName()!=null){
                				//construct name of query.attribute node (i.e. from COUNT . COUNT(R.*))
                				//only if no alias exist for this column
                            	if (expression.getAlias()== "")
                            		v.setName(v.getName()+ "(" + expression.getArg().getTableName() + ".*)");
                				HecataeusEvolutionNode x1 = find_relation(expression.getArg().getTableName());
                				//if no alias relation node is found (i.e., no self join)
                				if (x1 == null){ 
                					//get real table
                					for (int k = 0; k < sSelect.tFilter.length; k++)
                					{
                						if (expression.getArg().getTableName().equals(sSelect.tFilter[k].getName())){
                							tblIndex = k;
                							x1 = find_relation(sSelect.tFilter[i].getTable().getName());
                						}                                                
                					}
                				}else{ 
                					//get alias table
                					for (int g = 0; g < sSelect.tFilter.length; g++)
                					{
                						if (expression.getArg().getTableName().equals(sSelect.tFilter[g].getName())){
                							tblIndex = g;
                						}                                                
                					}
                				}

                				//convert func(R.*) it to graph
                				//map only query attributes to R attributes
                				for (int l = 0; l < sSelect.tFilter[tblIndex].getTable().getColumnCount(); l++){
                					x = find_attribute(sSelect.tFilter[tblIndex].getName(),sSelect.tFilter[tblIndex].getTable().getColumnName(l));
                					if (x == null){
                						x = find_attribute(sSelect.tFilter[tblIndex].getTable().getName(),sSelect.tFilter[tblIndex].getTable().getColumnName(l));
                					}
                					e = add_edge(w, x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                				}
                			}	
                			//if we have func(*)
                			else
                			{
                				//construct name of query.attribute node (i.e. from COUNT . COUNT(*))
                				//only if no alias exist for this column
                            	if (expression.getAlias()== "")
                            		v.setName(v.getName() + "(*)");
                            	
                				for (int l = 0; l < sSelect.tFilter.length; l++){
                					for (int j = 0; j < sSelect.tFilter[l].getTable().getColumnCount(); j++){
                						x = find_attribute(sSelect.tFilter[l].getName(),sSelect.tFilter[l].getTable().getColumnName(j));
                						if (x == null){
                							x = find_attribute(sSelect.tFilter[l].getTable().getName(), sSelect.tFilter[l].getTable().getColumnName(j));
                						}
                						e = add_edge(w, x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                					}
                				}
                			}
                			break;

                			//if the aggregate function's argument is a column
                		case Expression.COLUMN:
                			//construct name of query.attribute node (i.e. from COUNT . COUNT(attribute_name))			
                			//only if no alias exist for this column
                        	if (expression.getAlias()== "")
                        		v.setName(v.getName() + "(" + expression.getArg().getColumnName() + ")");
                			
                        	x = find_attribute(expression.getArg().getTableName(), expression.getArg().getColumnName());                            
                			e = add_edge(w,	x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                			break;

                		default : 
                			x = add_expression(expression.getArg());
                		e = add_edge(w,	x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                        break;
                		}
                	}
                }
		// __________________________________________________________________________________________________  
		// Visualize the from part of a query
                for (int i = 0; i < sSelect.tFilter.length; i++) {
                    x = find_relation(sSelect.tFilter[i].getName()); 
                    if (x== null ){
                        x = find_relation(sSelect.tFilter[i].getTable().getName());
                    }
                    e = add_edge(u, x, HecataeusEdgeType.EDGE_TYPE_FROM, "from");
                }
                    
		// __________________________________________________________________________________________________  
		// Visualize the where part of a query
		if (sSelect.eCondition != null ) {
                        v = add_expression(sSelect.eCondition);
                        e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_WHERE, "where");
                }
                    
		// __________________________________________________________________________________________________  
		// Visualize the GROUP BY part of a query
		if ( sSelect.iGroupLen > 0 ) {
			// add GB node
			v = add_node("GB", HecataeusNodeType.NODE_TYPE_GROUP_BY) ;
			e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_GROUP_BY, "group by") ;

			int j = 0;
			// add edges
			for (int i = (sSelect.eColumn.length - sSelect.iGroupLen- sSelect.iOrderLen); i < (sSelect.eColumn.length - sSelect.iOrderLen); i++){
				j++;
				GroupByLabel = "group by " + j;
				x = add_expression(sSelect.eColumn[i]);
				e = add_edge(v, x, HecataeusEdgeType.EDGE_TYPE_GROUP_BY, GroupByLabel);
			}
		}
		// __________________________________________________________________________________________________  
		return u ;
	}
        
	private HecataeusEvolutionNode new_query(Select sSelect, boolean nested) throws SQLException{
		Expression expression;
		String GroupByLabel = null;
		// TableFilter ta
		HecataeusEvolutionNode u ;
		HecataeusEvolutionNode v = null ;
		HecataeusEvolutionNode w = null ;
		HecataeusEvolutionNode x = null ;
		HecataeusEvolutionEdge e ;
		HecataeusEvolutionEdge e2;
		int tblIndex = 0;
		// __________________________________________________________________________________________________  
		// check for self-join
		boolean SJQuery = false;
		int len = sSelect.tFilter.length;
		int[] SelfJoin = new int[len]  ; 
		SelfJoin = checkSelfJoin(sSelect);
		
		if ( SelfJoin != null ) {
                    SJQuery = true;
                    for (int cur = 0; cur < len; cur++) {
                        if ( SelfJoin[cur] != -1 ) {
                            String tblName = sSelect.tFilter[cur].getTable().getName();
                        }
                    }
                }
		// __________________________________________________________________________________________________  
		// resolve tables names for all attributes
		// mentioned in the SELECT and WHERE clause
		Resolve(sSelect, SJQuery);
		// __________________________________________________________________________________________________  
		
                queries++;
                // __________________________________________________________________________________________________  
               //Add the query node
                u = add_node("Q" + queries, HecataeusNodeType.NODE_TYPE_QUERY);
		// __________________________________________________________________________________________________  
		// Visualize the select part of a view
		// The following loop examines only the selected columns
                for (int i = 0; i < sSelect.eColumn.length - sSelect.iOrderLen - sSelect.iGroupLen; i++) {
                	expression = sSelect.eColumn[i];
                	// SHOULD ADD CHECK FOR ALIAS FOR SELF JOIN QUERIES
                	// Type COLUMN
                    if (expression.getType()== Expression.COLUMN) {
                        v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE) ;
                        e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                        x = find_attribute(expression.getTableName(), expression.getColumnName()) ;
                        e = add_edge(v,	x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;
                    }
                    
                    // Type VALUE
                    else if (expression.getType()== Expression.VALUE) {
                        try{
                        	if (expression.getAlias()==""){
                        		v = add_node(expression.getValue().toString(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE) ;
                            }else{
                        		v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE) ;
                        		HecataeusEvolutionNode v1 = add_node(expression.getValue().toString(), HecataeusNodeType.NODE_TYPE_CONSTANT) ;
                        		e = add_edge(v, v1, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;
                        	}
                        	e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                        }
                        catch (Exception ex){
                            System.out.println("[Expression Object in new_query]"+ ex.getMessage() );
                        }
                    }
                    // Type Function
                    else if (expression.getType()== Expression.FUNCTION) {
                    	//add a recursive method for nested functions
                    	
                    	Function function = expression.getFunction();
                    	if (expression.getAlias()== "")
                            v = add_node(function.getName(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                        else 
                            v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                        
                    	w = add_node(function.getName(), HecataeusNodeType.NODE_TYPE_FUNCTION) ;
                        e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                        e = add_edge(v, w, HecataeusEdgeType.EDGE_TYPE_MAPPING,"map-select");
                        
                        for (int j = 0; j <function.eArg.length ; j++) {
                        	HecataeusEvolutionNode arg =add_expression(function.eArg[j]);
                   			e = add_edge(w,	arg, HecataeusEdgeType.EDGE_TYPE_MAPPING, "op"+(j+1)) ;
                   		}
						
                    }
                    // Type COUNT (40), SUM (41), MIN (42), MAX (43), AVG (44)
                    else if  (expression.getType() == Expression.COUNT
                            || expression.getType() == Expression.SUM
                            || expression.getType() == Expression.MIN
                            || expression.getType() == Expression.MAX
                            || expression.getType() == Expression.AVG) {
                        String strFunction = "" ;
                        switch (expression.getType()){
                            case Expression.COUNT :  
                                strFunction = "COUNT";
                                break;
                            case Expression.SUM :
                                strFunction = "SUM";
                                break;
                            case Expression.MIN :
                                strFunction = "MIN";
                                break;
                            case Expression.MAX :
                                strFunction = "MAX";
                                break;
                            case Expression.AVG :
                                strFunction = "AVG";
                                break;  
                            default: break;
                        }
                        if (expression.getAlias()== "")
                            v = add_node(strFunction, HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                        else 
                            v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                        
                        w = add_node(strFunction, HecataeusNodeType.NODE_TYPE_FUNCTION) ;
                        e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                        e = add_edge(v, w, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;
                   
                        switch (expression.getArg().getType())
                        {
                            //if the aggregate function's argument is an asterix
                            case Expression.ASTERIX:
                                //if we have func(R.*)
                                if (expression.getArg().getTableName()!=null){
                                    //construct name of query.attribute node (i.e. from COUNT . COUNT(R.*))
                                	//only if no alias exist for this column
                                	if (expression.getAlias()== "")
                                		v.setName(v.getName() + "(" + expression.getArg().getTableName() + ".*)");
                                    
                                	HecataeusEvolutionNode x1 = find_relation(expression.getArg().getTableName());
                                    //if no alias relation node is found (i.e., no self join)
                                    if (x1 == null){ 
                                        //get real table
                                        for (int k = 0; k < sSelect.tFilter.length; k++)
                                        {
                                            if (expression.getArg().getTableName().equals(sSelect.tFilter[k].getName())){
                                                tblIndex = k;
                                                x1 = find_relation(sSelect.tFilter[i].getTable().getName());
                                            }                                                
                                        }
                                    }else{ 
                                        //get alias table
                                        for (int g = 0; g < sSelect.tFilter.length; g++)
                                        {
                                            if (expression.getArg().getTableName().equals(sSelect.tFilter[g].getName())){
                                                tblIndex = g;
                                            }                                                
                                        }
                                    }
                                    
                                    //convert func(R.*) it to graph
                                    //map only query attributes to R attributes
                                    for (int l = 0; l < sSelect.tFilter[tblIndex].getTable().getColumnCount(); l++){
                                        x = find_attribute(sSelect.tFilter[tblIndex].getName(),sSelect.tFilter[tblIndex].getTable().getColumnName(l));
                                        if (x == null){
                                            x = find_attribute(sSelect.tFilter[tblIndex].getTable().getName(),sSelect.tFilter[tblIndex].getTable().getColumnName(l));
                                        }
                                        e = add_edge(w, x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                                    }
                                }	
                                //if we have func(*)
                                else
                                {
                                    //construct name of query.attribute node (i.e. from COUNT . COUNT(*))
                                	//only if no alias exist for this column
                                	if (expression.getAlias()== "")
                                		v.setName(v.getName() + "(*)");
                                    for (int l = 0; l < sSelect.tFilter.length; l++){
                                        for (int j = 0; j < sSelect.tFilter[l].getTable().getColumnCount(); j++){
                                            x = find_attribute(sSelect.tFilter[l].getName(),sSelect.tFilter[l].getTable().getColumnName(j));
                                            if (x == null){
                                                x = find_attribute(sSelect.tFilter[l].getTable().getName(), sSelect.tFilter[l].getTable().getColumnName(j));
                                            }
                                            e = add_edge(w, x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                                        }
                                    }
                                }
                                break;
                                
                            //if the aggregate function's argument is a column
                            case Expression.COLUMN:
                                //construct name of query.attribute node (i.e. from COUNT . COUNT(attribute_name))			
                            	//only if no alias exist for this column
                            	if (expression.getAlias()== "")
                            		v.setName(v.getName() + "(" + expression.getArg().getColumnName() + ")");
                                
                            	x = find_attribute(expression.getArg().getTableName(),expression.getArg().getColumnName());
                                e = add_edge(w,	x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                                break;
                            
                            default : 
                            	x = add_expression(expression.getArg());
                            e = add_edge(w,	x, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select");
                            break;
                        
                        }
                    }
                    //if column is an expression other
                    else {
                    	if (expression.getAlias()== "")
                            v = add_node("", HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                        else 
                            v = add_node(expression.getAlias(), HecataeusNodeType.NODE_TYPE_ATTRIBUTE);
                        
                        e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_SCHEMA, "S") ;
                        w = add_expression(expression) ;
                        e = add_edge(v, w, HecataeusEdgeType.EDGE_TYPE_MAPPING, "map-select") ;
                                       
                                           	
                    }
                }
                
                // __________________________________________________________________________________________________  
		// Visualize the from part of a query
                for (int i = 0; i < sSelect.tFilter.length; i++) {
                    x = find_relation(sSelect.tFilter[i].getName()); 
                    if (x== null ){
                        x = find_relation(sSelect.tFilter[i].getTable().getName());
                    }
                    e = add_edge(u, x, HecataeusEdgeType.EDGE_TYPE_FROM, "from");
                }
                    
		// __________________________________________________________________________________________________  
		// Visualize the where part of a query
		if (sSelect.eCondition != null ) {
                        v = add_expression(sSelect.eCondition);
                        e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_WHERE, "where");
                }
                    
		// __________________________________________________________________________________________________  
		// Visualize the GROUP BY part of a query
		if ( sSelect.iGroupLen > 0 ) {
			// add GB node
			v = add_node("GB", HecataeusNodeType.NODE_TYPE_GROUP_BY) ;
			e = add_edge(u, v, HecataeusEdgeType.EDGE_TYPE_GROUP_BY, "group by") ;

			int j = 0;
			// add edges
			for (int i = (sSelect.eColumn.length - sSelect.iGroupLen- sSelect.iOrderLen); i < (sSelect.eColumn.length - sSelect.iOrderLen); i++){
				j++;
				GroupByLabel = "group by " + j;
				x = add_expression(sSelect.eColumn[i]);
				e = add_edge(v, x, HecataeusEdgeType.EDGE_TYPE_GROUP_BY, GroupByLabel);
			}
		}
		// __________________________________________________________________________________________________  
		return u ;
        }
        

	
	private HecataeusEvolutionNode  add_expression(Expression expr){
		
		HecataeusEvolutionNode u=null;
		
		switch (expr.getType()){
               case Expression.NOT:
                   u = add_node(" != ", HecataeusNodeType.NODE_TYPE_OPERAND);
                   break;
                   
               case Expression.EQUAL:
            	   u =  add_node(" = ", HecataeusNodeType.NODE_TYPE_OPERAND);		
            	   break;
               case Expression.BIGGER_EQUAL:
            	   u =  add_node(" >= ", HecataeusNodeType.NODE_TYPE_OPERAND);
            	   break;
               case Expression.BIGGER:
            	   u =  add_node(" > ", HecataeusNodeType.NODE_TYPE_OPERAND);
            	   break;
               case Expression.SMALLER:
            	   u = add_node(" < ", HecataeusNodeType.NODE_TYPE_OPERAND);
            	   break;
               case Expression.SMALLER_EQUAL:
            	   u =  add_node(" <= ", HecataeusNodeType.NODE_TYPE_OPERAND);
            	   break;
               case Expression.NOT_EQUAL:
            	   u =  add_node(" <> ", HecataeusNodeType.NODE_TYPE_OPERAND);
            	   break;
               case Expression.VALUELIST:
            	   u =  add_node(" ValueList ", HecataeusNodeType.NODE_TYPE_CONSTANT);
            	   break;
               case Expression.VALUE:
                   try{
                	   if (expr.getValue()==null){
                		   u =  add_node("NULL", HecataeusNodeType.NODE_TYPE_CONSTANT);
                       }else {
                    	   u =  add_node(expr.getValue().toString(), HecataeusNodeType.NODE_TYPE_CONSTANT);
                       }
                	   break;
                   }
                   catch (Exception ex){
                   System.out.println("[Expression Object in add_expression]"+ ex.getMessage() );
                   }
                   
               case Expression.COLUMN:
            	   u =  find_attribute(expr.getTableName(), expr.getColumnName());
            	   break;
               case Expression.IN:
            	   u =  add_node(" IN ", HecataeusNodeType.NODE_TYPE_OPERAND);
            	   break;
                case Expression.EXISTS:
                	u =  add_node(" EXISTS ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.AND:
                	u =  add_node(" AND ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.OR:
                	u =  add_node(" OR ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.CONCAT:
                	u =  add_node(" || ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.ADD:
                	u =  add_node(" + ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.SUBTRACT:
                	u =  add_node(" - ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.MULTIPLY:
                	u =  add_node(" * ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.DIVIDE:
                	u =  add_node(" / ", HecataeusNodeType.NODE_TYPE_OPERAND);
                	break;
                case Expression.CASEWHEN:
                	u =  add_node(" CASEWHEN ", HecataeusNodeType.NODE_TYPE_FUNCTION);
                	break;
               case Expression.QUERY:
            	   try {
            		   u =  new_query(expr.sSelect, true);
            		   break;
            	   } catch (Exception e) {
            		   System.out.println("[Error in subquery]"+e.getMessage());
            	   }
               case Expression.FUNCTION:
            	// Type Function
            	   	Function function = expr.getFunction();
               		u = add_node(function.getName(), HecataeusNodeType.NODE_TYPE_FUNCTION) ;
               		for (int j = 0; j <function.eArg.length ; j++) {
               			HecataeusEvolutionNode arg =add_expression(function.eArg[j]);
               			HecataeusEvolutionEdge e = add_edge(u,	arg, HecataeusEdgeType.EDGE_TYPE_OPERATOR, "op"+(j+1)) ;
               		}
               		break;   		               	
				default:
                    break ;                  
           }
		
		HecataeusEvolutionEdge newedge ;
		HecataeusEvolutionNode arg ;
		if (expr.getArg()!=null) {
			arg = add_expression(expr.getArg()) ;
			if (arg!=null)
				newedge = add_edge(u, arg, HecataeusEdgeType.EDGE_TYPE_OPERATOR,"op1") ;
		}
		if (expr.getArg2()!=null) {
			arg = add_expression(expr.getArg2()) ;
			if (arg!=null)
				newedge = add_edge(u, arg, HecataeusEdgeType.EDGE_TYPE_OPERATOR,"op2") ;
		}
		
    	return u;
	}

	private HecataeusEvolutionNode find_relation(String relation) {
		HecataeusEvolutionNode v ;
		v = HGraph.getNodeByName(relation);
		if ( v != null ) {
			return v;
		}
		v = HGraph.getNodeByName(relation);
		return v ;
	}

	private void CopyTable(String tblName, String tblAlias) {
		HecataeusEvolutionNode v ;
		v = HGraph.createTableAlias(find_relation(tblName), tblAlias);
	}

	private int[] checkSelfJoin(Select sSelect) {
		int len = sSelect.tFilter.length;
		int[] tblCount = new int[len] ; // dim statement
		boolean SelfJoin = false;
		int copies = 0;
		for (int i = 0; i < len; i++) {
                    tblCount[i] = 0;
                }
		
                // Count how many times a table appears in the tFilter
                for (int i = 0; i < len; i++) {
                    for (int j = 0; j < len;  j++) {
                        if (sSelect.tFilter[i].getTable().equals(sSelect.tFilter[j].getTable()))
                            tblCount[i]++;
                    }
                }
		
                // Count the same table only once
		for (int i = 0; i < len; i++) {
			for (int j = i+1; j < len; j++) {
				if (sSelect.tFilter[i].getTable().equals(sSelect.tFilter[j].getTable())){
                                    tblCount[j] = 1;
                                    SelfJoin = true;                    
                                }
                        }
                }
                
		if ( !(SelfJoin) ) {
			return null;
		}
                
		// count number of copies
		// (multiple self-joins?)
		for (int i = 0; i < len;  i++) {
                    if ( tblCount[ i ] > 1 ) 
                        copies += tblCount[ i ];
                }
		
                // copy the tables
		int currentcopy = 1;
                for (int i = 0; i < len; i++) {
                    if ( tblCount[ i ] > 1 ) {
                        CopyTable(sSelect.tFilter[i].getTable().getName(),sSelect.tFilter[i].getName());
                        currentcopy++;
                        for (int j = i+1; j < len; j++) {
                            if ( sSelect.tFilter[i].getTable().equals(sSelect.tFilter[j].getTable())) {
                                CopyTable(sSelect.tFilter[j].getTable().getName(),sSelect.tFilter[j].getName());
                                currentcopy++;
                            }
                        }
                    }
                }
                return tblCount;
        }

        private void Resolve(Select sSelect, TableFilter f, Expression expr, boolean SelfJoinQuery) {
            if ( f != null && expr.getType()== Expression.COLUMN ) {
                if (expr.sTable == null || f.getName().equals(expr.sTable)) {
                    int i = f.getTable().searchColumn(expr.sColumn);
                    if (i != -1 && SelfJoinQuery ) {
                        expr.tFilter = f;
                        expr.iColumn = i;
                        expr.sTable= f.getName();
                        expr.iDataType = f.getTable().getColumnType(i);
                    } else if (i != -1 && !(SelfJoinQuery)){
                        expr.tFilter = f;
                        expr.iColumn = i;
                        expr.sTable = f.getTable().getName();
                        expr.iDataType = f.getTable().getColumnType(i);
                    }
                }
            }
            if ( f != null && expr.getType()== Expression.QUERY ) {
            	Resolve(expr.sSelect, f, SelfJoinQuery);
            }
            
            if ( f != null && expr.getType()== Expression.FUNCTION ) {
            	Function function = expr.getFunction();
            	for (int j = 0; j <function.eArg.length ; j++) {
            		if (function.eArg[j]!=null){
            			Resolve(sSelect, f, function.eArg[j], SelfJoinQuery);	
            		}	
            	}
            }
            if ( expr.eArg!= null ) {
                Resolve(sSelect, f, expr.eArg, SelfJoinQuery);
            }
            if ( expr.eArg2 != null ) {
                Resolve(sSelect, f, expr.eArg2, SelfJoinQuery);
            }
        }

	private void Resolve(Select sSelect, TableFilter f, boolean SelfJoinQuery) {
            if (sSelect.eCondition!= null ) {
                //  first set the table filter in the condition
                Resolve(sSelect, f, sSelect.eCondition, SelfJoinQuery);                
            }
            int len = sSelect.eColumn.length;
            for (int i = 0; i < len;  i++) {
                Resolve(sSelect, f, sSelect.eColumn[i], SelfJoinQuery);
            }
        }

	private void Resolve(Select sSelect, boolean SelfJoinQuery) {
            int len = sSelect.tFilter.length;
            for (int i = 0; i < len; i++) {
                Resolve(sSelect, sSelect.tFilter[i], SelfJoinQuery) ;
            }
        }
}