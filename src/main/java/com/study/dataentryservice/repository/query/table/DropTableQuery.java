package com.study.dataentryservice.repository.query.table;

import com.study.dataentryservice.repository.query.record.Query;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DropTableQuery implements Query {
    private static final String DROP_QUERY = "DROP TABLE %s";
    private String tableName;

    public String getQuery() {
        return String.format(DROP_QUERY, tableName);
    }
}
