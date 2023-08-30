package de.dummy.project.enums;

public enum MyLocalNestingEnum {
    VALUE1, VALUE2;

    public void nestingMethod() {
        enum MyLocalEnum {
            LOCAL1, LOCAL2
        }
        MyLocalEnum enumVar = MyLocalEnum.LOCAL1;
    }
}
