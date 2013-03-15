/**
 *
 * @author Fotini Anagnostou, National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

/**
 * The class gathers the possible types of a node
 *
 */
public class HecataeusNodeType {
	public static int NODE_TYPE_RELATION = 1;
	public static int NODE_TYPE_ATTRIBUTE = 2;
	public static int NODE_TYPE_QUERY = 3;
	public static int NODE_TYPE_CONDITION = 4;
	public static int NODE_TYPE_OPERAND = 5;
	public static int NODE_TYPE_CONSTANT = 6;
	public static int NODE_TYPE_GROUP_BY = 7;
	public static int NODE_TYPE_FUNCTION = 8;
	public static int NODE_TYPE_STORED_PROCEDURE = 9;
	public static int NODE_TYPE_APPLICATION = 10;
	public static int NODE_TYPE_VARIABLE = 11;
	public static int NODE_TYPE_VIEW = 12;
	
	/**
	 * Converts from the int representation of a type to corresponding the String representation
	 *
	 */
	public static String convertNodeTypeToString(int Value) {
		String convertNodeTypeToString = null;
		
		if (new Integer(NODE_TYPE_RELATION).equals(new Integer(Value))) {
			return "NODE_TYPE_RELATION";
		}
		else if (new Integer(NODE_TYPE_ATTRIBUTE).equals(new Integer(Value))) {
			return "NODE_TYPE_ATTRIBUTE";
		}
		else if (new Integer(NODE_TYPE_APPLICATION).equals(new Integer(Value))) {
			return "NODE_TYPE_APPLICATION";
		}
		else if (new Integer(NODE_TYPE_CONDITION).equals(new Integer(Value))) {
			return "NODE_TYPE_CONDITION";
		}
		else if (new Integer(NODE_TYPE_CONSTANT).equals(new Integer(Value))) {
			return "NODE_TYPE_CONSTANT";
		}
		else if (new Integer(NODE_TYPE_FUNCTION).equals(new Integer(Value))) {
			return "NODE_TYPE_FUNCTION";
		}
		else if (new Integer(NODE_TYPE_GROUP_BY).equals(new Integer(Value))) {
			return "NODE_TYPE_GROUP_BY";
		}
		else if (new Integer(NODE_TYPE_OPERAND).equals(new Integer(Value))) {
			return "NODE_TYPE_OPERAND";
		}
		else if (new Integer(NODE_TYPE_QUERY).equals(new Integer(Value))) {
			return "NODE_TYPE_QUERY";
		}
		else if (new Integer(NODE_TYPE_STORED_PROCEDURE).equals(new Integer(Value))) {
			return "NODE_TYPE_STORED_PROCEDURE";
		}
		else if (new Integer(NODE_TYPE_VARIABLE).equals(new Integer(Value))) {
			return "NODE_TYPE_VARIABLE";
		}
		else if (new Integer(NODE_TYPE_VIEW).equals(new Integer(Value))) {
			return "NODE_TYPE_VIEW";
		}
		else {
        		return convertNodeTypeToString;
                }
	}

	/**
	 * Converts from the String representation of a type to corresponding the int representation
	 *
	 */
	public static int convertStringToNodeType(String Value) {
		int convertStringToNodeType = -1;
		
		if ("NODE_TYPE_RELATION".equals(Value)) {
			return NODE_TYPE_RELATION;
		}
		else if ("NODE_TYPE_ATTRIBUTE".equals(Value)) {
			return NODE_TYPE_ATTRIBUTE;
		}
		else if ("NODE_TYPE_APPLICATION".equals(Value)) {
			return NODE_TYPE_APPLICATION;
		}
		else if ("NODE_TYPE_CONDITION".equals(Value)) {
			return NODE_TYPE_CONDITION;
		}
		else if ("NODE_TYPE_CONSTANT".equals(Value)) {
			return NODE_TYPE_CONSTANT;
		}
		else if ("NODE_TYPE_FUNCTION".equals(Value)) {
			return NODE_TYPE_FUNCTION;
		}
		else if ("NODE_TYPE_GROUP_BY".equals(Value)) {
			return NODE_TYPE_GROUP_BY;
		}
		else if ("NODE_TYPE_OPERAND".equals(Value)) {
			return NODE_TYPE_OPERAND;
		}
		else if ("NODE_TYPE_QUERY".equals(Value)) {
			return NODE_TYPE_QUERY;
		}
		else if ("NODE_TYPE_STORED_PROCEDURE".equals(Value)) {
			return NODE_TYPE_STORED_PROCEDURE;
		}
		else if ("NODE_TYPE_VARIABLE".equals(Value)) {
			return NODE_TYPE_VARIABLE;
		}
		else if ("NODE_TYPE_VIEW".equals(Value)) {
			return NODE_TYPE_VIEW;
		}
		else {
                    return convertStringToNodeType;
		}
	}
}
