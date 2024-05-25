package de.cfranzen.archsonar.components;

import de.cfranzen.archsonar.resources.Resource;

import java.util.Set;

public interface SourceFile {

    Resource resource();

    Set<ProgrammingElement> elements();

    Set<ElementRelation> relations();
}
