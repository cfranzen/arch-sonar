package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.Module;
import de.cfranzen.archsonar.components.*;
import de.cfranzen.archsonar.components.detector.DetectedComponents;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

public class DetectedJavaComponents implements DetectedComponents {

    private final Set<SourceFile> sourceFiles = new LinkedHashSet<>();

    private final Set<ProgrammingElement> elements = new LinkedHashSet<>();

    DetectedJavaComponents() {

    }

    @Override
    public Set<SoftwareSystem> softwareSystems() {
        return emptySet();
    }

    @Override
    public Set<SoftwareSubSystem> softwareSubSystems() {
        return emptySet();
    }

    @Override
    public Set<Module> modules() {
        return emptySet();
    }

    @Override
    public Set<SourcesRoot> sourcesRoots() {
        return emptySet();
    }

    @Override
    public Set<SourceFile> sourceFiles() {
        return Collections.unmodifiableSet(sourceFiles);
    }

    @Override
    public Set<ProgrammingElement> programmingElements() {
        return Collections.unmodifiableSet(elements);
    }

    void addSourceFile(SourceFile sourceFile) {
        sourceFiles.add(sourceFile);
        elements.addAll(sourceFile.elements());
    }
}
