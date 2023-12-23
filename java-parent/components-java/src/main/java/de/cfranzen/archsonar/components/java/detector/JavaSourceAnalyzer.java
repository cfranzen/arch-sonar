package de.cfranzen.archsonar.components.java.detector;

import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import de.cfranzen.archsonar.resources.Resource;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sun.source.tree.Tree.Kind.*;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.element.Modifier.*;

@Log4j2
class JavaSourceAnalyzer {

    private static final TypesVisitor TYPES_VISITOR = new TypesVisitor();
    private static final MethodsFieldsVisitor METHODS_FIELDS_VISITOR = new MethodsFieldsVisitor();

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    Optional<JavaSourceFile> analyze(Resource resource) {
        try {
            val compilationUnit = parse(resource);
            val javaPackage = createJavaPackageFromCompilationUnit(compilationUnit);
            val sourceFile = new JavaSourceFile(resource, javaPackage);
            TYPES_VISITOR.scan(compilationUnit, sourceFile);
            METHODS_FIELDS_VISITOR.scan(compilationUnit, sourceFile);
            return Optional.of(sourceFile);
        } catch (IOException e) {
            log.error("Unable to parse Java source file {}", resource, e);
            return Optional.empty();
        }
    }

    private CompilationUnitTree parse(final Resource resource) throws IOException {
        val javaFileObject = new SourceResourceJavaFileObject(resource);
        val task = (JavacTask) compiler.getTask(null, null, null, null, null, List.of(javaFileObject));
        return task.parse().iterator().next();
    }

    private static JavaPackage createJavaPackageFromCompilationUnit(final CompilationUnitTree compilationUnit) {
        val packageNameExpr = compilationUnit.getPackageName();
        if (packageNameExpr == null) {
            return JavaPackage.DEFAULT;
        }
        return new JavaPackage(packageNameExpr.toString());
    }

    private static class TypesVisitor extends TreePathScanner<Void, JavaSourceFile> {

        @Override
        public Void visitClass(final ClassTree node, final JavaSourceFile sourceFile) {
            createTypeIdentifier(sourceFile.javaPackage(), getCurrentPath())
                    .ifPresent(id -> {
                        val javaType = new JavaType(id);
                        sourceFile.addProgrammingElement(javaType);
                    });
            return super.visitClass(node, sourceFile);
        }
    }

    private static class MethodsFieldsVisitor extends TreePathScanner<Void, JavaSourceFile> {

        @Override
        public Void visitMethod(final MethodTree node, final JavaSourceFile sourceFile) {
            val parentPath = getCurrentPath().getParentPath();
            val parent = parentPath.getLeaf();
            if (parent instanceof ClassTree clazz) {
                val allMethods = clazz.getMembers().stream().filter(MethodTree.class::isInstance).toList();
                val index = allMethods.indexOf(node);
                if (index < 0) {
                    throw new IllegalStateException("Could not find method in class");
                }
                createTypeIdentifier(sourceFile.javaPackage(), parentPath)
                        .flatMap(sourceFile::findType).ifPresent(type -> {
                            val method = type.addMethod(node.getName().toString(), index);
                            sourceFile.addProgrammingElement(method);
                        });
            }
            return super.visitMethod(node, sourceFile);
        }

        @Override
        public Void visitVariable(final VariableTree node, final JavaSourceFile sourceFile) {
            val parentPath = getCurrentPath().getParentPath();
            val parent = parentPath.getLeaf();
            if (parent instanceof ClassTree clazz && !isEnumValue(node, clazz)) {
                createTypeIdentifier(sourceFile.javaPackage(), parentPath)
                        .flatMap(sourceFile::findType).ifPresent(type -> {
                            val field = type.addField(node.getName().toString());
                            sourceFile.addProgrammingElement(field);
                        });
            }
            return super.visitVariable(node, sourceFile);
        }

        private static boolean isEnumValue(final VariableTree node, final ClassTree parentClass) {
            return parentClass.getKind() == ENUM &&
                    node.getModifiers().getFlags().containsAll(List.of(PUBLIC, STATIC, FINAL)) &&
                    node.getType() instanceof IdentifierTree varTypeIdentifier &&
                    varTypeIdentifier.getName().equals(parentClass.getSimpleName());
        }
    }

    /**
     * Creates a TypeIdentifier from a TreePath. For local or anonymous types,
     * this method returns an empty optional
     */
    private static Optional<TypeIdentifier> createTypeIdentifier(final JavaPackage javaPackage, final TreePath path) {
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

