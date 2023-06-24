package de.cfranzen.archsonar.components.java.detector;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import de.cfranzen.archsonar.resources.Resource;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
class JavaSourceAnalyzer {

    private static final TypesVisitor TYPES_VISITOR = new TypesVisitor();

    Optional<JavaSourceFile> analyze(Resource resource) {
        try {
            val compilationUnit = StaticJavaParser.parse(resource.openInputStream());
            val javaPackage = createJavaPackageFromCompilationUnit(compilationUnit);
            val sourceFile = new JavaSourceFile(resource, javaPackage);
            compilationUnit.accept(TYPES_VISITOR, sourceFile);
            return Optional.of(sourceFile);
        } catch (ParseProblemException e) {
            log.error("Unable to parse Java source file {}", resource, e);
            return Optional.empty();
        }
    }

    private static class TypesVisitor extends VoidVisitorAdapter<JavaSourceFile> {

        @Override
        public void visit(final EnumDeclaration declaration, final JavaSourceFile sourceFile) {
            super.visit(declaration, sourceFile);

            final Optional<TypeIdentifier> id = createTypeIdentifier(sourceFile.javaPackage(), declaration);
            if (id.isEmpty()) {
                // TODO Handle enum declarations without identifier, e.g. local enum decleration
            } else {
                val javaType = new JavaType(id.get());
                sourceFile.addProgrammingElement(javaType);
            }
        }

        @Override
        public void visit(final AnnotationDeclaration declaration, final JavaSourceFile sourceFile) {
            super.visit(declaration, sourceFile);
        }

        @Override
        public void visit(final ClassOrInterfaceDeclaration declaration, final JavaSourceFile sourceFile) {
            super.visit(declaration, sourceFile);
        }

        @Override
        public void visit(final RecordDeclaration declaration, final JavaSourceFile sourceFile) {
            super.visit(declaration, sourceFile);
        }
    }

    private static Optional<TypeIdentifier> createTypeIdentifier(final JavaPackage javaPackage, final TypeDeclaration<?> type) {
        String typeName;
        if (type.isTopLevelType()) {
            typeName = type.getNameAsString();
        } else {
            val names = new ArrayList<String>();
            var current = type;
            while (!current.isTopLevelType()) {
                names.add(current.getNameAsString());
                val parent = current.findAncestor(TypeDeclaration.class);
                if (parent.isEmpty()) {
                    return Optional.empty();
                }
                current = parent.get();
            }
            names.add(current.getNameAsString());
            typeName = names.stream().sorted(Comparator.reverseOrder()).collect(Collectors.joining("$"));
        }
        return Optional.of(new TypeIdentifier(javaPackage, typeName));
    }

    private static JavaPackage createJavaPackageFromCompilationUnit(final CompilationUnit compilationUnit) {
        return compilationUnit
                .getPackageDeclaration()
                .map(NodeWithName::getNameAsString)
                .map(JavaPackage::new)
                .or(() -> Optional.of(JavaPackage.DEFAULT))
                .get();
    }
}
