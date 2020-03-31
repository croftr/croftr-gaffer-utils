package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.data.element.Edge;
import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.data.element.Entity;
import uk.gov.gchq.gaffer.federatedstore.FederatedStoreConstants;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;

import java.util.ArrayList;
import java.util.List;

public class OperationsManager {

    public AddElements addElements(List<Edge> edges, List<Entity> entities) {

        List<Element> elements = new ArrayList<>();
        elements.addAll(edges);
        elements.addAll(entities);

        AddElements addElements = new AddElements.Builder()
                .option(FederatedStoreConstants.KEY_OPERATION_OPTIONS_GRAPH_IDS, "kangaroos")
                .skipInvalidElements(false)
                .validate(true)
                .input(elements)
                .build();

        return addElements;

    }

}
