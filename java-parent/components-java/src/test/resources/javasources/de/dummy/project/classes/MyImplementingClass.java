package de.dummy.project.classes;

import de.dummy.project.enums.MySimpleEnum;
import de.dummy.project.interfaces.MyNestingInterface;
import de.dummy.project.interfaces.MySimpleInterface;

public class MyImplementingClass implements MySimpleInterface, MyNestingInterface {

    @Override
    public void setChild(final MyNestedInterface child) {

    }

    @Override
    public int getMyInt() {
        return 0;
    }

    @Override
    public MySimpleEnum getMyEnum() {
        return null;
    }

    @Override
    public String getMyString() {
        return null;
    }

    @Override
    public void setMyEnum(final MySimpleEnum myEnum) {

    }

    @Override
    public void setMyInt(final int myInt) {

    }

    @Override
    public void setMyString(final String myString) {

    }
}
