package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ElementIdentifier;
import de.cfranzen.archsonar.components.HasNamespace;
import de.cfranzen.archsonar.components.Namespace;

public record TypeIdentifier(JavaPackage javaPackage, String typeName) implements ElementIdentifier, HasNamespace {

    public static final String BINARY_TYPE_SEPARATOR = "$";

    public static final String SOURCE_TYPE_SEPARATOR = ".";

    @Override
    public Namespace namespace() {
        return javaPackage();
    }

    public String fullyQualifiedName() {
        return STR."\{javaPackage().name()}.\{typeName.replace(BINARY_TYPE_SEPARATOR, SOURCE_TYPE_SEPARATOR)}";
    }

    @Override
    public String name() {
        return fullyQualifiedName();
    }
}
