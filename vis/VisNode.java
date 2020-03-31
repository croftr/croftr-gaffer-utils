package uk.gov.gchq.gaffer.utils.vis;

import java.util.Objects;

public class VisNode extends VisElement {

    private String id;
    private String label;
    private String group;
    private String title;
    private String shape = "image";
    private boolean physics = false;

    public VisNode(String id, String label, String group, String title) {
        this.id = id;
        this.label = label;
        this.group = group;
        this.title = title;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public boolean isPhysics() {
        return physics;
    }

    public void setPhysics(boolean physics) {
        this.physics = physics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisNode visNode = (VisNode) o;
        return id.equals(visNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
