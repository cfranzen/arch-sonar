package de.cfranzen.archsonar.resources.preprocessing.mediatype;

import de.cfranzen.archsonar.resources.ResourcesCollection;
import de.cfranzen.archsonar.resources.preprocessing.ResourcesCollectionPreprocessor;
import de.cfranzen.archsonar.resources.test.MockResource;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class MediatypeDetectingResourcesCollectionPreprocessorTest {

    private final ResourcesCollectionPreprocessor sut = new MediatypeDetectingResourcesCollectionPreprocessor();

    @Test
    void detectMediatypeOfResources() {
        // Given
        final MockResource res1 = MockResource.fromText("file:///myclass.java", "some java code");
        final MockResource res2 = MockResource.fromBytes("file:///binary.dat", new byte[]{0xD, 0XE, 0xA, 0xD, 0xB, 0xE, 0xE, 0xF});
        final MockResourcesCollection collection = MockResourcesCollection.of(res1, res2);

        // When
        final ResourcesCollection preprocessed = sut.preprocess(collection);

        // Then
        assertThat(preprocessed.resources()
                .map(r -> Paths.get(r.uri().getPath()).getFileName().toString() + " " + r.type())
                .collect(toSet()))
                .contains(
                        "binary.dat application/octet-stream",
                        "myclass.java text/x-java-source"
                );
    }
}