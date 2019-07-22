package it.ziotob.game.scotlandyard.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Position {

    private final Long number;
    private Boolean used = false;
    private Boolean misterX = false;

    public String toJSON() {

        return String.format("{\"number\": %d, \"used\": %s, \"is_mister_x\": %s}",
                number,
                used.toString(),
                misterX.toString());
    }

    public void setUsed() {
        used = true;
    }

    public void setMisterX() {
        misterX = true;
    }
}
