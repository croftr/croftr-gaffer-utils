package uk.gov.gchq.gaffer.utils.upload.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.operation.OperationException;
import uk.gov.gchq.gaffer.utils.upload.DelimiterMapper;
import uk.gov.gchq.gaffer.utils.upload.SchemaService;
import uk.gov.gchq.gaffer.utils.upload.domain.CreateSchemaResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class CreateSchemaFromFileResource extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSchemaFromFileResource.class);

    private SchemaService schemaService;

    public void init() {
        schemaService = new SchemaService();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // pull out the request params
        String schemaName = request.getParameter("name");
        String description = request.getParameter("desc");
        String delimiterParam = request.getParameter("del");
        String auths = request.getParameter("auths");

        //get the actual delimiter to use from the delimiter description
        String delimiter = DelimiterMapper.delimiterType(delimiterParam);

        LOGGER.info("Received request to create schema {} with auths {} ", schemaName, auths);
        CreateSchemaResponse createSchemaResponse = null;
        try {
            createSchemaResponse = schemaService.createSchemaFromData(request.getParts(), schemaName, auths, description, delimiter);
        } catch (OperationException e) {
            LOGGER.error("Error creating graph from file ", e);
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
