package tests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import edu.ntua.dblab.hecataeus.HecataeusEventManagerGUI;
import edu.ntua.dblab.hecataeus.HecataeusViewer;
import edu.ntua.dblab.hecataeus.graph.evolution.EventType;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEvent;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.visual.VisualGraph;

public class ImpactAnalysisTest {

	private final String PATH = "C:\\Users\\Desktop\\impactAnalysis.txt";

	private final String NODE_NAME = "BIOENTRY_RELATIONSHIP_SCHEMA.BIOENTRY_RELATIONSHIP_ID"; //BIOSQL project

	private final String EVENT_TYPE = "DELETE_SELF";

	@Test
	public final void test() {
		HecataeusViewer viewer = new HecataeusViewer(new VisualGraph());
		viewer.main(null);
		viewer.openProject();

		try {

			//Pick the node
			HecataeusEventManagerGUI eventManager = viewer.getEventManagerGUI();
			eventManager.setNodeLabelText(NODE_NAME);

			//Create and run the event
			EvolutionEvent<EvolutionNode<EvolutionEdge>> event = new EvolutionEvent<>(EventType.toEventType(EVENT_TYPE));
			eventManager.SHOWIMPACTtest(event);

			/*
			 * Event's result. Information Area must be the same with the original.
			 * It is the easiest way to find if the same nodes deleted.
			 */
			String deletionText = viewer.getInformationAreaText();
			String deletionTextRead = new String(Files.readAllBytes(Paths.get(PATH)));

			if(!deletionTextRead.equals(deletionText)) {
				fail("EVENT: " + EVENT_TYPE + " ON NODE: " + NODE_NAME + " PRODUCED DIFFERENT RESULT" +
					System.lineSeparator() + "result: " + deletionTextRead);
			}
		} catch (IOException x) {
			System.out.println(x.getMessage());
		}

	}

}
