package de.dummy.project.classes;

public class MyLocalNestingClass {

    void nestingMethod() {
        class MyLocalClass {
            String str;
        }

        MyLocalClass var = new MyLocalClass();
    }
}
