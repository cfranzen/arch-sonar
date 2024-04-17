package de.cfranzen.archsonar.resources.filesystem;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;


class PathResourceTest {

    private static final String FILENAME = "aaa.txt";
    private static final String CONTENT = "Random Content";

    @TempDir
    Path rootDir;

    private Path file;

    @BeforeEach
    void setupFilesystem() throws IOException {
        file = rootDir.resolve(FILENAME);
        Files.writeString(file, CONTENT);
    }

    @Test
    void calculateUriOfPathResource() {
        // Given
        val sut = new PathResource(file);

        // When
        val uri = sut.uri();

        // Then
        assertThat(uri.toString())
                .startsWith("file://")
                .endsWith("/" + FILENAME);
    }

    @Test
    void readContentFromPathResource() throws IOException {
        // Given
        val sut = new PathResource(file);

        // When
        final String content;
        try (val reader = new BufferedReader(new InputStreamReader(sut.openInputStream()))) {
            content = reader.readLine();
        }

        // Then
        assertThat(content).isEqualTo(CONTENT);
    }
}