package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.resources.test.MockResource;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;

final class ResourceUtils {

    private ResourceUtils() {
        // Do not instantiate
    }

    static MockResource createResourceFromSource(final String sourceFQN) {
        val sourceFile = Path.of("src/test/resources/javasources/" + sourceFQN.replace('.', '/') + ".java");
        if (!Files.exists(sourceFile)) {
            throw new IllegalArgumentException("Could not find file for " + sourceFQN + " at " + sourceFile);
        }
        return MockResource.fromFile(sourceFile, JavaComponentDetector.SOURCE_TYPES.stream().findAny().orElseThrow());
    }

    static MockResource createResourceFromClassfile(final String classFQN) {
        val classFile = Path.of("src/test/resources/javaclasses/" + classFQN.replace('.', '/') + ".class");
        if (!Files.exists(classFile)) {
            throw new IllegalArgumentException("Could not find file for " + classFQN + " at " + classFile);
        }
        return MockResource.fromFile(classFile, JavaComponentDetector.BYTECODE_TYPES.stream().findAny().orElseThrow());
    }
}
