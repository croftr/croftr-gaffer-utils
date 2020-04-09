package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.store.schema.SchemaEntityDefinition;

import java.util.Map;

public class UserSchema {

    private Map<String, SchemaEdgeDefinition> edges;
    private Map<String, SchemaEntityDefinition> entities;

    public UserSchema(Map<String, SchemaEdgeDefinition> edges, Map<String, SchemaEntityDefinition> entities) {
        this.edges = edges;
        this.entities = entities;
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
}
