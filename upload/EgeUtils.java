package uk.gov.gchq.gaffer.utils.upload;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EgeUtils {

//    private static Map<String, String> edgeCategories = Stream.of(new String[][]{
//            {"alternative", "#alternative"},
//            {"communication", "#communication"},
//    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static String getDescription(String edgeType) {
        String type = edgeType.toLowerCase();

        String category = "";

        if (type.toLowerCase().contains("alternative") || type.toLowerCase().contains("alias") || type.toLowerCase().contains("aka")) {
            category = " #alternative";
        } else if (type.toLowerCase().startsWith("comm")) {
            category = " #communication";
        } else if (type.toLowerCase().startsWith("seen")) {
            category = " #index";
        }

        return edgeType + " Edge" + category;

    }
}
