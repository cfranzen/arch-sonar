package de.cfranzen.archsonar.components.java;

import de.cfranzen.archsonar.components.ProgrammingElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class JavaField implements ProgrammingElement {

    private final JavaType type;

    private final FieldIdentifier id;

    public JavaType type() {
        return type;
    }

    @Override
    public FieldIdentifier id() {
        return id;
    }
}
