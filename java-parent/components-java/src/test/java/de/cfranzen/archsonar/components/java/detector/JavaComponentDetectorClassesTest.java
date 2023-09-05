package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.SourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;

class JavaComponentDetectorClassesTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectClassInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.classes.MySimpleClass");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val elements = components.programmingElements();
        assertThat(elements).hasSize(1).allSatisfy(element ->
                assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.classes.MySimpleClass")));
    }

    @Test
    void detectNestedClassInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.classes.MyNestingClass");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val elements = components.programmingElements();
        assertThat(elements)
                .hasSize(3)
                .anySatisfy(element ->
                        assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                assertThat(type.id().fullyQualifiedName())
                                        .isEqualTo("de.dummy.project.classes.MyNestingClass")))
                .anySatisfy(element ->
                        assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                assertThat(type.id().fullyQualifiedName())
                                        .isEqualTo("de.dummy.project.classes.MyNestingClass.MyNestedClass")))
                .anySatisfy(element ->
                        assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                assertThat(type.id().fullyQualifiedName())
                                        .isEqualTo("de.dummy.project.classes.MyNestingClass.MyNestedClass.MyDoubleNestedClass")));
    }

    @Test
    void ignoreLocalClassInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.classes.MyLocalNestingClass");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val elements = components.programmingElements();
        assertThat(elements).hasSize(1).allSatisfy(element ->
                assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.classes.MyLocalNestingClass")));
    }
}