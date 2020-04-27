package uk.gov.gchq.gaffer.utils.upload.load;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Create and populate a centrality entity for recodings vertex centrality
 */
public class CentralityManager {

    private static final int HLLP_PRECISION = 10;

    private static final String GROUP = "cardinality";
    private static final String PROPERTY = "approxCardinality";

    public List<Entity> generateEntityCentrality(TypeSubTypeValue vertex1, TypeSubTypeValue vertex2) {

        List<Entity> entities = new ArrayList<>();

        HyperLogLogPlus nodeAHllp = new HyperLogLogPlus(HLLP_PRECISION);
        HyperLogLogPlus nodeBHllp = new HyperLogLogPlus(HLLP_PRECISION);
        nodeAHllp.offer(vertex2);
        nodeBHllp.offer(vertex1);

        Entity nodeAEntity = new Entity.Builder()
                .group(GROUP)
                .vertex(vertex1)
                .property(PROPERTY, nodeAHllp)
                .build();

        Entity nodeBEntity = new Entity.Builder()
                .group(GROUP)
                .vertex(vertex2)
                .property(PROPERTY, nodeBHllp)
                .build();

        entities.add(nodeAEntity);
        entities.add(nodeBEntity);

        return entities;

    }
}
