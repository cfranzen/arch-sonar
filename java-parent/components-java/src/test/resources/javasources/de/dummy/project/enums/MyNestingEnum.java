package de.dummy.project.enums;

public enum MyNestingEnum {
    A, B, C;

    public enum MyNestedEnum {
        NESTED1, NESTED2;

        public enum MyDoubleNestedEnum {
            DOUBLE_NESTED1
        }
    }
}
