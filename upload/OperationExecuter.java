package uk.gov.gchq.gaffer.utils.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.accumulostore.MockAccumuloStore;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.federatedstore.FederatedStoreConstants;
import uk.gov.gchq.gaffer.federatedstore.operation.AddGraph;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.store.StoreProperties;
import uk.gov.gchq.gaffer.store.operation.GetSchema;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;

import java.util.List;

/**
 * Execute operations against the graph
 * The equivalent of POST to /rest/v2/graph/operations/execute
 */
public class OperationExecuter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationExecuter.class);

    private Graph graph;
    private User user;

    public OperationExecuter(Graph graph, User user) {
        this.graph = graph;
        this.user = user;
    }

    /**
     * Print the full JSON of the body if debug is enabled
     *
     * @param object to be printed as JSON
     */
    private void printOperation(final Object object) {
        if (LOGGER.isDebugEnabled()) {
            try {
                byte[] addGraphBytes = JSONSerialiser.serialise(object, true);
                LOGGER.debug(new String(addGraphBytes));
            } catch (SerialisationException e) {
                LOGGER.error("Cant serialize operation: {}", e.getMessage());
            }
        }
    }

    /**
     * Add a new graph to the federated store
     *
     * @param graphId id of the graph to create
     * @param schema  of the graph to create
     * @param auths   read auths of the graph to create
     * @throws OperationException
     */
    public void addGraph(String graphId, Schema schema, String auths) throws OperationException {

        boolean isPublic = auths.equals("public") ? true : false;
        String[] authsArray = {};

        //if public or private access has not been specified then there should be an array of auth groups that we can use
        if (!auths.equals("public") && !auths.equals("private")) {
            authsArray = auths.split(",");
        }

        StoreProperties storeProperties = new StoreProperties();
        storeProperties.setStoreClass(MockAccumuloStore.class);

        AddGraph addGraph = new AddGraph.Builder()
                .graphId(graphId)
                .graphAuths(authsArray)
                .schema(schema)
                .isPublic(isPublic)
                .storeProperties(storeProperties)
                .build();

        printOperation(addGraph);

        graph.execute(addGraph, user);
    }

    /**
     *
     * @param graphId of the schema to get
     * @return the schema
     * @throws OperationException
     */
    public Schema getSchema(String graphId) throws OperationException {

        GetSchema getSchema = new GetSchema.Builder()
                .option(FederatedStoreConstants.KEY_OPERATION_OPTIONS_GRAPH_IDS, graphId)
                .build();

        Schema createdSchema = graph.execute(getSchema, user);

        return createdSchema;
    }

    /**
     *
     * @param elements to be added to the graph
     * @param graphId of the graph to add the emelemts to
     * @return the addElements operation
     * @throws OperationException
     */
    public AddElements addElements(List<Element> elements, String graphId) throws OperationException {

        AddElements addElements = new AddElements.Builder()
                .option(FederatedStoreConstants.KEY_OPERATION_OPTIONS_GRAPH_IDS, graphId)
                .skipInvalidElements(false)
                .validate(true)
                .input(elements)
                .build();

        printOperation(addElements);

        graph.execute(addElements, user);

        return addElements;

    }

}


