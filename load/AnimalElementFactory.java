package uk.gov.gchq.gaffer.utils.load;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.ArrayList;
import java.util.List;

public class AnimalElementFactory implements SchemaElementFactrory {

    private static final int HLLP_PRECISION = 10;

    static final int FROM_NODE = 0;
    static final int TO_NODE = 1;
    static final int EDGE_WEIGHT = 2;

    @Override
    public List<Element> createEdgesAndEntities(List<String> stringEdges, String species, String delimiter) {

        boolean isDubug = false;

        List<Edge> edges = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();

        List<Element> elements = new ArrayList<>();

        stringEdges.forEach(stringEdge -> {

            String[] edgeArray = stringEdge.split(delimiter);

            TypeSubTypeValue vertex1 = new TypeSubTypeValue();
            vertex1.setType("animal");
            vertex1.setSubType(species);
            vertex1.setValue(edgeArray[FROM_NODE]);

            TypeSubTypeValue vertex2 = new TypeSubTypeValue();
            vertex2.setType("animal");
            vertex2.setSubType(species);
            vertex2.setValue(edgeArray[TO_NODE]);

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

            String propertyName = null;
            Integer properyValue = null;
            if (edgeArray.length > 2) {
                propertyName = "count";
                properyValue = Integer.parseInt(edgeArray[EDGE_WEIGHT]);
            }

            Edge edge = new Edge.Builder()
                    .group("interaction")
                    .source(vertex1).dest(vertex2).directed(false)
                    .property(propertyName, properyValue)
                    .build();

            edges.add(edge);
            entities.add(nodeBEntity);

        });

        elements.addAll(edges);
        elements.addAll(entities);

        if (isDubug) {
            try {
                byte[] jsonBytes = new byte[0];
                jsonBytes = JSONSerialiser.serialise(elements, true);
                System.out.println(new String(jsonBytes));
            } catch (SerialisationException e) {
                e.printStackTrace();
            }
        }

        return elements;
    }
}
