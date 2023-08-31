package de.dummy.project.interfaces;

import de.dummy.project.enums.MySimpleEnum;

public interface MySimpleInterface {

    int getMyInt();

    MySimpleEnum getMyEnum();

    String getMyString();

    void setMyEnum(final MySimpleEnum myEnum);

    void setMyInt(final int myInt);

    void setMyString(final String myString);
}
