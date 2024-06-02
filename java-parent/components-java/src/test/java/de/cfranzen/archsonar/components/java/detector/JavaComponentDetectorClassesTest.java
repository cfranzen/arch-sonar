package de.cfranzen.archsonar.components.java.detector;

import de.cfranzen.archsonar.components.RelationType;
import de.cfranzen.archsonar.components.java.JavaField;
import de.cfranzen.archsonar.components.java.JavaMethod;
import de.cfranzen.archsonar.components.java.JavaType;
import de.cfranzen.archsonar.resources.test.MockResourcesCollection;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromClassfile;
import static de.cfranzen.archsonar.components.java.detector.ResourceUtils.createResourceFromSource;
import static de.cfranzen.archsonar.components.java.detector.TypeUtils.identifier;
import static de.cfranzen.archsonar.components.java.detector.TypeUtils.reference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JavaComponentDetectorClassesTest {

    private final JavaComponentDetector sut = new JavaComponentDetectorFactory().create();

    @Nested
    class OperateOnSourceFiles {

        @Test
        void detectClass() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MySimpleClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.classes.MySimpleClass"));
        }

        @Test
        void detectNestedClass() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MyNestingClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types)
                    .hasSize(3)
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.classes.MyNestingClass"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.classes.MyNestingClass.MyNestedClass"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.classes.MyNestingClass.MyNestedClass.MyDoubleNestedClass"));
        }

        @Test
        void ignoreLocalClass() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MyLocalNestingClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.classes.MyLocalNestingClass"));
        }

        @Test
        void findFieldsOfClass() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MySimpleClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val fields = components.programmingElements(JavaField.class);
            assertThat(fields)
                    .hasSize(3)
                    .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myEnum"))
                    .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myString"))
                    .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myInt"));

        }

        @Test
        void findMethodsOfClass() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MySimpleClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val methods = components.programmingElements(JavaMethod.class);
            assertThat(methods)
                    .hasSize(6)
                    .anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(0)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(1)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyString"),
                                    () -> assertThat(method.id().index()).isEqualTo(2)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("setMyEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(3)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("setMyInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(4)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("setMyString"),
                                    () -> assertThat(method.id().index()).isEqualTo(5)
                            )
                    );
        }

        @Test
        void detectExtendsRelation() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MyExtendingClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val relations = components.relations();
            assertThat(relations)
                    .hasSize(1)
                    .anySatisfy(rel -> assertAll(
                                    () -> assertThat(rel.from()).isEqualTo(identifier("de.dummy.project.classes", "MyExtendingClass")),
                                    () -> assertThat(rel.to()).isEqualTo(reference("de.dummy.project.classes", "MySimpleClass")),
                                    () -> assertThat(rel.type()).isEqualTo(RelationType.INHERITS)
                            )
                    );
        }

        @Test
        void detectImplementsRelation() {
            // Given
            val resource = createResourceFromSource("de.dummy.project.classes.MyImplementingClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val relations = components.relations();
            assertThat(relations)
                    .hasSize(2)
                    .anySatisfy(rel -> assertAll(
                                    () -> assertThat(rel.from()).isEqualTo(identifier("de.dummy.project.classes", "MyImplementingClass")),
                                    () -> assertThat(rel.to()).isEqualTo(reference("de.dummy.project.interfaces", "MySimpleInterface")),
                                    () -> assertThat(rel.type()).isEqualTo(RelationType.INHERITS)
                            )
                    ).anySatisfy(rel -> assertAll(
                                    () -> assertThat(rel.from()).isEqualTo(identifier("de.dummy.project.classes", "MyImplementingClass")),
                                    () -> assertThat(rel.to()).isEqualTo(reference("de.dummy.project.interfaces", "MyNestingInterface")),
                                    () -> assertThat(rel.type()).isEqualTo(RelationType.INHERITS)
                            )
                    );
        }
    }

    @Nested
    class OperateOnClassFiles {

        @Test
        void detectClass() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.classes.MySimpleClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.classes.MySimpleClass"));
        }

        @Test
        void detectNestedClass() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.classes.MyNestingClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types)
                    .hasSize(3)
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.classes.MyNestingClass"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.classes.MyNestingClass.MyNestedClass"))
                    .anySatisfy(type ->
                            assertThat(type.id().fullyQualifiedName())
                                    .isEqualTo("de.dummy.project.classes.MyNestingClass.MyNestedClass.MyDoubleNestedClass"));
        }

        @Test
        void ignoreLocalClass() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.classes.MyLocalNestingClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val types = components.programmingElements(JavaType.class);
            assertThat(types).hasSize(1).allSatisfy(type ->
                    assertThat(type.id().fullyQualifiedName())
                            .isEqualTo("de.dummy.project.classes.MyLocalNestingClass"));
        }

        @Test
        void findFieldsOfClass() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.classes.MySimpleClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val fields = components.programmingElements(JavaField.class);
            assertThat(fields)
                    .hasSize(3)
                    .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myEnum"))
                    .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myString"))
                    .anySatisfy(field -> assertThat(field.id().name()).isEqualTo("myInt"));

        }

        @Test
        void findMethodsOfClass() {
            // Given
            val resource = createResourceFromClassfile("de.dummy.project.classes.MySimpleClass");

            // When
            val components = sut.detect(MockResourcesCollection.of(resource));

            // Then
            val methods = components.programmingElements(JavaMethod.class);
            assertThat(methods)
                    .hasSize(7)
                    .anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("<init>"),
                                    () -> assertThat(method.id().index()).isEqualTo(0)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(1)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(2)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("getMyString"),
                                    () -> assertThat(method.id().index()).isEqualTo(3)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("setMyEnum"),
                                    () -> assertThat(method.id().index()).isEqualTo(4)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("setMyInt"),
                                    () -> assertThat(method.id().index()).isEqualTo(5)
                            )
                    ).anySatisfy(method -> assertAll(
                                    () -> assertThat(method.id().name()).isEqualTo("setMyString"),
                                    () -> assertThat(method.id().index()).isEqualTo(6)
                            )
                    );
        }
    }


}