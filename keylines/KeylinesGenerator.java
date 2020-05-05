package uk.gov.gchq.gaffer.utils.keylines;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.commonutil.iterable.ChainedIterable;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.data.element.Properties;
import uk.gov.gchq.gaffer.data.element.id.DirectedType;
import uk.gov.gchq.gaffer.data.generator.ElementGenerator;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;
import uk.gov.gchq.gaffer.utils.upload.SchemaDefinitionFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * function makeNode(name, type) {
 *   return {
 *     id: name,
 *     type: 'node',
 *     t: name,
 *     d: { type: type }
 *   };
 * }
 * function makeLink(actor, movie) {
 *   return {
 *     id: actor + '-' + movie,
 *     type: 'link',
 *     id1: actor,
 *     id2: movie,
 *     c: colour,
 *   };
 * }
 */

public class KeylinesGenerator implements ElementGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeylinesGenerator.class);

    @Override
    public List<KeylinesObject> apply(Object o) {

        ChainedIterable
                chainedIterable = (ChainedIterable) o;

        final List<KeylinesEdge> edges = new ArrayList<>();
        final List<KeylinesNode> nodes = new ArrayList<>();
        final List<KeylinesObject> keylinesObjects = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();

//        final List<KeylinesEdge> edges = (List<KeylinesEdge>) StreamSupport.stream(chainedIterable.spliterator(), false)
//                .map(item ->  {
//
//                    Edge edge = (Edge) item;
//
//                    String source = (String) edge.getSource();
//                    KeylinesNode node1 = new KeylinesNode(source, source);
//
//                    String dest = (String) edge.getDestination();
//                    KeylinesNode node2 = new KeylinesNode(dest, dest);
//
//                    DirectedType directedType = edge.getDirectedType();
//                    String category = edge.getGroup();
//                    Properties properties = edge.getProperties();
//
//                    properties.forEach( (a,b) -> {
//                        System.out.println("PROPERTIES LOOP " + a + " : " + b);
//                        data.put(a,b);
//                    });
//
//                    KeylinesEdge keylinesEdge = new KeylinesEdge(category, node1, node2, directedType.isDirected(), data);
//                    return keylinesEdge;
//                })
//                .collect(Collectors.toList());

//        strings.forEach(s -> System.out.println("got " + s));


        chainedIterable.forEach(item -> {

            if (item instanceof  Edge) {
                Edge edge = (Edge) item;

                TypeSubTypeValue source = (TypeSubTypeValue) edge.getSource();
                KeylinesNode node1 = new KeylinesNode(source.getType(), source.getSubType(), source.getValue());

                TypeSubTypeValue dest = (TypeSubTypeValue) edge.getDestination();
                KeylinesNode node2 = new KeylinesNode(dest.getType(), dest.getSubType(), dest.getValue());

                DirectedType directedType = edge.getDirectedType();
                String category = edge.getGroup();
                Properties properties = edge.getProperties();

                properties.forEach( (a,b) -> {
                    System.out.println("PROPERTIES LOOP " + a + " : " + b);
                    data.put(a,b);
                });

                KeylinesEdge keylinesEdge = new KeylinesEdge(category, node1.getId(), node2.getId(), directedType.isDirected(), data);

                nodes.addAll(Arrays.asList(node1, node2));
                edges.add(keylinesEdge);
            } else if (item instanceof Entity) {
                LOGGER.info("Got entity");
            } else {
                LOGGER.info("Got unexpected type {} ", item.getClass());
            }


        });

        keylinesObjects.addAll(edges);
        keylinesObjects.addAll(nodes);
        return keylinesObjects;

    }

    @Override
    public Function andThen(Function after) {
        System.out.println("AND THEN CALLED  !!!");
        return null;
    }

    @Override
    public Function compose(Function before) {
        System.out.println("COMPOSE CALLED  !!!");
        return null;
    }

}
