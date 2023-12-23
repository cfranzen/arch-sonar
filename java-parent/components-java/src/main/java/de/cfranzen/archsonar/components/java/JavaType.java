package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.HasChildElements;
import de.cfranzen.archsonar.components.HasNamespace;
import de.cfranzen.archsonar.components.Namespace;
import de.cfranzen.archsonar.components.ProgrammingElement;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.val;

import java.util.Collections;
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

    public Set<JavaField> fields() {
        return Collections.unmodifiableSet(fields);
    }

    public Set<JavaMethod> methods() {
        return Collections.unmodifiableSet(methods);
    }

    public JavaField addField(String name) {
        val field = new JavaField(this, new FieldIdentifier(id, name));
        fields.add(field);
        return field;
    }

    public JavaMethod addMethod(String name, int index) {
        val method = new JavaMethod(this, new MethodIdentifier(id, name, index));
        methods.add(method);
        return method;
    }
}
