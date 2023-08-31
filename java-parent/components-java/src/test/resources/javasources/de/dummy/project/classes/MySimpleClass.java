package de.dummy.project.classes;

import de.dummy.project.enums.MySimpleEnum;

public class MySimpleClass {

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

    public void setMyEnum(final MySimpleEnum myEnum) {
        this.myEnum = myEnum;
    }

    public void setMyInt(final int myInt) {
        this.myInt = myInt;
    }

    public void setMyString(final String myString) {
        this.myString = myString;
    }
}
