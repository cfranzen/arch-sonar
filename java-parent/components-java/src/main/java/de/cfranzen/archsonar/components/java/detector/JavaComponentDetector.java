package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.detector.ComponentDetector;
import de.cfranzen.archsonar.resources.ResourcesCollection;
import lombok.val;
import org.apache.tika.mime.MediaType;

import java.util.Set;

class JavaComponentDetector implements ComponentDetector {

    static final Set<MediaType> SOURCE_TYPES = MediaType.set("text/x-java-source", "text/x-java");
    static final Set<MediaType> BYTECODE_TYPES = MediaType.set("application/java-vm", "application/x-java-vm", "application/x-java");

    private final JavaSourceAnalyzer sourceAnalyzer = new JavaSourceAnalyzer();

    private final JavaBytecodeAnalyzer bytecodeAnalyzer = new JavaBytecodeAnalyzer();

    JavaComponentDetector() {

    }

    @Override
    public DetectedJavaComponents detect(final ResourcesCollection resources) {
        val components = new DetectedJavaComponents();
        resources.resources().forEach(resource -> {
            if (SOURCE_TYPES.contains(resource.type())) {
                sourceAnalyzer.analyze(resource).ifPresent(components::addSourceFile);
            } else if (BYTECODE_TYPES.contains(resource.type())) {
                bytecodeAnalyzer.analyze(resource).ifPresent(components::addSourceFile);
            }
        });
        return components;
    }
}
