package de.cfranzen.archsonar.resources.test;

import de.cfranzen.archsonar.resources.Resource;
import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class MockResourceTest {

    private static final String TEXT = "This is some random text";

    private static final byte[] DATA = TEXT.getBytes(StandardCharsets.UTF_8);

    @TempDir
    Path rootDir;

    @Test
    void createMockResourceFromFile() throws IOException {
        // Given
        final Path file = rootDir.resolve("dummyfile.txt");
        Files.writeString(file, TEXT);

        // When
        final Resource res = MockResource.fromFile(file);

        // Then
        assertThat(res.uri()).isEqualTo(file.toUri());
        assertThat(res.type()).isEqualTo(MediaType.OCTET_STREAM);
        assertThat(readBytes(res.openInputStream())).isEqualTo(TEXT);
    }

    @Test
    void createMockResourceFromText() throws IOException {
        // Given
        final String filename = "file:///dummyfile.txt";

        // When
        final Resource res = MockResource.fromText(filename, TEXT);

        // Then
        assertThat(res.uri()).isEqualTo(URI.create(filename));
        assertThat(res.type()).isEqualTo(MediaType.OCTET_STREAM);
        assertThat(readBytes(res.openInputStream())).isEqualTo(TEXT);
    }

    @Test
    void createMockResourceFromBytes() throws IOException {
        // Given
        final String filename = "file:///dummyfile.txt";

        // When
        final Resource res = MockResource.fromBytes(filename, DATA);

        // Then
        assertThat(res.uri()).isEqualTo(URI.create(filename));
        assertThat(res.type()).isEqualTo(MediaType.OCTET_STREAM);
        assertThat(readBytes(res.openInputStream())).isEqualTo(TEXT);
    }

    private String readBytes(final InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}