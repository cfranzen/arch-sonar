package de.cfranzen.archsonar.components.java.detector.source;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.util.TreePathScanner;
import de.cfranzen.archsonar.components.RelationType;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.components.java.TypeReference;
import de.cfranzen.archsonar.components.java.TypeRelation;
import lombok.val;

class TypesVisitor extends TreePathScanner<Void, ParsingContext> {

    @Override
    public Void visitClass(final ClassTree node, final ParsingContext context) {
        TypeIdentifierHelper.createTypeIdentifier(context.javaPackage(), getCurrentPath())
                .ifPresent(id -> {
                    val javaType = new JavaType(id);
                    context.addProgrammingElement(javaType);

                    val extendsClause = node.getExtendsClause();
                    if (extendsClause instanceof IdentifierTree extendsId) {
                        TypeReference reference = context.resolveTypeReference(extendsId.getName().toString());
                        context.addElementRelation(
                                new TypeRelation(id, reference, RelationType.INHERITS)
                        );
                    }

                    val implementsClauses = node.getImplementsClause();
                    for (val clause : implementsClauses) {
                        if (clause instanceof IdentifierTree implementsId) {
                            TypeReference reference = context.resolveTypeReference(implementsId.getName().toString());
                            context.addElementRelation(
                                    new TypeRelation(id, reference, RelationType.INHERITS)
                            );
                        }
                    }

                    val permitsClauses = node.getPermitsClause();
                    for (val clause : permitsClauses) {
                        if (clause instanceof IdentifierTree permitsId) {
                            TypeReference reference = context.resolveTypeReference(permitsId.getName().toString());
                            context.addElementRelation(
                                    new TypeRelation(id, reference, RelationType.EXPOSES)
                            );
                        }
                    }
                });
        return super.visitClass(node, context);
    }
}
