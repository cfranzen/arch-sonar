package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaBytecodeFile;
import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromClassfile;
import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
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

    @Test
    void detectResourceAsJavaClassFile() {
        // Given
        val resource = createResourceFromClassfile("de.dummy.project.enums.MySimpleEnum");

        // When
        val components = sut.detect(MockResourcesCollection.of(resource));

        // Then
        val sourceFiles = components.sourceFiles();
        assertThat(sourceFiles).hasSize(1).allSatisfy(file -> {
            assertThat(file.resource()).isEqualTo(resource);
            assertThat(file).isInstanceOf(JavaBytecodeFile.class);
        });
    }
}