package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.service.SubscriptionService;
import it.ziotob.game.scotlandyard.utils.ServletUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SubscriptionHandler extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(SubscriptionHandler.class.getSimpleName());

    public static final String BASE_URL = "subscriptions";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> pathVariables = ServletUtils.getPathVariables(request, BASE_URL);
        Map<String, Object> body = ServletUtils.parseBody(request);

        if (body.size() <= 1 || !body.containsKey("actions") || pathVariables.size() != 1) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Wrong body/path arguments given");
            return;
        }

        SubscriptionService.getInstance().upsertSubscription(pathVariables.get(0), (Collection<String>) body.get("actions"));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> pathVariables = ServletUtils.getPathVariables(request, BASE_URL);

        if (pathVariables.size() != 1) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Path variables number is wrong");
            return;
        }

        Future<String> responsePromise = Executors.newSingleThreadExecutor().submit(() -> SubscriptionService.getInstance().listenNotifications(pathVariables.get(0)));

        try {

            String responseBody = responsePromise.get(30, TimeUnit.SECONDS);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(responseBody);
        } catch (TimeoutException ignored) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {

            LOG.error("Error while resolving subscription request", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> pathVariables = ServletUtils.getPathVariables(request, BASE_URL);
        Map<String, Object> body = ServletUtils.parseBody(request);

        if (pathVariables.size() != 1) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Wrong number of path variables");
            return;
        }

        if (body.containsKey("actions")) {
            SubscriptionService.getInstance().deleteActions(pathVariables.get(0), (Collection<String>) body.get("actions"));
        } else {
            SubscriptionService.getInstance().deleteSubscription(pathVariables.get(0));
        }
    }
}
