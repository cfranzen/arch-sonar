package de.cfranzen.archsonar.components.java.detector;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.JavaSourceFile;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import de.cfranzen.archsonar.resources.Resource;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.Optional;

@Log4j2
class JavaSourceAnalyzer {

    Optional<JavaSourceFile> analyze(Resource resource) {
        val sourceFile = new JavaSourceFile(resource);

        try {
            val compilationUnit = StaticJavaParser.parse(resource.openInputStream());
            for (val type : compilationUnit.getTypes()) {
                analyzeType(sourceFile, type);
            }
            return Optional.of(sourceFile);

        } catch (ParseProblemException e) {
            log.error("Unable to parse Java source file {}", sourceFile, e);
            return Optional.empty();
        }
    }

    private void analyzeType(final JavaSourceFile sourceFile, final TypeDeclaration<?> type) {
        if (type.isEnumDeclaration()) {
            analyzeEnum(sourceFile, type.asEnumDeclaration());
        } else if (type.isAnnotationDeclaration()) {
            analyzeAnnotation(sourceFile, type.asAnnotationDeclaration());
        } else if (type.isClassOrInterfaceDeclaration()) {
            analyzeCLassOrInterface(sourceFile, type.asClassOrInterfaceDeclaration());
        } else if (type.isRecordDeclaration()) {
            analyzeRecord(sourceFile, type.asRecordDeclaration());
        } else {
            log.warn("Found unsupported type declaration {} in source file {}", type, sourceFile);
        }

        type.getMembers().forEach(member -> {
            if (member.isTypeDeclaration()) {
                analyzeType(sourceFile, member.asTypeDeclaration());
            }
        });
    }

    private void analyzeEnum(final JavaSourceFile sourceFile, final EnumDeclaration declaration) {
        final Optional<TypeIdentifier> id = createIdFromTypeDeclaration(declaration);
        if (id.isEmpty()) {
            // TODO Handle enum declarations without identifier, e.g. local enum decleration
        } else {
            val javaType = new JavaType(id.get());
            sourceFile.addProgrammingElement(javaType);
        }
    }


    private void analyzeAnnotation(final JavaSourceFile sourceFile, final AnnotationDeclaration declaration) {
    }

    private void analyzeCLassOrInterface(final JavaSourceFile sourceFile, final ClassOrInterfaceDeclaration declaration) {
    }

    private void analyzeRecord(final JavaSourceFile sourceFile, final RecordDeclaration declaration) {

    }

    private static Optional<TypeIdentifier> createIdFromTypeDeclaration(final TypeDeclaration<?> type) {
        val compilationUnit = type.findCompilationUnit().orElse(null);
        val fqn = type.getFullyQualifiedName().orElse(null);
        if (compilationUnit == null || fqn == null) {
            return Optional.empty();
        }
        return compilationUnit
                .getPackageDeclaration()
                .map(NodeWithName::getNameAsString)
                .map(packageName -> {
                    val typeName = fqn.substring(packageName.length() + 1).replace('.', '$');
                    return new TypeIdentifier(new JavaPackage(packageName), typeName);
                })
                .or(() ->
                        Optional.of(new TypeIdentifier(JavaPackage.DEFAULT, fqn.replace('.', '$')))
                );
    }
}
