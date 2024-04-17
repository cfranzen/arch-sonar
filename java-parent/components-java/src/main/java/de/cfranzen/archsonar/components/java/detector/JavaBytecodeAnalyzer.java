package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaBytecodeFile;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.components.java.TypeIdentifier;
import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourceUtils;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.IOException;
import java.lang.classfile.*;
import java.lang.classfile.attribute.InnerClassesAttribute;
import java.lang.constant.ClassDesc;
import java.util.Optional;

@Log4j2
class JavaBytecodeAnalyzer {

    Optional<JavaBytecodeFile> analyze(Resource resource) {
        try {
            val classModel = parse(resource);
            val javaPackage = createJavaPackage(classModel.thisClass().asSymbol());
            val sourceFile = new JavaBytecodeFile(resource, javaPackage);
            scanClassModel(classModel, sourceFile);
            return Optional.of(sourceFile);
        } catch (IOException e) {
            log.error("Unable to parse Java class file {}", resource, e);
            return Optional.empty();
        }
    }

    private static void scanClassModel(final ClassModel cm, final JavaBytecodeFile sourceFile) {
        sourceFile.addProgrammingElement(createJavaType(cm.thisClass().asSymbol()));

        scanForInnerClasses(cm, sourceFile);
        scanForFieldsAndMethods(cm, sourceFile);
    }

    private static void scanForInnerClasses(final ClassModel cm, final JavaBytecodeFile sourceFile) {
        for (ClassElement ce : cm) {
            if (ce instanceof InnerClassesAttribute innerAttr) {
                for (val inner : innerAttr.classes()) {
                    if (inner.outerClass().isEmpty()) {
                        // ignore local inner classes
                        continue;
                    }
                    val javaType = createJavaType(inner.innerClass().asSymbol());
                    sourceFile.addProgrammingElement(javaType);
                }
            }
        }
    }

    private static void scanForFieldsAndMethods(final ClassModel cm, final JavaBytecodeFile sourceFile) {
        for (ClassElement ce : cm) {
            switch (ce) {
                case FieldModel fm -> fm.parent().ifPresent(parent -> {
                    val typeId = createTypeIdentifier(parent.thisClass().asSymbol());
                    sourceFile.findType(typeId).ifPresent(type -> {
                        val field = type.addField(fm.fieldName().stringValue());
                        sourceFile.addProgrammingElement(field);
                    });
                });
                case MethodModel mm -> {
                    val index = cm.methods().indexOf(mm);
                    if (index < 0) {
                        throw new IllegalStateException("Could not find method in class");
                    }
                    mm.parent().ifPresent(parent -> {
                        val typeId = createTypeIdentifier(parent.thisClass().asSymbol());
                        sourceFile.findType(typeId).ifPresent(type -> {
                            val method = type.addMethod(mm.methodName().stringValue(), index);
                            sourceFile.addProgrammingElement(method);
                        });
                    });
                }
                default -> {
                    // Ignore other elements
                }
            }
        }
    }


    private static ClassModel parse(final Resource resource) throws IOException {
        val bytes = ResourceUtils.readContent(resource);
        return ClassFile.of().parse(bytes);
    }

    private static JavaPackage createJavaPackage(final ClassDesc classSymbol) {
        val packageName = classSymbol.packageName();
        if (packageName.isEmpty()) {
            return JavaPackage.DEFAULT;
        }
        return new JavaPackage(packageName);
    }

    private static JavaType createJavaType(final ClassDesc classSymbol) {
        val id = createTypeIdentifier(classSymbol);
        return new JavaType(id);
    }

    private static TypeIdentifier createTypeIdentifier(final ClassDesc classSymbol) {
        val javaPackage = createJavaPackage(classSymbol);
        return new TypeIdentifier(javaPackage, classSymbol.displayName());
    }
}

