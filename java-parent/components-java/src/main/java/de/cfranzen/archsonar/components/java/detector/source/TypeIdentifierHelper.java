package de.cfranzen.archsonar.components.java.detector.source;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import lombok.val;

import java.util.ArrayList;
import java.util.Optional;

import static com.sun.source.tree.Tree.Kind.*;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.joining;

class TypeIdentifierHelper {

    private TypeIdentifierHelper() {
        // Do not instantiate
    }

    /**
     * Creates a TypeIdentifier from a TreePath. For local or anonymous types,
     * this method returns an empty optional
     */
    static Optional<TypeIdentifier> createTypeIdentifier(final JavaPackage javaPackage, final TreePath path) {
        val names = new ArrayList<String>();
        for (final Tree current : path) {
            if (current.getKind() == METHOD) {
                return Optional.empty();
            } else if (current.getKind() == ENUM || current.getKind() == CLASS || current.getKind() == INTERFACE ||
                    current.getKind() == RECORD || current.getKind() == ANNOTATION_TYPE) {
                names.add(((ClassTree) current).getSimpleName().toString());
            }
        }
        val typeName = names.stream().sorted(reverseOrder()).collect(joining(TypeIdentifier.BINARY_TYPE_SEPARATOR));
        return Optional.of(new TypeIdentifier(javaPackage, typeName));
    }
}
