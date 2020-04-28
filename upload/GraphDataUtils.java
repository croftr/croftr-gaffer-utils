package uk.gov.gchq.gaffer.utils.upload;

import uk.gov.gchq.gaffer.data.elementdefinition.exception.SchemaException;
import uk.gov.gchq.gaffer.utils.upload.domain.GraphData;

import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static uk.gov.gchq.gaffer.utils.upload.DelimiterMapper.DEFAULT_EDGE_TYPE;
import static uk.gov.gchq.gaffer.utils.upload.DelimiterMapper.EDGE_TYPE;

/**
 * Convert the file data into a more generic java object for processing
 */
public class GraphDataUtils {

    private static final int SIMPLE_FILE_COLUMN_COUNT = 2;
    private static final int DETAIL_FILE_COLUMN_COUNT = 9;

    private static String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");

        if (contentDisposition != null) {
            for (String content : contentDisposition.split(";")) {
                if (content.trim().startsWith("filename"))
                    return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }

        return "default.txt";
    }

    public static GraphData convertGraphData(Collection<Part> parts, String delimiter) throws IOException {

        String fileName = null;
        boolean isSimpleFile = false;
        List<String> edges = new ArrayList<>();
        Set<String> edgeTypes = new HashSet<>();

        for (Part part : parts) {

            fileName = getFileName(part);

            InputStream inputStream = part.getInputStream();
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);

            String firstLine = reader.readLine();
            String[] firstLineArray = firstLine.split(delimiter);
            if (firstLineArray.length == SIMPLE_FILE_COLUMN_COUNT) {
                isSimpleFile = true;
            } else if (firstLineArray.length != DETAIL_FILE_COLUMN_COUNT) {
                throw new SchemaException("Files must contain either " + SIMPLE_FILE_COLUMN_COUNT + " or " + DETAIL_FILE_COLUMN_COUNT + " columns");
            }

            String str;
            while ((str = reader.readLine()) != null) {
                String[] edgeArray = str.split(",");
                String edgeType = isSimpleFile ? DEFAULT_EDGE_TYPE : edgeArray[EDGE_TYPE];
                edgeTypes.add(edgeType);
                edges.add(str);
            }
        }

        return new GraphData(fileName, edges, edgeTypes, isSimpleFile);

    }
}
