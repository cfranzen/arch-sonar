package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ElementIdentifier;
import de.cfranzen.archsonar.components.HasNamespace;
import de.cfranzen.archsonar.components.Namespace;

public record MethodIdentifier(TypeIdentifier type, String name, int index) implements ElementIdentifier, HasNamespace {

    @Override
    public Namespace namespace() {
        return type.javaPackage();
    }

    public String typeName() {
        return type.name();
    }
}
