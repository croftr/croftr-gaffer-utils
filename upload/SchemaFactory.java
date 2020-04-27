package uk.gov.gchq.gaffer.utils.upload;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.elementdefinition.exception.SchemaException;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.sketches.clearspring.cardinality.binaryoperator.HyperLogLogPlusAggregator;
import uk.gov.gchq.gaffer.sketches.clearspring.cardinality.serialisation.HyperLogLogPlusSerialiser;
import uk.gov.gchq.gaffer.store.schema.*;
import uk.gov.gchq.gaffer.types.FreqMap;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.types.function.FreqMapAggregator;
import uk.gov.gchq.gaffer.utils.upload.domain.UserSchema;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.impl.binaryoperator.*;
import uk.gov.gchq.koryphe.impl.predicate.Exists;
import uk.gov.gchq.koryphe.impl.predicate.IsFalse;
import uk.gov.gchq.koryphe.impl.predicate.IsTrue;

import java.util.*;
import java.util.function.Predicate;

public class SchemaFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaFactory.class);

    private Schema schema;

    public SchemaEdgeDefinition createSchemaEdge(String source, String destination, String description) {
        SchemaEdgeDefinition.Builder builder = new SchemaEdgeDefinition.Builder();

        builder.source(source);
        builder.destination(destination);
        builder.description(description);
        builder.property("weight", "count");

        SchemaEdgeDefinition schemaEdgeDefinition = builder.build();

        return schemaEdgeDefinition;
    }

    private SchemaEntityDefinition createSchemaEntity(String propertyName, String propertyType) {
        SchemaEntityDefinition.Builder builder = new SchemaEntityDefinition.Builder();

        builder.vertex("node");
        builder.property(propertyName, propertyType);

        SchemaEntityDefinition schemaEntityDefinition = builder.build();

        return schemaEntityDefinition;
    }

    private SchemaEntityDefinition createSchemaEntity(Map<String, String> properties) {
        SchemaEntityDefinition.Builder builder = new SchemaEntityDefinition.Builder();
        builder.vertex("node");

        properties.keySet().forEach(key -> {
            builder.property(key, properties.get(key));
        });

        SchemaEntityDefinition schemaEntityDefinition = builder.build();

        return schemaEntityDefinition;
    }

    private TypeDefinition createSchemaType(Class<?> clazz, Predicate predicate) {
        TypeDefinition.Builder builder = new TypeDefinition.Builder();

        if (clazz.equals(String.class)) {
            builder.aggregateFunction(new Last());
        } else if (clazz.equals(Integer.class)) {
            builder.aggregateFunction(new Sum());
        } else if (clazz.equals(TypeSubTypeValue.class)) {
            //no agg function needed for these
        } else if (clazz.equals(HyperLogLogPlus.class)) {
            builder.aggregateFunction(new HyperLogLogPlusAggregator());
            builder.serialiser(new HyperLogLogPlusSerialiser());
        } else if (clazz.equals(FreqMap.class)) {
            builder.aggregateFunction(new FreqMapAggregator());
        } else if (clazz.equals(Date.class)) {
            builder.aggregateFunction(new Max());
        }

        if (predicate != null) {
            builder.validateFunctions(predicate);
        }

        TypeDefinition typeDefinition = builder.clazz(clazz).build();

        return typeDefinition;
    }

    public Schema createSchema(Set<String> edgeTypes) throws SerialisationException {

        Map<String, TypeDefinition> types = new HashMap<>();
        Map<String, SchemaEdgeDefinition> edges = new HashMap<>();
        Map<String, SchemaEntityDefinition> entities = new HashMap<>();

        TypeDefinition vertexType = createSchemaType(TypeSubTypeValue.class, null);
        types.put("node", vertexType);

        TypeDefinition weightType = createSchemaType(Integer.class, null);
        types.put("count", weightType);

        TypeDefinition trueType = createSchemaType(Boolean.class, new IsTrue());
        types.put("true", trueType);

        TypeDefinition falseType = createSchemaType(Boolean.class, new IsFalse());
        types.put("false", falseType);

        TypeDefinition hyperloglogplus = createSchemaType(HyperLogLogPlus.class, null);
        types.put("hyperloglogplus", hyperloglogplus);

        //graph stats
        TypeDefinition edgeGroupCounts = createSchemaType(FreqMap.class, new Exists());
        types.put("counts.freqmap", edgeGroupCounts);
        TypeDefinition createdDate = createSchemaType(Date.class, null);
        types.put("date.created", createdDate);
        TypeDefinition createdBy = createSchemaType(String.class, null);
        types.put("user.created", createdBy);
        TypeDefinition description = createSchemaType(String.class, null);
        types.put("graph.description", description);

        edgeTypes.forEach(edgeType -> {
            SchemaEdgeDefinition schemaEdgeDefinition = createSchemaEdge("node", "node", EgeUtils.getDescription(edgeType));
            edges.put(edgeType, schemaEdgeDefinition);
        });

        SchemaEntityDefinition nodeStatsEntity = createSchemaEntity("countValue", "count");
        entities.put("nodeStats", nodeStatsEntity);

        SchemaEntityDefinition hyperloglogplusEntity = createSchemaEntity("approxCardinality", "hyperloglogplus");
        entities.put("cardinality", hyperloglogplusEntity);

        //graph creation stats
        Map<String, String> createdProperties = new HashMap<>();
        createdProperties.put("createdDate", "date.created");
        createdProperties.put("createdBy", "user.created");
        createdProperties.put("description", "graph.description");
        SchemaEntityDefinition graphCreatedEntity = createSchemaEntity(createdProperties);
        entities.put("graphCreation", graphCreatedEntity);

        //graph status stats
        Map<String, String> statsProperties = new HashMap<>();
        statsProperties.put("edgeGroupCounts", "counts.freqmap");
        SchemaEntityDefinition graphStatusEntity = createSchemaEntity(statsProperties);
        entities.put("graphStatus", graphStatusEntity);

        UserSchema userSchema = new UserSchema(edges, entities, types);
        byte[] jsonBytes = JSONSerialiser.serialise(userSchema, true);
        schema = Schema.fromJson(jsonBytes);

        ValidationResult validationResult = schema.validate();

        if (!validationResult.isValid()) {
            throw new SchemaException("Schema has failed validation " + validationResult.getErrorString());
        }

        LOGGER.info("Successfully created Schema");

        return schema;
    }

}
