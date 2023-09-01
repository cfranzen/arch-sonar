package de.dummy.project.annotations;

import de.dummy.project.enums.MySimpleEnum;

public @interface MySimpleAnnotation {

    int myInt = 123;

    int getMyInt() default 123;

    MySimpleEnum getMyEnum();

    String getMyString();
}