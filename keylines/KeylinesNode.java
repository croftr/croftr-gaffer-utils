package uk.gov.gchq.gaffer.utils.keylines;

import java.util.Map;

public class KeylinesNode extends KeylinesObject {

    private final String type = "node";

    private String id;
    private String t;
    private String nodeType;
    private String nodeSubType;
    private Map<String, Object> d;

    public KeylinesNode(String nodeType, String nodeSubType, String t) {
        this.t = t;
        this.nodeType = nodeType;
        this.nodeSubType = nodeSubType;
        this.id = nodeType+"|"+nodeSubType+"|"+t;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeSubType() {
        return nodeSubType;
    }

    public void setNodeSubType(String nodeSubType) {
        this.nodeSubType = nodeSubType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public Map<String, Object> getD() {
        return d;
    }

    public void setD(Map<String, Object> d) {
        this.d = d;
    }
}
