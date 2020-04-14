package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.accumulostore.MockAccumuloStore;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.federatedstore.operation.AddGraph;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.store.StoreProperties;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.load.FileReader;
import uk.gov.gchq.gaffer.utils.load.LoadInput;
import uk.gov.gchq.gaffer.utils.load.OperationsManager;
import uk.gov.gchq.koryphe.ValidationResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadRunner {

    public static void main(String[] args) {
        SchemaFactory schemaFactory = new SchemaFactory();
        try {

            Set<String> edgeTypes = new HashSet<>();
            edgeTypes.add("interaction");

            Schema schema = schemaFactory.createSchema(edgeTypes);
            ValidationResult s = schema.validate();
            System.out.println(s.isValid());

            GraphManager graphManager = new GraphManager();
            Graph graph = graphManager.getExistingGraph();

            StoreProperties storeProperties = new StoreProperties();
            storeProperties.setStoreClass(MockAccumuloStore.class);

            AddGraph publicGraph = new AddGraph.Builder()
                    .graphId("testGraph")
                    .schema(schema)
                    .isPublic(true)
                    .storeProperties(storeProperties)
                    .build();

            byte[] jsonBytes = JSONSerialiser.serialise(publicGraph, true);
            System.out.println(new String(jsonBytes));

            graph.execute(publicGraph, new User());

            LoadInput loadInput = new LoadInput("\\t", "example/federated-demo/scripts/data/uploadData.csv", "whatever");
            List<String> edges = new FileReader().readFile(loadInput.getGraphData());
            List<Element> elements = new QuickStartElementFactory().createEdgesAndEntities(edges, loadInput.getEdgeType(), loadInput.getDelimter());

            AddElements addElements = new OperationsManager().addElements(elements, "testGraph");

            graph.execute(addElements, new User());

        } catch (SerialisationException | OperationException e) {
            e.printStackTrace();
        }
    }
}
