package uk.gov.gchq.gaffer.utils.upload;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.utils.load.SchemaElementFactrory;

import java.util.ArrayList;
import java.util.List;

public class QuickStartElementFactory implements SchemaElementFactrory {

    private static final int HLLP_PRECISION = 10;

    static final int FROM_NODE = 0;
    static final int EDGE_TYPE = 1;
    static final int TO_NODE = 2;

    @Override
    public List<Element> createEdgesAndEntities(List<String> stringEdges, String edgeType, String delimiter) {

        List<Edge> edges = new ArrayList<>();
        List<Element> elements = new ArrayList<>();

        for (String stringEdge : stringEdges) {

            try {

                String[] edgeArray = stringEdge.split(delimiter);

                if (edgeArray.length < 2) {
                    continue;
                }

                String nodeA = edgeArray[FROM_NODE];
                String nodeB = edgeArray[TO_NODE];

                Edge edge = new Edge.Builder()
                        .group(edgeArray[EDGE_TYPE])
                        .source(nodeA).dest(nodeB).directed(true)
                        .build();

                edges.add(edge);

            } catch (Exception e) {
                System.out.println("failed to load " + stringEdge);
                throw e;
            }
        }

        elements.addAll(edges);

        return elements;
    }
}
