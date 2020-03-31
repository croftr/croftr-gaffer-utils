package uk.gov.gchq.gaffer.utils.vis;

import uk.gov.gchq.gaffer.commonutil.iterable.ChainedIterable;
import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Properties;
import uk.gov.gchq.gaffer.data.element.id.DirectedType;
import uk.gov.gchq.gaffer.data.generator.ElementGenerator;
import uk.gov.gchq.gaffer.types.TypeSubTypeValue;

import java.util.*;

public class VisGraphGenerator implements ElementGenerator {

    @Override
    public Object apply(Object o) {

        ChainedIterable
        chainedIterable = (ChainedIterable) o;

        final Set<VisNode> nodes = new HashSet<>();
        final List<VisEdge> edges = new ArrayList<>();
        final List<VisElement> elements = new ArrayList<>();


        chainedIterable.forEach(item -> {

            Edge edge = (Edge) item;

            TypeSubTypeValue source = (TypeSubTypeValue) edge.getSource();
            VisNode node1 = new VisNode(source.getType()+source.getSubType()+source.getValue(), source.getValue(), source.getType() + "|" + source.getSubType(), source.getSubType());

            TypeSubTypeValue dest = (TypeSubTypeValue) edge.getDestination();
            VisNode node2 = new VisNode(dest.getType()+dest.getSubType()+dest.getValue(), dest.getValue(), dest.getType() + "|" + dest.getSubType(), dest.getSubType());

            DirectedType directedType = edge.getDirectedType();
            String arrows = directedType.isDirected() ? "to" : null;
            String group = edge.getGroup();

            Properties properties = edge.getProperties();
            int value = (int) properties.get("count");

//            properties.forEach( (a,b) -> {
//                System.out.println("PROPERTIES LOOP " + a + " : " + b);
//                data.put(a,b);
//            });

            VisEdge visEdge = new VisEdge(node1.getId(), node2.getId(), arrows, group, value /10);

            nodes.add(node1);
            nodes.add(node2);
            edges.add(visEdge);

        });

        elements.addAll(nodes);
        elements.addAll(edges);

        return elements;
    }

}
