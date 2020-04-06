package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.data.element.Element;

import java.util.List;

public interface SchemaElementFactrory {

    List<Element> createEdgesAndEntities(List<String> stringEdges, String species, String delimiter);

}
