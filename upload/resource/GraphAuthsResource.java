package uk.gov.gchq.gaffer.utils.upload.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.gaffer.graph.Graph;
import uk.gov.gchq.gaffer.jsonserialisation.JSONSerialiser;
import uk.gov.gchq.gaffer.proxystore.ProxyStore;
import uk.gov.gchq.gaffer.utils.upload.SchemaService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GraphAuthsResource extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphAuthsResource.class);

    private SchemaService schemaService;

    public void init() throws ServletException {
        schemaService = new SchemaService();
    }

    //    http://localhost:8080/rest/shortestPath?node1=61&node2=7
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String schemaName = request.getParameter("name");
        String auths = request.getParameter("auths");
        LOGGER.info("Received request to create schema {} with auths {} ", schemaName, auths);

        Graph graph = new Graph.Builder()
                .store(new ProxyStore.Builder()
                        .graphId(schemaName)
                        .host("localhost")
                        .port(8080)
                        .contextRoot("rest")
                        .build())
                .build();

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        LOGGER.info("Got Graph {}", graph);
        byte[] jsonBytes = JSONSerialiser.serialise(graph, true);
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

//    private void setAccessControlHeaders(HttpServletResponse resp) {
//        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//        resp.setHeader("Access-Control-Allow-Methods", "POST");
//    }

    public void destroy() {
        // do nothing.
    }
}
