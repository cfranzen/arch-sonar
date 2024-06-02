package de.cfranzen.archsonar.components.java.detector.source;

import de.cfranzen.archsonar.components.ElementRelation;
import de.cfranzen.archsonar.components.ProgrammingElement;
import de.cfranzen.archsonar.components.java.*;

import java.util.Optional;

public record ParsingContext(
        JavaSourceFile sourceFile,

        TypeReferenceResolver resolver
) {
    public JavaPackage javaPackage() {
        return sourceFile.javaPackage();
    }

    public void addProgrammingElement(final ProgrammingElement element) {
        sourceFile.addProgrammingElement(element);
    }

    public void addElementRelation(final ElementRelation elementRelation) {
        sourceFile.addElementRelation(elementRelation);
    }

    public Optional<JavaType> findType(final TypeIdentifier identifier) {
        return sourceFile.findType(identifier);
    }

    public TypeReference resolveTypeReference(final String identifierName) {
        return resolver.resolve(identifierName);
    }
}
