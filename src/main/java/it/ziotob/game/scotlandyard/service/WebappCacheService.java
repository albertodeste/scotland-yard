package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.cache.CacheReloadThread;
import it.ziotob.game.scotlandyard.cache.WebappCache;

import java.io.File;
import java.util.Optional;

import static java.util.Objects.isNull;

public class WebappCacheService {

    private static WebappCacheService instance;

    private final WebappCache cache;

    public static WebappCacheService getInstance() {

        if (isNull(instance)) {
            instance = new WebappCacheService();
        }

        return instance;
    }

    private WebappCacheService() {
        cache = new WebappCache();
    }


    public void startReloadThread(File scanDirectory) {
        new CacheReloadThread(cache, scanDirectory).run();
    }

    public Optional<String> getResource(String resource) {
        return cache.get(resource);
    }

    public void loadCache(File scanDirectory) {
        new CacheReloadThread(cache, scanDirectory).loadCache();
    }
}
