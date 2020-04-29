package uk.gov.gchq.gaffer.utils.upload;

public class DelimiterMapper {

    /*
     * @param delimiterDescription the delimiter described
     * @return the actual delimter to use
     */
    public static String delimiterType(String delimiterDescription) {

        String delimiter;

        switch (delimiterDescription) {
            case "comma" :
                delimiter = ",";
                break;
            case "space" :
                delimiter = " ";
                break;
            case "tab" :
                delimiter = "\t";
                break;
            default:
                delimiter = ",";
        }

        return delimiter;
    }


    public static final int SIMPLE_FROM_NODE_VALUE = 0;
    public static final int SIMPLE_TO_NODE_VALUE = 1;

    public static final int FROM_NODE_TYPE = 0;
    public static final int FROM_NODE_SUBTYPE = 1;
    public static final int FROM_NODE_VALUE = 2;
    public static final int EDGE_TYPE = 3;
    public static final int DIRECTED = 4;
    public static final int EDGE_WEIGHT = 5;
    public static final int TO_NODE_TYPE = 6;
    public static final int TO_NODE_SUBTYPE = 7;
    public static final int TO_NODE_VALUE = 8;

    public static final String DEFAULT_EDGE_TYPE = "interaction";
}
