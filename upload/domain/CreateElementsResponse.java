package uk.gov.gchq.gaffer.utils.upload.domain;

import uk.gov.gchq.gaffer.data.element.Element;

import java.util.List;
import java.util.Set;

public class CreateElementsResponse {

    private List<Element> elements;
    private Set<String> edgeTypes;
    private int edgeCount;

    public CreateElementsResponse(List<Element> elements, Set<String> edgeTypes, int edgeCount) {
        this.elements = elements;
        this.edgeTypes = edgeTypes;
        this.edgeCount = edgeCount;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public Set<String> getEdgeTypes() {
        return edgeTypes;
    }

    public void setEdgeTypes(Set<String> edgeTypes) {
        this.edgeTypes = edgeTypes;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public void setEdgeCount(int edgeCount) {
        this.edgeCount = edgeCount;
    }
}
