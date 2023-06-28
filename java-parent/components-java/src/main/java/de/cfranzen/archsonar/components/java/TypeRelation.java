package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ElementRelation;
import de.cfranzen.archsonar.components.RelationType;

public record TypeRelation(
        TypeIdentifier from,
        TypeReference to,
        RelationType type

) implements ElementRelation {
}
