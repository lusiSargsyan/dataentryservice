package com.study.dataentryservice.model.record;

import com.mysql.cj.MysqlType;
import com.study.dataentryservice.exception.ValidationException;
import com.study.dataentryservice.model.table.column.validation.Validation;
import lombok.Getter;

@Getter
public class DataItem {
    private final String key;
    private final Object value;
    private final MysqlType type;

    public DataItem(String key, Object value, MysqlType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

   public void validate(Validation validation) throws ValidationException {
      if (!validation.getValidationStrategy().validateBy(value, validation.getStrategyLogic())) {
          throw new ValidationException("Invalid value for key: " + key);
      }
   }
}
