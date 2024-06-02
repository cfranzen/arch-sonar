package de.cfranzen.archsonar.components.java.detector.source;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePathScanner;
import lombok.val;

class MethodsVisitor extends TreePathScanner<Void, ParsingContext> {

    @Override
    public Void visitMethod(final MethodTree node, final ParsingContext context) {
        val parentPath = getCurrentPath().getParentPath();
        val parent = parentPath.getLeaf();
        if (parent instanceof ClassTree clazz) {
            val allMethods = clazz.getMembers().stream().filter(MethodTree.class::isInstance).toList();
            val index = allMethods.indexOf(node);
            if (index < 0) {
                throw new IllegalStateException("Could not find method in class");
            }
            TypeIdentifierHelper.createTypeIdentifier(context.javaPackage(), parentPath)
                    .flatMap(context::findType).ifPresent(type -> {
                        val method = type.addMethod(node.getName().toString(), index);
                        context.addProgrammingElement(method);
                    });
        }
        return super.visitMethod(node, context);
    }
}
