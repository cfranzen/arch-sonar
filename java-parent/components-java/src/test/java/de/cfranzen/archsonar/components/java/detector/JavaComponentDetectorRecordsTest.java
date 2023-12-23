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

class JavaComponentDetectorRecordsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectRecordInJavaSourceFile() {
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
    void detectNestedRecordInJavaSourceFile() {
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
    void ignoreLocalRecordInJavaSourceFile() {
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
    void findFieldsOfEnum() {
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
    void findMethodsOfEnum() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.records.MySimpleRecord");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val methods = components.programmingElements(JavaMethod.class);
        assertThat(methods)
                .hasSize(3)
                .anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("myInt"),
                                () -> assertThat(method.id().index()).isEqualTo(0)
                        )
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("myEnum"),
                                () -> assertThat(method.id().index()).isEqualTo(1)
                        )
                ).anySatisfy(method -> assertAll(
                                () -> assertThat(method.id().name()).isEqualTo("myString"),
                                () -> assertThat(method.id().index()).isEqualTo(2)
                        )
                );
    }
}