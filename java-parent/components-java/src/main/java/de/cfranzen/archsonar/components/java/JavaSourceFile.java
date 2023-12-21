package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ProgrammingElement;
import de.cfranzen.archsonar.components.SourceFile;
import de.cfranzen.archsonar.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "resource")
@ToString(of = "resource")
public class JavaSourceFile implements SourceFile {

    private final Set<ProgrammingElement> elements = new LinkedHashSet<>();

    private final Resource resource;

    private final JavaPackage javaPackage;

    @Override
    public Resource resource() {
        return resource;
    }

    @Override
    public Set<ProgrammingElement> elements() {
        return elements;
    }

    public <T extends ProgrammingElement> Set<T> elements(Class<T> elementType) {
        return elements.stream()
                .filter(elementType::isInstance)
                .map(elementType::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    public JavaPackage javaPackage() {
        return javaPackage;
    }

    public void addProgrammingElement(final ProgrammingElement element) {
        elements.add(element);
    }

    public Optional<JavaType> findType(final TypeIdentifier identifier) {
        return elements.stream()
                .filter(e -> e instanceof JavaType)
                .map(e -> (JavaType) e)
                .filter(t -> t.id().equals(identifier))
                .findFirst();
    }
}
