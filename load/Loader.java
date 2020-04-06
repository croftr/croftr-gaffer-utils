package uk.gov.gchq.gaffer.utils.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.user.User;

import java.util.List;
import java.util.Map;

public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    public static void main(String args[]) throws OperationException {

        long startTime = System.currentTimeMillis();

        System.setProperty("gaffer.serialiser.json.modules", "uk.gov.gchq.gaffer.sketches.serialisation.json.SketchesJsonModules");

        String graphId = "AnimalInteractions";
//        String graphId = "CovidCases";

        String delimter = "\\t";
        String graphData = "example/federated-demo/scripts/data/dolphins.txt";
        String animalType = "Dolphin";

//        String delimter = " ";
//        String graphData = "example/federated-demo/scripts/data/kangaroos.txt";
//        String animalType = "Kangaroo";

//        String delimter = "\\t";
//        String graphData = "example/federated-demo/scripts/data/covidAll.txt";
//        String animalType = "Covid";

        LOGGER.info("Loading {} data into {} ", animalType, graphId);

        SchemaManager schemaManager = new SchemaManager();
        Graph graph = schemaManager.getExistingGraph(graphId);

        List<String> interactions = new FileReader().readFile(graphData);

        List<Element> elements = new AnimalElementFactory().createEdgesAndEntities(interactions, animalType, delimter);
//        List<Element> elements = new CovidElementFactory().createEdgesAndEntities(interactions, animalType, delimter);

        AddElements addElements = new OperationsManager().addElements(elements, graphId);

        graph.execute(addElements, new User());

        long endTime = System.currentTimeMillis();

        LOGGER.info("Loaded {} interactions in {} seconds", interactions.size(), (endTime - startTime / 1000));

    }
}
