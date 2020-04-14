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

                TypeSubTypeValue vertex1 = new TypeSubTypeValue();
                vertex1.setType(null);
                vertex1.setSubType(null);
                vertex1.setValue(edgeArray[FROM_NODE]);

                TypeSubTypeValue vertex2 = new TypeSubTypeValue();
                vertex2.setType(null);
                vertex2.setSubType(null);
                vertex2.setValue(edgeArray[TO_NODE]);

                Edge edge = new Edge.Builder()
                        .group(edgeArray[EDGE_TYPE])
                        .source(vertex1).dest(vertex2).directed(true)
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
