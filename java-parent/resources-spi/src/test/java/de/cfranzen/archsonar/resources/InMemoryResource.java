package de.cfranzen.archsonar.resources;

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
public class InMemoryResource implements Resource {

    private static final MediaType DEFAULT_MEDIATYPE = MediaType.OCTET_STREAM;

    private final URI uri;

    private final byte[] data;

    private final MediaType mediaType;

    private InMemoryResource(final URI uri, final byte[] data, final MediaType mediaType) {
        this.uri = uri;
        this.data = data;
        this.mediaType = mediaType;
    }

    public static InMemoryResource fromText(final String uri, final String data) {
        return fromText(uri, data, DEFAULT_MEDIATYPE);
    }

    public static InMemoryResource fromText(final String uri, final String data, final MediaType mediaType) {
        return new InMemoryResource(URI.create(uri), data.getBytes(StandardCharsets.UTF_8), mediaType);
    }

    public static InMemoryResource fromBytes(final String uri, final byte[] data) {
        return fromBytes(uri, data, DEFAULT_MEDIATYPE);
    }

    public static InMemoryResource fromBytes(final String uri, final byte[] data, final MediaType mediaType) {
        return new InMemoryResource(URI.create(uri), data, mediaType);
    }

    public static InMemoryResource fromFile(final Path file) {
        return fromFile(file, DEFAULT_MEDIATYPE);
    }

    public static InMemoryResource fromFile(final Path file, final MediaType mediaType) {
        try {
            return new InMemoryResource(file.toUri(), Files.readAllBytes(file), mediaType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to initialize MockResource from file " + file, e);
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
        return mediaType;
    }
}
