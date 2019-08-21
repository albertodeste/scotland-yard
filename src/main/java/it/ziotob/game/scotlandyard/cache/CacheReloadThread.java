package it.ziotob.game.scotlandyard.cache;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CacheReloadThread implements Runnable {

    private static final Logger LOG = LogManager.getLogger(CacheReloadThread.class.getSimpleName());

    private final WebappCache cache;
    private final File scanDirectory;

    @Override
    public void run() {

        while (true) {

            loadCache();

            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {
            }
        }
    }

    public void loadCache() {
        detectCacheChanges().forEach(this::loadCacheResource);
    }

    private void loadCacheResource(String resourcePath) {

        File resource = new File(scanDirectory.getAbsolutePath() + "/" + resourcePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource)))) {

            LocalDateTime lastEditDateTime = extractLastEdit(resource);
            cache.set(resourcePath, new CacheEntry(reader.lines().collect(Collectors.joining("\n")), lastEditDateTime));
        } catch (Exception e) {
            LOG.error("Error while reloading resource " + resourcePath, e);
        }
    }

    private Stream<String> detectCacheChanges() {

        return Optional.of(scanDirectory)
                .map(this::loadResourcesRecursive)
                .orElse(Stream.empty())
                .filter(file -> cache.hasChanged(extractResourceName(file), extractLastEdit(file)))
                .map(this::extractResourceName);
    }

    private String extractResourceName(File file) {
        return file.getAbsolutePath().replace(scanDirectory.getAbsolutePath(), "").replaceAll("^/", "");
    }

    private Stream<File> loadResourcesRecursive(File file) {

        if (!file.exists()) {
            return Stream.empty();
        } else {

            return Optional.ofNullable(file.listFiles())
                    .map(Stream::of)
                    .orElse(Stream.empty())
                    .flatMap(this::mapIfDirectory);
        }
    }

    private Stream<File> mapIfDirectory(File file) {

        if (!file.exists()) {
            return Stream.empty();
        } else if (file.isDirectory()) {
            return loadResourcesRecursive(file);
        } else {
            return Stream.of(file);
        }
    }

    private LocalDateTime extractLastEdit(File file) {
        return Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
