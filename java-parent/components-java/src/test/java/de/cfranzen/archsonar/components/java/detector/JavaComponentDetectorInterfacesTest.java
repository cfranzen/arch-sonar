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

    @Test
    void findFieldsOfInterface() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.interfaces.MySimpleInterface");

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
    void findMethodsOfInterface() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.interfaces.MySimpleInterface");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val methods = components.programmingElements(JavaMethod.class);
        assertThat(methods)
                .hasSize(6)
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
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("setMyEnum"),
                                () -> assertThat(method.id().index()).isEqualTo(3)
                        )
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("setMyInt"),
                                () -> assertThat(method.id().index()).isEqualTo(4)
                        )
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("setMyString"),
                                () -> assertThat(method.id().index()).isEqualTo(5)
                        )
                );
    }
}