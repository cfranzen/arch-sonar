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

class JavaComponentDetectorAnnotationsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Nested
    class OperateOnSourceFiles {

        @Test
        void detectAnnotation() {
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
        void detectNestedAnnotation() {
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


        @Test
        void findFieldsOfAnnotation() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.annotations.MySimpleAnnotation");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val fields = components.programmingElements(JavaField.class);
            assertThat(fields)
                    .hasSize(1)
                    .allSatisfy(field -> assertThat(field.id().name()).isEqualTo("myInt"));

        }

        @Test
        void findMethodsOfAnnotation() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.annotations.MySimpleAnnotation");

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


    @Nested
    class OperateOnClassFiles {

        @Test
        void detectAnnotation() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.annotations.MySimpleAnnotation");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.annotations.MySimpleAnnotation"));
        }

        @Test
        void detectNestedAnnotation() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.annotations.MyNestingAnnotation");

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


        @Test
        void findFieldsOfAnnotation() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.annotations.MySimpleAnnotation");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val fields = components.programmingElements(JavaField.class);
            assertThat(fields)
                    .hasSize(1)
                    .allSatisfy(field -> assertThat(field.id().name()).isEqualTo("myInt"));

        }

        @Test
        void findMethodsOfAnnotation() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.annotations.MySimpleAnnotation");

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
}