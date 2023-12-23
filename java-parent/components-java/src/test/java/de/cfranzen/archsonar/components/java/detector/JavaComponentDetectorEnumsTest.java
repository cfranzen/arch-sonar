package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaField;
import de.cfranzen.archsonar.components.java.JavaMethod;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    void findFieldsOfEnum() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.enums.MySimpleEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val fields = components.programmingElements(JavaField.class);
        assertThat(fields)
                .hasSize(3)
                .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myEnum"))
                .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myString"))
                .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myInt"));

    }

    @Test
    void findMethodsOfEnum() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.enums.MySimpleEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val methods = components.programmingElements(JavaMethod.class);
        assertThat(methods)
                .hasSize(3)
                .anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("getMyInt"),
                                () -> assertThat(method.id().index()).isEqualTo(0)
                        )
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("getMyEnum"),
                                () -> assertThat(method.id().index()).isEqualTo(1)
                        )
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("getMyString"),
                                () -> assertThat(method.id().index()).isEqualTo(2)
                        )
                );
    }
}