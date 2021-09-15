package com.study.dataentryservice.model.table.column;

import com.study.dataentryservice.model.table.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class ReferenceModel {
    @NotNull
    private Table referenceTable;
    @NotNull
    private String toReferenceColumn;
    @NotNull
    private String fromReferenceColumn;
    @NotNull
    private ModifyStrategy modifyStrategy;
    public ReferenceModel() {}

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
