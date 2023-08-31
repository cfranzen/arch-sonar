package de.dummy.project.classes;

import de.dummy.project.enums.MySimpleEnum;

import java.time.Instant;

public class MyNestingClass {

    private MyNestedClass child;

    public MyNestedClass getChild() {
        return child;
    }

    public void setChild(final MyNestedClass child) {
        this.child = child;
    }

    static class MyNestedClass{

        private MySimpleEnum myEnum;

        public MySimpleEnum getMyEnum() {
            return myEnum;
        }

        public void setMyEnum(final MySimpleEnum myEnum) {
            this.myEnum = myEnum;
        }

        static class MyDoubleNestedClass{

            private Instant instant;

            public Instant getInstant() {
                return instant;
            }

            public void setInstant(final Instant instant) {
                this.instant = instant;
            }
        }
    }
}
