package de.cfranzen.archsonar.components;

public enum RelationType {
    /**
     * Start of the relation uses the end of the relation
     */
    USES,

    /**
     * Start of the relation exposes the end of the relation in its public API
     */
    EXPOSES,

    /**
     * Start of the relation inherits the end of the relation
     */
    INHERITS
}
