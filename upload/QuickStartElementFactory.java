package uk.gov.gchq.gaffer.utils.upload;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.utils.load.SchemaElementFactrory;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.gchq.gaffer.utils.upload.CsvMapper.*;

public class QuickStartElementFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuickStartElementFactory.class);

    private static final int HLLP_PRECISION = 10;

    public List<Element> createEdgesAndEntities(List<String> stringEdges, String delimiter) {

        List<Edge> edges = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        List<Element> elements = new ArrayList<>();

        for (String stringEdge : stringEdges) {

            try {

                String[] edgeArray = stringEdge.split(delimiter);

                if (edgeArray.length < 2) {
                    continue;
                }

                String edgeType = edgeArray[EDGE_TYPE];

                TypeSubTypeValue vertex1 = new TypeSubTypeValue();
                vertex1.setType(edgeArray[FROM_NODE_TYPE]);
                vertex1.setSubType(edgeArray[FROM_NODE_SUBTYPE]);
                vertex1.setValue(edgeArray[FROM_NODE_VALUE]);

                TypeSubTypeValue vertex2 = new TypeSubTypeValue();
                vertex2.setType(edgeArray[TO_NODE_TYPE]);
                vertex2.setSubType(edgeArray[TO_NODE_SUBTYPE]);
                vertex2.setValue(edgeArray[TO_NODE_VALUE]);



                HyperLogLogPlus nodeAHllp = new HyperLogLogPlus(HLLP_PRECISION);
                HyperLogLogPlus nodeBHllp = new HyperLogLogPlus(HLLP_PRECISION);
                nodeAHllp.offer(vertex2);
                nodeBHllp.offer(vertex1);

                Entity nodeAEntity = new Entity.Builder()
                        .group("cardinality")
                        .vertex(vertex1)
                        .property("approxCardinality", nodeAHllp)
                        .build();

                Entity nodeBEntity = new Entity.Builder()
                        .group("cardinality")
                        .vertex(vertex2)
                        .property("approxCardinality", nodeBHllp)
                        .build();




                Edge edge = new Edge.Builder()
                        .group(edgeType)
                        .source(vertex1).dest(vertex2).directed(true)
                        .build();

                edges.add(edge);
                entities.add(nodeAEntity);
                entities.add(nodeBEntity);

            } catch (Exception e) {
                LOGGER.error("failed to load {} ", stringEdge);
            }
        }

        LOGGER.info("Successfully loaded {} edges ", edges.size());

        elements.addAll(entities);
        elements.addAll(edges);

        return elements;
    }
}
