package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.SourceUtils.createResourceFromSource;
import static org.assertj.core.api.Assertions.assertThat;

class JavaComponentDetectorRecordsTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectRecordInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.records.MySimpleRecord");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val elements = components.programmingElements();
        assertThat(elements).hasSize(1).allSatisfy(element ->
                assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.records.MySimpleRecord")));
    }

    @Test
    void detectNestedRecordInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.records.MyNestingRecord");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val elements = components.programmingElements();
        assertThat(elements)
                .hasSize(3)
                .anySatisfy(element ->
                        assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                assertThat(type.id().fullyQualifiedName())
                                        .isEqualTo("de.dummy.project.records.MyNestingRecord")))
                .anySatisfy(element ->
                        assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                assertThat(type.id().fullyQualifiedName())
                                        .isEqualTo("de.dummy.project.records.MyNestingRecord.MyNestedRecord")))
                .anySatisfy(element ->
                        assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                assertThat(type.id().fullyQualifiedName())
                                        .isEqualTo("de.dummy.project.records.MyNestingRecord.MyNestedRecord.MyDoubleNestedRecord")));
    }

    @Test
    void ignoreLocalRecordInJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.records.MyLocalNestingRecord");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val elements = components.programmingElements();
        assertThat(elements).hasSize(1).allSatisfy(element ->
                assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                        assertThat(type.id().fullyQualifiedName())
                                .isEqualTo("de.dummy.project.records.MyLocalNestingRecord")));
    }
}