package com.study.dataentryservice.repository.query.table;

import com.study.dataentryservice.model.table.AlterTableRecord;
import com.study.dataentryservice.repository.query.record.Query;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AlterTableQuery implements Query {
    private static final String ALTER_QUERY = "ALTER TABLE %s %s %s";
    private AlterTableRecord alterTableRecord;

     public String getQuery() {
        return String.format(ALTER_QUERY, alterTableRecord.getTableName(), alterTableRecord.getAlterStrategy(),
                alterTableRecord.getColumn().toString());
    }
}
