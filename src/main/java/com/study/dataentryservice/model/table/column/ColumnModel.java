package com.study.dataentryservice.model.table.column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysql.cj.MysqlType;
import com.study.dataentryservice.model.table.column.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class ColumnModel {

    @NotNull(message = "Field name is required.")
    private String name;
    //TODO is required for add but not for alter
    private MysqlType type;
    private int size;
    @JsonProperty("isPrimaryKey")
    private boolean isPrimaryKey;

    @JsonProperty("isAutoIncrement")
    private boolean isAutoIncrement;

    private Validation validation;
    private Object defaultValue;

    public ColumnModel() {
    }

    @Override
    public String toString() {
        StringBuilder fieldStringBuilder = new StringBuilder("`" + getName() + "` ");
        if (Objects.nonNull(getType())) {
            fieldStringBuilder.append(" ").append(getType()).append(" ");
        }
        if (getSize() != 0) {
            fieldStringBuilder.append(" (").append(getSize()).append(") ");
        }
        if (isAutoIncrement()) {
            fieldStringBuilder.append(" AUTO_INCREMENT ");
        }
        if (isPrimaryKey()) {
            fieldStringBuilder.append(" PRIMARY KEY ");
        }
        if (Objects.nonNull(getValidation()) && Objects.nonNull(getValidation().getValidationStrategy())) {
            fieldStringBuilder.append(" NOT NULL ");
        }
        if (Objects.nonNull(getDefaultValue())) {
            fieldStringBuilder.append(" DEFAULT ").append(defaultValue);
        }
        return fieldStringBuilder.toString();
    }
}
