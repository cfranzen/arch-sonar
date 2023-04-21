package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.Module;
import de.cfranzen.archsonar.components.*;
import de.cfranzen.archsonar.components.detector.DetectedComponents;

import java.util.Set;

public class DetectedJavaComponents implements DetectedComponents {

    DetectedJavaComponents() {

    }

    @Override
    public Set<SoftwareSystem> softwareSystems() {
        return null;
    }

    @Override
    public Set<SoftwareSubSystem> softwareSubSystems() {
        return null;
    }

    @Override
    public Set<Module> modules() {
        return null;
    }

    @Override
    public Set<SourcesRoot> sourcesRoots() {
        return null;
    }

    @Override
    public Set<SourceFile> sourceFiles() {
        return null;
    }

    @Override
    public Set<ProgrammingElement> programmingElements() {
        return null;
    }
}
