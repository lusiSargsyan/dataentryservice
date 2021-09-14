package com.study.dataentryservice.model.table.column;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyStrategy {
    private final ModifyStrategyType onDelete;
    private final ModifyStrategyType onUpdate;

    private enum ModifyStrategyType {
        CASCADE, SET_NULL, NO_ACTION, RESTRICT, SET_DEFAULT;
    }
}
