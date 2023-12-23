package de.dummy.project.records;

import de.dummy.project.enums.MySimpleEnum;

public record MySimpleRecord(
        MySimpleEnum myEnum,
        String myString,
        int myInt
) {

    @Override
    public int myInt() {
        return myInt;
    }

    @Override
    public MySimpleEnum myEnum() {
        return myEnum;
    }

    @Override
    public String myString() {
        return myString;
    }
}
