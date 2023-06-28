package de.cfranzen.archsonar.components;

public interface ElementIdentifier {

    /**
     * Name that uniquely identifies a ProgrammingElement. Two ElementIdentifiers reference exactly the same
     * ProgrammingElement if their names are equal.
     * <br><br>
     * Examples:
     * <ul>
     *     <li>Java: java.util.List</li>
     * </ul>
     */
    String name();
}
