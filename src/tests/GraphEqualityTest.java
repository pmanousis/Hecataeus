package tests;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;

/**
 * In this test we check if both graphs are identical to its original. Things that
 * being tested in order to ensure similarity:
 * <ul>
 * <tt>EvolutionGraph's</tt>
 * <li>Total number of vertices</li>
 * <li>Total number of edges</li>
 * <li>Names of all out going edges from all vertices</li>
 * <li>Names of all in going edges from all vertices</li>
 * </ul>
 * <p>
 * <ul>
 * <tt>VisualGraph's</tt>
 * <li>Total number of vertices</li>
 * <li>Total number of edges</li>
 * </ul>
 */
public class GraphEqualityTest {

	private final static String PATH = "C:\\Users\\UniQ\\Desktop\\splitedVisualGraph.txt";

	@Test
	public final void test() {
		HecataeusViewer viewer = new HecataeusViewer(new EvolutionGraph());
		viewer.openProject();
		try {
			Path file = Paths.get(PATH);
			BufferedWriter writer = Files.newBufferedWriter(file);

			EvolutionGraph evolutionGraph = viewer.getEvolutionGraph();
			writer.write("" + evolutionGraph.getVertices().size());
			writer.newLine();
			writer.write("" + evolutionGraph.getEdges().size());
			writer.newLine();

			VisualGraph visualGraph = viewer.getSummaryVisualGraph();
			writer.write("" + visualGraph.getVertices().size());
			writer.newLine();
			writer.write("" + visualGraph.getEdges().size());
			writer.newLine();

			
			for (EvolutionNode vertice : evolutionGraph.getVertices()) {
				writer.write(Arrays.toString(vertice.getInEdges().toArray()));
				writer.newLine();
			}

			for (EvolutionNode vertice : evolutionGraph.getVertices()) {
				String q = Arrays.toString(vertice.getOutEdges().toArray());
				writer.write(q);
				writer.newLine();
			}
			writer.close();
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}

	}

}
