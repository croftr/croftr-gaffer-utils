package uk.gov.gchq.gaffer.utils.vis;

import java.util.List;

public class VisGraph {

    private List<VisNode> nodes;
    private List<VisEdge> edges;

    public VisGraph(List<VisNode> nodes, List<VisEdge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<VisNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<VisNode> nodes) {
        this.nodes = nodes;
    }

    public List<VisEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<VisEdge> edges) {
        this.edges = edges;
    }
}
