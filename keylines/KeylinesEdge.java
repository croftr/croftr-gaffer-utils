package uk.gov.gchq.gaffer.utils.keylines;

import java.util.Map;
import java.util.UUID;

public class KeylinesEdge extends KeylinesObject {

    private String type = "link";
    private String id;
    private String category;
    private String id1;
    private String id2;
    private boolean isDirected;
    private Map<java.lang.String, Object> d;

    public KeylinesEdge(String category, String id1, String id2, boolean isDirected, Map<String, Object> d) {
        this.id = UUID.randomUUID().toString();
        this.category = category;
        this.id1 = id1;
        this.id2 = id2;
        this.isDirected = isDirected;
        this.d = d;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }

    public Map<java.lang.String, Object> getD() {
        return d;
    }

    public void setD(Map<java.lang.String, Object> d) {
        this.d = d;
    }


}
