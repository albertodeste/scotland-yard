package it.ziotob.game.scotlandyard.model.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Graph {

    private final Map<Long, Map<Long, ConnectionType>> connections;

    public Graph() {
        connections = new HashMap<>();
    }

    public void connect(Long source, ConnectionType type, Long... destinations) {

        Arrays.stream(destinations).forEach(destination -> connect(source, destination, type));
    }

    public void connect(Long source, Long destination, ConnectionType type) {

        addConnection(source, destination, type);
        addConnection(destination, source, type);
    }

    private void addConnection(Long source, Long destination, ConnectionType type) {

        if (!connections.containsKey(source)) {
            connections.put(source, new HashMap<>());
        }

        connections.get(source).put(destination, type);
    }

    public boolean isConnected(Long source, Long destination, String moveType) {

        return Optional.ofNullable(connections.get(source))
                .map(n -> n.get(destination))
                .filter(type -> type.getValue().equals(moveType))
                .isPresent();
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
