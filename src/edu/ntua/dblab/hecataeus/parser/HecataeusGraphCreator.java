/**
 * @author George Papastefanatos, National Technical University of Athens
 * 
 */
package edu.ntua.dblab.hecataeus.parser;

import java.sql.SQLException;
import java.util.Vector;

import edu.ntua.dblab.hecataeus.graph.evolution.*;
import edu.ntua.dblab.hecataeus.graph.visual.VisualEdge;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;
import edu.ntua.dblab.hecataeus.hsql.*; 
import edu.ntua.dblab.hecataeus.parser.PlainSQL.SpecificOperator;

/**
 * @author  gpapas
 */

public class HecataeusGraphCreator{

	private static int queries;
	private static int anon_blocks;			//
	private static int scripts;				//	
	private static int inserts;				//
	private static int deletes;				//added by sgerag
	private static int updates;				//
	private static int assignments;			//
	private static int embeddeds;			//
	private static int mergeIntos;			//
	
	private static int line;				//
	private static String path;				//added by sgerag
	
	VisualGraph HGraph;

	HecataeusGraphCreator() {
		queries=0;
		inserts=0;						//
		anon_blocks=0;					//
		scripts=0;						//
		deletes=0;						//added by sgerag
		updates=0;						//
		assignments=0;					//
		embeddeds=0;
		line=0;							//
		path="";						//
		HGraph =  new VisualGraph();
	}

	HecataeusGraphCreator(VisualGraph graph) {
		HGraph = graph ;
	}

	private VisualNode add_node(String Label, NodeType Type) {
		VisualNode v = new VisualNode(Label, Type);
		v.setPath(path);														//added by sgerag
		v.setLine(line);
		HGraph.addVertex(v);
		return v;
	}

	private VisualEdge add_edge( VisualNode u, VisualNode  v, EdgeType Type, String Label) {
		VisualEdge e = new VisualEdge(Label, Type, u, v);
		HGraph.addEdge(e);
		return  e ;
	}

	//sgerag modification: return type (boolean--->HecataeusEvolutionNode)
	public VisualNode add_table(Table tTable, String definition) throws SQLException {
		try {
			VisualNode u  ;
			VisualNode v  ;
			VisualEdge e  ;
			// Add the relation node
			u = add_node(tTable.getName(), NodeType.NODE_TYPE_RELATION);
			u.setSQLDefinition(definition);
			// Add the attribute nodes
			for (int i = 0; i < tTable.getColumnCount();i++) {
				// Add new attribute node and a new edge with the relation node
				v = add_node(tTable.getColumnName(i), NodeType.NODE_TYPE_ATTRIBUTE);
				e = add_edge(u, v, EdgeType.EDGE_TYPE_SCHEMA, "S");
			}
			addTableConstraints(tTable);
			return u;								//added by sgerag
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	private void addTableConstraints(Table  tTable) {
		try { 
			// Add constraints
			for (int i=0;i<tTable.getConstraints().size();i++){
				Constraint c = (Constraint) tTable.getConstraints().get(i);
				VisualNode w =  addConstraintNode(tTable, c);
			}
		}
		catch(Exception e)
		{    //no constraints have been created    
			System.out.println(e.getMessage().toString());                   
		}                
	}


	private  VisualNode addConstraintNode(Table tTable, Constraint cConstraint){
		VisualNode u;
		int cols[] ; 
		if (cConstraint.getType() == Constraint.PRIMARY_KEY) {
			//add constraint node
			u = add_node(tTable.getName() + ".PK",NodeType.NODE_TYPE_CONDITION);
			//for each column in constraint node create edges
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				VisualNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				VisualEdge e = add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op");
			}
			//return constraint node
			return u;
		} else if (cConstraint.getType()==Constraint.UNIQUE)  {
			//add constraint node
			u = add_node(tTable.getName() + ".UC",NodeType.NODE_TYPE_CONDITION);
			//for each column in constraint node create edges
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				VisualNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				VisualEdge e = add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op");
			}
			//return constraint node
			return u;
		} else if (cConstraint.getType()==Constraint.NOT_NULL) {
			//add constraint node
			u = add_node(tTable.getName() + ".NC",NodeType.NODE_TYPE_CONDITION);
			//for each column in constraint node create edges
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				VisualNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				VisualEdge e = add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op");
			}
			//return constraint node
			return u;
		} else if (cConstraint.getType()==Constraint.FOREIGN_KEY) {
			//add constraint node
			u = add_node(tTable.getName() +  ".FK", NodeType.NODE_TYPE_CONDITION);
			//for each column in detail table create edges from attributes nodes to fk node
			tTable = cConstraint.getRef();
			cols = cConstraint.getRefColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				VisualNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				//from attribute nodes (v) -->constraint node(u) 
				VisualEdge e = add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op" + (i+1));
			}

