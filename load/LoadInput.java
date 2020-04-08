package uk.gov.gchq.gaffer.utils.load;

public class LoadInput {

    private String delimter = "\\t";
    private String graphData = "example/federated-demo/scripts/data/ESC-2016.txt";
    private String edgeType = "votedFor2016";

    public LoadInput(String delimter, String graphData, String edgeType) {
        this.delimter = delimter;
        this.graphData = graphData;
        this.edgeType = edgeType;
    }

    public String getDelimter() {
        return delimter;
    }

    public void setDelimter(String delimter) {
        this.delimter = delimter;
    }

    public String getGraphData() {
        return graphData;
    }

    public void setGraphData(String graphData) {
        this.graphData = graphData;
    }

    public String getEdgeType() {
        return edgeType;
    }

    public void setEdgeType(String edgeType) {
        this.edgeType = edgeType;
    }
}
