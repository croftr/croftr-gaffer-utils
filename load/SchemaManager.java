package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.upload.UserEdge;
import uk.gov.gchq.gaffer.utils.upload.UserSchema;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser.serialise;

public class SchemaManager {
    public SchemaManager() {
        System.setProperty("gaffer.serialiser.json.modules", "uk.gov.gchq.gaffer.sketches.serialisation.json.SketchesJsonModules");
    }

    public Graph getExistingGraph(String graphId) {
        Graph graph = new Graph.Builder()
                .store(new ProxyStore.Builder()
                        .graphId(graphId)
                        .host("localhost")
                        .port(8080)
                        .contextRoot("rest")
                        .build())
                .build();

        return graph;
    }

    public Schema createSchema() throws SerialisationException {

        Map<String, SchemaEdgeDefinition> edges = new HashMap<>();

        SchemaEdgeDefinition.Builder builder = new SchemaEdgeDefinition.Builder();
        builder.destination("node");
        builder.source("node");
        builder.description("A test edge");

        SchemaEdgeDefinition schemaEdgeDefinition = builder.build();

        edges.put("testEdge", schemaEdgeDefinition);

        //TODO DEFINE NODE TYPES
//        TypeSubTypeValue vertex1 = new TypeSubTypeValue();
//        vertex1.setType("animal");
//        vertex1.setSubType("species");
//        vertex1.setValue("cat");
//
//        TypeSubTypeValue vertex2 = new TypeSubTypeValue();
//        vertex2.setType("animal");
//        vertex2.setSubType("species");
//        vertex2.setValue("dog");
//
//        Edge edge = new Edge.Builder()
//                .group("testEdge")
//                .source(vertex1).dest(vertex2)
//                .build();


        Map<String, Entity> entities = new HashMap<>();
        UserSchema userSchema = new UserSchema(edges);
        byte[] jsonBytes = JSONSerialiser.serialise(userSchema, true);
        System.out.println(new String(jsonBytes));
        Schema schema = Schema.fromJson(jsonBytes);
        System.out.println(schema);
        return schema;
    }
}
