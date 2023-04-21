package de.cfranzen.archsonar.components.java;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaPackageTest {

    @Test
    void testParsingPackageToSegments() {
        val jp = new JavaPackage("de.cfranzen.archsonar.components.java");
        assertThat(jp.name()).isEqualTo("de.cfranzen.archsonar.components.java");
        assertThat(jp.nameSegments()).containsExactly("de", "cfranzen", "archsonar", "components", "java");
    }
}