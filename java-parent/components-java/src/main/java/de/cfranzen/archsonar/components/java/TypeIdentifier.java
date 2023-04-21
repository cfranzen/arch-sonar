package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ElementIdentifier;
import de.cfranzen.archsonar.components.HasNamespace;
import de.cfranzen.archsonar.components.Namespace;

public record TypeIdentifier(JavaPackage javaPackage, String name) implements ElementIdentifier, HasNamespace {
    @Override
    public Namespace namespace() {
        return javaPackage();
    }
}
