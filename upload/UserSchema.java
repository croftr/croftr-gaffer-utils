package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;

import java.util.Map;

public class UserSchema {

    private Map<String, SchemaEdgeDefinition> edges;

    public UserSchema(Map<String, SchemaEdgeDefinition> edges) {
        this.edges = edges;
    }

    public UserSchema() {
    }

    public Map<String, SchemaEdgeDefinition> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, SchemaEdgeDefinition> edges) {
        this.edges = edges;
    }
}
