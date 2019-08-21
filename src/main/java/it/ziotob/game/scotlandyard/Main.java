package it.ziotob.game.scotlandyard;

import it.ziotob.game.scotlandyard.handler.MatchHandler;
import it.ziotob.game.scotlandyard.handler.PlayerHandler;
import it.ziotob.game.scotlandyard.handler.StatusHandler;
import it.ziotob.game.scotlandyard.handler.WebappHandler;
import it.ziotob.game.scotlandyard.service.WebappCacheService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

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
        handler.addServletWithMapping(WebappHandler.class, "/webapp/*");

        server.setHandler(handler);

        try {
            server.start();
        } catch (Exception e) {
            System.out.println("ERROR while starting server");
            e.printStackTrace();
        }

        //TODO start match expiration cleaner thread

        String resourcesDirectory = Optional.ofNullable(Main.class.getClassLoader().getResource("."))
                .map(URL::getFile)
                .orElseThrow(() -> new RuntimeException("Error while determining resourcesDirectory"));

        if (isDevMode(args)) {
            WebappCacheService.getInstance().startReloadThread(new File(resourcesDirectory + "/../../../src/main/resources/webapp"));
        } else {
            WebappCacheService.getInstance().loadCache(new File(resourcesDirectory + "../resources/webapp"));
        }
    }

    private static boolean isDevMode(String[] args) {
        return Arrays.asList(args).contains("--dev");
    }
}
