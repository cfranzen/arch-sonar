package de.dummy.project.records;

import de.dummy.project.enums.MySimpleEnum;

public record MySimpleRecord(
        MySimpleEnum myEnum,
        String myString,
        int myInt
) {
}
