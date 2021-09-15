package com.study.dataentryservice.model.table;

import com.study.dataentryservice.model.table.column.ColumnModel;
import com.study.dataentryservice.repository.query.table.AlterStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class AlterTableRecord {
    @NotNull(message = "table name is required")
    private String tableName;

    @NotNull(message = "AlterStrategy is required")
    private AlterStrategy alterStrategy;

    @NotNull(message = "Column is required")
    private ColumnModel column;
    public AlterTableRecord() {}
}
