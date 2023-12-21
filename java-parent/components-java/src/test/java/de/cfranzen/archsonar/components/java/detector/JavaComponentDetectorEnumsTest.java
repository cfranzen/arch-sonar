package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;

class JavaComponentDetectorEnumsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectEnumInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.enums.MySimpleEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types).hasSize(1).allSatisfy(type ->
                assertThat(type.id().fullyQualifiedName())
                        .isEqualTo("de.dummy.project.enums.MySimpleEnum"));
    }

    @Test
    void detectNestedEnumInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.enums.MyNestingEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types)
                .hasSize(3)
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.enums.MyNestingEnum"))
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.enums.MyNestingEnum.MyNestedEnum"))
                .anySatisfy(type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.enums.MyNestingEnum.MyNestedEnum.MyDoubleNestedEnum"));
    }

    @Test
    void ignoreLocalEnumInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.enums.MyLocalNestingEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val types = components.programmingElements(JavaType.class);
        assertThat(types).hasSize(1).allSatisfy(type ->
                assertThat(type.id().fullyQualifiedName())
                        .isEqualTo("de.dummy.project.enums.MyLocalNestingEnum"));
    }
}