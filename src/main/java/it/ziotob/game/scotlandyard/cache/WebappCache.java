package it.ziotob.game.scotlandyard.cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WebappCache {

    private final Map<String, CacheEntry> cache;

    public WebappCache() {
        cache = new HashMap<>();
    }

    public void set(String resourcePath, CacheEntry cacheEntry) {
        cache.put(resourcePath, cacheEntry);
    }

    public Optional<String> get(String resource) {
        return Optional.ofNullable(cache.get(resource)).map(CacheEntry::getContent);
    }

    public boolean hasChanged(String resource, LocalDateTime lastEdit) {

        return Optional.ofNullable(cache.get(resource))
                .map(CacheEntry::getLastChange)
                .map(cacheEdit -> cacheEdit.isBefore(lastEdit))
                .orElse(true);
    }
}
