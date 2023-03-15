package de.cfranzen.archsonar.resources.filesystem

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path


private const val FILENAME = "aaa.txt"
private const val CONTENT = "Random Content"

class PathResourceTest {

    @TempDir
    lateinit var rootDir: Path

    private lateinit var file: Path

    @BeforeEach
    fun setupFilesystem() {
        file = rootDir.resolve(FILENAME)
        Files.writeString(file, CONTENT)
    }

    @Test
    fun `calculate URI of PathResource`() {
        // Given
        val sut = PathResource(file)

        // When
        val uri = sut.uri

        // Then
        assertThat(uri.toString())
            .startsWith("file://")
            .endsWith("/$FILENAME")
    }

    @Test
    fun `read content from PathResource`() {
        // Given
        val sut = PathResource(file)

        // When
        val content = sut.openInputStream().use {
            BufferedReader(InputStreamReader(it)).readLine()
        }

        // Then
        assertThat(content).isEqualTo(CONTENT)
    }
}