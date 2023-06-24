package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ProgrammingElement;
import de.cfranzen.archsonar.components.SourceFile;
import de.cfranzen.archsonar.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

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

    public JavaPackage javaPackage() {
        return javaPackage;
    }

    public void addProgrammingElement(final ProgrammingElement element) {
        elements.add(element);
    }
}
