package uk.gov.gchq.gaffer.utils.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.load.euro.EuroElementFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    public static void main(String args[]) throws OperationException {

        long startTime = System.currentTimeMillis();


        LoadInput loadInput1 = new LoadInput("\\t", "example/federated-demo/scripts/data/ESC-2016.txt", "votedFor2016");
        LoadInput loadInput2 = new LoadInput("\\t", "example/federated-demo/scripts/data/ESC-2015.txt", "votedFor2015");
        List<LoadInput> inputs = Arrays.asList(loadInput1, loadInput2);

        System.setProperty("gaffer.serialiser.json.modules", "uk.gov.gchq.gaffer.sketches.serialisation.json.SketchesJsonModules");

//        String graphId = "AnimalInteractions";
        String graphId = "euroGraph";
//        String graphId = "CovidCases";

//        String delimter = "\\t";
//        String graphData = "example/federated-demo/scripts/data/ESC-2016.txt";
//        String edgeType = "votedFor2016";

//        String delimter = " ";
//        String graphData = "example/federated-demo/scripts/data/kangaroos.txt";
//        String animalType = "Kangaroo";

//        String delimter = "\\t";
//        String graphData = "example/federated-demo/scripts/data/covidAll.txt";
//        String animalType = "Covid";

        LOGGER.info("Loading data into {} ", graphId);

        SchemaManager schemaManager = new SchemaManager();
        Graph graph = schemaManager.getExistingGraph(graphId);

        for (LoadInput input : inputs) {

            List<String> edges = new FileReader().readFile(input.getGraphData());

            List<Element> elements = new EuroElementFactory().createEdgesAndEntities(edges, input.getEdgeType(), input.getDelimter());

            AddElements addElements = new OperationsManager().addElements(elements, graphId);

            graph.execute(addElements, new User());

            long endTime = System.currentTimeMillis();

            LOGGER.info("Loaded {} {} edges in {} seconds", edges.size(), input.getEdgeType(), (endTime - startTime) / 1000);

        }
//        List<Element> elements = new AnimalElementFactory().createEdgesAndEntities(interactions, animalType, delimter);
//        List<Element> elements = new CovidElementFactory().createEdgesAndEntities(interactions, animalType, delimter);

    }
}
