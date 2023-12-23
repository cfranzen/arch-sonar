package de.dummy.project.enums;

public enum MySimpleEnum {
    VALUE1,
    VALUE2,
    VALUE3;

    private MySimpleEnum myEnum;

    private String myString;

    private int myInt;

    public int getMyInt() {
        return myInt;
    }

    public MySimpleEnum getMyEnum() {
        return myEnum;
    }

    public String getMyString() {
        return myString;
    }
}
