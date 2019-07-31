package it.ziotob.game.scotlandyard.handler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static it.ziotob.game.scotlandyard.utils.ServletUtils.getPathVariables;

public class WebappHandler extends HttpServlet {

    private static final String BASE_PATH = "webapp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String resourcePath = String.join("/", getPathVariables(request, BASE_PATH));

        if (resourcePath.isEmpty()) {
            resourcePath = "index.html";
        }

        extractResource(resourcePath, response);
    }

    private void extractResource(String resourcePath, HttpServletResponse response) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("webapp/" + resourcePath)))) {

            String responseStr = reader.lines().collect(Collectors.joining("\n"));

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(responseStr);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
