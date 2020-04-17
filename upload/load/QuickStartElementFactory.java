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

    private void mapSimpleEdge(String[] edgeArray, List<Element> elements) {

        TypeSubTypeValue vertex1 = createNode(null, null, edgeArray[SIMPLE_FROM_NODE_VALUE]);
        TypeSubTypeValue vertex2 = createNode(null, null, edgeArray[SIMPLE_TO_NODE_VALUE]);

        elements.addAll(centralityManager.generateEntityCentrality(vertex1, vertex2));

        Edge edge = createEdge(vertex1, vertex2, DEFAULT_EDGE_TYPE, null, null);

        elements.add(edge);
    }

    private TypeSubTypeValue createNode(String type, String subType, String value) {
        TypeSubTypeValue node = new TypeSubTypeValue();
        node.setType(type);
        node.setSubType(subType);
        node.setValue(value);
        return node;
    }

    private Edge createEdge(TypeSubTypeValue vertex1, TypeSubTypeValue vertex2, String edgeType, String directed, String weight) {

        boolean isDirected = Boolean.parseBoolean(directed);

        Integer edgeWeight = 1;
        if (weight != null ) {
            edgeWeight = Integer.parseInt(weight);
        }

        Edge edge = new Edge.Builder()
                .group(edgeType)
                .property("weight", edgeWeight)
                .source(vertex1).dest(vertex2).directed(isDirected)
                .build();

        return edge;
    }

    private void mapDetailEdge(String[] edgeArray, List<Element> elements) {

        TypeSubTypeValue vertex1 = createNode(edgeArray[FROM_NODE_TYPE], edgeArray[FROM_NODE_SUBTYPE], edgeArray[FROM_NODE_VALUE]);
        TypeSubTypeValue vertex2 = createNode(edgeArray[TO_NODE_TYPE], edgeArray[TO_NODE_SUBTYPE], edgeArray[TO_NODE_VALUE]);

        elements.addAll(centralityManager.generateEntityCentrality(vertex1, vertex2));

        Edge edge = createEdge(vertex1, vertex2, edgeArray[EDGE_TYPE], edgeArray[DIRECTED], edgeArray[EDGE_WEIGHT]);

        elements.add(edge);
    }


    public List<Element> createEdgesAndEntities(List<String> stringEdges, String delimiter, boolean simpleFile) {

        List<Element> elements = new ArrayList<>();

        for (String stringEdge : stringEdges) {

            try {

                String[] edgeArray = stringEdge.split(delimiter);

                if (edgeArray.length < 2) {
                    continue;
                }

                if (simpleFile) {
                    mapSimpleEdge(edgeArray,  elements);
                } else {
                    mapDetailEdge(edgeArray,  elements);
                }

            } catch (Exception e) {
                LOGGER.error("failed to load {} ", stringEdge, e);
            }
        }

        LOGGER.info("Successfully loaded {} elements ", elements.size());

        return elements;
    }
}
