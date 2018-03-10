package edu.ntua.dblab.hecataeus.gui.util;

import java.util.Collection;

import edu.ntua.dblab.hecataeus.graph.converter.EvolutionToVisualConverter;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;

public class GraphConverterUtilities {
	private static EvolutionToVisualConverter graphConverter = new EvolutionToVisualConverter();
	
	private GraphConverterUtilities(){
		
	}
	
	public static VisualGraph convertEvolutionGraphToVisual(EvolutionGraph evoGraph){
		return convertEvolutionGraphToVisual(evoGraph, evoGraph.getVertices());
	}
	
	public static VisualGraph convertEvolutionGraphToVisual(EvolutionGraph evoGraph, Collection<EvolutionNode> evoNodes){
		return graphConverter.productVisualGraph(evoNodes, evoGraph);
	}
}
