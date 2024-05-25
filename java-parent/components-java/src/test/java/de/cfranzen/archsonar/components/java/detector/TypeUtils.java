package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import de.cfranzen.archsonar.components.java.TypeReference;

final class TypeUtils {

    private TypeUtils() {
        // Do not instantiate
    }

    public static TypeReference reference(final String packageName, final String typeName) {
        return new TypeReference(STR."\{packageName}.\{typeName}");
    }

    public static TypeIdentifier identifier(final String packageName, final String typeName) {
        return new TypeIdentifier(new JavaPackage(packageName), typeName);
    }
}
