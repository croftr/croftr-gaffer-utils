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
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.load.FileReader;
import uk.gov.gchq.gaffer.utils.load.LoadInput;
import uk.gov.gchq.gaffer.utils.load.OperationsManager;
import uk.gov.gchq.koryphe.ValidationResult;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SchemaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaService.class);

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return "default.csv";
    }

    public void createSchemaFromData (Collection<Part> parts) throws IOException {

        String fileName = null;
        List<String> edges = new ArrayList<>();

        for (Part part : parts) {

            fileName = getFileName(part);

            InputStream inputStream  = part.getInputStream();
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
//            StringBuilder  sb = new StringBuilder();
            String str;
            while((str = reader.readLine())!= null){
                System.out.println("append>>>> " + str);
                edges.add(str);
//                sb.append(str);
            }
//            System.out.println("READ -----------------------------------------------------------");
//            System.out.println(sb.toString());
        }


        SchemaFactory schemaFactory = new SchemaFactory();
        try {


            Schema schema = schemaFactory.createSchema();
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

            LoadInput loadInput = new LoadInput(",", "example/federated-demo/scripts/data/uploadData.csv", "whatever");
//            List<String> edges = new FileReader().readFile(loadInput.getGraphData());

            List<Element> elements = new QuickStartElementFactory().createEdgesAndEntities(edges, loadInput.getEdgeType(), loadInput.getDelimter());

            System.out.println("created elements " + elements);
            System.out.println("created elements for graph " + graph.getGraphId());

            AddElements addElements = new OperationsManager().addElements(elements, publicGraph.getGraphId());

            byte[] jsonBytes1 = JSONSerialiser.serialise(addElements, true);
            System.out.println(new String(jsonBytes1));

            graph.execute(addElements, new User());

        } catch (SerialisationException | OperationException e) {
            e.printStackTrace();
        }

        LOGGER.info("Successfully create schama from file {}", fileName);

    }
}
