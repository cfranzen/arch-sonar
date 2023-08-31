package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResource;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class JavaComponentDetectorTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Test
    void detectResourceAsJavaSourceFile() {
        // Given
        val resource = createResourceFromSource("de.dummy.project.enums.MySimpleEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val sourceFiles = components.sourceFiles();
        assertThat(sourceFiles).hasSize(1).allSatisfy(file -> {
            assertThat(file.resource()).isEqualTo(resource);
            assertThat(file).isInstanceOf(JavaSourceFile.class);
        });
    }

    @Nested
    class DetectEnums {

        @Test
        void detectEnumInJavaSourceFile() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.enums.MySimpleEnum");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val elements = components.programmingElements();
            assertThat(elements).hasSize(1).allSatisfy(element ->
                    assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                            assertThat(type.id().fullyQualifiedName()).isEqualTo("de.dummy.project.enums.MySimpleEnum")));
        }

        @Test
        void detectNestedEnumInJavaSourceFile() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.enums.MyNestingEnum");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val elements = components.programmingElements();
            assertThat(elements)
                    .hasSize(3)
                    .anySatisfy(element ->
                            assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                    assertThat(type.id().fullyQualifiedName())
                                            .isEqualTo("de.dummy.project.enums.MyNestingEnum")))
                    .anySatisfy(element ->
                            assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                    assertThat(type.id().fullyQualifiedName())
                                            .isEqualTo("de.dummy.project.enums.MyNestingEnum.MyNestedEnum")))
                    .anySatisfy(element ->
                            assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                                    assertThat(type.id().fullyQualifiedName())
                                            .isEqualTo("de.dummy.project.enums.MyNestingEnum.MyNestedEnum.MyDoubleNestedEnum")));
        }

        @Test
        void ignoreLocalEnumInJavaSourceFile() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.enums.MyLocalNestingEnum");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val elements = components.programmingElements();
            assertThat(elements).hasSize(1).allSatisfy(element ->
                    assertThat(element).isInstanceOfSatisfying(JavaType.class, type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.enums.MyLocalNestingEnum")));
        }
    }

    @Nested
    class DetectClasses {

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
                            assertThat(type.id().fullyQualifiedName()).isEqualTo("de.dummy.project.classes.MySimpleClass")));
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

        private static MockResource createResourceFromSource(final String sourceFQN) {
        val sourceFile = Path.of("src/test/resources/javasources/" + sourceFQN.replace('.', '/') + ".java");
        if (!Files.exists(sourceFile)) {
            throw new IllegalArgumentException("Could not find file for " + sourceFQN + " at " + sourceFile);
        }
        return MockResource.fromFile(sourceFile, JavaComponentDetector.SOURCE_TYPES.stream().findAny().orElseThrow());
    }
}