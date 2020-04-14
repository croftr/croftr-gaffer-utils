package uk.gov.gchq.gaffer.utils.load;

import uk.gov.gchq.gaffer.data.element.Element;
import uk.gov.gchq.gaffer.federatedstore.FederatedStoreConstants;
import uk.gov.gchq.gaffer.operation.impl.add.AddElements;

import java.util.List;

public class OperationsManager {

    public AddElements addElements(List<Element> elements, String graphId) {

        System.out.println("check me out !!!! " + graphId);

        AddElements addElements = new AddElements.Builder()
                .option(FederatedStoreConstants.KEY_OPERATION_OPTIONS_GRAPH_IDS, graphId)
                .skipInvalidElements(false)
                .validate(true)
                .input(elements)
                .build();

        return addElements;

    }

}
