package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.ArrayList;
import java.util.List;

public class EdgeFactory {

    static final int FROM_NODE = 0;
    static final int TO_NODE = 1;
    static final int EDGE_WEIGHT = 2;

    public List<Edge> createEdges(List<String> stringEdges) {

        List<Edge> edges = new ArrayList<>();

        stringEdges.forEach(stringEdge -> {

            String[] edgeArray = stringEdge.split(" ");

            TypeSubTypeValue vertex1 = new TypeSubTypeValue();
            vertex1.setType("kangaroo");
            vertex1.setSubType("western grey kangaroo.");
            vertex1.setValue("Id:"+edgeArray[FROM_NODE]);

            TypeSubTypeValue vertex2 = new TypeSubTypeValue();
            vertex2.setType("kangaroo");
            vertex2.setSubType("western grey kangaroo.");
            vertex2.setValue("Id:"+edgeArray[TO_NODE]);

            Edge edge = new Edge.Builder()
                    .group("interaction")
                    .source(vertex1).dest(vertex2).directed(false)
                    .property("count", Integer.parseInt(edgeArray[EDGE_WEIGHT]))
                    .build();

            edges.add(edge);

        });

        return edges;
    }
}
