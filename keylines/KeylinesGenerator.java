package uk.gov.gchq.gaffer.utils.keylines;

import uk.gov.gchq.gaffer.commonutil.iterable.ChainedIterable;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Properties;
import uk.gov.gchq.gaffer.data.element.id.DirectedType;
import uk.gov.gchq.gaffer.data.generator.ElementGenerator;

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

    @Override
    public List<KeylinesEdge> apply(Object o) {

        ChainedIterable
                chainedIterable = (ChainedIterable) o;

//        final List<KeylinesEdge> edges = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();

        final List<KeylinesEdge> edges = (List<KeylinesEdge>) StreamSupport.stream(chainedIterable.spliterator(), false)
                .map(item ->  {

                    Edge edge = (Edge) item;

                    String source = (String) edge.getSource();
                    KeylinesNode node1 = new KeylinesNode(source, source);

                    String dest = (String) edge.getDestination();
                    KeylinesNode node2 = new KeylinesNode(dest, dest);

                    DirectedType directedType = edge.getDirectedType();
                    String category = edge.getGroup();
                    Properties properties = edge.getProperties();

                    properties.forEach( (a,b) -> {
                        System.out.println("PROPERTIES LOOP " + a + " : " + b);
                        data.put(a,b);
                    });

                    KeylinesEdge keylinesEdge = new KeylinesEdge(category, node1, node2, directedType.isDirected(), data);
                    return keylinesEdge;
                })
                .collect(Collectors.toList());

//        strings.forEach(s -> System.out.println("got " + s));


//        chainedIterable.forEach(item -> {
//
//            Edge edge = (Edge) item;
//
//            String source = (String) edge.getSource();
//            KeylinesNode node1 = new KeylinesNode(source, source);
//
//            String dest = (String) edge.getDestination();
//            KeylinesNode node2 = new KeylinesNode(source, source);
//
//            DirectedType directedType = edge.getDirectedType();
//            String category = edge.getGroup();
//            Properties properties = edge.getProperties();
//
//            properties.forEach( (a,b) -> {
//                System.out.println("PROPERTIES LOOP " + a + " : " + b);
//                data.put(a,b);
//            });
//
//            KeylinesEdge keylinesEdge = new KeylinesEdge(category, node1, node2, directedType.isDirected(), data);
//            edges.add(keylinesEdge);
//        });

        return edges;

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
