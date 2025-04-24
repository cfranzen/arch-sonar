package de.cfranzen.archsonar.resources.archive;

import de.cfranzen.archsonar.resources.test.MockResource;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.provider.Arguments;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ZipResourcesCollectionTest {

    private static final String ZIP_FILENAME1 = "aaa.zip";
    private static final String ZIP_FILENAME2 = "bbb.zip";
    private static final String ZIP_FILENAME3 = "empty.zip";

    private static final List<String> ENTRY_FILENAMES1 = List.of("bbb.txt", "subdir/ccc.txt", "otherdir/ddd.txt");
    private static final List<String> ENTRY_FILENAMES2 = List.of("abc.txt");
    private static final List<String> ENTRY_FILENAMES3 = List.of();
    private static final List<List<String>> ENTRY_FILENAMES = List.of(ENTRY_FILENAMES1, ENTRY_FILENAMES2, ENTRY_FILENAMES3);

    private static final String CONTENT = "Random Content of ";

    static Stream<Arguments> zipFilesProvider() {
        return Stream.of(
                arguments(ZIP_FILENAME1, ENTRY_FILENAMES1),
                arguments(ZIP_FILENAME2, ENTRY_FILENAMES2),
                arguments(ZIP_FILENAME3, ENTRY_FILENAMES3)
        );
    }

    @TempDir
    Path rootDir;

    List<Path> files = new ArrayList<>();

    @BeforeEach
    void setupZipFiles() {
        zipFilesProvider().forEach(arguments -> {
            Path file = rootDir.resolve((String) arguments.get()[0]);
            try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(file))) {
                List<String> entryFileNames = (List<String>) arguments.get()[1];
                for (val entryFileName : entryFileNames) {
                    byte[] data = (CONTENT + entryFileName).getBytes();
                    out.putNextEntry(new ZipEntry(entryFileName));
                    out.write(data, 0, data.length);
                    out.closeEntry();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            files.add(file);
        });
    }

    @Test
    void iterateOverAllResourcesOfSingleZipFile() throws IOException {
        // Given
        val zipFile = files.getFirst();
        val sut = new ZipResourcesCollection(MockResource.fromFile(zipFile));

        // When
        val iterated = sut.resources().map(r -> r.uri().toString()).collect(toSet());

        // Then
        try (FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + zipFile.toUri()), Map.of())) {
            final Set<String> expectedFiles = ENTRY_FILENAMES1.stream()
                    .map(it -> fileSystem.getPath(it).toUri().toString())
                    .collect(toSet());
            assertThat(iterated).isEqualTo(expectedFiles);
        }
    }

    @Test
    void iterateOverAllResourcesOfMultipleZipFiles() {
        // Given
        val sut = new ZipResourcesCollection(files.stream().map(MockResource::fromFile).toList());

        // When
        val iterated = sut.resources().map(r -> r.uri().toString()).collect(toSet());

        // Then
        final Set<String> expectedFiles = new HashSet<>();
        for (int i = 0; i < files.size(); i++) {
            val zipFile = files.get(i);
            try (FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + zipFile.toUri()), Map.of())) {
                expectedFiles.addAll(ENTRY_FILENAMES.get(i).stream()
                        .map(it -> fileSystem.getPath(it).toUri().toString())
                        .toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        assertThat(iterated).isEqualTo(expectedFiles);
    }

    @Test
    void filterOutDuplicateZipFiles() throws IOException {
        // Given
        val zipFileResource = MockResource.fromFile(files.getFirst());
        val sut = new ZipResourcesCollection(zipFileResource, zipFileResource);

        // When
        val iterated = sut.resources().map(r -> r.uri().toString()).collect(toSet());

        // Then
        try (FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + zipFileResource.uri()), Map.of())) {
            final Set<String> expectedFiles = ENTRY_FILENAMES1.stream()
                    .map(it -> fileSystem.getPath(it).toUri().toString())
                    .collect(toSet());
            assertThat(iterated).isEqualTo(expectedFiles);
        }
    }
}