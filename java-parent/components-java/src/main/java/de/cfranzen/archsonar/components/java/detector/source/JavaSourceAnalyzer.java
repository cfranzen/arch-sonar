package de.cfranzen.archsonar.components.java.detector.source;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.resources.Resource;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class JavaSourceAnalyzer {

    private static final TypesVisitor TYPES_VISITOR = new TypesVisitor();
    private static final MethodsVisitor METHODS_VISITOR = new MethodsVisitor();
    private static final FieldsVisitor FIELDS_VISITOR = new FieldsVisitor();

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    public Optional<JavaSourceFile> analyze(Resource resource) {
        try {
            val compilationUnit = parse(resource);
            val imports = getImports(compilationUnit);

            val javaPackage = createJavaPackageFromCompilationUnit(compilationUnit);
            val sourceFile = new JavaSourceFile(resource, javaPackage);
            val resolver = new TypeReferenceResolver(javaPackage, imports);
            val context = new ParsingContext(sourceFile, resolver);

            TYPES_VISITOR.scan(compilationUnit, context);
            METHODS_VISITOR.scan(compilationUnit, context);
            FIELDS_VISITOR.scan(compilationUnit, context);
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

    private List<String> getImports(final CompilationUnitTree compilationUnit) {
        return compilationUnit.getImports().stream()
                .map(importTree -> importTree.getQualifiedIdentifier().toString())
                .toList();
    }
}

