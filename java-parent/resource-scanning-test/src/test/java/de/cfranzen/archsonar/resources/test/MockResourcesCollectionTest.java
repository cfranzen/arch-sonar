package de.cfranzen.archsonar.resources.test;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MockResourcesCollectionTest {

    @Test
    void createEmptyCollection() {
        // Given / When
        final MockResourcesCollection collection = MockResourcesCollection.empty();

        // Then
        assertThat(collection.resources()).isEmpty();
    }

    @Test
    void createCollectionFromResources() {
        // Given
        final MockResource res1 = MockResource.fromText("file:///file1.txt", "My first file");
        final MockResource res2 = MockResource.fromText("file:///file2.txt", "My second file");

        // When
        final MockResourcesCollection collection = MockResourcesCollection.of(res1, res2);

        // Then
        assertThat(collection.resources()).containsExactly(res1, res2);
    }

    @Test
    void createCollectionFromCollection() {
        // Given
        final MockResource res1 = MockResource.fromText("file:///file1.txt", "My first file");
        final MockResource res2 = MockResource.fromText("file:///file2.txt", "My second file");
        final List<MockResource> resources = Arrays.asList(res1, res2);

        // When
        final MockResourcesCollection collection = MockResourcesCollection.of(resources);

        // Then
        assertThat(collection.resources()).containsExactly(res1, res2);
    }
}