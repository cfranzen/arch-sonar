package de.cfranzen.archsonar.components.java.detector;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import de.cfranzen.archsonar.components.ProgrammingElement;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import de.cfranzen.archsonar.resources.Resource;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner14;
import javax.lang.model.util.Elements;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sun.source.tree.Tree.Kind.ENUM;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;

@Log4j2
class JavaSourceAnalyzer {

    private static final ElementScanner SCANNER = new ElementScanner();

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    Optional<JavaSourceFile> analyze(Resource resource) {
        try {
            val partialContext = parse(resource);
            val rootType = partialContext.rootType;
            val javaPackage = createJavaPackageFromType(rootType);
            val sourceFile = new JavaSourceFile(resource, javaPackage);
            val fullContext = new ParsingContext(rootType, sourceFile, partialContext.elementUtils);
            SCANNER.scan(rootType, fullContext);
            return Optional.of(sourceFile);
        } catch (IOException e) {
            log.error("Unable to parse Java source file {}", resource, e);
            return Optional.empty();
        }
    }

    private ParsingContext parse(final Resource resource) throws IOException {
        val javaFileObject = new SourceResourceJavaFileObject(resource);
        val task = (JavacTask) compiler.getTask(null, null, null, null, null, List.of(javaFileObject));
        val elementUtils = task.getElements();
        val rootElement = task.analyze().iterator().next();
        if (rootElement instanceof TypeElement te) {
            return new ParsingContext(te, null, elementUtils);
        } else {
            throw new IllegalArgumentException(STR."Could not parse class from resource \{resource.uri()}");
        }
    }

    private static class ElementScanner extends ElementScanner14<Void, ParsingContext> {

        @Override
        public Void visitType(final TypeElement e, final ParsingContext context) {
            createTypeIdentifier(e).ifPresent(id -> {
                val javaType = new JavaType(id);
                context.addProgrammingElement(javaType);
            });
            return super.visitType(e, context);
        }

        @Override
        public Void visitExecutable(final ExecutableElement methodElement, final ParsingContext context) {
            val parent = methodElement.getEnclosingElement();
            if (parent instanceof TypeElement pt && isRegularMethod(methodElement, context)) {
                val allMethods = pt.getEnclosedElements().stream().filter(ExecutableElement.class::isInstance).toList();
                val index = allMethods.indexOf(methodElement);
                if (index < 0) {
                    throw new IllegalStateException("Could not find method in class");
                }
                createTypeIdentifier(parent).flatMap(context::findType).ifPresent(type -> {
                    val method = type.addMethod(methodElement.getSimpleName().toString(), index);
                    context.addProgrammingElement(method);
                });
            }
            return super.visitExecutable(methodElement, context);
        }

        private static boolean isRegularMethod(final ExecutableElement methodElement, final ParsingContext context) {
            return context.elementUtils.getOrigin(methodElement).isDeclared();
        }

        @Override
        public Void visitVariable(final VariableElement varElement, final ParsingContext context) {
            val parent = varElement.getEnclosingElement();
            if (parent instanceof TypeElement pt && (varElement.getKind() == LOCAL_VARIABLE || varElement.getKind() == FIELD)) {
                createTypeIdentifier(pt).flatMap(context::findType).ifPresent(type -> {
                    val field = type.addField(varElement.getSimpleName().toString());
                    context.addProgrammingElement(field);
                });
            }
            return super.visitVariable(varElement, context);
        }

        private static boolean isEnumValue(final VariableTree node, final ClassTree parentClass) {
            return parentClass.getKind() == ENUM && node.getModifiers().getFlags().containsAll(List.of(PUBLIC, STATIC, FINAL)) && node.getType() instanceof IdentifierTree varTypeIdentifier && varTypeIdentifier.getName().equals(parentClass.getSimpleName());
        }
    }

    private static JavaPackage createJavaPackageFromType(final TypeElement element) {
        val packageElement = element.getEnclosingElement();
        if (packageElement instanceof PackageElement pe) {
            return createJavaPackage(pe);
        } else {
            throw new IllegalArgumentException(STR."Expecting parent element of type \{element.getQualifiedName().toString()} to be a PackageElement");
        }
    }

    private static JavaPackage createJavaPackage(final PackageElement pe) {
        if (pe.isUnnamed()) {
            return JavaPackage.DEFAULT;
        } else {
            return new JavaPackage(pe.getQualifiedName().toString());
        }
    }

    /**
     * Creates a TypeIdentifier from a Element. For local or anonymous types,
     * this method returns an empty optional
     */
    private static Optional<TypeIdentifier> createTypeIdentifier(final Element element) {
        val names = new ArrayList<String>();
        var current = element;
        for (; current.getKind() != PACKAGE; current = current.getEnclosingElement()) {
            if (current instanceof TypeElement pe) {
                if (pe.getNestingKind() == NestingKind.ANONYMOUS || pe.getNestingKind() == NestingKind.LOCAL) {
                    return Optional.empty();
                }
                names.add(current.getSimpleName().toString());
            }
        }
        val typeName = names.stream().sorted(reverseOrder()).collect(joining(TypeIdentifier.BINARY_TYPE_SEPARATOR));
        val javaPackage = createJavaPackage((PackageElement) current);
        return Optional.of(new TypeIdentifier(javaPackage, typeName));
    }

    private record ParsingContext(
            TypeElement rootType,
            JavaSourceFile sourceFile,
            Elements elementUtils
    ) {
        public Optional<JavaType> findType(final TypeIdentifier identifier) {
            return sourceFile.findType(identifier);
        }

        public void addProgrammingElement(final ProgrammingElement element) {
            sourceFile.addProgrammingElement(element);
        }

    }
}

