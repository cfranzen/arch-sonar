package de.cfranzen.archsonar.components.java;


import de.cfranzen.archsonar.components.Namespace;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@EqualsAndHashCode(of = "fullPackageName")
@ToString(of = "fullPackageName")
public class JavaPackage implements Namespace {

    private static final Pattern DELIMITER = Pattern.compile(Pattern.quote("."));

    private final String fullPackageName;

    private final List<String> segments;

    public JavaPackage(final String fullPackageName) {
        this.fullPackageName = fullPackageName;
        this.segments = Collections.unmodifiableList(Arrays.asList(DELIMITER.split(fullPackageName)));
    }

    @Override
    public String name() {
        return fullPackageName;
    }

    @Override
    public List<String> nameSegments() {
        return segments;
    }
}
