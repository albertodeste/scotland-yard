package it.ziotob.game.scotlandyard.model.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class Connection {

    private final Long destination;
    private final Graph.ConnectionType type;

    public boolean matches(Long destination, String moveType) {
        return Objects.equals(this.destination, destination) && Objects.equals(type.getValue(), moveType);
    }
}
