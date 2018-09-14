/**
 * @author George Papastefanatos, National Technical University of Athens
 * 
 */
package edu.ntua.dblab.hecataeus.parser;

import java.io.File;
import java.sql.SQLException;
import java.util.Vector;

import edu.ntua.dblab.hecataeus.graph.evolution.EdgeType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.ntua.dblab.hecataeus.hsql.Constraint;
import edu.ntua.dblab.hecataeus.hsql.Expression;
import edu.ntua.dblab.hecataeus.hsql.Function;
import edu.ntua.dblab.hecataeus.hsql.Select;
import edu.ntua.dblab.hecataeus.hsql.Table;
import edu.ntua.dblab.hecataeus.hsql.TableFilter;
import edu.ntua.dblab.hecataeus.parser.PlainSQL.SpecificOperator;

/**
 * @author  gpapas
 */

public class HecataeusGraphCreator{

	private static int queries = 0;
	private static int anon_blocks = 0;
	private static int inserts = 0;
	private static int deletes = 0;
	private static int updates = 0;
	private static int assignments = 0;
	private static int embeddeds = 0;
	private static int line = 0;
	private static String path = "";

	private EvolutionGraph evoGraph;

	HecataeusGraphCreator() {
		evoGraph = new EvolutionGraph();
	}

	HecataeusGraphCreator(EvolutionGraph graph) {
		evoGraph = graph;
	}

