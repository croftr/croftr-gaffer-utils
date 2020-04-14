package uk.gov.gchq.gaffer.utils.upload.domain;

import java.util.List;
import java.util.Set;

public class GraphData {

    private String fileName;
    private List<String> edges;
    private Set<String> edgeTypes;

    public GraphData(String fileName, List<String> edges, Set<String> edgeTypes) {
        this.fileName = fileName;
        this.edges = edges;
        this.edgeTypes = edgeTypes;
    }

    public List<String> getEdges() {
        return edges;
    }

    public void setEdges(List<String> edges) {
        this.edges = edges;
    }

    public Set<String> getEdgeTypes() {
        return edgeTypes;
    }

    public void setEdgeTypes(Set<String> edgeTypes) {
        this.edgeTypes = edgeTypes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
