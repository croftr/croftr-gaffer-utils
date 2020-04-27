package uk.gov.gchq.gaffer.utils.upload;


/**
 *  Edge types will different names can represent similar types of relationships
 *  we try to give those relationships a category type to indicate this
 */
public class EgeCategories {

    private final static String INDEX_CATEGORY = "#index";
    private final static String ALTERNATIVE_CATEGORY = "#alternative";
    private final static String COMMS_CATEGORY = "#communication";

    /**
     * @param edgeType  for which we wan to try and get a category for
     * @return the category of the edge or an empty string id we cant find one
     */
    public static String getCategory(String edgeType) {

        String type = edgeType.toLowerCase();

        String category = "";

        if (type.toLowerCase().contains("alternative") || type.toLowerCase().contains("alias") || type.toLowerCase().contains("aka")) {
            category = ALTERNATIVE_CATEGORY;
        } else if (type.toLowerCase().startsWith("comm")) {
            category = COMMS_CATEGORY;
        } else if (type.toLowerCase().startsWith("seen")) {
            category = INDEX_CATEGORY;
        }

        return category;
    }
}
