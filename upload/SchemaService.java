package uk.gov.gchq.gaffer.utils.upload;

import com.google.inject.internal.util.$SourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.accumulostore.MockAccumuloStore;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.federatedstore.operation.AddGraph;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;
import uk.gov.gchq.gaffer.store.StoreProperties;
import uk.gov.gchq.gaffer.store.operation.GetSchema;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.load.FileReader;
import uk.gov.gchq.gaffer.utils.load.LoadInput;
import uk.gov.gchq.gaffer.utils.load.OperationsManager;
import uk.gov.gchq.gaffer.utils.upload.domain.GraphData;
import uk.gov.gchq.koryphe.ValidationResult;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class SchemaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaService.class);

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

    GraphData convertGraphData(Collection<Part> parts) throws IOException {

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
                System.out.println("append>>>> " + str);
                String edgeType = str.split(",")[1];

                edgeTypes.add(edgeType);
                edges.add(str);

            }
        }

        return new GraphData(fileName, edges, edgeTypes);

    }

    public Schema createSchemaFromData(Collection<Part> parts, String graphId) throws IOException, OperationException {

        GraphData graphData = convertGraphData(parts);

        SchemaFactory schemaFactory = new SchemaFactory();

        Schema schema = schemaFactory.createSchema(graphData.getEdgeTypes());
        ValidationResult s = schema.validate();
        System.out.println(s.isValid());

        GraphManager graphManager = new GraphManager();
        Graph graph = graphManager.getExistingGraph();

        StoreProperties storeProperties = new StoreProperties();
        storeProperties.setStoreClass(MockAccumuloStore.class);

        AddGraph publicGraph = new AddGraph.Builder()
                .graphId(graphId)
                .schema(schema)
                .isPublic(true)
                .storeProperties(storeProperties)
                .build();

        byte[] jsonBytes = JSONSerialiser.serialise(publicGraph, true);
        System.out.println(new String(jsonBytes));

        graph.execute(publicGraph, new User());

        LoadInput loadInput = new LoadInput(",", "example/federated-demo/scripts/data/uploadData.csv", "whatever");

        List<Element> elements = new QuickStartElementFactory().createEdgesAndEntities(graphData.getEdges(), loadInput.getEdgeType(), loadInput.getDelimter());

        AddElements addElements = new OperationsManager().addElements(elements, publicGraph.getGraphId());

        byte[] jsonBytes1 = JSONSerialiser.serialise(addElements, true);
        System.out.println(new String(jsonBytes1));

        graph.execute(addElements, new User());

        LOGGER.info("Successfully create schama from file {}", graphData.getFileName());

        GetSchema getSchema = new GetSchema.Builder()
                 .option("gaffer.federatedstore.operation.graphIds", graphId)
                 .build();

        byte[] jsonBytes2 = JSONSerialiser.serialise(getSchema, true);
        System.out.println(new String(jsonBytes2));

        Schema createdSchema = graph.execute(getSchema, new User());

        return createdSchema;

    }
}