			//for each column in master table create edges from fk node to attributes nodes
			tTable = cConstraint.getMain();
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				VisualNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				//from constraint node(u) --> attribute nodes (v)
				VisualEdge e = add_edge(u, v, EdgeType.EDGE_TYPE_OPERATOR, "op" + (i+1));
			}
			//return constraint node
			return u ;
		}
		else { //other constraints are not captured
			return null;
		}
	}

	private VisualNode  find_attribute(String TableName, String Attribute) {
		VisualNode u ;
		u = HGraph.getAttributeNode(TableName, Attribute);
		return u ;
	}

	private VisualNode dfind_attribute(Select sSelect, String attribute) {
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
	
	public boolean add_query(Select sSelect, String definition) throws SQLException{
		try {
			queries++;
			VisualNode   u = add_node("Q" + queries, NodeType.NODE_TYPE_QUERY);
			this.createQuery(u, sSelect, false);
			u.setSQLDefinition(definition);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return true;
	}

	public boolean add_view(Select sSelect, String view_name, String definition) throws SQLException{
		try {
			VisualNode u = add_node(view_name, NodeType.NODE_TYPE_VIEW);
			this.createQuery(u, sSelect, false);
			u.setSQLDefinition(definition);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return true;
	}

	/**
	 * adds a block node in the graph
	 * @author Stefanos Geraggelos
	 * @param block
	 * @param fileNode
	 * @exception SQLException
	 */
	private void addBlock(Block block,VisualNode fileNode) throws SQLException{
		try{
			VisualNode blockNode=null;
			boolean emptyBlock=true;
			
			if (block instanceof AnonymousBlock){
				anon_blocks++;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node("ANONYMOUS_BLOCK_"+anon_blocks,NodeType.NODE_TYPE_ANONYMOUS_BLOCK);
					emptyBlock=false;
				}
			}
			else if (block instanceof Script){
				scripts++;					
				line=block.getLine();
			}
			else if (block instanceof StoredProcedure){
				StoredProcedure proc=(StoredProcedure)block;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node(proc.getName(),NodeType.NODE_TYPE_STORED_PROCEDURE);
					emptyBlock=false;
				}
			}
			else if (block instanceof StoredFunction){
				StoredFunction func=(StoredFunction)block;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node(func.getName(),NodeType.NODE_TYPE_STORED_FUNCTION);
					emptyBlock=false;
				}
			}
			else if (block instanceof Trigger){
				Trigger trig=(Trigger)block;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node(trig.getName(),NodeType.NODE_TYPE_TRIGGER);
					emptyBlock=false;
				}
			}
			else if (block instanceof Package){
				Package pack=(Package)block;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){	
					blockNode=add_node(pack.getName(),NodeType.NODE_TYPE_PACKAGE);
					emptyBlock=false;
				}
			}
			else if (block instanceof EmbeddedStatement){
				embeddeds++;
				EmbeddedStatement emb=(EmbeddedStatement)block;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node("EMBEDDED_"+embeddeds,NodeType.NODE_TYPE_EMBEDDED_STATEMENT);
					emptyBlock=false;
				}
			}
			
			//visualize nested blocks
			Vector<Block> blocks=block.getBlocks();
			for (Block bl:blocks){
				if (!(block instanceof Script))		addBlock(bl,blockNode);
				else	addBlock(bl,fileNode);
			}
			
			if (!(block instanceof Script) && !emptyBlock){
				add_edge(fileNode,blockNode,EdgeType.EDGE_TYPE_CONTAINS,"contains");
			}
			
			Vector<Statement> stmts=block.getStatements();
			
			for (Statement nod:stmts){
				VisualNode stmtNode=new VisualNode();
				if (nod instanceof Relation){
					Relation rel=((Relation)nod);
					line=rel.getLine();
					
					stmtNode=this.add_table(rel.getTable(),rel.getDefinition());
				}
				else if (nod instanceof View){
					View view=((View)nod);
					line=view.getLine();
					
					stmtNode=add_node(view.getName(), NodeType.NODE_TYPE_VIEW);
					this.createQuery(stmtNode,view.getSelect(),false);
					stmtNode.setSQLDefinition(view.getDefinition());
				}
				else if (nod instanceof PlainSQL){
					PlainSQL plain=((PlainSQL)nod);
					line=plain.getLine();
					
					if (plain.getOperator()==SpecificOperator.SELECT){
						queries++;
						
						if (plain.isSelectInto()) stmtNode=add_node(plain.getName(),NodeType.NODE_TYPE_QUERY);
						else	stmtNode=add_node("Q"+queries, NodeType.NODE_TYPE_QUERY);
						this.createQuery(stmtNode,plain.getSelect(),false);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
					else if (plain.getOperator()==SpecificOperator.INSERT){
						inserts++;
						stmtNode=add_node("I"+inserts,NodeType.NODE_TYPE_INSERT);
						this.createQuery(stmtNode,plain.getSelect(),false);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
					else if (plain.getOperator()==SpecificOperator.DELETE){
						deletes++;
						stmtNode=add_node("D"+deletes,NodeType.NODE_TYPE_DELETE);
						this.createQuery(stmtNode,plain.getSelect(),false);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
					else if (plain.getOperator()==SpecificOperator.UPDATE){
						updates++;
						stmtNode=add_node("U"+updates,NodeType.NODE_TYPE_UPDATE);
						this.createQuery(stmtNode,plain.getSelect(),false);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
				}
				else if (nod instanceof Cursor){
					Cursor cur=((Cursor)nod);
					line=cur.getLine();
					
					if (cur.getId()==0){
						//static cursor
						stmtNode=add_node(cur.getName(),NodeType.NODE_TYPE_CURSOR);
					}
					else {
						//dynamic cursor
						stmtNode=add_node(cur.getName()+"_"+cur.getId(),NodeType.NODE_TYPE_CURSOR);
					}
					this.createQuery(stmtNode,cur.getSelect(),false);
					stmtNode.setSQLDefinition(cur.getDefinition());
				}
				else if (nod instanceof Variable){
					Variable var=((Variable)nod);
					line=var.getLine();
					
					stmtNode=add_node(var.getName(),NodeType.NODE_TYPE_VARIABLE);
					this.createQuery(stmtNode,var.getSelect(),false);
					stmtNode.setSQLDefinition(var.getDefinition());
				}
				else if (nod instanceof Assignment){
					assignments++;
					Assignment assig=((Assignment)nod);
					line=assig.getLine();
					
					stmtNode=add_node("A"+assignments,NodeType.NODE_TYPE_ASSIGNMENT);
					this.createQuery(stmtNode,assig.getSelect(),false);
					stmtNode.setSQLDefinition(assig.getDefinition());
				}
				else if (nod instanceof MergeInto){
					mergeIntos++;
					MergeInto merge = (MergeInto) nod;
					line = merge.getLine();
					
//					stmtNode=add_node("Merge_"+mergeIntos,NodeType.NODE_TYPE_MERGE_INTO);
//					this.createQuery(stmtNode,merge.getSelect(),false);
//					stmtNode.setSQLDefinition(plain.getDefinition());
					
				}
				
				if (block instanceof Script){
					add_edge(fileNode,stmtNode,EdgeType.EDGE_TYPE_CONTAINS,"contains");
				}
				else {
					add_edge(blockNode,stmtNode,EdgeType.EDGE_TYPE_CONTAINS,"contains");
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
			throw new SQLException();
		}	
	}
	
	
	/**
	 * adds a file node in the graph
	 * @author Stefanos Geraggelos
	 * @param plain
	 * @exception SQLException
	 */
	public boolean addFile(FileContainer file) throws SQLException{
		try {
			path=file.getPath();
			
			VisualNode fileNode=null;
			
			Vector<Block> blcks=file.getBlocks();
			if (!blcks.isEmpty())	fileNode=add_node(file.getName(),NodeType.NODE_TYPE_FILE);
			
			for (Block block:blcks){
				addBlock(block,fileNode);
			}

		}catch (Exception e){
			e.printStackTrace();
			throw new SQLException();
		}
		
		return true;
	}
	
	
	/**
	 * checks if all of the children's of block node are empty 
	 * @author Stefanos Geraggelos
	 * @param blockNode
	 * @exception SQLException
	 */
	private boolean blockHasEmptyChildren(Block blockNode){
		boolean hasEmpty=true;
		
		Vector<Block> blChilds=blockNode.getBlocks();
		
		for (Block bl:blChilds){
			hasEmpty=blockHasEmptyChildren(bl);
		}
		
		Vector<Statement> stChilds=blockNode.getStatements();
		
		if (!stChilds.isEmpty())	return false;
		
		return hasEmpty;
	}
	
	private void createQuery(VisualNode u, Select sSelect, boolean nested) throws SQLException{

		Expression expression;
		String GroupByLabel = null;
		// TableFilter ta
		VisualNode v = null ;
		VisualNode w = null ;
		VisualNode x = null ;
		VisualEdge e ;
		VisualEdge e2;
		int tblIndex = 0;
		// __________________________________________________________________________________________________  
		// check for self-join
		boolean SJQuery = false;
		int len = sSelect.tFilter.length;
		String[] selfJoinedTables = new String[len]  ; // dim statement
		selfJoinedTables = checkSelfJoin(sSelect);
		// __________________________________________________________________________________________________  
		// resolve tables names for all attributes
		// mentioned in the SELECT and WHERE clause
		Resolve(sSelect, selfJoinedTables);

		// Visualize the select part  
		// The following loop examines only the selected columns
		for (int i = 0; i < sSelect.eColumn.length - sSelect.iOrderLen - sSelect.iGroupLen; i++) {
			expression = sSelect.eColumn[i];
			// SHOULD ADD CHECK FOR ALIAS FOR SELF JOIN QUERIES
			// Type COLUMN
			if (expression.getType()== Expression.COLUMN) {
				v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE) ;
				e = add_edge(u, v, EdgeType.EDGE_TYPE_SCHEMA, "S") ;
//				VisualNode v1 = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE) ;
//				VisualEdge e1 = add_edge(u, v1, EdgeType.EDGE_TYPE_SCHEMA, "outS") ;
				x = find_attribute(expression.getTableName(), expression.getColumnName()) ;
				e = add_edge(v,	x, EdgeType.EDGE_TYPE_MAPPING, "map-select") ;
			}
			// Type VALUE
			if (expression.getType()== Expression.VALUE) {
				try{
					if (expression.getAlias()==""){
						v = add_node(expression.getValue().toString(), NodeType.NODE_TYPE_ATTRIBUTE) ;
					}else{
						v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE) ;
						VisualNode v1 = add_node(expression.getValue().toString(), NodeType.NODE_TYPE_CONSTANT) ;
						e = add_edge(v, v1, EdgeType.EDGE_TYPE_MAPPING, "map-select") ;
					}
					e = add_edge(u, v, EdgeType.EDGE_TYPE_SCHEMA, "S") ;
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
					v = add_node(function.getName(), NodeType.NODE_TYPE_ATTRIBUTE);
				else 
					v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE);

				w = add_node(function.getName(), NodeType.NODE_TYPE_FUNCTION) ;
				e = add_edge(u, v, EdgeType.EDGE_TYPE_SCHEMA, "S") ;
				e = add_edge(v, w, EdgeType.EDGE_TYPE_MAPPING,"map-select");

				for (int j = 0; j <function.eArg.length ; j++) {
					VisualNode arg =add_expression(function.eArg[j]);
					e = add_edge(w,	arg, EdgeType.EDGE_TYPE_MAPPING, "op"+(j+1)) ;
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
					v = add_node(strFunction, NodeType.NODE_TYPE_ATTRIBUTE);
				else 
					v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE);

				w = add_node(strFunction, NodeType.NODE_TYPE_FUNCTION) ;
				e = add_edge(u, v, EdgeType.EDGE_TYPE_SCHEMA, "S") ;
				e = add_edge(v, w, EdgeType.EDGE_TYPE_MAPPING, "map-select") ;

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
						VisualNode x1 = find_relation(expression.getArg().getTableName());
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
							e = add_edge(w, x, EdgeType.EDGE_TYPE_MAPPING, "map-select");
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
								e = add_edge(w, x, EdgeType.EDGE_TYPE_MAPPING, "map-select");
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
					e = add_edge(w,	x, EdgeType.EDGE_TYPE_MAPPING, "map-select");
					break;

				default : 
					x = add_expression(expression.getArg());
					e = add_edge(w,	x, EdgeType.EDGE_TYPE_MAPPING, "map-select");
					break;
				}
			}
		}
		// __________________________________________________________________________________________________  
		// Visualize the from part of a query
		for (int i = 0; i < sSelect.tFilter.length; i++) {
			if ( selfJoinedTables[i] != null) 
				x = find_relation(sSelect.tFilter[i].getName());
			else
				x = find_relation(sSelect.tFilter[i].getTable().getName());

			e = add_edge(u, x, EdgeType.EDGE_TYPE_FROM, "from");
		}

		// __________________________________________________________________________________________________  
		// Visualize the where part of a query
		if (sSelect.eCondition != null ) {
			v = add_expression(sSelect.eCondition);
			e = add_edge(u, v, EdgeType.EDGE_TYPE_WHERE, "where");
		}

		// __________________________________________________________________________________________________  
		// Visualize the GROUP BY part of a query
		if ( sSelect.iGroupLen > 0 ) {
			// add GB node
			v = add_node("GB", NodeType.NODE_TYPE_GROUP_BY) ;
			e = add_edge(u, v, EdgeType.EDGE_TYPE_GROUP_BY, "group by") ;

			int j = 0;
			// add edges
			for (int i = (sSelect.eColumn.length - sSelect.iGroupLen- sSelect.iOrderLen); i < (sSelect.eColumn.length - sSelect.iOrderLen); i++){
				j++;
				GroupByLabel = "group by " + j;
				x = add_expression(sSelect.eColumn[i]);
				e = add_edge(v, x, EdgeType.EDGE_TYPE_GROUP_BY, GroupByLabel);
			}
		}
	}
	private VisualNode  add_expression(Expression expr){

		VisualNode u=null;

		switch (expr.getType()){
		case Expression.NOT:
			u = add_node(" != ", NodeType.NODE_TYPE_OPERAND);
			break;

		case Expression.EQUAL:
			u =  add_node(" = ", NodeType.NODE_TYPE_OPERAND);		
			break;
		case Expression.BIGGER_EQUAL:
			u =  add_node(" >= ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.BIGGER:
			u =  add_node(" > ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.SMALLER:
			u = add_node(" < ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.SMALLER_EQUAL:
			u =  add_node(" <= ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.NOT_EQUAL:
			u =  add_node(" <> ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.VALUELIST:
			u =  add_node(" ValueList ", NodeType.NODE_TYPE_CONSTANT);
			break;
		case Expression.VALUE:
			try{
				if (expr.getValue()==null){
					u =  add_node("NULL", NodeType.NODE_TYPE_CONSTANT);
				}else {
					u =  add_node(expr.getValue().toString(), NodeType.NODE_TYPE_CONSTANT);
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
			u =  add_node(" IN ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.EXISTS:
			u =  add_node(" EXISTS ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.AND:
			u =  add_node(" AND ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.OR:
			u =  add_node(" OR ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.CONCAT:
			u =  add_node(" || ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.ADD:
			u =  add_node(" + ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.SUBTRACT:
			u =  add_node(" - ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.MULTIPLY:
			u =  add_node(" * ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.DIVIDE:
			u =  add_node(" / ", NodeType.NODE_TYPE_OPERAND);
			break;
		case Expression.CASEWHEN:
			u =  add_node(" CASEWHEN ", NodeType.NODE_TYPE_FUNCTION);
			break;
		case Expression.QUERY:
			try {
				queries++;
				u = add_node("Q" + queries, NodeType.NODE_TYPE_QUERY);
				this.createQuery(u, expr.sSelect, true);
				break;
			} catch (Exception e) {
				System.out.println("[Error in subquery]"+e.getMessage());
			}
		case Expression.FUNCTION:
			// Type Function
			Function function = expr.getFunction();
			u = add_node(function.getName(), NodeType.NODE_TYPE_FUNCTION) ;
			for (int j = 0; j <function.eArg.length ; j++) {
				VisualNode arg =add_expression(function.eArg[j]);
				VisualEdge e = add_edge(u,	arg, EdgeType.EDGE_TYPE_OPERATOR, "op"+(j+1)) ;
			}
			break;   		               	
		default:
			break ;                  
		}

		VisualEdge newedge ;
		VisualNode arg ;
		if (expr.getArg()!=null) {
			arg = add_expression(expr.getArg()) ;
			if (arg!=null)
				newedge = add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR,"op1") ;
		}
		if (expr.getArg2()!=null) {
			arg = add_expression(expr.getArg2()) ;
			if (arg!=null)
				newedge = add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR,"op2") ;
		}

		return u;
	}

	private VisualNode find_relation(String relation) {
		VisualNode v ;
		v = HGraph.findVertexByName(relation);
		if ( v != null ) {
			return v;
		}
		v = HGraph.findVertexByName(relation);
		return v ;
	}

	private void CopyTable(String tblName, String tblAlias) {
		//get src relation
		VisualNode srcTable = find_relation(tblName) ;
		//create table alias node
		VisualNode aliasTable =  new VisualNode(tblAlias, NodeType.NODE_TYPE_RELATION);
		// add node to graph
		HGraph.addVertex(aliasTable);
		aliasTable.setSQLDefinition(srcTable.getSQLDefinition());
		// create edge for alias
		this.add_edge(aliasTable, srcTable, EdgeType.EDGE_TYPE_ALIAS, "alias");
		//  for each attribute in source table create new attribute nodes
		for (EvolutionEdge edgeAttribute: srcTable.getOutEdges()) {
			if (edgeAttribute.getType()==EdgeType.EDGE_TYPE_SCHEMA)  {
				VisualNode aliasAttr =  new VisualNode(edgeAttribute.getToNode().getName(),NodeType.NODE_TYPE_ATTRIBUTE);
				HGraph.addVertex(aliasAttr);
				this.add_edge(aliasTable, aliasAttr, EdgeType.EDGE_TYPE_SCHEMA,"S");
				//if map select is added between the alias attributes and the source attributes
				this.add_edge(aliasAttr, (VisualNode) edgeAttribute.getToNode(),EdgeType.EDGE_TYPE_MAPPING,"map-select" );
			}
		}
	}


	private String[] checkSelfJoin(Select sSelect) {
		int len = sSelect.tFilter.length;
		String[] tblNames = new String[len];

		// Count the same table only once
		for (int i = 0; i < len; i++) {
			for (int j = i+1; j < len; j++) {
				if (sSelect.tFilter[i].getTable().equals(sSelect.tFilter[j].getTable())){
					tblNames[i] = sSelect.tFilter[i].getName();
					tblNames[j] = sSelect.tFilter[j].getName();
				}
			}
		}

		for (int i = 0; i < len; i++) {
			if (tblNames[i]!=null)
				CopyTable(sSelect.tFilter[i].getTable().getName(),tblNames[i]);
		}
		return tblNames;
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

	private void Resolve(Select sSelect,String[] selfJoinedTables) {
		int len = sSelect.tFilter.length;
		for (int i = 0; i < len; i++) {
			if ( selfJoinedTables[i] != null) 
				Resolve(sSelect, sSelect.tFilter[i], true);
			else
				Resolve(sSelect, sSelect.tFilter[i], false) ;
		}
	}


}