package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.service.WebappCacheService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.utils.ServletUtils.getPathVariables;

public class WebappHandler extends HttpServlet {

    private static final String BASE_PATH = "webapp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String resourcePath = String.join("/", getPathVariables(request, BASE_PATH));

        if (resourcePath.isEmpty()) {
            resourcePath = "index.html";
        }

        Optional<String> resource = WebappCacheService.getInstance().getResource(resourcePath);

        if (resource.isPresent()) {

                try {

                response.setContentType(detectContentType(resourcePath));
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(resource.get());
            } catch (Exception ignored) {

                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private String detectContentType(String resourcePath) {

        String extension = Stream.of(resourcePath.split(".")).reduce((a, b) -> b).orElse("html");

        if ("html".equals(extension)) {
            return "text/html";
        } else if ("js".equals(extension)) {
            return "text/javascript";
        } else if ("css".equals(extension)) {
            return "text/css";
        } else {
            return "text/plain";
        }
    }
}
