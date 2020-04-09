package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.store.schema.*;
import uk.gov.gchq.koryphe.ValidationResult;

import java.util.HashMap;
import java.util.Map;

public class SchemaFactory {

    private Schema schema;

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

        builder.vertex("node");
        builder.property("weight", "count");

        SchemaEntityDefinition schemaEntityDefinition = builder.build();

        return schemaEntityDefinition;
    }

    private TypeDefinition createSchemaType(Class<?> clazz) {
        TypeDefinition.Builder builder = new TypeDefinition.Builder();

        TypeDefinition typeDefinition = builder.build();
        typeDefinition.setClazz(clazz);

        return typeDefinition;
    }

    boolean isValid(Schema schema) {
        ValidationResult validationResult =  schema.validate();
        return validationResult.isValid();
    }

    public Schema createSchema() throws SerialisationException {

        Map<String, TypeDefinition> types = new HashMap<>();
        Map<String, SchemaEdgeDefinition> edges = new HashMap<>();
        Map<String, SchemaEntityDefinition> entities = new HashMap<>();

        SchemaEdgeDefinition schemaEdgeDefinition = createSchemaEdge("node", "node", "A test edge");
        edges.put("testEdge", schemaEdgeDefinition);

        SchemaEntityDefinition schemaEntityDefinition = createSchemaEntity();
        entities.put("nodeStats", schemaEntityDefinition);

        TypeDefinition vertexType = createSchemaType(String.class);
        types.put("node", vertexType);

        TypeDefinition weightType = createSchemaType(Integer.class);
        types.put("count", weightType);

        UserSchema userSchema = new UserSchema(edges, entities, types);
        byte[] jsonBytes = JSONSerialiser.serialise(userSchema, true);
//        System.out.println(new String(jsonBytes));
        schema = Schema.fromJson(jsonBytes);

        System.out.println(schema);

        return schema;
    }


}
