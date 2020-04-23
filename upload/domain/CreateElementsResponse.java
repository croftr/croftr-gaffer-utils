package uk.gov.gchq.gaffer.utils.upload.domain;

import uk.gov.gchq.gaffer.data.element.Element;

import java.util.List;
import java.util.Set;

public class CreateElementsResponse {

    private List<Element> elements;
    private Set<String> edgeTypes;
    private int edgeCount;
    private int rejectedEdgeLoadCount;
    private int newEdgeGroupCount;
    private Set<String> newEdgeTypes;

    public CreateElementsResponse(List<Element> elements, Set<String> edgeTypes, int edgeCount, int rejectedEdgeLoadCount, int newEdgeGroupCount, Set<String> newEdgeTypes) {
        this.elements = elements;
        this.edgeTypes = edgeTypes;
        this.edgeCount = edgeCount;
        this.newEdgeGroupCount = newEdgeGroupCount;
        this.rejectedEdgeLoadCount = rejectedEdgeLoadCount;
        this.newEdgeTypes = newEdgeTypes;
    }

    public int getNewEdgeGroupCount() {
        return newEdgeGroupCount;
    }

    public void setNewEdgeGroupCount(int newEdgeGroupCount) {
        this.newEdgeGroupCount = newEdgeGroupCount;
    }

    public Set<String> getNewEdgeTypes() {
        return newEdgeTypes;
    }

    public void setNewEdgeTypes(Set<String> newEdgeTypes) {
        this.newEdgeTypes = newEdgeTypes;
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

    public int getRejectedEdgeLoadCount() {
        return rejectedEdgeLoadCount;
    }

    public void setRejectedEdgeLoadCount(int rejectedEdgeLoadCount) {
        this.rejectedEdgeLoadCount = rejectedEdgeLoadCount;
    }
}
