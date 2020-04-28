package uk.gov.gchq.gaffer.utils.upload.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.elementdefinition.exception.SchemaException;
import uk.gov.gchq.gaffer.types.FreqMap;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.user.User;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateElementsResponse;

import java.util.*;

import static uk.gov.gchq.gaffer.utils.upload.CsvMapper.*;

public class QuickStartElementFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuickStartElementFactory.class);

    private CentralityManager centralityManager;
    private GraphStatsManager graphStatsManager;

    public QuickStartElementFactory() {
        centralityManager = new CentralityManager();
        graphStatsManager = new GraphStatsManager();
    }

    private void mapSimpleEdge(String[] edgeArray, List<Element> elements, Set<String> edgeTypes) {

        TypeSubTypeValue vertex1 = createNode(null, null, edgeArray[SIMPLE_FROM_NODE_VALUE]);
        TypeSubTypeValue vertex2 = createNode(null, null, edgeArray[SIMPLE_TO_NODE_VALUE]);

        elements.addAll(centralityManager.generateEntityCentrality(vertex1, vertex2));

        Edge edge = createEdge(vertex1, vertex2, DEFAULT_EDGE_TYPE, null, null);

        elements.add(edge);
        edgeTypes.add(DEFAULT_EDGE_TYPE);
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

    private void mapDetailEdge(String[] edgeArray, List<Element> elements, Set<String> edgeTypes) {

        TypeSubTypeValue vertex1 = createNode(edgeArray[FROM_NODE_TYPE], edgeArray[FROM_NODE_SUBTYPE], edgeArray[FROM_NODE_VALUE]);
        TypeSubTypeValue vertex2 = createNode(edgeArray[TO_NODE_TYPE], edgeArray[TO_NODE_SUBTYPE], edgeArray[TO_NODE_VALUE]);

        elements.addAll(centralityManager.generateEntityCentrality(vertex1, vertex2));

        Edge edge = createEdge(vertex1, vertex2, edgeArray[EDGE_TYPE], edgeArray[DIRECTED], edgeArray[EDGE_WEIGHT]);

        elements.add(edge);
        edgeTypes.add(edgeArray[EDGE_TYPE]);
    }

    public CreateElementsResponse createEdgesAndEntities(List<String> stringEdges, String delimiter, boolean simpleFile, String description, User user) {

        List<Element> elements = new ArrayList<>();
        Set<String> edgeTypes = new HashSet<>();
        FreqMap freqMap = new FreqMap(new HashMap());
        int edgeCounts = 0;
        int rejectedEdgeLoadCount = 0;

        for (String stringEdge : stringEdges) {

            try {

                String[] edgeArray = stringEdge.split(delimiter);

                String edgeType = simpleFile ? DEFAULT_EDGE_TYPE : edgeArray[EDGE_TYPE];

                if (edgeArray.length < 2) {
                    continue;
                }

                if (simpleFile) {
                    mapSimpleEdge(edgeArray, elements, edgeTypes);
                } else {
                    mapDetailEdge(edgeArray, elements, edgeTypes);
                }

                freqMap.upsert(edgeType, 1l);
                edgeCounts++;

            } catch (Exception e) {
                LOGGER.error("failed to load {} ", stringEdge, e);
                rejectedEdgeLoadCount++;
            }
        }

        elements.add(graphStatsManager.setCreatedGraphStats(description, user));
        elements.add(graphStatsManager.updateGraphStats(freqMap));

        LOGGER.info("Successfully loaded {} elements ", elements.size());

        return new CreateElementsResponse(elements, edgeTypes, edgeCounts, rejectedEdgeLoadCount,0, new HashSet<>());
    }

    public CreateElementsResponse addElements(List<String> stringEdges, String delimiter, boolean simpleFile, Set<String> currentEdgeGroups) {

        List<Element> elements = new ArrayList<>();
        Set<String> edgeTypes = new HashSet<>();
        Set<String> newEdgeTypes = new HashSet<>();
        FreqMap freqMap = new FreqMap(new HashMap());
        int edgeCounts = 0;
        int rejectedEdgeLoadCount = 0;
        int newEdgeGroupCount = 0;

        for (String stringEdge : stringEdges) {

            try {

                String[] edgeArray = stringEdge.split(delimiter);

                if (edgeArray.length < 2) {
                    continue;
                }

                String edgeType = simpleFile ? DEFAULT_EDGE_TYPE : edgeArray[EDGE_TYPE];

                if (!currentEdgeGroups.contains(edgeType)) {
                    LOGGER.warn("Got new edge type of {} ", edgeType);
                    newEdgeGroupCount++;
                    newEdgeTypes.add(edgeType);
                    throw new SchemaException("Attempting to add an unrecognised edge type " + edgeType);
                }

                if (simpleFile) {
                    mapSimpleEdge(edgeArray, elements, edgeTypes);
                } else {
                    mapDetailEdge(edgeArray, elements, edgeTypes);
                }

                freqMap.upsert(edgeType, 1l);
                edgeCounts++;

            } catch (Exception e) {
                LOGGER.error("failed to load {} ", stringEdge, e);
                rejectedEdgeLoadCount++;
            }
        }

        elements.add(graphStatsManager.updateGraphStats(freqMap));

        LOGGER.info("Successfully loaded {} elements ", elements.size());

        return new CreateElementsResponse(elements, edgeTypes, edgeCounts, rejectedEdgeLoadCount, newEdgeGroupCount, newEdgeTypes);
    }
}
