package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.store.schema.SchemaEntityDefinition;
import uk.gov.gchq.koryphe.ValidationResult;

import java.util.HashMap;
import java.util.Map;

public class SchemaFactory {

    private Schema schema;

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

    public SchemaEdgeDefinition createSchemaEdge(String source, String destination, String description) {
        SchemaEdgeDefinition.Builder builder = new SchemaEdgeDefinition.Builder();

        builder.source(source);
        builder.destination(destination);
        builder.description(description);

        SchemaEdgeDefinition schemaEdgeDefinition = builder.build();

        return schemaEdgeDefinition;

    }

    private SchemaEntityDefinition createSchemaEntity() {
        SchemaEntityDefinition.Builder builder = new SchemaEntityDefinition.Builder();

        builder.vertex("");
        builder.property("weight", "count");

        SchemaEntityDefinition schemaEntityDefinition = builder.build();

        return schemaEntityDefinition;
    }

    boolean isValid(Schema schema) {
        ValidationResult validationResult =  schema.validate();
        return validationResult.isValid();
    }


    public Schema createSchema() throws SerialisationException {

        Map<String, SchemaEdgeDefinition> edges = new HashMap<>();
        Map<String, SchemaEntityDefinition> entities = new HashMap<>();

        SchemaEdgeDefinition schemaEdgeDefinition = createSchemaEdge("node", "node", "A test edge");
        edges.put("testEdge", schemaEdgeDefinition);

        SchemaEntityDefinition schemaEntityDefinition = createSchemaEntity();
        entities.put("nodeStats", schemaEntityDefinition);

        UserSchema userSchema = new UserSchema(edges, entities);
        byte[] jsonBytes = JSONSerialiser.serialise(userSchema, true);
//        System.out.println(new String(jsonBytes));
        schema = Schema.fromJson(jsonBytes);

        System.out.println(schema);

        return schema;
    }


}
