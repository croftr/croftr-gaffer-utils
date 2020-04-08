package uk.gov.gchq.gaffer.utils.upload;

import org.apache.hadoop.classification.InterfaceAudience;
import uk.gov.gchq.gaffer.data.elementdefinition.ElementDefinition;

public class UserEdge implements ElementDefinition {

       private String description;
       private String source;
       private String destination;
       private boolean directed;

    public UserEdge(String description, String source, String destination, boolean directed) {
        this.description = description;
        this.source = source;
        this.destination = destination;
        this.directed = directed;
    }

    @Override
    public void lock() {

    }
}
