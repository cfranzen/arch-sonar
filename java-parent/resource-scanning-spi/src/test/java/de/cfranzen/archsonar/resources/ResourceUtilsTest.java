package de.cfranzen.archsonar.resources;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceUtilsTest {

    @Test
    void readContentFromResourceToByteArray() throws IOException {
        // Given
        val content = "This is the content";
        val resource = InMemoryResource.fromText("test.txt", content);

        // When
        val result = ResourceUtils.readContent(resource);

        // Then
        assertThat(result).isEqualTo(content.getBytes());
    }

    @Test
    void readContentFromResourceToByteArray_EmptyResource() throws IOException {
        // Given
        val resource = InMemoryResource.fromBytes("test.txt", new byte[0]);

        // When
        val result = ResourceUtils.readContent(resource);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void readContentFromResourceToString() throws IOException {
        // Given
        val content = "This is the content";
        val resource = InMemoryResource.fromText("test.txt", content);

        // When
        val result = ResourceUtils.readContentAsString(resource);

        // Then
        assertThat(result).isEqualTo(content);
    }

    @Test
    void readContentFromResourceToString_EmptyResource() throws IOException {
        // Given
        val resource = InMemoryResource.fromBytes("test.txt", new byte[0]);

        // When
        val result = ResourceUtils.readContentAsString(resource);

        // Then
        assertThat(result).isEmpty();
    }
}