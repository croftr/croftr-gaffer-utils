package uk.gov.gchq.gaffer.utils.upload.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.gchq.gaffer.utils.upload.CsvMapper.*;

public class QuickStartElementFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuickStartElementFactory.class);

    private CentralityManager centralityManager;

    public QuickStartElementFactory() {
        centralityManager = new CentralityManager();
    }

    public List<Element> createEdgesAndEntities(List<String> stringEdges, String delimiter) {

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

                elements.addAll(centralityManager.generateEntityCentrality(vertex1, vertex2));

                Integer edgeWeight = 1;
                if (edgeArray[EDGE_WEIGHT] != null ) {
                    edgeWeight = Integer.parseInt(edgeArray[EDGE_WEIGHT]);
                }

                Edge edge = new Edge.Builder()
                        .group(edgeType)
                        .property("weight", edgeWeight)
                        .source(vertex1).dest(vertex2).directed(true)
                        .build();

                elements.add(edge);

            } catch (Exception e) {
                LOGGER.error("failed to load {} ", stringEdge);
            }
        }

        LOGGER.info("Successfully loaded {} elements ", elements.size());

        return elements;
    }
}
