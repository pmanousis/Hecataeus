/**
 * @author George Papastefanatos, National Technical University of Athens
 * @author Fotini Anagnostou, National Technical University of Athens
 */

package org.hecataeus.jung;

import java.awt.Shape;

import org.hecataeus.evolution.HecataeusNodeType;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.AbstractVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexAspectRatioFunction;
import edu.uci.ics.jung.graph.decorators.VertexSizeFunction;
import edu.uci.ics.jung.visualization.VertexShapeFactory;

/*
 * sets the shape of nodes according to their type
 */
public final class HecataeusJungNodeShape extends AbstractVertexShapeFunction implements VertexSizeFunction, VertexAspectRatioFunction 
{
	VertexShapeFactory factory = new VertexShapeFactory(this,this );

	public Shape getShape(Vertex v) {

		HecataeusNodeType type = ((HecataeusJungNode) v).getType();
		
		if (type ==HecataeusNodeType.NODE_TYPE_QUERY)
			return factory.getRegularPolygon(v,6);
		else if (type ==HecataeusNodeType.NODE_TYPE_RELATION)
			return factory.getEllipse(v);
		else if (type ==HecataeusNodeType.NODE_TYPE_VIEW)
			return factory.getRegularPolygon(v,3);
		else 
			return factory.getRegularPolygon(v,4);
		
	}

	
	public int getSize(Vertex v) {
		HecataeusNodeType type = ((HecataeusJungNode) v).getType();

		if (type ==HecataeusNodeType.NODE_TYPE_QUERY
				||type ==HecataeusNodeType.NODE_TYPE_RELATION
				||type ==HecataeusNodeType.NODE_TYPE_VIEW)
			return 40;
		else
			return 10;
		
	}
	
	public float getAspectRatio(Vertex v) { 
		return 1.0f; 
	} 
	
}

