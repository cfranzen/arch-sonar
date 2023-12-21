package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.java.JavaBytecodeFile;
import de.cfranzen.archsonar.components.java.JavaPackage;
import de.cfranzen.archsonar.resources.Resource;
import de.cfranzen.archsonar.resources.ResourceUtils;
import io.github.dmlloyd.classfile.ClassFile;
import io.github.dmlloyd.classfile.ClassModel;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.IOException;
import java.util.Optional;

@Log4j2
class JavaBytecodeAnalyzer {

    Optional<JavaBytecodeFile> analyze(Resource resource) {
        try {
            val classModel = parse(resource);
            val javaPackage = createJavaPackageFromClassModel(classModel);
            val sourceFile = new JavaBytecodeFile(resource, javaPackage);
            // TODO: Process ClassModel
            return Optional.of(sourceFile);
        } catch (IOException e) {
            log.error("Unable to parse Java class file {}", resource, e);
            return Optional.empty();
        }
    }

    private ClassModel parse(final Resource resource) throws IOException {
        val bytes = ResourceUtils.readContent(resource);
        return ClassFile.of().parse(bytes);
    }

    private static JavaPackage createJavaPackageFromClassModel(final ClassModel classModel) {
        val packageName = classModel.thisClass().asSymbol().packageName();
        if (packageName.isEmpty()) {
            return JavaPackage.DEFAULT;
        }
        return new JavaPackage(packageName);
    }
}

