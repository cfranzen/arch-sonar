package de.dummy.project.records;

import de.dummy.project.enums.MySimpleEnum;

import java.time.Instant;

public record MyNestingRecord(
        MyNestedRecord child
) {
    record MyNestedRecord(
            MySimpleEnum myEnum
    ) {
        record MyDoubleNestedRecord(
                Instant instant
        ) {
        }
    }
}
