package de.cfranzen.archsonar.resources.filesystem

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path


class PathResourcesCollectionTest {

    @TempDir
    lateinit var rootDir: Path

    private val files = listOf(
        "dir1/subdir11/aaa.txt",
        "dir1/subdir11/bbb.txt",
        "dir1/subdir11/subdir111/ccc.txt",
        "dir1/subdir12/ddd.txt",
        "dir1/subdir12/eee.txt",
        "dir2/fff.txt",
        "dir3/subdir11/ggg.txt",
    )

    @BeforeEach
    fun setupFilesystem() {
        files.forEach {
            val file = rootDir.resolve(it)
            file.parent.toFile().mkdirs()
            Files.writeString(file, "Random content")
        }
    }

    @Test
    fun `iterate over all resources of a single directory`() {
        // Given
        val sut = PathResourcesCollection(rootDir)

        // When
        val iterated = sut.map { it.uri.path }.toSet()

        // Then
        assertThat(iterated).isEqualTo(files.map { rootDir.resolve(it).toString() }.toSet())
    }

    @Test
    fun `iterate over all resources of multiple directories`() {
        // Given
        val sut = PathResourcesCollection(
            rootDir.resolve("dir1"),
            rootDir.resolve("dir2"),
            rootDir.resolve("dir3"),
        )

        // When
        val iterated = sut.map { it.uri.path }.toSet()

        // Then
        assertThat(iterated).isEqualTo(files.map { rootDir.resolve(it).toString() }.toSet())
    }

    @Test
    fun `iterate over all resources of directories and files`() {
        // Given
        val sut = PathResourcesCollection(
            rootDir.resolve("dir1"),
            rootDir.resolve("dir2"),
            rootDir.resolve(files.last()),
        )

        // When
        val iterated = sut.map { it.uri.path }.toSet()

        // Then
        assertThat(iterated).isEqualTo(files.map { rootDir.resolve(it).toString() }.toSet())
    }

    @Test
    fun `filter out duplicate files`() {
        // Given
        val sut = PathResourcesCollection(rootDir, rootDir)

        // When
        val iterated = sut.map { it.uri.path }.toSet()

        // Then
        assertThat(iterated).isEqualTo(files.map { rootDir.resolve(it).toString() }.toSet())
    }
}