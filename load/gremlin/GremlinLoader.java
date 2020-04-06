package uk.gov.gchq.gaffer.utils.load.gremlin;

import com.google.inject.internal.util.$SourceProvider;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.Scope;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.utils.load.FileReader;

import java.util.*;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

public class GremlinLoader {

    static final int FROM_VERTEX = 0;
    static final int TO_VERTEX = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinLoader.class);

    public static void main(String args[]) throws OperationException {

        long startTime = System.currentTimeMillis();

        String graphId = "AnimalInteractions";
        String delimiter = "\\t";
        String graphData = "example/federated-demo/scripts/data/dolphins.txt";
        String animalType = "Dolphin";

        LOGGER.info("Loading {} data into {} ", animalType, graphId);

        List<String> interactions = new FileReader().readFile(graphData);

        Set<Integer> uniqueIds = new HashSet<>();

        for (String stringEdge : interactions) {
            String[] edgeArray = stringEdge.split(delimiter);
            uniqueIds.add(Integer.parseInt(edgeArray[FROM_VERTEX]));
            uniqueIds.add(Integer.parseInt(edgeArray[TO_VERTEX]));
        }

        Graph graph = TinkerGraph.open();
        GraphTraversalSource g = graph.traversal();

        Map<Integer, Vertex> vertexMap = new HashMap<>();
        for (Integer id : uniqueIds) {
            Vertex vertex = graph.addVertex(T.label, "dolphin", T.id, id);
            vertexMap.put(id, vertex);
        }



        for (String stringEdge : interactions) {

            String[] edgeArray = stringEdge.split(delimiter);

            Vertex fromVertex = vertexMap.get(Integer.parseInt(edgeArray[FROM_VERTEX]));
            Vertex toVertex = vertexMap.get(Integer.parseInt(edgeArray[TO_VERTEX]));

            fromVertex.addEdge("interaction", toVertex);
        }

//        g.V().toSet().forEach(v -> System.out.println("got vertex "+ v.id()) );

//        List<Vertex> listResults = new ArrayList();
//        g.V().fill(listResults);
//        listResults.forEach(t -> System.out.println("t " + t.id() + " " + t.label()));




        //find shorest path between nodes
        List<Path> listResults = new ArrayList();
        g.V(61).repeat(out().simplePath()).until(hasId(7)).path().limit(1).fill(listResults);
//        g.V(61).repeat(out().simplePath()).until(hasId(40)).path().limit(1).fill(listResults);
        listResults.forEach(t -> System.out.println("t " + t.toString() + " " + t.size()));

        long endTime = System.currentTimeMillis();
        LOGGER.info("Loaded {} interactions in {} seconds", interactions.size(), (endTime - startTime / 1000));

    }
}
