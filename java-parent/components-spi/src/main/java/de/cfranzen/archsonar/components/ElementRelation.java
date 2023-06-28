package de.cfranzen.archsonar.components;


public interface ElementRelation {

    ElementIdentifier from();

    ElementIdentifier to();

    RelationType type();
}
