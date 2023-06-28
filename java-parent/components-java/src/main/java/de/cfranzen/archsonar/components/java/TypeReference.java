package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ElementIdentifier;

public record TypeReference(String fullyQualifiedName) implements ElementIdentifier {

    @Override
    public String name() {
        return fullyQualifiedName.replace(TypeIdentifier.BINARY_TYPE_SEPARATOR, TypeIdentifier.SOURCE_TYPE_SEPARATOR);
    }
}
