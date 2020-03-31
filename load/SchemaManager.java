package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;

public class SchemaManager {

    public void addSchema(String location) {
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
