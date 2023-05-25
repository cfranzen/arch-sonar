package de.cfranzen.archsonar.resources.filesystem;

import de.cfranzen.archsonar.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.tika.mime.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@EqualsAndHashCode(of = "path")
@ToString(of = "uri")
public class PathResource implements Resource {

    private final Path path;

    private final URI uri;

    PathResource(final Path path) {
        this.path = path;
        this.uri = path.toAbsolutePath().toUri();
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public InputStream openInputStream() {
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to open InputStream for " + path, e);
        }
    }

    @Override
    public MediaType type() {
        return MediaType.OCTET_STREAM;
    }
}