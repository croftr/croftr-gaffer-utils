package uk.gov.gchq.gaffer.utils.vis;

public class VisEdge extends VisElement {

    private String from;
    private String to;
    private String arrows;
    private String title;
    private String color;
    private int width;

    public VisEdge(String from, String to, String arrows, String title, int width) {
        this.from = from;
        this.to = to;
        this.arrows = arrows;
        this.title = title;
        this.width = width;

        switch (title) {
            case "crossesPaths" : {
                this.color = "red";
                break;
            }
            case "isPartOfThisCompany": {
                this.color = "blue";
                break;
            }
            case "inTheSameBusCompany": {
                this.color = "green";
                break;
            }
            default:
                this.color = "grey";
        }

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getArrows() {
        return arrows;
    }

    public void setArrows(String arrows) {
        this.arrows = arrows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
