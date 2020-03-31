package uk.gov.gchq.gaffer.utils.keylines;

import java.util.Map;

/**
 * function makeNode(name, type) {
 *   return {
 *     id: name,
 *     type: 'node',
 *     t: name,
 *     d: { type: type }
 *   };
 * }
 * function makeLink(actor, movie) {
 *   return {
 *     id: actor + '-' + movie,
 *     type: 'link',
 *     id1: actor,
 *     id2: movie,
 *     c: colour,
 *   };
 * }
 */

public class KeylinesEdge {

    private String id;
    private String type = "link";
    private String category;
    private KeylinesNode id1;
    private KeylinesNode id2;
    private boolean isDirected;
    private Map<String, Object> d;

    public KeylinesEdge(String category, KeylinesNode id1, KeylinesNode id2, boolean isDirected, Map<String, Object> d) {
        this.id = id1.getId() + "-" + id2.getId();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public KeylinesNode getId1() {
        return id1;
    }

    public void setId1(KeylinesNode id1) {
        this.id1 = id1;
    }

    public KeylinesNode getId2() {
        return id2;
    }

    public void setId2(KeylinesNode id2) {
        this.id2 = id2;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean directed) {
        isDirected = directed;
    }

    public Map<String, Object> getD() {
        return d;
    }

    public void setD(Map<String, Object> d) {
        this.d = d;
    }


}
