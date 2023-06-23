package de.cfranzen.archsonar.components.java;


import de.cfranzen.archsonar.components.Namespace;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@EqualsAndHashCode(of = "fullPackageName")
@ToString(of = "fullPackageName")
public class JavaPackage implements Namespace {

    public static final JavaPackage DEFAULT = new JavaPackage();

    public static final String DELIMITER = ".";

    private static final Pattern DELIMITER_PATTERN = Pattern.compile(Pattern.quote(DELIMITER));

    private final String fullPackageName;

    private final List<String> segments;

    public JavaPackage(final String fullPackageName) {
        this.fullPackageName = Objects.requireNonNull(fullPackageName, "Full-qualified package name may not be null");
        this.segments = Collections.unmodifiableList(Arrays.asList(DELIMITER_PATTERN.split(fullPackageName)));
    }

    private JavaPackage() {
        this.fullPackageName = "<<default>>";
        this.segments = Collections.emptyList();
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
