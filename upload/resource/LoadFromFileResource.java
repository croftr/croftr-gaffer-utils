package uk.gov.gchq.gaffer.utils.upload.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.utils.upload.SchemaService;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateSchemaResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadFromFileResource extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadFromFileResource.class);

    private SchemaService schemaService;

    public void init() {
        schemaService = new SchemaService();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String schemaName = request.getParameter("name");
        String delimiterParam = request.getParameter("del");
        CreateSchemaResponse createSchemaResponse = null;

        String delimiter = ",";
        switch (delimiterParam) {
            case "comma" :
                delimiter = ",";
                break;
            case "space" :
                delimiter = " ";
                break;
            case "tab" :
                delimiter = "\t";
        }

        try {
            createSchemaResponse = schemaService.loadData(request.getParts(), schemaName, delimiter);
        } catch (OperationException e) {
            LOGGER.error("Error loading data into graph ", e);
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        byte[] jsonBytes = JSONSerialiser.serialise(createSchemaResponse, true);
        out.println(new String(jsonBytes));
    }

    public void destroy() {
        // do nothing.
    }
}
