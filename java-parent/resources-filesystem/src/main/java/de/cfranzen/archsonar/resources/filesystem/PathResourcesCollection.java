package de.cfranzen.archsonar.resources.filesystem;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourcesCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class PathResourcesCollection implements ResourcesCollection {

    private final Set<Resource> resources = new HashSet<>();

    public PathResourcesCollection(final Collection<Path> paths) {
        for (var path : paths) {
            try (var stream = Files.walk(path)) {
                resources.addAll(stream
                        .filter(Files::isRegularFile)
                        .map(PathResource::new)
                        .collect(toSet()));
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to traverse path " + path, e);
            }
        }
    }

    public PathResourcesCollection(final Path... paths) {
        this(paths == null ? Collections.emptyList() : Arrays.asList(paths));
    }

    @Override
    public Stream<Resource> resources() {
        return resources.stream();
    }
}