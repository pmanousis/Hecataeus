/**
 *
 * @author Fotini Anagnostou, National Technical Univercity of Athens
 */

package org.hecataeus.basegraph;

/**
 * The class gathers the possible types of an edge
 *
 */
public class HecataeusEdgeType {
	public static int EDGE_TYPE_SCHEMA = 50;
	public static int EDGE_TYPE_CONDITION = 51;
	public static int EDGE_TYPE_MAPPING = 52;
	public static int EDGE_TYPE_WHERE = 53;
	public static int EDGE_TYPE_FROM = 54;
	public static int EDGE_TYPE_GROUP_BY = 55;
	public static int EDGE_TYPE_ALIAS = 56;
	public static int EDGE_TYPE_CALLING = 57;
	public static int EDGE_TYPE_OPERATOR = 58;
	
	/**
	 * Converts from the int representation of a type to corresponding the String representation
	 *
	 */
	public static String convertEdgeTypeToString(int Value) {
		String convertEdgeTypeToString = null;
		
		if (new Integer(EDGE_TYPE_ALIAS).equals(new Integer(Value))) {
			return "EDGE_TYPE_ALIAS";
		}
		else if (new Integer(EDGE_TYPE_CALLING).equals(new Integer(Value))) {
			return "EDGE_TYPE_CALLING";
		}
		else if (new Integer(EDGE_TYPE_CONDITION).equals(new Integer(Value))) {
			return "EDGE_TYPE_CONDITION";
		}
		else if (new Integer(EDGE_TYPE_FROM).equals(new Integer(Value))) {
			return "EDGE_TYPE_FROM";
		}
		else if (new Integer(EDGE_TYPE_GROUP_BY).equals(new Integer(Value))) {
			return "EDGE_TYPE_GROUP_BY";
		}
		else if (new Integer(EDGE_TYPE_MAPPING).equals(new Integer(Value))) {
			return "EDGE_TYPE_MAPPING";
		}
		else if (new Integer(EDGE_TYPE_OPERATOR).equals(new Integer(Value))) {
			return "EDGE_TYPE_OPERATOR";
		}
		else if (new Integer(EDGE_TYPE_SCHEMA).equals(new Integer(Value))) {
			return "EDGE_TYPE_SCHEMA";
		}
		else if (new Integer(EDGE_TYPE_WHERE).equals(new Integer(Value))) {
			return "EDGE_TYPE_WHERE";
		}
		else {
                    return convertEdgeTypeToString;
		}
	}

	/**
	 * Converts from the String representation of a type to corresponding the int representation
	 *
	 */
	public static int convertStringToEdgeType(String Value) {
		int convertStringToEdgeType = -1;
		
		if ("EDGE_TYPE_ALIAS".equals(Value)) {
			return EDGE_TYPE_ALIAS;
		}
		else if ("EDGE_TYPE_CALLING".equals(Value)) {
			return EDGE_TYPE_CALLING;
		}
		else if ("EDGE_TYPE_CONDITION".equals(Value)) {
			return EDGE_TYPE_CONDITION;
		}
		else if ("EDGE_TYPE_FROM".equals(Value)) {
			return EDGE_TYPE_FROM;
		}
		else if ("EDGE_TYPE_GROUP_BY".equals(Value)) {
			return EDGE_TYPE_GROUP_BY;
		}
		else if ("EDGE_TYPE_MAPPING".equals(Value)) {
			return EDGE_TYPE_MAPPING;
		}
		else if ("EDGE_TYPE_OPERATOR".equals(Value)) {
			return EDGE_TYPE_OPERATOR;
		}
		else if ("EDGE_TYPE_SCHEMA".equals(Value)) {
			return EDGE_TYPE_SCHEMA;
		}
		else if ("EDGE_TYPE_WHERE".equals(Value)) {
			return EDGE_TYPE_WHERE;
		}
		else {
            return convertStringToEdgeType;
		}
	}

}
