package it.ziotob.game.scotlandyard;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.handler.MatchHandler;
import it.ziotob.game.scotlandyard.handler.PlayerHandler;
import it.ziotob.game.scotlandyard.handler.StatusHandler;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.service.MatchService;
import it.ziotob.game.scotlandyard.service.PlayerService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        ThreadPool threadPool = new QueuedThreadPool(100, 10, 120);
        Server server = new Server(threadPool);

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(StatusHandler.class, "/status");
        handler.addServletWithMapping(MatchHandler.class, "/match/*");
        handler.addServletWithMapping(PlayerHandler.class, "/player/*");

        server.setHandler(handler);

        try {
            server.start();
        } catch (Exception e) {
            System.out.println("ERROR while starting server");
            e.printStackTrace();
        }

        //TODO start match expiration cleaner thread

        injectMatch();
        injectPlayer();
    }

    private static void injectMatch() {
        Database.getInstance().putEvent(new Event("0", Match.EVENT_CREATE, null, LocalDateTime.now()), Match.GROUP);
    }

    private static void injectPlayer() {
        MatchService.getInstance().getMatch("0")
                .ifPresent(match -> PlayerService.getInstance().createPlayer(match, "ziotob", "detective"));
    }
}
