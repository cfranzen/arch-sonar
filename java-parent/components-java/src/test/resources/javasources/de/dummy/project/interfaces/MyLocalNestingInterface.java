package de.dummy.project.interfaces;

public interface MyLocalNestingInterface {

    default void nestingMethod() {
        interface MyLocalInterface {

            String create();
        }
    }
}
