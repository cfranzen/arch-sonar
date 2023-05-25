package de.cfranzen.archsonar.resources.test;

import de.cfranzen.archsonar.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.tika.mime.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@EqualsAndHashCode
@ToString(of = "uri")
public class MockResource implements Resource {

    private final URI uri;

    private final byte[] data;

    private MockResource(final URI uri, final byte[] data) {
        this.uri = uri;
        this.data = data;
    }

    public static MockResource fromText(final String uri, final String data) {
        return new MockResource(URI.create(uri), data.getBytes(StandardCharsets.UTF_8));
    }

    public static MockResource fromBytes(final String uri, final byte[] data) {
        return new MockResource(URI.create(uri), data);
    }

    public static MockResource fromFile(final Path file) {
        try {
            return new MockResource(file.toUri(), Files.readAllBytes(file));
        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize MockResource from file " + file, e);
        }
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public MediaType type() {
        return MediaType.OCTET_STREAM;
    }
}
