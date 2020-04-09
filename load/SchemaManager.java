package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;
import uk.gov.gchq.gaffer.store.schema.Schema;
import uk.gov.gchq.gaffer.store.schema.SchemaEdgeDefinition;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.upload.UserEdge;
import uk.gov.gchq.gaffer.utils.upload.UserSchema;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser.serialise;

public class SchemaManager {
    public SchemaManager() {
        System.setProperty("gaffer.serialiser.json.modules", "uk.gov.gchq.gaffer.sketches.serialisation.json.SketchesJsonModules");
    }

    public Graph getExistingGraph(String graphId) {
        Graph graph = new Graph.Builder()
                .store(new ProxyStore.Builder()
                        .graphId(graphId)
                        .host("localhost")
                        .port(8080)
                        .contextRoot("rest")
                        .build())
                .build();

        return graph;
    }
}
