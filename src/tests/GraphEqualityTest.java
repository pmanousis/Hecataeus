package tests;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;
import edu.ntua.dblab.hecataeus.graph.visual.VisualNode;

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

	private final String PATH = "C:\\Users\\Desktop\\splitedVisualGraph.txt";

	private final HecataeusViewer viewer = new HecataeusViewer(new VisualGraph());

	@Test
	public final void test() {

		viewer.openProject();
		try {
			Path file = Paths.get(PATH);
			BufferedReader reader = Files.newBufferedReader(file);

			testEvolutionGraphNumberOfEdgesAndVertices(reader);

			testVisualGraphNumberOfEdgesAndVertices(reader);

			testIfAllEdgesExistInRefactoredCode(reader);

			reader.close();
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}

	}

	private void testEvolutionGraphNumberOfEdgesAndVertices(BufferedReader reader) throws IOException {
		VisualGraph evolutionGraph = viewer.graph;
		String evolutionGraphTotalVertices = reader.readLine();
		String evolutionGraphTotalEdges = reader.readLine();
		if (evolutionGraphTotalVertices.equals(evolutionGraph.getVertices().size()))
			fail("EVOLUTION GRAPH! TOTAL NUMBER OF VERTICES NOT EQUALS!" + System.lineSeparator() +
				"Before refactor: " + evolutionGraph.getVertices().size() + System.lineSeparator() + "AFER: " +
				evolutionGraphTotalVertices);
		if (evolutionGraphTotalEdges.equals(evolutionGraph.getEdges().size()))
			fail("EVOLUTION GRAPH! TOTAL NUMBER OF EDGES NOT EQUALS!" + System.lineSeparator() + "Before refactor: " +
				evolutionGraph.getEdges().size() + System.lineSeparator() + "AFER: " + evolutionGraphTotalEdges);

	}

	private void testVisualGraphNumberOfEdgesAndVertices(BufferedReader reader) throws IOException {
		VisualGraph visualGraph = viewer.layout.getGraph();
		String visualGraphTotalVertices = reader.readLine();
		String visualGraphTotalEdges = reader.readLine();
		if (visualGraph.equals(visualGraph.getVertices().size()))
			fail("VISUAL GRAPH! TOTAL NUMBER OF VERTICES NOT EQUALS!" + System.lineSeparator() + "Before refactor: " +
				visualGraph.getVertices().size() + System.lineSeparator() + "AFER: " + visualGraphTotalVertices);
		if (visualGraph.equals(visualGraph.getEdges().size()))
			fail("VISUAL GRAPH! TOTAL NUMBER OF EDGES NOT EQUALS!" + System.lineSeparator() + "Before refactor: " +
				visualGraph.getEdges().size() + System.lineSeparator() + "AFER: " + visualGraphTotalEdges);

	}

	private void testIfAllEdgesExistInRefactoredCode(BufferedReader reader) throws IOException {
		VisualGraph evolutionGraph = viewer.graph;
		HashSet<String> evolutionEdgesNames = new HashSet<>();
		for (VisualNode vertice : evolutionGraph.getVertices()) {
			evolutionEdgesNames.add(Arrays.toString(vertice.getInEdges().toArray()));
		}

		for (VisualNode vertice : evolutionGraph.getVertices()) {
			evolutionEdgesNames.add(Arrays.toString(vertice.getOutEdges().toArray()));
		}

		String line = null;
		while ((line = reader.readLine()) != null) {
			if (!evolutionEdgesNames.contains(line))
				fail("Found an edge which is not exist in refactored code");
		}
	}

}
