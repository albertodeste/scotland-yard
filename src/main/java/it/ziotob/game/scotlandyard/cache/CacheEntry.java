package it.ziotob.game.scotlandyard.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CacheEntry {

    private final String content;
    private final LocalDateTime lastChange;

    public CacheEntry(String content) {

        this.content = content;
        lastChange = LocalDateTime.now();
    }
}
