package uk.gov.gchq.gaffer.utils.load.gremlin;

import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.GraphWriter;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONReader;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.utils.load.FileReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

public class GremlinLoader {

    static final int FROM_VERTEX = 0;
    static final int TO_VERTEX = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinLoader.class);

    private GraphTraversalSource g;


    public List<String> shortestPath(Integer nodeId1, Integer nodeId2) {
        GraphTraversalSource g = loadGraph();

        //find shorest path between nodes
        List<Path> listResults = new ArrayList();
        g.V(nodeId1).repeat(out().simplePath()).until(hasId(nodeId2)).path().limit(1).fill(listResults);
        if (listResults.size() == 0) {
            g.V(nodeId2).repeat(both().simplePath()).until(hasId(nodeId1)).path().limit(1).fill(listResults);
        }
//        listResults.forEach(t -> System.out.println("t " + t.toString() + " " + t.size()));

        List<String> pathIds = new ArrayList<>();
        listResults.forEach(path -> {
            path.objects().forEach(i-> pathIds.add( i.toString()));
        });

        return pathIds;
    }

    public GraphTraversalSource loadGraph() {
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

        long endTime = System.currentTimeMillis();
        LOGGER.info("Loaded {} interactions in {} seconds", interactions.size(), (endTime - startTime / 1000));


        try {

            GraphSONWriter graphSONWriter = GraphSONWriter.build().create();
            final OutputStream os = new FileOutputStream("tinkerpop-modern.json");
            graphSONWriter.writeGraph(os,graph);


            GraphSONReader graphSONReader = GraphSONReader.build().create();




//            final GraphWriter writer = (GraphWriter) graph..graphson()).writer();
//            final OutputStream os = new FileOutputStream("tinkerpop-modern.json");
//            writer.writeGraph(os, graph);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return g;
    }

    public static void main(String args[]) throws OperationException {
        GremlinLoader gremlinLoader = new GremlinLoader();
        gremlinLoader.loadGraph();
//        List<String> paths = gremlinLoader.shortestPath(61, 7);
        List<String> paths = gremlinLoader.shortestPath(36, 61);
        System.out.println("path ids  " + paths);
    }
}