	/**
	 * adds a file node in the graph
	 * 
	 * @author Stefanos Geraggelos
	 * @param plain
	 * @exception SQLException
	 */
	public boolean addFile(FileContainer file, File fileName) throws SQLException {
		try {
			path = file.getPath();
			EvolutionNode fileNode = null;
			Vector<Block> blcks = file.getBlocks();
			if (!blcks.isEmpty())
				fileNode = add_node(file.getName(), NodeType.NODE_TYPE_FILE, fileName);
			for (Block block : blcks) {
				addBlock(block, fileNode, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return true;
	}

	private EvolutionNode add_table(Table tTable, String definition, File fileName) throws SQLException {
		try {
			EvolutionNode u;
			EvolutionNode v;
			// Add the relation node
			u = add_node(tTable.getName(), NodeType.NODE_TYPE_RELATION, fileName);
			u.setSQLDefinition(definition);
			/**
			* @author pmanousi start
			* Make a new node called tableName+" OUTPUT" and my table hits this node, later attributes will hit outputNode.
			*/
			EvolutionNode outputNode =
				add_node(u.getName() + "_SCHEMA", NodeType.NODE_TYPE_OUTPUT, fileName);
			add_edge(u, outputNode, EdgeType.EDGE_TYPE_OUTPUT, "OUT_S");
			// Add the attribute nodes
			for (int i = 0; i < tTable.getColumnCount();i++) {
				// Add new attribute node and a new edge with the relation node
				v = add_node(tTable.getColumnName(i), NodeType.NODE_TYPE_ATTRIBUTE, fileName);
				add_edge(outputNode, v, EdgeType.EDGE_TYPE_SCHEMA, "S");
			}
			addTableConstraints(tTable, fileName);
			return u;								//added by sgerag
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	private void addTableConstraints(Table  tTable, File fileName) {
		try { 
			// Add constraints
			for (int i=0;i<tTable.getConstraints().size();i++){
				Constraint c = (Constraint) tTable.getConstraints().get(i);
				addConstraintNode(tTable, c, fileName);
			}
		}
		catch(Exception e)
		{    //no constraints have been created    
			System.out.println(e.getMessage().toString());                   
		}                
	}

	private EvolutionNode add_node(String name, NodeType Type, File fName) {
		EvolutionNode v = new EvolutionNode(name, Type, fName);
		v.setPath(path);
		v.setLine(line);
		getEvolutionGraph().addVertex(v);
		return v;
	}

	private EvolutionEdge add_edge(EvolutionNode u,
		EvolutionNode v,
		EdgeType Type,
		String name) {

		EvolutionEdge e = new EvolutionEdge(name, Type, u, v);
		getEvolutionGraph().addEdge(e);
		return e;
	}

	private EvolutionNode addConstraintNode(Table tTable, Constraint cConstraint, File fileName) {
		EvolutionNode u;
		int cols[] ; 
		if (cConstraint.getType() == Constraint.PRIMARY_KEY) {
			//add constraint node
			u = add_node(tTable.getName() + ".PK",NodeType.NODE_TYPE_CONDITION, fileName);
			//for each column in constraint node create edges
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				EvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op");
			}
			//return constraint node
			return u;
		} else if (cConstraint.getType()==Constraint.UNIQUE)  {
			//add constraint node
			u = add_node(tTable.getName() + ".UC",NodeType.NODE_TYPE_CONDITION, fileName);
			//for each column in constraint node create edges
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				EvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op");
			}
			//return constraint node
			return u;
		} else if (cConstraint.getType()==Constraint.NOT_NULL) {
			//add constraint node
			u = add_node(tTable.getName() + ".NC",NodeType.NODE_TYPE_CONDITION, fileName);
			//for each column in constraint node create edges
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				EvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op");
			}
			//return constraint node
			return u;
		} else if (cConstraint.getType()==Constraint.FOREIGN_KEY) {
			//add constraint node
			u = add_node(tTable.getName() +  ".FK", NodeType.NODE_TYPE_CONDITION, fileName);
			//for each column in detail table create edges from attributes nodes to fk node
			tTable = cConstraint.getRef();
			cols = cConstraint.getRefColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				EvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				add_edge(v, u, EdgeType.EDGE_TYPE_OPERATOR, "op" + (i + 1));
			}
			//for each column in master table create edges from fk node to attributes nodes
			tTable = cConstraint.getMain();
			cols = cConstraint.getMainColumns();
			for (int i = 0; i < cols.length; i++) {
				int columnNumber = ((Integer)cols[i]).intValue();
				EvolutionNode v = find_attribute(tTable.getName(), tTable.getColumnName(columnNumber));
				add_edge(u, v, EdgeType.EDGE_TYPE_OPERATOR, "op" + (i + 1));
			}
			//return constraint node
			return u ;
		}
		else { //other constraints are not captured
			return null;
		}
	}

	private EvolutionNode find_attribute(String TableName, String Attribute) {
		EvolutionNode u ;
		u = getAttributeNode(TableName + "_SCHEMA", Attribute);
		/**
		 * @author pmanousi
		 * If it is not a table then it is a view we ask about.
		 */
		if(u==null)
		{
			u = getAttributeNode(TableName + "_OUT", Attribute);
		}
		return u ;
	}

	/**
	 * adds a block node in the graph
	 * @author Stefanos Geraggelos
	 * @param block
	 * @param fileNode
	 * @exception SQLException
	 */
	private void addBlock(Block block, EvolutionNode fileNode, File fileName) throws SQLException {
		try{
			EvolutionNode blockNode = null;
			boolean emptyBlock=true;
			
			if (block instanceof AnonymousBlock){
				anon_blocks++;
				line=block.getLine();
				
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node("ANONYMOUS_BLOCK_"+anon_blocks,NodeType.NODE_TYPE_ANONYMOUS_BLOCK, fileName);
					emptyBlock=false;
				}
			}
			else if (block instanceof Script){
				line=block.getLine();
			}
			else if (block instanceof StoredProcedure){
				StoredProcedure proc=(StoredProcedure)block;
				line=block.getLine();
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node(proc.getName(),NodeType.NODE_TYPE_STORED_PROCEDURE, fileName);
					emptyBlock=false;
				}
			}
			else if (block instanceof StoredFunction){
				StoredFunction func=(StoredFunction)block;
				line=block.getLine();
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node(func.getName(),NodeType.NODE_TYPE_STORED_FUNCTION, fileName);
					emptyBlock=false;
				}
			}
			else if (block instanceof Trigger){
				Trigger trig=(Trigger)block;
				line=block.getLine();
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node(trig.getName(),NodeType.NODE_TYPE_TRIGGER, fileName);
					emptyBlock=false;
				}
			}
			else if (block instanceof Package){
				Package pack=(Package)block;
				line=block.getLine();
				if (!blockHasEmptyChildren(block)){	
					blockNode=add_node(pack.getName(),NodeType.NODE_TYPE_PACKAGE, fileName);
					emptyBlock=false;
				}
			}
			else if (block instanceof EmbeddedStatement){
				embeddeds++;
				line=block.getLine();
				if (!blockHasEmptyChildren(block)){
					blockNode=add_node("EMBEDDED_"+embeddeds,NodeType.NODE_TYPE_EMBEDDED_STATEMENT, fileName);
					emptyBlock=false;
				}
			}
			//visualize nested blocks
			Vector<Block> blocks=block.getBlocks();
			for (Block bl:blocks){
				if (!(block instanceof Script))		addBlock(bl,blockNode, fileName);
				else	addBlock(bl,fileNode, fileName);
			}
			if (!(block instanceof Script) && !emptyBlock){
				add_edge(fileNode,blockNode,EdgeType.EDGE_TYPE_CONTAINS,"contains");
			}
			Vector<Statement> stmts=block.getStatements();
			for (Statement nod:stmts){
				EvolutionNode stmtNode = new EvolutionNode();
				if (nod instanceof Relation){
					Relation rel=((Relation)nod);
					line=rel.getLine();
					stmtNode=this.add_table(rel.getTable(),rel.getDefinition(), fileName);
				}
				else if (nod instanceof View){
					View view=((View)nod);
					line=view.getLine();
					stmtNode=add_node(view.getName(), NodeType.NODE_TYPE_VIEW, fileName);
					this.createQuery(stmtNode,view.getSelect(),false, fileName);
					stmtNode.setSQLDefinition(view.getDefinition());
				}
				else if (nod instanceof PlainSQL){
					PlainSQL plain=((PlainSQL)nod);
					line=plain.getLine();
					if (plain.getOperator()==SpecificOperator.SELECT){
						queries++;
						if (plain.isSelectInto()) stmtNode=add_node(plain.getName(),NodeType.NODE_TYPE_QUERY, fileName);
						else	stmtNode=add_node("Q"+queries, NodeType.NODE_TYPE_QUERY, fileName);
						this.createQuery(stmtNode,plain.getSelect(),false, fileName);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
					else if (plain.getOperator()==SpecificOperator.INSERT){
						inserts++;
						stmtNode=add_node("I"+inserts,NodeType.NODE_TYPE_INSERT, fileName);
						this.createQuery(stmtNode,plain.getSelect(),false, fileName);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
					else if (plain.getOperator()==SpecificOperator.DELETE){
						deletes++;
						stmtNode=add_node("D"+deletes,NodeType.NODE_TYPE_DELETE, fileName);
						this.createQuery(stmtNode,plain.getSelect(),false, fileName);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
					else if (plain.getOperator()==SpecificOperator.UPDATE){
						updates++;
						stmtNode=add_node("U"+updates,NodeType.NODE_TYPE_UPDATE, fileName);
						this.createQuery(stmtNode,plain.getSelect(),false, fileName);
						stmtNode.setSQLDefinition(plain.getDefinition());
					}
				}
				else if (nod instanceof Cursor){
					Cursor cur=((Cursor)nod);
					line=cur.getLine();
					if (cur.getId()==0){
						//static cursor
						stmtNode=add_node(cur.getName(),NodeType.NODE_TYPE_CURSOR, fileName);
					}
					else {
						//dynamic cursor
						stmtNode=add_node(cur.getName()+"_"+cur.getId(),NodeType.NODE_TYPE_CURSOR, fileName);
					}
					this.createQuery(stmtNode,cur.getSelect(),false, fileName);
					stmtNode.setSQLDefinition(cur.getDefinition());
				}
				else if (nod instanceof Variable){
					Variable var=((Variable)nod);
					line=var.getLine();
					stmtNode=add_node(var.getName(),NodeType.NODE_TYPE_VARIABLE, fileName);
					this.createQuery(stmtNode,var.getSelect(),false, fileName);
					stmtNode.setSQLDefinition(var.getDefinition());
				}
				else if (nod instanceof Assignment){
					assignments++;
					Assignment assig=((Assignment)nod);
					line=assig.getLine();
					stmtNode=add_node("A"+assignments,NodeType.NODE_TYPE_ASSIGNMENT, fileName);
					this.createQuery(stmtNode,assig.getSelect(),false, fileName);
					stmtNode.setSQLDefinition(assig.getDefinition());
				}
				else if (nod instanceof MergeInto){
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

	/**
	 * @author pmanousi
	 * We check to see if a node already exists in any of the input nodes.  If not we create it and hang it in the table or view it should be getting input from.
	 */
	@SuppressWarnings("unchecked")
	private EvolutionNode existsInInputSchema(EvolutionNode qn,
		String tableName,
		String nodeName,
		File fileName)
	{
		EvolutionNode x = null;
		for(int i=0;i<qn.getOutEdges().size();i++)
		{
			if(qn.getOutEdges().get(i).getToNode().getName().equals(qn.getName()+"_IN_"+tableName))
			{
				EvolutionNode in = (EvolutionNode) qn.getOutEdges().get(i).getToNode();
				for(int j=0;j<in.getOutEdges().size();j++)
				{
					if(in.getOutEdges().get(j).getToNode().getName().equals(nodeName))
					{
						return (EvolutionNode) in.getOutEdges().get(j).getToNode();
					}
				}
				x=add_node(nodeName, NodeType.NODE_TYPE_ATTRIBUTE, fileName);
				add_edge(in, x, EdgeType.EDGE_TYPE_INPUT, "S");
				for(int j=0;j<in.getOutEdges().size();j++)
				{
					if(in.getOutEdges().get(j).getName().equals("from"))
					{
						for(int k=0;k<in.getOutEdges().get(j).getToNode().getOutEdges().size();k++)
						{
							if(in.getOutEdges().get(j).getToNode().getOutEdges().get(k).getToNode().getName().equals(nodeName))
							{
								add_edge(x,
									(EvolutionNode) in
										.getOutEdges().get(j).getToNode().getOutEdges().get(k).getToNode(),
									EdgeType.EDGE_TYPE_MAPPING, "map-select");
								break;
							}
						}
					}
				}
				break;
			}
		}
		return(x);
	}
	
	@SuppressWarnings("unchecked")
	private void createQuery(EvolutionNode u, Select sSelect, boolean nested, File fileName)
		throws SQLException {
		Expression expression;
		String GroupByLabel = null;
		// TableFilter ta
		EvolutionNode v = null;
		EvolutionNode w = null;
		EvolutionNode x = null;
		EvolutionNode input = null;
		EvolutionNode output = add_node(u.getName() + "_OUT", NodeType.NODE_TYPE_OUTPUT, fileName);
		EvolutionNode semantics =
			add_node(u.getName() + "_SMTX", NodeType.NODE_TYPE_SEMANTICS, fileName);
		add_edge(u, output, EdgeType.EDGE_TYPE_OUTPUT, u.getName() + "OUT_S");
		add_edge(u, semantics, EdgeType.EDGE_TYPE_SEMANTICS, u.getName() + "SMTX_S");
		
		int tblIndex = 0;
		if(sSelect == null) {
			return;
		}
		int len = sSelect.tFilter.length;
		String[] selfJoinedTables = new String[len]  ; // dim statement
		selfJoinedTables = checkSelfJoin(sSelect);
		// __________________________________________________________________________________________________  
		// resolve tables names for all attributes
		// mentioned in the SELECT and WHERE clause
		Resolve(sSelect, selfJoinedTables);
		// __________________________________________________________________________________________________  
		// Visualize the from part of a query
		for (int i = 0; i < sSelect.tFilter.length; i++) {
			if ( selfJoinedTables[i] != null)
			{
				input=add_node(u.getName()+"_IN_"+sSelect.tFilter[i].getName(), NodeType.NODE_TYPE_INPUT, fileName);
				add_edge(u, input, EdgeType.EDGE_TYPE_INPUT, "IN_S");
			}
			else
			{
				/**
				 * @author pmanousi
				 * Create new schema, also change the from edge from input to table_schema instead of from query to table.
				 */
				input=add_node(u.getName()+"_IN_"+sSelect.tFilter[i].getTable().getName(), NodeType.NODE_TYPE_INPUT, fileName);
				add_edge(u, input, EdgeType.EDGE_TYPE_INPUT, "IN_S");
			}
			add_edge((EvolutionNode) input.getInEdges().get(0).getFromNode(),
				(EvolutionNode) find_relationFrom(sSelect.tFilter[i].getTable().getName())
					.getInEdges().get(0).getFromNode(),
				EdgeType.EDGE_TYPE_USES, "uses");
			add_edge(input, find_relationFrom(sSelect.tFilter[i].getTable().getName()), EdgeType.EDGE_TYPE_FROM,
				"from");
		}
		// Visualize the select part  
		// The following loop examines only the selected columns
		for (int i = 0; i < sSelect.eColumn.length - sSelect.iOrderLen - sSelect.iGroupLen; i++) {
			expression = sSelect.eColumn[i];
			// SHOULD ADD CHECK FOR ALIAS FOR SELF JOIN QUERIES
			// Type COLUMN
			if (expression.getType()== Expression.COLUMN) {
				v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE, fileName) ;
				add_edge(output, v, EdgeType.EDGE_TYPE_SCHEMA, "S");
				x=existsInInputSchema(u, expression.getTableName(), expression.getColumnName(), fileName);
				add_edge(v, x, EdgeType.EDGE_TYPE_MAPPING, "map-select");
			}
			// Type VALUE
			if (expression.getType()== Expression.VALUE) {
				try{
					if (expression.getAlias()==""){
						v = add_node(expression.getValue().toString(), NodeType.NODE_TYPE_ATTRIBUTE, fileName) ;
					}else{
						v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE, fileName) ;
						EvolutionNode v1 =
							add_node(expression.getValue().toString(), NodeType.NODE_TYPE_CONSTANT, fileName);
						add_edge(v, v1, EdgeType.EDGE_TYPE_MAPPING, "map-select");
					}
					add_edge(u, v, EdgeType.EDGE_TYPE_SCHEMA, "S");
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
					v = add_node(function.getName(), NodeType.NODE_TYPE_ATTRIBUTE, fileName);
				else 
					v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE, fileName);
				w = add_node(function.getName(), NodeType.NODE_TYPE_FUNCTION, fileName) ;
				add_edge(output, v, EdgeType.EDGE_TYPE_SCHEMA, "S");
				add_edge(v, w, EdgeType.EDGE_TYPE_MAPPING, "map-select");
				add_edge(semantics, w, EdgeType.EDGE_TYPE_SEMANTICS, function.getName());
				for (int j = 0; j <function.eArg.length ; j++) {
					EvolutionNode arg = add_expression(function.eArg[j], u, fileName);
					add_edge(w, arg, EdgeType.EDGE_TYPE_MAPPING, "op" + (j + 1));
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
					v = add_node(strFunction, NodeType.NODE_TYPE_ATTRIBUTE, fileName);
				else 
					v = add_node(expression.getAlias(), NodeType.NODE_TYPE_ATTRIBUTE, fileName);
				w = add_node(strFunction, NodeType.NODE_TYPE_FUNCTION, fileName) ;
				add_edge(output, v, EdgeType.EDGE_TYPE_SCHEMA, "S");
				add_edge(v, w, EdgeType.EDGE_TYPE_MAPPING, "map-select");
				add_edge(semantics, w, EdgeType.EDGE_TYPE_SEMANTICS, strFunction);
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
						EvolutionNode x1 = find_relation(expression.getArg().getTableName());
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
							/**
							 * @author pmanousi
							 * Create INPUT schema nodes if they don't exist and fix their edges to output of table.
							 */
							x=existsInInputSchema(u, sSelect.tFilter[tblIndex].getTable().getName(), sSelect.tFilter[tblIndex].getTable().getColumnName(l), fileName);
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
							for (int j = 0; j < sSelect.tFilter[l].getTable().getColumnCount(); j++)
							{
								/**
								 * @author pmanousi
								 * Create INPUT schema nodes if they don't exist and fix their edges to output of table.
								 */
								x=existsInInputSchema(u, sSelect.tFilter[l].getTable().getName(), sSelect.tFilter[l].getTable().getColumnName(j), fileName);
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
						/**
						 * @author pmanousi
						 * Changed to work with output node of table.
						 */
						x=existsInInputSchema(u, expression.getArg().getTableName(), expression.getArg().getColumnName(), fileName);
					add_edge(w, x, EdgeType.EDGE_TYPE_MAPPING, "map-select");
					break;
				default : 
					/**
					 * @author pmanousi
					 * Changed to work with output node of table.
					 */
					x=existsInInputSchema(u, expression.getArg().getTableName(), expression.getArg().getColumnName(), fileName);
					break;
				}
			}
		}
		// __________________________________________________________________________________________________  
		// Visualize the where part of a query
		if (sSelect.eCondition != null ) {
			v = add_expression(sSelect.eCondition,u, fileName);
			add_edge(semantics, v, EdgeType.EDGE_TYPE_WHERE, "where");
		}
		// __________________________________________________________________________________________________  
		// Visualize the GROUP BY part of a query
		if ( sSelect.iGroupLen > 0 ) {
			// add GB node
			v = add_node("GB", NodeType.NODE_TYPE_GROUP_BY, fileName) ;
			add_edge(semantics, v, EdgeType.EDGE_TYPE_GROUP_BY, "group by");
			int j = 0;
			// add edges
			for (int i = (sSelect.eColumn.length - sSelect.iGroupLen- sSelect.iOrderLen); i < (sSelect.eColumn.length - sSelect.iOrderLen); i++){
				j++;
				GroupByLabel = "group by" + j;
				x = add_expression(sSelect.eColumn[i],u, fileName);
				add_edge(v, x, EdgeType.EDGE_TYPE_GROUP_BY, GroupByLabel);
			}
		}
	}
	
	private EvolutionNode add_expression(Expression expr,
		EvolutionNode head,
		File fileName) {
		EvolutionNode u = null;
		switch (expr.getType()){
		case Expression.NOT:
			u = add_node(" != ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.EQUAL:
			u =  add_node(" = ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.BIGGER_EQUAL:
			u =  add_node(" >= ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.BIGGER:
			u =  add_node(" > ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.SMALLER:
			u = add_node(" < ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.SMALLER_EQUAL:
			u =  add_node(" <= ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.NOT_EQUAL:
			u =  add_node(" <> ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.VALUELIST:
			u =  add_node(" ValueList ", NodeType.NODE_TYPE_CONSTANT, fileName);
			break;
		case Expression.VALUE:
			try{
				if (expr.getValue()==null){
					u =  add_node("NULL", NodeType.NODE_TYPE_CONSTANT, fileName);
				}else {
					u =  add_node(expr.getValue().toString(), NodeType.NODE_TYPE_CONSTANT, fileName);
				}
				break;
			}
			catch (Exception ex){
				System.out.println("[Expression Object in add_expression]"+ ex.getMessage() );
			}
		case Expression.COLUMN:
			/**
			 * @author pmanousi
			 * Changed it to work with INPUT schema.
			 */
			u=existsInInputSchema(head, expr.getTableName(), expr.getColumnName(), fileName);
			break;
		case Expression.IN:
			u =  add_node(" IN ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.EXISTS:
			u =  add_node(" EXISTS ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.AND:
			u =  add_node(" AND ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.OR:
			u =  add_node(" OR ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.CONCAT:
			u =  add_node(" || ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.ADD:
			u =  add_node(" + ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.SUBTRACT:
			u =  add_node(" - ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.MULTIPLY:
			u =  add_node(" * ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.DIVIDE:
			u =  add_node(" / ", NodeType.NODE_TYPE_OPERAND, fileName);
			break;
		case Expression.CASEWHEN:
			u =  add_node(" CASEWHEN ", NodeType.NODE_TYPE_FUNCTION, fileName);
			break;
		case Expression.QUERY:
			try {
				queries++;
				u = add_node("Q" + queries, NodeType.NODE_TYPE_QUERY, fileName);
				this.createQuery(u, expr.sSelect, true, fileName);
				break;
			} catch (Exception e) {
				System.out.println("[Error in subquery]"+e.getMessage());
			}
		case Expression.FUNCTION:
			// Type Function
			Function function = expr.getFunction();
			u = add_node(function.getName(), NodeType.NODE_TYPE_FUNCTION, fileName) ;
			for (int j = 0; j <function.eArg.length ; j++) {
				EvolutionNode arg = add_expression(function.eArg[j], head, fileName);
				add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR, "op" + (j + 1));
			}
			break;
		default:
			break ;
		}
		EvolutionNode arg;
		if (expr.getArg()!=null) {
			arg = add_expression(expr.getArg(),head, fileName) ;
			if (arg!=null)
			{
				/**
				 * @author pmanousi
				 * Was: newedge = add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR,"op1");
				 */
				if(find_relationFrom(arg.getName())==null||arg.getType()==NodeType.NODE_TYPE_ATTRIBUTE)
				{
					add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR, "op1");
				}
				else
				{
					add_edge(u, find_relationFrom(arg.getName()), EdgeType.EDGE_TYPE_OPERATOR, "op1");
				}
			}
		}
		if (expr.getArg2()!=null) {
			arg = add_expression(expr.getArg2(),head, fileName);
			if (arg!=null)
			{
				/**
				 * @author pmanousi
				 * Was: newedge = add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR,"op2");
				 */
				if(find_relationFrom(arg.getName())==null||arg.getType()==NodeType.NODE_TYPE_CONSTANT)
				{
					add_edge(u, arg, EdgeType.EDGE_TYPE_OPERATOR, "op2");
				}
				else
				{
					add_edge(u, find_relationFrom(arg.getName()), EdgeType.EDGE_TYPE_OPERATOR, "op2");
				}
			}
		}
		return u;
	}

	private EvolutionNode find_relation(String relation) {
		EvolutionNode v;
		v = getEvolutionGraph().findVertexByName(relation);
		return v ;
	}
	
	private EvolutionNode find_relationFrom(String relation)
	{
		EvolutionNode v;
		v = getEvolutionGraph().findVertexByName(relation+"_SCHEMA");
		if(v==null)
		{
			v=getEvolutionGraph().findVertexByName(relation+"_OUT");
		}
		return v ;
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

	/**
	 * used for finding explicitly an attribute by its name and its parent relation,
	 * view or query node
	 **/
	private EvolutionNode getAttributeNode(String TableName, String AttributeName) {
		for (EvolutionNode u : evoGraph.getVertices()) {

			if (u.getName().toUpperCase().equals(TableName) &&
				((u.getType().getCategory() == NodeCategory.INOUTSCHEMA) ||
					(u.getType().getCategory() == NodeCategory.MODULE))) {

				for (EvolutionEdge e : evoGraph.getOutEdges(u)) {
					if (evoGraph.getDest(e).getName().toUpperCase().equals(AttributeName.toUpperCase())) {
						return evoGraph.getDest(e);
					}
				}
			}
		}
		return null;
	}

	public EvolutionGraph getEvolutionGraph() {
		return evoGraph;
	}

}