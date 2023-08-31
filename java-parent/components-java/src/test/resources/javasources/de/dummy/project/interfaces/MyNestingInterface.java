package de.dummy.project.interfaces;

import de.dummy.project.enums.MySimpleEnum;

import java.time.Instant;

public interface MyNestingInterface {

    void setChild(final MyNestedInterface child);

    interface MyNestedInterface {
        MySimpleEnum getMyEnum();

        void setMyEnum(final MySimpleEnum myEnum);

        interface MyDoubleNestedInterface {

            Instant getInstant();

            void setInstant(final Instant instant);
        }
    }
}
