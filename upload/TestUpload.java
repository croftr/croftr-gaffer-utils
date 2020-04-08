package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.utils.load.SchemaManager;

public class TestUpload {

    public static void main(String args[]) throws SerialisationException {
        SchemaManager schemaManager = new SchemaManager();
        Schema schema = schemaManager.createSchema();
    }
}
