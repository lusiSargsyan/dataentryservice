package com.study.dataentryservice.model.table.column;

import com.study.dataentryservice.model.table.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ReferenceModel {
    @NotNull
    private final Table referenceTable;
    @NotNull
    private final String toReferenceColumn;
    @NotNull
    private final String fromReferenceColumn;
    @NotNull
    private final ModifyStrategy modifyStrategy;

    public String getReferenceQueryPart() {
        return " , FOREIGN KEY ( " +
                getFromReferenceColumn() +
                ") REFERENCES " +
                getReferenceTable().getName() +
                "(" +
                getToReferenceColumn() +
                ")" +
                " ON UPDATE " +
                modifyStrategy.getOnUpdate() +
                " ON DELETE " +
                modifyStrategy.getOnDelete();
    }
}
