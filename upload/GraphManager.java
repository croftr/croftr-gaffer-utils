package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;
import uk.gov.gchq.gaffer.store.schema.Schema;

import java.util.Properties;

public class GraphManager {

    public GraphManager() {
        System.setProperty("gaffer.serialiser.json.modules", "uk.gov.gchq.gaffer.sketches.serialisation.json.SketchesJsonModules");
    }

    public Graph getExistingGraph() {

        Graph graph = new Graph.Builder()
                .store(new ProxyStore.Builder()
                        .graphId("")  // for some reason this is needed
                        .host("localhost")
                        .port(8080)
                        .contextRoot("rest")
                        .build())
                .build();

        return graph;
    }
}
