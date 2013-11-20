/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */

package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Shape;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;

/*
 * sets the shape of nodes according to their type
 */
public final class VisualNodeShape extends AbstractVertexShapeTransformer<VisualNode> implements Transformer<VisualNode,Shape> {
	
	private static int INITIAL_SIZE = 21; // itan 60 alliws 4
	
	public VisualNodeShape() {
		//extends setSizeTransformer for defining the custom size of nodes  
		setSizeTransformer(new Transformer<VisualNode,Integer>() {
			public Integer transform(VisualNode v) {
				NodeType type = (v.getType());
				int allEdges;
				if(HecataeusViewer.nodeSize){
					INITIAL_SIZE = 21;   //60
				}
				else{
					INITIAL_SIZE = 4;
					allEdges = v._inEdges.size() + v._outEdges.size();
				//	INITIAL_SIZE = 4*allEdges;
					INITIAL_SIZE = 4 + 10*(int)Math.log((double)allEdges);
				}
				
				if (type.getCategory()== NodeCategory.MODULE)
					return INITIAL_SIZE;
				else if (type.getCategory()== NodeCategory.CONTAINER)
					//return INITIAL_SIZE * 4 ;
					return 10;
				/***
				 * @author pmanousi
				 */
				else if (type.getCategory()== NodeCategory.INOUTSCHEMA)
					//return INITIAL_SIZE / 2;
					return 10;
				else
					//return INITIAL_SIZE/4;
					return 10;

			}});
		//extends setAspectRatioTransformer for defining the custom aspect ration of nodes
		setAspectRatioTransformer(new Transformer<VisualNode,Float>() {
			public Float transform(VisualNode v) {
				return 1.0f;
			}});
	}
	
	public Shape transform(VisualNode v) {
		NodeType type = (v.getType());
		 
		if (type ==NodeType.NODE_TYPE_QUERY 
				|| type==NodeType.NODE_TYPE_INSERT			//
				|| type==NodeType.NODE_TYPE_DELETE			//added by sgerag
				|| type==NodeType.NODE_TYPE_UPDATE			//
				|| type==NodeType.NODE_TYPE_MERGE_INTO		//
				|| type==NodeType.NODE_TYPE_CURSOR			//
				|| type==NodeType.NODE_TYPE_VARIABLE		//
				|| type==NodeType.NODE_TYPE_ASSIGNMENT)
			return factory.getRegularPolygon(v,6);
		else if (type ==NodeType.NODE_TYPE_FILE)
			return factory.getRoundRectangle(v);
		else if (type==NodeType.NODE_TYPE_ANONYMOUS_BLOCK	//
				|| type==NodeType.NODE_TYPE_SCRIPT			//added by sgerag
				|| type==NodeType.NODE_TYPE_STORED_PROCEDURE			//
				|| type==NodeType.NODE_TYPE_STORED_FUNCTION			//
				|| type==NodeType.NODE_TYPE_TRIGGER		//
				|| type==NodeType.NODE_TYPE_PACKAGE		//
				|| type==NodeType.NODE_TYPE_EMBEDDED_STATEMENT)
			return factory.getRectangle(v);
		else if (type ==NodeType.NODE_TYPE_RELATION)
			return factory.getEllipse(v);
		else if (type ==NodeType.NODE_TYPE_VIEW)
			return factory.getRegularPolygon(v,3);
		/***
		 * @author pmanousi
		 */
		else if (type ==NodeType.NODE_TYPE_OUTPUT)
			return factory.getRegularPolygon(v,5);
		else if (type ==NodeType.NODE_TYPE_INPUT)
			return factory.getRegularPolygon(v,5);
		else if (type==NodeType.NODE_TYPE_SEMANTICS)
			return factory.getRegularPolygon(v,5);
		else
			return factory.getRegularPolygon(v,4);
	} 
	
}

