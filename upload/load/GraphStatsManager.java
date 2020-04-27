package uk.gov.gchq.gaffer.utils.upload.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.types.FreqMap;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.Date;

/**
 * We store some meta data about graphs in 2 different entities groups
 * 1) graphCreation - this is created and populated only at the moment the graph is created.
 *                    It stores created data and created by and description
 * 2) graphStatus   - this is created when the graph is created and then updated when new data is loaded.
 *                    it stores summary data of what is in the graph, e.g egde groups and their counts
 */
public class GraphStatsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphStatsManager.class);

    private static final String GRAPH_CREATION = "graphCreation";
    private static final String CREATED_DATE_PROPERTY = "createdDate";
    private static final String CREATED_BY_PROPERTY = "graphCreation";
    private static final String DESCRIPTION_PROPERTY = "graphCreation";

    private static final String GRAPH_STATUS = "graphStatus";
    private static final String EDGE_GROUPS_PROPERTY = "edgeGroupCounts";

    private TypeSubTypeValue edgeCountsVertex;

    public GraphStatsManager() {
        edgeCountsVertex = new TypeSubTypeValue();
        edgeCountsVertex.setValue("graphInfoVertex");
    }

    public Element setCreatedGraphStats(String description) {

        Entity edgeGroupCounts = new Entity.Builder()
                .group(GRAPH_CREATION)
                .vertex(edgeCountsVertex)
                .property(CREATED_DATE_PROPERTY, new Date())
                .property(CREATED_BY_PROPERTY, "Rob Croft")
                .property(DESCRIPTION_PROPERTY, description)
                .build();

        LOGGER.info("created entity for new graph {} ", edgeGroupCounts.getGroup() );
        return edgeGroupCounts;
    }

    public Element updateGraphStats(FreqMap freqMap) {

        Entity edgeGroupCounts = new Entity.Builder()
                .group(GRAPH_STATUS)
                .vertex(edgeCountsVertex)
                .property(EDGE_GROUPS_PROPERTY, freqMap)
                .build();

        LOGGER.info("updated entity for existing graph {} ", edgeGroupCounts.getGroup() );
        return edgeGroupCounts;
    }
}
