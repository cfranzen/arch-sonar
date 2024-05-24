package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaField;
import de.cfranzen.archsonar.components.java.JavaMethod;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromClassfile;
import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JavaComponentDetectorEnumsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Nested
    class OperateOnSourceFiles {

        @Test
        void detectEnum() {
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
        void detectNestedEnum() {
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
        void ignoreLocalEnum() {
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
                    .hasSize(6)
                    .anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("values"),
                                    () -> assertThat(method.id().index()).isEqualTo(0)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("valueOf"),
                                    () -> assertThat(method.id().index()).isEqualTo(1)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("<init>"),
                                    () -> assertThat(method.id().index()).isEqualTo(2)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(3)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(4)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyString"),
                                    () -> assertThat(method.id().index()).isEqualTo(5)
                            )
                    );
        }
    }

    @Nested
    class OperateOnClassFiles {

        @Test
        void detectEnum() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.enums.MySimpleEnum");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.enums.MySimpleEnum"));
        }

        @Test
        void detectNestedEnum() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.enums.MyNestingEnum");

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
        void ignoreLocalEnum() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.enums.MyLocalNestingEnum");

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
            val resource = createResourceFromClassfile("de.dummy.project.enums.MySimpleEnum");

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
            val resource = createResourceFromClassfile("de.dummy.project.enums.MySimpleEnum");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val methods = components.programmingElements(JavaMethod.class);
            assertThat(methods)
                    .hasSize(7)
                    .anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("values"),
                                    () -> assertThat(method.id().index()).isEqualTo(0)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("valueOf"),
                                    () -> assertThat(method.id().index()).isEqualTo(1)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("<init>"),
                                    () -> assertThat(method.id().index()).isEqualTo(2)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(3)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(4)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyString"),
                                    () -> assertThat(method.id().index()).isEqualTo(5)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("<clinit>"),
                                    () -> assertThat(method.id().index()).isEqualTo(6)
                            )
                    );
        }
    }
}