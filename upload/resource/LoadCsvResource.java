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
import java.io.*;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadCsvResource extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadCsvResource.class);

    private SchemaService schemaService;

    public void init() throws ServletException {
        schemaService = new SchemaService();
    }

    //    http://localhost:8080/rest/shortestPath?node1=61&node2=7
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String schemaName = request.getParameter("name");
        String auths = request.getParameter("auths");
        LOGGER.info("Received request to create schema {} with auths {} ", schemaName, auths);
        CreateSchemaResponse createSchemaResponse = null;
        try {
            createSchemaResponse = schemaService.createSchemaFromData(request.getParts(), schemaName, auths);
        } catch (OperationException e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        byte[] jsonBytes = JSONSerialiser.serialise(createSchemaResponse, true);
        out.println(new String(jsonBytes));
    }

//    //for Preflight
//    @Override
//    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        System.out.println("OPTIONS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        setAccessControlHeaders(resp);
//        resp.setStatus(HttpServletResponse.SC_OK);
//    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST");
    }

    public void destroy() {
        // do nothing.
    }
}