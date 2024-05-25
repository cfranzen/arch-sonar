package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.TypeReference;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

class TypeReferenceResolver {

    private static final String ADHOC_IMPORT_SUFFIX = ".*";
    private final List<String> fqnImports = new ArrayList<>();

    private final List<String> adhocImports = new ArrayList<>();

    private final JavaPackage javaPackage;

    TypeReferenceResolver(JavaPackage javaPackage, List<String> imports) {
        this.javaPackage = javaPackage;
        for (val imp : imports) {
            if (imp.endsWith(ADHOC_IMPORT_SUFFIX)) {
                adhocImports.add(imp.substring(0, imp.length() - ADHOC_IMPORT_SUFFIX.length()));
            } else {
                fqnImports.add(imp);
            }
        }
    }

    TypeReference resolve(final String typeIdentifier) {
        // Check if type identifier is full-qualified already
        if (typeIdentifier.contains(".")) {
            return new TypeReference(typeIdentifier);
        }

        val suffix = STR.".\{typeIdentifier}";
        for (val imp : fqnImports) {
            if (imp.endsWith(suffix)) {
                return new TypeReference(imp);
            }
        }

        if (adhocImports.isEmpty()) {
            return new TypeReference(javaPackage.name() + suffix);
        }

        // TODO: Support this case
        throw new IllegalStateException(STR."Could not uniquely resolve type identifier \{typeIdentifier}");
    }
}
