package de.dummy.project.records;

public record MyLocalNestingRecord() {

    void nestingMethod() {
        record MyLocalRecord(
                String str
        ) {
        }

        MyLocalRecord var = new MyLocalRecord("test");
    }
}
