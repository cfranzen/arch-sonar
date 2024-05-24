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

class JavaComponentDetectorRecordsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Nested
    class OperateOnSourceFiles {

        @Test
        void detectRecord() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.records.MySimpleRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.records.MySimpleRecord"));
        }

        @Test
        void detectNestedRecord() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.records.MyNestingRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types)
                    .hasSize(3)
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.records.MyNestingRecord"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.records.MyNestingRecord.MyNestedRecord"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.records.MyNestingRecord.MyNestedRecord.MyDoubleNestedRecord"));
        }

        @Test
        void ignoreLocalRecord() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.records.MyLocalNestingRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.records.MyLocalNestingRecord"));
        }

        @Test
        void findFieldsOfRecord() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.records.MySimpleRecord");

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
        void findMethodsOfRecord() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.records.MySimpleRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val methods = components.programmingElements(JavaMethod.class);
            assertThat(methods)
                    .hasSize(7)
                    .anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("<init>"),
                                    () -> assertThat(method.id().index()).isEqualTo(0)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("myInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(1)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("myEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(2)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("myString"),
                                    () -> assertThat(method.id().index()).isEqualTo(3)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("toString"),
                                    () -> assertThat(method.id().index()).isEqualTo(4)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("hashCode"),
                                    () -> assertThat(method.id().index()).isEqualTo(5)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("equals"),
                                    () -> assertThat(method.id().index()).isEqualTo(6)
                            )
                    );
        }
    }


    @Nested
    class OperateOnClassFiles {

        @Test
        void detectRecord() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.records.MySimpleRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.records.MySimpleRecord"));
        }

        @Test
        void detectNestedRecord() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.records.MyNestingRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types)
                    .hasSize(3)
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.records.MyNestingRecord"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.records.MyNestingRecord.MyNestedRecord"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.records.MyNestingRecord.MyNestedRecord.MyDoubleNestedRecord"));
        }

        @Test
        void ignoreLocalRecord() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.records.MyLocalNestingRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.records.MyLocalNestingRecord"));
        }

        @Test
        void findFieldsOfRecord() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.records.MySimpleRecord");

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
        void findMethodsOfRecord() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.records.MySimpleRecord");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val methods = components.programmingElements(JavaMethod.class);
            assertThat(methods)
                    .hasSize(7)
                    .anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("<init>"),
                                    () -> assertThat(method.id().index()).isEqualTo(0)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("toString"),
                                    () -> assertThat(method.id().index()).isEqualTo(1)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("hashCode"),
                                    () -> assertThat(method.id().index()).isEqualTo(2)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("equals"),
                                    () -> assertThat(method.id().index()).isEqualTo(3)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("myEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(4)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("myString"),
                                    () -> assertThat(method.id().index()).isEqualTo(5)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("myInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(6)
                            )
                    );
        }
    }
}