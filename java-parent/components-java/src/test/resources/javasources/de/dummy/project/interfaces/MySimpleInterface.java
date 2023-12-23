package de.dummy.project.interfaces;

import de.dummy.project.enums.MySimpleEnum;

public interface MySimpleInterface {

    MySimpleEnum myEnum = MySimpleEnum.VALUE1;

    String myString = "default";

    int myInt = 1;

    int getMyInt();

    MySimpleEnum getMyEnum();

    String getMyString();

    void setMyEnum(final MySimpleEnum myEnum);

    void setMyInt(final int myInt);

    void setMyString(final String myString);
}
