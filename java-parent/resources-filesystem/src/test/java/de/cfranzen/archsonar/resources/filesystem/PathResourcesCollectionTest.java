package de.cfranzen.archsonar.resources.filesystem;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;


class PathResourcesCollectionTest {

    @TempDir
    Path rootDir;

    private final List<String> files = List.of(
            "dir1/subdir11/aaa.txt",
            "dir1/subdir11/bbb.txt",
            "dir1/subdir11/subdir111/ccc.txt",
            "dir1/subdir12/ddd.txt",
            "dir1/subdir12/eee.txt",
            "dir2/fff.txt",
            "dir3/subdir11/ggg.txt"
    );

    @BeforeEach
    void setupFilesystem() {
        for (val relativePath : files) {
            val file = rootDir.resolve(relativePath);
            file.getParent().toFile().mkdirs();
            try {
                Files.writeString(file, "Random content");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void iterate_over_all_resources_of_a_single_directory() {
        // Given
        val sut = new PathResourcesCollection(rootDir);

        // When
        val iterated = sut.resources().map(r -> r.uri().getPath()).collect(toSet());

        // Then
        final Set<String> expectedFiles = files.stream()
                .map(it -> rootDir.resolve(it).toString())
                .collect(toSet());
        assertThat(iterated).isEqualTo(expectedFiles);
    }

    @Test
    void iterate_over_all_resources_of_multiple_directories() {
        // Given
        val sut = new PathResourcesCollection(
                rootDir.resolve("dir1"),
                rootDir.resolve("dir2"),
                rootDir.resolve("dir3")
        );

        // When
        val iterated = sut.resources().map(r -> r.uri().getPath()).collect(toSet());

        // Then
        final Set<String> expectedFiles = files.stream()
                .map(it -> rootDir.resolve(it).toString())
                .collect(toSet());
        assertThat(iterated).isEqualTo(expectedFiles);
    }

    @Test
    void iterate_over_all_resources_of_directories_and_files() {
        // Given
        val sut = new PathResourcesCollection(
                rootDir.resolve("dir1"),
                rootDir.resolve("dir2"),
                rootDir.resolve(files.get(files.size() - 1))
        );

        // When
        val iterated = sut.resources().map(r -> r.uri().getPath()).collect(toSet());

        // Then
        final Set<String> expectedFiles = files.stream()
                .map(it -> rootDir.resolve(it).toString())
                .collect(toSet());
        assertThat(iterated).isEqualTo(expectedFiles);
    }

    @Test
    void filter_out_duplicate_files() {
        // Given
        val sut = new PathResourcesCollection(rootDir, rootDir);

        // When
        val iterated = sut.resources().map(r -> r.uri().getPath()).collect(toSet());

        // Then
        final Set<String> expectedFiles = files.stream()
                .map(it -> rootDir.resolve(it).toString())
                .collect(toSet());
        assertThat(iterated).isEqualTo(expectedFiles);
    }
}