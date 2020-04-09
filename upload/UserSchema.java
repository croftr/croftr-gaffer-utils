package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.store.schema.SchemaEntityDefinition;
import uk.gov.gchq.gaffer.store.schema.TypeDefinition;

import java.util.Map;

public class UserSchema {

    private Map<String, SchemaEdgeDefinition> edges;
    private Map<String, SchemaEntityDefinition> entities;
    private Map<String, TypeDefinition> types;

    public UserSchema(Map<String, SchemaEdgeDefinition> edges, Map<String, SchemaEntityDefinition> entities, Map<String, TypeDefinition> types) {
        this.edges = edges;
        this.entities = entities;
        this.types = types;
    }

    public Map<String, SchemaEdgeDefinition> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, SchemaEdgeDefinition> edges) {
        this.edges = edges;
    }

    public Map<String, SchemaEntityDefinition> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, SchemaEntityDefinition> entities) {
        this.entities = entities;
    }

    public Map<String, TypeDefinition> getTypes() {
        return types;
    }

    public void setTypes(Map<String, TypeDefinition> types) {
        this.types = types;
    }
}
