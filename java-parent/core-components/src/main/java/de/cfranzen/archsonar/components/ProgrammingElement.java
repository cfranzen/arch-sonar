package de.cfranzen.archsonar.components;

import java.util.Set;

public interface ProgrammingElement {

    Set<ProgrammingElement> childElements();
}
