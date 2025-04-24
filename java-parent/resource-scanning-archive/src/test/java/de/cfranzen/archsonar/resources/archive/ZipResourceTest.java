package de.cfranzen.archsonar.resources.archive;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipResourceTest {

    private static final String ZIP_FILENAME = "aaa.zip";
    private static final String[] ENTRY_FILENAMES = {"bbb.txt", "subdir/ccc.txt", "otherdir/ddd.txt"};
    private static final String CONTENT = "Random Content of ";

    static Stream<String> fileNamesProvider() {
        return Stream.of(ENTRY_FILENAMES);
    }

    @TempDir
    Path rootDir;

    private Path file;

    @BeforeEach
    void setupZipFile() throws IOException {
        file = rootDir.resolve(ZIP_FILENAME);
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(file))) {
            for (val entryFileName : ENTRY_FILENAMES) {
                byte[] data = (CONTENT + entryFileName).getBytes();
                out.putNextEntry(new ZipEntry(entryFileName));
                out.write(data, 0, data.length);
                out.closeEntry();
            }
        }
    }

    @Test
    void calculateUriOfZipResource() {
        // Given
        val entryFileName = ENTRY_FILENAMES[0];
        val sut = new ZipResource(file, new ZipEntry(entryFileName));

        // When
        val uri = sut.uri();

        // Then
        assertThat(uri.toString())
                .startsWith("jar:file://")
                .endsWith("/" + ZIP_FILENAME + "!/" + entryFileName);
    }

    @ParameterizedTest
    @MethodSource("fileNamesProvider")
    void readContentFromZipResource(String entryFileName) throws IOException {
        // Given
        val sut = new ZipResource(file, new ZipEntry(entryFileName));

        // When
        final String content;
        try (val reader = new BufferedReader(new InputStreamReader(sut.openInputStream()))) {
            content = reader.readLine();
        }

        // Then
        assertThat(content).isEqualTo(CONTENT + entryFileName);
    }

    @Test
    void failReadingOnIOException() {
        // Given
        val missingFile = rootDir.resolve("unknown.zip");
        val entryFileName = ENTRY_FILENAMES[0];
        val sut = new ZipResource(missingFile, new ZipEntry(entryFileName));

        // When / Then
        assertThatThrownBy(sut::openInputStream)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unable to open ZipInputStream");
    }

    @Test
    void failReadingOnMissingEntry() {
        // Given
        val missingEntryFileName = "unknown.txt";
        val sut = new ZipResource(file, new ZipEntry(missingEntryFileName));

        // When / Then
        assertThatThrownBy(sut::openInputStream)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unable to find entry in ZIP archive");
    }
}