package uk.gov.gchq.gaffer.utils.keylines;

import java.util.Collections;
import java.util.Map;

public class KeylinesNode {

    private final String type = "node";
    private String id;
    private String t;
    private Map<String, Object> d;

    public KeylinesNode(String id, String t, Map<String, Object> d) {
        this.id = id;
        this.t = t;
        this.d = d;
    }

    public KeylinesNode(String id, String t) {
        this(id, t, Collections.emptyMap());
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
