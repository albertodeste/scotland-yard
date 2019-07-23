package it.ziotob.game.scotlandyard;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.handler.MatchHandler;
import it.ziotob.game.scotlandyard.handler.PlayerHandler;
import it.ziotob.game.scotlandyard.handler.StatusHandler;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.model.Position;
import it.ziotob.game.scotlandyard.service.MatchService;
import it.ziotob.game.scotlandyard.service.PlayerService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        injectPlayers();
        startMatch();
        setOverlappingPositions();
    }

    private static void setOverlappingPositions() {

        Match match = MatchService.getInstance().getMatch("0").orElseThrow(() -> new RuntimeException("ERROR"));
        List<Player> players = PlayerService.getInstance().getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());

        Position position = MatchService.getInstance().getMatch(match.getId()).map(Match::getPositions).map(Collection::stream).flatMap(Stream::findFirst).orElseThrow(() -> new RuntimeException("ERROR"));

        players.stream()
                .filter(player -> player.getRole().equals("detective"))
                .forEach(player -> PlayerService.getInstance().placePlayer(player, position.getNumber()));
    }

    private static void startMatch() {
        MatchService.getInstance().getMatch("0").map(match -> MatchService.getInstance().startMatch(match));
    }

    private static void injectMatch() {
        Database.getInstance().putEvent(new Event("0", Match.EVENT_CREATE, null, LocalDateTime.now()), Match.GROUP);
    }

    private static void injectPlayers() {

        MatchService.getInstance().getMatch("0")
                .ifPresent(match -> {
                    PlayerService.getInstance().createPlayer(match, "ziotob", "detective");
                    PlayerService.getInstance().createPlayer(match, "francesco", "mister_x");
                    PlayerService.getInstance().createPlayer(match, "alberto", "detective");
                });
    }
}
