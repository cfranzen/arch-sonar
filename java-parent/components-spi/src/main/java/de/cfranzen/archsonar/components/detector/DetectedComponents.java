package de.cfranzen.archsonar.components.detector;

import de.cfranzen.archsonar.components.Module;
import de.cfranzen.archsonar.components.*;

import java.util.Set;

public interface DetectedComponents {

    Set<SoftwareSystem> softwareSystems();

    Set<SoftwareSubSystem> softwareSubSystems();

    Set<Module> modules();

    Set<SourcesRoot> sourcesRoots();

    Set<SourceFile> sourceFiles();

    Set<ProgrammingElement> programmingElements();

    Set<ElementRelation> relations();
}
