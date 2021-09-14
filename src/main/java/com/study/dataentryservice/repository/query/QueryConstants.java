package com.study.dataentryservice.repository.query;

public enum QueryConstants {
    TABLE_NAME("tableName"),
    DATA("data"),
    FOREIGN_KEY_COLUMN("foreignKeyColumn");

    private final String constant;

    QueryConstants(String constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return  constant;
    }
}
