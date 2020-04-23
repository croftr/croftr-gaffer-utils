package uk.gov.gchq.gaffer.utils.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.elementdefinition.exception.SchemaException;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.load.LoadInput;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateElementsResponse;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateSchemaResponse;
import uk.gov.gchq.gaffer.utils.upload.domain.GraphData;
import uk.gov.gchq.gaffer.utils.upload.load.QuickStartElementFactory;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static uk.gov.gchq.gaffer.utils.upload.CsvMapper.*;

public class SchemaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaService.class);

    private static final int SIMPLE_FILE_COLUMN_COUNT = 2;
    private static final int DETAIL_FILE_COLUMN_COUNT = 9;

    private SchemaFactory schemaFactory;
    private OperationExecuter operationExecuter;
    private QuickStartElementFactory quickStartElementFactory;
    private User user;

    public SchemaService() {
        user = new User();
        schemaFactory = new SchemaFactory();
        quickStartElementFactory = new QuickStartElementFactory();
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");

        if (contentDisposition != null) {
            for (String content : contentDisposition.split(";")) {
                if (content.trim().startsWith("filename"))
                    return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }

        return "default.csv";
    }

    private GraphData convertGraphData(Collection<Part> parts) throws IOException {

        String fileName = null;
        boolean isSimpleFile = false;
        List<String> edges = new ArrayList<>();
        Set<String> edgeTypes = new HashSet<>();

        for (Part part : parts) {

            fileName = getFileName(part);

            InputStream inputStream = part.getInputStream();
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);

            String firstLine = reader.readLine();
            String[] firstLineArray = firstLine.split(",");
            if (firstLineArray.length == SIMPLE_FILE_COLUMN_COUNT) {
                isSimpleFile = true;
            } else if (firstLineArray.length != DETAIL_FILE_COLUMN_COUNT) {
                throw new SchemaException("CSV files must contain either " + SIMPLE_FILE_COLUMN_COUNT + " or " + DETAIL_FILE_COLUMN_COUNT + " columns");
            }

            String str;
            while ((str = reader.readLine()) != null) {
                String[] edgeArray = str.split(",");
                String edgeType = isSimpleFile ? DEFAULT_EDGE_TYPE : edgeArray[EDGE_TYPE];
                edgeTypes.add(edgeType);
                edges.add(str);
            }
        }

        return new GraphData(fileName, edges, edgeTypes, isSimpleFile);

    }

    public CreateSchemaResponse createSchemaFromData(Collection<Part> parts, String graphId, String auths) throws IOException, OperationException {

        GraphManager graphManager = new GraphManager();
        Graph graph = graphManager.getExistingGraph("");
        operationExecuter = new OperationExecuter(graph, user);

        GraphData graphData = convertGraphData(parts);

        Schema schema = schemaFactory.createSchema(graphData.getEdgeTypes());

        operationExecuter.addGraph(graphId, schema, auths);

        LoadInput loadInput = new LoadInput(",", "example/federated-demo/scripts/data/uploadData.csv", "whatever");

        CreateElementsResponse createElementsResponse = quickStartElementFactory.createEdgesAndEntities(graphData.getEdges(), loadInput.getDelimter(), graphData.isSimpleFile());

        operationExecuter.addElements(createElementsResponse.getElements(), graphId);

        LOGGER.info("Successfully create schama from file {}", graphData.getFileName());

        Schema createdSchema = operationExecuter.getSchema(graphId);

        boolean success = createElementsResponse.getEdgeCount() > 0;

        return new CreateSchemaResponse(createdSchema, success, createElementsResponse.getEdgeCount(), createElementsResponse.getRejectedEdgeLoadCount(), createElementsResponse.getEdgeTypes(), createElementsResponse.getNewEdgeGroupCount(), createElementsResponse.getNewEdgeTypes());

    }

    public CreateSchemaResponse loadData(Collection<Part> parts, String graphId) throws IOException, OperationException {

        LOGGER.info("Loading data into {}", graphId);

        GraphManager graphManager = new GraphManager();
        Graph graph = graphManager.getExistingGraph(graphId);
        operationExecuter = new OperationExecuter(graph, user);

        GraphData graphData = convertGraphData(parts);

        Schema schema = operationExecuter.getSchema(graphId);
        Set<String> currentEdgeGroups = schema.getEdgeGroups();

        LoadInput loadInput = new LoadInput(",", "example/federated-demo/scripts/data/uploadData.csv", "whatever");

        CreateElementsResponse createElementsResponse = quickStartElementFactory.addElements(graphData.getEdges(), loadInput.getDelimter(), graphData.isSimpleFile(), currentEdgeGroups);

        operationExecuter.addElements(createElementsResponse.getElements(), graphId);

        LOGGER.info("Successfully loaded data from file {}", graphData.getFileName());

        Schema createdSchema = operationExecuter.getSchema(graphId);

        boolean success = createElementsResponse.getEdgeCount() > 0;

        return new CreateSchemaResponse(createdSchema, success, createElementsResponse.getEdgeCount(), createElementsResponse.getRejectedEdgeLoadCount(), createElementsResponse.getEdgeTypes(), createElementsResponse.getNewEdgeGroupCount(), createElementsResponse.getNewEdgeTypes());

    }
}
