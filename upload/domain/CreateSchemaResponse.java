package uk.gov.gchq.gaffer.utils.upload.domain;

import uk.gov.gchq.gaffer.store.schema.Schema;

public class CreateSchemaResponse {

    private Schema schema;
    private boolean loadSuccess;
    private int edgeLoadCount;

    public CreateSchemaResponse(Schema schema, boolean loadSuccess, int edgeLoadCount) {
        this.schema = schema;
        this.loadSuccess = loadSuccess;
        this.edgeLoadCount = edgeLoadCount;
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
}
