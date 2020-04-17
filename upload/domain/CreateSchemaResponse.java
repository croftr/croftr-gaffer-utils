package uk.gov.gchq.gaffer.utils.upload.domain;

import uk.gov.gchq.gaffer.store.schema.Schema;

import java.util.Set;

public class CreateSchemaResponse {

    private Schema schema;
    private boolean loadSuccess;
    private int edgeLoadCount;
    private Set<String> edgeTypes;

    public CreateSchemaResponse(Schema schema, boolean loadSuccess, int edgeLoadCount, Set<String> edgeTypes) {
        this.schema = schema;
        this.loadSuccess = loadSuccess;
        this.edgeLoadCount = edgeLoadCount;
        this.edgeTypes = edgeTypes;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public boolean isLoadSuccess() {
        return loadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        this.loadSuccess = loadSuccess;
    }

    public int getEdgeLoadCount() {
        return edgeLoadCount;
    }

    public void setEdgeLoadCount(int edgeLoadCount) {
        this.edgeLoadCount = edgeLoadCount;
    }

    public Set<String> getEdgeTypes() {
        return edgeTypes;
    }

    public void setEdgeTypes(Set<String> edgeTypes) {
        this.edgeTypes = edgeTypes;
    }
}
