package tests;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import edu.ntua.dblab.hecataeus.HecataeusEventManagerGUI;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionGraph;

/**
 * Executes the {@link #EVENT_TYPE event} on the given {@link #NODE_NAME node} The
 * results of this is event is written to {@link #PATH file}
 */
public class ImpactAnalysisTest {

	private final String PATH = "C:\\Users\\UniQ\\Desktop\\impactAnalysis.txt";

	private final String NODE_NAME = "BIOENTRY_RELATIONSHIP_SCHEMA.BIOENTRY_RELATIONSHIP_ID"; //BIOSQL project

	private final String EVENT_TYPE = "DELETE_SELF";

	@Test
	public void test() {
		HecataeusViewer viewer = new HecataeusViewer(new EvolutionGraph());
		viewer.main(null);
		viewer.openProject();

		try {
			Path file = Paths.get(PATH);
			BufferedWriter writer = Files.newBufferedWriter(file);

			//Pick the node
			HecataeusEventManagerGUI eventManager = viewer.getEventManagerGUI();
			eventManager.setNodeLabelText(NODE_NAME);

			//Create and run the event
			EvolutionEvent event = new EvolutionEvent(EventType.toEventType(EVENT_TYPE));
			eventManager.SHOWIMPACTtest(event);

			/*
			 * Event's result. Information Area must be the same with the original.
			 * It is the easiest way to find if the same nodes deleted.
			 */
			String deletionText = viewer.getInformationAreaText();
			writer.write(deletionText);

			writer.close();
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}
	}

}
