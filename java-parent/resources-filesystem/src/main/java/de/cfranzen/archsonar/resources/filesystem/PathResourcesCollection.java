package de.cfranzen.archsonar.resources.filesystem;

import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourcesCollection;
import lombok.extern.log4j.Log4j2;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Log4j2
public class PathResourcesCollection implements ResourcesCollection {

    private final Set<Resource> resources = new HashSet<>();

    public PathResourcesCollection(final Collection<Path> paths) {
        final Detector detector = createDetector();
        for (var path : paths) {
            try (var stream = Files.walk(path)) {
                resources.addAll(stream
                        .filter(Files::isRegularFile)
                        .map(file -> mapToResource(file, detector))
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

    private PathResource mapToResource(final Path file, final Detector detector) {
        final MediaType mediaType = detectMediaType(file, detector);
        return new PathResource(file, mediaType);
    }

    private static MediaType detectMediaType(final Path file, final Detector detector) {
        try (final TikaInputStream is = TikaInputStream.get(file)) {
            final Metadata metadata = new Metadata();
            metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, file.getFileName().toString());

            return detector.detect(is, metadata);
        } catch (IOException | RuntimeException e) {
            log.warn("Unable to detect media type of file {}", file, e);
            return MediaType.OCTET_STREAM;
        }
    }

    private static Detector createDetector() {
        return new DefaultDetector();
    }
}