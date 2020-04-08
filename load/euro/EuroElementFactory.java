package uk.gov.gchq.gaffer.utils.load.euro;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.exception.SerialisationException;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.utils.load.SchemaElementFactrory;

import java.util.ArrayList;
import java.util.List;

public class EuroElementFactory implements SchemaElementFactrory {

    private static final int HLLP_PRECISION = 10;

    static final int FROM_NODE = 0;
    static final int TO_NODE = 1;
    static final int JURY_POINTS = 9;
    static final int TELE_POINTS = 10;

    @Override
    public List<Element> createEdgesAndEntities(List<String> stringEdges, String edgeType, String delimiter) {

        List<Edge> edges = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        List<Element> elements = new ArrayList<>();

        for (String stringEdge : stringEdges) {

            try {

                String[] edgeArray = stringEdge.split(delimiter);

                if (edgeArray.length < 11) {
                    continue;
                }

                int points;
                int juryPoints;
                int telePoints;
                String fromCountry = edgeArray[FROM_NODE];
                String toCountry = edgeArray[TO_NODE];

                try {
                    juryPoints = Integer.parseInt(edgeArray[JURY_POINTS]);
                    telePoints = Integer.parseInt(edgeArray[TELE_POINTS]);
                    points = juryPoints + telePoints;
                } catch (NumberFormatException e) {
                    continue;
                }

                TypeSubTypeValue vertex1 = new TypeSubTypeValue();
                vertex1.setType("country");
                vertex1.setSubType(null);
                vertex1.setValue(fromCountry);

                TypeSubTypeValue vertex2 = new TypeSubTypeValue();
                vertex2.setType("country");
                vertex2.setSubType(null);
                vertex2.setValue(toCountry);

                HyperLogLogPlus nodeAHllp = new HyperLogLogPlus(HLLP_PRECISION);
                HyperLogLogPlus nodeBHllp = new HyperLogLogPlus(HLLP_PRECISION);

                nodeBHllp.offer(vertex1);
//                Entity nodeAEntity = new Entity.Builder()
//                        .group("cardinality")
//                        .vertex(vertex1)
//                        .property("approxCardinality", nodeAHllp)
//                        .build();

                Entity nodeBEntity = new Entity.Builder()
                        .group("cardinality")
                        .vertex(vertex2)
                        .property("approxCardinality", nodeBHllp)
                        .build();

                Entity pointsEntity = new Entity.Builder()
                        .group("points")
                        .vertex(vertex2)
                        .property("totalPoints", points)
                        .property("juryPoints", juryPoints)
                        .property("telePoints", telePoints)
                        .build();

                Edge edge = new Edge.Builder()
                        .group(edgeType)
                        .source(vertex1).dest(vertex2).directed(true)
                        .property("count", points)
                        .build();

                edges.add(edge);
//                entities.add(nodeAEntity);
                entities.add(nodeBEntity);
                entities.add(pointsEntity);

            } catch (Exception e) {
                System.out.println("failed to load " + stringEdge);
                throw e;
            }
        }

        elements.addAll(edges);
        elements.addAll(entities);

        return elements;
    }
}
