package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Entity;

import java.util.Map;

public class UserSchema {

    private Map<String, UserEdge> edges;
    private Map<String, Entity> entities;

    public UserSchema(Map<String, UserEdge> edges, Map<String, Entity> entities) {
        this.edges = edges;
        this.entities = entities;
    }

    public UserSchema() {
    }

    public Map<String, UserEdge> getEdges() {
        return edges;
    }

    public void setEdges(Map<String, UserEdge> edges) {
        this.edges = edges;
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, Entity> entities) {
        this.entities = entities;
    }
}
