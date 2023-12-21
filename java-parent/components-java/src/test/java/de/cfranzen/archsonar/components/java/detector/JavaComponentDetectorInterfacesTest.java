package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;

class JavaComponentDetectorInterfacesTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectInterfaceInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.interfaces.MySimpleInterface");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types).hasSize(1).allSatisfy(type ->
                assertThat(type.id().fullyQualifiedName())
                        .isEqualTo("de.dummy.project.interfaces.MySimpleInterface"));
    }

    @Test
    void detectNestedInterfaceInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.interfaces.MyNestingInterface");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types)
                .hasSize(3)
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.interfaces.MyNestingInterface"))
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.interfaces.MyNestingInterface.MyNestedInterface"))
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.interfaces.MyNestingInterface.MyNestedInterface.MyDoubleNestedInterface"));
    }

    @Test
    void ignoreLocalInterfaceInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.interfaces.MyLocalNestingInterface");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types).hasSize(1).allSatisfy(type ->
                assertThat(type.id().fullyQualifiedName())
                        .isEqualTo("de.dummy.project.interfaces.MyLocalNestingInterface"));
    }
}