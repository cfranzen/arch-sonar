package de.cfranzen.archsonar.resources.archive;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourcesCollection;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

public class ZipResourcesCollection implements ResourcesCollection {

    private final Set<Resource> resources = new HashSet<>();

    public ZipResourcesCollection(final Collection<? extends Resource> zipResources) {
        for (val zipResource : zipResources) {
            val file = getFile(zipResource);
            val filePath = file.toPath();

            try (val zipFile = new ZipFile(file)) {
                zipFile.entries().asIterator().forEachRemaining(entry -> {
                    if (!entry.isDirectory()) {
                        resources.add(new ZipResource(filePath, entry));
                    }
                });
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to traverse files within ZIP archive " + zipResource.uri(), e);
            }
        }
    }

    public ZipResourcesCollection(final Resource... zipResources) {
        this(zipResources == null ? Collections.emptyList() : Arrays.asList(zipResources));
    }

    @Override
    public Stream<Resource> resources() {
        return resources.stream();
    }

    private static File getFile(final Resource zipResource) {
        final var file = new File(zipResource.uri());
        if (!file.exists()) {
            throw new IllegalArgumentException("Resource " + zipResource.uri() + " does not exist or is not a file");
        }
        return file;
    }
}