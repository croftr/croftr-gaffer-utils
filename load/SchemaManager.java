package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;

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
