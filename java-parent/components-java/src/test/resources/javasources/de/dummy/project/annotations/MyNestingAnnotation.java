package de.dummy.project.annotations;

import de.dummy.project.classes.MySimpleClass;
import de.dummy.project.enums.MySimpleEnum;

import java.time.Instant;

public @interface MyNestingAnnotation {

    MySimpleClass myClass = new MySimpleClass();

    MyNestedAnnotation getChild();

    @interface MyNestedAnnotation {

        MySimpleEnum myEnum = MySimpleEnum.VALUE1;

        MySimpleEnum getMyEnum();

        @interface MyDoubleNestedAnnotation {

            Instant instant = Instant.now();

            String getInstant();
        }
    }
}
