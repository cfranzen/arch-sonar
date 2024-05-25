package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ElementRelation;
import de.cfranzen.archsonar.components.ProgrammingElement;
import de.cfranzen.archsonar.components.SourceFile;
import de.cfranzen.archsonar.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "resource")
@ToString(of = "resource")
public class JavaBytecodeFile implements SourceFile {

    private final Set<ProgrammingElement> elements = new LinkedHashSet<>();

    private final Set<ElementRelation> relations = new LinkedHashSet<>();

    private final Resource resource;

    private final JavaPackage javaPackage;

    @Override
    public Resource resource() {
        return resource;
    }

    @Override
    public Set<ProgrammingElement> elements() {
        return Collections.unmodifiableSet(elements);
    }

    @Override
    public Set<ElementRelation> relations() {
        return Collections.unmodifiableSet(relations);
    }

    public JavaPackage javaPackage() {
        return javaPackage;
    }

    public void addProgrammingElement(final ProgrammingElement element) {
        elements.add(element);
    }

    public void addElementRelation(final ElementRelation elementRelation) {
        relations.add(elementRelation);
    }

    public Optional<JavaType> findType(final TypeIdentifier identifier) {
        return elements.stream()
                .filter(JavaType.class::isInstance)
                .map(e -> (JavaType) e)
                .filter(t -> t.id().equals(identifier))
                .findFirst();
    }
}
