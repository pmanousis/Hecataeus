###########################################################
############HecataeusSQLExtensionParser.java###############


HecataeusSQLExtensionParser is a module that reads an input file with policy clauses. For more details see paper in ICEIS.

In brief, input file must obey to the following conventions:

1. Each clause ends with an ";".

2. Each clause is of the form:

<nodeName>|DATABASE : ON <eventType> TO <nodeName>|<nodeType> THEN <policyType> ;

<eventType> can have one of the following values:
	ADD_ATTRIBUTE		   ,      
	ADD_CONDITION          ,
	ADD_RELATION           ,
	DELETE_ATTRIBUTE       ,
	DELETE_CONDITION       ,
	DELETE_RELATION        ,
	MODIFYDOMAIN_ATTRIBUTE ,
	RENAME_ATTRIBUTE       ,
	MODIFY_CONDITION       ,
	RENAME_RELATION        

and 
<policyType> :
	PROPAGATE,
	BLOCK,
	PROMPT  

Any other values are discarder by the parser.

3. There are two kinds of clauses :

a) Clauses with policies for types of nodes (database defaults). Then each clause is of the form:

DATABASE: ON <eventType> TO <nodeType Sustaining the event> THEN <policyType> ;

<nodeType Sustaining the event> takes the following values:

		NODE,
		RELATION,
		QUERY,
		VIEW, 
		ATTRIBUTE,
		CONDITION,
		CONSTANT,
		GROUP_BY,
		FUNCTION,
		PK,
		FK,
		NC,
		UC


b) Clauses with policies for specific nodes. Then each clause is of the form:

<nodeName>: ON <eventType> TO <nodeName Sustaining the event> THEN <policyType>;


<nodeName> can be the name of top-level node, such as a relation (e.g. EMPS), view (e.g., MyView) or query (e.g., Q1), or of a specific low level node (e.g. attribute). It is then preferred to refer to the low level node with the suffix (separated by a dot ".") of its parent, e.g. EMPS.Name, MyView.Description, Q1.T_Hours, etc.

When a policy is defined for a node it applies to itself as well as for all descending nodes.

4. The Parser processes the clauses of the input file with the following ordering:

a) First it parses the database defaults which are applied on then graph, according to the order they are read in the input file.

b) It then parses again and processes the node specific policy clauses, according to the order they are read in the input file.

The last parsed policies override the first ones.

5. Parser is case insensitive and ignores extra spaces and tabs.

