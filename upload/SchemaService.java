package uk.gov.gchq.gaffer.utils.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateElementsResponse;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateSchemaResponse;
import uk.gov.gchq.gaffer.utils.upload.domain.GraphData;
import uk.gov.gchq.gaffer.utils.upload.load.QuickStartElementFactory;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

/**
 * Entry point for creating a schema or loading data into a schame from a file
 */
public class SchemaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaService.class);

    private SchemaDefinitionFactory schemaFactory;
    private OperationExecuter operationExecuter;
    private QuickStartElementFactory quickStartElementFactory;
    private ProxyGraphManager proxyGraphManager;
    private User user;

    public SchemaService() {
        final List<String> opAuths = Arrays.asList("Auth1","Auth2"); // authorisations for running different Gaffer operations
        final List<String> dataAuths = Arrays.asList("Auth1","Auth2"); // authorisations for accessing data
        user =  new User.Builder()
                .userId("Rob")
                .opAuths(opAuths)
                .dataAuths(dataAuths)
                .build();

        schemaFactory = new SchemaDefinitionFactory();
        proxyGraphManager = new ProxyGraphManager();
        quickStartElementFactory = new QuickStartElementFactory();
    }


    /**
     *
     * @param parts containing the file data to be loaded into the graph from the multipart/form-data POST request
     * @param graphId id of the graph to create
     * @param auths the read auths for the graph to be created
     * @param description of the graph to create
     * @return the JSON of the schema created and a summary of edge types and counts loaded
     * @throws IOException
     * @throws OperationException
     */
    public CreateSchemaResponse createSchemaFromData(Collection<Part> parts, String graphId, String auths, String description, String delimiter) throws IOException, OperationException {

        Graph graph = proxyGraphManager.getExistingGraph("");
        operationExecuter = new OperationExecuter(graph, user);

        GraphData graphData = GraphDataUtils.convertGraphData(parts, delimiter);

        Schema schema = schemaFactory.createSchema(graphData.getEdgeTypes());

        operationExecuter.addGraph(graphId, schema, auths);

        CreateElementsResponse createElementsResponse =
                quickStartElementFactory.createEdgesAndEntities(graphData.getEdges(), delimiter, graphData.isSimpleFile(), description, user);

        operationExecuter.addElements(createElementsResponse.getElements(), graphId);

        LOGGER.info("Successfully create schama from file {}", graphData.getFileName());

        Schema createdSchema = operationExecuter.getSchema(graphId);

        boolean success = createElementsResponse.getEdgeCount() > 0;

        return new CreateSchemaResponse(
                createdSchema,
                success,
                createElementsResponse.getEdgeCount(),
                createElementsResponse.getRejectedEdgeLoadCount(),
                createElementsResponse.getEdgeTypes(),
                createElementsResponse.getNewEdgeGroupCount(),
                createElementsResponse.getNewEdgeTypes()
        );
    }

    /**
     *
     * @param parts containing the file data to be loaded into the graph from the multipart/form-data POST request
     * @param graphId id of the graph to load data into
     * @return a summary of edge types and counts loaded
     * @throws IOException
     * @throws OperationException
     */
    public CreateSchemaResponse loadData(Collection<Part> parts, String graphId, String delimiter) throws IOException, OperationException {

        LOGGER.info("Loading data into {}", graphId);

        Graph graph = proxyGraphManager.getExistingGraph(graphId);
        operationExecuter = new OperationExecuter(graph, user);

        GraphData graphData = GraphDataUtils.convertGraphData(parts, delimiter);

        Schema schema = operationExecuter.getSchema(graphId);
        Set<String> currentEdgeGroups = schema.getEdgeGroups();

        CreateElementsResponse createElementsResponse = quickStartElementFactory.addElements(graphData.getEdges(), delimiter, graphData.isSimpleFile(), currentEdgeGroups);

        operationExecuter.addElements(createElementsResponse.getElements(), graphId);

        LOGGER.info("Successfully loaded data from file {}", graphData.getFileName());

        Schema createdSchema = operationExecuter.getSchema(graphId);

        boolean success = createElementsResponse.getEdgeCount() > 0;

        return new CreateSchemaResponse(
                createdSchema,
                success,
                createElementsResponse.getEdgeCount(),
                createElementsResponse.getRejectedEdgeLoadCount(),
                createElementsResponse.getEdgeTypes(),
                createElementsResponse.getNewEdgeGroupCount(),
                createElementsResponse.getNewEdgeTypes()
        );
    }
}
