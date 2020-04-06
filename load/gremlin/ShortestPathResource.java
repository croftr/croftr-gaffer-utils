package uk.gov.gchq.gaffer.utils.load.gremlin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ShortestPathResource extends HttpServlet {

    private String message;

    public void init() throws ServletException {
        // Do required initialization
        message = "Hello World";
    }

//    http://localhost:8080/rest/shortestPath?node1=61&node2=7
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

        // Set response content type
        response.setContentType("application/json");

        Integer node1 = Integer.parseInt(request.getParameter("node1"));
        Integer node2 = Integer.parseInt(request.getParameter("node2"));

        System.out.println("shortest path between " + node1 + " " + node2);


        GremlinLoader gremlinLoader = new GremlinLoader();
        gremlinLoader.loadGraph();
        List<String> paths = gremlinLoader.shortestPath(node1, node2);
        System.out.println("paths " + paths);

        List<String> ids = new ArrayList<>();

        for (String path : paths) {
            ids.add(path.substring(2,path.indexOf("]")));
        }

        System.out.println("path id " + ids);

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println(ids);
    }

    public void destroy() {
        // do nothing.
    }
}
