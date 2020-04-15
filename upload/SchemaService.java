package uk.gov.gchq.gaffer.utils.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.load.LoadInput;
import uk.gov.gchq.gaffer.utils.upload.domain.GraphData;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static uk.gov.gchq.gaffer.utils.upload.CsvMapper.*;

public class SchemaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaService.class);

    private SchemaFactory schemaFactory;
    private OperationExecuter operationExecuter;
    private User user;

    public SchemaService() {
        user = new User();
        GraphManager graphManager = new GraphManager();
        Graph graph = graphManager.getExistingGraph();
        operationExecuter = new OperationExecuter(graph, user);
        schemaFactory = new SchemaFactory();
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
        List<String> edges = new ArrayList<>();
        Set<String> edgeTypes = new HashSet<>();

        for (Part part : parts) {

            fileName = getFileName(part);

            InputStream inputStream = part.getInputStream();
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);

            String str;
            while ((str = reader.readLine()) != null) {
                String edgeType = str.split(",")[EDGE_TYPE];
                edgeTypes.add(edgeType);
                edges.add(str);
            }
        }

        return new GraphData(fileName, edges, edgeTypes);

    }

    public Schema createSchemaFromData(Collection<Part> parts, String graphId) throws IOException, OperationException {

        GraphData graphData = convertGraphData(parts);

        Schema schema = schemaFactory.createSchema(graphData.getEdgeTypes());

        operationExecuter.addGraph(graphId, schema);

        LoadInput loadInput = new LoadInput(",", "example/federated-demo/scripts/data/uploadData.csv", "whatever");

        List<Element> elements = new QuickStartElementFactory().createEdgesAndEntities(graphData.getEdges(), loadInput.getDelimter());

        operationExecuter.addElements(elements, graphId);

        LOGGER.info("Successfully create schama from file {}", graphData.getFileName());

        Schema createdSchema = operationExecuter.getSchema(graphId);

        return createdSchema;

    }
}
