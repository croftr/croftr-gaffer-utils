package uk.gov.gchq.gaffer.utils.upload.domain;

import uk.gov.gchq.gaffer.store.schema.Schema;

import java.util.Set;

public class CreateSchemaResponse {

    private Schema schema;
    private boolean loadSuccess;
    private int edgeLoadCount;
    private int rejectedEdgeLoadCount;
    private Set<String> edgeTypes;
    private int newEdgeGroupCount;
    private Set<String> newEdgeTypes;

    public CreateSchemaResponse(Schema schema, boolean loadSuccess, int edgeLoadCount, int rejectedEdgeLoadCount, Set<String> edgeTypes, int newEdgeGroupCount, Set<String> newEdgeTypes) {
        this.schema = schema;
        this.loadSuccess = loadSuccess;
        this.edgeLoadCount = edgeLoadCount;
        this.rejectedEdgeLoadCount = rejectedEdgeLoadCount;
        this.edgeTypes = edgeTypes;
        this.newEdgeGroupCount = newEdgeGroupCount;
        this.newEdgeTypes = newEdgeTypes;
    }

    public int getNewEdgeGroupCount() {
        return newEdgeGroupCount;
    }

    public void setNewEdgeGroupCount(int newEdgeGroupCount) {
        this.newEdgeGroupCount = newEdgeGroupCount;
    }

    public Set<String> getNewEdgeTypes() {
        return newEdgeTypes;
    }

    public void setNewEdgeTypes(Set<String> newEdgeTypes) {
        this.newEdgeTypes = newEdgeTypes;
    }

    public int getRejectedEdgeLoadCount() {
        return rejectedEdgeLoadCount;
    }

    public void setRejectedEdgeLoadCount(int rejectedEdgeLoadCount) {
        this.rejectedEdgeLoadCount = rejectedEdgeLoadCount;
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
