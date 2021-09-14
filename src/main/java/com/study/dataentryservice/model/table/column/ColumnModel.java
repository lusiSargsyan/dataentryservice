package com.study.dataentryservice.model.table.column;

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
    private final String name;
    //TODO is required for add but not for alter
    private final MysqlType type;
    private final int size;
    private final boolean isPrimaryKey;
    private final boolean isAutoIncrement;
    private final Validation validation;
    private final Object defaultValue;

    @Override
    public String toString() {
        StringBuilder fieldStringBuilder =  new StringBuilder("`" + getName() + "` ");
        if(Objects.nonNull(getType())) {
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
        if(Objects.nonNull(getDefaultValue())) {
            fieldStringBuilder.append(" DEFAULT ").append(defaultValue);
        }
        return fieldStringBuilder.toString();
    }
}
