package uk.gov.gchq.gaffer.utils.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.commonutil.iterable.CloseableIterable;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.federatedstore.FederatedStoreConstants;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.operation.impl.get.GetAllElements;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.user.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    public static void loadMyData() {

        LOGGER.info("Creating graph");

        Graph graph = new Graph.Builder()
                .store(new ProxyStore.Builder()
                        .graphId("busCompanies1")
                        .host("localhost")
                        .port(8080)
                        .contextRoot("rest")
                        .build())
                .build();


        LOGGER.info("Loading data");

        Map<String, SchemaEdgeDefinition> schema = graph.getSchema().getEdges();
        schema.keySet().forEach(edge -> System.out.println("got edge " + edge));


        TypeSubTypeValue vertex1 = new TypeSubTypeValue();
        vertex1.setType("bus");
        vertex1.setSubType("small_bus");
        vertex1.setValue("Bus99");

        TypeSubTypeValue vertex2 = new TypeSubTypeValue();
        vertex2.setType("bus");
        vertex2.setSubType("small_bus");
        vertex2.setValue("Bus100");

        AddElements addElements = new AddElements.Builder()
                .option(FederatedStoreConstants.KEY_OPERATION_OPTIONS_GRAPH_IDS, "busCompanies1")
                .skipInvalidElements(false)
                .validate(true)
                .input(
                        new Entity.Builder()
                                .group("cardinality")
                                .vertex(vertex1)
                                .build(),
                        new Edge.Builder()
                                .group("isPartOfThisCompany")
                                .source(vertex1).dest(vertex2).directed(true)
                                .property("count", 999)
                                .build())
                .build();

        try {
            graph.execute(addElements, new User());
        } catch (OperationException e) {
            e.printStackTrace();
        }

        final GetAllElements operation = new GetAllElements();

        CloseableIterable<? extends Element> results = null;
        try {
            results = graph.execute(operation, new User());
        } catch (OperationException e) {
            e.printStackTrace();
        }

        for (Element result : results) {
            System.out.println("GOT " + result.toString());
        }

    }

    public static void main(String args[]) throws OperationException {

        SchemaManager schemaManager = new SchemaManager();
        Graph graph = schemaManager.getExistingGraph("kangaroos");

        Map<String, SchemaEdgeDefinition> schema = graph.getSchema().getEdges();
        schema.keySet().forEach(edge -> System.out.println("got edge type " + edge));

        List<String> interactions = new FileReader().readFile();

        List<Edge> edges = new EdgeFactory().createEdges(interactions);

        AddElements addElements = new OperationsManager().addElements(edges, Collections.emptyList());

        graph.execute(addElements, new User());

    }
}
