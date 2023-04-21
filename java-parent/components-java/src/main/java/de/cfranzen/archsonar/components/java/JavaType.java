package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.HasChildElements;
import de.cfranzen.archsonar.components.HasNamespace;
import de.cfranzen.archsonar.components.Namespace;
import de.cfranzen.archsonar.components.ProgrammingElement;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class JavaType implements HasNamespace, ProgrammingElement, HasChildElements {

    private final Set<JavaMethod> methods = new LinkedHashSet<>();

    private final Set<JavaField> fields = new LinkedHashSet<>();

    private final TypeIdentifier id;

    @Override
    public Namespace namespace() {
        return javaPackage();
    }

    public JavaPackage javaPackage() {
        return id.javaPackage();
    }

    @Override
    public TypeIdentifier id() {
        return id;
    }

    @Override
    public Set<ProgrammingElement> childElements() {
        final Set<ProgrammingElement> union = new LinkedHashSet<>();
        union.addAll(fields);
        union.addAll(methods);
        return union;
    }

    public JavaType addField(String name) {
        fields.add(new JavaField(new FieldIdentifier(id, name)));
        return this;
    }

    public JavaType addMethod(String name, int index) {
        methods.add(new JavaMethod(new MethodIdentifier(id, name, index)));
        return this;
    }
}
