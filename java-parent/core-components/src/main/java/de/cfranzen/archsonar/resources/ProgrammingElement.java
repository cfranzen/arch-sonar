package de.cfranzen.archsonar.resources;

import java.util.Set;

public interface ProgrammingElement {

    Set<ProgrammingElement> childElements();
}
