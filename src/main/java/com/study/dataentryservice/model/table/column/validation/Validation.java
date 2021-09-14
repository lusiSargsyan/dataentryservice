package com.study.dataentryservice.model.table.column.validation;

import com.study.dataentryservice.model.table.column.validation.validationStrategy.ValidateStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Validation {
    private String tableName;
    private String columnName;
    private final ValidateStrategy validationStrategy;
    private final String strategyLogic;
}
