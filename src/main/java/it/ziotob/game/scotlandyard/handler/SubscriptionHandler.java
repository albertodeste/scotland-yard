package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.utils.ServletUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

public class SubscriptionHandler extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(SubscriptionHandler.class.getSimpleName());

    public static final String BASE_URL = "subscribe";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {

            Map<String, Object> body = ServletUtils.parseBody(request);
            //TODO start looping every X milliseconds checking if events where notified for the subscriber

            //FIXME remove following code
            try {
                Thread.sleep(10 * 1000);
            } catch (Exception ignored) {
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(new Event("player-id", Player.EVENT_SET_MATCH_ID, body.get("id").toString(), LocalDateTime.now()).toJSON());
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.error("Error during subscription", e);
        }
    }
}
