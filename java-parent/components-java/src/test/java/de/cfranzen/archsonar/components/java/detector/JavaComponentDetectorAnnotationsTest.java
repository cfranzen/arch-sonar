package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;

class JavaComponentDetectorAnnotationsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectAnnotationInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.annotations.MySimpleAnnotation");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types).hasSize(1).allSatisfy(type ->
                assertThat(type.id().fullyQualifiedName())
                        .isEqualTo("de.dummy.project.annotations.MySimpleAnnotation"));
    }

    @Test
    void detectNestedAnnotationInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.annotations.MyNestingAnnotation");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types)
                .hasSize(3)
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.annotations.MyNestingAnnotation"))
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.annotations.MyNestingAnnotation.MyNestedAnnotation"))
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.annotations.MyNestingAnnotation.MyNestedAnnotation.MyDoubleNestedAnnotation"));
    }
}