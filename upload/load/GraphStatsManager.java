package uk.gov.gchq.gaffer.utils.upload.load;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.types.FreqMap;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.Date;

public class GraphStatsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphStatsManager.class);

    private TypeSubTypeValue edgeCountsVertex;

    public GraphStatsManager() {
        edgeCountsVertex = new TypeSubTypeValue();
        edgeCountsVertex.setValue("graphInfoVertex");
    }

    public Element setCreatedGraphStats(FreqMap freqMap) {

        Entity edgeGroupCounts = new Entity.Builder()
                .group("graphInfo")
                .vertex(edgeCountsVertex)
                .property("edgeGroupCounts", freqMap)
                .property("createdDate", new Date())
                .property("createdBy", "Rob Croft")
                .build();

        LOGGER.info("created entity for new graph {} ", edgeGroupCounts.getGroup() );
        return edgeGroupCounts;
    }

    public Element updateGraphStats(FreqMap freqMap) {

        Entity edgeGroupCounts = new Entity.Builder()
                .group("graphInfo")
                .vertex(edgeCountsVertex)
                .property("edgeGroupCounts", freqMap)
                .build();

        LOGGER.info("updated entity for existing graph {} ", edgeGroupCounts.getGroup() );
        return edgeGroupCounts;
    }
}
