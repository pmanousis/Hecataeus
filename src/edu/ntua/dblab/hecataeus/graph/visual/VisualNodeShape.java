/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */

package edu.ntua.dblab.hecataeus.graph.visual;

import java.awt.Shape;

import org.apache.commons.collections15.Transformer;

import edu.ntua.dblab.hecataeus.graph.evolution.NodeCategory;
import edu.ntua.dblab.hecataeus.graph.evolution.NodeType;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;

/*
 * sets the shape of nodes according to their type
 */
public final class VisualNodeShape extends AbstractVertexShapeTransformer<VisualNode> implements Transformer<VisualNode,Shape> {
	
	private static final int INITIAL_SIZE = 40;
	
	public VisualNodeShape() {
		//extends setSizeTransformer for defining the custom size of nodes  
		setSizeTransformer(new Transformer<VisualNode,Integer>() {
			public Integer transform(VisualNode v) {
				NodeType type = (v.getType());
				if (type.getCategory()== NodeCategory.MODULE)
					return INITIAL_SIZE;
				else
					return INITIAL_SIZE/4;

			}});
		//extends setAspectRatioTransformer for defining the custom aspect ration of nodes
		setAspectRatioTransformer(new Transformer<VisualNode,Float>() {
			public Float transform(VisualNode v) {
				return 1.0f;
			}});
	}
	
	public Shape transform(VisualNode v) {
		NodeType type = (v.getType());
		
		if (type ==NodeType.NODE_TYPE_QUERY)
			return factory.getRegularPolygon(v,6);
		else if (type ==NodeType.NODE_TYPE_RELATION)
			return factory.getEllipse(v);
		else if (type ==NodeType.NODE_TYPE_VIEW)
			return factory.getRegularPolygon(v,3);
		else 
			return factory.getRegularPolygon(v,4);
	} 
	
}

