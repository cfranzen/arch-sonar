package de.cfranzen.archsonar.resources.detector;

import de.cfranzen.archsonar.resources.Module;
import de.cfranzen.archsonar.resources.*;

import java.util.Set;

public interface DetectedComponents {

    Set<SoftwareSystem> softwareSystems();

    Set<SoftwareSubSystem> softwareSubSystems();

    Set<Module> modules();

    Set<SourcesRoot> sourcesRoots();

    Set<SourceFile> sourceFiles();

    Set<ProgrammingElement> programmingElements();
}
