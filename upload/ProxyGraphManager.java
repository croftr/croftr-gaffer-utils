package uk.gov.gchq.gaffer.utils.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;

/**
 * Get the instance of the currently running graph
 */
public class ProxyGraphManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyGraphManager.class);

    public ProxyGraphManager() {
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
