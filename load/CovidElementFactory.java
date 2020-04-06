package uk.gov.gchq.gaffer.utils.load;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CovidElementFactory implements SchemaElementFactrory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CovidElementFactory.class);

    private static final int HLLP_PRECISION = 10;

    static final int FROM_NODE_ID = 0;
    static final int TO_NODE_COUNTRY = 5;
    static final int TO_NODE_PROVINCE = 4;
    static final int TO_NODE_CITY = 3;
    static final int FROM_NODE_SEX = 2;


    @Override
    public List<Element> createEdgesAndEntities(List<String> stringEdges, String species, String delimiter) {

        List<Edge> edges = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();

        List<Element> elements = new ArrayList<>();

        int errorCount = 0;
        int successCount = 0;

        for (String stringEdge : stringEdges) {

            try {
                String[] edgeArray = stringEdge.split(delimiter);

                TypeSubTypeValue vertex1 = new TypeSubTypeValue();
                vertex1.setType("case");
                vertex1.setSubType("covid-19");
                vertex1.setValue(edgeArray[FROM_NODE_ID]);

                if (edgeArray[TO_NODE_COUNTRY].equals("") || edgeArray[TO_NODE_PROVINCE].equals("")) {
                    throw new Exception("invalid data");
                }

                TypeSubTypeValue vertex2 = new TypeSubTypeValue();
                vertex2.setType("location");
                vertex2.setSubType(edgeArray[TO_NODE_COUNTRY]);
                vertex2.setValue(edgeArray[TO_NODE_PROVINCE]);

                TypeSubTypeValue countryVertex = new TypeSubTypeValue();
                countryVertex.setType("country");
                countryVertex.setSubType(null);
                countryVertex.setValue(edgeArray[TO_NODE_COUNTRY]);

                HyperLogLogPlus nodeAHllp = new HyperLogLogPlus(HLLP_PRECISION);
                HyperLogLogPlus nodeBHllp = new HyperLogLogPlus(HLLP_PRECISION);
                HyperLogLogPlus countryCentrality = new HyperLogLogPlus(HLLP_PRECISION);
                nodeAHllp.offer(vertex2);
                nodeBHllp.offer(vertex1);
                countryCentrality.offer(countryCentrality);

                Entity peopleStats = new Entity.Builder()
                        .group("peopleStats")
                        .vertex(vertex1)
                        .property("sex", edgeArray[FROM_NODE_SEX])
                        .build();

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

                Entity countryEntity = new Entity.Builder()
                        .group("cardinality")
                        .vertex(countryVertex)
                        .property("approxCardinality", countryCentrality)
                        .build();

                Edge edge = new Edge.Builder()
                        .group("occuredIn")
                        .source(vertex1).dest(vertex2).directed(false)
                        .directed(true)
//                    .property(propertyName, properyValue)
                        .build();

                Edge countryEdge = new Edge.Builder()
                        .group("occuredInCountry")
                        .source(vertex1).dest(countryVertex).directed(true)
                        .directed(true)
//                    .property(propertyName, properyValue)
                        .build();

                edges.add(edge);
                edges.add(countryEdge);

                entities.add(nodeAEntity);
                entities.add(nodeBEntity);
                entities.add(countryEntity);
                entities.add(peopleStats);

            } catch (Exception e) {
                errorCount++;
                LOGGER.trace("Skipping Edge {}", stringEdge);
            }

            successCount++;

        };

        elements.addAll(edges);
        elements.addAll(entities);

        LOGGER.info("Successfully loaded {} cases.  Failed to load {} cases ", successCount, errorCount);

        return elements;
    }
}
