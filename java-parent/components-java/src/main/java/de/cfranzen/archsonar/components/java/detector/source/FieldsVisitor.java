package de.cfranzen.archsonar.components.java.detector.source;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePathScanner;
import lombok.val;

import java.util.List;

import static com.sun.source.tree.Tree.Kind.ENUM;
import static javax.lang.model.element.Modifier.*;

class FieldsVisitor extends TreePathScanner<Void, ParsingContext> {

    @Override
    public Void visitVariable(final VariableTree node, final ParsingContext context) {
        val parentPath = getCurrentPath().getParentPath();
        val parent = parentPath.getLeaf();
        if (parent instanceof ClassTree clazz && !isEnumValue(node, clazz)) {
            TypeIdentifierHelper.createTypeIdentifier(context.javaPackage(), parentPath)
                    .flatMap(context::findType).ifPresent(type -> {
                        val field = type.addField(node.getName().toString());
                        context.addProgrammingElement(field);
                    });
        }
        return super.visitVariable(node, context);
    }

    private static boolean isEnumValue(final VariableTree node, final ClassTree parentClass) {
        return parentClass.getKind() == ENUM &&
                node.getModifiers().getFlags().containsAll(List.of(PUBLIC, STATIC, FINAL)) &&
                node.getType() instanceof IdentifierTree varTypeIdentifier &&
                varTypeIdentifier.getName().equals(parentClass.getSimpleName());
    }
}
