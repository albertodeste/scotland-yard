package it.ziotob.game.scotlandyard.model.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {

    private final Map<Long, List<Connection>> connections;

    public Graph() {
        connections = new HashMap<>();
    }

    public void connect(Integer source, ConnectionType type, Integer... destinations) {
        Arrays.stream(destinations).forEach(destination -> connect(source.longValue(), type, destination.longValue()));
    }

    private void connect(Long source, ConnectionType type, Long destination) {

        addConnection(source, destination, type);
        addConnection(destination, source, type);
    }

    private void addConnection(Long source, Long destination, ConnectionType type) {

        if (!connections.containsKey(source)) {
            connections.put(source, new LinkedList<>());
        }

        connections.get(source).add(new Connection(destination, type));
    }

    public boolean isConnected(Long source, Long destination, String moveType) {

        return Optional.ofNullable(connections.get(source))
                .filter(n -> n.stream().anyMatch(connection -> connection.matches(destination, moveType)))
                .isPresent();
    }

    public List<Long> getReachablePositions(Long position, List<String> connectionTypes) {

        return Optional.ofNullable(connections.get(position))
                .map(Collection::stream)
                .orElse(Stream.empty())
                .filter(connection -> connectionTypes.contains(connection.getType().getValue()))
                .map(Connection::getDestination)
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    @Getter
    public static class ConnectionType {

        private final String value;

        public static final ConnectionType LOW = new ConnectionType("low");
        public static final ConnectionType MID = new ConnectionType("mid");
        public static final ConnectionType HIGH = new ConnectionType("high");
        public static final ConnectionType MISTER_X = new ConnectionType("mister_x");
    }
}
